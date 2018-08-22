/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.shared.tools.dto.entityexplorer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.definition.EntityDefinition;


public class EntityDataListResultInfo implements Serializable {

	/** シリアルバージョンNo */
	private static final long serialVersionUID = 6123091297872728806L;

	private String definitionName;

	private EntityDefinition definition;

	private List<Entity> records;

	private boolean isError;
	private List<String> logMessages;


	/**
	 * コンストラクタ
	 */
	public EntityDataListResultInfo() {
		isError = false;
		logMessages = new ArrayList<String>();
	}


	public void setDefinitionName(String definitionName) {
		this.definitionName = definitionName;
	}

	public String getDefinitionName() {
		return definitionName;
	}

	public void setDefinition(EntityDefinition definition) {
		this.definition = definition;
	}

	public EntityDefinition getDefinition() {
		return definition;
	}

	public void setRecords(List<Entity> records) {
		this.records = records;
	}

	public List<Entity> getRecords() {
		return records;
	}

	public void addLogMessage(String message) {
		logMessages.add(message);
	}

	public List<String> getLogMessages() {
		return logMessages;
	}

	public void setError(boolean isError) {
		this.isError = isError;
	}

	public boolean isError() {
		return isError;
	}

}
