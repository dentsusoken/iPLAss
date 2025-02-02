/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.shared.tools.dto.logexplorer;

import java.io.Serializable;

/**
 * ログファイル情報
 */
public class LogFile implements Serializable {

	private static final long serialVersionUID = -7013254428082272889L;

	/** パス */
	private String path;

	/** ログファイル名(表示用) */
	private String fileName;

	/** 最終更新日時(フォーマット済み) */
	private String lastModified;

	/** ファイルサイズ */
	private long size;

	/**
	 * パスを返します。
	 *
	 * @return パス
	 */
	public String getPath() {
		return path;
	}

	/**
	 * パスを設定します。
	 * @param path パス
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * ファイル名を返します。
	 *
	 * @return ファイル名
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * ファイル名を設定します。
	 *
	 * @param fileName ファイル名
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * 最終更新日時を返します。
	 *
	 * @return 最終更新日時
	 */
	public String getLastModified() {
		return lastModified;
	}

	/**
	 * 最終更新日時を設定します。
	 *
	 * @param lastModified 最終更新日時
	 */
	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	/**
	 * ファイルサイズを返します。
	 *
	 * @return ファイルサイズ
	 */
	public long getSize() {
		return size;
	}

	/**
	 * ファイルサイズを設定します。
	 *
	 * @param size ファイルサイズ
	 */
	public void setSize(long size) {
		this.size = size;
	}

}
