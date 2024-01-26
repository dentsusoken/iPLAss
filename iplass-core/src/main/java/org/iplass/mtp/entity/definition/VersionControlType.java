/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.entity.definition;

/**
 * Entityのバージョン管理の方式。
 * 
 * @author K.Higuchi
 *
 */
public enum VersionControlType {
	
	/** バージョン管理に関しなにもしない（デフォルト） */
	NONE,
	
	/** 複数バージョンの保持を可能にする（１つのバージョンのみ有効化可能） */
	VERSIONED,
	
	/** 有効期間を持ち、時間ベースでデータを管理する */
	TIMEBASE,
	
	/** 有効期間を持ち、時間ベースでデータを管理する。加えて異なるバージョンにおいて、有効期間の重複はない前提 */
	SIMPLE_TIMEBASE,
	
	/** stateプロパティが有効となっているバージョンを有効とみなす。stateが有効なバージョンは最大でも１つである前提 */
	STATEBASE
}
