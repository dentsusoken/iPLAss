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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.entity.GenericEntity;
import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.impl.web.template.report.MetaReportParamMap;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.web.template.report.definition.JxlsReportType;

/**
 * Jxls帳票出力エンジン
 */
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

		// NOTE Context は Map オブジェクトに変更
		// Context は内部クラスに変更されている ⇒ https://jxls.sourceforge.net/migration-to-v3-0.html
		// Context is now no longer just seen as a Map holder, but should in the future contain all the information necessary for creating reports. Context is now an internal interface.
		Map<String, Object> reportData = new HashMap<>();
		if (jxlsModel.getParamMap() != null) {
			putVar(request, reportData, jxlsModel.getParamMap());
		}

		String password = null;
		if (StringUtil.isNotEmpty(jxlsModel.getPasswordAttributeName())) {
			password = (String)getAttribute(request, jxlsModel.getPasswordAttributeName());
		}

		jxlsModel.write(reportData, requestStack.getResponse().getOutputStream(), password);
	}

	private void putVar(RequestContext request, Map<String, Object> reportData, MetaReportParamMap[] paramMap) {
		for(int i=0; i<paramMap.length; i++) {
			Object val = getAttribute(request, paramMap[i].getMapFrom());

			if (val == null) {
				break;
			}

			if (paramMap[i].isConvertEntityToMap()) {
				// GenericEntityのインスタンスとそのリストは、Map形式に変換して reportData にPut
				if (val instanceof GenericEntity) {
					reportData.put(paramMap[i].getName(), ((GenericEntity) val).toMap());
				} else if (val instanceof List<?>) {
					List<Map<String, Object>> entityMaps = new ArrayList<Map<String,Object>>();

					List<?> list = (List<?>)val;
					for (Object obj : list) {
						if (obj instanceof GenericEntity) {
							entityMaps.add(((GenericEntity) obj).toMap());
						}
					}
					reportData.put(paramMap[i].getName(), entityMaps);
				} else {
					reportData.put(paramMap[i].getName(), val);
				}
			} else {
				reportData.put(paramMap[i].getName(), val);
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

	/**
	 * サポートファイルを設定する
	 * @param supportFiles サポートファイル
	 */
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
