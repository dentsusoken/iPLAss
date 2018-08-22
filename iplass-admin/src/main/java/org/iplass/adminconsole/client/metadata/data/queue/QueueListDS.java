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

package org.iplass.adminconsole.client.metadata.data.queue;

import java.util.List;

import org.iplass.adminconsole.client.base.data.AbstractAdminDataSource;
import org.iplass.adminconsole.client.base.data.DataSourceConstants;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class QueueListDS extends AbstractAdminDataSource {

	private static final DataSourceField[] fields;
	private static MetaDataServiceAsync service = MetaDataServiceFactory.get();

	static {
		DataSourceTextField key = new DataSourceTextField("key", "KEY");
		key.setPrimaryKey(true);
		DataSourceField name = new DataSourceField(
				DataSourceConstants.FIELD_NAME,
				FieldType.TEXT,
				DataSourceConstants.FIELD_NAME_TITLE);
		fields = new DataSourceField[] {key, name};
	}

    public static void setDataSource(final SelectItem item, boolean addBlank) {
    	setup(item, addBlank);
    }

    private static void setup(final FormItem item, boolean addBlank) {

    	item.setOptionDataSource(QueueListDS.getInstance(addBlank));
    	item.setValueField(DataSourceConstants.FIELD_NAME);

    	if (item instanceof SelectItem) {
    		ListGridField nameField = new ListGridField(DataSourceConstants.FIELD_NAME, DataSourceConstants.FIELD_NAME_TITLE);
    		((SelectItem)item).setPickListFields(nameField);
    		((SelectItem)item).setPickListWidth(420);
    	} else if (item instanceof ComboBoxItem) {
    		ListGridField nameField = new ListGridField(DataSourceConstants.FIELD_NAME, DataSourceConstants.FIELD_NAME_TITLE);
    		((ComboBoxItem)item).setPickListFields(nameField);
    		((ComboBoxItem)item).setPickListWidth(420);
    	}

    }


	private boolean addBlank;

	public static QueueListDS getInstance() {
		return getInstance(false);
	}

	public static QueueListDS getInstance(boolean addBlank) {
		return new QueueListDS(addBlank);
	}

	private QueueListDS(boolean addBlank) {
		this.addBlank = addBlank;
		setFields(fields);
	}

	@Override
	protected void executeFetch(final String requestId, final DSRequest request, final DSResponse response) {

		service.getQueueNameList(TenantInfoHolder.getId(), new AsyncCallback<List<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("error!!!", caught);

				response.setStatus(RPCResponse.STATUS_FAILURE);
				processResponse(requestId, response);
			}

			@Override
			public void onSuccess(List<String> result) {
				GWT.log(result.toString());

				//ソート
//				Collections.sort(result, new Comparator<String>() {
//					@Override
//					public int compare(String o1, String o2) {
//						return o1.compareTo(o2);
//					}
//				});

				if (QueueListDS.this.addBlank) {
					result.add(0, "");
				}

				ListGridRecord[] list = new ListGridRecord[result.size()];
				int i = 0;
				for (String name : result) {
					ListGridRecord record = new ListGridRecord();
					record.setAttribute(DataSourceConstants.FIELD_NAME, name);
					list[i] = record;
					i++;
				}
				response.setData(list);
				response.setTotalRows(result.size());
				processResponse(requestId, response);
			}

		});
	}

}
