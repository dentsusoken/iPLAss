/*
 * Copyright (C) 2015 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.tools.lang;

import java.io.Serializable;

import org.iplass.mtp.definition.Definition;

public class LangDataPortingInfo implements Serializable {

	private static final long serialVersionUID = -8189255892737774121L;

	/** 定義情報のコンテキストコンテキストパス */
	private String contextPath;

	/** 定義情報の名前 */
	private String name;

	/** 定義情報 */
	private Definition definition;

	/**
	 * 定義情報のコンテキストコンテキストパスを取得します。
	 * @return 定義情報のコンテキストコンテキストパス
	 */
	public String getContextPath() {
	    return contextPath;
	}

	/**
	 * 定義情報のコンテキストコンテキストパスを設定します。
	 * @param contextPath 定義情報のコンテキストコンテキストパス
	 */
	public void setContextPath(String contextPath) {
	    this.contextPath = contextPath;
	}

	/**
	 * 定義情報の名前を取得します。
	 * @return 定義情報の名前
	 */
	public String getName() {
	    return name;
	}

	/**
	 * 定義情報の名前を設定します。
	 * @param name 定義情報の名前
	 */
	public void setName(String name) {
	    this.name = name;
	}

	/**
	 * 定義情報を取得します。
	 * @return 定義情報
	 */
	public Definition getDefinition() {
	    return definition;
	}

	/**
	 * 定義情報を設定します。
	 * @param definition 定義情報
	 */
	public void setDefinition(Definition definition) {
	    this.definition = definition;
	}
}
