/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.auth.oauth.definition;

import java.util.List;

/**
 * OpenID Connectでclaimのセットを定義するためのScope定義です。
 * 
 * @author K.Higuchi
 *
 */
public class OIDCClaimScopeDefinition extends ScopeDefinition {
	private static final long serialVersionUID = -3276795373511412049L;

	private List<ClaimMappingDefinition> claims;
	
	public List<ClaimMappingDefinition> getClaims() {
		return claims;
	}
	public void setClaims(List<ClaimMappingDefinition> claims) {
		this.claims = claims;
	}

}
