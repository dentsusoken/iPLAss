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

package org.iplass.gem.command;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;

/**
 * 検索コマンド間で共有する処理。
 */
public final class SearchUtil {

	private SearchUtil() {
	}

	/**
	 * プロパティパス上の参照の oid/version 列名を返す。
	 * <p>
	 * 既に {@code select} にある列は含めない。ドットが無いパスは空。
	 * 末端が参照でなければ、その末端の oid/version は含めない。
	 * 例: {@code mref2.mref3.name} → {@code mref2.oid}、{@code mref2.version}、{@code mref2.mref3.oid}、{@code mref2.mref3.version}
	 */
	public static List<String> referenceOidVersion(List<String> select, String propName,
			Function<String, PropertyDefinition> propertyDefinition) {
		if (propName == null || propName.indexOf('.') < 0) {
			return List.of();
		}
		String[] parts = propName.split("\\.");
		return IntStream.range(0, parts.length)
				.mapToObj(i -> String.join(".", Arrays.copyOf(parts, i + 1)))
				.filter(path -> propertyDefinition.apply(path) instanceof ReferenceProperty)
				.flatMap(path -> Stream.of(path + "." + Entity.OID, path + "." + Entity.VERSION))
				.filter(column -> !select.contains(column))
				.toList();
	}
}
