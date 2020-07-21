package org.iplass.mtp.web.template.report.definition;

/**
 * <p>JXLS帳票出力用テンプレートファイルのTemplate定義。</p>
 *
 * @author Y.Ishida
 *
 */
public class JxlsReportType extends ReportType {
	
	private static final long serialVersionUID = 4425769299483314155L;
	
	private ReportOutputLogicDefinition reportOutputLogicDefinition;
	
	private JxlsContextParamMapDefinition[] contextParamMap;
	
	private String passwordAttributeName;

	public ReportOutputLogicDefinition getReportOutputLogicDefinition() {
		return reportOutputLogicDefinition;
	}

	public void setReportOutputLogicDefinition(ReportOutputLogicDefinition reportOutputLogicDefinition) {
		this.reportOutputLogicDefinition = reportOutputLogicDefinition;
	}

	public JxlsContextParamMapDefinition[] getContextParamMap() {
		return contextParamMap;
	}

	public void setContextParamMap(JxlsContextParamMapDefinition[] contextParamMap) {
		this.contextParamMap = contextParamMap;
	}

	public String getPasswordAttributeName() {
		return passwordAttributeName;
	}

	public void setPasswordAttributeName(String passwordAttributeName) {
		this.passwordAttributeName = passwordAttributeName;
	}

}
