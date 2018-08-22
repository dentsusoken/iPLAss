/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.gem.command.calendar.ref;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.view.filter.EntityFilterItem;




/**
 * カレンダーエンティティフィルター情報
 * @author lis7zi
 */
public class CalendarFilterData {

	/** エンティティ定義 */
	private EntityDefinition entityDefinition;
	
	/** エンティティフィルター情報 */
	private List<EntityFilterItem> entityFilterIteList;
	
	/** 検索画面表示用データ情報 */
	private List<CalendarFilterPropertyItem> calendarFilterPropertyItemList;

	public EntityDefinition getEntityDefinition() {
		return entityDefinition;
	}

	public void setEntityDefinition(EntityDefinition entityDefinition) {
		this.entityDefinition = entityDefinition;
	}

	public List<EntityFilterItem> getEntityFilterIteList() {
		if (entityFilterIteList == null) {
			return new ArrayList<>();
		}
		return entityFilterIteList;
	}

	public void setEntityFilterIteList(List<EntityFilterItem> entityFilterIteList) {
		this.entityFilterIteList = entityFilterIteList;
	}

	public List<CalendarFilterPropertyItem> getCalendarFilterPropertyItemList() {
		return calendarFilterPropertyItemList;
	}

	public void setCalendarFilterPropertyItemList(
			List<CalendarFilterPropertyItem> calendarFilterPropertyItemList) {
		this.calendarFilterPropertyItemList = calendarFilterPropertyItemList;
	}
}
