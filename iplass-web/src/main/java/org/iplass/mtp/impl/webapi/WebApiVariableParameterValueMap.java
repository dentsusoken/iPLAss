/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.webapi;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.impl.web.ParameterValueMap;
import org.iplass.mtp.impl.web.RequestPath;
import org.iplass.mtp.impl.webapi.MetaWebApi.WebApiRuntime;
import org.iplass.mtp.impl.webapi.MetaWebApiParamMap.WebApiParamMapRuntime;


public class WebApiVariableParameterValueMap implements ParameterValueMap {

	private ParameterValueMap wrapped;
	private WebApiRuntime webApi;
	private RequestPath reqPath;
	
	private boolean noSubPath;
	private String subPath;
	private String rawSubPath;//encoded path
	private String[] fullPaths;
	private String[] subPaths;
	
	private Map<String, Object> paramMap;

	public WebApiVariableParameterValueMap(ParameterValueMap wrapped, RequestPath reqPath, WebApiRuntime webApi) {
		this.reqPath = reqPath;
		this.wrapped = wrapped;
		this.webApi = webApi;
	}
	
	ParameterValueMap getWrapped() {
		return wrapped;
	}
	
	String getSubPath() {
		initRawSubPath();
		
		if (noSubPath) {
			return null;
		}
		
		if (subPath == null) {
			subPath = decodePath(rawSubPath);
		}
		
		return subPath;
	}
	
	private String decodePath(String path) {
		try {
			//pathは+は空白にしない
			return URLDecoder.decode(path.replace("+", "%2B"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	private void initRawSubPath() {
		if (noSubPath == false && rawSubPath == null) {
			String path = reqPath.getTargetPath(true);
			String webApiName = webApi.getMetaData().getName();
			if (path.length() > webApiName.length()) {
				rawSubPath = path.substring(webApiName.length() + 1);
			} else {
				noSubPath = true;
			}
		}
	}

	String[] getFullPaths() {
		if (fullPaths == null) {
			String path = reqPath.getTargetPath(true);
			fullPaths = path.split("/");
			for (int i = 0; i < fullPaths.length; i++) {
				fullPaths[i] = decodePath(fullPaths[i]);
			}
		}
		return fullPaths;
	}

	String[] getSubPaths() {
		initRawSubPath();

		if (subPaths == null) {
			String sp = rawSubPath;
			if (sp == null) {
				subPaths = new String[0];
			} else {
				subPaths = sp.split("/");
				for (int i = 0; i < subPaths.length; i++) {
					subPaths[i] = decodePath(subPaths[i]);
				}
			}
		}
		return subPaths;
	}
	

	@Override
	public void cleanTempResource() {
		wrapped.cleanTempResource();
	}

	@Override
	public Object getParam(String name) {
		Map<String, List<WebApiParamMapRuntime>> map = webApi.getWebApiParamMapRuntimes();
		if (map != null) {
			List<WebApiParamMapRuntime> pmrList = map.get(name);
			if (pmrList != null) {
				for (WebApiParamMapRuntime pmr: pmrList) {
					if (pmr.isTarget(this)) {
						return pmr.getParam(this);
					}
				}
			}
		}
		
		return wrapped.getParam(name);
	}
	
	@Override
	public Object[] getParams(String name) {
		Map<String, List<WebApiParamMapRuntime>> map = webApi.getWebApiParamMapRuntimes();
		if (map != null) {
			List<WebApiParamMapRuntime> pmrList = map.get(name);
			if (pmrList != null) {
				for (WebApiParamMapRuntime pmr: pmrList) {
					if (pmr.isTarget(this)) {
						return pmr.getParams(this);
					}
				}
			}
		}
		
		return wrapped.getParams(name);
	}

	@Override
	public Map<String, Object> getParamMap() {
		if (paramMap == null) {
			Map<String, List<WebApiParamMapRuntime>> map = webApi.getWebApiParamMapRuntimes();
			if (map == null) {
				paramMap = wrapped.getParamMap();
			} else {
				paramMap = new HashMap<String, Object>(wrapped.getParamMap());
				for (Map.Entry<String, List<WebApiParamMapRuntime>> e: map.entrySet()) {
					for (WebApiParamMapRuntime pmr: e.getValue()) {
						if (pmr.isTarget(this)) {
							Object[] vals = pmr.getParams(this);
							if (vals != null) {
								paramMap.put(e.getKey(), vals);
							}
							break;
						}
					}
				}
			}
		}
		
		return paramMap;
	}

	@Override
	public Iterator<String> getParamNames() {
		return getParamMap().keySet().iterator();
	}
	
	public boolean hasParamMapDefs() {
		return webApi.getWebApiParamMapRuntimes() != null;
	}

}
