/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.gem.command.calendar;

import org.iplass.gem.command.Constants;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.action.ActionMapping;
import org.iplass.mtp.command.annotation.action.ParamMapping;
import org.iplass.mtp.command.annotation.action.Result;
import org.iplass.mtp.command.annotation.action.Result.Type;
import org.iplass.mtp.view.calendar.EntityCalendar;
import org.iplass.mtp.view.calendar.EntityCalendarManager;

@ActionMapping(
	name=CalendarCommand.ACTION_NANE,
	displayName="カレンダー表示",
	paramMapping=@ParamMapping(name="calendarType", mapFrom="${0}"),
	result=@Result(type=Type.JSP,
			value=Constants.CMD_RSLT_JSP_CALENDAR,
			templateName="gem/calendar/calendarView",
			layoutActionName=Constants.LAYOUT_NORMAL_ACTION)
)
@CommandClass(name="gem/calendar/CalendarCommand", displayName="カレンダー表示")
public final class CalendarCommand implements Command {

	public static final String ACTION_NANE = "gem/calendar/calendar";

	private EntityCalendarManager cm;

	public CalendarCommand() {
		cm = ManagerLocator.getInstance().getManager(EntityCalendarManager.class);
	}

	@Override
	public String execute(RequestContext request) {
		String calendarName = request.getParam("calendarName");
		String calendarType = request.getParam("calendarType");

		if (calendarType == null) {
			EntityCalendar ec = cm.get(calendarName);
			if (ec != null) calendarType = ec.getType().name().toLowerCase();
		}
		request.setAttribute("calendarType", calendarType);
		return null;
	}

}
