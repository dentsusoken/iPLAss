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

package org.iplass.mtp.impl.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.iplass.mtp.web.actionmapping.definition.result.ContentDispositionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContentDispositionPolicy {

	private static Logger logger = LoggerFactory.getLogger(ContentDispositionPolicy.class);

	private static String CONTENT_DISPOSITION_FILENAME_FORMAT = "filename=\"%s\"; filename*=%s''%s";

	/** UserAgent識別子(*はデフォルト) */
	private String userAgentKey;

	/** 未指定時のContentDispositionType名 */
	private String defaultContentDispositionTypeName;

	/** ContentDispositionType名(*は全て) */
	private String contentDispositionTypeName;


	/** エスケープしない文字(Alphabet、数値は無条件でエスケープ除外) */
	private String unescapeCharacter;

	public String getUserAgentKey() {
		return userAgentKey;
	}

	public void setUserAgentKey(String userAgentKey) {
		this.userAgentKey = userAgentKey;
	}

	public String getDefaultContentDispositionTypeName() {
		return defaultContentDispositionTypeName;
	}

	public void setDefaultContentDispositionTypeName(String defaultContentDispositionTypeName) {
		this.defaultContentDispositionTypeName = defaultContentDispositionTypeName;
	}

	public String getContentDispositionTypeName() {
		return contentDispositionTypeName;
	}

	public void setContentDispositionTypeName(String contentDispositionTypeName) {
		this.contentDispositionTypeName = contentDispositionTypeName;
	}

	public String getUnescapeCharacter() {
		return unescapeCharacter;
	}

	public void setUnescapeCharacter(String unescapeCharacter) {
		this.unescapeCharacter = unescapeCharacter;
	}

	public boolean isDefault() {
		//userAgentKeyが"*"の場合、デフォルトに設定
		return "*".equals(userAgentKey);
	}

	public boolean match(String userAgent, ContentDispositionType type) {
		return matchAgent(userAgent) && matchType(type);
	}

	public String getContentDisposition(ContentDispositionType type, String fileName) throws IOException {
		StringBuilder ret = new StringBuilder();

		if (type == null) {
			//typeが直接指定されていない場合は、service-configで指定されている値を使用
			type = getDefaultContentDispositionType();
		}
		if (type != null) {
			ret.append(type.getTypeString());
		}
		ret.append(getFileNameString(fileName));

		logger.debug("{} 's content disposition ={}", fileName, ret.toString());
		return ret.toString();
	}

	private boolean matchAgent(String userAgent) {
		return (userAgent.indexOf(userAgentKey) != -1);
	}

	private boolean matchType(ContentDispositionType type) {
		ContentDispositionType target = type;
		//タイプが未指定の場合は、デフォルトでチェック
		if (type == null) {
			target = getDefaultContentDispositionType();
		}
		return (target == null
				|| "*".equals(contentDispositionTypeName)
				|| type == ContentDispositionType.valueOf(contentDispositionTypeName));
	}

	private ContentDispositionType getDefaultContentDispositionType() {
		if (defaultContentDispositionTypeName != null) {
			return ContentDispositionType.valueOf(defaultContentDispositionTypeName);
		}
		return null;
	}

	private String getFileNameString(String fileName) throws IOException {

		byte[] source = fileName.getBytes(StandardCharsets.UTF_8.name());
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream(source.length)) {
			for (byte b : source) {
				if (b < 0) {
					b += 256;
				}
				if (isUnescape(b)) {
					bos.write(b);
				}
				else {
					bos.write('%');
					char hex1 = Character.toUpperCase(Character.forDigit((b >> 4) & 0xF, 16));
					char hex2 = Character.toUpperCase(Character.forDigit(b & 0xF, 16));
					bos.write(hex1);
					bos.write(hex2);
				}
			}
			String escapeName = new String(bos.toByteArray(), "US-ASCII");
			return String.format(CONTENT_DISPOSITION_FILENAME_FORMAT, fileName, StandardCharsets.UTF_8.name(), escapeName);
		}
	}

	private boolean isUnescape(int ch) {
		if (ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z') {
			return true;
		}
		if (ch >= '0' && ch <= '9') {
			return true;
		}
		//外部で指定されている文字列
		if (unescapeCharacter != null) {
			return unescapeCharacter.indexOf(ch) >= 0;
		}
		return false;
	}

}
