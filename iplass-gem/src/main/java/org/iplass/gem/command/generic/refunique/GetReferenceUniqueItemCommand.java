/*
 * Copyright (C) 2019 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.gem.command.generic.refunique;

import java.util.function.Predicate;

import org.iplass.gem.command.Constants;
import org.iplass.gem.command.generic.HasDisplayScriptBindings;
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
import org.iplass.mtp.entity.definition.IndexType;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.entity.query.condition.predicate.IsNull;
import org.iplass.mtp.impl.util.ConvertUtil;
import org.iplass.mtp.view.generic.EntityViewManager;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.RequestType;

@WebApi(
		name = GetReferenceUniqueItemCommand.WEBAPI_NAME,
		accepts = RequestType.REST_JSON,
		methods = MethodType.POST,
		restJson = @RestJson(parameterName = "param"),
		results = {Constants.DATA},
		checkXRequestedWithHeader = true
	)
@CommandClass(name = "gem/generic/refunique/ReferenceUniqueCommand", displayName = "参照ユニークキーコマンド")
public class GetReferenceUniqueItemCommand implements Command, HasDisplayScriptBindings {

	public static final String WEBAPI_NAME = "gem/generic/refunique/getUniqueItem";

	private EntityManager em = null;
	private EntityDefinitionManager edm = null;
	private EntityViewManager evm = null;

	public GetReferenceUniqueItemCommand() {
		em = ManagerLocator.getInstance().getManager(EntityManager.class);
		edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
		evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
	}

	@Override
	public String execute(RequestContext request) {
		// パラメータ取得
		String defName = request.getParam(Constants.DEF_NAME);
		String viewName = request.getParam(Constants.VIEW_NAME);
		String propName = request.getParam(Constants.PROP_NAME);
		String viewType = request.getParam(Constants.VIEW_TYPE);
		String uniqueValue = request.getParam(Constants.REF_UNIQUE_VALUE);

		Entity entity = getBindingEntity(request);
		// Editor取得
		PropertyEditor editor = evm.getPropertyEditor(defName, viewType, viewName, propName, entity);
		ReferencePropertyEditor rpe = null;
		if (editor instanceof ReferencePropertyEditor) {
			rpe = (ReferencePropertyEditor) editor;
		}
		if (rpe == null) {
			return "NOT_DEFINED_EDITOR";
		}
		if (!isUniqueProp(rpe)) {
			return "NOT_UNIQUE_PROP";
		}

		SimpleEntity data = getData(rpe, uniqueValue, request);
		request.setAttribute(Constants.DATA, data);

		return Constants.CMD_EXEC_SUCCESS;
	}

	private boolean isUniqueProp(ReferencePropertyEditor editor) {
		// OIDをユニークキーフィールドとして使えるように
		if (Entity.OID.equals(editor.getUniqueItem())) return true;

		EntityDefinition ed = edm.get(editor.getObjectName());
		PropertyDefinition pd = ed.getProperty(editor.getUniqueItem());
		return pd.getIndexType() == IndexType.UNIQUE || pd.getIndexType() == IndexType.UNIQUE_WITHOUT_NULL;
	}

	private SimpleEntity getData(ReferencePropertyEditor editor, String uniqueValue, RequestContext request) {
		Query q = new Query();
		q.select(Entity.OID, Entity.VERSION, editor.getUniqueItem());
		if (editor.getDisplayLabelItem() != null) {
			q.select().add(editor.getDisplayLabelItem());
		} else {
			q.select().add(Entity.NAME);
		}
		q.from(editor.getObjectName());
		if (uniqueValue == null) {
			q.where(new IsNull(editor.getUniqueItem()));
		} else {
			q.where(new Equals(editor.getUniqueItem(), uniqueValue));
		}

		final SimpleEntity data = new SimpleEntity();
		em.searchEntity(q, new Predicate<Entity>() {

			@Override
			public boolean test(Entity dataModel) {
				data.setOid(dataModel.getOid());
				data.setVersion(dataModel.getVersion());
				if (editor.getDisplayLabelItem() != null) {
					String displayLabelItem = editor.getDisplayLabelItem();
					// 表示ラベルとして扱うプロパティをNameに設定
					data.setName((String)dataModel.getValue(displayLabelItem));
				} else {
					data.setName(dataModel.getName());
				}
				//ユニークキー項目
				String uniqueItem = editor.getUniqueItem();
				if (uniqueItem != null) {
					String uniqueValue = ConvertUtil.convertToString(dataModel.getValue(uniqueItem));
					data.setUniqueValue(uniqueValue);
				}
				return false;
			}
		});

		return data;
	}

	class SimpleEntity {

		private String oid;
		private String name;
		private Long version;
		private String uniqueValue;
		
		public SimpleEntity() {
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

		public String getUniqueValue() {
			return uniqueValue;
		}

		public void setUniqueValue(String uniqueValue) {
			this.uniqueValue = uniqueValue;
		}
	}

}
