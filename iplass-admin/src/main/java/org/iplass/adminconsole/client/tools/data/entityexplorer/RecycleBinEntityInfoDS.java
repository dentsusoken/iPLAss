package org.iplass.adminconsole.client.tools.data.entityexplorer;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.data.AbstractAdminDataSource;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.shared.tools.dto.entityexplorer.RecycleBinEntityInfo;
import org.iplass.adminconsole.shared.tools.rpc.entityexplorer.EntityExplorerServiceAsync;
import org.iplass.adminconsole.shared.tools.rpc.entityexplorer.EntityExplorerServiceFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class RecycleBinEntityInfoDS extends AbstractAdminDataSource {

	public static RecycleBinEntityInfoDS getInstance(Timestamp purgeTargetDate, boolean isGetDataCount) {
		return new RecycleBinEntityInfoDS(purgeTargetDate, isGetDataCount);
	}

	/** 件数取得制御フラグ */
	private boolean isGetDataCount = false;
	private Timestamp purgeTargetDate;

	private RecycleBinEntityInfoDS(Timestamp purgeTargetDate, boolean isGetDataCount) {
		this.purgeTargetDate = purgeTargetDate;
		this.isGetDataCount = isGetDataCount;

		DataSourceField nameField = new DataSourceTextField("name", AdminClientMessageUtil.getString("datasource_tools_entityexplorer_SimpleEntityInfoDS_name"));
		nameField.setPrimaryKey(true);
		DataSourceField displayNameField = new DataSourceTextField("displayName", AdminClientMessageUtil.getString("datasource_tools_entityexplorer_SimpleEntityInfoDS_dispName"));
		DataSourceField countField = new DataSourceTextField("count", AdminClientMessageUtil.getString("datasource_tools_entityexplorer_SimpleEntityInfoDS_number"));

		setFields(nameField, displayNameField, countField);
	}

	@Override
	protected void executeFetch(final String requestId, final DSRequest request, final DSResponse response) {
		
		EntityExplorerServiceAsync service = EntityExplorerServiceFactory.get();
		service.getRecycleBinInfoList(TenantInfoHolder.getId(), purgeTargetDate, isGetDataCount, new AsyncCallback<List<RecycleBinEntityInfo>>() {

			@Override
			public void onSuccess(List<RecycleBinEntityInfo> entities) {
				List<ListGridRecord> records = createRecord(entities);
				response.setData(records.toArray(new ListGridRecord[] {}));
				response.setTotalRows(records.size());
				response.setStartRow(0);
				processResponse(requestId, response);
			}

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("error!!!", caught);

				SC.warn(AdminClientMessageUtil.getString("datasource_tools_entityexplorer_SimpleEntityInfoDS_failedToGetEntityList") + caught.getMessage());

				response.setStatus(RPCResponse.STATUS_FAILURE);
				processResponse(requestId, response);
			}
		});

	}

	@Override
	protected void executeAdd(String requestId, DSRequest request, DSResponse response) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void executeUpdate(String requestId, DSRequest request, DSResponse response) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void executeRemove(String requestId, DSRequest request, DSResponse response) {
		// TODO Auto-generated method stub

	}

	private List<ListGridRecord> createRecord(List<RecycleBinEntityInfo> entities) {
		List<ListGridRecord> list = new ArrayList<ListGridRecord>();

		if (entities != null) {
			for (RecycleBinEntityInfo entity : entities) {
				ListGridRecord record = new ListGridRecord();
				record.setAttribute("name", entity.getName());
				record.setAttribute("dispName", entity.getDisplayName());
				if (isGetDataCount) {
					record.setAttribute("count", entity.getCount());
				} else {
					record.setAttribute("count", "-");
				}
				record.setAttribute("isError", entity.isError());
				record.setAttribute("errorMessage", entity.getErrorMessage());
				list.add(record);
			}
		}

		return list;
	}

}
