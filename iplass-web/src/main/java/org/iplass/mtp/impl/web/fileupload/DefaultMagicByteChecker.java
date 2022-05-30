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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.impl.util.CoreResourceBundleUtil;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultMagicByteChecker implements MagicByteChecker {

	private static final Logger logger = LoggerFactory.getLogger(DefaultMagicByteChecker.class);

	private List<MagicByteRule> magicByteRule;

	public List<MagicByteRule> getMagicByteRule() {
		return magicByteRule;
	}

	public void setMagicByteRule(List<MagicByteRule> magicByteRule) {
		this.magicByteRule = magicByteRule;
	}

	@Override
	public void checkMagicByte(File tempFile, String mineType, String fileName) {
		String extension = StringUtil.substringAfterLast(fileName, ".");
		String magicByte = readMagicByte(tempFile);
		if(!isCorrectMagicByte(mineType, extension, magicByte)) {
			throw new ApplicationException(resourceString("impl.web.fileupload.UploadFileHandleImpl.invalidFileMsg", (Object[])null));
		}
	}
	
	private boolean isCorrectMagicByte(String mineType, String extension, String magicByte) {
		//mineTypeとextensionの組み合わせが定義されていない場合はマジックバイトチェックをしない
		if(magicByteRule == null || !magicByteRule.stream()
				.filter(rule -> rule.matchMineType(mineType))
				.filter(rule -> rule.matchExtension(extension))
				.findFirst().isPresent()) {
			return true;
		}

		return magicByteRule.stream()
				.filter(rule -> rule.matchMineType(mineType))
				.filter(rule -> rule.matchExtension(extension))
				.anyMatch(rule -> rule.matchMagicByte(magicByte));
	}
	
	private static String readMagicByte(File tempFile) {
		byte[] buf = new byte[128];

		try(InputStream is = new FileInputStream(tempFile);) {
			is.read(buf);
		} catch (IOException e) {
			logger.warn("upload file is externally deleted. maybe contains virus." , e);
		}

		StringBuffer temp = new StringBuffer();
		int cnt = 0;
		for (byte b : buf) {
			temp.append(String.format("%02x", b));
			cnt += 1;
			if (cnt > 64) {
				break;
			}
		}

		return temp.toString();
	}

	private static String resourceString(String key, Object... arguments) {
		return CoreResourceBundleUtil.resourceString(key, arguments);
	}
}
