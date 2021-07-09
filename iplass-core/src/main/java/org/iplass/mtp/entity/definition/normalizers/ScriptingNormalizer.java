/*
 * Copyright (C) 2021 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
 * <p>
 * ScriptによるNormalizer定義です。<br>
 * 正規化後の値を返却するように実装します。
 * asArrayフラグがtrueにセットされる場合、検証対象が配列の場合、分解せず配列のまま正規化Scriptのvalueへ渡します。
 * </p>
 * <h5>Script上から参照可能な変数</h5>
 * <ul>
 * <li>entity:正規化対象のEntityのインスタンス</li>
 * <li>propertyName:正規化対象のEntityのプロパティ名</li>
 * <li>value:正規化対象のEntityのインスタンスのプロパティに設定されている値</li>
 * <li>context:ValidationContextのインスタンス</li>
 * </ul>
 * <%} else {%>
 * <p>
 * This is the Normalizer definition by Script. <br>
 * It is implemented to return the normalized value.
 * When the asArray flag is set to true, if the validation target is an array,
 * it will be passed as an array to the value of the Normalizer Script.
 * </p>
 * <h5>Variables that can be referenced from within the script</h5>
 * <ul>
 * <li>entity:Instance of the Entity</li>
 * <li>propertyName:The property name to be normalized of the Entity</li>
 * <li>value:Value of the property of the Entity instance to be normalized</li>
 * <li>context:An instance of ValidationContext</li>
 * </ul>
 * <%}%>
 * <h5>Script Code Example</h5>
 * <pre>
 * if (value == null) {
 *   return null;
 * }
 * if (!value.startsWith('p_') {
 *   return 'p_' + value;
 * } else {
 *   return value;
 * }
 * </pre>
 * 
 * @author K.Higuchi
 *
 */
public class ScriptingNormalizer extends NormalizerDefinition {
	private static final long serialVersionUID = 1313549398564608707L;

	private String script;
	private boolean asArray = false;

	public ScriptingNormalizer() {
	}

	public ScriptingNormalizer(String script) {
		this.script = script;
	}

	public boolean isAsArray() {
		return asArray;
	}

	public void setAsArray(boolean asArray) {
		this.asArray = asArray;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

}
