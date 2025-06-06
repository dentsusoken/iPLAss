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

package org.iplass.adminconsole.client.base.ui.widget;

import org.iplass.gwt.ace.client.EditorMode;

public enum ScriptEditorDialogMode {
	/** CSS */
	CSS(EditorMode.CSS, "Css"),
	/** Groovy Script */
	GROOVY_SCRIPT(EditorMode.GROOVY, "Groovy Script"),
	/** HTML */
	HTML(EditorMode.HTML, "Html"),
	/** Java */
	JAVA(EditorMode.JAVA, "Java"),
	/** JavaScript */
	JAVASCRIPT(EditorMode.JAVASCRIPT, "JavaScript"),
	/** JSON */
	JSON(EditorMode.JSON, "JSON"),
	/** Jsp */
	JSP(EditorMode.JSP, "Jsp"),
	/** Sql */
	SQL(EditorMode.SQL, "Sql"),
	/** Text */
	TEXT(EditorMode.TEXT, "Text"),
	/** TypeScript */
	TYPESCRIPT(EditorMode.TYPESCRIPT, "TypeScript"),
	/** Vue.js */
	VUE(EditorMode.VUE, "Vue.js"),
	/** Xml */
	XML(EditorMode.XML, "Xml"),
	/** YAML */
	YAML(EditorMode.YAML, "YAML");

	private final EditorMode aceMode;
	private final String text;

	private ScriptEditorDialogMode(EditorMode aceMode, String text) {
		this.aceMode = aceMode;
		this.text = text;
	}

	public EditorMode getAceMode() {
		return aceMode;
	}

	public String getText() {
		return text;
	}

	public static ScriptEditorDialogMode getMode(String mode) {
		try {
			return ScriptEditorDialogMode.valueOf(mode.toUpperCase());
		} catch (Exception e) {
			return ScriptEditorDialogMode.JAVA;
		}
	}

}
