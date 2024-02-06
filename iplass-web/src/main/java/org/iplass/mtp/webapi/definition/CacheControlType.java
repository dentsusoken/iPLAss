/*
 * Copyright (C) 2018 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.webapi.definition;

/**
 * WebAPIキャッシュ指定（Cache-Controlヘッダ）の種別です。
 */
public enum CacheControlType {

	/**
	 * キャッシュを許可します。
	 * 具体的にはCache-Controlをprivate指定します。
	 * CACHEを指定する場合は、合わせてclientCacheMaxAgeも指定してください。
	 * 
	 */
	CACHE,
	
	/**
	 * キャッシュを許可します。
	 * 具体的にはCache-Controlをpublic指定します。
	 * CACHE_PUBLICを指定する場合は、合わせてclientCacheMaxAgeも指定してください。
	 */
	CACHE_PUBLIC,
	
	/**
	 * キャッシュを許可しません。
	 * 具体的にはCache-Controlをprivate, no-store, no-cache, must-revalidate指定します
	 * （加えて、HTTP/1.0の場合は、Pragmaをno-cache指定します）。
	 * 
	 */
	NO_CACHE,

	/**
	 * キャッシュ設定は未指定です。
	 * 具体的にはCache-Controlを指定しません。
	 */
	UNSPECIFIED;
}
