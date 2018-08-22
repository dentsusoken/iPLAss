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

package org.iplass.adminconsole.client.metadata.ui.entity.filter;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.view.filter.EntityFilterItem;

import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * フィルタアイテム編集ダイアログ
 *
 */
public class EntityFilterItemDialog extends AbstractWindow {

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
		setWidth(450);
		setHeight(170);
		setShowMinimizeButton(false);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();

		DynamicForm form = new DynamicForm();
		form.setPadding(5);
		form.setWidth100();
		form.setNumCols(2);
		form.setColWidths(100, "*");
		form.setAutoFocus(true);

		nameField = new TextItem("name", AdminClientMessageUtil.getString("ui_metadata_entity_filter_EntityFilterItemDialog_name"));
		nameField.setWidth(300);
		nameField.setSelectOnFocus(true);
		SmartGWTUtil.setRequired(nameField);

		displayNameField = new TextItem("displayName", AdminClientMessageUtil.getString("ui_metadata_entity_filter_EntityFilterItemDialog_dispName"));
		displayNameField.setWidth(300);
		form.setItems(nameField, displayNameField);

		//Mainコンテンツ
		VLayout contents = new VLayout(5);
		contents.setPadding(10);
		contents.setMembers(form);

		HLayout footer = new HLayout(5);
		footer.setMargin(10);
		footer.setAutoHeight();
		footer.setWidth100();
		footer.setAlign(VerticalAlignment.CENTER);

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

		addItem(contents);
		addItem(SmartGWTUtil.separator());
		addItem(footer);
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
