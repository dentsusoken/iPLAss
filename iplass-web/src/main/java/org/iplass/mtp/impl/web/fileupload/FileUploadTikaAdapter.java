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
import java.util.List;

/**
 * FileUpload機能で利用する Tika 機能アダプターインターフェース
 *
 * <p>
 * apache tika への依存を本クラスで解決することを目的としたインターフェース。
 * apache tika のバージョンアップ時は本インターフェースの実装クラスだけを修正することを想定している。
 * </p>
 *
 * <p>
 * アップロードファイルの検査の流れとして FileTypeDetector で抽出した MediaType(MimeType) を、MagicByteChecker で利用する。
 * Tika機能を利用する場合、TikaFileTypeDetector で抽出可能な MediaType(MimeType) と、TikaMagicByteChecker で検査可能なファイル種別を同一にするため、
 * 同一の Tika インスタンスを利用する必要がある。そのため、本インターフェース実装クラスのインスタンスを共有（bean定義）して利用する。
 * </p>
 *
 * @author SEKIGUCHI Naoya
 */
public interface FileUploadTikaAdapter {
	/**
	 * ファイルから MimeType（メディアタイプ）を検出する
	 * @param in ファイル InputStream
	 * @param name ファイル名
	 * @return 検出された MimeType（メディアタイプ）
	 * @throws IOException ファイル入出力例外
	 */
	String detect(InputStream in, String name) throws IOException;

	/**
	 * MimeType（メディアタイプ）文字列より MimeType インスタンスを取得する
	 * @param type MimeType（メディアタイプ）文字列
	 * @return MimeType インスタンス
	 */
	TikaMimeType getMimeType(String type);

	/**
	 * MimeType の親として定義されている MimeType を取得する
	 * @param type 対象 MimeType
	 * @return 親 MimeType
	 */
	TikaMimeType getParentMimeType(TikaMimeType type);

	/**
	 * parentType の子として childType が定義されているか確認する
	 *
	 * <p>
	 * ユースケース
	 * "application/octet-stream" の MimeType の場合に、は全ての親として返却されるパターンがあるので、定義として存在しているか確認する。
	 * </p>
	 *
	 * @param parentType 親として定義されているMimeType（汎化した定義）
	 * @param childType 子として定義されているMimeType（特化した定義）
	 * @return superType の子として childType が定義されている場合 true を返却
	 */
	boolean hasChild(TikaMimeType parentType, TikaMimeType childType);

	/**
	 * Tika MimeType インターフェース
	 */
	public interface TikaMimeType {
		/**
		 * MimeType 名を取得する
		 * @return MimeType 名
		 */
		String getName();

		/**
		 * MimeType に定義されている拡張子リストを取得する
		 * @return MimeType に定義されている拡張子リスト
		 */
		List<String> getExtensions();

		/**
		 * MimeType に magic bytes の定義が存在するか確認する。
		 * @return MimeType に magic bytes の定義が存在する場合 true
		 */
		boolean hasMagic();

		/**
		 * MimeType の magic bytes の定義に一致するか確認する
		 * @param magic チェック対象ファイルの magic byte
		 * @return 一致する場合 true
		 */
		boolean matchesMagic(byte[] magic);
	}
}
