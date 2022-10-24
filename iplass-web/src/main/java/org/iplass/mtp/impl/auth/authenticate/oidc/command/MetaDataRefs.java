/*
 * Copyright (C) 2022 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.auth.authenticate.oidc.command;

import org.iplass.mtp.command.annotation.MetaDataSeeAlso;
import org.iplass.mtp.command.annotation.template.Template;

@MetaDataSeeAlso({
	AuthCommand.class,
	AuthCallbackCommand.class,
	AccountConnectCommand.class,
	AccountConnectCallbackCommand.class,
	AccountDisconnectCommand.class
})
@Template(name=MetaDataRefs.TMPL_OIDC_ERROR, displayName="OpenID Connect Error", path="/jsp/oidc/Error.jsp", contentType="text/html; charset=utf-8")
public class MetaDataRefs {
	public static final String TMPL_OIDC_ERROR = "oidc/Error";
}
