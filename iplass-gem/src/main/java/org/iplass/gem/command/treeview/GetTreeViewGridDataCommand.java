/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 *
 * Unless you have purchased a commercial license,
 * the following license terms apply:
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.iplass.gem.command.treeview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.iplass.gem.command.Constants;
import org.iplass.gem.command.common.JsonStreamingOutput;
import org.iplass.gem.command.common.TreeGridData;
import org.iplass.gem.command.generic.search.SearchViewCommand;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.template.Template;
import org.iplass.mtp.command.annotation.webapi.RestJson;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.SortSpec;
import org.iplass.mtp.entity.query.SortSpec.SortType;
import org.iplass.mtp.entity.query.SubQuery;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.entity.query.condition.predicate.In;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.primary.EntityField;
import org.iplass.mtp.entity.query.value.primary.Literal;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.treeview.ReferenceTreeViewItem;
import org.iplass.mtp.view.treeview.TreeView;
import org.iplass.mtp.view.treeview.TreeViewGridColModel;
import org.iplass.mtp.view.treeview.TreeViewGridColModelMapping;
import org.iplass.mtp.view.treeview.TreeViewItem;
import org.iplass.mtp.view.treeview.TreeViewItem.TreeSortType;
import org.iplass.mtp.view.treeview.TreeViewManager;
import org.iplass.mtp.web.actionmapping.permission.ActionParameter;
import org.iplass.mtp.web.actionmapping.permission.ActionPermission;
import org.iplass.mtp.web.template.TemplateUtil;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.RequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebApi(
		name=GetTreeViewGridDataCommand.WEBAPI_NAME,
		displayName="ツリービューグリッド取得",
		accepts=RequestType.REST_FORM,
		methods=MethodType.POST,
		restJson=@RestJson(parameterName="param"),
		results={"data"},
		checkXRequestedWithHeader=true
	)
@Template(name="gem/treeview/treeViewWidget", displayName="ツリービューウィジェット", path="/jsp/gem/treeview/treeViewList.jsp")
@CommandClass(name="gem/treeview/GetTreeViewGridDataCommand", displayName="ツリービューグリッドデータ取得")
public final class GetTreeViewGridDataCommand implements Command {

	private static Logger logger = LoggerFactory.getLogger(GetTreeViewGridDataCommand.class);

	public static final String WEBAPI_NAME = "gem/treeview/getTreeViewGridData";

	private EntityManager em;

	/** EntityDefinitionManager */
	private EntityDefinitionManager edm = null;

	/** TreeViewManager */
	private TreeViewManager tvm = null;

	public GetTreeViewGridDataCommand() {
		em = ManagerLocator.getInstance().getManager(EntityManager.class);
		edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
		tvm = ManagerLocator.getInstance().getManager(TreeViewManager.class);
	}

	@Override
	public String execute(RequestContext request) {
		String defName = request.getParam(Constants.DEF_NAME);
		String nodeid = request.getParam("nodeid");
		String _level = request.getParam("n_level");
		int level = 0;
		if (_level != null) {
			try {
				level = Integer.parseInt(_level) + 1;
			} catch (NumberFormatException e) {
				if (logger.isDebugEnabled()) {
					logger.debug(e.getMessage(), e);
				}
			}
		}

		NodeInfo nodeInfo = new NodeInfo(nodeid, level);
		TreeView treeView = tvm.get(defName);
		List<TreeNodeData> nodes = null;

		if (nodeInfo.isInit()) {
			//初期ロード
			nodes = initTreeView(treeView, nodeInfo);
		} else {
			if (nodeInfo.isEntityDefinition()) {
				//Entity定義階層
				TreeViewItem item = treeView.getItem(nodeInfo.getCurrentPath());
				nodes = underEntityDefinition(item, nodeInfo, treeView.getColModel());
			} else if (nodeInfo.isIndex()) {
				//Index階層
				TreeViewItem item = treeView.getItem(nodeInfo.getCurrentPath());
				nodes = underIndex(item, nodeInfo, treeView.getColModel());
			} else if (nodeInfo.isEntity()) {
				//Entity階層
				TreeViewItem item = treeView.getItem(nodeInfo.getCurrentPath());
				nodes = underEntity(item, nodeInfo, treeView.getColModel());
			}
		}

		request.setAttribute("data", new JsonStreamingOutput(new TreeGridData<TreeNodeData>(nodes)));
		return null;
	}

	/**
	 * TreeViewを初期化します。
	 *
	 *<pre>
	 * Entityの一覧と件数確認、件数が指定以下ならEntityのリストを返す
	 *
	 * 例)表示件数10件の場合の戻り値
	 * Entity1(25件:定義階層)
	 * ├01～10(階層)
	 * ｜├Entity(01:Entityデータ)
	 * ｜├・・・
	 * ｜└Entity(10:Entityデータ)
	 * ├11～20(階層)
	 * ｜├Entity(11:Entityデータ)
	 * ｜├・・・
	 * ｜└Entity(20:Entityデータ)
	 * └21～30(階層)
	 *   ├Entity(21:Entityデータ)
	 *   ├・・・
	 *   └Entity(25:Entityデータ)
	 * Entity2(10件:定義階層)
	 * ├Entity(1:Entityデータ)
	 * ├Entity(2:Entityデータ)
	 * ├・・・
	 * └Entity(10:Entityデータ)
	 * Entity3(0件:定義階層)
	 *</pre>
	 *
	 * @param treeName ツリーメニュー定義
	 * @return ツリーのノード
	 */
	private List<TreeNodeData> initTreeView(TreeView treeView, NodeInfo nodeInfo) {
		AuthContext authContext = AuthContext.getCurrentContext();
		List<TreeNodeData> nodes = new ArrayList<TreeNodeData>();
		for (final TreeViewItem item : treeView.getItems()) {
			if (authContext.checkPermission(new ActionPermission(SearchViewCommand.SEARCH_ACTION_NAME,
					new ActionParameter() {
						@Override
						public Object getValue(String name) {
							if (Constants.DEF_NAME.equals(name)) {
								return item.getDefName();
							} else if (Constants.VIEW_NAME.equals(name)) {
								return item.getViewName();
							}
							return null;
						}}))) {
				if (item.isDisplayDefinitionNode()) {
					TreeNodeData node = createEntityDefinitionNode(item, nodeInfo, treeView.getColModel());
					if (node != null) nodes.add(node);
				} else {
					List<TreeNodeData> _nodes = underEntityDefinition(item, nodeInfo, treeView.getColModel());
					if (_nodes != null && !_nodes.isEmpty()) nodes.addAll(_nodes);
				}
			}
		}
		return nodes;
	}

	/**
	 * Entity定義名を持つノードを生成します。
	 * 配下には表示上限を超える場合は上限ごとの階層ノードとその配下にEntityノードを、
	 * 超えない場合にはEntityノードを持ちます。
	 *
	 * @param item ツリーメニューのアイテム
	 * @param oid EntityのOID
	 * @param currentPath ノードのパス
	 * @return Entity定義のノード
	 */
	private List<TreeNodeData> underEntityDefinition(TreeViewItem item, NodeInfo nodeInfo, List<TreeViewGridColModel> colModels) {
		List<TreeNodeData> nodes = new ArrayList<TreeNodeData>();
		int totalCount = count(item, nodeInfo.getOid());
		if (totalCount == 0) return nodes;

		if (totalCount > item.getLimit()) {
			// total/limit+1個分の階層作成
			int index = totalCount % item.getLimit() == 0 ?
					totalCount / item.getLimit() : totalCount / item.getLimit() + 1;

			String path = null;
			if (nodeInfo.isRoot()) {
				path = item.getDefName();
			} else {
				path = nodeInfo.getCurrentPath();
			}

			for (int i = 0; i < index; i++) {
				int _offset = item.getLimit() * i;
				IndexNode iNode = new IndexNode(path);
				iNode.setDisplayName((_offset + 1) + "～" + (_offset + item.getLimit()) + "件");
				iNode.setIcon(item.getIndexNodeIcon());
				iNode.setCssStyle(item.getIndexNodeCssStyle());
				iNode.setOffset(_offset);
				iNode.setOid(nodeInfo.getOid());

				TreeNodeData data = new TreeNodeData(iNode, nodeInfo.getNodeid(), nodeInfo.getLevel(), nodeInfo.getOid(), _offset, new Object[colModels.size()]);
				nodes.add(data);
			}
		} else {
			nodes.addAll(createEntityNode(item, nodeInfo, colModels));
		}
		return nodes;
	}

	private List<TreeNodeData> underIndex(TreeViewItem item, NodeInfo nodeInfo, List<TreeViewGridColModel> colModels) {
		List<TreeNodeData> nodes = createEntityNode(item, nodeInfo, colModels);
		return nodes;
	}

	/**
	 * Entityの参照するデータを取得します。
	 *
	 * <pre>
	 * 例1)Entity(参照が表示件数以下)の配下構造の戻り値の構造
	 * Entity1(定義階層)
	 * ├Entity(11:Entityデータ)
	 * └Entity(12:Entityデータ)
	 *
	 * 例2)Entity(参照が表示件数以上)の配下構造の戻り値の構造
	 * Entity2(定義階層)
	 * ├01～10(階層)
	 * ｜├Entity(01:Entityデータ)
	 * ｜├・・・
	 * ｜└Entity(10:Entityデータ)
	 * └11～20(階層)
	 *   ├Entity(11:Entityデータ)
	 *   └Entity(12:Entityデータ)
	 * </pre>
	 *
	 * @param item ツリーメニューのアイテム
	 * @param oid EntityのOID
	 * @param currentPath ノードのパス
	 * @return 下位のノード
	 */
	private List<TreeNodeData> underEntity(TreeViewItem item, NodeInfo nodeInfo, List<TreeViewGridColModel> colModels) {
		List<TreeNodeData> nodes = new ArrayList<TreeNodeData>();
		for (ReferenceTreeViewItem rItem : item.getReferenceTreeViewItems()) {
			if (rItem.isDisplayDefinitionNode()) {
				TreeNodeData rNode = createEntityDefinitionNode(rItem, nodeInfo, colModels);
				if (rNode != null) nodes.add(rNode);
			} else {
				NodeInfo newNodeInfo = new NodeInfo(nodeInfo.getNodeid(), nodeInfo.getLevel());
				newNodeInfo.currentPath += "/" + rItem.getPropertyName();
				List<TreeNodeData> _nodes = underEntityDefinition(rItem, newNodeInfo, colModels);
				if (_nodes != null && !_nodes.isEmpty()) nodes.addAll(_nodes);
			}
		}
		return nodes;
	}

	/**
	 * Entity定義名を持つノードを生成します。
	 * 配下には表示上限を超える場合は上限ごとの階層ノードとその配下にEntityノードを、
	 * 超えない場合にはEntityノードを持ちます。
	 *
	 * @param item ツリーメニューのアイテム
	 * @param oid EntityのOID
	 * @param path ノードのパス
	 * @return Entity定義のノード
	 */
	private TreeNodeData createEntityDefinitionNode(TreeViewItem item, NodeInfo nodeInfo, List<TreeViewGridColModel> colModels) {
		int totalCount = count(item, nodeInfo.getOid());
		if (totalCount == 0) return null;

		EntityDefinitionNode edNode = null;
		if (item instanceof ReferenceTreeViewItem) {
			ReferenceTreeViewItem rItem = (ReferenceTreeViewItem) item;
			edNode = new EntityDefinitionNode(nodeInfo.getCurrentPath(), rItem.getPropertyName());
			edNode.setDisplayName(rItem.getDisplayName());
		} else {
			edNode = new EntityDefinitionNode(nodeInfo.getCurrentPath(), item.getDefName());

			//表示名
			String displayName = TemplateUtil.getMultilingualString(edm.get(item.getDefName()).getDisplayName(), edm.get(item.getDefName()).getLocalizedDisplayNameList());

			edNode.setDisplayName(displayName);
		}
		edNode.setIcon(item.getEntityDefinitionNodeIcon());
		edNode.setCssStyle(item.getEntityDefinitionNodeCssStyle());
		TreeNodeData node = new TreeNodeData(edNode, nodeInfo.getNodeid(), nodeInfo.getLevel(), nodeInfo.getOid(), new Object[colModels.size()]);
		return node;
	}

	/**
	 * Entityを検索してEntityノードを作成します。
	 * @param item ツリーメニューのアイテム
	 * @param offset 検索位置
	 * @param oid EntityのOID
	 * @param currentPath ノードのパス
	 * @return Entityのノード
	 */
	private List<TreeNodeData> createEntityNode(TreeViewItem item, NodeInfo nodeInfo, List<TreeViewGridColModel> colModels) {
		List<TreeNodeData> nodes = new ArrayList<TreeNodeData>();

		String path = null;
		if (nodeInfo.isRoot()) {
			path = item.getDefName();
		} else {
			path = nodeInfo.getCurrentPath();
		}

		List<Object[]> entites = search(item, nodeInfo.getOffset(), nodeInfo.getOid(), colModels);
		for (Object[] values : entites) {
			EntityNode en = null;
			if (item instanceof ReferenceTreeViewItem) {
				en = new EntityNode(path, ((ReferenceTreeViewItem) item).getPropertyName());
			} else {
				en = new EntityNode(path, item.getDefName());
			}
			en.setOid(values[0].toString());
			en.setDisplayName(values[1].toString());
			en.setDefName(item.getDefName());
			en.setAction(item.getAction());
			en.setViewName(item.getViewName());
			en.setIcon(item.getEntityNodeIcon());
			en.setCssStyle(item.getEntityNodeCssStyle());
			en.setHasReference(!item.getReferenceTreeViewItems().isEmpty());

			//oidと表示ラベル以外を詰め直し
			Object[] propValues = null;
			if (values.length > 2) {
				propValues = Arrays.copyOfRange(values, 2, values.length);
			} else {
				propValues = new Object[]{};
			}
			TreeNodeData data = new TreeNodeData(en, nodeInfo.getNodeid(), nodeInfo.getLevel(), propValues);
			nodes.add(data);
		}
		return nodes;
	}

	/**
	 * 件数を取得取得します。
	 * @param item ツリーメニューのアイテム
	 * @param oid EntityのOID
	 * @return 件数
	 */
	private int count(TreeViewItem item, String oid) {
		Query cond = new Query();
		cond.select(Entity.OID);
		cond.select().setDistinct(true);
		cond.from(item.getDefName());
		if (oid != null && item instanceof ReferenceTreeViewItem) {
			cond.where(getCondition((ReferenceTreeViewItem) item, oid));
		}
		return em.count(cond);
	}

	/**
	 * Entityを検索します
	 *
	 * @param item ツリーメニューのアイテム
	 * @param offset 検索位置
	 * @param oid EntityのOID
	 * @return Entity
	 */
	private List<Object[]> search(TreeViewItem item, int offset, String oid, List<TreeViewGridColModel> colModels) {
		Query query = new Query();
		List<ValueExpression> select = new ArrayList<ValueExpression>();
		select.add(new EntityField(Entity.OID));
		if (existProperty(item)) {
			if (isReferenceProperty(item)) {
				//参照の場合はname表示
				select.add(new EntityField(item.getDisplayPropertyName() + "." + Entity.NAME));
			} else {
				select.add(new EntityField(item.getDisplayPropertyName()));
			}
		} else {
			select.add(new EntityField(Entity.NAME));
		}
		for (TreeViewGridColModel colModel : colModels) {
			TreeViewGridColModelMapping mapping = null;
			for (TreeViewGridColModelMapping tmp : item.getMapping()) {
				if (colModel.getName().equals(tmp.getName())) {
					mapping = tmp;
				}
			}
			if (mapping != null) {
				if (!existProperty(item, mapping.getMappingName())) {
					select.add(new Literal(""));//マッピングされてないところは空文字で埋めておく
				} else {
					if (isReferenceProperty(item, mapping.getMappingName())) {
						//参照の場合はname表示
						select.add(new EntityField(mapping.getMappingName() + "." + Entity.NAME));
					} else {
						select.add(new EntityField(mapping.getMappingName()));
					}
				}
			} else {
				select.add(new Literal(""));//マッピングされてないところは空文字で埋めておく
			}
		}

		query.select(select.toArray());
		query.from(item.getDefName());
		if (oid != null && item instanceof ReferenceTreeViewItem) {
			query.where(getCondition((ReferenceTreeViewItem) item, oid));
		}
		query.order(getSortSpec(item));
		if (offset > -1) {
			query.limit(item.getLimit(), offset);
		} else {
			query.limit(item.getLimit());
		}

		List<Object[]> list = em.search(query).getList();
		return list;
	}

	/**
	 * プロパティが定義に存在するかチェックします。
	 * @param item ツリーメニューのアイテム
	 * @return プロパティが定義に存在するか
	 */
	private boolean existProperty(TreeViewItem item) {
		if (item == null) return false;
		if (item.getDisplayPropertyName() == null) return false;
		EntityDefinition ed = edm.get(item.getDefName());
		if (ed.getProperty(item.getDisplayPropertyName()) == null) return false;
		return true;
	}

	/**
	 * プロパティが参照かチェックします。
	 * @param item ツリーメニューのアイテム
	 * @return プロパティが定義に存在するか
	 */
	private boolean isReferenceProperty(TreeViewItem item) {
		if (item == null) return false;
		if (item.getDisplayPropertyName() == null) return false;
		EntityDefinition ed = edm.get(item.getDefName());
		PropertyDefinition pd = ed.getProperty(item.getDisplayPropertyName());
		if (pd == null) return false;
		return pd instanceof ReferenceProperty;
	}

	/***
	 * 指定のプロパティが存在するかチェックします。
	 * @param item
	 * @param propName
	 * @return
	 */
	private boolean existProperty(TreeViewItem item, String propName) {
		if (item == null || propName == null) return false;
		EntityDefinition ed = edm.get(item.getDefName());
		PropertyDefinition pd = getPropertyDefinition(ed, propName);
		if (pd == null) return false;
		return true;
	}

	/**
	 * プロパティが参照かチェックします。
	 * @param item
	 * @param propName
	 * @return
	 */
	private boolean isReferenceProperty(TreeViewItem item, String propName) {
		if (item == null || propName == null) return false;
		EntityDefinition ed = edm.get(item.getDefName());
		PropertyDefinition pd = getPropertyDefinition(ed, propName);
		if (pd == null) return false;
		return pd instanceof ReferenceProperty;
	}

	/**
	 * プロパティ定義を取得します。
	 * @param definition
	 * @param propName
	 * @return
	 */
	private PropertyDefinition getPropertyDefinition(EntityDefinition definition, String propName) {
		int firstDotIndex = propName.indexOf('.');
		if (firstDotIndex > 0) {
			String topPropName = propName.substring(0, firstDotIndex);
			String subPropName = propName.substring(firstDotIndex + 1);
			PropertyDefinition topProperty = definition.getProperty(topPropName);
			if (topProperty instanceof ReferenceProperty) {
				EntityDefinition red = edm.get(((ReferenceProperty) topProperty).getObjectDefinitionName());
				if (red != null) {
					return getPropertyDefinition(red, subPropName);
				}
			}
		} else {
			return definition.getProperty(propName);
		}
		return null;
	}

	/**
	 * 参照先検索時の条件を取得します。
	 * @param item 参照ツリーメニューのアイテム
	 * @param oid EntityのOID
	 * @return 検索条件
	 */
	private Condition getCondition(ReferenceTreeViewItem item, String oid) {
		SubQuery sq = new SubQuery(new Query().select(
				item.getPropertyName() + "." + Entity.OID).from(
				item.getParent().getDefName()).where(
				new Equals(Entity.OID, oid)));
		return new In(Entity.OID, sq);
	}

	/**
	 * ソート条件を取得します。
	 * @param item ツリーメニューのアイテム
	 * @return ソート条件
	 */
	private SortSpec getSortSpec(TreeViewItem item) {
		String sortKey = item.getSortItem() != null ? item.getSortItem() : Entity.OID;
		SortType type = SortType.ASC;
		if (item.getSortType() != null && item.getSortType().equals(TreeSortType.DESC)) {
			type = SortType.DESC;
		}
		return new SortSpec(sortKey, type);
	}

	/**
	 * TreeGridのノード情報
	 * @author lis3wg
	 */
	private class NodeInfo {

		/** ノードのID(type/oid/[offset/]currentPath) */
		private String nodeid;

		/** ノードの階層の深さ */
		private int level;

		/** ノードの種類 */
		private String type;

		/** OID */
		private String oid;

		/** Index階層のオフセット */
		private int offset = -1;

		/** ノードのパス */
		private String currentPath;

		/**
		 * コンストラクタ
		 * @param nodeid
		 * @param level
		 */
		public NodeInfo(String nodeid, int level) {
			this.nodeid = nodeid;
			this.level = level;
			if (nodeid != null) {
				String[] type_nodeid = nodeid.split("/", 2);
				type = type_nodeid[0];
				if ("D".equals(type)) {
					//Entity定義階層
					String[] oid_nodeid = type_nodeid[1].split("/", 2);
					oid = oid_nodeid[0];
					currentPath = oid_nodeid[1];
				} else if ("I".equals(type)) {
					//Index階層
					String[] oid_nodeid = type_nodeid[1].split("/", 2);
					oid = oid_nodeid[0];
					String[] offset_nodeid = oid_nodeid[1].split("/", 2);
					try {
						offset = Integer.parseInt(offset_nodeid[0]);
					} catch (NumberFormatException e) {
						if (logger.isDebugEnabled()) {
							logger.debug(e.getMessage(), e);
						}
					}
					currentPath = offset_nodeid[1];
				} else if ("E".equals(type)) {
					//Entity階層
					String[] oid_nodeid = type_nodeid[1].split("/", 2);
					oid = oid_nodeid[0];
					currentPath = oid_nodeid[1];
				}
			} else {
				oid = "root";
			}
		}

		/**
		 * @return nodeid
		 */
		public String getNodeid() {
			return nodeid;
		}

		/**
		 * @return level
		 */
		public int getLevel() {
			return level;
		}

		/**
		 * @return oid
		 */
		public String getOid() {
			if (isRoot()) return null;
			return oid;
		}

		/**
		 * @return offset
		 */
		public int getOffset() {
			return offset;
		}

		/**
		 * @return currentPath
		 */
		public String getCurrentPath() {
			return currentPath;
		}

		/**
		 * @return
		 */
		public boolean isRoot() {
			return "root".equals(oid);
		}

		/**
		 * @return
		 */
		public boolean isInit() {
			return StringUtil.isBlank(nodeid);
		}

		/**
		 * @return
		 */
		public boolean isEntityDefinition() {
			return "D".equals(type);
		}

		/**
		 * @return
		 */
		public boolean isIndex() {
			return "I".equals(type);
		}

		/**
		 * @return
		 */
		public boolean isEntity() {
			return "E".equals(type);
		}
	}

}
