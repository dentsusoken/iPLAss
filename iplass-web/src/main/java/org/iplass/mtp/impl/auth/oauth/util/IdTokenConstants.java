/*
 * Copyright (C) 2022 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.auth.oauth.util;

public interface IdTokenConstants {
	static final String HEAER_ALG = "alg";
	
	static final String CLAIM_ISS = "iss";
	static final String CLAIM_SUB = "sub";
	static final String CLAIM_AUD = "aud";
	static final String CLAIM_EXP = "exp";
	static final String CLAIM_IAT = "iat";
	static final String CLAIM_AUTH_TIME = "auth_time";
	static final String CLAIM_NONCE = "nonce";
	static final String CLAIM_ACR = "acr";
	static final String CLAIM_AMR = "amr";
	static final String CLAIM_AZP = "azp";
	static final String CLAIM_AT_HASH = "at_hash";
	static final String CLAIM_C_HASH = "c_hash";

}
