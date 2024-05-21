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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Function;

import org.iplass.mtp.test.AuthUser;
import org.iplass.mtp.test.Commit;
import org.iplass.mtp.test.ConfigFile;
import org.iplass.mtp.test.NoAuthUser;
import org.iplass.mtp.test.Rollback;
import org.iplass.mtp.test.TenantName;

import groovy.lang.GroovyObject;

/**
 * テスト用設定読み取り機能
 *
 * <p>
 * 設定ファイル、クラス、メソッドからテスト用設定を読み取る。
 * </p>
 *
 * <h3>プロパティファイルによる指定</h3>
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
 * </p>
 *
 * <h3>アノテーションによる指定</h3>
 * <p>
 * テストクラス、メソッドにアノテーションを指定することで設定を指定可能です。
 * 指定可能なアノテーションは以下の通りです。
 *
 * <ul>
 * <li>サービスコンフィグ：{@link org.iplass.mtp.test.ConfigFile}</li>
 * <li>テナント： {@link org.iplass.mtp.test.TenantName}</li>
 * <li>ログイン： {@link org.iplass.mtp.test.AuthUser}, {@link org.iplass.mtp.test.NoAuthUser}</li>
 * <li>トランザクション制御： {@link org.iplass.mtp.test.Commit}, {@link org.iplass.mtp.test.Rollback}</li>
 * </p>
 *
 * @author SEKIGUCHI Naoya
 */
public class MTPTestConfigReader {
	/** テストシステムプロパティ名 */
	public static final String TEST_CONFIG_FILE_NAME_SYSTEM_PROPERTY_NAME = "mtp.test.config";
	/** 設定ファイル名 デフォルト値 */
	public static final String DEFAULT_TEST_CONFIG_FILE_NAME = "/mtptest.properties";

	/** プロパティファイルキー：設定ファイル名 */
	public static final String PROP_CONFIG_FILE_NAME = "configFileName";
	/** プロパティファイルキー：テナント名 */
	public static final String PROP_TENANT_NAME = "tenantName";
	/** プロパティファイルキー：ログインユーザーID */
	public static final String PROP_USER_ID = "userId";
	/** プロパティファイルキー：ログインパスワード */
	public static final String PROP_PASSWORD = "password";
	/** プロパティファイルキー：トランザクションをロールバックするか（true|false の設定を想定） */
	public static final String PROP_ROLLBACK_TRANSACTION = "rollbackTransaction";

	/**
	 * プライベートコンストラクタ
	 */
	private MTPTestConfigReader() {
	}

	/**
	 * テスト設定ファイルを読み取る
	 *
	 * @return テスト設定ファイルの設定情報
	 */
	public static MTPTestConfig read() {
		return createFromFile();
	}

	/**
	 * クラス設定を読み取る
	 *
	 * <p>
	 * 設定情報は親設定と合成され、MTPTestConfig の getter から取得した値が利用する値となる。
	 * </p>
	 *
	 * @param testClass 対象クラス
	 * @param parent メソッド設定が存在しない場合の親設定
	 * @return 親設定とメソッド設定を合成した設定情報
	 */
	public static MTPTestConfig read(Class<?> testClass, MTPTestConfig parent) {
		return createFromClassOrMethod(testClass, parent);
	}

	/**
	 * メソッド設定を読み取る
	 *
	 * <p>
	 * 設定情報は親設定と合成され、MTPTestConfig の getter から取得した値が利用する値となる。
	 * </p>
	 *
	 * @param testMethod 対象メソッド
	 * @param parent メソッド設定が存在しない場合の親設定
	 * @return 親設定とメソッド設定を合成した設定情報
	 */
	public static MTPTestConfig read(Method testMethod, MTPTestConfig parent) {
		return createFromClassOrMethod(testMethod, parent);
	}

	/**
	 * プロパティファイルから設定を読み取る
	 *
	 * <p>
	 * プロパティファイルの設定値は最終的な設定値となるため、Boolean データ型には必ず値を設定する。
	 * </p>
	 *
	 * @param testClass テスト対象クラス
	 * @return テスト設定
	 */
	private static MTPTestConfig createFromFile() {
		String propPath = System.getProperty(TEST_CONFIG_FILE_NAME_SYSTEM_PROPERTY_NAME);
		if (propPath == null) {
			propPath = DEFAULT_TEST_CONFIG_FILE_NAME;
		}

		try (InputStream is = MTPTestConfigReader.class.getResourceAsStream(propPath);
				Reader r = new InputStreamReader(is, StandardCharsets.UTF_8)) {
			Properties prop = new Properties();
			prop.load(r);

			MTPTestConfigImpl config = new MTPTestConfigImpl("FILE");

			// config 初期化
			// ログイン無し： true
			config.setNoAuth(Boolean.TRUE);
			// ロールバック： true
			config.setRollbackTransaction(Boolean.TRUE);

			// プロパティファイルに基づいた設定
			setValueIfNotEmpty((String) prop.get(PROP_CONFIG_FILE_NAME), v -> config.setConfigFileName(v));
			setValueIfNotEmpty((String) prop.get(PROP_TENANT_NAME), v -> config.setTenantName(v));
			setValueIfNotEmpty((String) prop.get(PROP_USER_ID), v -> {
				config.setUserId(v);
				// ユーザーIDが指定されていたら NoAuth は false に変更
				config.setNoAuth(Boolean.FALSE);
			});
			setValueIfNotEmpty((String) prop.get(PROP_PASSWORD), v -> config.setPassword(v));
			setValueIfNotEmpty((String) prop.get(PROP_ROLLBACK_TRANSACTION), v -> config.setRollbackTransaction(Boolean.valueOf(v)));

			// NOTE: 設定を読み込んだ時点では不明なので、groovy 判別は実施しない

			return config;

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * クラスもしくはメソッドから設定情報を読み取る。
	 * @param <T> Class もしくは Methodクラス
	 * @param classOrMethod Class もしくは Method インスタンス
	 * @param parent 親設定
	 * @return テスト設定
	 */
	private static <T extends AnnotatedElement> MTPTestConfig createFromClassOrMethod(T classOrMethod, MTPTestConfig parent) {
		MTPTestConfigImpl config = null == parent ? new MTPTestConfigImpl(classOrMethod) : new MTPTestConfigComposit(classOrMethod, parent);
		boolean isClass = classOrMethod instanceof Class;

		String configFileName = getAnnotationValue(classOrMethod, ConfigFile.class, a -> a.value());
		String tenantName = getAnnotationValue(classOrMethod, TenantName.class, a -> a.value());
		String userId = getAnnotationValue(classOrMethod, AuthUser.class, a -> a.userId());
		String password = getAnnotationValue(classOrMethod, AuthUser.class, a -> a.password());
		String isRollback = getAnnotationValue(classOrMethod, Commit.class, a -> Boolean.FALSE.toString());
		isRollback = getAnnotationValue(classOrMethod, Rollback.class, a -> Boolean.TRUE.toString(), isRollback);
		Boolean isNoAuth = getAnnotationValue(classOrMethod, NoAuthUser.class, a -> Boolean.TRUE);

		setValueIfNotEmpty(configFileName, v -> config.setConfigFileName(v));
		setValueIfNotEmpty(tenantName, v -> config.setTenantName(v));
		setValueIfNotEmpty(userId, v -> {
			config.setUserId(v);
			// ユーザーIDが指定されていたら NoAuth は false に変更
			config.setNoAuth(Boolean.FALSE);
		});
		setValueIfNotEmpty(password, v -> config.setPassword(v));
		setValueIfNotEmpty(isRollback, v -> config.setRollbackTransaction(Boolean.valueOf(v)));
		if (null != isNoAuth) {
			// 仮にユーザーIDが指定されていたとしても、NoAuth が設定されていれば未ログイン状態を優先する
			config.setNoAuth(isNoAuth);
		}

		// 実行しているクラスが groovy クラスかを判別する
		config.setGroovy(isTestClassGroovy(isClass ? (Class<?>) classOrMethod : ((Method) classOrMethod).getDeclaringClass()));

		return config;
	}

	/**
	 * 値が null もしくは空文字以外であれば設定ファンクションを実行する
	 * @param value 値
	 * @param valueSetFn 設定ファンクション
	 */
	private static void setValueIfNotEmpty(String value, Consumer<String> valueSetFn) {
		if (null != value && 0 < value.length()) {
			valueSetFn.accept(value);
		}
	}

	/**
	 * アノテーションの値を取得する
	 * @param <A> アノテーション型
	 * @param <T> アノテーション値データ型
	 * @param elem Class もしくは Method
	 * @param annotation 取得アノテーション
	 * @param valueGetFn アノテーション値取得メソッド
	 * @return アノテーション値
	 */
	private static <A extends Annotation, T> T getAnnotationValue(AnnotatedElement elem, Class<A> annotation, Function<A, T> valueGetFn) {
		return getAnnotationValue(elem, annotation, valueGetFn, null);
	}

	/**
	 * アノテーションの値を取得する
	 * @param <A> アノテーション型
	 * @param <T> アノテーション値データ型
	 * @param elem Class もしくは Method
	 * @param annotation 取得アノテーション
	 * @param valueGetFn アノテーション値取得メソッド
	 * @param defaultValue アノテーション指定がない場合のデフォルト値
	 * @return アノテーション値
	 */
	private static <A extends Annotation, T> T getAnnotationValue(AnnotatedElement elem, Class<A> annotation, Function<A, T> valueGetFn, T defaultValue) {
		A anno = elem.getAnnotation(annotation);
		if (null != anno) {
			return valueGetFn.apply(anno);
		}

		return defaultValue;
	}

	/**
	 * テストクラスが groovy テストクラスであるか判定する
	 * @param testClass 対象クラス
	 * @return groovy テストクラスの場合 true
	 */
	private static Boolean isTestClassGroovy(Class<?> testClass) {
		return Boolean.valueOf(GroovyObject.class.isAssignableFrom(testClass));
	}
}
