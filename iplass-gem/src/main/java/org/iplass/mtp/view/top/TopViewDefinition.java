/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.view.top;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.mtp.definition.Definition;
import org.iplass.mtp.view.top.parts.TopViewParts;

/**
 * TOP画面定義
 * @author lis3wg
 */
@XmlRootElement
public class TopViewDefinition implements Definition {

	/** SerialVersionUID */
	private static final long serialVersionUID = 8127325473209284041L;

	/** 定義名 */
	private String name;

	/** 表示名 */
	private String displayName;

	/** 概要 */
	private String description;

	/** TOP画面パーツ */
	@MultiLang(isMultiLangValue = false, itemKey = "parts", itemGetter = "getParts", itemSetter = "setParts")
	private List<TopViewParts> parts;

	/** ウィジェット */
	@MultiLang(isMultiLangValue = false, itemKey = "widgets", itemGetter = "getWidgets", itemSetter = "setWidgets")
	private List<TopViewParts> widgets;

	/**
	 * 定義名を取得します。
	 * @return 定義名
	 */
	public String getName() {
	    return name;
	}

	/**
	 * 定義名を設定します。
	 * @param name 定義名
	 */
	public void setName(String name) {
	    this.name = name;
	}

	/**
	 * 表示名を取得します。
	 * @return 表示名
	 */
	public String getDisplayName() {
	    return displayName;
	}

	/**
	 * 表示名を設定します。
	 * @param displayName 表示名
	 */
	public void setDisplayName(String displayName) {
	    this.displayName = displayName;
	}

	/**
	 * 概要を取得します。
	 * @return 概要
	 */
	public String getDescription() {
	    return description;
	}

	/**
	 * 概要を設定します。
	 * @param description 概要
	 */
	public void setDescription(String description) {
	    this.description = description;
	}

	/**
	 * TOP画面パーツを取得します。
	 * @return TOP画面パーツ
	 */
	public List<TopViewParts> getParts() {
		if (parts == null) parts = new ArrayList<TopViewParts>();
	    return parts;
	}

	/**
	 * TOP画面パーツを設定します。
	 * @param parts TOP画面パーツ
	 */
	public void setParts(List<TopViewParts> parts) {
	    this.parts = parts;
	}

	/**
	 * TOP画面パーツを追加します。
	 * @param parts TOP画面パーツ
	 */
	public void addParts(TopViewParts parts) {
		getParts().add(parts);
	}

	/**
	 * ウィジェットを取得します。
	 * @return ウィジェット
	 */
	public List<TopViewParts> getWidgets() {
		if (widgets == null) widgets = new ArrayList<TopViewParts>();
	    return widgets;
	}

	/**
	 * ウィジェットを設定します。
	 * @param widgets ウィジェット
	 */
	public void setWidgets(List<TopViewParts> widgets) {
	    this.widgets = widgets;
	}

	/**
	 * ウィジェットを追加します。
	 * @param widget ウィジェット
	 */
	public void addWidget(TopViewParts widget) {
		getWidgets().add(widget);
	}
}
