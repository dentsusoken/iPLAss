/*
 * Copyright (C) 2015 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.gem;

import java.io.Serializable;

/**
 * <p>画面のレイアウトに関するテーマ定義</p>
 *
 * <p>
 * ページ全体に対するテーマ名を保持する。
 * </p>
 */
public class Theme implements Serializable {

	private static final long serialVersionUID = 3877206355537600448L;

	/** テーマ名 */
	private String themeName;

	/** テーマ表示名 */
	private String displayName;

	/**
	 * テーマ名を取得します。
	 * @return テーマ名
	 */
	public String getThemeName() {
		return themeName;
	}

	/**
	 * テーマ名を設定します。
	 * @param themeName テーマ名
	 */
	public void setThemeName(String themeName) {
		this.themeName = themeName;
	}

	/**
	 * テーマ表示名を返します。
	 * テナント設定の選択値として表示されます。
	 *
	 * @return テーマ表示名
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * テーマ表示名を設定します。
	 * @param displayName テーマ表示名
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}
