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
import java.util.List;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.collections4.CollectionUtils;

public class MagicByteRule {
	private MagicByteRuleCondition mimeType;
	private MagicByteRuleCondition extension;
	private List<String> magicByte;
	
	private List<byte[]> magicByteList;

	public MagicByteRuleCondition getMimeType() {
		return mimeType;
	}

	public void setMimeType(MagicByteRuleCondition mimeType) {
		this.mimeType = mimeType;
	}

	public MagicByteRuleCondition getExtension() {
		return extension;
	}

	public void setExtension(MagicByteRuleCondition extension) {
		this.extension = extension;
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
		return this.mimeType == null || this.mimeType.match(mimeType);
	}
	
	public boolean matchExtension(String extension) {
		return this.extension == null || this.extension.match(extension);
	}
	
	public boolean matchMagicByte(byte[] inputMagicByte) {
		if(CollectionUtils.isEmpty(this.magicByteList)) {
			return true;
		}
		
		return magicByteList.stream()
				.anyMatch(m -> matchMagicByte(m, inputMagicByte));
	}
	
	private boolean matchMagicByte(byte[] magicByte, byte[] inputMagicByte) {
		for(int index = 0 ; index < magicByte.length ; index++) {
			if(magicByte[index] != inputMagicByte[index]) {
				return false;
			}
		}
		return true;
	}
}
