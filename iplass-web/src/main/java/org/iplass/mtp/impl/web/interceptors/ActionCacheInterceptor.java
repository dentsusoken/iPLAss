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

package org.iplass.mtp.impl.web.interceptors;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.web.WebProcessRuntimeException;
import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.impl.web.actionmapping.WebInvocationImpl;
import org.iplass.mtp.impl.web.actionmapping.MetaActionMapping.ActionMappingRuntime;
import org.iplass.mtp.impl.web.actionmapping.cache.CachableHttpServletResponse;
import org.iplass.mtp.impl.web.actionmapping.cache.ContentCache;
import org.iplass.mtp.impl.web.actionmapping.cache.ContentCacheContext;
import org.iplass.mtp.impl.web.actionmapping.cache.MetaCacheCriteria.CacheCriteriaRuntime;
import org.iplass.mtp.impl.web.actionmapping.cache.blocks.IncludeActionBlock;
import org.iplass.mtp.impl.web.preview.PreviewHandler;
import org.iplass.mtp.web.interceptor.RequestInterceptor;
import org.iplass.mtp.web.interceptor.RequestInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActionCacheInterceptor implements RequestInterceptor {
	
	private static Logger logger = LoggerFactory.getLogger(ActionCacheInterceptor.class);
	
	private PreviewHandler preview = new PreviewHandler();
	
	@Override
	public void intercept(RequestInvocation invocation) {
		
		//if preview, no cache contents
		if (preview.isPreview(invocation.getRequest())) {
			invocation.proceedRequest();
			return;
		}
		
		WebInvocationImpl webInvocation = (WebInvocationImpl) invocation;
		WebRequestStack reqStack = webInvocation.getRequestStack();
		reqStack.setAttribute(CachableHttpServletResponse.ACTION_RUNTIME_NAME, webInvocation.getAction());
		
		//CachableHttpServletResponseでラップする。
		CachableHttpServletResponse response = (CachableHttpServletResponse) reqStack.getRequest().getAttribute(CachableHttpServletResponse.CHSR_NAME);
		if (response == null) {
			response = new CachableHttpServletResponse(reqStack.getResponse());
			reqStack.getRequest().setAttribute(CachableHttpServletResponse.CHSR_NAME, response);
			reqStack.setResponse(response);
		}
		
		boolean prevDoCache = response.isDoCache();
		
		try {
			ActionMappingRuntime amr = webInvocation.getAction();
			
			if (reqStack.isIncludeStack()) {
				//includeの場合
				doInclude(response, webInvocation, amr);
			} else if (reqStack.isLayoutStack()) {
				//Layoutの場合
				doLayout(response, webInvocation, amr);
			} else {
				//Client直呼び出しの場合
				doDirect(response, webInvocation, amr, true);
			}
			
			response.flushToContentCache();
			
		} catch (IOException e) {
			throw new WebProcessRuntimeException(e);
		} catch (ServletException e) {
			throw new WebProcessRuntimeException(e);
		} finally {
			response.setDoCache(prevDoCache);
		}
	}
	
	//FIXME 多段のlayoutがあると駄目？？
	
	private void doDirect(CachableHttpServletResponse response, WebInvocationImpl invocation, ActionMappingRuntime amr, boolean isClientDirect) throws ServletException, IOException {
		if (amr.getCacheCriteria() != null) {
			//キャッシュな場合
			String lang = ExecuteContext.getCurrentContext().getLanguage();
			ContentCacheContext ac = ContentCacheContext.getContentCacheContext();
			CacheCriteriaRuntime ccr = amr.getCacheCriteria();
			String key = ccr.createContentCacheKey(invocation.getRequest());
			
			if (key == null) {
				withoutCache(response, invocation);
			} else {
				ContentCache cache = ac.get(invocation.getActionName(), lang, key);
				if (cache != null) {
					//キャッシュから出力
					response.setDoCache(false);
					
					long lastModified = cache.getLastModified(invocation, ac, lang);
					
					if (isClientDirect && isNotModified(cache, lastModified, response, invocation, ac, lang)) {
						if (logger.isDebugEnabled()) {
							logger.debug("action:" + invocation.getActionName() + " cached and not modified.");
						}
						response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
					} else {
						if (lastModified < Long.MAX_VALUE) {
							if (logger.isDebugEnabled()) {
								logger.debug("action:" + invocation.getActionName() + " all stack cached. response from cache.");
							}
							if (isClientDirect) {
								setLastModAndEtag(lastModified, response, invocation, lang, ac, cache);
							}
						} else {
							if (logger.isDebugEnabled()) {
								logger.debug("action:" + invocation.getActionName() + " partial stack cached. response from cache partially.");
							}
						}
						cache.write(invocation);
					}
				} else {
					//キャッシュを新規に作成
					long ttl = 0L;
					if (ccr.getMetaData().getTimeToLive() != null) {
						ttl = ccr.getMetaData().getTimeToLive().longValue();
					}
					ContentCache newCache = new ContentCache(invocation.getActionName(), lang, key, ttl);
					response.setDoCache(true);
					response.setCurrentContentCache(newCache);
					invocation.proceedRequest();
					
					//JSPのバッファをフラッシュ
					if (invocation.getRequestStack().getPageContext() != null) {
						invocation.getRequestStack().getPageContext().getOut().flush();
					}
					response.flushToContentCache();
					
					ContentCache createdCache = response.getCurrentContentCache();
					//エラー、リダイレクトの場合はキャッシュしない
					if (!response.isError() && !response.isRedirect()
							&& ccr.canCache(invocation)) {
						ac.put(invocation.getActionName(), lang, key, createdCache);
					}
				}
			}
			
		} else {
			withoutCache(response, invocation);
		}
	}

	private void withoutCache(CachableHttpServletResponse response, WebInvocationImpl invocation) throws IOException {
		//キャッシュしない場合
		response.setDoCache(false);
		response.setCurrentContentCache(null);
		invocation.proceedRequest();
		
		//JSPのバッファをフラッシュ
		if (invocation.getRequestStack().getPageContext() != null) {
			invocation.getRequestStack().getPageContext().getOut().flush();
		}
	}

	private void setLastModAndEtag(long lastModified, CachableHttpServletResponse response,
			WebInvocationImpl invocation, String lang, ContentCacheContext ac, ContentCache cache) {
		if (lastModified >= 0 && lastModified != Long.MAX_VALUE) {
			response.setDateHeader("Last-Modified", lastModified);
			response.setHeader("ETag", cache.getEtag(lastModified, lang));
		}
	}
	
	
	private boolean isNotModified(ContentCache cache, long lastModified,
			CachableHttpServletResponse response, WebInvocationImpl invocation, ContentCacheContext cc, String lang) {
		long ifModifiedSince = invocation.getRequestStack().getRequest().getDateHeader("If-Modified-Since");
		String ifNoneMatch = invocation.getRequestStack().getRequest().getHeader("If-None-Match");
		
		//両方ない場合
		if (ifModifiedSince < 0 && ifNoneMatch == null) {
			return false;
		}
		
		boolean isNotModified = true;
		//ifModifiedSince指定あり
		if (ifModifiedSince >= 0) {
			isNotModified = (lastModified <= ifModifiedSince + 1000);//ifModifiedSinceの精度が秒なので、、
		}
		
		//ifNoneMatch指定あり
		if (isNotModified && ifNoneMatch != null) {
			String etag = cache.getEtag(lastModified, lang);
            boolean conditionSatisfied = false;
            if (!ifNoneMatch.equals("*")) {
            	StringTokenizer commaTokenizer = new StringTokenizer(ifNoneMatch, ",");
				while (!conditionSatisfied && commaTokenizer.hasMoreTokens()) {
					String currentToken = commaTokenizer.nextToken();
					if (currentToken.trim().equals(etag))
						conditionSatisfied = true;
				}
			} else {
				conditionSatisfied = true;
			}
            
			if (conditionSatisfied) {
				if ( ("GET".equals(invocation.getRequestStack().getRequest().getMethod()))
							|| ("HEAD".equals(invocation.getRequestStack().getRequest().getMethod()))) {
					isNotModified &= true;
				} else {
					isNotModified &= false;
				}
	        } else {
				isNotModified &= false;
	        }
		}
		
		return isNotModified;
	}

	private void doLayout(CachableHttpServletResponse response, WebInvocationImpl invocation, ActionMappingRuntime amr) throws ServletException, IOException {
//		response.flushToContentCache(invocation.getRequestStack());
		//contentActionにlayoutAction名をセット
		ContentCache prevCc = response.getCurrentContentCache();
		if (prevCc != null) {
			prevCc.setLayoutActionName(invocation.getActionName());
		}
		
		invocation.getRequestStack().setAttribute(CachableHttpServletResponse.CONTENT_CACHE_NAME, prevCc);
		response.setCurrentContentCache(null);
		
		doDirect(response, invocation, amr, false);
		
		response.setCurrentContentCache(prevCc);
	}

	private void doInclude(CachableHttpServletResponse response, WebInvocationImpl invocation, ActionMappingRuntime amr) throws ServletException, IOException {
		//include元のキャッシュをflush&IncludeContent
		response.flushToContentCache();
		ContentCache prevCc = response.getCurrentContentCache();
		if (prevCc != null) {
			prevCc.addContent(new IncludeActionBlock(invocation.getActionName()));
		}
		
		doDirect(response, invocation, amr, false);
		
		response.setCurrentContentCache(prevCc);
	}

}
