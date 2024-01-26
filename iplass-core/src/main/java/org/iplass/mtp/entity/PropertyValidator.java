/*
 * Copyright (C) 2019 DENTSU SOKEN INC. All Rights Reserved.
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
 * <p>
 * Entityのプロパティの値の検証をJavaクラスでカスタム実装する際のインタフェースです。
 * </p>
 * 
 * <h5>実装例）</h5>
 * <pre>
 * public class CustomPropertyValidator implements PropertyValidator {
 *   public boolean validate(Object value, ValidationContext context) {
 *     if (value == null) {
 *       return false;
 *     }
 *     if (value instanceof String) {
 *       //contextにセットした値はエラーメッセージに埋め込み可能です。
 *       context.setAttribute("type", "String");
 *       return false;
 *     }
 *     
 *     return true;
 *   }
 * }
 * </pre>
 * 
 * @author K.Higuchi
 *
 */
public interface PropertyValidator {
	
	/**
	 * valueの値を検証するコードを実装します。
	 * falseを返却した場合、検証エラーとなります。
	 * 
	 * contextにsetAttributeした値は、エラーメッセージにて埋め込みが可能となります。
	 * 
	 * @param value
	 * @param context
	 * @return
	 */
	public boolean validate(Object value, ValidationContext context);

}
