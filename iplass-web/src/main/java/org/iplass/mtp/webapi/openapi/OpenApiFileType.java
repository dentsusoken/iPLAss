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

import java.util.ArrayList;
import java.util.List;

/**
 * OpenAPI ファイルタイプ列挙値
 * <p>
 * OpenAPI 仕様で利用可能なファイルタイプを表します
 * </p>
 * @author SEKIGUCHI Naoya
 */
public enum OpenApiFileType {
	/** JSON */
	JSON("JSON", List.of("json")),
	/** YAML */
	YAML("YAML", List.of("yaml", "yml"));

	/** 表示名 */
	private String displayName;
	/** 拡張子リスト */
	private List<String> extensionList;

	/**
	 * コンストラクタ
	 * @param displayName 表示名
	 * @param extensionList 拡張子リスト
	 */
	private OpenApiFileType(String displayName, List<String> extensionList) {
		this.displayName = displayName;
		this.extensionList = extensionList;
	}

	/**
	 * 表示名を取得します。
	 * @return 表示名
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * 拡張子リストを取得します。
	 * @return 拡張子リスト
	 */
	public List<String> getExtensionList() {
		return extensionList;
	}

	/**
	 * 拡張子を取得します。
	 * <p>
	 * 先頭の拡張子をデフォルトの拡張子として取得します。
	 * </p>
	 * @return 拡張子
	 */
	public String getExtension() {
		return extensionList.get(0);
	}

	/**
	 * 表示名から、対応する OpenApiFileType を取得します。
	 * @param displayName 表示名
	 * @return 対応する OpenApiFileType
	 */
	public static OpenApiFileType fromDisplayName(String displayName) {
		for (OpenApiFileType type : OpenApiFileType.values()) {
			if (type.getDisplayName().equalsIgnoreCase(displayName)) {
				return type;
			}
		}
		throw new IllegalArgumentException("Unknown OpenApiFileType: " + displayName);
	}

	/**
	 * ファイル名の拡張子から、対応する OpenApiFileType を取得します。
	 * @param fileName ファイル名
	 * @return 対応する OpenApiFileType
	 */
	public static OpenApiFileType fromExtension(String fileName) {
		String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
		for (OpenApiFileType type : OpenApiFileType.values()) {
			if (type.getExtensionList().contains(extension)) {
				return type;
			}
		}
		throw new IllegalArgumentException("Unknown OpenApiFileType for extension: " + extension);
	}

	/**
	 * 全ての拡張子のリストを取得します。
	 * @return 全ての拡張子のリスト
	 */
	public static String[] getAllExtensions() {
		List<String> allExtensionList = new ArrayList<>();
		for (OpenApiFileType type : OpenApiFileType.values()) {
			allExtensionList.addAll(type.extensionList);
		}
		return allExtensionList.toArray(new String[allExtensionList.size()]);
	}
}
