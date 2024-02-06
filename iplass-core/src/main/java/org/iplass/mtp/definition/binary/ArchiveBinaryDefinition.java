/*
 * Copyright (C) 2016 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.definition.binary;

import java.io.InputStream;

/**
 * zip形式（およびjar形式）のバイナリ定義（メタデータ）を表すインタフェース。
 * zip（jar）内のエントリを取得するためのインタフェースを持つ。
 * 
 * @author K.Higuchi
 *
 */
public interface ArchiveBinaryDefinition extends BinaryDefinition {
	
	/**
	 * 指定のpathで指し示されるzip（jar）内エントリのInputStreamを取得する。
	 * 
	 * @param path
	 * @return
	 */
	public InputStream getEntryAsStream(String path);
	
	/**
	 * 指定のpathで指し示されるzip（jar）内エントリのサイズを取得する。
	 * 
	 * @param path
	 * @return
	 */
	public long getEntrySize(String path);
	
	/**
	 * 指定のpathで指し示されるzip（jar）内エントリが存在するかを取得する。
	 * 
	 * @param path
	 * @return
	 */
	public boolean hasEntry(String path);
}
