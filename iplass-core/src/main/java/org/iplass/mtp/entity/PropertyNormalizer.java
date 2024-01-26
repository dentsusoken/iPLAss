/*
 * Copyright (C) 2021 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.entity;

/**
 * 
 * <% if (doclang == "ja") {%>
 * Entityのプロパティの値の正規化をJavaクラスでカスタム実装する際のインタフェースです。<br>
 * 
 * 正規化は検証の直前に実行されます。<br>
 * 正規化は、同一のプロパティの値に対して複数回呼び出される可能性があります。
 * htmlサニタイジング処理のような、複数回呼び出された場合に問題がある処理はPropertyNormalizerで実装しないでください。
 * <%} else {%>
 * It is an interface for custom implementation in Java class that normalizes the value of Entity property.<br>
 * 
 * Normalization is performed just before validation.<br>
 * Normalization can be called multiple times for the same property value.
 * Do not implement logic that has problems when called multiple times like html sanitizing, with PropertyNormalizer.
 * <%}%>
 * 
 * 
 * <h5>Example</h5>
 * <pre>
 * public class CustomPropertyNormalizer<String> implements PropertyNormalizer {
 *   public String normalize(Object value, ValidationContext context) {
 *     if (value == null) {
 *       return null;
 *     }
 *     
 *     return value.toString().replace(".", "_");
 *   }
 * }
 * </pre>
 * 
 * @author K.Higuchi
 *
 * @param <T>
 */
public interface PropertyNormalizer<T> {
	
	public T normalize(Object value, ValidationContext context);

}
