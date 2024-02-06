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

package org.iplass.adminconsole.client.metadata.ui.template.report;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class ReportOutLogicListGridRecord extends ListGridRecord {

	public static final String ELNAME = "elName";
	public static final String SCRIPT = "script";
	public static final String CNAME = "className";

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

	public String getClassName() {
		return getAttribute(CNAME);
	}

	public void setClassName(String value) {
		setAttribute(CNAME, value);
	}

	public String getGeneralPurpus() {
		return getAttribute(GP);
	}

	public void setGeneralPurpus(String value) {
		setAttribute(GP, value);
	}
}
