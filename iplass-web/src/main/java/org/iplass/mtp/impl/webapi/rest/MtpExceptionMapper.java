/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.webapi.rest;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.impl.webapi.WebApiResponse;
import org.iplass.mtp.impl.webapi.WebApiService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.webapi.WebApiRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MtpExceptionMapper implements ExceptionMapper<Throwable> {
	private static Logger logger = LoggerFactory.getLogger(MtpExceptionMapper.class);
	private static Logger fatalLogger = LoggerFactory.getLogger("mtp.fatal");
	
	public static final String STATUS_FAILURE = "FAILURE";
	
	private WebApiService waservice = ServiceRegistry.getRegistry().getService(WebApiService.class);

	@Override
	public Response toResponse(Throwable exception) {
		if (exception instanceof WebApplicationException) {
			if (logger.isDebugEnabled()) {
				logger.debug("WebApplicationException on web api call:" + exception, exception);
			}
			return ((WebApplicationException) exception).getResponse();
		}
		
		if (exception instanceof WrappedRestException) {
			WrappedRestException exp = (WrappedRestException) exception;
			WebApiResponse res = exp.getResponse();
			Throwable cause;
			if (res == null) {
				res = new WebApiResponse();
				cause = exp.getCause();
			} else {
				cause = res.getException();
			}
			if (logger.isDebugEnabled()) {
				logger.debug("runtime excepion on web api call:" + cause, cause);
			}
			res.setException(treatException(cause));
			if (res.getStatus() == null) {
				res.setStatus(STATUS_FAILURE);
			}
			return Response.status(mapStatus(cause)).entity(res).build();
		}
		
		//unhandled error
		if (exception instanceof Error) {
			fatalLogger.error(exception.toString(), exception);
		}
		logger.error("unhandle excepion on web api call:" + exception, exception);
		
		return Response.status(mapStatus(exception)).build();
	}

	private Throwable treatException(Throwable e) {
		if (e instanceof ApplicationException) {
			return e;
		} else {
			return new WebApiRuntimeException("System error occurred. See server log detail.");
		}
	}

	protected Response.Status mapStatus(Throwable e) {
		int statusCode = waservice.mapStatus(e);
		return Response.Status.fromStatusCode(statusCode);
	}

}
