/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.webapi.openapi;

/**
 * OpenAPI バージョン列挙値
 * @author SEKIGUCHI Naoya
 */
public enum OpenApiVersion {
	/** version 3.0*/
	V30("3.0", "3.0.4"),
	/** version 3.1 */
	V31("3.1", "3.1.1");

	/** シリーズバージョン */
	private String seriesVersion;
	/** デフォルトバージョン */
	private String defaultVersion;

	/**
	 * コンストラクタs
	 * @param seriesVersion シリーズバージョン
	 * @param defaultVersion デフォルトバージョン
	 */
	private OpenApiVersion(String seriesVersion, String defaultVersion) {
		this.seriesVersion = seriesVersion;
		this.defaultVersion = defaultVersion;
	}

	/**
	 * シリーズバージョンを取得します。
	 * @return シリーズバージョン
	 */
	public String getSeriesVersion() {
		return seriesVersion;
	}

	/**
	 * デフォルトバージョンを取得します。
	 * <p>
	 * デフォルトバージョンは、OpenAPI仕様の openapi に設定するデフォルトのバージョンを表します。
	 * </p>
	 * @return デフォルトバージョン
	 */
	public String getDefaultVersion() {
		return defaultVersion;
	}

	/**
	 * シリーズバージョンから OpenApiVersion を取得します。
	 * @param seriesVersion シリーズバージョン文字列
	 * @return 対応する OpenApiVersion
	 */
	public static OpenApiVersion fromSeriesVersion(String seriesVersion) {
		for (OpenApiVersion version : OpenApiVersion.values()) {
			if (version.getSeriesVersion().equalsIgnoreCase(seriesVersion)) {
				return version;
			}
		}
		throw new IllegalArgumentException("Unknown series version: " + seriesVersion);
	}
}
