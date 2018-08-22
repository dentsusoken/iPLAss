/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface MultiLang {

	/**
	 *  項目のnameを取得するGetterMethod名
	 *  <p>指定した場合、このgetterから取れた名前をLangExplorerのItem名の一部に利用する</p>
	 */
	String itemNameGetter() default "";

	/** 親クラスの名前をキー名に使うかどうか */
	boolean isUseSuperForItemName() default false;

	/** ListやMap等ではなく、この項目自体が多言語項目かどうか */
	boolean isMultiLangValue() default true;

	/**
	 *  多言語項目のキー名
	 *  <p>省略時はフィールド名を返す(メソッドでの利用時は省略不可)</p>
	 */
	String itemKey() default "";

	/**
	 * 多言語項目のGetterMethod名
	 * <p>省略時はgetXxxを返す(XxxはitemKeyの値で先頭が大文字)</p>
	 */
	String itemGetter() default "";

	/**
	 * 多言語項目のSetterMethod名
	 * <p>省略時はsetXxxを返す(XxxはitemKeyの値で先頭が大文字)</p>
	 */
	String itemSetter() default "";

	/**
	 * 多言語項目のLocalizedListのGetterMethod名
	 * <p>省略時はgetLocalizedXxxListを返す(XxxはitemKeyの値で先頭が大文字)</p>
	 */
	String multiLangGetter() default "";

	/**
	 * 多言語項目のLocalizedListのSetterMethod名
	 * <p>省略時はsetLocalizedXxxListを返す(XxxはitemKeyの値で先頭が大文字)</p>
	 */
	String multiLangSetter() default "";

	/** 多言語項目がSelectValueかどうか */
	boolean isSelectValue() default false;

	/** 多言語項目のデフォルト言語値が入力必須項目かどうか */
	boolean isRequired() default false;
}
