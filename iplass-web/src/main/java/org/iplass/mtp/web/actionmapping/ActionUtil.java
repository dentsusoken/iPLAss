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

package org.iplass.mtp.web.actionmapping;

import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.web.WebUtil;
import org.iplass.mtp.impl.web.actionmapping.cache.ContentCacheContext;
import org.iplass.mtp.web.WebRequestConstants;
import org.iplass.mtp.web.actionmapping.definition.cache.RelatedEntityType;

/**
 * Actionに関連する処理のユーティリティクラス。
 * 
 * @author K.Higuchi
 *
 */
public class ActionUtil {
	
	/**
	 * 現在のAction処理コンテキストの中で、
	 * プログラムから明示的に当該のEntityをキャッシュに関連付ける。
	 * 
	 * @param entityName
	 * @param oid
	 */
	public static void linkToActionCache(String entityName, String oid) {
		ContentCacheContext.getContentCacheContext().recordForce(RelatedEntityType.SPECIFIC_ID, entityName, oid);
	}
	
	/**
	 * 現在のAction処理コンテキストの中で、
	 * プログラムから明示的に当該のEntity名のEntityすべてをキャッシュに関連付ける。
	 * 
	 * @param entityName
	 */
	public static void linkToActionCache(String entityName) {
		ContentCacheContext.getContentCacheContext().recordForce(RelatedEntityType.WHOLE, entityName, null);
	}
	
	/**
	 * 現在のAction処理コンテキストの中で、
	 * キャッシュの有効切れする日時を指定（参照：System.currentTimeMillis()）する。
	 * expiresには、ミリ秒で測定した、現在時刻と協定世界時の UTC 1970 年 1 月 1 日午前 0 時との差を設定する。
	 * すでに、指定する日時より以前にキャッシュ有効期限が設定されていた場合は、そちらが（期限が短い方が）優先される。
	 * 
	 * @param expires ミリ秒で測定した、現在時刻と協定世界時の UTC 1970 年 1 月 1 日午前 0 時との差
	 */
	public static void setCacheExpires(long expires) {
		ContentCacheContext.getContentCacheContext().setCacheExpires(expires);
	}
	
	/**
	 * 現在のAciton処理コンテキストの中で、
	 * クライアントへのHTTPレスポンスのヘッダー情報を扱う際利用可能なResponseHeaderを取得する。
	 * Webからの呼び出しの場合のみ取得可能。
	 * 
	 * @return
	 */
	public static ResponseHeader getResponseHeader() {
		RequestContext context = WebUtil.getRequestContext();
		if (context == null) {
			return null;
		} else {
			return (ResponseHeader) context.getAttribute(WebRequestConstants.RESPONSE_HEADER);
		}
	}
	
	/**
	 * 指定のactionNameに関連するキャッシュをクリアする。
	 * 
	 * @param actionName
	 */
	public static void clearActionCache(String actionName) {
		ContentCacheContext.getContentCacheContext().invalidateByActionName(actionName);
	}
	
	/**
	 * すべてのActionキャッシュをクリアする。
	 * 
	 */
	public static void clearAllActionCache() {
		ContentCacheContext.getContentCacheContext().invalidateAllEntry();
	}

}
