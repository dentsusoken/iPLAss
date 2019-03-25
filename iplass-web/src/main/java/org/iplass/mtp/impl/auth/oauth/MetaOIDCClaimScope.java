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
package org.iplass.mtp.impl.auth.oauth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.auth.User;
import org.iplass.mtp.auth.oauth.definition.ClaimMappingDefinition;
import org.iplass.mtp.auth.oauth.definition.OIDCClaimScopeDefinition;
import org.iplass.mtp.auth.oauth.definition.ScopeDefinition;
import org.iplass.mtp.impl.auth.oauth.MetaClaimMapping.ClaimMappingRuntime;

/**
 * OpenID Connectでclaimのセットを定義するためのScope。
 * 
 * @author K.Higuchi
 *
 */
public class MetaOIDCClaimScope extends MetaScope {
	private static final long serialVersionUID = 4677632824181886235L;

	private List<MetaClaimMapping> claims;
	
	public List<MetaClaimMapping> getClaims() {
		return claims;
	}
	public void setClaims(List<MetaClaimMapping> claims) {
		this.claims = claims;
	}
	
	@Override
	public void applyConfig(ScopeDefinition def) {
		super.applyConfig(def);
		OIDCClaimScopeDefinition odef = (OIDCClaimScopeDefinition) def;
		if (odef.getClaims() != null) {
			claims = new ArrayList<>();
			for (ClaimMappingDefinition cmd: odef.getClaims()) {
				MetaClaimMapping mcm = new MetaClaimMapping();
				mcm.applyConfig(cmd);
				claims.add(mcm);
			}
		} else {
			claims = null;
		}
	}
	
	@Override
	public OIDCClaimScopeDefinition currentConfig() {
		OIDCClaimScopeDefinition def = new OIDCClaimScopeDefinition();
		fill(def);
		if (claims != null) {
			ArrayList<ClaimMappingDefinition> list = new ArrayList<>();
			for (MetaClaimMapping mcm: claims) {
				list.add(mcm.currentConfig());
			}
			def.setClaims(list);
		}
		
		return def;
	}
	
	public OIDCClaimScopeRuntime createRuntime(String defName) {
		return new OIDCClaimScopeRuntime(defName);
	}
	
	public class OIDCClaimScopeRuntime {
		
		private List<ClaimMappingRuntime> claimRuntimes;
		
		private OIDCClaimScopeRuntime(String defName) {
			if (claims != null) {
				claimRuntimes = new ArrayList<>();
				for (MetaClaimMapping m: claims) {
					claimRuntimes.add(m.createRuntime(defName, getName()));
				}
			}
		}
		
		public void map(User user, Map<String, Object> claimMap) {
			if (claimRuntimes != null) {
				for (ClaimMappingRuntime cmr: claimRuntimes) {
					Object val = cmr.value(user);
					if (val != null) {
						claimMap.put(cmr.name(), val);
					}
				}
			}
		}
	}
}
