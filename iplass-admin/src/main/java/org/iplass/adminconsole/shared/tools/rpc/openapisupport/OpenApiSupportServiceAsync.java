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
package org.iplass.adminconsole.shared.tools.rpc.openapisupport;

import org.iplass.adminconsole.shared.tools.dto.openapisupport.OpenApiSupportTreeGridEntry;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * OpenAPI(Swagger)Support 用の RPC サービス
 * <p>
 * クライアント用のインターフェースです。
 * </p>
 * @author SEKIGUCHI Naoya
 */
public interface OpenApiSupportServiceAsync {
	/**
	 * Web API 用の TreeGrid のエントリを取得します。
	 * @param tenantId テナントID
	 * @param callback Web API 用の TreeGrid のエントリを結果とするコールバック処理
	 */
	void getWebApiTreeGridEntry(int tenantId, AsyncCallback<OpenApiSupportTreeGridEntry> callback);

	/**
	 * Entity CRUD API 用の TreeGrid のエントリを取得します。
	 * @param tenantId テナントID
	 * @param callback Entity CRUD API 用の TreeGrid のエントリを結果とするコールバック処理
	 */
	void getEntityCrudApiTreeGridEntry(int tenantId, AsyncCallback<OpenApiSupportTreeGridEntry> callback);
}
