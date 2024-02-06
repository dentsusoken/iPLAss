/*
 * Copyright (C) 2019 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.http;

import java.util.function.Predicate;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.AbstractExecutionAwareRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleHttpInvoker {
	private static Logger logger = LoggerFactory.getLogger(SimpleHttpInvoker.class);
	
	private CloseableHttpClient httpClient;
	private ExponentialBackoff exponentialBackoff;
	
	public SimpleHttpInvoker(CloseableHttpClient httpClient, ExponentialBackoff exponentialBackoff) {
		this.httpClient = httpClient;
		if (exponentialBackoff == null) {
			this.exponentialBackoff = ExponentialBackoff.NO_RETRY;
		} else {
			this.exponentialBackoff = exponentialBackoff;
		}
	}
	
	private final boolean noRetry(final Response res) {
		return true;
	}
	
	public Response call(HttpUriRequest request) {
		return call(request, this::noRetry);
	}
	
	public Response call(HttpUriRequest request, Predicate<Response> stopRetryCondition) {
		Response response = new Response();
		
		long start = System.currentTimeMillis();
		try {
			exponentialBackoff.execute(() -> {
				try (CloseableHttpResponse res = httpClient.execute(request)) {
					response.status = res.getStatusLine().getStatusCode();
					HttpEntity entity = res.getEntity();
					if (entity == null) {
						response.content = null;
					} else {
						response.content = EntityUtils.toString(entity, "UTF-8");
					}
				} catch (Exception e) {
					if (response.exception != null) {
						e.addSuppressed(response.exception);
					}
					response.exception = e;
					response.status = 0;
					response.content = null;
					if (logger.isDebugEnabled()) {
						logger.debug("error while http call:" + request.toString(), e);
					}
				} finally {
					if (request instanceof AbstractExecutionAwareRequest) {
						((AbstractExecutionAwareRequest) request).reset();
					}
				}
				
				return stopRetryCondition.test(response);
			});
			
		} catch (InterruptedException e) {
			if (response.exception != null) {
				e.addSuppressed(response.exception);
			}
			response.exception = e;
			response.status = 0;
			response.content = null;
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("call external web resource: " + request + " " + (System.currentTimeMillis() - start) + "ms");
		}
		
		return response;
	}
	
	public static class Response {
		public String content;
		public int status;
		public Exception exception;
	}

}
