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

package org.iplass.mtp.view.generic;

import org.iplass.mtp.entity.LoadOption;

/**
 * LoadEntityInterrupterの実行結果を保持するContextです。
 * @author lis3wg
 *
 */
public class LoadEntityContext {

	/**
	 * ロード時のオプション
	 */
	private LoadOption loadOption;

	/**
	 * 特権実行を行うか
	 */
	private boolean doPrivileged;

	/**
	 * コンストラクタ
	 * @param loadOption ロード時のオプション
	 */
	public LoadEntityContext(LoadOption loadOption) {
		this.loadOption = loadOption;
	}

	/**
	 * コンストラクタ
	 * @param loadOption ロード時のオプション
	 * @param doPrivileged 特権実行を行うか
	 */
	public LoadEntityContext(LoadOption loadOption, boolean doPrivileged) {
		this.loadOption = loadOption;
		this.doPrivileged = doPrivileged;
	}

	/**
	 * ロード時のオプションを取得します。
	 * @return ロード時のオプション
	 */
	public LoadOption getLoadOption() {
	    return loadOption;
	}

	/**
	 * ロード時のオプションを設定します。
	 * @param loadOption ロード時のオプション
	 */
	public void setLoadOption(LoadOption loadOption) {
	    this.loadOption = loadOption;
	}

	/**
	 * 特権実行を行うかを取得します。
	 * @return 特権実行を行うか
	 */
	public boolean isDoPrivileged() {
	    return doPrivileged;
	}

	/**
	 * 特権実行を行うかを設定します。
	 * @param doPrivileged 特権実行を行うか
	 */
	public void setDoPrivileged(boolean doPrivileged) {
	    this.doPrivileged = doPrivileged;
	}
}
