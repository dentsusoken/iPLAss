/*
 * Copyright (C) 2020 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.view.generic;

/**
 * 汎用詳細編集画面の表示制御Handler
 *
 */
public interface DetailFormViewHandler {

	/**
	 * 詳細画面を表示します。
	 *
	 * @param event 詳細画面表示イベント
	 */
	default public void onShowDetailView(FormViewEvent event) {
	}

	/**
	 * 編集画面を表示します。
	 *
	 * @param event 編集画面表示イベント
	 */
	default public void onShowEditView(FormViewEvent event) {
	}

}
