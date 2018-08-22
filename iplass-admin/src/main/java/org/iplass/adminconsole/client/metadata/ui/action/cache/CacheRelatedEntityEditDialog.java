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

package org.iplass.adminconsole.client.metadata.ui.action.cache;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.data.entity.EntityDS;
import org.iplass.mtp.web.actionmapping.definition.cache.CacheRelatedEntityDefinition;
import org.iplass.mtp.web.actionmapping.definition.cache.RelatedEntityType;

import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.layout.HLayout;

public class CacheRelatedEntityEditDialog extends AbstractWindow {

	/** Entity */
	private ComboBoxItem entityNameField;
	/** Type */
	private SelectItem relatedEntityTypeField;

	/** フッター */
	private HLayout footer;

	/** データ変更ハンドラ */
	private List<DataChangedHandler> handlers = new ArrayList<DataChangedHandler>();

	public CacheRelatedEntityEditDialog() {

		setWidth(400);
		setHeight(130);
		setTitle("Cache Related Entity");
		setShowMinimizeButton(false);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();

		entityNameField = new ComboBoxItem("entityName", "Entity");
		entityNameField.setWidth(250);
		SmartGWTUtil.setRequired(entityNameField);
		EntityDS.setDataSource(entityNameField);

		relatedEntityTypeField = new SelectItem("relatedEntityType", "Type");
		relatedEntityTypeField.setWidth(250);
		SmartGWTUtil.setRequired(relatedEntityTypeField);
		LinkedHashMap<String, String> typeMap = new LinkedHashMap<String, String>();
		for (RelatedEntityType type : RelatedEntityType.values()) {
			typeMap.put(type.name(), type.name());
		}
		relatedEntityTypeField.setValueMap(typeMap);
		relatedEntityTypeField.setDefaultValue(RelatedEntityType.values()[0].name());

		final DynamicForm form = new DynamicForm();
		form.setMargin(5);
//		form.setHeight100();
		form.setAutoHeight();	//下に追加するためAutoHeight
		form.setWidth100();
		form.setItems(entityNameField, relatedEntityTypeField);

		IButton save = new IButton("OK");
		save.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				boolean commonValidate = form.validate();
				if (commonValidate) {
					saveDefinition();
				} else {
//					errors.setVisible(true);
				}
			}
		});

		IButton cancel = new IButton("Cancel");
		cancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				destroy();
			}
		});

		footer = new HLayout(5);
		footer.setMargin(5);
		footer.setHeight(20);
		footer.setWidth100();
		//footer.setAlign(Alignment.LEFT);
		footer.setAlign(VerticalAlignment.CENTER);
		footer.setMembers(save, cancel);


		addItem(form);
		addItem(footer);
	}

	/**
	 * 編集対象の {@link CacheRelatedEntityDefinition} を設定します。
	 * @param definition 編集対象の {@link CacheRelatedEntityDefinition}
	 */
	public void setDefinition(CacheRelatedEntityDefinition definition) {
		if (definition != null) {
			entityNameField.setValue(definition.getDefinitionName());
			relatedEntityTypeField.setValue(definition.getType().name());
		} else {
			entityNameField.setValue("");
			relatedEntityTypeField.setValue("");
		}
	}

	/**
	 * {@link DataChangedHandler} を追加します。
	 *
	 * @param handler {@link DataChangedHandler}
	 */
	public void addDataChangeHandler(DataChangedHandler handler) {
		handlers.add(0, handler);
	}

	private void saveDefinition() {
		getEditedDefinition();
	}

	private void getEditedDefinition() {

		CacheRelatedEntityDefinition definition = new CacheRelatedEntityDefinition();
		definition.setDefinitionName(SmartGWTUtil.getStringValue(entityNameField));
//		ListGridRecord record = entityNameField.getSelectedRecord();
//		if (record != null) {
//			definition.setDisplayName(record.getAttributeAsString(DataSourceConstants.FIELD_DISPLAY_NAME));
//		}
		definition.setType(RelatedEntityType.valueOf(SmartGWTUtil.getStringValue(relatedEntityTypeField)));

		//データ変更を通知
		fireDataChanged(definition);

		//ダイアログ消去
		destroy();
	}

	/**
	 * データ変更通知処理
	 */
	private void fireDataChanged(CacheRelatedEntityDefinition definition) {
		DataChangedEvent event = new DataChangedEvent();
		event.setValueObject(definition);
		for (DataChangedHandler handler : handlers) {
			handler.onDataChanged(event);
		}
	}
}


