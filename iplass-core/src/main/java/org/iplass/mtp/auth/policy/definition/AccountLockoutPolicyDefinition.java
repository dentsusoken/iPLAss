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

import java.io.Serializable;

/**
 * アカウントのロックアウト制御のポリシー定義。
 * 
 * @author K.Higuchi
 *
 */
public class AccountLockoutPolicyDefinition implements Serializable {
	private static final long serialVersionUID = -5956695743833838501L;
	
	/** アカウントロックアウトするまでの失敗回数。0はロックアウトしない */
	private int lockoutFailureCount = 10;
	/** ロックアウトしている期間（分）。0は無制限 */
	private int lockoutDuration;
	/** 失敗回数を記憶しておく期間（分） 。0は無制限*/
	private int lockoutFailureExpirationInterval;
	
	/**
	 *  アカウントロックアウトするまでの失敗回数。0はロックアウトしない。
	 *  設定可能な最大値は99。
	 * 
	 * @return
	 */
	public int getLockoutFailureCount() {
		return lockoutFailureCount;
	}
	/**
	 * see {@link #getLockoutFailureCount()}
	 * @param lockoutFailureCount
	 */
	public void setLockoutFailureCount(int lockoutFailureCount) {
		this.lockoutFailureCount = lockoutFailureCount;
	}
	/**
	 * ロックアウトしている期間（分）。0は無制限。
	 * 
	 * @return
	 */
	public int getLockoutDuration() {
		return lockoutDuration;
	}
	/**
	 * see {@link #getLockoutDuration()}
	 * @param lockoutDuration
	 */
	public void setLockoutDuration(int lockoutDuration) {
		this.lockoutDuration = lockoutDuration;
	}
	/**
	 * 失敗回数（{@link #getLockoutFailureCount() lockoutFailureCount}）を記憶しておく期間（分） 。
	 * 0は無制限。
	 * 
	 * @return
	 */
	public int getLockoutFailureExpirationInterval() {
		return lockoutFailureExpirationInterval;
	}
	/**
	 * see {@link #getLockoutFailureExpirationInterval()}
	 * @param lockoutFailureExpirationInterval
	 */
	public void setLockoutFailureExpirationInterval(
			int lockoutFailureExpirationInterval) {
		this.lockoutFailureExpirationInterval = lockoutFailureExpirationInterval;
	}

}
