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

package org.iplass.adminconsole.client.metadata.ui.action;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataViewGridButton;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.MetaDataItemMenuTreeNode;
import org.iplass.adminconsole.client.metadata.ui.action.result.ResultEditDialog;
import org.iplass.mtp.web.actionmapping.definition.ActionMappingDefinition;
import org.iplass.mtp.web.actionmapping.definition.result.ResultDefinition;
import org.iplass.mtp.web.actionmapping.definition.result.StaticResourceResultDefinition;
import org.iplass.mtp.web.actionmapping.definition.result.TemplateResultDefinition;
import org.iplass.mtp.web.staticresource.definition.StaticResourceDefinition;
import org.iplass.mtp.web.template.definition.TemplateDefinition;

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

public class ResultGridPane extends VLayout {

	private enum FIELD_NAME {
		SHOW_ICON,
		STATUS,
		EXCEPTION_CLASS_NAME,
		RESULT_TYPE,
		SUMMARY,
		VALUE_OBJECT,
	}

	private ResultGrid grid;

	public ResultGridPane() {
		setMargin(5);
		setAutoHeight();

		HLayout captionComposit = new HLayout(5);
		captionComposit.setHeight(25);

		Label caption = new Label("Results:");
		caption.setWrap(false);
		caption.setHeight(21);
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
				+ AdminClientMessageUtil.getString("ui_metadata_action_ResultGridPane_statusComment1")
				+ "</ul>");
		captionComposit.addMember(captionHint);

		grid = new ResultGrid();
		grid.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				editResult((ListGridRecord)event.getRecord());
			}
		});

		IButton addResult = new IButton("Add");
		addResult.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				addResult();
			}
		});

		IButton delResult = new IButton("Remove");
		delResult.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				deleteResult();
			}
		});

		HLayout resultButtonPane = new HLayout(5);
		resultButtonPane.setMargin(5);
		resultButtonPane.addMember(addResult);
		resultButtonPane.addMember(delResult);

		addMember(captionComposit);
		addMember(grid);
		addMember(resultButtonPane);
	}

	public void setResults(ResultDefinition[] results) {
		if (results != null) {
			List<ListGridRecord> records = new ArrayList<ListGridRecord>();
			for (ResultDefinition result : results) {
				records.add(createRecord(result, null));
			}
			grid.setData(records.toArray(new ListGridRecord[]{}));
		}
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
	 * 編集されたActionMappingDefinition情報を返します。
	 *
	 * @return 編集ActionMappingDefinition情報
	 */
	public ActionMappingDefinition getEditDefinition(ActionMappingDefinition definition) {

		ListGridRecord[] records = grid.getRecords();
		if (records == null || records.length == 0) {
			return definition;
		}

		ResultDefinition[] results = new ResultDefinition[records.length];
		int i = 0;
		for (ListGridRecord record : records) {
			ResultDefinition result = (ResultDefinition)record.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name());
			results[i] = result;
			i++;
		}
		definition.setResult(results);

		return definition;
	}

	public void addResult(MetaDataItemMenuTreeNode itemNode) {

		if (itemNode.getDefinitionClassName().equals(TemplateDefinition.class.getName())) {
			addTemplate(itemNode);
		} else if (itemNode.getDefinitionClassName().equals(StaticResourceDefinition.class.getName())) {
			addStaticResource(itemNode);
		}
	}

	private void addTemplate(MetaDataItemMenuTreeNode itemNode) {

		TemplateResultDefinition result = new TemplateResultDefinition();
		result.setTemplateName(itemNode.getDefName());
		addResultDefinition(result);
	}

	private void addStaticResource(MetaDataItemMenuTreeNode itemNode) {

		StaticResourceResultDefinition result = new StaticResourceResultDefinition();
		result.setStaticResourceName(itemNode.getDefName());
		addResultDefinition(result);
	}

	private void addResultDefinition(ResultDefinition result) {

		boolean showDialog = false;
		ListGridRecord[] records = grid.getRecords();
		if (records == null || records.length == 0) {
			result.setCommandResultStatus("*");
		} else {
			//既にResultが存在する場合は、ステータスを未指定でダイアログ表示
			showDialog = true;
		}
		ListGridRecord newRecord = createRecord(result, null);
		grid.addData(newRecord);
		grid.refreshFields();

		if (showDialog) {
			editResult(newRecord);
		}
	}

	private ListGridRecord createRecord(ResultDefinition definition, ListGridRecord record) {
		if (record == null) {
			record = new ListGridRecord();
		}
		record.setAttribute(FIELD_NAME.STATUS.name(), definition.getCommandResultStatus());
		record.setAttribute(FIELD_NAME.EXCEPTION_CLASS_NAME.name(), definition.getExceptionClassName());
		record.setAttribute(FIELD_NAME.RESULT_TYPE.name(), ResultType.valueOf(definition).displayName());
		record.setAttribute(FIELD_NAME.SUMMARY.name(), definition.summaryInfo());
		record.setAttribute(FIELD_NAME.VALUE_OBJECT.name(), definition);
		return record;
	}

	private void addResult() {
		editResult(null);
	}

	private void editResult(final ListGridRecord record) {
		final ResultEditDialog dialog = new ResultEditDialog();
		dialog.addDataChangeHandler(new DataChangedHandler() {

			@Override
			public void onDataChanged(DataChangedEvent event) {
				ResultDefinition param = event.getValueObject(ResultDefinition.class);
				ListGridRecord newRecord = createRecord(param, record);
				if (record != null) {
					grid.updateData(newRecord);
				} else {
					//追加
					grid.addData(newRecord);
				}
				grid.refreshFields();
			}
		});

		if (record != null) {
			dialog.setDefinition(
					(ResultDefinition)record.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name()));
		}
		dialog.show();
	}

	private void deleteResult() {
		grid.removeSelectedData();
	}

	private class ResultGrid extends ListGrid {

		public ResultGrid() {
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

			ListGridField showMetaButtonField = new ListGridField(FIELD_NAME.SHOW_ICON.name(), " ");
			showMetaButtonField.setWidth(25);
			ListGridField statusField = new ListGridField(FIELD_NAME.STATUS.name(), "Status");
			statusField.setWidth(200);
			ListGridField exceptionClassNameField = new ListGridField(FIELD_NAME.EXCEPTION_CLASS_NAME.name(), "Exception Class Name");
			exceptionClassNameField.setWidth(200);
			ListGridField resultTypeField = new ListGridField(FIELD_NAME.RESULT_TYPE.name(), "Type");
			resultTypeField.setWidth(100);
			ListGridField summaryField = new ListGridField(FIELD_NAME.SUMMARY.name(), "Value");

			setFields(showMetaButtonField, statusField, exceptionClassNameField, resultTypeField, summaryField);
		}

		@Override
		protected Canvas createRecordComponent(final ListGridRecord record, Integer colNum) {
			final String fieldName = this.getFieldName(colNum);
			if (FIELD_NAME.SHOW_ICON.name().equals(fieldName)) {

				//typeがTemplate,StaticResourceの場合のみ表示
				String type = record.getAttributeAsString(FIELD_NAME.RESULT_TYPE.name());
				if (ResultType.TEMPLATE.displayName().equals(type)){
					MetaDataViewGridButton button = new MetaDataViewGridButton(TemplateDefinition.class.getName());
					button.setMetaDataShowClickHandler(new MetaDataViewGridButton.MetaDataShowClickHandler() {
						@Override
						public String targetDefinitionName() {
							TemplateResultDefinition definition = (TemplateResultDefinition)record.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name());
							return definition.getTemplateName();
						}
					});
					return button;

				} else if (ResultType.STATICRESOURCE.displayName().equals(type)){
					MetaDataViewGridButton button = new MetaDataViewGridButton(StaticResourceDefinition.class.getName());
					button.setMetaDataShowClickHandler(new MetaDataViewGridButton.MetaDataShowClickHandler() {
						@Override
						public String targetDefinitionName() {
							StaticResourceResultDefinition definition = (StaticResourceResultDefinition)record.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name());
							return definition.getStaticResourceName();
						}
					});
					return button;
				}
			}
			return null;
		}
	}

}
