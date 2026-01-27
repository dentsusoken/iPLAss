/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui;

import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialog;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogCondition;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogHandler;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogMode;
import org.iplass.adminconsole.shared.metadata.dto.MetaDataConstants;

public final class MetaDataUtil {

	private MetaDataUtil() {
	}

	public static final String getMetaTypeIcon(boolean isShared, boolean isSharedOverwrite, boolean isOverwritable) {
		if (isShared) {
			if (!isOverwritable) {
				return MetaDataConstants.NODE_ICON_LOCKED_ITEM;
			}
			return MetaDataConstants.NODE_ICON_SHARED_ITEM;
		} else if (isSharedOverwrite) {
			return MetaDataConstants.NODE_ICON_SHARED_OVERWRITE_ITEM;
		}

		return MetaDataConstants.NODE_ICON_LOCAL_ITEM;
	}

	public static final String getMetaRepositoryTypeName(boolean isShared, boolean isSharedOverwrite) {
		if (isShared) {
			return "Shared";
		} else if (isSharedOverwrite) {
				return "Shared Overwrite";
		} else {
			return "Local";
		}
	}

	public static final String getMetaTypeName(boolean isShared, boolean isSharedOverwrite, boolean isOverwritable) {
		String typeName = null;
		if (isShared) {
			typeName = "Shared";
			if (!isOverwritable) {
				typeName += " , Not Overwrite";
			}
		} else if (isSharedOverwrite){
			typeName = "Tenant Local (Shared Overwrite)";
		} else {
			typeName = "Tenant Local";
		}
		return typeName;
	}

	/**
	 * スクリプト編集ダイアログを表示します。
	 * @param mode スクリプトエディタのモード
	 * @param text 編集するテキスト
	 * @param title 編集対象プロパティ名（ダイアログタイトル）
	 * @param hintKey ヒントKEY。adminのpropertyファイルに定義されているメッセージKEY。
	 *                 ただしhintMessageが指定されている場合、hintMessageを優先する。
	 * @param hintMessage Hintメッセージ（直接指定）
	 * @param handler 編集完了時のコールバック処理
	 */
	public static final void showScriptEditDialog(ScriptEditorDialogMode mode, String text,
			String title, String hintKey, String hintMessage, final ScriptEditorDialogHandler handler) {
		ScriptEditorDialogCondition cond = new ScriptEditorDialogCondition();
		cond.setInitEditorMode(mode);
		cond.setValue(text);
		cond.setPropertyKey(title);
		cond.setHintKey(hintKey);
		cond.setHintMessage(hintMessage);

		ScriptEditorDialog window = new ScriptEditorDialog(cond, handler);
		window.show();
	}

	/**
	 * スクリプト編集ダイアログを表示します。（オーバーロード）
	 * @param cond
	 * @param handler
	 */
	public static final void showScriptEditDialog(final ScriptEditorDialogCondition cond,
			final ScriptEditorDialogHandler handler) {
		ScriptEditorDialog window = new ScriptEditorDialog(cond, handler);
		window.show();
	}
}
