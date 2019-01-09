/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.webapi;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Variant;

import org.iplass.mtp.command.CommandRuntimeException;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.interceptor.CommandInterceptor;
import org.iplass.mtp.impl.command.InterceptorService;
import org.iplass.mtp.impl.command.MetaCommand;
import org.iplass.mtp.impl.command.MetaCommand.CommandRuntime;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.definition.DefinableMetaData;
import org.iplass.mtp.impl.metadata.BaseMetaDataRuntime;
import org.iplass.mtp.impl.metadata.BaseRootMetaData;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.script.GroovyScriptEngine;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.impl.script.ScriptRuntimeException;
import org.iplass.mtp.impl.script.template.GroovyTemplate;
import org.iplass.mtp.impl.script.template.GroovyTemplateCompiler;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.webapi.WebApiRuntimeException;
import org.iplass.mtp.webapi.definition.RequestType;
import org.iplass.mtp.webapi.definition.CacheControlType;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.StateType;
import org.iplass.mtp.webapi.definition.WebApiDefinition;


public class MetaWebApi extends BaseRootMetaData implements DefinableMetaData<WebApiDefinition> {
	private static final long serialVersionUID = 2590900624234333139L;

//	private static final String X_REQUESTED_WITH = "X-Requested-With";
//	private static final String XML_HTTP_REQUEST = "XMLHttpRequest";
//
	public static final String HEADER_ACCEPT = "Accept";
	public static final String COMMAND_INTERCEPTOR_NAME = "webApi";

	/** このWebAPIが呼び出されたときに実行するCommand。 */
	private MetaCommand command;

	private String[] results;

	private RequestType[] accepts;
	private MethodType[] methods;
	private StateType state = StateType.STATEFUL;
	private boolean supportBearerToken;

	private CacheControlType cacheControlType;
	private long cacheControlMaxAge = -1;

	private String restJsonParameterName = null;
	private Class<?> restJsonParameterType = void.class;
	private String restXmlParameterName = null;

	/** このWebAPIで処理されるCommandを特権（セキュリティ制約を受けない）にて処理するかどうか。デフォルトはfalse。 */
	private boolean isPrivilaged = false;

	/** このWebAPIの呼び出しをセキュリティ設定によらず呼び出し可能にする場合は、trueを設定。 isPrivilagedとの違いは、Entityの操作などにおいては、セキュリティ制約を受ける。デフォルトはfalse。*/
	private boolean isPublicWebApi;

	/** Tokenチェックの実行設定。未指定可(Tokenチェックは実行されない)。 */
	private MetaWebApiTokenCheck tokenCheck;

	/** XMLHttpRequestがセットされていることを確認するか。 */
	private boolean isCheckXRequestedWithHeader = true;

	/** sessionにて同期を行うか否か */
	private boolean synchronizeOnSession;

	private String responseType;

	private String accessControlAllowOrigin;

	private boolean accessControlAllowCredentials;

	//TODO Access-Control-Max-Age

	//TODO paramMap（pathParam）

	private boolean needTrustedAuthenticate;

	public boolean isSupportBearerToken() {
		return supportBearerToken;
	}

	public void setSupportBearerToken(boolean supportBearerToken) {
		this.supportBearerToken = supportBearerToken;
	}

	public StateType getState() {
		return state;
	}

	/**
	 * WebAPIをStatelessとして呼び出すか否かを設定します。
	 * デフォルトは、StateType.STATEFULです。
	 *
	 * @param state
	 */
	public void setState(StateType state) {
		this.state = state;
	}

	public boolean isNeedTrustedAuthenticate() {
		return needTrustedAuthenticate;
	}

	public void setNeedTrustedAuthenticate(boolean needTrustedAuthenticate) {
		this.needTrustedAuthenticate = needTrustedAuthenticate;
	}

	public String getAccessControlAllowOrigin() {
		return accessControlAllowOrigin;
	}

	public void setAccessControlAllowOrigin(String accessControlAllowOrigin) {
		this.accessControlAllowOrigin = accessControlAllowOrigin;
	}

	public boolean isAccessControlAllowCredentials() {
		return accessControlAllowCredentials;
	}

	public void setAccessControlAllowCredentials(boolean accessControlAllowCredentials) {
		this.accessControlAllowCredentials = accessControlAllowCredentials;
	}

	public String getResponseType() {
		return responseType;
	}

	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}

	public boolean isSynchronizeOnSession() {
		return synchronizeOnSession;
	}

	public void setSynchronizeOnSession(boolean synchronizeOnSession) {
		this.synchronizeOnSession = synchronizeOnSession;
	}


	public boolean isCheckXRequestedWithHeader() {
		return isCheckXRequestedWithHeader;
	}

	public void setCheckXRequestedWithHeader(boolean isCheckXRequestedWithHeader) {
		this.isCheckXRequestedWithHeader = isCheckXRequestedWithHeader;
	}

	public MetaWebApiTokenCheck getTokenCheck() {
		return tokenCheck;
	}

	public void setTokenCheck(MetaWebApiTokenCheck tokenCheck) {
		this.tokenCheck = tokenCheck;
	}

	public boolean isPrivilaged() {
		return isPrivilaged;
	}

	public void setPrivilaged(boolean isPrivilaged) {
		this.isPrivilaged = isPrivilaged;
	}

	public boolean isPublicWebApi() {
		return isPublicWebApi;
	}

	public void setPublicWebApi(boolean isPublicWebApi) {
		this.isPublicWebApi = isPublicWebApi;
	}

	@Override
	public WebApiRuntime createRuntime(MetaDataConfig metaDataConfig) {
		return new WebApiRuntime();
	}

	@Override
	public MetaWebApi copy() {
		return ObjectUtil.deepCopy(this);
	}

	public MetaCommand getCommand() {
		return command;
	}

	public void setCommand(MetaCommand command) {
		this.command = command;
	}

	public void setResults(String[] results) {
		this.results = results;
	}

	public String[] getResults() {
		return this.results;
	}


	public RequestType[] getAccepts() {
		return accepts;
	}

	public void setAccepts(RequestType[] accepts) {
		this.accepts = accepts;
	}

	public MethodType[] getMethods() {
		return methods;
	}

	public void setMethods(MethodType[] methods) {
		this.methods = methods;
	}

	public String getRestJsonParameterName() {
		return restJsonParameterName;
	}

	public void setRestJsonParameterName(String restJsonParameterName) {
		this.restJsonParameterName = restJsonParameterName;
	}

	public Class<?> getRestJsonParameterType() {
		return restJsonParameterType;
	}

	public void setRestJsonParameterType(Class<?> restJsonParameterType) {
		this.restJsonParameterType = restJsonParameterType;
	}

	public String getRestXmlParameterName() {
		return restXmlParameterName;
	}

	public void setRestXmlParameterName(String restXmlParameterName) {
		this.restXmlParameterName = restXmlParameterName;
	}

	public CacheControlType getCacheControlType() {
		return cacheControlType;
	}

	public void setCacheControlType(CacheControlType cacheControlType) {
		this.cacheControlType = cacheControlType;
	}

	public long getCacheControlMaxAge() {
		return cacheControlMaxAge;
	}

	public void setCacheControlMaxAge(long cacheControlMaxAge) {
		this.cacheControlMaxAge = cacheControlMaxAge;
	}

	public class WebApiRuntime extends BaseMetaDataRuntime {
		private CommandRuntime cmd;

		private GroovyTemplate accessControlAllowOriginTemplate;
		private List<Variant> variants;

		private WebApiService service = ServiceRegistry.getRegistry().getService(WebApiService.class);
		private InterceptorService is = ServiceRegistry.getRegistry().getService(InterceptorService.class);

		private MethodType specificMethod;
		private String parentName;

		public WebApiRuntime() {

			try {
				if (command != null) {
					cmd = command.createRuntime();
				}

//			if (paramMap != null) {
//				mapFromTo = new HashMap<String, String>();
//				mapToFrom = new HashMap<String, String>();
//				ArrayList<ParamMap> variables = new ArrayList<ParamMap>();
//				for (ParamMap p: paramMap) {
//					mapFromTo.put(p.getMapFrom(), p.getName());
//					mapToFrom.put(p.getName(), p.getMapFrom());
//					if (p.getMapFrom().startsWith("{") && p.getMapFrom().endsWith("}")) {
//						variables.add(p);
//					}
//				}
//				if (variables.size() > 0) {
//					variableParamMap = variables.toArray(new ParamMap[variables.size()]);
//				}
//			}

				ScriptEngine se = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();
				if (accessControlAllowOrigin != null && accessControlAllowOrigin.length() != 0) {
					accessControlAllowOriginTemplate = GroovyTemplateCompiler.compile(accessControlAllowOrigin, "AccessControlAllowOriginTemplate" + getName(), (GroovyScriptEngine) se);
				}

				//variants
				if (responseType != null && responseType.length() != 0) {
					String[] rtList = responseType.split(",");
					MediaType[] mtList = new MediaType[rtList.length];
					for (int i = 0; i < rtList.length; i++) {
						mtList[i] = MediaType.valueOf(rtList[i].trim());
					}
					variants = Variant.mediaTypes(mtList).add().build();
				} else {
					variants = Variant.mediaTypes(MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE).add().build();
				}

				//method
				for (MethodType mt: MethodType.values()) {
					if (name.endsWith(mt.toString())) {
						specificMethod = mt;
						parentName = name.substring(0, name.length() - mt.toString().length() - 1);
						break;
					}
				}

			} catch (RuntimeException e) {
				setIllegalStateException(e);
			}
		}

		public String getPublicWebApiName() {
			if (specificMethod != null) {
				return parentName;
			} else {
				return name;
			}
		}

		public WebApiRuntime getComprehensiveRuntime() {
			if (specificMethod == null) {
				return this;
			}
			return service.getRuntimeByName(parentName);
		}

		public List<WebApiRuntime> getIndividualRuntime() {
			List<WebApiRuntime> rl = new ArrayList<MetaWebApi.WebApiRuntime>(4);
			for (MethodType mt: MethodType.values()) {
				WebApiRuntime r = service.getRuntimeByName(parentName + "/" + mt.toString());
				if (r != null) {
					rl.add(r);
				}
			}
			return rl;
		}

		public MethodType getSpecificMethod() {
			return specificMethod;
		}

		public List<Variant> getVariants() {
			return variants;
		}

		public String getAccessControlAllowOrigin(RequestContext req) {
			if (accessControlAllowOriginTemplate != null) {
				StringWriter sw = new StringWriter();
				WebApiGroovyTemplateBinding gtb = new WebApiGroovyTemplateBinding(sw, req);
				try {
					accessControlAllowOriginTemplate.doTemplate(gtb);
				} catch (IOException e) {
					throw new ScriptRuntimeException(e);
				}
				String acao = sw.toString().trim();
				if (acao.length() == 0 || "null".equals(acao)) {
					return null;
				} else {
					return acao;
				}
			}

			return null;
		}

		public boolean isCorsAllowCredentials() {
			if (accessControlAllowOriginTemplate == null) {
				if (service.getCors() != null) {
					return service.getCors().isAllowCredentials();
				} else {
					return false;
				}
			} else {
				return accessControlAllowCredentials;
			}
		}

		public boolean isCorsAllowOrigin(String origin, RequestContext req) {
			String acao = getAccessControlAllowOrigin(req);
			if (acao == null) {
				if (service.getCors() != null && service.getCors().getAllowOrigin() != null) {
					for (String s: service.getCors().getAllowOrigin()) {
						if (corsAllowOrign(origin, s)) {
							return true;
						}
					}
				}

				return false;
			} else {
				return corsAllowOrign(origin, acao);
			}

		}

		private boolean corsAllowOrign(String origin, String accessControlAllowOrigin) {
			if (accessControlAllowOrigin != null) {
				if (accessControlAllowOrigin.indexOf(' ') >= 0) {
					String[] accessControlAllowOriginArray = StringUtil.split(accessControlAllowOrigin, ' ');
					for (String s: accessControlAllowOriginArray) {
						if ("*".equals(s) || origin.equals(s)) {
							return true;
						}
					}
				} else {
					if ("*".equals(accessControlAllowOrigin) || origin.equals(accessControlAllowOrigin)) {
						return true;
					}
				}
			}
			return false;
		}

		public MetaWebApi getMetaData() {
			return MetaWebApi.this;
		}

		public CommandRuntime getCommandRuntime() {
			return cmd;
		}

		public String executeCommand(final RequestContext req, String interceptorName) {
			checkState();
			CommandInterceptor[] cmdInterceptors = is.getInterceptors(interceptorName);


			if (synchronizeOnSession) {
				synchronized (req.getSession()) {
					return new WebApiInvocationImpl(cmdInterceptors, this, req).proceedCommand();
				}
			} else {
				return new WebApiInvocationImpl(cmdInterceptors, this, req).proceedCommand();
			}
		}

		public void checkXRequestedWith(HttpServletRequest request) {
			//prevent JSON hijack and CSRF
			if (isCheckXRequestedWithHeader) {
				for (Map.Entry<String, String> e: service.getXRequestedWithMap().entrySet()) {
					String xRequestedWith = request.getHeader(e.getKey());
					if (xRequestedWith != null && xRequestedWith.equals(e.getValue())) {
						return;
					}
				}

				throw new WebApiRuntimeException("X-Requested-With Header( or Custom Header) is needed on WebApi:" + name);
			}
		}

		public void checkRequestType(RequestType requestAcceptType, HttpServletRequest request) {

//			// JSONPの時のみ利用可能チェック
//			if (requestAcceptType == AcceptType.REST_JSON) {
//				String accept = request.getHeader(HEADER_ACCEPT);
//				if (accept != null && accept.equals(JSONP_MEDIA_TYPE)) {
//					if (!isPermittedJsonp()) {
//						throw new WebAPIException("Unsupported request(jsonp request not allowed).");
//					}
//				}
//			}

			if (accepts != null) {
				for (RequestType a: accepts) {
					if (a == requestAcceptType) {
						return;
					}
				}
			}
			throw new WebApplicationException(Status.UNSUPPORTED_MEDIA_TYPE);
//			throw new WebAPIException("Unsupported request(requested Content-Type not supported)");
		}

		public void checkMethodType(MethodType requestMethodType) {

			if (methods == null || methods.length == 0) {
				return;
			}
			for (MethodType m: methods) {
				if (m == requestMethodType) {
					return;
				}
			}
			throw new WebApplicationException(Status.METHOD_NOT_ALLOWED);
//			throw new WebAPIException("Unsupported request(requested method type not allowed)");
		}
	}

	// Meta → Definition
	public WebApiDefinition currentConfig() {
		WebApiDefinition definition = new WebApiDefinition();

		definition.setName(name);
		definition.setDisplayName(displayName);
		definition.setDescription(description);

		if (command != null) {
			definition.setCommandConfig(command.currentConfig());
		}

		if (accepts != null) {
			definition.setAccepts(Arrays.copyOf(accepts, accepts.length));
		}
		if (methods != null) {
			definition.setMethods(Arrays.copyOf(methods, methods.length));
		}
		definition.setState(state);
		definition.setSupportBearerToken(supportBearerToken);

		definition.setCacheControlType(cacheControlType);
		definition.setCacheControlMaxAge(cacheControlMaxAge);

		definition.setPrivilaged(isPrivilaged);
		definition.setPublicWebApi(isPublicWebApi);
		definition.setCheckXRequestedWithHeader(isCheckXRequestedWithHeader);

		if (results != null) {
			definition.setResults(Arrays.copyOf(results, results.length));
		}

		definition.setRestJsonParameterName(restJsonParameterName);
		definition.setRestXmlParameterName(restXmlParameterName);

		if (!(restJsonParameterType == null || restJsonParameterType == void.class)) {
			definition.setRestJsonParameterType(restJsonParameterType.getName());
		}

		if (tokenCheck != null) {
			definition.setTokenCheck(tokenCheck.currentConfig());
		}

		definition.setSynchronizeOnSession(synchronizeOnSession);
		definition.setResponseType(responseType);

		definition.setAccessControlAllowOrigin(accessControlAllowOrigin);
		definition.setAccessControlAllowCredentials(accessControlAllowCredentials);
		definition.setNeedTrustedAuthenticate(needTrustedAuthenticate);

		return definition;
	}

	// Definition → Meta
	public void applyConfig(WebApiDefinition definition) {
		name = definition.getName();
		displayName = definition.getDisplayName();
		description = definition.getDescription();

		if (definition.getResults() != null) {
			results = Arrays.copyOf(definition.getResults(), definition.getResults().length);
		} else {
			results = null;
		}

		isPrivilaged = definition.isPrivilaged();
		isPublicWebApi = definition.isPublicWebApi();
		isCheckXRequestedWithHeader = definition.isCheckXRequestedWithHeader();

		if (definition.getAccepts() != null) {
			accepts = Arrays.copyOf(definition.getAccepts(), definition.getAccepts().length);
		} else {
			accepts = null;
		}
		if (definition.getMethods() != null) {
			methods = Arrays.copyOf(definition.getMethods(), definition.getMethods().length);
		} else {
			methods = null;
		}
		state = definition.getState();
		supportBearerToken = definition.isSupportBearerToken();

		cacheControlType = definition.getCacheControlType();
		cacheControlMaxAge = definition.getCacheControlMaxAge();

		if (definition.getCommandConfig() != null) {
			command = MetaCommand.createInstance(definition.getCommandConfig());
			command.applyConfig(definition.getCommandConfig());
		} else {
			command = null;
		}

		restJsonParameterName = definition.getRestJsonParameterName();
		restXmlParameterName = definition.getRestXmlParameterName();

		try {
			if (definition.getRestJsonParameterType() != null) {
				restJsonParameterType = Class.forName(definition.getRestJsonParameterType());
			} else {
				restJsonParameterType = null;
			}
		} catch (ClassNotFoundException e) {
			throw new CommandRuntimeException(definition.getRestJsonParameterType() + " class not found.", e);
		}

		if (definition.getTokenCheck() != null) {
			tokenCheck = new MetaWebApiTokenCheck();
			tokenCheck.applyConfig(definition.getTokenCheck());
		} else {
			tokenCheck = null;
		}

		synchronizeOnSession = definition.isSynchronizeOnSession();
		responseType = definition.getResponseType();

		accessControlAllowOrigin = definition.getAccessControlAllowOrigin();
		accessControlAllowCredentials = definition.isAccessControlAllowCredentials();
		needTrustedAuthenticate = definition.isNeedTrustedAuthenticate();
	}
}
