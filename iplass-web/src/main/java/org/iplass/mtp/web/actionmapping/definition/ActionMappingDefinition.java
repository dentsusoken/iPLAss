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

package org.iplass.mtp.web.actionmapping.definition;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.mtp.command.definition.config.CommandConfig;
import org.iplass.mtp.definition.Definition;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.web.actionmapping.definition.cache.CacheCriteriaDefinition;
import org.iplass.mtp.web.actionmapping.definition.result.ResultDefinition;

/**
 * ActionMappingの定義です。
 * ActionMappingは、当該name（URLのパス）を呼び出された際の、処理内容を規定します。
 * 実行するCommandと、その実行結果による表示内容（template）を定義します。
 * 
 * @author K.Higuchi
 *
 */
@XmlRootElement
public class ActionMappingDefinition implements Definition {

	private static final long serialVersionUID = -8553251729036721789L;

	private String name;
	@MultiLang(itemNameGetter = "getName", itemKey = "displayName", itemGetter = "getDisplayName", itemSetter = "setDisplayName", multiLangGetter = "getLocalizedDisplayNameList", multiLangSetter = "setLocalizedDisplayNameList")
	private String displayName;
	private List<LocalizedStringDefinition> localizedDisplayNameList;
	private String description;

	/**
	 * クライアントでのキャッシュ指定。
	 * 未指定の場合はキャッシュを許す。
	 */
	private ClientCacheType clientCacheType;

	/** クライアントキャッシュのmax-age（秒）*/
	private long clientCacheMaxAge = -1;

	/**
	 * このActionMappingで指定される表示処理が部品かどうか。
	 * trueの場合、クライアントからの直接呼出し不可。
	 */
	private boolean isParts;

	/** このActionMappingで処理されるCommand,Templateを特権（セキュリティ制約を受けない）にて処理するかどうか。デフォルトはfalse。 */
	private boolean isPrivileged;
	
	/** このActionの呼び出しをセキュリティ設定によらず呼び出し可能にする場合は、trueを設定。 isPrivilegedとの違いは、Entityの操作などにおいては、セキュリティ制約を受ける。デフォルトはfalse。*/
	private boolean isPublicAction;

	/**
	 * このActionMappingが呼び出されたときに実行するCommand。
	 * 未指定可（Commandは実行せずテンプレートを表示）。
	 */
	private CommandConfig commandConfig;

	/** WebからのパラメータのCommand実行時のParameter名のマップの定義 */
	private ParamMapDefinition[] paramMap;

	/**
	 * Commandの実行結果と、表示処理の対応の定義。
	 * *指定可（実行したCommand結果によらず当該表示処理を実行）。
	 * 未指定可（このActionMappingのnameと同一名のTemplateを表示）。
	 */
	private ResultDefinition[] result;

	/** Tokenチェックの実行設定。
	 * 未指定可(Tokenチェックは実行されない)。
	 */
	private TokenCheck tokenCheck;

	/** キャッシュ基準 */
	private CacheCriteriaDefinition cacheCriteria;

	/** sessionにて同期を行うか否か */
	private boolean synchronizeOnSession;
	
	/** 許可するHTTP Method。未指定の場合は、すべて許可。 */
	private HttpMethodType[] allowMethod;

	private boolean needTrustedAuthenticate;

	private String[] allowRequestContentTypes;

	private Long maxRequestBodySize;
	private Long maxFileSize;

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
	 * 許可するリクエストボディのContentTypeを指定。未指定の場合はすべて許可。
	 * 
	 * @param allowRequestContentTypes
	 */
	public void setAllowRequestContentTypes(String[] allowRequestContentTypes) {
		this.allowRequestContentTypes = allowRequestContentTypes;
	}

	/**
	 * @see #setNeedTrustedAuthenticate(boolean)
	 * @return
	 */
	public boolean isNeedTrustedAuthenticate() {
		return needTrustedAuthenticate;
	}

	/**
	 * 当該Action呼び出しに信頼された認証が必要な場合、trueをセット。
	 * デフォルトfalse。
	 * 
	 * @param needTrustedAuthenticate
	 */
	public void setNeedTrustedAuthenticate(boolean needTrustedAuthenticate) {
		this.needTrustedAuthenticate = needTrustedAuthenticate;
	}
	
	/**
	 * @see #setAllowMethod(HttpMethodType[])
	 * @return
	 */
	public HttpMethodType[] getAllowMethod() {
		return allowMethod;
	}

	/**
	 * 許可するHTTP Methodを指定。未指定の場合は、すべて許可。
	 * 
	 * @param allowMethod
	 */
	public void setAllowMethod(HttpMethodType[] allowMethod) {
		this.allowMethod = allowMethod;
	}

	/**
	 * @see #setPublicAction(boolean)
	 * @return
	 */
	public boolean isPublicAction() {
		return isPublicAction;
	}

	/**
	 * このActionの呼び出しをセキュリティ設定によらず呼び出し可能にする場合は、trueを設定。
	 * isPrivilegedとの違いは、Entityの操作などにおいては、セキュリティ制約を受ける。
	 * デフォルトはfalse。
	 * 
	 * @param isPublicAction
	 */
	public void setPublicAction(boolean isPublicAction) {
		this.isPublicAction = isPublicAction;
	}

	/**
	 * @see #setClientCacheMaxAge(long)
	 * @return
	 */
	public long getClientCacheMaxAge() {
		return clientCacheMaxAge;
	}

	/**
	 * clientCacheType=ClientCacheType.CACHEを指定した場合の
	 * クライアントキャッシュのmax-age（秒）を指定。
	 * デフォルト値は-1でこの場合はmax-ageは未指定となる。<br>
	 * <b>注意：max-age未指定の場合、FF、Chromeでは実際はキャッシュが利用されない</b>
	 * 
	 * @param clientCacheMaxAge
	 */
	public void setClientCacheMaxAge(long clientCacheMaxAge) {
		this.clientCacheMaxAge = clientCacheMaxAge;
	}

	/**
	 * @see #setSynchronizeOnSession(boolean)
	 * @return
	 */
	public boolean isSynchronizeOnSession() {
		return synchronizeOnSession;
	}

	/**
	 * このActionを実行する際、自動的にsessionオブジェクトにて同期を行うか否かを設定。
	 * 同一セッションIDにて並列実行された場合に不整合が発生しうる場合にtrueをセットすることにより、
	 * 自動的にsession単位にsynchronizedされる。
	 * ただ、Actionの処理全体でsynchronizedされるので、オーバーヘッドが大きい。
	 * このフラグを利用するよりは、実際のCommandのコード内で、同期化が必要な処理部分のみ
	 * コード上でsynchronizedする方が望ましい。
	 * デフォルトfalse。
	 * 
	 * @param synchronizeOnSession
	 */
	public void setSynchronizeOnSession(boolean synchronizeOnSession) {
		this.synchronizeOnSession = synchronizeOnSession;
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

	public List<LocalizedStringDefinition> getLocalizedDisplayNameList() {
		return localizedDisplayNameList;
	}

	public void setLocalizedDisplayNameList(List<LocalizedStringDefinition> localizedDisplayNameList) {
		this.localizedDisplayNameList = localizedDisplayNameList;
	}

	public void addLocalizedDisplayName(LocalizedStringDefinition localizedDisplayName) {
		if (localizedDisplayNameList == null) {
			localizedDisplayNameList = new ArrayList<LocalizedStringDefinition>();
		}
		localizedDisplayNameList.add(localizedDisplayName);
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
	 * @see #setClientCacheType(ClientCacheType)
	 * @return clientCacheType
	 */
	public ClientCacheType getClientCacheType() {
		return clientCacheType;
	}

	/**
	 * クライアントでのキャッシュ指定（Cache-Controlヘッダの制御）。
	 * 未指定の場合はキャッシュを許す（service-config設定ファイルのデフォルト設定が適用）。
	 * ブラウザ種別によらず、キャッシュを有効化するためには、合わせてclientCacheMaxAgeの設定も必要。
	 * 
	 * @see #setClientCacheMaxAge(long)
	 * @param clientCacheType
	 */
	public void setClientCacheType(ClientCacheType clientCacheType) {
		this.clientCacheType = clientCacheType;
	}

	/**
	 * @see #setParts(boolean)
	 * @return 
	 */
	public boolean isParts() {
		return isParts;
	}

	/**
	 * このActionMappingで指定される表示処理が部品かどうかを設定。
	 * trueをセットした場合、クライアントからの直接呼出しが不可となる。
	 * 
	 * @param isParts
	 */
	public void setParts(boolean isParts) {
		this.isParts = isParts;
	}

	/**
	 * @see #setPrivilaged(boolean)
	 * @return
	 * @deprecated {@link #isPrivileged()} を使用してください。
	 */
	@Deprecated
	public boolean isPrivilaged() {
		return isPrivileged;
	}

	/**
	 * このActionMappingで処理されるCommand,Templateを特権（セキュリティ制約を受けない）にて処理するかどうかを設定。
	 * デフォルトはfalse。
	 *
	 * @param isPrivileged
	 * @deprecated {@link #setPrivileged(boolean)} を使用してください。 
	 */
	@Deprecated
	public void setPrivilaged(boolean isPrivileged) {
		this.isPrivileged = isPrivileged;
	}

	/**
	 * @see #setPrivileged(boolean)
	 * @return
	 */
	public boolean isPrivileged() {
		return isPrivileged;
	}

	/**
	 * このActionMappingで処理されるCommand,Templateを特権（セキュリティ制約を受けない）にて処理するかどうかを設定。
	 * デフォルトはfalse。
	 *
	 * @param isPrivileged
	 */
	public void setPrivileged(boolean isPrivileged) {
		this.isPrivileged = isPrivileged;
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
	public ParamMapDefinition[] getParamMap() {
		return paramMap;
	}

	/**
	 * @param paramMap
	 *            セットする paramMap
	 */
	public void setParamMap(ParamMapDefinition[] paramMap) {
		this.paramMap = paramMap;
	}

	/**
	 * @return result
	 */
	public ResultDefinition[] getResult() {
		return result;
	}

	/**
	 * @param result
	 *            セットする result
	 */
	public void setResult(ResultDefinition[] result) {
		this.result = result;
	}

	/**
	 * @return tokenCheck
	 */
	public TokenCheck getTokenCheck() {
		return tokenCheck;
	}

	/**
	 * @param tokenCheck セットする tokenCheck
	 */
	public void setTokenCheck(TokenCheck tokenCheck) {
		this.tokenCheck = tokenCheck;
	}

	/**
	 * @return cacheCriteria
	 */
	public CacheCriteriaDefinition getCacheCriteria() {
		return cacheCriteria;
	}

	/**
	 * @param cacheCriteria
	 *            セットする cacheCriteria
	 */
	public void setCacheCriteria(CacheCriteriaDefinition cacheCriteria) {
		this.cacheCriteria = cacheCriteria;
	}
}
