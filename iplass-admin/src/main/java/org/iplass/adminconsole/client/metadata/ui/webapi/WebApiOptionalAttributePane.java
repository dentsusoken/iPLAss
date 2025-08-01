/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.adminconsole.client.metadata.ui.webapi;

import org.iplass.mtp.webapi.definition.WebApiDefinition;

import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

/**
 * WebAPIメタデータのオプション属性用の入力領域
 * <p>
 * WebAPIのオプション属性を設定する為の入力領域です。
 * オプション情報はそれぞれタブでグループ化され、タブごとに意味のある単位で入力領域を用意します。
 * </p>
 *
 * @author SEKIGUCHI Naoya
 */
public class WebApiOptionalAttributePane extends VLayout {
	/** タブ */
	private TabSet tabset;
	/** スタブタブ */
	private Tab stubTab;
	/** OpenAPIタブ */
	private Tab openApiTab;

	/** スタブタブ用画面 */
	private WebApiOptionalStubTabPane stubPane;
	/** OpenAPIタブ用画面 */
	private WebApiOptionalOpenApiTabPane openApiPane;

	/**
	 * コンストラクタ
	 */
	public WebApiOptionalAttributePane() {
		setMargin(5);
		setHeight100();
		setWidth100();

		// tab
		tabset = new TabSet();
		tabset.setWidth100();
		tabset.setHeight100();

		stubPane = new WebApiOptionalStubTabPane();
		openApiPane = new WebApiOptionalOpenApiTabPane();

		stubTab = new Tab("Stub");
		stubTab.setPane(stubPane);
		tabset.addTab(stubTab);

		openApiTab = new Tab("OpenAPI");
		openApiTab.setPane(openApiPane);
		tabset.addTab(openApiTab);

		addMember(tabset);
	}

	/**
	 * WebAPI定義の設定情報を、本画面領域に反映します。
	 * @param definition WebAPI定義
	 */
	public void setDefinition(WebApiDefinition definition) {
		stubPane.setDefinition(definition);
		openApiPane.setDefinition(definition);
	}

	/**
	 * 本画面領域の設定情報を、WebAPI定義に反映します。
	 * @param definition WebAPI定義
	 * @return WebAPI定義
	 */
	public WebApiDefinition getDefinition(WebApiDefinition definition) {
		stubPane.getDefinition(definition);
		openApiPane.getDefinition(definition);

		return definition;
	}
}
