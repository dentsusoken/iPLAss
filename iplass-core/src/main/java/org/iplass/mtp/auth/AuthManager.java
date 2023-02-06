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

import org.iplass.mtp.Manager;
import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.auth.login.CredentialExpiredException;
import org.iplass.mtp.auth.login.CredentialUpdateException;
import org.iplass.mtp.auth.login.LoginFailedException;

/**
 * セキュリティ管理クラス。
 *
 *
 * @author K.Higuchi
 *
 */
public interface AuthManager extends Manager {

	public enum GroupOidListType {
		/** 子グループも同時に取得 */
		WITH_CHILDREN,
		/** 親グループも同時に取得 */
		WITH_PARENTS,
		/** 引数に指定したグループのみ取得 */
		ONLY_SPECIFY
	}

	public AuthContext getContext();

	public <T> T doPrivileged(Supplier<T> action);

	/**
	 * パスワード更新が可能か否か。
	 *
	 * @return
	 */
	public boolean canUpdateCredential();
	/**
	 * 指定の認証ポリシーの定義にて、パスワード更新が可能か否か。
	 * 複数のパスワード更新可能な認証プロバイダが存在する場合、認証ポリシーの定義によって、
	 * そのポリシー下で有効な認証プロバイダのリストを指定することが可能。
	 *
	 * @param policyName
	 * @return
	 */
	public boolean canUpdateCredential(String policyName);

	/**
	 * パスワードを更新する。
	 *
	 * @param oldCredential
	 * @param newCredential
	 * @throws CredentialUpdateException
	 */
	public void updateCredential(Credential oldCredential, Credential newCredential) throws CredentialUpdateException;

	/**
	 * 指定の認証ポリシーの定義下においてパスワードを更新する。
	 * 複数のパスワード更新可能な認証プロバイダが存在する場合、認証ポリシーの定義によって、
	 * そのポリシー下で有効な認証プロバイダのリストを指定することが可能。
	 *
	 * @param oldCredential
	 * @param newCredential
	 * @param policyName
	 * @throws CredentialUpdateException
	 */
	public void updateCredential(Credential oldCredential, Credential newCredential, String policyName) throws CredentialUpdateException;

	/**
	 * パスワードリセットが可能か否か。
	 * @return
	 */
	public boolean canResetCredential();

	/**
	 * 指定の認証ポリシーの定義下において、パスワードリセットが可能か否か。
	 * 複数のパスワードリセット可能な認証プロバイダが存在する場合、認証ポリシーの定義によって、
	 * そのポリシー下で有効な認証プロバイダのリストを指定することが可能。
	 *
	 * @param policyName
	 * @return
	 */
	boolean canResetCredential(String policyName);

	/**
	 * パスワードをリセットする。
	 *
	 * @param credential
	 */
	public void resetCredential(Credential credential);

	/**
	 * 指定の認証ポリシーの定義かにおいてパスワードをリセットする。
	 * 複数のパスワードリセット可能な認証プロバイダが存在する場合、認証ポリシーの定義によって、
	 * そのポリシー下で有効な認証プロバイダのリストを指定することが可能。
	 *
	 * @param credential
	 * @param policyName
	 */
	public void resetCredential(Credential credential, String policyName);

	/**
	 * アカウントロックアウトの（apiによる）解除が可能か否か。
	 * @return
	 */
	public boolean canResetLockoutStatus();

	/**
	 * 指定の認証ポリシーの定義下において、
	 * アカウントロックアウトの（apiによる）解除が可能か否か。
	 * 複数のアカウントロックアウト解除可能な認証プロバイダが存在する場合、認証ポリシーの定義によって、
	 * そのポリシー下で有効な認証プロバイダのリストを指定することが可能。
	 *
	 * @param policyName
	 * @return
	 */
	boolean canResetLockoutStatus(String policyName);

	/**
	 * アカウントロックアウトの解除をする。
	 *
	 * @param accountId
	 */
	public void resetLockoutStatus(String accountId);

	/**
	 * 指定の認証ポリシーの定義下においてアカウントロックアウトの解除をする。
	 * 複数のアカウントロックアウト解除可能な認証プロバイダが存在する場合、認証ポリシーの定義によって、
	 * そのポリシー下で有効な認証プロバイダのリストを指定することが可能。
	 *
	 * @param accountId
	 * @param policyName
	 */
	public void resetLockoutStatus(String accountId, String policyName);
	
	/**
	 * groupCodeで指定したグループのoid（のリスト）を一括で取得する。
	 * typeによって、子グループ、親グループのoidのリストを一括で取得可能。
	 *
	 * <pre>
	 * query.from("mtp.auth.User");
	 * String[] groupOid = authManager.getGroupOids(WITH_CHILDREN, groupCode);
	 * query.where(new In("groups.oid", groupOid));
	 * em.searchEntity(query);
	 * </pre>
	 *
	 * といった形で、Userなどを検索する際の条件に指定可能。<br>
	 * 返却されるoidのリストは、実行するユーザーのセキュリティ権限によらず、すべてのoidが返却される。
	 *
	 * @param type
	 * @param groupCode
	 * @return
	 */
	public String[] getGroupOids(GroupOidListType type, String... groupCode);

	public void login(Credential credential) throws LoginFailedException, CredentialExpiredException;

	/**
	 * 現在のログインセッションを信頼されたものにするために再認証する（セッション内に格納されているユーザー情報以外のものは保持される）。
	 *
	 * @param credential
	 * @throws LoginFailedException
	 * @throws CredentialExpiredException
	 */
	public void reAuth(Credential credential) throws LoginFailedException, CredentialExpiredException;

	public void logout();

}
