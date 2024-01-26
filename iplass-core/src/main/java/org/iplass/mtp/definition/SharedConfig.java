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

package org.iplass.mtp.definition;

import java.io.Serializable;

/**
 * Definitionの共有設定を表すクラス。
 * 
 * @author K.Higuchi
 *
 */
public class SharedConfig implements Serializable {
	private static final long serialVersionUID = 5040486452347455302L;
	
	private boolean sharable;
	private boolean overwritable;
	private boolean dataSharable;
	private boolean permissionSharable;
	
	public SharedConfig() {
	}
	
	public SharedConfig(boolean sharable, boolean overwritable) {
		this(sharable, overwritable, false, false);
	}
	
	public SharedConfig(boolean sharable, boolean overwritable, boolean dataSharable, boolean permissionSharable) {
		this.sharable = sharable;
		this.overwritable = overwritable;
		this.dataSharable = dataSharable;
		this.permissionSharable = permissionSharable;
	}
	
	/**
	 * 当該Definitionが各テナントで共有される場合、
	 * 各テナントでのDefinitionの上書きを許可するかどうか。
	 * 
	 * @return
	 */
	public boolean isOverwritable() {
		return overwritable;
	}
	
	public void setOverwritable(boolean overwritable) {
		this.overwritable = overwritable;
	}
	
	/**
	 * 当該Definitionを各テナントで共有可能である場合true。
	 * 
	 * @return
	 */
	public boolean isSharable() {
		return sharable;
	}
	
	public void setSharable(boolean sharable) {
		this.sharable = sharable;
	}
	
	/**
	 * 当該のDefinitionが扱うデータを各テナントで共有可能にする場合true。
	 * 現時点では、EntityDefinitionでのみこの設定が有効。
	 * 
	 * @return
	 */
	public boolean isDataSharable() {
		return dataSharable;
	}

	public void setDataSharable(boolean dataSharable) {
		this.dataSharable = dataSharable;
	}

	/**
	 * 当該のDefinitionの定義のセキュリティ設定を共有にする場合true。
	 * EntityDefinition、ActionMappingDefinition、WebAPIDefinition、WorkflowDefinitionに設定可能。
	 * 
	 * @return
	 */
	public boolean isPermissionSharable() {
		return permissionSharable;
	}

	public void setPermissionSharable(boolean permissionSharable) {
		this.permissionSharable = permissionSharable;
	}

}
