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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OpenAPIサポートのツリーグリッドで選択された項目を保持するクラス。
 * @author SEKIGUCHI Naoya
 */
public class OpenApiSupportTreeGridSelected {
	/** WebAPI選択リスト */
	private List<String> webApiList;
	/** Entity CRUD API 選択リスト */
	private Map<String, List<String>> entityCRUDApiMap;


	/**
	 * WebAPI選択リストを取得します。
	 * <p>
	 * WebAPI定義名がリストに保持されています。
	 * </p>
	 * @return WebAPI選択リスト
	 */
	public List<String> getWebApiList() {
		return webApiList;
	}

	/**
	 * WebAPI選択リストにWebAPIを追加します。
	 * @param webApi WebAPI定義名
	 */
	public void addWebApi(String webApi) {
		if (null == webApiList) {
			webApiList = new ArrayList<>();
		}
		webApiList.add(webApi);
	}

	/**
	 * Entity CRUD API選択リストを取得します。
	 * <p>
	 * キーに Entity 定義名、値に権限のリストが保持されています。
	 * 権限は、{@link org.iplass.mtp.webapi.openapi.EntityWebApiType} の文字列が設定されます。
	 * </p>
	 * @return entityCRUDApiList
	 */
	public Map<String, List<String>> getEntityCRUDApiMap() {
		return entityCRUDApiMap;
	}

	/**
	 * Entity CRUD API選択リストにEntity CRUD APIを追加します。
	 * <p>
	 * 設定する権限は、{@link org.iplass.mtp.webapi.openapi.EntityWebApiType} の文字列を参照してください。
	 * </p>
	 * @param definitionName エンティティ定義名
	 * @param auth 権限
	 */
	public void addEntityCRUDApi(String definitionName, String auth) {
		if (null == entityCRUDApiMap) {
			entityCRUDApiMap = new HashMap<>();
		}
		var authList = entityCRUDApiMap.get(definitionName);
		if (null == authList) {
			authList = new ArrayList<>();
			entityCRUDApiMap.put(definitionName, authList);
		}
		authList.add(auth);
	}

	/**
	 * WebAPIが選択されていないかを判定します。
	 * @return WebAPIが選択されていない場合はtrueを返却します
	 */
	public boolean isNotSelectWebApi() {
		return null == webApiList || webApiList.isEmpty();
	}

	/**
	 * WebAPIが選択されているかを判定します。
	 * @return WebAPIが選択されている場合はtrueを返却します
	 */
	public boolean isSelectWebApi() {
		return !isNotSelectWebApi();
	}

	/**
	 * Entity CRUD APIが選択されていないかを判定します。
	 * @return Entity CRUD APIが選択されていない場合はtrueを返却します
	 */
	public boolean isNotSelectEntityCRUDApi() {
		return null == entityCRUDApiMap || entityCRUDApiMap.isEmpty();
	}

	/**
	 * Entity CRUD APIが選択されているかを判定します。
	 * @return Entity CRUD APIが選択されている場合はtrueを返却します
	 */
	public boolean isSelectEntityCRUDApi() {
		return !isNotSelectEntityCRUDApi();
	}

	/**
	 * WebAPIとEntity CRUD APIの両方が選択されていないかを判定します。
	 * @return WebAPIとEntity CRUD APIの両方が選択されていない場合はtrueを返却します
	 */
	public boolean isNotSelect() {
		return isNotSelectWebApi() && isNotSelectEntityCRUDApi();
	}
}
