/*
 * Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.auth.oauth;

public interface OAuthConstants {
	
	//error codes
	//リクエストに必須パラメーターが含まれていない, サポート外のパラメーターが付与されている, 同一のパラメーターが複数含まれる場合, その他不正な形式であった場合もこれに含まれる
	public static final String ERROR_INVALID_REQUEST = "invalid_request";
	//クライアント認証に失敗した
	public static final String ERROR_INVALID_CLIENT = "invalid_client";
	//提供された認可グラント (例えば認可コード, リソースオーナークレデンシャル)またはリフレッシュトークン が不正, 有効期限切れ, 失効している, 認可リクエストで用いられたリダイレクトURIとマッチしていない, 他のクライアントに対して発行されたものである
	public static final String ERROR_INVALID_GRANT = "invalid_grant";
	//クライアントは現在の方法で認可コードを取得することを認可されていない
	public static final String ERROR_UNAUTHORIZED_CLIENT = "unauthorized_client";
	//リソースオーナーまたは認可サーバーがリクエストを拒否した
	public static final String ERROR_ACCESS_DENIED = "access_denied";
	//認可サーバーは現在の方法による認可コード取得をサポートしていない
	public static final String ERROR_UNSUPPORTED_RESPONSE_TYPE = "unsupported_response_type";
	//グラントタイプが認可サーバーによってサポートされていない
	public static final String ERROR_UNSUPPORTED_GRANT_TYPE = "unsupported_grant_type";
	//リクエストスコープが不正, 未知, もしくはその他の不当な形式である
	public static final String ERROR_INVALID_SCOPE = "invalid_scope";
	//認可サーバーがリクエストの処理ができないような予期しない状況に遭遇した. (500 Internal Server Error のHTTPステータスコードをHTTPのリダイレクトでクライアントに返すことができないため, このエラーコードは必要である)
	public static final String ERROR_SERVER_ERROR = "server_error";
	//認可サーバーが一時的な過負荷やメンテナンスによってリクエストを扱うことが出来ない. (503 Service Unavailable のHTTPステータスコードをHTTPのリダイレクトでクライアントに返すことができないため, このエラーコードは必要である)
	public static final String ERROR_TEMPORARILY_UNAVAILABLE = "temporarily_unavailable";
	//Authorization Server は処理を進めるためにいくつかの End-User interaction を必要とする. Authentication Request 中の prompt パラメータが none であるが, End-User interaction のためのユーザーインターフェースの表示なしには Authentication Request が完了できない時にこのエラーが返される
	public static final String ERROR_INTERACTION_REQUIRED = "interaction_required";
	//Authorization Server は End-User の認証を必要とする. Authentication Request 中の prompt パラメータが none であるが, End-User の認証のためのユーザーインターフェースの表示なしには Authentication Request が完了できない時にこのエラーが返される
	public static final String ERROR_LOGIN_REQUIRED = "login_required";
	//Authorization Server は End-User の同意を必要とする. Authentication Request 中の prompt パラメータが none であるが, End-User の同意のためのユーザーインターフェースの表示なしには Authentication Request が完了できない時にこのエラーが返される
	public static final String ERROR_CONSENT_REQUIRED = "consent_required";
	
	
	//response_type
	public static final String RESPONSE_TYPE_CODE = "code";
	
	//response_mode
	public static final String RESPONSE_MODE_QUERY = "query";
	public static final String RESPONSE_MODE_FRAGMENT = "fragment";
	public static final String RESPONSE_MODE_FORM_POST = "form_post";
	//JARM(そのうちサポート)
	//query.jwt
	//form_post.jwt
	//jwt
	
	//code_challenge_method
	public static final String CODE_CHALLENGE_METHOD_S256 = "S256";
	public static final String CODE_CHALLENGE_METHOD_PLAIN = "plain";
	
	//grant_type
	public static final String GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code";
	public static final String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";
	 
	//reserved scope
	public static final String SCOPE_OPENID = "openid";
	public static final String SCOPE_PROFILE = "profile";
	public static final String SCOPE_EMAIL = "email";
	public static final String SCOPE_ADDRESS = "address";
	public static final String SCOPE_PHONE = "phone";
	public static final String SCOPE_OFFLINE_ACCESS = "offline_access";
	
	//oidc prompt
	public static final String PROMPT_NONE = "none";
	public static final String PROMPT_LOGIN = "login";
	public static final String PROMPT_CONSENT = "consent";
	
	//token type
	public static final String TOKEN_TYPE_BEARER = "Bearer";
	
}
