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

package org.iplass.adminconsole.client.metadata.ui.entity.layout.item;

import java.io.Serializable;
import java.util.HashMap;

import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.element.section.SectionWindowController;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.metafield.MetaFieldUpdateEvent;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.metafield.MetaFieldUpdateHandler;
import org.iplass.adminconsole.view.annotation.Refrectable;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.HeaderControls;
import com.smartgwt.client.widgets.HeaderControl;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;

/**
 * 画面編集用のウィンドウコントロール
 * @author lis3wg
 *
 */
public abstract class ViewEditWindow extends AbstractWindow {
	/** パラメータを保持するマップ */
	private HashMap<String, Serializable> valueMap = new HashMap<String, Serializable>();

	/** 設定ボタン押下イベント */
	private ClickHandlerImpl handler = new ClickHandlerImpl();

	/** メタデータ更新イベント */
	private MetaFieldUpdateHandler updateHandler = null;

	/** 起動元のタイプ */
	protected FieldReferenceType triggerType = FieldReferenceType.ALL;

	/** 設定ボタン */
	protected final HeaderControl setting = new HeaderControl(HeaderControl.SETTINGS, handler);

	/** Entity定義名 */
	protected final String defName;

	/** SectionWindowController */
	protected SectionWindowController sectionController = GWT.create(SectionWindowController.class);

	/**
	 * コンストラクタ
	 */
	public ViewEditWindow(String defName) {
		this(defName, FieldReferenceType.ALL);
	}

	/**
	 * コンストラクタ
	 *
	 * @param defName 定義名
	 * @param triggerType 起動元のタイプ
	 */
	public ViewEditWindow(String defName, FieldReferenceType triggerType) {
		this.defName = defName;
		if (triggerType != null) {
			this.triggerType = triggerType;
		} else {
			this.triggerType = FieldReferenceType.ALL;
		}

		setWidth100();
		setShowEdges(false);
		setCanDrop(true);

		// ダミーで空のスタイルを指定しないとsmartget3.1からはsetBackgroundColorが有効にならない
		setStyleName("");

		setHeaderControls(HeaderControls.HEADER_LABEL, setting, HeaderControls.CLOSE_BUTTON);
	}

	/**
	 * パラメータを取得。
	 * @param key
	 * @return
	 */
	public Serializable getValue(String key) {
		return valueMap.get(key);
	}

	/**
	 * パラメータを設定。
	 * @param key
	 * @param value
	 */
	public void setValue(String key, Serializable value) {
		valueMap.put(key, value);
	}

	/**
	 * クラス名を設定
	 * @param className
	 */
	public void setClassName(String className) {
		setValue("className", className);
	}

	/**
	 * クラス名を取得。
	 * @return
	 */
	public String getClassName() {
		return (String) getValue("className");
	}

	/**
	 * Refrectableオブジェクトを設定。
	 * @param valueObject
	 */
	public void setValueObject(Refrectable valueObject) {
		setValue("valueObject", valueObject);
	}

	/**
	 * Refrectableオブジェクトを取得。
	 * @return
	 */
	public Refrectable getValueObject() {
		return (Refrectable) getValue("valueObject");
	}

	/**
	 * 起動元のタイプを取得。
	 * @return 起動元のタイプ
	 */
	public FieldReferenceType getTriggerType() {
		return triggerType;
	}

	/**
	 * 起動元のタイプの登録
	 * @param triggerType 起動元のタイプ
	 */
	public void setTriggerType(FieldReferenceType triggerType) {
		this.triggerType = triggerType;
	}

	/**
	 * ダイアログOK時のイベントハンドラの登録
	 * @param updateHandler
	 */
	protected void setMetaFieldUpdateHandler(MetaFieldUpdateHandler updateHandler) {
		this.updateHandler = updateHandler;
	}

	protected EntityViewFieldSettingWindow createSubWindow() {
		return new EntityViewFieldSettingWindow(getClassName(), getValueObject(), triggerType, defName);
	}

	/**
	 * 設定ボタン押下イベント。
	 * @author lis3wg
	 *
	 */
	private final class ClickHandlerImpl implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			final EntityViewFieldSettingWindow dialog = createSubWindow();

			dialog.setOkHandler(new MetaFieldUpdateHandler() {

				@Override
				public void execute(MetaFieldUpdateEvent event) {
					//ダイアログ破棄
					dialog.destroy();

					//ValueObject再設定
					setValueObject(event.getValue());

					//displayLabelかtitleがある場合はWindowのタイトル再設定
					if (event.getValueMap().containsKey("displayLabel")) {
						setTitle((String) event.getValueMap().get("displayLabel"));
					} else if (event.getValueMap().containsKey("title")) {
						setTitle((String) event.getValueMap().get("title"));
					}

					if (updateHandler != null) {
						//サブクラスで更新時の処理が設定されていたら実行
						updateHandler.execute(event);
					}
				}
			});

			dialog.setCancelHandler(new MetaFieldUpdateHandler() {

				@Override
				public void execute(MetaFieldUpdateEvent event) {
					//ダイアログ破棄
					dialog.destroy();
				}
			});


			dialog.show();
		}
	}
}
