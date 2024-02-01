/*
 * Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.
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

import org.iplass.gem.command.Constants;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.CommandConfig;
import org.iplass.mtp.command.annotation.action.ActionMapping;
import org.iplass.mtp.command.annotation.action.Result;
import org.iplass.mtp.command.annotation.action.Result.Type;

@ActionMapping(
		name=CalendarFilterCommand.ACTION_NANE,
				displayName="カレンダーフィルター",
		command={@CommandConfig(commandClass=CalendarFilterCommand.class)},
		result={
			@Result(type=Type.JSP,
					value=Constants.CMD_RSLT_JSP_CALENDAR_FILTER,
					templateName="gem/calendar/ref/calendarFilter",
					layoutActionName=Constants.LAYOUT_POPOUT_ACTION)
		})
@CommandClass(name="gem/calendar/ref/CalendarFilterCommand", displayName="カレンダーフィルター")
public final class CalendarFilterCommand implements Command {

	public  static final String ACTION_NANE = "gem/calendar/ref/calendarFilter";

	@Override
	public String execute(RequestContext request) {
		return Constants.CMD_EXEC_SUCCESS;
	}

}
