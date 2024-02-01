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

package org.iplass.adminconsole.client.base.plugin;

import org.iplass.adminconsole.client.base.ui.layout.AdminMenuTreeNode;
import org.iplass.adminconsole.client.base.ui.layout.MainWorkspaceTab;
import org.iplass.adminconsole.client.base.ui.widget.MtpTreeGrid;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.tree.Tree;


/**
 * <p>各プラグインごとのコントローラクラスです。</p>
 *
 * <p>今後プラグインが増えることを想定し、メイン構造と各プラグイン(ツールなど)との依存をこのクラスで分離します。</p>
 *
 * <p>プラグインを追加する際は、プラグインごとにこのManagerクラスを実装してください。</p>
 *
 */
public interface AdminPlugin extends ContentStateChangeHandler {

	/** ノードタイプ接尾語(Root) */
	public static final String NODE_TYPE_ROOT_SUFFIX = "Root";
	/** ノードタイプ接尾語(Folder) */
	public static final String NODE_TYPE_FOLDER_SUFFIX = "Folder";
	/** ノードタイプ接尾語(Item) */
	public static final String NODE_TYPE_ITEM_SUFFIX = "Item";

	/**
	 * <p>起動元となるオーナをセットします。</p>
	 *
	 * <p>TreeGridによりこのプラグインがインスタンス化される際に呼び出されます。</p>
	 *
	 * @param owner オーナ
	 */
	void setOwner(Canvas owner);

	/**
	 * <p>表示先ツリーをセットします。</p>
	 *
	 * <p>TreeGridによりこのプラグインがインスタンス化される際に呼び出されます。</p>
	 *
	 * @param tree ツリー（モデル）
	 */
	void setTree(Tree tree);

	/**
	 * <p>表示先ツリーGridをセットします。</p>
	 *
	 * <p>TreeGridによりこのプラグインがインスタンス化される際に呼び出されます。</p>
	 *
	 * @param treeGrid ツリーグリッド
	 */
	void setTreeGrid(MtpTreeGrid treeGrid);

	/**
	 * <p>プラグインを表示するワークスペースをセットします。</p>
	 *
	 * <p>TreeGridによりこのプラグインがインスタンス化される際に呼び出されます。</p>
	 *
	 * TODO MainPaneをシングルトン化したので不要
	 *
	 * @param workspace ワークスペース
	 */
	void setWorkSpace(MainWorkspaceTab workspace);

	/**
	 * <p>プラグインとしてTreeGridに表示するTreeNodeを返します。</p>
	 *
	 * <p>プラグインごとにTreeGridに表示するTreeNodeのRootを返すよう実装してください。</p>
	 */
	AdminMenuTreeNode createPluginRootNode();

	/**
	 * <p>プラグインのカテゴリ名を返します。</p>
	 *
	 * <p>
	 * プラグインごとにTreeGridに表示するカテゴリ名を返すよう実装してください。
	 * カテゴリを階層化する場合は/(スラッシュ)で区切ってください。
	 * </p>
	 */
	String getCategoryName();

	/**
	 * <p>ノードがダブルクリックされた際の処理を実装します。</p>
	 *
	 * <p>
	 * 注意！：どのノードがダブルクリックされた場合にも呼び出されます。<br/>
	 * {@link AdminMenuTreeNode#getType()} か {@link AdminMenuTreeNode#getName()} などを利用して、
	 * プラグインの対象ノードかを判断してください。
	 * </p>
	 *
	 * @param node ダブルクリックノード
	 */
	void onNodeDoubleClick(AdminMenuTreeNode node);

	/**
	 * <p>ノードが右クリックされた際の処理を実装します。</p>
	 *
	 * <p>
	 * 注意！：どのノードが右クリックされた場合にも呼び出されます。<br/>
	 * {@link AdminMenuTreeNode#getType()} か {@link AdminMenuTreeNode#getName()} などを利用して、
	 * プラグインの対象ノードかを判断してください。
	 * </p>
	 *
	 * @param node 右クリックノード
	 */
	void onNodeContextClick(AdminMenuTreeNode node);

	/**
	 * <p>ノード(Folder)がOpenされた際の処理を実装します。</p>
	 *
	 * <p>
	 * 注意！：どのノードがOpenされた場合にも呼び出されます。<br/>
	 * {@link AdminMenuTreeNode#getType()} か {@link AdminMenuTreeNode#getName()} などを利用して、
	 * プラグインの対象ノードかを判断してください。
	 * </p>
	 *
	 * @param node Openノード
	 */
	void onFolderOpened(AdminMenuTreeNode node);

}
