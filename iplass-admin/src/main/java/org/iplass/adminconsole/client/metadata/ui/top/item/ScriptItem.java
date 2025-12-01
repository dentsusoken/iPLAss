/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.top.item;

import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogCondition;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogHandler;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogMode;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogSetting;
import org.iplass.adminconsole.client.metadata.ui.MetaDataUtil;
import org.iplass.mtp.view.top.parts.ScriptParts;

/**
 *
 * @author lis3wg
 */
public class ScriptItem extends PartsItem {

	private ScriptParts parts;

	/**
	 * コンストラクタ
	 */
	public ScriptItem(ScriptParts parts) {
		this.parts = parts;
		setTitle("Script");
		setBackgroundColor("#F9F9F9");
	}

	@Override
	public ScriptParts getParts() {
		return parts;
	}

	@Override
	protected void onOpen() {
		
		    // ScriptParts 内の値（script / maxHeight）をダイアログに引き継ぐために Condition を明示的に生成する
		    ScriptEditorDialogCondition cond = new ScriptEditorDialogCondition();
		    cond.setInitEditorMode(ScriptEditorDialogMode.JSP);                // エディタモード（JSP）を初期設定
		    cond.setValue(this.parts.getScript());                             // ScriptParts に保存済みのスクリプトを設定
		    cond.setPropertyKey(ScriptEditorDialogCondition.TOPVIEW_SCRIPT_ITEM); // ダイアログタイトル用のキー
		    cond.setHintKey("ui_metadata_top_item_ScriptItem_scriptHint");     // ヒントメッセージのキー
		    cond.setHintMessage(null);                                         // 直接メッセージは未指定

			// ScriptParts に保存されている maxHeight をダイアログへ受け渡す
		    cond.setMaxHeight(this.parts.getMaxHeight());

		    // ScriptEditorDialogCondition を直接渡す新しい呼び出し形式
		    MetaDataUtil.showScriptEditDialog(cond, new ScriptEditorDialogHandler() {

				@Override
				public void onSaveDialogSetting(ScriptEditorDialogSetting dialogSetting) {
					// maxHeight を取得する
					Integer maxHeight = dialogSetting.getMaxHeight();

					// ScriptParts に保存する
					ScriptItem.this.parts.setMaxHeight(maxHeight);
				}

				@Override
				public void onSave(String text) {
					ScriptItem.this.parts.setScript(text);
				}
				@Override
				public void onCancel() {
				}
			});
		}

}
