/*
 * Copyright (C) 2020 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.view.top.parts;

import jakarta.xml.bind.annotation.XmlSeeAlso;

/**
 * 画面表示パーツ
 * @author li3369
 */
@XmlSeeAlso({ActionParts.class, TemplateParts.class, SeparatorParts.class})
public abstract class TopViewContentParts extends TopViewParts {

	/** SerialVersionUID */
	private static final long serialVersionUID = 1367215337993838879L;
	
	/** スタイルシートのクラス名 */
	private String style;

	/**
	 * スタイルシートのクラス名を取得します。
	 * @return スタイルシートのクラス名
	 */
	public String getStyle() {
		return style;
	}

	/**
	 * スタイルシートのクラス名を設定します。
	 * @param style スタイルシートのクラス名
	 */
	public void setStyle(String style) {
		this.style = style;
	}
}
