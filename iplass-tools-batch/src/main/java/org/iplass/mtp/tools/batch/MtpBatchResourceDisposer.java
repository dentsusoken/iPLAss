/*
 * Copyright (C) 2023 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.tools.batch;

import java.util.Optional;

import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * バッチ処理用リソース破棄処理
 *
 * <p>
 * バッチ処理でリソースを破棄するための処理を集約します。
 * </p>
 *
 * @author SEKIGUCHI Naoya
 */
public class MtpBatchResourceDisposer {
	/** ロガー */
	private static final Logger LOG = LoggerFactory.getLogger(MtpBatchResourceDisposer.class);

	/**
	 * プライベートコンストラクタ
	 */
	private MtpBatchResourceDisposer() {
		// NOP
	}

	/**
	 * リソース破棄処理の Shutdown Hook 追加
	 *
	 * <p>
	 * GUIの場合のリソース破棄は、Runtime.addShutdownHook で実施する。
	 * {@link #disposeResource()} は shutdown hook スレッドの最後でコールする。
	 * </p>
	 *
	 * <p>
	 * 終了時点の処理になるので、インスタンスの状態に注意してメソッドを実装すること。
	 * </p>
	 */
	public static void addShutdownHookForDisposeResource(Runnable disposeProcess) {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			LOG.info("Begin resource dispose by shutdown hook.");
			try {
				Optional.ofNullable(disposeProcess).ifPresent(r -> r.run());
			} finally {
				// リソース破棄
				disposeResource();
			}
		}));
	}

	/**
	 * リソース破棄処理
	 */
	public static void disposeResource() {
		LOG.info("Destroy all services.");
		// 全サービス破棄。全サービス破棄に先立ち、TenantContextService 破棄する
		// （TenantContext で管理している TenantResource 実装クラスが Service クラスに依存している可能性がある為）
		ServiceRegistry.getRegistry().getService(TenantContextService.class).destroy();
		ServiceRegistry.getRegistry().destroyAllService();
	}
}
