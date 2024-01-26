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
package org.iplass.mtp.impl.web.template.report;

import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.report.JxlsCompiledScriptCacheStore;
import org.iplass.mtp.impl.report.JxlsReportingOutputModel;
import org.iplass.mtp.impl.report.ReportingOutputModel;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.web.template.report.MetaJxlsReportOutputLogic.JxlsReportOutputLogicRuntime;
import org.iplass.mtp.web.template.report.definition.GroovyReportOutputLogicDefinition;
import org.iplass.mtp.web.template.report.definition.JxlsReportType;
import org.iplass.mtp.web.template.report.definition.ReportOutputLogicDefinition;
import org.iplass.mtp.web.template.report.definition.ReportParamMapDefinition;
import org.iplass.mtp.web.template.report.definition.ReportType;

public class MetaJxlsReportType extends MetaReportType {

	private static final long serialVersionUID = 8399080514538892382L;
	
	private MetaJxlsReportOutputLogic reportOutputLogic;
	
	private MetaReportParamMap[] paramMap;
	
	private String passwordAttributeName;
	
	private String templateName;
	
	public MetaJxlsReportOutputLogic getReportOutputLogic() {
		return reportOutputLogic;
	}

	public void setReportOutputLogic(MetaJxlsReportOutputLogic reportOutputLogic) {
		this.reportOutputLogic = reportOutputLogic;
	}
	
	public MetaReportParamMap[] getParamMap() {
		return paramMap;
	}

	public void setParamMap(MetaReportParamMap[] paramMap) {
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

	@Override
	public void applyConfig(ReportType definition) {
		JxlsReportType def = (JxlsReportType) definition;
		fillFrom(def);
		
		if(def.getReportOutputLogicDefinition() != null) {
			ReportOutputLogicDefinition ed = def.getReportOutputLogicDefinition();
			if (ed instanceof GroovyReportOutputLogicDefinition) {
				MetaGroovyJxlsReportOutputLogic ms = new MetaGroovyJxlsReportOutputLogic();
				ms.applyConfig(ed);
				reportOutputLogic = ms;
			} else {
				MetaJavaClassJxlsReportOutputLogic ms = new MetaJavaClassJxlsReportOutputLogic();
				ms.applyConfig(ed);
				reportOutputLogic = ms;
			}
		} else {
			reportOutputLogic = null;
		}
		
		if (def.getParamMap() != null) {
			paramMap = new MetaReportParamMap[def.getParamMap().length];
			int i = 0;
			for (ReportParamMapDefinition jcpmd : def.getParamMap()) {
				paramMap[i] = new MetaReportParamMap();
				paramMap[i].applyConfig(jcpmd);
				i++;
			}
		} else {
			paramMap = null;
		}
		passwordAttributeName = def.getPasswordAttributeName();
		templateName = def.getTemplateName();
	}

	@Override
	public ReportType currentConfig() {
		JxlsReportType definition = new JxlsReportType();
		fillTo(definition);
		
		if (reportOutputLogic != null) {
			definition.setReportOutputLogicDefinition(reportOutputLogic.currentConfig());
		}
		
		if (paramMap != null) {
			ReportParamMapDefinition[] paramMapDefinition = 
					new ReportParamMapDefinition[paramMap.length];
			int i = 0;
			for (MetaReportParamMap map : paramMap) {
				paramMapDefinition[i] = map.currentConfig();
				i++;
			}
			definition.setParamMap(paramMapDefinition);
		}
		
		definition.setPasswordAttributeName(passwordAttributeName);
		definition.setTemplateName(templateName);

		return definition;
	}
	
	@Override
	public ReportTypeRuntime createRuntime() {
		return new JxlsReportTypeRuntime();
	}
	
	public class JxlsReportTypeRuntime extends ReportTypeRuntime {
		
		private JxlsCompiledScriptCacheStore cacheStore;
		private JxlsReportOutputLogicRuntime outputLogicRuntime;
		
		public JxlsReportTypeRuntime() {
			cacheStore = new JxlsCompiledScriptCacheStore(templateName);
			if (reportOutputLogic != null) {
				outputLogicRuntime = reportOutputLogic.createRuntime(getMetaData());
			}
		}
		
		@Override
		public MetaJxlsReportType getMetaData() {
			return MetaJxlsReportType.this;
		}

		@Override
		public void setParam(ReportingOutputModel createOutputModel) {
			JxlsReportingOutputModel model = (JxlsReportingOutputModel)createOutputModel;
			if (reportOutputLogic !=null) {
				model.setLogicRuntime(outputLogicRuntime);
			}
			if (paramMap != null) {
				model.setParamMap(paramMap);
			}
			model.setPasswordAttributeName(passwordAttributeName);
			model.setCacheStore(cacheStore);
		}
	}
	
	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

}
