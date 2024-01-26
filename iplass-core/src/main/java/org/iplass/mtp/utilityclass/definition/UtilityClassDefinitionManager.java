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

package org.iplass.mtp.utilityclass.definition;

import org.iplass.mtp.definition.TypedDefinitionManager;

/**
 * UtilityClass定義を管理するクラスのインタフェース。
 *
 * @author K.Higuchi
 *
 */
public interface UtilityClassDefinitionManager extends TypedDefinitionManager<UtilityClassDefinition> {

	/**
	 * <p>指定のUtilityClass定義のインスタンスを生成します。</p>
	 *
	 * <p>Javaで作成されたCommandクラスなどからUtilityClassを利用したい場合に利用します。
	 * GroovyScript内などでUtilityClassを利用する場合は、Javaと同様にnewすれば利用可能なため、
	 * あえてこのメソッドを利用してインスタンス化する必要はありません。</p>
	 *
	 * <p>指定したUtilityClassが存在しない場合は、ClassNotFoundExceptionが発生します。</p>
	 *
	 * @param type 型
	 * @param definitionName 定義名
	 * @return typeに指定された型のインスタンス
	 * @throws ClassNotFoundException
	 */
	public <T> T createInstanceAs(Class<T> type, String definitionName) throws ClassNotFoundException;

}
