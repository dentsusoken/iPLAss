/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

/**
 *
 */
package org.iplass.mtp.impl.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author 片野　博之
 *
 */
public final class HashUtil {
	/**
	 *
	 */
	private HashUtil() {
	}


	/**
	 * 指定の文字列のHashを取得する。<br>
	 * Hashを作成するには、String -> byte[]に変換する必要がある。本メソッドではデフォルトのエンコーディングを利用しているので、
	 * 文字コードを指定したい場合は、文字コードを引数に持つメソッドを利用すること。
	 * @param target Hashを作成する文字列
	 * @param algorithm Hashのアルゴリズム
	 * @return Hashの文字列表現
	 * @throws NoSuchAlgorithmException
	 * @see MessageDigest
	 * @see #digest(byte[], String)
	 */
	public static String digest(String target, String algorithm) throws NoSuchAlgorithmException {
		return digest(target.getBytes(), algorithm);
	}

	/**
	 * 指定の文字列のHashを取得する。<br>
	 * @param target Hashを作成する文字列
	 * @param algorithm Hashのアルゴリズム
	 * @return Hashの文字列表現
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 * @see MessageDigest
	 * @see #digest(byte[], String)
	 */
	public static String digest(String target, String algorithm, String charsetName) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		return digest(target.getBytes(charsetName), algorithm);
	}


	/**
	 * 指定のByte配列のHashを取得する。<br>
	 * 本メソッドでは、大きなデータに対してのHash作成には対応していない。もし大きいファイル当のHashを作成するときは、独自の実装をしてください。
	 *
	 * @param target Hashを取得するByte配列
	 * @param algorithm アルゴリズム
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String digest(byte[] target, String algorithm) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance(algorithm);
		byte[] buf = md.digest(target);
		StringBuilder sb = new StringBuilder();
		return hexToString(sb, buf);
	}

	public static String hexToString(StringBuilder sb, byte[] data) {
		for (int i = 0; i < data.length; i++) {
			hexDigit(sb,data[i]);
		}
		String buf = sb.toString();
		sb.delete(0, sb.length());
		return buf.toString();
	}

	/**
	 * 指定のByteの文字列表記を引数sbに詰め込みます。
	 * @param sb 詰め込むStringBuilder
	 * @param byte 変換するByte
	 */
	public static void hexDigit(StringBuilder sb, byte byte0) {

		char c = (char) (byte0 >> 4 & 15);
		if (c > '\t') {
			c = (char) ((c - 10) + 65);
		} else {
			c += '0';
		}
		sb.append(c);
		c = (char) (byte0 & 15);
		if (c > '\t') {
			c = (char) ((c - 10) + 65);
		} else {
			c += '0';
		}
		sb.append(c);
	}

}
