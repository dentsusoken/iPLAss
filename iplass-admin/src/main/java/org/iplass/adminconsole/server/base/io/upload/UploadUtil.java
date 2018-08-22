/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.server.base.io.upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.FilenameUtils;
import org.iplass.adminconsole.server.base.i18n.AdminResourceBundleUtil;
import org.iplass.mtp.impl.web.WebFrontendService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UploadUtil {

	private static final Logger logger = LoggerFactory.getLogger(UploadUtil.class);

	private UploadUtil(){};

	public static File writeFileToTemporary(final FileItem fileItem, final File contextTempDir) {

		String fileName = FilenameUtils.getName(fileItem.getName());

		//tempディレクトリの取得
		File tempDir = getTempDir(contextTempDir);

		File tempFile = null;
		try {

			//Tempファイル作成
			tempFile = File.createTempFile("tmp", ".tmp", tempDir);

			if (logger.isDebugEnabled()) {
				logger.debug(fileName + " copy to " + tempFile.getName() + ". (dir " + tempDir.getPath() + ")");
			}

			//書き込み
			try (
				FileOutputStream fos = new FileOutputStream(tempFile);
				InputStream is = fileItem.getInputStream();
			) {
				Streams.copy(is, fos, true);
			}
		} catch (IOException e) {
			throw new UploadRuntimeException(rs("upload.UploadUtil.errReadFile"), e);
		}
		return tempFile;
	}

	private static File getTempDir(final File contextTempDir) {
		WebFrontendService webFront = ServiceRegistry.getRegistry().getService(WebFrontendService.class);
		File tempDir = null;
		if (webFront.getTempFileDir() == null) {
			tempDir = contextTempDir;
		} else {
			tempDir = new File(webFront.getTempFileDir());
		}
		return tempDir;
	}

	public static String getValueAsString(final FileItem fileItem) {

		try (InputStream is = fileItem.getInputStream()){
			return Streams.asString(is, "UTF-8");
		} catch (IOException e) {
			throw new UploadRuntimeException(rs("upload.UploadUtil.errReadParam"), e);
		}
	}

	/**
	 * {@link UploadResponseInfo} をJSON形式の文字列に変換します。
	 *
	 * @param response レスポンス
	 * @return JSON形式のレスポンス
	 */
	public static String toJsonResponse(UploadResponseInfo response) {

		if (response == null) {
			return "";
		}

		final StringBuilder builder = new StringBuilder();

		try {
			//JSONで出力
			ObjectMapper mapper = new ObjectMapper();
			JsonFactory f = new JsonFactory();
			JsonGenerator g = f.createGenerator(new Writer() {

				@Override
				public void write(char[] cbuf, int off, int len) throws IOException {
					builder.append(cbuf, off, len);
				}

				@Override
				public void flush() throws IOException {
				}

				@Override
				public void close() throws IOException {
				}
			});

			mapper.writeValue(g, response);
			g.close();
		} catch (IOException e) {
			throw new UploadRuntimeException(rs("upload.UploadUtil.errWriteResponse"), e);
		}

		return builder.toString();
	}

	private static String rs(String key, Object... arguments) {
		return AdminResourceBundleUtil.resourceString(key, arguments);
	}

}
