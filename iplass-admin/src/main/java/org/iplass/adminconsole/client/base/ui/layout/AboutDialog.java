/*
 * Copyright (C) 2018 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.base.ui.layout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.shared.base.dto.tenant.AdminPlatformInfo;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class AboutDialog extends MtpDialog {

	public AboutDialog(AdminPlatformInfo result) {

		setTitle("About iPLAss");

		setHeight(600);
		centerInPage();

		container.addMember(new TitlePane());
		container.addMember(new PlatformInfoPane(result));

		if (result.isShowServerInfo()) {
			container.addMember(new ServerInfoPane(result));
		}

		container.addMembers(new ContentsPane("Licenses :", result.getLicenseLines()));
		container.addMembers(new ContentsPane("Notices :", result.getNoticeLines()));

		IButton okButton = new IButton("OK");
		okButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				destroy();
			}
		});

		footer.setMembers(okButton);
	}

	private static class TitlePane extends HLayout {

		public TitlePane() {

			setMargin(10);
			setAutoHeight();
			setWidth100();
			setMembersMargin(10);
			setAlign(VerticalAlignment.CENTER);

			Label title = new Label("About iPLAss");
			title.setStyleName("adminWindowHeaderText");
			title.setWrap(false);

			setMembers(title);
		}
	}

	private static class PlatformInfoPane extends VLayout {

		public PlatformInfoPane(AdminPlatformInfo result) {

			Label titleLabel = new Label("Platform Information :");
			titleLabel.setHeight(20);
			titleLabel.setWrap(false);

			ListGrid versionGrid = new ListGrid();
			versionGrid.setWidth100();
			versionGrid.setHeight(1);

			versionGrid.setShowAllColumns(true);
			versionGrid.setShowAllRecords(true);
			versionGrid.setBodyOverflow(Overflow.VISIBLE);
			versionGrid.setOverflow(Overflow.VISIBLE);

			versionGrid.setShowHeader(false);
			versionGrid.setLeaveScrollbarGap(false);

			versionGrid.setCanAcceptDroppedRecords(false);
			versionGrid.setCanDragRecordsOut(false);
			versionGrid.setCanEdit(false);
			versionGrid.setCanAutoFitFields(false);
			versionGrid.setCanCollapseGroup(false);
			versionGrid.setCanFreezeFields(false);
			versionGrid.setCanGroupBy(false);
			versionGrid.setCanMultiSort(false);
			versionGrid.setCanPickFields(false);
			versionGrid.setCanReorderFields(false);
			versionGrid.setCanReorderRecords(false);
			versionGrid.setCanResizeFields(false);
			versionGrid.setCanSort(false);
			versionGrid.setCanDragSelectText(true);

			ListGridField versionKeyField = new ListGridField("key", "key");
			ListGridField versionValueField = new ListGridField("value", "value");
			versionGrid.setFields(versionKeyField, versionValueField);

			List<ListGridRecord> versionRecords = new ArrayList<ListGridRecord>();
			if (result.getPlatformInfomations() != null) {
				for (Entry<String, String> info : result.getPlatformInfomations().entrySet()) {
					ListGridRecord record = new ListGridRecord();
					record.setAttribute("key", info.getKey());
					record.setAttribute("value", info.getValue());
					versionRecords.add(record);
				}
			}
			versionGrid.setData(versionRecords.toArray(new ListGridRecord[] {}));

			addMembers(titleLabel, versionGrid);
		}
	}

	private static class ServerInfoPane extends VLayout {

		public ServerInfoPane(AdminPlatformInfo result) {

			Label titleLabel = new Label("Server Information :");
			titleLabel.setHeight(20);
			titleLabel.setWrap(false);

			ListGrid serverGrid = new ListGrid();
			serverGrid.setWidth100();
			serverGrid.setHeight(1);

			serverGrid.setShowAllColumns(true);
			serverGrid.setShowAllRecords(true);
			serverGrid.setBodyOverflow(Overflow.VISIBLE);
			serverGrid.setOverflow(Overflow.VISIBLE);

			serverGrid.setShowHeader(false);
			serverGrid.setLeaveScrollbarGap(false);

			serverGrid.setCanAcceptDroppedRecords(false);
			serverGrid.setCanDragRecordsOut(false);
			serverGrid.setCanEdit(false);
			serverGrid.setCanAutoFitFields(false);
			serverGrid.setCanCollapseGroup(false);
			serverGrid.setCanFreezeFields(false);
			serverGrid.setCanGroupBy(false);
			serverGrid.setCanMultiSort(false);
			serverGrid.setCanPickFields(false);
			serverGrid.setCanReorderFields(false);
			serverGrid.setCanReorderRecords(false);
			serverGrid.setCanResizeFields(false);
			serverGrid.setCanSort(false);
			serverGrid.setCanDragSelectText(true);

			ListGridField serverKeyField = new ListGridField("key", "key");
			ListGridField serverValueField = new ListGridField("value", "value");
			serverGrid.setFields(serverKeyField, serverValueField);

			List<ListGridRecord> serverRecords = new ArrayList<ListGridRecord>();
			if (result.getSeverInfomations() != null) {
				for (Entry<String, String> info : result.getSeverInfomations().entrySet()) {
					ListGridRecord record = new ListGridRecord();
					record.setAttribute("key", info.getKey());
					record.setAttribute("value", info.getValue());
					serverRecords.add(record);
				}
			}
			serverGrid.setData(serverRecords.toArray(new ListGridRecord[] {}));

			addMembers(titleLabel, serverGrid);
		}
	}

	private static class ContentsPane extends VLayout {

		public ContentsPane(String title, List<String> lines) {
			setHeight100();
			setWidth100();

			Label titleLabel = new Label(title);
			titleLabel.setHeight(20);
			titleLabel.setWrap(false);

			Canvas contentCanvas = new Canvas();
			contentCanvas.setHeight100();
			contentCanvas.setWidth100();
			contentCanvas.setPadding(5);
			contentCanvas.setOverflow(Overflow.AUTO);
			contentCanvas.setCanSelectText(true);
			//周りの枠をGridにあわせる
			contentCanvas.setStyleName("listGrid");

			StringBuilder contents = new StringBuilder();
			for (String line : lines) {
				contents.append("<p>").append(line).append("</p>");
			}
			contentCanvas.setContents(contents.toString());

			addMembers(titleLabel, contentCanvas);
		}
	}
}
