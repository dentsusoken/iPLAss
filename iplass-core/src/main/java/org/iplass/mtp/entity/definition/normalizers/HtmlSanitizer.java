/*
 * Copyright (C) 2026 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.entity.definition.normalizers;

import org.iplass.mtp.entity.definition.NormalizerDefinition;

/**
 * <% if (doclang == "ja") {%>
 * HTMLサニタイズのNormalizer定義です。
 * 許可されたHTML要素・属性以外を除去し、安全なHTMLに変換します。
 * 主に、RichTextエディタで入力されたHTMLのサーバー側サニタイズを想定しています。
 *
 * <p>
 * {@link SafelistType}でjsoupのSafelistプリセットを選択します。
 * デフォルトは{@link SafelistType#BASIC}です。
 * </p>
 * <p>
 * customizeScriptにGroovyスクリプトを指定すると、選択したプリセットのSafelistオブジェクトを
 * スクリプト内でカスタマイズできます。スクリプト内ではバインド変数{@code safelist}
 * （org.jsoup.safety.Safelist）を操作します。
 * </p>
 *
 * <%} else {%>
 * Normalizer definition for HTML sanitization.
 * Removes HTML elements and attributes that are not allowed, converting to safe HTML.
 * Primarily intended for server-side sanitization of HTML input from RichText editors.
 *
 * <p>
 * Select a jsoup Safelist preset via {@link SafelistType}.
 * The default is {@link SafelistType#BASIC}.
 * </p>
 * <p>
 * If customizeScript is specified, the Safelist object created from the selected preset
 * can be customized via a Groovy script. The binding variable {@code safelist}
 * (org.jsoup.safety.Safelist) is available in the script.
 * </p>
 * <%}%>
 *
 */
public class HtmlSanitizer extends NormalizerDefinition {
	private static final long serialVersionUID = 2897541036845127893L;

	private SafelistType safelistType;

	private String customizeScript;

	public HtmlSanitizer() {
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * 指定されたSafelistプリセットタイプで初期化します。
	 * <%} else {%>
	 * Initializes with the specified Safelist preset type.
	 * <%}%>
	 *
	 * @param safelistType Safelistプリセットタイプ
	 * @throws IllegalArgumentException safelistTypeがnullの場合
	 */
	public HtmlSanitizer(SafelistType safelistType) {
		setSafelistType(safelistType);
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * Safelistプリセットタイプを取得します。
	 * <%} else {%>
	 * Gets the Safelist preset type.
	 * <%}%>
	 *
	 * @return Safelistプリセットタイプ
	 */
	public SafelistType getSafelistType() {
		return safelistType;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * Safelistプリセットタイプを設定します。
	 * <%} else {%>
	 * Sets the Safelist preset type.
	 * <%}%>
	 *
	 * @param safelistType Safelistプリセットタイプ
	 */
	public void setSafelistType(SafelistType safelistType) {
		if (safelistType == null) {
			throw new IllegalArgumentException("safelistType cannot be null");
		}
		this.safelistType = safelistType;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * Safelistカスタマイズ用のGroovyスクリプトを取得します。
	 * <%} else {%>
	 * Gets the Groovy script for customizing the Safelist.
	 * <%}%>
	 *
	 * @return カスタマイズスクリプト
	 */
	public String getCustomizeScript() {
		return customizeScript;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * Safelistカスタマイズ用のGroovyスクリプトを設定します。
	 * スクリプト内ではバインド変数{@code safelist}（org.jsoup.safety.Safelist）を操作できます。
	 * <%} else {%>
	 * Sets the Groovy script for customizing the Safelist.
	 * The binding variable {@code safelist} (org.jsoup.safety.Safelist) is available in the script.
	 * <%}%>
	 *
	 * @param customizeScript カスタマイズスクリプト
	 */
	public void setCustomizeScript(String customizeScript) {
		this.customizeScript = customizeScript;
	}
}
