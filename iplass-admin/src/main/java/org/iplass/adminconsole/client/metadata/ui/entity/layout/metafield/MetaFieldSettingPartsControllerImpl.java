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

import org.iplass.adminconsole.client.metadata.data.MetaDataNameDS;
import org.iplass.adminconsole.client.metadata.data.MetaDataNameDS.MetaDataNameDSOption;
import org.iplass.adminconsole.shared.metadata.dto.refrect.FieldInfo;
import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.mtp.web.actionmapping.definition.ActionMappingDefinition;
import org.iplass.mtp.web.template.definition.TemplateDefinition;
import org.iplass.mtp.webapi.definition.WebApiDefinition;

import com.smartgwt.client.types.MultipleAppearance;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.IntegerItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;

public class MetaFieldSettingPartsControllerImpl implements MetaFieldSettingPartsController {

	private static final String DEFAULT_ACTION_NAME = "#default";
	private static final String DEFAULT_WEBAPI_NAME = "#default";

	public FormItem createItem(MetaFieldSettingPane pane, FieldInfo info) {
		FormItem item = null;
		if (info.getInputType() == InputType.TEXT) {
			item = new TextItem();
			if (pane.getValue(info.getName()) != null) {
				item.setValue(pane.getValueAs(String.class, info.getName()));
			}
//		} else if (info.getInputType() == InputType.TextArea) {
//			item = new TextAreaItem();
//			if (getValue(info.getName()) != null) {
//				item.setValue(getValueAs(String.class, info.getName()));
//				item.setWidth(500);
//			}
		} else if (info.getInputType() == InputType.NUMBER) {
			item = new IntegerItem();
			if (pane.getValue(info.getName()) != null) {
				item.setValue(pane.getValueAs(Integer.class, info.getName()));
			}
		} else if (info.getInputType() == InputType.CHECKBOX) {
			item = new CheckboxItem();
			if (pane.getValue(info.getName()) != null) {
				item.setValue(pane.getValueAs(Boolean.class, info.getName()));
			}
		} else if (info.getInputType() == InputType.ENUM) {
			item = new SelectItem();
			ArrayList<String> enms = new ArrayList<String>();
			enms.add("");
			for (int i = 0; i < info.getEnumValues().length; i++) {
				enms.add(info.getEnumValues()[i].toString());
			}
			item.setValueMap(enms.toArray(new String[]{}));
			((SelectItem) item).setMultiple(info.isMultiple());
			((SelectItem) item).setMultipleAppearance(MultipleAppearance.GRID);
			if (pane.getValue(info.getName()) != null) {
				item.setValue(pane.getValue(info.getName()).toString());
			}
		} else if (info.getInputType() == InputType.ACTION) {
			item = createActionList(info);
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
			if (pane.getValue(info.getName()) != null) {
				item.setValue(pane.getValueAs(String.class, info.getName()));
			}
		}
		return item;
	}

	private FormItem createActionList(final FieldInfo info) {
		final SelectItem item = new SelectItem();
		if (!info.isRequired()) {
			MetaDataNameDS.setDataSource(item, ActionMappingDefinition.class, new MetaDataNameDSOption(false, true));
		} else {
			MetaDataNameDS.setDataSource(item, ActionMappingDefinition.class);
		}
		return item;
	}

	private FormItem createWebAPIList(final FieldInfo info) {
		final SelectItem item = new SelectItem();
		if (!info.isRequired()) {
			MetaDataNameDS.setDataSource(item, WebApiDefinition.class, new MetaDataNameDSOption(false, true));
		} else {
			MetaDataNameDS.setDataSource(item, WebApiDefinition.class);
		}
		return item;
	}

	private FormItem createTemplateList(FieldInfo info) {
		SelectItem item = new SelectItem();
		MetaDataNameDS.setDataSource(item, TemplateDefinition.class);
		return item;
	}

}
