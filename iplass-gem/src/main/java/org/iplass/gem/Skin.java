/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
 * <p>画面のレイアウトに関するスキン定義</p>
 *
 * <p>
 * ページ全体とメニューに対するスキン名を保持する。
 * </p>
 */
public class Skin implements Serializable {

	private static final long serialVersionUID = 8073158426960478478L;

	/** ポップアップメニュースキン名 */
	public static final String SUB_MENU_POPUP = "sub-popup";
	/** ドロップリストメニュースキン名 */
	public static final String SUB_MENU_DROPLIST = "sub-droplist";

	/** スキン名 */
	private String skinName;

	/** スキン表示名 */
	private String displayName;

	/** ページスキン名 */
	private String pageSkinName;

	/** メニュースキン名 */
	private String menuSkinName;

	/**
	 * スキン名を返します。
	 *
	 * @return スキン名
	 */
	public String getSkinName() {
		return skinName;
	}

	/**
	 * スキン名を設定します。
	 * @param skinName スキン名
	 */
	public void setSkinName(String skinName) {
		this.skinName = skinName;
	}

	/**
	 * スキン表示名を返します。
	 * テナント設定の選択値として表示されます。
	 *
	 * @return スキン表示名
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * スキン表示名を設定します。
	 * @param displayName スキン表示名
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * ページ全体用のスキン名を返します。
	 *
	 * @return ページスキン名
	 */
	public String getPageSkinName() {
		if (pageSkinName != null) {
			return pageSkinName;
		} else {
			//未指定の場合はスキン名を返す
			return skinName;
		}
	}

	/**
	 * ページスキン名を設定します。
	 * @param pageSkinName ページスキン名
	 */
	public void setPageSkinName(String pageSkinName) {
		this.pageSkinName = pageSkinName;
	}

	/**
	 * メニュー用のスキン名を返します。
	 * @return メニュースキン名
	 */
	public String getMenuSkinName() {
		if (menuSkinName != null) {
			return menuSkinName;
		} else {
			//未指定の場合は、POPUPを返す
			return SUB_MENU_POPUP;
		}
	}

	/**
	 * メニュースキン名を設定します。
	 * @param menuSkinName メニュースキン名
	 */
	public void setMenuSkinName(String menuSkinName) {
		this.menuSkinName = menuSkinName;
	}

}
