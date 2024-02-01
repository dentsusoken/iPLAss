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

package org.iplass.adminconsole.client.metadata.ui.common;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.EditablePane;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataLangTextItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextAreaItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.MetaDataItemMenuTreeNode;
import org.iplass.adminconsole.client.metadata.ui.MetaDataUtil;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.definition.Definition;
import org.iplass.mtp.definition.DefinitionInfo;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.definition.SharedConfig;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;

public class MetaCommonAttributeSection<D extends Definition> extends SectionStackSection implements EditablePane<D> {

	private MetaDataServiceAsync service = MetaDataServiceFactory.get();

	private MetaDataSharableController sharableController = GWT.create(MetaDataSharableController.class);

	private Label metaTypeLabel;

	private MetaCommonAttributePane<D> attrPane;
	private SharedSettingPane sharedPane;

	private MetaDataItemMenuTreeNode targetNode;
	private Class<D> defClass;

	public MetaCommonAttributeSection(MetaDataItemMenuTreeNode targetNode, Class<D> defClass) {
		this(targetNode, defClass , false);
	}

	public MetaCommonAttributeSection(MetaDataItemMenuTreeNode targetNode, Class<D> defClass, boolean useLangSetting) {
		this.defClass = defClass;

		setTitle("Common Attribute");
		setExpanded(false);	//デフォルトは閉じた状態

		Label metaTypeCap = new Label("Type：");
		metaTypeCap.setAutoFit(true);
		metaTypeCap.setWrap(false);

		metaTypeLabel = new Label();
		metaTypeLabel.setAutoFit(true);
		metaTypeLabel.setWrap(false);
		setControls(metaTypeCap, metaTypeLabel);

		HLayout contents = new HLayout();
		contents.setHeight(20);	//これでこのSectionの高さが決まる

		attrPane = new MetaCommonAttributePane<D>(useLangSetting);
		sharedPane = new SharedSettingPane();

		contents.addMember(attrPane);
		contents.addMember(sharedPane);

		addItem(contents);


		setTargetNode(targetNode);
	}

	@Override
	public void setDefinition(D definition) {
		attrPane.setDefinition(definition);
	}

	@Override
	public D getEditDefinition(D definition) {
		return attrPane.getEditDefinition(definition);
	}

	@Override
	public boolean validate() {
		return attrPane.validate();
	}

	@Override
	public String getName() {
		//nameはDefinitionNameを返す
		return attrPane.getDefinitionName();
	}

	@Override
	public void clearErrors() {
		attrPane.clearErrors();
	}

	public void setLocalizedDisplayNameList(List<LocalizedStringDefinition> localizedDisplayNameList) {
		attrPane.setLocalizedDisplayNameList(localizedDisplayNameList);
	}
	public List<LocalizedStringDefinition> getLocalizedDisplayNameList() {
		return attrPane.getLocalizedDisplayNameList();
	}

	public void setTargetNode(MetaDataItemMenuTreeNode targetNode) {
		this.targetNode = targetNode;

		metaTypeLabel.setIcon(MetaDataUtil.getMetaTypeIcon(targetNode.isShared(), targetNode.isSharedOverwrite(), targetNode.isOverwritable()));
		metaTypeLabel.setContents(MetaDataUtil.getMetaTypeName(targetNode.isShared(), targetNode.isSharedOverwrite(), targetNode.isOverwritable()));

		sharedPane.setTargetNode(targetNode);

		if (targetNode.isShared()) {
			//Sharedの場合は変更不可
			sharedPane.setEditDisabled(true);
		}

	}

	public void setSharedEditDisabled(boolean disabled) {
		sharedPane.setEditDisabled(disabled);
	}

	public void refreshSharedConfig() {
		sharedPane.refreshSharedConfig();
	}

	public class MetaCommonAttributePane<T extends Definition> extends HLayout implements EditablePane<T> {

		private MtpForm form;

		/** 名前 */
		private TextItem nameField;
		/** 表示名 */
		private MetaDataLangTextItem displayNameField;
		/** 説明 */
		private TextAreaItem descriptionField;

		public MetaCommonAttributePane(boolean useLangSetting) {
			setMargin(5);

			form = new MtpForm();

			nameField = new MtpTextItem();
			nameField.setTitle("Name");
			SmartGWTUtil.setReadOnly(nameField);

			displayNameField = new MetaDataLangTextItem(useLangSetting);
			displayNameField.setTitle("Display Name");

			descriptionField = new MtpTextAreaItem();
			descriptionField.setTitle("Description");
			descriptionField.setHeight(55);
			descriptionField.setColSpan(2);

			form.setItems(nameField, displayNameField, descriptionField);

			addMember(form);
		}

		@Override
		public void setDefinition(T definition) {
			nameField.setValue(definition.getName());
			displayNameField.setValue(definition.getDisplayName());
			descriptionField.setValue(definition.getDescription());
		}

		@Override
		public T getEditDefinition(T definition) {
			definition.setName(SmartGWTUtil.getStringValue(nameField));
			definition.setDisplayName(SmartGWTUtil.getStringValue(displayNameField));
			definition.setDescription(SmartGWTUtil.getStringValue(descriptionField));
			return definition;
		}

		@Override
		public boolean validate() {
			return form.validate();
		}

		@Override
		public void clearErrors() {
			form.clearErrors(true);
		}

		public String getDefinitionName() {
			return SmartGWTUtil.getStringValue(nameField);
		}

		public void setLocalizedDisplayNameList(List<LocalizedStringDefinition> localizedDisplayNameList) {
			displayNameField.setLocalizedList(localizedDisplayNameList);
		}

		public List<LocalizedStringDefinition> getLocalizedDisplayNameList() {
			return displayNameField.getLocalizedList();
		}
	}

	private class SharedSettingPane extends VLayout {

		public DynamicForm form;

		public CheckboxItem sharableField;
		public CheckboxItem overwritableField;
		public CheckboxItem dataSharableField;
		public CheckboxItem permissionSharableField;
		public ButtonItem saveButton;

		public SharedSettingPane() {
			setWidth(120);
			setMargin(5);

			form = new DynamicForm();
			form.setWidth100();
			form.setNumCols(1);
			form.setColWidths("*");
			form.setIsGroup(true);
			form.setGroupTitle("Shared Setting");

			List<FormItem> items = new ArrayList<FormItem>();
			sharableField = new CheckboxItem("sharable", "CanShare");
			sharableField.setShowTitle(false);
			items.add(sharableField);
			overwritableField = new CheckboxItem("overwritable", "CanOverwrite");
			overwritableField.setShowTitle(false);
			items.add(overwritableField);

			if (sharableController.isDataSharableEnabled(defClass)) {
				dataSharableField = new CheckboxItem("dataSharable", "CanDataShare");
				dataSharableField.setShowTitle(false);
				items.add(dataSharableField);
			}
			if (sharableController.isPermissionSharableEnabled(defClass)) {
				permissionSharableField = new CheckboxItem("permissionSharable", "CanSecurityShare");
				permissionSharableField.setShowTitle(false);
				items.add(permissionSharableField);
			}

			saveButton = new ButtonItem("save", "Save");
			saveButton.setShowTitle(false);
			saveButton.setTooltip(SmartGWTUtil.getHoverString(
					AdminClientMessageUtil.getString("ui_metadata_common_MetaCommonAttributeSection_saveShareSett")));
			saveButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					updateSharedConfig();
				}
			});
			items.add(saveButton);


			form.setItems(items.toArray(new FormItem[0]));

			addMember(form);
		}

		public void setTargetNode(MetaDataItemMenuTreeNode targetNode) {

			sharableField.setValue(targetNode.isSharable());
			overwritableField.setValue(targetNode.isOverwritable());
			if (sharableController.isDataSharableEnabled(defClass)) {
				dataSharableField.setValue(targetNode.isDataSharable());
			}
			if (sharableController.isPermissionSharableEnabled(defClass)) {
				permissionSharableField.setValue(targetNode.isPermissionSharable());
			}
		}

		public void setEditDisabled(boolean disabled) {
			sharableField.setCanEdit(!disabled);
			overwritableField.setCanEdit(!disabled);
			if (sharableController.isDataSharableEnabled(defClass)) {
				dataSharableField.setCanEdit(!disabled);
			}
			if (sharableController.isPermissionSharableEnabled(defClass)) {
				permissionSharableField.setCanEdit(!disabled);
			}
			saveButton.setDisabled(disabled);
		}

		public void refreshSharedConfig() {
			service.getDefinitionInfo(TenantInfoHolder.getId(), defClass.getName(), targetNode.getDefName(), new AsyncCallback<DefinitionInfo>() {

				@Override
				public void onSuccess(DefinitionInfo result) {
					if (result != null) {
						SharedConfig config = result.getSharedConfig();
						sharableField.setValue(config.isSharable());
						overwritableField.setValue(config.isOverwritable());
						if (sharableController.isDataSharableEnabled(defClass)) {
							dataSharableField.setValue(config.isDataSharable());
						}
						if (sharableController.isPermissionSharableEnabled(defClass)) {
							permissionSharableField.setValue(config.isPermissionSharable());
						}
					} else {
						//該当なし(EntityViewやEntity FilterなどMetaデータが登録されていない場合)
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					// 失敗時
					SC.warn(AdminClientMessageUtil.getString("ui_metadata_common_MetaCommonAttributeSection_failedToSaveShareSett") + caught.getMessage());
				}
			});
		}

		private void updateSharedConfig() {
			SC.ask(AdminClientMessageUtil.getString("ui_metadata_common_MetaCommonAttributeSection_saveConfirm"),
					AdminClientMessageUtil.getString("ui_metadata_common_MetaCommonAttributeSection_saveConfirmComment"), new BooleanCallback() {

				@Override
				public void execute(Boolean value) {
					if (value) {
						final SharedConfig config = new SharedConfig(
								SmartGWTUtil.getBooleanValue(sharableField),
								SmartGWTUtil.getBooleanValue(overwritableField));

						if (sharableController.isDataSharableEnabled(defClass)) {
							config.setDataSharable(SmartGWTUtil.getBooleanValue(dataSharableField));
						}
						if (sharableController.isPermissionSharableEnabled(defClass)) {
							config.setPermissionSharable(SmartGWTUtil.getBooleanValue(permissionSharableField));
						}

						service.updateSharedConfig(TenantInfoHolder.getId(),
								defClass.getName(),
								targetNode.getDefName(),
								config,
								new AsyncCallback<Void>() {

									@Override
									public void onSuccess(Void result) {
										SC.say(AdminClientMessageUtil.getString("ui_metadata_common_MetaCommonAttributeSection_completion"),
												AdminClientMessageUtil.getString("ui_metadata_common_MetaCommonAttributeSection_shareSettCreate"));

										//TargetNodeに反映(再度表示された場合に反映するため)
										targetNode.setSharable(config.isSharable());
										targetNode.setOverwritable(config.isOverwritable());
										targetNode.setDataSharable(config.isDataSharable());
										targetNode.setPermissionSharable(config.isPermissionSharable());

										targetNode.refreshStatus();
									}

									@Override
									public void onFailure(Throwable caught) {
										// 失敗時
										SC.warn(AdminClientMessageUtil.getString("ui_metadata_common_MetaCommonAttributeSection_failedToSaveShareSett") + caught.getMessage());
									}
								});
					}
				}
			});
		}

	}

}
