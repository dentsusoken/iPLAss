/*
 * Copyright (C) 2026 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.entity.property.normalizer;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogConstants;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogHandler;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogMode;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpSelectItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextAreaItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.MetaDataUtil;
import org.iplass.mtp.entity.definition.NormalizerDefinition;
import org.iplass.mtp.entity.definition.normalizers.HtmlSanitizer;
import org.iplass.mtp.entity.definition.normalizers.SafelistType;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;

public class HtmlSanitizeAttributePane extends NormalizerAttributePane {

	private static final SafelistType DEFAULT_TYPE = SafelistType.BASIC;

	private final DynamicForm form;
	private final SelectItem selSafelistType;
	private final TextAreaItem txaCustomizeScript;

	public HtmlSanitizeAttributePane() {

		setHeight100();

		selSafelistType = createSafelistTypeSelect();
		ButtonItem editScript = createEditScriptButton();
		txaCustomizeScript = createCustomizeScriptArea();

		form = new MtpForm();
		form.setHeight100();
		form.setItems(selSafelistType, editScript, txaCustomizeScript);

		addMember(form);
	}

	@Override
	public void setDefinition(NormalizerDefinition definition) {
		if (!(definition instanceof HtmlSanitizer)) {
			form.clearValues();
			selSafelistType.setValue(DEFAULT_TYPE.name());
			return;
		}

		HtmlSanitizer impl = (HtmlSanitizer) definition;

		selSafelistType.setValue(impl.getSafelistType().name());

		String script = impl.getCustomizeScript();
		if (script != null && !script.isEmpty()) {
			txaCustomizeScript.setValue(script);
		} else {
			txaCustomizeScript.clearValue();
		}
	}

	@Override
	public NormalizerDefinition getEditDefinition(NormalizerDefinition definition) {
		HtmlSanitizer newOne = new HtmlSanitizer();

		String typeName = SmartGWTUtil.getStringValue(selSafelistType, true);
		newOne.setSafelistType(typeName != null ? SafelistType.valueOf(typeName) : DEFAULT_TYPE);
		newOne.setCustomizeScript(SmartGWTUtil.getStringValue(txaCustomizeScript, true));

		return newOne;
	}

	@Override
	public boolean validate() {
		return form.validate();
	}

	@Override
	public void clearErrors() {
		form.clearErrors(true);
	}

	@Override
	public int panelHeight() {
		return 200;
	}

	private SelectItem createSafelistTypeSelect() {
		SelectItem select = new MtpSelectItem();
		select.setTitle("Safelist");
		select.setWidth("100%");

		LinkedHashMap<String, String> typeMap = Arrays.stream(SafelistType.values())
				.collect(Collectors.toMap(
						SafelistType::name,
						SafelistType::name,
						(a, b) -> a,
						LinkedHashMap::new));
		select.setValueMap(typeMap);
		select.setDefaultValue(DEFAULT_TYPE.name());
		SmartGWTUtil.addHoverToFormItem(select,
				rs("ui_metadata_entity_property_HtmlSanitizeAttributePane_selSafelist"));

		return select;
	}

	private ButtonItem createEditScriptButton() {
		ButtonItem editScript = new ButtonItem("editScript", "Edit");
		editScript.setWidth(100);
		editScript.setStartRow(false);
		editScript.setColSpan(3);
		editScript.setAlign(Alignment.RIGHT);
		editScript.setPrompt(SmartGWTUtil.getHoverString(
				rs("ui_metadata_entity_property_HtmlSanitizeAttributePane_editScript")));
		editScript.addClickHandler(event -> {
			MetaDataUtil.showScriptEditDialog(ScriptEditorDialogMode.GROOVY_SCRIPT,
					SmartGWTUtil.getStringValue(txaCustomizeScript),
					ScriptEditorDialogConstants.ENTITY_NORMALIZER,
					null,
					rs("ui_metadata_entity_property_HtmlSanitizeAttributePane_scriptHint"),
					new ScriptEditorDialogHandler() {
						@Override
						public void onSave(String text) {
							txaCustomizeScript.setValue(text);
						}

						@Override
						public void onCancel() {
						}
					});
		});

		return editScript;
	}

	private TextAreaItem createCustomizeScriptArea() {
		TextAreaItem area = new MtpTextAreaItem();
		area.setColSpan(2);
		area.setTitle("Customize Script");
		area.setHeight("100%");
		SmartGWTUtil.setReadOnlyTextArea(area);
		SmartGWTUtil.addHoverToFormItem(area,
				rs("ui_metadata_entity_property_HtmlSanitizeAttributePane_txaCustomizeScript"));

		return area;
	}
}
