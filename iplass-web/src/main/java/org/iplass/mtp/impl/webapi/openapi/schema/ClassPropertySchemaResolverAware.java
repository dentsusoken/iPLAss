/*
 * Copyright (C) 2026 DENTSU SOKEN INC. All Rights Reserved.
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
 * クラス・プロパティスキーマ解決機能を利用するクラスが実装するインターフェース
 * <p>
 * 本インターフェースは {@link org.iplass.mtp.impl.webapi.openapi.schema.OpenApiComponentReusableSchemaFactory} の実装クラスに実装することで、<br>
 * 初期化時に {@link org.iplass.mtp.impl.webapi.openapi.schema.ClassPropertySchemaResolver} を設定します。<br>
 * </p>
 * @author SEKIGUCHI Naoya
 */
public interface ClassPropertySchemaResolverAware {
	/**
	 * クラス・プロパティスキーマ解決機能を設定します。
	 * @param resolver クラス・プロパティスキーマ解決機能
	 */
	public void setClassPropertySchemaResolver(ClassPropertySchemaResolver resolver);
}
