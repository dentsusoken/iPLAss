/*
 * Copyright (C) 2022 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.web.fileupload;

import java.util.regex.Pattern;

public class MagicByteRule {
	private String mineTypePattern;
	private String extensionPattern;
	private String magicBytePattern;
	
	private Pattern mineTypePatternCompile;
	private Pattern extensionPatternCompile;
	private Pattern magicBytePatternCompile;

	public String getMineTypePattern() {
		return mineTypePattern;
	}

	public void setMineTypePattern(String mineTypePattern) {
		this.mineTypePattern = mineTypePattern;
		if(mineTypePattern != null) {
			this.mineTypePatternCompile = Pattern.compile(mineTypePattern);
		}
	}

	public String getExtensionPattern() {
		return extensionPattern;
	}

	public void setExtensionPattern(String extensionPattern) {
		this.extensionPattern = extensionPattern;
		if(extensionPattern != null) {
			this.extensionPatternCompile = Pattern.compile(extensionPattern);
		}
	}

	public String getMagicBytePattern() {
		return magicBytePattern;
	}

	public void setMagicBytePattern(String magicBytePattern) {
		this.magicBytePattern = magicBytePattern;
		this.magicBytePatternCompile = Pattern.compile(magicBytePattern);
	}

	public boolean matchMineType(String mineType) {
		return this.mineTypePatternCompile == null || this.mineTypePatternCompile.matcher(mineType).matches();
	}
	
	public boolean matchExtension(String extension) {
		return this.extensionPatternCompile == null || this.extensionPatternCompile.matcher(extension).matches();
	}
	
	public boolean matchMagicByte(String magicByte) {
		return this.magicBytePatternCompile.matcher(magicByte).matches();
	}
}
