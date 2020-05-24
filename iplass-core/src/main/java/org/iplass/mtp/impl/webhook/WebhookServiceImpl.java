/*
 * Copyright (C) 2020 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
import org.apache.commons.codec.binary.Base64;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Callable;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLException;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.SystemException;
import org.iplass.mtp.async.AsyncTaskManager;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.impl.definition.AbstractTypedMetaDataService;
import org.iplass.mtp.impl.definition.DefinitionMetaDataTypeMap;
import org.iplass.mtp.impl.http.HttpClientConfig;
import org.iplass.mtp.impl.webhook.endpointaddress.MetaWebhookEndpointDefinition.WebhookEndpointRuntime;
import org.iplass.mtp.impl.webhook.endpointaddress.WebhookEndpointService;
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

public class WebhookServiceImpl extends AbstractTypedMetaDataService<MetaWebhookTemplate, WebhookTemplateRuntime>
		implements WebhookService {
	private final static String WEBHOOK_ISRETRY= "retry";
	private final static String WEBHOOK_RETRY_MAXIMUMATTEMPTS = "retryMaximumAttempts";
	private final static String WEBHOOK_RETRY_INTERVAL = "retryInterval";
	private final static String WEBHOOK_HMACTOKEN_ALGORITHM = "hmacHashAlgorithm";
	private final static String WEBHOOK_HMACTOKEN_DEFAULTNAME = "hmacTokenDefaultName";
	private final static String WEBHOOK_HTTP_CLIENT_CONFIG = "httpClientConfig";
	private AsyncTaskManager atm;
	private WebhookEndpointService wheps;
	private boolean webhookIsRetry;
	private int webhookRetryMaximumAttpempts;
	private int webhookRetryInterval;
	private String webhookHmacHashAlgorithm;
	private String webhookHmacTokenDefaultName;
	private HttpClientConfig webhookHttpClientConfig;
	public static CloseableHttpClient webhookHttpClient;
	public static Logger logger = LoggerFactory.getLogger(WebhookServiceImpl.class);
	public static final String WEBHOOK_TEMPLATE_META_PATH = "/webhook/template/";
	public static final String WEBHOOK_SECURITY_BINDING_HMACRESULT ="webhookHmacResult";
	public static final String WEBHOOK_SECURITY_BINDING_HMACTOKEN ="webhookHmacToken";
	public static final String WEBHOOK_SECURITY_BINDING_BASIC ="webhookBasic";
	public static final String WEBHOOK_SECURITY_BINDING_BASICNAME ="webhookBasicName";
	public static final String WEBHOOK_SECURITY_BINDING_BASICPASSWORD ="webhookBasicPassword";
	public static final String WEBHOOK_SECURITY_BINDING_BEARER ="webhookBearer";

	public static class TypeMap extends DefinitionMetaDataTypeMap<WebhookTemplateDefinition, MetaWebhookTemplate> {
		public TypeMap() {
			super(getFixedPath(), MetaWebhookTemplate.class, WebhookTemplateDefinition.class);
		}

		@Override
		public TypedDefinitionManager<WebhookTemplateDefinition> typedDefinitionManager() {
			return ManagerLocator.getInstance().getManager(WebhookTemplateDefinitionManager.class);
		}
	}

	public static String getFixedPath() {
		return WEBHOOK_TEMPLATE_META_PATH;
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
	public void init(Config config) {
		webhookHttpClientConfig = new HttpClientConfig();
		for (String name: config.getNames()) {
			switch (name) {
			case WEBHOOK_ISRETRY:
				 String tempIsRetry = config.getValue(WEBHOOK_ISRETRY);
				 if ("true".equals(tempIsRetry.replaceAll("\\s","").toLowerCase())) {
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
				if (webhookHmacHashAlgorithm==null||webhookHmacHashAlgorithm.replaceAll("\\s","").isEmpty()) {
					webhookHmacHashAlgorithm="HmacSHA256";
				}
				break;
			case WEBHOOK_HMACTOKEN_DEFAULTNAME:
				webhookHmacTokenDefaultName = config.getValue(WEBHOOK_HMACTOKEN_DEFAULTNAME);
				if (webhookHmacTokenDefaultName==null||webhookHmacTokenDefaultName.replaceAll("\\s","").isEmpty()) {
					webhookHmacTokenDefaultName="X-IPLASS-HMAC";
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

	@Override
	public void destroy() {
		try {
			webhookHttpClient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 同期sendWebhook
	 * 
	 * */
	@Override
	public void sendWebhookSync(Webhook webhook) {
			sendWebhook(webhook);
	}

	/**
	 * 非同期sendWebhook
	 * AsyncTaskManagerのローカルスレッドをつっかています
	 * */
	@Override
	public void sendWebhookAsync(Webhook webhook){
			atm.executeOnThread(new WebhookCallable(webhook));
	}
	
	private void sendWebhook(Webhook webhook) {
		try {
			if (webhook.getHttpMethod()==null) {
				if (logger.isDebugEnabled()) {
					logger.debug("Webhook: request method undefined. Post will be used.");
				}
			}
			HttpRequestBase httpRequest = getReqestMethodObject(webhook.getHttpMethod());

			//payload
			if (isEnclosingRequest(httpRequest)) {
				if (webhook.getPayloadContent()!=null&&!webhook.getPayloadContent().replaceAll("\\s","").isEmpty()) {
						StringEntity se = new StringEntity(webhook.getPayloadContent(),"UTF-8");
						se.setContentType(webhook.getContentType());
						((HttpEntityEnclosingRequestBase) httpRequest).setEntity(se);
					}
			} else {
				if (webhook.getContentType()!=null&&!webhook.getContentType().replaceAll("\\s","").isEmpty()) {
					httpRequest.setHeader(HttpHeaders.CONTENT_TYPE, webhook.getContentType());
				}
			}

			//and headers
			if (webhook.getHeaders()!=null) {
				for(WebhookHeader headerEntry: webhook.getHeaders()) {
					httpRequest.setHeader(headerEntry.getKey(), headerEntry.getValue());
				}
			}
			if (webhook.getWebhookEndpoint()!=null) {
				//hmac
				if (webhook.getWebhookEndpoint().getHmacKey()!=null) {
					if (!webhook.getWebhookEndpoint().getHmacKey().isEmpty()) {
						String hmacHeader;
						if (webhook.getWebhookEndpoint().getHmacHashHeader()==null||webhook.getWebhookEndpoint().getHmacHashHeader().replaceAll("\\s","").isEmpty()) {
							hmacHeader = webhookHmacTokenDefaultName;
						} else {
							hmacHeader = webhook.getWebhookEndpoint().getHmacHashHeader().replaceAll("\\s","");
						}
						httpRequest.setHeader(hmacHeader, this.getHmacSha256(webhook.getWebhookEndpoint().getHmacKey(), webhook.getPayloadContent()));
					}
				}
				//headerのauthorizationでの認証情報
				if (webhook.getWebhookEndpoint().getHeaderAuthorizationType() == null) {
	
				} else {
					String scheme = "";
					String authContent = "";
					if (WebhookAuthenticationType.BEARER.equals(webhook.getWebhookEndpoint().getHeaderAuthorizationType())) {
						scheme = "Bearer";
						authContent = webhook.getWebhookEndpoint().getHeaderAuthorizationContent();
					} else if (WebhookAuthenticationType.BASIC.equals(webhook.getWebhookEndpoint().getHeaderAuthorizationType())) {
						scheme = "Basic";
						if(webhook.getWebhookEndpoint().getHeaderAuthorizationContent()!=null&&!webhook.getWebhookEndpoint().getHeaderAuthorizationContent().replaceAll("\\s+", "").isEmpty()) {
							authContent = Base64.encodeBase64String(webhook.getWebhookEndpoint().getHeaderAuthorizationContent().getBytes());
						}
					} else if (WebhookAuthenticationType.CUSTOM.equals(webhook.getWebhookEndpoint().getHeaderAuthorizationType())) {
						scheme = "Custom";
						if (webhook.getWebhookEndpoint().getHeaderAuthCustomTypeName()!=null) {
							String authTypeName = webhook.getWebhookEndpoint().getHeaderAuthCustomTypeName().replace("\n", "").replaceAll("\\s+", "");
							if (!authTypeName.isEmpty()) {
								scheme = authTypeName;
							}
						}
						authContent = webhook.getWebhookEndpoint().getHeaderAuthorizationContent();
					}
					if (authContent!=null&&!authContent.replaceAll("\\s+", "").isEmpty()) {
						httpRequest.setHeader(HttpHeaders.AUTHORIZATION, scheme+ " " + authContent);
					}
				}
			}

			String url = webhook.getWebhookEndpoint().getUrl()+webhook.getUrlQuery();
			httpRequest.setURI(new URI(url));

			CloseableHttpResponse response = null;
			//if retry
			if (webhookIsRetry) {
				for(int i=0; i<webhookRetryMaximumAttpempts; i++) {
					try {
						response = webhookHttpClient.execute(httpRequest);
						break;
					} catch (Exception e) {
						if (e.getClass() == InterruptedIOException.class 
								||e.getClass() == UnknownHostException.class 
								||e.getClass() == ConnectException.class
								||e.getClass() == SSLException.class)	{//リトライ不可のException
							logger.info("Webhook:"+e.getClass().getName()+" has occured. Stop retrying.");
							break;
						}else{
							try {
								Thread.sleep(webhookRetryInterval);
							} catch (InterruptedException exception) {
								exception.printStackTrace();
							}
						}
					}
				}
			} else {
				response = webhookHttpClient.execute(httpRequest);
			}
			try {
				if (response != null) {
					WebhookResponse whr = generateWebhookResponse(response);
					if (webhook.getResultHandler()!=null) {
						webhook.getResultHandler().handleResponse(whr);
					}
				}
			} finally {
				if (response!=null) {
					response.close();
				}
			}
		} catch (Exception e) {
			throw (RuntimeException)e;
		}
	}

	
	/**
	 * Stringのtokenとpayloadでhmac暗号化する
	 * @param: String token
	 * @param: String message
	 * @return: base64String
	 * */
	public String getHmacSha256(String secret, String message) {
		try {
		    Mac Hmac = Mac.getInstance(this.webhookHmacHashAlgorithm);
		    SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes("UTF-8"), this.webhookHmacHashAlgorithm);
		    Hmac.init(secret_key);

		    String hash = Base64.encodeBase64String(Hmac.doFinal(message.getBytes("UTF-8")));
			return hash;
		} catch (Exception e) {
			e.printStackTrace();
		}  
		return null;
	}
	
	private class WebhookCallable implements Callable<Void>{
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
	
	private HttpRequestBase getReqestMethodObject(String methodName){
		if (methodName.equals("GET")) {
			return new HttpGet();
		}else if (methodName.equals("POST")) {
			return new HttpPost();
		}else if (methodName.equals("DELETE")) {
			return new HttpDelete();
		}else if (methodName.equals("PUT")) {
			return new HttpPut();
		}else if (methodName.equals("PATCH")) {
			return new HttpPatch();
		}else {
			return new HttpPost();
		}
	}

	private boolean isEnclosingRequest(HttpRequestBase request) {
		if (request.getMethod().equals("PATCH")
				|| request.getMethod().equals("POST")
				|| request.getMethod().equals("PUT")) {
			return true;
		} else {
			return false;
		}
	}

	private void initWebhookHttpClient() {
		if (webhookHttpClient == null) {
			try {
				webhookHttpClient = webhookHttpClientConfig.getInstance();
				} catch(Exception e){
				throw e;
			}
		}
	}
	private WebhookResponse generateWebhookResponse(HttpResponse response) {
		WebhookResponse whr = new WebhookResponse();
		if (response.getStatusLine()== null) {
			whr.setStatusCode(0);
			whr.setReasonPhrase(null);
		} else {
			whr.setStatusCode(response.getStatusLine().getStatusCode());
			whr.setReasonPhrase(response.getStatusLine().getReasonPhrase());
		}
		if (response.getEntity()==null) {
			whr.setContentType(null);
			whr.setContentEncoding(null);
			whr.setResponseBody(null);
		}else {
			try {
				String entity = EntityUtils.toString(response.getEntity());
				whr.setResponseBody(entity);
			} catch (Exception e) {
				e.printStackTrace();
			}
			whr.setContentType(response.getEntity().getContentType()==null?null:response.getEntity().getContentType().getValue());
			whr.setContentEncoding(response.getEntity().getContentEncoding()==null?null:response.getEntity().getContentEncoding().getValue());
		}
		ArrayList<WebhookHeader> headers = new ArrayList<WebhookHeader>();
		if (response.getAllHeaders()!=null) {
			for (Header hd : response.getAllHeaders()) {
				headers.add(new WebhookHeader(hd.getName(), hd.getValue()));
			}
		}
		whr.setHeaders(headers);
		return whr;
	}

	public Webhook generateWebhook(String webhookDefinitionName, Map<String, Object> binding, String endpointDefinitionName) {
		WebhookTemplateRuntime webhookRuntime = this.getRuntimeByName(webhookDefinitionName);
		WebhookEndpointRuntime endpointRuntime = wheps.getRuntimeByName(endpointDefinitionName);

		if (endpointRuntime == null) {
			throw new SystemException("Endpoint Template:" + endpointDefinitionName + " not found");
		}
		if (webhookRuntime == null) {
			throw new SystemException("WebhookTemplate:" + webhookDefinitionName + " not found");
		}

		Webhook webhook = webhookRuntime.createWebhook(binding);
		webhook.setWebhookEndpoint(endpointRuntime.createWebhookEndpoint(binding));
		return webhook;
	}

	@Override
	public Webhook getWebhookByName(String webhookDefinitionName, Map<String, Object> binding) {
		WebhookTemplateRuntime webhookRuntime = this.getRuntimeByName(webhookDefinitionName);
		Webhook webhook = webhookRuntime.createWebhook(binding);
		return webhook;
	}
}
