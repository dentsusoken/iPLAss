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

package org.iplass.mtp.webapi.definition;

import javax.xml.bind.annotation.XmlRootElement;

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
	 * このWebApiが呼び出されたときに実行するCommand。
	 */
	private CommandConfig commandConfig;

	/** このWebApiで処理されるCommandを特権（セキュリティ制約を受けない）にて処理するかどうか。デフォルトはfalse。 */
	private boolean isPrivilaged;

	/** このWebApiの呼び出しをセキュリティ設定によらず呼び出し可能にする場合は、trueを設定。 isPrivilagedとの違いは、Entityの操作などにおいては、セキュリティ制約を受ける。デフォルトはfalse。*/
	private boolean isPublicWebApi;

	/** XMLHttpRequestがセットされていることを確認するか。 */
	private boolean isCheckXRequestedWithHeader = true;

	/**
	 * Commandの実行結果と、表示処理の対応の定義。
	 */
	private String[] results;

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
	 * REST JSON受付時のパラメータタイプ（クラスパス）
	 */
	private String restJsonParameterType;

	/**
	 * REST XML受付時のパラメータ名
	 */
	private String restXmlParameterName;

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

	public String getRestXmlParameterName() {
		return restXmlParameterName;
	}

	public void setRestXmlParameterName(String restXmlParameterName) {
		this.restXmlParameterName = restXmlParameterName;
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
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            セットする name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName
	 *            セットする displayName
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            セットする description
	 */
	public void setDescription(String description) {
		this.description = description;
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
	 * @return result
	 */
	public String[] getResults() {
		return results;
	}

	/**
	 * @param result
	 *            セットする result
	 */
	public void setResults(String[] results) {
		this.results = results;
	}

	public boolean isPublicWebApi() {
		return isPublicWebApi;
	}

	/**
	 * このWebApiの呼び出しをセキュリティ設定によらず呼び出し可能にする場合は、trueを設定します。
	 * isPrivilagedとの違いは、Entityの操作などにおいては、セキュリティ制約を受けます。
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
	 */
	public boolean isPrivilaged() {
		return isPrivilaged;
	}

	/**
	 *
	 * @param isPrivilaged
	 */
	public void setPrivilaged(boolean isPrivilaged) {
		this.isPrivilaged = isPrivilaged;
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
}
