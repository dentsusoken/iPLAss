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

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.WrongMethodTypeException;
import java.lang.reflect.Constructor;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.CommandInvoker;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.script.GroovyScriptEngine;
import org.iplass.mtp.transaction.Transaction;

/**
 * iPLAssの単体テストを記述する上でのユーティリティクラスです。
 * 
 * 
 * 
 * @author K.Higuchi
 *
 */
public class MTPTest {
	static {
		System.setProperty(ManagerLocator.MANAGER_LOCATOR_SYSTEM_PROPERTY_NAME, TestManagerLocator.class.getName());
	}

	/**
	 * ManagerLocator/ServiceLocatorで取得する各Managerクラスを指定のmockで置き換えます。
	 * モックされた各Managerクラスは、テストメソッド単位でクリアされます。
	 * 
	 * @param managerInterface モックしたいManagerのインタフェース
	 * @param mock 置き換えるモックのインスタンス
	 */
	public static <T> void setManagerMock(Class<T> managerInterface, T mock) {
		((TestManagerLocator) ManagerLocator.getInstance()).setManager(managerInterface, mock);
	}
	
	/**
	 * 明示的に、現在設定されているモックをクリアします。
	 */
	public static void resetManagerMock() {
		((TestManagerLocator) ManagerLocator.getInstance()).reset();
	}
	
	/**
	 * commandNameで定義されているCommandを実行します。
	 * 実行する際には、トランザクションは自動で開始されます。
	 * 
	 * @param commandName
	 * @param request
	 * @return
	 */
	public static String invokeCommand(String commandName, RequestContext request) {
		return ManagerLocator.getInstance().getManager(CommandInvoker.class).execute(commandName, request);
	}
	
	/**
	 * commandNameで定義されているCommandのインスタンスを取得します。
	 * 
	 * @param commandName
	 * @return
	 */
	public static Command newCommand(String commandName) {
		return ManagerLocator.getInstance().getManager(CommandInvoker.class).getCommandInstance(commandName);
	}
	
	/**
	 * cmdで指定されるCommandのインスタンスを実行します。
	 * 実行する際には、トランザクションは自動で開始されます。
	 * 
	 * @param cmd
	 * @param request
	 * @return
	 */
	public static String invokeCommand(Command cmd, RequestContext request) {
		return ManagerLocator.getInstance().getManager(CommandInvoker.class).execute(cmd, request);
	}
	
	/**
	 * runnableの処理をトランザクション内で実行します。
	 * 
	 * @param runnable
	 */
	public static void transaction(Runnable runnable) {
		Transaction.required(t -> {
			runnable.run();
		});
	}
	
	/**
	 * 指定のnameで定義されるUtilityClassのクラスを取得します。
	 * 
	 * @param name
	 * @return
	 */
	public static Class<?> getUtilityClass(String name) {
		ExecuteContext ec = ExecuteContext.getCurrentContext();
		GroovyScriptEngine gse = (GroovyScriptEngine) ec.getTenantContext().getScriptEngine();
		try {
			return Class.forName(name, true, gse.getSharedClassLoader());
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("can't load Utility class:" + name, e);
		}
	}
	
	/**
	 * 指定のnameで定義されるUtilityClassのインスタンスを生成します。
	 * コンストラクタはデフォルトコンストラクタが呼び出されます。
	 * 
	 * @param name
	 * @return
	 */
	public static Object newUC(String name) {
		ExecuteContext ec = ExecuteContext.getCurrentContext();
		GroovyScriptEngine gse = (GroovyScriptEngine) ec.getTenantContext().getScriptEngine();
		
		try {
			Class<?> clazz = Class.forName(name, true, gse.getSharedClassLoader());
			return clazz.newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			throw new IllegalArgumentException("can't create Utility class instance:" + name, e);
		}
	}
	
	/**
	 * 指定のnameで定義されるUtilityClassのインスタンスを生成します。
	 * コンストラクタはargsで指定される引数に合わせたコンストラクタが呼び出されます。
	 * 
	 * @param name
	 * @param args
	 * @return
	 */
	public static Object newUC(String name, Object... args) {
		ExecuteContext ec = ExecuteContext.getCurrentContext();
		GroovyScriptEngine gse = (GroovyScriptEngine) ec.getTenantContext().getScriptEngine();
		
		Class<?> clazz;
		try {
			clazz = Class.forName(name, true, gse.getSharedClassLoader());
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("can't load Utility class:" + name, e);
		}
		
		if (args == null || args.length == 0) {
			args = new Object[1];
		}
		
		Lookup lookup = MethodHandles.lookup();
		Exception ee = null;
		for (Constructor<?> c: clazz.getConstructors()) {
			if (c.getParameterTypes().length == args.length) {
				try {
					MethodHandle constructor = lookup.unreflectConstructor(c);
					return constructor.invokeWithArguments(args);
				} catch (WrongMethodTypeException e) {
					ee = e;
				} catch (ClassCastException e) {
					ee = e;
				} catch (IllegalAccessException e) {
					throw new IllegalArgumentException("can't create Utility class instance:" + name, e);
				} catch (Throwable e) {
					throw new IllegalArgumentException("can't create Utility class instance:" + name, e);
				}
			}
		}
		
		throw new IllegalArgumentException("can't create Utility class instance:" + name, ee);
			
	}

}
