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
import java.io.StringWriter;
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
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.SystemException;
import org.iplass.mtp.async.AsyncTaskManager;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.impl.definition.AbstractTypedMetaDataService;
import org.iplass.mtp.impl.definition.DefinitionMetaDataTypeMap;
import org.iplass.mtp.impl.http.HttpClientConfig;
import org.iplass.mtp.impl.script.ScriptRuntimeException;
import org.iplass.mtp.impl.script.template.GroovyTemplate;
import org.iplass.mtp.impl.script.template.GroovyTemplateBinding;
import org.iplass.mtp.impl.webhook.endpointaddress.MetaWebHookEndPointDefinition.WebHookEndPointRuntime;
import org.iplass.mtp.impl.webhook.endpointaddress.WebHookEndPointService;
import org.iplass.mtp.impl.webhook.template.MetaWebHookTemplate;
import org.iplass.mtp.impl.webhook.template.MetaWebHookTemplate.WebHookTemplateRuntime;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.webhook.WebHook;
import org.iplass.mtp.webhook.WebHookHeader;
import org.iplass.mtp.webhook.WebHookResponse;
import org.iplass.mtp.webhook.template.definition.WebHookTemplateDefinition;
import org.iplass.mtp.webhook.template.definition.WebHookTemplateDefinitionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebHookServiceImpl extends AbstractTypedMetaDataService<MetaWebHookTemplate, WebHookTemplateRuntime>
		implements WebHookService {
	private final String WEBHOOK_ISRETRY= "webHookIsRetry";
	private final String WEBHOOK_RETRY_MAXIMUMATTEMPTS = "webHookRetryMaximumAttempts";
	private final String WEBHOOK_RETRY_INTERVAL = "webHookRetryInterval";
	private final String WEBHOOK_HMACTOKEN_ALGORITHM = "webHookHmacHashAlgorithm";
	private final String WEBHOOK_HMACTOKEN_DEFAULTNAME = "webHookHmacTokenDefaultName";
	private final String WEBHOOK_HTTP_CLIENT_CONFIG = "httpClientConfig";
	private AsyncTaskManager atm;
	private WebHookEndPointService wheps;
	private boolean webHookIsRetry;
	private int webHookRetryMaximumAttpempts;
	private int webHookRetryInterval;
	private String webHookHmacHashAlgorithm;
	private String webHookHmacTokenDefaultName;
	private HttpClientConfig webHookHttpClientConfig;
	public static CloseableHttpClient webHookHttpClient;
	public static Logger logger = LoggerFactory.getLogger(WebHookServiceImpl.class);
	public static final String WEBHOOK_TEMPLATE_META_PATH = "/webhook/template/";
	public static final String WEBHOOK_SECURITY_BINDING_HMACRESULT ="webhookHmacResult";
	public static final String WEBHOOK_SECURITY_BINDING_HMACTOKEN ="webhookHmacToken";
	public static final String WEBHOOK_SECURITY_BINDING_BASIC ="webhookBasic";
	public static final String WEBHOOK_SECURITY_BINDING_BASICNAME ="webhookBasicName";
	public static final String WEBHOOK_SECURITY_BINDING_BASICPASSWORD ="webhookBasicPassword";
	public static final String WEBHOOK_SECURITY_BINDING_BEARER ="webhookBearer";

	public static class TypeMap extends DefinitionMetaDataTypeMap<WebHookTemplateDefinition, MetaWebHookTemplate> {
		public TypeMap() {
			super(getFixedPath(), MetaWebHookTemplate.class, WebHookTemplateDefinition.class);
		}

		@Override
		public TypedDefinitionManager<WebHookTemplateDefinition> typedDefinitionManager() {
			return ManagerLocator.getInstance().getManager(WebHookTemplateDefinitionManager.class);
		}
	}

	public static String getFixedPath() {
		return WEBHOOK_TEMPLATE_META_PATH;
	}

	@Override
	public Class<MetaWebHookTemplate> getMetaDataType() {
		return MetaWebHookTemplate.class;
	}

	@Override
	public Class<WebHookTemplateRuntime> getRuntimeType() {
		return WebHookTemplateRuntime.class;
	}

	@Override
	public void init(Config config) {
		webHookHttpClientConfig = new HttpClientConfig();
		for (String name: config.getNames()) {
			switch (name) {
			case WEBHOOK_ISRETRY:
				 String tempIsRetry = config.getValue(WEBHOOK_ISRETRY);
				 if ("true".equals(tempIsRetry.replaceAll("\\s","").toLowerCase())) {
					 webHookIsRetry = true;
				 } else {
					 webHookIsRetry = false;
				 }
				break;
			case WEBHOOK_RETRY_MAXIMUMATTEMPTS:
				webHookRetryMaximumAttpempts = Integer.valueOf(config.getValue(WEBHOOK_RETRY_MAXIMUMATTEMPTS));
				break;
			case WEBHOOK_RETRY_INTERVAL:
				webHookRetryInterval = Integer.valueOf(config.getValue(WEBHOOK_RETRY_INTERVAL));
				break;
			case WEBHOOK_HMACTOKEN_ALGORITHM:
				webHookHmacHashAlgorithm = config.getValue(WEBHOOK_HMACTOKEN_ALGORITHM);
				if (webHookHmacHashAlgorithm==null||webHookHmacHashAlgorithm.replaceAll("\\s","").isEmpty()) {
					webHookHmacHashAlgorithm="HmacSHA256";
				}
				break;
			case WEBHOOK_HMACTOKEN_DEFAULTNAME:
				webHookHmacTokenDefaultName = config.getValue(WEBHOOK_HMACTOKEN_DEFAULTNAME);
				if (webHookHmacTokenDefaultName==null||webHookHmacTokenDefaultName.replaceAll("\\s","").isEmpty()) {
					webHookHmacTokenDefaultName="X-IPLASS-HMAC";
				}
				break;
			case WEBHOOK_HTTP_CLIENT_CONFIG:
				webHookHttpClientConfig = config.getValue(WEBHOOK_HTTP_CLIENT_CONFIG, HttpClientConfig.class);
				if (webHookHttpClientConfig == null) {
					webHookHttpClientConfig = new HttpClientConfig();
				}
				webHookHttpClientConfig.inited(this, config);
				break;
			}
		}

		atm = ManagerLocator.getInstance().getManager(AsyncTaskManager.class);
		wheps = ServiceRegistry.getRegistry().getService(WebHookEndPointService.class);
		initWebHookHttpClient();
	}

	@Override
	public void destroy() {
		try {
			webHookHttpClient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 同期sendWebHook
	 * 
	 * */
	@Override
	public void sendWebHookSync(WebHook webHook) {
			sendWebHook(webHook);
	}

	/**
	 * 非同期sendWebHook
	 * AsyncTaskManagerのローカルスレッドをつっかています
	 * */
	@Override
	public void sendWebHookAsync(WebHook webHook){
			atm.executeOnThread(new WebHookCallable(webHook));
	}
	
	private void sendWebHook(WebHook webHook) {
		try {
			WebHookEndPointRuntime endPointRuntime = null;
			if (webHook.getEndPointName()==null||webHook.getEndPointName().isEmpty()) {
				endPointRuntime = null;
			} else {
				endPointRuntime = wheps.getRuntimeByName(webHook.getEndPointName());
				if (endPointRuntime==null) {
					throw new SystemException("WebHookTemplate:" + webHook.getEndPointName() + " not found");
				}
			}
			
			if (webHook.getHttpMethod()==null) {
				if (logger.isDebugEnabled()) {
					logger.debug("WebHook: request method undefined. Post will be used.");
				}
			}
			HttpRequestBase httpRequest = getReqestMethodObject(webHook.getHttpMethod());
			HttpContext httpContext = new BasicHttpContext();
			httpContext.setAttribute(WEBHOOK_ISRETRY, webHookIsRetry);
			httpContext.setAttribute(WEBHOOK_RETRY_MAXIMUMATTEMPTS, webHookRetryMaximumAttpempts);
			httpContext.setAttribute(WEBHOOK_RETRY_INTERVAL, webHookRetryInterval);

			//payload
			if (isEnclosingRequest(httpRequest)) {
				if (webHook.getPayloadContent()!=null&&!webHook.getPayloadContent().replaceAll("\\s","").isEmpty()) {
						StringEntity se = new StringEntity(webHook.getPayloadContent(),"UTF-8");
						se.setContentType(webHook.getContentType());
						((HttpEntityEnclosingRequestBase) httpRequest).setEntity(se);
					}
			} else {
				if (webHook.getContentType()!=null&&!webHook.getContentType().replaceAll("\\s","").isEmpty()) {
					httpRequest.setHeader(HttpHeaders.CONTENT_TYPE, webHook.getContentType());
				}
			}

			//and headers
			if (webHook.getHeaders()!=null) {
				for(WebHookHeader headerEntry: webHook.getHeaders()) {
					httpRequest.setHeader(headerEntry.getKey(), headerEntry.getValue());
				}
			}
			if (endPointRuntime!=null) {

				//hmac
				if (endPointRuntime.isHmacEnabled()) {
					if (endPointRuntime.getHmacKey()!=null) {
						if (!endPointRuntime.getHmacKey().isEmpty()) {
							String hmacHeader;
							if (webHook.getHmacHashHeader()==null||webHook.getHmacHashHeader().replaceAll("\\s","").isEmpty()) {
								hmacHeader = webHookHmacTokenDefaultName;
							} else {
								hmacHeader = webHook.getHmacHashHeader();
							}
							httpRequest.setHeader(hmacHeader, this.getHmacSha256(endPointRuntime.getHmacKey(), webHook.getPayloadContent()));
						}
					}
				}
				//headerのauthorizationでの認証情報
				if (endPointRuntime.getAuthType()==null || endPointRuntime.getAuthType().isEmpty()) {
	
				} else {
					String scheme = "";
					String authContent = "";
					if ("WHBT".equals(endPointRuntime.getAuthType())) {
						scheme = "Bearer";
						authContent = endPointRuntime.getHeaderAuthToken();
					} else if ("WHBA".equals(endPointRuntime.getAuthType())) {
						scheme = "Basic";
						authContent = Base64.encodeBase64String(endPointRuntime.getHeaderAuthToken().getBytes());
					} else if ("WHCT".equals(endPointRuntime.getAuthType())) {
						if (endPointRuntime.getHeaderAuthCustomTypeName()!=null) {
							String authTypeName = endPointRuntime.getHeaderAuthCustomTypeName().replace("\n", "").replaceAll("\\s+", "");
							if (!authTypeName.isEmpty()) {
								scheme = authTypeName;
							}
						}
						authContent = endPointRuntime.getHeaderAuthToken();
					}
					httpRequest.setHeader(HttpHeaders.AUTHORIZATION, scheme+ " " + authContent);
				}
			}


			String url = webHook.getUrl()+webHook.getUrlQuery();
			httpRequest.setURI(new URI(url));

			CloseableHttpResponse response = null;
			//if retry
			if (webHookIsRetry) {
				for(int i=0; i<webHookRetryMaximumAttpempts; i++) {
					try {
						response = webHookHttpClient.execute(httpRequest,httpContext);
						break;
					} catch (Exception e) {
						if (e.getClass() == InterruptedIOException.class 
								||e.getClass() == UnknownHostException.class 
								||e.getClass() == ConnectException.class
								||e.getClass() == SSLException.class)	{//リトライ不可のException
							logger.info("WebHook:"+e.getClass().getName()+" has occured. Stop retrying.");
							break;
						}else{
							try {
								Thread.sleep(webHookRetryInterval);
							} catch (InterruptedException exception) {
								exception.printStackTrace();
							}
						}
					}
				}
			} else {
				response = webHookHttpClient.execute(httpRequest,httpContext);
			}
			try {
				if (response != null) {
					WebHookResponse whr = generateWebHookResponse(response);
					webHook.getResultHandler().handleResponse(whr);
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
		    Mac Hmac = Mac.getInstance(this.webHookHmacHashAlgorithm);
		    SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes("UTF-8"), this.webHookHmacHashAlgorithm);
		    Hmac.init(secret_key);

		    String hash = Base64.encodeBase64String(Hmac.doFinal(message.getBytes("UTF-8")));
			return hash;
		} catch (Exception e) {
			e.printStackTrace();
		}  
		return null;
	}
	
	private class WebHookCallable implements Callable<Void>{
		WebHook webHook;
		public WebHookCallable(WebHook webHook) {
			this.webHook = webHook;
		}
		@Override
		public Void call() throws Exception {
			sendWebHook(webHook);
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

	private void initWebHookHttpClient() {
		if (webHookHttpClient == null) {
			try {
				webHookHttpClient = webHookHttpClientConfig.getInstance();
				} catch(Exception e){
				throw e;
			}
		}
	}
	private WebHookResponse generateWebHookResponse(HttpResponse response) {
		WebHookResponse whr = new WebHookResponse();
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
		ArrayList<WebHookHeader> headers = new ArrayList<WebHookHeader>();
		if (response.getAllHeaders()!=null) {
			for (Header hd : response.getAllHeaders()) {
				headers.add(new WebHookHeader(hd.getName(), hd.getValue()));
			}
		}
		whr.setHeaders(headers);
		return whr;
	}

	public WebHook generateWebHook(String webHookDefinitionName, Map<String, Object> binding, String endPointDefinitionName) {
		WebHookTemplateRuntime webHookRuntime = this.getRuntimeByName(webHookDefinitionName);
		WebHookEndPointRuntime endPointRuntime;
		if (endPointDefinitionName==null||endPointDefinitionName.isEmpty()) {
			endPointRuntime = null;
		} else {
			endPointRuntime = wheps.getRuntimeByName(endPointDefinitionName);
			if (endPointRuntime == null) {
				throw new SystemException("End Point Template:" + endPointDefinitionName + " not found");
			}
		}
		if (webHookRuntime == null) {
			throw new SystemException("WebHookTemplate:" + webHookDefinitionName + " not found");
		}

		
		WebHook webHook = webHookRuntime.createWebHook(binding);
		
		GroovyTemplate contentTemplate = webHookRuntime.getContentTemplate();
		GroovyTemplate urlQueryTemplate = webHookRuntime.getUrlQueryTemplate();

		binding.put("webhook", webHook);
		if (endPointRuntime !=null) {
			webHook.setEndPointName(endPointDefinitionName);
			GroovyTemplate urlTemplate = endPointRuntime.getUrlTemplate();
			binding.put(WEBHOOK_SECURITY_BINDING_HMACTOKEN, endPointRuntime.getHmacKey());
			if (endPointRuntime.getAuthType()!=null) {
				if ("WHBA".equals(endPointRuntime.getAuthType())) {
					String[] basicArray = null;
					if (endPointRuntime.getHeaderAuthToken()!=null) {
						basicArray = endPointRuntime.getHeaderAuthToken().replaceAll("\\s","").split(":");
					} 
					if (basicArray==null||basicArray.length<2) {
					} else {
						binding.put(WEBHOOK_SECURITY_BINDING_BASICNAME, basicArray[0]);
						binding.put(WEBHOOK_SECURITY_BINDING_BASICPASSWORD, basicArray[1]);
					}
				} else if ("WHBT".equals(endPointRuntime.getAuthType())){
					binding.put(WEBHOOK_SECURITY_BINDING_BEARER, endPointRuntime.getHeaderAuthToken());	
				}
			}
			if (urlTemplate!=null) {
				StringWriter sw = new StringWriter();
				GroovyTemplateBinding gtb = new GroovyTemplateBinding(sw,binding);
				try {
					urlTemplate.doTemplate(gtb);
				} catch (IOException e) {
					throw new ScriptRuntimeException(e);
				}
				webHook.setUrl(sw.toString());
			}
			
			webHook.setHmacHashHeader(endPointRuntime.getHmacHashHeader());
		}
		if (contentTemplate != null) {
			StringWriter sw = new StringWriter();
			GroovyTemplateBinding gtb = new GroovyTemplateBinding(sw,binding);
			try {
				contentTemplate.doTemplate(gtb);
			} catch (IOException e) {
				throw new ScriptRuntimeException(e);
			}
			webHook.setPayloadContent(sw.toString());
		}
		if (urlQueryTemplate!=null) {
			StringWriter sw = new StringWriter();
			GroovyTemplateBinding gtb = new GroovyTemplateBinding(sw,binding);
			try {
				urlQueryTemplate.doTemplate(gtb);
			} catch (IOException e) {
				throw new ScriptRuntimeException(e);
			}
			webHook.setUrlQuery(sw.toString());
		}
		return webHook;
	}
}
