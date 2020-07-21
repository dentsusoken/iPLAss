package org.iplass.mtp.impl.web.template.report;

import java.io.InputStream;
import java.io.OutputStream;

import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.web.template.report.JxlsReportOutputLogic;
import org.iplass.mtp.web.template.report.definition.JavaClassReportOutputLogicDefinition;
import org.iplass.mtp.web.template.report.definition.ReportOutputLogicDefinition;
import org.jxls.common.Context;

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
		private JxlsReportOutputLogic writer;
		
		public JavaClassReportJxlsOutputLogicRuntime() {
			try {
				writer = (JxlsReportOutputLogic) Class.forName(className).newInstance();
			} catch (InstantiationException e) {
				throw new EntityRuntimeException("can not instantiate " + className, e);
			} catch (IllegalAccessException e) {
				throw new EntityRuntimeException("can not instantiate " + className, e);
			} catch (ClassNotFoundException e) {
				throw new EntityRuntimeException("class not found:" + className, e);
			}
		}

		@Override
		public void outputReport(InputStream is, OutputStream os, Context context) {
			writer.reportWrite(is, os, context);
		}

		public MetaJavaClassJxlsReportOutputLogic getMetaData() {
			return MetaJavaClassJxlsReportOutputLogic.this;
		}
	}

}
