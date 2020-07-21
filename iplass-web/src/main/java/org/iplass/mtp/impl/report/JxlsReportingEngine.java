package org.iplass.mtp.impl.report;

import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.impl.web.template.report.MetaJxlsContextParamMap;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.web.template.report.definition.JxlsReportType;
import org.jxls.common.Context;

public class JxlsReportingEngine implements ReportingEngine {

	private String[] supportFiles;
	
	private static final String SESSION_STR = "session";
	private static final String REQUEST_STR = "request";
	private static final String PREFIX_REQUEST = REQUEST_STR +".";
	private static final String PREFIX_SESSION = SESSION_STR +".";
	
	@Override
	public ReportingOutputModel createOutputModel(byte[] binary, String type, String extension) throws Exception {
		return new JxlsReportingOutputModel(binary, type, extension);
	}

	@Override
	public void exportReport(WebRequestStack requestStack, ReportingOutputModel model) throws Exception {
		JxlsReportingOutputModel jxlsModel = (JxlsReportingOutputModel) model;
		
		RequestContext request = requestStack.getRequestContext();

		Context context = new Context();
		if (jxlsModel.getContextParamMap() != null) {
			putVar(request, context, jxlsModel.getContextParamMap());
		}
		
		String password = null;
		if (StringUtil.isNotEmpty(jxlsModel.getPasswordAttributeName())) {
			password = (String)request.getAttribute(jxlsModel.getPasswordAttributeName());
		}
		
		jxlsModel.write(context, requestStack.getResponse().getOutputStream(), password);
	}

	private void putVar(RequestContext request, Context context, MetaJxlsContextParamMap[] contextParamMap) {
		for(int i=0; i<contextParamMap.length; i++) {
			context.putVar(contextParamMap[i].getKey(), getAttribute(request, contextParamMap[i].getMapFrom()));
		}
	}

	private Object getAttribute(RequestContext request, String attributeName) {
		if(attributeName.startsWith(PREFIX_REQUEST)){
			String valueName = attributeName.substring(PREFIX_REQUEST.length());
			return request.getAttribute(valueName);
		}else if(attributeName.startsWith(PREFIX_SESSION)){
			String valueName = attributeName.substring(PREFIX_SESSION.length());
			return request.getSession().getAttribute(valueName);
		} else {
			//Prefix未指定の場合はリクエストから取得
			return request.getAttribute(attributeName);
		}
	}

	@Override
	public boolean isSupport(String type) {
		for(String supportFile : this.supportFiles){
			if(supportFile.equals(type)){
				return true;
			}
		}
		return false;
	}

	@Override
	public String[] getSupportFiles() {
		return supportFiles;
	}
	
	public void setSupportFiles(String[] supportFiles) {
	    this.supportFiles = supportFiles;
	}

	@Override
	public ReportingType getReportingType() {
		ReportingType type = new ReportingType();
		type.setName(JxlsReportType.class.getName());
		type.setDisplayName("JXLS");
		return type;
	}

}
