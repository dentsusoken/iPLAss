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

package org.iplass.adminconsole.client.tools.ui.metaexplorer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.io.download.PostDownloadFrame;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.ui.widget.GridActionImgButton;
import org.iplass.adminconsole.client.base.ui.widget.MtpListGrid;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.tools.data.metaexplorer.MetaDataTagDS;
import org.iplass.adminconsole.client.tools.data.metaexplorer.MetaDataTagDS.FIELD_NAME;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.ConfigDownloadProperty;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.NameListDownloadProperty;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.TargetMode;
import org.iplass.adminconsole.shared.tools.rpc.metaexplorer.MetaDataExplorerServiceAsync;
import org.iplass.adminconsole.shared.tools.rpc.metaexplorer.MetaDataExplorerServiceFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.DataArrivedEvent;
import com.smartgwt.client.widgets.grid.events.DataArrivedHandler;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class MetaDataTagSelectDialog extends AbstractWindow {

	private static final String DEL_ICON = "[SKINIMG]/MultiUploadItem/icon_remove_files.png";
	private static final String REFRESH_ICON = "[SKIN]/actions/refresh.png";
	private static final String EXPORT_XML_ICON = "[SKINIMG]/actions/download.png";
	private static final String EXPORT_NAME_LIST_ICON = "page_white_text.png";

	private Label countLabel;
	private ListGrid grid;

	public MetaDataTagSelectDialog(final MetaDataTagSelectedHandler handler) {
		this(handler, null);
	}

	public MetaDataTagSelectDialog(final MetaDataTagSelectedHandler handler, String description) {

		setWidth(700);
		setHeight(400);
		setTitle("Tag List");
		setShowMinimizeButton(false);
		setShowMaximizeButton(false);
		setCanDragResize(true);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();

		//------------------------
		//Title
		//------------------------
		Label title = null;
		if (description != null) {
			title = new Label(description);
			title.setMargin(5);
			title.setWidth100();
			title.setAutoHeight();
		}

		//------------------------
		//Grid
		//------------------------

		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setWidth100();
		toolStrip.setMembersMargin(5);
		toolStrip.setAlign(VerticalAlignment.BOTTOM);

		final ToolStripButton deleteButton = new ToolStripButton();
		deleteButton.setTitle("Delete");
		deleteButton.setIcon(DEL_ICON);
		deleteButton.setTooltip(SmartGWTUtil.getHoverString(getRS("deleteTag")));
		deleteButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				deleteTag();
			}
		});
		toolStrip.addButton(deleteButton);

		toolStrip.addFill();

		countLabel = new Label();
		countLabel.setWrap(false);
		countLabel.setAutoWidth();
		setRecordCount(0);
		toolStrip.addMember(countLabel);

		final ToolStripButton refreshButton = new ToolStripButton();
		refreshButton.setIcon(REFRESH_ICON);
		refreshButton.setTooltip(SmartGWTUtil.getHoverString(getRS("refreshList")));
		refreshButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				refreshGrid();
			}
		});
		toolStrip.addButton(refreshButton);

		grid = new MtpListGrid(){
			@Override
			protected Canvas createRecordComponent(final ListGridRecord record, Integer colNum) {
				String fieldName = this.getFieldName(colNum);
				if (fieldName.equals("downloadXML")) {

					GridActionImgButton recordCanvas = new GridActionImgButton();
					recordCanvas.setActionButtonSrc(EXPORT_XML_ICON);
					recordCanvas.setActionButtonPrompt(SmartGWTUtil.getHoverString(getRS("downloadAnXmlFile")));
					recordCanvas.addActionClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							String fileOid = record.getAttribute(FIELD_NAME.OID.name());
							downloadXMLFile(fileOid);
						}
					});

					return recordCanvas;
				} else if (fieldName.equals("downloadNameList")) {

					GridActionImgButton recordCanvas = new GridActionImgButton();
					recordCanvas.setActionButtonSrc(EXPORT_NAME_LIST_ICON);
					recordCanvas.setActionButtonPrompt(SmartGWTUtil.getHoverString(getRS("downloadNameListFile")));
					recordCanvas.addActionClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							String fileOid = record.getAttribute(FIELD_NAME.OID.name());
							downloadNameList(fileOid);
						}
					});

					return recordCanvas;
				}
				return null;
			}
		};

		grid.setWidth100();
		grid.setHeight100();

		//CheckBox選択設定
		grid.setSelectionType(SelectionStyle.SIMPLE);
		grid.setSelectionAppearance(SelectionAppearance.CHECKBOX);

		//ソートを許可
		grid.setCanSort(true);

		//この２つを指定することでcreateRecordComponentが有効
		grid.setShowRecordComponents(true);
		grid.setShowRecordComponentsByCell(true);

		grid.addDataArrivedHandler(new DataArrivedHandler() {

			@Override
			public void onDataArrived(DataArrivedEvent event) {
				setRecordCount(grid.getTotalRows());
			}
		});
		grid.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {

			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				String fileOid = event.getRecord().getAttribute(FIELD_NAME.OID.name());
				String name = event.getRecord().getAttribute(FIELD_NAME.NAME.name());
				Date createDate = event.getRecord().getAttributeAsDate(FIELD_NAME.CREATEDATE.name());

				handler.selected(fileOid, name, createDate);
				destroy();
			}

		});

		//------------------------
		//Footer Layout
		//------------------------

		IButton cancelButton = new IButton("Cancel");
		cancelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				destroy();
			}
		});

		HLayout footer = new HLayout(5);
		footer.setMargin(5);
		footer.setHeight(20);
		footer.setWidth100();
		//footer.setAlign(Alignment.LEFT);
		footer.setAlign(VerticalAlignment.CENTER);
		footer.setMembers(cancelButton);

		//------------------------
		//Main Layout
		//------------------------
		VLayout mainLayout = new VLayout();
		mainLayout.setWidth100();
		mainLayout.setHeight100();
		mainLayout.setMargin(10);

		if (title != null) {
			mainLayout.addMember(title);
		}
		mainLayout.addMember(toolStrip);
		mainLayout.addMember(grid);

		addItem(mainLayout);
		addItem(SmartGWTUtil.separator());
		addItem(footer);

		initialize();
	}

	private void initialize() {
		refreshGrid();
	}

	private void setRecordCount(long count) {
		countLabel.setContents("Count：" + count);
	}

	private void deleteTag() {

		final ListGridRecord[] records = grid.getSelectedRecords(true);
		if (records == null || records.length == 0) {
			SC.say(getRS("selectTagTarget"));
			return;
		}

		SC.ask(getRS("confirm"), getRS("deleteTagConf"), new BooleanCallback() {

			@Override
			public void execute(Boolean value) {
				List<String> list = new ArrayList<String>();
				for (ListGridRecord record : records) {
					list.add(record.getAttribute(FIELD_NAME.OID.name()));
				}

				MetaDataExplorerServiceAsync service = MetaDataExplorerServiceFactory.get();
				service.removeTag(TenantInfoHolder.getId(), list, new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						GWT.log(caught.toString(), caught);
						SC.warn(getRS("failedToDeleteTag") + caught.getMessage());
					}

					@Override
					public void onSuccess(Void result) {
						SC.say(getRS("completion"), getRS("tagDeleteComp"));
						refreshGrid();
					}

				});
			}
		});
	}

	private void downloadXMLFile(String fileOid) {
		PostDownloadFrame frame = new PostDownloadFrame();
		frame.setAction(GWT.getModuleBaseURL() + ConfigDownloadProperty.ACTION_URL)
			.addParameter(ConfigDownloadProperty.TENANT_ID, String.valueOf(TenantInfoHolder.getId()))
			.addParameter(ConfigDownloadProperty.TARGET_MODE, TargetMode.TAG.name())
			.addParameter(ConfigDownloadProperty.FILE_OID, fileOid)
			.execute();
	}

	private void downloadNameList(String fileOid) {

		PostDownloadFrame frame = new PostDownloadFrame();
		frame.setAction(GWT.getModuleBaseURL() + NameListDownloadProperty.ACTION_URL)
			.addParameter(NameListDownloadProperty.TENANT_ID, String.valueOf(TenantInfoHolder.getId()))
			.addParameter(NameListDownloadProperty.TARGET_MODE, TargetMode.TAG.name())
			.addParameter(NameListDownloadProperty.FILE_OID, fileOid)
			.execute();
	}

	private void refreshGrid() {
		MetaDataTagDS ds = MetaDataTagDS.getInstance();
		grid.setDataSource(ds);

		//（参考）setFieldsは、setDataSource後に指定しないと効かない

		//ボタンを表示したいためListGridFieldを指定
		ListGridField nameField = new ListGridField(FIELD_NAME.NAME.name(), "Tag Name");
		ListGridField createDateField = new ListGridField(FIELD_NAME.CREATEDATE_DISP.name(), "Create Date");
		createDateField.setWidth(130);
		ListGridField descriptionField = new ListGridField(FIELD_NAME.DESCRIPTION.name(), "Comment");
		descriptionField.setWrap(true);
		descriptionField.setShowHover(true);
		descriptionField.setHoverCustomizer(new HoverCustomizer() {
			@Override
			public String hoverHTML(Object value, ListGridRecord record,
					int rowNum, int colNum) {
				return SmartGWTUtil.getHoverString(record.getAttribute(FIELD_NAME.DESCRIPTION.name()));
			}
		});
		ListGridField downloadXMLField = new ListGridField("downloadXML", " ");
		downloadXMLField.setWidth(25);
		ListGridField downloadNameListField = new ListGridField("downloadNameList", " ");
		downloadNameListField.setWidth(25);

		grid.setFields(nameField, createDateField, descriptionField, downloadXMLField, downloadNameListField);

		grid.fetchData();
	}

	private String getRS(String key) {
		return AdminClientMessageUtil.getString("ui_tools_metaexplorer_MetaDataTagSelectDialog_" + key);
	}

	public interface MetaDataTagSelectedHandler {
		public void selected(String fileOid, String name, Date createDate);
	}

}
