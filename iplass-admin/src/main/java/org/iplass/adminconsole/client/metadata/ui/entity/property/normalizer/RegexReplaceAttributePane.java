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

import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.entity.definition.NormalizerDefinition;
import org.iplass.mtp.entity.definition.normalizers.RegexReplace;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;

public class RegexReplaceAttributePane extends NormalizerAttributePane {

	private DynamicForm form;

	/** regex */
	private TextItem txtRegex;

	/** replacement */
	private TextItem txtReplacement;

	public RegexReplaceAttributePane() {

		txtRegex = new MtpTextItem();
		txtRegex.setTitle("Regex");
		SmartGWTUtil.setRequired(txtRegex);
		SmartGWTUtil.addHoverToFormItem(txtRegex, rs("ui_metadata_entity_property_RegexReplaceAttributePane_txtRegex"));

		txtReplacement = new MtpTextItem();
		txtReplacement.setTitle("Replacement");
		SmartGWTUtil.setRequired(txtReplacement);
		SmartGWTUtil.addHoverToFormItem(txtReplacement, rs("ui_metadata_entity_property_RegexReplaceAttributePane_txtReplacement"));

		form = new MtpForm();
		form.setItems(txtRegex, txtReplacement);

		addMember(form);
	}

	@Override
	public void setDefinition(NormalizerDefinition definition) {
		if (definition instanceof RegexReplace) {
			txtRegex.setValue(((RegexReplace)definition).getRegex());
			txtReplacement.setValue(((RegexReplace)definition).getReplacement());
		} else {
			form.clearValues();
		}
	}

	@Override
	public NormalizerDefinition getEditDefinition(NormalizerDefinition definition) {
		RegexReplace newOne = new RegexReplace();
		newOne.setRegex(SmartGWTUtil.getStringValue(txtRegex));
		newOne.setReplacement(SmartGWTUtil.getStringValue(txtReplacement, true));
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
		return 80;
	}

}
