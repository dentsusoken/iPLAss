/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.tools.ui.entityexplorer.defrag;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.shared.tools.rpc.entityexplorer.EntityExplorerServiceAsync;
import org.iplass.adminconsole.shared.tools.rpc.entityexplorer.EntityExplorerServiceFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Progressbar;
import com.smartgwt.client.widgets.layout.VLayout;

public class EntityDefragProgressDialog extends AbstractWindow {

	private static final String RESOURCE_PREFIX = "ui_tools_entityexplorer_EntityDefragListPane_";

	private static final EntityExplorerServiceAsync service = EntityExplorerServiceFactory.get();

	private EntityDefragListPane owner;

	private Label progressLabel;
	private Progressbar progress;

	public EntityDefragProgressDialog(EntityDefragListPane owner, final List<String> defNames) {
		this.owner = owner;

		setWidth(400);
		setHeight(150);
	setTitle(AdminClientMessageUtil.getString("ui_tools_entityexplorer_EntityDefragProgressDialog_defragEntity"));
		setShowMinimizeButton(false);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();

		VLayout header = new VLayout(5);
		header.setMargin(5);
		header.setHeight(20);
		header.setWidth100();
		header.setAlign(VerticalAlignment.CENTER);

		Label title = new Label(AdminClientMessageUtil.getString(RESOURCE_PREFIX + "asyncExecuteLabel"));
		title.setHeight(20);
		title.setWidth100();
		header.addMember(title);

		VLayout horizontalBars = new VLayout(4);
		horizontalBars.setMargin(5);
		horizontalBars.setAutoHeight();
		horizontalBars.setWidth100();
		horizontalBars.setAlign(VerticalAlignment.CENTER);

		progressLabel = new Label("Progress");
		progressLabel.setHeight(16);
		horizontalBars.addMember(progressLabel);

		progress = new Progressbar();
		progress.setHeight(24);
		progress.setVertical(false);
		horizontalBars.addMember(progress);

		addItem(header);
		addItem(horizontalBars);

		startClean(defNames);

	}

	private void startClean(List<String> defNames) {
		owner.startCallback();
		statusRefresh(defNames.size(), 0);
		cleanExecute(defNames, 0);
	}

	private void cleanExecute(final List<String> defNames, final int index) {

		String defName = defNames.get(index);

		service.defragEntity(TenantInfoHolder.getId(), defName, new AsyncCallback<List<String>>() {

			@Override
			public void onSuccess(List<String> result) {
				if (result != null) {
					owner.executeStatusCallback(result);
				}

				int next = index + 1;
				if (defNames.size() > next) {
					statusRefresh(defNames.size(), next);
					cleanExecute(defNames, next);
				} else {
					//終了
					statusRefresh(defNames.size(), defNames.size());

					List<String> message = new ArrayList<String>();
					message.add("");
					message.add(AdminClientMessageUtil.getString(RESOURCE_PREFIX + "completedToDefragEntity"));
					owner.executeStatusCallback(message);

					SC.say(AdminClientMessageUtil.getString(RESOURCE_PREFIX + "completed"),
							AdminClientMessageUtil.getString(RESOURCE_PREFIX + "completedToDefragEntity"),
							new BooleanCallback() {
								@Override
								public void execute(Boolean value) {
									finishClean();
								}
							});
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				SC.say(AdminClientMessageUtil.getString(RESOURCE_PREFIX + "failed"),
						AdminClientMessageUtil.getString(RESOURCE_PREFIX + "failedToDefragEntity"));

				GWT.log(caught.toString(), caught);

				List<String> message = new ArrayList<String>();
				message.add("");
				message.add(AdminClientMessageUtil.getString(RESOURCE_PREFIX + "failedToDefragEntity"));
				message.add("Cause:" + caught.getMessage());
				owner.executeErrorCallback(message);

				finishClean();
			}

		});
	}

	private void statusRefresh(final int allCount, final int execCount) {
		int percent = (int)(((double)execCount / (double)allCount) * 100.0);

		GWT.log(percent + "% execute." + execCount + "/" + allCount);
		progressLabel.setContents("Progress:" + percent + "%");
		progress.setPercentDone(percent);
	}

	private void finishClean() {
		owner.finishCallback();
		destroy();
	}
}
