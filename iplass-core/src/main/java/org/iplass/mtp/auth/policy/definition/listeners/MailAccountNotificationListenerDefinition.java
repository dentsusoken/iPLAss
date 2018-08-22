/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.auth.policy.definition.listeners;

import java.util.List;

import org.iplass.mtp.auth.policy.definition.AccountNotificationListenerDefinition;

/**
 * <p>
 * 対象のアカウントに対して、メール通知が行われるAccountNotificationListener定義。
 * </p>
 * <p>
 * 定義されてたメールテンプレートの内容が送信される。
 * メールテンプレートにバインドされる変数とそのオブジェクトは次のもの。
 * </p>
 * 
 * <table border="1">
 * <tr><th>変数名</th><th>オブジェクトの説明</th></tr>
 * <tr><td>tenant</td><td>テナント情報。org.iplass.mtp.tenant.Tenantのインスタンス</td></tr>
 * <tr><td>user</td><td>ユーザ情報。org.iplass.mtp.auth.Userのインスタンス</td></tr>
 * <tr><td>newPassword</td><td>自動生成されたパスワード。String。アカウント作成時、パスワードリセット時に参照可能</td></tr>
 * <tr><td>updatedPropertyNames</td><td>アカウント属性更新時の更新されたプロパティ名のList</td></tr>
 * 
 * </table>
 * 
 * @author K.Higuchi
 *
 */
public class MailAccountNotificationListenerDefinition extends AccountNotificationListenerDefinition {
	private static final long serialVersionUID = 7463255129416810210L;
	
	private String createUserMailTemplate;
	private String credentialResetMailTemplate;
	private String createUserWithSpecifiedPasswordMailTemplate;
	private String credentialResetWithSpecifiedPasswordMailTemplate;
	private String lockedoutMailTemplate;
	private String credentialUpdatedMailTemplate;
	private String propertyUpdatedMailTemplate;
	private String removeUserMailTemplate;
	private String loginSuccessUserMailTemplate;
	private List<String> propertiesForUpdateNotification;
	
	/**
	 * ログイン成功時のメールのテンプレート。
	 * テンプレートが指定された場合、ログイン成功したアカウントにメールが送信される。
	 * 
	 * @return
	 */
	public String getLoginSuccessUserMailTemplate() {
		return loginSuccessUserMailTemplate;
	}

	/**
	 * @see #getLoginSuccessUserMailTemplate()
	 * @param loginSuccessUserMailTemplate
	 */
	public void setLoginSuccessUserMailTemplate(String loginSuccessUserMailTemplate) {
		this.loginSuccessUserMailTemplate = loginSuccessUserMailTemplate;
	}
	
	/**
	 * パスワード更新時のメールのテンプレート。
	 * テンプレートが指定された場合、更新されたアカウントにメールが送信される。
	 * @return
	 */
	public String getCredentialUpdatedMailTemplate() {
		return credentialUpdatedMailTemplate;
	}
	
	/**
	 * @see #getCredentialUpdatedMailTemplate()
	 * @param credentialUpdatedMailTemplate
	 */
	public void setCredentialUpdatedMailTemplate(
			String credentialUpdatedMailTemplate) {
		this.credentialUpdatedMailTemplate = credentialUpdatedMailTemplate;
	}
	
	/**
	 * アカウントの属性が更新された際のメールのテンプレート。
	 * テンプレートが指定された場合、propertiesForUpdateNotificationが未設定の場合、
	 * もしくはpropertiesForUpdateNotificationに指定されるプロパティが更新された場合、当該アカウントに通知される。
	 * 
	 * @return
	 */
	public String getPropertyUpdatedMailTemplate() {
		return propertyUpdatedMailTemplate;
	}
	
	/**
	 * @see #getPropertyUpdatedMailTemplate()
	 * @param propertyUpdatedMailTemplate
	 */
	public void setPropertyUpdatedMailTemplate(String propertyUpdatedMailTemplate) {
		this.propertyUpdatedMailTemplate = propertyUpdatedMailTemplate;
	}
	
	/**
	 * アカウントの属性が更新された際、どの属性が更新された場合にメールを送信するかを指定する。
	 * この値が設定されている場合は、このプロパティ名リストに指定されているプロパティの値が更新された場合のみメールが送信される。
	 * 
	 * @return
	 */
	public List<String> getPropertiesForUpdateNotification() {
		return propertiesForUpdateNotification;
	}
	
	/**
	 * @see #getPropertiesForUpdateNotification()
	 * @param propertiesForUpdateNotification
	 */
	public void setPropertiesForUpdateNotification(
			List<String> propertiesForUpdateNotification) {
		this.propertiesForUpdateNotification = propertiesForUpdateNotification;
	}
	
	/**
	 * ユーザ削除時のメールのテンプレート。
	 * テンプレートが指定された場合、ユーザアカウントが削除された場合、当該アカウントにメールが送信される。
	 * @return
	 */
	public String getRemoveUserMailTemplate() {
		return removeUserMailTemplate;
	}
	
	/**
	 * @see #getRemoveUserMailTemplate()
	 * @param removedUpdatedMailTemplate
	 */
	public void setRemoveUserMailTemplate(String removeUserMailTemplate) {
		this.removeUserMailTemplate = removeUserMailTemplate;
	}
	
	/**
	 * アカウント作成時（初期パスワード自動生成）のメールのテンプレート。
	 * テンプレートが指定された場合、かつパスワードが自動生成された場合は、作成されたアカウントにメールが送信される。
	 * 
	 * @return
	 */
	public String getCreateUserMailTemplate() {
		return createUserMailTemplate;
	}
	
	/**
	 * @see #getCreateUserMailTemplate()
	 * @param createUserMailTemplate
	 */
	public void setCreateUserMailTemplate(String createUserMailTemplate) {
		this.createUserMailTemplate = createUserMailTemplate;
	}
	
	/**
	 * アカウント作成時（パスワード指定あり）のメールのテンプレート。
	 * テンプレートが指定された場合、かつパスワードが指定された場合は、作成されたアカウントにメールが送信される。
	 * 
	 * @return
	 */
	public String getCreateUserWithSpecifiedPasswordMailTemplate() {
		return createUserWithSpecifiedPasswordMailTemplate;
	}
	
	/**
	 * @see #getCreateUserWithSpecifiedPasswordMailTemplate()
	 * @param createUserWithSpecifiedPasswordMailTemplate
	 */
	public void setCreateUserWithSpecifiedPasswordMailTemplate(
			String createUserWithSpecifiedPasswordMailTemplate) {
		this.createUserWithSpecifiedPasswordMailTemplate = createUserWithSpecifiedPasswordMailTemplate;
	}
	/**
	 * パスワードリセット時（パスワード自動生成）のメールのテンプレート。
	 * テンプレートが指定された場合、かつパスワードが自動生成された場合、リセットされたアカウントにメールが送信される。
	 * @return
	 */
	public String getCredentialResetMailTemplate() {
		return credentialResetMailTemplate;
	}
	
	/**
	 * @see #getCredentialResetMailTemplate()
	 * @param credentialResetMailTemplate
	 */
	public void setCredentialResetMailTemplate(String credentialResetMailTemplate) {
		this.credentialResetMailTemplate = credentialResetMailTemplate;
	}
	
	/**
	 * パスワードリセット時（パスワード指定あり）のメールのテンプレート。
	 * テンプレートが指定された場合、かつパスワードが指定された場合、リセットされたアカウントにメールが送信される。
	 * 
	 * @return
	 */
	public String getCredentialResetWithSpecifiedPasswordMailTemplate() {
		return credentialResetWithSpecifiedPasswordMailTemplate;
	}
	
	/**
	 * @see #getCredentialResetWithSpecifiedPasswordMailTemplate()
	 * @param credentialResetWithSpecifiedPasswordMailTemplate
	 */
	public void setCredentialResetWithSpecifiedPasswordMailTemplate(
			String credentialResetWithSpecifiedPasswordMailTemplate) {
		this.credentialResetWithSpecifiedPasswordMailTemplate = credentialResetWithSpecifiedPasswordMailTemplate;
	}
	
	/**
	 * アカウントロックアウト時のメールのテンプレート。
	 * テンプレートが指定された場合、ロックアウトされたアカウントにメールが送信される。
	 * @return
	 */
	public String getLockedoutMailTemplate() {
		return lockedoutMailTemplate;
	}
	
	/**
	 * @see #getLockedoutMailTemplate()
	 * @param lockedoutMailTemplate
	 */
	public void setLockedoutMailTemplate(String lockedoutMailTemplate) {
		this.lockedoutMailTemplate = lockedoutMailTemplate;
	}

}
