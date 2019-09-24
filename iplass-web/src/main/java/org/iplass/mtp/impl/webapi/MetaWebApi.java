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
import org.iplass.mtp.impl.web.ParameterValueMap;
import org.iplass.mtp.impl.web.WebFrontendService;
import org.iplass.mtp.impl.web.WebRequestContext;
import org.iplass.mtp.impl.web.fileupload.MultiPartParameterValueMap;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.webapi.WebApiRuntimeException;
import org.iplass.mtp.webapi.definition.RequestType;
import org.iplass.mtp.webapi.definition.CacheControlType;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.StateType;
import org.iplass.mtp.webapi.definition.WebApiDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MetaWebApi extends BaseRootMetaData implements DefinableMetaData<WebApiDefinition> {
	private static final long serialVersionUID = 2590900624234333139L;

	private static Logger logger = LoggerFactory.getLogger(MetaWebApi.class);
	
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
	private String[] oauthScopes;

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

	private String[] allowRequestContentTypes;
	
	private Long maxRequestBodySize;
	private Long maxFileSize;

	public Long getMaxFileSize() {
		return maxFileSize;
	}

	public void setMaxFileSize(Long maxFileSize) {
		this.maxFileSize = maxFileSize;
	}

	public Long getMaxRequestBodySize() {
		return maxRequestBodySize;
	}

	public void setMaxRequestBodySize(Long maxRequestBodySize) {
		this.maxRequestBodySize = maxRequestBodySize;
	}

	public String[] getAllowRequestContentTypes() {
		return allowRequestContentTypes;
	}

	public void setAllowRequestContentTypes(String[] allowRequestContentTypes) {
		this.allowRequestContentTypes = allowRequestContentTypes;
	}
	
	public String[] getOauthScopes() {
		return oauthScopes;
	}

	public void setOauthScopes(String[] oauthScopes) {
		this.oauthScopes = oauthScopes;
	}

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
		
		private List<MediaType> allowedContentTypesRuntime;
		private long maxFileSizeRuntime;

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
				
				//allowRequestContentTypes
				if (allowRequestContentTypes != null && allowRequestContentTypes.length > 0) {
					allowedContentTypesRuntime = new ArrayList<>();
					for (String a: allowRequestContentTypes) {
						allowedContentTypesRuntime.add(MediaType.valueOf(a));
					}
				}
				
				//maxFileSize
				if (maxFileSize == null) {
					WebFrontendService wfs = ServiceRegistry.getRegistry().getService(WebFrontendService.class);
					maxFileSizeRuntime = wfs.getMaxUploadFileSize();
				} else {
					maxFileSizeRuntime = maxFileSize;
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

			if (req instanceof WebRequestContext) {
				WebRequestContext webRequestContext = (WebRequestContext) req;
				ParameterValueMap currentValueMap = webRequestContext.getValueMap();
				//set maxFileSize
				if (currentValueMap instanceof MultiPartParameterValueMap) {
					((MultiPartParameterValueMap) currentValueMap).setMaxFileSize(maxFileSizeRuntime);
				}
			}

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
			if (accepts != null) {
				for (RequestType a: accepts) {
					if (a == requestAcceptType) {
						return;
					}
				}
			}
			throw new WebApplicationException(Status.UNSUPPORTED_MEDIA_TYPE);
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
		}

		public void checkContentType(MediaType contentType) {
			if (contentType == null) {
				return;
			}

			if (allowedContentTypesRuntime == null) {
				return;
			}
			
			if (!contentType.isWildcardType() && !contentType.isWildcardSubtype()) {
				for (MediaType act: allowedContentTypesRuntime) {
					if (contentType.isCompatible(act)) {
						return;
					}
				}
			}
			
			throw new WebApplicationException(Status.UNSUPPORTED_MEDIA_TYPE);
		}
		
		public void checkMethodType(String requestMethod) {

			if (methods == null || methods.length == 0) {
				return;
			}
			
			for (MethodType m: methods) {
				if (m.name().equals(requestMethod)) {
					return;
				}
			}
			if (logger.isDebugEnabled()) {
				logger.debug("reject Request. HTTP Method:" + requestMethod + " not allowed for WebAPI:" + getName());
			}
			throw new WebApplicationException(Status.METHOD_NOT_ALLOWED);
		}
		
		public boolean isSufficientOAuthScope(List<String> grantedScopes) {
			if (oauthScopes == null || oauthScopes.length == 0) {
				return false;
			}
			if (grantedScopes == null || grantedScopes.size() == 0) {
				return false;
			}
			
			for (String os: oauthScopes) {
				if (os.indexOf(' ') > 0) {
					String[] osSplit = os.split(" ");
					if (grantedScopes.containsAll(Arrays.asList(osSplit))) {
						return true;
					}
				} else {
					if (grantedScopes.contains(os)) {
						return true;
					}
				}
			}

			return false;
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
		
		if (oauthScopes != null) {
			definition.setOauthScopes(Arrays.copyOf(oauthScopes, oauthScopes.length));
		}

		if (allowRequestContentTypes != null) {
			definition.setAllowRequestContentTypes(new String[allowRequestContentTypes.length]);
			System.arraycopy(allowRequestContentTypes, 0, definition.getAllowRequestContentTypes(), 0, allowRequestContentTypes.length);
		}
		
		definition.setMaxRequestBodySize(maxRequestBodySize);
		definition.setMaxFileSize(maxFileSize);

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
		
		if (definition.getOauthScopes() != null) {
			oauthScopes = Arrays.copyOf(definition.getOauthScopes(), definition.getOauthScopes().length);
		} else {
			oauthScopes = null;
		}
		
		if (definition.getAllowRequestContentTypes() != null) {
			allowRequestContentTypes = new String[definition.getAllowRequestContentTypes().length];
			System.arraycopy(definition.getAllowRequestContentTypes(), 0, allowRequestContentTypes, 0, allowRequestContentTypes.length);
		} else {
			allowRequestContentTypes = null;
		}
		
		maxRequestBodySize = definition.getMaxRequestBodySize();
		maxFileSize = definition.getMaxFileSize();
	}
}
