/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.action.result;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpSelectItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.action.ResultType;
import org.iplass.mtp.web.actionmapping.definition.result.ResultDefinition;

import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CanvasItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.validator.CustomValidator;

public class ResultEditDialog extends MtpDialog {

	/** Resultの種類選択用Map */
	private static LinkedHashMap<String, String> typeMap;
	static {
		typeMap = new LinkedHashMap<String, String>();
		for (ResultType type : ResultType.values()) {
			typeMap.put(type.name(), type.displayName());
		}
	}

	/** ステータス */
	private TextItem statusField;

	/** 例外クラス名 */
	private TextItem exceptionClassNameField;

	/** 種類 */
	private SelectItem typeField;

	/** 個別属性部分 */
	private ResultTypeEditPane typeEditPane;

	/** 編集対象 */
	private ResultDefinition curDefinition;

	/** データ変更ハンドラ */
	private List<DataChangedHandler> handlers = new ArrayList<DataChangedHandler>();

	public ResultEditDialog() {

		setHeight(460);
		setTitle("Status Result Setting");
		centerInPage();

		final Label require = new Label(AdminClientMessageUtil.getString("ui_metadata_action_result_ResultEditDialog_descLabel"));
		require.setAutoHeight();

		container.addMember(require);

		CustomValidator validator = new CustomValidator() {

			@Override
			protected boolean condition(Object value) {
				boolean requireValidate = (statusField.getValueAsString() == null || statusField.getValueAsString().isEmpty()) && (exceptionClassNameField.getValueAsString() == null || exceptionClassNameField.getValueAsString().isEmpty());
				return !requireValidate;
			}
		};
		validator.setErrorMessage(AdminClientMessageUtil.getString("ui_metadata_action_result_ResultEditDialog_errorMsg"));

		statusField = new MtpTextItem("status", "Status");
		statusField.setValidators(validator);

		CanvasItem statusHintField = new CanvasItem();
		statusHintField.setWidth("100%");
		statusHintField.setShowTitle(false);
		Label hint = new Label(AdminClientMessageUtil.getString("ui_metadata_action_result_ResultEditDialog_allStatus"));
		hint.setAutoHeight();
		statusHintField.setCanvas(hint);

		exceptionClassNameField = new MtpTextItem("exceptionClassName", "Exception Class Name");
		exceptionClassNameField.setValidators(validator);

		typeField = new MtpSelectItem("type", "Type");
		typeField.setValueMap(typeMap);
		SmartGWTUtil.setRequired(typeField);
		typeField.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				typeChanged(ResultType.valueOf(SmartGWTUtil.getStringValue(typeField)));
			}
		});

		final DynamicForm form = new MtpForm();
		form.setAutoHeight();	//下に追加するためAutoHeight
		form.setItems(statusField, new SpacerItem(), new SpacerItem(), statusHintField, exceptionClassNameField, typeField);

		container.addMember(form);

		IButton save = new IButton("OK");
		save.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				boolean commonValidate = form.validate();
				boolean typeValidate = typeEditPane.validate();
				if (commonValidate && typeValidate) {
					saveDefinition();
				} else {
//					errors.setVisible(true);
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

	/**
	 * 編集対象の {@link ResultDefinition} を設定します。
	 * @param definition 編集対象の {@link ResultDefinition}
	 */
	public void setDefinition(ResultDefinition definition) {
		this.curDefinition = definition;

		statusField.setValue(definition.getCommandResultStatus());
		exceptionClassNameField.setValue(definition.getExceptionClassName());
		typeField.setValue(ResultType.valueOf(definition).name());

		if (typeEditPane != null) {
			if (container.contains(typeEditPane)) {
				container.removeMember(typeEditPane);
			}
			typeEditPane = null;
		}

		if (definition != null) {
			ResultType type = ResultType.valueOf(definition);
			typeEditPane = ResultType.typeOfEditPane(type);
			typeEditPane.setDefinition(definition);
			container.addMember(typeEditPane);
		}
	}

	/**
	 * {@link DataChangedHandler} を追加します。
	 *
	 * @param handler {@link DataChangedHandler}
	 */
	public void addDataChangeHandler(DataChangedHandler handler) {
		handlers.add(0, handler);
	}

	private void typeChanged(ResultType type) {
		ResultDefinition newDefinition = null;
		if (type != null) {
			//タイプにあったDefinitionを取得
			newDefinition = ResultType.typeOfDefinition(type);

			//共通属性をコピー
			newDefinition.setCommandResultStatus(SmartGWTUtil.getStringValue(statusField));
			newDefinition.setExceptionClassName(SmartGWTUtil.getStringValue(exceptionClassNameField));
		}

		setDefinition(newDefinition);

	}

	private void saveDefinition() {
		getEditedDefinition();
	}

	private void getEditedDefinition() {
		//Comboは必須なので必ずcurDefinitionは生成済み
		curDefinition.setCommandResultStatus(SmartGWTUtil.getStringValue(statusField));
		curDefinition.setExceptionClassName(SmartGWTUtil.getStringValue(exceptionClassNameField));
		curDefinition = typeEditPane.getEditDefinition(curDefinition);

		//データ変更を通知
		fireDataChanged(curDefinition);

		//ダイアログ消去
		destroy();
	}

	/**
	 * データ変更通知処理
	 */
	private void fireDataChanged(ResultDefinition definition) {
		DataChangedEvent event = new DataChangedEvent();
		event.setValueObject(definition);
		for (DataChangedHandler handler : handlers) {
			handler.onDataChanged(event);
		}
	}
}
