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
package org.iplass.mtp.impl.async.internal;

/**
 * 内部利用 非同期タスクサービス
 * <p>
 * 内部利用専用の非同期タスクサービスを取得するサービス名を定義します。
 * 取得できるサービスは、テナントを決定しない状態で利用する非同期タスクサービスです。
 * </p>
 *
 * @author SEKIGUCHI Naoya
 */
public class InternalAsyncTaskServiceConstant {
	/** サービス名 */
	public static final String SERVICE_NAME = "internalAsyncTaskService";

	/**
	 * プライベートコンストラクタ
	 */
	private InternalAsyncTaskServiceConstant() {
	}
}
