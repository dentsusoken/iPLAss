/*
 * Copyright (C) 2016 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.iplass.mtp.spi.ServiceConfigrationException;

public class SecureRandomGenerator {
	private static Encoder base64urlsafewop = Base64.getUrlEncoder().withoutPadding();
	
	private final Queue<SecureRandom> randoms = new ConcurrentLinkedQueue<SecureRandom>();
	
	private final int numBitsOfSecureRandomToken;
	/** 2-36,64 */
	private final int radixOfSecureRandomToken;
	private final boolean useStrongSecureRandom;
	private final String algorithm;
	private final String provider;
	
	public SecureRandomGenerator(int numBitsOfSecureRandomToken, int radixOfSecureRandomToken, boolean useStrongSecureRandom) {
		this.numBitsOfSecureRandomToken = numBitsOfSecureRandomToken;
		this.radixOfSecureRandomToken = radixOfSecureRandomToken;
		this.useStrongSecureRandom = useStrongSecureRandom;
		this.algorithm = null;
		this.provider = null;
	}
	
	public SecureRandomGenerator(int numBitsOfSecureRandomToken, int radixOfSecureRandomToken, String algorithm) {
		this.numBitsOfSecureRandomToken = numBitsOfSecureRandomToken;
		this.radixOfSecureRandomToken = radixOfSecureRandomToken;
		this.useStrongSecureRandom = false;
		this.algorithm = algorithm;
		this.provider = null;
	}
	
	public SecureRandomGenerator(int numBitsOfSecureRandomToken, int radixOfSecureRandomToken, String algorithm, String provider) {
		this.numBitsOfSecureRandomToken = numBitsOfSecureRandomToken;
		this.radixOfSecureRandomToken = radixOfSecureRandomToken;
		this.useStrongSecureRandom = false;
		this.algorithm = algorithm;
		this.provider = provider;
	}
	
	private SecureRandom createSecureRandom() {
		try {
			SecureRandom sr = null;
			if (algorithm != null) {
				if (provider != null) {
					sr = SecureRandom.getInstance(algorithm, provider);
				} else {
					sr = SecureRandom.getInstance(algorithm);
				}
			} else {
				if (useStrongSecureRandom) {
					sr = SecureRandom.getInstanceStrong();
				} else {
					sr = new SecureRandom();
				}
			}
			sr.nextInt();//Force seeding
			return sr;
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			throw new ServiceConfigrationException("invalid secure random algorithm/provider", e);
		}
	}
	
	public String secureRandomToken() {
		SecureRandom rand = randoms.poll();
		if (rand == null) {
			rand = createSecureRandom();
        }
		BigInteger randInt = new BigInteger(numBitsOfSecureRandomToken, rand);
		randoms.add(rand);
		if (radixOfSecureRandomToken == 64) {
			byte[] b = randInt.toByteArray();
			return base64urlsafewop.encodeToString(b);
		} else {
			return randInt.toString(radixOfSecureRandomToken);
		}
	}
	
	public int randomInt(int bound) {
		SecureRandom rand = randoms.poll();
		if (rand == null) {
			rand = createSecureRandom();
        }
		int randInt = rand.nextInt(bound);
		randoms.add(rand);
		return randInt;
	}

	public String randomString(int length, char[] values) {
		SecureRandom rand = randoms.poll();
		if (rand == null) {
			rand = createSecureRandom();
        }
		String out = "";
		for (int i = 0; i < length; i++) {
			int idx = rand.nextInt(values.length);
			out += values[idx];
        }
		return out;
	}

}
