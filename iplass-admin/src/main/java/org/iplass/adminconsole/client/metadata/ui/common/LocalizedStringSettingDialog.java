/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.definition.LocalizedStringDefinition;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;

public class LocalizedStringSettingDialog extends AbstractWindow {

	private DisplayNameListPane entityPane;

	private List<LocalizedStringDefinition> localizedStringList;
	private boolean readOnly;

	private List<DataChangedHandler> handlers = new ArrayList<DataChangedHandler>();

	public LocalizedStringSettingDialog(List<LocalizedStringDefinition> localizedStringList) {
		this(localizedStringList, false);
	}

	public LocalizedStringSettingDialog(List<LocalizedStringDefinition> localizedStringList, boolean readOnly) {

		this.localizedStringList = localizedStringList;
		this.readOnly = readOnly;

		VLayout layout = new VLayout();

		String title = "Multilingual Setting Dialog";
		if (readOnly) {
			title += "(Read Only)";
		}
		setTitle(title);
		setHeight(425);
		setWidth(398);
		setMargin(10);
		setMembersMargin(10);

		setShowMinimizeButton(false);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();

		entityPane = new DisplayNameListPane();

		OperationPane opePane = new OperationPane(this);

		layout.addMember(entityPane);

		addItem(layout);
		addItem(opePane);
	}



	private class DisplayNameListPane extends HLayout {

		public DisplayNameListPane() {
			setHeight(320);
			setMargin(10);
			setMembersMargin(10);

			final Map<String, String> localizedString = new LinkedHashMap<String, String>();

			if (localizedStringList != null) {
				for (LocalizedStringDefinition lsd : localizedStringList) {
					localizedString.put(lsd.getLocaleName(), lsd.getStringValue());
				}
			}

			MetaDataServiceAsync service = MetaDataServiceFactory.get();
			service.getEnableLanguages(TenantInfoHolder.getId(), new AsyncCallback<Map<String, String>>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO 自動生成されたメソッド・スタブ

				}

				@Override
				public void onSuccess(Map<String, String> result) {
					final ListGrid listGrid = new ListGrid();

					if (!readOnly) {
						listGrid.setCanEdit(true);
						listGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
					}
					ListGridField languageField = new ListGridField("language", "Language");
					SelectItem viewField = new SelectItem("langItem", "LangItem");

					Map<String, String> enableLanguagesMap = new LinkedHashMap<String, String>();
					for (Map.Entry<String, String> e : result.entrySet()) {
						enableLanguagesMap.put(e.getKey(), e.getValue());
					}
					languageField.setEditorProperties(viewField);
					languageField.setValueMap(enableLanguagesMap);
					listGrid.setFields(languageField, new ListGridField("multilingual", "Multilingual"));

					ListGridRecord[] records = new ListGridRecord[localizedString.size()];

					int cnt = 0;
					for (Map.Entry<String, String> e : localizedString.entrySet()) {
						ListGridRecord record = new ListGridRecord();
						record.setAttribute("language", e.getKey());
						record.setAttribute("multilingual",localizedString.get(e.getKey()));
						records[cnt] = record;
						cnt++;
					}

					listGrid.setData(records);

					SectionStackSection section1 = new SectionStackSection();
					section1.setTitle("Multilingual Setting");
					section1.setItems(listGrid);
					section1.setExpanded(true);
					section1.setCanCollapse(false);

					if (!readOnly) {
						ImgButton addButton = new ImgButton();
						addButton.setSrc("[SKIN]actions/add.png");
						addButton.setSize(16);
						addButton.setShowFocused(false);
						addButton.setShowRollOver(false);
						addButton.setShowDown(false);
						addButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
							public void onClick(
									com.smartgwt.client.widgets.events.ClickEvent event) {
								listGrid.startEditingNew();
							}
						});

						ImgButton removeButton = new ImgButton();
						removeButton.setSrc("[SKIN]actions/remove.png");
						removeButton.setSize(16);
						removeButton.setShowFocused(false);
						removeButton.setShowRollOver(false);
						removeButton.setShowDown(false);
						removeButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
							public void onClick(
									com.smartgwt.client.widgets.events.ClickEvent event) {
								listGrid.removeSelectedData();
							}
						});
						section1.setControls(addButton, removeButton);
					}

					SectionStack sectionStack = new SectionStack();
					sectionStack.setSections(section1);
					sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
					sectionStack.setAnimateSections(true);
					sectionStack.setWidth(345);
					sectionStack.setHeight(300);
					sectionStack.setOverflow(Overflow.HIDDEN);

					addMember(sectionStack);
				}
			});
		}
	}

	private class OperationPane extends HLayout {
		public OperationPane(final LocalizedStringSettingDialog dialog) {
			HLayout footer = new HLayout(5);
			footer.setMargin(10);
			footer.setWidth100();
			footer.setAlign(VerticalAlignment.CENTER);

			IButton save = new IButton("Save");
			save.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
				public void onClick(
						com.smartgwt.client.widgets.events.ClickEvent event) {

					Canvas[] canvas = entityPane.getChildren();
					SectionStack stack = (SectionStack) canvas[0];
					Canvas[] temp =  stack.getMembers();
					ListGrid grid = (ListGrid) temp[1];
					ListGridRecord[] records = grid.getRecords();

					localizedStringList.clear();
					for (Record record : records) {
						String localeName = record.getAttribute("language");
						String stringValue = record.getAttribute("multilingual");

						LocalizedStringDefinition lsd = new LocalizedStringDefinition();
						lsd.setLocaleName(localeName);
						lsd.setStringValue(stringValue);

						localizedStringList.add(lsd);
					}

					fireDataChanged();

					dialog.destroy();
				}
			});
			if (readOnly) {
				save.setDisabled(true);
			}

			IButton cancel = new IButton("Cancel");
			cancel.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
				public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
					dialog.destroy();
				}
			});

			footer.setMembers(save, cancel);

			addMember(footer);
		}
	}

	public void addDataChangedHandler(DataChangedHandler handler) {
		handlers.add(handler);
	}

	private void fireDataChanged() {
		DataChangedEvent event = new DataChangedEvent();
		event.setValue("localizedStringList", (Serializable) localizedStringList);
		for (DataChangedHandler handler : handlers) {
			handler.onDataChanged(event);
		}
	}
}
