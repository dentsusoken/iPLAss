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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.entity.definition.NormalizerDefinition;
import org.iplass.mtp.entity.definition.normalizers.HtmlSanitizer;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;

public class HtmlSanitizeAttributePane extends NormalizerAttributePane {

	/** タグの区切り文字 */
	private static final String TAG_DELIMITER = ",";

	/** タグ分割用の正規表現パターン（区切り文字および空白） */
	private static final String TAG_SPLIT_PATTERN = "[" + TAG_DELIMITER + "\\s]+";

	private final DynamicForm form;

	/** 許可タグ */
	private final TextItem txtAllowTags;

	public HtmlSanitizeAttributePane() {

		txtAllowTags = new MtpTextItem();
		txtAllowTags.setTitle("Allow Tags");
		txtAllowTags.setWidth("100%");
		SmartGWTUtil.addHoverToFormItem(txtAllowTags,
				rs("ui_metadata_entity_property_HtmlSanitizeAttributePane_txtAllowTags"));

		form = new MtpForm();
		form.setItems(txtAllowTags);

		addMember(form);
	}

	@Override
	public void setDefinition(NormalizerDefinition definition) {
		if (!(definition instanceof HtmlSanitizer)) {
			form.clearValues();
			return;
		}

		List<String> tags = ((HtmlSanitizer) definition).getAllowTags();
		if (tags.isEmpty()) {
			txtAllowTags.clearValue();
			return;
		}

		txtAllowTags.setValue(String.join(TAG_DELIMITER, tags));
	}

	@Override
	public NormalizerDefinition getEditDefinition(NormalizerDefinition definition) {
		String value = SmartGWTUtil.getStringValue(txtAllowTags, true);
		if (value == null || value.isEmpty()) {
			return new HtmlSanitizer(Collections.emptyList());
		}
		List<String> tags = Arrays.stream(value.split(TAG_SPLIT_PATTERN))
				.map(String::trim)
				.filter(tag -> !tag.isEmpty())
				.collect(Collectors.toList());
		return new HtmlSanitizer(tags);
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
