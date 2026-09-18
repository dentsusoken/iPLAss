/*
 * Copyright (C) 2026 DENTSU SOKEN INC. All Rights Reserved.
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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package org.iplass.mtp.impl.report.converter;

import org.iplass.mtp.spi.Service;

/**
 * Office ドキュメント→PDF 等のドキュメント変換を行う独立 Service のインタフェース。
 */
public interface PdfConversionService extends Service {

	/**
	 * 入力バイナリを別のドキュメント形式へ変換する
	 *
	 * @param input    入力バイナリ（非 null）
	 * @param fileName 入力ファイル名メタデータ（拡張子で入力形式を判定。例: "report.xlsx"）
	 * @param context  変換コンテキスト（出力タイプ・パスワード等のメタ情報）
	 * @return 変換結果バイナリ
	 * @throws DocumentConversionException 変換失敗時
	 */
	byte[] convert(byte[] input, String fileName, ConvertContext context);
}
