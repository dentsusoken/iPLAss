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

package org.iplass.adminconsole.shared.tools.dto.pack;

import java.io.Serializable;
import java.util.List;

/**
 * パッケージ作成時の設定情報
 */
public class PackageCreateInfo implements Serializable {

	private static final long serialVersionUID = 1969389107353707392L;

	private List<String> metaDataPaths;

	private List<String> entityPaths;

	private String name;
	private String description;

	/**
	 * @return metaDataPaths
	 */
	public List<String> getMetaDataPaths() {
		return metaDataPaths;
	}

	/**
	 * @param metaDataPaths セットする metaDataPaths
	 */
	public void setMetaDataPaths(List<String> metaDataPaths) {
		this.metaDataPaths = metaDataPaths;
	}

	/**
	 * @return entityPaths
	 */
	public List<String> getEntityPaths() {
		return entityPaths;
	}

	/**
	 * @param entityPaths セットする entityPaths
	 */
	public void setEntityPaths(List<String> entityPaths) {
		this.entityPaths = entityPaths;
	}

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name セットする name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description セットする description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
