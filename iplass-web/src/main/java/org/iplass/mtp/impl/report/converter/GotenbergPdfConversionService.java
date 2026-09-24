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
import java.util.regex.Pattern;

import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceConfigrationException;

/**
 * Gotenberg（Docker 上のステートレス HTTP API）でドキュメント変換を行う
 * {@link PdfConversionService} の実装。
 *
 * <p>POST {baseUrl}/forms/libreoffice/convert へ multipart/form-data でファイルを送信し、
 * 応答バイナリ（PDF 等）を返す。新規依存なし（JDK 標準 HttpClient）。</p>
 *
 */
public class GotenbergPdfConversionService implements PdfConversionService {

	/** multipart ヘッダーを破壊しうる文字（制御文字・CR/LF・ダブルクォート）およびパス区切り文字 */
	private static final Pattern ILLEGAL_FILE_NAME_CHAR = Pattern.compile("[\\p{Cntrl}\"/\\\\]");

	/** Gotenberg API のベース URL。未設定時は変換実行時に例外とする（既定値は service-config.xml 側で定義） */
	private String baseUrl;

	private int connectTimeoutSeconds = 5;

	private int requestTimeoutSeconds = 60;

	private int maxRetries = 0;

	private long retryIntervalMillis = 1000L;

	private HttpClient client;

	/**
	 * Gotenberg API のベース URL を設定する（必須）
	 * @param baseUrl ベース URL
	 */
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	/**
	 * 接続タイムアウト（秒）を設定する
	 * @param connectTimeoutSeconds 接続タイムアウト（秒）
	 */
	public void setConnectTimeoutSeconds(int connectTimeoutSeconds) {
		this.connectTimeoutSeconds = connectTimeoutSeconds;
	}

	/**
	 * リクエストタイムアウト（秒）を設定する
	 * @param requestTimeoutSeconds リクエストタイムアウト（秒）
	 */
	public void setRequestTimeoutSeconds(int requestTimeoutSeconds) {
		this.requestTimeoutSeconds = requestTimeoutSeconds;
	}

	/**
	 * 変換失敗時の最大リトライ回数を設定する
	 * @param maxRetries 最大リトライ回数
	 */
	public void setMaxRetries(int maxRetries) {
		this.maxRetries = maxRetries;
	}

	/**
	 * リトライ時の待機間隔（ミリ秒）を設定する
	 * @param retryIntervalMillis リトライ間隔（ミリ秒）
	 */
	public void setRetryIntervalMillis(long retryIntervalMillis) {
		this.retryIntervalMillis = retryIntervalMillis;
	}

	@Override
	public void init(Config config) {
		baseUrl = config.getValue("baseUrl", String.class, baseUrl);
		connectTimeoutSeconds = config.getValue("connectTimeoutSeconds", Integer.class, connectTimeoutSeconds);
		requestTimeoutSeconds = config.getValue("requestTimeoutSeconds", Integer.class, requestTimeoutSeconds);
		maxRetries = config.getValue("maxRetries", Integer.class, maxRetries);
		retryIntervalMillis = config.getValue("retryIntervalMillis", Long.class, retryIntervalMillis);

		// HttpClient はスレッドセーフかつ接続確立を伴わないため、Service 初期化時に生成して以降使い回す
		client = HttpClient.newBuilder()
				.connectTimeout(Duration.ofSeconds(connectTimeoutSeconds))
				.build();
	}

	@Override
	public void destroy() {
		if (client != null) {
			// Java 21 以降の HttpClient は AutoCloseable。実行中のリクエスト完了を待ってリソースを解放する
			client.close();
		}
	}

	@Override
	public byte[] convert(byte[] input, String fileName, ConvertContext context) {
		if (baseUrl == null || baseUrl.isBlank()) {
			throw new ServiceConfigrationException("baseUrl is not configured for " + getClass().getName()
					+ ". Set the baseUrl property of " + PdfConversionService.class.getSimpleName() + " in service-config.xml.");
		}

		validateFileName(fileName);
		int attempts = 1 + Math.max(0, maxRetries);
		DocumentConversionException last = null;
		for (int i = 0; i < attempts; i++) {
			if (i > 0 && retryIntervalMillis > 0) {
				try {
					Thread.sleep(retryIntervalMillis);
				} catch (InterruptedException e) {
					Thread.currentThread()
							.interrupt();
					throw new DocumentConversionException("Gotenberg retry wait interrupted for " + fileName, -1, e);
				}
			}
			try {
				return doConvert(input, fileName);
			} catch (DocumentConversionException e) {
				last = e;
				if (e.getCause() instanceof InterruptedException) {
					throw e;
				}
			}
		}
		throw last;
	}

	/**
	 * multipart ヘッダーへ安全に埋め込めるファイル名かを検証する。
	 *
	 * <p>不正な値を黙って補正すると利用者が誤りに気付けないため、補正は行わず例外を送出する。</p>
	 *
	 * @param fileName 入力ファイル名
	 * @throws DocumentConversionException ファイル名が未指定、またはパス区切り文字・制御文字・ダブルクォートを含む場合
	 */
	private static void validateFileName(String fileName) {
		if (fileName == null || fileName.isBlank()) {
			throw new DocumentConversionException("fileName is required for Gotenberg conversion.", -1);
		}
		if (ILLEGAL_FILE_NAME_CHAR.matcher(fileName)
				.find()) {
			// ログ・メッセージ偽装を防ぐため、不正文字を '?' に置換した値のみを出力する
			String maskedFileName = ILLEGAL_FILE_NAME_CHAR.matcher(fileName)
					.replaceAll("?");
			throw new DocumentConversionException(
					"fileName must not contain a control character, a double quote or a path separator: " + maskedFileName, -1);
		}
		if (".".equals(fileName) || "..".equals(fileName)) {
			throw new DocumentConversionException("fileName must not be \".\" or \"..\": " + fileName, -1);
		}
	}

	private byte[] doConvert(byte[] input, String fileName) {
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
			response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
		} catch (IOException e) {
			throw new DocumentConversionException("Gotenberg request failed: " + e.getMessage(), -1, e);
		} catch (InterruptedException e) {
			Thread.currentThread()
					.interrupt();
			throw new DocumentConversionException("Gotenberg request interrupted", -1, e);
		}

		int statusCode = response.statusCode();
		if (statusCode != 200) {
			throw new DocumentConversionException("Gotenberg returned status " + statusCode
					+ " for " + fileName, statusCode);
		}
		return response.body();
	}

	private static byte[] multipartBody(String boundary, String fileName, byte[] file) {
		// fileName に非 ASCII 文字が含まれる場合、文字数と UTF-8 バイト数が一致しないため
		// 初期容量の算出にはエンコード後のバイト配列長を用いる
		byte[] head = ("--" + boundary + "\r\n"
				+ "Content-Disposition: form-data; name=\"files\"; filename=\"" + fileName + "\"\r\n"
				+ "Content-Type: application/octet-stream\r\n"
				+ "\r\n").getBytes(StandardCharsets.UTF_8);
		byte[] tail = ("\r\n--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8);
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream(head.length + file.length + tail.length);
			bos.write(head);
			bos.write(file);
			bos.write(tail);
			return bos.toByteArray();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}
