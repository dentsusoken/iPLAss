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

package org.iplass.mtp.impl.entity.auditlog;

import java.util.regex.Pattern;

public class LogRegexMaskHandler implements LogMaskHandler {

	private String maskChar = "*";
	private String maskRegex;
	private Pattern pattern;

	public String getMaskChar() {
		return maskChar;
	}

	public void setMaskChar(String maskChar) {
		this.maskChar = maskChar;
	}

	public String getMaskRegex() {
		return maskRegex;
	}

	public void setMaskRegex(String maskRegex) {
		this.maskRegex = maskRegex;
		// 正規表現にあわせてPatternも生成しておく
		pattern = Pattern.compile(maskRegex);
	}

	@Override
	public String mask(String value) {
		// 指定された正規表現でマスクする
		return pattern.matcher(value).replaceAll(maskChar);
	}
}
