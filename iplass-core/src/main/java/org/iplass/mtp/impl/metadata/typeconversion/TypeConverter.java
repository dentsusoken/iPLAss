/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.metadata.typeconversion;

import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.impl.metadata.MetaDataEntryInfo;

/**
 * pathとMetaDataの型を変換するためのインタフェース。
 * 
 * @author K.Higuchi
 *
 */
public interface TypeConverter {
	public boolean hasFallbackPath(String path);
	public String fallbackPath(String path);
	public boolean isConvertTarget(MetaDataEntry entry);
	public void convert(MetaDataEntry entry);
	public boolean isConvertTarget(MetaDataEntryInfo entryInfo);
	public void convert(MetaDataEntryInfo entryInfo);
}
