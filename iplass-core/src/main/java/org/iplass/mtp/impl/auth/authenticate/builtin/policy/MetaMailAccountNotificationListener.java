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

package org.iplass.mtp.impl.auth.authenticate.builtin.policy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.auth.User;
import org.iplass.mtp.auth.policy.AccountNotification;
import org.iplass.mtp.auth.policy.AccountNotificationListener;
import org.iplass.mtp.auth.policy.LoginNotification;
import org.iplass.mtp.auth.policy.PasswordNotification;
import org.iplass.mtp.auth.policy.PropertyNotification;
import org.iplass.mtp.auth.policy.definition.AccountNotificationListenerDefinition;
import org.iplass.mtp.auth.policy.definition.listeners.MailAccountNotificationListenerDefinition;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.LoadOption;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.auth.UserContext;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.mail.Mail;
import org.iplass.mtp.mail.MailManager;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.transaction.TransactionListener;
import org.iplass.mtp.transaction.TransactionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetaMailAccountNotificationListener extends MetaAccountNotificationListener {
	private static final long serialVersionUID = -9056364804302316265L;

	private static final Logger fatalLog = LoggerFactory.getLogger("mtp.fatal.mail");
	private static Logger logger = LoggerFactory.getLogger(MetaMailAccountNotificationListener.class);
	
	public static final String TENANT_BINDING_NAME ="tenant";
	public static final String USER_BINDING_NAME ="user";
	public static final String NEW_PASSWORD_BINDING_NAME ="newPassword";
	public static final String UPDATED_PROPERTY_NAMES = "updatedPropertyNames";
	
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
	
	public String getLoginSuccessUserMailTemplate() {
		return loginSuccessUserMailTemplate;
	}
	public void setLoginSuccessUserMailTemplate(String loginSuccessUserMailTemplate) {
		this.loginSuccessUserMailTemplate = loginSuccessUserMailTemplate;
	}
	public String getCreateUserWithSpecifiedPasswordMailTemplate() {
		return createUserWithSpecifiedPasswordMailTemplate;
	}
	public void setCreateUserWithSpecifiedPasswordMailTemplate(
			String createUserWithSpecifiedPasswordMailTemplate) {
		this.createUserWithSpecifiedPasswordMailTemplate = createUserWithSpecifiedPasswordMailTemplate;
	}
	public String getCredentialResetWithSpecifiedPasswordMailTemplate() {
		return credentialResetWithSpecifiedPasswordMailTemplate;
	}
	public void setCredentialResetWithSpecifiedPasswordMailTemplate(
			String credentialResetWithSpecifiedPasswordMailTemplate) {
		this.credentialResetWithSpecifiedPasswordMailTemplate = credentialResetWithSpecifiedPasswordMailTemplate;
	}
	public String getCredentialUpdatedMailTemplate() {
		return credentialUpdatedMailTemplate;
	}
	public void setCredentialUpdatedMailTemplate(
			String credentialUpdatedMailTemplate) {
		this.credentialUpdatedMailTemplate = credentialUpdatedMailTemplate;
	}
	public String getPropertyUpdatedMailTemplate() {
		return propertyUpdatedMailTemplate;
	}
	public void setPropertyUpdatedMailTemplate(String propertyUpdatedMailTemplate) {
		this.propertyUpdatedMailTemplate = propertyUpdatedMailTemplate;
	}
	public String getRemoveUserMailTemplate() {
		return removeUserMailTemplate;
	}
	public void setRemoveUserMailTemplate(String removeUserMailTemplate) {
		this.removeUserMailTemplate = removeUserMailTemplate;
	}
	public List<String> getPropertiesForUpdateNotification() {
		return propertiesForUpdateNotification;
	}
	public void setPropertiesForUpdateNotification(
			List<String> propertiesForUpdateNotification) {
		this.propertiesForUpdateNotification = propertiesForUpdateNotification;
	}
	public String getCreateUserMailTemplate() {
		return createUserMailTemplate;
	}
	public void setCreateUserMailTemplate(String createUserMailTemplate) {
		this.createUserMailTemplate = createUserMailTemplate;
	}
	public String getCredentialResetMailTemplate() {
		return credentialResetMailTemplate;
	}
	public void setCredentialResetMailTemplate(String credentialResetMailTemplate) {
		this.credentialResetMailTemplate = credentialResetMailTemplate;
	}
	public String getLockedoutMailTemplate() {
		return lockedoutMailTemplate;
	}
	public void setLockedoutMailTemplate(String lockedoutMailTemplate) {
		this.lockedoutMailTemplate = lockedoutMailTemplate;
	}

	@Override
	public AccountNotificationListener createInstance(String policyName, int i) {
		return new Listener();
	}
	
	private class Listener implements AccountNotificationListener {

		private MailManager mm = ManagerLocator.getInstance().getManager(MailManager.class);
		private EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);

		@Override
		public void created(PasswordNotification notification) {
			if (notification.isPasswordAutoGenerated()) {
				if (createUserMailTemplate != null) {
					//自動生成された場合のみメール送信
					sendMail(notification.getUserOid(), notification.getPassword(), null, createUserMailTemplate);
				}
			} else {
				if (createUserWithSpecifiedPasswordMailTemplate != null) {
					//パスワード指定の場合のメール送信
					sendMail(notification.getUserOid(), null, null, createUserWithSpecifiedPasswordMailTemplate);
				}
			}
		}

		@Override
		public void credentialReset(PasswordNotification notification) {
			if (notification.isPasswordAutoGenerated()) {
				if (credentialResetMailTemplate != null) {
					//自動生成された場合のみメール送信
					sendMail(notification.getUserOid(), notification.getPassword(), null, credentialResetMailTemplate);
				}
			} else {
				if (credentialResetWithSpecifiedPasswordMailTemplate != null) {
					//パスワード指定の場合のメール送信
					sendMail(notification.getUserOid(), null, null, credentialResetWithSpecifiedPasswordMailTemplate);
				}
			}
		}

		@Override
		public void rockedout(AccountNotification notification) {
			if (lockedoutMailTemplate != null) {
				sendMail(notification.getUserOid(), null, null, lockedoutMailTemplate);
			}
		}
		
		private void sendMail(final String userOid, final String newPassword, final List<String> updatedPropertyNames, final String mailTemplateName) {
			final Tenant tenant = ExecuteContext.getCurrentContext().getCurrentTenant();
			final User user = AuthContext.doPrivileged(() -> {
						return (User) em.load(userOid, User.DEFINITION_NAME, new LoadOption(false, false));
					});

			Transaction t = Transaction.getCurrent();
			if (t.getStatus() == TransactionStatus.ACTIVE) {
				t.addTransactionListener(new TransactionListener() {
						@Override
						public void afterRollback(Transaction t) {
							if (user != null) {
								logger.debug("Don't send mail cause transaciton rolledback:user=" + user.getAccountId());
							}
						}

						@Override
						public void afterCommit(Transaction t) {
							sendMailInternal(tenant, user, newPassword, updatedPropertyNames, mailTemplateName);
						}
					});
			} else {
				sendMailInternal(tenant, user, newPassword, updatedPropertyNames, mailTemplateName);
			}
		}

		private void sendMailInternal(final Tenant tenant, final User user, final String newPassword, final List<String> updatedPropertyNames, final String mailTemplateName) {
			try {
				//バインド変数設定
				Map<String, Object> bindings = new HashMap<String, Object>();
				bindings.put(TENANT_BINDING_NAME, tenant);
				bindings.put(USER_BINDING_NAME, user);
				bindings.put(NEW_PASSWORD_BINDING_NAME, newPassword);
				bindings.put(UPDATED_PROPERTY_NAMES, updatedPropertyNames);
	
				Mail mail = mm.createMail(mailTemplateName, bindings);
				mail.addRecipientTo(user.getMail());
			
				mm.sendMail(mail);
			} catch (RuntimeException e) {
				fatalLog.error("cannot send mail:"
						+ "uid=" + user.getAccountId()
						+ ",to=" + user.getMail()
						+ ",mailTemplateName=" + mailTemplateName,
						e);
			}
		}

		@Override
		public void credentialUpdated(PasswordNotification notification) {
			if (credentialUpdatedMailTemplate != null) {
				sendMail(notification.getUserOid(), notification.getPassword(), null, credentialUpdatedMailTemplate);
			}
		}

		@Override
		public void propertyUpdated(PropertyNotification notification) {
			if (propertyUpdatedMailTemplate != null) {
				if (propertiesForUpdateNotification == null) {
					sendMail(notification.getUserOid(), null, notification.getUpdatedPropertyNames(), propertyUpdatedMailTemplate);
				} else {
					if (contains(propertiesForUpdateNotification, notification.getUpdatedPropertyNames())) {
						sendMail(notification.getUserOid(), null, notification.getUpdatedPropertyNames(), propertyUpdatedMailTemplate);
					}
				}
			}
		}

		private boolean contains(List<String> propertiesForUpdateNotification,
				List<String> updatedPropertyNames) {
			for (String p: propertiesForUpdateNotification) {
				for (String u: updatedPropertyNames) {
					if (p.equals(u)) {
						return true;
					}
				}
			}
			return false;
		}

		@Override
		public void remove(AccountNotification notification) {
			if (removeUserMailTemplate != null) {
				sendMail(notification.getUserOid(), null, null, removeUserMailTemplate);
			}
		}

		@Override
		public void loginSuccess(LoginNotification notification) {
			if (loginSuccessUserMailTemplate != null) {
				Tenant tenant = ExecuteContext.getCurrentContext().getCurrentTenant();
				UserContext uc = AuthContextHolder.getAuthContext().getUserContext();
				User user = uc.getUser();
				sendMailInternal(tenant, user, null, null, loginSuccessUserMailTemplate);
			}
		}

		@Override
		public void loginFailed(LoginNotification notification) {
		}

	}

	@Override
	public AccountNotificationListenerDefinition currentConfig() {
		MailAccountNotificationListenerDefinition def = new MailAccountNotificationListenerDefinition();
		def.setCreateUserMailTemplate(createUserMailTemplate);
		def.setCreateUserWithSpecifiedPasswordMailTemplate(createUserWithSpecifiedPasswordMailTemplate);
		def.setCredentialResetMailTemplate(credentialResetMailTemplate);
		def.setCredentialResetWithSpecifiedPasswordMailTemplate(credentialResetWithSpecifiedPasswordMailTemplate);
		def.setLockedoutMailTemplate(lockedoutMailTemplate);
		def.setCredentialUpdatedMailTemplate(credentialUpdatedMailTemplate);
		def.setPropertyUpdatedMailTemplate(propertyUpdatedMailTemplate);
		def.setRemoveUserMailTemplate(removeUserMailTemplate);
		def.setLoginSuccessUserMailTemplate(loginSuccessUserMailTemplate);
		if (propertiesForUpdateNotification != null) {
			def.setPropertiesForUpdateNotification(new ArrayList<>(propertiesForUpdateNotification));
		}
		return def;
	}
	
	@Override
	public void applyConfig(AccountNotificationListenerDefinition def) {
		MailAccountNotificationListenerDefinition mdef = (MailAccountNotificationListenerDefinition) def;
		createUserMailTemplate = mdef.getCreateUserMailTemplate();
		createUserWithSpecifiedPasswordMailTemplate = mdef.getCreateUserWithSpecifiedPasswordMailTemplate();
		credentialResetMailTemplate = mdef.getCredentialResetMailTemplate();
		credentialResetWithSpecifiedPasswordMailTemplate = mdef.getCredentialResetWithSpecifiedPasswordMailTemplate();
		lockedoutMailTemplate = mdef.getLockedoutMailTemplate();
		credentialUpdatedMailTemplate = mdef.getCredentialUpdatedMailTemplate();
		propertyUpdatedMailTemplate = mdef.getPropertyUpdatedMailTemplate();
		removeUserMailTemplate = mdef.getRemoveUserMailTemplate();
		loginSuccessUserMailTemplate = mdef.getLoginSuccessUserMailTemplate();
		if (mdef.getPropertiesForUpdateNotification() != null) {
			propertiesForUpdateNotification = new ArrayList<>(mdef.getPropertiesForUpdateNotification());
		} else {
			propertiesForUpdateNotification = null;
		}
	}

}
