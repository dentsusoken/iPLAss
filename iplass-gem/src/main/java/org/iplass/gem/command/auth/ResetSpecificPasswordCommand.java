/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.gem.command.auth;

import java.util.List;

import org.iplass.gem.command.Constants;
import org.iplass.gem.command.GemResourceBundleUtil;
import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.SystemException;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.auth.AuthManager;
import org.iplass.mtp.auth.User;
import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.auth.login.CredentialUpdateException;
import org.iplass.mtp.auth.login.IdPasswordCredential;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.CommandConfig;
import org.iplass.mtp.command.annotation.action.ActionMapping;
import org.iplass.mtp.command.annotation.action.ActionMapping.ClientCacheType;
import org.iplass.mtp.command.annotation.action.ActionMappings;
import org.iplass.mtp.command.annotation.action.Result;
import org.iplass.mtp.command.annotation.action.Result.Type;
import org.iplass.mtp.command.annotation.action.TokenCheck;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.LoadOption;
import org.iplass.mtp.impl.auth.authenticate.builtin.policy.AuthenticationPolicyService;
import org.iplass.mtp.impl.auth.authenticate.builtin.policy.MetaAuthenticationPolicy.AuthenticationPolicyRuntime;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.tenant.TenantAuthInfo;
import org.iplass.mtp.web.actionmapping.definition.HttpMethodType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ActionMappings({
	@ActionMapping(name=ResetSpecificPasswordCommand.ACTION_VIEW_SPECIFIC_PASSWORD,
			allowMethod=HttpMethodType.POST,
			clientCacheType=ClientCacheType.CACHE,
			needTrustedAuthenticate=true,
			command={},
			result=@Result(type=Type.JSP,
							value=Constants.CMD_RSLT_JSP_RESET_SPECIFIC_PASSWORD,
							templateName="gem/auth/specificPassword",
							layoutActionName=Constants.LAYOUT_POPOUT_ACTION)
	),
	@ActionMapping(name=ResetSpecificPasswordCommand.ACTION_RESET_SPECIFIC_PASSWORD,
			allowMethod=HttpMethodType.POST,
			clientCacheType=ClientCacheType.NO_CACHE,
			needTrustedAuthenticate=true,
			command=@CommandConfig(commandClass=ResetSpecificPasswordCommand.class),
			result=@Result(type=Type.JSP,
					value=Constants.CMD_RSLT_JSP_RESET_SPECIFIC_PASSWORD,
					templateName="gem/auth/specificpassword/reset",
					layoutActionName=Constants.LAYOUT_POPOUT_ACTION),
			tokenCheck=@TokenCheck
	)
})
@CommandClass(name = "gem/auth/ResetSpecificPasswordCommand", description = "パスワード指定リセット")
public class ResetSpecificPasswordCommand implements Command, AuthCommandConstants {

	private static final Logger logger = LoggerFactory.getLogger(ResetSpecificPasswordCommand.class);
	
	public static final String ACTION_VIEW_SPECIFIC_PASSWORD = "gem/auth/specificpassword";
	public static final String ACTION_RESET_SPECIFIC_PASSWORD = "gem/auth/specificpassword/reset";

	private AuthManager am = ManagerLocator.getInstance().getManager(AuthManager.class);

	@Override
	public String execute(RequestContext request) {

		User current = AuthContext.getCurrentContext().getUser();
		if (current == null || current.isAnonymous()) {
			throw new SystemException("not logined");
		}

		// ユーザチェック
		String oid = request.getParam(Constants.OID);
		if (oid == null) {
			throw new SystemException("oid is null");
		}
		// アカウントID存在チェック
		EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);
		Entity user = em.load(oid, User.DEFINITION_NAME, new LoadOption(false, false));

		// アカウントID存在チェック
		String id = null;

		if (user != null) {
			id = user.getValue(User.ACCOUNT_ID);
		}

		if (id == null) {
			throw new SystemException("id is null");
		}

		AuthenticationPolicyService aps = ServiceRegistry.getRegistry().getService(AuthenticationPolicyService.class);
		AuthenticationPolicyRuntime policy = aps.getOrDefault(user.getValue(User.ACCOUNT_POLICY));
		if (!policy.isResetPasswordWithSpecificPassword()) {
			// 指定パスワードでリセットすることができないため、エラー
			request.setAttribute(RESULT_ERROR, new ApplicationException(resourceString("command.auth.ResetSpecificPasswordCommand.notAllowed")));
			return Constants.CMD_EXEC_ERROR;
		}

		String password = null;

		// パスワードを自動生成するか
		if (!isResetRandomPassword(request)) {
			String newPass1 = request.getParam(PARAM_NEW_PASSWORD);
			String newPass2 = request.getParam(PARAM_CONFIRM_PASSWORD);

			if (!checkSame(newPass1, newPass2)) {
				request.setAttribute(RESULT_PASSWORD_EXPIRE_USER_ID, id);
				request.setAttribute(RESULT_ERROR, new ApplicationException(resourceString("command.auth.UpdatePasswordCommand.notMatch")));
				return Constants.CMD_EXEC_ERROR;
			} 
			password = newPass1;
		}

		// ユーザ更新時であること
		String execType = request.getParam(Constants.EXEC_TYPE);
		if (!Constants.EXEC_TYPE_UPDATE.equals(execType)) {
			// ユーザ更新でないため、エラー
			request.setAttribute(Constants.MESSAGE, resourceString("command.auth.ResetPasswordCommand.onlyUpdate"));
			return Constants.CMD_EXEC_ERROR;
		}

		// アカウント管理のパスワードリセット機能が可能であること
		if (!am.canResetCredential(user.getValue(User.ACCOUNT_POLICY))) {
			// サポート外のため、エラー
			request.setAttribute(Constants.MESSAGE, resourceString("command.auth.ResetPasswordCommand.notSupport"));
			return Constants.CMD_EXEC_ERROR;
		}

		// 管理者以外にもpasswordリセットを許可するか確認
		Tenant tenant = AuthContext.getCurrentContext().getTenant();
		// パスワードリセット実施者が管理者権限であること
		if (!isUserAdminRole(tenant)) {
			// 管理者権限がないため、エラー
			request.setAttribute(Constants.MESSAGE, resourceString("command.auth.ResetPasswordCommand.onlyAdmin"));
			return Constants.CMD_EXEC_ERROR;
		}

		// passwordがnullの場合、自動生成するパスワードでリセットする。
		Credential credential = new IdPasswordCredential(id, password);
		try {
			am.resetCredential(credential);
			return Constants.CMD_EXEC_SUCCESS;
		} catch (CredentialUpdateException e) {
			if(logger.isDebugEnabled()) {
				logger.debug(e.getMessage(),e);
			}
			request.setAttribute(RESULT_ERROR, e);
			return Constants.CMD_EXEC_ERROR;
		}
	}

	private boolean isResetRandomPassword(RequestContext request) {
		String val = request.getParam(PARAM_RESET_RANDOM_PASSWORD);
		if (val != null && val.equals("1")) {
			return true;
		}
		return false;
	}
	
	private boolean checkSame(String pass1, String pass2) {
		if (pass1 == null) {
			return false;
		}
		if (pass2 == null) {
			return false;
		}
		return pass1.equals(pass2);
	}
	
	private boolean isUserAdminRole(Tenant tenant) {
		AuthContext auth = AuthContext.getCurrentContext();
		if (auth.getUser().isAdmin()) {
			return true;
		}
		List<String> userAdminRoles = tenant.getTenantConfig(TenantAuthInfo.class).getUserAdminRoles();
		if (userAdminRoles != null) {
			for (String role: userAdminRoles) {
				if (auth.userInRole(role)) {
					return true;
				}
			}
		}
		return false;
	}

	private static String resourceString(String key, Object... arguments) {
		return GemResourceBundleUtil.resourceString(key, arguments);
	}
}
