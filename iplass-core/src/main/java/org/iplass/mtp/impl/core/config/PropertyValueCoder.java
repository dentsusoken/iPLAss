/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.core.config;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.iplass.mtp.spi.ServiceConfigrationException;

public class PropertyValueCoder {
	public static final String PASSPHRASE_SUPPLIER = "passphraseSupplier";
	
	public static final String KEY_SALT = "keySalt";
	public static final String KEY_STRETCH = "keyStretch";
	public static final String KEY_ALGORITHM = "keyFactoryAlgorithm";
	public static final String KEY_LENGTH = "keyLength";
	public static final String CIPHER_ALGORITHM = "cipherAlgorithm";
	
	public static final String DEFAULT_KEY_ALGORITHM = "PBKDF2WithHmacSHA256";
	public static final int DEFAULT_KEY_LENGTH = 128;
	public static final String DEFAULT_CIPHER_ALGORITHM = "AES";
	public static final int DEFAULT_STRETCH = 2048;
	
	private String charset ="utf-8";//TODO 固定でいいよね？
	
	private Cipher cipher;
	private SecretKeySpec keySpec;
	
	public PropertyValueCoder(String keyAlg, int keyLength, String cipherAlg, byte[] salt, int stretch, PassphraseSupplier passphraseSupplier) {
		if (keyAlg == null) {
			keyAlg = DEFAULT_KEY_ALGORITHM;
		}
		if (keyLength < 0) {
			keyLength = DEFAULT_KEY_LENGTH;
		}
		if (cipherAlg == null) {
			cipherAlg = DEFAULT_CIPHER_ALGORITHM;
		}
		if (salt == null) {
			salt =  new byte[]{(byte)0xf5, (byte)0x1d, (byte)0x21,
	                   (byte)0x3d, (byte)0xf0, (byte)0x28,
	                   (byte)0x25, (byte)0x6d};
		}
		if (stretch < 0) {
			stretch = DEFAULT_STRETCH;
		}
		
		char[] passphrase = passphraseSupplier.getPassphrase();
		PBEKeySpec pbeKeySpec = new PBEKeySpec(passphrase, salt, stretch, keyLength);
		try {
			SecretKey secKey = SecretKeyFactory.getInstance(keyAlg).generateSecret(pbeKeySpec);
			Arrays.fill(passphrase, (char) 0);
			
			keySpec = new SecretKeySpec(secKey.getEncoded(), cipherAlg);
			cipher = Cipher.getInstance(cipherAlg + "/CBC/PKCS5Padding");
		} catch (InvalidKeySpecException e) {
			throw new ServiceConfigrationException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new ServiceConfigrationException(e);
		} catch (NoSuchPaddingException e) {
			throw new ServiceConfigrationException(e);
		}
		
		Arrays.fill(salt, (byte) 0);
	}
	
	public String encode(String plain) {
		
		try {
			cipher.init(Cipher.ENCRYPT_MODE, keySpec);
			byte[] enc = cipher.doFinal(plain.getBytes(charset));
			byte[] iv = cipher.getIV();
			byte[] ivAndEnc = new byte[iv.length + enc.length];
			System.arraycopy(iv, 0, ivAndEnc, 0, iv.length);
			System.arraycopy(enc, 0, ivAndEnc, iv.length, enc.length);
			
			return Base64.encodeBase64String(ivAndEnc);
		} catch (InvalidKeyException e) {
			throw new ServiceConfigrationException(e);
		} catch (IllegalBlockSizeException e) {
			throw new ServiceConfigrationException(e);
		} catch (BadPaddingException e) {
			throw new ServiceConfigrationException(e);
		} catch (UnsupportedEncodingException e) {
			throw new ServiceConfigrationException(e);
		}
	}
	
	public String decode(String encoded) {
		try {
			byte[] ivAndEnc = Base64.decodeBase64(encoded);
			int blockSize = cipher.getBlockSize();
			byte[] iv = new byte[blockSize];
			System.arraycopy(ivAndEnc, 0, iv, 0, blockSize);
			
			cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv));
			return new String(cipher.doFinal(ivAndEnc, blockSize, ivAndEnc.length - blockSize), charset);
		} catch (InvalidKeyException e) {
			throw new ServiceConfigrationException(e);
		} catch (InvalidAlgorithmParameterException e) {
			throw new ServiceConfigrationException(e);
		} catch (UnsupportedEncodingException e) {
			throw new ServiceConfigrationException(e);
		} catch (IllegalBlockSizeException e) {
			throw new ServiceConfigrationException(e);
		} catch (BadPaddingException e) {
			throw new ServiceConfigrationException(e);
		}
	}
	
	public void clear() {
		cipher = null;
		keySpec = null;
	}

}
