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
package org.iplass.mtp.impl.webapi.openapi.webapi.converter;

import java.util.Set;

import org.iplass.mtp.util.ArrayUtil;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.RequestType;

/**
 * OpenAPI仕様とWebAPI定義の変換を Operation 単位に行う抽象クラス（RequestType 専用の実装）
 * <p>
 * WebAPI定義の RequestType に応じて、処理を行います。
 * </p>
 * @author SEKIGUCHI Naoya
 */
public abstract class AbstractWebApiRestTypeOpenApiOperationConverter extends AbstractWebApiOpenApiOperationConverter {
	/** RequestBody をサポートしている HTTP メソッド */
	private static final Set<MethodType> BODY_SUPPORT_METHOD = Set.of(MethodType.POST, MethodType.PUT, MethodType.PATCH);

	@Override
	protected boolean isMapOpenApiOperationValue(WebApiOpenApiConvertContext context) {
		return containsTargetRequestType(context.getWebApiDefinition().getAccepts(), getTargetRequestType());
	}

	/**
	 * 本クラスが処理対象とするリクエストタイプを取得します。
	 * @return リクエストタイプ
	 */
	protected abstract RequestType getTargetRequestType();

	/**
	 * RequestBody がサポートされている HTTP メソッドかどうかを判定します。
	 * @param method HTTP メソッド
	 * @return RequestBody がサポートされている場合は true、それ以外は false
	 */
	protected boolean isBodySupportMethod(MethodType method) {
		return getBodySupportMethod().contains(method);
	}

	/**
	 * RequestBody がサポートされている HTTP メソッドのセットを取得します。
	 * @return HTTPメソッドのセット
	 */
	protected Set<MethodType> getBodySupportMethod() {
		return BODY_SUPPORT_METHOD;
	}

	/**
	 * 指定されたリクエストタイプが配列中に含まれているか判断します。
	 * <p>
	 * 配列は WebAPI 定義の accepts を想定しています。
	 * accepts は null もしくは空の場合、全てのリクエストタイプを含むと想定します。
	 * </p>
	 * @param accepts WebAPI定義の accepts
	 * @param checkType チェック対象のリクエストタイプ
	 * @return accepts に checkType が含まれている場合は true、それ以外は false
	 */
	private boolean containsTargetRequestType(RequestType[] accepts, RequestType checkType) {
		return ArrayUtil.isEmpty(accepts) || ArrayUtil.contains(accepts, checkType);
	}

}
