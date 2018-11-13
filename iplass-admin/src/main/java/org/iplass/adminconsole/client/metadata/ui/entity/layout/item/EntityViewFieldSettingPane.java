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

import org.iplass.adminconsole.client.base.data.DataSourceConstants;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.rpc.AdminAsyncCallback;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.data.entity.PropertyDS;
import org.iplass.adminconsole.client.metadata.data.filter.EntityFilterItemDS;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.metafield.MetaFieldSettingPane;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.metafield.MetaFieldSettingWindow;
import org.iplass.adminconsole.shared.metadata.dto.refrect.FieldInfo;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.Refrectable;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;

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
 * 起動トリガーのタイプを保持する。
 * またフィールド情報に参照タイプが指定されていた場合はチェックを行い、
 * 対象外の場合は画面に表示しない。
 */
public class EntityViewFieldSettingPane extends MetaFieldSettingPane {

	private FieldReferenceType triggerType;
	//画面定義の対象Entity
	private String defName;
	//参照プロパティの対象Entity
	private String refDefName;
	//入力タイプ:Propertyで選んだプロパティが参照型の場合の対象Entity
	private String propDefName;

	private final MetaDataServiceAsync service = MetaDataServiceFactory.get();

	public EntityViewFieldSettingPane(String className, Refrectable value, FieldReferenceType triggerType, String defName) {
		super(className, value);
		this.triggerType = triggerType;
		this.defName = defName;
		init();
	}

	public EntityViewFieldSettingPane(String className, Refrectable value, FieldReferenceType triggerType, String defName, String refDefName) {
		super(className, value);
		this.triggerType = triggerType;
		this.defName = defName;
		this.refDefName = refDefName;
		init();
	}

	@Override
	protected FormItem createSingleInputItem(FieldInfo info) {
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
			item.setAttribute(FIELD_ATTRIBUTE_NAME, info.getName());
			String description = getDescription(info);
			String prompt = "<div style=\"white-space: nowrap;\">" + description + "</div>";
			item.setPrompt(prompt);
			item.setRequired(info.isRequired());
			if (info.isRangeCheck()) {
				//数値型の範囲設定
				IntegerRangeValidator ir = new IntegerRangeValidator();
				if (info.getMaxRange() > -127) ir.setMax(info.getMaxRange());
				if (info.getMinRange() > -127) ir.setMin(info.getMinRange());
				item.setValidators(ir);
			}
		} else {
			item = super.createSingleInputItem(info);
		}

		return item;
	}

	@Override
	protected boolean isVisileField(FieldInfo info) {
		if (info.getEntityViewReferenceType() == null) {
			//アノテーションが未定義なので許可
			return true;
		}
		if (triggerType != null && triggerType == FieldReferenceType.ALL) {
			//トリガがALL(種類に無関係)なので許可
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
	protected MetaFieldSettingWindow createSubWindow(String className, Refrectable value, FieldInfo info) {
		FieldReferenceType triggerType = this.triggerType;
		if (info.getOverrideTriggerType() != FieldReferenceType.NONE) {
			triggerType = info.getOverrideTriggerType();
		}
		if (propDefName != null) {
			//プロパティのコンボで参照選択時
			return new EntityViewFieldSettingWindow(className, value, triggerType, defName, propDefName);
		} else if (refDefName != null) {
			//参照プロパティ、参照セクションから起動時
			return new EntityViewFieldSettingWindow(className, value, triggerType, defName, refDefName);
		} else {
			return new EntityViewFieldSettingWindow(className, value, triggerType, defName);
		}
	}

	private FormItem createFilterList(FieldInfo info) {
		SelectItem item = new SelectItem();
		EntityFilterItemDS.setDataSource(item, defName);
		return item;
	}

	private FormItem createPropertyList(FieldInfo info) {
		// defNameに対応するEntityのプロパティの選択リスト
		// Entityの依存関係まで考慮はできないので、.で子Entityのプロパティを指定することはできない
		// 子Entityのプロパティを指定する項目はTextベースにすること
		final ComboBoxItem item = new ComboBoxItem();
		if (info.getEntityDefinitionName() != null && !info.getEntityDefinitionName().isEmpty()) {
			item.setOptionDataSource(PropertyDS.create(info.getEntityDefinitionName()));
		} else if (refDefName != null) {
			item.setOptionDataSource(PropertyDS.create(refDefName));
		} else {
			item.setOptionDataSource(PropertyDS.create(defName));
		}
		item.setType("comboBox");
		item.setDisplayField(DataSourceConstants.FIELD_NAME);
		item.setValueField(DataSourceConstants.FIELD_NAME);

		ListGridField nameField = new ListGridField(DataSourceConstants.FIELD_NAME, AdminClientMessageUtil.getString("ui_metadata_common_MetaFieldSettingPane_name"));
		item.setPickListFields(nameField);
		item.setPickListWidth(400 + 20);

		if (info.isUseReferenceType()) {
			item.addChangedHandler(new ChangedHandler() {

				@Override
				public void onChanged(ChangedEvent event) {
					//このプロパティの型を取得
					final String propName = SmartGWTUtil.getStringValue(item);
					if (propName == null || propName.isEmpty()) {
						propDefName = null;
						return;
					}

					String _defName = refDefName != null ? refDefName : defName;
					service.getPropertyDefinition(TenantInfoHolder.getId(), _defName,
							propName, new AdminAsyncCallback<PropertyDefinition>() {

						@Override
						public void onSuccess(PropertyDefinition property) {
							//参照型の場合、Entity定義名を取得
							if (property instanceof ReferenceProperty) {
								propDefName = ((ReferenceProperty) property).getObjectDefinitionName();
							}
						}
					});
				}
			});
		}

		return item;
	}

}
