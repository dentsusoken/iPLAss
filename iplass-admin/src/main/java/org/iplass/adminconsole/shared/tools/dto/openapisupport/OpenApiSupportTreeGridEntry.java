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
package org.iplass.adminconsole.shared.tools.dto.openapisupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * OpenAPI サポート TreeGrid 入力情報
 * <p>
 * OpenAPI サポートの TreeGrid に表示する情報を保持するクラスです。
 * このクラスは、表示名、メタデータ定義パス、フォルダ判定、および子要素のリストを含みます。
 * メタデータ定義パスは、ルート要素により決定されます。
 * </p>
 * @author SEKIGUCHI Naoya
 */
public class OpenApiSupportTreeGridEntry implements Serializable {
	/** serialVersionUID */
	private static final long serialVersionUID = -4085072960087170292L;
	/** ツリー表示名 */
	private String name;
	/** メタデータ定義パス */
	private String path;
	/** フォルダ判定 */
	private boolean isFolder;
	/** 子要素 */
	private List<OpenApiSupportTreeGridEntry> children = new ArrayList<>();

	/**
	 * ツリー表示名を取得します
	 * @return ツリー表示名
	 */
	public String getName() {
		return name;
	}

	/**
	 * ツリー表示名を設定します
	 * @param name ツリー表示名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * メタデータ定義パスを取得します
	 * @return メタデータ定義パス
	 */
	public String getPath() {
		return path;
	}

	/**
	 * メタデータ定義パスを設定します
	 * @param path メタデータ定義パス
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * フォルダ判定値を取得します
	 * @return フォルダの場合は true を返却します
	 */
	public boolean isFolder() {
		return isFolder;
	}

	/**
	 * フォルダ判定値を設定します
	 * @param isFolder フォルダの場合は true を設定します
	 */
	public void setFolder(boolean isFolder) {
		this.isFolder = isFolder;
	}

	/**
	 * 子要素を追加します
	 * <p>
	 * ツリーの子要素を追加します。
	 * </p>
	 * @param child 子要素
	 */
	public void addChild(OpenApiSupportTreeGridEntry child) {
		children.add(child);
	}

	/**
	 * 指定された名前の子フォルダを取得します
	 * @param name フォルダ名
	 * @return 指定された名前の子フォルダが存在する場合はそのフォルダ、存在しない場合は null を返します
	 */
	public OpenApiSupportTreeGridEntry getChildFolder(String name) {
		for (var child : children) {
			if (child.getName().equals(name) && child.isFolder()) {
				return child;
			}
		}
		// 存在しない場合は null を返却
		return null;
	}

	/**
	 * 子要素のリストを取得します
	 * @return children 子要素のリスト
	 */
	public List<OpenApiSupportTreeGridEntry> getChildren() {
		return new ArrayList<>(children);
	}
}
