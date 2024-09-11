/*
 * Copyright (C) 2016 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.pushnotification.fcm;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.utils.DateUtils;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.iplass.mtp.impl.http.ExponentialBackoff;
import org.iplass.mtp.impl.http.HttpClientConfig;
import org.iplass.mtp.impl.pushnotification.PushNotificationService;
import org.iplass.mtp.pushnotification.PushNotification;
import org.iplass.mtp.pushnotification.PushNotificationException;
import org.iplass.mtp.pushnotification.PushNotificationResult;
import org.iplass.mtp.pushnotification.fcm.RegistrationIdHandler;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.tenant.Tenant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Firebase Cloud Messaging(FMC) プッシュ通知サービス
 */
public class FCMPushNotificationService extends PushNotificationService {
	private static Logger logger = LoggerFactory.getLogger(FCMPushNotificationService.class);

	private String authorizationKey;
	private boolean dryRun;
	private boolean enableRetry = true;

	private RegistrationIdHandler registrationIdHandler;
	//for Exponential Backoff
	private ExponentialBackoff exponentialBackoff;

	private HttpClientConfig httpClientConfig;

	private String endpoint ="https://fcm.googleapis.com/fcm/send";

	private ObjectMapper jsonMapper;

	private Pattern conditionPattern = Pattern.compile(".*in\\s*topics.*", Pattern.CASE_INSENSITIVE);

	@Override
	public void init(Config config) {
		super.init(config);
		authorizationKey = config.getValue("authorizationKey");
		if (config.getValue("dryRun") != null) {
			dryRun = Boolean.valueOf(config.getValue("dryRun"));
		}
		if (config.getValue("enableRetry") != null) {
			enableRetry = Boolean.valueOf(config.getValue("enableRetry"));
		}
		if (enableRetry) {
			exponentialBackoff = config.getValue("exponentialBackoff", ExponentialBackoff.class);
			if (exponentialBackoff == null) {
				exponentialBackoff = new ExponentialBackoff();
			}
		} else {
			exponentialBackoff = ExponentialBackoff.NO_RETRY;
		}

		registrationIdHandler = config.getValue("registrationIdHandler", RegistrationIdHandler.class);
		httpClientConfig = config.getValue("httpClientConfig", HttpClientConfig.class);
		if (httpClientConfig == null) {
			httpClientConfig = new HttpClientConfig();
			httpClientConfig.inited(this, config);
		}
		if (config.getValue("endpoint") != null) {
			endpoint = config.getValue("endpoint");
		}

		jsonMapper = new ObjectMapper();
		jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		jsonMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
		jsonMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);

	}

	@Override
	public void destroy() {
		jsonMapper = null;
		super.destroy();
	}

	@Override
	protected PushNotificationResult pushImpl(Tenant tenant, PushNotification notification) {
		try {
			boolean isMulti = notification.getToList().size() > 1;
			long endTime = System.currentTimeMillis() + exponentialBackoff.getMaxElapsedTimeMillis();
			PushNotificationResult result = new PushNotificationResult();
			long[] retryAfter = new long[]{-1};
			exponentialBackoff.execute(() -> {

				//retryAfter
				long currentTime = System.currentTimeMillis();
				if (retryAfter[0] > currentTime) {
					if (retryAfter[0] > endTime) {
						//timeout
						if (logger.isDebugEnabled()) {
							logger.debug("execution failed cause retryAfter Header value is over maxElapsedTimeMillis.");
						}
						result.setDetail("retryAfter", new Date(retryAfter[0]));
						return true;
					} else {
						try {
							Thread.sleep(retryAfter[0] - currentTime);
						} catch (Exception e) {
							throw new PushNotificationException("FCM call thread is Interrupted.", e);
						}
					}
				}

				try {
					if (isMulti) {
						return multiRegistrationIdCall(notification, result, retryAfter);
					} else {
						return simpleCall(notification, result, retryAfter);
					}
				} catch (ParseException | IOException e) {
					if (logger.isDebugEnabled()) {
						logger.debug("can not access FCM... cause:" + e.getMessage(), e);
					}
					result.setDetail("lastException", e);
					return false;
				}
			});

			return result;
		} catch (InterruptedException e) {
			throw new PushNotificationException("FCM call thread is Interrupted.", e);
		}
	}

	private long parseRetryAfter(HttpResponse res) {
		Header header = res.getFirstHeader("Retry-After");
		if (header == null) {
			return -1;
		}
		String val = header.getValue();
		if (val == null) {
			return -1;
		}
		try {
			long valLong = Long.parseLong(val);
			return System.currentTimeMillis() + valLong * 1000;
		} catch (NumberFormatException e) {
			//try parse as date
		}

		Date d = DateUtils.parseDate(val);
		if (d == null) {
			return -1;
		}
		return d.getTime();
	}

	private enum ResultType {
		SUCCESS,
		RETRY,
		ERROR
	}

	private ResultType handleResult(Map<String, Object> res, String regId) {
		//check error
		String error = (String) res.get(FCMConstants.ERROR);
		if (error != null) {
			if (error.equals(FCMConstants.ERROR_NOT_REGISTERED)) {
				//remove RegistrationId
				if (registrationIdHandler != null && regId != null) {
					registrationIdHandler.removeRegistrationId(regId);
				}
			}

			if (isRetryableError(error)) {
				return ResultType.RETRY;
			} else {
				return ResultType.ERROR;
			}
		}

		//check refresh RegistrationId
		if (registrationIdHandler != null && regId != null) {
			String newRegId = (String) res.get(FCMConstants.REGISTRATION_ID);
			if (newRegId != null) {
				registrationIdHandler.refreshRegistrationId(regId, newRegId);
			}
		}

		return ResultType.SUCCESS;
	}

	private boolean isRetryableError(String error) {
		switch (error) {
		case FCMConstants.ERROR_UNVAILABLE:
		case FCMConstants.ERROR_INTERNAL_SERVER_ERROR:
			return true;
		default:
			return false;
		}
	}

	interface ResHandler {
		void handle(ClassicHttpResponse res) throws ParseException, IOException;
	}

	private int sendMsg(PushNotification notification, ResHandler resHandler) throws ParseException, IOException {
		// jsonMsg is final
		final String jsonMsg = toJson(notification);

		if (logger.isDebugEnabled()) {
			logger.debug("call FCM whith message:" + jsonMsg);
		}

		HttpClient client = httpClientConfig.getInstance();
		HttpPost post = new HttpPost(endpoint);
		post.setHeader("Authorization", "key=" + authorizationKey);
		post.setHeader("Content-Type", "application/json");
		post.setEntity(new StringEntity(jsonMsg, StandardCharsets.UTF_8));

		return client.execute(post, response -> {
			try {
				final int status = response.getCode();

				if (401 == status) {
					//認証エラー
					throw new PushNotificationException("can not auth.");
				}

				if (status == 400) {
					if (logger.isDebugEnabled()) {
						logger.debug("{} {} {}", response.getVersion(), response.getCode(), response.getReasonPhrase());
						if (response.getHeaders() != null) {
							for (Header h : response.getHeaders()) {
								logger.debug(h.toString());
							}
						}
						if (response instanceof ClassicHttpResponse cres) {
							if (cres.getEntity() != null) {
								logger.debug(EntityUtils.toString(cres.getEntity(), StandardCharsets.UTF_8));
							}
						}
					}
					//無効な JSON
					throw new PushNotificationException("invalid json message:" + jsonMsg);
				}

				if (!(status >= 500 && status < 600) && status != 200) {
					//unknown status
					throw new PushNotificationException("FCM return unknown status:" + status);
				}

				resHandler.handle(response);
				return status;

			} finally {
				EntityUtils.consume(response.getEntity());
			}
		});
	}

	private String toJson(PushNotification notification) {
		try {
			return jsonMapper.writeValueAsString(toMessageMap(notification));
		} catch (JsonProcessingException e) {
			// Note: writeValueAsString は 2.1 で IOException の throws が削除されている
			throw new PushNotificationException("can not serialize to json", e);
		}
	}

	@SuppressWarnings("unchecked")
	private boolean simpleCall(PushNotification notification, PushNotificationResult result, long[] retryAfter) throws ParseException, IOException {

		final String[] content = new String[]{null};

		int status = sendMsg(notification, (res) -> {
			retryAfter[0] = parseRetryAfter(res);
			HttpEntity entity = res.getEntity();
			if (entity != null) {
				content[0] = EntityUtils.toString(entity, StandardCharsets.UTF_8);
			}
		});

		if (logger.isDebugEnabled()) {
			logger.debug("FCM response:status=" + status + ", content=" + content[0]);
		}

		if (status >= 500) {
			//retry
			return false;
		}

		Map<String, Object> cmap = jsonMapper.readValue(content[0], Map.class);
		result.setDetails(cmap);

		ResultType rt;
		if (cmap.get(FCMConstants.MESSAGE_ID) != null) {
			//topic message
			rt = handleResult(cmap, null);
		} else {
			//direct message
			rt = handleResult(((List<Map<String, Object>>) cmap.get(FCMConstants.RESULTS)).get(0), notification.getToList().get(0));
		}

		switch (rt) {
		case ERROR:
			return true;
		case RETRY:
			return false;
		case SUCCESS:
			result.setSuccess(true);
			return true;
		default:
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	private boolean multiRegistrationIdCall(PushNotification notification, PushNotificationResult result, long[] retryAfter) throws ParseException, IOException {

		List<Map<String, Object>> orgResults = (List<Map<String, Object>>) result.getDetail(FCMConstants.RESULTS);
		List<Integer> indexMap = null;
		List<String> ids = notification.getToList();
		PushNotification ntf = notification;

		if (orgResults != null) {
			indexMap = new ArrayList<>();
			ids = new ArrayList<>();
			List<String> orgList = notification.getToList();
			for (int i = 0; i < orgList.size(); i++) {
				String error = (String) orgResults.get(i).get(FCMConstants.ERROR);
				if (error != null && isRetryableError(error)) {
					indexMap.add(i);
					ids.add(orgList.get(i));
				}
			}
			ntf = new PushNotification();
			ntf.setToList(ids);
			ntf.setData(notification.getData());
			ntf.setNotification(notification.getNotification());
			ntf.setOptions(notification.getOptions());
		}

		final String[] content = new String[]{null};

		int status = sendMsg(ntf, (res) -> {
			retryAfter[0] = parseRetryAfter(res);
			HttpEntity entity = res.getEntity();
			if (entity != null) {
				content[0] = EntityUtils.toString(entity, StandardCharsets.UTF_8);
			}
		});

		if (logger.isDebugEnabled()) {
			logger.debug("FCM response:status=" + status + ", content=" + content[0]);
		}

		if (status >= 500) {
			//retry
			return false;
		}

		Map<String, Object> cmap = jsonMapper.readValue(content[0], Map.class);
		List<Map<String, Object>> rs = (List<Map<String, Object>>) cmap.get(FCMConstants.RESULTS);
		boolean needRetry = false;
		for (int i = 0; i < ids.size(); i++) {
			ResultType rt = handleResult(rs.get(i), ids.get(i));
			if (rt == ResultType.RETRY) {
				needRetry = true;
			}
		}

		if (orgResults == null) {
			result.setDetails(cmap);
			orgResults = rs;
		} else {
			//merge result
			int totalSuccessCount = ((Number) result.getDetail(FCMConstants.SUCCESS)).intValue();
			int totalFailureCount = ((Number) result.getDetail(FCMConstants.FAILURE)).intValue();
			int totalCanonicalIdsCount = ((Number) result.getDetail(FCMConstants.CANONICAL_IDS)).intValue();

			int successCount = ((Number) cmap.get(FCMConstants.SUCCESS)).intValue();
			int canonicalIdsCount = ((Number) cmap.get(FCMConstants.CANONICAL_IDS)).intValue();

			totalSuccessCount = totalSuccessCount + successCount;
			totalFailureCount = totalFailureCount - successCount;
			totalCanonicalIdsCount = totalCanonicalIdsCount + canonicalIdsCount;
			result.setDetail(FCMConstants.SUCCESS, totalSuccessCount);
			result.setDetail(FCMConstants.FAILURE, totalFailureCount);
			result.setDetail(FCMConstants.CANONICAL_IDS, totalCanonicalIdsCount);

			for (int i = 0; i < ids.size(); i++) {
				orgResults.set(indexMap.get(i), rs.get(i));
			}
		}

		if (needRetry) {
			return false;
		}

		//check success
		boolean hasError = false;
		for (int i = 0; i < orgResults.size(); i++) {
			String error = (String) orgResults.get(i).get(FCMConstants.ERROR);
			if (error != null) {
				hasError = true;
				break;
			}
		}

		if (!hasError) {
			result.setSuccess(true);
		}

		return true;
	}

	private Map<String, Object> toMessageMap(PushNotification notification) {
		HashMap<String, Object> map = new HashMap<>();
		List<String> toList = notification.getToList();
		if (toList.size() == 0) {
			throw new PushNotificationException("To list not specified.");
		}
		if (toList.size() == 1) {
			if (conditionPattern.matcher(toList.get(0)).matches()) {
				map.put(FCMConstants.CONDITION, toList.get(0));
			} else {
				map.put(FCMConstants.TO, toList.get(0));
			}
		} else {
			map.put(FCMConstants.REGISTRATION_IDS, toList);
		}

		if (notification.getOptions() != null) {
			map.putAll(notification.getOptions());
		}

		if (notification.getData() != null && notification.getData().size() > 0) {
			HashMap<String, Object> dataMap = new HashMap<>();
			for (String key: notification.getData().keySet()) {
				dataMap.put(key, notification.getData().get(key));
			}
			map.put(FCMConstants.DATA, dataMap);
		}

		if (notification.getNotification() != null && notification.getNotification().size() > 0) {
			HashMap<String, Object> notificationMap = new HashMap<>();
			for (String key: notification.getNotification().keySet()) {
				notificationMap.put(key, notification.getNotification().get(key));
			}
			map.put(FCMConstants.NOTIFICATION, notificationMap);
		}

		if (dryRun) {
			map.put(FCMConstants.DRY_RUN, true);
		}

		return map;
	}

}
