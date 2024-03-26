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
package org.iplass.adminconsole.server.base.io.upload;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * マルチパートパラメータ
 *
 * <p>
 * マルチパートリクエストのパラメータ情報を格納する。
 * </p>
 *
 * <p>
 * パラメータパターン
 * <ul>
 * <li>フォーム入力値</li>
 * <li>ファイル</li>
 * </ul>
 * </p>
 *
 * @author SEKIGUCHI Naoya
 */
public interface MultipartRequestParameter {
	/**
	 * 本パラメータがフォーム入力値か判定する
	 * @return フォーム入力値の判定結果（フォーム値の場合 true）
	 */
	boolean isFormField();

	/**
	 * フィールド名を取得する
	 *
	 * <p>
	 * input 要素の name 属性の値
	 * </p>
	 *
	 * @return フィールド名
	 */
	String getFieldName();

	/**
	 * ContentType を取得する
	 * @return ContentType
	 */
	String getContentType();

	/**
	 * パラメータサイズを取得する
	 * @return パラメータサイズ
	 */
	long getSize();

	/**
	 * パラメータ値のInputStream取得する
	 * @return パラメータ値のInputStream取得する
	 * @throws IOException 入出力例外
	 */
	InputStream getInputStream() throws IOException;

	/**
	 * パラメータ文字列を取得する
	 *
	 * <p>
	 * パラメータがフォーム入力値の場合に利用することを想定。
	 * デフォルト文字コードを利用して文字列を作成する。
	 * </p>
	 *
	 * @return 文字列
	 */
	String getString();

	/**
	 * パラメータ文字列を取得する
	 *
	 * <p>
	 * パラメータがフォーム入力値の場合に利用することを想定。
	 * </p>
	 *
	 * @param charset 文字コード
	 * @return パラメータ文字列
	 */
	String getString(Charset charset);

	/**
	 * ファイル名を取得する
	 *
	 * <p>
	 * パラメータがファイルの場合に利用することを想定。
	 * <p>
	 *
	 * @return ファイル名
	 */
	String getName();

	/**
	 * パラメータリソースを破棄する
	 */
	void dispose();
}
