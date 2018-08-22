/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.command.annotation.action.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.iplass.mtp.web.actionmapping.ActionCacheCriteria;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CacheCriteria {

	/** Cache判定の定義タイプ */
	Type type() default Type.NO_CACHE;

	/** キャッシュしてよいCommandのステータス。未指定、*指定の場合は、全てのステータスが対象。 */
	String[] cachableResultStatus() default {};

	/** キャッシュに関連するEntityの定義。当該Entityが更新されたら、キャッシュが無効化される。 */
	CacheRelatedEntity[] cacheRelatedEntity() default {};

	/** キャッシュ有効期間 (ms) */
	int timeToLive() default -1;

	/** Criteriaクラス。TypeがJavaClassの場合のみ有効 */
	Class<? extends ActionCacheCriteria> javaCriteriaClass() default ActionCacheCriteria.class;

	/** チェック用Parameter名。TypeがParameterMatchの場合のみ有効 */
	String[] matchingParameterName() default {};

	/** Criteria定義スクリプト。TypeがScriptingの場合のみ有効 */
	String scriptCriteria() default "";

	/**
	 * CacheCriteriaの定義タイプ
	 */
	public enum Type {
		/** Cacheしない */
		NO_CACHE,
		//NoCache,
		/** JavaClassで制御 */
		JAVA_CLASS,
		//JavaClass,
		/** Parameterの値で判定 */
		PARAMETER_MATCH,
		//ParameterMatch,
		/** Scriptで制御 */
		SCRIPTING
		//Scripting;
	}
}
