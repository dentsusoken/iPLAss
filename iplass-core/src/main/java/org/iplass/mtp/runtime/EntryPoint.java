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
 * <% if (doclang == "ja") {%>
 * <p>
 * EntryPointを利用し、Servletコンテナ外のコード上から、
 * 直接iPLAssの初期化・破棄、また特定のテナント・ユーザーを指定してのロジックの実行を行うことが可能です。
 * 
 * EntryPointは当該クラスがロードされるクラスローダ単位に一つのみ存在可能です。
 * </p>
 * 
 * <h5>EntryPointの利用例</h5>
 * <%} else {%>
 * <p>
 * By using EntryPoint,
 * iPLAss can be initialized/destroyed directly from the code even if outside the servlet container,
 * and logic can be executed by specifying a tenant/user.
 * 
 * There can be only one EntryPoint per class loader unit where the class is loaded.
 * </p>
 * 
 * <h5>EntryPoint usage example</h5>
 * <%}%>
 * 
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
	 * <% if (doclang == "ja") {%>
	 * EntryPointのインスタンスを取得します。
	 * EntryPointがまだ初期化されていない場合、
	 * 初期化したのちにそのインスタンスを取得します。
	 * <%} else {%>
	 * Get an instance of EntryPoint.
	 * If EntryPoint has not been initialized,
	 * it will be initialized.
	 * <%}%>
	 * 
	 * @return
	 */
	public static EntryPoint getInstance() {
		EntryPoint i = EntryPointImpl.getInstance();
		if (i == null) {
			try {
				i = init();
			} catch (AlreadyInitializedException e) {
				i = EntryPointImpl.getInstance();
			}
		}
		return i;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * EntryPointを初期化し、そのインスタンスを取得します。
	 * すでに初期化済みのEntryPointが存在する場合、
	 * AlreadyInitializedExceptionがスローされます。
	 * <%} else {%>
	 * Initialize EntryPoint and get an instance of it.
	 * If there is an EntryPoint that has already been initialized,
	 * an AlreadyInitializedException is thrown.
	 * <%}%>
	 * 
	 * @return <%=doclang == 'ja' ? 'EntryPointのインスタンス': 'EntryPoint instance'%>
	 * @throws AlreadyInitializedException <%=doclang == 'ja' ? 'EntryPointが既に初期化済みの場合': 'When EntryPoint has already been initialized'%>
	 */
	public static EntryPoint init() {
		return builder().build();
	}
	
	/**
	 * <% if (doclang == "ja") {%>
	 * EntryPointのBuilderを取得します。
	 * Builder経由で、EntryPointを構築する際のパラメータを指定可能です。
	 * <%} else {%>
	 * Get EntryPoint Builder.
	 * Parameters for building EntryPoint can be specified via Builder.
	 * <%}%>
	 * 
	 * @return
	 */
	public static EntryPointBuilder builder() {
		return new EntryPointBuilderImpl();
	}
	
	/**
	 * <% if (doclang == "ja") {%>
	 * EntryPointがすでに初期化済みか否かを取得します。
	 * <%} else {%>
	 * Gets whether EntryPoint has already been initialized.
	 * <%}%>
	 * 
	 * @return
	 */
	public static boolean isInited() {
		return EntryPointImpl.isInited();
	}
	
	/**
	 * <% if (doclang == "ja") {%>
	 * iPLAss内でロジックを実行するためのRunnerを取得します。
	 * <%} else {%>
	 * Get Runner to execute logic in iPLAss.
	 * <%}%>
	 * 
	 * @return
	 */
	public Runner runner();
	
	/**
	 * <% if (doclang == "ja") {%>
	 * 言語を指定した状態のRunnerのインスタンスを取得します。
	 * <%} else {%>
	 * Get an instance of Runner with language specified.
	 * <%}%>
	 * 
	 * @param lang
	 * @return
	 */
	public default Runner withLang(String lang) {
		return runner().withLang(lang);
	}
	
	/**
	 * <% if (doclang == "ja") {%>
	 * Credentialでログインした状態のRunnerのインスタンスを取得します。
	 * <%} else {%>
	 * Get an instance of Runner logged in with Credential.
	 * <%}%>
	 * 
	 * @param credential
	 * @return
	 */
	public default Runner withAuth(Credential credential) {
		return runner().withAuth(credential);
	}
	
	/**
	 * <% if (doclang == "ja") {%>
	 * テナントを指定した状態のRunnerのインスタンスを取得します。
	 * <%} else {%>
	 * Get an instance of Runner with tenant specified.
	 * <%}%>
	 * 
	 * @param tenantUrl <%=doclang == 'ja' ? 'テナントURL（テナントのパス）': 'Tenant URL (path of tenant)'%>
	 * @return
	 */
	public default Runner withTenant(String tenantUrl) {
		return runner().withTenant(tenantUrl);
	}
	
	/**
	 * <% if (doclang == "ja") {%>
	 * テナントを指定した状態のRunnerのインスタンスを取得します。
	 * <%} else {%>
	 * Get an instance of Runner with tenant specified.
	 * <%}%>
	 * 
	 * @param tenantId <%=doclang == 'ja' ? 'テナントID': 'Tenant ID'%>
	 * @return
	 */
	public default Runner withTenant(Integer tenantId) {
		return runner().withTenant(tenantId);
	}
	
	/**
	 * <% if (doclang == "ja") {%>
	 * EntryPointを破棄し、iPLAssが管理しているリソースを開放します。
	 * <%} else {%>
	 * Destroy EntryPoint and release resources managed by iPLAss.
	 * <%}%>
	 */
	public void destroy();
	
	/**
	 * <% if (doclang == "ja") {%>
	 * AutoCloseableインタフェースのclose()メソッドの実装です。
	 * destroy()を呼び出します。
	 * <%} else {%>
	 * Implementation of the close() method of the AutoCloseable interface.
	 * Call destroy().
	 * <%}%>
	 * 
	 */
	@Override
	public default void close() {
		destroy();
	}

}
