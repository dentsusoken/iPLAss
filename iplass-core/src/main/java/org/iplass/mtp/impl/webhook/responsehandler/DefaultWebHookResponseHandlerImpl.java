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

import org.iplass.mtp.webhook.WebHookResponseHandler;
import org.iplass.mtp.webhook.template.definition.WebHookHeader;
import org.iplass.mtp.impl.webhook.WebHookResponse;
import org.iplass.mtp.impl.webhook.WebHookServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultWebHookResponseHandlerImpl implements WebHookResponseHandler{
	@Override
	public void handleResponse(WebHookResponse response) {
		if (200<=response.getStatusCode()||response.getStatusCode()<300) {
			writeLog(response);	
		} else {
			throw new RuntimeException("UnHandled Response Status Code: "+response.getStatusCode()+"("+response.getReasonPhrase()+")");
		}
	}

	private void writeLog(WebHookResponse response) {
		Logger logger =  LoggerFactory.getLogger(WebHookServiceImpl.class);
		String msg = "";
		msg += makeStatusString(response);
		msg += makeHeaderString(response);
		msg += makeEntityString(response);
		logger.debug(msg);
	}
	
	private String makeStatusString(WebHookResponse whr) {
		String result= "Response Code ";
		result += whr.getStatusCode();
		result += ":"+whr.getReasonPhrase();
		result += ";";
		return result;
	}

	private String makeHeaderString(WebHookResponse response) {
		String result= "Headers: ";
		if (response.getHeaders()==null) {
			result+= "null;";
		} else {
			for (WebHookHeader hd:response.getHeaders()) {
				result += hd.getKey()+":";
				result += hd.getValue();
				result += "|";
			}
			result = result.substring(0, result.length()) +";";
		}
		return result;
	}
	private String makeEntityString(WebHookResponse whr) {
		String result = "";
		if ( whr.getResponseBody() == null) {
			result += "Entity(null);";
		} else {
			result += "Entity(";
			result += whr.getContentType()==null?"nullTypeName":whr.getContentType();
			result += ":";
			result += whr.getContentEncoding()==null?"nullEncodeName":whr.getContentEncoding();
			result += "):";
			result += whr.getResponseBody();
		}
		result += ";";
		return result;
	}

}
