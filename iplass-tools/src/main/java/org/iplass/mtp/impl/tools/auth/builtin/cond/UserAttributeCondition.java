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


public class UserAttributeCondition implements BuiltinAuthUserSearchCondition {

	private String accountId;
	private String name;
	private String mail;

	private SearchOperator validTermRemainDaysOparator;
	private int validTermRemainDays;

	private String directWhere;

	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}

	public SearchOperator getValidTermRemainDaysOparator() {
		return validTermRemainDaysOparator;
	}
	public void setValidTermRemainDaysOparator(SearchOperator validTermRemainDaysOparator) {
		this.validTermRemainDaysOparator = validTermRemainDaysOparator;
	}
	public int getValidTermRemainDays() {
		return validTermRemainDays;
	}
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
