/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.base.io.download;

import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gwt.http.client.URL;

public class GetRequestBuilder {

	private static final String QUESTIONMARK = "?";
	private static final String EQUAL_TO = "=";
	private static final String PARAMETER_DELIMITER = "&";
	
	private Map<String, String> params = new LinkedHashMap<String, String>();
	private String baseUrl;
	private String targetUrl;
	
	public GetRequestBuilder baseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
		return this;
	}

	public GetRequestBuilder targetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
		return this;
	}
	
	public GetRequestBuilder parameterWithValue(String name, String value) {
		if(value != null){
			params.put(name, value);
		}
		return this;
	}

	public String toEncodedUrl() {
		StringBuilder url = new StringBuilder();
		if(baseUrl != null) {
			url.append(baseUrl);
		}
		if(targetUrl != null) {
			url.append(targetUrl);
		}
		if(!params.entrySet().isEmpty()) {
			url.append(QUESTIONMARK);
		}
		int size = params.size();
		int count = 0;
		for (Map.Entry<String, String> requestParameter : params.entrySet()) {
			url.append(requestParameter.getKey());
			url.append(EQUAL_TO);
			url.append(requestParameter.getValue());
			if (count < size - 1) {
				url.append(PARAMETER_DELIMITER);
				count++;
			}
		}
		
		return URL.encode(url.toString());
	}
}
