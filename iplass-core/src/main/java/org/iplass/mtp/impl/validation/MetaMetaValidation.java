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

package org.iplass.mtp.impl.validation;

import org.iplass.mtp.entity.definition.ValidationDefinition;
import org.iplass.mtp.impl.metadata.MetaDataRuntime;

/**
 * Validation自体のメタデータ
 *
 * @author K.Higuchi
 *
 */
public class MetaMetaValidation {

	private String id;
	private String displayName;
	private Class<?> validationDefinitionClass;
	private Class<?>[] acceptableType;
	private ValidationDefinition defaultDefinition;

	public ValidationDefinition getDefaultDefinition() {
		return defaultDefinition;
	}

	public void setDefaultDefinition(ValidationDefinition defaultDefinition) {
		this.defaultDefinition = defaultDefinition;
	}

	public Class<?>[] getAcceptableType() {
		return acceptableType;
	}

	public void setAcceptableType(Class<?>[] acceptableType) {
		this.acceptableType = acceptableType;
	}

	public Class<?> getValidationDefinitionClass() {
		return validationDefinitionClass;
	}

	public void setValidationDefinitionClass(Class<?> validationDefinitionClass) {
		this.validationDefinitionClass = validationDefinitionClass;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public MetaMetaValidation copy() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setName(String name) {
		// TODO Auto-generated method stub

	}

	public MetaDataRuntime handler() {
		// TODO Auto-generated method stub
		return null;
	}

	public void initHandler() {
		// TODO Auto-generated method stub

	}

}
