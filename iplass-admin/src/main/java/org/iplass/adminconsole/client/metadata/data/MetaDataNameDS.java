/*
 * Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.iplass.adminconsole.client.base.data.AbstractAdminDataSource;
import org.iplass.adminconsole.client.base.data.DataSourceConstants;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.base.dto.i18n.I18nMetaDisplayInfo;
import org.iplass.adminconsole.shared.metadata.dto.Name;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.definition.Definition;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.DataArrivedEvent;
import com.smartgwt.client.widgets.form.fields.events.DataArrivedHandler;
import com.smartgwt.client.widgets.form.fields.events.HasDataArrivedHandlers;
import com.smartgwt.client.widgets.form.fields.events.ItemHoverEvent;
import com.smartgwt.client.widgets.form.fields.events.ItemHoverHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class MetaDataNameDS extends AbstractAdminDataSource {

	private static final DataSourceField[] fields;
//	private static MetaDataServiceAsync service = GWT.create(MetaDataService.class);
	private static MetaDataServiceAsync service = MetaDataServiceFactory.get();

	static {
		DataSourceField name = new DataSourceField(
				DataSourceConstants.FIELD_NAME,
				FieldType.TEXT,
				DataSourceConstants.FIELD_NAME_TITLE);
		DataSourceField dispName = new DataSourceField(
				DataSourceConstants.FIELD_DISPLAY_NAME,
				FieldType.TEXT,
				DataSourceConstants.FIELD_DISPLAY_NAME_TITLE);
		DataSourceField dispValue = new DataSourceField(
				DataSourceConstants.FIELD_DISPLAY_VALUE,
				FieldType.TEXT,
				DataSourceConstants.FIELD_DISPLAY_VALUE_TITLE);
		DataSourceField tooltip = new DataSourceField(
				DataSourceConstants.FIELD_TOOLTIP,
				FieldType.TEXT,
				DataSourceConstants.FIELD_TOOLTIP);

		fields = new DataSourceField[] {name, dispName, dispValue, tooltip};
	}

    public static void setDataSource(final SelectItem item, final Class<? extends Definition> definition) {
    	setup(item, definition, new MetaDataNameDSOption());
    }
    public static void setDataSource(final SelectItem item, final Class<? extends Definition> definition, final MetaDataNameDSOption option) {
    	setup(item, definition, option);
    }
    public static void setDataSource(final ComboBoxItem item, final Class<? extends Definition> definition) {
    	setup(item, definition, new MetaDataNameDSOption());
    }
    public static void setDataSource(final ComboBoxItem item, final Class<? extends Definition> definition, final MetaDataNameDSOption option) {
    	setup(item, definition, option);
    }

    private static void setup(final FormItem item, final Class<? extends Definition> definition, final MetaDataNameDSOption option) {

    	item.setOptionDataSource(getInstance(definition, option));
    	item.setValueField(DataSourceConstants.FIELD_NAME);

    	if (option.isShowTooltip()) {
        	//addValueHoverHandlerを利用したいが、ValueHoverはテキストがあふれないとEventが発生しないので、ItemHoverを利用
        	item.addItemHoverHandler(new ItemHoverHandler() {

    			@Override
    			public void onItemHover(ItemHoverEvent event) {

    				final ListGridRecord record = item.getSelectedRecord();
    				if (record != null) {
    					String tooltip = record.getAttributeAsString(DataSourceConstants.FIELD_TOOLTIP);
    					if (SmartGWTUtil.isNotEmpty(tooltip)) {
    						item.setPrompt(SmartGWTUtil.getHoverString(tooltip));
    					}

    				}

    			}
    		});
        	item.addChangedHandler(new ChangedHandler() {

    			@Override
    			public void onChanged(ChangedEvent event) {
    				//デフォルト値設定
    				item.setTooltip(option.getTooltip());

    				getTooltip(item, definition.getName());
    			}
    		});

        	if (item instanceof HasDataArrivedHandlers) {
    	    	((HasDataArrivedHandlers)item).addDataArrivedHandler(new DataArrivedHandler() {

    				@Override
    				public void onDataArrived(DataArrivedEvent event) {
    					//初期値に対するTooltip取得
    					getTooltip(item, definition.getName());
    				}
    			});
        	}
    	}

    	ListGrid pickListProperties = new ListGrid();
    	pickListProperties.setShowFilterEditor(true);
    	if (item instanceof SelectItem) {
    		ListGridField nameField = new ListGridField(DataSourceConstants.FIELD_NAME, DataSourceConstants.FIELD_NAME_TITLE);
    		((SelectItem)item).setPickListFields(nameField);
    		((SelectItem)item).setPickListWidth(420);
    		((SelectItem)item).setPickListProperties(pickListProperties);
    	} else if (item instanceof ComboBoxItem) {
    		ListGridField nameField = new ListGridField(DataSourceConstants.FIELD_NAME, DataSourceConstants.FIELD_NAME_TITLE);
    		((ComboBoxItem)item).setPickListFields(nameField);
    		((ComboBoxItem)item).setPickListWidth(420);
    		((ComboBoxItem)item).setPickListProperties(pickListProperties);
    	}

    }

    private static void getTooltip(final FormItem item, String definitionClassName) {
		final ListGridRecord record = item.getSelectedRecord();
		if (record != null) {
			final String defName = record.getAttributeAsString(DataSourceConstants.FIELD_NAME);
			final String displayValue = record.getAttributeAsString(DataSourceConstants.FIELD_DISPLAY_VALUE);
			if (!defName.isEmpty() && displayValue == null) {
				//表示名取得
				service.getMetaDisplayInfo(TenantInfoHolder.getId(), definitionClassName, defName, new AsyncCallback<I18nMetaDisplayInfo>() {

					@Override
					public void onSuccess(I18nMetaDisplayInfo result) {
						String value = result.getI18nDisplayName() != null ? result.getI18nDisplayName() : "";
						record.setAttribute(DataSourceConstants.FIELD_DISPLAY_VALUE, value);
						record.setAttribute(DataSourceConstants.FIELD_TOOLTIP, defName + "<br/>" + value);
					}

					@Override
					public void onFailure(Throwable caught) {
						//特に何もしない
					}
				});
			}
		}
    }

	public static MetaDataNameDS getInstance(Class<? extends Definition> definition) {
		return getInstance(definition, new MetaDataNameDSOption());
	}

	public static MetaDataNameDS getInstance(Class<? extends Definition> definition, MetaDataNameDSOption option) {
		return new MetaDataNameDS(definition, option);
	}

	private final String definitionClassName;
	private final MetaDataNameDSOption option;

	public MetaDataNameDS(Class<? extends Definition> definition, MetaDataNameDSOption option) {
		this.definitionClassName = definition.getName();
		this.option = option;
		setFields(fields);
	}

	@Override
	protected void executeFetch(final String requestId, final DSRequest request, final DSResponse response) {

		service.getDefinitionNameList(TenantInfoHolder.getId(), definitionClassName, new AsyncCallback<List<Name>>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("error!!!", caught);

				response.setStatus(RPCResponse.STATUS_FAILURE);
				processResponse(requestId, response);
			}

			@Override
			public void onSuccess(List<Name> result) {

				//nameでソート
				Collections.sort(result, new Comparator<Name>() {
					@Override
					public int compare(Name o1, Name o2) {
						return o1.getName().compareTo(o2.getName());
					}
				});

				List<ListGridRecord> searchRecords = createRecords(result, option);

				response.setData(searchRecords.toArray(new ListGridRecord[]{}));
				response.setTotalRows(searchRecords.size());
				processResponse(requestId, response);
			}

		});

	}

	protected List<ListGridRecord> createRecords(List<Name> names, MetaDataNameDSOption option) {

		List<ListGridRecord> records = new ArrayList<>();

		if (option.isAddBlank()) {
			records.add(createBlankRecord());
		}

		if (option.isAddDefault()) {
			records.add(createDefaultRecord());
		}

		for (Name name : names) {
			records.add(createRecord(name));
		}
		return records;
	}

	protected  ListGridRecord createRecord(Name name) {
		ListGridRecord record = new ListGridRecord();
		record.setAttribute(DataSourceConstants.FIELD_NAME, name.getName());

		//TODO 多言語対応できてない
		if (name.getDisplayName() == null || name.getDisplayName().isEmpty()) {
			record.setAttribute(DataSourceConstants.FIELD_DISPLAY_NAME, "(" + name.getName() + ")");
		} else {
			record.setAttribute(DataSourceConstants.FIELD_DISPLAY_NAME, name.getDisplayName());
		}
		return record;
	}

	protected ListGridRecord createBlankRecord() {
		ListGridRecord record = new ListGridRecord();
		record.setAttribute(DataSourceConstants.FIELD_NAME, "");
		record.setAttribute(DataSourceConstants.FIELD_DISPLAY_NAME, "");
		record.setAttribute(DataSourceConstants.FIELD_DISPLAY_VALUE, "");
		return record;
	}

	private ListGridRecord createDefaultRecord() {
		ListGridRecord record = new ListGridRecord();
		record.setAttribute(DataSourceConstants.FIELD_NAME, "#default");
		record.setAttribute(DataSourceConstants.FIELD_DISPLAY_NAME, "default");
		record.setAttribute(DataSourceConstants.FIELD_DISPLAY_VALUE, "default");
		return record;
	}


	public static class MetaDataNameDSOption {

		private final boolean addBlank;
		private final boolean addDefault;
		private final String tooltip;
		private final boolean showTooltip;

		public MetaDataNameDSOption() {
			this(false, false, null, true);
		}

		public MetaDataNameDSOption(boolean addBlank, boolean addDefault) {
			this(addBlank, addDefault, null, true);
		}

		public MetaDataNameDSOption(boolean addBlank, boolean addDefault, String tooltip) {
			this(addBlank, addDefault, tooltip, true);
		}

		public MetaDataNameDSOption(boolean addBlank, boolean addDefault, String tooltip, boolean showTooltip) {
			this.addBlank = addBlank;
			this.addDefault = addDefault;
			this.tooltip = tooltip;
			this.showTooltip = showTooltip;
		}

		public boolean isAddBlank() {
			return addBlank;
		}

		public boolean isAddDefault() {
			return addDefault;
		}

		public String getTooltip() {
			return tooltip;
		}

		public boolean isShowTooltip() {
			return showTooltip;
		}
	}

}
