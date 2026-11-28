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

package org.iplass.mtp.view.generic;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

/**
 * 編集画面用のFormレイアウト情報
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DetailFormView extends AbstractDetailFormView {

	/** シリアルバージョンID */
	private static final long serialVersionUID = -5906085368820747139L;

	/** コピー対象 */
	@XmlType(namespace = "http://mtp.iplass.org/xml/definition/view/generic")
	public enum CopyTarget {
		/** 当該エンティティのみコピー */
		@XmlEnumValue("Shallow")
		SHALLOW("Shallow"),
		/** 包含する（親子関係の）エンティティも一括にコピー */
		@XmlEnumValue("Deep")
		DEEP("Deep"),
		/** コピー時にShallowかDeepを選択 */
		@XmlEnumValue("Both")
		BOTH("Both"),
		/** 独自実装したコピー処理を実行 */
		@XmlEnumValue("Custom")
		CUSTOM("Custom");

		private final String value;

		private CopyTarget(final String value) {
			this.value = value;
		}

		public String value() {
			return this.value;
		}

		public static CopyTarget getEnum(String value) {
			for (CopyTarget v : values()) {
				if (v.value()
						.equals(value)) {
					return v;
				}
			}
			throw new IllegalArgumentException("no such CopyTarget for the value: " + value);
		}
	}

}
