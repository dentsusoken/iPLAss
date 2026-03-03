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

/**
 * <% if (doclang == "ja") {%>
 * HTMLサニタイズのポリシー定義です。
 * 許可するHTMLタグの範囲をプリセットとして定義します。
 * <%} else {%>
 * HTML sanitize policy definition.
 * Defines the scope of allowed HTML tags as a preset.
 * <%}%>
 */
public enum HtmlSanitizePolicy {

	/**
	 * <% if (doclang == "ja") {%>
	 * すべてのHTMLタグを除去します。テキストコンテンツのみが残ります。
	 * <%} else {%>
	 * Removes all HTML tags. Only text content remains.
	 * <%}%>
	 */
	NONE,

	/**
	 * <% if (doclang == "ja") {%>
	 * 基本的なインライン書式タグ（b, em, i, strong, u）を許可します。
	 * <%} else {%>
	 * Allows basic inline formatting tags (b, em, i, strong, u).
	 * <%}%>
	 */
	SIMPLE_TEXT,

	/**
	 * <% if (doclang == "ja") {%>
	 * 基本的な書式タグおよび構造タグを許可します。
	 * a（href属性付き）、b, blockquote, br, cite, code, dd, dl, dt, em, i,
	 * li, ol, p, pre, q, small, span, strike, strong, sub, sup, u, ul。
	 * <%} else {%>
	 * Allows basic formatting and structural tags.
	 * a (with href), b, blockquote, br, cite, code, dd, dl, dt, em, i,
	 * li, ol, p, pre, q, small, span, strike, strong, sub, sup, u, ul.
	 * <%}%>
	 */
	BASIC,

	/**
	 * <% if (doclang == "ja") {%>
	 * BASICポリシーに加え、img タグ（src, alt, title 属性付き）を許可します。
	 * <%} else {%>
	 * In addition to the BASIC policy, allows img tag (with src, alt, title attributes).
	 * <%}%>
	 */
	BASIC_WITH_IMAGES,

	/**
	 * <% if (doclang == "ja") {%>
	 * リッチテキストエディタで一般的に利用されるHTMLタグを広く許可します。
	 * h1-h6, div, table関連タグ, a, img, 各種書式タグ等が含まれます。
	 * script, iframe, form 等の危険なタグは除去されます。
	 * <%} else {%>
	 * Allows a wide range of HTML tags commonly used in rich text editors.
	 * Includes h1-h6, div, table-related tags, a, img, various formatting tags, etc.
	 * Dangerous tags such as script, iframe, form are removed.
	 * <%}%>
	 */
	RELAXED
}
