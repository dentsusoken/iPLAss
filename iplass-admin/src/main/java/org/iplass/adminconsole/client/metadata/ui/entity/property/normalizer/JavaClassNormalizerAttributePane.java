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
import org.iplass.mtp.entity.definition.normalizers.JavaClassNormalizer;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;

public class JavaClassNormalizerAttributePane extends NormalizerAttributePane {

	private DynamicForm form;

	private TextItem txtClassName;
	private CheckboxItem chkBindAsArray;

	public JavaClassNormalizerAttributePane() {

		txtClassName = new MtpTextItem();
		txtClassName.setTitle(rs("ui_metadata_entity_property_normalizer_JavaClassNormalizerAttributePane_javaClassName"));
		SmartGWTUtil.setRequired(txtClassName);
		SmartGWTUtil.addHoverToFormItem(txtClassName, rs("ui_metadata_entity_property_JavaClassNormalizerAttributePane_javaClassComment"));

		chkBindAsArray = new CheckboxItem();
		chkBindAsArray.setTitle(rs("ui_metadata_entity_property_normalizer_JavaClassNormalizerAttributePane_bindAsArray"));
		SmartGWTUtil.addHoverToFormItem(chkBindAsArray, rs("ui_metadata_entity_property_JavaClassNormalizerAttributePane_javaClassAsArray"));

		form = new MtpForm();
		form.setItems(txtClassName, chkBindAsArray);

		addMember(form);
	}

	@Override
	public void setDefinition(NormalizerDefinition definition) {
		if (definition instanceof JavaClassNormalizer) {
			txtClassName.setValue(((JavaClassNormalizer)definition).getClassName());
			chkBindAsArray.setValue(((JavaClassNormalizer)definition).isAsArray());
		} else {
			form.clearValues();
		}
	}

	@Override
	public NormalizerDefinition getEditDefinition(NormalizerDefinition definition) {
		JavaClassNormalizer newOne = new JavaClassNormalizer();
		newOne.setClassName(SmartGWTUtil.getStringValue(txtClassName));
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
		return 80;
	}

}
