/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.entity.layout.metafield;

import java.io.Serializable;
import java.util.LinkedHashMap;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.rpc.AdminAsyncCallback;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.MtpWidgetConstants;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpSelectItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.metadata.dto.Name;
import org.iplass.adminconsole.shared.metadata.dto.refrect.FieldInfo;
import org.iplass.adminconsole.shared.metadata.rpc.refrect.RefrectionServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.refrect.RefrectionServiceFactory;
import org.iplass.adminconsole.view.annotation.Refrectable;

import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class MetaFieldReferenceSingleItem extends MetaFieldCanvasItem {

	private DynamicForm form;

	private SelectItem typeItem;
	private IButton btnEdit;

	private RefrectionServiceAsync service = null;

	public MetaFieldReferenceSingleItem(final MetaFieldSettingPane pane, final FieldInfo info) {

		service = RefrectionServiceFactory.get();

		VLayout container = new VLayout();
		container.setAutoHeight();
		container.setWidth100();

		form = new DynamicForm();
		form.setWidth100();
		form.setNumCols(2);
		form.setColWidths(MtpWidgetConstants.FORM_WIDTH_ITEM, "*");

		typeItem = new MtpSelectItem();
		typeItem.setShowTitle(false);
		String description = pane.getDescription(info);
		if (SmartGWTUtil.isNotEmpty(description)) {
			SmartGWTUtil.addHoverToFormItem(typeItem, description);
		}
		if (info.isRequired()) {
			SmartGWTUtil.setRequired(typeItem);
		}
		form.setItems(typeItem);

		typeItem.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if (event.getOldValue().equals(event.getValue())) {
					// 変更されてなかったらキャンセル
					event.cancel();
				}
			}
		});
		typeItem.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				String className = SmartGWTUtil.getStringValue(typeItem);
				if (className == null || className.isEmpty()) {
					pane.setValue(info.getName(), null);
				} else {
					// 選択されたクラスのインスタンスを生成
					service.create(TenantInfoHolder.getId(), className, new AdminAsyncCallback<Refrectable>() {
						@Override
						public void onSuccess(Refrectable result) {
							// 指定フィールドの更新用の値として保管
							pane.setValue(info.getName(), result);
						}
					});
				}
			}
		});

		// コンボの内容を取得
		if (info.getFixedReferenceClass() == null || info.getFixedReferenceClass().length == 0) {
			service.getSubClass(TenantInfoHolder.getId(), info.getReferenceClassName(), new AdminAsyncCallback<Name[]>() {

				@Override
				public void onSuccess(Name[] names) {
					LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
					if (!info.isRequired()) {
						valueMap.put("", "");
					}
					for (Name clsName : names) {
						valueMap.put(clsName.getName(), clsName.getDisplayName());
					}
					typeItem.setValueMap(valueMap);
				}
			});
		} else {
			LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
			if (!info.isRequired()) {
				valueMap.put("", "");
			}
			for (Name clsName : info.getFixedReferenceClass()) {
				valueMap.put(clsName.getName(), clsName.getDisplayName());
			}
			typeItem.setValueMap(valueMap);
		}

		btnEdit = new IButton();
		btnEdit.setTitle(AdminClientMessageUtil.getString("ui_metadata_common_MetaFieldSettingPane_edit"));
		btnEdit.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// 指定フィールドの値を取得
				Serializable refValue = pane.getValue(info.getName());

				// 未選択時は無視
				if (refValue == null) {
					return;
				}

				// 編集ダイアログ表示
				final MetaFieldSettingDialog dialog = pane.createSubDialog(
						refValue.getClass().getName(), (Refrectable) refValue, info);

				dialog.setOkHandler(new MetaFieldUpdateHandler() {

					@Override
					public void execute(MetaFieldUpdateEvent event) {
						// ダイアログ破棄
						dialog.destroy();

						// 更新したObjectで上書き
						pane.setValue(info.getName(), event.getValue());
					}
				});

				dialog.setCancelHandler(new MetaFieldUpdateHandler() {

					@Override
					public void execute(MetaFieldUpdateEvent event) {
						dialog.destroy();
					}
				});

				dialog.show();
			}

		});

		HLayout buttonPane = new HLayout(5);
		buttonPane.setMargin(5);
		buttonPane.setMembers(btnEdit);

		Serializable value = pane.getValue(info.getName());
		final String className = value != null ? value.getClass().getName() : "";
		typeItem.setValue(className);

		container.addMember(form);
		container.addMember(buttonPane);

		setColSpan(2);
		setCanvas(container);
	}

	@Override
	public Boolean validate() {
		return form.validate();
	}

}
