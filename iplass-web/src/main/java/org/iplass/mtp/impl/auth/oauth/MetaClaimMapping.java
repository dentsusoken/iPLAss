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

import org.iplass.mtp.auth.oauth.definition.ClaimMappingDefinition;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;

public class MetaClaimMapping implements MetaData {
	private static final long serialVersionUID = -3286338403698162013L;

	private String claimName;
	private String userPropertyName;
	private String customValueScript;
	
	public String getClaimName() {
		return claimName;
	}
	public void setClaimName(String claimName) {
		this.claimName = claimName;
	}
	public String getUserPropertyName() {
		return userPropertyName;
	}
	public void setUserPropertyName(String userPropertyName) {
		this.userPropertyName = userPropertyName;
	}
	public String getCustomValueScript() {
		return customValueScript;
	}
	public void setCustomValueScript(String customValueScript) {
		this.customValueScript = customValueScript;
	}

	@Override
	public MetaClaimMapping copy() {
		return ObjectUtil.deepCopy(this);
	}
	public void applyConfig(ClaimMappingDefinition cmd) {
		claimName = cmd.getClaimName();
		userPropertyName = cmd.getUserPropertyName();
		customValueScript = cmd.getCustomValueScript();
	}
	public ClaimMappingDefinition currentConfig() {
		ClaimMappingDefinition def = new ClaimMappingDefinition();
		def.setClaimName(claimName);
		def.setUserPropertyName(userPropertyName);
		def.setCustomValueScript(customValueScript);
		return def;
	}

}
