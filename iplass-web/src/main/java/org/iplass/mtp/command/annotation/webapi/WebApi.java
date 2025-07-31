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

package org.iplass.mtp.command.annotation.webapi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.iplass.mtp.command.annotation.CommandConfig;
import org.iplass.mtp.webapi.WebApiRequestConstants;
import org.iplass.mtp.webapi.definition.CacheControlType;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.RequestType;
import org.iplass.mtp.webapi.definition.StateType;


/**
 * WebAPIの定義。
 *
 * @author K.Takahashi
 *
 */
@Repeatable(WebApis.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface WebApi {

	String id() default "##default";
	String name();
	String displayName() default "##default";
	String description() default "##default";

	/**
	 * WebAPIのキャッシュ種別（Cache-Controlヘッダの制御）を設定します。
	 * デフォルトは、UNSPECIFIED（未指定）です。
	 *
	 * @return WebAPIのキャッシュ種別
	 */
	CacheControlType cacheControlType() default CacheControlType.UNSPECIFIED;

	/**
	 * cacheControlType=CACHEを指定した場合の
	 * WebAPIキャッシュのmax-age（秒）を指定します。
	 * デフォルト値は-1でこの場合はmax-ageは未指定となります。<br>
	 * <b>注意：max-age未指定の場合、FF、Chromeでは実際はキャッシュが利用されません</b>
	 *
	 * @param cacheControlMaxAge
	 */
	long cacheControlMaxAge() default -1;

	boolean checkXRequestedWithHeader() default true;
	/** @deprecated {@link #privileged()} を使用してください。 */
	@Deprecated
	boolean privilaged() default false;
	boolean privileged() default false;
	boolean publicWebApi() default false;
	boolean overwritable() default true;
	boolean permissionSharable() default false;

	/** @deprecated {@link #accessControlAllowOrigin()} を使用してください。 */
	@Deprecated
	String accessControlAllowOrign() default "";
	String accessControlAllowOrigin() default "";
	boolean accessControlAllowCredentials() default false;
	boolean needTrustedAuthenticate() default false;

	WebApiParamMapping[] paramMapping() default {};

	CommandConfig command() default @CommandConfig;
	RequestType[] accepts() default{};
	MethodType[] methods() default{};

	/**
	 * 許可するリクエストボディのContentType。デフォルト未指定（＝すべて許可）。<br>
	 * accepts指定より、allowRequestContentTypesの指定による制限が優先されます。<br>
	 * 例えば、
	 * accepts指定によりJSON形式の処理が有効化されている場合において、
	 * allowRequestContentTypesに"application/json"が含まれない場合は、
	 * JSON形式によるリクエストは処理されません。
	 *
	 */
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
	StateType state() default StateType.STATEFUL;
	boolean supportBearerToken() default false;
	String[] oauthScopes() default{};

	/**
	 * @deprecated {@link #responseResults()} を利用してください。本メソッドは大きなバージョンアップの際に削除する予定です。
	 */
	@Deprecated
	String[] results() default {WebApiRequestConstants.DEFAULT_RESULT};

	/**
	 * WebAPIの結果に関する属性を設定します。
	 * @return WebAPIの結果に関する属性
	 */
	WebApiResultAttribute[] responseResults() default { @WebApiResultAttribute(name = WebApiRequestConstants.DEFAULT_RESULT) };

	RestJson restJson() default @RestJson(parameterName="");
	RestXml restXml() default @RestXml(parameterName="");

	/**
	 * <p>TokenCheck設定</p>
	 *
	 * デフォルトではTokenチェックは実行されません。
	 */
	WebApiTokenCheck tokenCheck() default @WebApiTokenCheck(executeCheck=false);

	/**
	 * このAction処理をSessionにて同期化するか否か。デフォルトfalse。
	 * @return
	 */
	boolean synchronizeOnSession() default false;

	String responseType() default "";

	/**
	 * スタブレスポンスを返却するかどうか。
	 * <p>
	 * true を設定するとスタブレスポンスを返却します。デフォルトの設定値は false です。<br>
	 * スタブレスポンスを返却する場合は、{@link #stubResponseStatusValue()} と {@link #stubResponseJsonValue()} を設定してください。
	 * </p>
	 * <h3>注意事項</h3>
	 * <p>
	 * プロパティに true を設定すると、常にスタブレスポンスを返却することになります。<br>
	 * スタブレスポンスは、WebAPIの実装を行わずに、WebAPIの仕様を確認するために利用されます。<br>
	 * そのため、本プロパティは通常は false に設定されていることを想定しています。<br>
	 * </p>
	 * @return スタブレスポンスを返却するか。
	 */
	boolean returnStubResponse() default false;

	/**
	 * スタブレスポンスの "status" の値。
	 * <p>
	 * スタブレスポンスを返却する場合の "status" キーの値を設定します。<br>
	 * 未指定の場合は、"SUCCESS" が設定されます。
	 * </p>
	 * @return スタブレスポンスの "status" の値
	 */
	String stubResponseStatusValue() default "";

	/**
	 * スタブレスポンスの JSON Value。
	 * <p>
	 * スタブレスポンスを返却する場合の JSON Value を設定します。<br>
	 * ここに設定する値は、JSON Object の形式で指定してください。<br>
	 * JSON Object のキーが、{@link #results()} に指定されているキーと一致する場合に、値がレスポンスに設定されます。
	 * </p>
	 * @return スタブレスポンスの JSON Value
	 */
	String stubResponseJsonValue() default "";

	/**
	 * OpenAPI バージョン
	 * <p>
	 * 設定可能な値は {@link org.iplass.mtp.webapi.openapi.OpenApiVersion} で定義されている seriesVersion の値です（3.0, 3.1 など）。
	 * </p>
	 * @return OpenAPI バージョン
	 */
	String openApiVersion() default "";

	/**
	 * OpenAPI ファイルタイプ
	 * <p>
	 * 設定可能な値は {@link org.iplass.mtp.webapi.openapi.OpenApiFileType} で定義されている列挙値の文字列です（JSON, YAML など）。
	 * </p>
	 * @return OpenAPI バージョン
	 */
	String openApiFileType() default "";

	/**
	 * OpenAPI 定義
	 * @return OpenAPI 定義
	 */
	String openApi() default "";
}
