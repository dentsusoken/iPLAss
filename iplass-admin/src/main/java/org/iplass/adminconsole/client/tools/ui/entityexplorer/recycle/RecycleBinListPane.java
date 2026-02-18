package org.iplass.adminconsole.client.tools.ui.entityexplorer.recycle;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.GridActionImgButton;
import org.iplass.adminconsole.client.base.ui.widget.MessageTabSet;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataViewGridButton;
import org.iplass.adminconsole.client.base.ui.widget.MtpListGrid;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.tools.data.entityexplorer.RecycleBinEntityInfoDS;
import org.iplass.mtp.entity.definition.EntityDefinition;

import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.DataArrivedEvent;
import com.smartgwt.client.widgets.grid.events.DataArrivedHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class RecycleBinListPane extends VLayout {

	private static final String RESOURCE_PREFIX = "ui_tools_entityexplorer_RecycleBinListPane_";

	private static final String CLEAN_ICON = "[SKIN]/actions/remove.png";
	private static final String REFRESH_ICON = "[SKIN]/actions/refresh.png";
	private static final String ERROR_ICON = "[SKINIMG]/actions/exclamation.png";

	private DateItem purgeTargetDateItem;
	private CheckboxItem showCountItem;
	private Label countLabel;
	private ListGrid grid;

	private MessageTabSet messageTabSet;

	public RecycleBinListPane(RecycleBinMainPane mainPane) {
		// レイアウト設定
		setWidth100();
		setHeight100();

		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setWidth100();
		toolStrip.setMembersMargin(5);
		toolStrip.setAlign(VerticalAlignment.BOTTOM);

		purgeTargetDateItem = SmartGWTUtil.createDateItem();
		purgeTargetDateItem.setValue(new Timestamp(System.currentTimeMillis()));
		purgeTargetDateItem.setTitle(AdminClientMessageUtil.getString("ui_tools_entityexplorer_RecycleBinListPane_purgeTargetDate"));
		purgeTargetDateItem.setWrapTitle(false);
		toolStrip.addFormItem(purgeTargetDateItem);

		final ToolStripButton cleanButton = new ToolStripButton();
		cleanButton.setIcon(CLEAN_ICON);
		cleanButton.setTitle(AdminClientMessageUtil.getString("ui_tools_entityexplorer_RecycleBinListPane_clean"));
		cleanButton.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString(RESOURCE_PREFIX + "cleanTooltip")));
		cleanButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				clean();
			}
		});
		toolStrip.addButton(cleanButton);

		toolStrip.addSeparator();

		showCountItem = new CheckboxItem();
		showCountItem.setTitle(AdminClientMessageUtil.getString("ui_tools_entityexplorer_RecycleBinListPane_getDataCount"));
		showCountItem.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString(RESOURCE_PREFIX + "dataNumOften")));
		showCountItem.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				refreshGrid();
			}
		});
		toolStrip.addFormItem(showCountItem);

		toolStrip.addFill();

		countLabel = new Label();
		countLabel.setWrap(false);
		countLabel.setAutoWidth();
		setRecordCount(0);
		toolStrip.addMember(countLabel);

		final ToolStripButton refreshButton = new ToolStripButton();
		refreshButton.setIcon(REFRESH_ICON);
		refreshButton.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString(RESOURCE_PREFIX + "refreshList")));
		refreshButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				refreshGrid();
			}
		});
		toolStrip.addButton(refreshButton);

		grid = new MtpListGrid() {
			@Override
			protected Canvas createRecordComponent(final ListGridRecord record, Integer colNum) {
				final String fieldName = this.getFieldName(colNum);
				if ("explorerButton".equals(fieldName)) {
					if (!record.getAttributeAsBoolean(RecycleBinEntityInfoDS.FIELD_NAME.IS_ERROR.name())) {
						MetaDataViewGridButton button = new MetaDataViewGridButton(EntityDefinition.class.getName());
						button.setActionButtonPrompt(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString(RESOURCE_PREFIX + "showMetaDataEditScreen")));
						button.setMetaDataShowClickHandler(new MetaDataViewGridButton.MetaDataShowClickHandler() {
							@Override
							public String targetDefinitionName() {
								return record.getAttributeAsString(RecycleBinEntityInfoDS.FIELD_NAME.NAME.name());
							}
						});
						return button;
					}
				} else if ("error".equals(fieldName)) {
					if (record.getAttributeAsBoolean(RecycleBinEntityInfoDS.FIELD_NAME.IS_ERROR.name())) {
						record.setEnabled(false);
						GridActionImgButton recordCanvas = new GridActionImgButton();
						recordCanvas.setActionButtonSrc(ERROR_ICON);
						recordCanvas.setActionButtonPrompt(record.getAttributeAsString(RecycleBinEntityInfoDS.FIELD_NAME.ERROR_MESSAGE.name()));
						return recordCanvas;
					}
				}
				return null;
			}
		};

		grid.setWidth100();
		grid.setHeight100();

		//データ件数が多い場合を考慮し、false
		grid.setShowAllRecords(false);

		//行番号表示
		grid.setShowRowNumbers(true);

		// CheckBox選択設定
		grid.setSelectionType(SelectionStyle.SIMPLE);
		grid.setSelectionAppearance(SelectionAppearance.CHECKBOX);

		//ソートを許可
		grid.setCanSort(true);

		// この２つを指定することでcreateRecordComponentが有効
		grid.setShowRecordComponents(true);
		grid.setShowRecordComponentsByCell(true);

		grid.addDataArrivedHandler(new DataArrivedHandler() {

			@Override
			public void onDataArrived(DataArrivedEvent event) {
				setRecordCount(grid.getTotalRows());
				finishClean();
			}
		});
		grid.setShowResizeBar(true); // リサイズ可能
		grid.setResizeBarTarget("next"); // リサイズバーをダブルクリックした際、下を収縮

		messageTabSet = new MessageTabSet();
		messageTabSet.setHeight(120);

		addMember(toolStrip);
		addMember(grid);
		addMember(messageTabSet);

		refreshGrid();
	}

	public void refresh() {
		refreshGrid();
	}

	private void setRecordCount(long count) {
		countLabel.setContents("Total Count：" + count);
	}

	public void setRecordCount() {

	}

	public void startCallback() {
		startClean();
	}

	public void finishCallback() {
		finishClean();
	}

	public void executeStatusCallback(List<String> messages) {
		messageTabSet.addMessage(messages);
	}

	public void executeErrorCallback(List<String> messages) {
		messageTabSet.addErrorMessage(messages);
	}

	private void clean() {
		ListGridRecord[] records = grid.getSelectedRecords();
		if (records == null || records.length == 0) {
			SC.say(AdminClientMessageUtil.getString(RESOURCE_PREFIX + "selectEntityTarget"));
			return;
		}

		final List<String> defNames = new ArrayList<>();
		for (ListGridRecord record : records) {
			defNames.add(record.getAttributeAsString(RecycleBinEntityInfoDS.FIELD_NAME.NAME.name()));
		}

		Date purgeTimeDate = SmartGWTUtil.getDateTimeValue(purgeTargetDateItem.getValueAsDate(), null, false, "00:00", "00", "000");
		if (purgeTimeDate == null) {
			SC.say(AdminClientMessageUtil.getString(RESOURCE_PREFIX + "specifyPurgeTargetDate"));
			return;
		}
		final Timestamp ts = new Timestamp(purgeTimeDate.getTime());

		SC.ask(AdminClientMessageUtil.getString(RESOURCE_PREFIX + "confirmTitle"), AdminClientMessageUtil.getString(RESOURCE_PREFIX + "cleanConfirm"), new BooleanCallback() {

			@Override
			public void execute(Boolean value) {

				if (value) {
					RecycleBinCleanProgressDialog dialog = new RecycleBinCleanProgressDialog(RecycleBinListPane.this, defNames, ts);
					dialog.show();
				}
			}
		});
	}

	private void refreshGrid() {

		Date purgeTimeDate = SmartGWTUtil.getDateTimeValue(purgeTargetDateItem.getValueAsDate(), null, false, "00:00", "00", "000");
		if (purgeTimeDate == null) {
			SC.say(AdminClientMessageUtil.getString(RESOURCE_PREFIX + "specifyPurgeTargetDate"));
			return;
		}

		startClean();

		Timestamp ts = new Timestamp(purgeTimeDate.getTime());
		boolean isGetDataCount = showCountItem.getValueAsBoolean();
		RecycleBinEntityInfoDS ds = RecycleBinEntityInfoDS.getInstance(ts, isGetDataCount);
		grid.setDataSource(ds);

		// （参考）setFieldsは、setDataSource後に指定しないと効かない

		// ボタンを表示したいためListGridFieldを指定
		ListGridField explorerField = new ListGridField("explorerButton", " ");
		explorerField.setWidth(25);
		ListGridField errorField = new ListGridField("error", " ");
		errorField.setWidth(25);
		ListGridField nameField = new ListGridField(RecycleBinEntityInfoDS.FIELD_NAME.NAME.name(), "Name");
		ListGridField displayNameField = new ListGridField(RecycleBinEntityInfoDS.FIELD_NAME.DISPLAY_NAME.name(), "Display Name");
		ListGridField countField = new ListGridField(RecycleBinEntityInfoDS.FIELD_NAME.DATA_COUNT.name(), "Count");
		countField.setWidth(70);

		grid.setFields(explorerField, errorField, nameField, displayNameField, countField);

		grid.fetchData();
	}

	private void startClean() {
		if (messageTabSet != null) {
			messageTabSet.clearMessage();
			messageTabSet.setTabTitleProgress();
		}
	}

	private void finishClean() {
		if (messageTabSet != null) {
			messageTabSet.setTabTitleNormal();
		}
	}
}
