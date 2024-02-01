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

package org.iplass.gem.command.calendar;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.iplass.gem.command.Constants;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.template.Template;
import org.iplass.mtp.command.annotation.webapi.RestJson;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.DateProperty;
import org.iplass.mtp.entity.definition.properties.DateTimeProperty;
import org.iplass.mtp.entity.definition.properties.TimeProperty;
import org.iplass.mtp.entity.query.PreparedQuery;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.SortSpec;
import org.iplass.mtp.entity.query.SortSpec.SortType;
import org.iplass.mtp.entity.query.Where;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.expr.And;
import org.iplass.mtp.entity.query.condition.predicate.Between;
import org.iplass.mtp.util.DateUtil;
import org.iplass.mtp.view.calendar.EntityCalendar;
import org.iplass.mtp.view.calendar.EntityCalendarItem;
import org.iplass.mtp.view.calendar.EntityCalendarItem.CalendarSearchType;
import org.iplass.mtp.view.calendar.EntityCalendarManager;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.RequestType;

/**
 * カレンダーウィジェットデータ取得コマンド
 * @author lis7zi
 */
@WebApi(
	name=GetCalendarWidgetCommand.WEBAPI_NAME,
	accepts=RequestType.REST_JSON,
	methods=MethodType.POST,
	restJson=@RestJson(parameterName="params"),
	results="calendarWidgetList",
	checkXRequestedWithHeader=true
)
@Template(name="gem/calendar/calendarWidget", displayName="カレンダーウィジェット", path="/jsp/gem/calendar/calendarWidget.jsp")
@CommandClass(name="gem/calendar/GetCalendarWidgetCommand", displayName="カレンダーウィジェット")
public final class GetCalendarWidgetCommand implements Command {

	public static final String WEBAPI_NAME = "gem/calendar/getCalendarWidget";

	/** EntityCalendarManager */
	private EntityCalendarManager ecm = null;

	/** EntityDefinitionManager */
	private EntityDefinitionManager edm = null;

	/** EntityManager */
	private EntityManager em = null;

	/**
	 * コンストラクタ
	 */
	public GetCalendarWidgetCommand() {
		ecm = ManagerLocator.getInstance().getManager(EntityCalendarManager.class);
		edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
		em = ManagerLocator.getInstance().getManager(EntityManager.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String execute(RequestContext request) {
		Map<String, String> json = (Map<String, String>)request.getAttribute("params");
		String calendarName = json.get(Constants.CALENDAR_NAME);
		java.util.Date targetDate = stringToTime(json.get(Constants.CALENDAR_TARGET_DATE));

		Set<String> linkList = new HashSet<>();

		EntityCalendar cd = ecm.get(calendarName);
		if (cd != null) {
			//Entity毎にデータを検索
			for (EntityCalendarItem item : cd.getItems()) {
				searchData(linkList, item, targetDate);
			}
		}

		request.setAttribute("calendarWidgetList", linkList);
		return Constants.CMD_EXEC_SUCCESS;
	}

	/**
	 * 対象月のデータを検索します。
	 * @param calendarWidjetList データ格納先
	 * @param item カレンダーに表示するEntityの設定
	 * @param targetDate 対象日
	 */
	private void searchData(Set<String> linkList, EntityCalendarItem item, java.util.Date targetDate) {
		String defName = item.getDefinitionName();
		String propertyName = CalendarSearchType.DATE.equals(item.getCalendarSearchType())
								? item.getPropertyName() : item.getFromPropertyName();
		EntityDefinition ed = edm.get(defName);
		PropertyDefinition pd = ed.getProperty(propertyName);

		targetDate = targetDate != null ? targetDate : new java.util.Date();


		Calendar fromCal = Calendar.getInstance();
		fromCal.setTime(targetDate);
		fromCal.set(Calendar.DAY_OF_MONTH, 1);
		fromCal.set(Calendar.HOUR_OF_DAY, 0);
		fromCal.set(Calendar.MINUTE, 0);
		fromCal.set(Calendar.SECOND, 0);
		fromCal.set(Calendar.MILLISECOND, 0);

		Calendar toCal = Calendar.getInstance();
		toCal.setTime(targetDate);
		toCal.set(Calendar.HOUR_OF_DAY, 23);
		toCal.set(Calendar.MINUTE, 59);
		toCal.set(Calendar.SECOND, 59);
		toCal.set(Calendar.MILLISECOND, 999);

		Query q = new Query();
		q.select(Entity.OID, Entity.NAME, propertyName);
		q.from(defName);

		Where where = new Where();
		ArrayList<Condition> conditions = new ArrayList<Condition>();
		//期間条件
		if (pd instanceof DateProperty) {
			conditions.add(new Between(propertyName, new Date(fromCal.getTimeInMillis()), new Date(toCal.getTimeInMillis())));
		} else if (pd instanceof DateTimeProperty) {
			conditions.add(new Between(propertyName, new Timestamp(fromCal.getTimeInMillis()), new Timestamp(toCal.getTimeInMillis())));
		} else if (pd instanceof TimeProperty) {
			conditions.add(new Between(propertyName, new Time(fromCal.getTimeInMillis()), new Time(toCal.getTimeInMillis())));
		}
		//フィルタ条件（個別バインド変数未指定）
		if (item.getFilterCondition() != null && !item.getFilterCondition().isEmpty()) {
			conditions.add(new PreparedQuery(item.getFilterCondition()).condition(null));
		}
		where.setCondition(new And(conditions));
		q.setWhere(where);

		q.order(new SortSpec(Entity.OID, SortType.ASC), new SortSpec(propertyName, SortType.ASC));
		if (item.getLimit() != null) {
			q.limit(item.getLimit());
		} else {
			q.limit(1000);
		}
		List<Entity> list = em.searchEntity(q).getList();

		SimpleDateFormat format = DateUtil.getSimpleDateFormat("yyyyMMdd", false);
		for (Entity entity : list) {
			java.util.Date date = entity.getValue(propertyName);
			linkList.add(timeToString(date, format));
		}
	}

	/**
	 * 時間を文字列に変換します。
	 * @param date 時間
	 * @param format フォーマット
	 * @return 文字列
	 */
	private String timeToString(java.util.Date date, SimpleDateFormat format) {
		return format.format(date);
	}

	/**
	 * 文字列を時間に変換します。
	 * @param time 文字列
	 * @param format フォーマット
	 * @return 時間
	 */
	private java.util.Date stringToTime(String time) {
		SimpleDateFormat format = DateUtil.getSimpleDateFormat("yyyyMMdd", false);
		java.util.Date date = null;

		try {
			date = format.parse(time);
		} catch (ParseException e) {
			// ignore
		}

		return date;
	}


}
