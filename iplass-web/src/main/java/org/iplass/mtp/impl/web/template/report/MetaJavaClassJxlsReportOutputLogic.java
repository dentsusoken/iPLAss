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

import org.iplass.mtp.impl.web.template.TemplateRuntimeException;
import org.iplass.mtp.web.template.report.JxlsReportOutputLogic;
import org.iplass.mtp.web.template.report.definition.JavaClassReportOutputLogicDefinition;
import org.iplass.mtp.web.template.report.definition.ReportOutputLogicDefinition;
import org.jxls.common.Context;
import org.jxls.transform.Transformer;

public class MetaJavaClassJxlsReportOutputLogic extends MetaJxlsReportOutputLogic {

	private static final long serialVersionUID = 3381174410522848182L;

	private String className;
	
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	
	@Override
	public MetaJxlsReportOutputLogic copy() {
		MetaJavaClassJxlsReportOutputLogic copy = new MetaJavaClassJxlsReportOutputLogic();
		copy.className = className;
		return copy;
	}

	@Override
	public void applyConfig(ReportOutputLogicDefinition def) {
		JavaClassReportOutputLogicDefinition d = (JavaClassReportOutputLogicDefinition)def;
		className = d.getClassName();
	}

	@Override
	public ReportOutputLogicDefinition currentConfig() {
		JavaClassReportOutputLogicDefinition d = new JavaClassReportOutputLogicDefinition();
		fillTo(d);
		d.setClassName(className);
		return d;
	}

	@Override
	public JxlsReportOutputLogicRuntime createRuntime(MetaReportType reportType) {
		return new JavaClassReportJxlsOutputLogicRuntime();
	}
	
	public class JavaClassReportJxlsOutputLogicRuntime extends JxlsReportOutputLogicRuntime {
		private JxlsReportOutputLogic outputLogic;
		
		public JavaClassReportJxlsOutputLogicRuntime() {
			try {
				outputLogic = (JxlsReportOutputLogic) Class.forName(className).newInstance();
			} catch (InstantiationException e) {
				throw new TemplateRuntimeException("can not instantiate " + className, e);
			} catch (IllegalAccessException e) {
				throw new TemplateRuntimeException("can not instantiate " + className, e);
			} catch (ClassNotFoundException e) {
				throw new TemplateRuntimeException("class not found:" + className, e);
			}
		}

		public MetaJavaClassJxlsReportOutputLogic getMetaData() {
			return MetaJavaClassJxlsReportOutputLogic.this;
		}

		@Override
		public void outputReport(Transformer transformer, Context context) {
			outputLogic.reportWrite(transformer, context);
		}
	}

}
