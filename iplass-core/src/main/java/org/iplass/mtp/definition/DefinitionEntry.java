/*
 * Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.definition;

import java.io.Serializable;

/**
 * Definitionおよび、DefinitionInfo（Definitionのメタデータ）を保持するためのクラスです。
 * 
 * @author K.Higuchi
 *
 */
public class DefinitionEntry implements Serializable {

	private static final long serialVersionUID = 5353106054693573775L;

	private Definition definition;

	private DefinitionInfo definitionInfo;

	public DefinitionInfo getDefinitionInfo() {
		return definitionInfo;
	}

	public void setDefinitionInfo(DefinitionInfo definitionInfo) {
		this.definitionInfo = definitionInfo;
	}

	public Definition getDefinition() {
		return definition;
	}

	public void setDefinition(Definition definition) {
		this.definition = definition;
	}
}
