/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.web.actionmapping.cache;

import java.io.IOException;
import java.io.Serializable;

import jakarta.servlet.ServletException;

import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.impl.web.actionmapping.WebInvocationImpl;

public interface ContentBlock extends Serializable {
	
	public void writeTo(WebRequestStack request) throws IOException, ServletException;

	public long lastModified(long lastModified, WebInvocationImpl invocation, ContentCacheContext cc, String lang);
	
}
