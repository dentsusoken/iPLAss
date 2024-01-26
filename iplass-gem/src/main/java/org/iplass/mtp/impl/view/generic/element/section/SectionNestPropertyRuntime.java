/*
 * Copyright (C) 2023 DENTSU SOKEN INC. All Rights Reserved.
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

/**
 * セクションネストプロパティランタイム。
 *
 * <p>
 * 本クラスでは、{@link org.iplass.mtp.impl.view.generic.element.section.SectionPropertyRuntimeImpl} で管理している情報に加え、以下の情報を管理する。
 * <ul>
 * <li>ネストプロパティランタイムマップ</li>
 * </ul>
 * </p>
 *
 * <p>package private</p>
 *
 * @author SEKIGUCHI Naoya
 */
class SectionNestPropertyRuntime extends SectionPropertyRuntimeImpl {
	/** ネストプロパティランタイムマップ */
	private Map<String, SectionPropertyRuntime> nest;

	/**
	 * コンストラクタ
	 * @param propertyId プロパティID
	 * @param nest ネストプロパティランタイムマップ
	 */
	public SectionNestPropertyRuntime(String propertyId, Map<String, SectionPropertyRuntime> nest) {
		super(propertyId);
		this.nest = nest;
	}

	/**
	 * ネストプロパティランタイムマップを取得する
	 * @return ネストプロパティランタイムマップ
	 */
	public Map<String, SectionPropertyRuntime> getNest() {
		return nest;
	}
}