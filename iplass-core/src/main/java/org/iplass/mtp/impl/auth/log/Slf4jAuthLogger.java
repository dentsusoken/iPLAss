/*
 * Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.auth.log;

import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.auth.login.CredentialUpdateException;
import org.iplass.mtp.impl.auth.UserContext;
import org.iplass.mtp.impl.auth.log.AuthLogConstants.LogKey;
import org.iplass.mtp.impl.auth.log.AuthLogConstants.Operation;
import org.iplass.mtp.impl.auth.log.AuthLogConstants.State;
import org.iplass.mtp.spi.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.logstash.logback.argument.StructuredArgument;
import net.logstash.logback.argument.StructuredArguments;

public class Slf4jAuthLogger extends AuthLoggerBase {

	public static final String DEFAULT_LOGGER_NAME ="mtp.auth";

	protected Logger auditLog;

	private String slf4LoggerName;

	public String getSlf4LoggerName() {
		return slf4LoggerName;
	}

	public void setSlf4LoggerName(String slf4LoggerName) {
		this.slf4LoggerName = slf4LoggerName;
	}

	@Override
	public void inited(AuthLoggerService service, Config config) {
		if (slf4LoggerName == null) {
			slf4LoggerName = DEFAULT_LOGGER_NAME;
		}

		auditLog = LoggerFactory.getLogger(slf4LoggerName);
	}

	@Override
	public void destroyed() {
	}

	@Override
	public void loginFail(Credential credential, Exception e) {
		if (auditLog.isWarnEnabled()) {
			if (e == null) {
				auditLog.warn("{},{},{}", argId(credential), Operation.LOGIN.getArg(), State.FAIL.getArg());
			} else {
				auditLog.warn("{},{},{},{}", argId(credential), Operation.LOGIN.getArg(), State.FAIL.getArg(), argExceptionMessage(e));
			}
		}
	}

	@Override
	public void loginLocked(Credential credential) {
		if (auditLog.isWarnEnabled()) {
			auditLog.warn("{},{},{}", argId(credential), Operation.LOGIN.getArg(), State.LOCKED.getArg());
		}
	}

	@Override
	public void loginSuccess(UserContext user) {
		if (auditLog.isInfoEnabled()) {
			auditLog.info("{},{},{}", argId(user), Operation.LOGIN.getArg(), State.SUCCESS.getArg());
		}
	}

	@Override
	public void loginPasswordExpired(Credential credential) {
		if (auditLog.isInfoEnabled()) {
			auditLog.info("{},{},{}", argId(credential), Operation.LOGIN.getArg(), State.PASSWORD_EXPIRED.getArg());
		}
	}

	@Override
	public void updatePasswordSuccess(Credential oldCredential) {
		if (auditLog.isInfoEnabled()) {
			auditLog.info("{},{},{}", argId(oldCredential), Operation.UPDATE_PASSWORD.getArg(), State.SUCCESS.getArg());
		}
	}

	@Override
	public void updatePasswordFail(Credential oldCredential, CredentialUpdateException e) {
		if (auditLog.isWarnEnabled()) {
			auditLog.warn("{},{},{},{}", argId(oldCredential), Operation.UPDATE_PASSWORD.getArg(), State.FAIL.getArg(),
					argExceptionMessage(e));
		}
	}

	@Override
	public void resetPasswordSuccess(Credential credential) {
		if (auditLog.isInfoEnabled()) {
			auditLog.info("{},{},{}", argId(credential), Operation.RESET_PASSSROD.getArg(), State.SUCCESS.getArg());
		}
	}

	/**
	 * ID ログ引数を取得する
	 * @param credential 認証情報
	 * @return ログ引数
	 */
	protected StructuredArgument argId(Credential credential) {
		if (credential.getId() == null) {
			return StructuredArguments.value(LogKey.ID, "unknown:" + credential.getClass().getSimpleName());
		}
		return StructuredArguments.value(LogKey.ID, credential.getId());
	}

	/**
	 * ID ログ引数を取得する
	 * @param user ユーザーコンテキスト
	 * @return ログ引数
	 */
	protected StructuredArgument argId(UserContext user) {
		return StructuredArguments.value(LogKey.ID, user.getUser().getAccountId());
	}

	/**
	 * 例外メッセージログ引数を取得する
	 * @param exception 対象例外
	 * @return ログ引数
	 */
	protected StructuredArgument argExceptionMessage(Exception exception) {
		return StructuredArguments.value(LogKey.EXCEPTION_MESSAGE, exception.toString());
	}
}
