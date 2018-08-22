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

package org.iplass.gem.command.generic.reflink;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import org.iplass.gem.command.CommandUtil;
import org.iplass.gem.command.Constants;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.RestJson;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.entity.query.PreparedQuery;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.SortSpec;
import org.iplass.mtp.entity.query.SortSpec.SortType;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.expr.And;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.EntityViewManager;
import org.iplass.mtp.view.generic.editor.LinkProperty;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.RequestType;

@WebApi(
		name=GetReferenceLinkItemCommand.WEBAPI_NAME,
		accepts=RequestType.REST_JSON,
		methods=MethodType.POST,
		restJson=@RestJson(parameterName="param"),
		results={"selName", "data"},
		checkXRequestedWithHeader=true
	)
@CommandClass(name="gem/generic/link/GetReferenceLinkItemCommand", displayName="連動アイテム取得")
public final class GetReferenceLinkItemCommand implements Command {

	public static final String WEBAPI_NAME = "gem/generic/link/getLinkItem";

	private EntityManager em = null;
	private EntityDefinitionManager edm = null;
	private EntityViewManager evm = null;

	/**
	 * コンストラクタ
	 */
	public GetReferenceLinkItemCommand() {
		em = ManagerLocator.getInstance().getManager(EntityManager.class);
		edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
		evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
	}

	@Override
	public String execute(RequestContext request) {
		//パラメータ取得
		String defName = request.getParam(Constants.DEF_NAME);
		String viewType = request.getParam(Constants.VIEW_TYPE);
		String viewName = request.getParam(Constants.VIEW_NAME);
		String propName = request.getParam(Constants.PROP_NAME);
		String linkValue = request.getParam(Constants.REF_LINK_VALUE);

		//Editor取得
		PropertyEditor editor = evm.getPropertyEditor(defName, viewType, viewName, propName);
		ReferencePropertyEditor rpe = null;
		if (editor instanceof ReferencePropertyEditor) {
			rpe = (ReferencePropertyEditor)editor;
		}
		if (rpe != null) {
			List<SimpleEntity> data = getData(rpe, linkValue, request);
			request.setAttribute("data", data);
		}

		return Constants.CMD_EXEC_SUCCESS;
	}

	private List<SimpleEntity> getData(ReferencePropertyEditor editor, String linkValue, RequestContext request) {

		boolean doSearch = false;
		LinkProperty linkProperty = editor.getLinkProperty();
		Condition condition = null;
		if (linkProperty != null) {
			//連動の場合は上位値を取得して値が設定されている場合のみ検索
			doSearch = false;
			if (StringUtil.isNotEmpty(linkValue)) {
				//連動先の型を取得
				EntityDefinition ed = edm.get(editor.getObjectName());
				if (ed != null) {
					PropertyDefinition pd = ed.getProperty(linkProperty.getLinkToPropertyName());
					String upperPropName = linkProperty.getLinkToPropertyName();
					String upperValue = linkValue;
					Condition upperCondition = null;
					if (pd instanceof ReferenceProperty) {
						upperPropName = upperPropName + "." + Entity.OID;

						int lastIndex = upperValue.lastIndexOf("_");
						if (lastIndex < 0) {
							upperCondition = new Equals(linkProperty.getLinkToPropertyName() + "." + Entity.OID, upperValue);
						} else {
							String oid = upperValue.substring(0, lastIndex);;
							Long version = CommandUtil.getLong(upperValue.substring(lastIndex + 1));
							upperCondition = new And(
									new Equals(linkProperty.getLinkToPropertyName() + "." + Entity.OID, oid),
									new Equals(linkProperty.getLinkToPropertyName() + "." + Entity.VERSION, version));
						}
					} else {
						upperCondition = new Equals(linkProperty.getLinkToPropertyName(), linkValue);
					}

					if (StringUtil.isNotEmpty(editor.getCondition())) {
						condition = new PreparedQuery(editor.getCondition()).condition(null);
					}
					if (condition != null) {
						condition = new And(condition, upperCondition);
					} else {
						condition = upperCondition;
					}
					doSearch = true;
				}
			}
		}

		if (doSearch) {
			Query q = new Query();
			q.from(editor.getObjectName());
			q.select(Entity.OID, Entity.NAME, Entity.VERSION);
			if (condition != null) {
				q.where(condition);
			}
			if (editor.getSortType() != null) {
				String sortItem = editor.getSortItem() != null ? editor.getSortItem() : Entity.OID;
				if (!Entity.OID.equals(sortItem) && !Entity.NAME.equals(sortItem)) {
					q.select().add(sortItem);
				}
				SortType sortType = SortSpec.SortType.ASC;
				if ("DESC".equals(editor.getSortType().name())) {
					sortType = SortSpec.SortType.DESC;
				}
				q.order(new SortSpec(sortItem, sortType));
			}

			final List<SimpleEntity> list = new ArrayList<SimpleEntity>();
			em.searchEntity(q, new Predicate<Entity>() {

				@Override
				public boolean test(Entity dataModel) {
					list.add(new SimpleEntity(dataModel));
					return true;
				}
			});

			return list;
		} else {
			return Collections.emptyList();
		}
	}

	class SimpleEntity {

		private String oid;
		private String name;
		private Long version;

		public SimpleEntity(Entity entity) {
			this.oid = entity.getOid();
			this.name = entity.getName();
			this.setVersion(entity.getVersion());
		}

		public String getOid() {
			return oid;
		}

		public void setOid(String oid) {
			this.oid = oid;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Long getVersion() {
			return version;
		}

		public void setVersion(Long version) {
			this.version = version;
		}
	}

}
