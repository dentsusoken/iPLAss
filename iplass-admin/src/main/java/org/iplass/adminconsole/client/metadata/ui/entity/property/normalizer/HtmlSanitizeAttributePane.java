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

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.entity.definition.NormalizerDefinition;
import org.iplass.mtp.entity.definition.normalizers.HtmlSanitize;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;

public class HtmlSanitizeAttributePane extends NormalizerAttributePane {

	private DynamicForm form;

	/** 許可タグ */
	private TextItem txtAllowTags;

	public HtmlSanitizeAttributePane() {

		txtAllowTags = new MtpTextItem();
		txtAllowTags.setTitle("Allow Tags");
		txtAllowTags.setWidth("100%");
		SmartGWTUtil.addHoverToFormItem(txtAllowTags, rs("ui_metadata_entity_property_HtmlSanitizeAttributePane_txtAllowTags"));

		form = new MtpForm();
		form.setItems(txtAllowTags);

		addMember(form);
	}

	@Override
	public void setDefinition(NormalizerDefinition definition) {
		if (definition instanceof HtmlSanitize) {
			List<String> tags = ((HtmlSanitize) definition).getAllowTags();
			if (tags != null && !tags.isEmpty()) {
				txtAllowTags.setValue(String.join(",", tags));
			} else {
				txtAllowTags.clearValue();
			}
		} else {
			form.clearValues();
		}
	}

	@Override
	public NormalizerDefinition getEditDefinition(NormalizerDefinition definition) {
		HtmlSanitize newOne = new HtmlSanitize();
		String value = SmartGWTUtil.getStringValue(txtAllowTags, true);
		if (value != null && !value.isEmpty()) {
			String[] parts = value.split("[,\\s]+");
			List<String> tags = new ArrayList<>();
			for (String part : parts) {
				String tag = part.trim();
				if (!tag.isEmpty()) {
					tags.add(tag);
				}
			}
			newOne.setAllowTags(tags);
		}
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
