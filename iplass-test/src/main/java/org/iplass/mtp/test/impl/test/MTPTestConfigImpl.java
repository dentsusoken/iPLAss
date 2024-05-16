/*
 * Copyright (C) 2024 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.test.impl.test;

/**
 * テストユーティリティ設定クラス
 *
 * @author SEKIGUCHI Naoya
 */
public class MTPTestConfigImpl implements MTPTestConfig {
	/** 設定ファイル名 */
	private String configFileName;
	/** テナント名 */
	private String tenantName;
	/** ログインユーザーID */
	private String userId;
	/** ログインパスワード */
	private String password;
	/** テストが groovy であるか */
	private Boolean isGroovy;
	/** トランザクションをロールバックするか */
	private Boolean isRollbackTransaction;
	/** &#64;NoAuth が付与されているか */
	private Boolean isNoAuth;

	@Override
	public String getConfigFileName() {
		return configFileName;
	}

	/**
	 * 設定ファイル名を設定する
	 * @param configFileName 設定ファイル名
	 */
	public void setConfigFileName(String configFileName) {
		this.configFileName = configFileName;
	}

	@Override
	public String getTenantName() {
		return tenantName;
	}

	/**
	 * テナント名を設定する
	 * @param tenantName テナント名
	 */
	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	@Override
	public String getUserId() {
		return userId;
	}

	/**
	 * ログインユーザーIDを設定する
	 * @param userId ログインユーザーID
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String getPassword() {
		return password;
	}

	/**
	 * ログインパスワードを設定する
	 * @param password ログインパスワード
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public Boolean isRollbackTransaction() {
		return isRollbackTransaction;
	}

	/**
	 * トランザクションをロールバックするかを設定する
	 * @param isRollbackTransaction トランザクションをロールバックするか（ロールバックする場合 true）
	 */
	public void setRollbackTransaction(Boolean isRollbackTransaction) {
		this.isRollbackTransaction = isRollbackTransaction;
	}

	@Override
	public Boolean isNoAuth() {
		return isNoAuth;
	}

	/**
	 * &#64;NoAuth が付与されているか
	 * @param isNoAuth &#64;NoAuth が付与されているか（&#64;NoAuth が付与されている場合 true）
	 */
	public void setNoAuth(Boolean isNoAuth) {
		this.isNoAuth = isNoAuth;
	}

	@Override
	public Boolean isGroovy() {
		return isGroovy;
	}

	/**
	 * テストが groovy であるかを設定する
	 * @param isGroovy テストが groovy であるか
	 */
	public void setGroovy(Boolean isGroovy) {
		this.isGroovy = isGroovy;
	}
}
