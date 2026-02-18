package org.iplass.adminconsole.client.tools.ui.entityexplorer.recycle;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.shared.tools.rpc.entityexplorer.EntityExplorerServiceAsync;
import org.iplass.adminconsole.shared.tools.rpc.entityexplorer.EntityExplorerServiceFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Progressbar;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

public class RecycleBinCleanProgressDialog extends Window {

	private static final String RESOURCE_PREFIX = "ui_tools_entityexplorer_RecycleBinListPane_";

	private static final EntityExplorerServiceAsync service = EntityExplorerServiceFactory.get();

	private RecycleBinListPane owner;

	private Label progressLabel;
	private Progressbar progress;

	public RecycleBinCleanProgressDialog(RecycleBinListPane owner, final List<String> defNames, final Timestamp ts) {
		this.owner = owner;

		setWidth(400);
		setHeight(150);
	setTitle(AdminClientMessageUtil.getString("ui_tools_entityexplorer_RecycleBinCleanProgressDialog_cleanRecycleBin"));
		setShowMinimizeButton(false);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();

		addCloseClickHandler(new CloseClickHandler() {
			@Override
			public void onCloseClick(CloseClickEvent event) {
				destroy();
			}
		});

		VLayout header = new VLayout(5);
		header.setMargin(5);
		header.setHeight(20);
		header.setWidth100();
		header.setAlign(VerticalAlignment.CENTER);

		Label title = new Label(AdminClientMessageUtil.getString(RESOURCE_PREFIX + "asyncExecuteLabel"));
		title.setHeight(20);
		title.setWidth100();
		header.addMember(title);

		VLayout horizontalBars = new VLayout(4);
		horizontalBars.setMargin(5);
		horizontalBars.setAutoHeight();
		horizontalBars.setWidth100();
		horizontalBars.setAlign(VerticalAlignment.CENTER);

		progressLabel = new Label("Progress");
		progressLabel.setHeight(16);
		horizontalBars.addMember(progressLabel);

		progress = new Progressbar();
		progress.setHeight(24);
		progress.setVertical(false);
		horizontalBars.addMember(progress);

		addItem(header);
		addItem(horizontalBars);

		startClean(defNames, ts);

	}

	private void startClean(List<String> defNames, Timestamp ts) {
		owner.startCallback();
		statusRefresh(defNames.size(), 0);
		cleanExecute(defNames, ts, 0);
	}

	private void cleanExecute(final List<String> defNames, final Timestamp ts, final int index) {

		String defName = defNames.get(index);

		service.cleanRecycleBin(TenantInfoHolder.getId(), defName, ts, new AsyncCallback<List<String>>() {

			@Override
			public void onSuccess(List<String> result) {
				if (result != null) {
					owner.executeStatusCallback(result);
				}

				int next = index + 1;
				if (defNames.size() > next) {
					statusRefresh(defNames.size(), next);
					cleanExecute(defNames, ts, next);
				} else {
					// 終了
					statusRefresh(defNames.size(), defNames.size());

					List<String> message = new ArrayList<String>();
					message.add("");
					message.add(
							AdminClientMessageUtil.getString(RESOURCE_PREFIX + "completedToCleanRecycleBin"));
					owner.executeStatusCallback(message);

					SC.say(AdminClientMessageUtil.getString(RESOURCE_PREFIX + "completed"),
							AdminClientMessageUtil.getString(RESOURCE_PREFIX + "completedToCleanRecycleBin"),
							new BooleanCallback() {
								@Override
								public void execute(Boolean value) {
									finishClean();
								}
							});
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				SC.say(AdminClientMessageUtil.getString(RESOURCE_PREFIX + "failed"),
						AdminClientMessageUtil.getString(RESOURCE_PREFIX + "failedToCleanRecycleBin"));

				GWT.log(caught.toString(), caught);

				List<String> message = new ArrayList<String>();
				message.add("");
				message.add(AdminClientMessageUtil.getString(RESOURCE_PREFIX + "failedToCleanRecycleBin"));
				message.add("Cause:" + caught.getMessage());
				owner.executeErrorCallback(message);

				finishClean();
			}

		});
	}

	private void statusRefresh(final int allCount, final int execCount) {
		int percent = (int) (((double) execCount / (double) allCount) * 100.0);

		GWT.log(percent + "% execute." + execCount + "/" + allCount);
		progressLabel.setContents("Progress:" + percent + "%");
		progress.setPercentDone(percent);
	}

	private void finishClean() {
		owner.finishCallback();
		destroy();
	}
}
