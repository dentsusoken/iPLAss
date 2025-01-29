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
package org.iplass.adminconsole.shared.tools.dto.logexplorer;

import java.io.Serializable;

/**
 * ログファイル情報検索条件
 */
public class LogFileCondition implements Serializable {

	private static final long serialVersionUID = 2459207754956721755L;

	/** Limit件数 */
	private int limit;

	/** ログファイル名(表示用)条件（正規表現） */
	private String fileName;

	/** 最終更新日時条件（正規表現） */
	private String lastModified;

	/**
	 * Limit件数を返します。
	 *
	 * @return Limit件数
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * Limit件数を設定します。
	 *
	 * @param limit Limit件数
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * ログファイル名(表示用)条件（正規表現）を返します。
	 *
	 * @return ログファイル名(表示用)条件（正規表現）
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * ログファイル名(表示用)条件（正規表現）を設定します。
	 *
	 * @param fileName ログファイル名(表示用)条件（正規表現）
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * 最終更新日時条件（正規表現）を返します。
	 *
	 * @return 最終更新日時条件（正規表現）
	 */
	public String getLastModified() {
		return lastModified;
	}

	/**
	 * 最終更新日時条件（正規表現）を設定します。
	 *
	 * @param lastModified 最終更新日時条件（正規表現）
	 */
	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

}
