/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.entity.property.type;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.rpc.AdminAsyncCallback;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyAttribute;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyListGridRecord;
import org.iplass.adminconsole.shared.metadata.dto.entity.SortInfo;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.entity.definition.properties.ReferenceType;
import org.iplass.mtp.entity.definition.properties.VersionControlReferenceType;

import com.google.gwt.core.client.GWT;

public class ReferenceAttribute implements PropertyAttribute {

	private ReferenceProperty attribute;

	private List<SortInfo> sortList;

	private final MetaDataServiceAsync service = MetaDataServiceFactory.get();
	private PropertyTypeAttributeController typeController = GWT.create(PropertyTypeAttributeController.class);

	public ReferenceAttribute() {
		attribute = new ReferenceProperty();
		setReferenceType(ReferenceType.ASSOCIATION);
		setVersionControlType(VersionControlReferenceType.CURRENT_BASE);
	}

	@Override
	public void applyFrom(PropertyDefinition property, EntityDefinition entity) {

		ReferenceProperty reference = (ReferenceProperty) property;

		setObjectDefinitionName(reference.getObjectDefinitionName());
		setReferenceType(reference.getReferenceType());
		setVersionControlType(reference.getVersionControlType());
		setVersionControlAsOfExpression(reference.getVersionControlAsOfExpression());
		setMappedBy(reference.getMappedBy());
		setAuditLogMappedBy(reference.isAuditLogMappedBy());

		//ソート
		if (reference.getOrderBy() != null && reference.getOrderBy().length() > 0) {
			//文字列を分解してソート情報生成
			service.getSortInfo(TenantInfoHolder.getId(), reference.getOrderBy(), new AdminAsyncCallback<List<SortInfo>>() {
				@Override
				public void onSuccess(List<SortInfo> result) {
					setSortList(result);
				}
			});
		}

	}

	@Override
	public void applyTo(PropertyDefinition property, EntityDefinition entity) {

		ReferenceProperty reference = (ReferenceProperty)property;

		reference.setObjectDefinitionName(getObjectDefinitionName());
		reference.setReferenceType(getReferenceType());
		if (SmartGWTUtil.isNotEmpty(getMappedBy())) {
			reference.setMappedBy(getMappedBy());
			reference.setAuditLogMappedBy(isAuditLogMappedBy());
		}
		if (getVersionControlType() != null) {
			reference.setVersionControlType(getVersionControlType());
		}
		if (getVersionControlAsOfExpression() != null) {
			reference.setVersionControlAsOfExpression(getVersionControlAsOfExpression());
		}

		if (SmartGWTUtil.isNotEmpty(getSortList())) {
			StringBuffer sb = new StringBuffer();
			boolean isFirst = true;
			for (SortInfo info : getSortList()) {
				if (isFirst) {
					isFirst = false;
				} else {
					sb.append(",");
				}
				sb.append(info.getPropertyName());
				sb.append(" ");
				sb.append(info.getSortType());
			}
			reference.setOrderBy(sb.toString());
		}

	}

	@Override
	public void applyTo(PropertyListGridRecord record) {
		if (SmartGWTUtil.isNotEmpty(getObjectDefinitionName())) {
			record.setRemarks(getObjectDefinitionName());
		}

		String typeDisplayName = typeController.getTypeDisplayName(PropertyDefinitionType.REFERENCE);
		if (SmartGWTUtil.isNotEmpty(getMappedBy())) {
			//非参照の場合
			record.setRecordTypeDispValue(typeDisplayName + "(By)");
		} else {
			record.setRecordTypeDispValue(typeDisplayName);
		}
	}

	public String getObjectDefinitionName() {
		return attribute.getObjectDefinitionName();
	}

	public void setObjectDefinitionName(String value) {
		attribute.setObjectDefinitionName(value);
	}

	public ReferenceType getReferenceType() {
		return attribute.getReferenceType();
	}

	public void setReferenceType(ReferenceType value) {
		attribute.setReferenceType(value);
	}

	public String getMappedBy() {
		return attribute.getMappedBy();
	}

	public void setMappedBy(String value) {
		attribute.setMappedBy(value);
	}

	public boolean isAuditLogMappedBy() {
		return attribute.isAuditLogMappedBy();
	}

	public void setAuditLogMappedBy(Boolean value) {
		attribute.setAuditLogMappedBy(value);
	}

	public VersionControlReferenceType getVersionControlType() {
		return attribute.getVersionControlType();
	}

	public void setVersionControlType(VersionControlReferenceType value) {
		attribute.setVersionControlType(value);
	}

	public String getVersionControlAsOfExpression() {
		return attribute.getVersionControlAsOfExpression();
	}

	public void setVersionControlAsOfExpression(String value) {
		attribute.setVersionControlAsOfExpression(value);
	}

	public List<SortInfo> getSortList() {
		if (sortList == null) sortList = new ArrayList<SortInfo>();
		return sortList;
	}

	public void setSortList(List<SortInfo> sortList) {
		this.sortList = sortList;
	}

}
