/*
 * Copyright (C) 2018 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.auth.authenticate.ldap;

public class LdapUtil {
	//OWASP Enterprise Security API (ESAPI)を参考に。
	
	public static String escapeForFilter(String str) {
		if (str == null) {
			return null;
		}
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
				char c = str.charAt(i);
				switch (c) {
				case '\\':
					sb.append("\\5c");
					break;
				case '*': 
					sb.append("\\2a"); 
					break;
				case '(':
					sb.append("\\28");
					break;
				case ')':
					sb.append("\\29");
					break;
				case '\0':
					sb.append("\\00");
					break;
				default:
					sb.append(c);
				}
		}
		
		return sb.toString();
	}

	public static String escapeForDN(String str) {
		if (str == null) {
			return null;
		}
		
		StringBuilder sb = new StringBuilder();
		if ((str.length() > 0) && ((str.charAt(0) == ' ') || (str.charAt(0) == '#'))) {
			sb.append('\\');
		}
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			switch (c) {
			case '\\':
			case ',':
			case '+':
			case '"':
			case '<':
			case '>':
			case ';':
				sb.append('\\');
				break;
			default:
			}
			sb.append(c);
		}
		if ((str.length() > 1) && (str.charAt(str.length() - 1) == ' ')) {
			sb.insert(sb.length() - 1, '\\');
		}
		
		return sb.toString();
	}
}
