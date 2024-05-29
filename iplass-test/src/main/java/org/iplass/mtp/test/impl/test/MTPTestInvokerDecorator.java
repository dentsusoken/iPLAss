/*
 * Copyright (C) 2024 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.test.impl.test;

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
import org.iplass.mtp.test.TestManagerLocator;

/**
 * テスト実施において前後処理拡張
 *
 * <p>
 * テスト設定に基づいて前後処理の拡張を行う。
 * </p>
 *
 *
 * @author SEKIGUCHI Naoya
 */
public class MTPTestInvokerDecorator {
	/**
	 * プライベートコンストラクタ
	 */
	private MTPTestInvokerDecorator() {
	}

	/**
	 * テストの前後処理拡張する
	 * @param <T> 返却データ型
	 * @param testProcess テスト処理
	 * @return 前後処理拡張したテスト処理
	 */
	public static <T> MTPTestInvoker<T> decorate(MTPTestInvoker<T> testProcess) {
		// サービスコンフィグ初期化
		return new ServiceConfigDecorator<T>(
				// 初期化処理
				new InitializeDecorator<T>(
						// トランザクション制御
						new TransactionDecorator<T>(
								// 認証
								new AuthenticationDecorator<T>(
										// モックマネージャーリセット
										new MockManagerDecorator<T>(testProcess)))));
	}

	/**
	 * 機能拡張マーキングインターフェース
	 * @param <T> 返却データ型
	 */
	private static interface Decorator<T> extends MTPTestInvoker<T> {
	}

	/**
	 * サービスコンフィグ初期化
	 *
	 * <p>
	 * サービスコンフィグが設定されていた場合初期化を行う
	 * </p>
	 */
	private static class ServiceConfigDecorator<T> implements Decorator<T> {
		/** テスト対象 */
		private MTPTestInvoker<T> test;

		/**
		 * コンストラクタ
		 * @param test テスト対象
		 */
		public ServiceConfigDecorator(MTPTestInvoker<T> test) {
			this.test = test;
		}

		@Override
		public T invoke(MTPTestConfig config) throws Throwable {
			if (config.getConfigFileName() != null) {
				if (BootstrapProps.getInstance().replaceProperty(BootstrapProps.CONFIG_FILE_NAME, config.getConfigFileName())) {
					ServiceRegistry.getRegistry().reInit();
				}
			}

			return test.invoke(config);
		}
	}

	/**
	 * 初期化処理
	 *
	 * <p>
	 * ResourceHolder の初期化および破棄、ExecuteContext の設定を責務とする。
	 * </p>
	 */
	private static class InitializeDecorator<T> implements Decorator<T> {
		/** テスト対象 */
		private MTPTestInvoker<T> test;

		/**
		 * コンストラクタ
		 * @param test テスト対象
		 */
		public InitializeDecorator(MTPTestInvoker<T> test) {
			this.test = test;
		}

		@Override
		public T invoke(MTPTestConfig config) throws Throwable {
			boolean isInit = false;
			try {
				isInit = ResourceHolder.init();

				//ここでセットしておかないと、TransactionManagerが初期化されてしまう
				TransactionService ts = ServiceRegistry.getRegistry().getService(TransactionService.class);
				if (!(ts instanceof TestTransactionService)) {
					ServiceRegistry.getRegistry().setService(TransactionService.class.getName(), new TestTransactionService());
				}

				TenantContext tc = ServiceRegistry.getRegistry().getService(TenantContextService.class).getTenantContext("/" + config.getTenantName());
				if (tc == null) {
					throw new IllegalArgumentException("tenantName:" + config.getTenantName() + " not found");
				}
				try {
					return ExecuteContext.executeAs(tc, () -> {
						try {
							ClassLoader cl = Thread.currentThread().getContextClassLoader();
							boolean replaceCl = config.isGroovy();
							try {
								if (replaceCl) {
									Thread.currentThread().setContextClassLoader(((GroovyScriptEngine) tc.getScriptEngine()).getSharedClassLoader());
								}
								return test.invoke(config);

							} finally {
								if (replaceCl) {
									Thread.currentThread().setContextClassLoader(cl);
								}
							}
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

	/**
	 * トランザクション制御
	 *
	 * <p>
	 * 設定に基づいて、TransactionService を設定する
	 * </p>
	 */
	private static class TransactionDecorator<T> implements Decorator<T> {
		/** テスト対象 */
		private MTPTestInvoker<T> test;

		/**
		 * コンストラクタ
		 * @param test テスト対象
		 */
		public TransactionDecorator(MTPTestInvoker<T> test) {
			this.test = test;
		}

		@Override
		public T invoke(MTPTestConfig config) throws Throwable {
			TransactionService ts = ServiceRegistry.getRegistry().getService(TransactionService.class);
			if (!(ts instanceof TestTransactionService)) {
				ServiceRegistry.getRegistry().setService(TransactionService.class.getName(), new TestTransactionService());
			}
			TestTransactionService.rollback = config.isRollbackTransaction();;
			return test.invoke(config);
		}
	}

	/**
	 * 認証
	 *
	 * <p>
	 * ログイン情報が設定されていた場合、ログインする。
	 * 認証無し設定がされていた場合は、ログアウトする。
	 * </p>
	 */
	private static class AuthenticationDecorator<T> implements Decorator<T> {
		/** テスト対象 */
		private MTPTestInvoker<T> test;

		/**
		 * コンストラクタ
		 * @param test テスト対象
		 */
		public AuthenticationDecorator(MTPTestInvoker<T> test) {
			this.test = test;
		}

		@Override
		public T invoke(MTPTestConfig config) throws Throwable {
			AuthService as = ServiceRegistry.getRegistry().getService(AuthService.class);
			if (Boolean.FALSE == config.isNoAuth()) {
				as.login(new IdPasswordCredential(config.getUserId(), config.getPassword()));
			} else {
				if (as.isAuthenticate()) {
					as.logout();
				}
			}
			try {
				return as.doSecuredAction(AuthContextHolder.getAuthContext(), () -> {
					try {
						return test.invoke(config);
					} catch (Throwable e) {
						throw new WrapException(e);
					}
				});
			} catch (WrapException e) {
				throw e.getCause();
			}
		}
	}

	/**
	 * モックマネージャーリセット
	 */
	private static class MockManagerDecorator<T> implements Decorator<T> {
		static {
			System.setProperty(ManagerLocator.MANAGER_LOCATOR_SYSTEM_PROPERTY_NAME, TestManagerLocator.class.getName());
		}
		/** テスト対象 */
		private MTPTestInvoker<T> test;

		/**
		 * コンストラクタ
		 * @param test テスト対象
		 */
		public MockManagerDecorator(MTPTestInvoker<T> test) {
			this.test = test;
		}

		@Override
		public T invoke(MTPTestConfig config) throws Throwable {
			try {
				return test.invoke(config);
			} finally {
				((TestManagerLocator) ManagerLocator.getInstance()).reset();
			}
		}
	}

	/**
	 * ラムダ式の例外を外部でハンドリングさせるためのラッパー例外クラス
	 */
	private static class WrapException extends RuntimeException {
		/** serialVersionUID */
		private static final long serialVersionUID = -4535302115540574513L;

		/**
		 * コンストラクタ
		 * @param cause 原因例外
		 */
		public WrapException(Throwable cause) {
			super(cause);
		}
	}

}
