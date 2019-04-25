/*
 * Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.gem.command.generic.common;

import java.util.List;

import org.iplass.gem.command.Constants;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.RestJson;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.condition.expr.And;
import org.iplass.mtp.entity.query.condition.expr.Or;
import org.iplass.mtp.entity.query.condition.expr.Paren;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.view.generic.EntityViewManager;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor.ReferenceDisplayType;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.RequestType;

/**
 * 参照データ名前一括取得コマンド
 * @author lis3wg
 */
@WebApi(name=GetEntityNameListCommand.WEBAPI_NAME,
	accepts=RequestType.REST_JSON,
	methods=MethodType.POST,
	restJson=@RestJson(parameterName="params", parameterType=GetEntityNameListParameter.class),
	results={"value"},
	checkXRequestedWithHeader=true
)
@CommandClass(name="gem/generic/common/GetEntityNameListCommand", displayName="参照データ名前一括取得")
public final class GetEntityNameListCommand implements Command {

	public static final String WEBAPI_NAME = "gem/generic/common/getEntityNameList";

	@SuppressWarnings("unchecked")
	@Override
	public String execute(RequestContext request) {
		GetEntityNameListParameter param = (GetEntityNameListParameter) request.getAttribute("params");

		String defName = param.getDefName();
		String parentDefName = param.getParentDefName();
		String parentViewName = param.getParentViewName();
		String parentPropName = param.getParentPropName();
		String viewType = param.getViewType();
		List<GetEntityNameListEntityParameter> list = param.getList();

		Object ret = null;
		if (defName != null && !defName.isEmpty()
				&& list != null && !list.isEmpty()) {
			String dispLabelProp = null;
			if (parentDefName != null && !parentDefName.isEmpty()
					&& parentPropName != null && !parentPropName.isEmpty()) {
				dispLabelProp = getDisplayLabelItem(parentDefName, parentViewName, parentPropName, viewType);
			}
			// ラベルとして扱うプロパティ項目が未設定の場合、nameを取得します。
			if (dispLabelProp == null) {
				dispLabelProp = Entity.NAME;
			}
			Query query = new Query();
			query.select(dispLabelProp, Entity.OID, Entity.VERSION).from(defName);
			Or or = new Or();
			for (GetEntityNameListEntityParameter entity : list) {
				And and = new And(new Equals(Entity.OID, entity.getOid()),
						new Equals(Entity.VERSION, entity.getVersion()));
				or.addExpression(new Paren(and));
			}
			query.where(or);

			EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);
			ret = em.searchEntity(query).getList();
			if (!Entity.NAME.equals(dispLabelProp) && ret != null) {
				// ラベルとして扱うプロパティ項目をnameとして返します。
				replaceNamePropWithDisplayLabelProp((List<Entity>) ret, dispLabelProp);
			}
		}
		request.setAttribute("value", ret);
		return "OK";
	}

	private String getDisplayLabelItem(String defName, String viewName, String propName, String viewType) {
		EntityViewManager evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
		String _propName = propName;
		if (Constants.VIEW_TYPE_SEARCH.equals(viewType) && propName.startsWith(Constants.SEARCH_COND_PREFIX)) {
			_propName = propName.substring(Constants.SEARCH_COND_PREFIX.length());
		}
		PropertyEditor editor = evm.getPropertyEditor(defName, viewType, viewName, _propName);
		if (editor != null && editor instanceof ReferencePropertyEditor) {
			ReferencePropertyEditor rpe = (ReferencePropertyEditor) editor;
			// TODO 表示タイプ：LINK、SELECTでの利用をサポートします。
			if ((rpe.getDisplayType() == ReferenceDisplayType.LINK || rpe.getDisplayType() == ReferenceDisplayType.SELECT)
					&& rpe.getDisplayLabelItem() != null) {
				return rpe.getDisplayLabelItem();
			}
		}
		return null;
	}

	private void replaceNamePropWithDisplayLabelProp(List<Entity> entities, String dispLabelProp) {
		for (Entity entity : entities) {
			String dispPropValue = entity.getValue(dispLabelProp);
			entity.setValue(Entity.NAME, dispPropValue);
			entity.setValue(dispLabelProp, null);
		}
	}
}
