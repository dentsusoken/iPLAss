/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.entity.layout;

import java.util.LinkedHashMap;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * EntityViewメニュー部分のレイアウト
 */
public class EntityViewMenuPane extends VLayout {

	public static final String DEFAULT_VIEW_NAME = "(default)";

	/** View選択用のプルダウン */
	private SelectItem viewSelect;
	/** 保存ボタン */
	private IButton save;
	/** 表示ボタン */
	private IButton display;
	/** 追加ボタン */
	private IButton add;
	/** 削除ボタン */
	private IButton delete;
	/** 標準ロードボタン */
	private IButton standard;
	/** コピーボタン */
	private IButton copy;

	public EntityViewMenuPane() {

		setWidth100();
		setHeight(30);
		setMargin(6);
		setMembersMargin(5);
		setAlign(Alignment.LEFT);

		//ビュー切替用のメニュー
		HLayout selecteLayout = new HLayout();
		selecteLayout.setMembersMargin(5);

		DynamicForm form = new DynamicForm();
		viewSelect = new SelectItem("name", AdminClientMessageUtil.getString("ui_metadata_entity_layout_EntityViewMenuPane_viewName"));
		viewSelect.setWrapTitle(false);
		form.setItems(viewSelect);
		selecteLayout.addMember(form);

		display = new IButton(AdminClientMessageUtil.getString("ui_metadata_entity_layout_EntityViewMenuPane_display"));
		save = new IButton(AdminClientMessageUtil.getString("ui_metadata_entity_layout_EntityViewMenuPane_save"));
		add = new IButton(AdminClientMessageUtil.getString("ui_metadata_entity_layout_EntityViewMenuPane_add"));
		delete = new IButton(AdminClientMessageUtil.getString("ui_metadata_entity_layout_EntityViewMenuPane_delete"));
		standard = new IButton(AdminClientMessageUtil.getString("ui_metadata_entity_layout_EntityViewMenuPane_standardLoad"));
		copy = new IButton(AdminClientMessageUtil.getString("ui_metadata_entity_layout_EntityViewMenuPane_copy"));

		selecteLayout.addMember(display);
		selecteLayout.addMember(save);
		selecteLayout.addMember(add);
		selecteLayout.addMember(delete);
		selecteLayout.addMember(standard);
		selecteLayout.addMember(copy);

		addMember(selecteLayout);
	}

	/**
	 * ビュー選択コンボを取得
	 * @return
	 */
	public SelectItem getViewSelectItem() {
		return viewSelect;
	}

	/**
	 * ビュー選択コンボのビュー名を取得
	 * @return
	 */
	public String getViewName() {
		return viewSelect.getValue().toString();
	}

	public void setValueMap(String... array) {
		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
		valueMap.put("", DEFAULT_VIEW_NAME);
		for (String str : array) {
			if (str != null && !str.equals("")) {
				valueMap.put(str, str);
			}
		}

		viewSelect.setValueMap(valueMap);
	}

	/**
	 * 表示ボタンクリックイベントを設定
	 * @param handler
	 */
	public void setDisplayClickHandler(ClickHandler handler) {
		display.addClickHandler(handler);
	}

	/**
	 * 保存ボタンクリックイベントを設定
	 * @param handler
	 */
	public void setSaveClickHandler(ClickHandler handler) {
		save.addClickHandler(handler);
	}

	/**
	 * 追加ボタンクリックイベントを設定
	 * @param handler
	 */
	public void setAddClickHandler(ClickHandler handler) {
		add.addClickHandler(handler);
	}

	/**
	 * 削除ボタンクリックイベントを設定
	 * @param handler
	 */
	public void setDeleteClickHandler(ClickHandler handler) {
		delete.addClickHandler(handler);
	}

	/**
	 * 標準ロードボタンクリックイベントを設定
	 * @param handler
	 */
	public void setStandardClickHandler(ClickHandler handler) {
		standard.addClickHandler(handler);
	}

	/**
	 * コピーボタンクリックイベントを設定
	 * @param handler
	 */
	public void setCopyClickHandler(ClickHandler handler) {
		copy.addClickHandler(handler);
	}

}
