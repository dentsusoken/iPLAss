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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;

import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.impl.web.actionmapping.ActionMappingService;
import org.iplass.mtp.impl.web.actionmapping.WebInvocationImpl;
import org.iplass.mtp.impl.web.actionmapping.MetaActionMapping.ActionMappingRuntime;
import org.iplass.mtp.impl.web.actionmapping.cache.MetaCacheCriteria.CacheCriteriaRuntime;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContentCache implements Serializable {
	private static final long serialVersionUID = -1814955389132383329L;

	private static Logger logger = LoggerFactory.getLogger(ContentCache.class);

	private final String key;
	private final String actionName;
	private final String lang;

	private final long creationTime;//キャッシュの追い越しチェック用。とりあえず時間。

	//Cookie（の操作）はキャッシュしない。

	private Integer httpStatus;
//	private String httpStatusMessage;//setStatus時のmessageは非推奨なので対応しない
	private String contentType;
	private List<Header> header;

	private String layoutActionName;

	private List<ContentBlock> content;

	private Set<String> relatedEntityName;
	private Set<String> relatedEntityNameAndOid;

	private long expires;

	private String etagBase;

	public ContentCache(String actionName, String lang, String key, long timeTolive) {
		this.actionName = actionName;
		this.lang = lang;
		this.key = key;
		creationTime = System.currentTimeMillis();
		if (timeTolive > 0) {
			expires = creationTime + timeTolive;
		} else {
			expires = Long.MAX_VALUE;
		}
		etagBase = StringUtil.randomToken();
	}

	public String getEtag(long lastModified, String lang) {
		return "W/\"" + etagBase + "-" + lastModified + "-" + lang + "\"";
	}

	public long getLastModified(WebInvocationImpl invocation, ContentCacheContext cc, String lang) {
		long lastModified = creationTime;
		if (content != null) {
			for (ContentBlock c: content) {
				lastModified = c.lastModified(lastModified, invocation, cc, lang);
			}
		}

		//layoutがある場合、そっちもチェック
		if (layoutActionName != null && lastModified < Long.MAX_VALUE) {
			ActionMappingRuntime amr = ServiceRegistry.getRegistry().getService(ActionMappingService.class).getRuntimeByName(layoutActionName);
			if (amr == null) {
				return Long.MAX_VALUE;
			}

			CacheCriteriaRuntime ccr = amr.getCacheCriteria();
			if (ccr == null) {
				return Long.MAX_VALUE;
			}

			String key = ccr.createContentCacheKey(invocation.getRequest());

			ContentCache layoutCache = cc.get(layoutActionName, lang, key);
			if (layoutCache == null) {
				return Long.MAX_VALUE;
			}

			long layoutLastMod = layoutCache.getLastModified(invocation, cc, lang);
			lastModified = Math.max(lastModified, layoutLastMod);
		}

		return lastModified;
	}

	public Set<String> getRelatedEntityName() {
		return relatedEntityName;
	}

	public void setRelatedEntityName(Set<String> relatedEntityName) {
		this.relatedEntityName = relatedEntityName;
	}

	public Set<String> getRelatedEntityNameAndOid() {
		return relatedEntityNameAndOid;
	}

	public void setRelatedEntityNameAndOid(Set<String> relatedEntityNameAndOid) {
		this.relatedEntityNameAndOid = relatedEntityNameAndOid;
	}

	public void addRelatedEntity(String entityName, String oid) {
		if (relatedEntityName == null) {
			relatedEntityName = new HashSet<String>();
		}
		if (relatedEntityNameAndOid == null) {
			relatedEntityNameAndOid = new HashSet<String>();
		}
		relatedEntityName.add(entityName);
		relatedEntityNameAndOid.add(entityName + ";" + oid);
	}

	public String getLayoutActionName() {
		return layoutActionName;
	}

	public void setLayoutActionName(String layoutActionName) {
		this.layoutActionName = layoutActionName;
	}

	public long getCreationTime() {
		return creationTime;
	}

	public void resetResponseData() {
		httpStatus = null;
//		httpStatusMessage = null;
		contentType = null;
		header = null;
		content = null;
	}

	public void resetContents() {
		content = null;
	}

	public Integer getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(Integer httpStatus) {
		this.httpStatus = httpStatus;
	}

//	public String getHttpStatusMessage() {
//		return httpStatusMessage;
//	}
//
//	public void setHttpStatusMessage(String httpStatusMessage) {
//		this.httpStatusMessage = httpStatusMessage;
//	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public List<Header> getHeader() {
		return header;
	}

	public void setHeader(List<Header> header) {
		this.header = header;
	}

	public void addHeader(Header h) {
		if (header == null) {
			header = new ArrayList<Header>();
		}
		header.add(h);
	}

	public List<ContentBlock> getContent() {
		return content;
	}

	public void setContent(List<ContentBlock> content) {
		this.content = content;
	}

	public void addContent(ContentBlock contentBlock) {
		if (content == null) {
			content = new ArrayList<ContentBlock>();
		}
		content.add(contentBlock);
	}

	public long getExpires() {
		return expires;
	}

	public void setExpires(long expires) {
		this.expires = expires;
	}

	public String getKey() {
		return key;
	}

	public String getActionName() {
		return actionName;
	}

	public void write(WebInvocationImpl webInvocation) throws IOException, ServletException {

		if (getLayoutActionName() == null) {
			//layout利用していない場合
			writeDirect(webInvocation.getRequestStack());
		} else {

//ここのしょりがあやしいか。。。
			//layout利用の場合
			ActionMappingService ams = ServiceRegistry.getRegistry().getService(ActionMappingService.class);
			ActionMappingRuntime layout = ams.getRuntimeByName(getLayoutActionName());

			WebRequestStack newStack = null;
			try {
				newStack = new WebRequestStack();
				newStack.setLayoutStack(true);
				newStack.setAttribute("org.iplass.mtp.contentTemplate", this);
				if (layout != null) {
					layout.executeCommand(newStack);
				}
			} finally {
				if (newStack != null) {
					newStack.finallyProcess();
				}
			}
		}
	}

	public void writeContent(WebRequestStack request) throws IOException, ServletException {
		writeDirect(request);
	}

	private void writeDirect(WebRequestStack request)
			throws IOException, ServletException {

		if (logger.isDebugEnabled()) {
			logger.debug("write content from cache:action=" + actionName + ", lang=" + lang + ", key=" + key);
		}

		HttpServletResponse res = request.getResponse();
		if (getContentType() != null) {
			res.setContentType(getContentType());
		}
		if (getHttpStatus() != null) {
			res.setStatus(getHttpStatus());
		}
		if (getHeader() != null) {
			for (Header h: getHeader()) {
				switch (h.getOpeType()) {
				case ADD:
					switch (h.getValType()) {
					case DATE:
						res.addDateHeader(h.getName(), (Long) h.getValue());
						break;
					case INT:
						res.addIntHeader(h.getName(), (Integer) h.getValue());
						break;
					case STRING:
						res.addHeader(h.getName(), (String) h.getValue());
						break;
					default:
						break;
					}
					break;
				case SET:
					switch (h.getValType()) {
					case DATE:
						res.setDateHeader(h.getName(), (Long) h.getValue());
						break;
					case INT:
						res.setIntHeader(h.getName(), (Integer) h.getValue());
						break;
					case STRING:
						res.setHeader(h.getName(), (String) h.getValue());
						break;
					default:
						break;
					}
					break;
				default:
					break;
				}
			}
		}

		if (getContent() != null) {
			for (ContentBlock cb: getContent()) {
				cb.writeTo(request);
			}
		}
	}


}
