/*
 * Copyright (C) 2023 DENTSU SOKEN INC. All Rights Reserved.
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

import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.spi.ServiceInitListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Apache Tika を利用したファイルタイプ（MIME Type・メディアタイプ）検出機能
 *
 * <p>
 * ブラウザが送信するファイルタイプは、一部を除いて 'application/octet-stream' になってしまう。
 * Apache Tika を利用してファイル名およびファイルのマジックバイトを確認、正確なMIME Type（メディアタイプ）を判別する。
 * </p>
 *
 * @author SEKIGUCHI Naoya
 */
public class TikaFileTypeDetector implements FileTypeDetector, ServiceInitListener<Service> {
	/** ロガー */
	private Logger logger = LoggerFactory.getLogger(TikaFileTypeDetector.class);
	/** FileUploadTikaAdapter */
	private FileUploadTikaAdapter tikaAdapter;

	@Override
	public void inited(Service service, Config config) {
	}

	@Override
	public void destroyed() {
	}

	@Override
	public String detect(File file, String fileName, String type) {
		try (InputStream in = new FileInputStream(file)) {
			return detect(in, fileName, type);
		} catch (IOException e) {
			logger.warn("Unable to retrieve media type.", e);
			return type;
		}
	}

	@Override
	public String detect(InputStream input, String fileName, String type) {
		try {
			return tikaAdapter.detect(input, fileName);
		} catch (IOException e) {
			logger.warn("Unable to retrieve media type.", e);
			return type;
		}
	}

	/**
	 * FileUploadTikaAdapter を設定する
	 *
	 * @param tikaAdapter FileUploadTikaAdapter
	 */
	public void setFileUploadTikaAdapter(FileUploadTikaAdapter tikaAdapter) {
		this.tikaAdapter = tikaAdapter;
	}
}
