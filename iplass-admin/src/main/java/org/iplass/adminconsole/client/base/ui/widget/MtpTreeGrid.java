/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeNode;

/**
 * ツリーグリッドのベースクラス。
 *
 * Chromeで画面倍率によってアイコンが表示されない不具合に対応。
 *
 */
public class MtpTreeGrid extends TreeGrid {

	public MtpTreeGrid() {
		this(false);
	}

	public MtpTreeGrid(boolean multiSelect) {
		if (multiSelect) {
			//SelectionAppearance.CHECKBOX対応
			setBaseStyle("mtpMultiSelectTreeGridRow");
		} else {
			setBaseStyle("mtpTreeGridRow");
		}

		//Chromeで画面zoom設定によってアイコンが表示されないので大きくする
		//実際のアイコンサイズはCSSで調整
		setIconSize(18); //16->18
	}

	/**
	 * <p>フォルダを選択し、表示します。</p>
	 *
	 * @param node 対象TreeNode
	 */
	public void selectAndScrollNode(TreeNode node) {

		//フォルダがOpenされていないと選択できないのでOpen
		openParentFolder(node);

		//選択
		selectSingleRecord(node);

		//選択Nodeを表示するためスクロール
		scrollToRow(getRecordIndex(node));
	}

	/**
	 * <p>フォルダをOpenします。</p>
	 *
	 * <p>親のフォルダがOpenされていない場合は、再帰的にOpenします。</p>
	 *
	 * @param node 対象TreeNode
	 */
	private void openParentFolder(TreeNode node) {

		//parentのOpen
		TreeNode parent = getTree().getParent(node);
		if (!getTree().isOpen(parent)) {
			openParentFolder(parent);
		}
		//自身のOpen
		if (getTree().isFolder(node) &&  !getTree().isOpen(node)) {
			getTree().openFolder(node);
		}
	}
}
