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

public interface AuthCommandConstants {
	
	public static final String PARAM_USER_ID = "id";
	public static final String PARAM_PASSWORD = "password";
	public static final String PARAM_BACK_URL = "backurl";
	public static final String PARAM_NEW_PASSWORD = "newPassword";
	public static final String PARAM_CONFIRM_PASSWORD = "confirmPassword";
	public static final String PARAM_RESET_RANDOM_PASSWORD = "resetRandomPassword";
	
	public static final String PARAM_REMEMBER_ME = "rememberMe";
	
	public static final String PARAM_PROCESS_TOKEN = "pt";
	public static final String PARAM_CODE = "code";
	public static final String PARAM_GENERATOR = "gen";
	public static final String PARAM_Q_SET = "qset";
	public static final String PARAM_Q_ID = "qid_";
	public static final String PARAM_ANSWER = "answer_";
	
	public static final String PARAM_VERIFY_METHOD_INDEX = "vmi";
	
	public static final String RESULT_ERROR = "mtp.auth.error";
	public static final String RESULT_REDIRECT_PATH = "mtp.auth.redirectPath";
	public static final String RESULT_PASSWORD_EXPIRE_USER_ID = "mtp.auth.passwordExpire.id";
	public static final String RESULT_EXPIRE_PATH = "expirePath";
	
	public static final String RESULT_TWOSTEP_STATE = "mtp.auth.twostep.state";
	public static final String RESULT_AVAILABLE_POLICY = "mtp.auth.twostep.policy";
	
	public static final String SESSION_CREDENTIAL_EXPIRE_STATE = "mtp.auth.expire.state";
	public static final String SESSION_REMEMBER_ME = "mtp.auth.rememberme";
	
	
	

}
