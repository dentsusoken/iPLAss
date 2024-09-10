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

import java.io.IOException;

import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;

import com.google.auth.oauth2.GoogleCredentials;

/**
 * GoogleCloud設定
 *
 * @author SEKIGUCHI Naoya
 */
public class GoogleCloudSettings implements Service {
	/** Google Credentials */
	private GoogleCredentials googleCredentials;

	@Override
	public void init(Config config) {
		GoogleCredentialsFactory credentialsFactory = config.getValue("credentialsFactory", GoogleCredentialsFactory.class);
		googleCredentials = credentialsFactory.create();
	}

	@Override
	public void destroy() {
		googleCredentials = null;
	}

	/**
	 * GoogleCredentials インスタンスを取得する
	 * @return GoogleCredentials インスタンス
	 */
	public GoogleCredentials getCredentials() {
		return googleCredentials;
	}

	/**
	 * アクセストークンを取得する
	 * @return アクセストークン
	 */
	public String getAccessTokenValue() {
		try {
			googleCredentials.refreshIfExpired();
			return googleCredentials.getAccessToken().getTokenValue();

		} catch (IOException e) {
			throw new RuntimeException("Failed to get access token.", e);
		}
	}
}
