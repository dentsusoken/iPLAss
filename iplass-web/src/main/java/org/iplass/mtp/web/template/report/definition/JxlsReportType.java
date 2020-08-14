/*
 * Copyright (C) 2020 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
 * <p>JXLS帳票出力用テンプレートファイルのTemplate定義。</p>
 *
 * @author Y.Ishida
 *
 */
public class JxlsReportType extends ReportType {
	
	private static final long serialVersionUID = 4425769299483314155L;
	
	private ReportOutputLogicDefinition reportOutputLogicDefinition;
	
	private JxlsContextParamMapDefinition[] contextParamMap;
	
	private String passwordAttributeName;

	public ReportOutputLogicDefinition getReportOutputLogicDefinition() {
		return reportOutputLogicDefinition;
	}

	public void setReportOutputLogicDefinition(ReportOutputLogicDefinition reportOutputLogicDefinition) {
		this.reportOutputLogicDefinition = reportOutputLogicDefinition;
	}

	public JxlsContextParamMapDefinition[] getContextParamMap() {
		return contextParamMap;
	}

	public void setContextParamMap(JxlsContextParamMapDefinition[] contextParamMap) {
		this.contextParamMap = contextParamMap;
	}

	public String getPasswordAttributeName() {
		return passwordAttributeName;
	}

	public void setPasswordAttributeName(String passwordAttributeName) {
		this.passwordAttributeName = passwordAttributeName;
	}

}
