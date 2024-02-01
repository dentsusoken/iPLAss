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

package org.iplass.adminconsole.client.base.ui.widget;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Progressbar;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class MetaDataStatusCheckDialog extends AbstractWindow {

	private static final int PAGE_SIZE = 10;

	private HLayout footer;

	private Label progressLabel;
	private Progressbar progress;

	private StatusCheckResultHandler resultHandler;

	private MetaDataServiceAsync service = MetaDataServiceFactory.get();

	public MetaDataStatusCheckDialog(final List<String> paths, StatusCheckResultHandler resultHandler) {
		this.resultHandler = resultHandler;

		setWidth(400);
		setHeight(150);
		setTitle("Status check MetaData");
		setShowMinimizeButton(false);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();

		VLayout header = new VLayout(5);
		header.setMargin(5);
		header.setHeight(20);
		header.setWidth100();
		header.setAlign(VerticalAlignment.CENTER);

		Label title = new Label(AdminClientMessageUtil.getString("ui_tools_metaexplorer_MetaDataStatusCheckDialog_checkMetaDataStatus"));
		title.setHeight(20);
		title.setWidth100();
		header.addMember(title);

		footer = new HLayout(5);
		footer.setMargin(5);
		footer.setHeight(20);
		footer.setWidth100();
		footer.setAlign(VerticalAlignment.CENTER);

		IButton start = new IButton("Execute");
		start.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				checkStart(paths);
			}
		});
		footer.addMember(start);

		IButton cancel = new IButton("Cancel");
		cancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				destroy();
			}
		});
		footer.addMember(cancel);

		VLayout horizontalBars = new VLayout(4);
		horizontalBars.setMargin(5);
		horizontalBars.setAutoHeight();
		horizontalBars.setWidth100();
		horizontalBars.setAlign(VerticalAlignment.CENTER);

		progressLabel = new Label("Check Progress");
		progressLabel.setHeight(16);
		horizontalBars.addMember(progressLabel);

		progress = new Progressbar();
		progress.setHeight(24);
		progress.setVertical(false);
		horizontalBars.addMember(progress);

		addItem(header);
		addItem(footer);
		addItem(horizontalBars);

	}

	private void checkStart(List<String> paths) {
		footer.setDisabled(true);
		if (paths == null || paths.size() == 0) {
			//全件
			getAllPathAndCheck();
		} else {
			statusRefresh(paths.size(), 0);
			checkExecute(new LinkedHashMap<String, String>(), paths, 0);
		}
	}

	private void getAllPathAndCheck() {
		service.getAllMetaDataPath(TenantInfoHolder.getId(), new AsyncCallback<List<String>>() {

			@Override
			public void onSuccess(List<String> result) {
				statusRefresh(result.size(), 0);
				checkExecute(new LinkedHashMap<String, String>(), result, 0);
			}

			@Override
			public void onFailure(Throwable caught) {
				SC.say(AdminClientMessageUtil.getString("ui_tools_metaexplorer_MetaDataStatusCheckDialog_failed"),
						AdminClientMessageUtil.getString("ui_tools_metaexplorer_MetaDataStatusCheckDialog_failedToCheckStatusMetaData"));

				GWT.log(caught.toString(), caught);
			}
		});
	}

	private void checkExecute(final LinkedHashMap<String, String> allResult, final List<String> paths, final int offset) {

		int endIndex = offset * PAGE_SIZE + PAGE_SIZE;
		if (endIndex > paths.size()) {
			endIndex = paths.size();
		}
		//subListのままだと、 「com.google.gwt.user.client.rpc.SerializationException: java.util.RandomAccessSubList is not a serializable type」
		List<String> execPath = new ArrayList<String>(paths.subList(offset * PAGE_SIZE , endIndex));

		service.checkStatus(TenantInfoHolder.getId(), execPath, new AsyncCallback<LinkedHashMap<String,String>>() {

			@Override
			public void onSuccess(LinkedHashMap<String, String> result) {
				if (!result.isEmpty()) {
					allResult.putAll(result);
				}
				int nextOffset = offset + 1;
				if (paths.size() > nextOffset * PAGE_SIZE) {
					statusRefresh(paths.size(), nextOffset * PAGE_SIZE);
					checkExecute(allResult, paths, nextOffset);
				} else {
					//終了
					statusRefresh(paths.size(), paths.size());
					showResult(allResult);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				SC.say(AdminClientMessageUtil.getString("ui_tools_metaexplorer_MetaDataStatusCheckDialog_failed"),
						AdminClientMessageUtil.getString("ui_tools_metaexplorer_MetaDataStatusCheckDialog_failedToCheckStatusMetaData"));

				GWT.log(caught.toString(), caught);
			}
		});
	}

	private void statusRefresh(final int allCount, final int execCount) {
		int percent = (int)(((double)execCount / (double)allCount) * 100.0);

		GWT.log(percent + "% check execute." + execCount + "/" + allCount);
		progressLabel.setContents("Check Progress:" + percent + "%");
		progress.setPercentDone(percent);
	}

	private void showResult(final LinkedHashMap<String, String> allResult) {
		footer.setDisabled(false);
		if (allResult.size() == 0) {
			SC.say(AdminClientMessageUtil.getString("ui_tools_metaexplorer_MetaDataStatusCheckDialog_checkComp"), new BooleanCallback() {

					@Override
					public void execute(Boolean value) {
						destroy();
					}
			});
		} else {

			SC.warn(AdminClientMessageUtil.getString("ui_tools_metaexplorer_MetaDataStatusCheckDialog_checkFinishErr", Integer.toString(allResult.size())), new BooleanCallback() {

					@Override
					public void execute(Boolean value) {
						showError(allResult);
					}
			});
		}
	}

	private void showError(LinkedHashMap<String, String> allResult) {

		resultHandler.onError(allResult);

		destroy();
	}

	public interface StatusCheckResultHandler {

		void onError(LinkedHashMap<String, String> result);
	}

}
