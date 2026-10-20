/*
 * Copyright (C) 2026 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.gem.command.generic.editor;

import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor;

/**
 * 参照プロパティエディタの属性を出力するためのユーティリティクラスです。
 */
public final class ReferencePropertyEditorUtil {

	private ReferencePropertyEditorUtil() {
	}

	/**
	 * 参照プロパティエディタに設定されたダイアログサイズのdata属性を生成します。
	 *
	 * @param editor 参照プロパティエディタ
	 * @return ダイアログサイズのdata属性。未設定の場合は空文字列
	 */
	public static String getDialogSizeAttributes(ReferencePropertyEditor editor) {
		return getDialogSizeAttributes(editor.getDialogWidth(), editor.getDialogHeight(), editor.isDialogMaximize());
	}

	/**
	 * ダイアログサイズのdata属性を生成します。
	 *
	 * @param dialogWidth    ダイアログの幅（ピクセル）
	 * @param dialogHeight   ダイアログの高さ（ピクセル）
	 * @param dialogMaximize ダイアログを最大化するかどうか
	 * @return ダイアログサイズのdata属性。設定がない場合は空文字列
	 */
	public static String getDialogSizeAttributes(Integer dialogWidth, Integer dialogHeight, boolean dialogMaximize) {
		StringBuilder attributes = new StringBuilder();
		if (dialogWidth != null) {
			attributes.append(" data-dialog-width=\"")
					.append(dialogWidth)
					.append('"');
		}
		if (dialogHeight != null) {
			attributes.append(" data-dialog-height=\"")
					.append(dialogHeight)
					.append('"');
		}
		if (dialogMaximize) {
			attributes.append(" data-dialog-maximize=\"true\"");
		}
		return attributes.toString();
	}
}
