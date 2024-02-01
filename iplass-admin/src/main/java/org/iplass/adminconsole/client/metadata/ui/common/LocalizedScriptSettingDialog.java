/*
 * Copyright 2014 DENTSU SOKEN INC. All Rights Reserved.
 */

package org.iplass.adminconsole.client.metadata.ui.common;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogHandler;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogMode;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.MetaDataUtil;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.definition.LocalizedStringDefinition;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;

public class LocalizedScriptSettingDialog extends MtpDialog {

	private LocaleListPane localeListPane;
	private String scriptTitle;
	private String scriptHintKey;

	private List<LocalizedStringDefinition> localizedStringList;

	public LocalizedScriptSettingDialog(List<LocalizedStringDefinition> localizedStringList, String scriptTitle, String scriptHintKey) {

		this.localizedStringList = localizedStringList;
		this.scriptTitle = scriptTitle;
		this.scriptHintKey = scriptHintKey;

		setTitle("Multilingual Script Dialog");
		setHeight(420);
		centerInPage();

		localeListPane = new LocaleListPane();

		container.addMember(localeListPane);

		IButton save = new IButton("Save");
		save.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				ok();
			}
		});

		IButton cancel = new IButton("Cancel");
		cancel.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				destroy();
			}
		});

		footer.setMembers(save, cancel);
	}

	private void ok() {

		ListGridRecord[] records = localeListPane.getLocaleRecords();

		localizedStringList.clear();
		for (Record record : records) {
			String localeName = record.getAttribute("language");
			String stringValue = record.getAttribute("script");

			if (!SmartGWTUtil.isEmpty(localeName) && !SmartGWTUtil.isEmpty(stringValue)) {
				LocalizedStringDefinition lsd = new LocalizedStringDefinition();
				lsd.setLocaleName(localeName);
				lsd.setStringValue(stringValue);

				localizedStringList.add(lsd);
			}
		}

		destroy();
	}

	private class LocaleListPane extends HLayout {

		private ListGrid grid;

		public LocaleListPane() {
			setHeight100();
			setWidth100();

			grid = new ListGrid();
			grid.setWidth100();
			grid.setHeight100();

			ListGridField languageField = new ListGridField("langDisp", "Language");
			ListGridField hasCustomField = new ListGridField("custom", "Custom");
			hasCustomField.setWidth(70);
			hasCustomField.setAlign(Alignment.CENTER);
			grid.setFields(languageField, hasCustomField);

			grid.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {

				@Override
				public void onRecordDoubleClick(RecordDoubleClickEvent event) {
					final int selectRow = event.getRecordNum();
					final ListGridRecord record = grid.getRecord(selectRow);

					MetaDataUtil.showScriptEditDialog(ScriptEditorDialogMode.JSP,
							record.getAttribute("script"),
							scriptTitle,
							scriptHintKey,
							null,
							new ScriptEditorDialogHandler() {

								@Override
								public void onSave(String text) {
									if (!SmartGWTUtil.isEmpty(text)) {
										record.setAttribute("script", text);
										record.setAttribute("custom", "Y");
									} else {
										record.setAttribute("script", "");
										record.setAttribute("custom", "");
									}
									grid.refreshRow(selectRow);
								}
								@Override
								public void onCancel() {
								}
							});
				}
			});

			ImgButton removeButton = new ImgButton();
			removeButton.setSrc("[SKIN]actions/remove.png");
			removeButton.setSize(16);
			removeButton.setShowFocused(false);
			removeButton.setShowRollOver(false);
			removeButton.setShowDown(false);
			removeButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
				public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
					for (ListGridRecord record : grid.getSelectedRecords()) {
						record.setAttribute("script", "");
						record.setAttribute("custom", "");
					}
					grid.refreshFields();
				}
			});

			SectionStack sectionStack = new SectionStack();

			SectionStackSection section1 = new SectionStackSection();
			section1.setTitle("Language List");
			section1.setItems(grid);
			section1.setControls(removeButton);
			section1.setExpanded(true);
			section1.setCanCollapse(false);

			sectionStack.setSections(section1);
			sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
			sectionStack.setAnimateSections(true);
			sectionStack.setWidth100();
			sectionStack.setHeight100();
			sectionStack.setOverflow(Overflow.HIDDEN);

			addMember(sectionStack);

			initializeData();
		}

		private void initializeData() {

			MetaDataServiceAsync service = MetaDataServiceFactory.get();
			service.getEnableLanguages(TenantInfoHolder.getId(), new AsyncCallback<Map<String, String>>() {

				@Override
				public void onFailure(Throwable caught) {
				}

				@Override
				public void onSuccess(Map<String, String> result) {

					//ListをMap化
					Map<String, String> localizedMap = new LinkedHashMap<String, String>();
					if (localizedStringList != null) {
						for (LocalizedStringDefinition lsd : localizedStringList) {
							localizedMap.put(lsd.getLocaleName(), lsd.getStringValue());
						}
					}

					List<ListGridRecord> records = new ArrayList<ListGridRecord>(result.size());
					for (Map.Entry<String, String> e : result.entrySet()) {
						ListGridRecord record = new ListGridRecord();
						record.setAttribute("language", e.getKey());
						record.setAttribute("langDisp", e.getValue());
						if (localizedMap.containsKey(e.getKey())) {
							record.setAttribute("script", localizedMap.get(e.getKey()));
							record.setAttribute("custom", "Y");
						}
						records.add(record);
					}
					grid.setData(records.toArray(new ListGridRecord[]{}));

				}
			});
		}

		public ListGridRecord[] getLocaleRecords() {
			return grid.getRecords();
		}
	}

}
