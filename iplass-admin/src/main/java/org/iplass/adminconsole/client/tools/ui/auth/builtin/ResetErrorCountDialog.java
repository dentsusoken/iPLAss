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

package org.iplass.adminconsole.client.tools.ui.auth.builtin;

import java.util.List;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.ui.widget.MessageTabSet;
import org.iplass.adminconsole.shared.tools.dto.auth.builtin.BuiltinAuthUserDto;
import org.iplass.adminconsole.shared.tools.dto.auth.builtin.BuiltinAuthUserResetErrorCountResultInfo;
import org.iplass.adminconsole.shared.tools.rpc.auth.builtin.BuiltinAuthExplorerServiceAsync;
import org.iplass.adminconsole.shared.tools.rpc.auth.builtin.BuiltinAuthExplorerServiceFactory;

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

public class ResetErrorCountDialog extends AbstractWindow {

	private List<BuiltinAuthUserDto> users;

	private IButton reset;
	private IButton cancel;

	private MessageTabSet messageTabSet;

	public ResetErrorCountDialog(List<BuiltinAuthUserDto> users) {
		this.users = users;

		setWidth(700);
		setMinWidth(500);
		setHeight(600);
		setMinHeight(600);
		setTitle("Reset Login Error Count");
		setCanDragResize(true);
		setShowMinimizeButton(false);
		setShowMaximizeButton(true);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();

		Label comment = new Label("<b><font color=\"red\">" + users.size() + "</font></b> records selected.");
		comment.setMargin(10);
		comment.setHeight(20);

		//------------------------
		//Buttons
		//------------------------
		reset = new IButton("Reset Count");
		reset.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SC.ask(AdminClientMessageUtil.getString("ui_tools_auth_builtin_ResetErrorCountDialog_confirm"),
						AdminClientMessageUtil.getString("ui_tools_auth_builtin_ResetErrorCountDialog_startResetProcess"), new BooleanCallback() {

					@Override
					public void execute(Boolean value) {
						if (value) {
							resetCount();
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

		HLayout footer = new HLayout(5);
		footer.setMargin(5);
		footer.setHeight(20);
		footer.setWidth100();
		//footer.setAlign(Alignment.LEFT);
		footer.setAlign(VerticalAlignment.CENTER);
		footer.setMembers(reset, cancel);

		//------------------------
		//Input Layout
		//------------------------
		VLayout inputLayout = new VLayout(5);
		inputLayout.setMargin(5);
		inputLayout.setHeight(100);

		inputLayout.addMember(comment);
		inputLayout.addMember(footer);

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

	private void resetCount() {
		disableComponent(true);
		messageTabSet.clearMessage();
		messageTabSet.setTabTitleProgress();

		BuiltinAuthExplorerServiceAsync service = BuiltinAuthExplorerServiceFactory.get();

		service.resetErrorCount(TenantInfoHolder.getId(), users, new AsyncCallback<BuiltinAuthUserResetErrorCountResultInfo>() {

			@Override
			public void onSuccess(BuiltinAuthUserResetErrorCountResultInfo result) {
				resetComplete(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				resetError(caught);
			}
		});

	}

	private void resetComplete(BuiltinAuthUserResetErrorCountResultInfo result) {
		messageTabSet.clearMessage();

		if (result.isError()) {
			messageTabSet.addErrorMessage("Reset failed.");
		} else {
			messageTabSet.addMessage("Reset completed.");
		}
		messageTabSet.addMessage("------------------------------------");

		for (String message : result.getMessages()) {
			if (result.isError()) {
				messageTabSet.addErrorMessage(message);
			} else {
				messageTabSet.addMessage(message);
			}
		}

		disableComponent(false);
		messageTabSet.setTabTitleNormal();
	}

	private void resetError(Throwable caught) {
		messageTabSet.clearMessage();
		messageTabSet.addErrorMessage("Reset failed.");
		messageTabSet.addMessage("------------------------------------");

		if (caught.getMessage() != null) {
			messageTabSet.addErrorMessage(caught.getMessage());
		} else {
			messageTabSet.addErrorMessage(caught.getClass().getName());
		}

		disableComponent(false);
		messageTabSet.setTabTitleNormal();
	}

	private void disableComponent(boolean disabled) {
		reset.setDisabled(disabled);
		cancel.setDisabled(disabled);
	}
}
