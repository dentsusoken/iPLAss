/*
 * Copyright (C) 2023 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.view.generic.element.section;

import java.util.Map;
import java.util.Optional;

import org.iplass.mtp.impl.view.generic.editor.MetaPropertyEditor.PropertyEditorRuntime;

/**
 * SectionRuntime でプロパティランタイムを管理している場合に付与するインターフェース
 *
 * <p>
 * 本インターフェースは、{@ org.iplass.mtp.impl.view.generic.element.section.SectionRuntime} 実装クラスに実装することを想定している。
 * 本インターフェースを実装することで、セクション管理しているプロパティランタイムのヘルパー機能をデフォルト実装する。
 * {@link #getSectionPropertyRuntimeMap()} で取得するプロパティランタイムは、ネストプロパティを意識した形で保持している必要がある。
 * </p>
 *
 * @author SEKIGUCHI Naoya
 */
public interface HasSectionPropertyRuntimeMap {
	/**
	 * セクションで保持しているプロパティランタイム情報を取得する。
	 *
	 * <p>
	 * 返却する値のキーは、プロパティ名称とする。
	 * </p>
	 *
	 * @return セクションで保持しているプロパティランタイム情報
	 */
	Map<String, SectionPropertyRuntime> getSectionPropertyRuntimeMap();

	/**
	 * プロパティ名からセクションで保持しているプロパティランタイムを取得する。
	 *
	 * <p>
	 * 引数のプロパティ名について、ネストプロパティの場合は <code>parent[0].nest</code> といった形で指定されることを想定している。
	 * </p>
	 *
	 * @param propertyName プロパティ名
	 * @return プロパティランタイム
	 */
	default SectionPropertyRuntime getSectionPropertyRuntime(String propertyName) {
		boolean isNestProperty = propertyName.contains(".");
		String parentPropertyKey = isNestProperty ? propertyName.substring(0, propertyName.indexOf('[')) : propertyName;
		String nestPropertyKey = isNestProperty ? propertyName.substring(propertyName.indexOf('.') + 1) : null;

		SectionPropertyRuntime parent = getSectionPropertyRuntimeMap().get(parentPropertyKey);

		return isNestProperty && null != parent && parent instanceof SectionNestPropertyRuntime
				// ネストプロパティの場合は、取得したプロパティのネストランタイムから取得し、返却する
				? ((SectionNestPropertyRuntime) parent).getNest().get(nestPropertyKey)
				// 通常プロパティの場合は、先に取得したプロパティランタイムを返却する
				: parent;
	}

	/**
	 * プロパティエディタランタイムを取得する
	 * @param propertyName 取得するプロパティ名
	 * @return プロパティエディタランタイム
	 */
	default PropertyEditorRuntime getPropertyEditorRuntime(String propertyName) {
		return getPropertyEditorRuntime(propertyName, PropertyEditorRuntime.class);
	}

	/**
	 * プロパティエディタランタイムを取得する
	 * @param <T> プロパティエディタランタイムタイプ
	 * @param propertyName 取得するプロパティ名
	 * @param clazz プロパティエディタランタイムデータ型
	 * @return プロパティエディタランタイム
	 */
	@SuppressWarnings("unchecked")
	default <T extends PropertyEditorRuntime> T getPropertyEditorRuntime(String propertyName, Class<T> clazz) {
		SectionPropertyRuntime property = getSectionPropertyRuntime(propertyName);

		return (T) Optional.ofNullable(property).map(v -> v.getEditor()).filter(v -> clazz.isAssignableFrom(v.getClass()))
				.orElse(null);
	}
}
