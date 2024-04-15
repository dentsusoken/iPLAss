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

import org.apache.poi.ss.usermodel.Workbook;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.web.template.report.definition.ReportOutputLogicDefinition;

@XmlSeeAlso({MetaJavaClassPoiReportOutputLogic.class, MetaGroovyPoiReportOutputLogic.class})
public abstract class MetaPoiReportOutputLogic implements MetaData {

	private static final long serialVersionUID = -2738794242654152258L;
	
	protected void fillTo(ReportOutputLogicDefinition def) {
	}

	public abstract MetaPoiReportOutputLogic copy();
	
	public abstract void applyConfig(ReportOutputLogicDefinition def);
	
	public abstract ReportOutputLogicDefinition currentConfig();
	
	public abstract PoiReportOutputLogicRuntime createRuntime(MetaReportType reportType);
	
	public abstract class PoiReportOutputLogicRuntime /*implements MetaDataRuntime*/ {
		
		public abstract void outputReport(RequestContext context, Workbook book);
		
		public MetaPoiReportOutputLogic getMetaData() {
			return MetaPoiReportOutputLogic.this;
		}
	}
}
