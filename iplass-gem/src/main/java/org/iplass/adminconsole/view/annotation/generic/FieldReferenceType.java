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

package org.iplass.adminconsole.view.annotation.generic;

/**
 * EntityViewのフィールドを参照するタイプ
 *
 * EntityViewの各Fieldに対して、参照するタイプを指定する。
 *
 */
public enum FieldReferenceType {
	/** 検索条件 */
	SEARCHCONDITION,
	/** 検索結果 */
	SEARCHRESULT,
	/** 詳細、編集画面 */
	DETAIL,
	/** 一括更新画面 */
	BULK,
	/** 全て */
	ALL,
	/** 未設定 */
	NONE
}
