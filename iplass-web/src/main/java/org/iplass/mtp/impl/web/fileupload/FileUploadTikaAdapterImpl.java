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
import java.util.Set;

import org.apache.tika.Tika;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.spi.ServiceConfigrationException;
import org.iplass.mtp.spi.ServiceInitListener;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 * FileUpload機能で利用する Tika 機能アダプター実装クラス
 *
 * @author SEKIGUCHI Naoya
 */
public class FileUploadTikaAdapterImpl implements FileUploadTikaAdapter, ServiceInitListener<Service> {
	/** ロガー */
	private static Logger LOG = LoggerFactory.getLogger(FileUploadTikaAdapterImpl.class);
	/** tika設定ファイルへのリソースパス */
	private String tikaConfigXml;
	/** tika 設定 */
	private TikaConfig tikaConfig;
	/** tika 本体 */
	private Tika tika;

	@Override
	public void inited(Service service, Config config) {
		// Tika 設定
		this.tikaConfig = getTikaConfig(tikaConfigXml);
		// Tika インスタンス
		this.tika = new Tika(tikaConfig);
	}

	@Override
	public void destroyed() {
		tika = null;
		tikaConfig = null;
	}

	/**
	 * tika設定ファイルへのリソースパス
	 *
	 * <p>
	 * 公式サイトでは tila-config.xml と記載されていす。
	 * </p>
	 *
	 * @param tikaConfigXml tika設定ファイルへのリソースパス
	 */
	public void setTikaConfigXml(String tikaConfigXml) {
		this.tikaConfigXml = tikaConfigXml;
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

	@Override
	public TikaMimeType getParentMimeType(TikaMimeType type) {
		MediaType mediaType = getMediaTypeInner(type);

		// NOTE getSupertype() の仕様の注意点
		// getSupertype() の戻り値として、親が見つからない場合 "application/octet-stream" 以外の場合は "application/octet-stream" が返却される。
		// 実際には関連のない MimeType となることがある。
		MediaType superMediaType = tikaConfig.getMediaTypeRegistry().getSupertype(mediaType);
		return null == superMediaType ? null : getMimeType(superMediaType.toString());
	}

	@Override
	public boolean hasChild(TikaMimeType parentType, TikaMimeType childType) {
		MediaType parentMediaType = getMediaTypeInner(parentType);
		MediaType childMediaType = getMediaTypeInner(childType);

		// superMediaType の子として mediaType が定義されているか確認する。
		Set<MediaType> childs = tikaConfig.getMediaTypeRegistry().getChildTypes(parentMediaType);
		return childs.contains(childMediaType);
	}

	/**
	 * TikaMimeType インターフェースから、apache tika の MimeType を取得する
	 * @param type TikaMimeType インターフェース実装クラス
	 * @return apache tika MimeType
	 */
	private MimeType getMimeTypeInner(TikaMimeType type) {
		if (type instanceof TikaMimeTypeImpl) {
			// TikaMimeTypeImpl であれば、保持インスタンスの MediaType を取得
			return ((TikaMimeTypeImpl) type).mimeType;
		}

		TikaMimeType t = getMimeType(type.getName());
		return null == t ? null : ((TikaMimeTypeImpl) t).mimeType;
	}

	/**
	 * TikaMimeType インターフェースから、apache tika の MediaType を取得する
	 * @param type TikaMimeType インターフェース実装クラス
	 * @return apache tika MediaType
	 */
	private MediaType getMediaTypeInner(TikaMimeType type) {
		MimeType mimeType = getMimeTypeInner(type);
		return null == mimeType ? null : mimeType.getType();
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
			URL resource = FileUploadTikaAdapterImpl.class.getResource(tikaConfigXml);
			try {
				return new TikaConfig(resource);
			} catch (TikaException | IOException | SAXException e) {
				throw new ServiceConfigrationException(e);
			}
		} else {
			return TikaConfig.getDefaultConfig();
		}
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
}
