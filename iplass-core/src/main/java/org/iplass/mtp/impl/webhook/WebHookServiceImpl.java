package org.iplass.mtp.impl.webhook;

import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.ArrayList;

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
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.impl.definition.AbstractTypedMetaDataService;
import org.iplass.mtp.impl.definition.DefinitionMetaDataTypeMap;
import org.iplass.mtp.impl.http.HttpClientConfig;
import org.iplass.mtp.impl.webhook.template.MetaWebHookTemplate;
import org.iplass.mtp.impl.webhook.template.MetaWebHookTemplate.WebHookTemplateRuntime;
import org.iplass.mtp.spi.Config;
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
	private HttpClientConfig httpClientConfig;
	private static Logger logger = LoggerFactory.getLogger(WebHookServiceImpl.class);
	public static final String WEBHOOK_TEMPLATE_META_PATH = "/webhook/template/";

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
		httpClientConfig = new HttpClientConfig();
		httpClientConfig.setProxyHost("http://sg-sd27b-1.isid.co.jp");//TODO: 設置できるように
		httpClientConfig.setProxyPort(8080);
		httpClientConfig.setConnectionTimeout(10000);
		httpClientConfig.setPoolingMaxTotal(100);
		httpClientConfig.setPoolingTimeToLive(30000);

		/**
		 * httpClientConfig = (HttpClientConfig)
		 * config.getValue("httpClientConfig",null); if (httpClientConfig == null) {
		 * httpClientConfig = new HttpClientConfig(); httpClientConfig.inited(this,
		 * config);
		 * 
		 * }
		 */
		httpClientConfig.inited(this, config);
	}

	@Override
	public void destroy() {
	}

	@Override
	public WebHook createWebHook(Tenant tenant, String charset) {
		return new WebHook();
	}

	@Override
	public void sendWebHook(Tenant tenant, WebHook webHook) {
		try {
			//if proxy is set
			HttpHost proxy = new HttpHost("sg-sd27b-1.isid.co.jp", 8080, "http");//TODO: 設置できるように

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
						return false;
						}
					};
					httpClientBuilder = HttpClientBuilder.create().setRetryHandler(requestRetryHandler).setProxy(proxy);
			} else {
				httpClientBuilder = HttpClientBuilder.create().disableAutomaticRetries().setProxy(proxy);
			}
			
			try {
				CloseableHttpClient httpClient= httpClientBuilder.build();
				ArrayList<WebHookSubscriber> receivers = new ArrayList<WebHookSubscriber>(webHook.getSubscribers());

				//fill in webhook payload
				String payload = webHook.getContent().getContent();
				StringEntity se = new StringEntity(payload);
				se.setContentType(webHook.getContent().getContentTypeString());
				
				
				
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
						String tokenTemp = temp.getSecurityToken();
						httpPost.setHeader("iplass-token", tokenTemp);//TODO sha256 tokenがヘッダーに表示する名前
					}
					if (temp.getSecurityUsername()!=null&&temp.getSecurityPassword()!=null) {
						String basic = temp.getSecurityUsername()+":"+ temp.getSecurityPassword();
						basic ="Basic " + Base64.encodeBase64String(basic.getBytes());
						httpPost.setHeader(HttpHeaders.AUTHORIZATION, basic);
					}
					CloseableHttpResponse response = httpClient.execute(httpPost);
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
		}catch(

	Exception e)
	{
		// handleException
	}

}

}
