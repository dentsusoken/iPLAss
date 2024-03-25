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

package org.iplass.adminconsole.server.base.rpc.util;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.iplass.adminconsole.server.base.i18n.AdminResourceBundleUtil;
import org.iplass.adminconsole.shared.base.dto.AdminUncaughtException;
import org.iplass.adminconsole.shared.base.dto.auth.AdminAuthException;
import org.iplass.adminconsole.shared.base.dto.auth.LoginFailureException;
import org.iplass.adminconsole.shared.base.dto.auth.TenantNotFoundException;
import org.iplass.adminconsole.shared.base.dto.auth.UnauthenticatedException;
import org.iplass.adminconsole.shared.base.dto.auth.UnauthorizedAccessException;
import org.iplass.mtp.SystemException;
import org.iplass.mtp.auth.login.IdPasswordCredential;
import org.iplass.mtp.auth.login.LoginException;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.auth.AuthService;
import org.iplass.mtp.impl.auth.UserContext;
import org.iplass.mtp.impl.auth.authenticate.AnonymousUserContext;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.web.RequestPath;
import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.impl.web.i18n.LangSelector;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.transaction.Propagation;
import org.iplass.mtp.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AuthUtil {

	private static final Logger log = LoggerFactory.getLogger(AuthUtil.class);
	private static final Logger fatalLogger = LoggerFactory.getLogger("mtp.fatal");

// FIXME そのうち処理を移動する事

	/**
	 * 実行する処理を定義するインターフェース
	 *
	 * @param <R> 戻り値
	 */
	public static interface Callable<R> {
		public R call();
	}

	/**
	 * セキュリティチェックを介した処理を実行します。
	 * セキュリティエラー時は {@link AdminAuthException} が返ります。
	 *
	 * @param <R> 戻り値型
	 * @param context ServletContext
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param tenantId テナントID
	 * @param callable 実行処理
	 * @return 処理結果
	 */
	public static <R> R authCheckAndInvoke(
			final ServletContext context, final HttpServletRequest request, final HttpServletResponse response,
			final int tenantId, final Callable<R> callable) {

		return doInvoke(context, request, response, tenantId, () -> {

			//Admin許可チェック
			checkAdmin();

			AuthService auth = ServiceRegistry.getRegistry().getService(AuthService.class);

			return auth.doSecuredAction(AuthContextHolder.getAuthContext(), () -> callable.call());

		});
	}

	/**
	 * 認証・認可チェックを行います。
	 * セキュリティエラー時は {@link AdminAuthException} が返ります。
	 *
	 * @param context ServletContext
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param tenantId テナントID
	 */
	public static void authCheck(
			final ServletContext context, final HttpServletRequest request, final HttpServletResponse response,
			final int tenantId) {
		authCheckAndInvoke(context, request, response, tenantId, () -> null);
	}

	/**
	 * ログインします。
	 * ログインエラー時は {@link LoginFailureException} または {@link AdminAuthException} が返ります。
	 *
	 * @param context ServletContext
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param tenantId テナントID
	 * @param id ユーザーID
	 * @param password パスワード
	 */
	public static void authLogin(
			final ServletContext context, final HttpServletRequest request, final HttpServletResponse response,
			final int tenantId, String id, String password) {

		doInvoke(context, request, response, tenantId, () -> {

			AuthService auth = ServiceRegistry.getRegistry().getService(AuthService.class);
			try {
				//ログイン
				auth.login(new IdPasswordCredential(id, password));

				//Admin許可チェック
				checkAdmin();

				return null;
			} catch (LoginException e) {
				throw new LoginFailureException(e.getMessage());
			} catch (UnauthorizedAccessException e) {
				//許可されていないのでログオフを実施
				auth.logout();
				throw e;
			} catch (OutOfMemoryError e) {
				throw new SystemException(e);
			} catch (Throwable e) {
				throw e;
			}
		});
	}

	/**
	 * ログオフします。
	 *
	 * @param context ServletContext
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param tenantId テナントID
	 */
	public static void authLogoff(
			final ServletContext context, final HttpServletRequest request, final HttpServletResponse response,
			int tenantId) {

		doInvoke(context, request, response, tenantId, () -> {
			AuthService auth = ServiceRegistry.getRegistry().getService(AuthService.class);
			auth.logout();
			return null;
		});
	}

	private static <R> R doInvoke(
			final ServletContext context, final HttpServletRequest request, final HttpServletResponse response,
			final int tenantId, final Callable<R> callable) {

		try {
			//TenantContextの取得
			TenantContext tc = getTenantContext(tenantId);

			//ExecuteContextの初期化
			ExecuteContext ec = new ExecuteContext(tc);
			ExecuteContext.initContext(ec);

			//トランザクション開始
			return Transaction.with(Propagation.REQUIRED, t -> {

				WebRequestStack requestStack = null;
				try {
					//RequestStackの生成
					requestStack = new WebRequestStack(new RequestPath(tc.getTenantUrl()), context, request, response);

					//Lang set
					LangSelector ls = new LangSelector();
					ls.selectLangByRequest(requestStack.getRequestContext(), ExecuteContext.getCurrentContext());
					ls.selectLangByUser(requestStack.getRequestContext(), ExecuteContext.getCurrentContext());

					return callable.call();

				} finally {
					if (requestStack != null) {
						requestStack.finallyProcess();
					}
				}
			});

		} catch (AdminAuthException e) {
			//認証、認可レベルでのエラーの場合はINFOで出力
			log.info(e.getMessage());
			throw e;
		} catch (Throwable e) {
		    if (e instanceof Error) {
		        fatalLogger.error(e.getMessage(), e);
		    } else {
		    	log.error(e.getMessage(), e);
		    }

			if (e instanceof RuntimeException) {
				throw (RuntimeException)e;
			} else {
				throw new AdminUncaughtException(e);
			}
		} finally {
			ExecuteContext.finContext();
		}
	}

	private static TenantContext getTenantContext(int tenantId) {
		if(log.isTraceEnabled()) {
			log.trace(rs("util.AuthUtil.runAdminConsoleId", tenantId));
		}

		TenantContext tc = ServiceRegistry.getRegistry().getService(TenantContextService.class).getTenantContext(tenantId);
		if(tc == null) {
			throw new TenantNotFoundException(rs("util.AuthUtil.canNotGetTenantInfo"));
		}

		return tc;
	}

	private static void checkAdmin() {
		UserContext userContext = AuthContextHolder.getAuthContext().getUserContext();

		if (userContext instanceof AnonymousUserContext) {
			throw new UnauthenticatedException(rs("util.AuthUtil.notLogin"));
		}
		if (!userContext.getUser().isAdmin()) {
			throw new UnauthorizedAccessException(rs("util.AuthUtil.notHavePermission"));
		}
	}

	private static String rs(String key, Object... arguments) {
		return AdminResourceBundleUtil.resourceString(key, arguments);
	}

}
