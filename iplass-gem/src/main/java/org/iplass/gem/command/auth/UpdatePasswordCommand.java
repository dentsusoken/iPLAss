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
import org.iplass.mtp.command.annotation.action.ActionMapping;
import org.iplass.mtp.command.annotation.action.ActionMapping.ClientCacheType;
import org.iplass.mtp.command.annotation.action.Result;
import org.iplass.mtp.command.annotation.action.Result.Type;
import org.iplass.mtp.web.actionmapping.definition.HttpMethodType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ActionMapping(name=UpdatePasswordCommand.ACTION_DO_UPDATE_PASSWORD,
		allowMethod=HttpMethodType.POST,
		clientCacheType=ClientCacheType.NO_CACHE,
		needTrustedAuthenticate=true,
		result=@Result(type=Type.TEMPLATE, value=Constants.TEMPLATE_UPDATE_PASSWORD)
)
@CommandClass(name="gem/auth/UpdatePasswordCommand", displayName="パスワード更新")
public final class UpdatePasswordCommand implements Command, AuthCommandConstants {

	private static Logger logger = LoggerFactory.getLogger(UpdatePasswordCommand.class);

	public static final String ACTION_DO_UPDATE_PASSWORD = "gem/auth/password/update";

	private AuthManager am = ManagerLocator.getInstance().getManager(AuthManager.class);

	@Override
	public String execute(RequestContext request) {

		String id = null;
		User current = AuthContext.getCurrentContext().getUser();
		if (current == null || current.isAnonymous()) {
			throw new SystemException("not logined");
		}
		id = current.getAccountId();

		String oldPass = request.getParam(PARAM_PASSWORD);
		String newPass1 = request.getParam(PARAM_NEW_PASSWORD);
		String newPass2 = request.getParam(PARAM_CONFIRM_PASSWORD);
		if (!checkSame(newPass1, newPass2)) {
			request.setAttribute(RESULT_PASSWORD_EXPIRE_USER_ID, id);
			request.setAttribute(RESULT_ERROR, new ApplicationException(resourceString("command.auth.UpdatePasswordCommand.notMatch")));
			return "ERROR";
		}

		if (!checkNotSame(oldPass, newPass1)) {
			request.setAttribute(RESULT_PASSWORD_EXPIRE_USER_ID, id);
			request.setAttribute(RESULT_ERROR, new ApplicationException(resourceString("command.auth.UpdatePasswordCommand.notSame")));
			return "ERROR";
		}

		Credential oldCredential = new IdPasswordCredential(id, oldPass);
		Credential newCredential = new IdPasswordCredential(id, newPass1);

		try {
			am.updateCredential(oldCredential, newCredential, current.getAccountPolicy());
			return "SUCCESS";
		} catch (CredentialUpdateException e) {
			if (logger.isDebugEnabled()) {
				logger.debug(e.getMessage(), e);
			}

			request.setAttribute(RESULT_PASSWORD_EXPIRE_USER_ID, id);
			request.setAttribute(RESULT_ERROR, e);
			return "ERROR";
		}
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

	private boolean checkNotSame(String oldPass, String newPass) {
		if (oldPass == null) {
			return false;
		}
		if (newPass == null) {
			return false;
		}
		return !oldPass.equals(newPass);
	}

	private static String resourceString(String key, Object... arguments) {
		return GemResourceBundleUtil.resourceString(key, arguments);
	}
}
