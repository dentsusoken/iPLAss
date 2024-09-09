/*
 * Copyright (C) 2019 DENTSU SOKEN INC. All Rights Reserved.
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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.function.Predicate;

import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple Http リクエスト実行機能
 */
public class SimpleHttpInvoker {
	private static Logger logger = LoggerFactory.getLogger(SimpleHttpInvoker.class);

	private HttpClientConfig httpClientConfig;
	private ExponentialBackoff exponentialBackoff;
	/** レスポンス文字コード */
	private Charset contentCharset = StandardCharsets.UTF_8;

	/**
	 * コンストラクタ
	 * @param httpClient httpClientConfig
	 * @param exponentialBackoff ExponentialBackoff
	 */
	public SimpleHttpInvoker(HttpClientConfig httpClientConfig, ExponentialBackoff exponentialBackoff) {
		this.httpClientConfig = httpClientConfig;
		if (exponentialBackoff == null) {
			this.exponentialBackoff = ExponentialBackoff.NO_RETRY;
		} else {
			this.exponentialBackoff = exponentialBackoff;
		}
	}

	private final boolean noRetry(final Response res) {
		return true;
	}

	/**
	 * リクエストを実行する
	 *
	 * <p>
	 * 実行に失敗したら、失敗レスポンスを返却する
	 * </p>
	 *
	 * @param request Httpリクエスト
	 * @return リクエスト実行結果
	 */
	public Response call(HttpUriRequest request) {
		return call(request, this::noRetry);
	}

	/**
	 * リクエストを実行する
	 *
	 * <p>
	 * 実行に成功するか、リトライ停止条件に達するまでリクエストを実行する。
	 * リトライ閾値に達し実行に失敗している場合は、失敗レスポンスを返却する
	 * </p>
	 *
	 * @param request Httpリクエスト
	 * @param stopRetryCondition リトライ停止条件
	 * @return リクエスト実行結果
	 */
	public Response call(HttpUriRequest request, Predicate<Response> stopRetryCondition) {
		Response response = new Response();

		long start = System.currentTimeMillis();
		try {
			exponentialBackoff.execute(() -> {
				try {
					httpClientConfig.getInstance().execute(request, resp -> {
						HttpEntity entity = resp.getEntity();

						try {
							response.status = resp.getCode();
							response.content = null == entity ? null : EntityUtils.toString(entity, contentCharset);
							return null;

						} finally {
							EntityUtils.consume(entity);
						}
					});

				} catch (IOException e) {
					if (logger.isDebugEnabled()) {
						logger.debug("error while http call: {}", request.toString(), e);
					}

					if (response.exception != null) {
						e.addSuppressed(response.exception);
					}

					response.exception = e;
					response.status = 0;
					response.content = null;

				} finally {
					if (request instanceof HttpUriRequestBase httpUriRequestBase) {
						httpUriRequestBase.reset();
					}
				}

				return stopRetryCondition.test(response);
			});

		} catch (InterruptedException e) {
			if (response.exception != null) {
				e.addSuppressed(response.exception);
			}
			response.exception = e;
			response.status = 0;
			response.content = null;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("call external web resource: {} {}ms", request, (System.currentTimeMillis() - start));
		}

		return response;
	}

	/**
	 * レスポンスインスタンス
	 */
	public static class Response {
		/** コンテンツ */
		public String content;
		/** http status */
		public int status;
		/** 失敗時の例外 */
		public Exception exception;
	}

}
