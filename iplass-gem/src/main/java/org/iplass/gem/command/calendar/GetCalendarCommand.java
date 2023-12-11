/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.text.StrTokenizer;
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
import org.iplass.mtp.entity.definition.PropertyDefinitionType;
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
import org.iplass.mtp.entity.query.condition.expr.Not;
import org.iplass.mtp.entity.query.condition.expr.Or;
import org.iplass.mtp.entity.query.condition.expr.Paren;
import org.iplass.mtp.entity.query.condition.predicate.Between;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.entity.query.condition.predicate.Greater;
import org.iplass.mtp.entity.query.condition.predicate.GreaterEqual;
import org.iplass.mtp.entity.query.condition.predicate.In;
import org.iplass.mtp.entity.query.condition.predicate.IsNotNull;
import org.iplass.mtp.entity.query.condition.predicate.IsNull;
import org.iplass.mtp.entity.query.condition.predicate.Lesser;
import org.iplass.mtp.entity.query.condition.predicate.LesserEqual;
import org.iplass.mtp.entity.query.condition.predicate.Like;
import org.iplass.mtp.entity.query.condition.predicate.NotEquals;
import org.iplass.mtp.impl.util.ConvertUtil;
import org.iplass.mtp.util.CollectionUtil;
import org.iplass.mtp.util.DateUtil;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.calendar.EntityCalendar;
import org.iplass.mtp.view.calendar.EntityCalendar.CalendarType;
import org.iplass.mtp.view.calendar.EntityCalendarItem;
import org.iplass.mtp.view.calendar.EntityCalendarItem.CalendarSearchType;
import org.iplass.mtp.view.calendar.EntityCalendarManager;
import org.iplass.mtp.view.filter.EntityFilter;
import org.iplass.mtp.view.filter.EntityFilterItem;
import org.iplass.mtp.view.filter.EntityFilterManager;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.RequestType;

/**
 * カレンダーデータ取得コマンド
 * @author lis3wg
 */
@WebApi(
	name=GetCalendarCommand.WEBAPI_NAME,
	accepts=RequestType.REST_JSON,
	methods=MethodType.POST,
	restJson=@RestJson(parameterName="params"),
	results="calendarData",
	checkXRequestedWithHeader=true
)
@Template(name="gem/calendar/calendarParts", displayName="カレンダー部品", path="/jsp/gem/calendar/calendarParts.jsp")
@CommandClass(name="gem/calendar/GetCalendarCommand", displayName="カレンダー")
public final class GetCalendarCommand implements Command {

	public static final String WEBAPI_NAME = "gem/calendar/getCalendar";

	/** EntityCalendarManager */
	private EntityCalendarManager ecm = null;

	/** EntityDefinitionManager */
	private EntityDefinitionManager edm = null;

	/** EntityFilterManager */
	private EntityFilterManager efm = null;

	/** EntityManager */
	private EntityManager em = null;

	/**
	 * フィルター項目の比較演算子。
	 */
	protected enum FilterCondition {
		/** = */
		EQ,
		/** != */
		NE,
		/** start with = like("a%") */
		SW,
		/** last with = like("%a") */
		LW,
		/** include = like("%a%") */
		IC,
		/** not include = not like("%a%") */
		NIC,
		/** in */
		IN,
		/** &lt; */
		LT,
		/** &gt; */
		GT,
		/** &lt;= */
		LE,
		/** &gt;= */
		GE,
		/** 範囲 */
		RG,
		/** 日付相対範囲 */
		RD,
		/** 日時相対範囲 */
		RDT,
		/** is not null*/
		NNL,
		/** is null */
		NL;

		private FilterCondition() {}
	}

	/**
	 * コンストラクタ
	 */
	public GetCalendarCommand() {
		ecm = ManagerLocator.getInstance().getManager(EntityCalendarManager.class);
		edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
		em = ManagerLocator.getInstance().getManager(EntityManager.class);
		efm = ManagerLocator.getInstance().getManager(EntityFilterManager.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String execute(RequestContext request) {
		Map<String, Object> json = (Map<String, Object>)request.getAttribute("params");
		String calendarName = (String) json.get(Constants.CALENDAR_NAME);
		String calendarType = (String) json.get(Constants.CALENDAR_TYPE);
		java.util.Date from = convDate((String) json.get(Constants.CALENDAR_FROM));
		java.util.Date to = convDate((String)json.get(Constants.CALENDAR_TO));

		List<CalendarData> calendarList = new ArrayList<>();

		EntityCalendar cd = ecm.get(calendarName);

		if (cd != null) {
			//Entity毎にデータを検索
			for (EntityCalendarItem item : cd.getItems()) {

				Map<String, Object> entityFilter = (Map<String, Object>) json.get(item.getDefinitionName());
				Boolean withoutEntityFlg = entityFilter != null
												? (Boolean) entityFilter.get(Constants.CALENDAR_WITHOUT_ENTITY) : null;
				if(withoutEntityFlg != null && withoutEntityFlg) {
					continue;
				}

				if (item.getCalendarSearchType() == CalendarSearchType.DATE) {
					searchDate(from, to, calendarList, item, calendarType, entityFilter, calendarName);
				} else if (item.getCalendarSearchType() == CalendarSearchType.PERIOD) {
					searchPeriod(from, to, calendarList, item, calendarType, entityFilter, calendarName);
				}
			}
		}

		request.setAttribute("calendarData", calendarList);
		return Constants.CMD_EXEC_SUCCESS;
	}

	/**
	 * 日付を元にデータを検索します。
	 * @param from 開始日
	 * @param to 終了日
	 * @param calendarList データ格納先
	 * @param item カレンダーに表示するEntityの設定
	 * @param type 期間タイプ
	 * @param entityFilter フィルター条件
	 */
	private void searchDate(java.util.Date from, java.util.Date to,
			List<CalendarData> calendarlist, EntityCalendarItem item, String type, Map<String, Object> entityFilter, String calendarName) {

		String defName = item.getDefinitionName();
		String propertyName = item.getPropertyName();
		EntityDefinition ed = edm.get(defName);
		PropertyDefinition pd = ed.getProperty(propertyName);

		Query q = new Query();
		q.select(Entity.OID, Entity.NAME, propertyName);
		q.from(defName);

		Where where = new Where();
		ArrayList<Condition> conditions = new ArrayList<Condition>();
		//期間条件
		boolean isDateProperty = false;
		if (pd instanceof DateProperty) {
			conditions.add(new Between(propertyName, new Date(from.getTime()), new Date(to.getTime())));
			isDateProperty = true;
		} else if (pd instanceof DateTimeProperty) {
			conditions.add(new Between(propertyName, new Timestamp(from.getTime()), new Timestamp(to.getTime())));
		} else if (pd instanceof TimeProperty) {
			conditions.add(new Between(propertyName, new Time(from.getTime()), new Time(to.getTime())));
		}

		setFilter(item, entityFilter, conditions);

		where.setCondition(new And(conditions));
		q.setWhere(where);

		q.order(new SortSpec(Entity.OID, SortType.ASC), new SortSpec(propertyName, SortType.ASC));
		if (item.getLimit() != null) {
			q.limit(item.getLimit());
		} else {
			q.limit(1000);
		}
		List<Entity> list = em.searchEntity(q).getList();

		SimpleDateFormat format = DateUtil.getSimpleDateFormat("HH:mm", false);
		for (Entity e : list) {

			java.util.Date date = e.getValue(propertyName);
			String time = isDateProperty ? null : timeToString(date, format);

			CalendarData calendarData = new CalendarData(type);
			if (item.getDisplayTime()) {
				calendarData.setTitle(createTitle(e.getName(),time));
			} else {
				calendarData.setTitle(e.getName());
			}
			calendarData.setStart(dateTimeToString(date));
			calendarData.setCalendarEntityData(new CalendarEntityData(e, time, item));
			calendarData.setColor(item.getEntityColor());
			if (StringUtil.isNotEmpty(item.getColorConfig())) {
				String resultColor = ecm.getColorConfigResult(calendarName, e);
				calendarData.setColor(resultColor);
			}

			calendarlist.add(calendarData);
		}
	}

	/**
	 * 期間を元にデータを検索します。
	 * @param from 開始日
	 * @param to 終了日
	 * @param calendarList データ格納先
	 * @param item カレンダーに表示するEntityの設定
	 * @param type 期間タイプ
	 * @param entityFilter フィルター条件
	 */
	private void searchPeriod(java.util.Date from, java.util.Date to,
			List<CalendarData> calendarList, EntityCalendarItem item, String type, Map<String, Object> entityFilter, String calendarName) {
		String defName = item.getDefinitionName();
		String fromPropertyName = item.getFromPropertyName();
		String toPropertyName = item.getToPropertyName();
		EntityDefinition ed = edm.get(defName);
		PropertyDefinition fromPd = ed.getProperty(fromPropertyName);
		PropertyDefinition toPd = ed.getProperty(toPropertyName);

		//終了日は23:59:59まで含めるので翌日00:00:00未満を条件にする
//		Calendar cal = Calendar.getInstance();
		Calendar cal = DateUtil.getCalendar(true);
		cal.setTime(to);
		cal.set(Calendar.HOUR, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		java.util.Date _to = cal.getTime();

		Query q = new Query();
		q.select(Entity.OID, Entity.NAME, fromPropertyName, toPropertyName);
		q.from(defName);

		Where where = new Where();
		ArrayList<Condition> conditions = new ArrayList<Condition>();

		//期間条件 fromProperty <= to + 1d && toProperty >= from
		boolean isDateProperty_from = false;
		if (fromPd instanceof DateProperty) {
			conditions.add(new Lesser(fromPropertyName, new Date(_to.getTime())));
			isDateProperty_from = true;
		} else if (fromPd instanceof DateTimeProperty) {
			conditions.add(new Lesser(fromPropertyName, new Timestamp(_to.getTime())));
		} else if (fromPd instanceof TimeProperty) {
			conditions.add(new Lesser(fromPropertyName, new Time(_to.getTime())));
		}
		boolean isDateProperty_to = false;
		if (toPd instanceof DateProperty) {
			conditions.add(new GreaterEqual(toPropertyName, new Date(from.getTime())));
			isDateProperty_to = true;
		} else if (toPd instanceof DateTimeProperty) {
			conditions.add(new GreaterEqual(toPropertyName, new Timestamp(from.getTime())));
		} else if (toPd instanceof TimeProperty) {
			conditions.add(new GreaterEqual(toPropertyName, new Time(from.getTime())));
		}


		setFilter(item, entityFilter, conditions);

		where.setCondition(new And(conditions));
		q.setWhere(where);

		q.order(new SortSpec(Entity.OID, SortType.ASC),
				new SortSpec(fromPropertyName, SortType.ASC),
				new SortSpec(toPropertyName, SortType.ASC));
		if (item.getLimit() != null) {
			q.limit(item.getLimit());
		} else {
			q.limit(1000);
		}
		List<Entity> list = em.searchEntity(q).getList();
		cal = DateUtil.getCalendar(true);

		SimpleDateFormat format = DateUtil.getSimpleDateFormat("HH:mm", false);
		for (Entity e : list) {

			java.util.Date toDate = e.getValue(toPropertyName);
			java.util.Date fromDate = e.getValue(fromPropertyName);
			String stoDate = dateTimeToString(fromDate);
			String toTime = isDateProperty_to ? null : timeToString(toDate, format);
			String sfromDate = fromDate != null ? dateTimeToString(toDate) : null;
			String fromTime = isDateProperty_from ? null : timeToString(fromDate, format);

			CalendarData calendarData = new CalendarData(type);

			calendarData.setTitle(e.getName());
			calendarData.setStart(stoDate);
			calendarData.setColor(item.getEntityColor());
			if (StringUtil.isNotEmpty(item.getColorConfig())) {
				String resultColor = ecm.getColorConfigResult(calendarName, e);
				calendarData.setColor(resultColor);
			}

			if (isAllDayEvent(fromDate, toDate)) {
				calendarData.setAllDay(true);
			}

			if (calendarData.isAllDay() || isMinMinutesLesser(fromDate, toDate, type)) {
				// alldayイベントでなく最低時間のイベントは終了時間を設定しない(fullcalendar側のイベント最小単位を適用させる為)
				calendarData.setEnd(sfromDate);
			}

			if(item.getDisplayTime()) {
				calendarData.setTime(createTime(fromTime, toTime));
			}

			calendarData.setCalendarEntityData(new CalendarEntityData(e, toTime, item));

			calendarList.add(calendarData);
		}
	}

	/**
	 * 文字列を日付に変換します。
	 * @param str 文字列
	 * @return 日付
	 */
	private java.util.Date convDate(String str) {
		try {
			SimpleDateFormat format = DateUtil.getSimpleDateFormat("yyyyMMdd", false);
			return new java.util.Date(format.parse(str).getTime());
		} catch (ParseException e) {
		}
		return null;
	}


	/**
	 * 日時を文字列に変換します。
	 * @param date 日時
	 * @return 文字列
	 */
	private String dateTimeToString(java.util.Date date) {
		SimpleDateFormat format = DateUtil.getSimpleDateFormat("yyyy-MM-dd HH:mm:ss", false);
		return format.format(date);
	}

	/**
	 * 時間を文字列に変換します。
	 * @param date 時間
	 * @return 文字列
	 */
	private String timeToString(java.util.Date date, SimpleDateFormat format) {
		return format.format(date);
	}

	/**
	 * カレンダーに表示するタイトルを作成します。
	 * @param name name属性の値
	 * @param time 時間
	 *
	 * @return 文字列
	 */
	private String createTitle(String name, String time) {
		StringBuilder sb = new StringBuilder();

		if (time != null) {
			sb.append(time)
			  .append("-");
		}

		sb.append(name);

		return sb.toString();
	}

	/**
	 * カレンダーに表示する時間を作成します。
	 * @param fromTime 開始時間
	 * @param toTime 終了時間
	 *
	 * @return 文字列
	 */
	private String createTime(String fromTime, String toTime) {

		StringBuilder sb = new StringBuilder();

		sb.append(fromTime);

		if (toTime != null && !toTime.equals(fromTime)) {
			sb.append("-")
			  .append(toTime);
		}

		return sb.toString();
	}

	/**
	 * カレンダーに表示する箇所がallDay欄か判定します。
	 * @param toDate 開始時間
	 * @param formDate 終了時間
	 *
	 * @return 文字列
	 */
	private boolean isAllDayEvent(java.util.Date toDate, java.util.Date formDate) {

		if (toDate == null || formDate == null) {
			return false;
		}


		Calendar toCal = Calendar.getInstance();
		Calendar fromCal = Calendar.getInstance();

		toCal.setTime(toDate);
		fromCal.setTime(formDate);

		return (fromCal.get(Calendar.DATE) - toCal.get(Calendar.DATE)) != 0;
	}

	/**
	 * イベントの範囲が最小未満か判別します。
	 * @param toDate 開始時間
	 * @param formDate 終了時間
	 * @param type カレンダータイプ
	 *
	 * @return 文字列
	 */
	private boolean isMinMinutesLesser(java.util.Date toDate, java.util.Date formDate, String type) {

		if (toDate == null || formDate == null || CalendarType.MONTH.toString().equals(type)) {
			return false;
		}


		Calendar toCal = Calendar.getInstance();
		Calendar fromCal = Calendar.getInstance();

		toCal.setTime(toDate);
		fromCal.setTime(formDate);

		long minMinutes;
		if (CalendarType.DAY.toString().equals(type)) {
			minMinutes = 30 * 60 * 1000;
		} else {
			minMinutes = 60 * 60 * 1000;
		}

		return (fromCal.getTimeInMillis() - toCal.getTimeInMillis() > minMinutes);
	}

	@SuppressWarnings("unchecked")
	private void setFilter(EntityCalendarItem item, Map<String, Object> entityFilter, ArrayList<Condition> conditions) {

		// カレンダーフィルタ条件（個別バインド変数未指定）
		if (item.getFilterCondition() != null && !item.getFilterCondition().isEmpty()) {
			conditions.add(new PreparedQuery(item.getFilterCondition()).condition(null));
		}

		if (entityFilter == null) {
			return;
		}

		String fixFilter = (String) entityFilter.get(Constants.CALENDAR_FIX_FILTER);
		if (fixFilter != null) {
			String entityCondition = getEntityFilterCondition(item.getDefinitionName(), fixFilter);
			if (entityCondition != null) {
				conditions.add(new PreparedQuery(entityCondition).condition(null));
			}
		} else {
			List<Map<String, Object>> filterList = (List<Map<String, Object>>) entityFilter.get(Constants.CALENDAR_VALUE_LIST);


			// 個別フィルター条件
			if (CollectionUtil.isNotEmpty(filterList)) {
				Map<String, ArrayList<Condition>> conditionMap = new HashMap<>();
				for (Map<String, Object> filter : filterList) {
					Map<String, Object> property = (Map<String, Object>) filter.get(Constants.CALENDAR_PROPERTY);
					Map<String, Object> condition = (Map<String, Object>) filter.get(Constants.CALENDAR_CONDITION);
					Map<String, Object> keyword = (Map<String, Object>) filter.get(Constants.CALENDAR_KEYWORD);
					Condition c = convertDetailCondition(
							FilterCondition.valueOf((String) condition.get(Constants.CALENDAR_VALUE)),
							(String) property.get(Constants.CALENDAR_VALUE),
							PropertyDefinitionType.valueOf((String) property.get(Constants.CALENDAR_PROPERTY_TYPE)),
							keyword);
					if (c != null) {
						ArrayList<Condition> propertyCondition = conditionMap.get((String) property.get(Constants.CALENDAR_VALUE));
						if (propertyCondition == null) {
							propertyCondition = new ArrayList<>();
							conditionMap.put((String) property.get(Constants.CALENDAR_VALUE), propertyCondition);
						}
						propertyCondition.add(c);
					}
				}

				for (Entry<String, ArrayList<Condition>> entry : conditionMap.entrySet()) {
					ArrayList<Condition> c = entry.getValue();
					if (c.size() > 1) {
						conditions.add(new Paren(new Or(c)));
					} else {
						conditions.add(c.get(0));
					}
				}
			}
		}
}

	private String getEntityFilterCondition(String defname, String filterName) {

		String condition = null;

		if (defname == null || filterName == null) {
			return condition;
		}

		EntityFilter ef = efm.get(defname);

		if (ef == null) {
			return condition;
		}


		for (EntityFilterItem efi : ef.getItems()) {
			if (filterName.equals(efi.getName())) {
				condition = efi.getCondition();
				break;
			}
		}

		return condition;
	}

	public Condition convertDetailCondition(FilterCondition condition, String property, PropertyDefinitionType type, Map<String, Object> keyword) {
		Condition filter = null;
		Object value = keyword != null ? keyword.get(Constants.CALENDAR_DATA_RANGE_FROM) : null;
		Object convValue = convertFilterValue(type, value);

		// リファレンスプロパティはname属性がフィルター項目となる
		if (PropertyDefinitionType.REFERENCE.equals(type)) {
			property = property + "." + Entity.NAME;
		}

		switch (condition) {
			case EQ:
				filter = new Equals(property, convValue);
				break;
			case NE:
				filter = new NotEquals(property, convValue);
				break;
			case SW:
//				filter = new Like(property,  StringUtil.escapeEqlForLike((String) value) + "%");
				filter = new Like(property,  (String) value, Like.MatchPattern.PREFIX);
				break;
			case LW:
//				filter = new Like(property, "%" + StringUtil.escapeEqlForLike((String) value) );
				filter = new Like(property,  (String) value, Like.MatchPattern.POSTFIX);
				break;
			case IC:
//				filter = new Like(property, "%" + StringUtil.escapeEqlForLike((String) value) + "%" );
				filter = new Like(property,  (String) value, Like.MatchPattern.PARTIAL);
				break;
			case NIC:
//				filter =new Paren(new Not(new Like(property, "%" + StringUtil.escapeEqlForLike((String) value) + "%" )));
				filter =new Paren(new Not(new Like(property, (String) value, Like.MatchPattern.PARTIAL)));
				break;
			case IN:
				StrTokenizer st = StrTokenizer.getCSVInstance((String) value);
				String[] values = st.getTokenArray();
				Object[] ret = new Object[values.length];
				for (int i = 0; i < values.length; i++) {
					ret[i] = convertFilterValue(type, values[i]);
				}
				filter = new In(property, ret);
				break;
			case LT:
				filter = new Lesser(property, convValue);
				break;
			case GT:
				filter = new Greater(property, convValue);
				break;
			case LE:
				filter = new LesserEqual(property, convValue);
				break;
			case GE:
				filter = new GreaterEqual(property, convValue);
				break;
			case RG:
				Condition fromCond = null;
				Condition toCond = null;
				Object to = convertFilterValue(type, keyword.get(Constants.CALENDAR_DATA_RANGE_TO));
				if (convValue != null) {
					fromCond = new GreaterEqual(property, convValue);
				}
				if (to != null) {
					toCond = new LesserEqual(property, to);
				}
				if (fromCond != null && toCond != null) {
					filter = new And(fromCond, toCond);
				} else if (fromCond != null && toCond == null) {
					filter = fromCond;
				} else if (fromCond == null && toCond != null) {
					filter = toCond;
				}
				break;
			case NNL:
				filter = new IsNotNull(property);
				break;
			case NL:
				filter = new IsNull(property);
				break;
			default:
				break;

		}

		return filter;
	}

	private Object convertFilterValue(PropertyDefinitionType type, Object val) {
		if (val == null) {
			return null;
		}

		if (type == null) {
			return val;
		}
		String s;
		 if (val instanceof String) {
			 s = (String) val;
		 } else {
			 return val;
		 }


		switch (type) {
		case BINARY:
		case EXPRESSION:
		case BOOLEAN:
			return ConvertUtil.convertFromString(Boolean.class, s);
		case DATETIME:
			return ConvertUtil.convertFromString(Timestamp.class, s);
		case DATE:
			return ConvertUtil.convertFromString(Date.class, s);
		case TIME:
			return ConvertUtil.convertFromString(Time.class, s);
		case INTEGER:
			return ConvertUtil.convertFromString(Long.class, s);
		case DECIMAL:
			return ConvertUtil.convertFromString(BigDecimal.class, s);
		case FLOAT:
			return ConvertUtil.convertFromString(Double.class, s);
		case STRING:
		case AUTONUMBER:
		case SELECT:
		case LONGTEXT:		//#8225
		case REFERENCE:		//#7670
		default:
			return val;
		}
	}

}
