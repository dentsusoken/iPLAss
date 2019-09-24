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

package org.iplass.mtp.command.annotation.action;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.iplass.mtp.command.annotation.CommandConfig;
import org.iplass.mtp.command.annotation.CompositeCommandConfig;
import org.iplass.mtp.command.annotation.action.cache.CacheCriteria;
import org.iplass.mtp.definition.annotation.LocalizedString;
import org.iplass.mtp.web.actionmapping.definition.ActionMappingDefinition;
import org.iplass.mtp.web.actionmapping.definition.HttpMethodType;

/**
 * ActionMappingを定義するアノテーションです。
 * 
 * @see ActionMappingDefinition
 * @author K.Higuchi
 */
@Repeatable(ActionMappings.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ActionMapping {
	String id() default "##default";
	String name();
	String displayName() default "##default";
	LocalizedString[] localizedDisplayName() default {};
	String description() default "##default";
	
	/**
	 * クライアントのキャッシュ種別（Cache-Controlヘッダの制御）を設定します。
	 * デフォルトは、UNSPECIFIED（未指定）です。
	 * 
	 * @return
	 */
	ClientCacheType clientCacheType() default ClientCacheType.UNSPECIFIED;

	/**
	 * clientCacheType=CACHEを指定した場合の
	 * クライアントキャッシュのmax-age（秒）を指定します。
	 * デフォルト値は-1でこの場合はmax-ageは未指定となります。<br>
	 * <b>注意：max-age未指定の場合、FF、Chromeでは実際はキャッシュが利用されません</b>
	 * 
	 * @param clientCacheMaxAge
	 */
	long clientCacheMaxAge() default -1;
	
	/** 許可するHTTP Methodの設定です。
	 * 未指定の場合は、すべて許可となります。
	 * デフォルト未指定（＝すべて許可）です。
	 */
	HttpMethodType[] allowMethod() default {};
	
	/** 許可するリクエストボディのContentType。デフォルト未指定（＝すべて許可） */
	String[] allowRequestContentTypes() default {};
	
	/**
	 * リクエストボディの最大サイズ（バイト）。-1の場合は無制限を表す。
	 * annotation上ではデフォルト値はLong.MIN_VALUEだが、これは未指定を表す。
	 * 
	 * @return
	 */
	long maxRequestBodySize() default Long.MIN_VALUE;

	/**
	 * multipart/form-dataの際のファイルの最大サイズ（バイト）。-1の場合は無制限を表す。
	 * annotation上ではデフォルト値はLong.MIN_VALUEだが、これは未指定を表す。
	 * １つのファイルに対する最大サイズなので、複数のファイルの合計サイズを制限したい場合は、
	 * maxRequestBodySizeを設定します。
	 * 
	 * @return
	 */
	long maxFileSize() default Long.MIN_VALUE;
	
	boolean needTrustedAuthenticate() default false;

	/**
	 * このActionMappingで指定される表示処理が部品かどうかを設定します。
	 * trueをセットした場合、クライアントからの直接呼出しが不可となります。
	 * デフォルトfalseです。
	 * 
	 * @return
	 */
	boolean parts() default false;
	
	/**
	 * このActionMappingで処理されるCommand,Templateを特権（セキュリティ制約を受けない）にて処理するかどうかを設定します。
	 * デフォルトはfalseです。
	 * publicActionとの違いは、Entity権限など、すべての権限が許可された状態で実行されます。
	 * 
	 * @return
	 */
	boolean privilaged() default false;

	/**
	 * このActionの呼び出しをAction権限設定によらず呼び出し可能にする場合は、trueを設定します。
	 * isPrivilagedとの違いは、Entityの操作などAction権限以外の権限がチェックされる状況においては、
	 * セキュリティ制約を受けます。
	 * デフォルトはfalseです。
	 * 
	 * @return
	 */
	boolean publicAction() default false;
	
	boolean overwritable() default true;
	boolean permissionSharable() default false;

	ParamMapping[] paramMapping() default {};

	/**
	 * 実行するCommandを設定します。
	 * compositeCommandが設定されている場合は、compositeCommandの設定が優先されます。
	 * 複数のCommandConnfigが設定された場合は、単純に順番に実行します。
	 * また、Command実行結果は最後のCommandの結果が返却されます。
	 * 
	 * @return
	 */
	CommandConfig[] command() default {@CommandConfig};
	
	/**
	 * 複合Commandを利用する場合の設定です。
	 * @return
	 */
	CompositeCommandConfig compositeCommand() default @CompositeCommandConfig;

	/**
	 * Command実行後の表示処理の定義です。
	 * 
	 * @return
	 */
	Result[] result() default{};

	/**
	 * <p>TokenCheck設定</p>
	 *
	 * デフォルトではTokenチェックは実行されません。
	 */
	TokenCheck tokenCheck() default @TokenCheck(executeCheck=false);

	/**
	 * サーバサイドのキャッシュ設定です。
	 * 
	 * @return
	 */
	CacheCriteria cacheCriteria() default @CacheCriteria;
	
	/**
	 * このAction処理をSessionにて同期化するか否かを設定します。
	 * デフォルトfalseです。
	 * @return
	 */
	boolean synchronizeOnSession() default false;

	/**
	 * クライアントのキャッシュ設定のenumです。<br>
	 * CACHE : "Cache-Control:private"をセット<br>
	 * NO_CACHE : "Cache-Control:no-store,no-cache"をセット
	 */
	public enum ClientCacheType {
		//TODO mustrevalidate必要か。画像とかの場合使いそう
		CACHE, NO_CACHE, UNSPECIFIED
	}

}
