/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * EntityVie編集用の画面で使用するフィールドの情報を定義するアノテーション
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EntityViewField {

	/**
	 * <p>フィールドを参照するタイプ</p>
	 *
	 * <p>
	 * ここで指定されたタイプの場合のみ設定画面に表示される。
	 * </p>
	 * <p>
	 * EditorなどはSearchLayout、DetailLayout両方で共有しているため、
	 * 検索条件でしか参照しないものや検索結果でのみ参照するもの、
	 * 編集画面でしか参照しないものなどフィールドが混在している。
	 * これを改善するため設定画面上へのフィールドの表示をフィルタするために利用する。
	 * </p>
	 */
	FieldReferenceType[] referenceTypes() default {FieldReferenceType.ALL};

	/**
	 * <p>起動トリガーのタイプを上書きする参照タイプ</p>
	 *
	 * <p>
	 * ここで指定されたフィールドの参照するタイプが起動トリガーのタイプに上書きされる。
	 * FieldReferenceType.NULLを設定した場合、上書きされない。
	 * <p>
	 *
	 * <p>
	 * 例えば、親画面が検索結果のトリガータイプ{@link org.iplass.adminconsole.view.annotation.generic.FieldReferenceType.SEARCHRESULT}で起動された場合、
	 * 子画面を詳細編集のトリガータイプ{@link org.iplass.adminconsole.view.annotation.generic.FieldReferenceType.DETAIL}として起動したい場合、
	 * FieldReferenceType.DETAILを設定することで可能になります。
	 * <p>
	 * @return
	 */
	FieldReferenceType overrideTriggerType() default FieldReferenceType.NONE;
}
