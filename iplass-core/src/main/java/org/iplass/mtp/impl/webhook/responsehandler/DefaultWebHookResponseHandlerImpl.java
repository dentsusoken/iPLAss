/*
 * Copyright (C) 2020 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.webhook.responsehandler;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;
import org.iplass.mtp.webhook.WebHookResponseHandler;
import org.iplass.mtp.impl.webhook.WebHookServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultWebHookResponseHandlerImpl implements WebHookResponseHandler{
	Logger logger;
	@Override
	public void handleResponse(HttpResponse response) {
		String msg = "";
		logger = LoggerFactory.getLogger(WebHookServiceImpl.class);
		msg += getStringStatus(response.getStatusLine());
//		msg += getStringHeader(response.getAllHeaders());
		msg += getStringEntity(response.getEntity());
		logger.debug(msg);
	}
	
	private String getStringStatus(StatusLine statusLine) {
		String result= "Response Code ";
		result += statusLine.getStatusCode();
		result += ":"+statusLine.getReasonPhrase();
		result += ";";
		return result;
	}

	private String getStringHeader(Header[] headers) {
		String result= "Headers: ";
		if (headers==null) {
			result+= "null;";
		} else {
			for (Header hd:headers) {
				result += hd.getName();
				result += hd.getValue();
				result += "|";
			}
			result = result.substring(0, result.length()) +";";
		}
		return result;
	}
	private String getStringEntity(HttpEntity entity) {
		String result = "";
		if (entity == null) {
			
		} else if(entity.getContentType()==null){
			result += "Entity(nullContentType):";
		} else {
			result += "Entity(";
			result += entity.getContentType().getName()==null?"nullTypeName":entity.getContentType().getName();
			result += ":";
			result += entity.getContentType().getValue()==null?"nullTypeName":entity.getContentType().getValue();
			result += "):";
		}
		try {
			result += EntityUtils.toString(entity);
		}catch (Exception e) {
			e.printStackTrace();
		}
		result += ";";
		return result;
	}
}
