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

package org.iplass.mtp.impl.tools.pack;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.impl.tools.entityport.EntityDataExportCondition;

/**
 * パッケージの作成条件
 */
public class PackageCreateCondition implements Serializable {

	private static final long serialVersionUID = -2068952449510445639L;

	/** 対象メタデータ */
	private List<String> metaDataPaths;

	/** 対象Entityデータ */
	private List<String> entityPaths;

	/** 対象Entityデータ条件(KEY：name) */
	private Map<String, EntityDataExportCondition> entityConditions;

	/** Package名 */
	private String name;

	/** Package説明 */
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
	 * @return entityConditions
	 */
	public Map<String, EntityDataExportCondition> getEntityConditions() {
		return entityConditions;
	}

	/**
	 * @param entityConditions セットする entityConditions
	 */
	public void setEntityConditions(Map<String, EntityDataExportCondition> entityConditions) {
		this.entityConditions = entityConditions;
	}

	/**
	 * @param name Entity定義名
	 * @param entityCondition セットする entityCondition
	 */
	public void addEntityCondition(String name, EntityDataExportCondition entityCondition) {
		if (entityConditions == null) {
			entityConditions = new HashMap<>();
		}
		entityConditions.put(name, entityCondition);
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
