/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.adminconsole.client.metadata.ui.top.item;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.view.top.parts.TopViewContentParts;

import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.IntegerItem;
import com.smartgwt.client.widgets.form.fields.TextItem;

public class SeparatorItemSettingDialog extends MtpDialog {
	private TextItem styleField;
	
	private IntegerItem maxHeightField;

	/**
	 * コンストラクタ
	 */
	public SeparatorItemSettingDialog(TopViewContentParts parts) {

		setTitle("Separator");
		setHeight(180);
		centerInPage();

		final DynamicForm form = new MtpForm();
		form.setAutoFocus(true);

		styleField = new MtpTextItem("style", "Class");
		styleField.setValue(parts.getStyle());
		SmartGWTUtil.addHoverToFormItem(styleField, AdminClientMessageUtil.getString("ui_metadata_top_item_TopViewContentParts_styleDescriptionKey"));

		maxHeightField = new IntegerItem("maxHeight", "Max Height");
		maxHeightField.setWidth("100%");
		if (parts.getMaxHeight() != null && parts.getMaxHeight() > 0) {
			maxHeightField.setValue(parts.getMaxHeight());
		}
		SmartGWTUtil.addHoverToFormItem(maxHeightField,
				AdminClientMessageUtil.getString("ui_metadata_top_item_TopViewContentParts_maxHeightDescriptionKey"));

		form.setItems(styleField, maxHeightField);

		container.addMember(form);
	
		IButton save = new IButton("OK");
		save.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (form.validate()){
					//入力情報をパーツに
					parts.setStyle(SmartGWTUtil.getStringValue(styleField));
					parts.setMaxHeight(maxHeightField.getValueAsInteger());
					destroy();
				}
			}
		});
		
		IButton cancel = new IButton("Cancel");
		cancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				destroy();
			}
		});
		
		footer.setMembers(save, cancel);
	}
	
}