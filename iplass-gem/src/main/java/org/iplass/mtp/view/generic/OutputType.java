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

package org.iplass.mtp.view.generic;

/**
 * 出力種別
 * @author lis3wg
 *
 */
public enum OutputType {
	/** 詳細表示 */ VIEW,
	/** 詳細編集 */ EDIT,
	/** 検索条件 */ SEARCHCONDITION,
	/** 検索結果 */ SEARCHRESULT,
	/** 単一選択 */ SINGLESELECT,
	/** 複数選択 */ MULTISELECT,
	/** 一括更新編集 */ BULKEDIT
}
