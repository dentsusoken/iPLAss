/*
 * Copyright (C) 2020 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.view.top.parts;

import javax.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.view.top.parts.ActionParts;
import org.iplass.mtp.view.top.parts.CalendarParts;
import org.iplass.mtp.view.top.parts.EntityListParts;
import org.iplass.mtp.view.top.parts.LastLoginParts;
import org.iplass.mtp.view.top.parts.SeparatorParts;
import org.iplass.mtp.view.top.parts.TopViewContentParts;
import org.iplass.mtp.view.top.parts.TopViewParts;
import org.iplass.mtp.view.top.parts.TreeViewParts;

/**
 * 画面表示パーツ
 * @author li3369
 */
@XmlSeeAlso({MetaActionParts.class, MetaEntityListParts.class, MetaCalendarParts.class, 
	MetaLastLoginParts.class, MetaTreeViewParts.class, MetaSeparatorParts.class})
public abstract class MetaTopViewContentParts extends MetaTopViewParts{

	/** SerialVersionUID */
	private static final long serialVersionUID = 7929105536750599630L;
	
	/**
	 * インスタンスを生成します。
	 * @param parts TOP画面パーツ
	 * @return インスタンス
	 */
	public static MetaTopViewContentParts createInstance(TopViewParts parts) {
		if (parts instanceof ActionParts) {
			return MetaActionParts.createInstance(parts);
		} else if (parts instanceof EntityListParts) {
			return MetaEntityListParts.createInstance(parts);
		} else if (parts instanceof CalendarParts) {
			return MetaCalendarParts.createInstance(parts);
		} else if (parts instanceof LastLoginParts) {
			return MetaLastLoginParts.createInstance(parts);
		} else if (parts instanceof TreeViewParts) {
			return MetaTreeViewParts.createInstance(parts);
		} else if (parts instanceof SeparatorParts) {
			return MetaSeparatorParts.createInstance(parts);
		}
		return null;
	}
	
	/** スタイルシートのクラス名 */
	protected String style;

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
	
	@Override
	protected void fillFrom(TopViewParts parts) {
		TopViewContentParts contentParts = (TopViewContentParts) parts;
		style = contentParts.getStyle();
	}
	
	@Override
	protected void fillTo(TopViewParts parts) {
		TopViewContentParts contentParts = (TopViewContentParts) parts;
		contentParts.setStyle(style);
	}
	
}
