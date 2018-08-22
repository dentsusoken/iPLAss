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
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.web.template.report.definition.JasperReportType;
import org.iplass.mtp.web.template.report.definition.LocalizedReportDefinition;
import org.iplass.mtp.web.template.report.definition.PoiReportType;
import org.iplass.mtp.web.template.report.definition.ReportType;

public class MetaLocalizedReport implements MetaData {

	private static final long serialVersionUID = -1474695966593631014L;

	private String localeName;
	private String fileName;
	private byte[] binary;
	private MetaReportType reportType;

	public MetaLocalizedReport() {
	}

	public MetaLocalizedReport(String localeName, String fileName, byte[] binary, MetaReportType reportType) {
		this.localeName = localeName;
		this.fileName = fileName;
		this.binary = binary;
		this.reportType = reportType;
	}

	public String getLocaleName() {
		return localeName;
	}

	public void setLocaleName(String localeName) {
		this.localeName = localeName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getBinary() {
		return binary;
	}

	public void setBinary(byte[] binary) {
		this.binary = binary;
	}

	public MetaReportType getReportType() {
		return reportType;
	}

	public void setReportType(MetaReportType reportType) {
		this.reportType = reportType;
	}

	@Override
	public MetaLocalizedReport copy() {
		return ObjectUtil.deepCopy(this);
	}

	// Definition → Meta
	public void applyConfig(LocalizedReportDefinition definition) {
		this.localeName = definition.getLocaleName();
		this.fileName = definition.getFileName();
		this.binary = definition.getBinary();

		if (definition.getReportType() != null) {
			ReportType reportType = definition.getReportType();
			if (reportType instanceof JasperReportType) {
				MetaJasperReportType jrt = new MetaJasperReportType();
				jrt.applyConfig(reportType);
				this.reportType = jrt;
			} else if(reportType instanceof PoiReportType) {
				MetaPoiReportType prt = new MetaPoiReportType();
				prt.applyConfig(reportType);
				this.reportType = prt;
			}
		} else {
			this.reportType = null;
		}
	}

	// Meta → Definition
	public LocalizedReportDefinition currentConfig() {
		LocalizedReportDefinition definition = new LocalizedReportDefinition();
		definition.setLocaleName(getLocaleName());
		definition.setFileName(getFileName());
		definition.setBinary(getBinary());
		if(reportType != null){
			definition.setReportType(reportType.currentConfig());
		}
		return definition;
	}


}
