/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.command.annotation.action;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>TokenCheckを定義するアノテーションです。</p>
 * 
 * @see org.iplass.mtp.web.actionmapping.definition.TokenCheck
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TokenCheck {

	/**
	 * <p>Tokenチェックの実施設定</p>
	 *
	 * デフォルト(true)の場合、Tokenチェックを実行します。
	 */
	boolean executeCheck() default true;

	/**
	 * <p>正常時のToken利用設定</p>
	 *
	 * デフォルト(true)の場合、Tokenは再利用されません。
	 */
	boolean consume() default true;

	/**
	 * <p>Exception発生時のTokenロールバック設定</p>
	 *
	 * デフォルト(true)の場合、ロールバック(現在のTokenを再設定)します。
	 */
	boolean exceptionRollback() default true;
	
	/**
	 * <p>固定値のTokenの利用可否の設定</p>
	 * 
	 * Tokenチェックに、セッション単位に固定に払いだされるTokenを用いる場合にtureを設定。
	 * デフォルトfalse。
	 * 固定値を利用する場合は、トランザクションの重複実行は防げないが、CSRFは防げる。
	 * 
	 * @return
	 */
	boolean useFixedToken() default false;

}
