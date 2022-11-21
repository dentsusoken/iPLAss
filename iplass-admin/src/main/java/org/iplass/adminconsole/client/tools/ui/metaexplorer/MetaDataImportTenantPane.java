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

package org.iplass.adminconsole.client.tools.ui.metaexplorer;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.screen.ScreenModuleBasedUIFactoryHolder;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.data.tenant.BaseTenantDS;
import org.iplass.adminconsole.client.metadata.data.tenant.TenantDSCellFormatter;
import org.iplass.mtp.tenant.Tenant;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.GroupStartOpen;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.GroupNode;
import com.smartgwt.client.widgets.grid.GroupTitleRenderer;
import com.smartgwt.client.widgets.grid.GroupValueFunction;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.DataArrivedEvent;
import com.smartgwt.client.widgets.grid.events.DataArrivedHandler;
import com.smartgwt.client.widgets.grid.events.GroupByCompleteEvent;
import com.smartgwt.client.widgets.grid.events.GroupByCompleteHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 */
public class MetaDataImportTenantPane extends VLayout {

	private static final String RESOURCE_PREFIX = "ui_tools_metaexplorer_MetaDataImportTenantPane_";

	private ListGrid grid;

	private BaseTenantDS dataSource;

	private TenantSelectActionCallback callback;

	/**
	 * コンストラクタ
	 */
	public MetaDataImportTenantPane(Tenant fileTenant, TenantSelectActionCallback callback) {
		this.callback = callback;

		//レイアウト設定
		setWidth100();
		setHeight100();
		setMargin(10);

		//------------------------
		//Lable Title
		//------------------------
		Label title = new Label(getResourceString("tenantPropertySelect"));
		title.setHeight(40);
		title.setWidth100();

		grid = new ListGrid(){
			//TODO 差異があるところの強調表示
			//実装自体はできているがもう少しテストするため
			/*
			@Override
		    protected String getBaseStyle(ListGridRecord record, int rowNum, int colNum) {

				if (colNum != 4) {
					return super.getBaseStyle(record, rowNum, colNum);
				}
				if (record.getEnabled() && dataSource.isDiffImportProperty(record)){
					return "cell importTenantPaneGridDiffRow";
				} else {
					return super.getBaseStyle(record, rowNum, colNum);
				}
			}
			*/
		};
		grid.setCanEdit(false);
		grid.setWidth100();
		grid.setHeight100();
		grid.setCellHeight(22);

		grid.setShowAllRecords(true);
		grid.setLeaveScrollbarGap(false);
		grid.setShowFilterEditor(false);

		grid.setCanSort(false);
		grid.setCanAutoFitFields(false);
		grid.setCanFreezeFields(false);
		grid.setCanGroupBy(false);
		grid.setCanPickFields(false);
		grid.setCanDragRecordsOut(false);
		grid.setCanDrop(false);

		grid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		grid.setShowSelectedStyle(false);

		//一覧Group設定
		grid.setGroupStartOpen(GroupStartOpen.ALL);	//グループ時に全部展開して表示
		//grid.setGroupByField("title");	//これでグループ化すると選択状態が消えてしまうため、明示的にgroupByするように変更
		//grid.setGroupIndentSize(100);	//インデントされないのでダミーフィールド作成

		ListGridField indentField = new ListGridField("indent", " ");
		indentField.setWidth(15);

		ListGridField titleField = new ListGridField("title", getResourceString("property"));
		titleField.setGroupValueFunction(new GroupValueFunction() {

			@Override
			public Object getGroupValue(Object value, ListGridRecord record,
					ListGridField field, String fieldName, ListGrid grid) {
				//Group値をレコードの"category"から取得する
				return record.getAttribute("category");
			}
		});
		titleField.setGroupTitleRenderer(new GroupTitleRenderer() {

			@Override
			public String getGroupTitle(Object groupValue, GroupNode groupNode,
					ListGridField field, String fieldName, ListGrid grid) {
				//上で返すGroup値をタイトルに指定する
				return BaseTenantDS.getTenantCategoryName((String) groupValue);
			}
		});

		ListGridField valueField = new ListGridField("displayValue", getResourceString("curValue"));
		valueField.setCellFormatter(new TenantDSCellFormatter());

		ListGridField fileValueField = new ListGridField("fileDisplayValue", getResourceString("importValue"));
		fileValueField.setCellFormatter(new TenantDSCellFormatter());

		grid.setFields(indentField, titleField, valueField, fileValueField);

		grid.setSelectionProperty("mySelected");	//選択状態保持用

		dataSource = ScreenModuleBasedUIFactoryHolder.getFactory().createTenantImportSelectDataSource(fileTenant);
		grid.setDataSource(dataSource);
		grid.setAutoFetchData(true);
		grid.setShowAsynchGroupingPrompt(false);

		grid.addDataArrivedHandler(new DataArrivedHandler() {

			@Override
			public void onDataArrived(DataArrivedEvent event) {

				//setGroupByFieldでグループ化すると初期選択がクリアされてしまうので、ここでグループ化
				grid.groupBy("title");

				//インポート用の設定を行う
				//レコードごとに選択を行うと処理がかなり遅いので、
				//gridに対してSelectionPropertyを指定し、その値をtrueにすることで初期選択する(高速)
				//ただしgrid.setSelectionPropertyは描画前に行う必要がある

				/*
				//grid.setSelectionProperty("mySelected");	//これは描画前に行わないといけない
				for (ListGridRecord record : grid.getRecords()) {
					if (Category.BasicInfo == Category.valueOf(record.getAttribute("category"))) {
						//基本情報は選択不可
						record.setEnabled(false);
					} else if (Category.MailSendSetting == Category.valueOf(record.getAttribute("category"))) {
						//メール情報は初期未選択
					} else if ("urlForRequest".equals(record.getAttribute("name"))) {
						//リクエストパス構築用テナントURLは初期未選択
					} else if ("usePreview".equals(record.getAttribute("name"))) {
						//日付プレビュー表示機能は初期未選択
					} else {
						//それ以外はデフォルト選択
						record.setAttribute("mySelected", true);
					}
				}
				*/

				/*
				List<ListGridRecord> selectRecords = new ArrayList<ListGridRecord>();
				//インポート用の設定を行う
				for (ListGridRecord record : grid.getRecords()) {
					if (TenantImportSelectDS.getRS("basicInfo").equals(record.getAttribute("category"))) {
						//基本情報は選択不可
						record.setEnabled(false);
					} else if (TenantImportSelectDS.getRS("mailSendSetting").equals(record.getAttribute("category"))) {
						//メール情報は初期未選択
					} else {
						//それ以外はデフォルト選択
						//grid.selectRecord(record);	//遅い
						selectRecords.add(record);
					}
				}
				grid.selectRecords(selectRecords.toArray(new ListGridRecord[]{}));	//遅い
				*/
				/*
				List<Integer> selectRecords = new ArrayList<Integer>();
				for (ListGridRecord record : grid.getRecords()) {
					if (TenantImportSelectDS.getRS("basicInfo").equals(record.getAttribute("category"))) {
						//基本情報は選択不可
						record.setEnabled(false);
					} else if (TenantImportSelectDS.getRS("mailSendSetting").equals(record.getAttribute("category"))) {
						//メール情報は初期未選択
					} else {
						//それ以外はデフォルト選択
						selectRecords.add(grid.getRecordIndex(record));
					}
				}
				int[] selectRecordsInt = new int[selectRecords.size()];
				for (int i = 0; i < selectRecords.size(); i++) {
					selectRecordsInt[i] = selectRecords.get(i).intValue();
				}
				grid.selectRecords(selectRecordsInt);	//遅い
				*/
				/*
				StringBuilder sbSelectedState = new StringBuilder();
				sbSelectedState.append("[");
				for (ListGridRecord record : grid.getRecords()) {
					if (TenantImportSelectDS.getRS("basicInfo").equals(record.getAttribute("category"))) {
						//基本情報は選択不可
						record.setEnabled(false);
					} else if (TenantImportSelectDS.getRS("mailSendSetting").equals(record.getAttribute("category"))) {
						//メール情報は初期未選択
					} else {
						//それ以外はデフォルト選択
						sbSelectedState.append("{name:\"" + record.getAttribute("name") + "\"},");
					}
				}
				sbSelectedState.deleteCharAt(sbSelectedState.length() - 1);
				sbSelectedState.append("]");
				grid.setSelectedState(sbSelectedState.toString());	//遅い
				*/
			}
		});

		grid.addGroupByCompleteHandler(new GroupByCompleteHandler() {

			@Override
			public void onGroupByComplete(GroupByCompleteEvent event) {
				SmartGWTUtil.hideProgress();
			}
		});

		HLayout footer = new HLayout(5);
		footer.setMargin(5);
		footer.setHeight(20);
		footer.setWidth100();
		//footer.setAlign(Alignment.LEFT);
		footer.setAlign(VerticalAlignment.CENTER);

		IButton importButton = new IButton("Import");
		importButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				selectFinish();
			}
		});

		IButton backButton = new IButton("Back");
		backButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				back();
			}
		});

		footer.setMembers(importButton, backButton);

		addMember(title);
		addMember(grid);
		addMember(footer);
	}

	/**
	 * インポートボタン処理
	 */
	private void selectFinish() {

//		if (grid.getSelectedRecords().length == 0) {	//setSelectionPropertyを指定しているとうまくいかない
		Record[] selectedRecord = grid.getRecordList().findAll("mySelected", true);
		if (selectedRecord == null || selectedRecord.length == 0) {
			SC.ask(getResourceString("confirm"), getResourceString("excludeTenant"),  new BooleanCallback() {

				@Override
				public void execute(Boolean value) {
					if (value) {
						//テナントはnullで返す
						callback.selected(null);
					}
				}
			});
		} else {
			Tenant tenant = dataSource.getUpdateData();

			for (Record record : selectedRecord) {
				String name = record.getAttribute("name");
				dataSource.applyToTenantField(tenant, name, "fileValue");
			}
			callback.selected(tenant);
		}
	}

	/**
	 * 戻るボタン処理
	 */
	private void back() {

		callback.canceled();
	}

	private String getResourceString(String key) {
		return AdminClientMessageUtil.getString(RESOURCE_PREFIX + key);
	}

	public interface TenantSelectActionCallback {

		public void selected(Tenant importTenant);

		public void canceled();
	}

}
