/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.tools.data.permissionexplorer;

import java.util.LinkedHashMap;

import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.shared.tools.dto.permissionexplorer.PermissionSearchResult;
import org.iplass.mtp.entity.Entity;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.util.SC;

public class EntityPermissionInfoDS extends PermissionListGridDS {

	/**
	 * DSインスタンスを返します。
	 * @return DSインスタンス
	 */
	public static EntityPermissionInfoDS getInstance(LinkedHashMap<String, Entity> roleMap) {
		return new EntityPermissionInfoDS(roleMap);
	}

	private EntityPermissionInfoDS(LinkedHashMap<String, Entity> roleMap) {
		super(roleMap);
	}

	@Override
	protected void executeFetch(final String requestId, final DSRequest request, final DSResponse response) {

		Criteria criteria = request.getCriteria();
		if (criteria != null && criteria.getAttributeAsObject(EDIT_DATA_KEY) != null) {
			PermissionSearchResult result = (PermissionSearchResult)criteria.getAttributeAsObject(EDIT_DATA_KEY);
			setResponsePermissinData(requestId, request, response, result);
		} else {
			//ここが個別
			service.getAllEntityPermissionData(TenantInfoHolder.getId(), new AsyncCallback<PermissionSearchResult>() {

				@Override
				public void onFailure(Throwable caught) {
					GWT.log(caught.getMessage(), caught);
					SC.warn(getPermissionSearchErrMessage() + caught.getMessage());
				}

				@Override
				public void onSuccess(PermissionSearchResult result) {
					setResponsePermissinData(requestId, request, response, result);
				}
			});
		}
	}

	/**
	 * <p>EntityPermissionはCRUDの状態を表示させるためカスタマイズ</p>
	 */
	@Override
	public String getPermissionConfiguredSummaryText(Entity permission) {
		StringBuffer status = new StringBuffer();
		if (Boolean.valueOf(permission.getValue("canCreate").toString())) {
			status.append("C");
			if (permission.getValue("createCondition") != null
					|| permission.getValue("createPropertyControlType") != null
					|| permission.getValue("createPropertyList") != null) {
				status.append("(*)");
			}
		}
		if (Boolean.valueOf(permission.getValue("canReference").toString())) {
			if (status.length() > 0) {
				status.append("&nbsp|&nbsp");
			}
			status.append("R");
			if (permission.getValue("referenceCondition") != null
					|| permission.getValue("referencePropertyControlType") != null
					|| permission.getValue("referencePropertyList") != null) {
				status.append("(*)");
			}
		}
		if (Boolean.valueOf(permission.getValue("canUpdate").toString())) {
			if (status.length() > 0) {
				status.append("&nbsp|&nbsp");
			}
			status.append("U");
			if (permission.getValue("updateCondition") != null
					|| permission.getValue("updatePropertyControlType") != null
					|| permission.getValue("updatePropertyList") != null) {
				status.append("(*)");
			}
		}
		if (Boolean.valueOf(permission.getValue("canDelete").toString())) {
			if (status.length() > 0) {
				status.append("&nbsp|&nbsp");
			}
			status.append("D");
			if (permission.getValue("deleteCondition") != null) {
				status.append("(*)");
			}
		}

		if (status.length() > 0) {
			return status.toString();
		} else {
			return "All not allowed";
		}
	}

	@Override
	public String getPermissionEditingSummaryText(Entity permission) {
		return getPermissionConfiguredSummaryText(permission);
	}
}
