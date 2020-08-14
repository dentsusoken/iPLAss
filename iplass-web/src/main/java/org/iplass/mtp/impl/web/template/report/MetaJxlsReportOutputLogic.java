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

import javax.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.web.template.report.MtpJxlsHelper;
import org.iplass.mtp.web.template.report.definition.ReportOutputLogicDefinition;
import org.jxls.common.Context;
import org.jxls.transform.Transformer;

@XmlSeeAlso({MetaJavaClassJxlsReportOutputLogic.class, MetaGroovyJxlsReportOutputLogic.class})
public abstract class MetaJxlsReportOutputLogic implements MetaData {

	private static final long serialVersionUID = 713508369259766066L;

	protected void fillTo(ReportOutputLogicDefinition def) {
	}

	public abstract MetaJxlsReportOutputLogic copy();
	
	public abstract void applyConfig(ReportOutputLogicDefinition def);
	
	public abstract ReportOutputLogicDefinition currentConfig();
	
	public abstract JxlsReportOutputLogicRuntime createRuntime(MetaReportType reportType);
	
	public abstract class JxlsReportOutputLogicRuntime /*implements MetaDataRuntime*/ {
		
		public abstract void outputReport(Transformer transformer, Context context, MtpJxlsHelper jxlsHelper);
		
		public MetaJxlsReportOutputLogic getMetaData() {
			return MetaJxlsReportOutputLogic.this;
		}
	}

}
