/*
 * Copyright (C) 2026 DENTSU SOKEN INC. All Rights Reserved.
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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package org.iplass.mtp.impl.report.converter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.UUID;

/**
 * Gotenberg（Docker 上のステートレス HTTP API）でドキュメント変換を行う実装。
 *
 * <p>POST {baseUrl}/forms/libreoffice/convert へ multipart/form-data でファイルを送信し、
 * 応答バイナリ（PDF 等）を返す。新規依存なし（JDK 標準 HttpClient）。</p>
 */
public class GotenbergDocumentConverter implements DocumentConverter {

	private String baseUrl = "http://localhost:3000";

	private int connectTimeoutSeconds = 5;

	private int requestTimeoutSeconds = 60;

	private int maxRetries = 0;

	private volatile HttpClient client;

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public void setConnectTimeoutSeconds(int connectTimeoutSeconds) {
		this.connectTimeoutSeconds = connectTimeoutSeconds;
	}

	public void setRequestTimeoutSeconds(int requestTimeoutSeconds) {
		this.requestTimeoutSeconds = requestTimeoutSeconds;
	}

	public void setMaxRetries(int maxRetries) {
		this.maxRetries = maxRetries;
	}

	@Override
	public byte[] convert(byte[] input, String fileName, ConvertContext context) {
		int attempts = 1 + Math.max(0, maxRetries);
		DocumentConversionException last = null;
		for (int i = 0; i < attempts; i++) {
			try {
				return doConvert(input, fileName);
			} catch (DocumentConversionException e) {
				last = e;
				// 4xx はリトライしない（レスポンス確定の失敗）
				if (e.getHttpStatus() >= 400 && e.getHttpStatus() < 500) {
					throw e;
				}
			}
		}
		throw last;
	}

	private byte[] doConvert(byte[] input, String fileName) {
		HttpClient current = client;
		if (current == null) {
			synchronized (this) {
				current = client;
				if (current == null) {
					current = HttpClient.newBuilder()
							.connectTimeout(Duration.ofSeconds(connectTimeoutSeconds))
							.build();
					client = current;
				}
			}
		}

		String boundary = "----iplass-" + UUID.randomUUID()
				.toString()
				.replace("-", "");
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(baseUrl + "/forms/libreoffice/convert"))
				.timeout(Duration.ofSeconds(requestTimeoutSeconds))
				.header("Content-Type", "multipart/form-data; boundary=" + boundary)
				.POST(HttpRequest.BodyPublishers.ofByteArray(multipartBody(boundary, fileName, input)))
				.build();

		HttpResponse<byte[]> response;
		try {
			response = current.send(request, HttpResponse.BodyHandlers.ofByteArray());
		} catch (IOException e) {
			throw new DocumentConversionException("Gotenberg request failed: " + e.getMessage(), -1, e);
		} catch (InterruptedException e) {
			Thread.currentThread()
					.interrupt();
			throw new DocumentConversionException("Gotenberg request interrupted", -1, e);
		}

		if (response.statusCode() != 200) {
			throw new DocumentConversionException("Gotenberg returned status " + response.statusCode()
					+ " for " + fileName, response.statusCode());
		}
		return response.body();
	}

	private static byte[] multipartBody(String boundary, String fileName, byte[] file) {
		String head = "--" + boundary + "\r\n"
				+ "Content-Disposition: form-data; name=\"files\"; filename=\"" + fileName + "\"\r\n"
				+ "Content-Type: application/octet-stream\r\n"
				+ "\r\n";
		String tail = "\r\n--" + boundary + "--\r\n";
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream(head.length() + file.length + tail.length());
			bos.write(head.getBytes(StandardCharsets.UTF_8));
			bos.write(file);
			bos.write(tail.getBytes(StandardCharsets.UTF_8));
			return bos.toByteArray();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}
