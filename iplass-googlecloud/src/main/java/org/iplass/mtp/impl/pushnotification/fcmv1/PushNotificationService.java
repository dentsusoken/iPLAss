/*
 * Copyright (C) 2024 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.pushnotification.fcmv1;


import java.io.IOException;
import java.net.SocketTimeoutException;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.GzipCompressingEntity;
import org.apache.hc.client5.http.utils.DateUtils;
import org.apache.hc.core5.http.ConnectionRequestTimeoutException;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.iplass.mtp.impl.googlecloud.GoogleCloudSettings;
import org.iplass.mtp.impl.http.ExponentialBackoff;
import org.iplass.mtp.impl.http.HttpClientConfig;
import org.iplass.mtp.pushnotification.NotificationPayload;
import org.iplass.mtp.pushnotification.PushNotification;
import org.iplass.mtp.pushnotification.PushNotificationException;
import org.iplass.mtp.pushnotification.PushNotificationResult;
import org.iplass.mtp.pushnotification.fcm.RegistrationIdHandler;
import org.iplass.mtp.pushnotification.fcmv1.PushNotificationResponseDetail;
import org.iplass.mtp.pushnotification.fcmv1.PushNotificationStatus;
import org.iplass.mtp.pushnotification.fcmv1.PushNotificationTarget;
import org.iplass.mtp.pushnotification.fcmv1.PushNotificationTargetType;
import org.iplass.mtp.pushnotification.fcmv1.RegistrationTokenHandler;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceConfigrationException;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Value;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

/**
 * Firebase Cloud Messaging (FCM) V1 API 通知サービス
 *
 * <p>
 * FCM V1 API を利用した Push 通知を実現する機能です。
 * FCM については、FCM サービス概要 を確認すること。
 * </p>
 *
 * <p>
 * Push通知を行う際の PushNotification は次のように設定されることを想定している。
 * </p>
 * <ol>
 * <li>{@link org.iplass.mtp.pushnotification.PushNotification#setMessage(Map)} に メッセージリソースの仕様 の通りに情報を設定する。宛先を含める。</li>
 * <li>
 * {@link org.iplass.mtp.pushnotification.PushNotification#setMessage(Map)} に メッセージリソースの仕様 の通りに情報が設定する。<br>
 * 宛先を含まない場合、{@link org.iplass.mtp.pushnotification.PushNotification#addTo(String)} で必要な宛先情報を追加する。<br>
 * 宛先には、宛先タイプを意味するプレフィックスを指定する必要がある。プレフィックスは {@link org.iplass.mtp.pushnotification.fcmv1.PushNotificationTargetType#getPrefixedValue(String)} を利用して付与する。
 * </li>
 * <li>
 * {@link org.iplass.mtp.pushnotification.PushNotification#setMessage(Map)} を利用しない場合、その他のメソッドに通知に関する情報を設定する。<br>
 * その際、{@link org.iplass.mtp.pushnotification.PushNotification#addTo(String)} で必要な宛先情報を追加する。<br>
 * 宛先には、宛先タイプを意味するプレフィックスを指定する必要がある。プレフィックスは {@link org.iplass.mtp.pushnotification.fcmv1.PushNotificationTargetType#getPrefixedValue(String)} を利用して付与する。
 * </li>
 * </ol>
 *
 * <p>
 * V1 API ではレスポンスに新しいデバイストークンが返却されることが無くなったので、
 * {@link RegistrationIdHandler#refreshRegistrationId(String, String)} を実行できない。
 * </p>
 *
 * <ul>
 * <li><a href="https://firebase.google.com/docs/cloud-messaging">FCM サービス概要</a></li>
 * <li><a href="https://firebase.google.com/docs/reference/fcm/rest/v1/projects.messages/send">FCM 送信 API仕様</a></li>
 * <li><a href="https://firebase.google.com/docs/reference/fcm/rest/v1/projects.messages">メッセージリソースの仕様</a></li>
 * <li><a href="https://firebase.google.com/docs/reference/fcm/rest/v1/ErrorCode">エラーコードの仕様</a></li>
 * <li><a href="https://firebase.google.com/docs/reference/fcm/rest/v1/FcmError">FcmError 仕様</a></li>
 * <li><a href="https://firebase.google.com/docs/reference/fcm/rest/v1/ErrorCode">FCM エラーコード</a></li>
 * <li><a href="https://firebase.google.com/docs/reference/fcm/rest/v1/ApnsError">ApnsError 仕様</a></li>
 * </ul>
 *
 * @author SEKIGUCHI Naoya
 */
public class PushNotificationService extends org.iplass.mtp.impl.pushnotification.PushNotificationService {
	/** レスポンス詳細キー：詳細リスト */
	public static final String RESPONSE_DETAILS_KEY_DETAIL_LIST = "responseDetailList";

	/** サービスエンドポイント：接頭辞 */
	private static final String SERVICE_ENDPOINT_PREFIX = "https://fcm.googleapis.com/v1/projects/";
	/** サービスエンドポイント：接尾辞 */
	private static final String SERVICE_ENDPOINT_POSTFIX = "/messages:send";
	/** レスポンスヘッダ：retry-after キー */
	private static final String RESPONSE_HEADER_RETRY_AFTER = "retry-after";
	/** 再実行間隔秒デフォルト値 */
	private static final long DEFAULT_RETRY_AFTER_SECONDS = 60L;
	/** 再実行が有効デフォルト値 */
	private static final boolean DEFAULT_ENABLE_RETRY = true;
	/** 数値パターン */
	private static final Pattern NUMBER_PATTERN = Pattern.compile("^[0-9]+$");

	/**
	 * Json Response キー
	 */
	private static class Keys {
		/**
		 * 成功時キー
		 */
		private static class Success {
			/** メッセージの識別子 */
			private static final String NAME = "name";
		}

		/**
		 * 失敗時キー
		 */
		private static class Fail {
			/** エラー */
			private static final String ERROR = "error";
			/** エラーメッセージ（error.message） */
			private static final String ERROR_MESSAGE = "message";
			/** エラー詳細（error.details） */
			private static final String ERROR_DETAILS = "details";
			/** エラー詳細タイプ（error.details[].@type） */
			private static final String ERROR_DETAILS_TYPE = "@type";
			/** エラー詳細エラーコード（error.details[].errorCode） */
			private static final String ERROR_DETAILS_ERROR_CODE = "errorCode";
		}
	}

	/**
	 * エラー詳細タイプ値
	 */
	private static class ErrorDetailsType {
		/** FcmError */
		private static final String FCM_ERROR = "type.googleapis.com/google.firebase.fcm.v1.FcmError";
	}

	/**
	 * エラー詳細コード値
	 */
	private static class ErrorDetailsErrorCode {
		/** 未登録 */
		private static final String UNREGISTERED = "UNREGISTERED";
	}

	/**
	 * PushApi リクエストメソッド用 FunctionalInterface
	 */
	@FunctionalInterface
	private interface PushApiRequestFunction {
		PushNotificationResponseDetail request(PushNotificationTarget target, Map<String, Object> messageResource);
	}

	// NOTE java 8 compatible. java 9 以上では Set.of
	/** リトライ可能と判定するHttpStatus */
	private Set<Integer> retryableStatusSet = Collections.unmodifiableSet(new HashSet<Integer>(Arrays.asList(
			HttpStatus.SC_TOO_MANY_REQUESTS, HttpStatus.SC_SERVICE_UNAVAILABLE, HttpStatus.SC_INTERNAL_SERVER_ERROR)));

	/** ロガー */
	private Logger logger = LoggerFactory.getLogger(PushNotificationService.class);

	/**
	 * FCM サービスエンドポイント
	 *
	 * <p>
	 * 以下の値が設定される。<br>
	 * https://fcm.googleapis.com/v1/projects/${projectId}/messages:send
	 * </p>
	 */
	private String serviceEndpoint;
	/** GoogleCloudSettings */
	private GoogleCloudSettings googleCloudSettings;
	/** リクエストエンティティ圧縮ファンクション。isCompressRequest の設定によってメソッドが設定される */
	private Function<HttpEntity, HttpEntity> compressRequestEntity;
	/** PushApi リクエストメソッド */
	private PushApiRequestFunction pushApi;

	// service-config property
	/** google cloud (firebase) project id */
	private String projectId;
	/** リクエストの圧縮設定。init メソッド以外で利用無し。 */
	private Boolean compressRequest;
	/** APIリクエストの検証のみに設定する */
	private Boolean apiRequestValidateOnly;
	/** リトライが有効 */
	private Boolean enableRetry;
	/** 指数バックオフ関数 */
	private ExponentialBackoff exponentialBackoff;
	/** 再実行間隔（秒）デフォルト */
	private Long defaultRetryAfterSeconds;
	/** RegistrationTokenHandler */
	private RegistrationTokenHandler registrationTokenHandler;
	/** ObjectMapper */
	private ObjectMapper objectMapper;
	/** HttpClientConfig */
	private HttpClientConfig httpClientConfig;

	@Override
	public void init(Config config) {
		super.init(config);

		// get config values
		projectId = config.getValue("projectId");
		compressRequest = config.getValue("compressRequest", Boolean.class, Boolean.TRUE);
		apiRequestValidateOnly = config.getValue("apiRequestValidateOnly", Boolean.class, Boolean.FALSE);
		enableRetry = config.getValue("enableRetry", Boolean.class, DEFAULT_ENABLE_RETRY);
		exponentialBackoff = enableRetry
				// 再実行が有効
				? config.getValue("exponentialBackoff", ExponentialBackoff.class, new ExponentialBackoff())
						// 再実行が無効
						: ExponentialBackoff.NO_RETRY;
		defaultRetryAfterSeconds = config.getValue("defaultRetryAfterSeconds", Long.class, DEFAULT_RETRY_AFTER_SECONDS).longValue();
		registrationTokenHandler = config.getValue("registrationTokenHandler", RegistrationTokenHandler.class, new EmptyRegistrationTokenHandler());
		httpClientConfig = config.getValueWithSupplier("httpClientConfig", HttpClientConfig.class, () -> new HttpClientConfig());
		objectMapper = config.getValueWithSupplier("objectMapper", ObjectMapper.class, () -> JsonMapper.builder()
				.disable(SerializationFeature.INDENT_OUTPUT)
				.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
				.enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS)
				.withConfigOverride(Map.class, c -> c.setInclude(Value.construct(JsonInclude.Include.NON_NULL, JsonInclude.Include.NON_NULL)))
				.build());

		// validation
		if (StringUtil.isEmpty(projectId)) {
			// projectId 未設定
			throw new ServiceConfigrationException("Service property 'projectId' is not set. Please fix the service-config settings.");
		}

		// instance initialze
		serviceEndpoint = SERVICE_ENDPOINT_PREFIX + projectId + SERVICE_ENDPOINT_POSTFIX;
		googleCloudSettings = ServiceRegistry.getRegistry().getService(GoogleCloudSettings.class);
		compressRequestEntity = compressRequest ? this::compressHttpEntity : this::plainHttpEntity;
		// リトライが有効な場合は、ExponentialBackoff が有効な処理を利用する
		pushApi = enableRetry ? this::requestPushApiExponentialBackoff : this::requestPushApi;
	}

	@Override
	public void destroy() {
	}

	@Override
	protected PushNotificationResult pushImpl(Tenant tenant, PushNotification notification) {
		boolean toListIsNotEmpty = 0 < notification.getToList().size();
		// TO の設定有無でエントリメソッドを変更する
		return toListIsNotEmpty ? pushByToList(tenant, notification) : pushByMessage(tenant, notification);
	}

	/**
	 * PushNotification の toList に設定されている宛先ベースで通知を送信する
	 *
	 * <p>
	 * toList に複数の宛先が設定されていた場合、１つの宛先毎に順番に通知を行います。
	 * </p>
	 *
	 * <p>
	 * {@link PushNotification#getMessage()} で取得するインスタンスに宛先情報が設定されていた場合、
	 * その宛先には通知が送信されない。
	 * </p>
	 *
	 * @param tenant テナント
	 * @param notification 通知情報
	 * @return 通知実行結果
	 */
	private PushNotificationResult pushByToList(Tenant tenant, PushNotification notification)  {
		Map<String, Object> message = toMessageResource(notification);
		PushNotificationTarget target = getTarget(message);

		if (null != target) {
			// message に target 指定されていても無視する
			message.remove(target.getType().getFieldName());
			// 無視される宛先の情報をログ出力
			logger.warn("A destination({}:{}) was set but is ignored, because it is the destination set for the message.", target.getType(), target.getId());
		}

		List<PushNotificationResponseDetail> responseDetailList = new ArrayList<PushNotificationResponseDetail>();
		for (String prefixedTo : notification.getToList()) {
			Map<String, Object> messageClone = new HashMap<>(message);
			PushNotificationTarget toTarget = PushNotificationTarget.create(prefixedTo);
			messageClone.put(toTarget.getType().getFieldName(), toTarget.getId());

			PushNotificationResponseDetail responseDetail = pushApi.request(toTarget, messageClone);
			responseDetailList.add(responseDetail);
		}

		Map<String, Object> detail = new HashMap<String, Object>();
		detail.put(RESPONSE_DETAILS_KEY_DETAIL_LIST, responseDetailList);
		// 失敗数が 0 であれば全て成功と判定する
		boolean isSuccessAll = responseDetailList.stream().filter(r -> PushNotificationStatus.SUCCESS != r.getStatus()).count() == 0;
		return new PushNotificationResult(isSuccessAll, detail);
	}

	/**
	 * PushNotification の message に設定されている宛先ベースで通知を送信する
	 *
	 * @param tenant テナント
	 * @param notification 通知情報
	 * @return 通知実行結果
	 */
	private PushNotificationResult pushByMessage(Tenant tenant, PushNotification notification)  {
		Map<String, Object> message = toMessageResource(notification);
		PushNotificationTarget target = getTarget(message);
		if (null == target) {
			// 宛先指定なし
			throw new PushNotificationException("Notification target information is not set. Please set " + PushNotificationTargetType.TOKEN.getFieldName() + " or "
					+ PushNotificationTargetType.TOPIC.getFieldName() + " or " + PushNotificationTargetType.CONDITION.getFieldName() + ".");
		}
		List<PushNotificationResponseDetail> responseDetailList = new ArrayList<PushNotificationResponseDetail>();
		PushNotificationResponseDetail responseDetail = pushApi.request(target, message);
		responseDetailList.add(responseDetail);

		Map<String, Object> detail = new HashMap<String, Object>();
		detail.put(RESPONSE_DETAILS_KEY_DETAIL_LIST, responseDetailList);
		// 失敗数が 0 であれば全て成功と判定する
		boolean isSuccessAll = responseDetailList.stream().filter(r -> PushNotificationStatus.SUCCESS != r.getStatus()).count() == 0;
		return new PushNotificationResult(isSuccessAll, detail);
	}

	/**
	 * 指数バックオフリトライを考慮した、FCM V1 API リクエストを行う
	 *
	 * <p>
	 * リトライ可能なエラーの場合は、レスポンスヘッダ "retry-after" の秒数待ち再度リクエストを行う。
	 * それ以外の場合は、リトライせず処理を終了する。
	 * </p>
	 *
	 * @param target 宛先情報
	 * @param messageResource メッセージリソース Map インスタンス
	 * @return 実行結果
	 */
	protected PushNotificationResponseDetail requestPushApiExponentialBackoff(PushNotificationTarget target, Map<String, Object> messageResource) {
		try {
			final PushNotificationResponseDetail[] result = new PushNotificationResponseDetail[] { null };
			final ZonedDateTime maxElapsedDateTime = ZonedDateTime.now().plus(Duration.ofMillis(exponentialBackoff.getMaxElapsedTimeMillis()));
			final int[] retryCount = new int[] { -1 };

			exponentialBackoff.execute(() -> {
				retryCount[0] = retryCount[0] + 1;

				PushNotificationResponseDetail responseDetail = requestPushApi(target, messageResource);
				// レスポンスを設定
				result[0] = responseDetail;

				if (PushNotificationStatus.FAIL_RETRYABLE == responseDetail.getStatus()) {
					ZonedDateTime waitFinishDateTime = ZonedDateTime.now().plus(Duration.ofSeconds(responseDetail.getRetryAfterSeconds()));
					if (maxElapsedDateTime.isAfter(waitFinishDateTime)) {
						// retry-after の秒数待ち、再実行を行うシーケンス
						try {
							Thread.sleep(Duration.ofSeconds(responseDetail.getRetryAfterSeconds()));
						} catch (InterruptedException e) {
							throw new PushNotificationException("FCM v1 API call thread is Interrupted.", e);
						}
						// 再実行（再実行を想定するが、exponentialBackoff で中断することもあり得る）
						return false;
					}

					// リトライ可能なエラーだが、最大経過時間を過ぎるので中断する。
					logger.warn("Retryable error, but aborted because the maximum elapsed time has passed. target = {}:{}.",
							responseDetail.getTarget().getType(), responseDetail.getTarget().getId());
				}

				return true;
			});

			// リトライ回数を設定したインスタンスを返却
			return PushNotificationResponseDetail.Builder.of(result[0])
					.setRetryCount(retryCount[0])
					.build();

		} catch (InterruptedException e) {
			throw new PushNotificationException("FCM v1 API call thread is Interrupted.", e);
		}
	}

	/**
	 * FCM V1 API リクエストを行う
	 *
	 * <p>
	 * FCM 送信 API仕様 に沿ったリクエストを行う。
	 * 実行結果は {@link #handleResponse(PushNotificationTarget, int, String, long)} でハンドリングする
	 * </p>
	 *
	 * @param target 宛先情報
	 * @param messageResource メッセージリソース Map インスタンス
	 * @return 実行結果
	 */
	protected PushNotificationResponseDetail requestPushApi(PushNotificationTarget target, Map<String, Object> messageResource) {
		HttpPost request = new HttpPost(serviceEndpoint);
		request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + googleCloudSettings.getAccessTokenValue());

		Map<String, Object> requestBodyMap = toRequestBody(messageResource);
		try {
			String requestBody = objectMapper.writeValueAsString(requestBodyMap);
			HttpEntity requestEntity = compressRequestEntity.apply(new StringEntity(requestBody, ContentType.APPLICATION_JSON));
			request.setEntity(requestEntity);

			logger.debug("Request - requestBody = {}, headers = {}", requestBody, request.getHeaders());

			PushNotificationResponseDetail responseDetail = httpClientConfig.getInstance().execute(request, response -> {
				HttpEntity entity = response.getEntity();
				try {
					String contents = EntityUtils.toString(entity);
					logger.debug("Response - serviceEndpoint = {}, code = {}({}), contents = {}, headers = {}",
							serviceEndpoint, response.getCode(), response.getReasonPhrase(), contents, response.getHeaders());
					// response header "retry-after" の値を数値変換する。設定が無い場合は、defaultRetryAfterMillis を設定する。
					Header retryAfterHeader = response.getFirstHeader(RESPONSE_HEADER_RETRY_AFTER);
					String retryAfterValue = null != retryAfterHeader ? retryAfterHeader.getValue() : null;
					long retryAfterSeconds = toRetryAfterSeconds(retryAfterValue, defaultRetryAfterSeconds);

					return handleResponse(target, response.getCode(), contents, retryAfterSeconds);

				} finally {
					EntityUtils.consume(entity);
				}
			});

			postProcessOfReequestPushApi(responseDetail);

			return responseDetail;

		} catch (ConnectionRequestTimeoutException | SocketTimeoutException e) {
			// 通信タイムアウト関連
			return PushNotificationResponseDetail.Builder.fail(PushNotificationStatus.FAIL_TIMEOUT)
					.setErrorMessage(e.getMessage())
					.setCause(e)
					.setTarget(target)
					.build();

		} catch (IOException e) {
			// その他のIO例外
			return PushNotificationResponseDetail.Builder.fail(PushNotificationStatus.FAIL)
					.setErrorMessage(e.getMessage())
					.setCause(e)
					.setTarget(target)
					.build();
		}
	}

	/**
	 * 実行結果をハンドリングする
	 * <p>
	 * 外部サービスの実行結果を受け、通知の結果を生成する。
	 * </p>
	 * @param target 通知対象
	 * @param code API 実行結果（HttpStatus）
	 * @param response レスポンスボディ
	 * @param retryAfterSeconds 再実行間隔（秒）
	 * @return 実行結果詳細インスタンス
	 * @throws IOException 入出力例外
	 */
	protected PushNotificationResponseDetail handleResponse(PushNotificationTarget target, int code, String response, long retryAfterSeconds)
			throws IOException {
		@SuppressWarnings("unchecked")
		Map<String, Object> contentsMap = objectMapper.readValue(response, Map.class);

		if (HttpStatus.SC_OK == code) {
			// 正常レスポンス
			return PushNotificationResponseDetail.Builder.success()
					.setTarget(target)
					.setResponse(response)
					.setMessageId((String) contentsMap.get(Keys.Success.NAME))
					.build();
		}

		// エラーレスポンス
		//
		// NOTE ドキュメントとエラーコードの仕様が合っていない。FcmError では フィールド名が error_code と記載されているが、実際は errorCode。
		// 実際のエラーレスポンス
		//
		// ```
		// {
		//   "error": {
		//     "code": 404,
		//     "message": "Requested entity was not found.",
		//     "status": "NOT_FOUND",
		//     "details": [
		//       {
		//         "@type": "type.googleapis.com/google.firebase.fcm.v1.FcmError",
		//         "errorCode": "UNREGISTERED"
		//       }
		//     ]
		//   }
		// }
		// ```
		Object error = contentsMap.get(Keys.Fail.ERROR);
		if (error instanceof String) {
			// "error" キーの値が文字列の場合
			return PushNotificationResponseDetail.Builder.fail(PushNotificationStatus.FAIL)
					.setTarget(target)
					.setResponse(response)
					.setErrorMessage((String) error)
					.build();
		}

		@SuppressWarnings("unchecked")
		Map<String, Object> errorMap = (Map<String, Object>) error;
		String errorMessage = (String) errorMap.get(Keys.Fail.ERROR_MESSAGE);

		if (retryableStatusSet.contains(code)) {
			// 再試行可能な HttpStatus の場合
			return PushNotificationResponseDetail.Builder.fail(PushNotificationStatus.FAIL_RETRYABLE)
					.setTarget(target)
					.setResponse(response)
					.setErrorMessage(errorMessage)
					.setRetryAfterSeconds(retryAfterSeconds)
					.build();
		}

		// 再試行可能ではない場合、デバイスが未登録か判断し、status を決定
		PushNotificationStatus status = PushNotificationStatus.FAIL;
		Object errorDetails = errorMap.get(Keys.Fail.ERROR_DETAILS);
		if (null != errorDetails && errorDetails instanceof List) {
			@SuppressWarnings("unchecked")
			List<Object> errorDetailsList = (List<Object>) errorDetails;
			for (Object detail : errorDetailsList) {
				if (detail instanceof Map) {
					@SuppressWarnings("unchecked")
					Map<String, Object> detailMap = (Map<String, Object>) detail;
					if (ErrorDetailsType.FCM_ERROR.equals(detailMap.get(Keys.Fail.ERROR_DETAILS_TYPE))
							&& ErrorDetailsErrorCode.UNREGISTERED.equals(detailMap.get(Keys.Fail.ERROR_DETAILS_ERROR_CODE))) {
						status = PushNotificationStatus.FAIL_DEVICE_UNREGISTERED;
						break;
					}
				}
			}
		}

		return PushNotificationResponseDetail.Builder.fail(status)
				.setTarget(target)
				.setResponse(response)
				.setErrorMessage(errorMessage)
				.build();
	}

	/**
	 * 通知APIリクエスト後処理
	 *
	 * <p>
	 * API の結果によって実行する処理を定義する。
	 * </p>
	 *
	 * @param responseDetail
	 */
	protected void postProcessOfReequestPushApi(PushNotificationResponseDetail responseDetail) {
		if (PushNotificationTargetType.TOKEN == responseDetail.getTarget().getType()
				&& PushNotificationStatus.FAIL_DEVICE_UNREGISTERED == responseDetail.getStatus()) {
			// DEVICE_UNREGISTERED の場合に、未登録を通知する。
			registrationTokenHandler.unregistered(responseDetail.getTarget());
		}

	}

	/**
	 * メッセージリソースから通知先を取得する
	 * @param message メッセージリソース
	 * @return 宛先情報
	 */
	private PushNotificationTarget getTarget(Map<String, Object> message) {
		for (PushNotificationTargetType type : PushNotificationTargetType.values()) {
			String v = (String) message.get(type.getFieldName());
			if (null != v) {
				return new PushNotificationTarget(type, v);
			}
		}

		// 存在しない場合は null
		return null;
	}

	/**
	 * ヘッダー "retry-after" の値を再実行間隔秒に変換する。
	 *
	 * <p>
	 * <a href="https://developer.mozilla.org/ja/docs/Web/HTTP/Headers/Retry-After">retry-afterの仕様</a>の通り解釈する。
	 * 数値が設定されていた場合は、秒と解釈する。
	 * それ以外の場合は、実行可能日時と判定し、現在日時との差分の秒を返却する。
	 * retryAfter が設定されていない場合、defaultSeconds を返却する。
	 * </p>
	 *
	 *
	 * @param retryAfter "retry-after" ヘッダー値
	 * @param defaultSeconds デフォルト秒数
	 * @return 再実行間隔秒
	 */
	private long toRetryAfterSeconds(String retryAfter, long defaultSeconds) {
		// retry-after の仕様
		// https://developer.mozilla.org/ja/docs/Web/HTTP/Headers/Retry-After
		if (null == retryAfter || 0 == retryAfter.length()) {
			return defaultSeconds;
		}

		boolean isNumber = NUMBER_PATTERN.matcher(retryAfter).matches();
		if (isNumber) {
			return Long.valueOf(retryAfter);
		}

		ZonedDateTime now = ZonedDateTime.now();
		ZonedDateTime retryAfterDate = DateUtils.parseStandardDate(retryAfter).atZone(ZoneId.systemDefault());

		long retryAfterSeconds = Duration.between(now, retryAfterDate).toSeconds();
		// マイナス値になる場合は 0 を設定する
		return 0 <= retryAfterSeconds ? retryAfterSeconds : 0;
	}

	/**
	 * FCM メッセージ送信 V1 API のリクエスト本文に変換する。
	 *
	 * <p>
	 * FCM 送信 API仕様 を参照。
	 * リクエスト本文に該当する Map インスタンスに変換する。
	 * validate_only は本サービスの "apiRequestValidateOnly" の設定値によって設定される。
	 * </p>
	 *
	 * <p>
	 * 参考：リクエスト本文 JSON イメージ
	 * <pre>
	 * {
	 *   "validate_only": boolean,
	 *   "message": {
	 *     object (Message)
	 *   }
	 * }
	 * </pre>
	 * </p>
	 *
	 * @param messageResource メッセージリソース
	 * @return リクエスト本文 Map
	 */
	private Map<String, Object> toRequestBody(Map<String, Object> messageResource) {
		// メッセージ仕様
		// https://firebase.google.com/docs/reference/fcm/rest/v1/projects.messages/send
		Map<String, Object> data = new HashMap<>();
		if (apiRequestValidateOnly) {
			data.put("validate_only", true);
		}
		data.put("message", messageResource);

		return data;
	}

	/**
	 * FCM メッセージ送信 V1 API のリクエスト本文のメッセージリソースに変換する。
	 *
	 * <p>
	 * メッセージリソースの仕様 を参照。
	 * PushNotification から、リクエスト本文の message 部に該当するMapオブジェクトへ変換する。
	 * 宛先情報（token|topic|condition) は、ではスコープ外となります。
	 * </p>
	 *
	 * <p>
	 * {@link PushNotification#getMessage()} で取得可能な情報をベースにメッセージを構築する。
	 * PushNotification のその他の設定値が存在する場合、設定値で上書きする。<br>
	 *
	 * その他の設定値をメッセージリソースへマッピングするイメージ
	 * <pre>
	 * {
	 *   "data": object (PushNotification#getData()),
	 *   "notification": object (PushNotification#getNotification()),
	 *   "android": {
	 *     "notification": object (PushNotification#getNotification()), // icon キーが存在する場合のみ
	 *   },
	 *   "fcm_options": object (notification.getOptions())
	 * }
	 * </pre>
	 * </p>
	 *
	 *
	 * @see #toRequestBody(Map)
	 * @param notification 通知インスタンス
	 * @return メッセージリソース
	 */
	private Map<String, Object> toMessageResource(PushNotification notification) {
		Map<String, Object> message = notification.getMessage() == null ? new HashMap<String, Object>() : new HashMap<>(notification.getMessage());

		ifNotNullPutAll(message, "data", notification.getData());
		ifNotNullPutAll(message, "fcm_options", notification.getOptions());

		// Legacy API 仕様では、notification に icon フィールドが含まれる可能性がある。
		// icon フィールドは android 専用の項目となるので、android.notification にも設定する。
		NotificationPayload notificationPayload = notification.getNotification();
		if (notificationPayload.containsKey(NotificationPayload.ICON)) {
			// icon フィールド有り
			// notification には、 icon を除いた notificationPayload を設定
			Map<String, Object> copyPayload = new HashMap<>(notificationPayload);
			copyPayload.remove(NotificationPayload.ICON);
			ifNotNullPutAll(message, "notification", copyPayload);

			// android.notification に notificationPayload を設定
			Map<String, Object> android = new HashMap<String, Object>();
			android.put("notification", notificationPayload);
			ifNotNullPutAll(message, "android", android);

		} else {
			// icon フィールド無しの場合は notification にそのまま設定
			ifNotNullPutAll(message, "notification", notificationPayload);
		}

		return message;
	}

	/**
	 * valueMap に要素が設定されている場合に、map の key に対して設定する。
	 * <p>
	 * map の key に既に Map が設定されている場合、当該 Map インスタンスに対して追加設定する。
	 * （既に設定されている値はそのまま残り、valueMap に設定されている値が 設定|上書き される。
	 * </p>
	 *
	 * @param map 親マップ
	 * @param key 親マップのキー
	 * @param valueMap 設定する値
	 */
	@SuppressWarnings("unchecked")
	private void ifNotNullPutAll(Map<String, Object> map, String key, Map<String, Object> valueMap) {
		if (null != valueMap && !valueMap.isEmpty()) {
			Object child = map.get(key);
			if (null == child) {
				child = new HashMap<>();
				map.put(key, child);
			}

			if (child instanceof Map) {
				Map<String, Object> childMap = (Map<String, Object>) child;
				childMap.putAll(valueMap);

			} else {
				logger.warn("The instance set to the key '{}' is not a java.util.Map. (Class of value set: '{}')", key, child.getClass().getName());
			}
		}
	}

	/**
	 * そのままの entity を返却する。
	 * <p>
	 * isCompressRequest == false の場合に、compressRequestEntity に設定されるメソッド
	 * </p>
	 * @param entity HttpEntity
	 * @return HttpEntity
	 */
	private HttpEntity plainHttpEntity(HttpEntity entity) {
		return entity;
	}

	/**
	 * 圧縮する HttpEntity を返却する。
	 * <p>
	 * 圧縮は HTTP V1 API がサポートしている、GZip 圧縮とする
	 * </p>
	 * <p>
	 * isCompressRequest == true の場合に、compressRequestEntity に設定されるメソッド
	 * </p>
	 * @param entity HttpEntity
	 * @return HttpEntity
	 */
	private HttpEntity compressHttpEntity(HttpEntity entity) {
		return new GzipCompressingEntity(entity);
	}
}
