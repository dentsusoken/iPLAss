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

import java.util.List;

import org.iplass.mtp.entity.definition.NormalizerDefinition;

/**
 * <% if (doclang == "ja") {%>
 * HTMLサニタイズのNormalizer定義です。
 * 許可されたHTML要素・属性以外を除去し、安全なHTMLに変換します。
 * 主に、RichTextエディタで入力されたHTMLのサーバー側サニタイズを想定しています。
 *
 * <p>
 * allowTagsに許可するHTMLタグ名を指定します。
 * 指定されていないタグは除去され、テキストコンテンツのみが残ります。
 * allowTagsが未設定（null）の場合は、すべてのタグが除去されます。
 * </p>
 * <%} else {%>
 * Normalizer definition for HTML sanitization.
 * Removes HTML elements and attributes that are not allowed, converting to safe HTML.
 * Primarily intended for server-side sanitization of HTML input from RichText editors.
 *
 * <p>
 * Specify allowed HTML tag names in allowTags.
 * Tags not specified will be removed, leaving only text content.
 * If allowTags is not set (null), all tags will be removed.
 * </p>
 * <%}%>
 *
 */
public class HtmlSanitize extends NormalizerDefinition {
	private static final long serialVersionUID = 2897541036845127893L;

	private List<String> allowTags;

	public HtmlSanitize() {
	}

	public HtmlSanitize(List<String> allowTags) {
		this.allowTags = allowTags;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * 許可するHTMLタグのリストを取得します。
	 * <%} else {%>
	 * Gets the list of allowed HTML tags.
	 * <%}%>
	 *
	 * @return 許可タグのリスト
	 */
	public List<String> getAllowTags() {
		return allowTags;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * 許可するHTMLタグのリストを設定します。
	 * タグ名を指定します（例："a", "b", "p", "div", "img" 等）。
	 * <%} else {%>
	 * Sets the list of allowed HTML tags.
	 * Specify tag names (e.g., "a", "b", "p", "div", "img", etc.).
	 * <%}%>
	 *
	 * @param allowTags 許可タグのリスト
	 */
	public void setAllowTags(List<String> allowTags) {
		this.allowTags = allowTags;
	}
}
