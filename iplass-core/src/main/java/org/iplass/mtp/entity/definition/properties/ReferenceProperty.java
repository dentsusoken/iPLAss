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

package org.iplass.mtp.entity.definition.properties;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;

/**
 * 別のEntityへの参照を表すプロパティ定義。
 * 
 * @author K.Higuchi
 *
 */
public class ReferenceProperty extends PropertyDefinition {
	private static final long serialVersionUID = -6892130540731635845L;
	
	//TODO 複数の型を扱えるようにする（インタフェースの概念の導入）
	
	//TODO 結合する際、oidだけでなく、それ以外の項目（oid以外のプライマリキー、キー以外の項目）での結合も許すかどうかの再検討。

	/** 属性のデータ型がAGGREGATE、ASSOCIATIONの場合の参照先の汎用データの定義名 */
	private String objectDefinitionName;
	private String mappedBy;
	private ReferenceType referenceType;
	private VersionControlReferenceType versionControlType;
	private String versionControlAsOfExpression;
	private boolean auditLogMappedBy;
	
	private String orderBy;
	
	
	public ReferenceProperty() {
	}
	
	public ReferenceProperty(String name, String objectDefinitionName,
			ReferenceType referenceType) {
		setName(name);
		this.objectDefinitionName = objectDefinitionName;
		this.referenceType = referenceType;
	}
	
	public ReferenceProperty(String name, String objectDefinitionName, String mappedBy,
			ReferenceType referenceType) {
		setName(name);
		this.objectDefinitionName = objectDefinitionName;
		this.mappedBy = mappedBy;
		this.referenceType = referenceType;
	}
	
	public ReferenceProperty(String name, String objectDefinitionName,
			ReferenceType referenceType, int multiplicity) {
		setName(name);
		setMultiplicity(multiplicity);
		this.objectDefinitionName = objectDefinitionName;
		this.referenceType = referenceType;
	}
	
	public ReferenceProperty(String name, String objectDefinitionName, String mappedBy,
			ReferenceType referenceType, int multiplicity) {
		setName(name);
		setMultiplicity(multiplicity);
		this.objectDefinitionName = objectDefinitionName;
		this.mappedBy = mappedBy;
		this.referenceType = referenceType;
	}
	
	public String getVersionControlAsOfExpression() {
		return versionControlAsOfExpression;
	}

	public void setVersionControlAsOfExpression(String versionControlAsOfExpression) {
		this.versionControlAsOfExpression = versionControlAsOfExpression;
	}

	public VersionControlReferenceType getVersionControlType() {
		return versionControlType;
	}

	public void setVersionControlType(VersionControlReferenceType versionControlType) {
		this.versionControlType = versionControlType;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getObjectDefinitionName() {
		return objectDefinitionName;
	}

	public void setObjectDefinitionName(String objectDefinitionName) {
		this.objectDefinitionName = objectDefinitionName;
	}

	public ReferenceType getReferenceType() {
		return referenceType;
	}

	public void setReferenceType(ReferenceType referenceType) {
		this.referenceType = referenceType;
	}

	public String getMappedBy() {
		return mappedBy;
	}

	public void setMappedBy(String mappedBy) {
		this.mappedBy = mappedBy;
	}

	@Override
	public Class<?> getJavaType() {
		return Entity.class;//TODO POJOを指定可能にする
	}
	
	@Override
	public PropertyDefinitionType getType() {
		return PropertyDefinitionType.REFERENCE;
	}

	public boolean isAuditLogMappedBy() {
		return auditLogMappedBy;
	}
	
	public void setAuditLogMappedBy(boolean auditLogMappedBy) {
		this.auditLogMappedBy = auditLogMappedBy;
	}

//	public ReferenceProperty copy() {
//		ReferenceProperty copy = new ReferenceProperty();
//		copyTo(copy);
//		copy.objectDefinitionName = objectDefinitionName;
//		copy.referenceType = referenceType;
//		copy.mappedBy = mappedBy;
//		return copy;
//	}

}
