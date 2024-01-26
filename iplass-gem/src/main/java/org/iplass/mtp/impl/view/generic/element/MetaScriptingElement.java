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

package org.iplass.mtp.impl.view.generic.element;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.i18n.MetaLocalizedString;
import org.iplass.mtp.impl.script.GroovyScriptEngine;
import org.iplass.mtp.impl.script.template.GroovyTemplate;
import org.iplass.mtp.impl.script.template.GroovyTemplateCompiler;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.view.generic.EntityViewRuntime;
import org.iplass.mtp.impl.view.generic.FormViewRuntime;
import org.iplass.mtp.view.generic.RequiredDisplayType;
import org.iplass.mtp.view.generic.element.Element;
import org.iplass.mtp.view.generic.element.ScriptingElement;

/**
 * スクリプト要素のメタデータ
 * @author lis3wg
 */
public class MetaScriptingElement extends MetaElement {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -1456163052715903895L;

	public static MetaScriptingElement createInstance(Element element) {
		return new MetaScriptingElement();
	}

	/** 詳細編集非表示設定 */
	private boolean hideDetail;

	/** 詳細表示非表示設定 */
	private boolean hideView;

	/** タイトル */
	private String title;

	/** 多言語設定情報 */
	private List<MetaLocalizedString> localizedTitleList = new ArrayList<>();

	/** クラス名 */
	private String style;

	/** ツールチップ */
	private String tooltip;

	/** ツールチップの多言語設定情報 */
	private List<MetaLocalizedString> localizedTooltipList;

	/** 必須属性表示タイプ */
	private RequiredDisplayType requiredDisplayType;

	/** スクリプト */
	private String script;

	/** テンプレートのキー */
	private String key;

	/**
	 * 詳細編集非表示設定を取得します。
	 * @return 詳細編集非表示設定
	 */
	public boolean isHideDetail() {
	    return hideDetail;
	}

	/**
	 * 詳細編集非表示設定を設定します。
	 * @param hideDetail 詳細編集非表示設定
	 */
	public void setHideDetail(boolean hideDetail) {
	    this.hideDetail = hideDetail;
	}

	/**
	 * 詳細表示非表示設定を取得します。
	 * @return 詳細表示非表示設定
	 */
	public boolean isHideView() {
	    return hideView;
	}

	/**
	 * 詳細表示非表示設定を設定します。
	 * @param hideView 詳細表示非表示設定
	 */
	public void setHideView(boolean hideView) {
	    this.hideView = hideView;
	}

	/**
	 * タイトルを取得します。
	 * @return タイトル
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * タイトルを設定します。
	 * @param title タイトル
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 多言語設定情報を取得します。
	 * @return リスト
	 */
	public List<MetaLocalizedString> getLocalizedTitleList() {
		return localizedTitleList;
	}

	/**
	 * 多言語設定情報を設定します。
	 * @param リスト
	 */
	public void setLocalizedTitleList(List<MetaLocalizedString> localizedTitleList) {
		this.localizedTitleList = localizedTitleList;
	}

	/**
	 * クラス名を取得します。
	 * @return クラス名
	 */
	public String getStyle() {
		return style;
	}

	/**
	 * クラス名を設定します。
	 * @param style クラス名
	 */
	public void setStyle(String style) {
		this.style = style;
	}

	/**
	 * ツールチップを取得します。
	 * @return ツールチップ
	 */
	public String getTooltip() {
		return tooltip;
	}

	/**
	 * ツールチップを設定します。
	 * @param tooltip ツールチップ
	 */
	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	/**
	 * ツールチップの多言語設定情報を取得します。
	 * @return リスト
	 */
	public List<MetaLocalizedString> getLocalizedTooltipList() {
		return localizedTooltipList;
	}

	/**
	 * ツールチップの多言語設定情報を設定します。
	 * @param localizedTooltipList リスト
	 */
	public void setLocalizedTooltipList(List<MetaLocalizedString> localizedTooltipList) {
		this.localizedTooltipList = localizedTooltipList;
	}

	/**
	 * 必須属性表示タイプを取得します。
	 * @return 必須属性表示タイプ
	 */
	public RequiredDisplayType getRequiredDisplayType() {
		return requiredDisplayType;
	}

	/**
	 * 必須属性表示タイプを設定します。
	 * @param requiredDisplayType 必須属性表示タイプ
	 */
	public void setRequiredDisplayType(RequiredDisplayType requiredDisplayType) {
		this.requiredDisplayType = requiredDisplayType;
	}

	/**
	 * スクリプトを取得します。
	 * @return スクリプト
	 */
	public String getScript() {
		return script;
	}

	/**
	 * スクリプトを設定します。
	 * @param script スクリプト
	 */
	public void setScript(String script) {
		this.script = script;
	}

	@Override
	public MetaScriptingElement copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public void applyConfig(Element element, String definitionId) {
		super.fillFrom(element, definitionId);

		ScriptingElement script = (ScriptingElement) element;
		this.hideDetail = script.isHideDetail();
		this.hideView = script.isHideView();
		this.title = script.getTitle();
		this.localizedTitleList = I18nUtil.toMeta(script.getLocalizedTitleList());
		this.style = script.getStyle();
		this.tooltip = script.getTooltip();
		this.localizedTooltipList = I18nUtil.toMeta(script.getLocalizedTooltipList());
		this.requiredDisplayType = script.getRequiredDisplayType();
		this.script = script.getScript();
	}

	@Override
	public Element currentConfig(String definitionId) {
		ScriptingElement script = new ScriptingElement();
		super.fillTo(script, definitionId);

		script.setHideDetail(this.hideDetail);
		script.setHideView(this.hideView);
		script.setTitle(this.title);
		script.setLocalizedTitleList(I18nUtil.toDef(localizedTitleList));
		script.setStyle(this.style);
		script.setTooltip(tooltip);
		script.setLocalizedTooltipList(I18nUtil.toDef(localizedTooltipList));
		script.setRequiredDisplayType(this.requiredDisplayType);
		script.setScript(this.script);
		script.setKey(key);
		return script;
	}

	@Override
	public ScriptingRuntime createRuntime(EntityViewRuntime entityView, FormViewRuntime formView) {
		return new ScriptingRuntime(this, entityView);
	}

	/**
	 * ランタイム
	 * @author lis3wg
	 */
	public class ScriptingRuntime extends ElementRuntime {

		/**
		 * コンストラクタ
		 * @param metadata メタデータ
		 * @param entityView 画面定義
		 */
		public ScriptingRuntime(MetaScriptingElement metadata, EntityViewRuntime entityView) {
			super(metadata, entityView);
			if (metadata.script != null && metadata.key == null) {
				metadata.key = "Jsp_" + GroovyTemplateCompiler.randomName().replace("-", "_");
				entityView.addTemplate(key, compile(key));
			}
		}

		/**
		 * スクリプトをコンパイルしてテンプレートを作成します。
		 * @param key テンプレートのキー
		 * @return テンプレート
		 */
		private GroovyTemplate compile(String key) {
			TenantContext tenant = ExecuteContext.getCurrentContext().getTenantContext();
			return GroovyTemplateCompiler.compile(
					getMetaData().script, key, (GroovyScriptEngine) tenant.getScriptEngine());
		}

		@Override
		public MetaScriptingElement getMetaData() {
			return (MetaScriptingElement) super.getMetaData();
		}
	}

}
