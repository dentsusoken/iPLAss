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

package org.iplass.adminconsole.view.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * メタデータ編集用の画面で使用するフィールドの情報を定義するアノテーション
 * @author lis3wg
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MetaFieldInfo {
	/**
	 * 画面に表示する名称
	 */
	String displayName();

	/**
	 * 画面に表示する名称のキー
	 */
	String displayNameKey() default "";

	/**
	 * フィールドの値の入力方法
	 */
	InputType inputType() default InputType.TEXT ;

	/**
	 * このフィールドが必須入力か
	 */
	boolean required() default false;

	/**
	 * 入力方法がNumberの時に範囲チェックを行うか
	 */
	boolean rangeCheck() default false;

	/**
	 * 範囲チェックの最大値(デフォルト-127の時はチェックを行わない)
	 */
	int maxRange() default -127;

	/**
	 * 範囲チェックの最小値(デフォルト-127の時はチェックを行わない)
	 */
	int minRange() default -127;

	/**
	 * 配列・リストのフィールドか
	 */
	boolean multiple() default false;

	/**
	 * 非推奨または未使用の項目か
	 */
	boolean deprecated() default false;

	/**
	 * 参照型クラス
	 */
	Class<?> referenceClass() default Object.class;

	/**
	 * 参照型クラス(固定)
	 * 特定のクラスのみ利用する場合に使用する。
	 */
	Class<?>[] fixedReferenceClass() default {};

	/**
	 * Enumクラス
	 */
	Class<?> enumClass() default Object.class;

	/**
	 * フィールドの説明
	 */
	String description() default "";

	/**
	 * フィールドの説明のキー
	 */
	String descriptionKey() default "";

	/**
	 * スクリプトのモード
	 */
	String mode() default "";

	/**
	 * 多言語設定を利用するか
	 */
	boolean useMultiLang() default false;

	/**
	 * inputTypeがPropertyの場合、そのプロパティの型を別の入力項目のProperty選択に利用するか
	 */
	boolean useReferenceType() default false;

	/**
	 * inputTypeがPropertyの場合、このプロパティの選択肢を取得するためのEntity定義名
	 */
	String entityDefinitionName() default "";
}
