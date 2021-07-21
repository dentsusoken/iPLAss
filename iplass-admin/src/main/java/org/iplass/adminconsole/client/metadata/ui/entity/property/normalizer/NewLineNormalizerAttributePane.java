/*
 * Copyright (C) 2021 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpSelectItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.entity.definition.NormalizerDefinition;
import org.iplass.mtp.entity.definition.normalizers.NewlineNormalizer;
import org.iplass.mtp.entity.definition.normalizers.NewlineType;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;

public class NewLineNormalizerAttributePane extends NormalizerAttributePane {

	private DynamicForm form;

	/** 改行文字 */
	private SelectItem selNewLineType;

	public NewLineNormalizerAttributePane() {

		selNewLineType = new MtpSelectItem();
		selNewLineType.setTitle("Line Breaks Code");
		LinkedHashMap<String, String> newLineTypeMap = new LinkedHashMap<>();
		for (NewlineType type : NewlineType.values()) {
			newLineTypeMap.put(type.name(), type.name());
		}
		selNewLineType.setValueMap(newLineTypeMap);
		SmartGWTUtil.setRequired(selNewLineType);
		SmartGWTUtil.addHoverToFormItem(selNewLineType, rs("ui_metadata_entity_property_NewLineNormalizerAttributePane_selNewLineType"));

		form = new MtpForm();
		form.setItems(selNewLineType);

		addMember(form);
	}

	@Override
	public void setDefinition(NormalizerDefinition definition) {
		if (definition instanceof NewlineNormalizer) {
			NewlineType newLineType = ((NewlineNormalizer)definition).getType();
			if (newLineType != null) {
				selNewLineType.setValue(newLineType.name());
			} else {
				selNewLineType.clearValue();
			}
		} else {
			form.clearValues();
		}
	}

	@Override
	public NormalizerDefinition getEditDefinition(NormalizerDefinition definition) {
		NewlineNormalizer newOne = new NewlineNormalizer();
		newOne.setType(NewlineType.valueOf(SmartGWTUtil.getStringValue(selNewLineType)));
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
