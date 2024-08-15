/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.cluster.channel.http;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.iplass.mtp.impl.async.AsyncTaskService;
import org.iplass.mtp.impl.cluster.ClusterService;
import org.iplass.mtp.impl.cluster.Message;
import org.iplass.mtp.impl.cluster.channel.MessageChannel;
import org.iplass.mtp.impl.cluster.channel.MessageReceiver;
import org.iplass.mtp.impl.core.config.BootstrapProps;
import org.iplass.mtp.impl.core.config.ServerEnv;
import org.iplass.mtp.impl.http.HttpClientConfig;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceConfigrationException;
import org.iplass.mtp.spi.ServiceInitListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Httpを介してメッセージ送受信するMessageChannel。
 * 到達順保証とかなしのシンプルな通信。
 *
 * @author K.Higuchi
 *
 */
public class HttpMessageChannel implements MessageChannel, ServiceInitListener<ClusterService> {
	private static Logger logger = LoggerFactory.getLogger(HttpMessageChannel.class);

	/** 自サーバーポートを指定するためのシステムプロパティキー */
	public static final String PORT_DEF_SYSTEM_PROP_NAME = "mtp.cluster.http.myportno";
	/** 自サーバー名を指定するためのシステムプロパティキー */
	public static final String SERVER_NAME_DEF_SYSTEM_PROP_NAME = "mtp.cluster.http.myservername";
	/** 通信用ネットワークインターフェースを指定する為のシステムプロパティキー */
	public static final String INTERFACE_NAME_DEF_SYSTEM_PROP_NAME = "mtp.cluster.http.myinterfacename";

	/** 非同期タスクサービス名 */
	public static final String ASYNC_TASK_SERVICE_NAME ="AsyncTaskServiceForHttpMessageChannel";

	/** メッセージ通信の認証用キーのパラメータ名。 */
	public static final String CERT_KEY_NAME = "certKey";
	/** eventNameのパラメータ名 */
	public static final String EVENT_NAME_NAME = "en";

	/** クラスタメンバのサーバのメッセージ通信用Url（Servletまでのパス） */
	private List<String> serverUrl;

	/** メッセージ通信時の認証キー。クラスタメンバ内では統一して定義する。 */
	private String certKey;

	private MessageReceiver messageHandler;

	private Integer connectionTimeout = HttpClientConfig.DEFAULT_CONNECTION_TIMEOUT;
	private Integer soTimeout = HttpClientConfig.DEFAULT_SO_TIMEOUT;
	private String proxyHost;
	private Integer proxyPort;
	private Integer poolingMaxTotal = HttpClientConfig.DEFAULT_POOLING_MAX_TOTAL;
	private Integer poolingDefaultMaxPerRoute = HttpClientConfig.DEFAULT_POOLING_DEFAULT_MAX_PER_ROUTE;
	private Integer poolingTimeToLive;

	/** メッセージ送信失敗時のリトライ回数。 */
	private Integer retryCount = Integer.valueOf(3);

	/** メッセージ送信失敗時のリトライ間隔（ms）。 */
	private Integer retryDelay = Integer.valueOf(10000);

	private HttpClientConfig httpClientConfig;
	private Timer timer = new Timer("httpMessageChannelRetryTimer", true);
	private AsyncTaskService asyncTaskService;

	private Charset contentEncoding = StandardCharsets.UTF_8;

	/**
	 * @return HttpClient
	 */
	HttpClient getHttpClient() {
		return httpClientConfig.getInstance();
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
	 * @return メッセージ送信失敗時のリトライ回数
	 */
	public Integer getRetryCount() {
		return retryCount;
	}

	/**
	 * @param retryCount メッセージ送信失敗時のリトライ回数
	 */
	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
	}

	/**
	 * @return メッセージ送信失敗時のリトライ間隔
	 */
	public Integer getRetryDelay() {
		return retryDelay;
	}

	/**
	 * @param retryDelay メッセージ送信失敗時のリトライ間隔
	 */
	public void setRetryDelay(Integer retryDelay) {
		this.retryDelay = retryDelay;
	}

	/**
	 * @return クラスタメンバのサーバのメッセージ通信用Url
	 */
	public List<String> getServerUrl() {
		return serverUrl;
	}

	/**
	 * @param serverUrl クラスタメンバのサーバのメッセージ通信用Url
	 */
	public void setServerUrl(List<String> serverUrl) {
		this.serverUrl = serverUrl;
	}

	/**
	 * @return 通信時の認証用のキー
	 */
	public String getCertKey() {
		return certKey;
	}

	/**
	 * @param certKey 通信時の認証用のキー
	 */
	public void setCertKey(String certKey) {
		this.certKey = certKey;
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

	@Override
	public void setMessageReceiver(MessageReceiver messageHandler) {
		this.messageHandler = messageHandler;
	}

	/**
	 * メッセージを受信する
	 * @param msg メッセージ
	 * @param certKeyFromOther メッセージ通信時の認証キー
	 */
	public void doReceiveMessage(Message msg, String certKeyFromOther) {
		if (certKey != null) {
			if (!certKey.equals(certKeyFromOther)) {
				return;
			}
		}
		messageHandler.receiveMessage(msg);
	}

	/**
	 * @return メッセージハンドラ
	 */
	public MessageReceiver getMessageHandler() {
		return messageHandler;
	}


	void doRetry(final SendMessageTask task) {
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				asyncTaskService.execute(task);
			}
		}, retryDelay);
	}

	int doSend(Message message, String url) throws IOException {
		HttpPost post = null;
		try {
			post = new HttpPost(url);

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			if (certKey != null) {
				params.add(new BasicNameValuePair(CERT_KEY_NAME, certKey));
			}
			params.add(new BasicNameValuePair(EVENT_NAME_NAME, message.getEventName()));
			if (message.getParam() != null) {
				for (Map.Entry<String, String> e: message.getParam().entrySet()) {
					params.add(new BasicNameValuePair(e.getKey(), e.getValue()));
				}
			}
			post.setEntity(new UrlEncodedFormEntity(params, contentEncoding));//TODO 設定？？

			return getHttpClient().execute(post, (response) -> {
				try {
					return response.getCode();
				} finally {
					EntityUtils.consume(response.getEntity());
				}
			});

		} catch (IOException e) {
			if (post != null) {
				post.abort();
			}
			throw e;
		} catch (RuntimeException e) {
			if (post != null) {
				post.abort();
			}
			throw e;
		}
	}


	@Override
	public void sendMessage(final Message message) {
		if (serverUrl != null && serverUrl.size() > 0) {
			for (String url: serverUrl) {
				asyncTaskService.execute(new SendMessageTask(message, url, retryCount, this));
			}
		}
	}

	private List<URI> toURI(List<String> urls) throws URISyntaxException {
		List<URI> ret = new ArrayList<>();
		for (String url: urls) {
			URI u = new URI(url);
			ret.add(u);
		}
		return ret;
	}

	private void addHostNameAndAddress(Set<String> list, InetAddress ia) {
		String hostName = ia.getHostName();
		if (ia.isLoopbackAddress()) {
			list.add("localhost");//hostNameがIPになってしまう
			list.add(ia.getHostAddress());
		} else {
			list.add(hostName);
			String address = ia.getHostAddress();
			if (hostName.contains(".")
					&& !address.equals(hostName)) {
				list.add(hostName.substring(0, hostName.indexOf('.')));
			}
			list.add(address);
		}
	}

	private String[] getMyServerNameAndAddress() throws SocketException {
		String defHostName = ServerEnv.getInstance().getProperty(SERVER_NAME_DEF_SYSTEM_PROP_NAME);
		if (defHostName == null) {
			defHostName = ServerEnv.getInstance().getProperty(BootstrapProps.SERVER_NAME);
		}
		if (defHostName != null) {
			return new String[]{defHostName};
		} else {
			Set<String> list = new LinkedHashSet<>();
			String networkInterfaceName = ServerEnv.getInstance().getProperty(INTERFACE_NAME_DEF_SYSTEM_PROP_NAME);
			if (networkInterfaceName == null) {
				networkInterfaceName = ServerEnv.getInstance().getProperty(BootstrapProps.NETWORK_INTERFACE_NAME);
			}
			NetworkInterface ni = null;
			NetworkInterface loopBack = null;
			if (networkInterfaceName != null) {
				ni = NetworkInterface.getByName(networkInterfaceName);
				if (ni == null) {
					throw new ServiceConfigrationException("networkInterfaceName:" + networkInterfaceName + " not found...");
				}
			} else {
				//Loopbackでない、先頭に定義されているNetworkInterfaceの定義を採用
				Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
				while (e.hasMoreElements()) {
					NetworkInterface forCheck = e.nextElement();
					if (forCheck.isLoopback()) {
						loopBack = forCheck;
					} else if (forCheck.isUp()) {
						ni = forCheck;
						break;
					}
				}
			}

			if (ni != null) {
				Enumeration<InetAddress> addresses = ni.getInetAddresses();
				while (addresses.hasMoreElements()) {
					addHostNameAndAddress(list, addresses.nextElement());
				}
			}
			if (loopBack != null) {
				Enumeration<InetAddress> addresses = loopBack.getInetAddresses();
				while (addresses.hasMoreElements()) {
					addHostNameAndAddress(list, addresses.nextElement());
				}
			}
			return list.toArray(new String[list.size()]);
		}
	}

	private int getMyPort() {
		String portStr = ServerEnv.getInstance().getProperty(PORT_DEF_SYSTEM_PROP_NAME);
		if (portStr == null) {
			return -1;
		} else {
			return Integer.parseInt(portStr);
		}
	}

	@Override
	public void inited(ClusterService service, Config config) {
		asyncTaskService = config.getDependentService(ASYNC_TASK_SERVICE_NAME);

		//自身をURLを除く。
		if (serverUrl != null) {
			try {
				List<String> newServerUrl = new ArrayList<>();
				List<URI> uriList = toURI(serverUrl);

				String[] myServerNames = getMyServerNameAndAddress();
				int port = getMyPort();
				for (int i = 0; i < uriList.size(); i++) {
					URI uri = uriList.get(i);
					boolean isMyHost = false;
					for (String myName: myServerNames) {
						if (uri.getHost().equals(myName)) {
							if (port == -1) {
								//ポート未指定の場合は、同一サーバ名での定義が1つの場合、それを自身のサーバ定義とみなす
								int portCount = 0;
								for (URI uu: uriList) {
									if (uu.getHost().equals(uri.getHost())) {
										portCount++;
									}
								}
								if (portCount > 1) {
									if (uri.getPort() == port) {
										isMyHost = true;
									}
								} else {
									isMyHost = true;
								}
							} else {
								if (uri.getPort() == port) {
									isMyHost = true;
								}
							}
						}
						if (isMyHost) {
							break;
						}
					}
					if (!isMyHost) {
						newServerUrl.add(serverUrl.get(i));
					} else {
						if (logger.isDebugEnabled()) {
							logger.debug(serverUrl.get(i) + " is my server(and port) url, so remove notifiy list.");
						}
					}
				}

				serverUrl = newServerUrl;

			} catch(SocketException | URISyntaxException e) {
				throw new ServiceConfigrationException("clusterservice setting is invalid:" + e.toString(), e);
			}
		}

		if (serverUrl != null && serverUrl.size() > 0) {
			HttpClientConfig httpClientConfig = new HttpClientConfig();
			httpClientConfig.setConnectionTimeout(connectionTimeout);
			httpClientConfig.setSoTimeout(soTimeout);

			if (proxyHost != null) {
				httpClientConfig.setProxyHost(proxyHost);
				httpClientConfig.setProxyPort(proxyPort);
			}

			if (poolingMaxTotal != null) {
				httpClientConfig.setPoolingMaxTotal(poolingMaxTotal);
			}
			if (poolingDefaultMaxPerRoute != null) {
				httpClientConfig.setPoolingDefaultMaxPerRoute(poolingDefaultMaxPerRoute);
			}
			if (poolingTimeToLive != null) {
				httpClientConfig.setPoolingTimeToLive(poolingTimeToLive);
			}

			httpClientConfig.inited(service, config);
			this.httpClientConfig = httpClientConfig;
		}
	}

	@Override
	public void destroyed() {
		if (httpClientConfig != null) {
			httpClientConfig.destroyed();
			httpClientConfig = null;
		}
		timer.cancel();
	}
}
