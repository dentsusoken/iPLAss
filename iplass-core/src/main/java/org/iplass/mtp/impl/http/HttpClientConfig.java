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

package org.iplass.mtp.impl.http;

import java.io.IOException;

import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.spi.ServiceInitListener;

/**
 * HttpClinet 設定
 */
public class HttpClientConfig implements ServiceInitListener<Service> {
	/** コネクションタイムアウト ミリ秒 デフォルト値 */
	public static final Integer DEFAULT_CONNECTION_TIMEOUT = Integer.valueOf(30_000);
	/** ソケットタイムアウト ミリ秒 デフォルト値 */
	public static final Integer DEFAULT_SO_TIMEOUT = Integer.valueOf(30_000);
	/** httpコネクションのプール最大数 デフォルト値 */
	public static final Integer DEFAULT_POOLING_MAX_TOTAL = Integer.valueOf(20);
	/** ドメイン単位のhttpコネクションの最大数 デフォルト値 */
	public static final Integer DEFAULT_POOLING_DEFAULT_MAX_PER_ROUTE = Integer.valueOf(2);

	/** コネクションタイムアウト ミリ秒 */
	private Integer connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;
	/** ソケットタイムアウト ミリ秒 */
	private Integer soTimeout = DEFAULT_SO_TIMEOUT;
	/** プロキシホスト */
	private String proxyHost;
	/** プロキシポート */
	private Integer proxyPort;
	/** httpコネクションのプール最大数 */
	private Integer poolingMaxTotal = DEFAULT_POOLING_MAX_TOTAL;
	/** ドメイン単位のhttpコネクションの最大数 */
	private Integer poolingDefaultMaxPerRoute = DEFAULT_POOLING_DEFAULT_MAX_PER_ROUTE;
	/** プールされているhttpコネクションの生存期間 ミリ秒 */
	private Integer poolingTimeToLive;
	/** HttpClientBuilder生成機能 */
	private HttpClientBuilderFactory httpClientBuilderFactory;

	/** HttpClientインスタンス */
	private CloseableHttpClient instance;

	/**
	 * @return HttpClientBuilder生成機能
	 */
	public HttpClientBuilderFactory getHttpClientBuilderFactory() {
		return httpClientBuilderFactory;
	}

	/**
	 * @param httpClientBuilderFactory HttpClientBuilder生成機能
	 */
	public void setHttpClientBuilderFactory(HttpClientBuilderFactory httpClientBuilderFactory) {
		this.httpClientBuilderFactory = httpClientBuilderFactory;
	}

	/**
	 * @return プールされているhttpコネクションの生存期間 ミリ秒
	 */
	public Integer getPoolingTimeToLive() {
		return poolingTimeToLive;
	}

	/**
	 * @param poolingTimeToLive プールされているhttpコネクションの生存期間 ミリ秒
	 */
	public void setPoolingTimeToLive(Integer poolingTimeToLive) {
		this.poolingTimeToLive = poolingTimeToLive;
	}

	/**
	 * @return httpコネクションのプール最大数
	 */
	public Integer getPoolingMaxTotal() {
		return poolingMaxTotal;
	}

	/**
	 * @param poolingMaxTotal httpコネクションのプール最大数
	 */
	public void setPoolingMaxTotal(Integer poolingMaxTotal) {
		this.poolingMaxTotal = poolingMaxTotal;
	}

	/**
	 * @return ドメイン単位のhttpコネクションの最大数
	 */
	public Integer getPoolingDefaultMaxPerRoute() {
		return poolingDefaultMaxPerRoute;
	}

	/**
	 * @param poolingDefaultMaxPerRoute ドメイン単位のhttpコネクションの最大数
	 */
	public void setPoolingDefaultMaxPerRoute(Integer poolingDefaultMaxPerRoute) {
		this.poolingDefaultMaxPerRoute = poolingDefaultMaxPerRoute;
	}

	/**
	 * @return コネクションタイムアウト ミリ秒
	 */
	public Integer getConnectionTimeout() {
		return connectionTimeout;
	}

	/**
	 * @param connectionTimeout コネクションタイムアウト ミリ秒
	 */
	public void setConnectionTimeout(Integer connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	/**
	 * @return ソケットタイムアウト ミリ秒
	 */
	public Integer getSoTimeout() {
		return soTimeout;
	}

	/**
	 * @param soTimeout ソケットタイムアウト ミリ秒
	 */
	public void setSoTimeout(Integer soTimeout) {
		this.soTimeout = soTimeout;
	}

	/**
	 * @return プロキシホスト
	 */
	public String getProxyHost() {
		return proxyHost;
	}

	/**
	 * @param proxyHost プロキシホスト
	 */
	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	/**
	 * @return プロキシポート
	 */
	public Integer getProxyPort() {
		return proxyPort;
	}

	/**
	 * @param proxyPort プロキシポート
	 */
	public void setProxyPort(Integer proxyPort) {
		this.proxyPort = proxyPort;
	}

	/**
	 * @return HttpClientインスタンス
	 */
	public CloseableHttpClient getInstance() {
		return instance;
	}

	@Override
	public void inited(Service service, Config config) {
		if (httpClientBuilderFactory != null) {
			instance = httpClientBuilderFactory.create(service, config, this).build();

		} else {
			// --------------------------------------------------------------
			// ConnectionConfig
			var connectionConfigBuilder = ConnectionConfig.custom()
					.setConnectTimeout(Timeout.ofMilliseconds(connectionTimeout))
					.setSocketTimeout(Timeout.ofMilliseconds(soTimeout));

			if (poolingTimeToLive != null) {
				connectionConfigBuilder.setTimeToLive(TimeValue.ofMilliseconds(poolingTimeToLive));
			}

			// --------------------------------------------------------------
			// PoolingHttpClientConnectionManager
			var poolingConnectionManagerBuilder = PoolingHttpClientConnectionManagerBuilder.create()
					.setDefaultConnectionConfig(connectionConfigBuilder.build());

			if (poolingMaxTotal != null) {
				poolingConnectionManagerBuilder.setMaxConnTotal(poolingMaxTotal);
			}
			if (poolingDefaultMaxPerRoute != null) {
				poolingConnectionManagerBuilder.setMaxConnPerRoute(poolingDefaultMaxPerRoute);
			}

			// --------------------------------------------------------------
			// HttpClient
			var httpClinetBuilder = HttpClientBuilder.create();

			if (proxyHost != null) {
				httpClinetBuilder.setProxy(new HttpHost(proxyHost, proxyPort));
			}

			instance = httpClinetBuilder
					.setConnectionManager(poolingConnectionManagerBuilder.build())
					.build();
		}
	}

	@Override
	public void destroyed() {
		try {
			instance.close();
		} catch (IOException e) {
		}
		instance = null;
	}

}
