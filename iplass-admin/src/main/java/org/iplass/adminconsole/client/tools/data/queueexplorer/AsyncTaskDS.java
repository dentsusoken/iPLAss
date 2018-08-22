/*
 * Copyright 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 */

package org.iplass.adminconsole.client.tools.data.queueexplorer;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.data.AbstractAdminDataSource;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.adminconsole.shared.tools.dto.queueexplorer.TaskSearchResultInfo;
import org.iplass.mtp.async.AsyncTaskInfoSearchCondtion;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class AsyncTaskDS extends AbstractAdminDataSource {

	private final MetaDataServiceAsync service = MetaDataServiceFactory.get();

	private AsyncTaskInfoSearchCondtion condition;

	public void setCondition(AsyncTaskInfoSearchCondtion condition) {
		this.condition = condition;
	}

	public AsyncTaskInfoSearchCondtion getCondition() {
		return condition;
	}

	/**
	 * <p>DataSourceを返します。</p>
	 *
	 * @return DataSource
	 */
	public static AsyncTaskDS getInstance() {
		return new AsyncTaskDS();
	}

	/**
	 * コンストラクタ
	 *
	 */
	private AsyncTaskDS() {
		DataSourceField taskId = new DataSourceTextField("taskId", "Task ID");
		DataSourceField groupingKey = new DataSourceTextField("groupingKey", "Grouping Key");
		DataSourceField status = new DataSourceTextField("status", "Status");
		DataSourceField retryCount = new DataSourceTextField("retryCount", "Retry Count");
		DataSourceField exceptionHandlingMode = new DataSourceTextField("exceptionHandlingMode", "Exception Handling Mode");
		setFields(taskId, groupingKey, status, retryCount, exceptionHandlingMode);
	}

	@Override
	protected void executeFetch(final String requestId, final DSRequest request,
			final DSResponse response) {

		debug("executeFetch ◆◆◆呼び出し.");

		if (condition == null) {
			//条件未指定は終了
			final long starttime = System.currentTimeMillis();
			processResponse(requestId, response);

			debug("executeFetch condition未指定のため終了. exec time -> " + (System.currentTimeMillis() - starttime) + "ms");
			return;
		}

		final long starttime = System.currentTimeMillis();

		service.searchAsyncTaskInfo(TenantInfoHolder.getId(), condition, new AsyncCallback<List<TaskSearchResultInfo>>() {

			@Override
			public void onSuccess(List<TaskSearchResultInfo> result) {
				debug("executeFetch searchTaskInfo実行完了. exec time -> " + (System.currentTimeMillis() - starttime) + "ms");

				final long starttime2 = System.currentTimeMillis();

				List<ListGridRecord> records = createRecord(result);
				response.setData(records.toArray(new ListGridRecord[]{}));
				response.setTotalRows(records.size());
				processResponse(requestId, response);

				debug("executeFetch searchTaskInfo結果からレコード作成完了. exec time -> " + (System.currentTimeMillis() - starttime2) + "ms");
			}

			@Override
			public void onFailure(Throwable caught) {
				GWT.log(caught.toString(), caught);

				response.setStatus(RPCResponse.STATUS_FAILURE);
				processResponse(requestId, response);

				debug("executeFetch searchTaskInfoでエラーが発生. exec time -> " + (System.currentTimeMillis() - starttime) + "ms");
			}
		});

	}

	/**
	 * レコードを作成します。
	 *
	 * @param request リクエスト情報
	 * @return {@link ListGridRecord} のリスト
	 */
	private List<ListGridRecord> createRecord(List<TaskSearchResultInfo> result) {

		List<ListGridRecord> list = new ArrayList<ListGridRecord>(result.size());

		for (TaskSearchResultInfo task : result) {
			ListGridRecord record = new ListGridRecord();
			record.setAttribute("taskId", task.getTaskId());
			record.setAttribute("groupingKey", task.getGroupingKey());
			record.setAttribute("status", task.getStatus().name());
			record.setAttribute("retryCount", task.getRetryCount());
			record.setAttribute("exceptionHandlingMode", task.getExceptionHandlingMode().name());
			list.add(record);
		}
		return list;
	}

	private void debug(String message) {
		GWT.log("AsyncTaskListDS DEBUG " + message);
	}

}
