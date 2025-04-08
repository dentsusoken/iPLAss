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
package org.iplass.mtp.impl.auth.authenticate.webauthn;

import java.io.Serializable;

public class WebAuthnState implements Serializable {
	private static final long serialVersionUID = 6611302510784613466L;

	private String metaDataName;
	private byte[] challenge;
	private String rpId;
	private byte[] userId;
	private long createTime = System.currentTimeMillis();

	public boolean isAvailable(long timeout) {
		if (System.currentTimeMillis() - createTime > timeout) {
			return false;
		} else {
			return true;
		}
	}

	public long getCreateTime() {
		return createTime;
	}

	public byte[] getUserId() {
		return userId;
	}

	public void setUserId(byte[] userId) {
		this.userId = userId;
	}

	public String getMetaDataName() {
		return metaDataName;
	}

	public void setMetaDataName(String metaDataName) {
		this.metaDataName = metaDataName;
	}

	public String getRpId() {
		return rpId;
	}

	public void setRpId(String rpId) {
		this.rpId = rpId;
	}

	public byte[] getChallenge() {
		return challenge;
	}

	public void setChallenge(byte[] challenge) {
		this.challenge = challenge;
	}

}
