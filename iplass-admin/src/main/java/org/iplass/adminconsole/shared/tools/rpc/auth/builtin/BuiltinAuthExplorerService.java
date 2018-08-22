/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.shared.tools.rpc.auth.builtin;

import java.util.List;

import org.iplass.adminconsole.shared.tools.dto.auth.builtin.BuiltinAuthUserDto;
import org.iplass.adminconsole.shared.tools.dto.auth.builtin.BuiltinAuthUserListResultDto;
import org.iplass.adminconsole.shared.tools.dto.auth.builtin.BuiltinAuthUserResetErrorCountResultInfo;
import org.iplass.adminconsole.shared.tools.dto.auth.builtin.BuiltinAuthUserSearchConditionDto;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.client.rpc.XsrfProtectedService;

@RemoteServiceRelativePath("service/authexplorer")
public interface BuiltinAuthExplorerService extends XsrfProtectedService {

	public BuiltinAuthUserListResultDto search(final int tenantId, BuiltinAuthUserSearchConditionDto cond);

	public BuiltinAuthUserResetErrorCountResultInfo resetErrorCount(final int tenantId, final List<BuiltinAuthUserDto> users);

}
