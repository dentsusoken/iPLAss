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

package org.iplass.adminconsole.client.metadata.ui.menu.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataLangTextItem;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataSelectItem;
import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogConstants;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogHandler;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogMode;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpSelectItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextAreaItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.data.menu.MenuItemTreeDS;
import org.iplass.adminconsole.client.metadata.ui.MetaDataUtil;
import org.iplass.adminconsole.client.metadata.ui.common.MetaDataUpdateCallback;
import org.iplass.adminconsole.client.metadata.ui.menu.item.event.MenuItemDataChangeHandler;
import org.iplass.adminconsole.client.metadata.ui.menu.item.event.MenuItemDataChangedEvent;
import org.iplass.adminconsole.shared.metadata.dto.AdminDefinitionModifyResult;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.definition.DefinitionInfo;
import org.iplass.mtp.definition.SharedConfig;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.view.generic.EntityView;
import org.iplass.mtp.view.menu.ActionMenuItem;
import org.iplass.mtp.view.menu.EntityMenuItem;
import org.iplass.mtp.view.menu.MenuItem;
import org.iplass.mtp.view.menu.NodeMenuItem;
import org.iplass.mtp.view.menu.UrlMenuItem;
import org.iplass.mtp.web.actionmapping.definition.ActionMappingDefinition;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * メニューアイテム編集ダイアログ
 *
 */
public class MenuItemDialog extends MtpDialog {

	/** メニューアイテムタイプ */
	private MenuItemTreeDS.MenuItemType type;

	/** 更新対象メニューアイテム */
	private MenuItem curMenuItem;

	/** コピーモード */
	private Boolean isCopy;

	/** カスタマイズ用スクリプト(Action) */
	private String actionCustomizeScript;

	/** カスタマイズ用スクリプト(Entity) */
	private String entityCustomizeScript;

	/** カスタマイズ用スクリプト(Url) */
	private String urlCustomizeScript;

	/** データ変更ハンドラ */
	private List<MenuItemDataChangeHandler> handlers = new ArrayList<>();

	//共通項目
	private TextItem nameField;
	private MetaDataLangTextItem displayNameField;
	private TextAreaItem descriptionField;
	private TextItem imageUrlField;
	private TextItem iconTagField;
	private SelectItem imageColorField;

	//ActionMenuItem項目
	private DynamicForm actionForm;
	private SelectItem actionNameField;
	private TextItem actionParameterField;
	private ButtonItem actionCustomizeScriptField;

	//EntityMenuItem項目
	private DynamicForm entityForm;
	private SelectItem entityNameField;
	private TextItem entityParameterField;
	private SelectItem entityViewNameField;
	private CheckboxItem executeSearchField;
	private ButtonItem entityCustomizeScriptField;

	//UrlMenuItem項目
	private DynamicForm urlForm;
	private TextItem urlField;
	private TextItem urlParameterField;
	private CheckboxItem urlShowNewPageField;
	private ButtonItem urlCustomizeScriptField;

	private IButton save;

	private VLayout sharedPane;
	private DynamicForm sharedForm;
	private CheckboxItem sharableField;
	private CheckboxItem overwritableField;
	private ButtonItem sharedSaveButton;

	private MetaDataServiceAsync service = MetaDataServiceFactory.get();

	public MenuItemDialog(MenuItemTreeDS.MenuItemType type) {
		this.type = type;
		this.isCopy = false;

		setTitle("Create MenuItem");
		setHeight(630); //centerInPageする前に仮のHeight設定(画面下部に隠れるのを防ぐ)
		centerInPage();

		final DynamicForm commonForm = new MtpForm();

		nameField = new MtpTextItem("name","Name");
		SmartGWTUtil.setRequired(nameField);
		displayNameField = new MetaDataLangTextItem();
		displayNameField.setTitle(rs("ui_metadata_menu_item_MenuItemDialog_displayName"));

		descriptionField = new MtpTextAreaItem("description", "Description");
		descriptionField.setColSpan(2);
		descriptionField.setHeight(45);

		imageUrlField = new MtpTextItem("imageUrl", "Icon URL");
		String prompt = "<div style=\"white-space: nowrap;\">"
			+ rs("ui_metadata_menu_item_MenuItemDialog_rulesComment1")
			+ "<ul style=\"margin:5px 0px;padding-left:5px;list-style-type:none;\">"
			+ rs("ui_metadata_menu_item_MenuItemDialog_rulesComment2")
			+ rs("ui_metadata_menu_item_MenuItemDialog_rulesComment3")
			+ rs("ui_metadata_menu_item_MenuItemDialog_rulesComment4")
			+ "</ul>"
			+ "</div>";
		imageUrlField.setPrompt(prompt);

		iconTagField = new MtpTextItem("iconTag", "Icon Tag");
		prompt = "<div style=\"white-space: nowrap;\">"
			+ rs("ui_metadata_menu_item_MenuItemDialog_iconTagComment")
			+ "</div>";
		iconTagField.setPrompt(prompt);

		imageColorField = new MtpSelectItem("imageColor", "Image Color");

		commonForm.setItems(nameField, displayNameField, descriptionField, imageUrlField, iconTagField, imageColorField);

		container.addMember(commonForm);

		if (MenuItemTreeDS.MenuItemType.ACTION.equals(type)){
			actionForm = new MtpForm();
			actionForm.setIsGroup(true);
			actionForm.setGroupTitle("Action Menu Attribute");

			actionNameField = new MetaDataSelectItem(ActionMappingDefinition.class, "Execute Action");
			SmartGWTUtil.setRequired(actionNameField);

			actionParameterField = new MtpTextItem("actionParameter", "Parameter");

			actionCustomizeScriptField = new ButtonItem();
			actionCustomizeScriptField.setTitle(rs("ui_metadata_menu_item_MenuItemDialog_dynamicCustomizeSetting"));
			actionCustomizeScriptField.setStartRow(false);
			actionCustomizeScriptField.setWidth("100%");
			actionCustomizeScriptField.setPrompt(rs("ui_metadata_menu_item_MenuItemDialog_customizeScriptComment"));
			actionCustomizeScriptField.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

				@Override
				public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
					MetaDataUtil.showScriptEditDialog(ScriptEditorDialogMode.GROOVY_SCRIPT,
						actionCustomizeScript,
						ScriptEditorDialogConstants.MENU_ITEM_CUSTOMIZE_SCRIPT_NAME,
						null,
						rs("ui_metadata_menu_item_MenuItemDialog_actionCustomizeScriptHint"),
						new ScriptEditorDialogHandler() {

							@Override
							public void onSave(String text) {
								actionCustomizeScript = text;
							}

							@Override
							public void onCancel() {
							}
						}
					);
				}
			});

			actionForm.setItems(actionNameField, actionParameterField, new SpacerItem(),
					new SpacerItem(), actionCustomizeScriptField);

			container.addMember(actionForm);
		}

		if (MenuItemTreeDS.MenuItemType.ENTITY.equals(type)){
			entityForm = new MtpForm();
			entityForm.setIsGroup(true);
			entityForm.setGroupTitle("Entity Menu Attribute");

			entityNameField = new MetaDataSelectItem(EntityDefinition.class, "Entity");
			SmartGWTUtil.setRequired(entityNameField);
			entityNameField.addChangedHandler(new ChangedHandler() {
				@Override
				public void onChanged(ChangedEvent event) {
					entityViewNameField.setValue("");
					getEntityViewName();
				}
			});

			entityViewNameField = new MtpSelectItem();
			entityViewNameField.setTitle(rs("ui_metadata_menu_item_MenuItemDialog_viewName"));

			entityParameterField = new MtpTextItem("entityParameter", "Parameter");

			executeSearchField = new CheckboxItem();
			executeSearchField.setTitle(rs("ui_metadata_menu_item_MenuItemDialog_showWithExecuteSearch"));
			executeSearchField.setShowTitle(false);

			entityCustomizeScriptField = new ButtonItem();
			entityCustomizeScriptField.setTitle(rs("ui_metadata_menu_item_MenuItemDialog_dynamicCustomizeSetting"));
			entityCustomizeScriptField.setWidth("100%");
			entityCustomizeScriptField.setStartRow(false);
			entityCustomizeScriptField.setPrompt(rs("ui_metadata_menu_item_MenuItemDialog_customizeScriptComment"));
			entityCustomizeScriptField.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

				@Override
				public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
					MetaDataUtil.showScriptEditDialog(ScriptEditorDialogMode.GROOVY_SCRIPT,
						entityCustomizeScript,
						ScriptEditorDialogConstants.MENU_ITEM_CUSTOMIZE_SCRIPT_NAME,
						null,
						rs("ui_metadata_menu_item_MenuItemDialog_entityCustomizeScriptHint"),
						new ScriptEditorDialogHandler() {

							@Override
							public void onSave(String text) {
								entityCustomizeScript = text;
							}

							@Override
							public void onCancel() {
							}
						}
					);
				}
			});

			entityForm.setItems(entityNameField, entityViewNameField, entityParameterField,
					new SpacerItem(), new SpacerItem(), executeSearchField,
					new SpacerItem(), new SpacerItem(), entityCustomizeScriptField);

			container.addMember(entityForm);
		} else {
			// EntityMenuの場合のみ表示名は必須ではない
			SmartGWTUtil.setRequired(displayNameField);
		}

		if (MenuItemTreeDS.MenuItemType.URL.equals(type)){
			urlForm = new MtpForm();
			urlForm.setIsGroup(true);
			urlForm.setGroupTitle("Url Menu Attribute");

			urlField = new MtpTextItem("url", "URL");
			SmartGWTUtil.setRequired(urlField);

			urlParameterField = new MtpTextItem("urlParameter", "Parameter");

			urlShowNewPageField = new CheckboxItem();
			urlShowNewPageField.setTitle(rs("ui_metadata_menu_item_MenuItemDialog_showNewPage"));
			urlShowNewPageField.setShowTitle(false);

			urlCustomizeScriptField = new ButtonItem();
			urlCustomizeScriptField.setTitle(rs("ui_metadata_menu_item_MenuItemDialog_dynamicCustomizeSetting"));
			urlCustomizeScriptField.setWidth("100%");
			urlCustomizeScriptField.setStartRow(false);
			urlCustomizeScriptField.setPrompt(rs("ui_metadata_menu_item_MenuItemDialog_customizeScriptComment"));
			urlCustomizeScriptField.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

				@Override
				public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
					MetaDataUtil.showScriptEditDialog(ScriptEditorDialogMode.GROOVY_SCRIPT,
						urlCustomizeScript,
						ScriptEditorDialogConstants.MENU_ITEM_CUSTOMIZE_SCRIPT_NAME,
						null,
						rs("ui_metadata_menu_item_MenuItemDialog_urlCustomizeScriptHint"),
						new ScriptEditorDialogHandler() {

							@Override
							public void onSave(String text) {
								urlCustomizeScript = text;
							}

							@Override
							public void onCancel() {
							}
						}
					);
				}
			});

			urlForm.setItems(urlField, urlParameterField,
					new SpacerItem(), new SpacerItem(), urlShowNewPageField,
					new SpacerItem(), new SpacerItem(), urlCustomizeScriptField);

			container.addMember(urlForm);
		}

		//共有設定部
		sharedPane = new VLayout(5);
		sharedPane.setAlign(Alignment.CENTER);
		sharedPane.setVisible(false);

		sharedForm = new DynamicForm();
		sharedForm.setMargin(10);
		sharedForm.setPadding(5);
		sharedForm.setWidth100();
		sharedForm.setAlign(Alignment.CENTER);
		sharedForm.setIsGroup(true);
		sharedForm.setGroupTitle("Shared Setting：");
		sharedForm.setNumCols(4);	//間延びしないように最後に１つ余分に作成

		sharableField = new CheckboxItem("sharable", "CanShare");
		sharableField.setShowTitle(false);
		overwritableField = new CheckboxItem("overwritable", "CanOverwrite");
		overwritableField.setShowTitle(false);
		sharedSaveButton = new ButtonItem("sharedSave", "Save");
		sharedSaveButton.setShowTitle(false);
		sharedSaveButton.setStartRow(false);
		sharedSaveButton.setTooltip(SmartGWTUtil.getHoverString(rs("ui_metadata_menu_item_MenuItemDialog_saveShareSettBr")));
		sharedSaveButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			@Override
			public void onClick(
					com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				updateSharedConfig();
			}
		});
		sharedForm.setItems(sharableField, overwritableField, sharedSaveButton);
		sharedPane.addMember(sharedForm);

		container.addMember(sharedPane);

		save = new IButton("Save");
		save.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				//最後に実行したvalidateのエラー箇所にフォーカスがいくので逆から実行
				boolean isEntityValidate = (entityForm != null ? entityForm.validate() : true);
				boolean isActionValidate = (actionForm != null ? actionForm.validate() : true);
				boolean isCommonValidate = commonForm.validate();
				if (isCommonValidate && isActionValidate && isEntityValidate){
					saveMenuItem();
				}
			}
		});

		IButton cancel = new IButton("Cancel");
		cancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				destroy();
			}
		});

		footer.setMembers(save, cancel);

		//タイプ別画面調整
		setTitleSuffix();
		setItemTypeDisplay(false);

		service.getImageColorList(TenantInfoHolder.getId(), new AsyncCallback<List<String>>() {

			@Override
			public void onSuccess(List<String> result) {
				List<String> imageColors = new ArrayList<>();
				imageColors.add("");
				imageColors.addAll(result);
				imageColorField.setValueMap(imageColors.toArray(new String[imageColors.size()]));
			}

			@Override
			public void onFailure(Throwable caught) {
				SC.warn(rs("ui_metadata_menu_item_MenuItemDialog_failedGetScreenInfo") + caught.getMessage());
				GWT.log(caught.toString(), caught);
			}
		});
	}

	/**
	 * 更新対象のメニューアイテムを設定します。
	 *
	 * @param menuItem 更新対象{@link MenuItem}
	 */
	public void setMenuItem(final MenuItem menuItem, final boolean isCopy) {

		//SmartGWTUtil.showProgress();
		service.getDefinition(TenantInfoHolder.getId(), MenuItem.class.getName(), menuItem.getName(), new AsyncCallback<MenuItem>() {

			@Override
			public void onFailure(Throwable caught) {
				//SmartGWTUtil.hideProgress();
				SC.warn(rs("ui_metadata_menu_item_MenuItemDialog_failedGetScreenInfo") + caught.getMessage());
				GWT.log(caught.toString(), caught);
			}

			@Override
			public void onSuccess(MenuItem current) {
				MenuItemDialog.this.curMenuItem = current;
				MenuItemDialog.this.isCopy = isCopy;

				if (isCopy) {
					//タイトルの変更
					setTitle("Copy MenuItem");
					nameField.setValue(current.getName() + "_Copy");
					if (current.getDisplayName() != null) {
						displayNameField.setValue(current.getDisplayName());
					}
					displayNameField.setLocalizedList(current.getLocalizedDisplayNameList());
				} else {
					//タイトルの変更
					setTitle("Edit MenuItem");
					SmartGWTUtil.setReadOnly(nameField);
					nameField.setValue(current.getName());
					displayNameField.setValue(current.getDisplayName());
					displayNameField.setLocalizedList(current.getLocalizedDisplayNameList());
				}
				descriptionField.setValue(current.getDescription());
				imageUrlField.setValue(current.getImageUrl());
				iconTagField.setValue(current.getIconTag());

				if (current.getImageColor() != null) {
					imageColorField.setValue(menuItem.getImageColor());
				}
				if (MenuItemTreeDS.MenuItemType.ACTION.equals(type)){
					setMenuItem((ActionMenuItem)current);
				} else if (MenuItemTreeDS.MenuItemType.ENTITY.equals(type)){
					setMenuItem((EntityMenuItem)current);
				} else if (MenuItemTreeDS.MenuItemType.URL.equals(type)) {
					setMenuItem((UrlMenuItem)current);
				}

				setTitleSuffix();

				setSharedConfig();

				//SmartGWTUtil.hideProgress();
			}
		});
	}

	public void setInitialName(String name) {
		nameField.setValue(name);
	}

	/**
	 * MenuItemDataChangeHandlerを追加します。
	 *
	 * @param handler {@link MenuItemDataChangeHandler}
	 */
	public void addMenuItemDataChangeHandler(MenuItemDataChangeHandler handler) {
		handlers.add(0, handler);
	}


	/**
	 * タイプ別の画面サイズを調整します。
	 */
	private void setItemTypeDisplay(boolean isShowSharedPane) {

		int height = 300;
		if (MenuItemTreeDS.MenuItemType.ACTION.equals(type)){
			height = 420;
		} else if (MenuItemTreeDS.MenuItemType.ENTITY.equals(type)){
			height = 460;
		} else if (MenuItemTreeDS.MenuItemType.URL.equals(type)) {
			height = 440;
		}
		if (isShowSharedPane) {
			height += 90;
		}
		setHeight(height);
	}

	/**
	 * タイトルの接尾語を設定します。
	 */
	private void setTitleSuffix() {
		if (MenuItemTreeDS.MenuItemType.NODE.equals(type)){
			setTitle(getTitle() + " : Node Item");
		} else if (MenuItemTreeDS.MenuItemType.ACTION.equals(type)){
			setTitle(getTitle() + " : Action Item");
		} else if (MenuItemTreeDS.MenuItemType.ENTITY.equals(type)){
			setTitle(getTitle() + " : Entity Item");
		} else if (MenuItemTreeDS.MenuItemType.URL.equals(type)) {
			setTitle(getTitle() + " : Url Item");
		}
	}


	/**
	 * ActionMenuItemに特化した値を設定します。
	 *
	 * @param item {@link ActionMenuItem}
	 */
	private void setMenuItem(ActionMenuItem item) {

		actionNameField.setValue(item.getActionName());
		actionParameterField.setValue(item.getParameter());
		actionCustomizeScript = item.getCustomizeScript();
	}

	/**
	 * EntityMenuItemに特化した値を設定します。
	 *
	 * @param item {@link EntityMenuItem}
	 */
	private void setMenuItem(EntityMenuItem item) {

		entityNameField.setValue(item.getEntityDefinitionName());
		entityParameterField.setValue(item.getParameter());
		entityViewNameField.setValue(item.getViewName());
		executeSearchField.setValue(item.isExecuteSearch());
		entityCustomizeScript = item.getCustomizeScript();

		getEntityViewName();
	}

	/**
	 * UrlMenuItemに特化した値を設定します。
	 *
	 * @param item {@link UrlMenuItem}
	 */
	private void setMenuItem(UrlMenuItem item) {

		urlField.setValue(item.getUrl());
		urlParameterField.setValue(item.getParameter());
		urlShowNewPageField.setValue(item.isShowNewPage());
		urlCustomizeScript = item.getCustomizeScript();
	}

	/**
	 * 保存処理
	 */
	private void saveMenuItem() {

		if (isNew()) {
			//新規登録、またはコピー
			//同じ名前の存在チェックする
			service.getDefinition(TenantInfoHolder.getId(), MenuItem.class.getName(), SmartGWTUtil.getStringValue(nameField), new AsyncCallback<MenuItem>() {

				@Override
				public void onFailure(Throwable caught) {
					SC.warn(rs("ui_metadata_menu_item_MenuItemDialog_failedToSaveMenuItem") + caught.getMessage());
				}

				@Override
				public void onSuccess(MenuItem menuItem) {
					if (menuItem != null) {
						SC.warn(rs("ui_metadata_common_MetaDataCreateDialog_alreadyExistsErr",
								rs("ui_metadata_menu_MenuPluginManager_menuItem")));
					} else {
						createMenuItem();
					}
				}

			});
		} else {
			//更新処理
			updateMenuItem();
		}
	}

	private boolean isNew() {
		return (curMenuItem == null || isCopy);
	}

	/**
	 * メニューアイテム追加処理
	 */
	private void createMenuItem() {
		final MenuItem menuItem = getEditMenuItem();

		SmartGWTUtil.showSaveProgress();
		service.createMenuItem(TenantInfoHolder.getId(), menuItem, new MetaDataUpdateCallback() {

			@Override
			protected void overwriteUpdate() {
			}

			@Override
			protected void afterUpdate(AdminDefinitionModifyResult result) {
				SC.say(rs("ui_metadata_menu_item_MenuItemDialog_createMenuItemComp"), new BooleanCallback() {

					@Override
					public void execute(Boolean value) {
						//ダイアログ消去
						destroy();

						//データ変更を通知
						fireDataChanged(MenuItemDataChangedEvent.Type.ADD, menuItem);
					}
				});
			}
		});
	}

	/**
	 * メニューアイテム更新処理
	 */
	private void updateMenuItem() {
		final MenuItem menuItem = getEditMenuItem();

		SmartGWTUtil.showSaveProgress();
		service.updateMenuItem(TenantInfoHolder.getId(), menuItem, new MetaDataUpdateCallback() {

			@Override
			protected void overwriteUpdate() {
				//TODO version check
			}

			@Override
			protected void afterUpdate(AdminDefinitionModifyResult result) {
				SC.say(rs("ui_metadata_menu_item_MenuItemDialog_saveMenuItem"), new BooleanCallback() {

					@Override
					public void execute(Boolean value) {
						//ダイアログ消去
						destroy();

						//データ変更を通知
						fireDataChanged(MenuItemDataChangedEvent.Type.UPDATE, menuItem);
					}
				});
			}
		});
	}

	/**
	 * 編集結果メニューアイテムを返します。
	 */
	private MenuItem getEditMenuItem() {
		MenuItem menuItem = null;
		if (MenuItemTreeDS.MenuItemType.NODE.equals(type)){
			menuItem = getEditNodeMenuItem();
		} else if (MenuItemTreeDS.MenuItemType.ACTION.equals(type)){
			menuItem = getEditActionMenuItem();
		} else if (MenuItemTreeDS.MenuItemType.ENTITY.equals(type)){
			menuItem = getEditEntityMenuItem();
		} else if (MenuItemTreeDS.MenuItemType.URL.equals(type)){
			menuItem = getEditUrlMenuItem();
		}
		return menuItem;
	}

	/**
	 * 編集結果Nodeメニューアイテムを返します。
	 */
	private MenuItem getEditNodeMenuItem() {
		NodeMenuItem item = new NodeMenuItem();
		setCommonValues(item);
		return item;
	}

	/**
	 * 編集結果Actionメニューアイテムを返します。
	 */
	private MenuItem getEditActionMenuItem() {
		ActionMenuItem item = new ActionMenuItem();
		setCommonValues(item);
		item.setActionName(SmartGWTUtil.getStringValue(actionNameField));
		item.setParameter(SmartGWTUtil.getStringValue(actionParameterField));
		item.setCustomizeScript(actionCustomizeScript);

		return item;
	}

	/**
	 * 編集結果Entityメニューアイテムを返します。
	 */
	private MenuItem getEditEntityMenuItem() {
		EntityMenuItem item = new EntityMenuItem();
		setCommonValues(item);
		item.setEntityDefinitionName(SmartGWTUtil.getStringValue(entityNameField));
		item.setParameter(SmartGWTUtil.getStringValue(entityParameterField));
		String viewName = SmartGWTUtil.getStringValue(entityViewNameField);
		if (SmartGWTUtil.isEmpty(viewName)) {
			item.setViewName(null);
		} else {
			item.setViewName(viewName);
		}
		item.setExecuteSearch(SmartGWTUtil.getBooleanValue(executeSearchField));
		item.setCustomizeScript(entityCustomizeScript);
		return item;
	}

	/**
	 * 編集結果Urlメニューアイテムを返します。
	 */
	private MenuItem getEditUrlMenuItem() {
		UrlMenuItem item = new UrlMenuItem();
		setCommonValues(item);
		item.setUrl(SmartGWTUtil.getStringValue(urlField));
		item.setParameter(SmartGWTUtil.getStringValue(urlParameterField));
		item.setShowNewPage(SmartGWTUtil.getBooleanValue(urlShowNewPageField));
		item.setCustomizeScript(urlCustomizeScript);

		return item;
	}

	/**
	 * 共通編集項目をセットします。
	 */
	private void setCommonValues(MenuItem item) {
		item.setName(SmartGWTUtil.getStringValue(nameField));
		item.setDisplayName(SmartGWTUtil.getStringValue(displayNameField));
		item.setLocalizedDisplayNameList(displayNameField.getLocalizedList());
		item.setDescription(SmartGWTUtil.getStringValue(descriptionField));
		item.setImageUrl(SmartGWTUtil.getStringValue(imageUrlField));
		item.setIconTag(SmartGWTUtil.getStringValue(iconTagField));
		item.setImageColor(SmartGWTUtil.getStringValue(imageColorField));
	}

	/**
	 * データの変更を通知します。
	 *
	 * @param type イベントタイプ {@link MenuItemDataChangedEvent.Type}
	 * @param item 更新 {@link MenuItem}
	 */
	private void fireDataChanged(MenuItemDataChangedEvent.Type type, MenuItem item) {
		//イベントに更新MenuItemをセットして発行する
		MenuItemDataChangedEvent event = new MenuItemDataChangedEvent(type);
		event.setValueObject(item);
		for (MenuItemDataChangeHandler handler : handlers) {
			handler.onDataChanged(event);
		}
	}


	private void setSharedConfig() {
		//新規でないときのみ検索して設定
		if (!isNew()) {
			//MenuItem.class.getName()でないと駄目。curMenuItem.getClass().getName()だと取れない
			service.getDefinitionInfo(TenantInfoHolder.getId(), MenuItem.class.getName(), curMenuItem.getName(), new AsyncCallback<DefinitionInfo>() {

				@Override
				public void onSuccess(DefinitionInfo result) {
					if (result != null) {
						SharedConfig config = result.getSharedConfig();

						setItemTypeDisplay(true);

						String iconHTML = Canvas.imgHTML(
								MetaDataUtil.getMetaTypeIcon(result.isShared(), result.isSharedOverwrite(), config.isOverwritable())
						);
						String caption = MetaDataUtil.getMetaTypeName(result.isShared(), result.isSharedOverwrite(), config.isOverwritable());
						sharedForm.setGroupTitle(sharedForm.getGroupTitle() + "<span>" + iconHTML + "&nbsp;" + caption + "</span>");

						sharableField.setValue(config.isSharable());
						overwritableField.setValue(config.isOverwritable());

						//Sharedの場合は共有設定変更不可
						if (result.isShared()) {
							sharedSaveButton.setDisabled(true);
						}

						//Sharedでかつ上書き不可の場合は保存不可
						if (result.isShared() && !config.isOverwritable()) {
							save.setDisabled(true);
						}

						sharedPane.setVisible(true);
					} else {
						SC.warn(rs("ui_metadata_menu_item_MenuItemDialog_failedToGetShareSettNotFoundData"));
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					SC.warn(rs("ui_metadata_menu_item_MenuItemDialog_failedToGetShareSett") + caught.getMessage());
				}
			});
		}
	}

	private void updateSharedConfig() {
		SC.ask(rs("ui_metadata_menu_item_MenuItemDialog_savesharedSettComment"), new BooleanCallback() {

			@Override
			public void execute(Boolean value) {
				if (value) {
					SharedConfig config = new SharedConfig(
							SmartGWTUtil.getBooleanValue(sharableField),
							SmartGWTUtil.getBooleanValue(overwritableField));

					service.updateSharedConfig(TenantInfoHolder.getId(),
							MenuItem.class.getName(),
							curMenuItem.getName(),
							config,
							new AsyncCallback<Void>() {

								@Override
								public void onSuccess(Void result) {
									SC.say(rs("ui_metadata_menu_item_MenuItemDialog_saveShareSett"));
								}

								@Override
								public void onFailure(Throwable caught) {
									// 失敗時
									SC.warn(rs("ui_metadata_menu_item_MenuItemDialog_failedToSaveShareSett") + caught.getMessage());
								}
							});
				}
			}
		});
	}

	private void getEntityViewName() {
		String defName = SmartGWTUtil.getStringValue(entityNameField);

		if (SmartGWTUtil.isEmpty(defName)) {
			entityViewNameField.setValueMap(Collections.emptyMap());
		} else {
			service.getDefinition(TenantInfoHolder.getId(), EntityView.class.getName(), defName,
					new AsyncCallback<EntityView>() {

				@Override
				public void onSuccess(EntityView entityView) {

					Set<String> viewNames = new HashSet<>();
					if (entityView != null) {
						//登録SearchFormを追加
						viewNames.addAll(Arrays.asList(entityView.getSearchFormViewNames()));

						//管理設定がある場合、自動生成分のみ追加
						if (entityView.getViewControlSettings() != null) {
							viewNames.addAll(
									entityView.getViewControlSettings().stream()
										.filter(setting -> setting.isAutoGenerateSearchView())
										.map(setting -> setting.getName() != null ? setting.getName() : "")
										.collect(Collectors.toSet()));
						}
					}

					//１つもない場合はデフォルト追加
					if (viewNames.isEmpty()) {
						viewNames.add("");
					}

					//ソートして、Map生成
					Map<String, String> valueMap = viewNames.stream()
							.sorted(Comparator.nullsFirst(Comparator.naturalOrder()))
							.collect(Collectors.toMap(viewName -> viewName, viewName -> {
								return viewName.isEmpty() ? "(default)" : viewName;
							}));
					entityViewNameField.setValueMap(valueMap);
				}

				@Override
				public void onFailure(Throwable caught) {
					SC.warn(rs("ui_metadata_menu_item_MenuItemDialog_failedGetScreenInfo") + caught.getMessage());
					GWT.log(caught.toString(), caught);
				}
			});
		}
	}

	private String rs(String key, Object... arguments) {
		return AdminClientMessageUtil.getString(key, arguments);
	}

}
