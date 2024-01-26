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

package org.iplass.adminconsole.client.metadata.ui.top;

import org.iplass.adminconsole.client.base.event.MTPEvent;

/**
 * パーツ管理用ハンドラ
 * @author lis3wg
 */
public interface PartsOperationHandler {

	/**
	 * チェック処理。
	 * @param event
	 * @return
	 */
	boolean check(MTPEvent event);

	/**
	 * 追加処理。
	 * @param event
	 */
	void add(MTPEvent event);

	/**
	 * 削除処理。
	 * @param event
	 */
	void remove(MTPEvent event);
}
