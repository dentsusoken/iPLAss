/*
 * Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.auth.policy.definition;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.mtp.definition.Definition;
import org.iplass.mtp.definition.LocalizedStringDefinition;

/**
 * 認証方式のポリシー定義。
 * 基盤組み込み（Builtin）の認証方式を利用する際に設定可能。
 *
 * @author K.Higuchi
 *
 */
@XmlRootElement
public class AuthenticationPolicyDefinition implements Definition {
	private static final long serialVersionUID = 1686825565380276912L;

	private String name;
	@MultiLang(itemNameGetter = "getName", itemKey = "displayName", itemGetter = "getDisplayName", itemSetter = "setDisplayName", multiLangGetter = "getLocalizedDisplayNameList", multiLangSetter = "setLocalizedDisplayNameList")
	private String displayName;
	private List<LocalizedStringDefinition> localizedDisplayNameList;
	private String description;

	private AccountLockoutPolicyDefinition accountLockoutPolicy;
	@MultiLang(isMultiLangValue = false, itemKey = "passwordPolicy", itemGetter = "getPasswordPolicy", itemSetter = "setPasswordPolicy")
	private PasswordPolicyDefinition passwordPolicy;
	private boolean recordLastLoginDate = true;
	private RememberMePolicyDefinition rememberMePolicy;
	private List<String> authenticationProvider;
	private List<String> openIdConnectDefinition;
	
	/** ユーザー作成時、パスワード更新時にその情報を受け取る為のListener */
	private List<AccountNotificationListenerDefinition> notificationListener;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public List<LocalizedStringDefinition> getLocalizedDisplayNameList() {
		return localizedDisplayNameList;
	}

	public void setLocalizedDisplayNameList(
			List<LocalizedStringDefinition> localizedDisplayNameList) {
		this.localizedDisplayNameList = localizedDisplayNameList;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @see AccountLockoutPolicyDefinition
	 * @return
	 */
	public AccountLockoutPolicyDefinition getAccountLockoutPolicy() {
		return accountLockoutPolicy;
	}

	/**
	 * @see AccountLockoutPolicyDefinition
	 * @param accountLockoutPolicy
	 */
	public void setAccountLockoutPolicy(
			AccountLockoutPolicyDefinition accountLockoutPolicy) {
		this.accountLockoutPolicy = accountLockoutPolicy;
	}

	/**
	 * @see PasswordPolicyDefinition
	 * @return
	 */
	public PasswordPolicyDefinition getPasswordPolicy() {
		return passwordPolicy;
	}

	/**
	 * @see PasswordPolicyDefinition
	 * @param passwordPolicy
	 */
	public void setPasswordPolicy(PasswordPolicyDefinition passwordPolicy) {
		this.passwordPolicy = passwordPolicy;
	}

	/**
	 * 最終ログイン日時を記録するか否か。
	 * 記録する場合、ログインの都度DBへ書き込み処理が発生する。
	 *
	 * @return
	 */
	public boolean isRecordLastLoginDate() {
		return recordLastLoginDate;
	}

	/**
	 * @see #isRecordLastLoginDate()
	 * @param recordLastLoginDate
	 */
	public void setRecordLastLoginDate(boolean recordLastLoginDate) {
		this.recordLastLoginDate = recordLastLoginDate;
	}

	/**
	 * RememberMe機能（ログインしたままにする）に関する設定。
	 * @return
	 */
	public RememberMePolicyDefinition getRememberMePolicy() {
		return rememberMePolicy;
	}

	/**
	 * @see #getRememberMePolicy()
	 * @param rememberMePolicy
	 */
	public void setRememberMePolicy(RememberMePolicyDefinition rememberMePolicy) {
		this.rememberMePolicy = rememberMePolicy;
	}

	/**
	 * 当該ポリシーで利用する認証プロバイダのリスト。
	 * 未指定の場合は、すべての認証プロバイダを利用可能。
	 * 
	 * @return
	 */
	public List<String> getAuthenticationProvider() {
		return authenticationProvider;
	}

	/**
	 * @see #getAuthenticationProvider()
	 * @param authenticationProvider
	 */
	public void setAuthenticationProvider(List<String> authenticationProvider) {
		this.authenticationProvider = authenticationProvider;
	}
	
	/**
	 * @see AccountNotificationListenerDefinition
	 * @return
	 */
	public List<AccountNotificationListenerDefinition> getNotificationListener() {
		return notificationListener;
	}

	/**
	 * @see AccountNotificationListenerDefinition
	 * @param notificationListener
	 */
	public void setNotificationListener(
			List<AccountNotificationListenerDefinition> notificationListener) {
		this.notificationListener = notificationListener;
	}

	public List<String> getOpenIdConnectDefinition() {
		return openIdConnectDefinition;
	}

	public void setOpenIdConnectDefinition(List<String> openIdConnectDefinition) {
		this.openIdConnectDefinition = openIdConnectDefinition;
	}
}
