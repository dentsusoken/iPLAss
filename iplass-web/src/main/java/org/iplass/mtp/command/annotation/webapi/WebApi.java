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

package org.iplass.mtp.command.annotation.webapi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.iplass.mtp.command.annotation.CommandConfig;
import org.iplass.mtp.webapi.WebApiRequestConstants;
import org.iplass.mtp.webapi.definition.RequestType;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.StateType;


/**
 * WebAPIの定義。
 *
 * @author K.Takahashi
 *
 */
@Repeatable(WebApis.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface WebApi {

	String id() default "##default";
	String name();
	String displayName() default "##default";
	String description() default "##default";

	boolean checkXRequestedWithHeader() default true;
	boolean privilaged() default false;
	boolean publicWebApi() default false;
	boolean overwritable() default true;
	boolean permissionSharable() default false;

	String accessControlAllowOrign() default "";
	boolean accessControlAllowCredentials() default false;
	boolean needTrustedAuthenticate() default false;

	CommandConfig command() default @CommandConfig;
	RequestType[] accepts() default{};
	MethodType[] methods() default{};
	StateType state() default StateType.STATEFUL;
	boolean supportBearerToken() default false;

	String[] results() default {WebApiRequestConstants.DEFAULT_RESULT};

	RestJson restJson() default @RestJson(parameterName="");
	RestXml restXml() default @RestXml(parameterName="");

	/**
	 * <p>TokenCheck設定</p>
	 *
	 * デフォルトではTokenチェックは実行されません。
	 */
	WebApiTokenCheck tokenCheck() default @WebApiTokenCheck(executeCheck=false);

	/**
	 * このAction処理をSessionにて同期化するか否か。デフォルトfalse。
	 * @return
	 */
	boolean synchronizeOnSession() default false;

	String responseType() default "";
}
