/*
 * Copyright (C) 2019 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.auth.oauth.definition.consents;

import org.iplass.mtp.auth.oauth.definition.ConsentTypeDefinition;

/**
 * 一度承認済みのscopeに関しては承認画面を出さないConsentType定義です。
 * ただし、scopeにoffline_accessを含む場合（RefreshTokenの発行）は、承認画面を表示します。
 * 
 * @author K.Higuchi
 *
 */
public class OnceConsentTypeDefinition extends ConsentTypeDefinition {
	private static final long serialVersionUID = -5487595498343449693L;
}
