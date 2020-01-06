/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.gem.command.generic.reftree;

import java.util.ArrayList;
import java.util.List;

import org.iplass.gem.command.Constants;
import org.iplass.gem.command.common.JqTreeData;
import org.iplass.gem.command.common.JsonStreamingOutput;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.RestJson;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.GenericEntity;
import org.iplass.mtp.entity.query.PreparedQuery;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.SortSpec;
import org.iplass.mtp.entity.query.SortSpec.SortType;
import org.iplass.mtp.entity.query.SubQuery;
import org.iplass.mtp.entity.query.condition.expr.And;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.entity.query.condition.predicate.In;
import org.iplass.mtp.entity.query.value.aggregate.Count;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.EntityViewManager;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor;
import org.iplass.mtp.view.generic.editor.ReferenceRecursiveTreeSetting;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.RequestType;

@WebApi(
		name=SearchTreeDataCommand.WEBAPI_NAME,
		accepts=RequestType.REST_FORM,
		methods=MethodType.POST,
		restJson=@RestJson(parameterName="param"),
		results={Constants.DATA},
		checkXRequestedWithHeader=true
	)
@CommandClass(name="gem/generic/reftree/SearchTreeDataCommand")
public class SearchTreeDataCommand implements Command {

	public static final String WEBAPI_NAME = "gem/generic/reftree/searchReferenceTreeData";

	private EntityViewManager evm;
	private EntityManager em;

	public SearchTreeDataCommand() {
		evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
		em = ManagerLocator.getInstance().getManager(EntityManager.class);
	}

	@Override
	public String execute(RequestContext request) {
		String defName = request.getParam(Constants.DEF_NAME);
		String viewType = request.getParam(Constants.VIEW_TYPE);
		String viewName = request.getParam(Constants.VIEW_NAME);
		String propName = request.getParam(Constants.PROP_NAME);
		String oid = request.getParam(Constants.OID);
		String linkValue = request.getParam("linkValue");

		Entity entity = getCurrentEntity(request);
		PropertyEditor editor = evm.getPropertyEditor(defName, viewType, viewName, propName, entity);

		//検索
		List<RefTreeJqTreeData > ret = new ArrayList<>();
		ret.addAll(search((ReferencePropertyEditor) editor, oid, linkValue));

		request.setAttribute(Constants.DATA, new JsonStreamingOutput(ret));

		return Constants.CMD_EXEC_SUCCESS;
	}

	private Entity getCurrentEntity(RequestContext request) {
		String defName = request.getParam(Constants.DEF_NAME);
		String entityOid = request.getParam(Constants.DISPLAY_SCRIPT_ENTITY_OID);
		String entityVersion = request.getParam(Constants.DISPLAY_SCRIPT_ENTITY_VERSION);

		if (StringUtil.isNotBlank(entityOid) && StringUtil.isNotBlank(entityVersion)) {
			Entity e = new GenericEntity(defName);
			e.setOid(entityOid);
			e.setVersion(Long.valueOf(entityVersion));
			return e;
		}
		return null;
	}

	private List<RefTreeJqTreeData > search(ReferencePropertyEditor editor, String oid, String linkValue) {
		ReferenceRecursiveTreeSetting treeSetting = editor.getReferenceRecursiveTreeSetting();

		Query query = new Query().select(Entity.OID, Entity.VERSION, new Count(treeSetting.getChildPropertyName() + ".oid"))
				.from(editor.getObjectName())
				.groupBy(Entity.OID, Entity.VERSION);
		if (editor.getDisplayLabelItem() != null) {
			query.select().add(editor.getDisplayLabelItem());
			query.getGroupBy().add(editor.getDisplayLabelItem());
		} else {
			query.select().add(Entity.NAME);
			query.getGroupBy().add(Entity.NAME);
		}


		//条件設定
		if (oid == null && treeSetting.getRootCondition() != null) {
			//定義された条件でルート階層を検索
			And and = new And();
			and.addExpression(new PreparedQuery(treeSetting.getRootCondition()).condition(null));

			//連動プロパティの設定がある場合はルート検索時に条件追加
			if (editor.getLinkProperty() != null && StringUtil.isNotBlank(linkValue)) {
				and.addExpression(new Equals(editor.getLinkProperty().getLinkToPropertyName(), linkValue));
			}

			query.where(and);
		} else if (oid != null) {
			//親のOIDから紐づく子のOIDを取得し、それを条件にして子を検索
			query.where(new In(Entity.OID, new SubQuery(
					new Query().selectDistinct(treeSetting.getChildPropertyName() + ".oid")
						.from(editor.getObjectName())
						.where(new Equals(Entity.OID, oid))
					)));
		}

		//ソート指定
		if (editor.getSortItem() != null) {
			String sortItem = editor.getSortItem();
			SortType sortType = null;
			if (editor.getSortType() == null) {
				sortType = SortType.ASC;
			} else {
				sortType = SortType.valueOf(editor.getSortType().name());
			}
			query.getGroupBy().add(sortItem);
			query.order(new SortSpec(sortItem, sortType));
		}

		List<Object[]> list = em.search(query).getList();
		List<RefTreeJqTreeData> ret = new ArrayList<>();
		for (Object[] data : list) {
			ret.add(new RefTreeJqTreeData(data));
		}

		return ret;
	}

	class RefTreeJqTreeData extends JqTreeData {
		private String oid;
		private Long version;

		public RefTreeJqTreeData(Object[] data) {
			setId((String) data[0]);
			setOid((String) data[0]);
			setVersion((Long) data[1]);
			setLoad_on_demand(((Long) data[2]) > 0);
			setName((String) data[3]);
		}

		public String getOid() {
			return oid;
		}

		public void setOid(String oid) {
			this.oid  = oid;
		}

		public Long getVersion() {
			return version;
		}

		public void setVersion(Long version) {
			this.version = version;
		}
	}
}
