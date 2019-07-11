/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.runtime;

import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.impl.runtime.EntryPointBuilderImpl;
import org.iplass.mtp.impl.runtime.EntryPointImpl;

/**
 * <p>
 * EntryPointを利用し、Servletコンテナ外のコード上から、
 * 直接iPLAssの初期化・破棄、また特定のテナント・ユーザを指定してのロジックの実行を行うことが可能です。
 * 
 * EntryPointは当該クラスがロードされるクラスローダ単位に一つのみ存在可能です。
 * </p>
 * 
 * <h5>EntryPointの利用例</h5>
 * <pre>
 *   //initialize EntryPoint on application startup
 *   EntryPoint entryPoint = EntryPoint.builder()
 *       .config("/backend-service-config.xml")
 *       .serverRole("backendService")
 *       .build();
 *   
 *   :
 *   :
 *   
 *   //do process some logic while application is running
 *   entryPoint.withTenant(tenantId).withLang("en").run(() -> {
 *   
 *     :
 *     :
 *     
 *     Entity e = entityManager.load(oid, "SomeEntity");
 *     
 *     :
 *     :
 *     
 *   });
 *   
 *   :
 *   :
 *   
 *   //destroy EntryPoint when application is shutdown
 *   entryPoint.destroy();
 * 
 * </pre>
 * 
 * @author K.Higuchi
 *
 */
public interface EntryPoint extends AutoCloseable {

	/**
	 * EntryPointを初期化し、そのインスタンスを取得します。
	 * すでに初期化済みのEntryPointが存在する場合、
	 * SystemExceptionがスローされます。
	 * 
	 * @return
	 */
	public static EntryPoint init() {
		return builder().build();
	}
	
	/**
	 * EntryPointのBuilderを取得します。
	 * Builder経由で、EntryPointを構築する際のパラメータを指定可能です。
	 * 
	 * @return
	 */
	public static EntryPointBuilder builder() {
		return new EntryPointBuilderImpl();
	}
	
	/**
	 * ApplicationContextがすでに初期化済みか否かを取得します。
	 * 
	 * @return
	 */
	public static boolean isInited() {
		return EntryPointImpl.isInited();
	}
	
	/**
	 * iPLAss内でロジックを実行するためのRunnerを取得します。
	 * 
	 * @return
	 */
	public Runner runner();
	
	/**
	 * 言語を指定した状態のRunnerのインスタンスを取得します。
	 * 
	 * @param lang
	 * @return
	 */
	public default Runner withLang(String lang) {
		return runner().withLang(lang);
	}
	
	/**
	 * Credentialでログインした状態のRunnerのインスタンスを取得します。
	 * 
	 * @param credential
	 * @return
	 */
	public default Runner withAuth(Credential credential) {
		return runner().withAuth(credential);
	}
	
	/**
	 * テナントを指定した状態のRunnerのインスタンスを取得します。
	 * 
	 * @param tenantUrl テナントURL（識別子）
	 * @return
	 */
	public default Runner withTenant(String tenantUrl) {
		return runner().withTenant(tenantUrl);
	}
	
	/**
	 * テナントを指定した状態のRunnerのインスタンスを取得します。
	 * 
	 * @param tenantId テナントID
	 * @return
	 */
	public default Runner withTenant(Integer tenantId) {
		return runner().withTenant(tenantId);
	}
	
	/**
	 * EntryPointを破棄し、iPLAssが管理しているリソースを開放します。
	 */
	public void destroy();
	
	/**
	 * AutoCloseableインタフェースのclose()メソッドの実装です。
	 * destroy()を呼び出します。
	 * 
	 */
	@Override
	public default void close() {
		destroy();
	}

}
