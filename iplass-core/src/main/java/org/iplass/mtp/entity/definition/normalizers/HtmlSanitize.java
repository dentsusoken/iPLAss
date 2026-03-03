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
 * <%} else {%>
 * Normalizer definition for HTML sanitization.
 * Removes HTML elements and attributes that are not allowed, converting to safe HTML.
 * Primarily intended for server-side sanitization of HTML input from RichText editors.
 * <%}%>
 *
 */
public class HtmlSanitize extends NormalizerDefinition {
	private static final long serialVersionUID = 2897541036845127893L;

	private HtmlSanitizePolicy policy;

	public HtmlSanitize() {
	}

	public HtmlSanitize(HtmlSanitizePolicy policy) {
		this.policy = policy;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * サニタイズポリシーを取得します。
	 * <%} else {%>
	 * Gets the sanitize policy.
	 * <%}%>
	 *
	 * @return サニタイズポリシー
	 */
	public HtmlSanitizePolicy getPolicy() {
		return policy;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * サニタイズポリシーを設定します。
	 * <%} else {%>
	 * Sets the sanitize policy.
	 * <%}%>
	 *
	 * @param policy サニタイズポリシー
	 */
	public void setPolicy(HtmlSanitizePolicy policy) {
		this.policy = policy;
	}
}
