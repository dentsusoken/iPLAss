/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.entity.layout.metafield;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.ui.widget.MetaDataLangTextItem;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataSelectItem;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataSelectItem.ItemOption;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpIntegerItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpSelectItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextAreaItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.metadata.dto.refrect.FieldInfo;
import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.web.actionmapping.definition.ActionMappingDefinition;
import org.iplass.mtp.web.template.definition.TemplateDefinition;
import org.iplass.mtp.webapi.definition.WebApiDefinition;

import com.smartgwt.client.types.MultipleAppearance;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;

public class MetaFieldSettingPartsControllerImpl implements MetaFieldSettingPartsController {

	private static final String DEFAULT_ACTION_NAME = "#default";
	private static final String DEFAULT_WEBAPI_NAME = "#default";

	@Override
	@SuppressWarnings("unchecked")
	public FormItem createItem(MetaFieldSettingPane pane, FieldInfo info) {
		FormItem item = null;
		if (info.getInputType() == InputType.TEXT) {
			item = new MtpTextItem();
			if (info.isRequired()) {
				SmartGWTUtil.setRequired(item);
			}
			if (pane.getValue(info.getName()) != null) {
				item.setValue(pane.getValueAs(String.class, info.getName()));
			}
		} else if (info.getInputType() == InputType.TEXT_AREA) {
			item = new MtpTextAreaItem();
			if (info.isRequired()) {
				SmartGWTUtil.setRequired(item);
			}
			if (pane.getValue(info.getName()) != null) {
				item.setValue(pane.getValueAs(String.class, info.getName()));
			}
		} else if (info.getInputType() == InputType.MULTI_TEXT) {
			item = new MetaDataMultiTextGridItem(pane, info);
		} else if (info.getInputType() == InputType.NUMBER) {
			item = new MtpIntegerItem();
			if (info.isRequired()) {
				SmartGWTUtil.setRequired(item);
			}
			if (pane.getValue(info.getName()) != null) {
				item.setValue(pane.getValueAs(Integer.class, info.getName()));
			}
		} else if (info.getInputType() == InputType.CHECKBOX) {
			item = new CheckboxItem();
			if (info.isRequired()) {
				SmartGWTUtil.setRequired(item);
			}
			if (pane.getValue(info.getName()) != null) {
				item.setValue(pane.getValueAs(Boolean.class, info.getName()));
			}
		} else if (info.getInputType() == InputType.ENUM) {
			item = new MtpSelectItem();
			ArrayList<String> enms = new ArrayList<>();
			enms.add("");
			for (int i = 0; i < info.getEnumValues().length; i++) {
				enms.add(info.getEnumValues()[i].toString());
			}
			item.setValueMap(enms.toArray(new String[]{}));
			((SelectItem) item).setMultiple(info.isMultiple());
			((SelectItem) item).setMultipleAppearance(MultipleAppearance.GRID);
			if (info.isRequired()) {
				SmartGWTUtil.setRequired(item);
			}
			if (pane.getValue(info.getName()) != null) {
				if (info.isMultiple()) {
					List<Object> values = (List<Object>)pane.getValue(info.getName());
					List<String> strValues = new ArrayList<>();
					for (Object value : values) {
						strValues.add(value.toString());
					}
					((SelectItem) item).setValues(strValues.toArray(new String[0]));
				} else {
					item.setValue(pane.getValue(info.getName()).toString());
				}
			}
		} else if (info.getInputType() == InputType.ACTION) {
			item = createActionList(info);
			if (info.isRequired()) {
				SmartGWTUtil.setRequired(item);
			}
			if (pane.getValue(info.getName()) != null) {
				String val = pane.getValueAs(String.class, info.getName());
				if (val.isEmpty()) {
					item.setValue(DEFAULT_ACTION_NAME);
				} else {
					item.setValue(val);
				}
			} else {
				item.setValue(DEFAULT_ACTION_NAME);
			}
		} else if (info.getInputType() == InputType.WEBAPI) {
			item = createWebAPIList(info);
			if (info.isRequired()) {
				SmartGWTUtil.setRequired(item);
			}
			if (pane.getValue(info.getName()) != null) {
				String val = pane.getValueAs(String.class, info.getName());
				if (val.isEmpty()) {
					item.setValue(DEFAULT_WEBAPI_NAME);
				} else {
					item.setValue(val);
				}
			} else {
				item.setValue(DEFAULT_WEBAPI_NAME);
			}
		} else if (info.getInputType() == InputType.TEMPLATE) {
			item = createTemplateList(info);
			if (info.isRequired()) {
				SmartGWTUtil.setRequired(item);
			}
			if (pane.getValue(info.getName()) != null) {
				item.setValue(pane.getValueAs(String.class, info.getName()));
			}
		} else if (info.getInputType() == InputType.MULTI_LANG) {
			item = new MetaDataLangTextItem();
			if (info.isRequired()) {
				SmartGWTUtil.setRequired(item);
			}
			if (pane.getValue(info.getName()) != null) {
				item.setValue(pane.getValueAs(Boolean.class, info.getName()));
			}
			if (SmartGWTUtil.isNotEmpty(info.getMultiLangFieldName())) {
				List<LocalizedStringDefinition> localizedList = (List<LocalizedStringDefinition>) pane
						.getValue(info.getMultiLangFieldName());
				((MetaDataLangTextItem) item).setLocalizedList(localizedList);
			}
		} else if (info.getInputType() == InputType.SCRIPT) {
			item = new MetaFieldScriptItem(pane, info);
		} else if (info.getInputType() == InputType.REFERENCE) {
			if (info.isMultiple()) {
				item = new MetaFieldReferenceMultiItem(pane, info);
			} else {
				item = new MetaFieldReferenceSingleItem(pane, info);
			}
		}
		return item;
	}

	private FormItem createActionList(final FieldInfo info) {
		if (!info.isRequired()) {
			return new MetaDataSelectItem(ActionMappingDefinition.class, new ItemOption(false, true));
		} else {
			return new MetaDataSelectItem(ActionMappingDefinition.class);
		}
	}

	private FormItem createWebAPIList(final FieldInfo info) {
		if (!info.isRequired()) {
			return new MetaDataSelectItem(WebApiDefinition.class, new ItemOption(false, true));
		} else {
			return new MetaDataSelectItem(WebApiDefinition.class);
		}
	}

	private FormItem createTemplateList(FieldInfo info) {
		return new MetaDataSelectItem(TemplateDefinition.class);
	}

}
