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
import org.iplass.adminconsole.client.base.ui.widget.MetaDataComboBoxItem;
import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpSelectItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.web.actionmapping.definition.cache.CacheRelatedEntityDefinition;
import org.iplass.mtp.web.actionmapping.definition.cache.RelatedEntityType;

import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;

public class CacheRelatedEntityEditDialog extends MtpDialog {

	/** Entity */
	private ComboBoxItem entityNameField;
	/** Type */
	private SelectItem relatedEntityTypeField;

	/** データ変更ハンドラ */
	private List<DataChangedHandler> handlers = new ArrayList<DataChangedHandler>();

	public CacheRelatedEntityEditDialog() {

		setHeight(200);
		setTitle("Cache Related Entity");
		centerInPage();

		entityNameField = new MetaDataComboBoxItem(EntityDefinition.class, "Entity");
		SmartGWTUtil.setRequired(entityNameField);

		relatedEntityTypeField = new MtpSelectItem("relatedEntityType", "Type");
		SmartGWTUtil.setRequired(relatedEntityTypeField);
		LinkedHashMap<String, String> typeMap = new LinkedHashMap<String, String>();
		for (RelatedEntityType type : RelatedEntityType.values()) {
			typeMap.put(type.name(), type.name());
		}
		relatedEntityTypeField.setValueMap(typeMap);
		relatedEntityTypeField.setDefaultValue(RelatedEntityType.values()[0].name());

		final DynamicForm form = new MtpForm();
		form.setItems(entityNameField, relatedEntityTypeField);

		container.addMember(form);

		IButton save = new IButton("OK");
		save.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				boolean commonValidate = form.validate();
				if (commonValidate) {
					saveDefinition();
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


