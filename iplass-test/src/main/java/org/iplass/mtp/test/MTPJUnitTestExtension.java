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
import java.util.ArrayDeque;
import java.util.Deque;

import org.iplass.mtp.test.impl.test.MTPTestConfig;
import org.iplass.mtp.test.impl.test.MTPTestConfigReader;
import org.iplass.mtp.test.impl.test.MTPTestInvoker;
import org.iplass.mtp.test.impl.test.MTPTestInvokerDecorator;
import org.junit.jupiter.api.extension.AfterAllCallback;
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
 * <dt>クラスレベルアノテーションによる指定</dt><dd>当該クラスに限り設定が適用されます。ネストクラスに同一設定が存在した場合は、ネストクラスの設定が優先されます。</dd>
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
 * class SampleJavaTest {
 *   {@literal @}org.junit.jupiter.api.Test
 *   public void testDemo1Tenant() throws IOException {
 *     //do test...
 *     :
 *     :
 *   }
 *
 *   {@literal @}TenantName("testDemo2")
 *   {@literal @}AuthUser(userId="test2", password="testtest")
 *   {@literal @}Nested
 *   class NestTest {
 *     {@literal @}org.junit.jupiter.api.Test
 *     public void testDemo2Tenant() throws IOException {
 *       //do test as tenant:testDemo2 user:test2 ...
 *       :
 *       :
 *     }
 *
 *     {@literal @}NoAuthUser
 *     {@literal @}org.junit.jupiter.api.Test
 *     public void testDemo2TenantNoAuth() throws IOException {
 *       //do test as tenant:testDemo2 no auth ...
 *       :
 *       :
 *     }
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
 * <p>
 * Extension はテストファイル単位で作成される。本クラスでは、設定をテストファイル単位でスタックに格納し、テスト実行時に利用する。
 * </p>
 *
 * @author N.Sekiguchi
 */
public class MTPJUnitTestExtension implements BeforeAllCallback, AfterAllCallback, InvocationInterceptor {
	/** 設定スタック */
	private Deque<MTPTestConfig> configStack = new ArrayDeque<MTPTestConfig>();

	public MTPJUnitTestExtension() {
		// 設定ファイルの情報を読み込み、スタックに格納
		configStack.push(MTPTestConfigReader.read());
	}

	@Override
	public void beforeAll(ExtensionContext context) throws Exception {
		// テストクラスの @BeforeAll アノテーションメソッドの前に実行される。テストクラスに @BeforeAll が無くても実行される。

		// スタックの最終位置を取得
		MTPTestConfig parent = configStack.peek();
		// クラス設定情報をスタックに格納
		configStack.push(MTPTestConfigReader.read(context.getTestClass().get(), parent));
	}

	@Override
	public void afterAll(ExtensionContext context) throws Exception {
		// テストクラスの @AfterAll アノテーションメソッドの後に実行される。テストクラスに @AfterAll が無くても実行される。

		// スタックからポップ
		configStack.pop();
	}

	@Override
	public void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext)
			throws Throwable {
		// テストメソッド実行前に実行される

		// メソッド設定を読み取る
		MTPTestConfig parent = configStack.peek();
		MTPTestConfig testConfig = MTPTestConfigReader.read(invocationContext.getExecutable(), parent);

		MTPTestInvoker<Void> decoratedInvoker = MTPTestInvokerDecorator.decorate(config -> {
			invocation.proceed();
			return null;
		});

		// テスト実行
		decoratedInvoker.invoke(testConfig);
	}
}
