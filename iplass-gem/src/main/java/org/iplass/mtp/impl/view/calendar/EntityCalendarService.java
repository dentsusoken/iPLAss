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

package org.iplass.mtp.impl.view.calendar;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.impl.definition.AbstractTypedMetaDataService;
import org.iplass.mtp.impl.definition.DefinitionMetaDataTypeMap;
import org.iplass.mtp.impl.definition.DefinitionNameChecker;
import org.iplass.mtp.impl.view.calendar.MetaCalendar.CalendarHandler;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.view.calendar.EntityCalendar;
import org.iplass.mtp.view.calendar.EntityCalendarManager;

/**
 * カレンダー定義を扱うサービス
 * @author lis3wg
 */
public class EntityCalendarService extends AbstractTypedMetaDataService<MetaCalendar, CalendarHandler> implements Service {

	/** メタデータのパス */
	public static final String META_PATH = "/view/calendar/";

	public static class TypeMap extends DefinitionMetaDataTypeMap<EntityCalendar, MetaCalendar> {
		public TypeMap() {
			super(getFixedPath(), MetaCalendar.class, EntityCalendar.class);
		}
		@Override
		public TypedDefinitionManager<EntityCalendar> typedDefinitionManager() {
			return ManagerLocator.getInstance().getManager(EntityCalendarManager.class);
		}

		@Override
		protected DefinitionNameChecker createDefinitionNameChecker() {
			return DefinitionNameChecker.getPathSlashDefinitionNameChecker();
		}
	}

	@Override
	public void destroy() {
	}

	@Override
	public void init(Config config) {
	}

	public static String getFixedPath() {
		return META_PATH;
	}

	@Override
	public Class<MetaCalendar> getMetaDataType() {
		return MetaCalendar.class;
	}

	@Override
	public Class<CalendarHandler> getRuntimeType() {
		return CalendarHandler.class;
	}
}
