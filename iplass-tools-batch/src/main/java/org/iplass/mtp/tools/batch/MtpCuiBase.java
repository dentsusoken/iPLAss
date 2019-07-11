/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.tools.batch;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.iplass.mtp.SystemException;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.config.BootstrapProps;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapterService;
import org.iplass.mtp.impl.rdb.connection.ConnectionFactory;
import org.iplass.mtp.impl.rdb.connection.DriverManagerConnectionFactory;
import org.iplass.mtp.impl.tools.tenant.TenantInfo;
import org.iplass.mtp.impl.tools.tenant.TenantToolService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tools.ToolsBatchResourceBundleUtil;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class MtpCuiBase {

	/** サイレントモードで実行する場合の引数値 */
	public static final String SILENT_MODE = "silent";

	private static Logger logger = LoggerFactory.getLogger(MtpCuiBase.class);

	private final List<LogListner> logListners = new ArrayList<LogListner>();
	private final List<String> logMessage = new ArrayList<String>();

	private boolean isSuccess = false;

	/** 環境情報 */
	private ConfigSetting configSetting;

	/** コンソール出力用 */
	private LogListner consoleLogListner;

	/** Logging出力用 */
	private LogListner loggingLogListner;

	/** 言語(locale名) */
	private String language;

	/** リソースバンドル */
	private ResourceBundle resourceBundle;

	public MtpCuiBase() {
		setupLanguage();
	}

	public List<String> getLogMessage() {
		return logMessage;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void addLogListner(LogListner listner) {
		logListners.add(listner);
	}

	public void removeLogListner(LogListner listner) {
		if (logListners.contains(listner)) {
			logListners.remove(listner);
		}
	}

	public String getLanguage() {
		return language;
	}

	protected void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	protected void clearLog() {
		logMessage.clear();
	}

	protected void logDebug(String message) {
		fireDebubLogMessage(message);
	}
	protected void logInfo(String message) {
		fireInfoLogMessage(message);
		logMessage.add(message);
	}
	protected void logInfo(String message, Throwable e) {
		fireInfoLogMessage(message, e);
		logMessage.add(message);
	}

	protected void logWarn(String message) {
		fireWarnLogMessage(message);
		logMessage.add(message);
	}
	protected void logWarn(String message, Throwable e) {
		fireWarnLogMessage(message, e);
		logMessage.add(message);
	}

	protected void logError(String message) {
		fireErrorLogMessage(message);
		logMessage.add(message);
	}
	protected void logError(String message, Throwable e) {
		fireErrorLogMessage(message, e);
		logMessage.add(message);
	}

	protected ConfigSetting getConfigSetting() {
		if (configSetting != null) {
			return configSetting;
		}

		try {
			//Config FileName
			String configFileName = BootstrapProps.getInstance().getProperty(BootstrapProps.CONFIG_FILE_NAME, BootstrapProps.DEFAULT_CONFIG_FILE_NAME);

			//Rdb Adapter
			RdbAdapterService adapterService = ServiceRegistry.getRegistry().getService(RdbAdapterService.class);
			RdbAdapter adapter = adapterService.getRdbAdapter();

			//Connection Factory
			String conenctUrl = null;
			ConnectionFactory factory = ServiceRegistry.getRegistry().getService(ConnectionFactory.class);
			if (factory instanceof DriverManagerConnectionFactory) {
				DriverManagerConnectionFactory dmFactory = (DriverManagerConnectionFactory) factory;
				conenctUrl = getDriverUrl(dmFactory);
			} else {
				throw new SystemException("unsupport ConnectionFactory class : " + factory.getClass().getName());
			}

			configSetting = new ConfigSetting(configFileName, adapter, conenctUrl);

		} catch (Throwable e) {
			throw new SystemException("failed to get config setting", e);
		}

		return configSetting;
	}

	private String getDriverUrl(DriverManagerConnectionFactory dmFactory) throws Exception {
		//private フィールドなのでリフレクションでセット
		Field urlField = dmFactory.getClass().getDeclaredField("url");
		urlField.setAccessible(true);
		return (String)urlField.get(dmFactory);
	}

	/**
	 * 環境情報を出力します。
	 */
	protected void logEnvironment() {
		ConfigSetting configSetting = getConfigSetting();

		logInfo("-----------------------------------------------------------");
		logInfo("■Environment");
		logInfo("\tconfig file :" + configSetting.getConfigFileName());
		logInfo("\trdb adapter type :" + configSetting.getRdbAdapterName());
		logInfo("\tconnect url :" + configSetting.getConenctUrl());
		logInfo("\tlanguage :" + getLanguage());
		logInfo("-----------------------------------------------------------");
		logInfo("");
	}

	//TODO mainで参照しているところが多いので一旦static
	protected static List<TenantInfo> getValidTenantInfoList() {
		TenantToolService tenantToolService = ServiceRegistry.getRegistry().getService(TenantToolService.class);
		return tenantToolService.getValidTenantInfoList();
	}

	//TODO mainで参照しているところが多いので一旦static
	protected  static List<TenantInfo> getAllTenantInfoList() {
		TenantToolService tenantToolService = ServiceRegistry.getRegistry().getService(TenantToolService.class);
		return tenantToolService.getAllTenantInfoList();
	}

	/**
	 * テナントの一覧を出力します。
	 */
	protected void showAllTenantList() {
		try {
			List<TenantInfo> tenantList = getAllTenantInfoList();
			logInfo("-----------------------------------------------------------");
			logInfo("■Tenant List");
			for (TenantInfo tenant : tenantList) {
				logInfo("[" + tenant.getId() + "] " + tenant.getName());
			}
			logInfo("-----------------------------------------------------------");
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}

	/**
	 * 有効なテナントの一覧を出力します。
	 */
	protected void showValidTenantList() {
		try {
			List<TenantInfo> tenantList = getValidTenantInfoList();
			logInfo("-----------------------------------------------------------");
			logInfo("■Tenant List");
			for (TenantInfo tenant : tenantList) {
				logInfo("[" + tenant.getId() + "] " + tenant.getName());
			}
			logInfo("-----------------------------------------------------------");
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}

	/**
	 * Console出力用LogListner作成Util
	 * @return
	 */
	protected LogListner getConsoleLogListner() {
		if (consoleLogListner != null) {
			return consoleLogListner;
		}
		consoleLogListner = new ConsoleLogListner();

		return consoleLogListner;
	}

	/**
	 * Logging出力用LogListner作成Util
	 * @return
	 */
	protected LogListner getLoggingLogListner() {
		if (loggingLogListner != null) {
			return loggingLogListner;
		}

		loggingLogListner = new LogListner() {

			@Override
			public void debug(String message) {
				logger.debug(message);
			}

			@Override
			public void warn(String message) {
				logger.warn(message);
			}

			@Override
			public void warn(String message, Throwable e) {
				logger.warn(message, e);
			}

			@Override
			public void info(String message) {
				logger.info(message);
			}

			@Override
			public void info(String message, Throwable e) {
				logger.info(message, e);
			}

			@Override
			public void error(String message) {
				logger.error(message);
			}

			@Override
			public void error(String message, Throwable e) {
				logger.error(message, e);
			}

		};

		return loggingLogListner;
	}

	/**
	 * Consoleから入力を受け取ります。
	 *
	 * @param message メッセージ
	 * @return 入力結果
	 */
	protected String readConsole(String message) {
		Console console = System.console();
		if (console != null) {
			return console.readLine("%s%n", message + " : ");
		} else {
			//EclipseではConsoleがnull
			System.out.println(message + " : ");
			InputStreamReader isr = null;
			BufferedReader br = null;
			try {
				isr = new InputStreamReader(System.in);
				br = new BufferedReader(isr);
				return br.readLine();
			} catch (IOException e) {
				throw new SystemException(e);
			}
		}
	}

	/**
	 * ConsoleからTrueFalseの入力を受け取ります。
	 *
	 * @param message メッセージ
	 * @param retDefault 空の場合のデフォルト値
	 * @return 入力結果
	 */
	protected boolean readConsoleBoolean(String message, boolean retDefault) {
		String answerStr = readConsole(message + " [yes/no](" + (retDefault ? "yes" : "no") + ")");
		if (StringUtil.isEmpty(answerStr)) {
			return retDefault;
		}
		boolean answer = false;
		try {
			answer = answerStr.equalsIgnoreCase("yes") || Boolean.parseBoolean(answerStr);
		} catch (Exception e) {
		}
		return answer;
	}

	/**
	 * ConsoleからIntegerの入力を受け取ります。
	 *
	 * @param message メッセージ
	 * @param retDefault 空の場合のデフォルト値
	 * @return 入力結果
	 */
	protected int readConsoleInteger(String message, int retDefault) {
		String answerStr = readConsole(message + "(" + retDefault + ")");
		if (StringUtil.isEmpty(answerStr)) {
			return retDefault;
		}
		int answer = retDefault;
		try {
			answer = Integer.parseInt(answerStr);
		} catch (Exception e) {
		}
		return answer;
	}

	/**
	 * Consoleからパスワード入力を受け取ります。
	 *
	 * @param message メッセージ
	 * @return 入力結果
	 */
	protected String readConsolePassword(String message) {
		Console console = System.console();
		if (console != null) {
			char[] password = console.readPassword("%s%n", message + " : ");
			return new String(password);
		} else {
			//EclipseではConsoleがnullなので普通の入力にする
			return readConsole(message);
		}
	}

	private void fireDebubLogMessage(String message) {
		for (LogListner listner : logListners) {
			listner.debug(message);
		}
	}
	private void fireInfoLogMessage(String message) {
		for (LogListner listner : logListners) {
			listner.info(message);
		}
	}
	private void fireInfoLogMessage(String message, Throwable e) {
		for (LogListner listner : logListners) {
			listner.info(message, e);
		}
	}

	private void fireWarnLogMessage(String message) {
		for (LogListner listner : logListners) {
			listner.warn(message);
		}
	}
	private void fireWarnLogMessage(String message, Throwable e) {
		for (LogListner listner : logListners) {
			listner.warn(message, e);
		}
	}

	private void fireErrorLogMessage(String message) {
		for (LogListner listner : logListners) {
			listner.error(message);
		}
	}
	private void fireErrorLogMessage(String message, Throwable e) {
		for (LogListner listner : logListners) {
			listner.error(message, e);
		}
	}

	private void setupLanguage() {

		language = ToolsBatchResourceBundleUtil.getLanguage();
		resourceBundle = ToolsBatchResourceBundleUtil.getResourceBundle(language);

		ExecuteContext context = ExecuteContext.getCurrentContext();
		context.setLanguage(language);
	}

	/**
	 * メッセージを返します。
	 *
	 * @param key メッセージKEY
	 * @param args 引数
	 * @return メッセージ
	 */
	protected String rs(String key, Object... args) {
		return ToolsBatchResourceBundleUtil.resourceString(resourceBundle, key, args);
	}

	public interface LogListner {
		default void debug(String message) {};
		void info(String message);
		void info(String message, Throwable e);
		void warn(String message);
		void warn(String message, Throwable e);
		void error(String message);
		void error(String message, Throwable e);
	}

	private class ConsoleLogListner implements LogListner {

		private Console console;

		public ConsoleLogListner() {
			console = System.console();
		}

		@Override
		public void warn(String message) {
			if (console != null) {
				console.printf("[WARN]%s%n",message);
			} else {
				//EclipseではConsoleがnull
				System.out.println("[WARN]" + message);
			}
		}

		@Override
		public void warn(String message, Throwable e) {
			warn(message);
		}

		@Override
		public void info(String message) {
			if (console != null) {
				console.printf("%s%n",message);
			} else {
				//EclipseではConsoleがnull
				System.out.println(message);
			}
		}

		@Override
		public void info(String message, Throwable e) {
			info(message);
		}

		@Override
		public void error(String message) {
			if (console != null) {
				console.printf("[ERROR]%s%n", message);
			} else {
				//EclipseではConsoleがnull
				System.out.println("[ERROR]" + message);
			}
		}

		@Override
		public void error(String message, Throwable e) {
			error(message);
		}

	}

}
