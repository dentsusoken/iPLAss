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

package org.iplass.mtp.webapi.definition;

import jakarta.xml.bind.annotation.XmlRootElement;

import org.iplass.mtp.command.definition.config.CommandConfig;
import org.iplass.mtp.definition.Definition;

@XmlRootElement
public class WebApiDefinition implements Definition {

	private static final long serialVersionUID = 7903913056944225448L;

	/**
	 * 共通項目。
	 */
	private String name;
	private String displayName;
	private String description;

	/**
	 * WebAPIキャッシュ指定。
	 * 未指定の場合はキャッシュをしない。
	 */
	private CacheControlType cacheControlType;

	/** WebAPIキャッシュのmax-age（秒）*/
	private long cacheControlMaxAge = -1;

	/**
	 * このWebApiが呼び出されたときに実行するCommand。
	 */
	private CommandConfig commandConfig;

	/** WebApiからのパラメータのCommand実行時のParameter名のマップの定義 */
	private WebApiParamMapDefinition[] webApiParamMap;

	/** このWebApiで処理されるCommandを特権（セキュリティ制約を受けない）にて処理するかどうか。デフォルトはfalse。 */
	private boolean isPrivileged;

	/** このWebApiの呼び出しをセキュリティ設定によらず呼び出し可能にする場合は、trueを設定。 isPrivilegedとの違いは、Entityの操作などにおいては、セキュリティ制約を受ける。デフォルトはfalse。*/
	private boolean isPublicWebApi;

	/** XMLHttpRequestがセットされていることを確認するか。 */
	private boolean isCheckXRequestedWithHeader = true;

	/**
	 * Commandの実行結果と、表示処理の対応の定義。
	 * @deprecated {@link #responseResults} を使用してください。本設定項目は、{@link WebApiResultAttribute}の配列を設定するように変更されました。本フィールドは大きなバージョンアップで削除する予定です。
	 */
	@Deprecated
	private String[] results;

	/**
	 * WebAPIレスポンスの結果の属性
	 */
	private WebApiResultAttribute[] responseResults;

	/**
	 * WebApiの受付種別。
	 */
	private RequestType[] accepts;

	/**
	 * WebApiのメソッド種別。
	 */
	private MethodType[] methods;

	/**
	 * REST JSON受付時のパラメータ名
	 */
	private String restJsonParameterName;

	/**
	 * REST JSON受付時のパラメータタイプ（クラス名）
	 */
	private String restJsonParameterType;

	/**
	 * REST JSON 受付可能な Conetnt-Type
	 */
	private String[] restJsonAcceptableConetntTypes;

	/**
	 * REST XML受付時のパラメータ名
	 */
	private String restXmlParameterName;

	/**
	 * REST XML受付時のパラメータタイプ（クラス名）
	 */
	private String restXmlParameterType;

	/**
	 * REST XML 受付可能な Conetnt-Type
	 */
	private String[] restXmlAcceptableConetntTypes;

	/** Tokenチェックの実行設定。
	 * 未指定可(Tokenチェックは実行されない)。
	 */
	private WebApiTokenCheck tokenCheck;

	/** sessionにて同期を行うか否か */
	private boolean synchronizeOnSession;

	private String responseType;

	private String accessControlAllowOrigin;

	private boolean accessControlAllowCredentials;

	private boolean needTrustedAuthenticate;

	private StateType state = StateType.STATEFUL;

	private boolean supportBearerToken;

	private String[] oauthScopes;

	private String[] allowRequestContentTypes;

	private Long maxRequestBodySize;
	private Long maxFileSize;

	/** スタブレスポンスを返却するか */
	private boolean returnStubResponse;
	/** スタブレスポンス "status" の値 */
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

	/**
	 * multipart/form-data時のアップロードファイルの最大サイズ。-1の場合は無制限。
	 * １つのファイルに対する最大サイズなので、複数のファイルの合計サイズを制限したい場合は、
	 * maxRequestBodySizeを設定します。
	 *
	 * @param maxFileSize
	 */
	public void setMaxFileSize(Long maxFileSize) {
		this.maxFileSize = maxFileSize;
	}

	public Long getMaxRequestBodySize() {
		return maxRequestBodySize;
	}

	/**
	 * リクエストボディの最大サイズ。-1の場合は無制限。
	 *
	 * @param maxRequestBodySize
	 */
	public void setMaxRequestBodySize(Long maxRequestBodySize) {
		this.maxRequestBodySize = maxRequestBodySize;
	}

	public String[] getAllowRequestContentTypes() {
		return allowRequestContentTypes;
	}

	/**
	 * 許可するリクエストボディのContentTypeを指定。未指定の場合はすべて許可。<br>
	 * accepts指定より、allowRequestContentTypesの指定による制限が優先されます。<br>
	 * 例えば、
	 * accepts指定によりJSON形式の処理が有効化されている場合において、
	 * allowRequestContentTypesに"application/json"が含まれない場合は、
	 * JSON形式によるリクエストは処理されません。
	 *
	 * @param allowRequestContentTypes
	 */
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
	 * WebApiをStatelessとして呼び出すか否かを設定します。
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

	public WebApiTokenCheck getTokenCheck() {
		return tokenCheck;
	}

	public void setTokenCheck(WebApiTokenCheck tokenCheck) {
		this.tokenCheck = tokenCheck;
	}

	public String getRestJsonParameterType() {
		return restJsonParameterType;
	}

	public void setRestJsonParameterType(String restJsonParameterType) {
		this.restJsonParameterType = restJsonParameterType;
	}

	public String getRestJsonParameterName() {
		return restJsonParameterName;
	}

	public void setRestJsonParameterName(String restJsonParameterName) {
		this.restJsonParameterName = restJsonParameterName;
	}

	/**
	 * REST JSON として受付可能な Content-Type を取得します。
	 * @return Content-Type 配列
	 */
	public String[] getRestJsonAcceptableContentTypes() {
		return restJsonAcceptableConetntTypes;
	}

	/**
	 * REST JSON として受付可能な Content-Type を設定します。
	 * @param acceptableContentTypes Content-Type 配列
	 */
	public void setRestJsonAcceptableContentTypes(String[] acceptableContentTypes) {
		this.restJsonAcceptableConetntTypes = acceptableContentTypes;
	}

	public String getRestXmlParameterName() {
		return restXmlParameterName;
	}

	public void setRestXmlParameterName(String restXmlParameterName) {
		this.restXmlParameterName = restXmlParameterName;
	}

	public String getRestXmlParameterType() {
		return restXmlParameterType;
	}

	public void setRestXmlParameterType(String restXmlParameterType) {
		this.restXmlParameterType = restXmlParameterType;
	}

	/**
	 * REST XML として受付可能な Content-Type を取得します。
	 * @return Content-Type 配列
	 */
	public String[] getRestXmlAcceptableContentTypes() {
		return restXmlAcceptableConetntTypes;
	}

	/**
	 * REST XML として受付可能な Content-Type を設定します。
	 * @param acceptableContentTypes Content-Type 配列
	 */
	public void setRestXmlAcceptableContentTypes(String[] acceptableContentTypes) {
		this.restXmlAcceptableConetntTypes = acceptableContentTypes;
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

	/**
	 * @return name
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            セットする name
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return displayName
	 */
	@Override
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName
	 *            セットする displayName
	 */
	@Override
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return description
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            セットする description
	 */
	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @see #setCacheControlType(cacheControlType)
	 * @return cacheControlType
	 */
	public CacheControlType getCacheControlType() {
		return cacheControlType;
	}

	/**
	 * WebAPIキャッシュ指定（Cache-Controlヘッダの制御）。
	 * 未指定の場合はキャッシュをしない。
	 * ブラウザ種別によらず、キャッシュを有効化するためには、合わせてCacheControlMaxAgeの設定も必要。
	 *
	 * @see #setCacheControlMaxAge(long)
	 * @param cacheControlType
	 */
	public void setCacheControlType(CacheControlType cacheControlType) {
		this.cacheControlType = cacheControlType;
	}

	/**
	 * @see #setCacheControlMaxAge(long)
	 * @return
	 */
	public long getCacheControlMaxAge() {
		return cacheControlMaxAge;
	}

	/**
	 * cacheControlMaxAge=CacheControlType.CACHEを指定した場合の
	 * WebAPIキャッシュのmax-age（秒）を指定。
	 * デフォルト値は-1でこの場合はmax-ageは未指定となる。<br>
	 * <b>注意：max-age未指定の場合、FF、Chromeでは実際はキャッシュが利用されない</b>
	 *
	 * @param cacheControlMaxAge
	 */
	public void setCacheControlMaxAge(long cacheControlMaxAge) {
		this.cacheControlMaxAge = cacheControlMaxAge;
	}

	/**
	 * @return commandConfig
	 */
	public CommandConfig getCommandConfig() {
		return commandConfig;
	}

	/**
	 * @param commandConfig
	 *            セットする commandConfig
	 */
	public void setCommandConfig(CommandConfig commandConfig) {
		this.commandConfig = commandConfig;
	}

	/**
	 * @return paramMap
	 */
	public WebApiParamMapDefinition[] getWebApiParamMap() {
		return webApiParamMap;
	}

	/**
	 * @param webApiParamMap
	 *            セットする webApiParamMap
	 */
	public void setWebApiParamMap(WebApiParamMapDefinition[] webApiParamMap) {
		this.webApiParamMap = webApiParamMap;
	}

	/**
	 * @return result
	 * @deprecated {@link #getResponseResults()} を使用してください。本メソッドは大きなバージョンアップで削除する予定です。
	 */
	@Deprecated
	public String[] getResults() {
		return results;
	}

	/**
	 * @param result
	 *            セットする result
	 * @deprecated {@link #setResponseResults(WebApiResultAttribute[])} を使用してください。本メソッドは大きなバージョンアップで削除する予定です。
	 */
	@Deprecated
	public void setResults(String[] results) {
		this.results = results;
	}

	/**
	 * WebApiレスポンスの結果の属性を取得します。
	 * @return WebApiレスポンスの結果の属性
	 */
	public WebApiResultAttribute[] getResponseResults() {
		return responseResults;
	}

	/**
	 * WebApiレスポンスの結果の属性を設定します。
	 * @param responseResults WebApiレスポンスの結果の属性
	 */
	public void setResponseResults(WebApiResultAttribute[] responseResults) {
		this.responseResults = responseResults;
	}

	public boolean isPublicWebApi() {
		return isPublicWebApi;
	}

	/**
	 * このWebApiの呼び出しをセキュリティ設定によらず呼び出し可能にする場合は、trueを設定します。
	 * isPrivilegedとの違いは、Entityの操作などにおいては、セキュリティ制約を受けます。
	 * デフォルトはfalseです。
	 *
	 * @param isPublicWebApi
	 */
	public void setPublicWebApi(boolean isPublicWebApi) {
		this.isPublicWebApi = isPublicWebApi;
	}

	/**
	 *
	 * @return
	 * @deprecated {@link #isPrivileged()} を使用してください。
	 */
	@Deprecated
	public boolean isPrivilaged() {
		return isPrivileged;
	}

	/**
	 *
	 * @param isPrivileged
	 * @deprecated {@link #setPrivileged(boolean)} を使用してください。
	 */
	@Deprecated
	public void setPrivilaged(boolean isPrivileged) {
		this.isPrivileged = isPrivileged;
	}

	/**
	 *
	 * @return
	 */
	public boolean isPrivileged() {
		return isPrivileged;
	}

	/**
	 *
	 * @param isPrivileged
	 */
	public void setPrivileged(boolean isPrivileged) {
		this.isPrivileged = isPrivileged;
	}

	/**
	 * XMLHttpRequestがセットされていることを確認するかを取得します。
	 * @return XMLHttpRequestチェック可否
	 */
	public boolean isCheckXRequestedWithHeader() {
		return isCheckXRequestedWithHeader;
	}

	/**
	 * XMLHttpRequestチェック可否を設定します。
	 * @param isCheckXRequestedWithHeader
	 */
	public void setCheckXRequestedWithHeader(boolean isCheckXRequestedWithHeader) {
		this.isCheckXRequestedWithHeader = isCheckXRequestedWithHeader;
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
}
