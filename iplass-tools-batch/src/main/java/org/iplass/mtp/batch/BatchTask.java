/*
 * Copyright 2013 DENTSU SOKEN INC. All Rights Reserved.
 */

package org.iplass.mtp.batch;

import java.util.concurrent.Callable;

import org.iplass.mtp.auth.login.IdPasswordCredential;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.auth.AuthService;
import org.iplass.mtp.impl.core.Executable;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.rdb.connection.ResourceHolder;
import org.iplass.mtp.spi.ServiceRegistry;

/**
 * バッチタスクを実行するための簡易的なCallableのラッパー。
 * コンストラクタで指定されるテナント、（および、指定のユーザー）で
 * 実行コンテキストを初期化して実際の処理を呼び出す。
 * Callableをimplementsしているので、バッチ起動スレッドから直接call()を呼び出してもよいし、
 * ExecutorServiceを利用して非同期スレッドで実行してもよい。
 * 
 * @author K.Higuchi
 *
 * @param <V>
 */
public class BatchTask<V> implements Callable<V> {
	
	private final Callable<V> actualTask;
	
	private final String tenantName;
	
	private final String userId;
	private final String password;
	
	/**
	 * 指定のtenantNameで初期化し、actualTaskを実行する。
	 * ユーザーは未指定。Entityの更新ユーザーやログには、"batchTask"として記録される。
	 * 権限は、特権実行として判断される。
	 * 
	 * @param actualTask
	 * @param tenantName
	 */
	public BatchTask(Callable<V> actualTask, String tenantName) {
		this(actualTask, tenantName, null, null);
	}
	
	/**
	 * 指定のtenantName、指定のuserId、passwordで初期化し、actualTaskを実行する。
	 * 権限は指定のユーザーの権限が適用される。
	 * userIdがnullの場合は、
	 * Entityの更新ユーザーやログには、"batchTask"として記録される。
	 * また、権限は、特権実行として判断される。
	 * 
	 * @param actualTask
	 * @param tenantName
	 * @param userId
	 * @param password
	 */
	public BatchTask(Callable<V> actualTask, String tenantName, String userId, String password) {
		this.tenantName = tenantName;
		this.userId = userId;
		this.password = password;
		Callable<V> task = actualTask;
		task = new InitRHInterceptor(new TenantInterceptor(new AuthInterceptor(task)));
		this.actualTask = task;
	}

	/**
	 * 初期設定を行った後、actualTaskのcall()を呼び出す。
	 */
	@Override
	public V call() throws Exception {
		return actualTask.call();
	}
	
	
	private class InitRHInterceptor implements Callable<V> {
		
		private final Callable<V> actualTask;
		
		private InitRHInterceptor(Callable<V> actualTask) {
			this.actualTask = actualTask;
		}

		@Override
		public V call() throws Exception {
			boolean isInit = false;
			try {
				isInit = ResourceHolder.init();
				return actualTask.call();
			} finally {
				if (isInit) {
					ResourceHolder.fin();
				}
			}
			
		}
	}
	
	private class TenantInterceptor implements Callable<V> {
		
		private final Callable<V> actualTask;
		
		private TenantInterceptor(Callable<V> actualTask) {
			this.actualTask = actualTask;
		}

		@Override
		public V call() throws Exception {
			
			boolean needInit = false;
			if (!ExecuteContext.isInited()) {
				needInit = true;
			} else if (!tenantName.equals(ExecuteContext.getCurrentContext().getCurrentTenant().getName())) {
				needInit = true;
			}
			
			if (needInit) {
				TenantContext tc = ServiceRegistry.getRegistry().getService(TenantContextService.class).getTenantContext("/" + tenantName);
				if (tc == null) {
					throw new IllegalArgumentException("tenantName:" + tenantName + " not found");
				}
				try {
					return ExecuteContext.executeAs(tc, new Executable<V>() {
						@Override
						public V execute() {
							try {
								return actualTask.call();
							} catch (Exception e) {
								throw new WrapException(e);
							}
						}
					});
				} catch (WrapException e) {
					throw (Exception) e.getCause();
				}
			} else {
				return actualTask.call();
			}
		}
	}
	
	private class AuthInterceptor implements Callable<V> {
		
		private final Callable<V> actualTask;
		
		private AuthInterceptor(Callable<V> actualTask) {
			this.actualTask = actualTask;
		}

		@Override
		public V call() throws Exception {
			if (userId != null) {
				AuthService as = ServiceRegistry.getRegistry().getService(AuthService.class);
				as.login(new IdPasswordCredential(userId, password));
				try {
					return as.doSecuredAction(AuthContextHolder.getAuthContext(), () -> {
						try {
							return actualTask.call();
						} catch (Exception e) {
							throw new WrapException(e);
						}
					});
				} catch (WrapException e) {
					throw (Exception) e.getCause();
				}
			} else {
				ExecuteContext.getCurrentContext().setClientId("batchTask");
				return actualTask.call();
			}
		}
	}
	
	private static class WrapException extends RuntimeException {
		private static final long serialVersionUID = 5933671029711171148L;
		public WrapException(Throwable cause) {
			super(cause);
		}
	}

}
