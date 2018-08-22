/*
 * Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.util.random;

public class SecureRandomGeneratorConfig {
	private int numBitsOfSecureRandomToken = 128;
	private int radixOfSecureRandomToken = 16;
	private boolean useStrongSecureRandom = false;
	private String algorithm;
	private String provider;
	
	public int getNumBitsOfSecureRandomToken() {
		return numBitsOfSecureRandomToken;
	}
	public void setNumBitsOfSecureRandomToken(int numBitsOfSecureRandomToken) {
		this.numBitsOfSecureRandomToken = numBitsOfSecureRandomToken;
	}
	public int getRadixOfSecureRandomToken() {
		return radixOfSecureRandomToken;
	}
	public void setRadixOfSecureRandomToken(int radixOfSecureRandomToken) {
		this.radixOfSecureRandomToken = radixOfSecureRandomToken;
	}
	public boolean isUseStrongSecureRandom() {
		return useStrongSecureRandom;
	}
	public void setUseStrongSecureRandom(boolean useStrongSecureRandom) {
		this.useStrongSecureRandom = useStrongSecureRandom;
	}
	public String getAlgorithm() {
		return algorithm;
	}
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	
	public SecureRandomGenerator createGenerator() {
		if (algorithm != null) {
			return new SecureRandomGenerator(numBitsOfSecureRandomToken, radixOfSecureRandomToken, algorithm, provider);
		} else {
			return new SecureRandomGenerator(numBitsOfSecureRandomToken, radixOfSecureRandomToken, useStrongSecureRandom);
		}
	}

}
