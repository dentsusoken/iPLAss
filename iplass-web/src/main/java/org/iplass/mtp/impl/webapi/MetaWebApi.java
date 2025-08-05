/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.Variant;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlTransient;

import org.apache.commons.io.FilenameUtils;
import org.iplass.mtp.command.CommandRuntimeException;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.definition.config.SingleCommandConfig;
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
import org.iplass.mtp.impl.web.CorsConfig;
import org.iplass.mtp.impl.web.ParameterValueMap;
import org.iplass.mtp.impl.web.RequestPath.PathType;
import org.iplass.mtp.impl.web.RequestRestriction;
import org.iplass.mtp.impl.web.WebFrontendService;
import org.iplass.mtp.impl.web.WebRequestContext;
import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.impl.web.fileupload.MultiPartParameterValueMap;
import org.iplass.mtp.impl.webapi.MetaWebApiParamMap.WebApiParamMapRuntime;
import org.iplass.mtp.impl.webapi.MetaWebApiResultAttribute.WebApiResultAttributeRuntime;
import org.iplass.mtp.impl.webapi.jackson.WebApiObjectMapperService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.webapi.WebApiRuntimeException;
import org.iplass.mtp.webapi.definition.CacheControlType;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.RequestType;
import org.iplass.mtp.webapi.definition.StateType;
import org.iplass.mtp.webapi.definition.WebApiDefinition;
import org.iplass.mtp.webapi.definition.WebApiParamMapDefinition;
import org.iplass.mtp.webapi.definition.WebApiResultAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * WebAPI メタデータ
 *
 * @see org.iplass.mtp.command.annotation.webapi.WebApi
 * @see org.iplass.mtp.command.annotation.webapi.RestJson
 * @see org.iplass.mtp.command.annotation.webapi.RestXml
 */
public class MetaWebApi extends BaseRootMetaData implements DefinableMetaData<WebApiDefinition> {
	private static final long serialVersionUID = 2590900624234333139L;

	private static Logger logger = LoggerFactory.getLogger(MetaWebApi.class);

	public static final String HEADER_ACCEPT = "Accept";
	public static final String COMMAND_INTERCEPTOR_NAME = "webApi";

	/** HTTPMethod 指定時のワイルドカード */
	private static final String WILDCARD = "*";

	/** このWebAPIが呼び出されたときに実行するCommand。 */
	private MetaCommand command;

	/** WebからのパラメータのCommand実行時のParameter名のマップの定義 */
	private MetaWebApiParamMap[] webApiParamMap;

	/**
	 * @deprecated {@link #responseResults} を使用してください。本フィールドは大きなバージョンアップで削除する予定です。
	 */
	@Deprecated
	private String[] results;

	/** 結果属性メタデータ */
	private MetaWebApiResultAttribute[] responseResults;

	private RequestType[] accepts;
	private MethodType[] methods;
	private StateType state = StateType.STATEFUL;
	private boolean supportBearerToken;
	private String[] oauthScopes;

	private CacheControlType cacheControlType;
	private long cacheControlMaxAge = -1;

	// RestJson パラメータ
	private String restJsonParameterName = null;
	private Class<?> restJsonParameterType = void.class;
	/**  REST JSON として受け付け可能 ContentType */
	private String[] restJsonAcceptableContentTypes = null;
	// RestXml パラメータ
	private String restXmlParameterName = null;
	private Class<?> restXmlParameterType = void.class;
	/** REST XML として受け付け可能 ContentType */
	private String[] restXmlAcceptableContentTypes = null;

	/** このWebAPIで処理されるCommandを特権（セキュリティ制約を受けない）にて処理するかどうか。デフォルトはfalse。 */
	@XmlElement
	private Boolean privilaged;
	private boolean privileged = false;

	/** このWebAPIの呼び出しをセキュリティ設定によらず呼び出し可能にする場合は、trueを設定。 isPrivilegedとの違いは、Entityの操作などにおいては、セキュリティ制約を受ける。デフォルトはfalse。*/
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

	private boolean needTrustedAuthenticate;

	private String[] allowRequestContentTypes;

	private Long maxRequestBodySize;
	private Long maxFileSize;

	/** スタブレスポンスを返却するか */
	private boolean returnStubResponse;
	/** スタブレスポンスの "status" の値 */
	private String stubResponseStatusValue;
	/** スタブレスポンスの JSON Value */
	private String stubResponseJsonValue;
	/** OpenAPI バージョン */
	private String openApiVersion;
	/** OpenAPI ファイルタイプ */
	private String openApiFileType;
	/** OpenAPI 定義 */
	private String openApi;

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

	/** @deprecated {@link #isPrivileged()} を使用してください。 */
	@Deprecated
	@XmlTransient
	public boolean isPrivilaged() {
		return isPrivileged();
	}

	/** @deprecated {@link #setPrivileged(boolean)} を使用してください。 */
	@Deprecated
	public void setPrivilaged(boolean isPrivilaged) {
		setPrivileged(isPrivilaged);
	}

	public boolean isPrivileged() {
		if (privilaged != null) {
			//古い属性の方が有効
			return privilaged;
		}
		//新しい属性の方が有効
		return privileged;
	}

	public void setPrivileged(boolean isPrivileged) {
		//保存時に古い属性をnullにセットするようにする
		this.privileged = isPrivileged;
		privilaged = null;
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

	public MetaWebApiParamMap[] getWebApiParamMap() {
		return webApiParamMap;
	}

	public void setWebApiParamMap(MetaWebApiParamMap[] webApiParamMap) {
		this.webApiParamMap = webApiParamMap;
	}

	/**
	 * @deprecated {@link #setResponseResults(MetaWebApiResultAttribute[])} を利用してください。本メソッドは大きなバージョンアップで削除する予定です。<br>
	 * 本設定値は {@link MetaWebApiResultAttribute#setName(String)} の値に代替されます。
	 */
	@Deprecated
	public void setResults(String[] results) {
		this.results = results;
	}

	/**
	 * @deprecated {@link #getResponseResults()} を利用してください。本メソッドは大きなバージョンアップで削除する予定です。<br>
	 * 本設定値は {@link MetaWebApiResultAttribute#getName()} の値に代替されます。
	 */
	@Deprecated
	public String[] getResults() {
		return this.results;
	}

	/**
	 * 結果属性メタデータを設定します。
	 * @param responseResults 結果属性メタデータの配列
	 */
	public void setResponseResults(MetaWebApiResultAttribute[] responseResults) {
		this.responseResults = responseResults;
	}

	/**
	 * 結果属性メタデータを取得します。
	 * @return 結果属性メタデータの配列
	 */
	public MetaWebApiResultAttribute[] getResponseResults() {
		return this.responseResults;
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

	/**
	 * REST JSON として受付可能な Conteent-Type を取得します
	 * @return Content-Type 配列
	 */
	public String[] getRestJsonAcceptableContentTypes() {
		return restJsonAcceptableContentTypes;
	}

	/**
	 * REST JSON として受付可能な Conteent-Type を設定します
	 * @param acceptableContentTypes Content-Type 配列
	 */
	public void setRestJsonAcceptableContentTypes(String[] acceptableContentTypes) {
		this.restJsonAcceptableContentTypes = acceptableContentTypes;
	}

	public String getRestXmlParameterName() {
		return restXmlParameterName;
	}

	public void setRestXmlParameterName(String restXmlParameterName) {
		this.restXmlParameterName = restXmlParameterName;
	}

	public Class<?> getRestXmlParameterType() {
		return restXmlParameterType;
	}

	public void setRestXmlParameterType(Class<?> restXmlParameterType) {
		this.restXmlParameterType = restXmlParameterType;
	}

	/**
	 * REST XML として受付可能な Content-Type を取得します
	 * @return Content-Type 配列
	 */
	public String[] getRestXmlAcceptableContentTypes() {
		return restXmlAcceptableContentTypes;
	}

	/**
	 * REST XML として受付可能な Content-Type を設定します
	 * @param acceptableContentTypes Content-Type 配列
	 */
	public void setRestXmlAcceptableContentTypes(String[] acceptableContentTypes) {
		this.restXmlAcceptableContentTypes = acceptableContentTypes;
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

	/**
	 * スタブレスポンスを返却するかを取得します。
	 * @return スタブレスポンスを返却する場合は true を返却
	 */
	public boolean isReturnStubResponse() {
		return returnStubResponse;
	}

	/**
	 * スタブレスポンスを返却するかを設定します。
	 * @param returnStubResponse スタブレスポンスを返却する場合は true を設定
	 */
	public void setReturnStubResponse(boolean returnStubResponse) {
		this.returnStubResponse = returnStubResponse;
	}

	/**
	 * スタブレスポンスの "status" の値を取得します。
	 * @return スタブレスポンスの "status" の値
	 */
	public String getStubResponseStatusValue() {
		return stubResponseStatusValue;
	}

	/**
	 * スタブレスポンスの "status" の値を設定します。
	 * @param stubResponseStatusValue スタブレスポンスの "status" の値
	 */
	public void setStubResponseStatusValue(String stubResponseStatusValue) {
		this.stubResponseStatusValue = stubResponseStatusValue;
	}

	/**
	 * スタブレスポンスのJSON値を取得します。
	 * @return スタブレスポンスのJSON値
	 */
	public String getStubResponseJsonValue() {
		return stubResponseJsonValue;
	}

	/**
	 * スタブレスポンスのJSON値を設定します。
	 * @param stubResponseJsonValue スタブレスポンスのJSON値
	 */
	public void setStubResponseJsonValue(String stubResponseJsonValue) {
		this.stubResponseJsonValue = stubResponseJsonValue;
	}

	/**
	 * OpenAPIのバージョンを取得します。
	 * @return OpenAPIのバージョン
	 */
	public String getOpenApiVersion() {
		return openApiVersion;
	}

	/**
	 * OpenAPIのバージョンを設定します。
	 * <p>
	 * 3.0, 3.1 などマイナーバージョンまで記載します。
	 * OpenAPI バージョンに記載できる内容は {@link org.iplass.mtp.webapi.openapi.OpenApiVersion} を参照してください。
	 * </p>
	 * @param openApiVersion OpenAPIのバージョン
	 */
	public void setOpenApiVersion(String openApiVersion) {
		this.openApiVersion = openApiVersion;
	}

	/**
	 * OpenAPIのファイルタイプを取得します。
	 * @return OpenAPIのファイルタイプ
	 */
	public String getOpenApiFileType() {
		return openApiFileType;
	}

	/**
	 * OpenAPIのファイルタイプを設定します。
	 * <p>
	 * OpenAPI のフォーマットは、JSON または YAML です。
	 * 設定できる内容は、 {@link org.iplass.mtp.webapi.openapi.OpenApiFileType} を参照してください。
	 * </p>
	 * @param openApiFileType OpenAPIのファイルタイプ
	 */
	public void setOpenApiFileType(String openApiFileType) {
		this.openApiFileType = openApiFileType;
	}

	/**
	 * OpenAPI定義を取得します。
	 * @return OpenAPI定義
	 */
	public String getOpenApi() {
		return openApi;
	}

	/**
	 * OpenAPI定義を設定します。
	 * @param openApi OpenAPI定義
	 */
	public void setOpenApi(String openApi) {
		this.openApi = openApi;
	}

	public class WebApiRuntime extends BaseMetaDataRuntime {
		private CommandRuntime cmd;
		private HashMap<String, List<WebApiParamMapRuntime>> webApiParamMapRuntimes;

		private GroovyTemplate accessControlAllowOriginTemplate;
		private List<Variant> variants;

		private WebApiService service = ServiceRegistry.getRegistry().getService(WebApiService.class);
		private InterceptorService is = ServiceRegistry.getRegistry().getService(InterceptorService.class);

		private MethodType specificMethod;
		private String parentName;

		private RequestRestriction requestRestrictionRuntime;
		private String corsAllowString;

		/** restJsonAcceptableContentType を Set で保持する */
		private Set<String> restJsonAcceptableContentTypeSet;
		/** restXmlAcceptableContentType を Set で保持する */
		private Set<String> restXmlAcceptableContentTypeSet;
		/** 結果属性ランタイム */
		private WebApiResultAttributeRuntime[] responseResultsRuntime;
		/** 結果属性名、ランタイムマップ */
		private Map<String, WebApiResultAttributeRuntime> responseResultsNameRuntimeMap;

		/** スタブレスポンスのステータス値 */
		private String stubResponseStatusValue;;
		/** スタブレスポンスのJSONMap */
		private Map<String, Object> stubResponseJsonMap;

		@SuppressWarnings("unchecked")
		public WebApiRuntime() {
			var webApiService = ServiceRegistry.getRegistry().getService(WebApiService.class);

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

				if (webApiParamMap != null) {
					webApiParamMapRuntimes = new HashMap<>();
					for (MetaWebApiParamMap p: webApiParamMap) {
						WebApiParamMapRuntime pmr = p.createRuntime();
						List<WebApiParamMapRuntime> pmrList = webApiParamMapRuntimes.get(p.getName());
						if (pmrList == null) {
							pmrList = new LinkedList<>();
							webApiParamMapRuntimes.put(p.getName(), pmrList);
						}
						pmrList.add(pmr);
					}
				}

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
					if (name.endsWith(mt.toString()) && name.charAt(name.length() - mt.toString().length() - 1) == '/') {
						specificMethod = mt;
						parentName = name.substring(0, name.length() - mt.toString().length() - 1);
						break;
					}
				}

				WebFrontendService wfs = ServiceRegistry.getRegistry().getService(WebFrontendService.class);
				if (getCacheControlType() != null) {
					cacheControlType = getCacheControlType();
				} else {
					cacheControlType = wfs.getDefaultCacheControlType();
				}

				requestRestrictionRuntime = wfs.getRequestRestriction(getPublicWebApiName(), PathType.REST);
				if (!requestRestrictionRuntime.isForce()) {
					if (maxRequestBodySize != null || maxFileSize != null
							|| (methods != null && methods.length > 0)
							|| (allowRequestContentTypes != null && allowRequestContentTypes.length > 0)) {
						requestRestrictionRuntime = requestRestrictionRuntime.copy();
						if (maxRequestBodySize != null) {
							requestRestrictionRuntime.setMaxBodySize(maxRequestBodySize);
						}
						if (maxFileSize != null) {
							requestRestrictionRuntime.setMaxFileSize(maxFileSize);
						}
						if (methods != null && methods.length > 0) {
							ArrayList<String> aml = new ArrayList<>(methods.length);
							for (MethodType hmt: methods) {
								aml.add(hmt.toString());
							}
							requestRestrictionRuntime.setAllowMethods(aml);
						}
						if (allowRequestContentTypes != null && allowRequestContentTypes.length > 0) {
							requestRestrictionRuntime.setAllowContentTypes(Arrays.asList(allowRequestContentTypes));
						}

						requestRestrictionRuntime.init();
					}
				}

				corsAllowString = corsAllowString();


				restJsonAcceptableContentTypeSet = restJsonAcceptableContentTypes != null
						? new HashSet<>(Arrays.asList(restJsonAcceptableContentTypes))
								: Collections.emptySet();

				restXmlAcceptableContentTypeSet = restXmlAcceptableContentTypes != null
						? new HashSet<>(Arrays.asList(restXmlAcceptableContentTypes))
								: Collections.emptySet();


				// responseResults 設定
				List<WebApiResultAttributeRuntime> responseResultsRuntimeList = new ArrayList<>();
				if (responseResults != null && responseResults.length > 0) {
					for (int i = 0; i < responseResults.length; i++) {
						responseResultsRuntimeList.add(responseResults[i].createRuntime());
					}
				}

				// TODO 下位バージョン互換性の為のロジックです。results を削除した場合、以下の if ブロックは削除してください。
				if (results != null && results.length > 0) {
					if (responseResults == null || responseResults.length == 0) {
						// results に設定があり、responseResults に何も設定が無い場合にログ出力する
						logger.debug("WebApi {}, we recommend changing the setting from results to responseResults.", getName());
					}

					// results に値が設定されている場合、responseResults の name に一致しないキーが存在すれば、キーの内容をマージする。
					for (int i = 0; i < results.length; i++) {
						// responseResultsRuntimeList に同一の属性名を持つかを確認する
						var attributeName = results[i];
						var isNotContainsName = responseResultsRuntimeList.stream().filter(r -> r.getName().equals(attributeName)).findAny().isEmpty();
						if (isNotContainsName) {
							// 属性名が存在しない場合は、Runtime を作成し追加する
							var attribute = new MetaWebApiResultAttribute();
							attribute.setName(attributeName);
							responseResultsRuntimeList.add(attribute.createRuntime());
						}
					}
				}

				responseResultsRuntime = responseResultsRuntimeList.toArray(WebApiResultAttributeRuntime[]::new);
				responseResultsNameRuntimeMap = responseResultsRuntimeList.stream().collect(Collectors.toMap(r -> r.getName(), r -> r));

				// スタブ判定
				if (webApiService.isEnableStubResponse() && MetaWebApi.this.returnStubResponse) {
					var objectMapper = ServiceRegistry.getRegistry().getService(WebApiObjectMapperService.class).getObjectMapper();

					var config = new SingleCommandConfig();
					// WebApiService に設定されている stubResponseCommandName を取得
					config.setCommandName(webApiService.getStubResponseCommandName());
					var stubCommand = MetaCommand.createInstance(config);
					stubCommand.applyConfig(config);
					this.cmd = stubCommand.createRuntime();

					// ステータス値は設定されていない場合は、デフォルト値を設定
					this.stubResponseStatusValue = StringUtil.isEmpty(MetaWebApi.this.stubResponseStatusValue) ? "SUCCESS"
							: MetaWebApi.this.stubResponseStatusValue;
					try {
						this.stubResponseJsonMap = Collections.emptyMap();
						if (StringUtil.isNotEmpty(getStubResponseJsonValue())) {
							// レスポンスの JSON が設定されている場合は、ObjectMapper で Map に変換する
							this.stubResponseJsonMap = objectMapper.readValue(getStubResponseJsonValue(), Map.class);
						}

					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			} catch (RuntimeException e) {
				setIllegalStateException(e);
			}
		}

		private String corsAllowString() {
			StringBuilder sb = new StringBuilder();
			for (String m: requestRestrictionRuntime.getAllowMethods()) {
				switch (m) {
				case HttpMethod.GET:
					sb.append(HttpMethod.GET).append(", ");
					break;
				case HttpMethod.POST:
					sb.append(HttpMethod.POST).append(", ");
					break;
				case HttpMethod.PUT:
					sb.append(HttpMethod.PUT).append(", ");
					break;
				case HttpMethod.DELETE:
					sb.append(HttpMethod.DELETE).append(", ");
					break;
				case HttpMethod.PATCH:
					sb.append(HttpMethod.PATCH).append(", ");
					break;
				case WILDCARD:
					// GET
					sb.append(HttpMethod.GET).append(", ");
					// POST
					sb.append(HttpMethod.POST).append(", ");
					// PUT
					sb.append(HttpMethod.PUT).append(", ");
					// DELETE
					sb.append(HttpMethod.DELETE).append(", ");
					// PATCH
					sb.append(HttpMethod.PATCH).append(", ");
					break;
				default:
					// HEAD, TRACE, CONNECT は設定しない
					// OPTIONS は最後に無条件設定する
					break;
				}
			}
			sb.append(HttpMethod.OPTIONS);
			return sb.toString();
		}

		public RequestRestriction getRequestRestriction() {
			return requestRestrictionRuntime;
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
			List<WebApiRuntime> rl = new ArrayList<>(MethodType.values().length);
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

		public Map<String, List<WebApiParamMapRuntime>> getWebApiParamMapRuntimes() {
			checkState();
			return webApiParamMapRuntimes;
		}

		private String getAccessControlAllowOrigin(RequestContext req) {
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
			CorsConfig cc = requestRestrictionRuntime.getCors();
			if (cc == null) {
				cc = service.getCors();
			}

			if (!requestRestrictionRuntime.isForce()) {
				if (accessControlAllowOriginTemplate != null) {
					return accessControlAllowCredentials;
				}
			}

			if (cc != null) {
				return cc.isAllowCredentials();
			}

			return false;
		}

		public boolean isCorsAllowOrigin(String origin, RequestContext req) {
			CorsConfig cc = requestRestrictionRuntime.getCors();
			if (cc == null) {
				cc = service.getCors();
			}

			if (!requestRestrictionRuntime.isForce()) {
				String acao = getAccessControlAllowOrigin(req);
				if (acao != null) {
					return corsAllowOrign(origin, acao);
				}
			}

			if (cc != null && cc.getAllowOrigin() != null) {
				for (String s: cc.getAllowOrigin()) {
					if (corsAllowOrign(origin, s)) {
						return true;
					}
				}
			}

			return false;
		}

		private boolean corsAllowOrign(String origin, String accessControlAllowOrigin) {
			if (accessControlAllowOrigin != null) {
				if (accessControlAllowOrigin.indexOf(' ') >= 0) {
					String[] accessControlAllowOriginArray = StringUtil.split(accessControlAllowOrigin, ' ');
					for (String s: accessControlAllowOriginArray) {
						if (isMatchOrigin(origin, s)) {
							return true;
						}
					}
				} else {
					if (isMatchOrigin(origin, accessControlAllowOrigin)) {
						return true;
					}
				}
			}
			return false;
		}

		private boolean isMatchOrigin(String origin, String accessControlAllowOrigin) {
			return FilenameUtils.wildcardMatch(origin, accessControlAllowOrigin);
		}

		public String corsAccessControlAllowMethods() {
			return corsAllowString;
		}

		@Override
		public MetaWebApi getMetaData() {
			return MetaWebApi.this;
		}

		public CommandRuntime getCommandRuntime() {
			return cmd;
		}

		public String executeCommand(final WebRequestStack stack, String interceptorName) {
			checkState();
			CommandInterceptor[] cmdInterceptors = is.getInterceptors(interceptorName);

			RequestContext req = stack.getRequestContext();
			if (req instanceof WebRequestContext) {
				WebRequestContext webRequestContext = (WebRequestContext) req;
				ParameterValueMap currentValueMap = webRequestContext.getValueMap();
				//set maxFileSize
				if (currentValueMap instanceof MultiPartParameterValueMap) {
					((MultiPartParameterValueMap) currentValueMap).setMaxFileSize(requestRestrictionRuntime.maxFileSize());
				}
				//parameter map
				if (webApiParamMap != null) {
					WebApiVariableParameterValueMap variableValueMap = new WebApiVariableParameterValueMap(currentValueMap, stack.getRequestPath(), this);
					webRequestContext.setValueMap(variableValueMap);
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

		/**
		 * リクエストタイプを確認する
		 *
		 * <p>
		 * 実行したリクエストが許可されているリクエストタイプであるか確認します。
		 * 許可されていないリクエストタイプの場合は、UNSUPPORTED_MEDIA_TYPE(415)の例外をスローします。
		 * </p>
		 *
		 * <p>
		 * 例外がスローされるパターンは以下のパターンです。
		 * </p>
		 * <ul>
		 * <li>許可リクエストタイプが設定されている： 許可リクエストタイプに、実行されたリクエストのリクエストタイプが含まれていない</li>
		 * <li>
		 * 許可リクエストタイプが設定されていない： 実行されたリクエストのリクエストタイプが REST_OTHERS の場合<br>
		 * ※REST_OTHERS は、許可する content-type の範囲が広いので、明示的に設定された場合のみ許可する。
		 * </li>
		 * </ul>
		 *
		 *
		 *
		 * @param requestAcceptType 実行されたリクエストのリクエストタイプ
		 * @param request HttpServletRequest
		 */
		public void checkRequestType(RequestType requestAcceptType, HttpServletRequest request) {
			if (accepts != null && accepts.length >= 1) {
				// 許可リクエストタイプが設定されている
				for (RequestType a: accepts) {
					if (a == requestAcceptType) {
						return;
					}
				}

			} else if (RequestType.REST_OTHERS != requestAcceptType) {
				// 許可リクエストタイプが設定されていないかつ、リクエストタイプが REST_OTHERS ではない
				return;
			}

			throw new WebApplicationException(Status.UNSUPPORTED_MEDIA_TYPE);
		}

		public void checkMethodType(MethodType requestMethodType) {
			checkMethodType(requestMethodType.toString());
		}

		public void checkContentType(MediaType contentType) {
			if (contentType == null) {
				return;
			}

			if (!requestRestrictionRuntime.isAllowedContentType(contentType)) {
				throw new WebApplicationException(Status.UNSUPPORTED_MEDIA_TYPE);
			}
		}

		public void checkMethodType(String requestMethod) {
			if (HttpMethod.OPTIONS.equals(requestMethod)) {
				return;
			}

			if (!requestRestrictionRuntime.isAllowedMethod(requestMethod)) {
				if (logger.isDebugEnabled()) {
					logger.debug("reject Request. HTTP Method:" + requestMethod + " not allowed for WebAPI:" + getName());
				}
				throw new WebApplicationException(Status.METHOD_NOT_ALLOWED);
			}
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

		/**
		 * REST JSON として受け付け可能な Content-Type か判定します
		 * @param contentType content-type
		 * @return 受け付け可能な場合は true を返却する
		 */
		public boolean isAcceptableRestJson(String contentType) {
			return restJsonAcceptableContentTypeSet.contains(contentType);
		}

		/**
		 * REST XML として受け付け可能な Content-Type か判定します
		 * @param contentType content-type
		 * @return 受け付け可能な場合は true を返却する
		 */
		public boolean isAcceptableRestXml(String contentType) {
			return restXmlAcceptableContentTypeSet.contains(contentType);
		}

		/**
		 * 結果属性ランタイムを取得します。
		 * @return 結果属性ランタイム
		 */
		public WebApiResultAttributeRuntime[] getResponseResults() {
			return this.responseResultsRuntime;
		}

		/**
		 * 指定した名前の結果属性ランタイムが存在するか確認します。
		 * @param name 結果属性の名前
		 * @return 指定した名前の結果属性ランタイムが存在する場合は true、それ以外は false
		 */
		public boolean containsResponseResult(String name) {
			return responseResultsNameRuntimeMap.containsKey(name);
		}

		/**
		 * スタブレスポンスのステータス値を取得します。
		 * <p>
		 * メタデータ解析時に設定値が存在しない場合は、デフォルト値を設定済みです。
		 * そのため、本メソッドの返却値は常に null にはなりません。
		 * </p>
		 * @return スタブレスポンスのステータス値
		 */
		public String getStubResponseStatusValue() {
			return stubResponseStatusValue;
		}

		/**
		 * スタブレスポンスのJSONMapを取得します。
		 * <p>
		 * メタデータの JSON 文字列を解析し、Map インスタンスへ変換済みです。
		 * 設定されていない場合は、Collections#emptyMap() を設定してあります。
		 * </p>
		 * @return スタブレスポンスのJSONMap
		 */
		public Map<String, Object> getStubResponseJsonMap() {
			return stubResponseJsonMap;
		}
	}

	// Meta → Definition
	@Override
	public WebApiDefinition currentConfig() {
		WebApiDefinition definition = new WebApiDefinition();

		definition.setName(name);
		definition.setDisplayName(displayName);
		definition.setDescription(description);

		if (command != null) {
			definition.setCommandConfig(command.currentConfig());
		}

		if (webApiParamMap != null) {
			WebApiParamMapDefinition[] paramMapDefinition = new WebApiParamMapDefinition[webApiParamMap.length];
			int i = 0;
			for (MetaWebApiParamMap map : webApiParamMap) {
				paramMapDefinition[i] = map.currentConfig();
				i++;
			}
			definition.setWebApiParamMap(paramMapDefinition);
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

		definition.setPrivileged(isPrivileged());
		definition.setPublicWebApi(isPublicWebApi);
		definition.setCheckXRequestedWithHeader(isCheckXRequestedWithHeader);

		// TODO results は非推奨項目となりました。大きなバージョンアップで削除する予定です。
		if (results != null) {
			definition.setResults(Arrays.copyOf(results, results.length));
		}

		if (responseResults != null) {
			WebApiResultAttribute[] definitionResponseResults = new WebApiResultAttribute[responseResults.length];
			for (int i = 0; i < responseResults.length; i++) {
				definitionResponseResults[i] = responseResults[i].currentConfig();
			}
			definition.setResponseResults(definitionResponseResults);
		}

		definition.setRestJsonParameterName(restJsonParameterName);
		definition.setRestXmlParameterName(restXmlParameterName);

		if (!(restJsonParameterType == null || restJsonParameterType == void.class)) {
			definition.setRestJsonParameterType(restJsonParameterType.getName());
		}

		if (!(restXmlParameterType == null || restXmlParameterType == void.class)) {
			definition.setRestXmlParameterType(restXmlParameterType.getName());
		}

		if (restJsonAcceptableContentTypes != null) {
			definition.setRestJsonAcceptableContentTypes(Arrays.copyOf(restJsonAcceptableContentTypes, restJsonAcceptableContentTypes.length));
		}

		if (restXmlAcceptableContentTypes != null) {
			definition.setRestXmlAcceptableContentTypes(Arrays.copyOf(restXmlAcceptableContentTypes, restXmlAcceptableContentTypes.length));
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

		definition.setReturnStubResponse(returnStubResponse);
		definition.setStubResponseStatusValue(stubResponseStatusValue);
		definition.setStubResponseJsonValue(stubResponseJsonValue);

		definition.setOpenApiVersion(openApiVersion);
		definition.setOpenApiFileType(openApiFileType);
		definition.setOpenApi(openApi);

		return definition;
	}

	// Definition → Meta
	@Override
	public void applyConfig(WebApiDefinition definition) {
		name = definition.getName();
		displayName = definition.getDisplayName();
		description = definition.getDescription();

		// TODO results は非推奨項目となりました。大きなバージョンアップで削除する予定です。
		if (definition.getResults() != null) {
			results = Arrays.copyOf(definition.getResults(), definition.getResults().length);
		} else {
			results = null;
		}

		this.responseResults = null;
		if (definition.getResponseResults() != null) {
			MetaWebApiResultAttribute[] responseResults = new MetaWebApiResultAttribute[definition.getResponseResults().length];
			for (int i = 0; i < definition.getResponseResults().length; i++) {
				MetaWebApiResultAttribute attribute = new MetaWebApiResultAttribute();
				attribute.applyConfig(definition.getResponseResults()[i]);
				responseResults[i] = attribute;
			}
			this.responseResults = responseResults;
		}

		privileged = definition.isPrivileged();
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

		if (definition.getWebApiParamMap() != null) {
			webApiParamMap = new MetaWebApiParamMap[definition.getWebApiParamMap().length];
			int i = 0;
			for (WebApiParamMapDefinition paramDef : definition.getWebApiParamMap()) {
				webApiParamMap[i] = new MetaWebApiParamMap();
				webApiParamMap[i].applyConfig(paramDef);
				i++;
			}
		} else {
			webApiParamMap = null;
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

		try {
			if (definition.getRestXmlParameterType() != null) {
				restXmlParameterType = Class.forName(definition.getRestXmlParameterType());
			} else {
				restXmlParameterType = null;
			}
		} catch (ClassNotFoundException e) {
			throw new CommandRuntimeException(definition.getRestXmlParameterType() + " class not found.", e);
		}

		restJsonAcceptableContentTypes = definition.getRestJsonAcceptableContentTypes() != null
				// null ではない場合
				? Arrays.copyOf(definition.getRestJsonAcceptableContentTypes(), definition.getRestJsonAcceptableContentTypes().length)
						// null の場合
						: null;

		restXmlAcceptableContentTypes = definition.getRestXmlAcceptableContentTypes() != null
				// null ではない場合
				? Arrays.copyOf(definition.getRestXmlAcceptableContentTypes(), definition.getRestXmlAcceptableContentTypes().length)
						// null の場合
						: null;

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

		returnStubResponse = definition.isReturnStubResponse();
		stubResponseStatusValue = definition.getStubResponseStatusValue();
		stubResponseJsonValue = definition.getStubResponseJsonValue();

		openApiVersion = definition.getOpenApiVersion();
		openApiFileType = definition.getOpenApiFileType();
		openApi = definition.getOpenApi();

	}
}
