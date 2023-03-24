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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.User;
import org.iplass.mtp.auth.login.CredentialUpdateException;
import org.iplass.mtp.auth.login.IdPasswordCredential;
import org.iplass.mtp.auth.policy.AccountNotification;
import org.iplass.mtp.auth.policy.AccountNotificationListener;
import org.iplass.mtp.auth.policy.PasswordNotification;
import org.iplass.mtp.auth.policy.PropertyNotification;
import org.iplass.mtp.auth.policy.definition.AccountNotificationListenerDefinition;
import org.iplass.mtp.auth.policy.definition.AuthenticationPolicyDefinition;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.impl.auth.AccountManagementModuleWrapper;
import org.iplass.mtp.impl.auth.AuthService;
import org.iplass.mtp.impl.auth.LoggingAccountManagementModule;
import org.iplass.mtp.impl.auth.authenticate.AccountManagementModule;
import org.iplass.mtp.impl.auth.authenticate.AuthenticationProvider;
import org.iplass.mtp.impl.auth.authenticate.builtin.BuiltinAccount;
import org.iplass.mtp.impl.auth.authenticate.builtin.BuiltinAccountHandle;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.definition.DefinableMetaData;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.metadata.BaseMetaDataRuntime;
import org.iplass.mtp.impl.metadata.BaseRootMetaData;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.script.GroovyScript;
import org.iplass.mtp.impl.script.ScriptContext;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.impl.util.CoreResourceBundleUtil;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.util.random.SecureRandomGenerator;
import org.iplass.mtp.impl.util.random.SecureRandomService;
import org.iplass.mtp.spi.ServiceRegistry;

/**
 * 認証ポリシー定義。
 *
 * @author K.Higuchi
 *
 */
public class MetaAuthenticationPolicy extends BaseRootMetaData implements DefinableMetaData<AuthenticationPolicyDefinition> {
	private static final long serialVersionUID = 7953683827749947294L;

	private static class RandomHolder {
		static final SecureRandomGenerator random = ServiceRegistry.getRegistry().getService(SecureRandomService.class).createGenerator();
	}

	private MetaAccountLockoutPolicy accountLockoutPolicy;
	private MetaPasswordPolicy passwordPolicy;
	private boolean recordLastLoginDate = true;
	private MetaRememberMePolicy rememberMePolicy;
	private List<String> authenticationProvider;
	private List<String> openIdConnectDefinition;

	/** ユーザー作成時、パスワード更新時にその情報を受け取る為のListener */
	private List<MetaAccountNotificationListener> notificationListener;

	public List<String> getAuthenticationProvider() {
		return authenticationProvider;
	}

	public void setAuthenticationProvider(List<String> authenticationProvider) {
		this.authenticationProvider = authenticationProvider;
	}

	public MetaRememberMePolicy getRememberMePolicy() {
		return rememberMePolicy;
	}

	public void setRememberMePolicy(MetaRememberMePolicy rememberMePolicy) {
		this.rememberMePolicy = rememberMePolicy;
	}

	public MetaAccountLockoutPolicy getAccountLockoutPolicy() {
		return accountLockoutPolicy;
	}

	public void setAccountLockoutPolicy(
			MetaAccountLockoutPolicy accountLockoutPolicy) {
		this.accountLockoutPolicy = accountLockoutPolicy;
	}

	public MetaPasswordPolicy getPasswordPolicy() {
		return passwordPolicy;
	}

	public void setPasswordPolicy(MetaPasswordPolicy passwordPolicy) {
		this.passwordPolicy = passwordPolicy;
	}

	public boolean isRecordLastLoginDate() {
		return recordLastLoginDate;
	}

	public void setRecordLastLoginDate(boolean recordLastLoginDate) {
		this.recordLastLoginDate = recordLastLoginDate;
	}

	public List<MetaAccountNotificationListener> getNotificationListener() {
		return notificationListener;
	}

	public void setNotificationListener(
			List<MetaAccountNotificationListener> notificationListener) {
		this.notificationListener = notificationListener;
	}

	public List<String> getOpenIdConnectDefinition() {
		return openIdConnectDefinition;
	}

	public void setOpenIdConnectDefinition(List<String> openIdConnectDefinition) {
		this.openIdConnectDefinition = openIdConnectDefinition;
	}

	@Override
	public AuthenticationPolicyRuntime createRuntime(MetaDataConfig metaDataConfig) {
		return new AuthenticationPolicyRuntime();
	}

	@Override
	public MetaAuthenticationPolicy copy() {
		return ObjectUtil.deepCopy(this);
	}

	public void applyConfig(AuthenticationPolicyDefinition def) {
		name = def.getName();
		description = def.getDescription();
		displayName = def.getDisplayName();
		localizedDisplayNameList = I18nUtil.toMeta(def.getLocalizedDisplayNameList());

		if (def.getAccountLockoutPolicy() != null) {
			accountLockoutPolicy = new MetaAccountLockoutPolicy();
			accountLockoutPolicy.applyConfig(def.getAccountLockoutPolicy());
		} else {
			accountLockoutPolicy = null;
		}
		if (def.getPasswordPolicy() != null) {
			passwordPolicy = new MetaPasswordPolicy();
			passwordPolicy.applyConfig(def.getPasswordPolicy());
		} else {
			passwordPolicy = null;
		}
		recordLastLoginDate = def.isRecordLastLoginDate();
		if (def.getRememberMePolicy() != null) {
			rememberMePolicy = new MetaRememberMePolicy();
			rememberMePolicy.applyConfig(def.getRememberMePolicy());
		} else {
			rememberMePolicy = null;
		}

		if (def.getNotificationListener() != null) {
			notificationListener = new ArrayList<>();
			for (AccountNotificationListenerDefinition anld: def.getNotificationListener()) {
				MetaAccountNotificationListener manl = MetaAccountNotificationListener.newMeta(anld);
				manl.applyConfig(anld);
				notificationListener.add(manl);
			}
		} else {
			notificationListener = null;;
		}

		if (def.getAuthenticationProvider() != null) {
			authenticationProvider = new ArrayList<>(def.getAuthenticationProvider());
		} else {
			authenticationProvider = null;
		}

		if (def.getOpenIdConnectDefinition() != null) {
			openIdConnectDefinition = new ArrayList<String>(def.getOpenIdConnectDefinition());
		} else {
			openIdConnectDefinition = null;
		}
	}

	public AuthenticationPolicyDefinition currentConfig() {
		AuthenticationPolicyDefinition def = new AuthenticationPolicyDefinition();
		def.setName(name);
		def.setDescription(description);
		def.setDisplayName(displayName);
		def.setLocalizedDisplayNameList(I18nUtil.toDef(localizedDisplayNameList));

		if (accountLockoutPolicy != null) {
			def.setAccountLockoutPolicy(accountLockoutPolicy.currentConfig());
		}

		if (passwordPolicy != null) {
			def.setPasswordPolicy(passwordPolicy.currentConfig());
		}
		def.setRecordLastLoginDate(recordLastLoginDate);
		if (rememberMePolicy != null) {
			def.setRememberMePolicy(rememberMePolicy.currentConfig());
		}

		if (notificationListener != null) {
			ArrayList<AccountNotificationListenerDefinition> nlist = new ArrayList<>();
			for (MetaAccountNotificationListener manl: notificationListener) {
				nlist.add(manl.currentConfig());
			}
			def.setNotificationListener(nlist);
		}

		if (authenticationProvider != null) {
			def.setAuthenticationProvider(new ArrayList<>(authenticationProvider));
		}

		if (openIdConnectDefinition != null) {
			def.setOpenIdConnectDefinition(new ArrayList<>(openIdConnectDefinition));
		}

		return def;
	}

	public class AuthenticationPolicyRuntime extends BaseMetaDataRuntime {

		private List<AccountNotificationListener> listeners;
		private Pattern passwordPattern;
		private Set<String> denyList;
		private char[] randomPasswordIncludeSigns;
		private char[] randomPasswordExcludeChars;
		private GroovyScript customUserEndDateScript;
		private AccountManagementModule amm;

		private ScriptEngine scriptEngine = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();

		public AuthenticationPolicyRuntime() {
			try {
				listeners = new ArrayList<>();
				if (notificationListener != null) {
					AuthenticationPolicyDefinition def = currentConfig();
					for (int i = 0; i < notificationListener.size(); i++) {
						AccountNotificationListener anl = notificationListener.get(i).createInstance(name, i);
						anl.init(def);
						listeners.add(anl);
					}
				}
				if (passwordPolicy != null) {
					if (passwordPolicy.getPasswordPattern() != null
							&& passwordPolicy.getPasswordPattern().length() > 0) {
						passwordPattern = Pattern.compile(passwordPolicy.getPasswordPattern());
					}
					if (passwordPolicy.getDenyList() != null) {
						denyList = new HashSet<>(Arrays.asList(passwordPolicy.getDenyList().split("\r\n|\n|\r")));
					}
					if (passwordPolicy.getRandomPasswordIncludeSigns() != null && passwordPolicy.getRandomPasswordIncludeSigns().length() > 0) {
						randomPasswordIncludeSigns = passwordPolicy.getRandomPasswordIncludeSigns().toCharArray();
					}
					if (passwordPolicy.getRandomPasswordExcludeChars() != null && passwordPolicy.getRandomPasswordExcludeChars().length() > 0) {
						randomPasswordExcludeChars = passwordPolicy.getRandomPasswordExcludeChars().toCharArray();
					}
					if (StringUtils.isNotEmpty(passwordPolicy.getCustomUserEndDate())) {
						try {
							String scriptName = "MetaPasswordPolicy_customUserEndDate_" + getName();
							this.customUserEndDateScript = (GroovyScript) scriptEngine.createScript(
									passwordPolicy.getCustomUserEndDate(), scriptName);
						} catch (Exception e) {
							setIllegalStateException(new RuntimeException(e));
						}
					}
				}

				if (authenticationProvider != null && authenticationProvider.size() > 0) {
					AccountManagementModuleWrapper ammr = new AccountManagementModuleWrapper();
					AuthenticationProvider[] aps = ServiceRegistry.getRegistry().getService(AuthService.class).getAuthenticationProviders();
					for (AuthenticationProvider ap: aps) {
						if (authenticationProvider.contains(ap.getProviderName())) {
							ammr.add(ap.getAccountManagementModule());
						}
					}
					amm = new LoggingAccountManagementModule(ammr.stripOrThis());
				}

			} catch (RuntimeException e) {
				setIllegalStateException(e);
			}
		}

		public AccountManagementModule getAccountManagementModule() {
			return amm;
		}

		@Override
		public MetaAuthenticationPolicy getMetaData() {
			return MetaAuthenticationPolicy.this;
		}

		public List<AccountNotificationListener> getListeners() {
			return listeners;
		}

		/**
		 * TenantManagerで初期adminUser作成の際のパスワード通知のため必要
		 *
		 * @param listener
		 */
		public void addAccountNotificationListener(AccountNotificationListener listener) {
			if (listeners == null) {
				listeners = new ArrayList<>();
			}
			listeners.add(listener);
		}

		public boolean removeAccountNotificationListener(AccountNotificationListener listener) {
			if (listeners != null) {
				return listeners.remove(listener);
			}
			return false;
		}

		public void notify(AccountNotification notification) {
			checkState();
			if (listeners != null) {
				for (int i = listeners.size() - 1; i >= 0; i--) {
					AccountNotificationListener listener = listeners.get(i);
					switch (notification.getType()) {
					case CREATED:
						listener.created((PasswordNotification) notification);
						break;
					case CREDENTIAL_RESET:
						listener.credentialReset((PasswordNotification) notification);
						break;
					case ROCKEDOUT:
						listener.rockedout(notification);
						break;
					case CREDENTIAL_UPDATED:
						listener.credentialUpdated((PasswordNotification) notification);
						break;
					case PROPERTY_UPDATED:
						listener.propertyUpdated((PropertyNotification) notification);
						break;
					case REMOVE:
						listener.remove(notification);
						break;
					default:
						break;
					}
				}
			}
		}

		public boolean isJustLockedout(BuiltinAccount account) {
			if (isCheckLockout()) {
				if (account.getLoginErrorCnt() == accountLockoutPolicy.getLockoutFailureCount()) {
					return true;
				}
			}
			return false;
		}

		public boolean isCheckLockout() {
			return accountLockoutPolicy.getLockoutFailureCount() > 0;
		}

		/**
		 *
		 * @param account
		 * @return BuiltinAccountに保持している値を変更したか否か。変更ある場合は、BuiltinAccountの更新を行うこと。
		 */
		public boolean initLoginStatus(BuiltinAccount account) {
			//ロックアウトチェック
			if (isCheckLockout()) {
				//ロックアウト記憶有効期間確認
				if (accountLockoutPolicy.getLockoutFailureExpirationInterval() > 0) {
					Timestamp loginErrorDate = account.getLoginErrorDate();
					if (account.getLoginErrorCnt() > 0 && loginErrorDate != null) {//以前のデータではエラー回数が0以上で、エラー日時が入っていないものがある
						long currentTime = System.currentTimeMillis();//ExecuteContext#currentTimestampは利用しない（プレビュー機能によって日付変更出来ないように）
						if (loginErrorDate.getTime() + TimeUnit.MINUTES.toMillis(accountLockoutPolicy.getLockoutFailureExpirationInterval()) < currentTime) {
							//ログインエラーカウントをリセット
							account.resetLoginErrorCount();
							return true;
						}
					}
				}
			}
			return false;
		}

		/**
		 *
		 * @param accountHandle
		 * @param account
		 * @return BuiltinAccountに保持している値を変更したか否か。変更ある場合は、BuiltinAccountの更新を行うこと。
		 */
		public boolean checkLoginPolicy(BuiltinAccountHandle accountHandle, BuiltinAccount account) {
			boolean isUpdateAccount = false;
			long currentTime = System.currentTimeMillis();//ExecuteContext#currentTimestampは利用しない（プレビュー機能によって日付変更出来ないように）

			//ロックアウトチェック
			if (isCheckLockout()) {

				//ロックアウト確認
				if (account.getLoginErrorCnt() >= accountLockoutPolicy.getLockoutFailureCount()) {
					//ロックアウト期間確認
					Timestamp loginErrorDate = account.getLoginErrorDate();
					if (accountLockoutPolicy.getLockoutDuration() == 0
							|| loginErrorDate == null//以前のデータではエラー回数が0以上で、エラー日時が入っていないものがある
							|| loginErrorDate.getTime() + TimeUnit.MINUTES.toMillis(accountLockoutPolicy.getLockoutDuration()) >= currentTime) {
						accountHandle.setAccountLocked(true);
					} else {
						//ロックアウト期間経過後
						account.resetLoginErrorCount();
						isUpdateAccount = true;
					}
				} else {
					//ロックアウトしてなく、かつログイン成功なので、ロックアウト回数が1以上だったら、クリアする
					if (account.getLoginErrorCnt() > 0) {
						account.resetLoginErrorCount();
						isUpdateAccount = true;
					}
				}
			}

			//初期ログイン or パスワードリセット直後の場合
			if (account.getLastPasswordChange() == null) {
				accountHandle.setPasswordExpired(true);
				accountHandle.setInitialLogin(true);
			} else {
				//パスワード有効期間チェック
				if (isOverMaximumPasswordAge(account, currentTime)) {
					accountHandle.setPasswordExpired(true);
				}
			}

			//ログイン日時記録するか否か確認
			if (isRecordLastLoginDate()) {
				if (!accountHandle.isAccountLocked() && !accountHandle.isExpired()) {
					account.setLastLoginOn(new Timestamp(currentTime));
					isUpdateAccount = true;
				}
			}

			return isUpdateAccount;

		}

		public void checkPasswordUpdatePolicy(IdPasswordCredential newIdPass, BuiltinAccount account) {
			//固有のポリシー
			checkPasswordPattern(newIdPass.getPassword(), account.getAccountId());

			if (isUnderMinimumPasswordAge(account, System.currentTimeMillis())) {
				throw new CredentialUpdateException(resourceString("impl.auth.authenticate.updateCredential.minTermErr"));
			}
		}

		/**
		 * パスワードの最大有効期間をチェックする。
		 *
		 * @param tenant
		 *            テナント情報
		 * @param account
		 *            チェックするアカウント
		 * @return true:有効期間切れ/false:有効期間内
		 */
		private boolean isOverMaximumPasswordAge(BuiltinAccount account, long now) {
			//パスワード期間チェックをする設定になっている場合（passwordPolicy.getMaximumPasswordAge() > 0）、チェック
			if (passwordPolicy.getMaximumPasswordAge() > 0) {
				if (account.getLastPasswordChange() == null) {
					return true;
				}
				long lastPasswordChange = account.getLastPasswordChange().getTime();
				if (now >= lastPasswordChange + TimeUnit.DAYS.toMillis(passwordPolicy.getMaximumPasswordAge())) {
					return true;
				}
			}
			return false;
		}

		/**
		 * パスワード変更最小期間のチェック
		 *
		 * @param tenant
		 *            テナント情報
		 * @param id
		 *            ユーザーID
		 * @param account
		 *            アカウント情報
		 * @return true:最小期間内/false:最小期間外
		 */
		private boolean isUnderMinimumPasswordAge(BuiltinAccount account, long now) {
			if (passwordPolicy.getMinimumPasswordAge() > 0) {
				if (account.getLastPasswordChange() == null) {
					return false;
				}
				long lastPasswordChange = account.getLastPasswordChange().getTime();
				if (now <= lastPasswordChange + TimeUnit.DAYS.toMillis(passwordPolicy.getMinimumPasswordAge())) {
					// 最小変更期間内なので変更できない
					return true;
				}
			}
			return false;
		}

		public void checkPasswordPattern(String password, String accountId) {

			if (passwordPattern != null && !passwordPattern.matcher(password).matches()) {
				String passwordPatternErrorMessage = I18nUtil.stringMeta(passwordPolicy.getPasswordPatternErrorMessage(), passwordPolicy.getLocalizedPasswordPatternErrorMessageList());
				throw new CredentialUpdateException(passwordPatternErrorMessage);
			}

			if (passwordPolicy.isDenySamePasswordAsAccountId() && accountId.equals(password)) {
				String passwordPatternErrorMessage = I18nUtil.stringMeta(passwordPolicy.getPasswordPatternErrorMessage(), passwordPolicy.getLocalizedPasswordPatternErrorMessageList());
				throw new CredentialUpdateException(passwordPatternErrorMessage);
			}

			if (denyList != null) {
				if(denyList.contains(password)) {
					String passwordPatternErrorMessage = I18nUtil.stringMeta(passwordPolicy.getPasswordPatternErrorMessage(), passwordPolicy.getLocalizedPasswordPatternErrorMessageList());
					throw new CredentialUpdateException(passwordPatternErrorMessage);
				}

				List<String> selectUserList = new ArrayList<>();

				// DenyListに${プロパティ名}で設定されているプロパティを取得する
				denyList.forEach(deny -> {
					// Userエンティティのプロパティを取得
					if (deny.startsWith("${user.") && deny.endsWith("}")) {
						selectUserList.add(deny.substring(7, deny.length() - 1));
					}
				});

				if (!selectUserList.isEmpty()) {
					EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);
					Query q = new Query().select(selectUserList.toArray()).from(User.DEFINITION_NAME)
							.where(new Equals(User.ACCOUNT_ID, accountId));
					Object[] result = em.search(q).getFirst();

					for (Object property : result) {
						// 多重度複数の場合
						if (property != null && property.getClass().isArray()) {
							Object[] values = (Object[]) property;
							for (Object value : values) {
								if (password.equals(value)) {
									String passwordPatternErrorMessage = I18nUtil.stringMeta(
											passwordPolicy.getPasswordPatternErrorMessage(),
											passwordPolicy.getLocalizedPasswordPatternErrorMessageList());
									throw new CredentialUpdateException(passwordPatternErrorMessage);
								}
							}
						} else {
							if (password.equals(property)) {
								String passwordPatternErrorMessage = I18nUtil.stringMeta(
										passwordPolicy.getPasswordPatternErrorMessage(),
										passwordPolicy.getLocalizedPasswordPatternErrorMessageList());
								throw new CredentialUpdateException(passwordPatternErrorMessage);
							}
						}
					}
				}
			}
		}

		public String makePassword() {
			return makeRandomString(passwordPolicy.getRandomPasswordLength(), randomPasswordIncludeSigns != null, randomPasswordIncludeSigns, randomPasswordExcludeChars);
		}

		public boolean isResetPasswordWithSpecificPassword() {
			return passwordPolicy.isResetPasswordWithSpecificPassword();
		}

		private String makeRandomString(int length, boolean isSign, char[] sign, char[] excludedChar) {

			char[] c = new char[length];
			int num = isSign ? 4 : 3;
			for (int i = 0; i < c.length; i++) {
				char buf = ' ';
				switch (RandomHolder.random.randomInt(num)) {
				case 0: // a-z
					buf = createBuf(97, 26, null, false, excludedChar, RandomHolder.random);
					break;
				case 1: // A-Z
					buf = createBuf(65, 26, null, false, excludedChar, RandomHolder.random);
					break;
				case 2: // 0-9
					buf = createBuf(48, 10, null, false, excludedChar, RandomHolder.random);
					break;
				case 3: // 記号
					buf = createBuf(-1, -1, sign, true, excludedChar, RandomHolder.random);
					break;

				}
				c[i] = buf;
			}

			return new String(c);
		}

		private char createBuf(int start, int length, char[] sign, boolean signBuf, char[] excludedChar, SecureRandomGenerator rand) {
			while(true) {
				char buf;

				if (signBuf) {
					int index = rand.randomInt(sign.length - 1);
					buf = sign[index];
				} else {
					buf = (char) (start + rand.randomInt(length));
				}

				boolean exclude = false;
				if (excludedChar != null) {
					for (char c : excludedChar) {
						if (c == buf) {
							exclude = true;
							break;
						}
					}

					if (!exclude) {
						return buf;
					}
				} else {
					return buf;
				}
			}
		}

		public Timestamp getCustomUserEndDate(User user) {
			if(customUserEndDateScript == null) {
				return null;
			}
			TenantContext tc = ExecuteContext.getCurrentContext().getTenantContext();
			ScriptEngine ss = tc.getScriptEngine();

			ScriptContext sc = ss.newScriptContext();
			sc.setAttribute("user", user);
			return (Timestamp) customUserEndDateScript.eval(sc);
		}
	}

	private static String resourceString(String key, Object... arguments) {
		return CoreResourceBundleUtil.resourceString(key, arguments);
	}
}
