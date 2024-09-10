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
package org.iplass.mtp.impl.googlecloud;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;

import org.iplass.mtp.spi.ServiceConfigrationException;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.auth.oauth2.GoogleCredentials;

/**
 * サービスアカウント用秘密鍵ファイルを利用した Google資格情報
 *
 * <p>
 * Google以外の環境を利用して、Google サービスを利用する場合はサービスアカウント用の秘密鍵ファイルを生成し設定する必要があります。
 * サービスアカウント用の秘密鍵ファイルを生成する方法は以下を参照。<br>
 * https://firebase.google.com/docs/admin/setup#initialize_the_sdk_in_non-google_environments
 * </p>
 *
 * <p>
 * Google環境を利用する場合は、秘密鍵ファイル以外の方法を検討してください。<br>
 * https://cloud.google.com/iam/docs/best-practices-for-managing-service-account-keys
 * </p>
 *
 * <p>
 * 設定可能なスコープは<a href="https://developers.google.com/identity/protocols/oauth2/scopes">Google API の OAuth 2.0 スコープ</a>を確認してください。
 * </p>
 *
 * @author SEKIGUCHI Naoya
 */
public class ServiceAccountSecretKeyGoogleCredentialsFactory implements GoogleCredentialsFactory {
	/** サービスアカウント用の秘密鍵ファイル */
	private String serviceAccountSecretKeyFilePath;
	/** Credentials に設定する scope */
	private String[] scope;
	/** プロキシホスト */
	private String proxyHost;
	/** プロキシポート */
	private Integer proxyPort;

	/**
	 * サービスアカウント用の秘密鍵ファイルを取得する
	 * @return serviceAccountSecretKeyFilePath サービスアカウント用の秘密鍵ファイル
	 */
	public String getServiceAccountSecretKeyFilePath() {
		return serviceAccountSecretKeyFilePath;
	}

	/**
	 * サービスアカウント用の秘密鍵ファイルを設定する
	 * @param serviceAccountSecretKeyFilePath サービスアカウント用の秘密鍵ファイル
	 */
	public void setServiceAccountSecretKeyFilePath(String serviceAccountSecretKeyFilePath) {
		this.serviceAccountSecretKeyFilePath = serviceAccountSecretKeyFilePath;
	}

	/**
	 * GoogleCredentials に設定するスコープを取得する
	 * @return スコープ
	 */
	public String[] getScope() {
		return scope;
	}

	/**
	/**
	 * GoogleCredentials に設定するスコープを設定する
	 * @param scope スコープ
	 */
	public void setScope(String[] scope) {
		this.scope = scope;
	}

	/**
	 * プロキシホストを取得する
	 * @return proxyHost プロキシホスト
	 */
	public String getProxyHost() {
		return proxyHost;
	}

	/**
	 * プロキシホストを設定する
	 * @param proxyHost プロキシホスト
	 */
	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	/**
	 * プロキシポートを取得する
	 * @return proxyPort プロキシポート
	 */
	public int getProxyPort() {
		return proxyPort;
	}

	/**
	 * プロキシポートを設定する
	 * @param proxyPort プロキシポート
	 */
	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}

	@Override
	public GoogleCredentials create() {
		try (InputStream sercretKeyFile = getFileOrResource(serviceAccountSecretKeyFilePath)) {

			GoogleCredentials googleCredentials = null == proxyHost
					// プロキシ無し
					? GoogleCredentials.fromStream(sercretKeyFile)
					// プロキシ設定有り
					: GoogleCredentials.fromStream(sercretKeyFile, () -> {
						Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
						return new NetHttpTransport.Builder().setProxy(proxy).build();
					});

			// スコープを設定
			return null == scope ? googleCredentials : googleCredentials.createScoped(scope);

		} catch (FileNotFoundException e) {
			// サービスアカウント秘密鍵ファイルが見つからない
			throw new ServiceConfigrationException("Service account secret key file not found. Please fix the service-config settings.", e);

		} catch (IOException e) {
			// Google資格情報の生成に失敗
			throw new ServiceConfigrationException("Failed to generate Google credentials.", e);
		}
	}

	private InputStream getFileOrResource(String path) throws FileNotFoundException {
		InputStream resource = ServiceAccountSecretKeyGoogleCredentialsFactory.class.getResourceAsStream(path);
		if (null != resource) {
			return resource;
		}

		return new BufferedInputStream(new FileInputStream(path));
	}

}
