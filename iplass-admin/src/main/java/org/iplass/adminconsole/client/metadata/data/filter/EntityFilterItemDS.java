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

package org.iplass.adminconsole.client.metadata.data.filter;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.data.AbstractAdminDataSource;
import org.iplass.adminconsole.client.base.data.DataSourceConstants;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.view.filter.EntityFilter;
import org.iplass.mtp.view.filter.EntityFilterItem;

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


/**
 * フィルタ条件アイテム データソース
 */
public class EntityFilterItemDS extends AbstractAdminDataSource {

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

    public static void setDataSource(final SelectItem item, String defName) {
    	setup(item, defName);
    }

    private static void setup(final FormItem item, String defName) {

    	item.setOptionDataSource(EntityFilterItemDS.getInstance(defName));
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

    public static EntityFilterItemDS getInstance(String defName) {
		return new EntityFilterItemDS(defName);
    }

    private EntityFilterItemDS(String defName) {
    	setAttribute("defName", defName, true);

		setFields(fields);
    }

	@Override
	protected void executeFetch(final String requestId, final DSRequest request,
			final DSResponse response) {

		final String defName = getAttribute("defName");

		service.getDefinition(TenantInfoHolder.getId(), EntityFilter.class.getName(), defName, new AsyncCallback<EntityFilter>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("error!!!", caught);

				response.setStatus(RPCResponse.STATUS_FAILURE);
				processResponse(requestId, response);
			}

			@Override
			public void onSuccess(EntityFilter filter) {

				List<ListGridRecord> records = null;
				if  (filter != null) {
					List<EntityFilterItem> items = filter.getItems();
					if (items == null) {
						records = new ArrayList<ListGridRecord>(0);
					} else {
						records = new ArrayList<ListGridRecord>(items.size());

						for (EntityFilterItem item : items) {
							ListGridRecord record = new ListGridRecord();
							record.setAttribute("key", item.getName());
							record.setAttribute(DataSourceConstants.FIELD_NAME, item.getName());
							record.setAttribute(DataSourceConstants.FIELD_VALUE_OBJECT, item);
							records.add(record);
						}
					}
				} else {
					records = new ArrayList<ListGridRecord>(0);
				}

		    	response.setData(records.toArray(new ListGridRecord[]{}));
				response.setTotalRows(records.size());
				processResponse(requestId, response);
			}
		});

	}

	@Override
	protected void executeAdd(String requestId, DSRequest request, DSResponse response) {
		ListGridRecord record = new ListGridRecord(request.getData());
		response.setData(new ListGridRecord[] {record});
		processResponse(requestId, response);
	}

	@Override
	protected void executeUpdate(String requestId, DSRequest request, DSResponse response) {
		ListGridRecord record = new ListGridRecord(request.getData());
		response.setData(new ListGridRecord[] {record});
		processResponse(requestId, response);
	}

	@Override
	protected void executeRemove(String requestId, DSRequest request, DSResponse response) {
		ListGridRecord record = new ListGridRecord(request.getData());
		response.setData(new ListGridRecord[] {record});
		processResponse(requestId, response);
	}

}
