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
 * テストユーティリティコンポジットクラス
 *
 * <p>
 * テスト設定は「設定ファイル＞クラス＞メソッド」で優先順位がある。
 * 本クラスではそれぞれの設定を管理し、当該項目に設定がなければ親を参照する為のクラス。
 * </p>
 *
 * @author SEKIGUCHI Naoya
 */
public class MTPTestConfigComposit extends MTPTestConfigImpl {
	/** 親設定項目 */
	private MTPTestConfig parent;

	/**
	 * コンストラクタ
	 * @param layer テスト設定を取得したレイヤー情報（ファイル、クラス、メソッドのいずれか）
	 * @param parent 親設定項目
	 */
	public MTPTestConfigComposit(Object layer, MTPTestConfig parent) {
		super(layer);
		this.parent = parent;
	}

	@Override
	public String getConfigFileName() {
		if (null != super.getConfigFileName()) {
			return super.getConfigFileName();
		}
		return parent.getConfigFileName();
	}

	@Override
	public String getTenantName() {
		if (null != super.getTenantName()) {
			return super.getTenantName();
		}
		return parent.getTenantName();
	}

	@Override
	public String getUserId() {
		if (null != super.getUserId()) {
			return super.getUserId();
		}
		return parent.getUserId();
	}

	@Override
	public String getPassword() {
		if (null != super.getPassword()) {
			return super.getPassword();
		}
		return parent.getPassword();
	}

	@Override
	public Boolean isRollbackTransaction() {
		if (null != super.isRollbackTransaction()) {
			return super.isRollbackTransaction();
		}
		return parent.isRollbackTransaction();
	}

	@Override
	public Boolean isNoAuth() {
		if (null != super.isNoAuth()) {
			return super.isNoAuth();
		}
		return parent.isNoAuth();
	}

	@Override
	public Boolean isGroovy() {
		if (null != super.isGroovy()) {
			return super.isGroovy();
		}
		return parent.isGroovy();
	}

}
