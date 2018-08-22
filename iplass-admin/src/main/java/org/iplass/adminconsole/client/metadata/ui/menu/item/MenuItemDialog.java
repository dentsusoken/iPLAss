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

package org.iplass.adminconsole.client.metadata.ui.menu.item;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogConstants;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogHandler;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogMode;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.data.MetaDataNameDS;
import org.iplass.adminconsole.client.metadata.data.entity.EntityDS;
import org.iplass.adminconsole.client.metadata.data.menu.MenuItemTreeDS;
import org.iplass.adminconsole.client.metadata.ui.MetaDataUtil;
import org.iplass.adminconsole.client.metadata.ui.common.LocalizedStringSettingDialog;
import org.iplass.adminconsole.client.metadata.ui.common.MetaDataUpdateCallback;
import org.iplass.adminconsole.client.metadata.ui.menu.item.event.MenuItemDataChangeHandler;
import org.iplass.adminconsole.client.metadata.ui.menu.item.event.MenuItemDataChangedEvent;
import org.iplass.adminconsole.shared.metadata.dto.AdminDefinitionModifyResult;
import org.iplass.adminconsole.shared.metadata.dto.MetaDataConstants;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.definition.DefinitionInfo;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.definition.SharedConfig;
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
import com.smartgwt.client.types.VerticalAlignment;
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
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * メニューアイテム編集ダイアログ
 *
 */
public class MenuItemDialog extends AbstractWindow {

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
	private List<MenuItemDataChangeHandler> handlers = new ArrayList<MenuItemDataChangeHandler>();

	//共通項目
	private TextItem nameField;
	private TextItem displayNameField;
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

	public List<LocalizedStringDefinition> localizedDisplayNameList;

	private ButtonItem langBtn;

	private MetaDataServiceAsync service = MetaDataServiceFactory.get();

	public MenuItemDialog(MenuItemTreeDS.MenuItemType type) {
		this.type = type;
		this.isCopy = false;

		setHeight(630); //centerInPageする前に仮のHeight設定(画面下部に隠れるのを防ぐ)

		setTitle("Create MenuItem");
		setShowMinimizeButton(false);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();


		VLayout contents = new VLayout(5);
		//contents.setAlign(VerticalAlignment.CENTER);
		contents.setAlign(Alignment.CENTER);

		final DynamicForm commonForm = new DynamicForm();
		commonForm.setMargin(10);
		commonForm.setWidth100();
		commonForm.setNumCols(3);

		nameField = new TextItem("name","Name");
		nameField.setWidth(MetaDataConstants.DEFAULT_FORM_ITEM_WIDTH);
		nameField.setColSpan(3);
		SmartGWTUtil.setRequired(nameField);
		displayNameField = new TextItem("displayName", "Display Name");
		displayNameField.setWidth(212);

		langBtn = new ButtonItem("addDisplayName", "Languages");
		langBtn.setShowTitle(false);
		langBtn.setIcon("world.png");
		langBtn.setStartRow(false);	//これを指定しないとButtonの場合、先頭にくる
		langBtn.setEndRow(false);	//これを指定しないと次のFormItemが先頭にいく
		langBtn.setPrompt(getRS("eachLangDspName"));
		langBtn.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				if (localizedDisplayNameList == null) {
					localizedDisplayNameList = new ArrayList<LocalizedStringDefinition>();
				}
				LocalizedStringSettingDialog dialog = new LocalizedStringSettingDialog(localizedDisplayNameList);
				dialog.show();
			}
		});

		descriptionField = new TextAreaItem("description", "Description");
		descriptionField.setWidth(MetaDataConstants.DEFAULT_FORM_ITEM_WIDTH);
		descriptionField.setColSpan(3);
		descriptionField.setHeight(45);

		imageUrlField = new TextItem("imageUrl", "Icon URL");
		imageUrlField.setWidth(MetaDataConstants.DEFAULT_FORM_ITEM_WIDTH);
		String prompt = "<div style=\"white-space: nowrap;\">"
			+ getRS("rulesComment1")
			+ "<ul style=\"margin:5px 0px;padding-left:5px;list-style-type:none;\">"
			+ getRS("rulesComment2")
			+ getRS("rulesComment3")
			+ getRS("rulesComment4")
			+ "</ul>"
			+ "</div>";
		imageUrlField.setPrompt(prompt);
		imageUrlField.setColSpan(3);

		iconTagField = new TextItem("iconTag", "Icon Tag");
		iconTagField.setWidth(MetaDataConstants.DEFAULT_FORM_ITEM_WIDTH);
		prompt = "<div style=\"white-space: nowrap;\">"
			+ getRS("iconTagComment")
			+ "</div>";
		iconTagField.setPrompt(prompt);
		iconTagField.setColSpan(3);

		imageColorField = new SelectItem("imageColor", "Image Color");
		imageColorField.setColSpan(3);

		commonForm.setItems(nameField, displayNameField, langBtn, descriptionField, imageUrlField, iconTagField, imageColorField);

		contents.addMember(commonForm);

		if (MenuItemTreeDS.MenuItemType.ACTION.equals(type)){
			actionForm = new DynamicForm();
			actionForm.setMargin(10);
			actionForm.setPadding(5);
			actionForm.setWidth100();
			actionForm.setAlign(Alignment.CENTER);
			actionForm.setIsGroup(true);
			actionForm.setGroupTitle("Action Menu Attribute");

			actionNameField = new SelectItem("actionName", "Execute Action");
			actionNameField.setWidth(300);
			SmartGWTUtil.setRequired(actionNameField);

			actionParameterField = new TextItem("actionParameter", "Parameter");
			actionParameterField.setWidth(300);

			actionCustomizeScriptField = new ButtonItem();
			actionCustomizeScriptField.setTitle("Dynamic Customize Setting");
			actionCustomizeScriptField.setWidth("100%");
			actionCustomizeScriptField.setColSpan(2);
			actionCustomizeScriptField.setPrompt(getRS("customizeScriptComment"));
			actionCustomizeScriptField.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

				@Override
				public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
					MetaDataUtil.showScriptEditDialog(ScriptEditorDialogMode.GROOVY_SCRIPT,
						actionCustomizeScript,
						ScriptEditorDialogConstants.MENU_ITEM_CUSTOMIZE_SCRIPT_NAME,
						null,
						getRS("actionCustomizeScriptHint"),
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

			actionForm.setItems(actionNameField, actionParameterField, actionCustomizeScriptField);

			contents.addMember(actionForm);
		}

		if (MenuItemTreeDS.MenuItemType.ENTITY.equals(type)){
			entityForm = new DynamicForm();
			entityForm.setMargin(10);
			entityForm.setPadding(5);
			entityForm.setWidth100();
			entityForm.setAlign(Alignment.CENTER);
			entityForm.setIsGroup(true);
			entityForm.setGroupTitle("Entity Menu Attribute");

			entityNameField = new SelectItem("entityName", "Entity");
			entityNameField.setWidth(300);
			SmartGWTUtil.setRequired(entityNameField);
			entityNameField.addChangedHandler(new ChangedHandler() {
				@Override
				public void onChanged(ChangedEvent event) {
					entityViewNameField.setValue("");
					getEntityViewName();
				}
			});

			entityViewNameField = new SelectItem();
			entityViewNameField.setTitle("View Name");
			entityViewNameField.setWidth(300);

			entityParameterField = new TextItem("entityParameter", "Parameter");
			entityParameterField.setWidth(300);

			executeSearchField = new CheckboxItem();
			executeSearchField.setTitle("show with execute search");
			executeSearchField.setShowTitle(false);
			executeSearchField.setColSpan(2);

			entityCustomizeScriptField = new ButtonItem();
			entityCustomizeScriptField.setTitle("Dynamic Customize Setting");
			entityCustomizeScriptField.setWidth("100%");
			entityCustomizeScriptField.setColSpan(2);
			entityCustomizeScriptField.setPrompt(getRS("customizeScriptComment"));
			entityCustomizeScriptField.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

				@Override
				public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
					MetaDataUtil.showScriptEditDialog(ScriptEditorDialogMode.GROOVY_SCRIPT,
						entityCustomizeScript,
						ScriptEditorDialogConstants.MENU_ITEM_CUSTOMIZE_SCRIPT_NAME,
						null,
						getRS("entityCustomizeScriptHint"),
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

			entityForm.setItems(entityNameField, entityViewNameField, entityParameterField, executeSearchField, entityCustomizeScriptField);

			contents.addMember(entityForm);
		} else {

			// EntityMenuの場合のみ表示名は必須ではない
			SmartGWTUtil.setRequired(displayNameField);
		}

		if (MenuItemTreeDS.MenuItemType.URL.equals(type)){
			urlForm = new DynamicForm();
			urlForm.setMargin(10);
			urlForm.setPadding(5);
			urlForm.setWidth100();
			urlForm.setAlign(Alignment.CENTER);
			urlForm.setIsGroup(true);
			urlForm.setGroupTitle("Url Menu Attribute");

			urlField = new TextItem("url", "URL");
			urlField.setWidth(300);
			SmartGWTUtil.setRequired(urlField);

			urlParameterField = new TextItem("urlParameter", "Parameter");
			urlParameterField.setWidth(300);

			urlShowNewPageField = new CheckboxItem();
			urlShowNewPageField.setTitle("show new page");
			urlShowNewPageField.setShowTitle(false);
			urlShowNewPageField.setColSpan(2);

			urlCustomizeScriptField = new ButtonItem();
			urlCustomizeScriptField.setTitle("Dynamic Customize Setting");
			urlCustomizeScriptField.setWidth("100%");
			urlCustomizeScriptField.setColSpan(2);
			urlCustomizeScriptField.setPrompt(getRS("customizeScriptComment"));
			urlCustomizeScriptField.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

				@Override
				public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
					MetaDataUtil.showScriptEditDialog(ScriptEditorDialogMode.GROOVY_SCRIPT,
						urlCustomizeScript,
						ScriptEditorDialogConstants.MENU_ITEM_CUSTOMIZE_SCRIPT_NAME,
						null,
						getRS("urlCustomizeScriptHint"),
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

			urlForm.setItems(urlField, urlParameterField, urlShowNewPageField, urlCustomizeScriptField);

			contents.addMember(urlForm);
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
		sharedSaveButton.setTooltip(SmartGWTUtil.getHoverString(getRS("saveShareSettBr")));
		sharedSaveButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			@Override
			public void onClick(
					com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				updateSharedConfig();
			}
		});
		sharedForm.setItems(sharableField, overwritableField, sharedSaveButton);
		sharedPane.addMember(sharedForm);

		//フッター部
		HLayout footer = new HLayout(5);
		footer.setMargin(5);
		footer.setHeight(20);
		footer.setWidth100();
		//footer.setAlign(Alignment.LEFT);
		footer.setAlign(VerticalAlignment.CENTER);

		save = new IButton("Save");
		save.addClickHandler(new ClickHandler() {
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
			public void onClick(ClickEvent event) {
				destroy();
			}
		});

		footer.setMembers(save, cancel);

		addItem(contents);
		addItem(sharedPane);
		addItem(SmartGWTUtil.separator());
		addItem(footer);

		//タイプ別画面調整
		setTitleSuffix();
		setItemTypeDisplay(false);

		service.getImageColorList(TenantInfoHolder.getId(), new AsyncCallback<List<String>>() {

			@Override
			public void onSuccess(List<String> result) {
				List<String> imageColors = new ArrayList<String>();
				imageColors.add("");
				imageColors.addAll(result);
				imageColorField.setValueMap(imageColors.toArray(new String[imageColors.size()]));
			}

			@Override
			public void onFailure(Throwable caught) {
				SC.warn(getRS("failedGetScreenInfo") + caught.getMessage());
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
				SC.warn(getRS("failedGetScreenInfo") + caught.getMessage());
				GWT.log(caught.toString(), caught);
			}

			@Override
			public void onSuccess(MenuItem current) {
				MenuItemDialog.this.curMenuItem = current;
				MenuItemDialog.this.isCopy = isCopy;
				MenuItemDialog.this.localizedDisplayNameList = current.getLocalizedDisplayNameList();

				if (isCopy) {
					//タイトルの変更
					setTitle("(Copy)" + current.getDisplayName());
					nameField.setValue(current.getName() + "_Copy");
					displayNameField.setValue(current.getDisplayName() + "_Copy");
				} else {
					//タイトルの変更
					setTitle(current.getDisplayName());
					SmartGWTUtil.setReadOnly(nameField);
					nameField.setValue(current.getName());
					displayNameField.setValue(current.getDisplayName());
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
		setWidth(450);

//		List<String> imageColors = new ArrayList<String>();
//		imageColors.add("");
//		for (int i = 0; i < ImageColor.values().length; i++) { FIXME
//			imageColors.add(ImageColor.values()[i].name());
//		}
//		imageColorField.setValueMap(imageColors.toArray(new String[imageColors.size()]));

		int height = 300;
		if (MenuItemTreeDS.MenuItemType.ACTION.equals(type)){
			height = 420;
			//Action名コンボデータ作成
			MetaDataNameDS.setDataSource(actionNameField, ActionMappingDefinition.class);
		} else if (MenuItemTreeDS.MenuItemType.ENTITY.equals(type)){
			height = 460;
			//Entity名コンボデータ作成
			EntityDS.setDataSource(entityNameField);
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
			//同じ名前の場合、上書きされてしまうため存在チェックする
			service.getDefinition(TenantInfoHolder.getId(), MenuItem.class.getName(), SmartGWTUtil.getStringValue(nameField), new AsyncCallback<MenuItem>() {

				@Override
				public void onFailure(Throwable caught) {
					SC.warn(getRS("failedToSaveMenuItem") + caught.getMessage());
				}

				@Override
				public void onSuccess(MenuItem menuItem) {
					if (menuItem != null) {
						SC.ask(getRS("alreadyExistsMenuItemRecreate"),
								new BooleanCallback() {
							@Override
							public void execute(Boolean value) {
								if (value) {
									createMenuItem();
								}
							}
						});
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
				SC.say(getRS("completion"), getRS("createMenuItemComp"), new BooleanCallback() {

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
				SC.say(getRS("completion"), getRS("saveMenuItem"), new BooleanCallback() {

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
		item.setDescription(SmartGWTUtil.getStringValue(descriptionField));
		item.setImageUrl(SmartGWTUtil.getStringValue(imageUrlField));
		item.setIconTag(SmartGWTUtil.getStringValue(iconTagField));
		item.setLocalizedDisplayNameList(localizedDisplayNameList);
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
						SC.warn(getRS("failedToGetShareSettNotFoundData"));
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					SC.warn(getRS("failedToGetShareSett") + caught.getMessage());
				}
			});
		}
	}

	private void updateSharedConfig() {
		SC.ask(getRS("saveConfirm"), getRS("savesharedSettComment"), new BooleanCallback() {

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
									SC.say(getRS("completion"), getRS("saveShareSett"));
								}

								@Override
								public void onFailure(Throwable caught) {
									// 失敗時
									SC.warn(getRS("failedToSaveShareSett") + caught.getMessage());
								}
							});
				}
			}
		});
	}

	private void getEntityViewName() {
		String defName = SmartGWTUtil.getStringValue(entityNameField);

		final LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
		if (SmartGWTUtil.isEmpty(defName)) {
			entityViewNameField.setValueMap(valueMap);
		} else {
			service.getDefinition(TenantInfoHolder.getId(), EntityView.class.getName(), defName,
					new AsyncCallback<EntityView>() {

				@Override
				public void onSuccess(EntityView result) {
					valueMap.put("", "default");
					if (result != null) {
						for (String viewName : result.getSearchFormViewNames()) {
							if (!viewName.isEmpty()) {
								valueMap.put(viewName, viewName);
							}
						}
					}
					entityViewNameField.setValueMap(valueMap);
				}

				@Override
				public void onFailure(Throwable caught) {
					SC.say(getRS("failed"), getRS("failedGetScreenInfo") + caught.getMessage());
					GWT.log(caught.toString(), caught);
				}
			});
		}
	}

	private String getRS(String key) {
		return AdminClientMessageUtil.getString("ui_metadata_menu_item_MenuItemDialog_" + key);
	}

}
