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
import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.definition.LocalizedStringDefinition;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.AutoFitWidthApproach;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class LocalizedStringSettingDialog extends MtpDialog {

	private DisplayNameListPane entityPane;

	private List<LocalizedStringDefinition> localizedStringList;
	private LocalizedStringSettingDialogOption option;

	private List<DataChangedHandler> handlers = new ArrayList<DataChangedHandler>();

	public LocalizedStringSettingDialog(List<LocalizedStringDefinition> localizedStringList) {
		this(localizedStringList, new LocalizedStringSettingDialogOption());
	}

	public LocalizedStringSettingDialog(List<LocalizedStringDefinition> localizedStringList, boolean readOnly) {
		this(localizedStringList, new LocalizedStringSettingDialogOption(readOnly));
	}
	public LocalizedStringSettingDialog(List<LocalizedStringDefinition> localizedStringList, LocalizedStringSettingDialogOption option) {

		this.localizedStringList = localizedStringList;
		this.option = option;

		String title = "Multilingual Setting Dialog";
		if (option.isReadOnly()) {
			title += "(Read Only)";
		}
		setTitle(title);
		setHeight(425);
		centerInPage();

		entityPane = new DisplayNameListPane();

		container.addMember(entityPane);

		IButton save = new IButton("Save");
		save.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				List<LocalizedStringDefinition> editList = entityPane.getLocalizedStringList();
				fireDataChanged(editList);
				destroy();
			}
		});
		if (option.isReadOnly()) {
			save.setDisabled(true);
		}

		IButton cancel = new IButton("Cancel");
		cancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				destroy();
			}
		});

		footer.setMembers(save, cancel);
	}

	private class DisplayNameListPane extends VLayout {

		private ListGrid grid;

		public DisplayNameListPane() {

			setAutoHeight();
			setWidth100();
			setMargin(10);

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

					grid = new ListGrid();
					grid.setWidth100();
					grid.setHeight(1);

					grid.setShowAllColumns(true);								//列を全て表示
					grid.setShowAllRecords(true);								//レコードを全て表示
					grid.setCanResizeFields(true);								//列幅変更可能
					grid.setCanSort(false);										//ソート不可
					grid.setCanPickFields(false);								//表示フィールドの選択不可
					grid.setCanGroupBy(false);									//GroupByの選択不可
					grid.setAutoFitWidthApproach(AutoFitWidthApproach.BOTH);	//AutoFit時にタイトルと値を参照
					grid.setLeaveScrollbarGap(false);							//縦スクロールバー自動表示制御
					grid.setBodyOverflow(Overflow.VISIBLE);
					grid.setOverflow(Overflow.VISIBLE);

					if (!option.isReadOnly()) {
						grid.setCanEdit(true);
						grid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
					}
					ListGridField languageField = new ListGridField("language", "Language");
					languageField.setWidth(200);
					SelectItem viewField = new SelectItem("langItem", "LangItem");

					Map<String, String> enableLanguagesMap = new LinkedHashMap<String, String>();
					for (Map.Entry<String, String> e : result.entrySet()) {
						enableLanguagesMap.put(e.getKey(), e.getValue());
					}
					languageField.setEditorProperties(viewField);
					languageField.setValueMap(enableLanguagesMap);

					ListGridField valueField = new ListGridField("multilingual", "Multilingual");
					if (option.isModeTextArea()) {
						TextAreaItem txtValueField = new TextAreaItem();
						txtValueField.setHeight(55);
						txtValueField.setBrowserSpellCheck(false);
						valueField.setEditorProperties(txtValueField);
					}

					grid.setFields(languageField, valueField);

					ListGridRecord[] records = new ListGridRecord[localizedString.size()];

					int cnt = 0;
					for (Map.Entry<String, String> e : localizedString.entrySet()) {
						ListGridRecord record = new ListGridRecord();
						record.setAttribute("language", e.getKey());
						record.setAttribute("multilingual",localizedString.get(e.getKey()));
						records[cnt] = record;
						cnt++;
					}

					grid.setData(records);
					addMember(grid);

					if (!option.isReadOnly()) {
						IButton btnAdd = new IButton("Add");
						btnAdd.addClickHandler(new ClickHandler() {
							public void onClick(ClickEvent event) {
								grid.startEditingNew();
							}
						});

						IButton btnDel = new IButton("Remove");
						btnDel.addClickHandler(new ClickHandler() {
							public void onClick(ClickEvent event) {
								grid.removeSelectedData();
							}
						});

						HLayout buttonPane = new HLayout(5);
						buttonPane.setMargin(5);
						buttonPane.addMember(btnAdd);
						buttonPane.addMember(btnDel);

						addMember(buttonPane);
					}
				}
			});
		}

		public List<LocalizedStringDefinition> getLocalizedStringList() {
			if (grid == null) {
				return null;
			}

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
			return localizedStringList;
		}
	}

	public void addDataChangedHandler(DataChangedHandler handler) {
		handlers.add(handler);
	}

	private void fireDataChanged(List<LocalizedStringDefinition> localizedStringList) {
		DataChangedEvent event = new DataChangedEvent();
		event.setValue("localizedStringList", (Serializable)localizedStringList);
		for (DataChangedHandler handler : handlers) {
			handler.onDataChanged(event);
		}
	}

	public static class LocalizedStringSettingDialogOption {

		private boolean readOnly;
		private boolean modeTextArea;

		public LocalizedStringSettingDialogOption() {

		}

		public LocalizedStringSettingDialogOption(boolean readOnly) {
			this.readOnly = readOnly;
		}

		public boolean isReadOnly() {
			return readOnly;
		}
		public void setReadOnly(boolean readOnly) {
			this.readOnly = readOnly;
		}

		public boolean isModeTextArea() {
			return modeTextArea;
		}
		public void setModeTextArea(boolean modeTextArea) {
			this.modeTextArea = modeTextArea;
		}

	}

}
