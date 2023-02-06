/*
 * Copyright (C) 2022 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.auth.oidc;

import java.util.Map;

import org.iplass.mtp.auth.User;
import org.iplass.mtp.auth.oidc.definition.OpenIdConnectDefinition;
import org.iplass.mtp.impl.auth.authenticate.oidc.OIDCAccountHandle;

/**
 * OpenID Provider（OpenID Connect）を利用したログイン処理の際、
 * iPLAss内に存在しないユーザーを自動的に作成する場合に実装するインタフェース。
 * 
 * @author K.Higuchi
 *
 */
public interface AutoUserProvisioningHandler {
	
	/**
	 * AutoUserProvisioningHandlerの処理化処理を記述可能。
	 * @param definition
	 */
	public default void init(OpenIdConnectDefinition definition) {
	}
	
	/**
	 * 認証されたユーザーが存在しない場合に呼び出されるので、
	 * Userエンティティを生成する処理を記述する。
	 * 
	 * @param subjectId OpenID Provider内で一意となるユーザー識別子
	 * @param subjectName OpenId Connect定義で指定されるsubjectNameClaimの値
	 * @param attributes その他IdToken、UserInfo Endpointから取得したClaim情報
	 * @return 生成したUserエンティティのoid
	 */
	public String createUser(String subjectId, String subjectName, Map<String, Object> attributes);
	
	/**
	 * 認証されたユーザーの再ログインのタイミングで、呼び出される。
	 * ユーザー属性を更新する必要がある場合は、更新処理を記述する。
	 * 
	 * @param user
	 * @param subjectId OpenID Provider内で一意となるユーザー識別子
	 * @param subjectName OpenId Connect定義で指定されるsubjectNameClaimの値
	 * @param attributes その他IdToken、UserInfo Endpointから取得したClaim情報
	 */
	public void updateUser(User user, String subjectId, String subjectName, Map<String, Object> attributes);
	
	/**
	 * attributesの値をUserエンティティにマッピングします。
	 * 
	 * デフォルトの実装では、次の値を設定します。
	 * <table border="1">
	 * <tr><th>Userエンティティのプロパティ</th><th>OIDCで連携されたユーザー属性</th></tr>
	 * <tr><td>accountId</td><td>[subjectId]@[OpenIdConnect定義名]※１形式で値を設定</td></tr>
	 * <tr><td>name</td><td>subjectNameの値。subjectNameの値がnullの場合は、accountIdと同じ値を設定</td></tr>
	 * <tr><td>mail</td><td>email。ただしemail_verifiedがtrueの場合</td></tr>
	 * <tr><td>firstName</td><td>given_name</td></tr>
	 * <tr><td>lastName</td><td>family_name</td></tr>
	 * </table>
	 * ※１：OpenIdConnect定義名がDEFAULTの場合は、[subjectId]のみが設定されます。また、OpenIdConnect定義名の階層区切りは"."で表現されます
	 * 
	 * @param subjectId OpenID Provider内で一意となるユーザー識別子
	 * @param subjectName OpenIdConnect定義で指定されるsubjectNameClaimの値
	 * @param attributes その他IdToken、UserInfo Endpointから取得したClaim情報
	 */
	public default void attributeMappingToUserInstance(User user, String subjectId, String subjectName, Map<String, Object> attributes) {
		String accountId = (String) attributes.get(OIDCAccountHandle.SUBJECT_ID_WITH_DEFINITION_NAME);
		user.setAccountId(accountId);
		String gname = (String) attributes.get(OIDCAccountHandle.GIVEN_NAME);
		String fname = (String) attributes.get(OIDCAccountHandle.FAMILY_NAME);
		if (gname != null) {
			user.setFirstName(gname);
		}
		if (fname != null) {
			user.setLastName(fname);
		} else {
			if (subjectName == null) {
				user.setLastName(accountId);
				user.setName(accountId);
			} else {
				user.setLastName(subjectName);
				user.setName(subjectName);
			}
		}
		
		Object bv = attributes.get(OIDCAccountHandle.EMAIL_VERIFIED);
		if (bv != null && bv instanceof Boolean && ((Boolean) bv).booleanValue()) {
			user.setMail((String) attributes.get(OIDCAccountHandle.EMAIL));
		}
	}

	/**
	 * Userエンティティを作成、永続化せずに一時的なログインを許可する場合（OpenIdConnectDefinitionのenableTransientUserがtrue）の
	 * 一時的なUserエンティティのインスタンスを作成して返却するよう実装します。
	 * 
	 * デフォルトの実装では、 attributeMappingToUserInstance()メソッドを呼び出し、
	 * oidには、accountIdと同様の値（[subjectId]@[OpenIdConnect定義名]）を設定したインスタンスを返却します。
	 * 
	 * @param subjectId OpenID Provider内で一意となるユーザー識別子
	 * @param subjectName OpenIdConnect定義で指定されるsubjectNameClaimの値
	 * @param attributes その他IdToken、UserInfo Endpointから取得したClaim情報
	 * @return 
	 */
	public default User transientUser(String subjectId, String subjectName, Map<String, Object> attributes) {
		User u = new User();
		attributeMappingToUserInstance(u, subjectId, subjectName, attributes);
		u.setOid(u.getAccountId());
		
		return u;
	}

}
