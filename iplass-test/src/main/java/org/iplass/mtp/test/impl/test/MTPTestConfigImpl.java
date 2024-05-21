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
	/** コンフィグレイヤー */
	private Object layer;

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

	/**
	 * コンストラクタ
	 * @param layer テスト設定を取得したレイヤー情報（ファイル、クラス、メソッドのいずれか）
	 */
	public MTPTestConfigImpl(Object layer) {
		this.layer = layer;
	}

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

	@Override
	public String toString() {
		return new StringBuilder()
				.append("{")
				.append("\"configValue\": {")
				.append(stringValue("configFileName", getConfigFileName())).append(", ")
				.append(stringValue("tenantName", getTenantName())).append(", ")
				.append(stringValue("userId", getUserId())).append(", ")
				.append(stringValue("isRollbackTransaction", isRollbackTransaction())).append(", ")
				.append(stringValue("isNoAuth", isNoAuth())).append(", ")
				.append(stringValue("isGroovy", isGroovy()))
				.append("},")
				.append("\"layerValue\": {")
				.append(stringValue("layer", layer.toString())).append(", ")
				.append(stringValue("configFileName", configFileName)).append(", ")
				.append(stringValue("tenantName", tenantName)).append(", ")
				.append(stringValue("userId", userId)).append(", ")
				.append(stringValue("isRollbackTransaction", isRollbackTransaction)).append(", ")
				.append(stringValue("isNoAuth", isNoAuth)).append(", ")
				.append(stringValue("isGroovy", isGroovy))
				.append("}")
				.append("}")
				.toString();
	}

	/**
	 * toString 用文字列を生成する
	 *
	 * <p>
	 * <code>"key": "value"</code> のような値を返却する。 value が null の場合は <code>"key": null</code> を返却する。
	 * </p>
	 * @param <T> valueデータ型
	 * @param key キー
	 * @param value 値
	 * @return 連結文字列
	 */
	private <T> String stringValue(String key, T value) {
		StringBuilder s = new StringBuilder("\"").append(key).append("\": ");
		if (null == value) {
			s.append(value);
		} else if (value instanceof String) {
			s.append("\"").append(value).append("\"");
		} else {
			s.append(value);
		}

		return s.toString();
	}
}
