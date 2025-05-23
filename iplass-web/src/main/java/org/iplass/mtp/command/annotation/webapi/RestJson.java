/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.command.annotation.webapi;

/**
 * REST JSON パラメータを指定します。
 */
public @interface RestJson {
	/**
	 * パラメータ名を設定します。
	 * @return パラメータ名
	 */
	String parameterName();

	/**
	 * パラメータデータ型クラスを指定します。
	 * @return パラメータデータ型クラス
	 */
	Class<?> parameterType() default void.class;

	/**
	 * REST JSON として受け付ける Content-Type を指定します。
	 * @return Content-Type 配列
	 */
	String[] acceptableContentTypes() default {};
}
