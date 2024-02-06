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

import org.iplass.adminconsole.client.base.event.ViewEntityDataEvent.ViewEntityDataHandler;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

/**
 * Entityデータの表示をリクエストするイベントです。
 */
public class ViewEntityDataEvent extends GwtEvent<ViewEntityDataHandler> {

	/**
	 * Entityのデータ表示を実現するクラスで実装します。
	 */
	public interface ViewEntityDataHandler extends EventHandler {

		/**
		 * Entityデータを表示します。
		 *
		 * @param event 対象Entityイベント
		 */
		void onViewEntityData(ViewEntityDataEvent event);
	}

	public static final Type<ViewEntityDataHandler> TYPE = new Type<>();

	/** 対象Entity定義名 */
	private final String entityName;

	public ViewEntityDataEvent(String entityName) {
		this.entityName = entityName;
	}

	/**
	 * 対象のソースに対して、イベントを発行します。
	 *
	 * @param entityName 対象Entity定義名
	 * @param source 対象ソース
	 */
	public static void fire(String entityName, HasHandlers source) {
		source.fireEvent(new ViewEntityDataEvent(entityName));
	}

	/**
	 * 対象のEntity定義名を返します。
	 *
	 * @return Entity定義名
	 */
	public String getEntityName() {
		return entityName;
	}

	@Override
	public Type<ViewEntityDataHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ViewEntityDataHandler handler) {
		handler.onViewEntityData(this);
	}

}
