package org.iplass.adminconsole.client.tools.ui.entityexplorer.recycle;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.MessageTabSet;
import org.iplass.adminconsole.client.base.ui.widget.MtpListGrid;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.tools.data.entityexplorer.RecycleBinDataInfoDS;
import org.iplass.adminconsole.shared.tools.rpc.entityexplorer.EntityExplorerServiceAsync;
import org.iplass.adminconsole.shared.tools.rpc.entityexplorer.EntityExplorerServiceFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.DataArrivedEvent;
import com.smartgwt.client.widgets.grid.events.DataArrivedHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * EntityExplorerのRecycleBinにおけるEntity別の削除データ一覧画面です。
 * <p>
 * 指定されたEntityの削除データを一覧表示し、選択したデータのリストアまたは完全削除を
 * 実行します。RecycleBinMainPaneがEntity選択時に生成し、一覧画面へ戻る際に破棄します。
 */
public class RecycleBinDataListPane extends VLayout {

	private static final String RESOURCE_PREFIX = "ui_tools_entityexplorer_RecycleBinDataListPane_";
	private static final String OPERATION_ERROR_PREFIX = "Failed to ";

	private static final String BACK_ICON = "[SKIN]/actions/back.png";
	private static final String CLEAR_ICON = "[SKIN]/actions/remove.png";
	private static final String RESTORE_ICON = "[SKIN]/actions/undo.png";
	private static final String REFRESH_ICON = "[SKIN]/actions/refresh.png";
	private static final EntityExplorerServiceAsync service = EntityExplorerServiceFactory.get();

	private final RecycleBinMainPane mainPane;
	private final String entityName;
	private final Timestamp purgeTargetDate;
	private final ListGrid grid;
	private final Label countLabel;
	private final MessageTabSet messageTabSet;

	public RecycleBinDataListPane(RecycleBinMainPane mainPane, String entityName, Timestamp purgeTargetDate) {
		this.mainPane = mainPane;
		this.entityName = entityName;
		this.purgeTargetDate = purgeTargetDate;

		setWidth100();
		setHeight100();

		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setWidth100();
		toolStrip.setMembersMargin(5);
		toolStrip.setAlign(VerticalAlignment.BOTTOM);

		ToolStripButton backButton = new ToolStripButton();
		backButton.setIcon(BACK_ICON);
		backButton.setTitle("Back");
		backButton.setTooltip(SmartGWTUtil.getHoverString(getResourceString("backTooltip")));
		backButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				mainPane.backRecycleBinListPane();
			}
		});
		toolStrip.addButton(backButton);
		toolStrip.addSeparator();

		ToolStripButton restoreButton = new ToolStripButton();
		restoreButton.setIcon(RESTORE_ICON);
		restoreButton.setTitle("Restore");
		restoreButton.setTooltip(SmartGWTUtil.getHoverString(getResourceString("restoreTooltip")));
		restoreButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				restore();
			}
		});
		toolStrip.addButton(restoreButton);

		ToolStripButton clearButton = new ToolStripButton();
		clearButton.setIcon(CLEAR_ICON);
		clearButton.setTitle("Clean");
		clearButton.setTooltip(SmartGWTUtil.getHoverString(getResourceString("clearTooltip")));
		clearButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				clearSelectedData();
			}
		});
		toolStrip.addButton(clearButton);
		toolStrip.addSeparator();

		Label entityLabel = new Label("Entity: " + entityName + "  Purge target date: "
				+ SmartGWTUtil.formatTimestamp(purgeTargetDate));
		entityLabel.setAutoWidth();
		entityLabel.setWrap(false);
		toolStrip.addMember(entityLabel);
		toolStrip.addFill();

		countLabel = new Label();
		countLabel.setWrap(false);
		countLabel.setAutoWidth();
		setRecordCount(0);
		toolStrip.addMember(countLabel);

		ToolStripButton refreshButton = new ToolStripButton();
		refreshButton.setIcon(REFRESH_ICON);
		refreshButton.setTooltip(SmartGWTUtil.getHoverString(getResourceString("refreshTooltip")));
		refreshButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				refreshGrid();
			}
		});
		toolStrip.addButton(refreshButton);

		grid = new MtpListGrid();
		grid.setWidth100();
		grid.setHeight100();
		grid.setShowAllRecords(false);
		grid.setShowRowNumbers(true);
		grid.setSelectionType(SelectionStyle.SIMPLE);
		grid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		grid.setCanSort(true);
		grid.addDataArrivedHandler(new DataArrivedHandler() {
			@Override
			public void onDataArrived(DataArrivedEvent event) {
				setRecordCount(grid.getTotalRows());
			}
		});

		messageTabSet = new MessageTabSet();
		messageTabSet.setHeight(120);

		grid.setShowResizeBar(true);
		grid.setResizeBarTarget("next");

		addMember(toolStrip);
		addMember(grid);
		addMember(messageTabSet);

		refreshGrid();
	}

	private void setRecordCount(long count) {
		countLabel.setContents("Total Count：" + count);
	}

	private void refreshGrid() {
		grid.setDataSource(RecycleBinDataInfoDS.getInstance(entityName, purgeTargetDate));

		grid.setFields(
				new ListGridField(RecycleBinDataInfoDS.FIELD_NAME.NAME.name(), "Name"),
				new ListGridField(RecycleBinDataInfoDS.FIELD_NAME.RECYCLE_DATE.name(), "Deleted Date"));
		grid.fetchData();
	}

	private void restore() {
		final List<Long> recycleBinIds = getSelectedRecycleBinIds();
		if (recycleBinIds.isEmpty()) {
			SC.say(getResourceString("selectTargetData"));
			return;
		}

		SC.ask(getResourceString("confirmTitle"),
				getResourceString("restoreConfirm"), new BooleanCallback() {
					@Override
					public void execute(Boolean value) {
						if (value) {
							executeRestore(recycleBinIds);
						}
					}
				});
	}

	private void clearSelectedData() {
		final List<Long> recycleBinIds = getSelectedRecycleBinIds();
		if (recycleBinIds.isEmpty()) {
			SC.say(getResourceString("selectTargetData"));
			return;
		}

		SC.ask(getResourceString("confirmTitle"),
				getResourceString("clearConfirm"), new BooleanCallback() {
					@Override
					public void execute(Boolean value) {
						if (value) {
							executeClear(recycleBinIds);
						}
					}
				});
	}

	private List<Long> getSelectedRecycleBinIds() {
		List<Long> recycleBinIds = new ArrayList<>();
		for (ListGridRecord record : grid.getSelectedRecords(true)) {
			recycleBinIds.add(record.getAttributeAsLong(RecycleBinDataInfoDS.FIELD_NAME.RECYCLE_BIN_ID.name()));
		}
		return recycleBinIds;
	}

	private interface RecycleBinDataOperation {
		void execute(List<Long> recycleBinIds, AsyncCallback<List<String>> callback);
	}

	private void executeRestore(final List<Long> recycleBinIds) {
		executeRecycleBinDataOperation(recycleBinIds, new RecycleBinDataOperation() {
			@Override
			public void execute(List<Long> recycleBinIds, AsyncCallback<List<String>> callback) {
				service.restoreRecycleBinData(TenantInfoHolder.getId(), entityName, recycleBinIds, callback);
			}
		}, "restoring", "restoreFailed", "Failed to restore recycle bin data.");
	}

	private void executeClear(final List<Long> recycleBinIds) {
		executeRecycleBinDataOperation(recycleBinIds, new RecycleBinDataOperation() {
			@Override
			public void execute(List<Long> recycleBinIds, AsyncCallback<List<String>> callback) {
				service.purgeRecycleBinData(TenantInfoHolder.getId(), entityName, recycleBinIds, callback);
			}
		}, "clearing", "clearFailed", "Failed to clear recycle bin data.");
	}

	private void executeRecycleBinDataOperation(final List<Long> recycleBinIds,
			RecycleBinDataOperation operation, final String progressResourceSuffix,
			final String failureResourceSuffix, final String failureLogMessage) {
		startExecute();
		SmartGWTUtil.showProgress(getResourceString(progressResourceSuffix));
		operation.execute(recycleBinIds, new AsyncCallback<List<String>>() {
			@Override
			public void onFailure(Throwable caught) {
				SmartGWTUtil.hideProgress();
				GWT.log(failureLogMessage, caught);
				List<String> messages = new ArrayList<>();
				messages.add(getResourceString(failureResourceSuffix));
				messages.add("Cause:" + caught.getMessage());
				executeErrorCallback(messages);
				finishExecute();
				SC.say(getResourceString("failed"), getResourceString(failureResourceSuffix));
			}

			@Override
			public void onSuccess(List<String> result) {
				SmartGWTUtil.hideProgress();
				refreshGrid();
				if (hasOperationError(result)) {
					executeErrorCallback(result);
					finishExecute();
					SC.say(getResourceString("failed"),
							formatOperationMessages(result));
					return;
				}
				executeStatusCallback(createCompletedMessages(result));
				finishExecute();
				SC.say(getResourceString("completed"));
			}
		});
	}

	private void startExecute() {
		messageTabSet.clearMessage();
		messageTabSet.setTabTitleProgress();
	}

	private void finishExecute() {
		messageTabSet.setTabTitleNormal();
	}

	private void executeStatusCallback(List<String> messages) {
		messageTabSet.addMessage(messages);
	}

	private void executeErrorCallback(List<String> messages) {
		messageTabSet.addErrorMessage(messages);
	}

	private boolean hasOperationError(List<String> result) {
		if (result == null) {
			return false;
		}
		for (String message : result) {
			if (message != null && message.startsWith(OPERATION_ERROR_PREFIX)) {
				return true;
			}
		}
		return false;
	}

	private List<String> createCompletedMessages(List<String> result) {
		List<String> messages = new ArrayList<>();
		messages.add(getResourceString("completed"));
		if (result == null || result.isEmpty()) {
			messages.add(getResourceString("noDataProcessed"));
		} else {
			messages.addAll(result);
		}
		return messages;
	}

	private String formatOperationMessages(List<String> messages) {
		if (messages == null || messages.isEmpty()) {
			return getResourceString("noDataProcessed");
		}

		StringBuilder result = new StringBuilder();
		for (String message : messages) {
			if (result.length() > 0) {
				result.append("<br/>");
			}
			result.append(message);
		}
		return result.toString();
	}

	private static String getResourceString(String key) {
		return AdminClientMessageUtil.getString(RESOURCE_PREFIX + key);
	}
}
