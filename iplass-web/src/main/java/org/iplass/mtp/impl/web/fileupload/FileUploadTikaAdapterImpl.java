/*
 * Copyright (C) 2024 DENTSU SOKEN INC. All Rights Reserved.
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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.tika.Tika;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.iplass.mtp.spi.ObjectBuilder;
import org.iplass.mtp.spi.ServiceConfigrationException;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 * FileUpload機能で利用する Tika 機能アダプター実装クラス
 *
 * <p>
 * FileUpload機能で利用する Tika インスタンスを共有する。
 * </p>
 *
 * @author SEKIGUCHI Naoya
 */
public class FileUploadTikaAdapterImpl implements FileUploadTikaAdapter {
	/** ロガー */
	private static Logger LOG = LoggerFactory.getLogger(FileUploadTikaAdapterImpl.class);
	/** tika 設定 */
	private TikaConfig tikaConfig;
	/** tika 本体 */
	private Tika tika;

	/**
	 * コンストラクタ
	 * @param tikaConfig tika 設定
	 * @param tika tika 本体
	 */
	public FileUploadTikaAdapterImpl(TikaConfig tikaConfig, Tika tika) {
		this.tikaConfig = tikaConfig;
		this.tika = tika;
	}

	@Override
	public String detect(InputStream in, String name) throws IOException {
		return tika.detect(in, name);
	}

	@Override
	public TikaMimeType getMimeType(String type) {
		try {
			MimeType registered = tikaConfig.getMimeRepository().getRegisteredMimeType(type);
			if (null != registered) {
				return new TikaMimeTypeImpl(registered);
			}

			// このポイントに来たら、type に対応する MimeType の定義が存在しない。
			LOG.warn("MimeType does not exist. (type = {})", type);

		} catch (MimeTypeException e) {
			// MimeType 取得時に例外発生
			LOG.warn("An exception occurred when acquiring MimeType. (type = {})", type, e);
		}

		return null;
	}

	/**
	 * Tike MimeType 実装クラス
	 *
	 * <p>
	 * apache tika への依存を解消するクラス
	 * </p>
	 */
	public static class TikaMimeTypeImpl implements TikaMimeType {
		/** apache tika の MimeType インスタンス */
		private MimeType mimeType;

		/**
		 * コンストラクタ
		 * @param mimeType MimeType インスタンス
		 */
		public TikaMimeTypeImpl(MimeType mimeType) {
			this.mimeType = mimeType;
		}

		@Override
		public String getName() {
			return mimeType.getName();
		}

		@Override
		public List<String> getExtensions() {
			return mimeType.getExtensions();
		}

		@Override
		public boolean hasMagic() {
			return mimeType.hasMagic();
		}

		@Override
		public boolean matchesMagic(byte[] magic) {
			return mimeType.matchesMagic(magic);
		}
	}

	/**
	 * FileUploadTikaAdapterImpl ビルダークラス
	 */
	public static class Builder implements ObjectBuilder<FileUploadTikaAdapter> {
		/**
		 * tika-config.xml のシステムリソース位置
		 *
		 * @see https://tika.apache.org/
		 * @see https://github.com/apache/any23/blob/any23-1.1/mime/src/main/resources/org/apache/any23/mime/tika-config.xml
		 */
		private String tikaConfigXml;

		@Override
		public void setProperties(Map<String, Object> properties) {
			tikaConfigXml = (String) properties.get("tikaConfigXml");
		}

		@Override
		public FileUploadTikaAdapter build() {
			// Tika 設定
			TikaConfig tikaConfig = getTikaConfig(tikaConfigXml);
			// Tika インスタンス
			Tika tika = new Tika(tikaConfig);
			return new FileUploadTikaAdapterImpl(tikaConfig, tika);
		}

		/**
		 * TikaConfig を取得する
		 *
		 * <p>
		 * tikaConfigXml の指定が無い場合は、デフォルト設定とる。
		 * tikaConfigXml の指定があり、ファイルが存在しない場合は例外がスローされる。
		 * </p>
		 *
		 * @param tikaConfigXml tikaConfigXml のリソースパス
		 * @return TikaConfig インスタンス
		 */
		private TikaConfig getTikaConfig(String tikaConfigXml) {
			if (StringUtil.isNotBlank(tikaConfigXml)) {
				// ログ
				URL resource = this.getClass().getClassLoader().getResource(tikaConfigXml);
				try {
					return new TikaConfig(resource);
				} catch (TikaException | IOException | SAXException e) {
					throw new ServiceConfigrationException(e);
				}
			} else {
				return TikaConfig.getDefaultConfig();
			}
		}

	}
}
