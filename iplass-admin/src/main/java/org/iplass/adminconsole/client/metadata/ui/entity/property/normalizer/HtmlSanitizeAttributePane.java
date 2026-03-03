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

import java.util.LinkedHashMap;

import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpSelectItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.entity.definition.NormalizerDefinition;
import org.iplass.mtp.entity.definition.normalizers.HtmlSanitize;
import org.iplass.mtp.entity.definition.normalizers.HtmlSanitizePolicy;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;

public class HtmlSanitizeAttributePane extends NormalizerAttributePane {

	private DynamicForm form;

	/** サニタイズポリシー */
	private SelectItem selPolicy;

	public HtmlSanitizeAttributePane() {

		selPolicy = new MtpSelectItem();
		selPolicy.setTitle("Sanitize Policy");
		LinkedHashMap<String, String> policyMap = new LinkedHashMap<>();
		for (HtmlSanitizePolicy policy : HtmlSanitizePolicy.values()) {
			policyMap.put(policy.name(), policy.name());
		}
		selPolicy.setValueMap(policyMap);
		SmartGWTUtil.setRequired(selPolicy);
		SmartGWTUtil.addHoverToFormItem(selPolicy, rs("ui_metadata_entity_property_HtmlSanitizeAttributePane_selPolicy"));

		form = new MtpForm();
		form.setItems(selPolicy);

		addMember(form);
	}

	@Override
	public void setDefinition(NormalizerDefinition definition) {
		if (definition instanceof HtmlSanitize) {
			HtmlSanitizePolicy policy = ((HtmlSanitize) definition).getPolicy();
			if (policy != null) {
				selPolicy.setValue(policy.name());
			} else {
				selPolicy.clearValue();
			}
		} else {
			form.clearValues();
		}
	}

	@Override
	public NormalizerDefinition getEditDefinition(NormalizerDefinition definition) {
		HtmlSanitize newOne = new HtmlSanitize();
		newOne.setPolicy(HtmlSanitizePolicy.valueOf(SmartGWTUtil.getStringValue(selPolicy)));
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
