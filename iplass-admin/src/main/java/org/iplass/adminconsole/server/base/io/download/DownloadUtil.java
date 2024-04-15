/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.server.base.io.download;

import java.io.UnsupportedEncodingException;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.MediaType;

import org.iplass.adminconsole.shared.base.dto.io.download.DownloadProperty.ENCODE;
import org.iplass.mtp.util.StringUtil;

public class DownloadUtil {

	private final static MediaType TEXT_CSV_TYPE = new MediaType("text", "csv");
	private final static MediaType APPLICATION_ZIP_TYPE = new MediaType("application", "zip");

	private DownloadUtil(){}

	public static void setResponseHeader(HttpServletResponse resp, String contentType, String fileName) throws UnsupportedEncodingException {
		setResponseHeader(resp, contentType, fileName, ENCODE.UTF8.getValue());
	}

	public static void setResponseHeader(HttpServletResponse resp, String contentType, String fileName, String encode) throws UnsupportedEncodingException {

		resp.setContentType(contentType);

        // ファイル名を設定
        String fileFullName = StringUtil.escapeXml10(fileName, true);
        String downloadName = new String(fileFullName.getBytes("Shift_JIS"), "ISO-8859-1");
        resp.setHeader("Content-Disposition", "attachment; filename=" + downloadName);

        if (encode != null) {
        	resp.setCharacterEncoding(encode);
        }
	}

	public static void setCsvResponseHeader(HttpServletResponse resp, String fileName) throws UnsupportedEncodingException {
		setCsvResponseHeader(resp, fileName, ENCODE.UTF8.getValue());
	}

	public static void setCsvResponseHeader(HttpServletResponse resp, String fileName, String encode) throws UnsupportedEncodingException {
		setResponseHeader(resp, TEXT_CSV_TYPE.getType(), fileName, encode);
	}

	public static void setZipResponseHeader(HttpServletResponse resp, String fileName) throws UnsupportedEncodingException {
		setResponseHeader(resp, APPLICATION_ZIP_TYPE.getType(), fileName, null);
	}

}
