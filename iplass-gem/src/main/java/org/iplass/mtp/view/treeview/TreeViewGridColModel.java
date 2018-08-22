/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.view.treeview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.definition.LocalizedStringDefinition;

/**
 * ツリービューグリッドのColModel
 * @author lis3wg
 *
 */
public class TreeViewGridColModel implements Serializable {

	private static final long serialVersionUID = 6385565760001843711L;

	/** 名前 */
	private String name;

	/** 表示ラベル */
	private String displayLabel;

	/** 幅 */
	private Integer width;

	/** 配置 */
	private String align;

	/** 多言語設定情報 */
	private List<LocalizedStringDefinition> localizedDisplayLabelList;

	/**
	 * 名前を取得します。
	 * @return 名前
	 */
	public String getName() {
	    return name;
	}

	/**
	 * 名前を設定します。
	 * @param name 名前
	 */
	public void setName(String name) {
	    this.name = name;
	}

	/**
	 * 表示ラベルを取得します。
	 * @return 表示ラベル
	 */
	public String getDisplayLabel() {
	    return displayLabel;
	}

	/**
	 * 表示ラベルを設定します。
	 * @param displayLabel 表示ラベル
	 */
	public void setDisplayLabel(String displayLabel) {
	    this.displayLabel = displayLabel;
	}

	/**
	 * 幅を取得します。
	 * @return 幅
	 */
	public Integer getWidth() {
	    return width;
	}

	/**
	 * 幅を設定します。
	 * @param width 幅
	 */
	public void setWidth(Integer width) {
	    this.width = width;
	}

	/**
	 * 配置を取得します。
	 * @return 配置
	 */
	public String getAlign() {
	    return align;
	}

	/**
	 * 配置を設定します。
	 * @param align 配置
	 */
	public void setAlign(String align) {
	    this.align = align;
	}

	/**
	 * 多言語設定情報を取得します。
	 * @return 多言語設定情報
	 */
	public List<LocalizedStringDefinition> getLocalizedDisplayLabelList() {
	    return localizedDisplayLabelList;
	}

	/**
	 * 多言語設定情報を設定します。
	 * @param localizedDisplayLabelList 多言語設定情報
	 */
	public void setLocalizedDisplayLabelList(List<LocalizedStringDefinition> localizedDisplayLabelList) {
	    this.localizedDisplayLabelList = localizedDisplayLabelList;
	}

	/**
	 * 多言語設定情報を追加します。
	 * @param localizedDisplayLabel 多言語設定情報
	 */
	public void addLocalizedDisplayLabel(LocalizedStringDefinition localizedDisplayLabel) {
		if (localizedDisplayLabelList == null) {
			localizedDisplayLabelList = new ArrayList<LocalizedStringDefinition>();
		}

		localizedDisplayLabelList.add(localizedDisplayLabel);
	}

}
