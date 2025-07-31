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

package org.iplass.adminconsole.client.metadata.ui.webapi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;

import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;

/**
 * Result レコード編集ダイアログ
 */
public class ResultEditDialog extends MtpDialog {
	/** Attribute Name フィールド */
	private TextItem nameField;
	/** Data Type フィールド*/
	private ComboBoxItem dataTypeField;

	/** データ変更ハンドラ */
	private List<DataChangedHandler> handlers = new ArrayList<>();

	/**
	 * コンストラクタ
	 */
	public ResultEditDialog() {

		setHeight(180);
		setTitle("Result setting");
		centerInPage();

		nameField = new MtpTextItem("result", "Attribute name");
		SmartGWTUtil.setRequired(nameField);

		dataTypeField = new ComboBoxItem("dataType", "Data Type");
		dataTypeField.setValueMap(getDataTypeFieldValueMap());

		final DynamicForm form = new MtpForm();
		form.setItems(nameField, dataTypeField);

		container.addMember(form);

		IButton save = new IButton("OK");
		save.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (form.validate()){
					editComplete();
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

	/**
	 * 本ダイアログで編集する値を設定します。
	 * @param value ダイアログ設定値
	 */
	public void setDialogValue(DialogValue value) {
		nameField.setValue(value.getName());
		dataTypeField.setValue(value.getDataType());
	}

	/**
	 * {@link DataChangedHandler} を追加します。
	 * <p>
	 * ここで設定したハンドラは、OKボタンが押下された時に実行されます。<br>
	 * 編集したデータは {@link DataChangedEvent#getValueObject(Class)} で取得します。<br>
	 * データ型は {@link ResultEditDialog.DialogValue} です。<br>
	 * </p>
	 *
	 * @param handler {@link DataChangedHandler}
	 */
	public void addDataChangeHandler(DataChangedHandler handler) {
		handlers.add(0, handler);
	}

	private Map<String, String> getDataTypeFieldValueMap() {
		Map<String, String> valueMap = new HashMap<>();
		valueMap.put("java.lang.String", "String");
		valueMap.put("java.lang.Long", "Number");
		valueMap.put("java.lang.Boolean", "Boolean");
		return valueMap;
	}

	/**
	 * 編集完了時の操作
	 */
	private void editComplete() {
		try {
			String name = SmartGWTUtil.getStringValue(nameField);
			String dataType = SmartGWTUtil.getStringValue(dataTypeField);
			DialogValue value = new DialogValue(name, dataType);

			// イベント通知
			fireDataChanged(value);

		} finally {
			// 通知完了後ダイアログ破棄
			destroy();
		}
	}

	/**
	 * データ変更通知処理
	 */
	private void fireDataChanged(DialogValue value) {
		DataChangedEvent event = new DataChangedEvent();
		event.setValueObject(value);
		for (DataChangedHandler handler : handlers) {
			handler.onDataChanged(event);
		}
	}

	@Override
	protected boolean onPreDestroy() {
		nameField = null;
		dataTypeField = null;

		return super.onPreDestroy();
	}

	/**
	 * ダイアログの編集対象の値のクラス
	 */
	public static class DialogValue implements Serializable {
		/** serialVersionUID */
		private static final long serialVersionUID = 828213328084591496L;

		/** 属性名 */
		private String name;
		/** 属性データ型 */
		private String dataType;

		/**
		 * コンストラクタ
		 * @param name 属性名
		 * @param dataType 属性データ型
		 */
		public DialogValue(String name, String dataType) {
			this.name = name;
			this.dataType = dataType;
		}

		/**
		 * 属性名を取得します。
		 * @return 属性名
		 */
		public String getName() {
			return name;
		}

		/**
		 * 属性データ型を取得します。
		 * @return 属性データ型
		 */
		public String getDataType() {
			return dataType;
		}
	}
}
