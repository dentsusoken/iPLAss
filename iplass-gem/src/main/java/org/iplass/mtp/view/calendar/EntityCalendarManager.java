/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.view.calendar;

import org.iplass.mtp.definition.DefinitionModifyResult;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.entity.Entity;

/**
 * カレンダー定義を管理するクラスのインターフェース
 * @author lis3wg
 */
public interface EntityCalendarManager extends TypedDefinitionManager<EntityCalendar> {

	/**
	 * 指定のCalendar定義を取得します。
	 * @param name カレンダーの定義名
	 * @return Calendar定義
	 * @deprecated {@link #get(String)} を使用してください。
	 */
	@Deprecated
	public EntityCalendar getCalendarByName(String name);

	/**
	 * Calendar定義を新規に作成します。
	 * @param calendar カレンダー定義
	 * @deprecated {@link #create(EntityCalendar)} を使用してください。
	 */
	@Deprecated
	public DefinitionModifyResult createCalendar(EntityCalendar calendar);

	/**
	 * Calendar定義を更新します。
	 * @param calendar カレンダー定義
	 * @deprecated {@link #update(EntityCalendar)} を使用してください。
	 */
	@Deprecated
	public DefinitionModifyResult updateCalendar(EntityCalendar calendar);

	/**
	 * Calendar定義を削除します。
	 * @param name カレンダーの定義名
	 * @deprecated {@link #remove(String)} を使用してください。
	 */
	@Deprecated
	public DefinitionModifyResult removeCalendar(String name);

	/**
	 * Calendar定義のGroovyScriptによるカラー設定を取得します。
	 * @return GroovyScriptによるカラー設定
	 */
	public String getColorConfigResult(String name, Entity entity);
}
