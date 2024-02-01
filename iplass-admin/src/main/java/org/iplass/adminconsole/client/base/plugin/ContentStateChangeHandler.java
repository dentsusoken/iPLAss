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

/**
 * <p>Workspaceのコンテンツ変更イベントハンドラです。</p>
 *
 * <p>Workspaceに追加するPane(Canvas)でこのハンドラを実装すると
 * タブの状態が変更された際に呼び出されます。</p>
 */
public interface ContentStateChangeHandler {

	/**
	 * ContentClosedイベントです。
	 *
	 * @param event {@link ContentClosedEvent}
	 */
	void onContentClosed(ContentClosedEvent event);

	/**
	 * ContentSelectedイベントです。
	 *
	 * @param event {@link ContentSelectedEvent}
	 */
	void onContentSelected(ContentSelectedEvent event);

}
