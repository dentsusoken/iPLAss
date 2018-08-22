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

package org.iplass.adminconsole.client.metadata.ui.command.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogCondition;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogHandler;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogMode;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.MetaDataUtil;
import org.iplass.mtp.transaction.Propagation;

import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.layout.HLayout;

public class CompositeCommandConfigEditDialog extends AbstractWindow {

	private SelectItem transactionPropagationField;
	private CheckboxItem rollbackWhenExceptionField;
	private CheckboxItem throwExceptionIfSetRollbackOnlyField;
	private TextAreaItem initScriptField;
	private TextAreaItem execScriptField;

	/** データ変更ハンドラ */
	private List<DataChangedHandler> handlers = new ArrayList<DataChangedHandler>();

	public CompositeCommandConfigEditDialog() {
		this(true);
	}

	public CompositeCommandConfigEditDialog(boolean isMin) {

		setWidth(500);
		setTitle("Composite Command Config");
		setShowMinimizeButton(false);
		setShowMaximizeButton(true);	//最大化は可能に設定（スクリプト編集用）
		setCanDragResize(true);			//リサイズは可能に設定（スクリプト編集用）
		setIsModal(true);
		setShowModalMask(true);

		ButtonItem initScript = new ButtonItem("editScript1", "Edit");
		initScript.setWidth(100);
		initScript.setStartRow(false);
		initScript.setPrompt(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_metadata_command_config_CompositeCommandConfigEditDialog_dispEditDialogInitScript")));
		initScript.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				MetaDataUtil.showScriptEditDialog(ScriptEditorDialogMode.GROOVY_SCRIPT,
						SmartGWTUtil.getStringValue(initScriptField),
						ScriptEditorDialogCondition.COMMAND_EXEC_SCRIPT,
						"ui_metadata_command_config_CompositeCommandConfigEditDialog_initScriptHint",
						null,
						new ScriptEditorDialogHandler() {

							@Override
							public void onSave(String text) {
								initScriptField.setValue(text);
							}
							@Override
							public void onCancel() {
							}
						});
			}
		});

		initScriptField = new TextAreaItem("initScript", "Initialize Script");
		initScriptField.setColSpan(2);
		initScriptField.setWidth("100%");
		initScriptField.setHeight("100%");

		ButtonItem editScript = new ButtonItem("editScript2", "Edit");
		editScript.setWidth(100);
		editScript.setStartRow(false);
		editScript.setPrompt(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_metadata_command_config_CompositeCommandConfigEditDialog_displayDialogEditExecuteRuleScript")));
		editScript.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				MetaDataUtil.showScriptEditDialog(ScriptEditorDialogMode.GROOVY_SCRIPT,
						SmartGWTUtil.getStringValue(execScriptField),
						ScriptEditorDialogCondition.COMMAND_EXEC_SCRIPT,
						"ui_metadata_command_config_CompositeCommandConfigEditDialog_executeRuleScriptHint",
						null,
						new ScriptEditorDialogHandler() {

							@Override
							public void onSave(String text) {
								execScriptField.setValue(text);
							}
							@Override
							public void onCancel() {
							}
						});
			}
		});

		execScriptField = new TextAreaItem("execScript", "Execute Rule Script");
		execScriptField.setColSpan(2);
		execScriptField.setWidth("100%");
		execScriptField.setHeight("100%");

		final DynamicForm form1 = new DynamicForm();
		form1.setNumCols(3);
		form1.setColWidths(100, "*", 100);
		form1.setMargin(5);
		form1.setHeight(66);
		form1.setWidth100();

		final DynamicForm form2 = new DynamicForm();
		form2.setNumCols(3);
		form2.setColWidths(100, "*", 100);
		form2.setMargin(5);
		form2.setHeight(330);
		form2.setWidth100();
		form2.setItems(new SpacerItem(), new SpacerItem(), initScript, initScriptField, new SpacerItem(), new SpacerItem(), editScript, execScriptField);

		if (isMin) {
			setHeight(400);
		} else {
			setHeight(490);

			transactionPropagationField = new SelectItem("transactionPropagation", "Transaction Propagation");
			transactionPropagationField.setWidth(250);
			HashMap<String, String> valueMap = new HashMap<>();
			for (Propagation propagation : Propagation.values()) {
				valueMap.put(propagation.name(), propagation.name());
			}
			transactionPropagationField.setValueMap(valueMap);
			transactionPropagationField.setDefaultValue(Propagation.REQUIRED.name());
			SmartGWTUtil.addHoverToFormItem(transactionPropagationField, AdminClientMessageUtil.getString("ui_metadata_command_config_CompositeCommandConfigEditDialog_transactionPropagation"));

			rollbackWhenExceptionField = new CheckboxItem("rollbackWhenException", "Rollback when exception");
			SmartGWTUtil.addHoverToFormItem(rollbackWhenExceptionField, AdminClientMessageUtil.getString("ui_metadata_command_config_CompositeCommandConfigEditDialog_rollbackWhenException"));
			throwExceptionIfSetRollbackOnlyField = new CheckboxItem("throwExceptionIfSetRollbackOnly", "Throw exception if setRollbackOnly");
			SmartGWTUtil.addHoverToFormItem(throwExceptionIfSetRollbackOnlyField, AdminClientMessageUtil.getString("ui_metadata_command_config_CompositeCommandConfigEditDialog_throwExceptionIfSetRollbackOnly"));

			form1.setItems(transactionPropagationField, rollbackWhenExceptionField, throwExceptionIfSetRollbackOnlyField);
		}


		IButton save = new IButton("OK");
		save.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				boolean isValidate1 = form1.validate();
				boolean isValidate2 = form2.validate();
				if (isValidate1 && isValidate2){
					saveExecScript();
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

		HLayout footer = new HLayout(5);
		footer.setMargin(5);
		footer.setHeight(20);
		footer.setWidth100();
		//footer.setAlign(Alignment.LEFT);
		footer.setAlign(VerticalAlignment.CENTER);
		footer.setMembers(save, cancel);

		if (form1.getFieldCount() > 0) addItem(form1);
		addItem(form2);
		addItem(footer);

		centerInPage();

		SmartGWTUtil.setReadOnlyTextArea(initScriptField);
		SmartGWTUtil.setReadOnlyTextArea(execScriptField);
	}

	public void setExecScript(String execScript) {
		if (execScriptField != null) {
			execScriptField.setValue(execScript);
		}
	}

	public void setInitScript(String initScript) {
		if (initScriptField != null) {
			initScriptField.setValue(initScript);
		}
	}

	public void setTransactionPropagation(String transactionPropagation) {
		if (transactionPropagationField != null) {
			transactionPropagationField.setValue(transactionPropagation);
		}
	}

	public void setRollbackWhenException(boolean rollbackWhenException) {
		if (rollbackWhenExceptionField != null) {
			rollbackWhenExceptionField.setValue(rollbackWhenException);
		}
	}

	public void settThrowExceptionIfSetRollbackOnly(boolean throwExceptionIfSetRollbackOnly) {
		if (throwExceptionIfSetRollbackOnlyField != null) {
			throwExceptionIfSetRollbackOnlyField.setValue(throwExceptionIfSetRollbackOnly);
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

	private void saveExecScript() {
		//データ変更を通知
		String execScript = null;
		if (execScriptField != null && SmartGWTUtil.isNotEmpty(SmartGWTUtil.getStringValue(execScriptField))) {
			execScript = SmartGWTUtil.getStringValue(execScriptField);
		}
		String initScript = null;
		if (initScriptField != null && SmartGWTUtil.isNotEmpty(SmartGWTUtil.getStringValue(initScriptField))) {
			initScript = SmartGWTUtil.getStringValue(initScriptField);
		}
		String transactionPropagation = null;
		if (transactionPropagationField != null && SmartGWTUtil.isNotEmpty(SmartGWTUtil.getStringValue(transactionPropagationField))) {
			transactionPropagation = SmartGWTUtil.getStringValue(transactionPropagationField);
		}
		boolean rollbackWhenException = rollbackWhenExceptionField != null ? SmartGWTUtil.getBooleanValue(rollbackWhenExceptionField) : true;
		boolean throwExceptionIfSetRollbackOnly = throwExceptionIfSetRollbackOnlyField != null ? SmartGWTUtil.getBooleanValue(throwExceptionIfSetRollbackOnlyField) : false;

		fireDataChanged(execScript, initScript, transactionPropagation, rollbackWhenException, throwExceptionIfSetRollbackOnly);

		//ダイアログ消去
		destroy();
	}

	/**
	 * データ変更通知処理
	 * @param throwExceptionIfSetRollbackOnly
	 * @param rollbackWhenException
	 */
	private void fireDataChanged(String execScript, String initScript, String transactionPropagation, boolean rollbackWhenException, boolean throwExceptionIfSetRollbackOnly) {
		DataChangedEvent event = new DataChangedEvent();
		event.setValue("execScript", execScript);
		event.setValue("initScript", initScript);
		event.setValue("transactionPropagation", transactionPropagation);
		event.setValue("rollbackWhenException", rollbackWhenException);
		event.setValue("throwExceptionIfSetRollbackOnly", throwExceptionIfSetRollbackOnly);
		for (DataChangedHandler handler : handlers) {
			handler.onDataChanged(event);
		}
	}
}
