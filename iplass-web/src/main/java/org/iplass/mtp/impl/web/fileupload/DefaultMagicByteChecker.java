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
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
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
	public void checkMagicByte(File tempFile, String mimeType, String fileName) {
		String extension = StringUtil.substringAfterLast(fileName, ".");
		byte[] magicByte = readMagicByte(tempFile);
		if(!isCorrectMagicByte(mimeType, extension, magicByte)) {
			throw new ApplicationException(resourceString("impl.web.fileupload.UploadFileHandleImpl.invalidFileMsg", (Object[])null));
		}
	}
	
	private boolean isCorrectMagicByte(String mimeType, String extension, byte[] magicByte) {
		if(magicByteRule == null) {
			return true;
		}
		
		List<MagicByteRule> filteredMagicByteRule = magicByteRule.stream()
				.filter(rule -> rule.matchMimeType(mimeType))
				.filter(rule -> rule.matchExtension(extension))
				.collect(Collectors.toList());
		
		//mimeTypeとextensionの組み合わせが定義されていない場合はマジックバイトチェックをしない
		return CollectionUtils.isEmpty(filteredMagicByteRule) || filteredMagicByteRule.stream()
				.anyMatch(rule -> rule.matchMagicByte(magicByte));
	}
	
	private static byte[] readMagicByte(File tempFile) {
		byte[] buf = new byte[128];

		try(InputStream is = new FileInputStream(tempFile);) {
			is.read(buf);
		} catch (IOException e) {
			logger.warn("upload file is externally deleted. maybe contains virus." , e);
		} 
		return buf;
	}

	private static String resourceString(String key, Object... arguments) {
		return CoreResourceBundleUtil.resourceString(key, arguments);
	}
}
