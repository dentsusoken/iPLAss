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
package org.iplass.mtp.impl.infinispan.task;

import org.infinispan.manager.EmbeddedCacheManager;
import org.iplass.mtp.impl.infinispan.InfinispanService;
import org.iplass.mtp.spi.ServiceRegistry;

/**
 * Infinispan ユーティリティ
 */
class InfinispanUtil {
	/**
	 * プライベートコンストラクタ
	 */
	private InfinispanUtil() {
	}

	/**
	 * 処理実行中ノードを取得する
	 * @return 実行中ノード
	 */
	public static String getExecutionNode() {
		return InfinispanUtil.getNode(ServiceRegistry.getRegistry().getService(InfinispanService.class).getCacheManager());
	}


	/**
	 * ノード情報を取得する
	 * @param manager キャッシュマネージャー
	 * @return ノード情報
	 */
	private static String getNode(EmbeddedCacheManager manager) {
		return manager.getAddress() == null ? null : manager.getAddress().toString();
	}

}