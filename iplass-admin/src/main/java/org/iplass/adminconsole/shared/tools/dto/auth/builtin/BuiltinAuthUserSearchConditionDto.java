/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.shared.tools.dto.auth.builtin;

import java.io.Serializable;
import java.util.Date;

public class BuiltinAuthUserSearchConditionDto implements Serializable {

	private static final long serialVersionUID = -1078894537565739526L;

	private BuiltinAuthUserSearchType searchType;

	private int limit;
	private int offset;


	//-------------------------
	//Specific
	//-------------------------
	private BuiltinAuthUserSpecificType specificType;

	private String lockedUserPolicyName;

	private String passwordRemainDaysPolicyName;
	private BuiltinAuthUserSearchOperator passwordRemainDaysOparator;
	private int passwordRemainDays;

	private Date lastLoginFrom;
	private Date lastLoginTo;

	//-------------------------
	//User Attribute
	//-------------------------
	private String accountId;
	private String name;
	private String mail;

	private BuiltinAuthUserSearchOperator validTermRemainDaysOparator;
	private int validTermRemainDays;

	private String directWhere;



	/**
	 * @return searchType
	 */
	public BuiltinAuthUserSearchType getSearchType() {
		return searchType;
	}
	/**
	 * @param searchType セットする searchType
	 */
	public void setSearchType(BuiltinAuthUserSearchType searchType) {
		this.searchType = searchType;
	}


	/**
	 * @return limit
	 */
	public int getLimit() {
		return limit;
	}
	/**
	 * @param limit セットする limit
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}
	/**
	 * @return offset
	 */
	public int getOffset() {
		return offset;
	}
	/**
	 * @param offset セットする offset
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}



	/**
	 * @return specificType
	 */
	public BuiltinAuthUserSpecificType getSpecificType() {
		return specificType;
	}
	/**
	 * @param specificType セットする specificType
	 */
	public void setSpecificType(BuiltinAuthUserSpecificType specificType) {
		this.specificType = specificType;
	}

	public String getLockedUserPolicyName() {
		return lockedUserPolicyName;
	}
	public void setLockedUserPolicyName(String lockedUserPolicyName) {
		this.lockedUserPolicyName = lockedUserPolicyName;
	}

	public String getPasswordRemainDaysPolicyName() {
		return passwordRemainDaysPolicyName;
	}
	public void setPasswordRemainDaysPolicyName(
			String passwordRemainDaysPolicyName) {
		this.passwordRemainDaysPolicyName = passwordRemainDaysPolicyName;
	}

	/**
	 * @return passwordRemainDaysOparator
	 */
	public BuiltinAuthUserSearchOperator getPasswordRemainDaysOparator() {
		return passwordRemainDaysOparator;
	}
	/**
	 * @param passwordRemainDaysOparator セットする passwordRemainDaysOparator
	 */
	public void setPasswordRemainDaysOparator(BuiltinAuthUserSearchOperator passwordRemainDaysOparator) {
		this.passwordRemainDaysOparator = passwordRemainDaysOparator;
	}
	/**
	 * @return passwordRemainDays
	 */
	public int getPasswordRemainDays() {
		return passwordRemainDays;
	}
	/**
	 * @param passwordRemainDays セットする passwordRemainDays
	 */
	public void setPasswordRemainDays(int passwordRemainDays) {
		this.passwordRemainDays = passwordRemainDays;
	}

	/**
	 * @return lastLoginFrom
	 */
	public Date getLastLoginFrom() {
		return lastLoginFrom;
	}
	/**
	 * @param lastLoginFrom セットする lastLoginFrom
	 */
	public void setLastLoginFrom(Date lastLoginFrom) {
		this.lastLoginFrom = lastLoginFrom;
	}
	/**
	 * @return lastLoginTo
	 */
	public Date getLastLoginTo() {
		return lastLoginTo;
	}
	/**
	 * @param lastLoginTo セットする lastLoginTo
	 */
	public void setLastLoginTo(Date lastLoginTo) {
		this.lastLoginTo = lastLoginTo;
	}




	/**
	 * @return accountId
	 */
	public String getAccountId() {
		return accountId;
	}
	/**
	 * @param accountId セットする accountId
	 */
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name セットする name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return mail
	 */
	public String getMail() {
		return mail;
	}
	/**
	 * @param mail セットする mail
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}

	/**
	 * @return validTermRemainDaysOparator
	 */
	public BuiltinAuthUserSearchOperator getValidTermRemainDaysOparator() {
		return validTermRemainDaysOparator;
	}
	/**
	 * @param validTermRemainDaysOparator セットする validTermRemainDaysOparator
	 */
	public void setValidTermRemainDaysOparator(BuiltinAuthUserSearchOperator validTermRemainDaysOparator) {
		this.validTermRemainDaysOparator = validTermRemainDaysOparator;
	}
	/**
	 * @return validTermRemainDays
	 */
	public int getValidTermRemainDays() {
		return validTermRemainDays;
	}
	/**
	 * @param validTermRemainDays セットする validTermRemainDays
	 */
	public void setValidTermRemainDays(int validTermRemainDays) {
		this.validTermRemainDays = validTermRemainDays;
	}

	public String getDirectWhere() {
		return directWhere;
	}
	public void setDirectWhere(String directWhere) {
		this.directWhere = directWhere;
	}

}
