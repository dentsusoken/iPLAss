/*
 * Copyright (C) 2019 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.command.annotation.webapi;

import org.iplass.mtp.webapi.definition.WebApiParamMapDefinition;

/**
 * WebApiParamMapDefinitionを定義するアノテーションです。
 */
public @interface WebApiParamMapping {

	/**
	 * WebApiのパスを除いたサブパスの文字列をマップする場合の定数です。
	 */
	public static final String PATHS = WebApiParamMapDefinition.PATHS;
	
	String name();
	String mapFrom();
	String condition() default "##default";
}
