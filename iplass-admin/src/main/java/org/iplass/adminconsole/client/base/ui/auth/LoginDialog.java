/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.base.ui.auth;

import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.base.dto.AdminUncaughtException;
import org.iplass.adminconsole.shared.base.dto.auth.LoginFailureException;
import org.iplass.adminconsole.shared.base.dto.auth.UnauthorizedAccessException;
import org.iplass.adminconsole.shared.base.rpc.tenant.TenantServiceAsync;
import org.iplass.adminconsole.shared.base.rpc.tenant.TenantServiceFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

public class LoginDialog extends AbstractWindow {

	private TenantServiceAsync service = TenantServiceFactory.get();

	private DynamicForm form;

	private TextItem idField;
	private PasswordItem passField;

	private LoginHandler handler;

	public LoginDialog() {
		setTitle("Direct Login");

		setHeight(230);
		setWidth(500);
		setMargin(10);

		setShowMinimizeButton(false);
		setShowCloseButton(false);	//Closeボタン非表示
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();

		VLayout layoutPane = new VLayout();
		layoutPane.setWidth100();
		layoutPane.setHeight100();
		layoutPane.setMargin(8);

        ToolStrip topBar = new ToolStrip();
        topBar.setHeight(33);
        topBar.setWidth100();

        topBar.addSpacer(6);
        ImgButton sgwtHomeButton = new ImgButton();
        sgwtHomeButton.setSrc("cubes_all.png");
        sgwtHomeButton.setWidth(24);
        sgwtHomeButton.setHeight(24);
        sgwtHomeButton.setShowRollOver(false);
        sgwtHomeButton.setShowDownIcon(false);
        sgwtHomeButton.setShowDown(false);
        sgwtHomeButton.setCanFocus(false);
        topBar.addMember(sgwtHomeButton);
        topBar.addSpacer(6);

        final Label title = new Label("Direct Login");
        title.setStyleName("sgwtTitle");
        topBar.addMember(title);

		VLayout main = new VLayout();
		main.setMargin(10);
		main.setHeight100();
		main.setWidth100();
		main.setAlign(VerticalAlignment.CENTER);

		form = new DynamicForm();
		form.setMargin(10);
		form.setHeight100();
		form.setWidth100();
		form.setNumCols(2);
		form.setColWidths(100, "*");
		form.setAutoFocus(true);

		idField = new TextItem("idField","ID");
		idField.setWidth(300);
		SmartGWTUtil.setRequired(idField);

		passField = new PasswordItem("passField", "Password");
		passField.setWidth(300);
		SmartGWTUtil.setRequired(passField);

		form.setItems(idField, passField);
		main.addMember(form);

		HLayout footer = new HLayout(5);
		footer.setPadding(5);
		footer.setAutoHeight();
		footer.setWidth100();
		footer.setAlign(VerticalAlignment.CENTER);

		IButton loginButton = new IButton("Login");
		loginButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (form.validate()) {
					login();
				}
			}
		});
		IButton cancelButton = new IButton("Cancel");
		cancelButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (handler != null) {
					handler.onCancel();
				}
			}
		});

		footer.setMembers(loginButton, cancelButton);

		layoutPane.addMember(topBar);
		layoutPane.addMember(main);
		layoutPane.addMember(footer);

		addItem(layoutPane);
	}

	public void setLoginHandler(LoginHandler handler) {
		this.handler = handler;
	}

	private void login() {
		SmartGWTUtil.showProgress("Login ....");

		//ログイン
		service.authLogin(TenantInfoHolder.getId(), SmartGWTUtil.getStringValue(idField), SmartGWTUtil.getStringValue(passField), new AsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				// 認証成功

				//ログインしたということはAdminConsoleの直接起動なので、Lang設定
				setWindowLanguage();

				if (handler != null) {
					handler.onLogin();
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				SmartGWTUtil.hideProgress();

				if (caught instanceof LoginFailureException) {
					//ログインエラー
					SC.warn(caught.getMessage());
					return;
				} else if (caught instanceof UnauthorizedAccessException) {
					//許可エラー
					SC.warn(caught.getMessage());
					return;
				}
				throw new AdminUncaughtException(caught);
			}
		});
	}

	private void setWindowLanguage() {
		//引数で明示的に指定されている場合はLangを設定
		if (TenantInfoHolder.getLanguage() != null && !TenantInfoHolder.getLanguage().isEmpty()) {
			service.setLanguage(TenantInfoHolder.getId(), TenantInfoHolder.getLanguage(), new AsyncCallback<Void>() {

				@Override
				public void onSuccess(Void result) {
					GWT.log("success to set session language.");
				}

				@Override
				public void onFailure(Throwable caught) {
					GWT.log("failed to set session language. :" + caught.getMessage());
				}
			});
		}
	}

}
