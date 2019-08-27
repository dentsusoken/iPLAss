/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.entity.layout.item.element.property;

import java.util.ArrayList;

import org.iplass.adminconsole.client.base.event.MTPEvent;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.rpc.AdminAsyncCallback;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.EntityViewDragPane;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.HasPropertyOperationHandler;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.PropertyOperationHandler;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.EntityViewFieldSettingDialog;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.EntityViewFieldSettingDialog.PropertyInfo;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.ItemControl;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.metafield.MetaFieldUpdateEvent;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.metafield.MetaFieldUpdateHandler;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.AutoNumberProperty;
import org.iplass.mtp.entity.definition.properties.BinaryProperty;
import org.iplass.mtp.entity.definition.properties.BooleanProperty;
import org.iplass.mtp.entity.definition.properties.DateProperty;
import org.iplass.mtp.entity.definition.properties.DateTimeProperty;
import org.iplass.mtp.entity.definition.properties.DecimalProperty;
import org.iplass.mtp.entity.definition.properties.ExpressionProperty;
import org.iplass.mtp.entity.definition.properties.FloatProperty;
import org.iplass.mtp.entity.definition.properties.IntegerProperty;
import org.iplass.mtp.entity.definition.properties.LongTextProperty;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.entity.definition.properties.SelectProperty;
import org.iplass.mtp.entity.definition.properties.StringProperty;
import org.iplass.mtp.entity.definition.properties.TimeProperty;
import org.iplass.mtp.view.generic.editor.AutoNumberPropertyEditor;
import org.iplass.mtp.view.generic.editor.BinaryPropertyEditor;
import org.iplass.mtp.view.generic.editor.BinaryPropertyEditor.BinaryDisplayType;
import org.iplass.mtp.view.generic.editor.BooleanPropertyEditor;
import org.iplass.mtp.view.generic.editor.BooleanPropertyEditor.BooleanDisplayType;
import org.iplass.mtp.view.generic.editor.DatePropertyEditor;
import org.iplass.mtp.view.generic.editor.DateTimePropertyEditor.DateTimeDisplayType;
import org.iplass.mtp.view.generic.editor.DecimalPropertyEditor;
import org.iplass.mtp.view.generic.editor.ExpressionPropertyEditor;
import org.iplass.mtp.view.generic.editor.FloatPropertyEditor;
import org.iplass.mtp.view.generic.editor.IntegerPropertyEditor;
import org.iplass.mtp.view.generic.editor.LongTextPropertyEditor;
import org.iplass.mtp.view.generic.editor.NestProperty;
import org.iplass.mtp.view.generic.editor.NumberPropertyEditor.NumberDisplayType;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor.ReferenceDisplayType;
import org.iplass.mtp.view.generic.editor.SelectPropertyEditor;
import org.iplass.mtp.view.generic.editor.SelectPropertyEditor.SelectDisplayType;
import org.iplass.mtp.view.generic.editor.StringPropertyEditor;
import org.iplass.mtp.view.generic.editor.StringPropertyEditor.StringDisplayType;
import org.iplass.mtp.view.generic.editor.TimePropertyEditor;
import org.iplass.mtp.view.generic.editor.TimestampPropertyEditor;
import org.iplass.mtp.view.generic.element.property.PropertyBase;
import org.iplass.mtp.view.generic.element.property.PropertyColumn;
import org.iplass.mtp.view.generic.element.property.PropertyItem;

import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * プロパティ用のウィンドウ
 * @author lis3wg
 *
 */
public class PropertyControl extends ItemControl implements HasPropertyOperationHandler {

	/** Window破棄前にプロパティの重複チェックリストから削除するためのハンドラ */
	private PropertyOperationHandler propertyOperationHandler;

	/** 参照Propertyの場合の参照先Entity名 */
	private String refDefName;

	/** 対象Propertyの表示名 */
	private String entityPropertyDisplayName;

	private MetaDataServiceAsync service;

	/**
	 * プロパティGridからDropされた場合のProperty用Control生成
	 *
	 * @param defName 対象Entity名(ルート)
	 * @param triggerType 対象となるトリガ
	 * @param record DropされたEntityPropertyTreeGrid(PropertyTreeDS)のTreeNodeまたはEntityPropertyListGrid(PropertyDS)のListGridRecord
	 * @param property 対象となるPropertyItemかPropertyColumn
	 */
	public PropertyControl(String defName, FieldReferenceType triggerType, ListGridRecord record, PropertyBase property) {
		this(defName, triggerType);

		//Drop時はDropされたレコードからPropertyDefinitionを取得
		PropertyDefinition pd = (PropertyDefinition) record.getAttributeAsObject("propertyDefinition");

		setValue("name", record.getAttribute("name"));
		setValue("propertyEditor", createDefaultEditor(pd));

		property.setDispFlag(true);
		property.setPropertyName((String) getValue("name"));
		property.setEditor((PropertyEditor) getValue("propertyEditor"));
		setClassName(property.getClass().getName());
		setValueObject(property);

		//Dropされた段階ではラベルはnull
		checkPropertyType(pd, null);
	}

	/**
	 * DefaultSectionまたはSearchConditionSectionのProperty用Control生成
	 *
	 * @param defName 対象Entity名(ルート)
	 * @param triggerType 対象となるトリガ
	 * @param property 対象となるプロパティ設定
	 */
	public PropertyControl(String defName, FieldReferenceType triggerType, PropertyItem property) {
		this(defName, triggerType);

		//一旦Itemの表示名をセット(未指定の場合あり)
		setTitle(property.getDisplayLabel());

		setValue("name", property.getPropertyName());
		setValue("propertyEditor", property.getEditor());
		setClassName(property.getClass().getName());
		setValueObject(property);

		//プロパティ定義を取得
		getEntityPropertyDefinition(property);
	}

	/**
	 * SearchResultSectionのProperty用Control生成
	 *
	 * @param defName 対象Entity名(ルート)
	 * @param triggerType 対象となるトリガ
	 * @param property 対象となるプロパティ設定
	 */
	public PropertyControl(String defName, FieldReferenceType triggerType, PropertyColumn property) {
		this(defName, triggerType);

		//一旦Itemの表示名をセット(未指定の場合あり)
		setTitle(property.getDisplayLabel());

		setValue("name", property.getPropertyName());
		setValue("propertyEditor", property.getEditor());
		setClassName(property.getClass().getName());
		setValueObject(property);

		//プロパティ定義を取得
		getEntityPropertyDefinition(property);
	}

	private PropertyControl(String defName, FieldReferenceType triggerType) {
		super(defName, triggerType);

		service = MetaDataServiceFactory.get();

		setDragType(EntityViewDragPane.DRAG_TYPE_PROPERTY);

		setShowMinimizeButton(false);
		setBackgroundColor("lightgreen");
		setBorder("1px solid green");
		setHeight(22);

		setMetaFieldUpdateHandler(new MetaFieldUpdateHandler() {

			@Override
			public void execute(MetaFieldUpdateEvent event) {
				String displayName = null;
				if (event.getValueMap().containsKey("displayLabel")) {
					displayName = (String) event.getValueMap().get("displayLabel");
				} else if (event.getValueMap().containsKey("title")) {
					displayName = (String) event.getValueMap().get("title");
				}
				createTitle(displayName);
			}
		});
	}

	private void getEntityPropertyDefinition(final PropertyBase property) {

		service.getPropertyDefinition(TenantInfoHolder.getId(), defName, property.getPropertyName(), new AdminAsyncCallback<PropertyDefinition>() {

			@Override
			public void onSuccess(PropertyDefinition pd) {
				checkPropertyType(pd, property.getDisplayLabel());
			}
		});
	}

	private void checkPropertyType(PropertyDefinition pd, String itemTitle) {
		if (pd != null) {
			entityPropertyDisplayName = pd.getDisplayName();
			if (pd instanceof ReferenceProperty) {
				refDefName = ((ReferenceProperty)pd).getObjectDefinitionName();
			} else {
				refDefName = null;
			}
		} else {
			entityPropertyDisplayName = null;
			refDefName = null;
		}
		createTitle(itemTitle);
	}

	private void createTitle(String itemDisplayName) {
		String title = itemDisplayName != null ? itemDisplayName + " ": "";
		title = title + "(" + entityPropertyDisplayName + "[" + (String)getValue("name") + "])";
		setTitle(title);
		SmartGWTUtil.addHoverToCanvas(this, getTitle());
	}

	@Override
	public void setPropertyOperationHandler(PropertyOperationHandler handler) {
		this.propertyOperationHandler = handler;
	}

	@Override
	protected boolean onPreDestroy() {
		if (propertyOperationHandler != null) {
			MTPEvent event = new MTPEvent();
			event.setValue("name", getValue("name"));
			propertyOperationHandler.remove(event);
		}
		return true;
	}

	/**
	 * Propety取得。
	 * @return
	 */
	public PropertyItem getProperty() {
		return (PropertyItem) getValueObject();
	}

	/**
	 * PropertyColumn取得。
	 * @return
	 */
	public PropertyColumn getPropertyColumn() {
		return (PropertyColumn) getValueObject();
	}

	/**
	 * プロパティの種類からデフォルトのエディタ生成。
	 * @param pd
	 * @return
	 */
	private PropertyEditor createDefaultEditor(PropertyDefinition pd) {
		PropertyEditor editor = null;
		if (pd instanceof BinaryProperty) {
			BinaryPropertyEditor be = new BinaryPropertyEditor();
			be.setDisplayType(BinaryDisplayType.BINARY);
			editor = be;
		} else if (pd instanceof BooleanProperty) {
			BooleanPropertyEditor be = new BooleanPropertyEditor();
			be.setDisplayType(BooleanDisplayType.RADIO);
			be.setTrueLabel(AdminClientMessageUtil.getString("ui_metadata_entity_layout_item_PropertyWindow_valid"));
			be.setFalseLabel(AdminClientMessageUtil.getString("ui_metadata_entity_layout_item_PropertyWindow_invalid"));
			editor = be;
		} else if (pd instanceof DateProperty) {
			DatePropertyEditor de = new DatePropertyEditor();
			de.setDisplayType(DateTimeDisplayType.DATETIME);
			editor = de;
		} else if (pd instanceof DateTimeProperty) {
			TimestampPropertyEditor te = new TimestampPropertyEditor();
			te.setDisplayType(DateTimeDisplayType.DATETIME);
			te.setUseDatetimePicker(true);
			editor = te;
		} else if (pd instanceof DecimalProperty) {
			DecimalPropertyEditor de = new DecimalPropertyEditor();
			de.setDisplayType(NumberDisplayType.TEXT);
			editor = de;
		} else if (pd instanceof ExpressionProperty) {
			editor = new ExpressionPropertyEditor();
		} else if (pd instanceof FloatProperty) {
			FloatPropertyEditor fe = new FloatPropertyEditor();
			fe.setDisplayType(NumberDisplayType.TEXT);
			editor = fe;
		} else if (pd instanceof IntegerProperty) {
			IntegerPropertyEditor ie = new IntegerPropertyEditor();
			ie.setDisplayType(NumberDisplayType.TEXT);
			editor = ie;
		} else if (pd instanceof LongTextProperty) {
			LongTextPropertyEditor le = new LongTextPropertyEditor();
			le.setDisplayType(StringDisplayType.TEXTAREA);
			editor = le;
		} else if (pd instanceof ReferenceProperty) {
			ReferencePropertyEditor re = new ReferencePropertyEditor();
			re.setDisplayType(ReferenceDisplayType.LINK);
			re.setObjectName(((ReferenceProperty) pd).getObjectDefinitionName());
			re.setNestProperties(new ArrayList<NestProperty>());
			editor = re;
		} else if (pd instanceof SelectProperty) {
			SelectPropertyEditor se = new SelectPropertyEditor();
			se.setDisplayType(SelectDisplayType.RADIO);
			editor = se;
		} else if (pd instanceof StringProperty) {
			StringPropertyEditor se = new StringPropertyEditor();
			se.setDisplayType(StringDisplayType.TEXT);
			editor = se;
		} else if (pd instanceof TimeProperty) {
			TimePropertyEditor te = new TimePropertyEditor();
			te.setDisplayType(DateTimeDisplayType.DATETIME);
			te.setUseTimePicker(true);
			editor = te;
		} else if (pd instanceof AutoNumberProperty) {
			editor = new AutoNumberPropertyEditor();
		}

		return editor;
	}

	@Override
	protected EntityViewFieldSettingDialog createSubDialog() {
		EntityViewFieldSettingDialog dialog = new EntityViewFieldSettingDialog(getClassName(), getValueObject(), triggerType, defName, refDefName);

		// ダイアログのタイトルに対象のプロパティ名を表示
		dialog.setTitlePropertyInfo(new PropertyInfo((String)getValue("name"), entityPropertyDisplayName));
		return dialog;
	}
}
