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

package org.iplass.mtp.impl.i18n;

import java.util.List;

/**
 * 言語に合わせたフォント設定
 *
 * @author lis3wg
 *
 */
public class LanguageFonts {

	/** 言語 */
	private String language;

	/** フォント */
	private List<String> fonts;

	/**
	 * 言語を取得します。
	 * @return 言語
	 */
	public String getLanguage() {
	    return language;
	}

	/**
	 * 言語を設定します。
	 * @param language 言語
	 */
	public void setLanguage(String language) {
	    this.language = language;
	}

	/**
	 * フォントを取得します。
	 * @return フォント
	 */
	public List<String> getFonts() {
	    return fonts;
	}

	/**
	 * フォントを設定します。
	 * @param fonts フォント
	 */
	public void setFonts(List<String> fonts) {
	    this.fonts = fonts;
	}

}
