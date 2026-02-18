/*
 * Copyright (C) 2021 DENTSU SOKEN INC. All Rights Reserved.
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

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogConstants;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogHandler;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogMode;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextAreaItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.MetaDataUtil;
import org.iplass.mtp.entity.definition.NormalizerDefinition;
import org.iplass.mtp.entity.definition.normalizers.ScriptingNormalizer;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;

public class ScriptingNormalizerAttributePane extends NormalizerAttributePane {

	private DynamicForm form;

	private TextAreaItem txaScript;
	private CheckboxItem chkBindAsArray;

	public ScriptingNormalizerAttributePane() {

		setHeight100();

		ButtonItem editScript = new ButtonItem("editScript", "Edit");
		editScript.setWidth(100);
		editScript.setStartRow(false);
		editScript.setColSpan(3);
		editScript.setAlign(Alignment.RIGHT);
		editScript.setPrompt(SmartGWTUtil.getHoverString(rs("ui_metadata_entity_property_ScriptingNormalizerAttributePane_displayDialogEditScript")));
		editScript.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				MetaDataUtil.showScriptEditDialog(ScriptEditorDialogMode.GROOVY_SCRIPT,
						SmartGWTUtil.getStringValue(txaScript),
						ScriptEditorDialogConstants.ENTITY_NORMALIZER,
						null,
						rs("ui_metadata_entity_property_ScriptingNormalizerAttributePane_scriptHint"),
						new ScriptEditorDialogHandler() {

							@Override
							public void onSave(String text) {
								txaScript.setValue(text);
							}
							@Override
							public void onCancel() {
							}
						});
			}
		});

		txaScript = new MtpTextAreaItem();
		txaScript.setColSpan(2);
	txaScript.setTitle(AdminClientMessageUtil.getString("ui_metadata_entity_property_normalizer_ScriptingNormalizerAttributePane_script"));
		txaScript.setHeight("100%");
		SmartGWTUtil.setRequired(txaScript);
		SmartGWTUtil.setReadOnlyTextArea(txaScript);

		chkBindAsArray = new CheckboxItem();
		chkBindAsArray.setTitle(AdminClientMessageUtil.getString("ui_metadata_entity_property_normalizer_ScriptingNormalizerAttributePane_bindAsArray"));
		SmartGWTUtil.addHoverToFormItem(chkBindAsArray, rs("ui_metadata_entity_property_ScriptingNormalizerAttributePane_scriptAsArray"));

		form = new MtpForm();
		form.setHeight100();
		form.setItems(editScript, txaScript, chkBindAsArray);

		addMember(form);
	}

	@Override
	public void setDefinition(NormalizerDefinition definition) {
		if (definition instanceof ScriptingNormalizer) {
			txaScript.setValue(((ScriptingNormalizer)definition).getScript());
			chkBindAsArray.setValue(((ScriptingNormalizer)definition).isAsArray());
		} else {
			form.clearValues();
		}
	}

	@Override
	public NormalizerDefinition getEditDefinition(NormalizerDefinition definition) {
		ScriptingNormalizer newOne = new ScriptingNormalizer();
		newOne.setScript(SmartGWTUtil.getStringValue(txaScript, true));
		newOne.setAsArray(SmartGWTUtil.getBooleanValue(chkBindAsArray));
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

}
