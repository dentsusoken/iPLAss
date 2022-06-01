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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.collections4.CollectionUtils;

public class MagicByteRule {
	private String mimeTypePattern;
	private String extensionPattern;
	private List<String> magicByte;
	
	private Pattern mimeTypePatternCompile;
	private Pattern extensionPatternCompile;
	private List<byte[]> magicByteList;

	public String getMimeTypePattern() {
		return mimeTypePattern;
	}

	public void setMimeTypePattern(String mimeTypePattern) {
		this.mimeTypePattern = mimeTypePattern;
		if(mimeTypePattern != null) {
			this.mimeTypePatternCompile = Pattern.compile(mimeTypePattern);
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

	public List<String> getMagicByte() {
		return magicByte;
	}

	public void setMagicByte(List<String> magicByte) throws DecoderException {
		this.magicByte = magicByte;
		if(magicByte != null) {
			this.magicByteList = new ArrayList<>();
			for(String magicByteStr : magicByte) {
				this.magicByteList.add(Hex.decodeHex(magicByteStr.toCharArray()));
			}
		}
	}

	public boolean matchMimeType(String mimeType) {
		return this.mimeTypePatternCompile == null || this.mimeTypePatternCompile.matcher(mimeType).matches();
	}
	
	public boolean matchExtension(String extension) {
		return this.extensionPatternCompile == null || this.extensionPatternCompile.matcher(extension).matches();
	}
	
	public boolean matchMagicByte(byte[] magicByte) {
		return CollectionUtils.isEmpty(this.magicByteList) || this.magicByteList.stream()
				.anyMatch(m -> Arrays.equals(Arrays.copyOf(magicByte, m.length), m));
	}
}
