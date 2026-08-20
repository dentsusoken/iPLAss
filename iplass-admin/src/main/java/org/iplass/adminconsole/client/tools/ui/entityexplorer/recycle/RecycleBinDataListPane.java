package org.iplass.adminconsole.client.tools.ui.entityexplorer.recycle;

import java.sql.Timestamp;

import org.iplass.adminconsole.client.base.ui.widget.MtpListGrid;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.tools.data.entityexplorer.RecycleBinDataInfoDS;

import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.DataArrivedEvent;
import com.smartgwt.client.widgets.grid.events.DataArrivedHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class RecycleBinDataListPane extends VLayout {

	private static final String REFRESH_ICON = "[SKIN]/actions/refresh.png";

	private RecycleBinMainPane mainPane;
	private String entityName;
	private Timestamp purgeTargetDate;
	private ListGrid grid;
	private Label countLabel;

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
		backButton.setTitle("Back");
		backButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				mainPane.backRecycleBinListPane();
			}
		});
		toolStrip.addButton(backButton);
		toolStrip.addSeparator();

		Label entityLabel = new Label("Entity: " + entityName + "  Purge target date: " + SmartGWTUtil.formatTimestamp(purgeTargetDate));
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
		refreshButton.setTooltip(SmartGWTUtil.getHoverString("Refresh recycle bin data list"));
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
		grid.setCanSort(true);
		grid.addDataArrivedHandler(new DataArrivedHandler() {
			@Override
			public void onDataArrived(DataArrivedEvent event) {
				setRecordCount(grid.getTotalRows());
			}
		});

		addMember(toolStrip);
		addMember(grid);

		refreshGrid();
	}

	private void setRecordCount(long count) {
		countLabel.setContents("Total Count: " + count);
	}

	private void refreshGrid() {
		RecycleBinDataInfoDS ds = RecycleBinDataInfoDS.getInstance(entityName, purgeTargetDate);
		grid.setDataSource(ds);

		ListGridField nameField = new ListGridField(RecycleBinDataInfoDS.FIELD_NAME.NAME.name(), "Name");
		ListGridField recycleDateField = new ListGridField(RecycleBinDataInfoDS.FIELD_NAME.RECYCLE_DATE.name(), "Deleted Date");
		grid.setFields(nameField, recycleDateField);
		grid.fetchData();
	}
}