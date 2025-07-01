/*
 * Copyright (C) 2020 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.webhook;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLException;

import org.apache.commons.codec.binary.Base64;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPatch;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.SystemException;
import org.iplass.mtp.async.AsyncTaskManager;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.impl.definition.AbstractTypedMetaDataService;
import org.iplass.mtp.impl.definition.DefinitionMetaDataTypeMap;
import org.iplass.mtp.impl.definition.DefinitionNameChecker;
import org.iplass.mtp.impl.http.HttpClientConfig;
import org.iplass.mtp.impl.webhook.endpoint.MetaWebhookEndpoint.WebhookEndpointRuntime;
import org.iplass.mtp.impl.webhook.endpoint.WebhookEndpointService;
import org.iplass.mtp.impl.webhook.template.MetaWebhookTemplate;
import org.iplass.mtp.impl.webhook.template.MetaWebhookTemplate.WebhookTemplateRuntime;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.webhook.Webhook;
import org.iplass.mtp.webhook.WebhookHeader;
import org.iplass.mtp.webhook.WebhookResponse;
import org.iplass.mtp.webhook.endpoint.WebhookAuthenticationType;
import org.iplass.mtp.webhook.template.definition.WebhookTemplateDefinition;
import org.iplass.mtp.webhook.template.definition.WebhookTemplateDefinitionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * WebhookService
 */
public class WebhookServiceImpl extends AbstractTypedMetaDataService<MetaWebhookTemplate, WebhookTemplateRuntime>
implements WebhookService {
	/** webhook template メタデータパス */
	public static final String WEBHOOK_TEMPLATE_META_PATH = "/webhook/template/";
	private static final String WEBHOOK_ISRETRY = "retry";
	private static final String WEBHOOK_RETRY_MAXIMUMATTEMPTS = "retryMaximumAttempts";
	private static final String WEBHOOK_RETRY_INTERVAL = "retryInterval";
	private static final String WEBHOOK_HMACTOKEN_ALGORITHM = "hmacHashAlgorithm";
	private static final String WEBHOOK_HMACTOKEN_DEFAULTNAME = "hmacTokenDefaultName";
	private static final String WEBHOOK_HTTP_CLIENT_CONFIG = "httpClientConfig";
	private static final String WEBHOOK_DEFAULT_HMACALGORITHM = "HmacSHA256";
	private static final String WEBHOOK_HMAC_TOKEN_DEFAULT_NAME = "X-IPLASS-HMAC";
	private static Logger logger = LoggerFactory.getLogger(WebhookServiceImpl.class);

	/** Webhook コンテンツ文字コード */
	private Charset webhookContentCharset = StandardCharsets.UTF_8;

	/**
	 * type map
	 */
	public static class TypeMap extends DefinitionMetaDataTypeMap<WebhookTemplateDefinition, MetaWebhookTemplate> {
		/**
		 * コンストラクタ
		 */
		public TypeMap() {
			super(getFixedPath(), MetaWebhookTemplate.class, WebhookTemplateDefinition.class);
		}

		@Override
		public TypedDefinitionManager<WebhookTemplateDefinition> typedDefinitionManager() {
			return ManagerLocator.getInstance().getManager(WebhookTemplateDefinitionManager.class);
		}

		@Override
		protected DefinitionNameChecker createDefinitionNameChecker() {
			return DefinitionNameChecker.getPathSlashDefinitionNameChecker();
		}
	}

	/**
	 * webhook template メタデータパスを取得する
	 * @return webhook template メタデータパス
	 */
	public static String getFixedPath() {
		return WEBHOOK_TEMPLATE_META_PATH;
	}

	private AsyncTaskManager atm;
	private WebhookEndpointService wheps;
	private boolean webhookIsRetry;
	private int webhookRetryMaximumAttpempts;
	private int webhookRetryInterval;
	private String webhookHmacHashAlgorithm;
	private String webhookHmacTokenDefaultName;
	private HttpClientConfig webhookHttpClientConfig;
	private CloseableHttpClient webhookHttpClient;

	@Override
	public void init(Config config) {
		webhookHttpClientConfig = new HttpClientConfig();
		for (String name : config.getNames()) {
			switch (name) {
			case WEBHOOK_ISRETRY:
				String tempIsRetry = config.getValue(WEBHOOK_ISRETRY);
				if ("true".equals(tempIsRetry.replaceAll("\\s", "").toLowerCase())) {
					webhookIsRetry = true;
				} else {
					webhookIsRetry = false;
				}
				break;
			case WEBHOOK_RETRY_MAXIMUMATTEMPTS:
				webhookRetryMaximumAttpempts = Integer.valueOf(config.getValue(WEBHOOK_RETRY_MAXIMUMATTEMPTS));
				break;
			case WEBHOOK_RETRY_INTERVAL:
				webhookRetryInterval = Integer.valueOf(config.getValue(WEBHOOK_RETRY_INTERVAL));
				break;
			case WEBHOOK_HMACTOKEN_ALGORITHM:
				webhookHmacHashAlgorithm = config.getValue(WEBHOOK_HMACTOKEN_ALGORITHM);
				if (webhookHmacHashAlgorithm == null || webhookHmacHashAlgorithm.replaceAll("\\s", "").isEmpty()) {
					webhookHmacHashAlgorithm = WEBHOOK_DEFAULT_HMACALGORITHM;
				}
				break;
			case WEBHOOK_HMACTOKEN_DEFAULTNAME:
				webhookHmacTokenDefaultName = config.getValue(WEBHOOK_HMACTOKEN_DEFAULTNAME);
				if (webhookHmacTokenDefaultName == null
						|| webhookHmacTokenDefaultName.replaceAll("\\s", "").isEmpty()) {
					webhookHmacTokenDefaultName = WEBHOOK_HMAC_TOKEN_DEFAULT_NAME;
				}
				break;
			case WEBHOOK_HTTP_CLIENT_CONFIG:
				webhookHttpClientConfig = config.getValue(WEBHOOK_HTTP_CLIENT_CONFIG, HttpClientConfig.class);
				if (webhookHttpClientConfig == null) {
					webhookHttpClientConfig = new HttpClientConfig();
				}
				break;
			}
		}
		webhookHttpClientConfig.inited(this, config);
		atm = ManagerLocator.getInstance().getManager(AsyncTaskManager.class);
		wheps = ServiceRegistry.getRegistry().getService(WebhookEndpointService.class);
		initWebhookHttpClient();
	}

	private void initWebhookHttpClient() {
		if (webhookHttpClient == null) {
			webhookHttpClient = webhookHttpClientConfig.getInstance();
		}
	}

	@Override
	public void destroy() {
		try {
			webhookHttpClient.close();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public Class<MetaWebhookTemplate> getMetaDataType() {
		return MetaWebhookTemplate.class;
	}

	@Override
	public Class<WebhookTemplateRuntime> getRuntimeType() {
		return WebhookTemplateRuntime.class;
	}

	@Override
	public Webhook getWebhookByName(String webhookDefinitionName, Map<String, Object> binding) {
		WebhookTemplateRuntime webhookRuntime = this.getRuntimeByName(webhookDefinitionName);
		Webhook webhook = webhookRuntime.createWebhook(binding);
		return webhook;
	}

	/**
	 * 同期sendWebhook
	 *
	 */
	@Override
	public void sendWebhookSync(Webhook webhook) {
		sendWebhook(webhook);
	}

	/**
	 * 非同期sendWebhook
	 *
	 * <p>
	 * AsyncTaskManagerのローカルスレッドを使用する
	 * </p>
	 */
	@Override
	public void sendWebhookAsync(Webhook webhook) {
		atm.executeOnThread(new WebhookCallable(webhook));
	}

	@Override
	public Webhook generateWebhook(String webhookDefinitionName, Map<String, Object> binding,
			String endpointDefinitionName) {
		WebhookTemplateRuntime webhookRuntime = this.getRuntimeByName(webhookDefinitionName);
		WebhookEndpointRuntime endpointRuntime = wheps.getRuntimeByName(endpointDefinitionName);

		if (endpointRuntime == null) {
			throw new SystemException("Endpoint Template:" + endpointDefinitionName + " not found");
		}
		if (webhookRuntime == null) {
			throw new SystemException("WebhookTemplate:" + webhookDefinitionName + " not found");
		}

		Webhook webhook = webhookRuntime.createWebhook(binding);
		webhook.setEndpoint(endpointRuntime.createWebhookEndpoint(binding));
		return webhook;
	}

	private void sendWebhook(Webhook webhook) {
		try {
			if (webhook.getHttpMethod() == null) {
				if (logger.isDebugEnabled()) {
					logger.debug("Webhook: request method undefined. Post will be used.");
				}
			}
			String url = webhook.getEndpoint().getUrl() + webhook.getPathAndQuery();
			HttpUriRequest httpRequest = getReqestMethodObject(webhook.getHttpMethod(), new URI(url));
			logger.debug("Webhook: Endpoint = {}, pathAndQuery = {}, httpMethod = {}",
					webhook.getEndpoint().getUrl(), webhook.getPathAndQuery(), webhook.getHttpMethod());

			// payload
			if (isEnclosingRequest(httpRequest)) {
				if (webhook.getPayloadContent() != null && !webhook.getPayloadContent().replaceAll("\\s", "").isEmpty()) {
					ContentType contentType = ContentType.create(webhook.getContentType()).withCharset(webhookContentCharset);
					StringEntity se = new StringEntity(webhook.getPayloadContent(), contentType);
					httpRequest.setEntity(se);
				}
			} else {
				if (webhook.getContentType() != null && !webhook.getContentType().replaceAll("\\s", "").isEmpty()) {
					httpRequest.setHeader(HttpHeaders.CONTENT_TYPE, webhook.getContentType());
				}
			}

			// and headers
			if (webhook.getHeaders() != null) {
				for (WebhookHeader headerEntry : webhook.getHeaders()) {
					httpRequest.setHeader(headerEntry.getKey(), headerEntry.getValue());
				}
			}
			if (webhook.getEndpoint() != null) {
				// hmac
				if (webhook.getEndpoint().getHmacKey() != null) {
					if (!webhook.getEndpoint().getHmacKey().isEmpty()) {
						String hmacHeader;
						if (webhook.getEndpoint().getHmacHashHeader() == null
								|| webhook.getEndpoint().getHmacHashHeader().replaceAll("\\s", "").isEmpty()) {
							hmacHeader = webhookHmacTokenDefaultName;
						} else {
							hmacHeader = webhook.getEndpoint().getHmacHashHeader().replaceAll("\\s", "");
						}
						httpRequest.setHeader(hmacHeader,
								this.getHmacSha256(webhook.getEndpoint().getHmacKey(), webhook.getPayloadContent()));
					}
				}
				// headerのauthorizationでの認証情報
				if (webhook.getEndpoint().getHeaderAuthorizationType() != null) {
					String scheme = "";
					String authContent = "";
					if (WebhookAuthenticationType.BEARER.equals(webhook.getEndpoint().getHeaderAuthorizationType())) {
						scheme = "Bearer";
						authContent = webhook.getEndpoint().getHeaderAuthorizationContent();
					} else if (WebhookAuthenticationType.BASIC
							.equals(webhook.getEndpoint().getHeaderAuthorizationType())) {
						scheme = "Basic";
						if (webhook.getEndpoint().getHeaderAuthorizationContent() != null && !webhook.getEndpoint()
								.getHeaderAuthorizationContent().replaceAll("\\s+", "").isEmpty()) {
							authContent = Base64.encodeBase64String(
									webhook.getEndpoint().getHeaderAuthorizationContent().getBytes());
						}
					} else if (WebhookAuthenticationType.CUSTOM
							.equals(webhook.getEndpoint().getHeaderAuthorizationType())) {
						scheme = "Custom";
						if (webhook.getEndpoint().getHeaderAuthCustomTypeName() != null) {
							String authTypeName = webhook.getEndpoint().getHeaderAuthCustomTypeName().replace("\n", "")
									.replaceAll("\\s+", "");
							if (!authTypeName.isEmpty()) {
								scheme = authTypeName;
							}
						}
						authContent = webhook.getEndpoint().getHeaderAuthorizationContent();
					}
					if (authContent != null && !authContent.replaceAll("\\s+", "").isEmpty()) {
						httpRequest.setHeader(HttpHeaders.AUTHORIZATION, scheme + " " + authContent);
					}
				}
			}

			WebhookResponse webhookResponse = doSendWebhookRequest(httpRequest);
			if (null != webhookResponse) {
				if (webhook.getResponseHandler() == null) {// 設定しないなら必ずデフォルトに通します
					webhook.setResponseHandler(new DefaultWebhookResponseHandler());
				}
				webhook.getResponseHandler().handleResponse(webhookResponse);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	private WebhookResponse doSendWebhookRequest(HttpUriRequest request) {
		if (webhookIsRetry) {
			for (int i = 0; i < webhookRetryMaximumAttpempts; i++) {
				try {
					return webhookHttpClient.execute(request, response -> {
						return generateWebhookResponse(response);
					});
				} catch (Exception e) {
					if (e.getClass() == InterruptedIOException.class || e.getClass() == UnknownHostException.class
							|| e.getClass() == ConnectException.class || e.getClass() == SSLException.class) {
						// リトライ不可のExceptions
						logger.info("Webhook:" + e.getClass().getName() + " has occured. Stop retrying.");
						break;
					} else {
						try {
							Thread.sleep(webhookRetryInterval);
						} catch (InterruptedException exception) {
							logger.warn("An Webhook retry interval is interrupted, stop retrying.");
							break;
						}
					}
				}
			}
		} else {
			try {
				return webhookHttpClient.execute(request, response -> {
					return generateWebhookResponse(response);
				});
			} catch (IOException e) {
				logger.warn("Webhook: {} has occured.", e.getClass().getName(), e);
			}
		}
		return null;
	}

	/**
	 * Stringのtokenとpayloadでhmac暗号化する
	 * @param secret
	 * @param message
	 * @return base64String
	 */
	private String getHmacSha256(String secret, String message) {
		try {
			Mac Hmac = Mac.getInstance(this.webhookHmacHashAlgorithm);
			SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes("UTF-8"), this.webhookHmacHashAlgorithm);
			Hmac.init(secret_key);
			String hash = Base64.encodeBase64String(Hmac.doFinal(message.getBytes("UTF-8")));
			return hash;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	private class WebhookCallable implements Callable<Void> {
		Webhook webhook;

		public WebhookCallable(Webhook webhook) {
			this.webhook = webhook;
		}

		@Override
		public Void call() throws Exception {
			sendWebhook(webhook);
			return null;
		}
	}

	private HttpUriRequest getReqestMethodObject(String methodName, URI uri) {
		if (methodName.equals("GET")) {
			return new HttpGet(uri);
		} else if (methodName.equals("POST")) {
			return new HttpPost(uri);
		} else if (methodName.equals("DELETE")) {
			return new HttpDelete(uri);
		} else if (methodName.equals("PUT")) {
			return new HttpPut(uri);
		} else if (methodName.equals("PATCH")) {
			return new HttpPatch(uri);
		} else {
			return new HttpPost(uri);
		}
	}

	private boolean isEnclosingRequest(HttpUriRequest request) {
		return request.getMethod().equals("PATCH") || request.getMethod().equals("POST")
				|| request.getMethod().equals("PUT");
	}

	private WebhookResponse generateWebhookResponse(ClassicHttpResponse response) {
		WebhookResponse whr = new WebhookResponse();

		whr.setStatusCode(response.getCode());
		whr.setReasonPhrase(response.getReasonPhrase());

		if (response.getEntity() == null) {
			whr.setContentType(null);
			whr.setContentEncoding(null);
			whr.setResponseBody(null);
		} else {
			try {
				String entity = EntityUtils.toString(response.getEntity());
				whr.setResponseBody(entity);
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			}
			whr.setContentType(response.getEntity().getContentType());
			whr.setContentEncoding(response.getEntity().getContentEncoding());
		}
		ArrayList<WebhookHeader> headers = new ArrayList<WebhookHeader>();
		if (response.getHeaders() != null) {
			for (Header hd : response.getHeaders()) {
				headers.add(new WebhookHeader(hd.getName(), hd.getValue()));
			}
		}
		whr.setHeaders(headers);
		return whr;
	}

}
