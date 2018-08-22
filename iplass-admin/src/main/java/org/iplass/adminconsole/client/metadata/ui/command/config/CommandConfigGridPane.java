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
import java.util.List;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.rpc.AdminAsyncCallback;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataViewGridButton;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.MetaDataItemMenuTreeNode;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.command.definition.CommandDefinition;
import org.iplass.mtp.command.definition.config.CommandConfig;
import org.iplass.mtp.command.definition.config.CompositeCommandConfig;
import org.iplass.mtp.command.definition.config.SingleCommandConfig;
import org.iplass.mtp.transaction.Propagation;

import com.smartgwt.client.types.AutoFitWidthApproach;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class CommandConfigGridPane extends VLayout {

	private enum FIELD_NAME {
		SHOW_ICON,
		NAME,
		DISPLAY_NAME,
//		COMMAND_NAME,
		INIT_SCRIPT,
		INIT_SCRIPT_STATUS,
		VALUE_OBJECT,
	}

	private CommandConfigGrid grid;

	private IButton compositeCommandConfigButton;

	private String execScript;
	private String initScript;
	private String transactionPropagation;
	private boolean rollbackWhenException = true;
	private boolean throwExceptionIfSetRollbackOnly = false;

	public CommandConfigGridPane() {
		setMargin(5);
		setAutoHeight();

		HLayout captionComposit = new HLayout(5);
		captionComposit.setHeight(25);

		Label caption = new Label("Execute Commands:");
		caption.setHeight(21);
		caption.setWrap(false);
		captionComposit.addMember(caption);

		Label captionHint = new Label();
		SmartGWTUtil.addHintToLabel(captionHint,
				"<style type=\"text/css\"><!--"
				+ "ul.notes{margin-top:5px;padding-left:15px;list-style-type:disc;}"
				+ "ul.notes li{padding:5px 0px;}"
				+ "ul.notes li span.strong {text-decoration:underline;color:red}"
				+ "ul.subnotes {margin-top:5px;padding-left:10px;list-style-type:circle;}"
				+ "--></style>"
				+ "<h3>Notes</h3>"
				+ "<ul class=\"notes\">"
				+ AdminClientMessageUtil.getString("ui_metadata_command_config_CommandConfigGridPane_runScriptComment1")
				+ AdminClientMessageUtil.getString("ui_metadata_command_config_CommandConfigGridPane_runScriptComment2")
				+ AdminClientMessageUtil.getString("ui_metadata_command_config_CommandConfigGridPane_runScriptComment3")
				+ "</ul>");
		captionComposit.addMember(captionHint);

		grid = new CommandConfigGrid();
		// レコード編集イベント設定
		grid.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				editCommand((ListGridRecord)event.getRecord());
			}
		});

		IButton addCommand = new IButton("Add");
		addCommand.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				addCommand();
			}
		});

		IButton delCommand = new IButton("Remove");
		delCommand.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				deleteCommand();
			}
		});

		compositeCommandConfigButton = new IButton("Composite Command Config");
		compositeCommandConfigButton.setWidth(180);
		compositeCommandConfigButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				editExecScript();
			}
		});
		compositeCommandConfigButton.setDisabled(true);
		compositeCommandConfigButton.setHoverWrap(false);

		HLayout commandButtonPane = new HLayout(5);
		commandButtonPane.setMargin(5);
		commandButtonPane.addMember(addCommand);
		commandButtonPane.addMember(delCommand);
		commandButtonPane.addMember(compositeCommandConfigButton);

		addMember(captionComposit);
		addMember(grid);
		addMember(commandButtonPane);
	}

	/**
	 * 編集対象のCommandConfigを設定します。
	 *
	 * @param config 編集対象のCommandConfig
	 */
	public void setConfig(CommandConfig config) {
		grid.setData(new ListGridRecord[]{});
		if (config != null) {
			List<ListGridRecord> records = new ArrayList<ListGridRecord>();
			if (config instanceof SingleCommandConfig) {
				records.add(createSingleRecord((SingleCommandConfig)config, null));
			} else if (config instanceof CompositeCommandConfig) {
				CompositeCommandConfig composite = (CompositeCommandConfig)config;
				records.addAll(createCompositeRecord(composite));

				execScript = composite.getExecuteScript();
				initScript = composite.getInitializeScript();
				if (composite.getTransactionPropagation() != null) {
					transactionPropagation = composite.getTransactionPropagation().name();
				}
				rollbackWhenException = composite.isRollbackWhenException();
				throwExceptionIfSetRollbackOnly = composite.isThrowExceptionIfSetRollbackOnly();
			}
			grid.setData(records.toArray(new ListGridRecord[]{}));
		}
		setExecScriptCommandStatus();
	}

	/**
	 * 入力チェックを実行します。
	 *
	 * @return 入力チェック結果
	 */
	public boolean validate() {
		//チェック対象なし
		return true;
	}

	/**
	 * 編集されたCommandConfig情報を返します。
	 *
	 * @return 編集CommandConfig情報
	 */
	public CommandConfig getEditCommandConfig() {

		ListGridRecord[] records = grid.getRecords();
		if (records == null || records.length == 0) {
			return null;
		}

		if (records.length == 1) {
			SingleCommandConfig single = (SingleCommandConfig)records[0].getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name());
			return single;
		} else {
			CompositeCommandConfig composite = new CompositeCommandConfig();
			if (SmartGWTUtil.isEmpty(execScript)) {
				execScript = null;
			}
			composite.setExecuteScript(execScript);
			if (SmartGWTUtil.isEmpty(initScript)) {
				initScript = null;
			}
			composite.setInitializeScript(initScript);
			if (SmartGWTUtil.isNotEmpty(transactionPropagation)) {
				composite.setTransactionPropagation(Propagation.valueOf(transactionPropagation));
			}
			composite.setRollbackWhenException(rollbackWhenException);
			composite.setThrowExceptionIfSetRollbackOnly(throwExceptionIfSetRollbackOnly);

			SingleCommandConfig[] commands = new SingleCommandConfig[records.length];
			int i = 0;
			for (ListGridRecord record : records) {
				SingleCommandConfig single = (SingleCommandConfig)record.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name());
				commands[i] = single;
				i++;
			}
			composite.setCommands(commands);
			return composite;
		}
	}

	public void addCommand(MetaDataItemMenuTreeNode itemNode) {

		MetaDataServiceAsync service = MetaDataServiceFactory.get();
		service.getDefinition(TenantInfoHolder.getId(), CommandDefinition.class.getName(), itemNode.getDefName(), new AdminAsyncCallback<CommandDefinition>() {

			@Override
			public void onSuccess(CommandDefinition result) {
				SingleCommandConfig command = new SingleCommandConfig();
//				command.setName(result.getName());
//				command.setDisplayName(result.getDisplayName());
//				command.setDescription(result.getDescription());
				command.setCommandName(result.getName());
				command.setInitializeScript(null);

				ListGridRecord newRecord = createSingleRecord(command, null);
				grid.addData(newRecord);
				setExecScriptCommandStatus();
				grid.refreshFields();
			}

		});
	}

	private ListGridRecord createSingleRecord(SingleCommandConfig single, ListGridRecord record) {
		if (record == null) {
			record = new ListGridRecord();
		}
		record.setAttribute(FIELD_NAME.NAME.name(), single.getCommandName());
//		record.setAttribute(FIELD_NAME.DISPLAY_NAME.name(), single.getDisplayName());
//		record.setAttribute(FIELD_NAME.COMMAND_NAME.name(), single.getMetaCommandId());	//IDはDefinitionレベルでは不明
		record.setAttribute(FIELD_NAME.INIT_SCRIPT.name(), single.getInitializeScript());
		if (SmartGWTUtil.isEmpty(single.getInitializeScript())) {
			record.setAttribute(FIELD_NAME.INIT_SCRIPT_STATUS.name(), "");
			single.setInitializeScript(null);	//今まで""の可能性があったので編集しない場合も想定しクリア
		} else {
			record.setAttribute(FIELD_NAME.INIT_SCRIPT_STATUS.name(), "*");
		}
		record.setAttribute(FIELD_NAME.VALUE_OBJECT.name(), single);
		return record;
	}

	private List<ListGridRecord> createCompositeRecord(CompositeCommandConfig composite) {
		List<ListGridRecord> records = new ArrayList<ListGridRecord>();
		CommandConfig[] commands = composite.getCommands();
		for (CommandConfig config : commands) {
			if (config instanceof SingleCommandConfig) {
				records.add(createSingleRecord((SingleCommandConfig)config, null));
			} else if (config instanceof CompositeCommandConfig) {
				//実際Compositeの中にComposite設定が可能だが、AdminConsoleでの登録は１階層とする。
				//この用途としてはCompositeでは実行を制御できるScriptが定義できること
				//でも一番上でスクリプト制御すれば、あえて子供で再度制御スクリプト定義をすることもないだろう
				//TODO むしろCompositeCommandConfigが保持するCommandをSingleCommandConfigにしたほうがいいか？
				records.addAll(createCompositeRecord((CompositeCommandConfig)config));
			}
		}
		return records;
	}

	private void setExecScriptCommandStatus() {
		if (grid.getRecords().length > 1) {
			compositeCommandConfigButton.setDisabled(false);
		} else {
			compositeCommandConfigButton.setDisabled(true);
			execScript = null;
			initScript = null;
			transactionPropagation = null;
			rollbackWhenException = true;
			throwExceptionIfSetRollbackOnly = false;
		}
		if (!SmartGWTUtil.isEmpty(execScript)
				|| !SmartGWTUtil.isEmpty(initScript)
				|| (!SmartGWTUtil.isEmpty(transactionPropagation) && !Propagation.REQUIRED.name().equals(transactionPropagation))
				|| !rollbackWhenException
				|| throwExceptionIfSetRollbackOnly) {
			compositeCommandConfigButton.setTitle("Composite Command Config(*)");
		} else {
			compositeCommandConfigButton.setTitle("Composite Command Config");
		}
	}

	private void addCommand() {
		editCommand(null);
	}

	private void editCommand(final ListGridRecord record) {
		final CommandConfigEditDialog dialog = new CommandConfigEditDialog(false);
		dialog.addDataChangeHandler(new DataChangedHandler() {

			@Override
			public void onDataChanged(DataChangedEvent event) {
				SingleCommandConfig command = event.getValueObject(SingleCommandConfig.class);
				ListGridRecord newRecord = createSingleRecord(command, record);
				if (record != null) {
					grid.updateData(newRecord);
				} else {
					//追加
					grid.addData(newRecord);
					setExecScriptCommandStatus();
				}
				grid.refreshFields();
			}
		});

		if (record != null) {
			dialog.setCommandConfig((SingleCommandConfig)record.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name()));
		}
		dialog.show();
	}

	private void deleteCommand() {
		grid.removeSelectedData();
		setExecScriptCommandStatus();
	}

	private void editExecScript() {
		final CompositeCommandConfigEditDialog dialog = new CompositeCommandConfigEditDialog(false);
		dialog.addDataChangeHandler(new DataChangedHandler() {

			@Override
			public void onDataChanged(DataChangedEvent event) {
				execScript = event.getValue(String.class, "execScript");
				initScript = event.getValue(String.class, "initScript");
				transactionPropagation = event.getValue(String.class, "transactionPropagation");
				rollbackWhenException = event.getValue(Boolean.class, "rollbackWhenException");
				throwExceptionIfSetRollbackOnly = event.getValue(Boolean.class, "throwExceptionIfSetRollbackOnly");
				setExecScriptCommandStatus();
			}
		});

		if (execScript != null) {
			dialog.setExecScript(execScript);
		}
		if (initScript != null) {
			dialog.setInitScript(initScript);
		}
		if (transactionPropagation != null) {
			dialog.setTransactionPropagation(transactionPropagation);
		}
		dialog.setRollbackWhenException(rollbackWhenException);
		dialog.settThrowExceptionIfSetRollbackOnly(throwExceptionIfSetRollbackOnly);
		dialog.show();
	}

	private class CommandConfigGrid extends ListGrid {

		public CommandConfigGrid() {
			setWidth100();
			setHeight(1);

			setShowAllColumns(true);							//列を全て表示
			setShowAllRecords(true);							//レコードを全て表示
			setCanResizeFields(true);							//列幅変更可能
			setCanSort(false);									//ソート不可
			setCanPickFields(false);							//表示フィールドの選択不可
			setCanGroupBy(false);								//GroupByの選択不可
			setAutoFitWidthApproach(AutoFitWidthApproach.BOTH);	//AutoFit時にタイトルと値を参照
			setLeaveScrollbarGap(false);						//縦スクロールバー自動表示制御
			setBodyOverflow(Overflow.VISIBLE);
			setOverflow(Overflow.VISIBLE);

			//この２つを指定することでcreateRecordComponentが有効
			setShowRecordComponents(true);
			setShowRecordComponentsByCell(true);

			setCanReorderRecords(true);							//Dragによる並び替えを可能にする

			ListGridField showMetaButtonField = new ListGridField(FIELD_NAME.SHOW_ICON.name(), " ");
			showMetaButtonField.setWidth(25);
			ListGridField nameField = new ListGridField(FIELD_NAME.NAME.name(), "Command Name");
//			ListGridField dispNameField = new ListGridField(FIELD_NAME.DISPLAY_NAME.name(), "Display Name");;
//			ListGridField commandField = new ListGridField(FIELD_NAME.COMMAND_NAME.name(), "実行Command");	//IDはDefinitionレベルでは不明
			ListGridField initScriptField = new ListGridField(FIELD_NAME.INIT_SCRIPT_STATUS.name(), "Init Script");
			initScriptField.setWidth(80);

			setFields(showMetaButtonField, nameField, initScriptField);
		}

		@Override
		protected Canvas createRecordComponent(final ListGridRecord record, Integer colNum) {
			final String fieldName = this.getFieldName(colNum);
			if (FIELD_NAME.SHOW_ICON.name().equals(fieldName)) {
				MetaDataViewGridButton button = new MetaDataViewGridButton(CommandDefinition.class.getName());
				button.setActionButtonPrompt(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_metadata_command_config_CommandConfigGridPane_dispMetadataEditScreenCommand")));
				button.setMetaDataShowClickHandler(new MetaDataViewGridButton.MetaDataShowClickHandler() {
					@Override
					public String targetDefinitionName() {
						return record.getAttributeAsString(FIELD_NAME.NAME.name());
					}
				});
				return button;
			}
			return null;
		}
	}

}
