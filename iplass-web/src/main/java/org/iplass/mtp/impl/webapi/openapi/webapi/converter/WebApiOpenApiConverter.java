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

/**
 * OpenAPI仕様とWebAPI定義の変換インターフェース
 * @author SEKIGUCHI Naoya
 */
public interface WebApiOpenApiConverter {
	/**
	 * WebAPI 定義の変換順序を定義するクラス
	 * <p>
	 * 左側ほど早い順序で実行されます。<br>
	 * [早] INIT &lt; FIRST &lt; SECOND &lt; DEFAULT &lt; BEFORE_LAST &lt; LAST [遅]
	 * </p>
	 */
	static final class Order {
		/** 最初に実行されます */
		static int INIT = 0;
		/** INIT の後、SECOND の前に実行されます */
		static int FIRST = 100;
		/** FIRST の後、DEFAULT の前に実行されます */
		static int SECOND = 200;
		/** SECOND の後、BEFORE_LAST の前に実行されます */
		static int DEFAULT = 1_000;
		/** DEFAULT の後、LAST の前に実行されます */
		static int BEFORE_LAST = 5_000;
		/** 最後に実行されます */
		static int LAST = 10_000;
	}

	/**
	 * 実行順序を取得します。
	 * <p>
	 * 実行順序が小さいほど先に実行されます。
	 * </p>
	 * @return 実行順序
	 */
	default int getOrder() {
		return Order.DEFAULT;
	};

	/**
	 * WebAPI 定義から OpenAPI 仕様へ変換する。
	 * @param context コンテキスト
	 */
	void convertOpenApi(WebApiOpenApiConvertContext context);

	/**
	 * OpenAPI 仕様から WebAPI 定義へ変換する。
	 * @param context コンテキスト
	 */
	void convertWebApi(WebApiOpenApiConvertContext context);
}
