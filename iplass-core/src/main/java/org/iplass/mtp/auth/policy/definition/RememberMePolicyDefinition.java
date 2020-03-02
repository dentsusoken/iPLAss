/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
 * RememberMe（ログインしたままにする）機能に関する設定。
 *
 * @author K.Higuchi
 *
 */
public class RememberMePolicyDefinition implements Serializable {
	private static final long serialVersionUID = -6269024681108556061L;

	private long lifetimeMinutes; //単位：分、0はRememberMe機能を利用しない。
	private boolean absoluteLifetime;

	/**
	 * 有効期間を絶対値にするか否か。<br>
	 * true:id/passによるログインを行った日時からlifetimeMinutes期間有効<br>
	 * false:最終アクセス（RememberMeトークンでの認証処理）の日時からlifetimeMinutes期間有効<br>
	 * @return
	 */
	public boolean isAbsoluteLifetime() {
		return absoluteLifetime;
	}

	/**
	 * @see {@link #isAbsoluteLifetime()}
	 * @param absoluteLifetime
	 */
	public void setAbsoluteLifetime(boolean absoluteLifetime) {
		this.absoluteLifetime = absoluteLifetime;
	}

	/**
	 * 有効期間（分）を取得。
	 * 0の場合は、RememberMe機能を利用しないの意味となる。
	 *
	 * @return
	 */
	public long getLifetimeMinutes() {
		return lifetimeMinutes;
	}

	/**
	 * @see {@link #getLifetimeMinutes()}
	 * @param lifetimeMinutes
	 */
	public void setLifetimeMinutes(long lifetimeMinutes) {
		this.lifetimeMinutes = lifetimeMinutes;
	}

}
