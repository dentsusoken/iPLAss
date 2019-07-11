/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.core.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class FileUtil {
	
	private static Logger logger = LoggerFactory.getLogger(FileUtil.class);
	
	static String readContent(String fileName) throws IOException {
		InputStream is = null;
		try {
			is = FileUtil.class.getResourceAsStream(fileName);
			if (is != null) {
				if (logger.isDebugEnabled()) {
					logger.debug("File:" + fileName + " found from classpath(classloader).");
				}
			} else {
				File file = new File(fileName);
				if (file.exists()) {
					try {
						is = new FileInputStream(file);
						if (logger.isDebugEnabled()) {
							logger.debug("File:" + fileName + " found from OS file system.");
						}
					} catch (FileNotFoundException e) {
						if (logger.isDebugEnabled()) {
							logger.debug("File:" + fileName + " not found.", e);
						}
					}
				}
			}

			if (is == null) {
				return null;
			}
			
			InputStreamReader r = new InputStreamReader(is, "utf-8");
			StringBuffer str = new StringBuffer();
			char[] buf = new char[1024];
			int length = 0;
			while ((length = r.read(buf)) != -1) {
				str.append(buf, 0, length);
			}
			return str.toString();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					logger.warn("resource close failed. Maybe Resource Leak.", e);
				}
			}
		}
	}

}
