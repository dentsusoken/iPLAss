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

package org.iplass.mtp.impl.view.generic.element;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.auth.UserBinding;
import org.iplass.mtp.impl.command.RequestContextBinding;
import org.iplass.mtp.impl.command.SessionBinding;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.i18n.MetaLocalizedString;
import org.iplass.mtp.impl.script.GroovyScriptEngine;
import org.iplass.mtp.impl.script.Script;
import org.iplass.mtp.impl.script.ScriptContext;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.impl.script.template.GroovyTemplate;
import org.iplass.mtp.impl.script.template.GroovyTemplateCompiler;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.view.generic.EntityViewHandler;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.OutputType;
import org.iplass.mtp.view.generic.element.Button;
import org.iplass.mtp.view.generic.element.DisplayType;
import org.iplass.mtp.view.generic.element.Element;

/**
 * ボタンのメタデータ
 * @author lis3wg
 */
public class MetaButton extends MetaElement {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -2033613830792032497L;

	public static MetaButton createInstance(Element element) {
		return new MetaButton();
	}

	/** 表示タイプ */
	private DisplayType displayType;

	/** タイトル */
	private String title;

	/** 多言語設定情報 */
	private List<MetaLocalizedString> localizedTitleList = new ArrayList<MetaLocalizedString>();

	/** 表示ラベル */
	private String displayLabel;

	/** 多言語設定情報 */
	private List<MetaLocalizedString> localizedDisplayLabelList = new ArrayList<MetaLocalizedString>();

	/** プライマリー */
	private boolean primary = true;

	/** クラス名 */
	private String style;

	/** オンクリックイベント */
	private String onclickEvent;

	/** 入力カスタムスタイル */
	private String inputCustomStyle;

	/** 入力カスタムスタイルスクリプトのキー */
	private String inputCustomStyleScriptKey;

	/** 表示判定用スクリプト */
	private String customDisplayTypeScript;

	/** 表示判定用スクリプトキー */
	private String customDisplayTypeScriptKey;

	/**
	 * 表示タイプを取得します。
	 * @return 表示タイプ
	 */
	public DisplayType getDisplayType() {
	    return displayType;
	}

	/**
	 * 表示タイプを設定します。
	 * @param displayType 表示タイプ
	 */
	public void setDisplayType(DisplayType displayType) {
	    this.displayType = displayType;
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
	 * プライマリーを取得します。
	 * @return プライマリー
	 */
	public boolean isPrimary() {
		return primary;
	}

	/**
	 * プライマリーを設定します。
	 * @param primary プライマリー
	 */
	public void setPrimary(boolean primary) {
		this.primary = primary;
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
	 * オンクリックイベントを取得します。
	 * @return オンクリックイベント
	 */
	public String getOnclickEvent() {
		return onclickEvent;
	}

	/**
	 * オンクリックイベントを設定します。
	 * @param onclickEvent オンクリックイベント
	 */
	public void setOnclickEvent(String onclickEvent) {
		this.onclickEvent = onclickEvent;
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

	/**
	 * 入力カスタムスタイルスクリプトのキーを取得します。
	 * @return 入力カスタムスタイルスクリプトのキー
	 */
	@XmlTransient
	public String getInputCustomStyleScriptKey() {
		return inputCustomStyleScriptKey;
	}

	/**
	 * 入力カスタムスタイルスクリプトのキーを設定します。
	 * @param inputCustomStyleScriptKey 入力カスタムスタイルスクリプトのキー
	 */
	public void setInputCustomStyleScriptKey(String inputCustomStyleScriptKey) {
		this.inputCustomStyleScriptKey = inputCustomStyleScriptKey;
	}

	/**
	 * 表示判定用スクリプトを取得します。
	 * @return 表示判定用スクリプト
	 */
	public String getCustomDisplayTypeScript() {
	    return customDisplayTypeScript;
	}

	/**
	 * 表示判定用スクリプトを設定します。
	 * @param customDisplayTypeScript 表示判定用スクリプト
	 */
	public void setCustomDisplayTypeScript(String customDisplayTypeScript) {
	    this.customDisplayTypeScript = customDisplayTypeScript;
	}

	/**
	 * 表示判定用スクリプトキーを取得します。
	 * @return 表示判定用スクリプトキー
	 */
	@XmlTransient
	public String getCustomDisplayTypeScriptKey() {
	    return customDisplayTypeScriptKey;
	}

	/**
	 * 表示判定用スクリプトキーを設定します。
	 * @param customDisplayTypeScriptKey 表示判定用スクリプトキー
	 */
	public void setCustomDisplayTypeScriptKey(String customDisplayTypeScriptKey) {
	    this.customDisplayTypeScriptKey = customDisplayTypeScriptKey;
	}

	@Override
	public MetaButton copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public void applyConfig(Element element, String definitionId) {
		super.fillFrom(element, definitionId);

		Button button = (Button) element;
		this.displayType = button.getDisplayType();
		this.title = button.getTitle();
		this.displayLabel = button.getDisplayLabel();
		this.primary = button.isPrimary();
		this.style = button.getStyle();
		this.inputCustomStyle = button.getInputCustomStyle();
		this.onclickEvent = button.getOnclickEvent();
		this.customDisplayTypeScript = button.getCustomDisplayTypeScript();

		// 言語毎の文字情報設定
		localizedTitleList = I18nUtil.toMeta(button.getLocalizedTitleList());

		// 言語毎の文字情報設定
		localizedDisplayLabelList = I18nUtil.toMeta(button.getLocalizedDisplayLabelList());

	}

	@Override
	public Element currentConfig(String definitionId) {
		Button button = new Button();
		super.fillTo(button, definitionId);

		button.setDisplayType(this.displayType);
		button.setTitle(this.title);
		button.setDisplayLabel(this.displayLabel);
		button.setPrimary(this.primary);
		button.setStyle(this.style);
		button.setOnclickEvent(this.onclickEvent);
		button.setInputCustomStyle(this.inputCustomStyle);
		button.setInputCustomStyleScriptKey(inputCustomStyleScriptKey);
		button.setCustomDisplayTypeScript(this.customDisplayTypeScript);
		button.setCustomDisplayTypeScriptKey(this.customDisplayTypeScriptKey);

		button.setLocalizedTitleList(I18nUtil.toDef(localizedTitleList));

		button.setLocalizedDisplayLabelList(I18nUtil.toDef(localizedDisplayLabelList));

		return button;
	}

	@Override
	public ButtonHandler createRuntime(EntityViewHandler entityView) {
		return new ButtonHandler(this, entityView);
	}

	public class ButtonHandler extends ElementHandler {
		public static final String REQUEST_BINDING_NAME = "request";
		public static final String SESSION_BINDING_NAME = "session";
		public static final String USER_BINDING_NAME = "user";
		public static final String OUTPUT_TYPE_BINDING_NAME = "outputType";
		public static final String ENTITY_BINDING_NAME = "entity";

		private static final String SCRIPT_PREFIX_INPUT_CUSTOM_STYLE = "ButtonHandler_inputCustomStyle";
		private static final String SCRIPT_PREFIX_CUSTOM_DISPLAY_TYPE = "ButtonHandler_inputCustomStyle_customDisplayTypeScript";

		private GroovyTemplate inputCustomStyleScript;

		private Script compiledCustomDisplayTypeScript;

		public ButtonHandler(MetaButton metadata, EntityViewHandler entityView) {
			super(metadata, entityView);

			inputCustomStyleScriptKey = "Button_InputStyle" + GroovyTemplateCompiler.randomName().replace("-", "_");
			if (StringUtil.isNotEmpty(inputCustomStyle)) {
				ScriptEngine scriptEngine = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();
				inputCustomStyleScript = GroovyTemplateCompiler.compile(inputCustomStyle,
						inputCustomStyleScriptKey + "_" + SCRIPT_PREFIX_INPUT_CUSTOM_STYLE,
						(GroovyScriptEngine) scriptEngine);
			}

			if (StringUtil.isNotEmpty(customDisplayTypeScript)) {
				ScriptEngine scriptEngine = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();
				String scriptName = SCRIPT_PREFIX_CUSTOM_DISPLAY_TYPE + "_" + entityView.getMetaData().getId()
						+ GroovyTemplateCompiler.randomName().replace("-", "_");
				compiledCustomDisplayTypeScript = scriptEngine.createScript(customDisplayTypeScript, scriptName);
				customDisplayTypeScriptKey = scriptName;
			}

			entityView.addButtonHandler(this);
		}

		public GroovyTemplate getInputCustomStyleScript() {
			return inputCustomStyleScript;
		}

		public boolean isDisplayButton(OutputType outputType, Entity entity) {
			if (compiledCustomDisplayTypeScript == null) return false;

			UserBinding user = AuthContextHolder.getAuthContext().newUserBinding();

			ScriptEngine scriptEngine = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();
			ScriptContext sc = scriptEngine.newScriptContext();

			sc.setAttribute(REQUEST_BINDING_NAME, RequestContextBinding.newRequestContextBinding());
			sc.setAttribute(SESSION_BINDING_NAME, SessionBinding.newSessionBinding());
			sc.setAttribute(USER_BINDING_NAME, user);
			sc.setAttribute(OUTPUT_TYPE_BINDING_NAME, outputType);
			sc.setAttribute(ENTITY_BINDING_NAME, entity);

			Object val = compiledCustomDisplayTypeScript.eval(sc);
			if (val != null) {
				if (val instanceof Boolean) {
					return (Boolean) val;
				} else if (val instanceof String) {
					return Boolean.parseBoolean((String) val);
				}
			}

			return false;
		}

		@Override
		public MetaButton getMetaData() {
			return (MetaButton) super.getMetaData();
		}
	}
}
