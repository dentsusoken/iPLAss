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
 * <% if (doclang == "ja") {%>
 * Attestation conveyance preferenceを表すEnum。
 *
 * Attestation conveyance preferenceは、WebAuthn登録プロセス中に
 * Attestation情報がrelying partyにどのように伝達されるかを決定します。 
 * 
 * <% } else {%>
 * Enum representing the attestation conveyance preferences.
 * 
 * Attestation conveyance preferences determine how attestation information
 * is conveyed to the relying party during the WebAuthn registration process.
 * 
 * <%}%>
 * 
 */
public enum AttestationConveyancePreference {
	NONE,
	INDIRECT,
	DIRECT,
	ENTERPRISE
}
