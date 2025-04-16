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
 * <%if (doclang == "ja") {%>
 * レジデントキー（発見可能な資格情報）の要件を定義するEnum。
 * <%} else {%>
 * This enumeration defines the requirement for a Resident Key(discoverable credentials).
 * <%}%>
 */
public enum ResidentKeyRequirement {
	DISCOURAGED,
	PREFERRED,
	//note
	//requireResidentKey, of type boolean, defaulting to false
	//This member is retained for backwards compatibility with WebAuthn Level 1 and,
	//for historical reasons, its naming retains the deprecated “resident” terminology for discoverable credentials.
	//Relying Parties SHOULD set it to true if, and only if, residentKey is set to required.
	REQUIRED;
}
