/*
 * Copyright (C) 2018 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.gwt.ace.client;

public enum EditorTheme {

	AMBIANCE("ambiance", "Ambiance"),
	COBALT("cobalt", "Cobalt"),
	CHROME("chrome", "Chrome"),
	ECLIPSE("eclipse", "Eclipse"),
	GOB("gob", "Green on Black"),
	SOLARIZED_DARK("solarized_dark", "Solarized Dark"),
	TEXTMATE("textmate", "Textmate"),
	TERMINAL("terminal", "Terminal"),
	XCODE("xcode", "XCode")
	;

	private final String themeName;
	private final String text;

	private EditorTheme(String themeName, String text) {
		this.themeName = themeName;
		this.text = text;
	}

	/**
	 * @return the theme name (e.g., "eclipse")
	 */
	public String getThemeName() {
		return themeName;
	}

	/**
	 * @return the theme display text
	 */
	public String getText() {
		return text;
	}

	public static EditorTheme themeNameOf(String themeName) {
		for (EditorTheme theme : values()) {
			if (theme.getThemeName().equals(themeName.toLowerCase())) {
				return theme;
			}
		}
		return null;
	}

}
