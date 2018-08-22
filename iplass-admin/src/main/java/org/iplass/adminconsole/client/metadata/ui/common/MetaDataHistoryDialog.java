/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.common;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.io.download.PostDownloadFrame;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.definition.DefinitionInfo;
import org.iplass.mtp.definition.VersionHistory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.AutoFitWidthApproach;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class MetaDataHistoryDialog extends AbstractWindow {

	private MetaDataHistoryListPane metaDataHistoryListPane;
	private MetaDataServiceAsync service = MetaDataServiceFactory.get();
	private static final String EXPORT_ICON = "[SKIN]/actions/download.png";

	public MetaDataHistoryDialog(String className, final String definitionId, int curVersion) {

		VLayout layout = new VLayout();

		String title = "MetaData History Dialog";
		setTitle(title);
		setHeight(420);
		setWidth(400);
		setMargin(0);
		setMembersMargin(0);

		setShowMinimizeButton(false);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();

		metaDataHistoryListPane = new MetaDataHistoryListPane(className, definitionId, curVersion);

		layout.addMember(metaDataHistoryListPane);

		HLayout footer;

		footer = new HLayout(5);
		footer.setMargin(10);
		footer.setHeight(50);
		footer.setWidth100();
		footer.setAlign(VerticalAlignment.CENTER);

		IButton create = new IButton("Create");
		create.setIcon(EXPORT_ICON);
		create.setTitle("ConfigExport");
		create.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				ListGridRecord[] records = metaDataHistoryListPane.grid.getSelectedRecords();
				if (records == null || records.length == 0) {
					SC.say("Please select the export version.");
					return;
				}

				String versions = "";
				for (ListGridRecord record : records) {
					String version = record.getAttribute(FIELD_NAME.VERSION.name());

					if (versions.equals("")) {
						versions = versions + version;
					} else {
						versions = versions + "," + version;
					}
				}

				PostDownloadFrame frame = new PostDownloadFrame();
				frame.setAction(GWT.getModuleBaseURL() + "service/metadatahistoryconfigdownload")
					.addParameter("tenantId", String.valueOf(TenantInfoHolder.getId()))
					.addParameter("metadataPath", definitionId)
					.addParameter("versions", versions)
					.execute();
			}
		});
		footer.setMembers(create);

		HLayout labelLayout = new HLayout(5);
		labelLayout.setHeight(20);
		labelLayout.setWidth100();
		labelLayout.setAlign(Alignment.LEFT);
		labelLayout.setPadding(7);

		Label label = new Label(AdminClientMessageUtil.getString("ui_metadata_common_MetaDataHistoryDialog_label"));
		label.setHeight(30);
		label.setPadding(10);
		label.setAlign(Alignment.LEFT);
		label.setWrap(false);

		labelLayout.setMembers(label);

		addItem(labelLayout);
		addItem(layout);
		addItem(SmartGWTUtil.separator());
		addItem(footer);
	}

	private class MetaDataHistoryListPane extends HLayout {

		private HistoryInfoGrid grid;

		public MetaDataHistoryListPane(String className, final String definitionId, final int curVersion) {
			setMargin(10);
			setMembersMargin(10);
			grid = new HistoryInfoGrid();

			service.getHistoryById(TenantInfoHolder.getId(), className, definitionId, new AsyncCallback<DefinitionInfo>() {

				@Override
				public void onFailure(Throwable caught) {
				}

				@Override
				public void onSuccess(DefinitionInfo result) {
					List<ListGridRecord> records = new ArrayList<ListGridRecord>();

					for (VersionHistory versionHistory : result.getVersionHistory()) {

						ListGridRecord record = new ListGridRecord();
						if (versionHistory.getVersion() == curVersion) {
							record.setAttribute("cssText", "font-weight:700;");
						}
						record.setAttribute(FIELD_NAME.VERSION.name(), versionHistory.getVersion());
						record.setAttribute(FIELD_NAME.UPDATE_USER.name(), versionHistory.getUpdateBy());
						record.setAttribute(FIELD_NAME.UPDATE_DATE.name(), SmartGWTUtil.formatTimestamp(versionHistory.getUpdateDate()));

						records.add(record);
					}
					grid.setData(records.toArray(new ListGridRecord[]{}));
				}

			});

			addItem(grid);
		}
	}

	private enum FIELD_NAME {
		VERSION,
		UPDATE_USER,
		UPDATE_DATE
	}

	private class HistoryInfoGrid extends ListGrid {

		public HistoryInfoGrid() {
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

			//CheckBox選択設定
			setSelectionType(SelectionStyle.SIMPLE);
			setSelectionAppearance(SelectionAppearance.CHECKBOX);

			//この２つを指定することでcreateRecordComponentが有効
			setShowRecordComponents(true);
			setShowRecordComponentsByCell(true);

			ListGridField createDateField = new ListGridField(FIELD_NAME.VERSION.name(), "version");
			ListGridField updateUserField = new ListGridField(FIELD_NAME.UPDATE_USER.name(), "Update User");
			ListGridField updateDateField = new ListGridField(FIELD_NAME.UPDATE_DATE.name(), "Update Date");

			setFields(createDateField, updateUserField, updateDateField);
		}
	}

}
