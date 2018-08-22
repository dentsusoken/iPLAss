/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.common.LocalizedStringSettingDialog;
import org.iplass.mtp.definition.LocalizedStringDefinition;

import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;

public class SelectValueEditDialog extends AbstractWindow {

	private boolean _isNewRow = false;
	private boolean _isReadOnly = false;
	private SelectValueListGridRecord _target;
	private ListGrid _parentGrid;

	private TextItem _valueItem;
	private TextItem _dispNameItem;

	private ButtonItem langBtn;

	private List<LocalizedStringDefinition> _localizedDisplayNameList;

	public SelectValueEditDialog(boolean isNewRow, ListGrid parentGrid, SelectValueListGridRecord target, boolean isReadOnly) {
		_isNewRow = isNewRow;
		_parentGrid = parentGrid;
		_target = target;
		_isReadOnly = isReadOnly;
		_localizedDisplayNameList = target.getLocalizedDisplayNameList();

		initialize();
		dataInitialize();
	}

	private void initialize() {
		// ダイアログ本体のプロパティ設定
		setWidth(370);
		setMaxWidth(370);
		setHeight(140);
		setMaxHeight(140);
		if (_isReadOnly) {
			setTitle("Select Value (Read Only)");
		} else {
			setTitle("Select Value");
		}
		setShowMinimizeButton(false);
		setIsModal(true);
		setShowModalMask(true);
		setCanDragResize(true);
		centerInPage();

		_valueItem = new TextItem();
		_valueItem.setTitle("Value");
		SmartGWTUtil.setRequired(_valueItem);
		_dispNameItem = new TextItem();
		_dispNameItem.setTitle("Display Name");
		SmartGWTUtil.setRequired(_dispNameItem);

		langBtn = new ButtonItem("addDisplayName", "Languages");
		langBtn.setShowTitle(false);
		langBtn.setIcon("world.png");
		langBtn.setStartRow(false);	//これを指定しないとButtonの場合、先頭にくる
		langBtn.setEndRow(false);	//これを指定しないと次のFormItemが先頭にいく
		langBtn.setPrompt(AdminClientMessageUtil.getString("ui_metadata_entity_PropertyListGrid_eachLangDspName"));
		langBtn.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {

				if (_localizedDisplayNameList == null) {
					_localizedDisplayNameList = new ArrayList<LocalizedStringDefinition>();
				}
				LocalizedStringSettingDialog dialog = new LocalizedStringSettingDialog(_localizedDisplayNameList);
				dialog.show();

			}
		});

		final DynamicForm form = new DynamicForm();
		form.setMargin(5);
		form.setNumCols(3);
		form.setWidth100();
		form.setHeight(25);
		form.setItems(_valueItem, _dispNameItem, langBtn);

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

		HLayout btnLayout = new HLayout(5);
		btnLayout.setMargin(5);
		btnLayout.setHeight(20);
		btnLayout.setWidth100();
		btnLayout.setAlign(VerticalAlignment.CENTER);
		btnLayout.addMember(ok);
		btnLayout.addMember(cancel);

		addItem(form);
		addItem(SmartGWTUtil.separator());
		addItem(btnLayout);
	}

	/**
	 * データ初期化
	 */
	private void dataInitialize() {
		_valueItem.setValue(_target.getSelectValue());
		_dispNameItem.setValue(_target.getDispName());
	}

	/**
	 * データ更新
	 */
	private void updateRecordData() {
		if (_valueItem.getValue() != null) {
			_target.setSelectValue(_valueItem.getValue().toString());
		}
		if (_dispNameItem.getValue() != null) {
			_target.setDispName(_dispNameItem.getValue().toString());
		}
		if (_localizedDisplayNameList != null) {
			_target.setLocalizedDisplayNameList(_localizedDisplayNameList);
		}

		_parentGrid.updateData(_target);
		_parentGrid.refreshFields();
	}
}
