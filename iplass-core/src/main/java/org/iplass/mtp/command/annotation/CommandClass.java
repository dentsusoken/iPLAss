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

package org.iplass.mtp.command.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.iplass.mtp.definition.annotation.LocalizedString;

/**
 * Commandクラスの定義。
 *
 * @author K.Higuchi
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommandClass {
	/**
	 * Commandクラス定義名。
	 * 未指定の場合（デフォルト）は、このアノテーションが付与されたクラスのクラス名の"."を"/"に置き換えた定義名となる。
	 * @return
	 */
	String name() default "##default";
	/**
	 * 表示名。
	 * @return
	 */
	String displayName() default "##default";
	/**
	 * 表示名（多言語設定）。
	 * @return
	 */
	LocalizedString[] localizedDisplayName() default {};
	/**
	 * 概要説明文。
	 * @return
	 */
	String description() default "##default";
	
	/**
	 * ローカルテナントで上書き可能か否か。
	 * @return
	 */
	boolean overwritable() default true;
	
	/**
	 * このCommandの処理がreadOnly（トランザクションの意味合いで）の場合trueを設定。
	 * デフォルトfalse。
	 * @return
	 */
	boolean readOnly() default false;
	
	/**
	 * このCommandのインスタンスを処理（execute）の都度生成するか否か。
	 * デフォルトfalse（同一のインスタンスを使い回す）。
	 * @return
	 */
	boolean newInstancePerRequest() default false;
	
	
//	/**
//	 * Commandの実行結果ステータスの一覧定義（現状、未指定でも可。仕様明確化のためのメモの位置づけ）。
//	 * @return
//	 */
//	String[] resultStatus() default {};
//	CommandDef[] instance() default{@CommandDef};
//	ParameterDef[] configParameterName();
}
