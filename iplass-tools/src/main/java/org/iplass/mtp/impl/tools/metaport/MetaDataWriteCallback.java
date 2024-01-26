/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.tools.metaport;

/**
 * MetaDataのExport処理用Callback定義
 */
public interface MetaDataWriteCallback {

	/**
	 * Exportが開始されました。
	 */
	void onStarted();

	/**
	 * Pathに指定されたMetaDataがExportされました。
	 * @param path 出力MetaData
	 */
	void onWrited(String path, String version);

	/**
	 * Exportが終了しました。
	 */
	void onFinished();

	/**
	 * Warningが発生しました。
	 *
	 * @param path 対象MetaData
	 * @param message Warningメッセージ
	 * @return 処理を継続する場合、true
	 */
	boolean onWarning(String path, String message, String version);

	/**
	 * Errorが発生しました。
	 *
	 * @param path 対象MetaData
	 * @param message Errorメッセージ
	 * @return 処理を継続する場合、true
	 */
	boolean onErrored(String path, String message, String version);

}
