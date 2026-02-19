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

package org.iplass.adminconsole.client.tools.ui.metaexplorer;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.iplass.adminconsole.client.base.event.AdminConsoleGlobalEventBus;
import org.iplass.adminconsole.client.base.event.ViewMetaDataEvent;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.shared.metadata.dto.MetaDataInfo;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * MetaDataステータスエラー通知パネル
 */
public class StatusErrorListPane extends HLayout {

	//Owner
	private Layout workspace;

	private MetaDataServiceAsync service = MetaDataServiceFactory.get();

	/**
	 * コンストラクタ
	 */
	public StatusErrorListPane(Layout workspace, LinkedHashMap<String, String> allResult) {
		this.workspace = workspace;

		//レイアウト設定
		setWidth100();
//		setAutoHeight();
		setHeight("50%");
//		setMargin(6);
		setMembersMargin(5);
		setAlign(Alignment.LEFT);
		setOverflow(Overflow.VISIBLE);

		addMember(new StatusErrorWindow(allResult));
	}

	private class StatusErrorWindow extends AbstractWindow {

		public StatusErrorWindow(LinkedHashMap<String, String> allResult) {
			setWidth100();
			setTitle("Status Check Error");
			setHeaderIcon("exclamation.png");
			setHeight100();
			setCanDragReposition(false);
			setCanDragResize(true);

			addItem(new StatusErrorMessagePane(allResult));
		}

		@Override
		protected boolean onPreDestroy() {
			workspace.removeMember(StatusErrorListPane.this);
			return true;
		}
	}

	private class StatusErrorMessagePane extends VLayout {

		public StatusErrorMessagePane(LinkedHashMap<String, String> allResult) {
			setWidth100();
			setHeight100();
			setPadding(10);
//			setBackgroundColor("#CCFFFF");

			Label title = new Label(AdminClientMessageUtil.getString("ui_tools_metaexplorer_StatusErrorListPane_errCheckSett"));
			title.setHeight(20);
			title.setWidth100();

			ListGrid messageGrid = new ListGrid();
			messageGrid.setWidth100();
			messageGrid.setHeight(100);
			messageGrid.setShowAllRecords(true);
			messageGrid.setWrapCells(true);
			messageGrid.setLeaveScrollbarGap(false);	//falseで縦スクロールバー領域が自動表示制御される
			messageGrid.setShowRowNumbers(true);		//行番号表示

			messageGrid.setAutoFitData(Autofit.HORIZONTAL);
//			messageGrid.setAutoFitMaxHeight(56);

			ListGridField pathField = new ListGridField("path", AdminClientMessageUtil.getString("ui_tools_ui_metaexplorer_StatusErrorListPane_path"), 200);
	        ListGridField messageField = new ListGridField("message", AdminClientMessageUtil.getString("ui_tools_ui_metaexplorer_StatusErrorListPane_message"));
	        messageGrid.setFields(pathField, messageField);

	        ListGridRecord[] records = new ListGridRecord[allResult.size()];
	        int row = 0;
	        for (Entry<String, String> entry : allResult.entrySet()) {
	        	records[row] = new ListGridRecord();
	        	records[row].setAttribute("path", entry.getKey());
	        	records[row].setAttribute("message", entry.getValue());
	        	row++;
	        }
	        messageGrid.setData(records);

			Label msgTitle = new Label(AdminClientMessageUtil.getString("ui_tools_metaexplorer_StatusErrorListPane_errMessage"));
			msgTitle.setHeight(20);
			msgTitle.setWidth100();

			final Canvas msgContents = new Canvas();
			msgContents.setHeight100();
			msgContents.setWidth100();
			msgContents.setOverflow(Overflow.VISIBLE);
			msgContents.setCanSelectText(true);
			msgContents.setBorder("1px solid silver");
			msgContents.setAlign(Alignment.LEFT);
//			msgContents.setContents("<font color=\"red\">" + message + "</font>");

	        messageGrid.addRecordClickHandler(new RecordClickHandler() {

				@Override
				public void onRecordClick(RecordClickEvent event) {
					Record record = event.getRecord();
					if (record == null) {
						return;
					}

					String message = record.getAttribute("message");
					msgContents.setContents("<font color=\"red\">" + message + "</font>");
				}
			});

	        messageGrid.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {

				@Override
				public void onRecordDoubleClick(RecordDoubleClickEvent event) {
					Record record = event.getRecord();
					if (record == null) {
						return;
					}

					final String path = record.getAttribute("path");

					service.getMetaDataInfo(TenantInfoHolder.getId(), path, new AsyncCallback<MetaDataInfo>() {

						@Override
						public void onSuccess(MetaDataInfo result) {
							if (result == null) {
								SC.say(AdminClientMessageUtil.getString("ui_tools_metaexplorer_StatusErrorListPane_failed"),
										AdminClientMessageUtil.getString("ui_tools_metaexplorer_StatusErrorListPane_failedToCheckStatusMetaData"));

								GWT.log("metadata not found. path=" + path);
								return;
							}

							ViewMetaDataEvent.fire(result.getDefinitionClassName(), result.getName(), AdminConsoleGlobalEventBus.getEventBus());
						}

						@Override
						public void onFailure(Throwable caught) {
							SC.say(AdminClientMessageUtil.getString("ui_tools_metaexplorer_StatusErrorListPane_failed"),
									AdminClientMessageUtil.getString("ui_tools_metaexplorer_StatusErrorListPane_failedToCheckStatusMetaData"));

							GWT.log(caught.toString(), caught);
						}
					});
				}
			});


			addMember(title);
			addMember(messageGrid);
			addMember(msgTitle);
			addMember(msgContents);
		}
	}

}
