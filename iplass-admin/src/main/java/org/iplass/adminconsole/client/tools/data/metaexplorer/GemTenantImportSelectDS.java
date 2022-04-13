/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.tools.data.metaexplorer;

import org.iplass.adminconsole.client.metadata.data.tenant.GemTenantDS;
import org.iplass.adminconsole.client.metadata.data.tenant.TenantCategory;
import org.iplass.adminconsole.shared.metadata.dto.tenant.TenantInfo;
import org.iplass.mtp.tenant.Tenant;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class GemTenantImportSelectDS extends GemTenantDS {

	private static final DataSourceField[] fields;
	static {
		DataSourceField name = new DataSourceField("name", FieldType.TEXT);
		name.setPrimaryKey(true);
		DataSourceField title = new DataSourceField("title", FieldType.TEXT);
		DataSourceField value = new DataSourceField("value", FieldType.TEXT);
		DataSourceField displayValue = new DataSourceField("displayValue", FieldType.TEXT);
		DataSourceField category = new DataSourceField("category", FieldType.TEXT);

		DataSourceField fileValue = new DataSourceField("fileValue", FieldType.TEXT);
		DataSourceField fileDispValue = new DataSourceField("fileDisplayValue", FieldType.TEXT);

		fields = new DataSourceField[] { name, title, value, displayValue, category, fileValue, fileDispValue };
	}

	public static GemTenantImportSelectDS getInstance(Tenant fileTenant) {
		// 同時には考えにくいがMetaDataExplorerとPackageで2つ上がる可能性があるので、singletonにはしない（ダイアログだからありえないか）
		GemTenantImportSelectDS ds = new GemTenantImportSelectDS(fileTenant);
		ds.setFields(fields);
		return ds;
	}

	private Tenant fileTenant;

	/**
	 * コンストラクタ
	 *
	 */
	private GemTenantImportSelectDS(Tenant fileTenant) {
		super();

		this.fileTenant = fileTenant;

		// setFields(fields); 継承しているため、superのFieldとぶつかってWARNがでるのでgetInstanceで設定
	}

	@Override
	protected void executeFetch(final String requestId, final DSRequest request, final DSResponse response) {

		doExecuteFetch(new AsyncCallback<TenantInfo>() {

			@Override
			public void onSuccess(TenantInfo result) {

				// fileTenant情報を反映(値をfileValueに、表示値をfileDisplayValue)
				applyToRecord(fileTenant, "fileValue", "fileDisplayValue");

				for (ListGridRecord record : records.values()) {
					if (TenantCategory.BASICINFO == TenantCategory.valueOf(record.getAttribute("category"))) {
						// 基本情報は選択不可
						record.setEnabled(false);
					} else if (TenantCategory.MAILSENDSETTING == TenantCategory
							.valueOf(record.getAttribute("category"))) {
						// メール情報は初期未選択
					} else if ("urlForRequest".equals(record.getAttribute("name"))) {
						// リクエストパス構築用テナントURLは初期未選択
					} else if ("usePreview".equals(record.getAttribute("name"))) {
						// 日付プレビュー表示機能は初期未選択
					} else {
						// それ以外はデフォルト選択
						record.setAttribute("mySelected", true);
					}
				}

				response.setData(records.values().toArray(new ListGridRecord[] {}));
				response.setTotalRows(records.size());

				response.setAttribute("valueObject", result);

				processResponse(requestId, response);
			}

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("error!!!", caught);

				response.setStatus(RPCResponse.STATUS_FAILURE);
				processResponse(requestId, response);
			}
		});

	}

}
