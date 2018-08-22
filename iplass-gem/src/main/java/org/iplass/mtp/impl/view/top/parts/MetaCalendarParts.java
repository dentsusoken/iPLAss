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

package org.iplass.mtp.impl.view.top.parts;

import javax.servlet.http.HttpServletRequest;

import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.view.top.parts.CalendarParts;
import org.iplass.mtp.view.top.parts.TopViewParts;

/**
 * カレンダーパーツ
 * @author lis3wg
 */
public class MetaCalendarParts extends MetaTemplateParts {

	/** SerialVersionUID */
	private static final long serialVersionUID = -1412208019553670440L;

	/**
	 * インスタンスを生成します。
	 * @param parts TOP画面パーツ
	 * @return インスタンス
	 */
	public static MetaCalendarParts createInstance(TopViewParts parts) {
		return new MetaCalendarParts();
	}

	/** カレンダー定義名 */
	private String calendarName;

	/** アイコンタグ */
	private String iconTag;

	/**
	 * カレンダー定義名を取得します。
	 * @return カレンダー定義名
	 */
	public String getCalendarName() {
	    return calendarName;
	}

	/**
	 * カレンダー定義名を設定します。
	 * @param calendarName カレンダー定義名
	 */
	public void setCalendarName(String calendarName) {
	    this.calendarName = calendarName;
	}

	/**
	 * アイコンタグを取得します。
	 * @return アイコンタグ
	 */
	public String getIconTag() {
	    return iconTag;
	}

	/**
	 * アイコンタグを設定します。
	 * @param iconTag アイコンタグ
	 */
	public void setIconTag(String iconTag) {
	    this.iconTag = iconTag;
	}

	@Override
	public void applyConfig(TopViewParts parts) {
		CalendarParts calendar = (CalendarParts) parts;
		calendarName = calendar.getCalendarName();
		iconTag = calendar.getIconTag();
	}

	@Override
	public TopViewParts currentConfig() {
		CalendarParts calendar = new CalendarParts();
		calendar.setCalendarName(calendarName);
		calendar.setIconTag(iconTag);
		return calendar;
	}

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public TemplatePartsHandler createRuntime() {
		return new TemplatePartsHandler(this) {
			private static final String TEMPLATE_PATH_GEM = "gem/calendar/calendarParts";
			private static final String TEMPLATE_PATH_GEM_WIDGET = "gem/calendar/calendarWidget";

			@Override
			public boolean isParts() {
				return true;
			}

			@Override
			public boolean isWidget() {
				return true;
			}

			@Override
			public String getTemplatePathForParts(HttpServletRequest req) {
				return TEMPLATE_PATH_GEM;
			}

			@Override
			public String getTemplatePathForWidget(HttpServletRequest req) {
				return TEMPLATE_PATH_GEM_WIDGET;
			}

			@Override
			public void setAttribute(HttpServletRequest req) {
				req.setAttribute("calendarName", calendarName);
				req.setAttribute("calendarParts", currentConfig());
			}

			@Override
			public void clearAttribute(HttpServletRequest req) {
				req.removeAttribute("calendarName");
				req.removeAttribute("calendarParts");
			}
		};
	}
}
