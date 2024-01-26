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
package org.iplass.mtp.impl.metadata.xmlfile.dom;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.impl.metadata.MetaDataRuntimeException;

public class AbstractXsiType implements XsiType {

	/**
	 * 外部参照ファイルとするパスの取得. 
	 * <p>@{link ExternalRefPathClass}で定義したPathクラスを全部入れる.</p>
	 */
	@Override
	public final List<ExternalRefPath> getExternalRefPaths() {
		List<ExternalRefPath> list = new ArrayList<ExternalRefPath>();
		Class<?>[] annotatedPathClass = getClass().getAnnotation(ExternalRefPathClass.class).value();
		for (Class<?> ac : annotatedPathClass) {
			try {
				list.add((ExternalRefPath) ac.newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				throw new MetaDataRuntimeException("can not instantiate " + ac.getName(), e);
			}
		}
		return list;
	}
}