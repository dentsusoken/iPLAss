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
package org.iplass.mtp.impl.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.entity.GenericEntity;
import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.impl.web.template.report.MetaReportParamMap;
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
		if (jxlsModel.getParamMap() != null) {
			putVar(request, context, jxlsModel.getParamMap());
		}
		
		String password = null;
		if (StringUtil.isNotEmpty(jxlsModel.getPasswordAttributeName())) {
			password = (String)getAttribute(request, jxlsModel.getPasswordAttributeName());
		}
		
		jxlsModel.write(context, requestStack.getResponse().getOutputStream(), password);
	}

	private void putVar(RequestContext request, Context context, MetaReportParamMap[] paramMap) {
		for(int i=0; i<paramMap.length; i++) {
			Object val = getAttribute(request, paramMap[i].getMapFrom());
			
			if (val == null) {
				break;
			}
			
			if (paramMap[i].isConvertEntityToMap()) {
				//GenericEntityのインスタンスとそのリストは、Map形式に変換してContextにPut
				if (val instanceof GenericEntity) {
					context.putVar(paramMap[i].getName(),  ((GenericEntity) val).toMap());
				} else if (val instanceof List<?>) {
					List<Map<String, Object>> entityMaps = new ArrayList<Map<String,Object>>();
					
					List<?> list = (List<?>)val;
					for (Object obj : list) {
						if (obj instanceof GenericEntity) {
							entityMaps.add(((GenericEntity) obj).toMap());
						}
					}
					context.putVar(paramMap[i].getName(), entityMaps);
				} else {
					context.putVar(paramMap[i].getName(), val);
				}
			} else {
				context.putVar(paramMap[i].getName(), val);
			}
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
