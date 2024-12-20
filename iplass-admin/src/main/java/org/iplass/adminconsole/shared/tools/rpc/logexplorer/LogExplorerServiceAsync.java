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

package org.iplass.adminconsole.shared.tools.rpc.logexplorer;

import java.util.List;

import org.iplass.adminconsole.shared.tools.dto.logexplorer.LogConditionInfo;
import org.iplass.adminconsole.shared.tools.dto.logexplorer.LogFile;
import org.iplass.adminconsole.shared.tools.dto.logexplorer.LogFileCondition;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * LogExplorer用AsyncService
 */
public interface LogExplorerServiceAsync {

	void getLogfileNames(final int tenantId, LogFileCondition logFileCondition, AsyncCallback<List<LogFile>> callback);

	void getLogConditions(final int tenantId, AsyncCallback<List<LogConditionInfo>> callback);

	void applyLogConditions(final int tenantId, List<LogConditionInfo> logConditions, AsyncCallback<String> callback);

}
