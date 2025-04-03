/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.warmup;

/**
 * ウォームアップステータス
 * <p>
 * ウォームアップ状態を表す列挙型です。
 * ウォームアップ状態は、{@link org.iplass.mtp.impl.warmup.WarmupService} で管理されます。
 * 状態は次のように遷移します。
 * </p>
 *
 * <h3>状態遷移</h3>
 * <pre>
 * NOT_PROCESSING
 *   |
 *   +--> PROCESSING
 *          |
 *          +--> COMPLETE
 *          |
 *          +--> FAILED
 * </pre>
 *
 * @author SEKIGUCHI Naoya
 */
public enum WarmupStatus {
	/** 処理されていない */
	NOT_PROCESSING(0, "not_processing"),
	/** 処理中 */
	PROCESSING(1, "processing"),
	/** 完了（正常終了） */
	COMPLETE(2, "complete"),
	/** 完了（失敗） */
	FAILED(2, "failed");

	/** 重み */
	private int weight;
	/** ステータス名 */
	private String status;

	/**
	 * コンストラクタ
	 * @param weight 重み
	 * @param status ステータス名
	 */
	private WarmupStatus(int weight, String status) {
		this.weight = weight;
		this.status = status;
	}

	/**
	 * ステータス名を取得する
	 * @return ステータス名
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * 次のステータスに変更可能か判定する
	 * <p>
	 * ステータス変更の判定は、重みによって判断されます。
	 * 自身の重みよりも大きいものに変更可能です。
	 * </p>
	 *
	 * @param nextStatus 変更先ステータス
	 * @return 変更可能な場合は true を返却する。
	 */
	public boolean canChange(WarmupStatus nextStatus) {
		if (this == nextStatus) {
			return false;
		}

		return this.weight < nextStatus.weight;
	}
}
