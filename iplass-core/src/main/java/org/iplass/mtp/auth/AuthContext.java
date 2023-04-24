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

package org.iplass.mtp.auth;

import java.util.function.Supplier;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.auth.token.AuthTokenInfoList;
import org.iplass.mtp.tenant.Tenant;

/**
 * 認証・認可に関する情報を管理するクラスです。
 * 現在の実行スレッドに紐づけられるセキュリティ情報のスナップショットをあらわします。
 *
 * @author K.Higuchi
 *
 */
public abstract class AuthContext {

	private static AuthManager authManager = ManagerLocator.getInstance().getManager(AuthManager.class);

	/**
	 * 現時点のセキュリティ情報（AuthContext）を取得します。
	 * AuthManagerのgetContext()メソッドを呼び出します。
	 *
	 * @return
	 */
	public static AuthContext getCurrentContext() {
		return authManager.getContext();
	}

	/**
	 * 現在実行中のテナントを取得します。
	 *
	 * @return
	 */
	public abstract Tenant getTenant();//TODO 公開する情報を絞るべきか？？

	/**
	 * 現在実行中のユーザーを取得します。
	 * 代理ログインしている場合は、成り代わり先のユーザーを取得します。
	 *
	 * @return
	 */
	public abstract User getUser();
	
	/**
	 * 現在実行中のユーザーに紐付く認証トークンのリストを取得します。
	 * @return
	 */
	public abstract AuthTokenInfoList getAuthTokenInfos();

	/**
	 * 現在実行中のユーザーの認証ポリシー名を取得します。
	 * 代理ログインしている場合は、成り代わり先のユーザーの認証ポリシー名を取得します。
	 *
	 * @return ポリシー名
	 */
	public abstract String getPolicyName();

	/**
	 * <p>
	 * 現在のセキュリティコンテキストに紐づいている属性を取得します。
	 * 認証されたユーザーのUserエンティティのプロパティ、
	 * また、認証プロバイダが返却したユーザー属性を取得可能です。
	 * </p>
	 * <p>
	 * 利用する認証プロバイダによらず次の属性を取得可能です。
	 * </p>
	 * <table border=1>
	 * <tr><th>属性名</th><th>説明</th></tr>
	 * <tr><td>providerName</td><td>このアカウントを認証した認証プロバイダ名</td></tr>
	 * <tr><td>authenticationProcessType</td><td>このアカウントを認証した際の{@link org.iplass.mtp.auth.login.AuthenticationProcessType AuthenticationProcessType}
	 * </table>
	 * 
	 * @param name
	 * @return
	 */
	public abstract Object getAttribute(String name);
	
	/**
	 * 現在のセッションが認証された時間を取得します。
	 * 未ログインユーザーの場合は、返却される値は不定です。
	 * 
	 * @return
	 */
	public abstract long getAuthTime();

	/**
	 * 現在のセッションが認証済みユーザーによるものか否かを取得します。
	 *
	 * @return
	 */
	public abstract boolean isAuthenticated();

	/**
	 * 現在のログインセッションが信頼された認証に基づいたものか否かを取得します。
	 *
	 * @return 信頼されている場合はtrue
	 */
	public abstract boolean isCurrentSessionTrusted();

	/**
	 * 現在のログインセッションを信頼された認証にするために必要なCredentialの型を取得します。
	 *
	 * @return 信頼された認証に必要なCredentialのクラス
	 */
	public abstract Class<? extends Credential> getCredentialTypeForTrust();

	/**
	 * 現在実行中のユーザーが当該ロールかどうかを判定します。
	 *
	 * @param role ロール名
	 * @return
	 */
	public abstract boolean userInRole(String role);

	/**
	 * 現在実行中のユーザーが引数のpermission（権限）を保持しているかどうかを判定します。
	 *
	 * @param permission 判定したいPermissionのインスタンス
	 * @return 許可される場合true
	 */
	public abstract boolean checkPermission(Permission permission);

	/**
	 *
	 * 現在のAuthContext（User情報、セキュリティ権限）を再読み込みします。
	 * ユーザー情報のセキュリティに関連する属性等を変更した場合、
	 * 現在ログインしているユーザーの権限状態を最新化したい場合に呼び出します。
	 *
	 */
	public abstract void refresh();
	
	
	/**
	 * 現在のAuthContextが特権実行されている状態かを取得します。
	 * 
	 * @return 特権実行中の場合true
	 */
	public abstract boolean isPrivileged();
	
	/**
	 * 指定のfunctionを特権実行（セキュリティ制約がない状態で実行）します。<br>
	 * 
	 * <h5>実装例</h5>
	 * <pre>
	 * //EntityManager経由の検索を権限によらず特権実行する。
	 * EntityManager em = ...
	 * 
	 * SearchResult&lt;Entity&gt; res = 
	 *     AuthContext.doPrivileged(() -> em.searchEntity(new Query("select oid from HogeEntity")));
	 * </pre>
	 * 
	 * @param function
	 * @return
	 */
	public static <T> T doPrivileged(Supplier<T> function) {
		return authManager.doPrivileged(function);
	}

	/**
	 * 指定のfunctionを特権実行（セキュリティ制約がない状態で実行）します。
	 * 戻り値がvoidの場合、利用することが可能です。<br>
	 * 
	 * <h5>実装例</h5>
	 * <pre>
	 * //EntityManager経由の更新を権限によらず特権実行する。
	 * EntityManager em = ...
	 * Entity entity = ...
	 * 
	 * AuthContext.doPrivileged(() -> {
	 *     UpdateOption op = new UpdateOption(true);
	 *     op.setUpdateProperties("name", "propA");
	 *     em.update(entity, op);
	 * });
	 * </pre>
	 * 
	 * 
	 * @param function
	 */
	public static void doPrivileged(Runnable function) {
		authManager.doPrivileged(() -> {
			function.run();
			return null;
		});
	}

}
