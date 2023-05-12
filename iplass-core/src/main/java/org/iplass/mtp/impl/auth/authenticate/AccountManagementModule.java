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

package org.iplass.mtp.impl.auth.authenticate;

import java.util.List;

import org.iplass.mtp.auth.User;
import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.auth.login.CredentialUpdateException;



/**
 * アカウント情報の更新を行うためのインタフェース。
 *
 * @author K.Higuchi
 *
 */
public interface AccountManagementModule {

	/** ユーザーアクティベーション 仮パスワード設定方式*/
	public static final String USER_ACTIVATION_PUBLISH = "publish";

	/** ユーザーアクティベーション パスワード指定方式(未指定の場合は仮パスワード方式)*/
	public static final String USER_ACTIVATION_SPECIFY_OR_PUBLISH = "specifyOrPublish";

	public boolean canCreate();
	public boolean canUpdate();
	public boolean canRemove();
	public boolean canRestore();
	public boolean canPurge();
	public boolean canUpdateCredential();
	public boolean canResetCredential();
	public boolean canResetLockoutStatus();
	
	public void create(User user);
	public void afterCreate(User user);
	public void update(User user, List<String> updateProperties);
	//既存のカスタム実装への影響考慮してdefaultとする
	public default void afterUpdate(User user, String policyName, List<String> updateProperties) {}
	public void remove(User user);
	public void restore(User user);
	public void purge(User user);
	public void updateCredential(Credential oldCredential, Credential newCredential) throws CredentialUpdateException;
	public void resetCredential(Credential credential) throws CredentialUpdateException;
	public void resetLockoutStatus(String accountId);

}
