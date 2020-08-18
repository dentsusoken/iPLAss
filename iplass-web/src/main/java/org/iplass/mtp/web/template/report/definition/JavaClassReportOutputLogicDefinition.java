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

package org.iplass.mtp.web.template.report.definition;


/**
 * <% if (doclang == "ja") {%>
 * <p>
 * JavaクラスによるReportOutputLogic定義<br>
 * </p>
 * ReportOutputLogicをimplementsしたクラスを登録する。
 * 
 * <%} else {%>
 * <p>
 * The Definition of a report output logic in Java class<br>
 * </p>
 * Set a class that implements ReportOutputLogic.
 * 
 * <%}%>
 *
 * @author lis71n
 *
 */

public class JavaClassReportOutputLogicDefinition extends ReportOutputLogicDefinition {
	
	private static final long serialVersionUID = -5912655631675112047L;
	
	private String className;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

}
