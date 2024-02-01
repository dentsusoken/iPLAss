/*
 * Copyright (C) 2021 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.entity.property.normalizer;

import java.util.LinkedHashMap;

import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpSelectItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.entity.property.NormalizerListGridRecord;
import org.iplass.adminconsole.client.metadata.ui.entity.property.NormalizerListGridRecord.NormalizerType;

import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;

public class NormalizerEditDialog extends MtpDialog {

	private static final int BASE_HEIGHT = 140;

	private SelectItem selType;

	private NormalizerAttributePane typePane;

	private boolean isReadOnly = false;

	/** 対象Propertyのレコード */
	private NormalizerListGridRecord record;

	public interface NormalizerEditDialogHandler {

		void onSaved(NormalizerListGridRecord record);
	}

	private NormalizerEditDialogHandler handler;

	public NormalizerEditDialog(NormalizerListGridRecord target, boolean isReadOnly, NormalizerEditDialogHandler handler) {
		this.record = target;
		this.isReadOnly = isReadOnly;
		this.handler = handler;

		if (isReadOnly) {
			setTitle("Normalizer (Read Only)");
		} else {
			setTitle("Normalizer");
		}
		setHeight(BASE_HEIGHT);

		initialize();
		dataInitialize();
		formVisibleChange();

		centerInPage();
	}

	/**
	 * コンポーネント初期化
	 */
	private void initialize() {

		selType = new MtpSelectItem();
		selType.setTitle("Type");
		SmartGWTUtil.setRequired(selType);
		selType.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				formVisibleChange();
			}
		});

		final DynamicForm form = new MtpForm();
		form.setItems(selType);

		container.addMember(form);

		IButton ok = new IButton("OK");
		ok.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				boolean validate = form.validate();
				if (typePane != null) {
					validate = typePane.validate() && validate;
				}
				if (!validate) {
					return;
				}

				validateUpdateRecordData();
				destroy();
			}
		});
		if (isReadOnly) {
			ok.setDisabled(true);
		}
		IButton cancel = new IButton("Cancel");
		cancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				destroy();
			}
		});

		footer.setMembers(ok, cancel);
	}

	/**
	 * データ初期化
	 */
	private void dataInitialize() {

		LinkedHashMap<String, String> validationMap = NormalizerType.allTypeMap();
		selType.setValueMap(validationMap);

		if (record.getType() != null) {
			selType.setValue(record.getType().name());
		}
	}

	private void formVisibleChange() {

		if (typePane != null) {
			container.removeMember(typePane);
			typePane = null;
		}

		if (selType.getValue() != null) {
			NormalizerType normalizerType
					= NormalizerType.valueOf(SmartGWTUtil.getStringValue(selType));

			typePane = normalizerType.attributePane();
			if (typePane != null) {
				typePane.setDefinition(record.getNormalizerDefinition());
				container.addMember(typePane);
				setHeight(BASE_HEIGHT + typePane.panelHeight());
			}
		}
	}

	private void validateUpdateRecordData() {

		if (typePane != null) {
			record.setNormalizerDefinition(typePane.getEditDefinition(null));
		}

		handler.onSaved(record);
	}

}
