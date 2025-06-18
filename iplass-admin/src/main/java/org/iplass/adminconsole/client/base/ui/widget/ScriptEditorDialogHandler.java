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

/**
 * スクリプトエディターダイアログハンドラー
 * <p>
 * エディターによる操作完了・キャンセル用イベントハンドラーです。
 * </p>
 */
public interface ScriptEditorDialogHandler {
	/**
	 * 編集完了時イベントです
	 * @param text 編集されたテキスト
	 */
	void onSave(String text);

	/**
	 * 編集キャンセル時のイベントです
	 */
	void onCancel();

	/**
	 * 編集完了時イベントです
	 * <p>
	 * 本メソッドでは、編集完了時のダイアログ設定を取得することができます。
	 * </p>
	 * @param setting ダイアログ設定
	 */
	default void onSaveDialogSetting(ScriptEditorDialogSetting setting) {
		// NOTE onSaveDialogSetting は後から追加した機能なので onSave メソッドと分けているが、本来は同時が望ましい。
		// 必要に応じて実装する。
	}
}
