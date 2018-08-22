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

package org.iplass.mtp.impl.web.template.report;

import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.report.JasperReportingOutputModel;
import org.iplass.mtp.impl.report.ReportingOutputModel;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.web.template.report.definition.JasperReportType;
import org.iplass.mtp.web.template.report.definition.ReportParamMapDefinition;
import org.iplass.mtp.web.template.report.definition.ReportType;

public class MetaJasperReportType extends MetaReportType {

	private static final long serialVersionUID = -7729412056808322644L;

	/** パラメータマッピング定義 */
	private MetaReportParamMap[] paramMap;

	/** DataSource Attribute名 */
	private String dataSourceAttributeName;

	/** パスワードAttribute名 */
	private String passwordAttributeName;

	public MetaReportParamMap[] getParamMap() {
		return paramMap;
	}

	public void setParamMap(MetaReportParamMap[] paramMap) {
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

	@Override
	public void applyConfig(ReportType reportType) {
		JasperReportType def = (JasperReportType)reportType;
		fillFrom(def);

		if (def.getParamMap() != null) {
			paramMap = new MetaReportParamMap[def.getParamMap().length];
			int i = 0;
			for (ReportParamMapDefinition paramDef : def.getParamMap()) {
				paramMap[i] = new MetaReportParamMap();
				paramMap[i].applyConfig(paramDef);
				i++;
			}
		} else {
			paramMap = null;
		}

		dataSourceAttributeName = def.getDataSourceAttributeName();

		passwordAttributeName = def.getPasswordAttributeName();
	}

	@Override
	public ReportType currentConfig() {
		JasperReportType definition = new JasperReportType();
		fillTo(definition);

		if (paramMap != null) {
			ReportParamMapDefinition[] paramMapDefinition = new ReportParamMapDefinition[paramMap.length];
			int i = 0;
			for (MetaReportParamMap map : paramMap) {
				paramMapDefinition[i] = map.currentConfig();
				i++;
			}
			definition.setParamMap(paramMapDefinition);
		}

		definition.setDataSourceAttributeName(dataSourceAttributeName);
		definition.setPasswordAttributeName(passwordAttributeName);

		return definition;
	}

	@Override
	public void setParam(ReportingOutputModel createOutputModel) {
		JasperReportingOutputModel model = (JasperReportingOutputModel)createOutputModel;
		if(paramMap != null){
			model.setMaps(paramMap);
		}
		model.setDataSourceAttributeName(dataSourceAttributeName);
		model.setPasswordAttributeName(passwordAttributeName);
	}

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

}
