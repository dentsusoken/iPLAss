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

public class WebApiPermissionInfoDS extends PermissionTreeGridDS {

	/**
	 * DSインスタンスを返します。
	 *
	 * @return DSインスタンス
	 */
	public static WebApiPermissionInfoDS getInstance(LinkedHashMap<String, Entity> roleMap) {
		return new WebApiPermissionInfoDS(roleMap);
	}

	private WebApiPermissionInfoDS(LinkedHashMap<String, Entity> roleMap) {
		super(roleMap);
	}

	@Override
	protected void executeFetch(final String requestId, final DSRequest request, final DSResponse response) {

		Criteria criteria = request.getCriteria();
		if (criteria != null && criteria.getAttributeAsObject(EDIT_DATA_KEY) != null) {
			final PermissionSearchResult editingResult = (PermissionSearchResult)criteria.getAttributeAsObject(EDIT_DATA_KEY);

			//FIXME そのままResponseを返すとTreeの展開のための情報がうまく制御できないのでサーバに接続
			//(onDataArrivedイベントが走らない、ds.fetchDataのDSCallbackのタイミングだとまだTreeのNodeが生成されていない)
			//setResponsePermissinData(requestId, request, response, editingResult);
			service.dummyConnect(TenantInfoHolder.getId(), new AsyncCallback<Void>() {
				@Override
				public void onFailure(Throwable caught) {
					GWT.log(caught.getMessage(), caught);
					SC.warn(getPermissionSearchErrMessage() + caught.getMessage());
				}

				@Override
				public void onSuccess(Void result) {
					setResponsePermissinData(requestId, request, response, editingResult);
				}
			});

		} else {
			//ここが個別
			service.getAllWebApiPermissionData(TenantInfoHolder.getId(), new AsyncCallback<PermissionSearchResult>() {

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

}
