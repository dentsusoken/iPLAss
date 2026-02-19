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

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.mtp.webapi.definition.WebApiDefinition;
import org.iplass.mtp.webapi.definition.WebApiResultAttribute;

import com.smartgwt.client.types.AutoFitWidthApproach;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * WebAPIメタデータ 結果設定入力領域
 */
public class ResultPane extends VLayout {

	static final String RESULT = "result";
	static final String DATA_TYPE = "dataType";

	private ResultGrid grid;

	/**
	 * コンストラクタ
	 */
	public ResultPane() {
		setMargin(5);
		setAutoHeight();

		Label caption = new Label("Results");
		caption.setHeight(21);

		grid = new ResultGrid();
		grid.addRecordDoubleClickHandler(this::onDoubleClickRecord);

		IButton addMap = new IButton("Add");
		addMap.addClickHandler(this::onClickAddButton);

		IButton delMap = new IButton("Remove");
		delMap.addClickHandler(this::onClickRemoveButton);

		HLayout mapButtonPane = new HLayout(5);
		mapButtonPane.setMargin(5);
		mapButtonPane.addMember(addMap);
		mapButtonPane.addMember(delMap);

		addMember(caption);
		addMember(grid);
		addMember(mapButtonPane);
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
	 * WebApiDefinition の情報を、本設定画面へ反映します。
	 * @param definition WebApiDefinition
	 */
	@SuppressWarnings("deprecation")
	public void setDefinition(WebApiDefinition definition) {
		List<ListGridRecord> records = new ArrayList<>();
		if (null != definition.getResponseResults() && 0 < definition.getResponseResults().length) {
			for (WebApiResultAttribute attribute : definition.getResponseResults()) {
				ListGridRecord listGridrecord = new ListGridRecord();
				listGridrecord.setAttribute(RESULT, attribute.getName());
				listGridrecord.setAttribute(DATA_TYPE, attribute.getDataType());

				records.add(listGridrecord);
			}
		}

		// TODO 互換性維持のため設定を反映。results を削除した場合は、以下の if ブロックも削除する。
		if (null != definition.getResults() && 0 < definition.getResults().length) {
			for (String name : definition.getResults()) {
				boolean isNotContainsName = records.stream().map(r -> r.getAttributeAsString(RESULT)).filter(n -> n.equals(name)).findAny().isEmpty();
				if (isNotContainsName) {
					ListGridRecord listGridrecord = new ListGridRecord();
					listGridrecord.setAttribute(RESULT, name);
					// name のみ設定。dataType は設定しない。
					records.add(listGridrecord);
				}
			}
		}

		grid.setData(records.toArray(new ListGridRecord[records.size()]));
	}

	/**
	 * 編集されたWebAPIDefinition情報を返します。
	 *
	 * @param definition 編集前のWebAPIDefinition情報
	 * @return 編集WebAPIDefinition情報
	 */
	@SuppressWarnings("deprecation")
	public WebApiDefinition getEditDefinition(WebApiDefinition definition) {
		ListGridRecord[] records = grid.getRecords();
		if (records == null || records.length == 0) {
			return definition;
		}

		// WebApiDefinitionのresponseResultsを設定
		WebApiResultAttribute[] responseResults = new WebApiResultAttribute[records.length];
		for (int i = 0; i < records.length; i++) {
			ListGridRecord rec = records[i];
			WebApiResultAttribute attribute = new WebApiResultAttribute();
			attribute.setName(rec.getAttribute(RESULT));
			attribute.setDataType(rec.getAttribute(DATA_TYPE));

			responseResults[i] = attribute;
		}
		definition.setResponseResults(responseResults);

		// TODO 互換性維持のため results にも設定を保存する。大きなバージョンアップのタイミングで削除する予定。
		String[] results = new String[responseResults.length];
		for (int i = 0; i < responseResults.length; i++) {
			results[i] = responseResults[i].getName();
		}
		definition.setResults(results);

		return definition;
	}

	/**
	 * レコードのダブルクリック時処理
	 * @param event レコードのダブルクリックイベント
	 */
	private void onDoubleClickRecord(RecordDoubleClickEvent event) {
		final ListGridRecord gridRecord = event.getRecord();
		String name = gridRecord.getAttributeAsString(RESULT);
		String dataType = gridRecord.getAttributeAsString(DATA_TYPE);

		ResultEditDialog.DialogValue value = new ResultEditDialog.DialogValue(name, dataType);

		ResultEditDialog dialog = new ResultEditDialog();
		dialog.addDataChangeHandler(dataChangeEvent -> {
			ResultEditDialog.DialogValue result = dataChangeEvent.getValueObject(ResultEditDialog.DialogValue.class);
			gridRecord.setAttribute(RESULT, result.getName());
			gridRecord.setAttribute(DATA_TYPE, result.getDataType());
			// 既存レコードを更新
			grid.updateData(gridRecord);
			grid.refreshFields();
		});
		dialog.setDialogValue(value);
		dialog.show();
	}

	/**
	 * Add ボタン押下時処理
	 * @param event クリックイベント
	 */
	private void onClickAddButton(ClickEvent event) {
		ResultEditDialog dialog = new ResultEditDialog();
		dialog.addDataChangeHandler(dataChangeEvent -> {
			ResultEditDialog.DialogValue result = dataChangeEvent.getValueObject(ResultEditDialog.DialogValue.class);

			ListGridRecord gridRecord = new ListGridRecord();
			gridRecord.setAttribute(RESULT, result.getName());
			gridRecord.setAttribute(DATA_TYPE, result.getDataType());
			// レコードを新規追加
			grid.addData(gridRecord);
			grid.refreshFields();
		});
		dialog.show();
	}

	/**
	 * Remove ボタン押下時処理
	 * @param event クリックイベント
	 */
	private void onClickRemoveButton(ClickEvent event) {
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

			ListGridField nameField = new ListGridField(RESULT, AdminClientMessageUtil.getString("ui_metadata_ui_webapi_ResultPane_attributeName"));
			nameField.setRequired(Boolean.TRUE);

			ListGridField dataTypeField = new ListGridField(DATA_TYPE, AdminClientMessageUtil.getString("ui_metadata_ui_webapi_ResultPane_dataType"));
			dataTypeField.setRequired(Boolean.FALSE);

			setFields(nameField, dataTypeField);
		}
	}
}
