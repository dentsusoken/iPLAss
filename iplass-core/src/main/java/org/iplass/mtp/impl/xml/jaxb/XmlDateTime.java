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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.GregorianCalendar;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * jaxb用のjava.sql.Timestamp表現。
 * xs:dateTimeへマッピング。
 *
 * @author K.Higuchi
 *
 */
@XmlType(name="dateTime", namespace="http://www.w3.org/2001/XMLSchema")
public class XmlDateTime {

	public XmlDateTime() {
	}

	public XmlDateTime(Timestamp dateTime) {

		//system固定のlocale/timezoneでOK。最終的にXMLGregorianCalendarで使うためだけなので
		GregorianCalendar cal = new GregorianCalendar();
//		GregorianCalendar cal = DateUtil.getGregorianCalendar(true);
		cal.setTime(dateTime);
		XMLGregorianCalendar xmlCal = XmlDataTypeFactory.factory.newXMLGregorianCalendar(cal);
		xmlCal.setFractionalSecond(new BigDecimal(BigInteger.valueOf(dateTime.getNanos()), 9));

		this.value = xmlCal.toXMLFormat();
	}

	public Timestamp toTimestamp() {
		XMLGregorianCalendar xmlCal = XmlDataTypeFactory.factory.newXMLGregorianCalendar(value);
		GregorianCalendar cal = xmlCal.toGregorianCalendar();
		Timestamp ts = new Timestamp(cal.getTimeInMillis());
		if (xmlCal.getFractionalSecond() != null) {
			ts.setNanos(xmlCal.getFractionalSecond().movePointRight(9).intValue());
		}
		return ts;
	}

	@XmlValue
	String value;

	@Override
	public String toString() {
		return "XmlDateTime [value=" + value + "]";
	}

	public String toXmlString() {
		return value;
	}

}
