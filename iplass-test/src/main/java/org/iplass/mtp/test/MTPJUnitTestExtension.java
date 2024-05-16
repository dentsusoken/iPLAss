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
package org.iplass.mtp.test;

import java.lang.reflect.Method;

import org.iplass.mtp.test.impl.test.MTPTestConfig;
import org.iplass.mtp.test.impl.test.MTPTestConfigReader;
import org.iplass.mtp.test.impl.test.MTPTestInvoker;
import org.iplass.mtp.test.impl.test.MTPTestInvokerDecorator;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;

/**
 * <p>
 * JUnitに単体テスト用のユーティリティ処理を組み込むための JUnit5 Extension です。
 * </p>
 * <p>
 * MTPJUnitTestExtension を組み込むことにより、容易な方法により、iPLAssの設定ファイルの指定、テナント、テストユーザーの設定、トランザクション制御を行うことが可能です。
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
 * {@literal @}ExtendWith( MTPJUnitTestExtension.class )
 * public class SampleJavaTest {
 *   {@literal @}org.junit.jupiter.api.Test
 *   public void testHogeHoge() throws IOException {
 *     //do test...
 *     :
 *     :
 *   }
 *
 *   {@literal @}org.junit.jupiter.api.Test
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
 * @author N.Sekiguchi
 */
public class MTPJUnitTestExtension implements InvocationInterceptor, BeforeAllCallback {
	/** */
	private MTPTestConfig fileAndClassConfig;

	@Override
	public void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext)
			throws Throwable {

		MTPTestConfig methodconConfig = MTPTestConfigReader.read(invocationContext.getExecutable(), fileAndClassConfig);

		MTPTestInvoker<Void> test = MTPTestInvokerDecorator.decorate(config -> {
			invocation.proceed();
			return null;
		});

		// テスト実行
		test.invoke(methodconConfig);
	}

	@Override
	public void beforeAll(ExtensionContext context) throws Exception {
		Class<?> testClass = context.getTestClass().get();
		fileAndClassConfig = MTPTestConfigReader.read(testClass);
	}
}
