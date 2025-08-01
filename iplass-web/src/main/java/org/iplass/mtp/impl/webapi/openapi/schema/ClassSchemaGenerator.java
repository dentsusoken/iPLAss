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
package org.iplass.mtp.impl.webapi.openapi.schema;

/**
 * クラスから JSON スキーマを生成するインターフェース。
 * <p>
 * POJOクラスから JSON スキーマを生成しますが、基本的な概念としては、<br>
 * プロパティおよび引数のないゲッターメソッドを JSON スキーマプロパティとして返却することを想定します。
 * </p>
 * @author SEKIGUCHI Naoya
 */
public interface ClassSchemaGenerator {
	/**
	 * クラスから JSON スキーマを生成します。
	 * @param clazz 生成対象のクラス
	 * @return JSON スキーマの文字列表現
	 */
	String generate(Class<?> clazz);
}
