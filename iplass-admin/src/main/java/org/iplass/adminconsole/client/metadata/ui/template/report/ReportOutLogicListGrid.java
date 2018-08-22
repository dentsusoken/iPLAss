/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.template.report;

import java.util.LinkedHashMap;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogCondition;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogHandler;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogMode;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.MetaDataUtil;
import org.iplass.mtp.web.template.report.definition.GroovyReportOutputLogicDefinition;
import org.iplass.mtp.web.template.report.definition.JavaClassReportOutputLogicDefinition;
import org.iplass.mtp.web.template.report.definition.PoiReportType;
import org.iplass.mtp.web.template.report.definition.ReportOutputLogicDefinition;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 *
 * レポート出力ロジック一覧グリッド
 *
 * @author lis71n
 *
 */
public class ReportOutLogicListGrid extends ListGrid {

	private final String SCRIPT = "Groovy";
	private final String JAVACLASS = "JavaClass";

	/**
	 * コンストラクタ
	 *
	 * @param defName 選択Entity名
	 * @param service Service
	 */
	public ReportOutLogicListGrid() {
		// 横幅を画面の100%に
		setWidth100();
		// 縦幅
		setHeight(1);
		// 列を全て表示
		setShowAllColumns(true);
		// レコードを全て表示
		setShowAllRecords(true);
		// 列幅変更可能
		setCanResizeFields(true);
		setBodyOverflow(Overflow.VISIBLE);
		setOverflow(Overflow.VISIBLE);
		setCanSort(false);

		//grid内でのD&Dでの並べ替えを許可
		setCanDragRecordsOut(true);
		setCanAcceptDroppedRecords(true);
		setCanReorderRecords(true);

		// 各フィールド初期化
		ListGridField elNameField = new ListGridField(ReportOutLogicListGridRecord.ELNAME, "Type");
		elNameField.setWidth(150);
		ListGridField gpField = new ListGridField(ReportOutLogicListGridRecord.GP, "Value");

		// 各フィールドをListGridに設定
		setFields(elNameField, gpField);

		// レコードダブルクリックイベント設定
		addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				startReportOutputLogicEdit(false, (ReportOutLogicListGridRecord)getRecord(event.getRecordNum()));
			}
		});
	}

	public void setDefinition(PoiReportType definition) {
		ReportOutputLogicDefinition RepOutLogicDef = definition.getReportOutputLogicDefinition();

		if (RepOutLogicDef == null) { return; }

		//１件固定
		ReportOutLogicListGridRecord[] records = new ReportOutLogicListGridRecord[1];
		ReportOutLogicListGridRecord record = new ReportOutLogicListGridRecord();
		setElDefData(record, RepOutLogicDef);
		records[0] = record;

		setData(records);
	}

	public PoiReportType getEditDefinition(PoiReportType definition) {
		ReportOutputLogicDefinition result = null;

		ListGridRecord[] records = getRecords();

		if(records != null && records.length > 0){
			result = createElDef((ReportOutLogicListGridRecord)records[0]);

		}
		definition.setReportOutputLogicDefinition(result);
		return definition;
	}

	public ReportOutputLogicDefinition getReportOutputLogicDefinition() {
		ReportOutputLogicDefinition result = null;

		ListGridRecord[] records = getRecords();

		if(records != null && records.length > 0){
			result = createElDef((ReportOutLogicListGridRecord)records[0]);
		}
		return result;
	}

	private void setElDefData(ReportOutLogicListGridRecord record, ReportOutputLogicDefinition elDef) {
		if (elDef instanceof GroovyReportOutputLogicDefinition) {
			record.setElName(SCRIPT);

			GroovyReportOutputLogicDefinition sDef = (GroovyReportOutputLogicDefinition)elDef;
			// Script文字列
			record.setScript(sDef.getScript());
			record.setGeneralPurpus(sDef.getScript());

		} else if (elDef instanceof JavaClassReportOutputLogicDefinition) {
			record.setElName(JAVACLASS);

			JavaClassReportOutputLogicDefinition jcDef = (JavaClassReportOutputLogicDefinition)elDef;
			record.setClassName(jcDef.getClassName());
			record.setGeneralPurpus(jcDef.getClassName());
		} else {
		}
	}

	/**
	 *
	 * @param record
	 * @return
	 */
	private ReportOutputLogicDefinition createElDef(ReportOutLogicListGridRecord record) {
		ReportOutputLogicDefinition result = null;
		String elName = record.getElName();

		if (elName != null && elName.length() > 0) {
			if (SCRIPT.equals(elName)) {
				GroovyReportOutputLogicDefinition sDef = new GroovyReportOutputLogicDefinition();
				sDef.setScript(record.getScript());

				result = sDef;
			} else if (JAVACLASS.equals(elName)) {
				JavaClassReportOutputLogicDefinition jDef = new JavaClassReportOutputLogicDefinition();
				jDef.setClassName(record.getClassName());

				result = jDef;
			}
		}

		return result;
	}

	/**
	 *
	 *
	 * @param isNewRow
	 * @param record
	 */
	public void startReportOutputLogicEdit(boolean isNewRow, ReportOutLogicListGridRecord record) {
		ReportOutputLogicEditDialog dialog = new ReportOutputLogicEditDialog(isNewRow, record);
		dialog.show();
	}


	/**
	 * レポート出力ロジック編集用ダイアログ
	 *
	 * @author lis71n
	 *
	 */
	private class ReportOutputLogicEditDialog extends AbstractWindow {

		private static final int DEFAULT_WIDTH = 600;

		private boolean isNewRow = false;
		private ReportOutLogicListGridRecord target;

		//Type
		private DynamicForm typeItemForm;
		private SelectItem typeItem;

		//Main
		private VLayout mainLayout = new VLayout();

		//Script
		private DynamicForm scriptItemForm;
		private TextAreaItem scriptItem;

		//JavaClass
		private DynamicForm javaClassItemForm;
		private TextItem javaClassNameItem;


		private ReportOutputLogicEditDialog(boolean isNewRow, ReportOutLogicListGridRecord target) {
			this.isNewRow = isNewRow;
			this.target = target;

			initialize();
			dataInitialize();
			formVisibleChange();
		}

		private void initialize() {
			// ダイアログ本体のプロパティ設定
			setWidth(DEFAULT_WIDTH);
			setHeight(100);
			setTitle("ReportOutputLogic");
			setShowMinimizeButton(false);
			setShowMaximizeButton(true);	//最大化は可能に設定（スクリプト編集用）
			setCanDragResize(true);			//リサイズは可能に設定（スクリプト編集用）
			setIsModal(true);
			setShowModalMask(true);
			centerInPage();

			//---------------------------------
			//Type
			//---------------------------------
			typeItem = new SelectItem();
			typeItem.setTitle("Type");
			SmartGWTUtil.setRequired(typeItem);
			typeItem.addChangedHandler(new ChangedHandler() {
				public void onChanged(ChangedEvent event) {
					formVisibleChange();
				}
			});

			typeItemForm = new DynamicForm();
			typeItemForm.setMargin(5);
			typeItemForm.setNumCols(3);
			typeItemForm.setColWidths(100, "*", 100);
			typeItemForm.setAlign(Alignment.LEFT);
			typeItemForm.setItems(typeItem);

			mainLayout.setWidth100();
			mainLayout.setHeight100();
//			mainLayout.setMargin(10);

			//---------------------------------
			//Script
			//---------------------------------
			scriptItem = new TextAreaItem();
			scriptItem.setColSpan(2);
			scriptItem.setTitle("Script");
			scriptItem.setWidth("100%");
			scriptItem.setHeight("100%");
			SmartGWTUtil.setRequired(scriptItem);
			SmartGWTUtil.setReadOnlyTextArea(scriptItem);

			ButtonItem editScript = new ButtonItem("editScript", "Edit");
			editScript.setWidth(100);
			editScript.setStartRow(false);
			editScript.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

				@Override
				public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
					MetaDataUtil.showScriptEditDialog(ScriptEditorDialogMode.GROOVY_SCRIPT,
							SmartGWTUtil.getStringValue(scriptItem),
							ScriptEditorDialogCondition.TEMPLATE_REPORT_OUTPUT_LOGIC,
							"ui_metadata_template_report_ReportOutLogicListGrid_scriptHint",
							null,
							new ScriptEditorDialogHandler() {

								@Override
								public void onSave(String text) {
									scriptItem.setValue(text);
								}
								@Override
								public void onCancel() {
								}
							});
				}
			});

			scriptItemForm = new DynamicForm();
			scriptItemForm.setMargin(5);
			scriptItemForm.setNumCols(3);
			scriptItemForm.setColWidths(100, "*", 100);
			scriptItemForm.setWidth100();
			scriptItemForm.setHeight100();
			scriptItemForm.setAlign(Alignment.LEFT);
			scriptItemForm.setItems(new SpacerItem(), new SpacerItem(), editScript, scriptItem);

			//---------------------------------
			//Java Class
			//---------------------------------
			javaClassNameItem = new TextItem();
			javaClassNameItem.setTitle("Class Name");
			javaClassNameItem.setWidth("*");
			SmartGWTUtil.setRequired(javaClassNameItem);
			SmartGWTUtil.addHoverToFormItem(javaClassNameItem,
					AdminClientMessageUtil.getString("ui_metadata_template_report_ReportOutLogicListGrid_javaClassNameItemComment"));

			javaClassItemForm = new DynamicForm();
			javaClassItemForm.setMargin(5);
			javaClassItemForm.setNumCols(3);
			javaClassItemForm.setColWidths(100, "*", 50);
			javaClassItemForm.setHeight(25);
			javaClassItemForm.setItems(javaClassNameItem, SmartGWTUtil.createSpacer(50));

			//---------------------------------
			//Footer
			//---------------------------------
			IButton ok = new IButton("OK");
			ok.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if (!validate()) {
						return;
					}
					if (isNewRow) {
						//１件しか存在しないため、レコード削除
						ListGridRecord[] records = getRecords();
						if(records.length > 0) {
							removeData(records[0]);
						}
						addData(target);
					}
					updateRecordData();
					destroy();
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
			footer.setAlign(VerticalAlignment.CENTER);
			footer.setMembers(ok, cancel);

			addItem(typeItemForm);
			addItem(mainLayout);
			addItem(SmartGWTUtil.separator());
			addItem(footer);
		}

		private void dataInitialize() {
			LinkedHashMap<String, String> typeMap = new LinkedHashMap<String, String>();
			typeMap.put(SCRIPT, "Groovy");
			typeMap.put(JAVACLASS, "JavaClass");
			typeItem.setValueMap(typeMap);

			typeItem.setValue(target.getElName());

			scriptItem.setValue(target.getScript());

			javaClassNameItem.setValue(target.getClassName());

		}

		private void formVisibleChange() {
			typeItemForm.clearErrors(true);

			if (mainLayout.contains(scriptItemForm)) {
				scriptItemForm.clearErrors(true);
				mainLayout.removeMember(scriptItemForm);
			}
			if (mainLayout.contains(javaClassItemForm)) {
				javaClassItemForm.clearErrors(true);
				mainLayout.removeMember(javaClassItemForm);
			}

			String selectValType = SmartGWTUtil.getStringValue(typeItem);
			if (SCRIPT.equals(selectValType)) {
				mainLayout.addMember(scriptItemForm);
				setHeight(470);
				centerInPage();
			} else if (JAVACLASS.equals(selectValType)) {
				mainLayout.addMember(javaClassItemForm);
				setHeight(190);
			} else {
				setHeight(120);
			}
		}

		private boolean validate() {
			boolean isValidate = true;
			if (!typeItemForm.validate()) {
				isValidate = false;
			}
			if (mainLayout.contains(scriptItemForm)) {
				if (!scriptItemForm.validate()) {
					isValidate = false;
				}
			}
			if (mainLayout.contains(javaClassItemForm)) {
				if (!javaClassItemForm.validate()) {
					isValidate = false;
				}
			}
			return isValidate;
		}

		private void updateRecordData() {
			String selectValType = SmartGWTUtil.getStringValue(typeItem);
			target.setElName(selectValType);
			if (SCRIPT.equals(selectValType)) {
				target.setScript(SmartGWTUtil.getStringValue(scriptItem));
				target.setGeneralPurpus(SmartGWTUtil.getStringValue(scriptItem));

			} else if (JAVACLASS.equals(selectValType)) {
				target.setClassName(SmartGWTUtil.getStringValue(javaClassNameItem));
				target.setGeneralPurpus(SmartGWTUtil.getStringValue(javaClassNameItem));
			}
			updateData(target);
			refreshFields();
		}
	}
}
