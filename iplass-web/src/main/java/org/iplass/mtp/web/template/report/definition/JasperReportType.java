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

package org.iplass.mtp.web.template.report.definition;


/**
 * <% if (doclang == "ja") {%>
 * <p>Jasper帳票出力用テンプレートファイルのTemplate定義</p>
 * <%} else {%>
 * <p>Template definition of the template file for Jasper report output</p>
 * <%}%>
 *
 * @author lis71n
 *
 */
public class JasperReportType extends ReportType {

	private static final long serialVersionUID = 1706746796693132616L;

	/** パラメータマッピング定義 */
	private ReportParamMapDefinition[] paramMap;

	/** DataSource Attribute名 */
	private String dataSourceAttributeName;

	/** パスワードAttribute名 */
	private String passwordAttributeName;

	public ReportParamMapDefinition[] getParamMap() {
		return paramMap;
	}

	public void setParamMap(ReportParamMapDefinition[] paramMap) {
		this.paramMap = paramMap;
	}

	public String getDataSourceAttributeName() {
		return dataSourceAttributeName;
	}

	public void setDataSourceAttributeName(String dataSourceAttributeName) {
		this.dataSourceAttributeName = dataSourceAttributeName;
	}

	public String getPasswordAttributeName() {
		return passwordAttributeName;
	}

	public void setPasswordAttributeName(String passwordAttributeName) {
		this.passwordAttributeName = passwordAttributeName;
	}

}
