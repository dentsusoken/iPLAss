/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.selectvalue;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataLangTextItem;
import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class SelectValueEditDialog extends MtpDialog {

	private boolean _isNewRow = false;
	private boolean _isReadOnly = false;
	private SelectValueListGridRecord _target;
	private ListGrid _parentGrid;

	private TextItem _valueItem;
	private MetaDataLangTextItem _dispNameItem;

	public SelectValueEditDialog(boolean isNewRow, ListGrid parentGrid, SelectValueListGridRecord target, boolean isReadOnly) {
		_isNewRow = isNewRow;
		_parentGrid = parentGrid;
		_target = target;
		_isReadOnly = isReadOnly;

		initialize();
		dataInitialize();
	}

	private void initialize() {

		setHeight(200);

		if (_isReadOnly) {
			setTitle("Select Value (Read Only)");
		} else {
			setTitle("Select Value");
		}
		centerInPage();

		_valueItem = new MtpTextItem();
		_valueItem.setTitle(AdminClientMessageUtil.getString("ui_metadata_selectvalue_SelectValueEditDialog_value"));
		SmartGWTUtil.setRequired(_valueItem);

		_dispNameItem = new MetaDataLangTextItem();
		_dispNameItem.setTitle(AdminClientMessageUtil.getString("ui_metadata_selectvalue_SelectValueEditDialog_displayName"));
		SmartGWTUtil.setRequired(_dispNameItem);

		final DynamicForm form = new MtpForm();
		form.setItems(_valueItem, _dispNameItem);

		container.addMember(form);

		IButton ok = new IButton("OK");
		ok.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (!form.validate()) {
					return;
				}

				String value = _valueItem.getValue().toString();

				//重複チェック
				if (_isNewRow && _parentGrid.getRecords() != null && _parentGrid.getRecords().length > 0) {
					for (ListGridRecord record : _parentGrid.getRecords()) {
						SelectValueListGridRecord sRecord = (SelectValueListGridRecord) record;
						if (sRecord.getSelectValue().equals(value)) {
							SC.say(AdminClientMessageUtil.getString("ui_metadata_entity_PropertyListGrid_sameValueExists") + "[" + value + "]");
							return;
						}
					}
				}
				if (_isNewRow) {
					_parentGrid.addData(_target);
				}
				updateRecordData();
				destroy();
			}
		});
		if (_isReadOnly) {
			ok.setDisabled(true);
		}
		IButton cancel = new IButton("Cancel");
		cancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				destroy();
			}
		});

		footer.setMembers(ok, cancel);
	}

	/**
	 * データ初期化
	 */
	private void dataInitialize() {
		_valueItem.setValue(_target.getSelectValue());
		_dispNameItem.setValue(_target.getDispName());
		_dispNameItem.setLocalizedList(_target.getLocalizedDisplayNameList());
	}

	/**
	 * データ更新
	 */
	private void updateRecordData() {
		_target.setSelectValue(SmartGWTUtil.getStringValue(_valueItem, true));
		_target.setDispName(SmartGWTUtil.getStringValue(_dispNameItem, true));
		_target.setLocalizedDisplayNameList(_dispNameItem.getLocalizedList());

		_parentGrid.updateData(_target);
		_parentGrid.refreshFields();
	}
}
