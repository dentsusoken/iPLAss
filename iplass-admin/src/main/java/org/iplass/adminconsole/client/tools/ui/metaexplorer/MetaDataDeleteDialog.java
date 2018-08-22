/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.iplass.adminconsole.client.base.event.AdminConsoleGlobalEventBus;
import org.iplass.adminconsole.client.base.event.RefreshMetaDataEvent;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.ui.widget.MessageTabSet;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.DeleteResultInfo;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.Message;
import org.iplass.adminconsole.shared.tools.rpc.metaexplorer.MetaDataExplorerServiceAsync;
import org.iplass.adminconsole.shared.tools.rpc.metaexplorer.MetaDataExplorerServiceFactory;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * MetaDataを削除するダイアログ
 */
public class MetaDataDeleteDialog extends AbstractWindow {

	private IButton delete;
	private IButton cancel;

	private MessageTabSet messageTabSet;

	private MetaDataListPane owner;
	private String[] selectedPaths;

	/**
	 * コンストラクタ
	 */
	public MetaDataDeleteDialog(MetaDataListPane owner, final String[] selectedPaths) {
		this.owner = owner;
		this.selectedPaths = selectedPaths;

		setWidth(700);
		setMinWidth(500);
		setHeight(500);
		setMinHeight(400);
		setTitle("Delete MetaData");
		setCanDragResize(true);
		setShowMinimizeButton(false);
		setShowMaximizeButton(true);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();

		//------------------------
		//Input Items
		//------------------------
		Label title = new Label(AdminClientMessageUtil.getString("ui_tools_metaexplorer_MetaDataDeleteDialog_deleteMetaDataSelect")
				+ AdminClientMessageUtil.getString("ui_tools_metaexplorer_MetaDataDeleteDialog_deleteMetaDataComment"));
		title.setHeight(40);
		title.setWidth100();

		//TODO Grid

		//------------------------
		//Buttons
		//------------------------
		delete = new IButton("Delete");
		delete.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				SC.ask(AdminClientMessageUtil.getString("ui_tools_metaexplorer_MetaDataDeleteDialog_confirm"),
						AdminClientMessageUtil.getString("ui_tools_metaexplorer_MetaDataDeleteDialog_startDeleteConfirm"), new BooleanCallback() {

					@Override
					public void execute(Boolean value) {
						if (value) {
							deleteMetaData();
						}
					}
				});
			}
		});
		cancel = new IButton("Cancel");
		cancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				destroy();
			}
		});

		HLayout execButtons = new HLayout(5);
		execButtons.setMargin(5);
		execButtons.setHeight(20);
		execButtons.setWidth100();
		execButtons.setAlign(VerticalAlignment.CENTER);
		execButtons.setMembers(delete, cancel);

		//------------------------
		//Input Layout
		//------------------------
		VLayout inputLayout = new VLayout(5);
		inputLayout.setMargin(5);
		inputLayout.setHeight(100);

		inputLayout.addMember(title);
		inputLayout.addMember(execButtons);

		//------------------------
		//MessagePane
		//------------------------
		messageTabSet = new MessageTabSet();

		//------------------------
		//Main Layout
		//------------------------
		VLayout mainLayout = new VLayout();
		mainLayout.setWidth100();
		mainLayout.setHeight100();
		mainLayout.setMargin(10);

		mainLayout.addMember(inputLayout);
		mainLayout.addMember(messageTabSet);

		addItem(mainLayout);
	}

	private void deleteMetaData() {
		disableComponent(true);
		messageTabSet.clearMessage();
		messageTabSet.setTabTitleProgress();

		MetaDataExplorerServiceAsync service = MetaDataExplorerServiceFactory.get();
		ArrayList<String> paths = new ArrayList<String>();
		Collections.addAll(paths, selectedPaths);
		service.deleteMetaData(TenantInfoHolder.getId(), paths, new AsyncCallback<DeleteResultInfo>() {

			@Override
			public void onSuccess(DeleteResultInfo result) {
				deleteComplete(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				deleteError(caught);
			}
		});
	}

	private void deleteComplete(DeleteResultInfo result) {
		messageTabSet.clearMessage();

		if (result.isError()) {
			messageTabSet.addErrorMessage(AdminClientMessageUtil.getString("ui_tools_metaexplorer_MetaDataDeleteDialog_deleteErr")
					+ getCountString(result));
		} else if (result.isWarn()) {
			messageTabSet.addWarnMessage(AdminClientMessageUtil.getString("ui_tools_metaexplorer_MetaDataDeleteDialog_deleteWarn")
					+ getCountString(result));
		} else {
			messageTabSet.addMessage(AdminClientMessageUtil.getString("ui_tools_metaexplorer_MetaDataDeleteDialog_deleteComp")
					+ getCountString(result));
		}
		messageTabSet.addMessage("------------------------------------");

		for (Message message : result.getMessages()) {
			if (message.isError()) {
				messageTabSet.addErrorMessage(message.getMessage());
			} else if (message.isWarn()) {
				messageTabSet.addWarnMessage(message.getMessage());
			} else {
				messageTabSet.addMessage(message.getMessage());
			}
		}

		disableComponent(false);
		delete.setDisabled(true);	//削除ボタンは使用不可
		messageTabSet.setTabTitleNormal();

		//リフレッシュ
		if (result.getDeleteCount() > 0) {
			owner.refreshGrid();
			RefreshMetaDataEvent.fire(AdminConsoleGlobalEventBus.getEventBus());
		}
	}

	private void deleteError(Throwable caught) {
		messageTabSet.clearMessage();

		List<String> errors = new ArrayList<String>(1);
		errors.add(AdminClientMessageUtil.getString("ui_tools_metaexplorer_MetaDataDeleteDialog_deleteErr"));
		if (caught.getMessage() != null) {
			errors.add(caught.getMessage());
		} else {
			errors.add(caught.getClass().getName());
		}
		messageTabSet.setErrorMessage(errors);

		disableComponent(false);
		messageTabSet.setTabTitleNormal();
	}

	private String getCountString(DeleteResultInfo result) {
		return "(Error : " + result.getErrorCount()
			+ ", Warning : " + result.getWarnCount()
			+ ", Deleted : " + result.getDeleteCount() + ")";
	}

	private void disableComponent(boolean disabled) {
		delete.setDisabled(disabled);
		cancel.setDisabled(disabled);
	}

}
