/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import org.iplass.adminconsole.client.base.event.AdminConsoleGlobalEventBus;
import org.iplass.adminconsole.client.base.event.ViewMetaDataEvent;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.base.dto.tenant.AdminPlatformInfo;
import org.iplass.adminconsole.shared.base.rpc.tenant.TenantServiceAsync;
import org.iplass.adminconsole.shared.base.rpc.tenant.TenantServiceFactory;
import org.iplass.mtp.tenant.Tenant;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.HoverEvent;
import com.smartgwt.client.widgets.events.HoverHandler;
import com.smartgwt.client.widgets.layout.VStack;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import com.smartgwt.client.widgets.toolbar.ToolStripMenuButton;

public class MainTopNav extends VStack {

	public interface MainTopNavHandler extends EventHandler {

		void onLogoff();

		void onClose();
	}

	//ダイレクトログインフラグ
	private boolean directLogin = false;

	private ToolStripButton closeButton;

	public MainTopNav(final MainTopNavHandler handler) {

		Tenant tenant = TenantInfoHolder.getTenant();

		ToolStrip topBar = new ToolStrip();
		topBar.setWidth100();

		topBar.setLayoutLeftMargin(6);
		topBar.setLayoutRightMargin(6);
		topBar.setMembersMargin(6);

		Label title = new Label("Admin Console");
		title.setStyleName("adminWindowHeaderText");
		title.setWidth(350);
		topBar.addMember(title);

		topBar.addFill();

		ToolStripMenuButton aboutButton = new ToolStripMenuButton();
		aboutButton.setTitle(tenant.getName());

		Menu aboutMenu = new Menu();
		aboutMenu.setShowShadow(true);
		aboutMenu.setShadowDepth(3);

		MenuItem tenantItem = new MenuItem("Tenant Information", "house.png");
		tenantItem.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(MenuItemClickEvent event) {
				ViewMetaDataEvent.fire(Tenant.class.getName(), tenant.getName(), AdminConsoleGlobalEventBus.getEventBus());
			}
		});
		MenuItem aboutItem = new MenuItem("About iPLAss", "information.png");
		aboutItem.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(MenuItemClickEvent event) {
				showPlatformInformation();
			}
		});
		aboutMenu.setItems(tenantItem, aboutItem);
		aboutButton.setMenu(aboutMenu);

		topBar.addMenuButton(aboutButton);

		topBar.addSeparator();

		closeButton = new ToolStripButton();
		closeButton.setTitle("Close");
		closeButton.setIcon("close.png");
		closeButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			public void onClick(ClickEvent event) {
				if (isDirectLogin()) {
					SC.ask(AdminClientMessageUtil.getString("ui_MenuPane_confirm"),
							AdminClientMessageUtil.getString("ui_MenuPane_logOffConf"), new BooleanCallback() {

								@Override
								public void execute(Boolean value) {
									if (value) {
										handler.onLogoff();
									}
								}
							});

				} else {
					SC.ask(AdminClientMessageUtil.getString("ui_MenuPane_confirm"),
							AdminClientMessageUtil.getString("ui_MenuPane_windowClose"), new BooleanCallback() {
								@Override
								public void execute(Boolean value) {
									if (value) {
										handler.onClose();
									}
								}
							});
				}
			}
		});
		closeButton.setCanHover(true);
		closeButton.addHoverHandler(new HoverHandler() {
			@Override
			public void onHover(HoverEvent event) {
				closeButton.setPrompt(
						SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_MenuPane_closeWindow")));
			}
		});
		topBar.addButton(closeButton);

		addMember(topBar);

		initializeData();
	}

	private void initializeData() {
	}

	public void setDirectLogin(boolean directLogin) {
		this.directLogin = directLogin;

		if (directLogin) {
			closeButton.setTitle("Logoff");
		} else {
			closeButton.setTitle("Close");
		}
	}

	public boolean isDirectLogin() {
		return directLogin;
	}

	private void showPlatformInformation() {
		TenantServiceAsync service = TenantServiceFactory.get();
		service.getPlatformInformation(TenantInfoHolder.getId(), new AsyncCallback<AdminPlatformInfo>() {

			@Override
			public void onSuccess(final AdminPlatformInfo result) {
				AboutDialog dialog = new AboutDialog(result);
				dialog.show();
			}

			@Override
			public void onFailure(Throwable caught) {
				SC.warn(AdminClientMessageUtil.getString("ui_MenuPane_failedToGetPlatformInfo") + caught.getMessage());
			}
		});
	}

}
