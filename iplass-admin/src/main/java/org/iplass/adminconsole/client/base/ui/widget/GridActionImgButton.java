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

package org.iplass.adminconsole.client.base.ui.widget;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;

/**
 * Grid上に表示するImageButton
 * 
 * @author lis70i
 *
 */
public class GridActionImgButton extends HLayout {

	/** アクションボタン */
	private ImgButton actionBtn;
	
	/**
	 * コンストラクタ
	 */
	public GridActionImgButton() {
		
		setMembersMargin(3);
		setHeight100();
		setWidth100();
		setAlign(Alignment.CENTER);
		
		actionBtn = new ImgButton();
		actionBtn.setShowDown(false);
		actionBtn.setShowRollOver(false);
		actionBtn.setLayoutAlign(Alignment.CENTER);
		actionBtn.setHeight(16);
		actionBtn.setWidth(16);

		addMember(actionBtn);
		
	}
	
	/**
	 * Actionボタンに対してClickHandlerを追加します。
	 * 
	 * @param handler ClickHandler
	 */
	public void addActionClickHandler(ClickHandler handler) {
		actionBtn.addClickHandler(handler);
	}
	
	/**
	 * Actionボタンのアイコンを設定します。
	 * 
	 * @param src アイコン
	 */
	public void setActionButtonSrc(String src) {
		actionBtn.setSrc(src);
	}

	/**
	 * Actionボタンのツールチップを設定します。
	 * 
	 * @param prompt ツールチップ
	 */
	public void setActionButtonPrompt(String prompt) {
		actionBtn.setPrompt(prompt);
	}
}
