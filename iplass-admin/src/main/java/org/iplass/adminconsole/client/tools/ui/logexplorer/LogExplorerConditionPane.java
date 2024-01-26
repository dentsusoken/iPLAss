/*
 * Copyright (C) 2019 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.tools.ui.logexplorer;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.rpc.AdminAsyncCallback;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.MtpListGrid;
import org.iplass.adminconsole.client.base.ui.widget.MtpWidgetConstants;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogHandler;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogMode;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpSelectItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextAreaItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.MetaDataUtil;
import org.iplass.adminconsole.client.tools.data.logexplorer.LogConditionDS;
import org.iplass.adminconsole.client.tools.data.logexplorer.LogConditionDS.FIELD_NAME;
import org.iplass.adminconsole.shared.tools.dto.logexplorer.LogConditionInfo;
import org.iplass.adminconsole.shared.tools.rpc.logexplorer.LogExplorerServiceAsync;
import org.iplass.adminconsole.shared.tools.rpc.logexplorer.LogExplorerServiceFactory;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.DateTimeItem;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class LogExplorerConditionPane extends VLayout {

	private LogExplorerServiceAsync service = LogExplorerServiceFactory.get();

	private LogConditionGridPane gridPane;

	public LogExplorerConditionPane() {

		setWidth100();
		setHeight100();

		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setWidth100();
		toolStrip.setMembersMargin(5);
		toolStrip.setAlign(VerticalAlignment.BOTTOM);

		Label lblTitle = new Label();
		lblTitle.setWrap(false);
		lblTitle.setAutoWidth();
		lblTitle.setPadding(5);
		lblTitle.setContents("Log Conditions");
		toolStrip.addMember(lblTitle);

		toolStrip.addFill();

		final ToolStripButton refreshButton = new ToolStripButton();
		refreshButton.setIcon(MtpWidgetConstants.ICON_REFRESH);
		refreshButton.setHoverWrap(false);
		refreshButton.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_tools_logexplorer_LogExplorerConditionPane_refreshConditionList")));
		refreshButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				refreshGrid();
			}
		});
		toolStrip.addButton(refreshButton);

		gridPane = new LogConditionGridPane();

		addMember(toolStrip);
		addMember(gridPane);

		refreshGrid();
	}

	private void refreshGrid() {
		gridPane.refreshGrid();
	}

	private class LogConditionGridPane extends VLayout {

		private IButton btnApply;

		private LogConditionListGrid grid;
		private LogConditionEditPane editPane;

		private IButton btnAdd;
		private IButton btnRemove;

		private ListGridRecord editRecord;

		private boolean isEditing;

		public LogConditionGridPane() {
			setWidth100();
			setHeight100();

			HLayout pnlHeader = new HLayout(5);
			pnlHeader.setMargin(5);
			pnlHeader.setAutoHeight();

			btnApply = new IButton("Apply");
			btnApply.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if (!isEditing) {
						applyCondition();
					} else {
						SC.warn(AdminClientMessageUtil.getString("ui_tools_logexplorer_LogExplorerConditionPane_editingCondition"));
					}
				}
			});
			pnlHeader.addMember(btnApply);

			grid = new LogConditionListGrid();
			grid.addRecordDoubleClickHandler((event) -> {
				if (!isEditing) {
					ListGridRecord record = grid.getRecord(event.getRecordNum());
					editCondition(record);
				} else {
					SC.warn(AdminClientMessageUtil.getString("ui_tools_logexplorer_LogExplorerConditionPane_editingCondition"));
				}
			});

			HLayout pnlFooter = new HLayout(5);
			pnlFooter.setMargin(5);
			pnlFooter.setAutoHeight();

			btnAdd = new IButton("Add");
			btnAdd.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if (!isEditing) {
						editCondition(null);
					} else {
						SC.warn(AdminClientMessageUtil.getString("ui_tools_logexplorer_LogExplorerConditionPane_editingCondition"));
					}
				}
			});

			btnRemove = new IButton("Remove");
			btnRemove.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if (!isEditing) {
						grid.removeSelectedData();
					} else {
						SC.warn(AdminClientMessageUtil.getString("ui_tools_logexplorer_LogExplorerConditionPane_editingCondition"));
					}
				}
			});
			pnlFooter.setMembers(btnAdd, btnRemove);

			editPane = new LogConditionEditPane();
			editPane.addDataChangeHandler(new DataChangedHandler() {

				@Override
				public void onDataChanged(DataChangedEvent event) {
					LogConditionInfo info = event.getValueObject(LogConditionInfo.class);
					applyRecord(info);
				}
			});

			addMember(pnlHeader);
			addMember(grid);
			addMember(pnlFooter);
			addMember(editPane);
		}

		public void refreshGrid() {
			if (!isEditing) {
				grid.refreshGrid();
			} else {
				SC.warn(AdminClientMessageUtil.getString("ui_tools_logexplorer_LogExplorerConditionPane_editingCondition"));
			}
		}

		private void applyCondition() {
			SmartGWTUtil.showSaveProgress();
			List<LogConditionInfo> conditions = grid.getEditConditions();
			service.applyLogConditions(TenantInfoHolder.getId(), conditions, new AdminAsyncCallback<String>() {

				@Override
				protected void beforeFailure(Throwable caught){
					SmartGWTUtil.hideProgress();
				};

				@Override
				public void onSuccess(String errorMessage) {
					SmartGWTUtil.hideProgress();
					if (SmartGWTUtil.isNotEmpty(errorMessage)) {
						SC.warn(errorMessage);
					} else {
						SC.say(AdminClientMessageUtil.getString("ui_tools_logexplorer_LogExplorerConditionPane_applyComplete"));
					}
				}
			});
		}

		private void editCondition(ListGridRecord record) {
			itemControl(true);
			editRecord = record;
			isEditing = true;

			LogConditionInfo info = null;
			if (record != null) {
				info = (LogConditionInfo)record.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name());
			}
			editPane.setCondition(info);
		}

		private void applyRecord(LogConditionInfo info) {
			//infoがnullはキャンセル
			if (info != null) {
				grid.applyToRecord(editRecord, info);
			}
			itemControl(false);
			editRecord = null;
			isEditing = false;
		}

		private void itemControl(boolean disabled) {
			btnApply.setDisabled(disabled);
			btnAdd.setDisabled(disabled);
			btnRemove.setDisabled(disabled);
		}

	}

	private class LogConditionListGrid extends MtpListGrid {

		private LogConditionDS ds;

		public LogConditionListGrid() {

			setWidth100();
			setHeight100();

			//データ件数が多い場合を考慮し、false
			setShowAllRecords(false);

			//行番号表示
			setShowRowNumbers(true);

			refreshGrid();
		}

		public void refreshGrid() {
			ds = LogConditionDS.getInstance();
			setDataSource(ds);

			//ボタンを表示したいためListGridFieldを指定
			ListGridField levelField = new ListGridField(FIELD_NAME.LEVEL.name(), "Level");
			levelField.setWidth(120);
			ListGridField expiresAtField = new ListGridField(FIELD_NAME.EXPIRES_AT.name(), "ExpiresAt");
			expiresAtField.setWidth(120);
			ListGridField conditionField = new ListGridField(FIELD_NAME.CONDITION.name(), "Condition");
			conditionField.setWidth(100);
			ListGridField loggerNamePatternField = new ListGridField(FIELD_NAME.LOGGER_NAME_PATTERN.name(), "loggerName Pattern");

			setFields(levelField, expiresAtField, conditionField, loggerNamePatternField);

			fetchData();
		}

		public void applyToRecord(ListGridRecord current, LogConditionInfo info) {
			if (current == null) {
				ListGridRecord newRecord = new ListGridRecord();
				ds.appyToRecord(newRecord, info);
				addData(newRecord);
			} else {
				ds.appyToRecord(current, info);
				updateData(current);
			}
			refreshFields();
		}

		public List<LogConditionInfo> getEditConditions() {
			List<LogConditionInfo> result = new ArrayList<>();
			for (ListGridRecord record : getRecords()) {
				result.add((LogConditionInfo)record.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name()));
			}
			return result.isEmpty() ? null : result;
		}

	}

	private class LogConditionEditPane extends VLayout {

		private IButton btnOK;
		private IButton btnCancel;

		private DynamicForm form;
		private MtpSelectItem selLevel;
		private DateTimeItem txtExpiresAt;
		private MtpTextItem txtLoggerNamePattern;
		private ButtonItem btnEditCondition;
		private MtpTextAreaItem txaCondition;

		@SuppressWarnings("unused")
		private LogConditionInfo current;

		private List<DataChangedHandler> handlers = new ArrayList<>();

		public LogConditionEditPane() {
			setWidth100();
			setHeight(290);

			HLayout pnlHeader = new HLayout(5);
			pnlHeader.setMargin(5);
			pnlHeader.setAutoHeight();

			//ボタン
			btnOK = new IButton("OK");
			btnOK.setDisabled(true);
			btnOK.addClickHandler((event) -> {
				saveCondition();
			});
			btnCancel = new IButton("Cancel");
			btnCancel.setDisabled(true);
			btnCancel.addClickHandler((event)-> {
				cancelCondition();
			});
			pnlHeader.setMembers(btnOK, btnCancel);

			selLevel =  new MtpSelectItem();
			selLevel.setTitle("Level");
			LinkedHashMap<String, String> levelMap = new LinkedHashMap<>();
			levelMap.put("TRACE", "TRACE");
			levelMap.put("DEBUG", "DEBUG");
			levelMap.put("INFO", "INFO");
			levelMap.put("WARN", "WARN");
			levelMap.put("ERROR", "ERROR ");
			selLevel.setValueMap(levelMap);
			SmartGWTUtil.setRequired(selLevel);
			selLevel.setDisabled(true);

			txtExpiresAt = SmartGWTUtil.createDateTimeItem();
			txtExpiresAt.setTitle("Expires At");
			SmartGWTUtil.setRequired(txtExpiresAt);
			txtExpiresAt.setDisabled(true);

			txtLoggerNamePattern = new MtpTextItem();
			txtLoggerNamePattern.setTitle("Logger Name Pattern");
			txtLoggerNamePattern.setDisabled(true);

			btnEditCondition = new ButtonItem();
			btnEditCondition.setTitle("Edit");
			btnEditCondition.setWidth(100);
			btnEditCondition.setStartRow(true);
			btnEditCondition.setColSpan(3);
			btnEditCondition.setAlign(Alignment.RIGHT);
			btnEditCondition.setPrompt(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_tools_logexplorer_LogExplorerConditionPane_dispEditDialogCondition")));
			btnEditCondition.addClickHandler((event) -> {
				MetaDataUtil.showScriptEditDialog(ScriptEditorDialogMode.GROOVY_SCRIPT,
						SmartGWTUtil.getStringValue(txaCondition),
						"Log Condition",
						"ui_tools_logexplorer_LogExplorerConditionPane_condition_scriptHint",
						null,
						new ScriptEditorDialogHandler() {

							@Override
							public void onSave(String text) {
								txaCondition.setValue(text);
							}
							@Override
							public void onCancel() {
							}
						});
			});
			btnEditCondition.setDisabled(true);

			txaCondition = new MtpTextAreaItem();
			txaCondition.setTitle("Condition");
			txaCondition.setColSpan(2);
			txaCondition.setHeight(80);
			SmartGWTUtil.setReadOnlyTextArea(txaCondition);

			form = new MtpForm();
			form.setHeight100();
			form.setColWidths(MtpWidgetConstants.FORM_WIDTH_TITLE, "*", 10);
			form.setAutoFocus(true);
			form.setItems(selLevel, txtExpiresAt, txtLoggerNamePattern, btnEditCondition, txaCondition);

			HLayout dummy = new HLayout();
			dummy.setWidth100();
			dummy.setHeight(5);

			VLayout layout = new VLayout(5);
			layout.setMargin(5);
			layout.setHeight100();
			layout.setOverflow(Overflow.AUTO);

			layout.addMember(pnlHeader);
			layout.addMember(form);
			layout.addMember(dummy);

			SectionStack stack = new SectionStack();
			SectionStackSection section = new SectionStackSection("Condition Setting");
			section.setCanCollapse(false);	//CLOSE不可

			section.addItem(layout);
			stack.addSection(section);

			addMember(stack);
		}

		public void addDataChangeHandler(DataChangedHandler handler) {
			handlers.add(0, handler);
		}

		public void setCondition(LogConditionInfo info) {
			this.current = info;
			if (info == null) {
				newCondition();
			} else {
				editCondition(info);
			}
		}

		private void newCondition() {
			applyFromCondition(null);
			itemControl(false);
		}

		private void editCondition(LogConditionInfo info) {
			applyFromCondition(info);
			itemControl(false);
		}

		private void saveCondition() {
			if (form.validate()) {
				LogConditionInfo info = getEditCondition();
				applyFromCondition(null);
				itemControl(true);
				fireDataChanged(info);
			}
		}

		private void cancelCondition() {
			applyFromCondition(null);
			itemControl(true);
			fireDataChanged(null);
		}

		private void itemControl(boolean disabled) {

			btnOK.setDisabled(disabled);
			btnCancel.setDisabled(disabled);

			selLevel.setDisabled(disabled);
			txtExpiresAt.setDisabled(disabled);
			txtLoggerNamePattern.setDisabled(disabled);
			btnEditCondition.setDisabled(disabled);
		}

		private void applyFromCondition(LogConditionInfo info) {
			if (info == null) {
				selLevel.clearValue();
				selLevel.clearErrors();
				txtExpiresAt.clearValue();
				txtExpiresAt.clearErrors();
				txtLoggerNamePattern.clearValue();
				txtLoggerNamePattern.clearErrors();
				txaCondition.clearValue();
				txaCondition.clearErrors();
			} else {
				selLevel.setValue(info.getLevel());
				if (info.getExpiresAt() > 0) {
					Timestamp date = new Timestamp(info.getExpiresAt());
					txtExpiresAt.setValue(date);
				} else {
					txtExpiresAt.clearValue();
				}
				txtLoggerNamePattern.setValue(info.getLoggerNamePattern());
				txaCondition.setValue(info.getCondition());
			}
		}

		private LogConditionInfo getEditCondition() {
			LogConditionInfo info = new LogConditionInfo();
			info.setLevel(SmartGWTUtil.getStringValue(selLevel));
			Date expiresAt = txtExpiresAt.getValueAsDate();
			if (expiresAt != null) {
				info.setExpiresAt(expiresAt.getTime());
			} else {
				info.setExpiresAt(0);
			}
			info.setLoggerNamePattern(SmartGWTUtil.getStringValue(txtLoggerNamePattern));
			info.setCondition(SmartGWTUtil.getStringValue(txaCondition));
			return info;
		}

		private void fireDataChanged(LogConditionInfo info) {
			DataChangedEvent event = new DataChangedEvent();
			event.setValueObject(info);
			for (DataChangedHandler handler : handlers) {
				handler.onDataChanged(event);
			}
		}

	}


}
