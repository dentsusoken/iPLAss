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

package org.iplass.mtp.impl.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.web.actionmapping.ResponseHeader;

public class ResponseHeaderImpl implements ResponseHeader {
	
	private HttpServletResponse response;
	
	ResponseHeaderImpl() {
		WebRequestStack stack = WebRequestStack.getCurrent();
		if (stack != null) {
			response = stack.getResponse();
		}
	}

	@Override
	public void addCookie(Cookie cookie) {
		if (response != null) {
			response.addCookie(cookie);
		}
	}

	@Override
	public boolean containsHeader(String name) {
		if (response != null) {
			return response.containsHeader(name);
		} else {
			return false;
		}
	}

	//TODO sendErrorは非公開か、例外をスローの形
//	@Override
//	public void sendError(int sc, String msg) {
//		if (response != null) {
//			try {
//				response.sendError(sc, msg);
//			} catch (IOException e) {
//				throw new WebProcessRuntimeException(e);
//			}
//		}
//	}
//
//	@Override
//	public void sendError(int sc) {
//		if (response != null) {
//			try {
//				response.sendError(sc);
//			} catch (IOException e) {
//				throw new WebProcessRuntimeException(e);
//			}
//		}
//	}
//
	@Override
	public void setDateHeader(String name, long date) {
		if (response != null) {
			response.setDateHeader(name, date);
		}
	}

	@Override
	public void addDateHeader(String name, long date) {
		if (response != null) {
			response.addDateHeader(name, date);
		}
	}

	@Override
	public void setHeader(String name, String value) {
		if (response != null) {
			response.setHeader(name, StringUtil.removeLineFeedCode(value));
		}
	}

	@Override
	public void addHeader(String name, String value) {
		if (response != null) {
			response.addHeader(name, StringUtil.removeLineFeedCode(value));
		}
	}

	@Override
	public void setIntHeader(String name, int value) {
		if (response != null) {
			response.setIntHeader(name, value);
		}
	}

	@Override
	public void addIntHeader(String name, int value) {
		if (response != null) {
			response.addIntHeader(name, value);
		}
	}

	@Override
	public void setStatus(int sc) {
		if (response != null) {
			response.setStatus(sc);
		}
	}

}
