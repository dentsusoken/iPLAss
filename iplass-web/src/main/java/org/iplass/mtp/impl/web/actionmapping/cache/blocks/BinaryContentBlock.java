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

package org.iplass.mtp.impl.web.actionmapping.cache.blocks;

import java.io.IOException;

import javax.servlet.ServletException;

import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.impl.web.actionmapping.WebInvocationImpl;
import org.iplass.mtp.impl.web.actionmapping.cache.ContentBlock;
import org.iplass.mtp.impl.web.actionmapping.cache.ContentCacheContext;

public class BinaryContentBlock implements ContentBlock {
	private static final long serialVersionUID = 773587476554049941L;
	
	private final byte[] content;
	
	public BinaryContentBlock(byte[] content) {
		this.content = content;
	}

	public byte[] getContent() {
		return content;
	}

	@Override
	public void writeTo(WebRequestStack request) throws IOException,
			ServletException {
		if (content != null) {
			request.getResponse().getOutputStream().write(content);
		}
	}

	@Override
	public long lastModified(long lastModified, WebInvocationImpl invocation, ContentCacheContext cc, String lang) {
		return lastModified;
	}

}
