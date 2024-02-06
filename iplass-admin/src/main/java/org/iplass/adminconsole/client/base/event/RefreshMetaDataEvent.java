/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.base.event;

import org.iplass.adminconsole.client.base.event.RefreshMetaDataEvent.RefreshMetaDataHandler;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

/**
 * メタデータの表示をリフレッシュするイベントです。
 */
public class RefreshMetaDataEvent extends GwtEvent<RefreshMetaDataHandler> {

	/**
	 * メタデータのデータ表示リフレッシュを実現するクラスで実装します。
	 */
	public interface RefreshMetaDataHandler extends EventHandler {

		/**
		 * メタデータデータ表示をリフレッシュします。
		 *
		 * @param event 対象イベント
		 */
		void onRefresh(RefreshMetaDataEvent event);
	}

	public static final Type<RefreshMetaDataHandler> TYPE = new Type<>();

	public RefreshMetaDataEvent() {
	}

	/**
	 * 対象のソースに対して、イベントを発行します。
	 *
	 * @param source 対象ソース
	 */
	public static void fire(HasHandlers source) {
		source.fireEvent(new RefreshMetaDataEvent());
	}

	@Override
	public Type<RefreshMetaDataHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(RefreshMetaDataHandler handler) {
		handler.onRefresh(this);
	}

}
