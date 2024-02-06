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

package org.iplass.mtp.impl.webapi.rest;

import java.io.IOException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.iplass.mtp.impl.web.RequestPath;
import org.iplass.mtp.impl.webapi.MetaWebApi.WebApiRuntime;
import org.iplass.mtp.impl.webapi.WebApiService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PreMatching
public class MtpContainerRequestFilter implements ContainerRequestFilter {

	private static Logger logger = LoggerFactory.getLogger(MtpContainerRequestFilter.class);
	
	private WebApiService apiservice = ServiceRegistry.getRegistry().getService(WebApiService.class);
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		RequestPath path = new RequestPath(requestContext.getUriInfo().getPath(), (RequestPath) requestContext.getProperty(RequestPath.ATTR_NAME));
		requestContext.setProperty(RequestPath.ATTR_NAME, path);
		String webApiName = path.getTargetPath(true);
		
		WebApiRuntime runtime = apiservice.getByPathHierarchy(webApiName, requestContext.getMethod());
		if (runtime == null) {
			if (logger.isDebugEnabled()) {
				logger.debug(webApiName + " not defined path.");
			}
 			throw new WebApplicationException(Status.NOT_FOUND);
		}
		
		//method,content-type,bodySizeはボディパース前にチェックする
		runtime.checkMethodType(requestContext.getMethod());
		MediaType mt = requestContext.getMediaType();
		runtime.checkContentType(mt);
		if (MediaType.MULTIPART_FORM_DATA_TYPE.isCompatible(mt)) {
			//multipart/form-dataの場合は、直接ServletRequestのInputStreamを扱うので、ここではbodyを消費しないようにする
			if (runtime.getRequestRestriction().maxBodySize() != -1) {
				requestContext.setProperty(RestRequestContext.MAX_BODY_SIZE, runtime.getRequestRestriction().maxBodySize());
			}
		} else {
			if (requestContext.hasEntity()) {
				//bodySize
				if (runtime.getRequestRestriction().maxBodySize() != -1) {
					requestContext.setEntityStream(new LimitRequestBodyInputStream(requestContext.getEntityStream(),
							runtime.getRequestRestriction().maxBodySize(), requestContext.getLength()));
					requestContext.setProperty(RestRequestContext.MAX_BODY_SIZE, runtime.getRequestRestriction().maxBodySize());
				}
			}
		}
		
		requestContext.setProperty(RestRequestContext.WEB_API_RUNTIME_NAME, runtime);
		
	}

}
