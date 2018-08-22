/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.cluster.channel.http;

import java.net.HttpURLConnection;
import java.util.concurrent.Callable;

import org.iplass.mtp.impl.cluster.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendMessageTask implements Callable<Void> {
	private static Logger logger = LoggerFactory.getLogger(SendMessageTask.class);
	private static Logger fatalLog = LoggerFactory.getLogger("mtp.fatal.cluster");
	
	private final Message message;
	private final HttpMessageChannel channel;
	private final String url;
	
	private volatile int retryCount;
	
	public SendMessageTask(Message message, String url, int retryCount, HttpMessageChannel channel) {
		this.message = message;
		this.url = url;
		this.channel = channel;
		this.retryCount = retryCount;
	}

	@Override
	public Void call() throws Exception {
		
		int statusCode = -1;
		Exception exp = null;
		try {
			statusCode = channel.doSend(message, url);
		} catch (Exception e) {
			exp = e;
		}
		
		if (statusCode == HttpURLConnection.HTTP_OK) {
			return null;
		}
		
		if (retryCount == 0) {
			//log fatal
			if (exp != null) {
				fatalLog.error("send message failed.url=" + url + ", error=" + exp + ", message=" + message, exp);
			} else {
				fatalLog.error("send message failed.url=" + url + ", http status=" + statusCode + ", " + message);
			}
		} else {
			//log error and retry..
			retryCount = retryCount - 1;
			
			if (exp != null) {
				logger.debug("send message failed.url=" + url + ", remained retry=" + retryCount + ", error=" + exp + ", message=" + message, exp);
			} else {
				logger.debug("send message failed.url=" + url + ", remained retry=" + retryCount + ", http status=" + statusCode + ", " + message);
			}
			channel.doRetry(this);
		}
		return null;
	}
}
