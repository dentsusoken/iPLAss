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
package org.iplass.mtp.impl.webhook;

import org.iplass.mtp.webhook.WebhookResponseHandler;
import org.iplass.mtp.webhook.WebhookException;
import org.iplass.mtp.webhook.WebhookHeader;
import org.iplass.mtp.webhook.WebhookResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultWebhookResponseHandler implements WebhookResponseHandler{
	private static Logger logger =  LoggerFactory.getLogger(DefaultWebhookResponseHandler.class);
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
	        makeStatusString(response, msg);
	        makeHeaderString(response, msg);
	        makeEntityString(response, msg);
		
			logger.debug(msg.toString());
		}
	}
	
	private void makeStatusString(WebhookResponse whr, StringBuilder msg) {
		msg.append("Response Code ");
		msg.append(whr.getStatusCode());
		msg.append(":");
		msg.append(whr.getReasonPhrase());
		msg.append(";");
	}

	private void makeHeaderString(WebhookResponse response, StringBuilder msg) {
		msg.append("Headers: ");
		if (response.getHeaders()==null) {
			msg.append("null;");
		} else {
			for (WebhookHeader hd:response.getHeaders()) {
				msg.append(hd.getKey());
				msg.append(":");
				msg.append(hd.getValue());
				msg.append("|");
			}
			msg.substring(0, msg.length()-1);
			msg.append(";");
		}
	}
	private void makeEntityString(WebhookResponse whr, StringBuilder msg) {
		if ( whr.getResponseBody() == null) {
			msg.append("Entity(null);");
		} else {
			msg.append("Entity(");
			msg.append(whr.getContentType()==null?"nullTypeName":whr.getContentType());
			msg.append(":");
			msg.append(whr.getContentEncoding()==null?"nullEncodeName":whr.getContentEncoding());
			msg.append("):");
			msg.append(whr.getResponseBody());
		}
		msg.append(";");
	}

}
