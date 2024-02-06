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

import javax.xml.bind.annotation.XmlTransient;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.i18n.MetaLocalizedString;
import org.iplass.mtp.impl.script.GroovyScriptEngine;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.impl.script.template.GroovyTemplate;
import org.iplass.mtp.impl.script.template.GroovyTemplateCompiler;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.view.generic.EntityViewRuntime;
import org.iplass.mtp.impl.view.generic.FormViewRuntime;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.element.Element;
import org.iplass.mtp.view.generic.element.Link;

/**
 * リンクのメタデータ
 * @author lis3wg
 *
 */
public class MetaLink extends MetaElement {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1819770758000735919L;

	public static MetaLink createInstance(Element element) {
		return new MetaLink();
	}

	/** タイトル */
	private String title;

	/** 多言語設定情報 */
	private List<MetaLocalizedString> localizedTitleList = new ArrayList<>();

	/** 表示ラベル */
	private String displayLabel;

	/** 多言語設定情報 */
	private List<MetaLocalizedString> localizedDisplayLabelList = new ArrayList<>();

	/** URL */
	private String url;

	/** 別ウィンドウで表示するか */
	private boolean dispNewWindow;

	/** 入力カスタムスタイル */
	private String inputCustomStyle;

	/** 入力カスタムスタイルスクリプトのキー */
	private String inputCustomStyleScriptKey;

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
	 * 表示ラベルを取得します。
	 * @return 表示ラベル
	 */
	public String getDisplayLabel() {
		return displayLabel;
	}

	/**
	 * 表示ラベルを設定します。
	 * @param displayLabel 表示ラベル
	 */
	public void setDisplayLabel(String displayLabel) {
		this.displayLabel = displayLabel;
	}

	/**
	 * URLを取得します。
	 * @return URL
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * URLを設定します。
	 * @param url URL
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 別ウィンドウで表示するかを取得します。
	 * @return 別ウィンドウで表示するか
	 */
	public boolean isDispNewWindow() {
		return dispNewWindow;
	}

	/**
	 * 別ウィンドウで表示するかを設定します。
	 * @param dispNewWindow 別ウィンドウで表示するか
	 */
	public void setDispNewWindow(boolean dispNewWindow) {
		this.dispNewWindow = dispNewWindow;
	}

	/**
	 * 入力カスタムスタイルを取得します。
	 * @return 入力カスタムスタイル
	 */
	public String getInputCustomStyle() {
		return inputCustomStyle;
	}

	/**
	 * 入力カスタムスタイルを設定します。
	 * @param inputCustomStyle 入力カスタムスタイル
	 */
	public void setInputCustomStyle(String inputCustomStyle) {
		this.inputCustomStyle = inputCustomStyle;
	}

	@XmlTransient
	public String getInputCustomStyleScriptKey() {
		return inputCustomStyleScriptKey;
	}

	public void setInputCustomStyleScriptKey(String inputCustomStyleScriptKey) {
		this.inputCustomStyleScriptKey = inputCustomStyleScriptKey;
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
	 * 多言語設定情報を取得します。
	 * @return リスト
	 */
	public List<MetaLocalizedString> getLocalizedDisplayLabelList() {
		return localizedDisplayLabelList;
	}

	/**
	 * 多言語設定情報を設定します。
	 * @param リスト
	 */
	public void setLocalizedDisplayLabelList(
			List<MetaLocalizedString> localizedDisplayLabelList) {
		this.localizedDisplayLabelList = localizedDisplayLabelList;
	}

	@Override
	public MetaLink copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public void applyConfig(Element element, String definitionId) {
		super.fillFrom(element, definitionId);

		Link link = (Link) element;
		this.title = link.getTitle();
		this.displayLabel = link.getDisplayLabel();
		this.url = link.getUrl();
		this.dispNewWindow = link.isDispNewWindow();
		this.inputCustomStyle = link.getInputCustomStyle();

		// 言語毎の文字情報設定
		localizedTitleList = I18nUtil.toMeta(link.getLocalizedTitleList());

		// 言語毎の文字情報設定
		localizedDisplayLabelList = I18nUtil.toMeta(link.getLocalizedDisplayLabelList());
	}

	@Override
	public Element currentConfig(String definitionId) {
		Link link = new Link();
		super.fillTo(link, definitionId);

		link.setTitle(this.title);
		link.setDisplayLabel(this.displayLabel);
		link.setUrl(this.url);
		link.setDispNewWindow(this.dispNewWindow);
		link.setInputCustomStyle(this.inputCustomStyle);
		link.setInputCustomStyleScriptKey(inputCustomStyleScriptKey);

		link.setLocalizedTitleList(I18nUtil.toDef(localizedTitleList));

		link.setLocalizedDisplayLabelList(I18nUtil.toDef(localizedDisplayLabelList));
		return link;
	}

	@Override
	public LinkRuntime createRuntime(EntityViewRuntime entityView, FormViewRuntime formView) {
		return new LinkRuntime(this, entityView);
	}

	public class LinkRuntime extends ElementRuntime {

		private static final String SCRIPT_PREFIX = "LinkHandler_inputCustomStyle";

		private GroovyTemplate inputCustomStyleScript;

		public LinkRuntime(MetaLink metadata, EntityViewRuntime entityView) {
			super(metadata, entityView);

			inputCustomStyleScriptKey = "Link_InputStyle_" + GroovyTemplateCompiler.randomName().replace("-", "_");
			if (StringUtil.isNotEmpty(inputCustomStyle)) {
				ScriptEngine scriptEngine = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();
				inputCustomStyleScript = GroovyTemplateCompiler.compile(inputCustomStyle,
						inputCustomStyleScriptKey + "_" + SCRIPT_PREFIX,
						(GroovyScriptEngine) scriptEngine);
			}

		}

		public GroovyTemplate getInputCustomStyleScript() {
			return inputCustomStyleScript;
		}

	}
}
