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
package org.iplass.mtp.impl.infinispan;

/**
 * Infinispan固定値クラス
 *
 * @author SEKIGUCHI Naoya
 */
public final class InfinispanConstants {
	// クラス名の混同を避けるため LogMDC として定義
	/**
	 * ログ MDC用固定値
	 */
	public static final class LogMDC {
		/** Infinispan要求ID */
		public static final String REQUEST_ID = "infinispanRequestId";

		/**
		 * プライベートコンストラクタ
		 */
		private LogMDC() {
		}
	}

	/**
	 * プライベートコンストラクタ
	 */
	private InfinispanConstants() {
	}
}
