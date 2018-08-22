/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import org.iplass.adminconsole.client.base.event.ViewMetaDataEvent.ViewMetaDataHandler;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

/**
 * メタデータの表示をリクエストするイベントです。
 */
public class ViewMetaDataEvent extends GwtEvent<ViewMetaDataHandler> {

	/**
	 * メタデータのデータ表示を実現するクラスで実装します。
	 */
	public interface ViewMetaDataHandler extends EventHandler {

		/**
		 * メタデータデータを表示します。
		 *
		 * @param event 対象イベント
		 */
		void onViewMetaData(ViewMetaDataEvent event);
	}

	public static final Type<ViewMetaDataHandler> TYPE = new Type<>();

	/** 対象メタデータ定義名 */
	private final String definitionName;
	/** 対象メタデータClass名 */
	private final String definitionClassName;

	public ViewMetaDataEvent(String definitionClassName, String definitionName) {
		this.definitionClassName = definitionClassName;
		this.definitionName = definitionName;
	}

	/**
	 * 対象のソースに対して、イベントを発行します。
	 *
	 * @param definitionClassName 対象メタデータクラス名
	 * @param definitionName 対象メタデータ定義名
	 * @param source 対象ソース
	 */
	public static void fire(String definitionClassName, String definitionName, HasHandlers source) {
		source.fireEvent(new ViewMetaDataEvent(definitionClassName, definitionName));
	}

	/**
	 * 対象のメタデータClass名を返します。
	 *
	 * @return メタデータClass名
	 */
	public String getDefinitionClassName() {
		return definitionClassName;
	}

	/**
	 * 対象のメタデータ定義名を返します。
	 *
	 * @return メタデータ定義名
	 */
	public String getDefinitionName() {
		return definitionName;
	}

	@Override
	public Type<ViewMetaDataHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ViewMetaDataHandler handler) {
		handler.onViewMetaData(this);
	}

}
