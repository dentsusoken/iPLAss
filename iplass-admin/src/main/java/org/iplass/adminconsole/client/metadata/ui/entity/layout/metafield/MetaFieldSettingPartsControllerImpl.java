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
import org.iplass.adminconsole.client.base.ui.widget.MtpWidgetConstants;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogHandler;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogMode;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpIntegerItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpSelectItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextAreaItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.MetaDataUtil;
import org.iplass.adminconsole.shared.metadata.dto.refrect.FieldInfo;
import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.web.actionmapping.definition.ActionMappingDefinition;
import org.iplass.mtp.web.template.definition.TemplateDefinition;
import org.iplass.mtp.webapi.definition.WebApiDefinition;

import com.smartgwt.client.types.MultipleAppearance;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CanvasItem;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.layout.VLayout;

public class MetaFieldSettingPartsControllerImpl implements MetaFieldSettingPartsController {

	private static final String DEFAULT_ACTION_NAME = "#default";
	private static final String DEFAULT_WEBAPI_NAME = "#default";

	@SuppressWarnings("unchecked")
	public FormItem createItem(MetaFieldSettingPane pane, FieldInfo info) {
		FormItem item = null;
		if (info.getInputType() == InputType.TEXT) {
			item = new MtpTextItem();
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
			item = new MtpIntegerItem();
			if (pane.getValue(info.getName()) != null) {
				item.setValue(pane.getValueAs(Integer.class, info.getName()));
			}
		} else if (info.getInputType() == InputType.CHECKBOX) {
			item = new CheckboxItem();
			if (pane.getValue(info.getName()) != null) {
				item.setValue(pane.getValueAs(Boolean.class, info.getName()));
			}
		} else if (info.getInputType() == InputType.ENUM) {
			item = new MtpSelectItem();
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
		} else if (info.getInputType() == InputType.MULTI_LANG) {
			item = new MetaDataLangTextItem();
			if (pane.getValue(info.getName()) != null) {
				item.setValue(pane.getValueAs(Boolean.class, info.getName()));
			}
			if (SmartGWTUtil.isNotEmpty(info.getMultiLangFieldName())) {
				List<LocalizedStringDefinition> localizedList = (List<LocalizedStringDefinition>) pane
						.getValue(info.getMultiLangFieldName());
				((MetaDataLangTextItem) item).setLocalizedList(localizedList);
			}
		} else if (info.getInputType() == InputType.SCRIPT) {
			CanvasItem canvasItem = new MetaFieldCanvasItem();
			canvasItem.setCanvas(new ScriptItemPane(pane, info));
			canvasItem.setColSpan(2);
			item = canvasItem;
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

	private final class ScriptItemPane extends VLayout {

		private DynamicForm form;

		private TextAreaItem txtScript;
		private IButton btnScript;

		public ScriptItemPane(final MetaFieldSettingPane pane, final FieldInfo info) {
			setAutoHeight();
			setWidth100();

			form = new DynamicForm();
			form.setWidth100();
			form.setNumCols(2);
			form.setColWidths(MtpWidgetConstants.FORM_WIDTH_ITEM, "*");

			txtScript = new MtpTextAreaItem();
			txtScript.setShowTitle(false);
			txtScript.setWidth("100%");
			txtScript.setHeight(55);
			SmartGWTUtil.setReadOnlyTextArea(txtScript);
			txtScript.setColSpan(2);
			String description = pane.getDescription(info);
			if (SmartGWTUtil.isNotEmpty(description)) {
				SmartGWTUtil.addHoverToFormItem(txtScript, description);
			}

			form.setItems(txtScript);

			String displayName = pane.getDisplayName(info);
			final String title = info.isDeprecated() ? "<del>" + displayName + "</del>" : displayName;
			final String scriptHint = pane.getScriptHint(info);

			btnScript = new IButton();
			btnScript.setTitle("Script");
			btnScript.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					Object value = pane.getValue(info.getName());
					String strValue = value != null ? value.toString() : "";
					MetaDataUtil.showScriptEditDialog(
							ScriptEditorDialogMode.getMode(info.getMode()),
							strValue, title, null, scriptHint, new ScriptEditorDialogHandler() {

								@Override
								public void onSave(String text) {
									txtScript.setValue(text);
									pane.setValue(info.getName(), text);
								}

								@Override
								public void onCancel() {
								}
							});
				}

			});

			if (pane.getValue(info.getName()) != null) {
				txtScript.setValue(pane.getValueAs(String.class, info.getName()));
			}

			addMember(form);
			addMember(btnScript);
		}
	}
}
