/*
 * Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Deprecated
public class UploadFileUtil {

	private static final Logger logger = LoggerFactory.getLogger(UploadFileUtil.class);

	public static void checkMagicByte(File tempFile, String type, String fileName) {
		byte[] buf = new byte[128];

		InputStream is = null;
		try {
			is = new FileInputStream(tempFile);
			is.read(buf);
		} catch (IOException e) {
			logger.warn("upload file is externally deleted. maybe contains virus." , e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					logger.warn("can not close resource:" + tempFile.getName(), e);
				}
			}
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

		String magicByte = temp.toString();

		if (type.equals("image/gif")) {
			if (!(magicByte.startsWith("474946383761") || magicByte.startsWith("474946383961"))) {
				throw new ApplicationException(resourceString("impl.web.fileupload.UploadFileHandleImpl.invalidFileMsg", (Object[])null));
			}
		}

		if (type.equals("image/bmp")) {
			if (!magicByte.startsWith("424d")) {
				throw new ApplicationException(resourceString("impl.web.fileupload.UploadFileHandleImpl.invalidFileMsg", (Object[])null));
			}
		}

		if (type.equals("image/jpeg")) {
			if (!magicByte.startsWith("ffd8")) {
				throw new ApplicationException(resourceString("impl.web.fileupload.UploadFileHandleImpl.invalidFileMsg", (Object[])null));
			}
		}

		if (type.equals("image/png")) {
			if (!magicByte.startsWith("89504e470d0a1a0a")) {
				throw new ApplicationException(resourceString("impl.web.fileupload.UploadFileHandleImpl.invalidFileMsg", (Object[])null));
			}
		}

		if (type.equals("application/x-shockwave-flash")) {
			if (!(magicByte.startsWith("465753") || magicByte.startsWith("435753"))) {
				throw new ApplicationException(resourceString("impl.web.fileupload.UploadFileHandleImpl.invalidFileMsg", (Object[])null));
			}
		}

		if (type.equals("application/pdf")) {
			if (!magicByte.startsWith("25504446")) {
				throw new ApplicationException(resourceString("impl.web.fileupload.UploadFileHandleImpl.invalidFileMsg", (Object[])null));
			}
		}

		// office97 - 2003
		if (type.startsWith("application/vnd.ms-") || type.startsWith("application/msword")) {

			// マクロ有効ブック対応
			if (magicByte.startsWith("504b030414000600")) {
				return;
			}
			// Microsoft Office データファイル または RTF（リッチテキストフォーマット）かを確認し、違う場合は拡張子を確認
			if (!magicByte.startsWith("d0cf11e0a1b11ae1") && !magicByte.startsWith("7b5c72746631")) {
				// IE かつ 拡張子がcsvの場合はOKとする
				String ext = StringUtil.substringAfterLast(fileName, ".");
//				if (!((isIe || isChrome) && StringUtils.equalsIgnoreCase(ext, "csv"))) {
//					throw new ApplicationException(ResourceBundleWrapper.getString("impl.web.fileupload.UploadFileHandleImpl.invalidFileMsg", (Object[])null));
//				}
				// 拡張子がcsvの場合はOKとする
				if (!(StringUtil.equalsIgnoreCase(ext, "csv"))) {
					throw new ApplicationException(resourceString("impl.web.fileupload.UploadFileHandleImpl.invalidFileMsg", (Object[])null));
				}
			}
		}

		// office97 - 2003のエクセルだけapplication/unknownになるので拡張子でチェック
		if (!StringUtil.isEmpty(fileName)) {
			String ext = StringUtil.substringAfterLast(fileName, ".");

			if (StringUtil.equalsIgnoreCase(ext, "xls")) {
				if (!magicByte.startsWith("d0cf11e0a1b11ae1")) {
					throw new ApplicationException(resourceString("impl.web.fileupload.UploadFileHandleImpl.invalidFileMsg", (Object[])null));
				}
			}
		}

		// office2007 -
		if (type.startsWith("application/vnd.openxmlformats-officedocument")) {
			if (!(magicByte.startsWith("504b030414000600") || magicByte.startsWith("d0cf11e0a1b11ae1"))) {

				// poiで生成したファイルの対応
				String ext = StringUtil.substringAfterLast(fileName, ".");
				if (!((magicByte.startsWith("504b030414000808") || magicByte.startsWith("d0cf11e0a1b11ae1"))
						&& (StringUtil.equalsIgnoreCase(ext, "xlsx") || StringUtil.equalsIgnoreCase(ext, "docx") || StringUtil.equalsIgnoreCase(ext, "pptx")))) {
					throw new ApplicationException(resourceString("impl.web.fileupload.UploadFileHandleImpl.invalidFileMsg", (Object[])null));
				}
			}
		}
	}

	private static String resourceString(String key, Object... arguments) {
		return CoreResourceBundleUtil.resourceString(key, arguments);
	}
}
