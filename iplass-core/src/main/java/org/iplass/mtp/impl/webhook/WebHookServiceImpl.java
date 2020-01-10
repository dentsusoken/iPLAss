package org.iplass.mtp.impl.webhook;

import java.io.IOException;
import org.apache.commons.codec.binary.Base64;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.lang.Thread.State;
import java.math.BigInteger;
import java.net.ConnectException;
import java.net.URI;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.Callable;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLException;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpContext;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.async.AsyncTaskManager;
import org.iplass.mtp.definition.DefinitionEntry;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.impl.auth.authenticate.token.AuthTokenService;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.definition.AbstractTypedMetaDataService;
import org.iplass.mtp.impl.definition.DefinitionMetaDataTypeMap;
import org.iplass.mtp.impl.webhook.template.MetaWebHookTemplate;
import org.iplass.mtp.impl.webhook.template.MetaWebHookTemplate.WebHookTemplateRuntime;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.webhook.WebHook;
import org.iplass.mtp.webhook.template.definition.WebHookHeader;
import org.iplass.mtp.webhook.template.definition.WebHookSubscriber;
import org.iplass.mtp.webhook.template.definition.WebHookTemplateDefinition;
import org.iplass.mtp.webhook.template.definition.WebHookTemplateDefinitionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebHookServiceImpl extends AbstractTypedMetaDataService<MetaWebHookTemplate, WebHookTemplateRuntime>
		implements WebHookService {
	public static final String WEBHOOK_PROXY_HOST= "webHook.Proxy.Host";
	public static final String WEBHOOK_PROXY_PORT= "webHook.Proxy.Port";
	public static final String WEBHOOK_USE_PROXY= "webHook.Use.Proxy";
	
	private AsyncTaskManager atm;
	private WebHookAuthTokenHandler authTokenHandler;
	
	private String webHookProxyHost;
	private int webHookProxyPort;
	private boolean webHookUseProxy;
	private static Logger logger = LoggerFactory.getLogger(WebHookServiceImpl.class);
	public static final String WEBHOOK_TEMPLATE_META_PATH = "/webhook/template/";

	public static class TypeMap extends DefinitionMetaDataTypeMap<WebHookTemplateDefinition, MetaWebHookTemplate> {
		
		@Override
		public WebHookTemplateDefinition toDefinition(MetaWebHookTemplate metaData) {
			WebHookService ws = ServiceRegistry.getRegistry().getService(WebHookService.class);
			WebHookTemplateDefinition definition = super.toDefinition(metaData);
			definition = ws.fillSubscriberListByDef(definition);
			return definition;
		}

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
		for (String name: config.getNames()) {
			switch (name) {
			case WEBHOOK_PROXY_HOST:
				webHookProxyHost = config.getValue(WEBHOOK_PROXY_HOST);
			case WEBHOOK_PROXY_PORT:
				webHookProxyPort = Integer.valueOf(config.getValue(WEBHOOK_PROXY_PORT));
			case WEBHOOK_USE_PROXY:
				 String temp = config.getValue(WEBHOOK_USE_PROXY);
				 if (temp.replaceAll("\\s","").toLowerCase().equals("true")) {
					 webHookUseProxy = true;
				 }
			}
			
		}
	}

	@Override
	public void destroy() {
	}

	@Override
	public WebHook createWebHook(Tenant tenant, String charset) {
		logger.info("empty webhook created.");
		return new WebHook();
	}

	/**
	 * 同期非同期を判断してからsendWebHook(WebHook webHook)を呼びます
	 * AsyncTaskManagerのローカルスレッドをつっかています
	 * */
	@Override
	public void sendWebHook(Tenant tenant, WebHook webHook) {
		WebHookAuthTokenHandler tokenHandler = (WebHookAuthTokenHandler)ServiceRegistry
				.getRegistry().getService(AuthTokenService.class)
				.getHandler(WebHookAuthTokenHandler.TYPE_WEBHOOK_AUTHTOKEN_HANDLER);
		this.fillData(tenant.getId(), webHook.getMetaDataId(), tokenHandler, webHook.getSubscribers());
		atm = ManagerLocator.getInstance().getManager(AsyncTaskManager.class);
		if (webHook.isSynchronous()) {
			sendWebHook(webHook);
		} else {
			atm.executeOnThread(new WebHookCallable(webHook));
		}
	}
	

	private void sendWebHook(WebHook webHook) {
		try {

			
			logger.info("WebHook:"+webHook.getTemplateName()+" Attempted.");
			HttpClientBuilder httpClientBuilder = null;
			if (webHook.isRetry()) {
				int retryCount = webHook.getRetryLimit();
				int retryInterval = webHook.getRetryInterval();
				HttpRequestRetryHandler requestRetryHandler=new HttpRequestRetryHandler(){
					public boolean retryRequest(final IOException exception, int executionCount, final HttpContext context){
						//特定の失敗ケースだけリトライ
						if (exception.getClass() == InterruptedIOException.class 
								||exception.getClass() == UnknownHostException.class 
								||exception.getClass() == ConnectException.class
								||exception.getClass() == SSLException.class)	{
							if (executionCount < retryCount) {
								try {
									Thread.sleep(retryInterval);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								return true;
							}//TODO log retryInterval exceeded
						}
						logger.info("WebHook:"+webHook.getTemplateName()+"retry limit exceeded");
						return false;
						}
					};
					httpClientBuilder = HttpClientBuilder.create().setRetryHandler(requestRetryHandler);
			} else {
				httpClientBuilder = HttpClientBuilder.create().disableAutomaticRetries();
			}
			if (this.webHookUseProxy) {
				HttpHost proxy = new HttpHost(this.webHookProxyHost,this.webHookProxyPort,"http");
				httpClientBuilder.setProxy(proxy);
			}
			try {
				CloseableHttpClient httpClient= httpClientBuilder.build();
				ArrayList<WebHookSubscriber> receivers = new ArrayList<WebHookSubscriber>(webHook.getSubscribers());

				//fill in webhook payload
				String payload = webHook.getContent().getContent();
				StringEntity se = new StringEntity(payload);
				se.setContentType(webHook.getContent().getCharset());
				
				
				
				//se.setContentEncoding(webHook.getContent().getCharset());
				
				Exception ex = null;

					//TODO: sync か　asyn,なんかsyncは珍しいみたいで
				for (int j = 0; j < receivers.size();j++) {
					WebHookSubscriber temp= receivers.get(j);
					//TODO: fill in info to post
					HttpPost httpPost = new HttpPost(new URI(receivers.get(j).getUrl()));
					// and payload and headers
					httpPost.setEntity(se);
					for(WebHookHeader headerEntry: webHook.getHeaders()) {
						httpPost.setHeader(headerEntry.getKey(), headerEntry.getValue());
					}
					if (temp.getSecurityToken()!=null) {
						if (!temp.getSecurityToken().isEmpty()) {
							String hmacToken= getHmacSha256(temp.getSecurityToken(), payload);
							String tokenHeader;
							if (webHook.getTokenHeader()==null||webHook.getTokenHeader().replaceAll("\\s","").isEmpty()) {
								tokenHeader = "security-token";
							} else {
								tokenHeader = webHook.getTokenHeader();
							}
							httpPost.setHeader(tokenHeader, hmacToken);//FIXME:iplass-token should be configurable.
						}
						//TODO: need more testing
					}
					if (temp.getSecurityBearerToken()!=null) {
						if (!temp.getSecurityBearerToken().isEmpty()) {
							httpPost.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " +temp.getSecurityBearerToken());
						}
						//TODO: need more testing
					}
					if (temp.getSecurityUsername()!=null&&temp.getSecurityPassword()!=null) {
						if(!temp.getSecurityUsername().isEmpty()&&!temp.getSecurityPassword().isEmpty()) {
							String basic = temp.getSecurityUsername()+":"+ temp.getSecurityPassword();
							basic ="Basic " + Base64.encodeBase64String(basic.getBytes());
							httpPost.setHeader(HttpHeaders.AUTHORIZATION, basic);
						}
					}
					CloseableHttpResponse response = httpClient.execute(httpPost);
					logger.debug("\n---------------------------\n response headers: \n"+response.getAllHeaders().toString()+"\n response entity: \n"+ response.getEntity().getContentType()+"\n"+response.getEntity().getContent()+"\n---------------------------");
					try {
						StatusLine statusLine= response.getStatusLine();
						if (statusLine.getStatusCode() == HttpStatus.SC_OK) {//普通に成功
							receivers.remove(j);
						}
					} finally {
						response.close();
						httpClient.close();
					}
				}
				
			} finally {
				
			}
		}catch(Exception e)
		{
			// handleException
		}
	}

	
	/**
	 * Stringのtokenとpayloadでhmac暗号化する
	 * @param: String token
	 * @param: String message
	 * @return: base64String
	 * */
	public String getHmacSha256(String token, String message) {
		try {
		    Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
		    SecretKeySpec secret_key = new SecretKeySpec(token.getBytes(), "HmacSHA256");
		    sha256_HMAC.init(secret_key);

		    String hash = Base64.encodeBase64String(sha256_HMAC.doFinal(message.getBytes()));
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
	
	public String composeSeries(final String metaDataId, final String subscriberId) {
		return metaDataId+subscriberId;
	}
	
	/**
	 * @param: データベースを読む必要の webHookTemplate Definition.
	 * @return: WebHookSubscriberのリスト
	 * */
	public WebHookTemplateDefinition fillSubscriberListByDef(WebHookTemplateDefinition definition){
		int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
		String metaDataId = definition.getMetaDataId();
		WebHookAuthTokenHandler tokenHandler = (WebHookAuthTokenHandler)ServiceRegistry
				.getRegistry().getService(AuthTokenService.class)
				.getHandler(WebHookAuthTokenHandler.TYPE_WEBHOOK_AUTHTOKEN_HANDLER);
		ArrayList<WebHookSubscriber> tempList = definition.getSubscribers();
		
		tempList = fillData(tenantId, metaDataId, tokenHandler, tempList);
		
		definition.setSubscribers(tempList);
		return definition;
	}

	/**
	 * ArrayList<WebHookSubscriber>に対して,セキュリティ情報を取得して書き込みます
	 * */
	private ArrayList<WebHookSubscriber> fillData(int tenantId, String metaDataId, WebHookAuthTokenHandler tokenHandler, ArrayList<WebHookSubscriber> tempList) {
		for (WebHookSubscriber temp : tempList) {
			String subscriberId = temp.getWebHookSubscriberId();
			temp.setSecurityBearerToken(tokenHandler.getSecret(tenantId, composeSeries(metaDataId, subscriberId), WebHookAuthTokenHandler.BEARER_AUTHENTICATION_TYPE));
			temp.setSecurityToken(tokenHandler.getSecret(tenantId, composeSeries(metaDataId, subscriberId), WebHookAuthTokenHandler.HMAC_AUTHENTICATION_TYPE));
			String basic64=tokenHandler.getSecret(tenantId, composeSeries(metaDataId, subscriberId), WebHookAuthTokenHandler.BASIC_AUTHENTICATION_TYPE);
			String basic = new String(Base64.decodeBase64(basic64));
			String[] basicArray = basic.split(":");
			if (basicArray.length<2) {
				temp.setSecurityUsername("");
				temp.setSecurityPassword("");
			} else {
				temp.setSecurityUsername(basicArray[0].replaceAll(" ", ""));
				temp.setSecurityPassword(basicArray[1].replaceAll(" ", ""));
			}
		}
		return tempList;
	}
	
	/**
	 * definitionに応じ、データベースのエントリーを更新します
	 * @param: webHookTemplate Definition.
	 * */
	public WebHookTemplateDefinition updateSubscriberListByDef(WebHookTemplateDefinition definition) {
		//definition のsubscribersに記録したstateによって、insertや、delete,updateなとを行う
		int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
		String metaDataId = definition.getMetaDataId();
		WebHookAuthTokenHandler tokenHandler = (WebHookAuthTokenHandler)ServiceRegistry
				.getRegistry().getService(AuthTokenService.class)
				.getHandler(WebHookAuthTokenHandler.TYPE_WEBHOOK_AUTHTOKEN_HANDLER);
		ArrayList<WebHookSubscriber> newList = new ArrayList<WebHookSubscriber>();
		for (WebHookSubscriber temp : definition.getSubscribers()) {
			
			
			if (temp.isDelete()) {
				String subscriberId = temp.getWebHookSubscriberId();
				String series = composeSeries(metaDataId, subscriberId);
				tokenHandler.deleteSecret(tenantId, WebHookAuthTokenHandler.BASIC_AUTHENTICATION_TYPE, series);
				tokenHandler.deleteSecret(tenantId, WebHookAuthTokenHandler.BEARER_AUTHENTICATION_TYPE, series);
				tokenHandler.deleteSecret(tenantId, WebHookAuthTokenHandler.HMAC_AUTHENTICATION_TYPE, series);
			} else if(temp.isCreate()) {
				temp.setWebHookSubscriberId(generateUuid());
				String subscriberId = temp.getWebHookSubscriberId();
				String series = composeSeries(metaDataId, subscriberId);
				String basic = temp.getSecurityUsername()+":"+ temp.getSecurityPassword();
				String basicTokenSecret = Base64.encodeBase64String(basic.getBytes());
				tokenHandler.insertSecret(tenantId, WebHookAuthTokenHandler.BASIC_AUTHENTICATION_TYPE, metaDataId, series, basicTokenSecret);
				tokenHandler.insertSecret(tenantId, WebHookAuthTokenHandler.BEARER_AUTHENTICATION_TYPE, metaDataId, series, temp.getSecurityBearerToken()==null?"":temp.getSecurityBearerToken());
				tokenHandler.insertSecret(tenantId, WebHookAuthTokenHandler.HMAC_AUTHENTICATION_TYPE, metaDataId, series, temp.getSecurityToken()==null?"":temp.getSecurityToken());
				temp.setState(WebHookSubscriber.WEBHOOKSUBSCRIBERSTATE.UNCHANGED);
				newList.add(temp);
			} else if (temp.isChanged()) {
				String subscriberId = temp.getWebHookSubscriberId();
				String series = composeSeries(metaDataId, subscriberId);
				String basic = temp.getSecurityUsername()+":"+ temp.getSecurityPassword();
				String basicTokenSecret = Base64.encodeBase64String(basic.getBytes());
				tokenHandler.updateSecret(tenantId, WebHookAuthTokenHandler.BASIC_AUTHENTICATION_TYPE, metaDataId, series, basicTokenSecret);
				tokenHandler.updateSecret(tenantId, WebHookAuthTokenHandler.BEARER_AUTHENTICATION_TYPE, metaDataId, series, temp.getSecurityBearerToken()==null?"":temp.getSecurityBearerToken());
				tokenHandler.updateSecret(tenantId, WebHookAuthTokenHandler.HMAC_AUTHENTICATION_TYPE, metaDataId, series, temp.getSecurityToken()==null?"":temp.getSecurityToken());
				temp.setState(WebHookSubscriber.WEBHOOKSUBSCRIBERSTATE.UNCHANGED);
				newList.add(temp);	
			}else {
				//ここに来ると、UNCHANGED確定
				newList.add(temp);
			}
		}
		definition.setSubscribers(newList);
		return definition;
	}

	public String generateUuid() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}
}
