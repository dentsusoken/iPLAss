/*
 * Copyright (C) 2016 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.login.IdPasswordCredential;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.auth.AuthService;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.core.config.BootstrapProps;
import org.iplass.mtp.impl.rdb.connection.ResourceHolder;
import org.iplass.mtp.impl.script.GroovyScriptEngine;
import org.iplass.mtp.impl.transaction.TransactionService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.test.impl.test.TestTransactionService;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import groovy.lang.GroovyObject;

/**
 * <p>
 * JUnitに単体テスト用のユーティリティ処理を組み込むためのTestRuleです。
 * </p>
 * <p>
 * MTPJUnitTestRuleを組み込むことにより、容易な方法により、iPLAssの設定ファイルの指定、テナント、テストユーザーの設定、トランザクション制御を行うことが可能です。
 * 設定方法は以下のいずれかの方法、またその組み合わせで行うことが可能です。
 * </p>
 * <dl>
 * <dt>プロパティファイルによる指定</dt><dd>テスト全体にわたり設定が適用されます</dd>
 * <dt>クラスレベルアノテーションによる指定</dt><dd>当該クラスに限り設定が適用されます</dd>
 * <dt>メソッドレベルアノテーションによる指定</dt><dd>当該メソッドに限り設定が適用されます</dd>
 * </dl>
 * <p>
 * 上記の指定方法は混在可能です。
 * 指定の優先度は、
 * </p>
 * <blockquote>メソッドレベルアノテーション ＞ クラスレベルアノテーション ＞ プロパティファイル</blockquote>
 * <p>
 * となります。
 * </p>
 * <h3>プロパティファイルによる指定方法</h3>
 * <p>
 * プロパティファイルは、"/mtptest.properties"として、クラスパスからロードされます。
 * プロパティファイルをロードするパスを変更する場合は、システムプロパティ"mtp.test.config"にて指定可能です。
 * 次の項目をプロパティファイルにて指定可能です。
 * </p>
 * <table border="1">
 * <caption>プロパティファイルの項目の説明</caption>
 * <tr><th>項目名</th><th>説明</th></tr>
 * <tr>
 * <td>configFileName</td>
 * <td>テスト実行時のiPLAssの設定ファイルのパスを指定します。<br>
 * 設定例：<br>
 * configFileName=/testsample/test-sample-service-config.xml</td>
 * </tr>
 * <tr>
 * <td>rollbackTransaction</td>
 * <td>テスト実行時のトランザクションをロールバックするか否かを指定します。<br>
 * 設定例：<br>
 * rollbackTransaction=true</td>
 * </tr>
 * <tr>
 * <td>tenantName</td>
 * <td>テスト実行時のテナント名を指定します。<br>
 * 設定例：<br>
 * tenantName=testDemo1</td>
 * </tr>
 * <tr>
 * <td>userId</td>
 * <td>テスト実行時のユーザーのaccountIdを指定します。<br>
 * 設定例：<br>
 * userId=testUser</td>
 * </tr>
 * <tr>
 * <td>password</td>
 * <td>テスト実行時のユーザーのパスワードを指定します。<br>
 * 設定例：<br>
 * password=testUserPass</td>
 * </tr>
 * </table>
 *
 * <h3>クラスレベルアノテーションによる指定、及びメソッドレベルアノテーションによる指定方法</h3>
 * <p>
 * アノテーションにより、クラス単位、メソッド単位でテナント、ユーザーなどの指定が可能です。
 * 指定可能なアノテーションは以下です。
 * </p>
 * <ul>
 * <li>{@link ConfigFile}</li>
 * <li>{@link TenantName}</li>
 * <li>{@link AuthUser}</li>
 * <li>{@link NoAuthUser}</li>
 * <li>{@link Commit}</li>
 * <li>{@link Rollback}</li>
 * </ul>
 * <p>
 * 以下にアノテーション記述されたテストクラスの例を示します。
 * </p>
 * <pre>
 * //アノテーションで指定されていない設定はプロパティファイルの設定値が適用されます。
 * {@literal @}TenantName("testDemo1")
 * {@literal @}AuthUser(userId="testuser", password="testtest")
 * public class SampleJavaTest {
 *   {@literal @}Rule
 *   public MTPJUnitTestRule rule = new MTPJUnitTestRule();
 *
 *   {@literal @}Test
 *   public void testHogeHoge() throws IOException {
 *     //do test...
 *     :
 *     :
 *   }
 *
 *   {@literal @}Test
 *   {@literal @}TenantName("testDemo2")
 *   {@literal @}AuthUser(userId="test2", password="testtest")
 *   public void testHogeHoge() throws IOException {
 *     //do test as tenant:testDemo2 user:test2 ...
 *     :
 *     :
 *   }
 *
 *   :
 *   :
 * }
 * </pre>
 * <p>
 * テストクラスは、Java、もしくはGroovyで記述可能です。
 * Groovyで記述された、Command、UtilityClassをテストする場合は、Groovyでテストクラスを記述した方が記述が簡易になります。
 * 詳しくはそれぞれのテストクラスのサンプル実装を参照下さい。
 * </p>
 *
 * @deprecated 本クラスは JUnit4 向けの機能となります。JUnit5 向けの {@link org.iplass.mtp.test.MTPJUnitTestExtension} を利用してください。
 * @author K.Higuchi
 *
 */
@Deprecated
public class MTPJUnitTestRule implements TestRule {

	public static final String TEST_CONFIG_FILE_NAME_SYSTEM_PROPERTY_NAME = "mtp.test.config";
	public static final String DEFAULT_TEST_CONFIG_FILE_NAME = "/mtptest.properties";
	public static final String PROP_CONFIG_FILE_NAME = "configFileName";
	public static final String PROP_ROLLBACK_TRANSACTION = "rollbackTransaction";
	public static final String PROP_USER_ID = "userId";
	public static final String PROP_PASSWORD = "password";
	public static final String PROP_TENANT_NAME = "tenantName";

	static {
		System.setProperty(ManagerLocator.MANAGER_LOCATOR_SYSTEM_PROPERTY_NAME, TestManagerLocator.class.getName());
	}

	private volatile String configFileName;
	private volatile boolean rollbackTransaction = true;
	private volatile String tenantName;
	private volatile String userId;
	private volatile String password;

	public MTPJUnitTestRule() {
		String propPath = System.getProperty(TEST_CONFIG_FILE_NAME_SYSTEM_PROPERTY_NAME);
		if (propPath == null) {
			propPath = DEFAULT_TEST_CONFIG_FILE_NAME;
		}

		Properties prop = null;
		try (InputStream is = MTPJUnitTestRule.class.getResourceAsStream(propPath);
				Reader r = new InputStreamReader(is, "UTF-8")) {
			if (is != null) {
				prop = new Properties();
				prop.load(r);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		if (prop != null) {
			String val = (String) prop.get(PROP_CONFIG_FILE_NAME);
			if (val != null && val.length() > 0) {
				configFileName = val;
			}
			val = (String) prop.get(PROP_ROLLBACK_TRANSACTION);
			if (val != null && val.length() > 0) {
				rollbackTransaction = Boolean.valueOf(val);
			}
			val = (String) prop.get(PROP_USER_ID);
			if (val != null && val.length() > 0) {
				userId = val;
			}
			val = (String) prop.get(PROP_PASSWORD);
			if (val != null && val.length() > 0) {
				password = val;
			}
			val = (String) prop.get(PROP_TENANT_NAME);
			if (val != null && val.length() > 0) {
				tenantName = val;
			}
		}
	}

	/**
	 * Ruleに直接configFileNameを設定します。
	 *
	 * @param configFileName
	 * @return
	 */
	public MTPJUnitTestRule configFileName(String configFileName) {
		this.configFileName = configFileName;
		return this;
	}

	/**
	 * Ruleに直接rollbackTransactionを設定します。
	 *
	 * @param rollbackTransaction
	 * @return
	 */
	public MTPJUnitTestRule rollbackTransaction(boolean rollbackTransaction) {
		this.rollbackTransaction = rollbackTransaction;
		return this;
	}

	/**
	 * Ruleに直接tenantを設定します。
	 *
	 * @param tenantName
	 * @return
	 */
	public MTPJUnitTestRule tenant(String tenantName) {
		this.tenantName = tenantName;
		return this;
	}

	/**
	 * Ruleに直接authUserを設定します。
	 *
	 * @param userId
	 * @param password
	 * @return
	 */
	public MTPJUnitTestRule authUser(String userId, String password) {
		this.userId = userId;
		this.password = password;
		return this;
	}

	@Override
	public Statement apply(final Statement base, final Description description) {
		return new ConfigStatement(base, description);
	}

	private class ConfigStatement extends Statement {
		private InitStatement base;
		private String configFile;

		private ConfigStatement(Statement base, Description description) {
			this.base = new InitStatement(base, description);
			if (configFileName != null) {
				configFile = configFileName;
			}

			ConfigFile cf = description.getTestClass().getAnnotation(ConfigFile.class);
			if (cf != null) {
				configFile = cf.value();
			}
			cf = description.getAnnotation(ConfigFile.class);
			if (cf != null) {
				configFile = cf.value();
			}
		}

		@Override
		public void evaluate() throws Throwable {

			if (configFile != null) {
				if (BootstrapProps.getInstance().replaceProperty(BootstrapProps.CONFIG_FILE_NAME, configFile)) {
					ServiceRegistry.getRegistry().reInit();
				}
			}

			base.evaluate();
		}
	}

	private class InitStatement extends Statement {

		private TransactionStatement base;
		private boolean isGroovy;
		private String tname;

		private InitStatement(Statement base, Description description) {
			isGroovy = GroovyObject.class.isAssignableFrom(description.getTestClass());
			this.base = new TransactionStatement(base, description);
			if (tenantName != null) {
				tname = tenantName;
			}

			TenantName tn = description.getTestClass().getAnnotation(TenantName.class);
			if (tn != null) {
				tname = tn.value();
			}
			tn = description.getAnnotation(TenantName.class);
			if (tn != null) {
				tname = tn.value();
			}
		}

		@Override
		public void evaluate() throws Throwable {

			boolean isInit = false;
			try {
				isInit = ResourceHolder.init();

				//ここでセットしておかないと、TransactionManagerが初期化されてしまう
				TransactionService ts = ServiceRegistry.getRegistry().getService(TransactionService.class);
				if (!(ts instanceof TestTransactionService)) {
					ServiceRegistry.getRegistry().setService(TransactionService.class.getName(), new TestTransactionService());
				}

				TenantContext tc = ServiceRegistry.getRegistry().getService(TenantContextService.class).getTenantContext("/" + tname);
				if (tc == null) {
					throw new IllegalArgumentException("tenantName:" + tname + " not found");
				}
				try {
					ExecuteContext.executeAs(tc, () -> {
						try {
							ClassLoader cl = Thread.currentThread().getContextClassLoader();
							boolean replaceCl = isGroovy;
							try {
								if (replaceCl) {
									Thread.currentThread().setContextClassLoader(((GroovyScriptEngine) tc.getScriptEngine()).getSharedClassLoader());
								}
								base.evaluate();

							} finally {
								if (replaceCl) {
									Thread.currentThread().setContextClassLoader(cl);
								}
							}
							return null;
						} catch (Throwable e) {
							throw new WrapException(e);
						}
					});
				} catch (WrapException e) {
					throw e.getCause();
				}
			} finally {
				if (isInit) {
					ResourceHolder.fin();
				}
			}
		}
	}

	private class TransactionStatement extends Statement {

		AuthStatement base;
		boolean isRollback;

		TransactionStatement(Statement base, Description description) {
			this.base = new AuthStatement(base, description);

			//default setting
			isRollback = rollbackTransaction;

			//class level annotation
			Commit tc = description.getTestClass().getAnnotation(Commit.class);
			if (tc != null) {
				isRollback = false;
			}
			Rollback tr = description.getTestClass().getAnnotation(Rollback.class);
			if (tr != null) {
				isRollback = true;
			}

			//method level annotation
			tc = description.getAnnotation(Commit.class);
			if (tc != null) {
				isRollback = false;
			}
			tr = description.getAnnotation(Rollback.class);
			if (tr != null) {
				isRollback = true;
			}
		}

		@Override
		public void evaluate() throws Throwable {
			TransactionService ts = ServiceRegistry.getRegistry().getService(TransactionService.class);
			if (!(ts instanceof TestTransactionService)) {
				ServiceRegistry.getRegistry().setService(TransactionService.class.getName(), new TestTransactionService());
			}
			TestTransactionService.rollback = isRollback;
			base.evaluate();
		}

	}

	private class AuthStatement extends Statement {

		MockManagerStatement base;
		String userId;
		String password;

		AuthStatement(Statement base, Description description) {
			this.base = new MockManagerStatement(base, description);

			//default setting
			userId = MTPJUnitTestRule.this.userId;
			password = MTPJUnitTestRule.this.password;

			//class level annotation
			AuthUser au = description.getTestClass().getAnnotation(AuthUser.class);
			if (au != null) {
				userId = au.userId();
				password = au.password();
			}
			NoAuthUser nau = description.getTestClass().getAnnotation(NoAuthUser.class);
			if (nau != null) {
				userId = null;
				password = null;
			}

			//method level annotation
			au = description.getAnnotation(AuthUser.class);
			if (au != null) {
				userId = au.userId();
				password = au.password();
			}
			nau = description.getAnnotation(NoAuthUser.class);
			if (nau != null) {
				userId = null;
				password = null;
			}
		}

		@Override
		public void evaluate() throws Throwable {

			AuthService as = ServiceRegistry.getRegistry().getService(AuthService.class);
			if (userId != null) {
				as.login(new IdPasswordCredential(userId, password));
			} else {
				if (as.isAuthenticate()) {
					as.logout();
				}
			}
			try {
				as.doSecuredAction(AuthContextHolder.getAuthContext(), () -> {
					try {
						base.evaluate();
						return null;
					} catch (Throwable e) {
						throw new WrapException(e);
					}
				});
			} catch (WrapException e) {
				throw e.getCause();
			}
		}

	}

	private class MockManagerStatement extends Statement {
		Statement base;

		public MockManagerStatement(Statement base, Description description) {
			this.base = base;
		}

		@Override
		public void evaluate() throws Throwable {
			try {
				base.evaluate();
			} finally {
				((TestManagerLocator) ManagerLocator.getInstance()).reset();
			}
		}
	}

	private static class WrapException extends RuntimeException {
		private static final long serialVersionUID = -9157815643184354559L;

		public WrapException(Throwable cause) {
			super(cause);
		}
	}


}
