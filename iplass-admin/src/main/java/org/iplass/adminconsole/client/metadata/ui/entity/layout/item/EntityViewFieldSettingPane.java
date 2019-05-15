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

package org.iplass.adminconsole.client.metadata.ui.entity.layout.item;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.iplass.adminconsole.client.base.data.DataSourceConstants;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.rpc.AdminAsyncCallback;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpComboBoxItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpSelectItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.data.entity.PropertyDS;
import org.iplass.adminconsole.client.metadata.data.filter.EntityFilterItemDS;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.metafield.MetaFieldSettingDialog;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.metafield.MetaFieldSettingPane;
import org.iplass.adminconsole.shared.metadata.dto.refrect.FieldInfo;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.Refrectable;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.validator.IntegerRangeValidator;
import com.smartgwt.client.widgets.grid.ListGridField;

/**
 * EntityView用のプロパティ編集用パネル
 *
 * 起動トリガーのタイプを保持する。 またフィールド情報に参照タイプが指定されていた場合はチェックを行い、 対象外の場合は画面に表示しない。
 */
public class EntityViewFieldSettingPane extends MetaFieldSettingPane {

	private FieldReferenceType triggerType;

	// 画面定義の対象Entity
	private String defName;

	// 参照プロパティの対象Entity
	private String refDefName;

	// 入力タイプ:Propertyで選んだプロパティが参照型の場合の対象Entity
	private String childRefDefName;

	private Map<ComboBoxItem, String> triggerdPropertyList = new HashMap<>();

	private final MetaDataServiceAsync service = MetaDataServiceFactory.get();

	public EntityViewFieldSettingPane(EntityViewFieldSettingDialog owner, String className, Refrectable value,
			FieldReferenceType triggerType, String defName) {
		super(owner, className, value);
		this.triggerType = triggerType;
		this.defName = defName;
		init();
	}

	public EntityViewFieldSettingPane(EntityViewFieldSettingDialog owner, String className, Refrectable value,
			FieldReferenceType triggerType, String defName, String refDefName) {
		super(owner, className, value);
		this.triggerType = triggerType;
		this.defName = defName;
		this.refDefName = refDefName;
		init();
	}

	@Override
	protected FormItem createInputItem(FieldInfo info) {
		FormItem item = null;
		// フィルタ選択は親側ではなく、画面定義側でオーバーライドしてフィールドを生成
		if (info.getInputType() == InputType.FILTER) {
			item = createFilterList(info);
			if (getValue(info.getName()) != null) {
				item.setValue(getValueAs(String.class, info.getName()));
			}
		} else if (info.getInputType() == InputType.PROPERTY) {
			item = createPropertyList(info);
			if (getValue(info.getName()) != null) {
				item.setValue(getValueAs(String.class, info.getName()));
			}
		}

		if (item != null) {
			String displayName = getDisplayName(info);
			String title = info.isDeprecated() ? "<del>" + displayName + "</del>" : displayName;
			item.setTitle(title);
			item.setName(info.getName());
			String description = getDescription(info);
			String prompt = "<div style=\"white-space: nowrap;\">" + description + "</div>";
			item.setPrompt(prompt);
			item.setRequired(info.isRequired());
			if (info.isRangeCheck()) {
				// 数値型の範囲設定
				IntegerRangeValidator ir = new IntegerRangeValidator();
				if (info.getMaxRange() > -127)
					ir.setMax(info.getMaxRange());
				if (info.getMinRange() > -127)
					ir.setMin(info.getMinRange());
				item.setValidators(ir);
			}
		} else {
			item = super.createInputItem(info);
		}

		return item;
	}

	@Override
	protected void afterCreatePane(DynamicForm form) {

		// 選択値によって対象のEntityが変わる場合は、変更時にDataSourceを再設定
		for (Entry<ComboBoxItem, String> entry : triggerdPropertyList.entrySet()) {
			String triggerProperty = entry.getValue();
			if (form.getItem(triggerProperty) != null) {
				form.getItem(triggerProperty).addChangedHandler(new ChangedHandler() {

					@Override
					public void onChanged(ChangedEvent event) {
						String _defName = refDefName != null ? refDefName : defName;
						String refPropDefName = SmartGWTUtil.getStringValue(form.getItem(triggerProperty));
						entry.getKey().setOptionDataSource(PropertyDS.create(_defName, refPropDefName));
					}
				});
			}
		}
		triggerdPropertyList.clear();
	}

	@Override
	protected boolean isVisileField(FieldInfo info) {
		if (info.getEntityViewReferenceType() == null) {
			// アノテーションが未定義なので許可
			return true;
		}
		if (triggerType != null && triggerType == FieldReferenceType.ALL) {
			// トリガがALL(種類に無関係)なので許可
			return true;
		}
		for (FieldReferenceType type : info.getEntityViewReferenceType()) {
			if (type == FieldReferenceType.ALL) {
				return true;
			}
			if (type == triggerType) {
				return true;
			}
		}

		return false;
	}

	@Override
	protected MetaFieldSettingDialog createSubDialog(String className, Refrectable value, FieldInfo info) {
		FieldReferenceType triggerType = this.triggerType;
		// EntityViewFieldアノテーションを利用しないフィールドの場合、上書きをしません。
		if (info.getOverrideTriggerType() != null && info.getOverrideTriggerType() != FieldReferenceType.NONE) {
			triggerType = info.getOverrideTriggerType();
		}
		if (childRefDefName != null) {
			// サブダイアログの参照Entityとして指定されたEntityを設定
			return new EntityViewFieldSettingDialog(className, value, triggerType, defName, childRefDefName);
		} else if (refDefName != null) {
			// サブダイアログの参照Entityとして対象の参照Entityを設定
			return new EntityViewFieldSettingDialog(className, value, triggerType, defName, refDefName);
		} else {
			return new EntityViewFieldSettingDialog(className, value, triggerType, defName);
		}
	}

	private FormItem createFilterList(FieldInfo info) {
		SelectItem item = new MtpSelectItem();
		EntityFilterItemDS.setDataSource(item, defName);
		return item;
	}

	private FormItem createPropertyList(FieldInfo info) {

		final ComboBoxItem item = new MtpComboBoxItem();

		if (SmartGWTUtil.isNotEmpty(info.getFixedEntityName())) {
			//Entityが固定されている場合
			item.setOptionDataSource(PropertyDS.create(info.getFixedEntityName()));
		} else if (SmartGWTUtil.isNotEmpty(info.getSourceEntityNameField())) {
			//Entityが他のプロパティで設定される場合
			String _defName = refDefName != null ? refDefName : defName;
			String refPropDefName = getValueAs(String.class, info.getSourceEntityNameField());
			item.setOptionDataSource(PropertyDS.create(_defName, refPropDefName));

			//Form作成後にTriggerに対してChangeを設定するために保持
			triggerdPropertyList.put(item, info.getSourceEntityNameField());
		} else if (info.isUseRootEntityName()) {
			//RootのEntityが指定されている場合
			item.setOptionDataSource(PropertyDS.create(defName));
		} else if (refDefName != null) {
			//参照先としてEntityが指定されている場合
			item.setOptionDataSource(PropertyDS.create(refDefName));
		} else {
			item.setOptionDataSource(PropertyDS.create(defName));
		}
		item.setType("comboBox");
		item.setDisplayField(DataSourceConstants.FIELD_NAME);
		item.setValueField(DataSourceConstants.FIELD_NAME);

		ListGridField nameField = new ListGridField(DataSourceConstants.FIELD_NAME,
				AdminClientMessageUtil.getString("ui_metadata_common_MetaFieldSettingPane_name"));
		item.setPickListFields(nameField);
		item.setPickListWidth(400 + 20);

		if (info.isChildEntityName()) {
			String storedRefPropName = getValueAs(String.class, info.getName());
			getChildReferenceEntityName(storedRefPropName);

			//値が変更された場合に保持する
			item.addChangedHandler(new ChangedHandler() {

				@Override
				public void onChanged(ChangedEvent event) {
					// このプロパティの値(プロパティ名)を取得
					final String propName = SmartGWTUtil.getStringValue(item);
					getChildReferenceEntityName(propName);
				}
			});
		}

		return item;
	}

	private void getChildReferenceEntityName(String propertyName) {
		if (SmartGWTUtil.isEmpty(propertyName)) {
			childRefDefName = null;
			return;
		}
		String _defName = refDefName != null ? refDefName : defName;
		service.getPropertyDefinition(TenantInfoHolder.getId(), _defName, propertyName,
				new AdminAsyncCallback<PropertyDefinition>() {

					@Override
					public void onSuccess(PropertyDefinition property) {
						// 参照型の場合、Entity定義名を取得
						if (property instanceof ReferenceProperty) {
							childRefDefName = ((ReferenceProperty) property).getObjectDefinitionName();
						} else {
							childRefDefName = null;
						}
					}
		});
	}
}
