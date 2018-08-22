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

package org.iplass.mtp.definition;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * 定義がもつ共通のインタフェースを定義。
 *
 * @author K.Higuchi
 *
 */
@XmlRootElement
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS)
public interface Definition extends Serializable {

	/**
	 * 名前を取得します。
	 *
	 * @return 名前
	 */
	public String getName();

	/**
	 * 名前を設定します。
	 *
	 * @param name 名前
	 */
	public void setName(String name);

	/**
	 * 表示名を取得します。
	 *
	 * @return 表示名
	 */
	public String getDisplayName();

	/**
	 * 表示名を設定します。
	 *
	 * @param displayName 表示名
	 */
	public void setDisplayName(String displayName);

	/**
	 * 概要を取得します。
	 *
	 * @return 概要
	 */
	public String getDescription();

	/**
	 * 概要を設定します。
	 *
	 * @param description 概要
	 */
	public void setDescription(String description);
}
