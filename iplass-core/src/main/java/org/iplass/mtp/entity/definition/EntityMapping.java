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

package org.iplass.mtp.entity.definition;

import java.io.Serializable;


/**
 * javaコード内で、汎用モデルをPOJOで扱いたい場合のEntity-POJO間のマッピングを定義するクラス。
 *
 * @author K.Higuchi
 *
 */
public class EntityMapping implements Serializable {

	private static final long serialVersionUID = 1163207793249672806L;

	/** POJOにマッピングする際のクラス */
	private String mappingModelClass;

	//TODO とりあえずクラス名のマッピングだけで、プロパティは同一名のプロパティに格納するものとする。後々、プロパティのマッピングも可能とする。

	public EntityMapping() {
	}

	public EntityMapping(String mappingModelClass) {
		this.mappingModelClass = mappingModelClass;
	}

	public String getMappingModelClass() {
		return mappingModelClass;
	}

	public void setMappingModelClass(String mappingModelClass) {
		this.mappingModelClass = mappingModelClass;
	}

}
