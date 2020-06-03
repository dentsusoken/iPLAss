/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import org.iplass.adminconsole.client.base.event.MTPEvent;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.top.PartsOperationHandler;
import org.iplass.mtp.view.top.parts.CalendarParts;

import com.smartgwt.client.types.HeaderControls;
import com.smartgwt.client.widgets.HeaderControl;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;

/**
 *
 * @author lis3wg
 */
public class CalendarItem extends PartsItem {

	private PartsOperationHandler controler;
	private CalendarParts parts;

	public CalendarItem(CalendarParts parts, PartsOperationHandler controler) {
		this.parts = parts;
		this.controler = controler;
		setBackgroundColor("#FFBBFF");
		setTitle("Calendar(" + parts.getCalendarName() + ")");

		setHeaderControls(HeaderControls.HEADER_LABEL, new HeaderControl(HeaderControl.SETTINGS, new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				CalendarItemSettingDialog dialog = new CalendarItemSettingDialog();
				dialog.show();
			}
		}), HeaderControls.CLOSE_BUTTON);
	}

	@Override
	public CalendarParts getParts() {
		return parts;
	}

	@Override
	protected boolean onPreDestroy() {
		MTPEvent e = new MTPEvent();
		e.setValue("key", dropAreaType + "_" + CalendarParts.class.getName() + "_" + parts.getCalendarName());
		controler.remove(e);
		return true;
	}

	private class CalendarItemSettingDialog extends MtpDialog {

		private TextItem iconTagField;
		private TextItem styleField;

		/**
		 * コンストラクタ
		 */
		public CalendarItemSettingDialog() {

			setTitle("Calendar(" + parts.getCalendarName() + ")");
			setHeight(200);
			centerInPage();

			final DynamicForm form = new MtpForm();
			form.setAutoFocus(true);

			iconTagField = new MtpTextItem("iconTag", "Icon Tag");
			iconTagField.setValue(parts.getIconTag());
			SmartGWTUtil.addHoverToFormItem(iconTagField, AdminClientMessageUtil.getString("ui_metadata_top_item_CalendarItem_iconTagComment"));
			
			styleField = new MtpTextItem("style", "Class");
			styleField.setValue(parts.getStyle());
			SmartGWTUtil.addHoverToFormItem(styleField, AdminClientMessageUtil.getString("ui_metadata_top_item_TopViewContentParts_styleDescriptionKey"));

			form.setItems(iconTagField, styleField);

			container.addMember(form);

			IButton save = new IButton("OK");
			save.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if (form.validate()){
						//入力情報をパーツに
						parts.setIconTag(SmartGWTUtil.getStringValue(iconTagField));
						parts.setStyle(SmartGWTUtil.getStringValue(styleField));
						destroy();
					}
				}
			});

			IButton cancel = new IButton("Cancel");
			cancel.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					destroy();
				}
			});

			footer.setMembers(save, cancel);
		}
	}

}
