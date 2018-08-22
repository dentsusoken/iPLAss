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

package org.iplass.adminconsole.client.metadata.ui.asynccommand;

import org.iplass.adminconsole.client.base.data.ValueMapUtil;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.data.queue.QueueListDS;
import org.iplass.mtp.async.ExceptionHandlingMode;
import org.iplass.mtp.command.async.definition.AsyncCommandDefinition;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;

/**
 * AsyncCommand属性編集パネル
 *
 */
public class AsyncCommandAttributePane extends HLayout {

	/** フォーム */
	private DynamicForm form;

	/** ExceptionHandlingMode設定 */
	private SelectItem exceptionHandlingModeField;

	/** QueueName設定 */
	private SelectItem queueNameField;

	/** GroupingKeyAttributeName設定 */
	private TextItem groupingKeyAttributeNameField;

	/**
	 * コンストラクタ
	 */
	public AsyncCommandAttributePane() {

		setHeight(20);
		setMargin(5);

		//入力部分
		form = new DynamicForm();
		form.setWidth100();
		form.setNumCols(8);
//		form.setColWidths(120, "*", 120, "*", 120, "*", "*");
		form.setColWidths(120, "*", 130, "*", 130, "*", "*", "*");

		exceptionHandlingModeField = new SelectItem("ExceptionHandlingMode", "Exception Handling Mode");
		exceptionHandlingModeField.setWidth(150);
		exceptionHandlingModeField.setValueMap(ValueMapUtil.getExceptionHandlingModeValueMap(false));

		queueNameField = new SelectItem("queueName", "Queue Name");
		queueNameField.setWidth(150);
		QueueListDS.setDataSource(queueNameField, true);

		groupingKeyAttributeNameField = new TextItem("groupingKeyAttributeName", "GroupingKey Attribute Name");

		form.setItems(exceptionHandlingModeField, queueNameField, groupingKeyAttributeNameField);

		//配置
		addMember(form);

	}

	/**
	 * AsyncCommandを展開します。
	 *
	 * @param definition AsyncCommandDefinition
	 */
	public void setDefinition(AsyncCommandDefinition definition) {

		if (definition.getExceptionHandlingMode() != null) {
			exceptionHandlingModeField.setValue(definition.getExceptionHandlingMode().name());
		} else {
			exceptionHandlingModeField.setValue("");
		}
		if (definition.getQueue() != null) {
			queueNameField.setValue(definition.getQueue());
		} else {
			queueNameField.setValue("");
		}
		if (definition.getGroupingKeyAttributeName() != null) {
			groupingKeyAttributeNameField.setValue(definition.getGroupingKeyAttributeName());
		} else {
			groupingKeyAttributeNameField.setValue("");
		}
	}

	/**
	 * 編集されたAsyncCommandDefinition情報を返します。
	 *
	 * @return 編集AsyncCommandDefinition情報
	 */
	public AsyncCommandDefinition getEditDefinition(AsyncCommandDefinition definition) {

		if (exceptionHandlingModeField.getValue() != null && !exceptionHandlingModeField.getValueAsString().isEmpty()) {
			definition.setExceptionHandlingMode(ExceptionHandlingMode.valueOf(SmartGWTUtil.getStringValue(exceptionHandlingModeField)));
		} else {
			definition.setExceptionHandlingMode(null);
		}
		if (queueNameField.getValue() != null && !queueNameField.getValueAsString().isEmpty()) {
			definition.setQueue(SmartGWTUtil.getStringValue(queueNameField));
		} else {
			definition.setQueue(null);
		}
		if (groupingKeyAttributeNameField.getValue() != null && !groupingKeyAttributeNameField.getValueAsString().isEmpty()) {
			definition.setGroupingKeyAttributeName(SmartGWTUtil.getStringValue(groupingKeyAttributeNameField));
		} else {
			definition.setGroupingKeyAttributeName(null);
		}

		return definition;
	}

	/**
	 * 入力チェックを実行します。
	 *
	 * @return 入力チェック結果
	 */
	public boolean validate() {
		return form.validate();
	}
}
