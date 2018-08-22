/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.util;

import java.util.Collection;
import java.util.Map;

public class CollectionUtil {
	//FIXME CollectionUtilsのメソッドを呼ぶ。位置づけ的にCollectionUtilsのThinWrapperとして。
	

	/**
	 * Collectionが空かを判定します。
	 * nullの場合もtrueを返します。
	 *
	 * @param collection 対象Collection
	 * @return true 空（またはnull）
	 */
	public static boolean isEmpty(Collection<?> collection) {
		return (collection == null || collection.isEmpty());
	}

	/**
	 * Collectionが空ではないかを判定します。
	 *
	 * @param collection 対象Collection
	 * @return true 空（またはnull）ではない
	 */
	public static boolean isNotEmpty(Collection<?> collection) {
		return !(isEmpty(collection));
	}

	/**
	 * Collectionが空かを判定します。
	 * nullの場合もtrueを返します。
	 *
	 * @param collection 対象Collection
	 * @return true 空（またはnull）
	 */
	public static boolean isEmpty(Map<?, ?> collection) {
		return (collection == null || collection.isEmpty());
	}

	/**
	 * Collectionが空ではないかを判定します。
	 *
	 * @param collection 対象Collection
	 * @return true 空（またはnull）ではない
	 */
	public static boolean isNotEmpty(Map<?, ?> collection) {
		return !(isEmpty(collection));
	}
}
