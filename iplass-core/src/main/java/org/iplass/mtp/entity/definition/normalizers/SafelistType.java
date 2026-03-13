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
 * HTMLサニタイズで使用するSafelistのプリセットタイプです。
 * jsoupの{@code org.jsoup.safety.Safelist}のファクトリメソッドに対応します。
 * <%} else {%>
 * Preset types for Safelist used in HTML sanitization.
 * Corresponds to the factory methods of jsoup's {@code org.jsoup.safety.Safelist}.
 * <%}%>
 */
public enum SafelistType {

	/** すべてのHTMLを除去（テキストのみ） */
	NONE,

	/** b, em, i, strong, u のみ許可 */
	SIMPLE_TEXT,

	/** 基本的なHTMLタグ（a, b, blockquote, br, cite, code, dd, dl, dt, em, i, li, ol, p, pre, q, small, span, strike, strong, sub, sup, u, ul）を許可 */
	BASIC,

	/** basic に加えて img タグを許可 */
	BASIC_WITH_IMAGES,

	/** 広範囲のHTMLタグ・属性を許可 */
	RELAXED
}