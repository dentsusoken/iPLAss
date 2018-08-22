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

package org.iplass.mtp.impl.metadata;

import java.io.Serializable;

public interface MetaData extends Serializable {
	
	/**
	 * 当該のメタデータのコピーを作成する。
	 * 
	 * @return コピーされたメタデータ
	 */
	public MetaData copy();
	
	public boolean equals(Object obj);
	
	public int hashCode();
	
//	/**
//	 * このメタデータの情報から、何らかの処理を実際に実行するMetaDataRuntimeを取得する。
//	 * メタデータ自身は、リポジトリ上に保存するデータのみを保持する形に作成し、
//	 * 動的なものは、MetaDataRuntimeに保持するようにする。
//	 * 
//	 * @return MetaDataRuntimeのインスタンス
//	 */
//	public MetaDataRuntime createRuntime();
	
	

}
