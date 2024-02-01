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

package org.iplass.mtp.impl.xml.jaxb;

import java.sql.Time;
import java.util.GregorianCalendar;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * jaxb用のjava.sql.Time表現。
 * xs:timeへマッピング。
 *
 * @author K.Higuchi
 *
 */
@XmlType(name="time", namespace="http://www.w3.org/2001/XMLSchema")
public class XmlTime {

	public XmlTime() {
	}

	public XmlTime(Time time) {

		//system固定のlocale/timezoneでOK。最終的にXMLGregorianCalendarで使うためだけなので
		GregorianCalendar cal = new GregorianCalendar();
//		GregorianCalendar cal = DateUtil.getGregorianCalendar(false);
		cal.setTime(time);
		XMLGregorianCalendar xmlCal = XmlDataTypeFactory.factory.newXMLGregorianCalendar(cal);
		xmlCal.setYear(DatatypeConstants.FIELD_UNDEFINED);
		xmlCal.setMonth(DatatypeConstants.FIELD_UNDEFINED);
		xmlCal.setDay(DatatypeConstants.FIELD_UNDEFINED);
		xmlCal.setMillisecond(DatatypeConstants.FIELD_UNDEFINED);

		//TODO Time（時刻）にtimezoneは必要か？

		this.value = xmlCal.toXMLFormat();
	}

	@XmlValue
	String value;

	@Override
	public String toString() {
		return "XmlTime [value=" + value + "]";
	}

	public Time toTime() {
		XMLGregorianCalendar xmlCal = XmlDataTypeFactory.factory.newXMLGregorianCalendar(value);
		GregorianCalendar cal = xmlCal.toGregorianCalendar();
		return new Time(cal.getTimeInMillis());
	}
	
	public String toXmlString() {
		return value;
	}
}
