/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import org.iplass.mtp.entity.PropertyValidator;
import org.iplass.mtp.entity.definition.ValidationDefinition;

/**
 * JavaのクラスによるValidation定義。
 * {@link PropertyValidator}の実装クラスを指定します。
 * asArrayフラグがtrueにセットされる場合、検証対象が配列の場合、分解せず配列のままPropertyValidatorのvalueへ渡します。
 * 
 * @author K.Higuchi
 *
 */
public class JavaClassValidation extends ValidationDefinition {
	private static final long serialVersionUID = -4742425715523483901L;

	private String className;
	private boolean asArray = false;

	public JavaClassValidation() {
	}
	
	public JavaClassValidation(String className, String errorMessage) {
		this(className, errorMessage, null);
	}

	public JavaClassValidation(String className, String errorMessage, String errorCode) {
		this.className = className;
		setErrorMessage(errorMessage);
		setErrorCode(errorCode);
	}
	
	public boolean isAsArray() {
		return asArray;
	}

	public void setAsArray(boolean asArray) {
		this.asArray = asArray;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

}
