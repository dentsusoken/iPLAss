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

package org.iplass.mtp.impl.auth.authenticate.builtin;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.SystemException;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.auth.NoPermissionException;
import org.iplass.mtp.auth.User;
import org.iplass.mtp.auth.UserExistsException;
import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.auth.login.CredentialUpdateException;
import org.iplass.mtp.auth.login.IdPasswordCredential;
import org.iplass.mtp.auth.policy.AccountNotification;
import org.iplass.mtp.auth.policy.PasswordNotification;
import org.iplass.mtp.auth.policy.PropertyNotification;
import org.iplass.mtp.auth.policy.definition.NotificationType;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.SearchResult;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.entity.query.value.aggregate.Count;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.auth.AuthService;
import org.iplass.mtp.impl.auth.authenticate.AccountHandle;
import org.iplass.mtp.impl.auth.authenticate.AccountManagementModule;
import org.iplass.mtp.impl.auth.authenticate.AuthenticationProviderBase;
import org.iplass.mtp.impl.auth.authenticate.builtin.policy.AuthenticationPolicyService;
import org.iplass.mtp.impl.auth.authenticate.builtin.policy.MetaAuthenticationPolicy.AuthenticationPolicyRuntime;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.definition.DefinitionService;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.impl.util.CoreResourceBundleUtil;
import org.iplass.mtp.impl.util.random.SecureRandomGenerator;
import org.iplass.mtp.impl.util.random.SecureRandomService;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceConfigrationException;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.tenant.TenantAuthInfo;
import org.iplass.mtp.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * デフォルトの認証プロバイダー<br>
 * iPLAssが持つパスワード情報を利用して認証する
 *
 * @author K.Higuchi
 *
 */
public class BuiltinAuthenticationProvider extends AuthenticationProviderBase {
	private static Logger logger = LoggerFactory.getLogger(BuiltinAuthenticationProvider.class);

	private static class RandomHolder {
		static final SecureRandomGenerator random = ServiceRegistry.getRegistry().getService(SecureRandomService.class).createGenerator("saltGenerator");
	}

	private TenantContextService tenantContextService;
	private AuthenticationPolicyService authPolicyService;

	private boolean updatable;

	private BuiltinAccountManagementModule amm = new BuiltinAccountManagementModule();
	private AccountStore accountDao;

	private List<PasswordHashSetting> passwordHashSettings;

	public BuiltinAuthenticationProvider() {
	}

	public boolean isUpdatable() {
		return updatable;
	}

	public void setUpdatable(boolean updatable) {
		this.updatable = updatable;
	}

	public List<PasswordHashSetting> getPasswordHashSettings() {
		return passwordHashSettings;
	}

	public void setPasswordHashSettings(
			List<PasswordHashSetting> passwordHashSettings) {
		this.passwordHashSettings = passwordHashSettings;
	}

	/**
	 * ログインユーザ共有か否かを取得
	 * @return boolean 共有テナントのユーザ情報がデータ共有か否かを返却
	 */
//	@Override
	public boolean isSharedLoginUser() {
		TenantContext shareTenantContext = tenantContextService.getSharedTenantContext();
		MetaDataEntry mde = shareTenantContext.getMetaDataContext().getMetaDataEntry(DefinitionService.getInstance().getPath(EntityDefinition.class, User.DEFINITION_NAME));
		return mde.isDataSharable();
	}

	private PasswordHashSetting selectSetting(String ver) {
		if (ver == null || ver.length() == 0 || passwordHashSettings == null) {
			return null;
		}

		for (int i = passwordHashSettings.size() - 1; i >= 0; i--) {
			PasswordHashSetting set = passwordHashSettings.get(i);
			if (ver.equals(set.getVersion())) {
				return set;
			}
		}

		return null;
	}

	private String[] divVerAndSalt(String saltString) {
		if (saltString != null && saltString.length() != 0 && saltString.charAt(0) == '$') {
			int index = saltString.indexOf('$', 1);
			String ver = saltString.substring(1, index);
			String salt = saltString.substring(index + 1);
			return new String[]{ver, salt};
		} else {
			return new String[]{null, saltString};
		}
	}

	private AuthenticationPolicyRuntime getPolicy(String policyName) {
		AuthenticationPolicyRuntime pol = authPolicyService.getOrDefault(policyName);
		if (pol == null) {
			throw new SystemException("policy:" + policyName + " not found.");
		}

		return pol;
	}

	@Override
	public AccountHandle login(Credential credential) {

		String accountId;
		String password;
		if (credential instanceof IdPasswordCredential) {
			accountId = ((IdPasswordCredential) credential).getId();
			password = ((IdPasswordCredential) credential).getPassword();
		} else {
			return null;
//			throw new SystemException("BuiltinAuthenticationProvider supports IdPasswordCredential");
		}

		if (accountId == null || accountId.isEmpty()) {
			return null;
		}

		int tenantId = getTenantId();

		final BuiltinAccount account = accountDao.getAccount(tenantId, accountId);
		if (account == null) {
			return null;
		}

		//前回ログイン日時を記録
		Timestamp lastLoginOn = account.getLastLoginOn();

		AuthenticationPolicyRuntime policy = getPolicy(account.getPolicyName());
		//認証時のPolicyをセット
		AuthContextHolder.getAuthContext().setPolicy(policy);

		//ログイン関連ステータスの初期化
		boolean needAccountUpdate = policy.initLoginStatus(account);

		//パスワードチェック
		String[] verAndSalt = divVerAndSalt(account.getSalt());
		if (!account.getPassword().equals(convertPassword(password, verAndSalt[1], selectSetting(verAndSalt[0])))) {
			//ログイン失敗回数の記録
			loginFail(account, policy);
			return null;
		}

		//パスワード情報クリア
		account.setPassword(null);
		account.setSalt(null);

		final BuiltinAccountHandle user = new BuiltinAccountHandle(account, policy.getMetaData().getName());

		//短絡評価されないように、||でなく、|を利用
		needAccountUpdate = needAccountUpdate | policy.checkLoginPolicy(user, account);

		if (needAccountUpdate) {
			accountDao.updateAccountLoginStatus(account);
		}

		//前回ログイン日時を復帰
		account.setLastLoginOn(lastLoginOn);
		return user;
	}
	
	@Override
	public void afterLoginSuccess(AccountHandle account) {
		Timestamp llo = (Timestamp) account.getAttributeMap().get(AccountHandle.LAST_LOGIN_ON);
		if (llo == null) {
			String policyName = (String) account.getAttributeMap().get(User.ACCOUNT_POLICY);
			AuthenticationPolicyRuntime pol = authPolicyService.getOrDefault(policyName);
			if (pol != null && pol.getMetaData().isRecordLastLoginDate()) {
				//前回ログイン日時を取得
				BuiltinAccount ba = accountDao.getAccountFromOid(getTenantId(), account.getUnmodifiableUniqueKey());
				if (ba != null) {
					account.getAttributeMap().put(AccountHandle.LAST_LOGIN_ON, ba.getLastLoginOn());
				}
			}
		}
	}
	

	private void loginFail(final BuiltinAccount account, final AuthenticationPolicyRuntime policy) {
		if (policy.isCheckLockout()) {
			account.loginFail();
			Transaction.requiresNew(t -> {
				accountDao.updateAccountLoginStatus(account);
			});
			if (policy.isJustLockedout(account)) {
				policy.notify(new AccountNotification(NotificationType.ROCKEDOUT, account.getOid()));
			}
		}
	}

	@Override
	public void logout(AccountHandle user) {

	}

	@Override
	public Class<? extends Credential> getCredentialType() {
		return IdPasswordCredential.class;
	}

	@Override
	protected Class<? extends AccountHandle> getAccountHandleClassForTrust() {
		return BuiltinAccountHandle.class;
	}

	@Override
	public void destroyed() {
		super.destroyed();
	}

	@Override
	public void inited(AuthService s, Config config) {
		super.inited(s, config);

		tenantContextService = ServiceRegistry.getRegistry().getService(TenantContextService.class);
		authPolicyService = ServiceRegistry.getRegistry().getService(AuthenticationPolicyService.class);
		accountDao = new RdbAccountStore();
		accountDao.inited(this, config);

		if (passwordHashSettings == null) {
			throw new ServiceConfigrationException("passwordHashSettings must specified.");
		}
		//check hash alg
		for (PasswordHashSetting phs: passwordHashSettings) {
			try {
				MessageDigest.getInstance(phs.getPasswordHashAlgorithm());
			} catch (NoSuchAlgorithmException e) {
				throw new ServiceConfigrationException("invalid PasswordHashAlgorithm", e);
			}
		}
	}

	@Override
	public AccountManagementModule getAccountManagementModule() {
		return amm;
	}

	/**
	 * パスワードのHash値を作成する。
	 *
	 * @param password
	 * @return 変換後の文字列
	 */
	private String convertPassword(String password, String salt, PasswordHashSetting setting) {
			return setting.hash(password, salt);
	}

	private static String makeSalt() {
		return RandomHolder.random.secureRandomToken();
	}

	private int getTenantId() {
		int tenantId;
		if (isSharedLoginUser()) {
			tenantId = tenantContextService.getSharedTenantId();
		} else {
			tenantId = ExecuteContext.getCurrentContext().getCurrentTenant().getId();
		}
		return tenantId;
	}

	private boolean isUserAdminRole(Tenant tenant, AuthContext auth) {
		if (auth.getUser().isAdmin()) {
			return true;
		}
		List<String> userAdminRoles = tenant.getTenantConfig(TenantAuthInfo.class).getUserAdminRoles();
		if (userAdminRoles != null) {
			for (String role: userAdminRoles) {
				if (auth.userInRole(role)) {
					return true;
				}
			}
		}
		return false;
	}

	private class BuiltinAccountManagementModule implements AccountManagementModule {

		@Override
		public void create(User user) {
			if (canCreate()) {
				AuthContext authContext = AuthContext.getCurrentContext();
				Tenant tenant = ExecuteContext.getCurrentContext().getCurrentTenant();
				AuthenticationPolicyRuntime policy = getPolicy(user.getAccountPolicy());

				// 管理者権限がないにも関わらず、管理者項目を登録しようとしていた場合エラー
				if (!authContext.isPrivileged()) {
					//adminフラグはadminのみ設定可能
					if (user.isAdmin()) {
						if (!authContext.getUser().isAdmin()) {
							throw new NoPermissionException(resourceString("impl.auth.authenticate.builtin.BuiltinAuthenticationProvider.registerAdmin"));
						}
					}
					//DEFAULT以外のauthPolicyの設定はUserAdminRole、adminのみ可能
					if (user.getAccountPolicy() != null
							&& !user.getAccountPolicy().equals(AuthenticationPolicyService.DEFAULT_NAME)) {
						if (!isUserAdminRole(tenant, authContext)) {
							throw new NoPermissionException(resourceString("impl.auth.authenticate.builtin.BuiltinAuthenticationProvider.canNotSetSecuredProperty"));
						}
					}
				}

				//パスワード指定方式
				if(policy.getMetaData().getPasswordPolicy().isCreateAccountWithSpecificPassword()) {
					if (user.getPassword() != null) {
						//パスワードが設定されている場合パスワードパターンチェック
						policy.checkPasswordPattern(user.getPassword(), user.getAccountId());
					}
				}

				//TODO ここの段階で、ユーザ存在チェックは必要か？
				//Userの取得
				User userEntity = getUserEntity(user.getAccountId(), false);
				//Userが存在していた場合
				if (userEntity != null) {
					// ユーザがすでに存在していた場合はエラーとする。
					throw new UserExistsException(resourceString("impl.auth.authenticate.builtin.BuiltinAuthenticationProvider.alreadyRegistered"));
				}
			}
		}

		@Override
		public void afterCreate(User user) {
			if (canCreate()) {
				AuthenticationPolicyRuntime policy = getPolicy(user.getAccountPolicy());

				int tenantId = getTenantId();
				String registId = ExecuteContext.getCurrentContext().getClientId();
				BuiltinAccount account = accountDao.getAccount(tenantId, user.getAccountId());
				if (account == null) {
					account = new BuiltinAccount();
					account.setTenantId(tenantId);
					account.setAccountId(user.getAccountId());
					account.setOid(user.getOid());
					account.setPolicyName(policy.getMetaData().getName());

					//パスワード生成
					boolean isGenPasswoed = true;
					String newPassword;
					if (policy.getMetaData().getPasswordPolicy().isCreateAccountWithSpecificPassword()) {
						//パスワード指定方式の場合
						if (user.getPassword() != null) {
							newPassword = user.getPassword();
							isGenPasswoed = false;
							account.setLastPasswordChange(new Date(System.currentTimeMillis()));
						} else {
							newPassword = policy.makePassword();
							isGenPasswoed = true;
						}
					} else {
						//仮パスワード発行方式の場合
						newPassword = policy.makePassword();
						isGenPasswoed = true;
					}

					final String salt = makeSalt();
					if (passwordHashSettings == null) {
						account.setPassword(convertPassword(newPassword, salt, null));
						account.setSalt(salt);
					} else {
						PasswordHashSetting newest = passwordHashSettings.get(passwordHashSettings.size() - 1);
						account.setPassword(convertPassword(newPassword, salt, newest));
						account.setSalt("$" + newest.getVersion() + "$" + salt);
					}

					//ユーザパスワードクリア
					user.setPassword(null);

					// アカウント情報を登録する
					accountDao.registAccount(account, registId);

					//通知
					policy.notify(new PasswordNotification(NotificationType.CREATED, user.getOid(), newPassword, isGenPasswoed));
				} else {
					//存在している場合は、エラー
					throw new UserExistsException(resourceString("impl.auth.authenticate.builtin.BuiltinAuthenticationProvider.userRemainTrash"));
				}
			}
		}

		@Override
		public void update(User user, List<String> updateProperties) {

			AuthContext authContext = AuthContext.getCurrentContext();
			Tenant tenant = ExecuteContext.getCurrentContext().getCurrentTenant();
			String updateId = ExecuteContext.getCurrentContext().getClientId();

			AuthenticationPolicyRuntime policy = null;

			//管理者権限チェック
			boolean updateAdminFlag = updateProperties.contains(User.ADMIN_FLG);
			if (!authContext.isPrivileged()) {
				//adminフラグはadminのみ設定可能
				if (updateAdminFlag) {
					if (!authContext.getUser().isAdmin()) {
						throw new NoPermissionException(resourceString("impl.auth.authenticate.builtin.BuiltinAuthenticationProvider.canNotSetSecuredProperty"));
					}
				}
				//authPolicyの設定はUserAdminRole、adminのみ可能
				if (updateProperties.contains(User.ACCOUNT_POLICY)) {
					if (!isUserAdminRole(tenant, authContext)) {
						throw new NoPermissionException(resourceString("impl.auth.authenticate.builtin.BuiltinAuthenticationProvider.canNotSetSecuredProperty"));
					}
				}
			}

			//ログインユーザ共有でない場合または、テナントIDが共有テナントの場合
			if (canUpdate()) {
				int tenantId = getTenantId();
				BuiltinAccount account = accountDao.getAccountFromOid(tenantId, user.getOid());
				if (account == null) {
					// データが不正な状態
					throw new SystemException(resourceString("impl.auth.authenticate.builtin.BuiltinAuthenticationProvider.incorrectUser"));
				}

				//AdminフラグをOFFにしようとしている場合、adminが一人もいなくなってしまうことがないようにチェック
				if (updateAdminFlag && !user.isAdmin()) {
					//Userの取得
					User beforeUser = getUserEntityFromOid(user.getOid());
					if (beforeUser != null) {
						if (beforeUser.isAdmin()) {
							// 最終管理者の確認
							if (getAdministratorCount() == 1) {
								// 最終なので削除できない
								throw new ApplicationException(resourceString("impl.auth.authenticate.builtin.BuiltinAuthenticationProvider.adminLast", user.getName()));
							}
						}
					} else {
						// データが不正な状態
						throw new SystemException(resourceString("impl.auth.authenticate.builtin.BuiltinAuthenticationProvider.notExist"));
					}
				}

				// 更新要否の確認
				boolean updateFlg = false;

				//ユーザIDに変更が発生していた場合
				if (updateProperties.contains(User.ACCOUNT_ID)) {
					//Userの取得
					User userEntity = getUserEntity(user.getAccountId(), false);
					//Userが存在していた場合
					if (userEntity != null && !(userEntity.getOid().equals(user.getOid()))) {
						// ユーザがすでに存在していた場合はエラーとする。
						throw new UserExistsException(resourceString("impl.auth.authenticate.builtin.BuiltinAuthenticationProvider.alreadyRegistered"));
					}

					//Password履歴の更新
					accountDao.updatePasswordHistoryAccountId(tenantId, account.getAccountId(), user.getAccountId());

					account.setAccountId(user.getAccountId());
					updateFlg = true;
				}
				//アカウントポリシーに変更が発生していた場合
				if (updateProperties.contains(User.ACCOUNT_POLICY)) {
					policy = getPolicy(user.getAccountPolicy());
					account.setPolicyName(policy.getMetaData().getName());
					updateFlg = true;
				}

				//更新要の場合、更新
				if(updateFlg) {
					accountDao.updateAccount(account, updateId);
				}

				//通知
				if (policy == null) {
					policy = getPolicy(account.getPolicyName());
				}
				policy.notify(new PropertyNotification(NotificationType.PROPERTY_UPDATED, account.getOid(), new ArrayList<>(updateProperties)));

			}
		}

		@Override
		public void remove(User user) {
			if (canRemove()) {
				AuthContext authContext = AuthContext.getCurrentContext();
				User loginUser = authContext.getUser();

				if(loginUser.getValue(User.ACCOUNT_ID).equals(user.getValue(User.ACCOUNT_ID))) {
					throw new ApplicationException(resourceString("impl.auth.authenticate.builtin.BuiltinAuthenticationProvider.deleteOwnData"));
				}

				//管理者ではないユーザが管理者を削除するのは不可
				if (user.isAdmin() && !authContext.getUser().isAdmin() && !authContext.isPrivileged()) {
					throw new NoPermissionException(resourceString("impl.auth.authenticate.builtin.BuiltinAuthenticationProvider.canNotDeleteAdmin"));
				}

				// 管理者権限を保有しているので最終かいなかチェックする
				if (user.isAdmin() && getAdministratorCount() == 1) {
					// 最終なので削除できない
					throw new ApplicationException(resourceString("impl.auth.authenticate.builtin.BuiltinAuthenticationProvider.canNotdeleted", user.getName()));
				}

				//通知
				int tenantId = getTenantId();
				BuiltinAccount ac = accountDao.getAccountFromOid(tenantId, user.getOid());
				if (ac != null) {
					AuthenticationPolicyRuntime policy = getPolicy(ac.getPolicyName());
					policy.notify(new AccountNotification(NotificationType.REMOVE, user.getOid()));
				}
			}
		}

		@Override
		public void restore(User user) {
		}

		@Override
		public void purge(User user) {
			//ログインユーザ共有でない場合または、テナントIDが共有テナントの場合
			if (canPurge()) {
				int tenantId = getTenantId();
				//アカウント情報削除
				BuiltinAccount ac = accountDao.getAccountFromOid(tenantId, user.getOid());
				if (ac != null) {
					accountDao.removeAccount(tenantId, ac.getAccountId());
					accountDao.deletePasswordHistory(tenantId, ac.getAccountId());
				}
			}
		}

		@Override
		public void updateCredential(Credential oldCredential,
				Credential newCredential) {
			if (canUpdateCredential()) {
				IdPasswordCredential oldIdPass = (IdPasswordCredential) oldCredential;
				IdPasswordCredential newIdPass = (IdPasswordCredential) newCredential;

				if (oldIdPass.getPassword().equals(newIdPass.getPassword())) {
					throw new CredentialUpdateException(resourceString("impl.auth.authenticate.updateCredential.sameErr"));
				}

				int tenantId = getTenantId();
				//Accountの取得
				final BuiltinAccount account = accountDao.getAccount(tenantId, oldCredential.getId());
				if (account == null) {
					// この段階でAccountが存在しないはSecurityエラー
					throw new CredentialUpdateException(resourceString("impl.auth.authenticate.updateCredential.noAccountErr"));
				}

				AuthenticationPolicyRuntime policy = getPolicy(account.getPolicyName());

				try {
					//パスワードチェック
					String[] verAndSalt = divVerAndSalt(account.getSalt());
					if (!account.getPassword().equals(convertPassword(oldIdPass.getPassword(), verAndSalt[1], selectSetting(verAndSalt[0])))) {
						throw new CredentialUpdateException(resourceString("impl.auth.authenticate.updateCredential.unmatchPasswordErr"));
					}
				} catch (CredentialUpdateException e) {
					// エラーが発生しているので、パスワードエラー回数を更新する
					if (account != null) {
						loginFail(account, policy);
					}
					throw e;
				}

				//パスワードポリシーの確認
				policy.checkPasswordUpdatePolicy(newIdPass, account);
				//パスワード履歴の確認
				List<Password> passList = null;
				int passwordHistoryCount = policy.getMetaData().getPasswordPolicy().getPasswordHistoryCount();
				int passwordHistoryPeriod = policy.getMetaData().getPasswordPolicy().getPasswordHistoryPeriod();
				if (passwordHistoryCount > 0 || passwordHistoryPeriod > 0) {
					passList = accountDao.getPasswordHistory(account.getTenantId(), account.getAccountId());
					checkPasswordHistory(newIdPass.getPassword(), account, passList, passwordHistoryCount, passwordHistoryPeriod);
				}

				//Salt再作成
				String newSalt = makeSalt();
				//パスワード更新
				Timestamp currentTime = new Timestamp(System.currentTimeMillis());
				Password pass = null;
				if (passwordHashSettings == null) {
					pass = new Password(tenantId, oldCredential.getId(), convertPassword(newIdPass.getPassword(), newSalt, null), newSalt, currentTime);
				} else {
					PasswordHashSetting newest = passwordHashSettings.get(passwordHashSettings.size() - 1);
					pass = new Password(tenantId, oldCredential.getId(), convertPassword(newIdPass.getPassword(), newSalt, newest), "$" + newest.getVersion() + "$" + newSalt, currentTime);
				}
				accountDao.updatePassword(pass, ExecuteContext.getCurrentContext().getClientId());
				if (passwordHistoryCount > 0 || passwordHistoryPeriod > 0) {
					//前回パスワードを保存
					accountDao.addPasswordHistory(new Password(tenantId, account.getAccountId(), account.getPassword(), account.getSalt(), currentTime));
				}
				if (passList != null) {
					if (passwordHistoryCount <= 0 || passList.size() >=  passwordHistoryCount) {
						//過去のパスワード履歴の削除（厳密な削除は求めないものとする。タイムスタンプが同一の場合を考慮しない）
						//パスワード保持個数が設定されている場合はパスワード保持個数から溢れたインデックスを開始インデックスとする。
						int startIndex = passwordHistoryCount > 0 ? passwordHistoryCount -1 : 0;
						for (int i = startIndex; i < passList.size(); i++) {
							Password pwd = passList.get(i);
							//パスワード保持期間が設定されている場合は更新日時がパスワード保持期間外であるパスワードを削除
							if (passwordHistoryPeriod <= 0 || pwd.getUpdateDate().getTime() + TimeUnit.DAYS.toMillis(passwordHistoryPeriod) < System.currentTimeMillis()) {
								accountDao.deletePasswordHistory(pwd.getTenantId(), pwd.getUid(), pwd.getUpdateDate());
								break;
							}
						}
					}
				}

				//通知
				policy.notify(new PasswordNotification(NotificationType.CREDENTIAL_UPDATED, account.getOid(), newIdPass.getPassword(), false));
			}
		}

		@Override
		public boolean canCreate() {
			return isUpdatable();
		}

		@Override
		public boolean canUpdate() {
			return isUpdatable();
		}

		@Override
		public boolean canRemove() {
			return isUpdatable();
		}

		@Override
		public boolean canRestore() {
			return isUpdatable();
		}

		@Override
		public boolean canPurge() {
			return isUpdatable();
		}

		@Override
		public boolean canUpdateCredential() {
			return isUpdatable();
		}

		@Override
		public boolean canResetCredential() {
			return isUpdatable();
		}

		@Override
		public void resetCredential(Credential credential)
				throws CredentialUpdateException {
			if (canResetCredential()) {
				int tenantId = getTenantId();
				//Accountの取得
				final BuiltinAccount account = accountDao.getAccount(tenantId, credential.getId());
				if (account == null) {
					// この段階でAccountが存在しないはSecurityエラー
					throw new CredentialUpdateException(resourceString("impl.auth.authenticate.updateCredential.noAccountErr"));
				}

				AuthenticationPolicyRuntime policy = getPolicy(account.getPolicyName());

				boolean isGenPassword = true;
				final String newPassword;
				if (policy.getMetaData().getPasswordPolicy().isResetPasswordWithSpecificPassword()
						&& ((IdPasswordCredential) credential).getPassword() != null) {

					//管理者もしくは、特権実行のみリセット可能
					AuthContext authContext = AuthContext.getCurrentContext();
					Tenant tenant = ExecuteContext.getCurrentContext().getCurrentTenant();
					if (!isUserAdminRole(tenant, authContext) && !authContext.isPrivileged()) {
						throw new NoPermissionException(resourceString("impl.auth.authenticate.builtin.BuiltinAuthenticationProvider.canNotSetSecuredProperty"));
					}

					newPassword = ((IdPasswordCredential) credential).getPassword();
					isGenPassword = false;

					//パスワードポリシーの確認
					policy.checkPasswordPattern(newPassword, account.getAccountId());
					//パスワード履歴の確認
					List<Password> passList = null;
					int passwordHistoryCount = policy.getMetaData().getPasswordPolicy().getPasswordHistoryCount();
					int passwordHistoryPeriod = policy.getMetaData().getPasswordPolicy().getPasswordHistoryPeriod();
					if (passwordHistoryCount > 0 || passwordHistoryPeriod > 0) {
						passList = accountDao.getPasswordHistory(account.getTenantId(), account.getAccountId());
						checkPasswordHistory(newPassword, account, passList, passwordHistoryCount, passwordHistoryPeriod);
					}
				} else {
					if (((IdPasswordCredential) credential).getPassword() != null) {
						logger.warn("resetCredential() was called with specific password, but authPolicy not allow specific password...");
					}

					newPassword = policy.makePassword();
				}

				final String newSalt = makeSalt();

				//パスワード更新
				Password pass = null;
				Timestamp updateTime = null;
				if (!isGenPassword) {
					updateTime = new Timestamp(System.currentTimeMillis());
				}

				if (passwordHashSettings == null) {
					pass = new Password(tenantId, credential.getId(), convertPassword(newPassword, newSalt, null), newSalt, updateTime);
				} else {
					PasswordHashSetting newest = passwordHashSettings.get(passwordHashSettings.size() - 1);
					pass = new Password(tenantId, credential.getId(), convertPassword(newPassword, newSalt, newest), "$" + newest.getVersion() + "$" + newSalt, updateTime);
				}
				accountDao.updatePassword(pass, ExecuteContext.getCurrentContext().getClientId());

				//通知
				policy.notify(new PasswordNotification(NotificationType.CREDENTIAL_RESET, account.getOid(), newPassword, isGenPassword));
			}
		}

		//管理者権限保持者カウント
		private long getAdministratorCount() {
			return AuthContext.doPrivileged(() -> {
				Query q = new Query()
					.select(new Count())
					.from(User.DEFINITION_NAME)
					.where(new Equals(User.ADMIN_FLG, true));
				final Long[] adminCnt = new Long[1];
				EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);
				em.search(q, new Predicate<Object[]>() {
					@Override
					public boolean test(Object[] data) {
						adminCnt[0] = (Long) data[0];
						return false;//1件のはず
					}
				});
				return adminCnt[0];
			});
		}

		//ユーザEntity取得
		private User getUserEntity(final String accountId, final boolean withAllProp) {
			return AuthContext.doPrivileged(() -> {
				if (withAllProp) {
					Query query =
							new Query().selectAll(User.DEFINITION_NAME, true, false)
								.where(new Equals(User.ACCOUNT_ID, accountId));
						SearchResult<Entity> user = ManagerLocator.getInstance().getManager(EntityManager.class).searchEntity(query);
						if(user.getList().size() > 0) {
							return (User) user.getList().get(0);
						}
				} else {
					Query query =
							new Query().select(User.OID, User.ACCOUNT_ID)
								.from(User.DEFINITION_NAME)
								.where(new Equals(User.ACCOUNT_ID, accountId));
						SearchResult<Entity> user = ManagerLocator.getInstance().getManager(EntityManager.class).searchEntity(query);
						if(user.getList().size() > 0) {
							return (User) user.getList().get(0);
						}
				}
				return null;
			});
		}

		//oidからユーザEntity取得
		private User getUserEntityFromOid(final String oid) {
			return AuthContext.doPrivileged(() -> {
				Query query =
						new Query().selectAll(User.DEFINITION_NAME, true, false)
							.where(new Equals(Entity.OID, oid));
					SearchResult<Entity> user = ManagerLocator.getInstance().getManager(EntityManager.class).searchEntity(query);
					if(user.getList().size() > 0){
						return (User) user.getList().get(0);
					}
					return null;
			});
		}

		private void checkPasswordHistory(String newPassword, BuiltinAccount account, List<Password> passList, int passwordHistoryCount, int passwordHistoryPeriod) {
			if (passList != null) {
				for (int i = 0; i < passList.size(); i++) {
					Password pwd = passList.get(i);
					//パスワード保持個数内またはパスワード保持期間内の場合、そのパスワード履歴をチェック
					if(passwordHistoryCount > i || (passwordHistoryPeriod > 0 && pwd.getUpdateDate().getTime() + TimeUnit.DAYS.toMillis(passwordHistoryPeriod) >=  System.currentTimeMillis())) {
						String[] verAndSalt = divVerAndSalt(pwd.getSalt());
						if (pwd.getConvertedPassword().equals(convertPassword(newPassword, verAndSalt[1], selectSetting(verAndSalt[0])))) {
							throw new CredentialUpdateException(resourceString("impl.auth.authenticate.updateCredential.passHistoryExists"));
						}
					}
				}
			}
		}

		@Override
		public boolean canResetLockoutStatus() {
			return isUpdatable();
		}

		@Override
		public void resetLockoutStatus(String accountId) {
			if (canResetLockoutStatus()) {
				//管理者もしくは、特権実行のみリセット可能
				AuthContext authContext = AuthContext.getCurrentContext();
				Tenant tenant = ExecuteContext.getCurrentContext().getCurrentTenant();
				if (!isUserAdminRole(tenant, authContext) && !authContext.isPrivileged()) {
					throw new NoPermissionException(resourceString("impl.auth.authenticate.builtin.BuiltinAuthenticationProvider.canNotSetSecuredProperty"));
				}

				int tenantId = getTenantId();
				//Accountの取得
				final BuiltinAccount account = accountDao.getAccount(tenantId, accountId);
				if (account == null) {
					logger.warn("resetLockoutStatus() was called with accountId:" + accountId + ", but not exists.");
					return;
				}

				if (account.getLoginErrorCnt() > 0) {
					accountDao.resetLoginErrorCnt(account);
				}
			}
		}
	}

	private static String resourceString(String key, Object... arguments) {
		return CoreResourceBundleUtil.resourceString(key, arguments);
	}
}
