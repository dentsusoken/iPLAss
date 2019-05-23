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
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.EntityViewManager;
import org.iplass.mtp.view.generic.editor.JoinPropertyEditor;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor;
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
			// 表示ラベルとして扱うプロパティを取得します
			String dispLabelProp = getDisplayLabelItem(parentDefName, parentViewName, parentPropName, viewType);
			Query query = new Query();
			query.select(Entity.OID, Entity.VERSION).from(defName);
			if (dispLabelProp == null) { // 未設定場合、Nameを検索します。
				query.select().add(Entity.NAME);
			} else if (!dispLabelProp.isEmpty()) { // 表示ラベルが設定されている場合
				query.select().add(dispLabelProp);
			}
			Or or = new Or();
			for (GetEntityNameListEntityParameter entity : list) {
				And and = new And(new Equals(Entity.OID, entity.getOid()),
						new Equals(Entity.VERSION, entity.getVersion()));
				or.addExpression(new Paren(and));
			}
			query.where(or);

			EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);
			ret = em.searchEntity(query).getList();
			if (StringUtil.isNotBlank(dispLabelProp) && ret != null) {
				// ラベルとして扱うプロパティ項目をnameとして返します。
				replaceNamePropWithDisplayLabelProp((List<Entity>) ret, dispLabelProp);
			}
		}
		request.setAttribute("value", ret);
		return "OK";
	}

	private String getDisplayLabelItem(String defName, String viewName, String propName, String viewType) {
		EntityViewManager evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
		PropertyEditor editor = evm.getPropertyEditor(defName, viewType, viewName, propName);
		if (editor instanceof ReferencePropertyEditor) {
			ReferencePropertyEditor rpe = (ReferencePropertyEditor) editor;
			return rpe.getDisplayLabelItem();
		} else if (editor instanceof JoinPropertyEditor) {
			JoinPropertyEditor jpe = (JoinPropertyEditor) editor;
			if (jpe.getEditor() instanceof ReferencePropertyEditor) {
				return ((ReferencePropertyEditor) jpe.getEditor()).getDisplayLabelItem();
			}
		}
		// エディター定義が見つからなかった場合、空文字を返します。
		return "";
	}

	private void replaceNamePropWithDisplayLabelProp(List<Entity> entities, String dispLabelProp) {
		for (Entity entity : entities) {
			String dispPropValue = entity.getValue(dispLabelProp);
			entity.setValue(dispLabelProp, null);
			entity.setValue(Entity.NAME, dispPropValue);
		}
	}
}
