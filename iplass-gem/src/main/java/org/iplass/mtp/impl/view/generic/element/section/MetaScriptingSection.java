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

package org.iplass.mtp.impl.view.generic.element.section;

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
import org.iplass.mtp.view.generic.element.Element;
import org.iplass.mtp.view.generic.element.section.ScriptingSection;

/**
 * スクリプトセクションのメタデータ
 * @author lis3wg
 */
public class MetaScriptingSection extends MetaSection {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -8540919663557511912L;

	public static MetaScriptingSection createInstance(Element element) {
		return new MetaScriptingSection();
	}

	/** スクリプト */
	private String script;

	/** タイトル */
	private String title;

	/** id */
	private String id;

	/** リンクを表示するか */
	private boolean showLink;

	/** 詳細編集非表示設定 */
	private boolean hideDetail;

	/** 詳細表示非表示設定 */
	private boolean hideView;

	/** テンプレートのキー */
	private String key;

	/** 多言語設定情報 */
	private List<MetaLocalizedString> localizedTitleList = new ArrayList<MetaLocalizedString>();

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
	 * idを取得します。
	 * @return id
	 */
	public String getId() {
	    return id;
	}

	/**
	 * idを設定します。
	 * @param id id
	 */
	public void setId(String id) {
	    this.id = id;
	}

	/**
	 * リンクを表示するかを取得します。
	 * @return リンクを表示するか
	 */
	public boolean isShowLink() {
	    return showLink;
	}

	/**
	 * リンクを表示するかを設定します。
	 * @param showLink リンクを表示するか
	 */
	public void setShowLink(boolean showLink) {
	    this.showLink = showLink;
	}

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

	@Override
	public MetaScriptingSection copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public void applyConfig(Element element, String definitionId) {
		super.fillFrom(element, definitionId);

		ScriptingSection script = (ScriptingSection) element;
		this.script = script.getScript();
		title = script.getTitle();
		id = script.getId();
		this.showLink = script.isShowLink();
		this.hideDetail = script.isHideDetail();
		this.hideView = script.isHideView();

		// 言語毎の文字情報設定
		localizedTitleList = I18nUtil.toMeta(script.getLocalizedTitleList());
	}

	@Override
	public Element currentConfig(String definitionId) {
		ScriptingSection script = new ScriptingSection();
		super.fillTo(script, definitionId);

		script.setScript(this.script);
		script.setKey(key);
		script.setTitle(title);
		script.setId(id);
		script.setShowLink(showLink);
		script.setHideDetail(hideDetail);
		script.setHideView(hideView);
		script.setLocalizedTitleList(I18nUtil.toDef(localizedTitleList));

		return script;
	}

	@Override
	public ScriptingSectionHandler createRuntime(EntityViewRuntime entityView) {
		return new ScriptingSectionHandler(this, entityView);
	}

	/**
	 * ランタイム
	 * @author lis3wg
	 */
	public class ScriptingSectionHandler extends SectionRuntime {

		/**
		 * コンストラクタ
		 * @param metadata メタデータ
		 * @param entityView 画面定義
		 */
		public ScriptingSectionHandler(MetaScriptingSection metadata, EntityViewRuntime entityView) {
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
		public MetaScriptingSection getMetaData() {
			return (MetaScriptingSection) super.getMetaData();
		}
	}
}
