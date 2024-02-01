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

package org.iplass.mtp.impl.web.template.report;

import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.report.PoiReportingOutputModel;
import org.iplass.mtp.impl.report.ReportingOutputModel;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.web.template.report.MetaPoiReportOutputLogic.PoiReportOutputLogicRuntime;
import org.iplass.mtp.web.template.report.definition.GroovyReportOutputLogicDefinition;
import org.iplass.mtp.web.template.report.definition.PoiReportType;
import org.iplass.mtp.web.template.report.definition.ReportOutputLogicDefinition;
import org.iplass.mtp.web.template.report.definition.ReportType;

public class MetaPoiReportType extends MetaReportType {

	private static final long serialVersionUID = -7786960053523202593L;

	private MetaPoiReportOutputLogic reportOutputLogic;

	private String passwordAttributeName;

	public MetaPoiReportOutputLogic getReportOutputLogic() {
	    return reportOutputLogic;
	}

	public void setReportOutputLogic(MetaPoiReportOutputLogic reportOutputLogic) {
	    this.reportOutputLogic = reportOutputLogic;
	}

	public String getPasswordAttributeName() {
		return passwordAttributeName;
	}

	public void setPasswordAttributeName(String passwordAttributeName) {
		this.passwordAttributeName = passwordAttributeName;
	}

	@Override
	public void applyConfig(ReportType definition) {
		PoiReportType def = (PoiReportType) definition;
		fillFrom(def);

		if (def.getReportOutputLogicDefinition() != null) {
			ReportOutputLogicDefinition ed = def.getReportOutputLogicDefinition();
			if (ed instanceof GroovyReportOutputLogicDefinition) {
				MetaGroovyPoiReportOutputLogic ms = new MetaGroovyPoiReportOutputLogic();
				ms.applyConfig(ed);
				reportOutputLogic = ms;
			} else {
				MetaJavaClassPoiReportOutputLogic ms = new MetaJavaClassPoiReportOutputLogic();
				ms.applyConfig(ed);
				reportOutputLogic = ms;
			}
		} else {
			reportOutputLogic = null;
		}
		passwordAttributeName = def.getPasswordAttributeName();
	}

	@Override
	public ReportType currentConfig() {
		PoiReportType definition = new PoiReportType();
		fillTo(definition);
		if (reportOutputLogic != null) {
			definition.setReportOutputLogicDefinition(reportOutputLogic.currentConfig());
		}
		definition.setPasswordAttributeName(passwordAttributeName);

		return definition;
	}
	
	@Override
	public ReportTypeRuntime createRuntime() {
		return new PoiReportTypeRuntime();
	}
	
	public class PoiReportTypeRuntime extends ReportTypeRuntime {
		private PoiReportOutputLogicRuntime outputLogicRuntime;
		
		public PoiReportTypeRuntime() {
			if (reportOutputLogic != null) {
				outputLogicRuntime = reportOutputLogic.createRuntime(getMetaData());
			}
		}
		
		@Override
		public MetaPoiReportType getMetaData() {
			return MetaPoiReportType.this;
		}

		@Override
		public void setParam(ReportingOutputModel createOutputModel) {
			PoiReportingOutputModel model = (PoiReportingOutputModel)createOutputModel;
			if(reportOutputLogic != null){
				model.setLogicRuntime(outputLogicRuntime);
			}
			model.setPasswordAttributeName(passwordAttributeName);
		}
	}

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

}
