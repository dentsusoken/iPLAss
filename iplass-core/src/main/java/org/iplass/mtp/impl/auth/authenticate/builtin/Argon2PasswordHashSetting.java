/*
 * Copyright (C) 2023 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.auth.authenticate.builtin;

import java.io.UnsupportedEncodingException;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;
import org.iplass.mtp.impl.util.HashUtil;
import org.iplass.mtp.spi.ServiceConfigrationException;

public class Argon2PasswordHashSetting extends PasswordHashSetting {

	public static final String ALG_ARGON2_D = "argon2d";
	public static final String ALG_ARGON2_I = "argon2i";
	public static final String ALG_ARGON2_ID = "argon2id";

	private int parallelism;
	private int memorySizeKB;
	private int iterations;
	private int hashLength = 32;
	private int algArgon2 = Argon2Parameters.ARGON2_id;

	@Override
	public void setPasswordHashAlgorithm(String passwordHashAlgorithm) {
		if (passwordHashAlgorithm == null) {
			//default is ARGON2_id
			algArgon2 = Argon2Parameters.ARGON2_id;
		}
		switch (passwordHashAlgorithm.toLowerCase()) {
		case ALG_ARGON2_D:
			algArgon2 = Argon2Parameters.ARGON2_d;
			break;
		case ALG_ARGON2_I:
			algArgon2 = Argon2Parameters.ARGON2_i;
			break;
		case ALG_ARGON2_ID:
			algArgon2 = Argon2Parameters.ARGON2_id;
			break;
		default:
			throw new IllegalArgumentException("unknown Algorithm:" + passwordHashAlgorithm);
		}
		super.setPasswordHashAlgorithm(passwordHashAlgorithm);
	}
	public int getParallelism() {
		return parallelism;
	}
	public void setParallelism(int parallelism) {
		this.parallelism = parallelism;
	}
	public int getMemorySizeKB() {
		return memorySizeKB;
	}
	public void setMemorySizeKB(int memorySizeKB) {
		this.memorySizeKB = memorySizeKB;
	}
	public int getHashLength() {
		return hashLength;
	}
	public void setHashLength(int hashLength) {
		this.hashLength = hashLength;
	}
	public int getIterations() {
		return iterations;
	}
	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	@Override
	public void checkValidConfiguration() {
		if (algArgon2 != Argon2Parameters.ARGON2_d
				&& algArgon2 != Argon2Parameters.ARGON2_i
				&& algArgon2 != Argon2Parameters.ARGON2_id) {
			throw new ServiceConfigrationException("invalid PasswordHashAlgorithm:" + getPasswordHashAlgorithm());
		}
		if (parallelism < 1) {
			throw new ServiceConfigrationException("invalid parallelism:" + parallelism);
		}
		if (memorySizeKB < 1) {
			throw new ServiceConfigrationException("invalid memorySizeKB:" + memorySizeKB);
		}
		if (iterations < 1) {
			throw new ServiceConfigrationException("invalid iterations:" + iterations);
		}
		if (hashLength < 1) {
			throw new ServiceConfigrationException("invalid hashLength:" + hashLength);
		}
	}

	@Override
	protected String hash(String password, String salt) {
		byte[] ret = new byte[hashLength];
		
		Argon2Parameters.Builder pb = new Argon2Parameters.Builder(algArgon2)
				.withParallelism(parallelism)
				.withMemoryAsKB(memorySizeKB)
				.withIterations(iterations);
		try {
			pb.withSalt(salt.getBytes("UTF-8"));
			if (getSystemSalt() != null) {
				pb.withSecret(getSystemSalt().getBytes("UTF-8"));
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		
		Argon2Parameters params = pb.build();
		Argon2BytesGenerator generator = new Argon2BytesGenerator();
		generator.init(params);
		generator.generateBytes(password.toCharArray(), ret);
		StringBuilder sb = new StringBuilder();
		return HashUtil.hexToString(sb, ret);
	}

}
