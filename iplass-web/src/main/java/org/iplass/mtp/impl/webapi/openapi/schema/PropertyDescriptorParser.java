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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

/**
 * プロパティ記述子を解析するクラス
 * @author SEKIGUCHI Naoya
 */
class PropertyDescriptorParser {
	/** 解析対象クラス */
	private final Class<?> clazz;
	/** clazz の PropertyDescriptor */
	private final PropertyDescriptor[] descriptors;

	/**
	 * コンストラクタ
	 * @param clazz 解析対象クラス
	 */
	public PropertyDescriptorParser(Class<?> clazz) {
		this.clazz = clazz;
		this.descriptors = getPropertyDesctiptors(clazz);
	}

	/**
	 * プロパティ名に一致する PropertyDescriptor インスタンスを取得します
	 * @param propertyName プロパティ名
	 * @return PropertyDescriptor インスタンス
	 */
	public PropertyDescriptor getPropertyDesctiptor(String propertyName) {
		for (PropertyDescriptor descriptor : descriptors) {
			if (descriptor.getName().equals(propertyName)) {
				return descriptor;
			}
		}
		// プロパティが見つからない場合は例外をスロー
		throw new RuntimeException("Property '" + propertyName + "' not found in class " + clazz.getName());
	}

	/**
	 * クラスのプロパティ記述子を取得します。
	 * @param clazz クラス
	 * @return プロパティ記述子の配列
	 */
	private PropertyDescriptor[] getPropertyDesctiptors(Class<?> clazz) {
		if (null == clazz) {
			throw new IllegalArgumentException("Class must not be null.");
		}

		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
			return beanInfo.getPropertyDescriptors();

		} catch (IntrospectionException e) {
			throw new RuntimeException("Failed to get property descriptor for " + clazz.getName() + " class.", e);
		}
	}

}
