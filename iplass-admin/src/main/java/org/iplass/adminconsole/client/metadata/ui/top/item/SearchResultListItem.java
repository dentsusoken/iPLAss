/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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
import org.iplass.adminconsole.client.base.ui.widget.MetaDataLangTextItem;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataSelectItem;
import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpComboBoxItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpSelectItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.view.filter.EntityFilter;
import org.iplass.mtp.view.filter.EntityFilterItem;
import org.iplass.mtp.view.generic.EntityView;
import org.iplass.mtp.view.top.parts.EntityListParts;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.IntegerItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;

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
	}

	@Override
	public EntityListParts getParts() {
		return parts;
	}

	@Override
	protected void onOpen() {
		EntityListItemSettingDialog dialog = new EntityListItemSettingDialog();
		dialog.addDataChangedHandler(new DataChangedHandler() {

			@Override
			public void onDataChanged(DataChangedEvent event) {
				setTitle("SearchResult List(" + parts.getDefName() + ")");
			}
		});
		dialog.show();
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

	private class EntityListItemSettingDialog extends MtpDialog {

		private TextItem iconTagField;
		private TextItem styleField;
		private ComboBoxItem viewField;
		private ComboBoxItem viewForLinkField;
		private ComboBoxItem viewForDetailField;
		private SelectItem filterField;
		private IntegerItem heightField;
		private IntegerItem maxHeightField;
		private CheckboxItem searchAsync;

		private List<DataChangedHandler> handlers = new ArrayList<>();

		/**
		 * コンストラクタ
		 */
		public EntityListItemSettingDialog() {

			setTitle("SearchResult List");
			setHeight(370);
			centerInPage();

			final DynamicForm form = new MtpForm();
			form.setAutoFocus(true);

			final SelectItem entityField = new MetaDataSelectItem(EntityDefinition.class, "Entity");
			SmartGWTUtil.setRequired(entityField);
			entityField.setValue(parts.getDefName());

			viewField = new MtpComboBoxItem("view", "ResultList View");
			viewField.setDisabled(true);
			viewField.setValue(parts.getViewName());

			viewForLinkField = new MtpComboBoxItem("viewForLink", "Link Action View");
			viewForLinkField.setDisabled(true);
			viewForLinkField.setValue(parts.getViewNameForLink());

			viewForDetailField = new MtpComboBoxItem("viewForDetail", "Detail Action View");
			viewForDetailField.setDisabled(true);
			viewForDetailField.setValue(parts.getViewNameForDetail());

			getViewList(parts.getDefName());

			filterField = new MtpSelectItem("filter", "Filter");
			filterField.setDisabled(true);
			filterField.setValue(parts.getFilterName());
			getFilterList(parts.getDefName());

			entityField.addChangedHandler(new ChangedHandler() {

				@Override
				public void onChanged(ChangedEvent event) {
					if (event.getValue() == null || event.getValue().equals("")) {
						viewField.setValue("");
						viewField.setDisabled(true);
						viewForLinkField.setValue("");
						viewForLinkField.setDisabled(true);
						viewForDetailField.setValue("");
						viewForDetailField.setDisabled(true);
						filterField.setValue("");
						filterField.setDisabled(true);
					} else {
						viewField.setValue("");
						viewForLinkField.setValue("");
						viewForDetailField.setValue("");
						getViewList((String) event.getValue());
						filterField.setValue("");
						getFilterList((String) event.getValue());
					}
				}
			});

			final MetaDataLangTextItem titleField = new MetaDataLangTextItem();
			titleField.setTitle(AdminClientMessageUtil.getString("ui_metadata_top_item_SearchResultListItem_title"));
			titleField.setValue(parts.getTitle());
			titleField.setLocalizedList(parts.getLocalizedTitleList());

			iconTagField = new MtpTextItem("iconTag", "Icon Tag");
			iconTagField.setValue(parts.getIconTag());
			SmartGWTUtil.addHoverToFormItem(iconTagField, AdminClientMessageUtil.getString("ui_metadata_top_item_EntityListItem_iconTagComment"));

			styleField = new MtpTextItem("style", "Class");
			styleField.setValue(parts.getStyle());
			SmartGWTUtil.addHoverToFormItem(styleField, AdminClientMessageUtil.getString("ui_metadata_top_item_TopViewContentParts_styleDescriptionKey"));

			heightField = new IntegerItem("height", "Table Height");
			heightField.setWidth("100%");
			heightField.setValue(parts.getHeight());
			
			maxHeightField = new IntegerItem("maxHeight", "Max Height");
			maxHeightField.setWidth("100%");
			if (parts.getMaxHeight() != null && parts.getMaxHeight() > 0) {
				maxHeightField.setValue(parts.getMaxHeight());
			}
			SmartGWTUtil.addHoverToFormItem(maxHeightField, AdminClientMessageUtil.getString("ui_metadata_top_item_TopViewContentParts_maxHeightDescriptionKey"));

			searchAsync = new CheckboxItem("searchAsync", "Search asynchronously");
			searchAsync.setValue(parts.isSearchAsync());

			form.setItems(entityField, viewField, viewForLinkField, viewForDetailField, filterField, titleField, iconTagField, styleField, heightField, maxHeightField, searchAsync);

			container.addMember(form);

			IButton save = new IButton("OK");
			save.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if (form.validate()){
						//入力情報をパーツに
						parts.setDefName(SmartGWTUtil.getStringValue(entityField));
						parts.setViewName(SmartGWTUtil.getStringValue(viewField));
						parts.setViewNameForLink(SmartGWTUtil.getStringValue(viewForLinkField));
						parts.setViewNameForDetail(SmartGWTUtil.getStringValue(viewForDetailField));
						parts.setFilterName(SmartGWTUtil.getStringValue(filterField));
						parts.setTitle(SmartGWTUtil.getStringValue(titleField));
						parts.setIconTag(SmartGWTUtil.getStringValue(iconTagField));
						parts.setStyle(SmartGWTUtil.getStringValue(styleField));
						parts.setHeight(heightField.getValueAsInteger());
						parts.setMaxHeight(maxHeightField.getValueAsInteger());
						parts.setSearchAsync(searchAsync.getValueAsBoolean());
						parts.setLocalizedTitleList(titleField.getLocalizedList());
						fireDataChanged();
						destroy();
					}
				}
			});

			IButton cancel = new IButton("Cancel");
			cancel.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					destroy();
				}
			});

			footer.setMembers(save, cancel);
		}

		private void getViewList(String defName) {
			if (defName == null || defName.isEmpty()) {
				viewField.setValueMap(new LinkedHashMap<String, String>());
				viewForLinkField.setValueMap(new LinkedHashMap<String, String>());
				viewForDetailField.setValueMap(new LinkedHashMap<String, String>());
			} else {
				service.getDefinition(TenantInfoHolder.getId(), EntityView.class.getName(), defName,
						new AsyncCallback<EntityView>() {

					@Override
					public void onSuccess(EntityView result) {
						viewField.setDisabled(false);
						viewForLinkField.setDisabled(false);
						viewForDetailField.setDisabled(false);

						LinkedHashMap<String, String> valueMap = new LinkedHashMap<>();
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
						viewForDetailField.setValueMap(valueMap);
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
			final LinkedHashMap<String, String> valueMap = new LinkedHashMap<>();
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
