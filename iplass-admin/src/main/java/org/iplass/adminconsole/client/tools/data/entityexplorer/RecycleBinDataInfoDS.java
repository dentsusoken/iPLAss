package org.iplass.adminconsole.client.tools.data.entityexplorer;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.data.AbstractAdminDataSource;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.tools.dto.entityexplorer.RecycleBinDataInfo;
import org.iplass.adminconsole.shared.tools.rpc.entityexplorer.EntityExplorerServiceAsync;
import org.iplass.adminconsole.shared.tools.rpc.entityexplorer.EntityExplorerServiceFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * EntityExplorerのRecycleBinに登録された削除データを取得するDataSourceです。
 * <p>
 * Entity名とPurge対象日時を指定して生成し、Fetch要求ごとに対象Entityの削除データを
 * サーバから取得します。追加・更新・削除はサポートしません。
 */
public class RecycleBinDataInfoDS extends AbstractAdminDataSource {

	private static final EntityExplorerServiceAsync service = EntityExplorerServiceFactory.get();

	public enum FIELD_NAME {
		RECYCLE_BIN_ID,
		NAME,
		RECYCLE_DATE,
	}

	public static RecycleBinDataInfoDS getInstance(String entityName, Timestamp purgeTargetDate) {
		return new RecycleBinDataInfoDS(entityName, purgeTargetDate);
	}

	private final String entityName;
	private final Timestamp purgeTargetDate;

	private RecycleBinDataInfoDS(String entityName, Timestamp purgeTargetDate) {
		this.entityName = entityName;
		this.purgeTargetDate = purgeTargetDate;

		setFields(new DataSourceTextField(FIELD_NAME.RECYCLE_BIN_ID.name()).setPrimaryKey(true),
				new DataSourceTextField(FIELD_NAME.NAME.name()),
				new DataSourceTextField(FIELD_NAME.RECYCLE_DATE.name()));
	}

	@Override
	protected void executeFetch(final String requestId, final DSRequest request, final DSResponse response) {
		service.getRecycleBinDataList(TenantInfoHolder.getId(), entityName, purgeTargetDate, new AsyncCallback<List<RecycleBinDataInfo>>() {

			@Override
			public void onSuccess(List<RecycleBinDataInfo> dataList) {
				List<ListGridRecord> records = createRecord(dataList);
				response.setData(records.toArray(new ListGridRecord[] {}));
				response.setTotalRows(records.size());
				response.setStartRow(0);
				processResponse(requestId, response);
			}

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("error!!!", caught);
				SC.warn(AdminClientMessageUtil.getString("datasource_tools_entityexplorer_SimpleEntityInfoDS_failedToGetEntityList")
						+ caught.getMessage());
				response.setStatus(RPCResponse.STATUS_FAILURE);
				processResponse(requestId, response);
			}
		});
	}

	@Override
	protected void executeAdd(String requestId, DSRequest request, DSResponse response) {
	}

	@Override
	protected void executeUpdate(String requestId, DSRequest request, DSResponse response) {
	}

	@Override
	protected void executeRemove(String requestId, DSRequest request, DSResponse response) {
	}

	private List<ListGridRecord> createRecord(List<RecycleBinDataInfo> dataList) {
		List<ListGridRecord> records = new ArrayList<>();
		if (dataList != null) {
			for (RecycleBinDataInfo data : dataList) {
				ListGridRecord record = new ListGridRecord();
				record.setAttribute(FIELD_NAME.RECYCLE_BIN_ID.name(), data.getRecycleBinId());
				record.setAttribute(FIELD_NAME.NAME.name(), data.getName());
				if (data.getRecycleDate() != null) {
					record.setAttribute(FIELD_NAME.RECYCLE_DATE.name(), SmartGWTUtil.formatTimestamp(data.getRecycleDate()));
				}
				records.add(record);
			}
		}
		return records;
	}
}
