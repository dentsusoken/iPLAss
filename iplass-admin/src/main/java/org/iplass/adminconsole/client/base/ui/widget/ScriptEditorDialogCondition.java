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

import org.iplass.gwt.ace.client.EditorTheme;

public class ScriptEditorDialogCondition implements ScriptEditorDialogConstants {

	private ScriptEditorDialogMode initEditorMode = ScriptEditorDialogMode.GROOVY_SCRIPT;

	private EditorTheme initEditorTheme;

	private String value;

	private boolean readOnly;

	/**
	 * 対象PropertyKEY
	 * <p>
	 * 現状はDialogのタイトルにのみ利用。
	 * </p>
	 */
	private String propertyKey;

	/**
	 * HintメッセージKEY
	 * <p>
	 * adminのpropertyファイルに定義されているメッセージKEY。
	 * ただしhintMessageが指定されている場合、hintMessageを優先する。
	 * </p>
	 */
	private String hintKey;

	/** Hintメッセージ */
	private String hintMessage;

	public ScriptEditorDialogCondition() {
	}

	public ScriptEditorDialogMode getInitEditorMode() {
		return initEditorMode;
	}

	public void setInitEditorMode(ScriptEditorDialogMode initEditorMode) {
		this.initEditorMode = initEditorMode;
	}

	public EditorTheme getInitEditorTheme() {
		return initEditorTheme;
	}

	public void setInitEditorTheme(EditorTheme initEditorTheme) {
		this.initEditorTheme = initEditorTheme;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public String getPropertyKey() {
		return propertyKey;
	}

	public void setPropertyKey(String propertyKey) {
		this.propertyKey = propertyKey;
	}

	public String getHintKey() {
		return hintKey;
	}

	public void setHintKey(String hintKey) {
		this.hintKey = hintKey;
	}

	public String getHintMessage() {
		return hintMessage;
	}

	public void setHintMessage(String hintMessage) {
		this.hintMessage = hintMessage;
	}

}
