/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
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

/**
 * エディターダイアログ設定
 *
 * @author SEKIGUCHI Naoya
 */
public class ScriptEditorDialogSetting {
	/** ダイアログモード */
	private ScriptEditorDialogMode mode;
	/** エディターテーマ */
	private EditorTheme theme;

	/**
	 * ダイアログモードを設定します。
	 * @param mode ダイアログモード
	 */
	public void setMode(ScriptEditorDialogMode mode) {
		this.mode = mode;
	}

	/**
	 * ダイアログモードを取得します。
	 * @return ダイアログモード
	 */
	public ScriptEditorDialogMode getMode() {
		return mode;
	}

	/**
	 * エディターテーマを設定します。
	 * @param theme エディターテーマ
	 */
	public void setTheme(EditorTheme theme) {
		this.theme = theme;
	}

	/**
	 * エディターテーマを取得します。
	 * @return エディターテーマ
	 */
	public EditorTheme getTheme() {
		return theme;
	}
}
