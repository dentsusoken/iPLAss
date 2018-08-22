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

package org.iplass.mtp.impl.view.calendar;

import org.iplass.mtp.definition.DefinitionModifyResult;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.impl.definition.AbstractTypedDefinitionManager;
import org.iplass.mtp.impl.definition.TypedMetaDataService;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.impl.view.calendar.MetaCalendar.CalendarHandler;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.view.calendar.EntityCalendar;
import org.iplass.mtp.view.calendar.EntityCalendarManager;

/**
 * カレンダー定義を管理するクラス
 * @author lis3wg
 */
public class EntityCalendarManagerImpl extends AbstractTypedDefinitionManager<EntityCalendar> implements EntityCalendarManager {

	/** サービス */
	private EntityCalendarService service = ServiceRegistry.getRegistry().getService(EntityCalendarService.class);

	@Deprecated
	@Override
	public EntityCalendar getCalendarByName(String name) {
		return get(name);
	}

	@Deprecated
	@Override
	public DefinitionModifyResult createCalendar(EntityCalendar definition) {
		return create(definition);
	}

	@Deprecated
	@Override
	public DefinitionModifyResult updateCalendar(EntityCalendar definition) {
		return update(definition);
	}

	@Deprecated
	@Override
	public DefinitionModifyResult removeCalendar(String name) {
		return remove(name);
	}

	@Override
	public String getColorConfigResult(String name, Entity entity) {
		CalendarHandler handler = service.getRuntimeByName(name);
		if (handler == null) return null;
		return ((CalendarHandler) handler.getMetaData().createRuntime(null)).getColorConfigResult(entity);
	}

	@Override
	public Class<EntityCalendar> getDefinitionType() {
		return EntityCalendar.class;
	}

	@Override
	protected RootMetaData newInstance(EntityCalendar definition) {
		return new MetaCalendar();
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected TypedMetaDataService getService() {
		return service;
	}
}
