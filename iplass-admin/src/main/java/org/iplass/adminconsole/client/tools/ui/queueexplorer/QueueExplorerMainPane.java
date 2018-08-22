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

package org.iplass.adminconsole.client.tools.ui.queueexplorer;

import org.iplass.adminconsole.client.base.data.DataSourceConstants;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.plugin.ContentClosedEvent;
import org.iplass.adminconsole.client.base.plugin.ContentSelectedEvent;
import org.iplass.adminconsole.client.base.plugin.ContentStateChangeHandler;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.data.queue.QueueListDS;

import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * QueueExplorer
 */
public class QueueExplorerMainPane extends VLayout implements ContentStateChangeHandler {

	private SelectItem queueNameItem;

	private QueueInfoPane queuePane;

	/**
	 * コンストラクタ
	 */
	public QueueExplorerMainPane() {

		//レイアウト設定
		setWidth100();

		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setWidth100();
		toolStrip.setMembersMargin(5);

		//------------------------
		//Queue
		//------------------------

		queueNameItem = new SelectItem();
		queueNameItem.setTitle("Queue");
		//queueNameItem.setShowTitle(false);
		queueNameItem.setWidth(200);
		QueueListDS.setDataSource(queueNameItem, false);
		queueNameItem.setAutoFetchData(false);	//Fetchは初期表示時に行う

		queueNameItem.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				refreshGrid();
			}
		});
		toolStrip.addFormItem(queueNameItem);

		toolStrip.addFill();

		//------------------------
		//Refresh
		//------------------------

		final ToolStripButton refreshButton = new ToolStripButton();
		refreshButton.setIcon("[SKINIMG]/actions/refresh.png");
		refreshButton.setTooltip(SmartGWTUtil.getHoverString(getResString("refreshList")));
		refreshButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				refreshGrid();
			}
		});
		toolStrip.addButton(refreshButton);

		queuePane = new QueueInfoPane();

		addMember(toolStrip);
		addMember(queuePane);

		initializeData();
	}

	private void initializeData() {

		//Queueのフェッチ
		queueNameItem.fetchData(new DSCallback() {

			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				if (response.getData() == null || response.getData().length == 0) {
					SC.say(getResString("notExistQueue"));
				} else if (response.getData().length == 1) {
					//1件しかない場合は、それを選択
					queueNameItem.setValue(response.getData()[0].getAttribute(DataSourceConstants.FIELD_NAME));
					refreshGrid();
				}
			}
		});
	}

	@Override
	public void onContentClosed(ContentClosedEvent event) {
	}

	@Override
	public void onContentSelected(ContentSelectedEvent event) {
	}

	private void refreshGrid() {
		queuePane.setQueueName(SmartGWTUtil.getStringValue(queueNameItem));
	}

	private static final String RES_PREFIX = "ui_tools_queueexplorer_QueueExplorerMainPane_";
	private String getResString(String key) {
		return AdminClientMessageUtil.getString(RES_PREFIX + key);
	}

}
