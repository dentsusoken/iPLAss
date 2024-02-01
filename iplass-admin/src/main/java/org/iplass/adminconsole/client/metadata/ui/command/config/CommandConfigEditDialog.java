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

package org.iplass.adminconsole.client.metadata.ui.command.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataSelectItem;
import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogCondition;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogHandler;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogMode;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpSelectItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextAreaItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.MetaDataUtil;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.command.definition.CommandDefinition;
import org.iplass.mtp.command.definition.config.SingleCommandConfig;
import org.iplass.mtp.transaction.Propagation;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;

public class CommandConfigEditDialog extends MtpDialog {

	private SelectItem commandField;
	private SelectItem transactionPropagationField;
	private CheckboxItem rollbackWhenExceptionField;
	private CheckboxItem throwExceptionIfSetRollbackOnlyField;
	private TextAreaItem initScriptField;

	/** データ変更ハンドラ */
	private List<DataChangedHandler> handlers = new ArrayList<DataChangedHandler>();

	public CommandConfigEditDialog() {
		this(true);
	}

	public CommandConfigEditDialog(boolean isMin) {

		setTitle("Command Config");
		setShowMaximizeButton(true);	//最大化は可能に設定（スクリプト編集用）
		centerInPage();

		//共通
		commandField = new MetaDataSelectItem(CommandDefinition.class, "Execute Command");
		SmartGWTUtil.setRequired(commandField);

		ButtonItem editScript = new ButtonItem("editScript", "Edit");
		editScript.setWidth(100);
		editScript.setColSpan(3);
		editScript.setAlign(Alignment.RIGHT);
		editScript.setPrompt(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_metadata_command_config_CommandConfigEditDialog_dispEditDialogInitScript")));
		editScript.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				MetaDataUtil.showScriptEditDialog(ScriptEditorDialogMode.GROOVY_SCRIPT,
						SmartGWTUtil.getStringValue(initScriptField),
						ScriptEditorDialogCondition.COMMAND_INIT_SCRIPT,
						"ui_metadata_command_config_CommandConfigEditDialog_scriptHint",
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

		initScriptField = new MtpTextAreaItem("initScript", "Init Script");
		initScriptField.setColSpan(2);
		initScriptField.setHeight("100%");
		SmartGWTUtil.setReadOnlyTextArea(initScriptField);

		//SelectItemとTextAreaItem(Height=100%)の場合に画面レイアウトが崩れるのでformを分ける
		final DynamicForm form1 = new MtpForm();

		if (isMin) {
			setHeight(400);

			form1.setHeight(22);
			form1.setItems(commandField);

		} else {
			setHeight(490);

			transactionPropagationField = new MtpSelectItem("transactionPropagation", "Transaction Propagation");
			HashMap<String, String> valueMap = new HashMap<>();
			for (Propagation propagation : Propagation.values()) {
				valueMap.put(propagation.name(), propagation.name());
			}
			transactionPropagationField.setValueMap(valueMap);
			transactionPropagationField.setDefaultValue(Propagation.REQUIRED.name());
			SmartGWTUtil.addHoverToFormItem(transactionPropagationField, AdminClientMessageUtil.getString("ui_metadata_command_config_CommandConfigEditDialog_transactionPropagation"));

			rollbackWhenExceptionField = new CheckboxItem("rollbackWhenException", "Rollback when exception");
			rollbackWhenExceptionField.setDefaultValue(true);
			SmartGWTUtil.addHoverToFormItem(rollbackWhenExceptionField, AdminClientMessageUtil.getString("ui_metadata_command_config_CommandConfigEditDialog_rollbackWhenException"));
			throwExceptionIfSetRollbackOnlyField = new CheckboxItem("throwExceptionIfSetRollbackOnly", "Throw exception if setRollbackOnly");
			SmartGWTUtil.addHoverToFormItem(throwExceptionIfSetRollbackOnlyField, AdminClientMessageUtil.getString("ui_metadata_command_config_CommandConfigEditDialog_throwExceptionIfSetRollbackOnly"));

			form1.setHeight(88);
			form1.setItems(commandField, transactionPropagationField, rollbackWhenExceptionField, throwExceptionIfSetRollbackOnlyField);
		}

		final DynamicForm form2 = new MtpForm();
		form2.setHeight100();
		form2.setItems(editScript, initScriptField);

		container.addMember(form1);
		container.addMember(form2);

		IButton save = new IButton("OK");
		save.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				boolean isValidate1 = form1.validate();
				boolean isValidate2 = form2.validate();
				if (isValidate1 && isValidate2){
					saveCommand();
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
	 * 編集対象の {@link SingleCommandConfig} を設定します。
	 * @param command 編集対象の {@link SingleCommandConfig}
	 */
	public void setCommandConfig(SingleCommandConfig command) {
		if (commandField != null) {
			commandField.setValue(command.getCommandName());
		}
		if (initScriptField != null) {
			initScriptField.setValue(command.getInitializeScript());
		}
		if (transactionPropagationField != null && command.getTransactionPropagation() != null) {
			transactionPropagationField.setValue(command.getTransactionPropagation().name());
		}
		if (rollbackWhenExceptionField != null) {
			rollbackWhenExceptionField.setValue(command.isRollbackWhenException());
		}
		if (throwExceptionIfSetRollbackOnlyField != null) {
			throwExceptionIfSetRollbackOnlyField.setValue(command.isThrowExceptionIfSetRollbackOnly());
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

	private void saveCommand() {
		getEditedCommandConfig();
	}

	private void getEditedCommandConfig() {
		MetaDataServiceAsync service = MetaDataServiceFactory.get();
		service.getDefinition(TenantInfoHolder.getId(), CommandDefinition.class.getName(), SmartGWTUtil.getStringValue(commandField), new AsyncCallback<CommandDefinition>() {

			@Override
			public void onFailure(Throwable caught) {
				SC.warn(AdminClientMessageUtil.getString("ui_metadata_command_config_CommandConfigEditDialog_failedToCreateCommand") + caught.getMessage());
			}

			@Override
			public void onSuccess(CommandDefinition result) {
				SingleCommandConfig command = new SingleCommandConfig();
//				command.setName(result.getName());
//				command.setDisplayName(result.getDisplayName());
//				command.setDescription(result.getDescription());
				command.setCommandName(result.getName());

				if (initScriptField == null || SmartGWTUtil.isEmpty(SmartGWTUtil.getStringValue(initScriptField))) {
					command.setInitializeScript(null);
				} else {
					command.setInitializeScript(SmartGWTUtil.getStringValue(initScriptField));
				}

				if (transactionPropagationField == null || SmartGWTUtil.isEmpty(SmartGWTUtil.getStringValue(transactionPropagationField))) {
					command.setTransactionPropagation(Propagation.REQUIRED);
				} else {
					command.setTransactionPropagation(Propagation.valueOf(SmartGWTUtil.getStringValue(transactionPropagationField)));
				}

				if (rollbackWhenExceptionField == null) {
					command.setRollbackWhenException(true);
				} else {
					command.setRollbackWhenException(SmartGWTUtil.getBooleanValue(rollbackWhenExceptionField));
				}
				if (throwExceptionIfSetRollbackOnlyField == null) {
					command.setThrowExceptionIfSetRollbackOnly(false);
				} else {
					command.setThrowExceptionIfSetRollbackOnly(SmartGWTUtil.getBooleanValue(throwExceptionIfSetRollbackOnlyField));
				}

				//データ変更を通知
				fireDataChanged(command);

				//ダイアログ消去
				destroy();
			}

		});
	}

	/**
	 * データ変更通知処理
	 */
	private void fireDataChanged(SingleCommandConfig command) {
		DataChangedEvent event = new DataChangedEvent();
		event.setValueObject(command);
		for (DataChangedHandler handler : handlers) {
			handler.onDataChanged(event);
		}
	}
}
