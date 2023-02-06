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

package org.iplass.adminconsole.client;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.rpc.AdminAsyncCallback;
import org.iplass.adminconsole.client.base.screen.ScreenModuleBasedUIFactory;
import org.iplass.adminconsole.client.base.screen.ScreenModuleBasedUIFactoryGenerator;
import org.iplass.adminconsole.client.base.screen.ScreenModuleBasedUIFactoryHolder;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.auth.LoginDialog;
import org.iplass.adminconsole.client.base.ui.auth.LoginHandler;
import org.iplass.adminconsole.client.base.ui.layout.MainContentPane;
import org.iplass.adminconsole.client.base.ui.layout.MainTopNav;
import org.iplass.adminconsole.client.base.ui.layout.MainTopNav.MainTopNavHandler;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.base.dto.AdminUncaughtException;
import org.iplass.adminconsole.shared.base.dto.auth.TenantNotFoundException;
import org.iplass.adminconsole.shared.base.dto.auth.UnauthenticatedException;
import org.iplass.adminconsole.shared.base.dto.auth.UnauthorizedAccessException;
import org.iplass.adminconsole.shared.base.dto.tenant.TenantEnv;
import org.iplass.adminconsole.shared.base.rpc.AdminXsrfTokenHolder;
import org.iplass.adminconsole.shared.base.rpc.screen.ScreenModuleServiceAsync;
import org.iplass.adminconsole.shared.base.rpc.screen.ScreenModuleServiceFactory;
import org.iplass.adminconsole.shared.base.rpc.tenant.TenantServiceAsync;
import org.iplass.adminconsole.shared.base.rpc.tenant.TenantServiceFactory;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RpcTokenException;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.rpc.XsrfToken;
import com.google.gwt.user.client.rpc.XsrfTokenService;
import com.google.gwt.user.client.rpc.XsrfTokenServiceAsync;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class AdminConsole implements EntryPoint {

	/** 全体レイアウト */
	private VLayout container;

	private MainTopNav topNav;
	private MainContentPane mainPane;

	private ScreenModuleServiceAsync screenModuleServiceAsync;
	private TenantServiceAsync tenantServiceAsync;

	/**
	 * This is the entry point method.
	 */
	@Override
	public void onModuleLoad() {

		// Debug ModuleBaseURL
		GWT.log("ModuleBaseURL:" + GWT.getModuleBaseURL());
		GWT.log("ModuleBaseForStaticFiles:" + GWT.getModuleBaseForStaticFiles());

		// 例外発生時の設定
		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void onUncaughtException(Throwable e) {
				if (e instanceof TenantNotFoundException) {
					// テナント情報不明
					invalidMessage(e.getMessage(), e);
				} else if (e instanceof UnauthenticatedException) {
					// 認証エラー

					// ログイン画面表示
					showLoginDialog();
				} else if (e instanceof UnauthorizedAccessException) {
					// 権限エラー
					invalidMessage(e.getMessage(), e);
				} else if (e instanceof AdminUncaughtException) {
					// 処理されないエラー
					invalidMessage(AdminClientMessageUtil.getString("ui_MtpAdmin_systemErrNotContinue"), e);
				} else if (e instanceof JavaScriptException) {
					// GWTのJavaScriptエラー（メッセージは表示しない）

					GWT.log(AdminClientMessageUtil.getString("ui_MtpAdmin_uncaughtException"), e);

					// プログレス表示を消す
					SmartGWTUtil.hideProgress();
				} else if (e instanceof RpcTokenException) {
					// XsrfTokenエラー
					invalidMessage(AdminClientMessageUtil.getString("ui_MtpAdmin_rpcTokenErr"), e);
				} else {
					// その他のエラー
					invalidMessage(AdminClientMessageUtil.getString("ui_MtpAdmin_systemErrNotContinue"), e);
				}
			}
		});

		// テナント情報の初期化
		GWT.log("init admin console: tenantId=[" + getTenantId() + "]");

		// 全体レイアウト表示
		container = new VLayout();
		container.setWidth100();
		container.setHeight100();
		container.setMargin(8);

		// container.show(); //smartgwt3.1でshowだとエラーになるためdrawに変更
		container.draw();

		// 初期化
		initialize();

	}

	private static native int getTenantId() /*-{
		return $wnd._tenantId;
	}-*/;

	private static native String getLanguage() /*-{
		return $wnd._language;
	}-*/;

	public static native void closeWindow() /*-{
		$wnd.close();
	}-*/;

	// ------------------------------
	// 初期処理
	// ------------------------------

	private void initialize() {
		SmartGWTUtil.showProgress("Loading Tenant Information.<br/>Please wait ....");

		// Token取得
		getXsrfToken();
	}

	private void getXsrfToken() {

		// Tokenの取得
		XsrfTokenServiceAsync xsrf = GWT.create(XsrfTokenService.class);
		((ServiceDefTarget) xsrf).setServiceEntryPoint(GWT.getModuleBaseURL() + "xsrf");
		xsrf.getNewXsrfToken(new AdminAsyncCallback<XsrfToken>() {

			@Override
			public void onSuccess(XsrfToken token) {
				AdminXsrfTokenHolder.init(token);

				// テナントチェック
				validateTenant();
			}
		});

	}

	private void validateTenant() {
		// テナント存在チェック
		tenantServiceAsync = TenantServiceFactory.get();
		tenantServiceAsync.getTenantEnv(getTenantId(), new TenantCheckAsyncCallback());
	}

	private void invalidMessage(String message) {
		invalidMessage(message, null);
	}

	private void invalidMessage(String message, Throwable caught) {
		if (caught != null) {
			GWT.log(message, caught);
		} else {
			GWT.log(message);
		}

		SmartGWTUtil.hideProgress();
		SC.warn(message);
	}

	// ------------------------------
	// テナントチェック
	// ------------------------------
	private class TenantCheckAsyncCallback implements AsyncCallback<TenantEnv> {

		@Override
		public void onSuccess(TenantEnv result) {

			if (result != null) {
				String lang = getLanguage();
				if (lang == null || lang.isEmpty()) {
					// 引数で指定されていない場合はログインしたテナントから設定（まだユーザーが確定していないので）
					if (result.getTenantLocale() != null && !result.getTenantLocale().isEmpty()) {
						lang = result.getTenantLocale().split("_")[0];
					}
				}

				// テナント情報をセット
				TenantInfoHolder.init(result, lang);

				// 画面モジュールに依存したUIクラスを生成するFactoryを取得
				getScreenModuleBasedUIFactory();
			} else {
				// テナントが存在しない
				throw new TenantNotFoundException(AdminClientMessageUtil.getString("ui_MtpAdmin_tenantInfoCannotGet"));
			}
		}

		@Override
		public void onFailure(Throwable caught) {
			if (caught instanceof TenantNotFoundException) {
				throw (TenantNotFoundException) caught;
			}
			invalidMessage(AdminClientMessageUtil.getString("ui_MtpAdmin_systemErrNotContinue"), caught);
		}
	}

	private void getScreenModuleBasedUIFactory() {
		screenModuleServiceAsync = ScreenModuleServiceFactory.get();

		screenModuleServiceAsync.getScreenModuleType(new AdminAsyncCallback<String>() {
			@Override
			public void onSuccess(String type) {
				ScreenModuleBasedUIFactoryGenerator factoryGenerator = GWT
						.create(ScreenModuleBasedUIFactoryGenerator.class);
				ScreenModuleBasedUIFactory factory = factoryGenerator.generate(type);
				ScreenModuleBasedUIFactoryHolder.init(factory);

				createMainPane();
			}
		});
	}

	// ------------------------------
	// メイン画面生成
	// ------------------------------
	private void createMainPane() {
		createMainPane(false);
	}

	private void createMainPane(boolean isDirect) {
		// 既に作成されているかのチェック（メニューが表示されているかで）
		if (topNav != null) {
			// 既に作成済み（途中でセッションが切れて再ログインした場合など）の場合は
			// ダイレクトでログインしたかをセットして終了
			topNav.setDirectLogin(isDirect);
			return;
		}

		// メニュー部
		topNav = new MainTopNav(new MainTopNavHandler() {

			@Override
			public void onLogoff() {
				logoff();
			}

			@Override
			public void onClose() {
				close();
				closeWindow();
			}
		});
		topNav.setDirectLogin(isDirect);
		topNav.setWidth100();

		// メイン部
		mainPane = new MainContentPane();

		container.addMember(topNav);
		container.addMember(mainPane);

		SmartGWTUtil.hideProgress();
	}

	// ------------------------------
	// ログイン画面表示
	// ------------------------------
	private void showLoginDialog() {

		final LoginDialog dialog = new LoginDialog();
		dialog.setLoginHandler(new LoginHandler() {

			@Override
			public void onLogin() {
				createMainPane(true);
				dialog.destroy();
			}

			@Override
			public void onCancel() {
				clearMainPane();
				showDisablePane();
				dialog.destroy();
			}
		});
		dialog.show();

		SmartGWTUtil.hideProgress();
	}

	// ------------------------------
	// ログオフ処理
	// ------------------------------
	public void logoff() {

		tenantServiceAsync.authLogoff(TenantInfoHolder.getId(), new AsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				clearMainPane();

				// ログイン画面表示
				showLoginDialog();
			}

			@Override
			public void onFailure(Throwable caught) {
				throw new AdminUncaughtException(caught);
			}
		});
	}

	public void close() {
		clearMainPane();
		showDisablePane();
	}

	private void clearMainPane() {
		if (topNav != null) {
			container.removeMember(topNav);
			container.removeMember(mainPane);

			topNav.destroy();
//			MainContentPane.destroyInstance();
			mainPane.destroy();

			topNav = null;
			mainPane = null;
		}
	}

	private void showDisablePane() {

		HLayout disableLayout = new HLayout();
		disableLayout.setWidth100();
		disableLayout.setHeight100();
		disableLayout.setBackgroundColor("darkgray");
		disableLayout.setOpacity(60);

		Label title = new Label("Disabled AdminConsole");
		title.setStyleName("AdminDisabledMessage");

		disableLayout.addMember(title);

		container.addMember(disableLayout);
	}

}
