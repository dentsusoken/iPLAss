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

package org.iplass.adminconsole.client.tools.ui.queueexplorer;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.MessageTabSet;
import org.iplass.adminconsole.client.base.ui.widget.MtpListGrid;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.tools.data.queueexplorer.AsyncTaskDS;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.adminconsole.shared.tools.dto.queueexplorer.CancelResult;
import org.iplass.adminconsole.shared.tools.dto.queueexplorer.TaskCancelResultInfo;
import org.iplass.adminconsole.shared.tools.dto.queueexplorer.TaskForceDeleteResultInfo;
import org.iplass.adminconsole.shared.tools.dto.queueexplorer.TaskLoadResultInfo;
import org.iplass.mtp.async.AsyncTaskInfoSearchCondtion;
import org.iplass.mtp.async.TaskStatus;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.types.AutoFitWidthApproach;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.DataArrivedEvent;
import com.smartgwt.client.widgets.grid.events.DataArrivedHandler;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class QueueInfoPane extends VLayout {

	private final MetaDataServiceAsync service = MetaDataServiceFactory.get();

	private QueueGridPane gridPane;

	private MessageTabSet messageTabSet;

	public QueueInfoPane() {
		setWidth100();
		setHeight100();

		gridPane = new QueueGridPane();
		gridPane.setShowResizeBar(true);		//リサイズ可能
		gridPane.setResizeBarTarget("next");	//リサイズバーをダブルクリックした際、下を収縮

		messageTabSet = new MessageTabSet();
		messageTabSet.setHeight(120);

		addMember(gridPane);
		addMember(messageTabSet);

		initializeData();
	}

	public void setQueueName(String queueName) {
		gridPane.setQueue(queueName);
	}

	private void initializeData() {
		gridPane.refreshGrid();
	}

	private void startExecute() {
		messageTabSet.clearMessage();
		messageTabSet.setTabTitleProgress();
	}

	private void finishExecute() {
		messageTabSet.setTabTitleNormal();
	}

	public class QueueGridPane extends VLayout {

		private static final int LIMIT = 30;

		private static final String CANCEL_ICON = "cancel.png";
		private static final String DELETE_ICON = "error_delete.png";


		private CheckboxItem showHistory;
		private Label pageNumLabel;
		private int pageNum;

		private ListGrid grid;

		private AsyncTaskDS ds;

		private String queueName;

		public QueueGridPane() {

			//------------------------
			//Toolbar
			//------------------------
			ToolStrip toolStrip = new ToolStrip();
			toolStrip.setWidth100();
			toolStrip.setMembersMargin(5);
			toolStrip.setAlign(VerticalAlignment.BOTTOM);

			//------------------------
			//Cancel Task
			//------------------------

			final ToolStripButton taskCancel = new ToolStripButton();
			taskCancel.setIcon(CANCEL_ICON);
			taskCancel.setTitle("Cancel Task");
			taskCancel.setTooltip(SmartGWTUtil.getHoverString(getResString("cancelTaskHint")));
			taskCancel.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					cancelTask();
				}
			});
			toolStrip.addButton(taskCancel);

			//------------------------
			//Delete Task
			//------------------------

			final ToolStripButton taskDelete = new ToolStripButton();
			taskDelete.setIcon(DELETE_ICON);
			taskDelete.setTitle("Delete Task");
			taskDelete.setTooltip(SmartGWTUtil.getHoverString(getResString("deleteTaskHint")));
			taskDelete.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					deleteTask();
				}
			});
			toolStrip.addButton(taskDelete);

			toolStrip.addSeparator();

			//------------------------
			//Show History
			//------------------------
			showHistory = new CheckboxItem();
			showHistory.setTitle("show with history");
			showHistory.setTooltip(SmartGWTUtil.getHoverString(getResString("showWithHistoryHint")));
			showHistory.addChangedHandler(new ChangedHandler() {

				@Override
				public void onChanged(ChangedEvent event) {
					refreshGrid();
				}
			});
			toolStrip.addFormItem(showHistory);

			toolStrip.addFill();

			//------------------------
			//Paging
			//------------------------
			final ToolStripButton firstButton = new ToolStripButton();
			firstButton.setIcon("resultset_first.png");
			firstButton.setTitle("First");
			firstButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					doFirst();
				}
			});
			toolStrip.addButton(firstButton);

			final ToolStripButton prevButton = new ToolStripButton();
			prevButton.setIcon("resultset_previous.png");
			prevButton.setTitle("Prev");
			prevButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					doPrev();
				}
			});
			toolStrip.addButton(prevButton);

			final ToolStripButton nextButton = new ToolStripButton();
			nextButton.setIcon("resultset_next.png");
			nextButton.setTitle("Next");
			nextButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					doNext();
				}
			});
			toolStrip.addButton(nextButton);

			toolStrip.addSeparator();

			pageNumLabel = new Label();
			pageNumLabel.setWrap(false);
			pageNumLabel.setAutoWidth();
			toolStrip.addMember(pageNumLabel);

			Label pageLabel = new Label();
			pageLabel.setWrap(false);
			pageLabel.setAutoWidth();
			pageLabel.setContents("Page");
			toolStrip.addMember(pageLabel);

			toolStrip.addSpacer(5);

			//------------------------
			//Grid
			//------------------------
			grid = new MtpListGrid() {
				@Override
				protected String getBaseStyle(ListGridRecord record, int rowNum, int colNum) {
					String status = record.getAttribute("status");
					if (TaskStatus.SUBMITTED.name().equals(status)
							|| TaskStatus.EXECUTING.name().equals(status)) {
						return "taskActiveGridRow";
					} else if (TaskStatus.ABORTED.name().equals(status)) {
						return "taskAbortGridRow";
					} else if (TaskStatus.COMPLETED.name().equals(status)
									|| TaskStatus.RETURNED.name().equals(status)) {
						return "taskCompletedGridRow";
					} else {
						return super.getBaseStyle(record, rowNum, colNum);
					}
				}
			};

			grid.setWidth100();
			grid.setHeight100();

			//データ件数が多い場合を考慮し、false
			grid.setShowAllRecords(false);
			//列数が多い場合を考慮し、false
			grid.setShowAllColumns(false);

			//列幅自動調節（タイトルに設定）
			grid.setAutoFitFieldWidths(true);
			grid.setAutoFitWidthApproach(AutoFitWidthApproach.TITLE);
			//幅が足りないときに先頭行を自動的に伸ばさない
			grid.setAutoFitFieldsFillViewport(false);

			//行番号表示
			grid.setShowRowNumbers(true);

			//CheckBox選択設定
			grid.setSelectionType(SelectionStyle.SIMPLE);
			grid.setSelectionAppearance(SelectionAppearance.CHECKBOX);

			grid.addDataArrivedHandler(new DataArrivedHandler() {

				@Override
				public void onDataArrived(DataArrivedEvent event) {
					if (ds.getCondition() != null) {
						messageTabSet.addMessage("With history : " + ds.getCondition().isWithHistory()
								+ ", Limit : limit = " + ds.getCondition().getLimit() + ", offset = " + ds.getCondition().getOffset());
						messageTabSet.addMessage("Count : " + grid.getRecords().length);
					}
					finishExecute();
				}

			});

			//データ編集画面表示
			grid.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {

				@Override
				public void onRecordDoubleClick(RecordDoubleClickEvent event) {
					//FIXME 詳細結果を表示するか？
					long taskId = event.getRecord().getAttributeAsLong("taskId");
					loadDetail(taskId);
				}
			});

			//明示的にFetchする
			grid.setAutoFetchData(false);


			addMember(toolStrip);
			addMember(grid);
		}

		public void refreshGrid() {
			refreshGrid(true);
		}

		private void refreshGrid(boolean isMessageClear) {
			if (this.ds == null) {
				ds = AsyncTaskDS.getInstance();
				grid.setDataSource(ds);

				ListGridField taskId = new ListGridField("taskId", "Task ID");
				taskId.setWidth(70);
				taskId.setCanEdit(false);
				ListGridField groupingKey = new ListGridField("groupingKey", "Grouping Key");
				groupingKey.setWidth(150);
				groupingKey.setCanEdit(false);
				ListGridField status = new ListGridField("status", "Status");
				status.setWidth(120);
				status.setCanEdit(false);
				ListGridField retryCount = new ListGridField("retryCount", "Retry Count");
				retryCount.setWidth(70);
				retryCount.setCanEdit(false);
				ListGridField exceptionHandlingMode = new ListGridField("exceptionHandlingMode", "Exception Handling Mode");
				exceptionHandlingMode.setWidth(120);
				exceptionHandlingMode.setCanEdit(false);

				grid.setFields(taskId, groupingKey, status, retryCount, exceptionHandlingMode);

			}

			//Pageの初期化
			setPageNum(1);

			doFetch(isMessageClear);
		}

		public void setQueue(String queueName) {
			this.queueName = queueName;

			refreshGrid();
		}

		private void setPageNum(int page) {
			pageNum = page;
			pageNumLabel.setContents(pageNum + "");
		}

		private int getPageNum() {
			return pageNum;
		}

		private void doFirst() {
			int page = getPageNum();
			if (page == 1) {
				return;
			}
			setPageNum(1);

			doFetch(true);
		}

		private void doPrev() {
			int page = getPageNum();
			if (page == 1) {
				return;
			}
			setPageNum(page - 1);

			doFetch(true);
		}

		private void doNext() {
			int page = getPageNum();
			setPageNum(page + 1);

			doFetch(true);
		}

		private void doFetch(boolean isMessageClear) {
			if (isMessageClear) {
				startExecute();
			}

			if (SmartGWTUtil.isEmpty(queueName)) {
				ds.setCondition(null);
			} else {
				AsyncTaskInfoSearchCondtion condition = new AsyncTaskInfoSearchCondtion();
				condition.setQueue(queueName);
				condition.setWithHistory(SmartGWTUtil.getBooleanValue(showHistory));
				condition.setOffset((getPageNum() - 1) * LIMIT);
				condition.setLimit(LIMIT);

				ds.setCondition(condition);
			}

			//条件を変えないとfetchが実行されないので設定
			Criteria criteria = new Criteria();
			criteria.addCriteria("dummy", System.currentTimeMillis() + "");

			grid.fetchData(criteria);
		}

		private void cancelTask() {
			final List<Long> canCancelTaskIdList = new ArrayList<Long>();

			//選択データをチェック
			for (ListGridRecord record : grid.getSelectedRecords()) {
				//削除用に他のステータスも選択可能なため、キャンセル可能なもののみ抽出
				String status = record.getAttribute("status");
				if (TaskStatus.SUBMITTED.name().equals(status)
						|| TaskStatus.EXECUTING.name().equals(status)) {
					long taskId = record.getAttributeAsLong("taskId");
					canCancelTaskIdList.add(taskId);
				}
			}

			if (canCancelTaskIdList.size() == 0) {
				SC.warn(getResString("selectTask"));
				return;
			}

			SC.ask(getResString("confirm"), getResString("startCancelTaskConfirm"), new BooleanCallback() {

				@Override
				public void execute(Boolean value) {
					if (value) {
						startExecute();
						service.cancelAsyncTask(TenantInfoHolder.getId(), queueName, canCancelTaskIdList, new AsyncCallback<TaskCancelResultInfo>() {

							@Override
							public void onSuccess(TaskCancelResultInfo info) {

								messageTabSet.addMessage(getResString("completeCancelTask"));
								messageTabSet.addMessage("DETAIL:");
								for (CancelResult result : info.getResultList()) {
									messageTabSet.addMessage("Task[" + result.getTaskId() + "] cancel result = " + result.isCanceled() + "."
											+ " ,before status = " + (result.getBeforeStatus() != null ? result.getBeforeStatus() :"null")
											+ " ,after status = " + (result.getResultStatus() != null ? result.getResultStatus() :"null"));
								}
								messageTabSet.addMessage("-------------------------------------------");

								finishExecute();

								SC.say(getResString("completeCancelTask"));

								refreshGrid(false);
							}

							@Override
							public void onFailure(Throwable caught) {
								GWT.log(caught.toString(), caught);

								messageTabSet.addErrorMessage(getResString("errCancelTask") + caught.toString());

								finishExecute();

								SC.warn(getResString("errCancelTask") + caught.toString());
							}
						});
					}
				}

			});
		}

		private void deleteTask() {
			final List<Long> deleteTaskIdList = new ArrayList<Long>();

			//選択データをチェック
			for (ListGridRecord record : grid.getSelectedRecords()) {
				String status = record.getAttribute("status");
				if (!TaskStatus.COMPLETED.name().equals(status)) {
					long taskId = record.getAttributeAsLong("taskId");
					deleteTaskIdList.add(taskId);
				}
			}

			if (deleteTaskIdList.size() == 0) {
				SC.warn(getResString("selectDeleteTask"));
				return;
			}

			SC.ask(getResString("confirm"), getResString("startDeleteTaskConfirm"), new BooleanCallback() {

				@Override
				public void execute(Boolean value) {
					if (value) {
						startExecute();
						service.forceDeleteAsyncTask(TenantInfoHolder.getId(), queueName, deleteTaskIdList, new AsyncCallback<TaskForceDeleteResultInfo>() {

							@Override
							public void onSuccess(TaskForceDeleteResultInfo info) {

								messageTabSet.addMessage(getResString("completeDeleteTask"));
								messageTabSet.addMessage("DETAIL:");
								for (Long taskId : info.getTaskList()) {
									messageTabSet.addMessage("Task[" + taskId + "] deleted.");
								}
								messageTabSet.addMessage("-------------------------------------------");

								finishExecute();

								SC.say(getResString("completeDeleteTask"));

								refreshGrid(false);
							}

							@Override
							public void onFailure(Throwable caught) {
								GWT.log(caught.toString(), caught);

								messageTabSet.addErrorMessage(getResString("errDeleteTask") + caught.toString());

								finishExecute();

								SC.warn(getResString("errDeleteTask") + caught.toString());
							}
						});
					}
				}

			});
		}

		private void loadDetail(long taskId) {

			startExecute();
			service.loadAsyncTaskInfo(TenantInfoHolder.getId(), queueName, taskId, new AsyncCallback<TaskLoadResultInfo>() {

				@Override
				public void onSuccess(TaskLoadResultInfo result) {
					messageTabSet.addMessage("TASK DETAIL:");
					messageTabSet.addMessage("Task[" + result.getTaskId() + "]"
							+ ", Grouping key = " + result.getGroupingKey());
					messageTabSet.addMessage("Status = " + result.getStatus()
							+ ", RetryCount = " + result.getRetryCount()
							+ ", Task Info = " + result.getTaskInfo()
							+ ", Result Info = " + result.getResultInfo());

					finishExecute();
				}

				@Override
				public void onFailure(Throwable caught) {
					GWT.log(caught.toString(), caught);

					messageTabSet.addErrorMessage(getResString("errLoadTask") + caught.toString());

					finishExecute();

					SC.warn(getResString("errLoadTask") + caught.toString());
				}
			});
		}
	}

	private static final String RES_PREFIX = "ui_tools_queueexplorer_QueueInfoPane_";
	private String getResString(String key) {
		return AdminClientMessageUtil.getString(RES_PREFIX + key);
	}
}
