/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.gem.command.generic.detail;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.iplass.gem.command.Constants;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.RestJson;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.view.generic.EntityViewManager;
import org.iplass.mtp.view.generic.EntityViewUtil;
import org.iplass.mtp.view.generic.editor.DateRangePropertyEditor;
import org.iplass.mtp.view.generic.editor.NestProperty;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor.ReferenceDisplayType;
import org.iplass.mtp.view.generic.element.property.PropertyItem;
import org.iplass.mtp.webapi.definition.RequestType;
import org.iplass.mtp.webapi.definition.MethodType;

@WebApi(
		name=GetNestTableDataCommand.WEBAPI_NAME,
		displayName="Entityデータ取得",
		accepts=RequestType.REST_JSON,
		methods=MethodType.POST,
		restJson=@RestJson(parameterName="param"),
		results={Constants.DATA},
		checkXRequestedWithHeader=true
	)
@CommandClass(name="gem/generic/detail/GetNestTableDataCommand", displayName="NestTableデータ取得")
public final class GetNestTableDataCommand implements Command {

	public static final String WEBAPI_NAME = "gem/generic/detail/getNestTableData";

	private EntityDefinitionManager edm;
	private EntityViewManager evm;
	private EntityManager em;

	public GetNestTableDataCommand() {
		edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
		evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
		em = ManagerLocator.getInstance().getManager(EntityManager.class);
	}

	@Override
	public String execute(RequestContext request) {
		DetailCommandContext context = new DetailCommandContext(request, em, edm);
		context.setEntityDefinition(edm.get(context.getDefinitionName()));
		context.setEntityView(evm.get(context.getDefinitionName()));
		if (context.getView() == null) {
			// 画面定義なし
			return Constants.CMD_EXEC_ERROR_VIEW;
		}

		String propName = request.getParam(Constants.PROP_NAME);//対象の参照プロパティ
		PropertyItem property = getProperty(context, propName);
		if (property == null) {
			// 画面定義にプロパティが設定されてない
			return Constants.CMD_EXEC_ERROR_VIEW;
		}

		PropertyDefinition pd = context.getProperty(property.getPropertyName());
		if (pd == null || !(pd instanceof ReferenceProperty)) {
			// Propertyの型違い
			return Constants.CMD_EXEC_ERROR_VIEW;
		}

		ReferenceProperty rp = (ReferenceProperty) pd;
		List<String> nestDispLabelProps = new ArrayList<String>();
		List<String> properties = getNestProperty(context, property, rp, nestDispLabelProps);

		String targetDefName = rp.getObjectDefinitionName();
		String oid = request.getParam(Constants.OID);

		Query query = new Query().select(properties.toArray())
				.from(targetDefName)
				.where(new Equals(Entity.OID, oid));

		Entity entity = em.searchEntity(query).getFirst();
		replaceNestPropOutputName(entity, nestDispLabelProps);
		request.setAttribute(Constants.DATA, entity);
		return Constants.CMD_EXEC_SUCCESS;
	}

	private List<String> getNestProperty(DetailCommandContext context, PropertyItem property, ReferenceProperty rp, List<String> nestDispLabelProps) {
		ReferencePropertyEditor rpe = (ReferencePropertyEditor) property.getEditor();
		if (!(property.getEditor() instanceof ReferencePropertyEditor)) {
			// Propertyの型違い
			return null;
		}
		if (rpe.getDisplayType() != ReferenceDisplayType.NESTTABLE) {
			// NestTableではない
			return null;
		}

		if (rpe.getNestProperties().isEmpty()) {
			// NestPropety未定義
			return null;
		}

		final List<String> propNames = new ArrayList<String>();
		propNames.add(Entity.OID);
		propNames.add(Entity.VERSION);
		rpe.getNestProperties().stream().filter(np -> isDispProperty(context, rp, np)).forEach(np -> {
			if (np.getEditor() instanceof ReferencePropertyEditor) {
				if (!propNames.contains(np.getPropertyName() + "." + Entity.OID)) {
					propNames.add(np.getPropertyName() + "." + Entity.OID);
				}
				if (!propNames.contains(np.getPropertyName() + "." + Entity.NAME)) {
					propNames.add(np.getPropertyName() + "." + Entity.NAME);
				}
				if (!propNames.contains(np.getPropertyName() + "." + Entity.VERSION)) {
					propNames.add(np.getPropertyName() + "." + Entity.VERSION);
				}
				String displayLabelProp = ((ReferencePropertyEditor) np.getEditor()).getDisplayLabelItem();
				if (displayLabelProp != null && !propNames.contains(np.getPropertyName() + "." + displayLabelProp)) {
					propNames.add(np.getPropertyName() + "." + displayLabelProp);
					nestDispLabelProps.add(np.getPropertyName() + "." + displayLabelProp);
				}
			} else if (np.getEditor() instanceof DateRangePropertyEditor) {
				DateRangePropertyEditor editor = (DateRangePropertyEditor) np.getEditor();
				if (!propNames.contains(np.getPropertyName())) {
					propNames.add(np.getPropertyName());
				}
				if (!propNames.contains(editor.getToPropertyName())) {
					propNames.add(editor.getToPropertyName());
				}
			} else {
				if (!propNames.contains(np.getPropertyName())) {
					propNames.add(np.getPropertyName());
				}
			}
		});

		return propNames;
	}

	private PropertyItem getProperty(DetailCommandContext context, String propName) {
		Optional<PropertyItem> property = context.getProperty().stream().filter(
				pi -> propName.equals(pi.getPropertyName())).findFirst();

		if (property.isPresent()) {
			return property.get();
		}
		return null;
	}

	private boolean isDispProperty(DetailCommandContext context, ReferenceProperty rp, NestProperty np) {
		if (np.isHideDetail()) return false;
		if (np.getEditor() == null) return false;

		String propName = rp.getName() + "." + np.getPropertyName();
		PropertyDefinition pd = EntityViewUtil.getPropertyDefinition(propName, context.getEntityDefinition());
		if (pd == null) return false;
		if (pd.getMultiplicity() != 1) return false;
		if (pd instanceof ReferenceProperty) {
			if (rp.getMappedBy() != null && pd.getName().equals(rp.getMappedBy())) return false;//逆参照が本体の場合
		}
		return true;
	}

	private void replaceNestPropOutputName(Entity entity, List<String> nestDisplayLabelProps) {
		if (entity == null || nestDisplayLabelProps == null || nestDisplayLabelProps.isEmpty()) return;

		for (String displayLabelProp : nestDisplayLabelProps) {
			String currentPropName = displayLabelProp.substring(0, displayLabelProp.indexOf("."));
			String subPropName = displayLabelProp.substring(displayLabelProp.indexOf(".") + 1);
			// 「Name」がラベルプロパティ項目として設定された場合、クリアしません。
			if (!Entity.NAME.equals(subPropName)) {
				// ラベルとして扱うプロパティ項目を「name」として返します。
				entity.setValue(currentPropName + "." + Entity.NAME, entity.getValue(displayLabelProp));
				entity.setValue(displayLabelProp, null);
			}
		}
	}
}
