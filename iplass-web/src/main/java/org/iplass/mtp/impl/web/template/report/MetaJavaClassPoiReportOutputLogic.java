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

import org.apache.poi.ss.usermodel.Workbook;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.web.template.TemplateRuntimeException;
import org.iplass.mtp.web.template.report.PoiReportOutputLogic;
import org.iplass.mtp.web.template.report.definition.JavaClassReportOutputLogicDefinition;
import org.iplass.mtp.web.template.report.definition.ReportOutputLogicDefinition;

public class MetaJavaClassPoiReportOutputLogic extends MetaPoiReportOutputLogic {
	private static final long serialVersionUID = 817043706754815712L;
	
	private String className;
	
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public void applyConfig(ReportOutputLogicDefinition def) {
		JavaClassReportOutputLogicDefinition d = (JavaClassReportOutputLogicDefinition) def;
		className = d.getClassName();
	}

	@Override
	public MetaPoiReportOutputLogic copy() {
		MetaJavaClassPoiReportOutputLogic copy = new MetaJavaClassPoiReportOutputLogic();
		copy.className = className;
		return copy;
	}

	@Override
	public ReportOutputLogicDefinition currentConfig() {
		JavaClassReportOutputLogicDefinition d = new JavaClassReportOutputLogicDefinition();
		fillTo(d);
		d.setClassName(className);
		return d;
	}

	@Override
	public JavaClassReportPoiOutputLogicRuntime createRuntime(MetaReportType reportType) {
		return new JavaClassReportPoiOutputLogicRuntime();
	}
	
	
	
	public class JavaClassReportPoiOutputLogicRuntime extends PoiReportOutputLogicRuntime {
		
		private PoiReportOutputLogic logic;
		
		public JavaClassReportPoiOutputLogicRuntime() {
			try {
				logic = (PoiReportOutputLogic) Class.forName(className).newInstance();
			} catch (InstantiationException e) {
				throw new TemplateRuntimeException("can not instantiate " + className, e);
			} catch (IllegalAccessException e) {
				throw new TemplateRuntimeException("can not instantiate " + className, e);
			} catch (ClassNotFoundException e) {
				throw new TemplateRuntimeException("class not found:" + className, e);
			}
		}

		@Override
		public void outputReport(RequestContext context, Workbook book) {
			logic.reportOutput(context, book);
		}

		public MetaJavaClassPoiReportOutputLogic getMetaData() {
			return MetaJavaClassPoiReportOutputLogic.this;
		}
	}

}
