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
package org.iplass.mtp.util;

import java.util.function.IntFunction;

import org.apache.commons.lang3.ArrayUtils;

/**
 * 配列ユーティリティ
 * <p>
 * このクラスは、配列操作のユーティリティメソッドを提供します。
 * Commons Lang - ArrayUtils へのThinWapper。機能が存在しない場合は追加します。
 * </p>
 * @author SEKIGUCHI Naoya
 */
public class ArrayUtil {
	/**
	 * 配列に値を追加して新しい配列を返します。
	 *
	 * @param <T> 配列の要素の型
	 * @param arr 追加元の配列
	 * @param value 追加する値
	 * @param generator 要素の型の配列コンストラクタ
	 * @return 値が追加された配列
	 */
	public static <T> T[] add(T[] arr, T value, IntFunction<T[]> generator) {
		if (arr == null) {
			var newArr = generator.apply(1);
			newArr[0] = value;
			return newArr;
		}

		var newArr = generator.apply(arr.length + 1);
		System.arraycopy(arr, 0, newArr, 0, arr.length);
		newArr[arr.length] = value;
		return newArr;
	}

	/**
	 * ２つの配列を結合して新しい配列を返却します。
	 * <pre>
	 * - ArrayUtil.addAll([1,2,3], [4,5,6], Integer[]::new) = (Integer[])[1,2,3,4,5,6]
	 * - ArrayUtil.addAll([1,2,3], []     , Integer[]::new) = (Integer[])[1,2,3]
	 * - ArrayUtil.addAll([1,2,3], null   , Integer[]::new) = (Integer[])[1,2,3]
	 * - ArrayUtil.addAll([]     , [4,5,6], Integer[]::new) = (Integer[])[4,5,6]
	 * - ArrayUtil.addAll(null   , [4,5,6], Integer[]::new) = (Integer[])[4,5,6]
	 * - ArrayUtil.addAll([]     , []     , Integer[]::new) = (Integer[])[]
	 * - ArrayUtil.addAll(null   , null   , Integer[]::new) = (Integer[])[]
	 * </pre>
	 * @param <T> 配列の要素の型
	 * @param arr1 配列１
	 * @param arr2 配列２
	 * @param generator 要素の型の配列コンストラクタ
	 * @return 結合された新しい配列
	 */
	public static <T> T[] addAll(T[] arr1, T[] arr2, IntFunction<T[]> generator) {
		if (null == arr1 && null == arr2) {
			return generator.apply(0);
		}

		if (null == arr1) {
			// arr1がnullの場合、arr2をそのまま返す
			var newArr = generator.apply(arr2.length);
			System.arraycopy(arr2, 0, newArr, 0, arr2.length);
			return newArr;
		}

		if (null == arr2) {
			// arr2がnullの場合、arr1をそのまま返す
			var newArr = generator.apply(arr1.length);
			System.arraycopy(arr1, 0, newArr, 0, arr1.length);
			return newArr;
		}

		var newArr = generator.apply(arr1.length + arr2.length);

		System.arraycopy(arr1, 0, newArr, 0, arr1.length);
		System.arraycopy(arr2, 0, newArr, arr1.length, arr2.length);

		return newArr;
	}

	/**
	 * 配列が null もしくは空かチェックします
	 * @param array 配列
	 * @return 配列が null もしくは空の場合は true、そうでない場合は false
	 */
	public static boolean isEmpty(Object[] array) {
		return ArrayUtils.isEmpty(array);
	}

	/**
	 * 配列に要素が存在するかチェックします
	 * @param array 配列
	 * @return 配列に要素が存在する場合は true、そうでない場合は false
	 */
	public static boolean isNotEmpty(Object[] array) {
		return ArrayUtils.isNotEmpty(array);
	}

	/**
	 * 配列に値が含まれているかどうかをチェックします。
	 * @param array 配列
	 * @param value 検査値
	 * @return 値が配列に含まれている場合はtrue、そうでない場合はfalse
	 */
	public static boolean contains(Object[] array, Object value) {
		return ArrayUtils.contains(array, value);
	}
}
