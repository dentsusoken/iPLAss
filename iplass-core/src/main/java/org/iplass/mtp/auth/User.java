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

package org.iplass.mtp.auth;

import org.iplass.mtp.entity.GenericEntity;

/**
 * ユーザを表現するEntity。
 * 当該Entityに保持されるプロパティは、
 * Entityとして設計されているmtp.auth.Userと一致するとは限らない。
 * セキュリティの認証機構に応じて、その認証機構特有のプロパティが設定されることがある。
 * 
 * @author K.Higuchi
 *
 */
public class User extends GenericEntity {
	private static final long serialVersionUID = -3856409198625059398L;
	
	public static final String DEFINITION_NAME = "mtp.auth.User";
	public static final String ACCOUNT_ID = "accountId";
	public static final String FIRST_NAME = "firstName";
//	public static final String MIDDLE_NAME = "middleName";//TODO 実装？
	public static final String LAST_NAME = "lastName";
	public static final String MAIL = "mail";
	public static final String PASSWORD = "password";
	public static final String ADMIN_FLG = "admin";
	public static final String TEMPORARY_FLG = "temporary";
	public static final String LANGUAGE = "language";
	public static final String FIRST_NAME_KANA = "firstNameKana";
	public static final String LAST_NAME_KANA = "lastNameKana";
	public static final String GROUPS = "groups";
	public static final String RANK = "rank";
	public static final String ACCOUNT_POLICY = "accountPolicy";
	public static final String IP_ADDRESS_HISTORY = "ipAddressHistory";
	
	
	public User() {
		setDefinitionName(DEFINITION_NAME);
	}
	
	public User(String oid, String accountId, boolean isAnonymous) {
		setDefinitionName(DEFINITION_NAME);
		setOid(oid);
		setValue(ACCOUNT_ID, accountId);
		setValue("ANONYMOUS_FLG", Boolean.valueOf(isAnonymous));
	}
	
	public String getAccountId() {
		return getValue(ACCOUNT_ID);
	}
	public void setAccountId(String accountId) {
		setValue(ACCOUNT_ID, accountId);
	}
	
	public String getFirstName() {
		return getValue(FIRST_NAME);
	}
	public void setFirstName(String firstName) {
		setValue(FIRST_NAME, firstName);
	}
	
	public String getLastName() {
		return getValue(LAST_NAME);
	}
	public void setLastName(String lastName) {
		setValue(LAST_NAME, lastName);
	}
	
	public String getMail() {
		return getValue(MAIL);
	}
	public void setMail(String mail) {
		setValue(MAIL, mail);
	}
	
	public String getPassword() {
		return getValue(PASSWORD);
	}
	public void setPassword(String password) {
		setValue(PASSWORD, password);
	}
	
	public boolean isAdmin() {
		Object obj = getValue(ADMIN_FLG);
		if (obj == null) {
			return false;
		}
		return ((Boolean) obj).booleanValue();
	}
	public void setAdmin(boolean admin) {
		setValue(ADMIN_FLG, Boolean.valueOf(admin));
	}
	
	public boolean isTemporary() {
		Object obj = getValue(TEMPORARY_FLG);
		if (obj == null) {
			return false;
		}
		return ((Boolean) obj).booleanValue();
	}
	public void setTemporary(boolean temporary) {
		setValue(TEMPORARY_FLG, Boolean.valueOf(temporary));
	}
	
	public String getLanguage() {
		return getValue(LANGUAGE);
	}
	public void setLanguage(String language) {
		setValue(LANGUAGE, language);
	}
	
	public String getFirstNameKana() {
		return getValue(FIRST_NAME_KANA);
	}
	public void setFirstNameKana(String firstNameKana) {
		setValue(FIRST_NAME_KANA, firstNameKana);
	}
	
	public String getLastNameKana() {
		return getValue(LAST_NAME_KANA);
	}
	public void setLastNameKana(String lastNameKana) {
		setValue(LAST_NAME_KANA, lastNameKana);
	}
	
	public Group[] getGroups() {
		Object g = getValue(GROUPS);
		if (g instanceof Group) {
			return new Group[]{(Group) g};
		} else {
			return (Group[]) g;
		}
	}
	public void setGroups(Group[] groups) {
		setValue(GROUPS, groups);
	}
	
	public Rank getRank() {
		return getValue(RANK);
	}
	public void setRank(Rank rank) {
		setValue(RANK, rank);
	}
	
	public boolean isAnonymous() {
		Object obj = getValue("ANONYMOUS_FLG");
		if(obj instanceof Boolean) {
			return (Boolean)obj;
		}
		return false;
	}
	
	public String getAccountPolicy() {
		return getValue(ACCOUNT_POLICY);
	}
	public void setAccountPolicy(String accountPolicy) {
		setValue(ACCOUNT_POLICY, accountPolicy);
	}
	
	/**
	 * 2step認証時、リスクベースによる判断で、IPAddress履歴による判断を行う場合、その履歴を保存するためのプロパティ。
	 */
	public String getIpAddressHistory() {
		return getValue(IP_ADDRESS_HISTORY);
	}
	public void setIpAddressHistory(String ipAddressHistory) {
		setValue(IP_ADDRESS_HISTORY, ipAddressHistory);
	}
	
}
