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

package org.iplass.mtp.impl.view.generic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.entity.query.PreparedQuery;
import org.iplass.mtp.impl.metadata.BaseMetaDataRuntime;
import org.iplass.mtp.impl.script.template.GroovyTemplate;
import org.iplass.mtp.impl.view.generic.common.MetaAutocompletionSetting.AutocompletionSettingHandler;
import org.iplass.mtp.impl.view.generic.element.MetaButton.ButtonHandler;

/**
 * 画面定義のランタイム
 * @author lis3wg
 */
public class EntityViewHandler extends BaseMetaDataRuntime {

	/** メタデータ */
	protected MetaEntityView metaData;

	/** レイアウト情報のランタイム */
	private List<FormViewHandler> formViews;

	/** テンプレート情報 */
	private Map<String, GroovyTemplate> templates;

	/** カスタムスタイル */
	private Map<String, Map<String, GroovyTemplate>> customStylesMap;

	private Map<String, ButtonHandler> buttonHandlerMap;

	private Map<String, AutocompletionSettingHandler> autocompletionSettingMap;

	/** Query */
	private Map<String, PreparedQuery> queries;

	/**
	 * コンストラクタ
	 * @param metaData レイアウト情報
	 */
	public EntityViewHandler(MetaEntityView metaData) {
		try {
			this.metaData = metaData;
			if (metaData.getViews().size() > 0) {
				for (MetaFormView view : metaData.getViews()) {
					this.addFormView(view.createRuntime(this));
				}
			}
		} catch (RuntimeException e) {
			setIllegalStateException(e);
		}
	}

	/**
	 * レイアウト情報のランタイムを追加
	 * @param formView レイアウト情報
	 */
	private void addFormView(FormViewHandler formView) {
		if (this.formViews == null) this.formViews = new ArrayList<FormViewHandler>();
		this.formViews.add(formView);
	}

	public List<FormViewHandler> getFormViews() {
		if (this.formViews == null) this.formViews = new ArrayList<FormViewHandler>();
		return formViews;
	}

	/**
	 * テンプレートを追加します。
	 * @param key テンプレートのキー
	 * @param template テンプレート
	 */
	public void addTemplate(String key, GroovyTemplate template) {
		checkState();
		if (templates == null) templates = new HashMap<String, GroovyTemplate>();
		templates.put(key, template);
	}

	/**
	 * テンプレートを取得します。
	 * @param key テンプレートのキー
	 * @return テンプレート
	 */
	public GroovyTemplate getTemplate(String key) {
		checkState();
		return templates.get(key);
	}

	/**
	 * Queryを追加します。
	 * @param key Queryのキー
	 * @param template Query
	 */
	public void addQuery(String key, PreparedQuery query) {
		checkState();
		if (queries == null) queries = new HashMap<String, PreparedQuery>();
		queries.put(key, query);
	}

	/**
	 * Queryを取得します。
	 * @param key Queryのキー
	 * @return Query
	 */
	public PreparedQuery getQuery(String key) {
		checkState();
		return queries.get(key);
	}

	/**
	 * メタデータを取得します。
	 * @return メタデータ
	 */
	public MetaEntityView getMetaData() {
		return this.metaData;
	}

	/**
	 * カスタムスタイルを追加します。
	 * @param key カスタムスタイルのキー
	 * @param customStyleMap カスタムスタイルの格納されたマップ
	 */
	public void addCustomStyle(String key, Map<String, GroovyTemplate> customStyleMap) {
		if (customStylesMap == null) customStylesMap = new HashMap<String, Map<String, GroovyTemplate>>();
		customStylesMap.put(key, customStyleMap);
	}

	/**
	 * カスタムスタイルの格納されたマップを取得します。
	 * @return カスタムスタイルの格納されたマップ
	 */
	public Map<String, GroovyTemplate> getCustomStyleScriptMap(String key) {
		return customStylesMap.get(key);
	}

	/**
	 * ボタンハンドラを追加します。
	 * @param handler
	 */
	public void addButtonHandler(ButtonHandler handler) {
		if (buttonHandlerMap == null) buttonHandlerMap = new HashMap<String, ButtonHandler>();
		if (handler.getMetaData().getCustomDisplayTypeScriptKey() != null) {
			buttonHandlerMap.put(handler.getMetaData().getCustomDisplayTypeScriptKey(), handler);
		}
	}

	/**
	 * ボタンハンドラを取得します。
	 * @param key
	 * @return
	 */
	public ButtonHandler getButtonHandler(String key) {
		if (buttonHandlerMap == null) return null;
		return buttonHandlerMap.get(key);
	}

	/**
	 * 自動補完設定を追加します。
	 * @param handler
	 */
	public void addAutocompletionSettingHandler(AutocompletionSettingHandler handler) {
		if (autocompletionSettingMap == null) autocompletionSettingMap = new HashMap<String, AutocompletionSettingHandler>();
		if (handler.getMetaData().getRuntimeKey() != null) {
			autocompletionSettingMap.put(handler.getMetaData().getRuntimeKey(), handler);
		}
	}

	/**
	 * 自動補完設定を取得します。
	 * @param key
	 * @return
	 */
	public AutocompletionSettingHandler getAutocompletionSettingHandler(String key) {
		return autocompletionSettingMap.get(key);
	}
}
