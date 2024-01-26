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

package org.iplass.adminconsole.client.metadata.ui.top.item;

import org.iplass.adminconsole.client.base.event.MTPEvent;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataLangTextItem;
import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.top.PartsOperationHandler;
import org.iplass.mtp.view.top.parts.PreviewDateParts;

import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;

public class PreviewDateItem extends PartsItem {

	private PartsOperationHandler controler;

	private PreviewDateParts parts;

	/**
	 * コンストラクタ
	 */
	public PreviewDateItem(PreviewDateParts parts, PartsOperationHandler controler) {
		this.parts = parts;
		this.controler = controler;
		setTitle("Preview Date");
		setBackgroundColor("#909090");
	}

	@Override
	public PreviewDateParts getParts() {
		return parts;
	}

	@Override
	protected void onOpen() {
		PreviewDatePartsItemSettingDialog dialog = new PreviewDatePartsItemSettingDialog();
		dialog.show();
	}

	@Override
	protected boolean onPreDestroy() {
		MTPEvent e = new MTPEvent();
		e.setValue("key", dropAreaType + "_" + PreviewDateParts.class.getName() + "_");
		controler.remove(e);
		return true;
	}

	private class PreviewDatePartsItemSettingDialog extends MtpDialog {

		private MetaDataLangTextItem txtTitle;
		private CheckboxItem chkUsePreviewDate;

		/**
		 * コンストラクタ
		 */
		public PreviewDatePartsItemSettingDialog() {

			setTitle("Preview Date");
			setHeight(200);
			centerInPage();

			final DynamicForm form = new MtpForm();
			form.setAutoFocus(true);

			txtTitle = new MetaDataLangTextItem();
			txtTitle.setTitle("Title");
			txtTitle.setValue(parts.getTitle());
			txtTitle.setLocalizedList(parts.getLocalizedTitleList());

			chkUsePreviewDate = new CheckboxItem();
			chkUsePreviewDate.setTitle("Use Preview Date");
			chkUsePreviewDate.setValue(parts.isUsePreviewDate());
			SmartGWTUtil.addHoverToFormItem(chkUsePreviewDate,
					AdminClientMessageUtil.getString("ui_metadata_top_item_PreviewDatePartsItem_usePreviewDate"));

			form.setItems(txtTitle, chkUsePreviewDate);

			container.addMember(form);

			IButton save = new IButton("OK");
			save.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if (form.validate()){
						//入力情報をパーツに
						parts.setTitle(SmartGWTUtil.getStringValue(txtTitle));
						parts.setLocalizedTitleList(txtTitle.getLocalizedList());
						parts.setUsePreviewDate(SmartGWTUtil.getBooleanValue(chkUsePreviewDate));
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
}
