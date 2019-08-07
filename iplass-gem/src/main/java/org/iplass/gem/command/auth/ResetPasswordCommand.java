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

package org.iplass.gem.command.auth;

import java.util.List;

import org.iplass.gem.command.Constants;
import org.iplass.gem.command.GemResourceBundleUtil;
import org.iplass.gem.command.generic.detail.DetailViewCommand;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.SystemException;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.auth.AuthManager;
import org.iplass.mtp.auth.User;
import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.auth.login.IdPasswordCredential;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.CommandConfig;
import org.iplass.mtp.command.annotation.action.ActionMapping;
import org.iplass.mtp.command.annotation.action.ActionMapping.ClientCacheType;
import org.iplass.mtp.command.annotation.action.ActionMappings;
import org.iplass.mtp.command.annotation.action.ParamMapping;
import org.iplass.mtp.command.annotation.action.Result;
import org.iplass.mtp.command.annotation.action.Result.Type;
import org.iplass.mtp.command.annotation.action.TokenCheck;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.permission.EntityPermission;
import org.iplass.mtp.entity.permission.EntityPropertyPermission;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.tenant.TenantAuthInfo;

@ActionMappings({
	@ActionMapping(name=ResetPasswordCommand.ACTION_NAME,
			paramMapping=@ParamMapping(name=Constants.VIEW_NAME, mapFrom="${0}"),
			clientCacheType=ClientCacheType.NO_CACHE,
			command={
					@CommandConfig(commandClass=DetailViewCommand.class, value="cmd.detail=false;"),
					@CommandConfig(commandClass=ResetPasswordCommand.class)
				},
			result={
				@Result(status=Constants.CMD_EXEC_SUCCESS,type=Type.JSP,
						value=Constants.CMD_RSLT_JSP_VIEW,
						templateName="gem/generic/detail/view",
						layoutActionName=Constants.LAYOUT_NORMAL_ACTION),
				@Result(status=Constants.CMD_EXEC_ERROR, type=Type.TEMPLATE, value=Constants.TEMPLATE_EDIT)
				},
			tokenCheck=@TokenCheck
	),
	@ActionMapping(name=ResetPasswordCommand.ACTION_REF_NAME,
			paramMapping=@ParamMapping(name=Constants.VIEW_NAME, mapFrom="${0}"),
			clientCacheType=ClientCacheType.NO_CACHE,
			command={
					@CommandConfig(commandClass=DetailViewCommand.class, value="cmd.detail=false;"),
					@CommandConfig(commandClass=ResetPasswordCommand.class)
				},
			result={
				@Result(status=Constants.CMD_EXEC_SUCCESS,type=Type.JSP,
						value=Constants.CMD_RSLT_JSP_REF_VIEW,
								templateName="gem/generic/detail/ref/view",
						layoutActionName=Constants.LAYOUT_POPOUT_ACTION),
				@Result(status=Constants.CMD_EXEC_ERROR,type=Type.JSP,
						value=Constants.CMD_RSLT_JSP_REF_EDIT,
								templateName="gem/generic/detail/ref/edit",
						layoutActionName=Constants.LAYOUT_POPOUT_ACTION)
				},
			tokenCheck=@TokenCheck
	)
})
@CommandClass(name="gem/auth/ResetPasswordCommand", displayName="パスワードリセット")
public final class ResetPasswordCommand implements Command, AuthCommandConstants {

	public static final String ACTION_NAME = "gem/auth/password/reset";
	public static final String ACTION_REF_NAME = "gem/auth/password/ref/reset";

	private AuthManager am = ManagerLocator.getInstance().getManager(AuthManager.class);

	@Override
	public String execute(RequestContext request) {

		//ユーザチェック
		String oid = request.getParam(Constants.OID);
		if (oid == null) {
			throw new SystemException("oid is null");
		}

		EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);
		Entity user = EntityPermission.doQueryAs(EntityPermission.Action.UPDATE, () ->
				em.searchEntity(new Query().select(User.ACCOUNT_ID, User.ACCOUNT_POLICY)
						.from(User.DEFINITION_NAME)
						.where(new Equals(Entity.OID, oid))).getFirst());

		//アカウントID存在チェック
		String id = null;

		if (user != null) {
			id = user.getValue(User.ACCOUNT_ID);
		}

		if (id == null) {
			request.setAttribute(Constants.MESSAGE, resourceString("command.auth.ResetPasswordCommand.onlyAdmin"));
			return Constants.CMD_EXEC_ERROR;
		}

		//ユーザ更新時であること
		String execType = request.getParam(Constants.EXEC_TYPE);
		if(!Constants.EXEC_TYPE_UPDATE.equals(execType)){
			//ユーザ更新でないため、エラー
			request.setAttribute(Constants.MESSAGE, resourceString("command.auth.ResetPasswordCommand.onlyUpdate"));
			return Constants.CMD_EXEC_ERROR;
		}

		//アカウント管理のパスワードリセット機能が可能であること
		if(!am.canResetCredential(user.getValue(User.ACCOUNT_POLICY))) {
			//サポート外のため、エラー
			request.setAttribute(Constants.MESSAGE, resourceString("command.auth.ResetPasswordCommand.notSupport"));
			return Constants.CMD_EXEC_ERROR;
		}

		//管理者以外にもpasswordリセットを許可するか確認
		Tenant tenant = AuthContext.getCurrentContext().getTenant();
		//パスワードリセット実施者が管理者権限であること
		if (!isUserAdminRole(tenant)) {
			//管理者権限がないため、エラー
			request.setAttribute(Constants.MESSAGE, resourceString("command.auth.ResetPasswordCommand.onlyAdmin"));
			return Constants.CMD_EXEC_ERROR;
		}

		//UserのEntityProperty権限チェック
		if (!AuthContext.getCurrentContext().checkPermission(
				new EntityPropertyPermission(User.DEFINITION_NAME, User.PASSWORD, EntityPropertyPermission.Action.UPDATE))) {
			request.setAttribute(Constants.MESSAGE, resourceString("command.auth.ResetPasswordCommand.onlyAdmin"));
			return Constants.CMD_EXEC_ERROR;
		}

		Credential credential = new IdPasswordCredential(id, null);
		am.resetCredential(credential, user.getValue(User.ACCOUNT_POLICY));
		return Constants.CMD_EXEC_SUCCESS;
	}

	static boolean isUserAdminRole(Tenant tenant) {
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
