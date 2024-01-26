/*
 * Copyright (C) 2015 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.tools.ui.langexplorer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.screen.ScreenModuleBasedUIFactoryHolder;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.MtpListGrid;
import org.iplass.adminconsole.shared.base.dto.i18n.MultiLangFieldInfo;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.adminconsole.shared.tools.dto.langexplorer.OutputMode;
import org.iplass.mtp.definition.LocalizedStringDefinition;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;

public class LangEditListPane extends VLayout {

	private static final String DEFAULT_KEY_NAME = "defaultLang";
	private static final String ITEM_KEY_NAME = "itemKey";

	private SectionStack sectionStack;
	private OperationPane opePane;
	private Map<String, String> enableLanguages;

	/** メタデータサービス */
	private final MetaDataServiceAsync service = MetaDataServiceFactory.get();

	private LangExplorerSettingController controller = ScreenModuleBasedUIFactoryHolder.getFactory().createLangExplorerSettingController();

	public LangEditListPane() {

		// 多言語情報表示部分の初期化、この時点で利用可能言語を取得しておく
		service.getEnableLanguages(TenantInfoHolder.getId(), new AsyncCallback<Map<String, String>>() {

			@Override
			public void onFailure(Throwable caught) {
				SC.say(caught.getMessage());
				GWT.log(caught.toString(), caught);
			}

			@Override
			public void onSuccess(Map<String, String> result) {
				enableLanguages = result;

				sectionStack = new SectionStack();
				sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
				sectionStack.setShowShadow(false);

				addMember(sectionStack);

				opePane = new OperationPane(LangEditListPane.this);
				opePane.disable();
				addMember(opePane);
			}

		});
	}

	public void enableButtons() {
		opePane.enable();
	}

	/**
	 * グリッド作成
	 * @param localizedStringMap
	 * @param definitionName
	 * @param langEditListPane
	 * @param path
	 */
	public void createGrid(Map<String, List<LocalizedStringDefinition>> localizedStringMap, String definitionClassName, String definitionName, String path) {
		//古いグリッド削除
		if (sectionStack.getMember(0) != null) {
			sectionStack.removeChild(sectionStack.getMember(0));
		}

		//グリッド作成
		ListGrid listGrid = new MtpListGrid();

		listGrid.setCanEdit(true);
		listGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);

		ListGridField itemKey = new ListGridField(ITEM_KEY_NAME, "Item");
		itemKey.setCanEdit(false);
		itemKey.setWidth(300);
		ListGridField defaultLang = new ListGridField(DEFAULT_KEY_NAME, "Default");

		ListGridField[] listGridFields = new ListGridField[enableLanguages.size() + 2];
		listGridFields[0] = itemKey;
		listGridFields[1] = defaultLang;

		int enableCnt = 2;
		for(Map.Entry<String, String> entry : enableLanguages.entrySet()) {
			listGridFields[enableCnt] = new ListGridField(entry.getKey(), entry.getValue());
			enableCnt ++;
		}

		listGrid.setFields(listGridFields);

		ListGridRecord[] records = new ListGridRecord[localizedStringMap.size()];
		int cnt = 0;
		for(Map.Entry<String, List<LocalizedStringDefinition>> entry : localizedStringMap.entrySet()) {
			ListGridRecord record = new ListGridRecord();

			record.setAttribute(ITEM_KEY_NAME, entry.getKey());

			for (LocalizedStringDefinition definition : entry.getValue()) {
				record.setAttribute(definition.getLocaleName(), definition.getStringValue());
			}

			records[cnt] = record;
			cnt ++;
		}

		listGrid.setData(records);

		SectionStackSection sectionStackSection = new SectionStackSection();
		sectionStackSection.setTitle(definitionName);
		sectionStackSection.setItems(listGrid);
		sectionStackSection.setExpanded(true);
		sectionStackSection.setCanCollapse(true);
		sectionStackSection.setResizeable(true);

		sectionStack.setSections(sectionStackSection);
		opePane.setDefinitionName(definitionName);
		opePane.setDefinitionClassName(definitionClassName);
		opePane.setPath(path);
	}

	/**
	 * 更新情報作成
	 * @param langEditListPane
	 * @return
	 */
	public Map<String, MultiLangFieldInfo> createUpdateInfo() {
		Canvas[] temp =  sectionStack.getMembers();
		ListGrid grid = (ListGrid) temp[1];
		ListGridRecord[] records = grid.getRecords();

		// update用のdefinitionを作成する為の元データを作成する
		Map<String, MultiLangFieldInfo> multiLangFieldsMap = new LinkedHashMap<String, MultiLangFieldInfo>();
		for (Record record : records) {
			String itemKey = record.getAttribute(ITEM_KEY_NAME);

			MultiLangFieldInfo multiLangFieldInfo = new MultiLangFieldInfo();
			multiLangFieldInfo.setDefaultString(record.getAttribute(DEFAULT_KEY_NAME));

			List<LocalizedStringDefinition> localizedDisplayNameList = new ArrayList<LocalizedStringDefinition>();
			for(Map.Entry<String, String> entry : this.enableLanguages.entrySet()) {
				String multiLang = record.getAttribute(entry.getKey());
				if (multiLang == null || multiLang.isEmpty()) continue;//未設定の場合は多言語定義作らない

				LocalizedStringDefinition localizedDefinition = new LocalizedStringDefinition();
				localizedDefinition.setLocaleName(entry.getKey());
				localizedDefinition.setStringValue(multiLang);

				localizedDisplayNameList.add(localizedDefinition);
			}
			multiLangFieldInfo.setLocalizedStringList(localizedDisplayNameList);
			multiLangFieldsMap.put(itemKey, multiLangFieldInfo);
		}
		return multiLangFieldsMap;
	}

	public String getDefinitionClassName() {
		return opePane.getDefinitionClassName();
	}

	public String getDefinitionName() {
		return opePane.getDefinitionName();
	}

	public String getPath() {
		return opePane.getPath();
	}

	public boolean isShowGrid() {
		return sectionStack.getMember(0) != null;
	}

	private class OperationPane extends HLayout {

		private String definitionClassName;
		private String definitionName;
		private String path;

		public OperationPane(final LangEditListPane parent) {
			setHeight(10);

			HLayout footer = new HLayout();
			footer.setMargin(10);
			footer.setMembersMargin(5);
			footer.setWidth100();

			IButton save = new IButton("Save");
			save.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
				@Override
				public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
					controller.updateMultiLangInfo(parent, definitionClassName, definitionName, path);
				}
			});

			IButton cancel = new IButton("Cancel");
			cancel.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
				@Override
				public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
					controller.displayMultiLangInfo(parent, definitionClassName, definitionName, path);
				}
			});

			IButton export = new IButton("Export");
			export.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
				@Override
				public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
					controller.exportMultiLangInfo(path, definitionName);
				}
			});

			IButton importButton = new IButton("Import");
			importButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
				@Override
				public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
					LangCsvUploadDialog dialog = new LangCsvUploadDialog(OutputMode.SINGLE, definitionName, path);
					dialog.addDialogCloseHandler(new DataChangedHandler() {

						@Override
						public void onDataChanged(DataChangedEvent event) {
							controller.displayMultiLangInfo(parent, definitionClassName, definitionName, path);
						}
					});
					dialog.show();
				}
			});

			footer.setMembers(save, cancel, export, importButton);

			addMember(footer);
		}

		public void setDefinitionClassName(String definitionClassName) {
			this.definitionClassName = definitionClassName;
		}

		public String getDefinitionClassName() {
			return this.definitionClassName;
		}

		public void setDefinitionName(String definitionName) {
			this.definitionName = definitionName;
		}

		public String getDefinitionName() {
			return this.definitionName;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public String getPath() {
			return this.path;
		}
	}
}
