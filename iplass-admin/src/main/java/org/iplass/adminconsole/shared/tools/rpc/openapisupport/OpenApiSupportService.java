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

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.client.rpc.XsrfProtectedService;

/**
 * OpenAPI(Swagger)Support 用の RPC サービス
 * <p>
 * サーブレット処理用のインターフェースです。
 * </p>
 * @author SEKIGUCHI Naoya
 */
@RemoteServiceRelativePath(OpenApiSupportRpcConstant.Service.SERVICE_NAME)
public interface OpenApiSupportService extends XsrfProtectedService {
	/**
	 * Web API 用の TreeGrid のエントリを取得します。
	 * <p>
	 * WebAPI 定義から TreeGrid のエントリを生成します。
	 * </p>
	 * @param tenantId テナントID
	 * @return Web API 用の TreeGrid のエントリ
	 */
	OpenApiSupportTreeGridEntry getWebApiTreeGridEntry(int tenantId);

	/**
	 * Entity CRUD API 用の TreeGrid のエントリを取得します。
	 * <p>
	 * EntityWebApiDefinition に基づいて Entity CRUD API の定義を取得し、TreeGrid エントリを作成します。
	 * </p>
	 * @param tenantId テナントID
	 * @return Entity CRUD API 用の TreeGrid のエントリ
	 */
	OpenApiSupportTreeGridEntry getEntityCrudApiTreeGridEntry(int tenantId);
}
