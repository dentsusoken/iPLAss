/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.entity.property;

import java.util.Map;

import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyAttributePane.NeedsEnableLangMap;
import org.iplass.adminconsole.client.metadata.ui.entity.property.ValidationListPane.ValidationListPaneHandler;
import org.iplass.adminconsole.client.metadata.ui.entity.property.common.PropertyCommonAttributePane;
import org.iplass.adminconsole.client.metadata.ui.entity.property.common.PropertyCommonAttributePane.PropertyCommonAttributePaneHandler;
import org.iplass.adminconsole.client.metadata.ui.entity.property.type.PropertyTypeAttributeController;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;

import com.google.gwt.core.shared.GWT;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

public class PropertyEditDialog extends MtpDialog {

	/** 対象Propertyのレコード */
	private PropertyListGridRecord record;

	private PropertyCommonAttributePane pnlCommonAttribute;

	private ValidationListPane pnlValidation;

	private NormalizerListPane pnlNormalizer;

	private VLayout pnlAttributeContainer;
	private Canvas typeSeparater = SmartGWTUtil.separator();
	private PropertyAttributePane pnlTypeAttribute;

	private Map<String, String> enableLangMap;

	private String defName;

	private PropertyEditDialogHandler handler;

	private PropertyTypeAttributeController typeController = GWT.create(PropertyTypeAttributeController.class);

	public interface PropertyEditDialogHandler {

		void onSaved(PropertyListGridRecord record);
	}

	/**
	 * コンストラクタ
	 */
	public PropertyEditDialog(String defName, PropertyListGridRecord record, Map<String, String> enableLangMap, PropertyEditDialogHandler handler) {
		this.defName = defName;
		this.enableLangMap = enableLangMap;
		this.handler = handler;

		if (record == null) {
			record = new PropertyListGridRecord();
		}
		this.record = record;

		if (record.isInherited()) {
			setTitle("Property (Read Only)");
		} else {
			setTitle("Property");
		}
		setHeight(600);
		setShowMaximizeButton(true);

		pnlCommonAttribute = new PropertyCommonAttributePane(new PropertyCommonAttributePaneHandler() {

			@Override
			public void onChangeType(PropertyDefinitionType type) {

				PropertyAttribute typeAttribute = typeController.createTypeAttribute(type);

				createTypeAttributePane(type, typeAttribute);
			}

			@Override
			public void onChangeNotNull(boolean isNotNull) {
				pnlValidation.onChangeNotNullFromAttribute(isNotNull);
			}
		});

		pnlValidation = new ValidationListPane(new ValidationListPaneHandler() {

			@Override
			public void onChangeNotNull(boolean isNotNull) {
				pnlCommonAttribute.onChangeNotNullFromList(isNotNull);
			}

			@Override
			public boolean canEditNotNull() {
				return pnlCommonAttribute.canEditNotNull();
			}
		});

		pnlNormalizer = new NormalizerListPane();

		pnlAttributeContainer = new VLayout();
		pnlAttributeContainer.setWidth100();
		pnlAttributeContainer.setAutoHeight();
		pnlAttributeContainer.addMember(pnlCommonAttribute);

		container.addMember(pnlAttributeContainer);
		container.addMember(SmartGWTUtil.separator());
		container.addMember(pnlValidation);
		container.addMember(SmartGWTUtil.separator());
		container.addMember(pnlNormalizer);

		IButton btnOk = new IButton();
		btnOk.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (!validate()) {
					return;
				}
				saveRecord();
			}
		});
		if (record.isInherited()) {
			btnOk.setDisabled(true);
		}
		//削除の場合は、復活にボタン名を変更
		if (record.isDelete()) {
			btnOk.setTitle("Activate");
		} else {
			btnOk.setTitle("OK");
		}

		IButton btnCancel = new IButton("Cancel");
		btnCancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				destroy();
			}
		});

		footer.setMembers(btnOk, btnCancel);

		initialize();
		centerInPage();
	}

	/**
	 * データ初期化
	 */
	private void initialize() {

		pnlCommonAttribute.applyFrom(defName, record, record.getCommonAttribute());

		createTypeAttributePane(record.getRecordType(), record.getTypeAttribute());

		pnlValidation.applyFrom(defName, record, record.getTypeAttribute());

		pnlNormalizer.applyFrom(defName, record, record.getTypeAttribute());

	}

	private boolean validate() {

		boolean isValidate = true;

		if (!pnlCommonAttribute.validate()) {
			isValidate = false;
		}
		if (pnlTypeAttribute != null && !pnlTypeAttribute.validate()) {
			isValidate = false;
		}
		if (!pnlValidation.validate()) {
			isValidate = false;
		}
		if (!pnlNormalizer.validate()) {
			isValidate = false;
		}

		return isValidate;
	}

	private void saveRecord() {

		//共通属性を反映
		pnlCommonAttribute.applyTo(record);
		PropertyAttribute commonAttribute = record.getCommonAttribute();
		commonAttribute.applyTo(record);

		//選択タイプ属性を作成
		PropertyAttribute typeAttribute = typeController.createTypeAttribute(record.getRecordType());
		record.setTypeAttribute(typeAttribute);

		//選択タイプ属性を反映
		if (pnlTypeAttribute != null) {
			pnlTypeAttribute.applyTo(record);
			typeAttribute.applyTo(record);
		}

		//制約属性を反映
		pnlValidation.applyTo(record);

		//Normalizerを反映
		pnlNormalizer.applyTo(record);

		//新規追加出ない場合はStatusをUpdateに変更
		//TODO 変更チェック
		if (!record.isInsert()) {
			record.setStatusUpdate();
		}

		handler.onSaved(record);

		destroy();
	}

	private void createTypeAttributePane(PropertyDefinitionType type, PropertyAttribute typeAttribute) {

		//既存のタイプ設定をクリア
		if (pnlTypeAttribute != null) {
			pnlAttributeContainer.removeMember(typeSeparater);
			pnlAttributeContainer.removeMember((Canvas)pnlTypeAttribute);
			pnlTypeAttribute = null;
		}

		if (type != null) {
			pnlTypeAttribute = typeController.createTypeAttributePane(type);
			if (pnlTypeAttribute != null) {
				if (pnlTypeAttribute instanceof NeedsEnableLangMap) {
					((NeedsEnableLangMap)pnlTypeAttribute).setEnableLangMap(enableLangMap);
				}
				pnlAttributeContainer.addMember(typeSeparater);
				pnlAttributeContainer.addMember((Canvas)pnlTypeAttribute);

				pnlTypeAttribute.applyFrom(defName, record, typeAttribute);
			}
		}
	}

}
