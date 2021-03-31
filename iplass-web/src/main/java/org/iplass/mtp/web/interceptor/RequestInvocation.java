/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.web.interceptor;

import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.web.RequestPath;

public interface RequestInvocation {
	
	public void proceedRequest();
	public void proceedResult();
	
	public void redirectAction(String actionName, RequestContext requestContext);
	
	public RequestContext getRequest();
	public Command getCommand();
	public String getStatus();
	public Throwable getException();
	
	public void getStatus(String status);
	public void setException(Throwable exception);
	
	public boolean isInclude();
	
	public String getActionName();
	
	public RequestPath getRequestPath();

}
