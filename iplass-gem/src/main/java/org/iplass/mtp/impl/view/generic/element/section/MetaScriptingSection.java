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

package org.iplass.mtp.impl.view.generic.element.section;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.script.GroovyScriptEngine;
import org.iplass.mtp.impl.script.template.GroovyTemplate;
import org.iplass.mtp.impl.script.template.GroovyTemplateCompiler;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.view.generic.EntityViewRuntime;
import org.iplass.mtp.impl.view.generic.FormViewRuntime;
import org.iplass.mtp.view.generic.element.Element;
import org.iplass.mtp.view.generic.element.section.ScriptingSection;

/**
 * スクリプトセクションのメタデータ
 * @author lis3wg
 */
public class MetaScriptingSection extends MetaAdjustableHeightSection {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -8540919663557511912L;

	public static MetaScriptingSection createInstance(Element element) {
		return new MetaScriptingSection();
	}

	/** スクリプト */
	private String script;

	/** リンクを表示するか */
	private boolean showLink;

	/** 詳細編集非表示設定 */
	private boolean hideDetail;

	/** 詳細表示非表示設定 */
	private boolean hideView;

	/** テンプレートのキー */
	private String key;

	/** セクション内に配置した場合に枠線を表示 */
	private boolean dispBorderInSection;

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
	 * セクション内に配置した場合に枠線を表示を取得します。
	 * @return セクション内に配置した場合に枠線を表示
	 */
	public boolean isDispBorderInSection() {
		return dispBorderInSection;
	}

	/**
	 * セクション内に配置した場合に枠線を表示を設定します。
	 * @param dispBorderInSection セクション内に配置した場合に枠線を表示
	 */
	public void setDispBorderInSection(boolean dispBorderInSection) {
		this.dispBorderInSection = dispBorderInSection;
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
		this.showLink = script.isShowLink();
		this.hideDetail = script.isHideDetail();
		this.hideView = script.isHideView();
		this.dispBorderInSection = script.isDispBorderInSection();
	}

	@Override
	public Element currentConfig(String definitionId) {
		ScriptingSection script = new ScriptingSection();
		super.fillTo(script, definitionId);

		script.setScript(this.script);
		script.setKey(key);
		script.setShowLink(showLink);
		script.setHideDetail(hideDetail);
		script.setHideView(hideView);
		script.setDispBorderInSection(dispBorderInSection);

		return script;
	}

	@Override
	public ScriptingSectionRuntime createRuntime(EntityViewRuntime entityView, FormViewRuntime formView) {
		return new ScriptingSectionRuntime(this, entityView);
	}

	/**
	 * ランタイム
	 * @author lis3wg
	 */
	public class ScriptingSectionRuntime extends SectionRuntime {

		/**
		 * コンストラクタ
		 * @param metadata メタデータ
		 * @param entityView 画面定義
		 */
		public ScriptingSectionRuntime(MetaScriptingSection metadata, EntityViewRuntime entityView) {
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
