/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.MetaDataItemMenuTreeNode;
import org.iplass.adminconsole.client.metadata.ui.MetaDataUtil;
import org.iplass.adminconsole.shared.metadata.dto.MetaDataConstants;
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
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;

public class MetaCommonAttributeSection extends SectionStackSection {

	//TODO Definitionにこの属性を定義したらDefinitionをInterfaceにできる・・・

	private MetaDataServiceAsync service = MetaDataServiceFactory.get();

	private MetaDataSharableController sharableController = GWT.create(MetaDataSharableController.class);

	private Label metaTypeLabel;

	private MetaCommonAttributePane attrPane;
	private SharedSettingPane sharedPane;

	private MetaDataItemMenuTreeNode targetNode;
	private Class<? extends Definition> defClass;

	public MetaCommonAttributeSection(MetaDataItemMenuTreeNode targetNode, Class<? extends Definition> defClass) {
		this(targetNode, defClass , false);
	}

	public MetaCommonAttributeSection(MetaDataItemMenuTreeNode targetNode, Class<? extends Definition> defClass, boolean useLangSetting) {
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

		attrPane = new MetaCommonAttributePane(useLangSetting);
		sharedPane = new SharedSettingPane();

		contents.addMember(attrPane);
		contents.addMember(sharedPane);

		addItem(contents);


		setTargetNode(targetNode);
	}

	public void setName(String name) {
		attrPane.nameField.setValue(name);
	}
	public String getName() {
		return SmartGWTUtil.getStringValue(attrPane.nameField);
	}
	public void setDisplayName(String displayName) {
		attrPane.displayNameField.setValue(displayName);
	}
	public String getDisplayName() {
		return SmartGWTUtil.getStringValue(attrPane.displayNameField);
	}
	public void setDescription(String description) {
		attrPane.descriptionField.setValue(description);
	}
	public String getDescription() {
		return SmartGWTUtil.getStringValue(attrPane.descriptionField);
	}
	public void setLocalizedDisplayNameList(List<LocalizedStringDefinition> localizedDisplayNameList) {
		attrPane.localizedDisplayNameList = localizedDisplayNameList;
	}
	public List<LocalizedStringDefinition> getLocalizedDisplayNameList() {
		return attrPane.localizedDisplayNameList;
	}
	public boolean validate() {
		return attrPane.form.validate();
	}
	public void clearErrors() {
		attrPane.form.clearErrors(true);
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

	public class MetaCommonAttributePane extends HLayout {

		public DynamicForm form;

		/** 名前 */
		public TextItem nameField;
		/** 表示名 */
		public TextItem displayNameField;
		/** 説明 */
		public TextAreaItem descriptionField;

		public List<LocalizedStringDefinition> localizedDisplayNameList;

		private ButtonItem langBtn;

		public MetaCommonAttributePane(boolean useLangSetting) {
//			setHeight(20);	//これでこのSectionの高さが決まる
			setMargin(5);

			form = new DynamicForm();
			form.setWidth100();
			form.setNumCols(4);	//間延びしないように最後に１つ余分に作成
			form.setColWidths(100, "*", 100, "*");

			nameField = new TextItem("name", "Name");
			nameField.setWidth(MetaDataConstants.DEFAULT_FORM_ITEM_WIDTH);
			SmartGWTUtil.setReadOnly(nameField);

			displayNameField = new TextItem("displayName", "Display Name");
			displayNameField.setWidth(MetaDataConstants.DEFAULT_FORM_ITEM_WIDTH);
			displayNameField.setStartRow(true);

			if (useLangSetting) {
				langBtn = new ButtonItem("addDisplayName", "Languages");
				langBtn.setShowTitle(false);
				langBtn.setIcon("world.png");
				langBtn.setStartRow(false);	//これを指定しないとButtonの場合、先頭にくる
				langBtn.setEndRow(false);	//これを指定しないと次のFormItemが先頭にいく
				langBtn.setPrompt(AdminClientMessageUtil.getString("ui_metadata_common_MetaCommonAttributeSection_eachLangDspName"));
				langBtn.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

					@Override
					public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {

						if (attrPane.localizedDisplayNameList == null) {
							attrPane.localizedDisplayNameList = new ArrayList<LocalizedStringDefinition>();
						}
						LocalizedStringSettingDialog dialog = new LocalizedStringSettingDialog(attrPane.localizedDisplayNameList);
						dialog.show();

					}
				});
			}

			//リサイズさせたくないが、設定できない
			descriptionField = new TextAreaItem("description", "Description");
			descriptionField.setWidth("100%");
			descriptionField.setHeight(55);
			descriptionField.setColSpan(4);
			descriptionField.setStartRow(true);

			if (useLangSetting) {
				form.setItems(nameField, displayNameField, langBtn, new SpacerItem(), descriptionField);
			} else {
				form.setItems(nameField, displayNameField, new SpacerItem(), descriptionField);
			}

			addMember(form);
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

					GWT.log(caught.toString(), caught);
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

										GWT.log(caught.toString(), caught);
									}
								});
					}
				}
			});
		}

//		private boolean isDataSharableEnabled() {
//			//TODO instanceofで判断したほうがいいいかも
//			//Entity、Cube(念のためSubClassも)の場合はデータ共有設定可
//			return (EntityDefinition.class.getName().equals(defClass.getName())
//					|| CubeDefinition.class.getName().equals(defClass.getName())
//					|| CsvCubeDefinition.class.getName().equals(defClass.getName())
//					|| EntityCubeDefinition.class.getName().equals(defClass.getName())
//					|| JdbcCubeDefinition.class.getName().equals(defClass.getName())
//					|| DerivedCubeDefinition.class.getName().equals(defClass.getName())
//					);
//		}
//
//		private boolean isPermissionSharableEnabled() {
//			//TODO instanceofで判断したほうがいいいかも
//			return (EntityDefinition.class.getName().equals(defClass.getName())
//					|| ActionMappingDefinition.class.getName().equals(defClass.getName())
//					|| WebAPIDefinition.class.getName().equals(defClass.getName())
//					|| WorkflowDefinition.class.getName().equals(defClass.getName())
//					|| CubeDefinition.class.getName().equals(defClass.getName())
//					|| CsvCubeDefinition.class.getName().equals(defClass.getName())
//					|| EntityCubeDefinition.class.getName().equals(defClass.getName())
//					|| JdbcCubeDefinition.class.getName().equals(defClass.getName())
//					|| DerivedCubeDefinition.class.getName().equals(defClass.getName())
//					);
//		}
	}
}
