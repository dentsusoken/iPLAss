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
package org.iplass.mtp.impl.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * クラスユーティリティ
 *
 * @author SEKIGUCHI Naoya
 */
public final class ClassUtil {
	/**
	 * プライベートコンストラクタ
	 */
	private ClassUtil() {
	}

	/**
	 * 文字列からクラスを取得する
	 * @param <T> クラス型
	 * @param className クラス名
	 * @return クラス
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> forName(String className) {
		if (null == className || className.isEmpty()) {
			throw new IllegalArgumentException("class name is empty.");
		}

		try {
			return (Class<T>) Class.forName(className);

		} catch (ClassNotFoundException e) {
			// クラスが見つからない
			throw new RuntimeException("Class " + className + " not found", e);
		}
	}

	/**
	 * 文字列からクラスコンストラクタを取得する
	 * @param <T> クラス型
	 * @param className クラス名
	 * @param parameterTypeArray コンストラクタパラメータ
	 * @return コンストラクタ
	 */
	public static <T> Constructor<T> getConstructor(String className, Class<?>... parameterTypeArray) {
		Class<T> clazz = forName(className);
		return getConstructor(clazz, parameterTypeArray);
	}

	/**
	 * クラスコンストラクタを取得する
	 * @param <T> クラス型
	 * @param clazz クラス
	 * @param parameterTypeArray コンストラクタパラメータ
	 * @return コンストラクタ
	 */
	public static <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>... parameterTypeArray) {
		try {
			return clazz.getDeclaredConstructor(parameterTypeArray);

		} catch (NoSuchMethodException | SecurityException e) {
			// コンストラクタ存在しない
			var parameterArrayString = String.join(",", List.of(parameterTypeArray).stream().map(c -> c != null ? c.getName() : "null").toList());
			throw new RuntimeException("Constructor does not exist. class = " + clazz.getName() + ", parameter = " + parameterArrayString, e);

		}
	}

	/**
	 * クラス名からインスタンスを生成する
	 * <p>
	 * パラメータのないデフォルトコンストラクタを利用したインスタンス生成を行う。
	 * </p>
	 * @param <T> クラス型
	 * @param className クラス名
	 * @return インスタンス
	 */
	public static <T> T newInstance(String className) {
		Class<T> clazz = forName(className);
		return newInstance(clazz);
	}

	/**
	 * インスタンスを生成する
	 * <p>
	 * パラメータのないデフォルトコンストラクタを利用したインスタンス生成を行う。
	 * </p>
	 * @param <T> クラス型
	 * @param clazz クラス
	 * @return インスタンス
	 */
	public static <T> T newInstance(Class<T> clazz) {
		if (clazz == null) {
			throw new IllegalArgumentException("class is null.");
		}

		Constructor<T> defaultCtor = getConstructor(clazz);
		return newInstance(defaultCtor);
	}

	/**
	 * インスタンスを生成する
	 * @param <T> クラス型
	 * @param ctor コンストラクタ
	 * @param args コンストラクタ引数
	 * @return インスタンス
	 */
	public static <T> T newInstance(Constructor<T> ctor, Object... args) {
		if (ctor == null) {
			throw new IllegalArgumentException("constructor is null.");
		}

		try {
			return ctor.newInstance(args);

		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// インスタンス生成失敗
			throw new RuntimeException("Failed to create instance.", e);
		}
	}
}
