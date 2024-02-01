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

package org.iplass.adminconsole.client.metadata.ui.menu.item.event;

import java.io.Serializable;
import java.util.Map;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;


/**
 * <p>汎用データ変更イベントです。</p>
 * 
 * <p>
 * イベント内部にMapを持ちます。
 * 引き渡したい情報を格納してください。
 * </p>
 * 
 * <p>
 * 複数のコンポーネントに分割した際に、変更を伝える手段として利用してください。
 * </p>
 */
public class MenuItemDataChangedEvent extends DataChangedEvent {

	public enum Type {
		ADD,
		UPDATE,
		DELETE
	}
	
	/** イベントタイプ */
	private Type type;
	
	/**
	 * コンストラクタ
	 */
	public MenuItemDataChangedEvent(Type type) {
		super();
		setType(type);
	}
	
	/**
	 * コンストラクタ
	 * 
	 * @param valueMap 値を保持するマップ
	 */
	public MenuItemDataChangedEvent(Type type, Map<String, Serializable> valueMap) {
		super(valueMap);
		setType(type);
	}
	
	/**
	 * イベントのタイプを設定します。
	 * 
	 * @param type イベントタイプ
	 */
	public void setType(Type type) {
		this.type = type;
	}
	
	/**
	 * イベントのタイプを返します。
	 * 
	 * @return イベントタイプ
	 */
	public Type getType() {
		return type;
	}

}
