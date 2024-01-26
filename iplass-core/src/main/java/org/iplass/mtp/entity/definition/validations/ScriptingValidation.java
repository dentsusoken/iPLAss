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

package org.iplass.mtp.entity.definition.validations;

import org.iplass.mtp.entity.definition.ValidationDefinition;

/**
 * <p>
 * ScriptによるValidation定義です。
 * Scriptにより、falseを返却した場合、検証エラーとなります。
 * asArrayフラグがtrueにセットされる場合、検証対象が配列の場合、分解せず配列のまま検証Scriptのvalueへ渡します。
 * </p>
 * <h5>Script上から参照可能な変数</h5>
 * <ul>
 * <li>entity:検証対象のEntityのインスタンス</li>
 * <li>propertyName:検証対象のEntityのプロパティ名</li>
 * <li>value:検証対象のEntityのインスタンスのプロパティに設定されている値</li>
 * <li>context:ValidationContextのインスタンス</li>
 * </ul>
 * <h5>Scriptコード例）</h5>
 * <pre>
 * if (value == null) {
 *   return false;
 * }
 * if (value.equals("test")) {
 *   return true;
 * }
 * if (entity.name.equals(value)) {
 *   return true;
 * }
 * return false;
 * </pre>
 * 
 * @author K.Higuchi
 *
 */
public class ScriptingValidation extends ValidationDefinition {
	private static final long serialVersionUID = -3806173080063387599L;
	
	private String script;
	private boolean asArray = false;

	public ScriptingValidation() {
	}
	
	public ScriptingValidation(String script, String errorMessage) {
		this(script, errorMessage, null);
	}

	public ScriptingValidation(String script, String errorMessage, String errorCode) {
		this.script = script;
		setErrorMessage(errorMessage);
		setErrorCode(errorCode);
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
