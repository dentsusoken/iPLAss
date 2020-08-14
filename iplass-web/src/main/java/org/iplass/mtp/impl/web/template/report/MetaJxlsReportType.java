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
package org.iplass.mtp.impl.web.template.report;

import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.report.JxlsCompiledScriptCacheStore;
import org.iplass.mtp.impl.report.JxlsReportingOutputModel;
import org.iplass.mtp.impl.report.ReportingOutputModel;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.web.template.report.MetaJxlsReportOutputLogic.JxlsReportOutputLogicRuntime;
import org.iplass.mtp.web.template.report.definition.GroovyReportOutputLogicDefinition;
import org.iplass.mtp.web.template.report.definition.JxlsContextParamMapDefinition;
import org.iplass.mtp.web.template.report.definition.JxlsReportType;
import org.iplass.mtp.web.template.report.definition.ReportOutputLogicDefinition;
import org.iplass.mtp.web.template.report.definition.ReportType;

public class MetaJxlsReportType extends MetaReportType {

	private static final long serialVersionUID = 8399080514538892382L;
	
	private MetaJxlsReportOutputLogic reportOutputLogic;
	
	private MetaJxlsContextParamMap[] contextParamMap;
	
	private String passwordAttributeName;
	
	public MetaJxlsReportOutputLogic getReportOutputLogic() {
		return reportOutputLogic;
	}

	public void setReportOutputLogic(MetaJxlsReportOutputLogic reportOutputLogic) {
		this.reportOutputLogic = reportOutputLogic;
	}
	
	public MetaJxlsContextParamMap[] getContextParamMap() {
		return contextParamMap;
	}

	public void setContextParamMap(MetaJxlsContextParamMap[] contextParamMap) {
		this.contextParamMap = contextParamMap;
	}

	public String getPasswordAttributeName() {
		return passwordAttributeName;
	}

	public void setPasswordAttributeName(String passwordAttributeName) {
		this.passwordAttributeName = passwordAttributeName;
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
		
		if (def.getContextParamMap() != null) {
			contextParamMap = new MetaJxlsContextParamMap[def.getContextParamMap().length];
			int i = 0;
			for (JxlsContextParamMapDefinition jcpmd : def.getContextParamMap()) {
				contextParamMap[i] = new MetaJxlsContextParamMap();
				contextParamMap[i].applyConfig(jcpmd);
				i++;
			}
		} else {
			contextParamMap = null;
		}
		passwordAttributeName = def.getPasswordAttributeName();
	}

	@Override
	public ReportType currentConfig() {
		JxlsReportType definition = new JxlsReportType();
		fillTo(definition);
		
		if (reportOutputLogic != null) {
			definition.setReportOutputLogicDefinition(reportOutputLogic.currentConfig());
		}
		
		if (contextParamMap != null) {
			JxlsContextParamMapDefinition[] paramMapDefinition = 
					new JxlsContextParamMapDefinition[contextParamMap.length];
			int i = 0;
			for (MetaJxlsContextParamMap map : contextParamMap) {
				paramMapDefinition[i] = map.currentConfig();
				i++;
			}
			definition.setContextParamMap(paramMapDefinition);
		}
		
		definition.setPasswordAttributeName(passwordAttributeName);

		return definition;
	}
	
	@Override
	public ReportTypeRuntime createRuntime(MetaDataConfig metaDataConfig) {
		return new JxlsReportTypeRuntime();
	}
	
	public class JxlsReportTypeRuntime extends ReportTypeRuntime {
		
		private JxlsCompiledScriptCacheStore cacheStore;
		private JxlsReportOutputLogicRuntime outputLogicRuntime;
		
		public JxlsReportTypeRuntime() {
			cacheStore = new JxlsCompiledScriptCacheStore();
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
			if (contextParamMap != null) {
				model.setContextParamMap(contextParamMap);
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
