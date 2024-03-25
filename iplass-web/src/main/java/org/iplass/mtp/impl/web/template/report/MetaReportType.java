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

import jakarta.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.impl.metadata.BaseMetaDataRuntime;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.report.ReportingOutputModel;
import org.iplass.mtp.web.template.report.definition.ReportType;

@XmlSeeAlso({MetaJasperReportType.class, MetaPoiReportType.class, MetaJxlsReportType.class})
public abstract class MetaReportType implements MetaData {

	private static final long serialVersionUID = 401694582362361475L;

	private String outputFileType;

	public String getOutputFileType() {
	    return outputFileType;
	}

	public void setOutputFileType(String outputFileType) {
	    this.outputFileType = outputFileType;
	}
	
	//Definition → Meta共通項目
	protected void fillFrom(ReportType reportType) {
		outputFileType = reportType.getOutputFileType();
	}
	
	//Meta共通項目 → Definition
	protected void fillTo(ReportType reportType) {
		reportType.setOutputFileType(outputFileType);
	}

	//Definition → Meta
	public abstract void applyConfig(ReportType reportType);

	//Meta → Definition
	public abstract ReportType currentConfig();

	public abstract ReportTypeRuntime createRuntime();

	public abstract class ReportTypeRuntime extends BaseMetaDataRuntime {

		public ReportTypeRuntime() {
		}
		
		public abstract void setParam(ReportingOutputModel createOutputModel);
	}

}
