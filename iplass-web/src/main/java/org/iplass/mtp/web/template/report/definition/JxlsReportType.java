/*
 * Copyright (C) 2020 DENTSU SOKEN INC. All Rights Reserved.
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
 * <p>JXLS帳票出力用テンプレートファイルのTemplate定義</p>
 * <%} else {%>
 * <p>Template definition of the template file for JXLS report output</p>
 * <%}%>
 *
 * @author Y.Ishida
 *
 */
public class JxlsReportType extends ReportType {
	
	private static final long serialVersionUID = 4425769299483314155L;
	
	private ReportOutputLogicDefinition reportOutputLogicDefinition;
	
	private ReportParamMapDefinition[] paramMap;
	
	private String passwordAttributeName;
	
	private String templateName;

	public ReportOutputLogicDefinition getReportOutputLogicDefinition() {
		return reportOutputLogicDefinition;
	}

	public void setReportOutputLogicDefinition(ReportOutputLogicDefinition reportOutputLogicDefinition) {
		this.reportOutputLogicDefinition = reportOutputLogicDefinition;
	}

	public ReportParamMapDefinition[] getParamMap() {
		return paramMap;
	}

	public void setParamMap(ReportParamMapDefinition[] paramMap) {
		this.paramMap = paramMap;
	}

	public String getPasswordAttributeName() {
		return passwordAttributeName;
	}

	public void setPasswordAttributeName(String passwordAttributeName) {
		this.passwordAttributeName = passwordAttributeName;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

}
