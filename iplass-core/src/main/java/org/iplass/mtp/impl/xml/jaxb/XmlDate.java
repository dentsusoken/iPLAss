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

import java.sql.Date;
import java.util.GregorianCalendar;

import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.XmlValue;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * jaxb用のjava.sql.Date表現。
 * xs:dateへマッピング。
 *
 * @author K.Higuchi
 *
 */
@XmlType(name="date", namespace="http://www.w3.org/2001/XMLSchema")
public class XmlDate {

	public XmlDate() {
	}

	public XmlDate(Date date) {

		//system固定のlocale/timezoneでOK。最終的にXMLGregorianCalendarで使うためだけなので
		GregorianCalendar cal = new GregorianCalendar();
//		GregorianCalendar cal = DateUtil.getGregorianCalendar(false);
		cal.setTime(date);
		XMLGregorianCalendar xmlCal = XmlDataTypeFactory.factory.newXMLGregorianCalendar(cal);
		xmlCal.setHour(DatatypeConstants.FIELD_UNDEFINED);
		xmlCal.setMinute(DatatypeConstants.FIELD_UNDEFINED);
		xmlCal.setSecond(DatatypeConstants.FIELD_UNDEFINED);

		//TODO Date（日付）にtimezoneは必要か？

		this.value = xmlCal.toXMLFormat();
	}

	@XmlValue
	String value;

	@Override
	public String toString() {
		return "XmlDate [value=" + value + "]";
	}

	public Date toDate() {
		XMLGregorianCalendar xmlCal = XmlDataTypeFactory.factory.newXMLGregorianCalendar(value);
		GregorianCalendar cal = xmlCal.toGregorianCalendar();
		return new Date(cal.getTimeInMillis());
	}
	
	public String toXmlString() {
		return value;
	}

}
