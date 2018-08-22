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

import org.iplass.mtp.view.calendar.EntityCalendar.CalendarType;




/**
 * 日付毎のデータ
 * @author lis7zi
 */
public class CalendarData {

	/** カレンダータイプ */
	private String calendarType;
	
	/** タイトル */
	private String title;
	
	/** 時間表記 */
	private String time;

	/** 表示用開始時間(カレンダーに表示される開始時間) */
	private String start;
	
	/** 表示用終了時間(カレンダーに表示される終了時間) */
	private String end;
	
	/** 終日フラグ trueの時は終日欄に表示されます */
	private boolean allDay;
	
	/** エンティティごとの予定表、テキスト色 */
	private String textColor;
	
	/** カレンダーに表示するEntityのデータ */
	private CalendarEntityData calendarEntityData;
	

	public CalendarData(String calendarType) {
		this.calendarType = calendarType;
	}
	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public boolean isAllDay() {
		return allDay;
	}

	public void setAllDay(boolean allDay) {
		this.allDay = allDay;
	}

	public CalendarEntityData getCalendarEntityData() {
		return calendarEntityData;
	}

	public void setCalendarEntityData(CalendarEntityData calendarEntityData) {
		this.calendarEntityData = calendarEntityData;
	}

	/** 月表示かつallday = false の場合、透過100% */
	public String getBackgroundColor() {
		if(!allDay && CalendarType.MONTH.toString().equals(calendarType)) {
			return "rgba(255,255,255,0.0)";
		}
			
		return "rgba(255,255,255,1.0)";
	}

	
	public void setTextColor(String textColor) {
		this.textColor = textColor;
	}

	public String getTextColor() {
		return textColor;
	}
	
	public String getBorderColor() {
		if(!allDay && CalendarType.MONTH.toString().equals(calendarType)) {
			return "rgba(255,255,255,0.0)";
		}
		return null;
	}


}
