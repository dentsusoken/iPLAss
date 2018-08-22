/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.top.item;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.data.entity.EntityDS;
import org.iplass.adminconsole.client.metadata.ui.common.LocalizedStringSettingDialog;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.view.filter.EntityFilter;
import org.iplass.mtp.view.filter.EntityFilterItem;
import org.iplass.mtp.view.generic.EntityView;
import org.iplass.mtp.view.top.parts.EntityListParts;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.HeaderControls;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.HeaderControl;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.IntegerItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;

/**
 *
 * @author lis3wg
 */
public class SearchResultListItem extends PartsItem {

	private EntityListParts parts;

	private MetaDataServiceAsync service;

	/**
	 * コンストラクタ
	 */
	public SearchResultListItem(EntityListParts parts) {
		this.parts = parts;
		this.service = MetaDataServiceFactory.get();
		setTitle("SearchResult List(" + parts.getDefName() + ")");
		setBackgroundColor("#DDDDFF");

		setHeaderControls(HeaderControls.HEADER_LABEL, new HeaderControl(HeaderControl.SETTINGS, new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				EntityListItemSettingDialog dialog = new EntityListItemSettingDialog();
				dialog.addDataChangedHandler(new DataChangedHandler() {

					@Override
					public void onDataChanged(DataChangedEvent event) {
						setTitle("SearchResult List(" + parts.getDefName() + ")");
					}
				});
				dialog.show();
			}
		}), HeaderControls.CLOSE_BUTTON);
	}

	@Override
	public EntityListParts getParts() {
		return parts;
	}

	@Override
	public void doDropAction(final DropActionCallback callback) {
		EntityListItemSettingDialog dialog = new EntityListItemSettingDialog();
		dialog.addDataChangedHandler(new DataChangedHandler() {

			@Override
			public void onDataChanged(DataChangedEvent event) {
				setTitle("SearchResult List(" + parts.getDefName() + ")");
				callback.handle();
			}
		});
		dialog.show();
	}

	private class EntityListItemSettingDialog extends AbstractWindow {

		private TextItem iconTagField;
		private ComboBoxItem viewField;
		private ComboBoxItem viewForLinkField;
		private SelectItem filterField;
		private IntegerItem heightField;
		private ButtonItem langBtn;
		private List<LocalizedStringDefinition> localizedTitleList;

		private List<DataChangedHandler> handlers = new ArrayList<DataChangedHandler>();

		/**
		 * コンストラクタ
		 */
		public EntityListItemSettingDialog() {
			setTitle("SearchResult List");
			setHeight(280);
			setWidth(430);
			setMargin(10);
			setMembersMargin(10);

			setShowMinimizeButton(false);
			setIsModal(true);
			setShowModalMask(true);
			centerInPage();

			final DynamicForm form = new DynamicForm();
			form.setAlign(Alignment.CENTER);
			form.setAutoFocus(true);
			form.setNumCols(3);

			final SelectItem entityField = new SelectItem("entity", "Entity");
			entityField.setWidth(250);
			EntityDS.setDataSource(entityField);
			SmartGWTUtil.setRequired(entityField);
			entityField.setValue(parts.getDefName());
			entityField.setColSpan(3);

			viewField = new ComboBoxItem("view", "ResultList View");
			viewField.setWidth(250);
			viewField.setDisabled(true);
			viewField.setValue(parts.getViewName());
			viewField.setColSpan(3);

			viewForLinkField = new ComboBoxItem("viewForLink", "Link Action View");
			viewForLinkField.setWidth(250);
			viewForLinkField.setDisabled(true);
			viewForLinkField.setValue(parts.getViewNameForLink());
			viewForLinkField.setColSpan(3);

			getViewList(parts.getDefName());

			filterField = new SelectItem("filter", "Filter");
			filterField.setWidth(250);
			filterField.setDisabled(true);
			filterField.setValue(parts.getFilterName());
			filterField.setColSpan(3);
			getFilterList(parts.getDefName());

			entityField.addChangedHandler(new ChangedHandler() {

				@Override
				public void onChanged(ChangedEvent event) {
					if (event.getValue() == null || event.getValue().equals("")) {
						viewField.setValue("");
						viewField.setDisabled(true);
						viewForLinkField.setValue("");
						viewForLinkField.setDisabled(true);
						filterField.setValue("");
						filterField.setDisabled(true);
					} else {
						viewField.setValue("");
						viewForLinkField.setValue("");
						getViewList((String) event.getValue());
						filterField.setValue("");
						getFilterList((String) event.getValue());
					}
				}
			});

			final TextItem titleField = new TextItem("title", "Title");
			titleField.setWidth(165);
			titleField.setValue(parts.getTitle());

			langBtn = new ButtonItem("addDisplayName", "Languages");
			langBtn.setShowTitle(false);
			langBtn.setIcon("world.png");
			langBtn.setStartRow(false);	//これを指定しないとButtonの場合、先頭にくる
			langBtn.setEndRow(false);	//これを指定しないと次のFormItemが先頭にいく
			langBtn.setPrompt(AdminClientMessageUtil.getString("ui_metadata_top_item_EntityListItem_eachLangDspName"));
			langBtn.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

				@Override
				public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
					if (localizedTitleList == null) {
						localizedTitleList = new ArrayList<LocalizedStringDefinition>();
					}
					LocalizedStringSettingDialog dialog = new LocalizedStringSettingDialog(localizedTitleList);
					dialog.show();
				}
			});
			localizedTitleList = parts.getLocalizedTitleList();

			iconTagField = new TextItem("iconTag", "Icon Tag");
			iconTagField.setWidth(250);
			iconTagField.setValue(parts.getIconTag());
			iconTagField.setColSpan(3);
			SmartGWTUtil.addHoverToFormItem(iconTagField, AdminClientMessageUtil.getString("ui_metadata_top_item_EntityListItem_iconTagComment"));

			heightField = new IntegerItem("height", "Height");
			heightField.setWidth(250);
			heightField.setValue(parts.getHeight());
			heightField.setColSpan(3);

			form.setItems(entityField, viewField, viewForLinkField, filterField, titleField, langBtn, iconTagField, heightField);

			HLayout footer = new HLayout(5);
			footer.setMargin(10);
			footer.setWidth100();
			footer.setAlign(VerticalAlignment.CENTER);

			IButton save = new IButton("OK");
			save.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if (form.validate()){
						//入力情報をパーツに
						parts.setDefName(SmartGWTUtil.getStringValue(entityField));
						parts.setViewName(SmartGWTUtil.getStringValue(viewField));
						parts.setViewNameForLink(SmartGWTUtil.getStringValue(viewForLinkField));
						parts.setFilterName(SmartGWTUtil.getStringValue(filterField));
						parts.setTitle(SmartGWTUtil.getStringValue(titleField));
						parts.setIconTag(SmartGWTUtil.getStringValue(iconTagField));
						parts.setHeight(heightField.getValueAsInteger());
						parts.setLocalizedTitleList(localizedTitleList);
						fireDataChanged();
						destroy();
					}
				}
			});

			IButton cancel = new IButton("Cancel");
			cancel.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					destroy();
				}
			});

			footer.setMembers(save, cancel);

			addItem(form);
			addItem(footer);
		}

		private void getViewList(String defName) {
			if (defName == null || defName.isEmpty()) {
				viewField.setValueMap(new LinkedHashMap<String, String>());
				viewForLinkField.setValueMap(new LinkedHashMap<String, String>());
			} else {
				service.getDefinition(TenantInfoHolder.getId(), EntityView.class.getName(), defName,
						new AsyncCallback<EntityView>() {

					@Override
					public void onSuccess(EntityView result) {
						viewField.setDisabled(false);
						viewForLinkField.setDisabled(false);

						LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
						valueMap.put("", "default");

						if (result != null && result.getSearchFormViewNames().length > 0) {
							for (String viewName : result.getSearchFormViewNames()) {
								if (!valueMap.containsKey(viewName)) {
									valueMap.put(viewName, viewName);
								}
							}
						}

						viewField.setValueMap(valueMap);
						viewForLinkField.setValueMap(valueMap);
					}

					@Override
					public void onFailure(Throwable caught) {
						SC.say(AdminClientMessageUtil.getString("ui_metadata_top_item_EntityListItem_failed"),
								AdminClientMessageUtil.getString("ui_metadata_top_item_EntityListItem_failedGetScreenInfo") + caught.getMessage());

						GWT.log(caught.toString(), caught);
					}
				});
			}
		}

		private void getFilterList(String defName) {
			final LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
			if (defName == null || defName.isEmpty()) {
				filterField.setValueMap(valueMap);
			} else {
				service.getDefinition(TenantInfoHolder.getId(), EntityFilter.class.getName(), defName,
						new AsyncCallback<EntityFilter>() {

					@Override
					public void onSuccess(EntityFilter result) {
						if (result != null && !result.getItems().isEmpty()) {
							valueMap.put("", "");//初期選択用
							filterField.setDisabled(false);
							for (EntityFilterItem item : result.getItems()) {
								valueMap.put(item.getName(), item.getDisplayName());
							}

							filterField.setValueMap(valueMap);
						} else {
							filterField.setDisabled(true);
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						SC.say(AdminClientMessageUtil.getString("ui_metadata_top_item_EntityListItem_failed"),
								AdminClientMessageUtil.getString("ui_metadata_top_item_EntityListItem_failedGetFileterInfo") + caught.getMessage());

						GWT.log(caught.toString(), caught);
					}
				});
			}
		}

		public void addDataChangedHandler(DataChangedHandler handler) {
			handlers.add(handler);
		}

		private void fireDataChanged() {
			DataChangedEvent event = new DataChangedEvent();
			for (DataChangedHandler handler : handlers) {
				handler.onDataChanged(event);
			}
		}
	}
}
