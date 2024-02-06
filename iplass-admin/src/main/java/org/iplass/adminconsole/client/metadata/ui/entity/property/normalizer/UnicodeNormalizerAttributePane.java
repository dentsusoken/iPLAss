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

import java.util.LinkedHashMap;

import org.iplass.adminconsole.client.base.ui.widget.form.MtpComboBoxItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.entity.definition.NormalizerDefinition;
import org.iplass.mtp.entity.definition.normalizers.UnicodeNormalizer;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;

public class UnicodeNormalizerAttributePane extends NormalizerAttributePane {

	private DynamicForm form;

	/** Form */
	private ComboBoxItem cboForm;

	public UnicodeNormalizerAttributePane() {

		cboForm = new MtpComboBoxItem();
		cboForm.setTitle("Form");
		LinkedHashMap<String, String> formMap = new LinkedHashMap<>();
		//GWTではjava.text.Normalizer.FormはEmulationできないので、直接
//		for (Form form : Form.values()) {
//			formMap.put(form.name(), form.name());
//		}
		formMap.put("NFD", "NFD");
		formMap.put("NFC", "NFC");
		formMap.put("NFKD", "NFKD");
		formMap.put("NFKC", "NFKC");
		cboForm.setValueMap(formMap);
		SmartGWTUtil.setRequired(cboForm);
		SmartGWTUtil.addHoverToFormItem(cboForm, rs("ui_metadata_entity_property_UnicodeNormalizerAttributePane_cboForm"));

		form = new MtpForm();
		form.setItems(cboForm);

		addMember(form);
	}

	@Override
	public void setDefinition(NormalizerDefinition definition) {
		if (definition instanceof UnicodeNormalizer) {
			String form = ((UnicodeNormalizer)definition).getForm();
			if (SmartGWTUtil.isNotEmpty(form)) {
				cboForm.setValue(form);
			} else {
				cboForm.clearValue();
			}
		} else {
			form.clearValues();
		}
	}

	@Override
	public NormalizerDefinition getEditDefinition(NormalizerDefinition definition) {
		UnicodeNormalizer newOne = new UnicodeNormalizer();
		newOne.setForm(SmartGWTUtil.getStringValue(cboForm));
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
		return 30;
	}

}
