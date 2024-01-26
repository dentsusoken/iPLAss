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

package org.iplass.mtp.impl.metadata;

public interface MetaDataRuntime {
	
	
	public MetaData getMetaData();
	
	//TODO 持つべきメソッドは？
	//instanceate()
	//handle()<-汎用的過ぎる。。
	
	/**
	 * 実行可能な状態かどうかをチェックする。
	 * 実行不可の場合、MetaDataIllegalStateExceptionをスロー
	 * 
	 * @throws MetaDataIllegalStateException
	 */
	public void checkState() throws MetaDataIllegalStateException;

}
