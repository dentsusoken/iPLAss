/*
 * Copyright (C) 2023 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
import java.io.InputStream;

/**
 * ファイルタイプ（MIME Type・メディアタイプ）検出機能インターフェース
 *
 * @author SEKIGUCHI Naoya
 */
public interface FileTypeDetector {
	/**
	 * ファイルからMIME Type（メディアタイプ）を検出する。
	 *
	 * @param file 対象ファイル
	 * @param fileName アップロード時のファイル名
	 * @param type ブラウザが送信したファイルタイプ
	 * @return ファイルから検出したメディアタイプ
	 */
	String detect(File file, String fileName, String type);

	/**
	 * InputStream からMIME Type（メディアタイプ）を検出する。
	 *
	 *
	 * @param input 対象ファイル InputStream
	 * @param fileName アップロード時のファイル名
	 * @param type ブラウザが送信したファイルタイプ
	 * @return ファイルから検出したメディアタイプ
	 */
	String detect(InputStream input, String fileName, String type);
}
