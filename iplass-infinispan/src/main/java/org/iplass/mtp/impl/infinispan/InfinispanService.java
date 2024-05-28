/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.infinispan.Cache;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfigurationChildBuilder;
import org.infinispan.configuration.parsing.ConfigurationBuilderHolder;
import org.infinispan.configuration.parsing.ParserRegistry;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.spi.ServiceConfigrationException;

/**
 * Infinispan Service
 *
 * <p>
 * 本サービスを利用し {@link #destroy} を実施しない場合に File Store Cache のロックが外れずに再実行できなくなる。
 * 再実行できなくなってしまった場合は、キャッシュのロックファイルを削除すること。ファイルパスは infinispan.xml を参照する。
 * </p>
 *
 * @author SEKIGUCHI Naoya
 */
public class InfinispanService implements Service {

	private String configurationFile;
	private EmbeddedCacheManager cm;
	//クラスタ管理用（forming用、通信用）Cache
	private Cache<Object, Object> defaultCache;

	@Override
	public void init(Config config) {
		configurationFile = config.getValue("configurationFile");

		if (configurationFile == null) {
			GlobalConfiguration globalConfig = initGlobalConfigurationBuilder(new GlobalConfigurationBuilder(), config).build();
			cm = new DefaultCacheManager(globalConfig);

		} else {
			try {
				ConfigurationBuilderHolder holder = new ParserRegistry().parseFile(configurationFile);
				initGlobalConfigurationBuilder(holder.getGlobalConfigurationBuilder(), config);
				cm = new DefaultCacheManager(holder, true);

			} catch (IOException e) {
				throw new ServiceConfigrationException(e);
			}
		}
		defaultCache = cm.getCache();
	}

	@Override
	public void destroy() {
		if (cm != null) {
			cm.stop();
			cm = null;
		}
	}

	/**
	 * Infinispan キャッシュマネージャーを取得する
	 * @return Infinispan キャッシュマネージャー
	 */
	public EmbeddedCacheManager getCacheManager() {
		return cm;
	}

	/**
	 * Infinispan デフォルトキャッシュを取得する
	 * @return Infinispan デフォルトキャッシュ
	 */
	public Cache<Object, Object> getDefaultCache() {
		return defaultCache;
	}

	/**
	 * Infinispan GlobalConfigurationBuilder の初期設定を行う
	 *
	 * @param <T> ビルダークラス
	 * @param builder ビルダー
	 * @param config iPLAss service設定
	 * @return ビルダー
	 */
	private <T extends GlobalConfigurationChildBuilder> T initGlobalConfigurationBuilder(T builder, Config config) {
		// infinispan.xml /infinispan/cache-container/serialization/allow-list/class の設定追加
		List<String> allowClassList = config.getValues("infinispan.serialization.allowList.class", String.class, Collections.emptyList());
		if (0 < allowClassList.size()) {
			builder.serialization().allowList().addClasses(allowClassList.toArray(new String[allowClassList.size()]));
		}

		// infinispan.xml /infinispan/cache-container/serialization/allow-list/regex の設定追加
		List<String> allowRegexList = config.getValues("infinispan.serialization.allowList.regex", String.class, Collections.emptyList());
		if (0 < allowRegexList.size()) {
			builder.serialization().allowList().addRegexps(allowRegexList.toArray(new String[allowRegexList.size()]));
		}

		return builder;
	}
}
