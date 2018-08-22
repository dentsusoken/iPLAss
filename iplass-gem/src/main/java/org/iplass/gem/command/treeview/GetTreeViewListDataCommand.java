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
import java.util.List;

import org.iplass.gem.command.Constants;
import org.iplass.gem.command.common.JsonStreamingOutput;
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
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.SortSpec;
import org.iplass.mtp.entity.query.SortSpec.SortType;
import org.iplass.mtp.entity.query.SubQuery;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.entity.query.condition.predicate.In;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.treeview.ReferenceTreeViewItem;
import org.iplass.mtp.view.treeview.TreeView;
import org.iplass.mtp.view.treeview.TreeViewItem;
import org.iplass.mtp.view.treeview.TreeViewItem.TreeSortType;
import org.iplass.mtp.view.treeview.TreeViewManager;
import org.iplass.mtp.web.actionmapping.permission.ActionParameter;
import org.iplass.mtp.web.actionmapping.permission.ActionPermission;
import org.iplass.mtp.web.template.TemplateUtil;
import org.iplass.mtp.webapi.definition.RequestType;
import org.iplass.mtp.webapi.definition.MethodType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebApi(
		name=GetTreeViewListDataCommand.WEBAPI_NAME,
		accepts=RequestType.REST_FORM,
		methods=MethodType.POST,
		restJson=@RestJson(parameterName="param"),
		results={"data"},
		checkXRequestedWithHeader=true
	)
@Template(name="gem/treeview/treeViewParts", displayName="ツリービューパーツ", path="/jsp/gem/treeview/treeViewParts.jsp")
@CommandClass(name="gem/treeview/GetTreeViewListDataCommand", displayName="ツリービューリストデータ取得")
public final class GetTreeViewListDataCommand implements Command {

	private static Logger logger = LoggerFactory.getLogger(GetTreeViewListDataCommand.class);

	public static final String WEBAPI_NAME = "gem/treeview/getTreeViewListData";

	/** EntityManager */
	private EntityManager em = null;

	/** EntityDefinitionManager */
	private EntityDefinitionManager edm = null;

	/** TreeViewManager */
	private TreeViewManager tvm = null;

	/**
	 * コンストラクタ
	 */
	public GetTreeViewListDataCommand() {
		em = ManagerLocator.getInstance().getManager(EntityManager.class);
		edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
		tvm = ManagerLocator.getInstance().getManager(TreeViewManager.class);
	}

	@Override
	public String execute(RequestContext request) {
		String defName = request.getParam(Constants.DEF_NAME);
		String path = request.getParam(Constants.TREE_PATH);
		String type = request.getParam("type");
		String oid = request.getParam(Constants.OID);
		String offset = request.getParam("offset");

		TreeView tree = tvm.get(defName);
		List<TreeViewNode> nodes = null;

		if (StringUtil.isBlank(path)) {
			//初期ロード
			nodes = initTreeView(tree);
		} else {
			TreeViewItem item = tree.getItem(path);
			if ("root".equals(oid)) oid = null;
			if (EntityDefinitionNode.NODE_TYPE.equals(type)) {
				nodes = underEntityDefinition(item, oid, path);
			} else if (IndexNode.NODE_TYPE.equals(type)) {
				//パスからデータ取得
				if (StringUtil.isNotBlank(offset)) {
					//offset位置からの取得(件数絞り)
					try {
						int _offset = Integer.parseInt(offset);
						nodes = underIndex(item, oid, path, _offset);
					} catch (NumberFormatException e) {
						if (logger.isDebugEnabled()) {
							logger.debug(e.getMessage(), e);
						}
					}
				}
			} else if (EntityNode.NODE_TYPE.equals(type)) {
				//指定データ直下を全取得
				nodes = underEntity(item, oid, path);
			}
		}

		request.setAttribute("data", new JsonStreamingOutput(convertData(nodes)));
		return Constants.CMD_EXEC_SUCCESS;
	}

	/**
	 * JqTree用のデータに変換
	 * @param nodes
	 * @return
	 */
	private List<TreeViewJqTreeData> convertData(List<TreeViewNode> nodes) {
		List<TreeViewJqTreeData> ret = new ArrayList<TreeViewJqTreeData>();
		for (TreeViewNode node : nodes) {
			TreeViewJqTreeData data = new TreeViewJqTreeData();
			data.setType(node.getType());
			data.setPath(node.getPath());
			data.setName(node.getDisplayName());
			data.setStyle(node.getCssStyle());
			data.setIcon(node.getIcon());
			if (node instanceof EntityDefinitionNode) {
				data.setLoad_on_demand(true);
				data.setId(node.getPath().replaceAll("/", "_"));
			} else if (node instanceof IndexNode) {
				IndexNode in = (IndexNode) node;
				data.setLoad_on_demand(true);
				data.setOid(in.getOid());
				data.setOffset(in.getOffset());
				data.setId(node.getPath().replaceAll("/", "_") + ":" + in.getOffset());
			} else if (node instanceof EntityNode) {
				EntityNode en = (EntityNode) node;
				data.setLoad_on_demand(en.isHasReference());
				data.setOid(en.getOid());
				data.setDefName(en.getDefName());
				data.setAction(en.getAction());
				data.setViewName(en.getViewName());
				data.setId(node.getPath().replaceAll("/", "_") + ":" + en.getOid());
			}
			ret.add(data);
		}
		return ret;
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
	private List<TreeViewNode> initTreeView(TreeView treeView) {
		AuthContext authContext = AuthContext.getCurrentContext();
		List<TreeViewNode> nodes = new ArrayList<TreeViewNode>();
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
					TreeViewNode node = createEntityDefinitionNode(item, null, (String) null);
					if (node != null) nodes.add(node);
				} else {
					List<TreeViewNode> _nodes = underEntityDefinition(item, null, item.getDefName());
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
	 * @param path ノードのパス
	 * @return Entity定義のノード
	 */
	private List<TreeViewNode> underEntityDefinition(TreeViewItem item, String oid, String path) {
		List<TreeViewNode> list = new ArrayList<TreeViewNode>();
		int totalCount = count(item, oid);
		if (totalCount == 0) return list;

		EntityDefinitionNode edNode = new EntityDefinitionNode((String)null, path);

		if (totalCount > item.getLimit()) {
			// total/limit+1個分の階層作成
			int index = totalCount % item.getLimit() == 0 ?
					totalCount / item.getLimit() : totalCount / item.getLimit() + 1;
			for (int i = 0; i < index; i++) {
				int _offset = item.getLimit() * i;
				IndexNode iNode = new IndexNode(edNode.getPath());
				iNode.setDisplayName((_offset + 1) + "～" + (_offset + item.getLimit()) + "件");
				iNode.setIcon(item.getIndexNodeIcon());
				iNode.setCssStyle(item.getIndexNodeCssStyle());
				iNode.setOffset(_offset);
				iNode.setOid(oid);
				list.add(iNode);
			}
		} else {
			list.addAll(createEntityNode(item, -1, oid, edNode.getPath()));
		}
		return list;
	}

	private List<TreeViewNode> underIndex(TreeViewItem item, String oid, String path, int offset) {
		List<EntityNode> entities = createEntityNode(item, offset, oid, path);
		List<TreeViewNode> nodes = new ArrayList<TreeViewNode>();
		nodes.addAll(entities);
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
	 * @param path ノードのパス
	 * @return 下位のノード
	 */
	private List<TreeViewNode> underEntity(TreeViewItem item, String oid, String path) {
		List<TreeViewNode> nodes = new ArrayList<TreeViewNode>();
		for (ReferenceTreeViewItem rItem : item.getReferenceTreeViewItems()) {
			if (rItem.isDisplayDefinitionNode()) {
				TreeViewNode rNode = createEntityDefinitionNode(rItem, oid, path);
				if (rNode != null) nodes.add(rNode);
			} else {
				List<TreeViewNode> _nodes = underEntityDefinition(rItem, oid, path + "/" + rItem.getPropertyName());
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
	private TreeViewNode createEntityDefinitionNode(TreeViewItem item, String oid, String path) {
		int totalCount = count(item, oid);
		if (totalCount == 0) return null;

		EntityDefinitionNode edNode = null;
		if (item instanceof ReferenceTreeViewItem) {
			ReferenceTreeViewItem rItem = (ReferenceTreeViewItem) item;
			edNode = new EntityDefinitionNode(path, rItem.getPropertyName());
			edNode.setDisplayName(rItem.getDisplayName());
		} else {
			edNode = new EntityDefinitionNode(path, item.getDefName());

			//表示名
			String displayName = TemplateUtil.getMultilingualString(edm.get(item.getDefName()).getDisplayName(), edm.get(item.getDefName()).getLocalizedDisplayNameList());

			edNode.setDisplayName(displayName);
		}
		edNode.setIcon(item.getEntityDefinitionNodeIcon());
		edNode.setCssStyle(item.getEntityDefinitionNodeCssStyle());
		return edNode;
	}

	/**
	 * Entityを検索してEntityノードを作成します。
	 * @param item ツリーメニューのアイテム
	 * @param offset 検索位置
	 * @param oid EntityのOID
	 * @param path ノードのパス
	 * @return Entityのノード
	 */
	private List<EntityNode> createEntityNode(TreeViewItem item, int offset, String oid, String path) {
		List<EntityNode> nodes = new ArrayList<EntityNode>();
		Entity[] entites = search(item, offset, oid);
		for (Entity entity : entites) {
			EntityNode en = null;
			if (item instanceof ReferenceTreeViewItem) {
				en = new EntityNode(path, ((ReferenceTreeViewItem) item).getPropertyName());
			} else {
				en = new EntityNode(path, item.getDefName());
			}
			if (existProperty(item)) {
				en.setDisplayName(entity.getValue(item.getDisplayPropertyName()).toString());
			} else {
				en.setDisplayName(entity.getName());
			}
			en.setOid(entity.getOid());
			en.setDefName(entity.getDefinitionName());
			en.setAction(item.getAction());
			en.setViewName(item.getViewName());
			en.setIcon(item.getEntityNodeIcon());
			en.setCssStyle(item.getEntityNodeCssStyle());
			en.setHasReference(!item.getReferenceTreeViewItems().isEmpty());
			nodes.add(en);
		}
		return nodes;
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
	private Entity[] search(TreeViewItem item, int offset, String oid) {
		Query cond = new Query();
		List<String> select = new ArrayList<String>();
		select.add(Entity.OID);
		select.add(Entity.NAME);
		if (existProperty(item)) {
			if (!select.contains(item.getDisplayPropertyName()))select.add(item.getDisplayPropertyName());
		}
		cond.select(select.toArray());
		cond.from(item.getDefName());
		if (oid != null && item instanceof ReferenceTreeViewItem) {
			cond.where(getCondition((ReferenceTreeViewItem) item, oid));
		}
		cond.order(getSortSpec(item));
		if (offset > -1) {
			cond.limit(item.getLimit(), offset);
		} else {
			cond.limit(item.getLimit());
		}

		List<Entity> list = em.searchEntity(cond).getList();
		return list.toArray(new Entity[list.size()]);
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
}
