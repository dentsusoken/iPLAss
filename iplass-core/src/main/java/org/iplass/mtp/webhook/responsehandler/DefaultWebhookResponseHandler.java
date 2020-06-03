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
package org.iplass.mtp.webhook.responsehandler;

import org.iplass.mtp.webhook.WebhookResponseHandler;
import org.iplass.mtp.webhook.WebhookException;
import org.iplass.mtp.webhook.WebhookHeader;
import org.iplass.mtp.webhook.WebhookResponse;
import org.iplass.mtp.impl.webhook.WebhookServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultWebhookResponseHandler implements WebhookResponseHandler{
	static Logger logger =  LoggerFactory.getLogger(WebhookServiceImpl.class);
	@Override
	public void handleResponse(WebhookResponse response) {
		if (200<=response.getStatusCode()||response.getStatusCode()<300) {
			writeLog(response);	
		} else {
			throw new WebhookException("UnHandled Response Status Code: "+response.getStatusCode()+"("+response.getReasonPhrase()+")");
		}
	}

	private void writeLog(WebhookResponse response) {
		if (logger.isDebugEnabled()) {
	        StringBuilder msg = new StringBuilder(); 
	        msg.append(makeStatusString(response));
	        msg.append(makeHeaderString(response));
	        msg.append(makeEntityString(response));
		
			logger.debug(msg.toString());
		}
	}
	
	private String makeStatusString(WebhookResponse whr) {
		StringBuilder result = new StringBuilder(); 
		result.append("Response Code ");
		result.append(whr.getStatusCode());
		result.append(":");
		result.append(whr.getReasonPhrase());
		result.append(";");
		return result.toString();
	}

	private String makeHeaderString(WebhookResponse response) {
		StringBuilder result = new StringBuilder(); 
		result.append("Headers: ");
		if (response.getHeaders()==null) {
			result.append("null;");
		} else {
			for (WebhookHeader hd:response.getHeaders()) {
				result.append(hd.getKey());
				result.append(":");
				result.append(hd.getValue());
				result.append("|");
			}
			result.substring(0, result.length()-1);
			result.append(";");
		}
		return result.toString();
	}
	private String makeEntityString(WebhookResponse whr) {
		StringBuilder result = new StringBuilder(); 
		if ( whr.getResponseBody() == null) {
			result.append("Entity(null);");
		} else {
			result.append("Entity(");
			result.append(whr.getContentType()==null?"nullTypeName":whr.getContentType());
			result.append(":");
			result.append(whr.getContentEncoding()==null?"nullEncodeName":whr.getContentEncoding());
			result.append("):");
			result.append(whr.getResponseBody());
		}
		result.append(";");
		return result.toString();
	}

}
