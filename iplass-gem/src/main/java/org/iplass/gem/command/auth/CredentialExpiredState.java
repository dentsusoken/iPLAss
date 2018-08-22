/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.io.Serializable;

import org.iplass.mtp.auth.login.Credential;

public class CredentialExpiredState implements Serializable {
	private static final long serialVersionUID = -365463462100206431L;

	private final String id;
	private final String token;
	private Credential secondaryCredential;

	private final boolean rememberMe;
	private final boolean useTwoStep;
	private final String redirectPath;
	
	private final String policyName;
	
	public CredentialExpiredState(String id, String token, Credential secondaryCredential,
			boolean rememberMe, boolean useTwoStep, String redirectPath, String policyName) {
		this.id = id;
		this.token = token;
		this.secondaryCredential = secondaryCredential;
		this.rememberMe = rememberMe;
		this.useTwoStep = useTwoStep;
		this.redirectPath = redirectPath;
		this.policyName = policyName;
	}

	public String getId() {
		return id;
	}

	public String getToken() {
		return token;
	}

	public Credential getSecondaryCredential() {
		return secondaryCredential;
	}

	public boolean isRememberMe() {
		return rememberMe;
	}

	public boolean isUseTwoStep() {
		return useTwoStep;
	}
	
	public String getRedirectPath() {
		return redirectPath;
	}
	
	public String getPolicyName() {
		return policyName;
	}

}
