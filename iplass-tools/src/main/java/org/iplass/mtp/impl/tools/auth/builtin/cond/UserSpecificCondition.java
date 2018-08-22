/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.tools.auth.builtin.cond;

import java.sql.Date;

public class UserSpecificCondition implements BuiltinAuthUserSearchCondition {

	public enum SpecificType {
		/** ロックユーザ */
		LOCKED,
		/** パスワード有効期間 */
		EXPIRED_PASSWORD,
		/** ラストログイン期間 */
		LAST_LOGIN
	}

	private SpecificType type;

	private String lockedUserPolicyName;

	private String passwordRemainDaysPolicyName;
	private SearchOperator passwordRemainDaysOparator;
	private int passwordRemainDays;

	private Date lastLoginFromDate;
	private Date lastLoginToDate;

	public SpecificType getType() {
		return type;
	}
	public void setType(SpecificType type) {
		this.type = type;
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
	public SearchOperator getPasswordRemainDaysOparator() {
		return passwordRemainDaysOparator;
	}
	public void setPasswordRemainDaysOparator(SearchOperator passwordRemainDaysOparator) {
		this.passwordRemainDaysOparator = passwordRemainDaysOparator;
	}
	public int getPasswordRemainDays() {
		return passwordRemainDays;
	}
	public void setPasswordRemainDays(int passwordRemainDays) {
		this.passwordRemainDays = passwordRemainDays;
	}

	public Date getLastLoginFromDate() {
		return lastLoginFromDate;
	}
	public void setLastLoginFromDate(Date lastLoginFromDate) {
		this.lastLoginFromDate = lastLoginFromDate;
	}
	public Date getLastLoginToDate() {
		return lastLoginToDate;
	}
	public void setLastLoginToDate(Date lastLoginToDate) {
		this.lastLoginToDate = lastLoginToDate;
	}

}
