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

package org.iplass.mtp.entity.definition.properties;

/**
 * {@link ReferenceProperty}での参照先のバージョン管理タイプ
 * 
 * @author K.Higuchi
 *
 */
public enum VersionControlReferenceType {
	
	/** 参照元Entityを保存したバージョン（もしくは日付）で参照先を検索 */
	RECORD_BASE,
	/** 現時点の最新の状態（有効なバージョン、もしくは現在日付）で参照先を検索 */
	CURRENT_BASE,
	/** 特定の時点のバージョン（もしくは日付）を指定して参照先を検索 */
	AS_OF_EXPRESSION_BASE

}
