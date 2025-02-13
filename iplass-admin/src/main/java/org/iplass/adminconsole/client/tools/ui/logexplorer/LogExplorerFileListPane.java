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

package org.iplass.adminconsole.client.tools.ui.logexplorer;

import java.util.LinkedHashMap;
import java.util.Map;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.io.download.PostDownloadFrame;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.GridActionImgButton;
import org.iplass.adminconsole.client.base.ui.widget.MtpListGrid;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.tools.data.logexplorer.LogFileDS;
import org.iplass.adminconsole.client.tools.data.logexplorer.LogFileDS.FIELD_NAME;
import org.iplass.adminconsole.shared.tools.dto.logexplorer.LogFileDownloadProperty;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.SortSpecifier;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler;
import com.smartgwt.client.widgets.grid.events.SortChangedHandler;
import com.smartgwt.client.widgets.grid.events.SortEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class LogExplorerFileListPane extends VLayout {

	private static final String REFRESH_ICON = "[SKIN]/actions/refresh.png";
	private static final String EXPORT_ICON = "[SKINIMG]/actions/download.png";

	/** Limit選択用Map */
	private static LinkedHashMap<Integer, String> limitValueMap;
	static {
		limitValueMap = new LinkedHashMap<Integer, String>();
		limitValueMap.put(-1, "-1");
		limitValueMap.put(50, "50");
		limitValueMap.put(100, "100");
		limitValueMap.put(200, "200");
		limitValueMap.put(500, "500");
	}

	private ComboBoxItem limitItem;

	private ListGrid grid;

	/**
	 * コンストラクタ
	 */
	public LogExplorerFileListPane() {

		//レイアウト設定
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
		lblTitle.setContents("Log Files");
		toolStrip.addMember(lblTitle);

		toolStrip.addFill();

		limitItem = new ComboBoxItem();
		limitItem.setTitle("Limit");
		limitItem.setValueMap(limitValueMap);
		// limitItem.setValidators(new IsIntegerValidator());
		limitItem.setValue(100);
		toolStrip.addFormItem(limitItem);

		final ToolStripButton refreshButton = new ToolStripButton();
		refreshButton.setIcon(REFRESH_ICON);
		refreshButton.setTooltip(
				SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_tools_logexplorer_LogExplorerListPane_refreshFileList")));
		refreshButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				refreshGrid();
			}
		});
		toolStrip.addButton(refreshButton);

		grid = new MtpListGrid() {
			@Override
			protected Canvas createRecordComponent(final ListGridRecord record, Integer colNum) {
				String fieldName = this.getFieldName(colNum);
				if (fieldName.equals("download")) {

					GridActionImgButton recordCanvas = new GridActionImgButton();
					recordCanvas.setActionButtonSrc(EXPORT_ICON);
					recordCanvas.setActionButtonPrompt(
							SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_tools_logexplorer_LogExplorerListPane_fileDownload")));
					recordCanvas.addActionClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							String fullPath = record.getAttribute(FIELD_NAME.FULL_PATH.name());
							download(fullPath);
						}
					});
					return recordCanvas;
				}
				return null;
			}

		};

		grid.setWidth100();
		grid.setHeight100();

		//ソートを許可
		grid.setCanSort(true);

		//Filterを許可
		grid.setShowFilterEditor(true);
		//正規表現でFilterするため標準の機能を非表示にする
		grid.setShowFilterEditorHovers(false);
		grid.setCanShowFilterEditor(false);
		grid.setAllowFilterOperators(false);

		//この２つを指定することでcreateRecordComponentが有効
		grid.setShowRecordComponents(true);
		grid.setShowRecordComponentsByCell(true);

		grid.setInitialSort(new SortSpecifier(FIELD_NAME.SIZE.name(), SortDirection.ASCENDING));

		grid.addSortChangedHandler(new SortChangedHandler() {

			@Override
			public void onSortChanged(SortEvent event) {
				// 標準のSort処理を行わず明示的にFetchする
				fetchGrid(grid.getFilterEditorCriteria());
			}
		});

		grid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {

			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				// 標準のFilter処理を行わず明示的にFetchする
				event.cancel();
				fetchGrid(event.getCriteria());
			}
		});

		addMember(toolStrip);
		addMember(grid);

		refreshGrid();
	}

	/**
	 * 一覧のFetch処理
	 *
	 * @param filter フィルタ条件
	 */
	private void fetchGrid(Criteria filter) {

		// サーバーにリクエストが飛ぶように一意の条件を付与
		Criteria condition = new Criteria("dummy", String.valueOf(System.currentTimeMillis()));

		// Limit指定。未指定または文字列など変換不可の場合は-1
		if (limitItem.getValueAsInteger() == null) {
			limitItem.setValue(-1);
		}
		condition.addCriteria(LogFileDS.LIMIT, limitItem.getValueAsInteger());

		// 画面上でのフィルタ条件
		if (filter != null) {
			Map<?, ?> criteriaMap = filter.getValues();
			for (Object key : criteriaMap.keySet()) {
				if (!key.equals("dummy")) {
					condition.addCriteria((String) key, criteriaMap.get(key));
				}
			}
		}

		grid.fetchData(condition);
	}

	/**
	 * 一覧のRefresh処理
	 */
	private void refreshGrid() {
		LogFileDS ds = LogFileDS.getInstance();
		grid.clearSort();
		grid.setDataSource(ds);

		//（参考）setFieldsは、setDataSource後に指定しないと効かない

		//ボタンを表示したいためListGridFieldを指定
		ListGridField pathField = new ListGridField(FIELD_NAME.PATH.name(), "Path");
		pathField.setCanFilter(true);
		ListGridField lastModifiedField = new ListGridField(FIELD_NAME.LAST_MODIFIED.name(), "Last Modified");
		lastModifiedField.setWidth(120);
		lastModifiedField.setCanFilter(true);
		ListGridField sizeField = new ListGridField(FIELD_NAME.SIZE.name(), "Size");
		sizeField.setWidth(100);
		sizeField.setAlign(Alignment.RIGHT);
		sizeField.setCanFilter(false);
		sizeField.setCellFormatter(new CellFormatter() {
			@Override
			public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
				long size = record.getAttributeAsLong(FIELD_NAME.SIZE.name());
				return getSizeString(size);
			}
		});
		ListGridField downloadField = new ListGridField("download", " ");
		downloadField.setWidth(25);
		downloadField.setCanSort(false);
		downloadField.setCanFilter(false);

		grid.setFields(pathField, lastModifiedField, sizeField, downloadField);

		//初期ソートはPathを設定
		grid.setSort(new SortSpecifier(FIELD_NAME.PATH.name(), SortDirection.ASCENDING));

		//SortChangedHandler側でfetch
		//grid.fetchData();
	}

	private String getSizeString(long size) {
		if (size > (1024 * 1024 * 1024)) {
			return (size / (1024 * 1024 * 1024)) + " GB";
		} else if (size > (1024 * 1024)) {
			return (size / (1024 * 1024)) + " MB";
		} else if (size > 1024) {
			return (size / 1024) + " KB";
		} else {
			return size + " Byte";
		}
	}

	private void download(String fullPath) {

		PostDownloadFrame frame = new PostDownloadFrame();
		frame.setAction(GWT.getModuleBaseURL() + LogFileDownloadProperty.ACTION_URL)
				.addParameter(LogFileDownloadProperty.TENANT_ID, String.valueOf(TenantInfoHolder.getId()))
				.addParameter(LogFileDownloadProperty.TARGET_PATH, fullPath)
				.execute();

	}
}
