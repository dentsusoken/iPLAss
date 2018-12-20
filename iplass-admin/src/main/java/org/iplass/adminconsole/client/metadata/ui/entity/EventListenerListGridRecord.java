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

package org.iplass.adminconsole.client.metadata.ui.entity;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class EventListenerListGridRecord extends ListGridRecord {

	public static final String ELNAME = "elName";
	public static final String SCRIPT = "script";
	public static final String AFTERD = "afterD";
	public static final String AFTERI = "afterI";
	public static final String AFTERU = "afterU";
	public static final String BEFORED = "beforeD";
	public static final String BEFOREI = "beforeI";
	public static final String BEFOREU = "beforeU";
	public static final String AFTERR = "afterR";
	public static final String AFTERP = "afterP";
	public static final String ONLOAD = "onLoad";
	public static final String BEFOREVALIDATE = "beforeValidate";
	public static final String CNAME = "className";
	public static final String WITHOUTMAPPEDBY = "WITHOUTMAPPEDBY";
	public static final String NTYPE = "notificationType";
	public static final String NCONDSCRIPT = "notificationCondScript";
	public static final String TDNAME = "tmplDefName";

	public static final String GP = "gp";

	public String getElName() {
		return getAttribute(ELNAME);
	}

	public void setElName(String value) {
		setAttribute(ELNAME, value);
	}

	public String getScript() {
		return getAttribute(SCRIPT);
	}

	public void setScript(String value) {
		setAttribute(SCRIPT, value);
	}

	public boolean isAfterD() {
		return getAttributeAsBoolean(AFTERD);
	}

	public void setAfterD(boolean value) {
		setAttribute(AFTERD, value);
	}

	public boolean isAfterI() {
		return getAttributeAsBoolean(AFTERI);
	}

	public void setAfterI(boolean value) {
		setAttribute(AFTERI, value);
	}

	public boolean isAfterU() {
		return getAttributeAsBoolean(AFTERU);
	}

	public void setAfterU(boolean value) {
		setAttribute(AFTERU, value);
	}

	public boolean isBeforeD() {
		return getAttributeAsBoolean(BEFORED);
	}

	public void setBeforeD(boolean value) {
		setAttribute(BEFORED, value);
	}

	public boolean isBeforeI() {
		return getAttributeAsBoolean(BEFOREI);
	}

	public void setBeforeI(boolean value) {
		setAttribute(BEFOREI, value);
	}

	public boolean isBeforeU() {
		return getAttributeAsBoolean(BEFOREU);
	}

	public void setBeforeU(boolean value) {
		setAttribute(BEFOREU, value);
	}

	public boolean isAfterR() {
		return getAttributeAsBoolean(AFTERR);
	}

	public void setAfterR(boolean value) {
		setAttribute(AFTERR, value);
	}

	public boolean isAfterP() {
		return getAttributeAsBoolean(AFTERP);
	}

	public void setAfterP(boolean value) {
		setAttribute(AFTERP, value);
	}

	public boolean isOnLoad() {
		return getAttributeAsBoolean(ONLOAD);
	}

	public void setOnLoad(boolean value) {
		setAttribute(ONLOAD, value);
	}

	public boolean isBeforeValidate() {
		return getAttributeAsBoolean(BEFOREVALIDATE);
	}

	public void setBeforeValidate(boolean value) {
		setAttribute(BEFOREVALIDATE, value);
	}

	public String getClassName() {
		return getAttribute(CNAME);
	}

	public void setClassName(String value) {
		setAttribute(CNAME, value);
	}

	public boolean isWithoutMappedByReference() {
		return getAttributeAsBoolean(WITHOUTMAPPEDBY);
	}

	public void setWithoutMappedByReference(boolean value) {
		setAttribute(WITHOUTMAPPEDBY, value);
	}

	public String getNotificationType() {
		return getAttribute(NTYPE);
	}

	public void setNotificationType(String value) {
		setAttribute(NTYPE, value);
	}

	public String getTmplDefName() {
		return getAttribute(TDNAME);
	}

	public void setTmplDefName(String value) {
		setAttribute(TDNAME, value);
	}

	public String getNotificationCondScript() {
		return getAttribute(NCONDSCRIPT);
	}

	public void setNotificationCondScript(String value) {
		setAttribute(NCONDSCRIPT, value);
	}

	public String getGeneralPurpus() {
		return getAttribute(GP);
	}

	public void setGeneralPurpus(String value) {
		setAttribute(GP, value);
	}
}
