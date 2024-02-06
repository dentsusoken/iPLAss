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
import java.io.InputStream;

/**
 * デフォルト ファイルタイプ（MIME Type・メディアタイプ）検出機能
 *
 * <p>
 * 既存ロジック互換。
 * ブラウザが送信したファイルタイプをそのまま返却する。
 * </p>
 */
public class DefaultFileTypeDetector implements FileTypeDetector {

	@Override
	public String detect(File file, String fileName, String type) {
		return type;
	}

	@Override
	public String detect(InputStream input, String fileName, String type) {
		return type;
	}

}
