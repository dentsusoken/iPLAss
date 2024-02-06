/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.entity.filter;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.view.filter.EntityFilterItem;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * フィルタアイテム編集ダイアログ
 *
 */
public class EntityFilterItemDialog extends MtpDialog {

	private ListGrid parent;

	/** 名前 */
	private TextItem nameField;
	/** 表示名 */
	private TextItem displayNameField;

	/**
	 * コンストラクタ
	 *
	 * @param category カテゴリ
	 */
	public EntityFilterItemDialog(ListGrid parent) {
		this.parent = parent;

		setTitle(AdminClientMessageUtil.getString("ui_metadata_entity_filter_EntityFilterItemDialog_title"));
		setHeight(170);
		centerInPage();

		DynamicForm form = new MtpForm();
		form.setAutoFocus(true);

		nameField = new MtpTextItem("name", AdminClientMessageUtil.getString("ui_metadata_entity_filter_EntityFilterItemDialog_name"));
		SmartGWTUtil.setRequired(nameField);

		displayNameField = new MtpTextItem("displayName", AdminClientMessageUtil.getString("ui_metadata_entity_filter_EntityFilterItemDialog_dispName"));
		form.setItems(nameField, displayNameField);

		container.addMember(form);

		IButton add = new IButton(AdminClientMessageUtil.getString("ui_metadata_entity_filter_EntityFilterItemDialog_add"));
		add.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (form.validate()){
					add();
				}
			}
		});

		IButton cancel = new IButton(AdminClientMessageUtil.getString("ui_metadata_entity_filter_EntityFilterItemDialog_cancel"));
		cancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				destroy();
			}
		});

		footer.setMembers(add, cancel);
	}

	/**
	 * 保存処理
	 */
	private void add() {
		String name = nameField.getValueAsString();
		for (ListGridRecord record : parent.getRecords()) {
			EntityFilterItem item = (EntityFilterItem) record.getAttributeAsObject("valueObject");
			if (name.equals(item.getName())) {
				SC.warn(AdminClientMessageUtil.getString("ui_metadata_entity_filter_EntityFilterItemDialog_itemAlreadyExists"));
				return;
			}
		}

		EntityFilterItem item = new EntityFilterItem();
		item.setName(name);
		item.setDisplayName(displayNameField.getValueAsString());
		ListGridRecord record = item2Record(item);
		parent.addData(record);
		destroy();
	}


	private ListGridRecord item2Record(EntityFilterItem item) {
		ListGridRecord record = new ListGridRecord();
		record.setAttribute("key", item.getName());
		record.setAttribute("name", item.getName());
		record.setAttribute("displayName", item.getDisplayName());
		record.setAttribute("condition", "");
		record.setAttribute("valueObject", item);
		return record;
	}

}
