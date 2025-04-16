/*
 * Copyright (C) 2024 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.auth.webauthn.definition;

/**
 * <%if (doclang == 'ja') {%>
 * 認証器のアタッチメントタイプを表すEnum。
 * 
 * 認証器のアタッチメントは、WebAuthn認証プロセスで使用される認証器のモダリティを指定します。
 * 
 * <%} else {%>
 * Enum representing the types of authenticator attachments.
 *
 * Authenticator attachments specify the modality of the authenticator
 * used in the WebAuthn authentication process.
 * <%}%>
 */
public enum AuthenticatorAttachment {
	PLATFORM,
	CROSS_PLATFORM
}
