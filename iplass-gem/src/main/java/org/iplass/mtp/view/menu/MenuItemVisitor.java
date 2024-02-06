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

package org.iplass.mtp.view.menu;

/**
 * メニューツリー・メニュー（アイテム）に対する処理定義
 *
 * @param <R> 各処理結果で戻す型
 */
public interface MenuItemVisitor<R> {

	/**
	 * メニューツリーに対する処理
	 *
	 * @param menuTree 対象メニューツリー
	 * @return 処理結果
	 */
	public R visit(MenuTree menuTree);

	/**
	 * 子階層のメニューアイテムが存在しないメニューツリーに対する処理
	 *
	 * @param menuTree 対象メニューツリー
	 * @return 処理結果
	 */
	public R visitNoMenu(MenuTree menuTree);

	/**
	 * 階層フォルダ用メニューアイテムに対する処理
	 *
	 * @param nodeMenuItem 階層フォルダ用メニューアイテム
	 * @return 処理結果
	 */
	public R visit(NodeMenuItem nodeMenuItem) ;

	/**
	 * Action実行用メニューアイテムに対する処理
	 *
	 * @param actionMenuItem Action実行用メニューアイテム
	 * @return 処理結果
	 */
	public R visit(ActionMenuItem actionMenuItem);

	/**
	 * Entity表示用メニューアイテムに対する処理
	 *
	 * @param entityMenuItem Entity表示用メニューアイテム
	 * @return 処理結果
	 */
	public R visit(EntityMenuItem entityMenuItem);

	/**
	 * UR義用メニューアイテムに対する処理
	 *
	 * @param urlMenuItem URL用メニューアイテム
	 * @return 処理結果
	 */
	public R visit(UrlMenuItem urlMenuItem);
}
