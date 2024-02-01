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

package org.iplass.adminconsole.client.metadata.ui.common;

import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.MetaDataItemMenuTreeNode;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;

/**
 * 共通編集ヘッダパネル
 *
 * アクションボタンを制御します。
 */
public class MetaCommonHeaderPane extends HLayout {

	/** 保存ボタン */
	private IButton save;
	/** キャンセルボタン */
	private IButton cancel;
	/** 履歴ボタン */
	private IButton history;

	/**
	 * コンストラクタ
	 */
	public MetaCommonHeaderPane() {
		this(null);
	}

	/**
	 * コンストラクタ
	 */
	public MetaCommonHeaderPane(MetaDataItemMenuTreeNode targetNode) {

		//レイアウト設定
		setWidth100();
		setHeight(30);
		setMargin(6);
		setMembersMargin(5);
		setAlign(Alignment.LEFT);

		//ボタン
		save = new IButton("Save");
		cancel = new IButton("Cancel");
		history = new IButton("Show History");

		//配置
		addMember(save);
		addMember(cancel);
		addMember(history);

		setTargetNode(targetNode);
	}

	/**
	 * 保存ボタンクリックイベントを設定します。
	 *
	 * @param handler 保存ボタン処理クリックハンドラ
	 */
	public void setSaveClickHandler(ClickHandler handler) {
		save.addClickHandler(handler);
	}

	/**
	 * 保存ボタンの使用可否を設定します。
	 *
	 * @param disabled true：使用不可
	 */
	public void setSaveDisabled(boolean disabled) {
		save.setDisabled(disabled);
	}

	/**
	 * 保存ボタンの表示可否を設定します。
	 *
	 * @param visible true：表示
	 */
	public void setSaveVisible(boolean visible) {
		save.setVisible(visible);
	}

	/**
	 * 保存ボタンのHoverを設定します。
	 *
	 * @param hover Hover
	 */
	public void setSaveHover(String hover) {
		SmartGWTUtil.addHoverToCanvas(save, hover);
	}


	/**
	 * キャンセルボタンクリックイベントを設定します。
	 *
	 * @param handler キャンセルボタン処理クリックハンドラ
	 */
	public void setCancelClickHandler(ClickHandler handler) {
		cancel.addClickHandler(handler);
	}

	/**
	 * キャンセルボタンの使用可否を設定します。
	 *
	 * @param disabled true：使用不可
	 */
	public void setCancelDisabled(boolean disabled) {
		cancel.setDisabled(disabled);
	}

	/**
	 * キャンセルボタンの表示可否を設定します。
	 *
	 * @param visible true：表示
	 */
	public void setCancelVisible(boolean visible) {
		cancel.setVisible(visible);
	}

	/**
	 * キャンセルボタンのHoverを設定します。
	 *
	 * @param hover Hover
	 */
	public void setCancelHover(String hover) {
		SmartGWTUtil.addHoverToCanvas(cancel, hover);
	}


	/**
	 * 履歴ボタンクリックイベントを設定します。
	 *
	 * @param handler 履歴ボタン処理クリックハンドラ
	 */
	public void setHistoryClickHandler(ClickHandler handler) {
		history.addClickHandler(handler);
	}

	/**
	 * 履歴ボタンの使用可否を設定します。
	 *
	 * @param disabled true：使用不可
	 */
	public void setHistoryDisabled(boolean disabled) {
		history.setDisabled(disabled);
	}

	/**
	 * 履歴ボタンの表示可否を設定します。
	 *
	 * @param visible true：表示
	 */
	public void setHistoryVisible(boolean visible) {
		history.setVisible(visible);
	}

	/**
	 * キャンセルボタンのHoverを設定します。
	 *
	 * @param hover Hover
	 */
	public void setHistoryHover(String hover) {
		SmartGWTUtil.addHoverToCanvas(cancel, hover);
	}

	public void setTargetNode(MetaDataItemMenuTreeNode targetNode) {
		//上書き可能チェック
		if (targetNode != null && targetNode.isShared()) {
			//SharedでOverritableがfalseは上書き禁止
			if (!targetNode.isOverwritable()) {
				save.setDisabled(true);
			}
		}
	}
}
