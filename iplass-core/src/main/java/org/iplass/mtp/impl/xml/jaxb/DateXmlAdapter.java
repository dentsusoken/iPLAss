/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * 日付関連プロパティのマッピングをカスタマイズ。
 *
 * @author K.Higuchi
 *
 */
public class DateXmlAdapter extends XmlAdapter<Object, Object> {

	@Override
	public Object unmarshal(Object v) throws Exception {

		//XmlGregorianCalendar#getXMLSchemaType
		//Date、Time、DateTimeを出しわけるためカスタムのマッピングを行う
		if (v instanceof XmlDateTime) {
			return ((XmlDateTime) v).toTimestamp();
		}
		if (v instanceof XmlDate) {
			return ((XmlDate) v).toDate();
		}
		if (v instanceof XmlTime) {
			return ((XmlTime) v).toTime();
		}
		if (v instanceof XmlDateTime[]) {
			XmlDateTime[] xdt = (XmlDateTime[]) v;
			java.sql.Timestamp[] ts = new java.sql.Timestamp[xdt.length];
			for (int i = 0; i < xdt.length; i++) {
				ts[i] = xdt.clone()[i].toTimestamp();
			}
			return ts;
		}
		if (v instanceof XmlDate[]) {
			XmlDate[] xd = (XmlDate[]) v;
			java.sql.Date[] ts = new java.sql.Date[xd.length];
			for (int i = 0; i < xd.length; i++) {
				ts[i] = xd[i].toDate();
			}
			return ts;
		}
		if (v instanceof XmlTime[]) {
			XmlTime[] xt = (XmlTime[]) v;
			java.sql.Time[] ts = new java.sql.Time[xt.length];
			for (int i = 0; i < xt.length; i++) {
				ts[i] = xt[i].toTime();
			}
			return ts;
		}

		return v;
	}

	@Override
	public Object marshal(Object v) throws Exception {

		//Date、Time、DateTimeを出しわけるためカスタムのマッピングを行う
		if (v instanceof java.sql.Timestamp) {
			return new XmlDateTime((java.sql.Timestamp) v);
		}
		if (v instanceof java.sql.Date) {
			return new XmlDate((java.sql.Date) v);
		}
		if (v instanceof java.sql.Time) {
			return new XmlTime((java.sql.Time) v);
		}
		if (v instanceof java.sql.Timestamp[]) {
			java.sql.Timestamp[] ts = (java.sql.Timestamp[]) v;
			XmlDateTime[] res = new XmlDateTime[ts.length];
			for (int i = 0; i < ts.length; i++) {
				res[i] = new XmlDateTime(ts[i]);
			}
			return res;
		}
		if (v instanceof java.sql.Date[]) {
			java.sql.Date[] ts = (java.sql.Date[]) v;
			XmlDate[] res = new XmlDate[ts.length];
			for (int i = 0; i < ts.length; i++) {
				res[i] = new XmlDate(ts[i]);
			}
			return res;
		}
		if (v instanceof java.sql.Time[]) {
			java.sql.Time[] ts = (java.sql.Time[]) v;
			XmlTime[] res = new XmlTime[ts.length];
			for (int i = 0; i < ts.length; i++) {
				res[i] = new XmlTime(ts[i]);
			}
			return res;
		}

		return v;
	}



}
