/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.spi.ServiceInitListener;

public class HttpClientConfig implements ServiceInitListener<Service> {

	private Integer connectionTimeout = Integer.valueOf(30000);
	private Integer soTimeout = Integer.valueOf(30000);
	private String proxyHost;
	private Integer proxyPort;
	private Integer poolingMaxTotal;
	private Integer poolingDefaultMaxPerRoute;
	private Integer poolingTimeToLive;
	private HttpClientBuilderFactory httpClientBuilderFactory;
	
	private CloseableHttpClient instance;

	public HttpClientBuilderFactory getHttpClientBuilderFactory() {
		return httpClientBuilderFactory;
	}

	public void setHttpClientBuilderFactory(HttpClientBuilderFactory httpClientBuilderFactory) {
		this.httpClientBuilderFactory = httpClientBuilderFactory;
	}

	public Integer getPoolingTimeToLive() {
		return poolingTimeToLive;
	}

	public void setPoolingTimeToLive(Integer poolingTimeToLive) {
		this.poolingTimeToLive = poolingTimeToLive;
	}

	public Integer getPoolingMaxTotal() {
		return poolingMaxTotal;
	}

	public void setPoolingMaxTotal(Integer poolingMaxTotal) {
		this.poolingMaxTotal = poolingMaxTotal;
	}

	public Integer getPoolingDefaultMaxPerRoute() {
		return poolingDefaultMaxPerRoute;
	}

	public void setPoolingDefaultMaxPerRoute(Integer poolingDefaultMaxPerRoute) {
		this.poolingDefaultMaxPerRoute = poolingDefaultMaxPerRoute;
	}

	public Integer getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(Integer connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public Integer getSoTimeout() {
		return soTimeout;
	}

	public void setSoTimeout(Integer soTimeout) {
		this.soTimeout = soTimeout;
	}

	public String getProxyHost() {
		return proxyHost;
	}

	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	public Integer getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(Integer proxyPort) {
		this.proxyPort = proxyPort;
	}

	public CloseableHttpClient getInstance() {
		return instance;
	}

	@Override
	public void inited(Service service, Config config) {
		
		if (httpClientBuilderFactory != null) {
			instance = httpClientBuilderFactory.create(service, config, this).build();
			
		} else {
			RequestConfig.Builder builder = RequestConfig.custom()
					.setConnectTimeout(connectionTimeout)
					.setSocketTimeout(soTimeout);
			if (proxyHost != null) {
				builder.setProxy(new HttpHost(proxyHost, proxyPort));
			}
			HttpClientBuilder hcBuilder = HttpClientBuilder.create().setDefaultRequestConfig(builder.build());
			if (poolingMaxTotal != null) {
				hcBuilder.setMaxConnTotal(poolingMaxTotal);
			}
			if (poolingDefaultMaxPerRoute != null) {
				hcBuilder.setMaxConnPerRoute(poolingDefaultMaxPerRoute);
			}
			if (poolingTimeToLive != null) {
				hcBuilder.setConnectionTimeToLive(poolingTimeToLive.longValue(), TimeUnit.MILLISECONDS);
			}
			
			instance = hcBuilder.build();
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
