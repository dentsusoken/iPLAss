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

package org.iplass.mtp.impl.auth.authenticate.builtin.policy;

import org.iplass.mtp.auth.policy.definition.AccountLockoutPolicyDefinition;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;

public class MetaAccountLockoutPolicy implements MetaData {
	private static final long serialVersionUID = 2820093765295977330L;
	
	/** アカウントロックアウトするまでの失敗回数。0はロックアウトしない */
	private int lockoutFailureCount = 10;
	/** ロックアウトしている期間（分）。0は無制限 */
	private int lockoutDuration;
	/** 失敗回数を記憶しておく期間（分） 。0は無制限*/
	private int lockoutFailureExpirationInterval;
	
	public int getLockoutFailureCount() {
		return lockoutFailureCount;
	}

	public void setLockoutFailureCount(int lockoutFailureCount) {
		this.lockoutFailureCount = lockoutFailureCount;
	}

	public int getLockoutDuration() {
		return lockoutDuration;
	}

	public void setLockoutDuration(int lockoutDuration) {
		this.lockoutDuration = lockoutDuration;
	}

	public int getLockoutFailureExpirationInterval() {
		return lockoutFailureExpirationInterval;
	}

	public void setLockoutFailureExpirationInterval(
			int lockoutFailureExpirationInterval) {
		this.lockoutFailureExpirationInterval = lockoutFailureExpirationInterval;
	}

	@Override
	public MetaAccountLockoutPolicy copy() {
		return ObjectUtil.deepCopy(this);
	}

	public void applyConfig(AccountLockoutPolicyDefinition accountLockoutPolicy) {
		lockoutFailureCount = accountLockoutPolicy.getLockoutFailureCount();
		lockoutDuration = accountLockoutPolicy.getLockoutDuration();
		lockoutFailureExpirationInterval = accountLockoutPolicy.getLockoutFailureExpirationInterval();
	}

	public AccountLockoutPolicyDefinition currentConfig() {
		AccountLockoutPolicyDefinition def = new AccountLockoutPolicyDefinition();
		def.setLockoutFailureCount(lockoutFailureCount);
		def.setLockoutDuration(lockoutDuration);
		def.setLockoutFailureExpirationInterval(lockoutFailureExpirationInterval);
		return def;
	}

}
