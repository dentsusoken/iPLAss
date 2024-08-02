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

public enum EditorMode {

	/** CSS. */
	CSS("css", "Css"),
	/** Groovy. */
	GROOVY("groovy", "Groovy"),
	/** HTML. */
	HTML("html", "Html"),
	/** JAVA. */
	JAVA("java", "Java"),
	/** Javascript. */
	JAVASCRIPT("javascript", "JavaScript"),
	/** JSP. */
	JSP("jsp", "Jsp"),
	/** SQL. */
	SQL("sql", "Sql"),
	/** Text. */
	TEXT("text", "Text"),
	/** Vue.js. */
	VUEJS("vue", "Vue.js"),
	/** TypeScript. */
	TYPESCRIPT("typescript", "TypeScript"),
	/** XML. */
	XML("xml", "Xml")
	;

	private final String modeName;
	private final String text;

	private EditorMode(String modeName, String text) {
		this.modeName = modeName;
		this.text = text;
	}

	/**
	 * @return mode name (e.g., "java" for Java mode)
	 */
	public String getModeName() {
		return modeName;
	}

	/**
	 * @return mode display text
	 */
	public String getText() {
		return text;
	}

	public static EditorMode modeNameOf(String modeName) {
		for (EditorMode mode : values()) {
			if (mode.getModeName().equals(modeName.toLowerCase())) {
				return mode;
			}
		}
		return null;
	}

}
