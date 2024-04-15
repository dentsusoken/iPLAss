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
package org.iplass.mtp.impl.webapi.command.definition;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;

import org.iplass.mtp.SystemException;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.auth.NoPermissionException;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.RestJson;
import org.iplass.mtp.command.annotation.webapi.RestXml;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.definition.Definition;
import org.iplass.mtp.definition.DefinitionModifyResult;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.impl.definition.DefinitionPath;
import org.iplass.mtp.impl.definition.DefinitionService;
import org.iplass.mtp.impl.webapi.WebApiService;
import org.iplass.mtp.impl.webapi.command.Constants;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.webapi.WebApiRequestConstants;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.RequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebApi(name="mtp/definition/GET",
		accepts={RequestType.REST_FORM},
		methods={MethodType.GET},
		results={DefinitionCommand.RESULT_DEFINITION, DefinitionCommand.RESULT_DEFINITION_LIST},
		supportBearerToken = true,
		overwritable=false)
@WebApi(name="mtp/definition/POST",
		accepts={RequestType.REST_JSON, RequestType.REST_XML},
		methods={MethodType.POST},
		restJson=@RestJson(parameterName=DefinitionCommand.PARAM_DEFINITION, parameterType=Definition.class),
		restXml=@RestXml(parameterName=DefinitionCommand.PARAM_DEFINITION),
		supportBearerToken = true,
		overwritable=false)
@WebApi(name="mtp/definition/PUT",
		accepts={RequestType.REST_JSON, RequestType.REST_XML},
		methods={MethodType.PUT},
		restJson=@RestJson(parameterName=DefinitionCommand.PARAM_DEFINITION, parameterType=Definition.class),
		restXml=@RestXml(parameterName=DefinitionCommand.PARAM_DEFINITION),
		supportBearerToken = true,
		overwritable=false)
@WebApi(name="mtp/definition/DELETE",
		accepts={RequestType.REST_FORM},
		methods={MethodType.DELETE},
		supportBearerToken = true,
		overwritable=false)
@CommandClass(name="mtp/definition/DefinitionCommand", displayName="Definition Web API", overwritable=false)
public final class DefinitionCommand implements Command, Constants {
	
	private static final Logger logger = LoggerFactory.getLogger(DefinitionCommand.class);

	public static final String PARAM_DEFINITION = "definition";
	public static final String PARAM_RECURSIVE = "recursive";
	
	public static final String RESULT_DEFINITION_LIST = "list";
	public static final String RESULT_DEFINITION = "definition";
	
	DefinitionService service = DefinitionService.getInstance();
	WebApiService webApiService = ServiceRegistry.getRegistry().getService(WebApiService.class);

	@Override
	public String execute(RequestContext request) {
		if (!webApiService.isEnableDefinitionApi()) {
			logger.warn("definition web api is disabled on WebApiService configration.");
			throw new WebApplicationException(Status.NOT_FOUND);
		}
		
		//require User is admin
		if (!AuthContext.getCurrentContext().getUser().isAdmin()){
			throw new NoPermissionException("this user have no admin permission");
		}
		
		MethodType method = (MethodType) request.getAttribute(WebApiRequestConstants.HTTP_METHOD);
		
		switch (method) {
		case GET:
			return doGet(request);
		case POST:
			return doPost(request);
		case PUT:
			return doPut(request);
		case DELETE:
			return doDelete(request);
		default:
			throw new WebApplicationException(Status.METHOD_NOT_ALLOWED);
		}
	}

	private String doDelete(RequestContext request) {
		//remove
		DefinitionPath typeAndPath = typeAndPath(request);
		TypedDefinitionManager<? extends Definition> tdm = service.getTypedDefinitionManager(typeAndPath.getType());
		DefinitionModifyResult res = tdm.remove(typeAndPath.getPath());
		if (res.isSuccess()) {
			return CMD_EXEC_SUCCESS;
		} else {
			throw new SystemException(res.getMessage());
		}
	}

	private String doPut(RequestContext request) {
		//update
		DefinitionPath typeAndPath = typeAndPath(request);
		@SuppressWarnings("unchecked")
		TypedDefinitionManager<Definition> tdm = (TypedDefinitionManager<Definition>) service.getTypedDefinitionManager(typeAndPath.getType());
		
		Definition def = (Definition) request.getAttribute(PARAM_DEFINITION);
		if (def == null) {
			throw new IllegalArgumentException("definition must specify by put body content.");
		}
		if (typeAndPath.getClass().isAssignableFrom(def.getClass())) {
			throw new IllegalArgumentException("definition type unmatch. expect:" + typeAndPath.getClass().getName() + " actual:" + def.getClass().getName());
		}
		
		DefinitionModifyResult res = tdm.update(def);
		if (res.isSuccess()) {
			return CMD_EXEC_SUCCESS;
		} else {
			throw new SystemException(res.getMessage());
		}
	}

	private String doPost(RequestContext request) {
		//create
		DefinitionPath typeAndPath = typeAndPath(request);
		@SuppressWarnings("unchecked")
		TypedDefinitionManager<Definition> tdm = (TypedDefinitionManager<Definition>) service.getTypedDefinitionManager(typeAndPath.getType());
		
		Definition def = (Definition) request.getAttribute(PARAM_DEFINITION);
		if (def == null) {
			throw new IllegalArgumentException("definition must specify by post body content.");
		}
		if (typeAndPath.getClass().isAssignableFrom(def.getClass())) {
			throw new IllegalArgumentException("definition type unmatch. expect:" + typeAndPath.getClass().getName() + " actual:" + def.getClass().getName());
		}
		
		DefinitionModifyResult res = tdm.create(def);
		if (res.isSuccess()) {
			return CMD_EXEC_SUCCESS;
		} else {
			throw new SystemException(res.getMessage());
		}
	}
	
	private DefinitionPath typeAndPath(RequestContext request) {
		String subPath = (String) request.getAttribute(WebApiRequestConstants.SUB_PATH);
		if (subPath == null || subPath.length() == 0) {
			throw new NullPointerException("definition path must specify.");
		}
		
		DefinitionPath path = service.resolvePath(subPath);
		if (path == null) {
			throw new IllegalArgumentException("illegal definition path:" + subPath);
		}
		return path;
	}
	
	private String doGet(RequestContext request) {
		DefinitionPath typeAndPath = typeAndPath(request);
		TypedDefinitionManager<? extends Definition> tdm = service.getTypedDefinitionManager(typeAndPath.getType());
		String defPath = typeAndPath.getPath();
		if (defPath.length() == 0 || defPath.endsWith("/")) {
			//list
			Boolean recursive = request.getParam(PARAM_RECURSIVE, Boolean.class, false);
			request.setAttribute(RESULT_DEFINITION_LIST, tdm.definitionSummaryList(defPath, recursive));
		} else {
			//definition
			request.setAttribute(RESULT_DEFINITION, tdm.get(defPath));
		}
		
		return CMD_EXEC_SUCCESS;
	}
	
}
