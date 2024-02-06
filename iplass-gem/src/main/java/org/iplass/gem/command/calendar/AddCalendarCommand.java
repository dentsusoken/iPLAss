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

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.iplass.gem.command.Constants;
import org.iplass.gem.command.generic.detail.DetailFormViewData;
import org.iplass.gem.command.generic.detail.DetailViewCommand;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.action.ActionMapping;
import org.iplass.mtp.command.annotation.action.ParamMapping;
import org.iplass.mtp.command.annotation.action.Result;
import org.iplass.mtp.command.annotation.action.Result.Type;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.GenericEntity;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.DateProperty;
import org.iplass.mtp.entity.definition.properties.DateTimeProperty;
import org.iplass.mtp.entity.definition.properties.TimeProperty;
import org.iplass.mtp.util.DateUtil;
import org.iplass.mtp.view.calendar.EntityCalendar;
import org.iplass.mtp.view.calendar.EntityCalendarItem;
import org.iplass.mtp.view.calendar.EntityCalendarItem.CalendarSearchType;
import org.iplass.mtp.view.calendar.EntityCalendarManager;
import org.iplass.mtp.web.template.TemplateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * カレンダーデータ追加コマンド
 * @author lis3wg
 */
@ActionMapping(
	name=AddCalendarCommand.ACTION_NANE,
	displayName="追加",
	paramMapping=@ParamMapping(name=Constants.VIEW_NAME, mapFrom="${0}"),
	result={
		@Result(status=Constants.CMD_EXEC_SUCCESS, type=Type.TEMPLATE, value=Constants.TEMPLATE_REF_EDIT),
		@Result(status=Constants.CMD_EXEC_ERROR_VIEW, type=Type.TEMPLATE, value=Constants.TEMPLATE_COMMON_ERROR,
				layoutActionName=Constants.LAYOUT_POPOUT_ACTION),
		@Result(status=Constants.CMD_EXEC_ERROR_NODATA, type=Type.TEMPLATE, value=Constants.TEMPLATE_COMMON_ERROR,
				layoutActionName=Constants.LAYOUT_POPOUT_ACTION)
	}
)
@CommandClass(name="gem/calendar/AddCalendarCommand", displayName="追加")
public final class AddCalendarCommand implements Command {

	private static Logger logger = LoggerFactory.getLogger(AddCalendarCommand.class);

	public  static final String ACTION_NANE = "gem/calendar/add";

	/** EntityDefinitionManager */
	private EntityDefinitionManager edm = null;

	/** EntityCalendarManager */
	private EntityCalendarManager ecm = null;

	/**
	 * コンストラクタ
	 */
	public AddCalendarCommand() {
		edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
		ecm = ManagerLocator.getInstance().getManager(EntityCalendarManager.class);
	}

	@Override
	public String execute(RequestContext request) {
		DetailViewCommand command = new DetailViewCommand();
		command.setDetail(true);
		String ret = command.execute(request);
		if (!Constants.CMD_EXEC_SUCCESS.equals(ret)) return ret;

		Long date = dateToLong(request.getParam(Constants.CALENDAR_DATE));
		String defName = request.getParam(Constants.DEF_NAME);
		String calendarName = request.getParam(Constants.CALENDAR_NAME);

		if (date != null) {
			EntityCalendar ec = ecm.get(calendarName);
			EntityCalendarItem item = ec.getItem(defName);
			EntityDefinition ed = edm.get(defName);
			Entity entity = null;
			if (ed != null && item != null) {
				entity = newEntity(ed);
				if (item.getCalendarSearchType() == CalendarSearchType.DATE) {
					setValue(ed, item.getPropertyName(), entity, date);
				} else if (item.getCalendarSearchType() == CalendarSearchType.PERIOD) {
					setValue(ed, item.getFromPropertyName(), entity, date);
					setValue(ed, item.getToPropertyName(), entity, date);
				}
			}

			if (entity != null) {
				DetailFormViewData data = (DetailFormViewData) request.getAttribute(Constants.DATA);
				data.setEntity(entity);
			}
		}

		return Constants.CMD_EXEC_SUCCESS;
	}

	/**
	 * Entityに変換した日付の値を設定します。
	 * @param ed Entity定義
	 * @param propName プロパティ名
	 * @param entity Entity
	 * @param date 日付
	 */
	private void setValue(EntityDefinition ed, String propName, Entity entity, Long date) {
		if (propName != null) {
			PropertyDefinition pd = ed.getProperty(propName);
			entity.setValue(propName, newValue(pd, date));
		}
	}

	/**
	 * プロパティ定義に合わせて日付を変換します。
	 * @param pd プロパティ定義
	 * @param date 日付
	 * @return 変換した日付
	 */
	private java.util.Date newValue(PropertyDefinition pd, Long date) {
		java.util.Date d = null;
		if (pd != null) {
			if (pd instanceof DateProperty) {
				d = new Date(date);
			} else if (pd instanceof DateTimeProperty) {
				d = new Timestamp(date);
			} else if (pd instanceof TimeProperty) {
				//TODO Time型必要？
				d = new Timestamp(date);
			}
		}
		return d;
	}

	/**
	 * 日付をLong値に変換します。
	 * @param str 日付
	 * @return Long値
	 */
	private Long dateToLong(String str) {
		SimpleDateFormat format = DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getServerDateFormat(), false);
		format.setLenient(false);
		try {
			return format.parse(str).getTime();
		} catch (ParseException e) {
			if (logger.isDebugEnabled()) {
				logger.debug(e.getMessage(), e);
			}

			return null;
		}
	}

	/**
	 * Entity定義に合わせたEntityのインスタンスを生成します。
	 * @param entityDefinition Entity定義
	 * @return Entityインスタンス
	 */
	private Entity newEntity(EntityDefinition entityDefinition) {
		Entity res = null;
		if (entityDefinition.getMapping() != null && entityDefinition.getMapping().getMappingModelClass() != null) {
			try {
				res = (Entity) Class.forName(entityDefinition.getMapping().getMappingModelClass()).newInstance();
			} catch (InstantiationException e) {
				throw new EntityRuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new EntityRuntimeException(e);
			} catch (ClassNotFoundException e) {
				throw new EntityRuntimeException(e);
			}
		} else {
			res = new GenericEntity();
		}
		res.setDefinitionName(entityDefinition.getName());
		return res;
	}
}
