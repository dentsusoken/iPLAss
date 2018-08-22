package org.iplass.adminconsole.client.metadata.data.auth;

import java.util.List;

import org.iplass.adminconsole.client.base.data.AbstractAdminDataSource;
import org.iplass.adminconsole.client.base.data.DataSourceConstants;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.entity.Entity;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class RoleDS extends AbstractAdminDataSource {

	private static final DataSourceField[] fields;
	private static MetaDataServiceAsync service = MetaDataServiceFactory.get();

	static {
		DataSourceTextField key = new DataSourceTextField("key", "KEY");
		key.setPrimaryKey(true);
		DataSourceField name = new DataSourceField(
				DataSourceConstants.FIELD_NAME,
				FieldType.TEXT,
				DataSourceConstants.FIELD_NAME_TITLE);
		DataSourceField code = new DataSourceField("code", FieldType.CUSTOM, "CODE");
		fields = new DataSourceField[] {key, name, code};
	}

    public static void setDataSource(final SelectItem item) {

    	item.setOptionDataSource(RoleDS.getInstance());
    	item.setValueField("code");
		ListGridField nameField = new ListGridField(DataSourceConstants.FIELD_NAME, DataSourceConstants.FIELD_NAME_TITLE);
		item.setPickListFields(nameField);
		item.setPickListWidth(420);
    }

	public static RoleDS getInstance() {
		return new RoleDS();
	}

	private RoleDS() {
		setFields(fields);
	}

	@Override
	protected void executeFetch(final String requestId, final DSRequest request, final DSResponse response) {

		service.getRoles(TenantInfoHolder.getId(), new AsyncCallback<List<Entity>>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("error!!!", caught);

				response.setStatus(RPCResponse.STATUS_FAILURE);
				processResponse(requestId, response);
			}

			@Override
			public void onSuccess(List<Entity> result) {
				GWT.log(result.toString());

				ListGridRecord[] list = new ListGridRecord[result.size()];
				int i = 0;
				for (Entity entity : result) {
					ListGridRecord record = new ListGridRecord();
					record.setAttribute("key", entity.getOid());
					record.setAttribute(DataSourceConstants.FIELD_NAME, entity.getName());
					record.setAttribute("code", (String) entity.getValue("code"));
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
