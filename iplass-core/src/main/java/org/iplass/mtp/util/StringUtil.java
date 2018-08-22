/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.text.translate.AggregateTranslator;
import org.apache.commons.text.translate.CharSequenceTranslator;
import org.apache.commons.text.translate.EntityArrays;
import org.apache.commons.text.translate.LookupTranslator;
import org.iplass.mtp.impl.util.random.SecureRandomGenerator;
import org.iplass.mtp.impl.util.random.SecureRandomService;
import org.iplass.mtp.spi.ServiceRegistry;

/**
 * 文字列操作のユーティリティ。
 * CommonsLangの各ユーティリティへのThinWapper。
 *
 * @author K.Higuchi
 *
 */
public class StringUtil {

	private static class RandomHolder {
		static final SecureRandomGenerator random = ServiceRegistry.getRegistry().getService(SecureRandomService.class).createGenerator();
	}

	private static final Map<CharSequence, CharSequence> APOS_ESCAPE;
	static {
		final Map<CharSequence, CharSequence> initMap = new HashMap<>();
		initMap.put("'", "&#039;");
		APOS_ESCAPE = Collections.unmodifiableMap(initMap);
	}

	private static final CharSequenceTranslator ESCAPE_HTML4_SP =
			new AggregateTranslator(
				new LookupTranslator(EntityArrays.BASIC_ESCAPE),
				new LookupTranslator(APOS_ESCAPE),
				new LookupTranslator(EntityArrays.ISO8859_1_ESCAPE),
				new LookupTranslator(EntityArrays.HTML40_EXTENDED_ESCAPE)
			);

	/**
	 * 文字列中に、'があった場合、''とエスケープする。
	 * Likeの_、%のエスケープは未対応。
	 *
	 * @param str
	 * @return
	 */
	public static String escapeEql(String str) {
		if (str == null) {
			return null;
		}
		return StringUtils.replace(str, "'", "''");
	}

	/**
	 * Likeのパターン文中に利用する文字列のエスケープ処理。
	 * %、_、\をそれぞれ、\%、\_、\\とエスケープ。<br>
	 * <b>※ '（シングルクォート）のエスケープはこのメソッドでは行わない。’のエスケープする場合は、esacleEqlも同時に利用すること。<b>
	 *
	 * @param str
	 * @return
	 */
	public static String escapeEqlForLike(String str) {
		if (str == null) {
			return null;
		}

		boolean needSanitaizing = false;
		char current = 0;
		for (int i = 0; i < str.length(); i++) {
			current = str.charAt(i);
		
			switch (current) {
		//	case '\'':
			case '%':
			case '_':
			case '\\':
				needSanitaizing = true;
				break;
			default:
				break;
			}
		
			if (needSanitaizing) {
				break;
			}
		}
		
		if (!needSanitaizing) {
			return str;
		}
		
		StringBuilder buff = new StringBuilder();
		
		for (int i = 0; i < str.length(); i++) {
			current = str.charAt(i);
			switch (current) {
			//case '\'':
			//buff.append('\'');
			//break;
			case '%':
			case '_':
			case '\\':
				buff.append('\\');
				break;
			default:
				break;
			}
			
			buff.append(current);
		}
		
		return buff.toString();
	}

	/**
	 * XML1.0仕様に基づくエスケープ処理をする。
	 *
	 * @param str
	 * @return エスケープされた文字列。strがnullの場合はnullが返却
	 */
	public static String escapeXml10(String str) {
		return StringEscapeUtils.escapeXml10(str);
	}

    /**
	 * XML1.0仕様に基づくエスケープ処理をする。
	 *
	 * @param str
	 * @param emptyIfNull strがnullの場合、空文字で返却するか否か
	 * @return エスケープされた文字列
	 */
	public static String escapeXml10(String str, boolean emptyIfNull) {
		if (emptyIfNull && str == null) {
			return "";
		}
		return StringEscapeUtils.escapeXml10(str);
	}
	/**
	 * XML1.1仕様に基づくエスケープ処理をする。
	 *
	 * @param str
	 * @return エスケープされた文字列。strがnullの場合はnullが返却
	 */
	public static String escapeXml11(String str) {
		return StringEscapeUtils.escapeXml11(str);
	}

    /**
	 * XML1.1仕様に基づくエスケープ処理をする。
	 *
	 * @param str
	 * @param emptyIfNull strがnullの場合、空文字で返却するか否か
	 * @return エスケープされた文字列
	 */
	public static String escapeXml11(String str, boolean emptyIfNull) {
		if (emptyIfNull && str == null) {
			return "";
		}
		return StringEscapeUtils.escapeXml11(str);
	}
	public static String unescapeXml(String str) {
		return StringEscapeUtils.unescapeXml(str);
	}
    /**
     * HTML4.0レベルで定義されるエンティティ、および'を&#039;に変換するエスケープ処理をする。
     *
     * @param str
	 * @return エスケープされた文字列。strがnullの場合はnullが返却
     */
	public static String escapeHtml(String str) {
		return ESCAPE_HTML4_SP.translate(str);
	}
	/**
     * HTML4.0レベルで定義されるエンティティ、および'を&#039;に変換するエスケープ処理をする。
	 *
	 * @param str
     * @param emptyIfNull strがnullの場合、空文字で返却するか否か
     * @return エスケープされた文字列
	 */
	public static String escapeHtml(String str, boolean emptyIfNull) {
		if (emptyIfNull && str == null) {
			return "";
		}
		return ESCAPE_HTML4_SP.translate(str);
	}
	public static String unescapeHtml(String str) {
		return StringEscapeUtils.unescapeHtml4(str);
	}

	/**
	 * ランダムな文字列を生成します。
	 * 長期間にわたり保存するトークンとして利用する場合、
	 * 当メソッドが返却する文字列では十分に安全ではない可能性があります。
	 * 
	 * @return
	 */
	public static String randomToken() {
		return RandomHolder.random.secureRandomToken();
	}

	public static boolean isEmpty(String str) {
		return StringUtils.isEmpty(str);
	}
	public static boolean isNotEmpty(String str) {
		return StringUtils.isNotEmpty(str);
	}
	public static boolean isBlank(String str) {
		return StringUtils.isBlank(str);
	}
	public static boolean isNotBlank(String str) {
		return StringUtils.isNotBlank(str);
	}
	public static String deleteWhitespace(String str) {
		return StringUtils.deleteWhitespace(str);
	}

	public static String leftPad(String str, int size, char padChar) {
		return StringUtils.leftPad(str, size, padChar);
	}

	public static String escapeJavaScript(String str) {
		return StringEscapeUtils.escapeEcmaScript(str);
	}

	public static String removeLineFeedCode(String str) {
		if (StringUtils.isEmpty(str)) {
			return str;
		} else {
			return str.replace("\n", "").replace("\r", "");
		}
	}

	/**
	 * JavaBeansの仕様にしたがってキャピタライズを行ないます。
	 * 大文字が2つ以上続く場合は、大文字にならないので注意してください。
	 *
	 * @param name
	 *            名前
	 * @return 結果の文字列
	 */
	public static String capitalize(final String name) {
		if (isEmpty(name)) {
			return name;
		}
		char chars[] = name.toCharArray();
		chars[0] = Character.toUpperCase(chars[0]);
		return new String(chars);
	}

	/**
	 * JavaBeansの仕様にしたがってデキャピタライズを行ないます。
	 * 大文字が2つ以上続く場合は、小文字にならないので注意してください。
	 *
	 * @param name 名前
	 * @return 結果の文字列
	 */
	public static String decapitalize(final String name) {
		if (isEmpty(name)) {
			return name;
		}
		char chars[] = name.toCharArray();
		if (chars.length >= 2 && Character.isUpperCase(chars[0])
				&& Character.isUpperCase(chars[1])) {
			return name;
		}
		chars[0] = Character.toLowerCase(chars[0]);
		return new String(chars);
	}

	/**
	 * _記法をキャメル記法に変換します。
	 *
	 * @param s
	 *            テキスト
	 * @return 結果の文字列
	 */
	public static String camelize(String s) {
		if (s == null) {
			return null;
		}
		s = s.toLowerCase();
		//String[] array = StringUtil.split(s, "_");
		String[] array = s.split("_");
		if (array.length == 1) {
			return StringUtil.capitalize(s);
		}
		StringBuffer buf = new StringBuffer(40);
		for (int i = 0; i < array.length; ++i) {
			buf.append(StringUtil.capitalize(array[i]));
		}
		return buf.toString();
	}

	/**
	 * キャメル記法を_記法に変換します。
	 *
	 * @param s
	 *            テキスト
	 * @return 結果の文字列
	 */
	public static String decamelize(final String s) {
		if (s == null) {
			return null;
		}
		if (s.length() == 1) {
			return s.toUpperCase();
		}
		StringBuffer buf = new StringBuffer(40);
		int pos = 0;
		for (int i = 1; i < s.length(); ++i) {
			if (Character.isUpperCase(s.charAt(i))) {
				if (buf.length() != 0) {
					buf.append('_');
				}
				buf.append(s.substring(pos, i).toUpperCase());
				pos = i;
			}
		}
		if (buf.length() != 0) {
			buf.append('_');
		}
		buf.append(s.substring(pos, s.length()).toUpperCase());
		return buf.toString();
	}

	public static String remove(String arg0, String arg1) {
		return StringUtils.remove(arg0, arg1);
	}

	public static int countMatches(String str, String sub) {
		return StringUtils.countMatches(str, sub);
	}

	public static String replace(String data, String from, String to) {
		return StringUtils.replace(data, from, to);
	}

	public static String replaceEach(String text, String[] searchList, String[] replacementList) {
		return StringUtils.replaceEach(text, searchList, replacementList);
	}

	public static String lowerCase(String str) {
		return StringUtils.lowerCase(str);
	}

	public static boolean startsWithAny(String arg0, String[] arg1) {
		return StringUtils.startsWithAny(arg0, arg1);
	}

	public static String[] split(String str, char separatorChar) {
		return StringUtils.split(str, separatorChar);
	}

	public static String join(Object[] array, String separator) {
		return StringUtils.join(array, separator);
	}

	public static String substringAfterLast(String str, String separator) {
		return StringUtils.substringAfterLast(str, separator);
	}

	public static boolean equalsIgnoreCase(String str1, String str2) {
		return StringUtils.equalsIgnoreCase(str1, str2);
	}

	public static boolean endsWithIgnoreCase(String str, String suffix) {
		return StringUtils.endsWithIgnoreCase(str, suffix);
	}

	public static String removeStart(String str, String remove) {
		return StringUtils.removeStart(str, remove);
	}

	public static String stripToEmpty(String str) {
		return StringUtils.stripToEmpty(str);
	}

	public static String stripToNull(String str) {
		return StringUtils.stripToNull(str);
	}

}
