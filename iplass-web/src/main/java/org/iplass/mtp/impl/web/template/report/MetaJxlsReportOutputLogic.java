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
