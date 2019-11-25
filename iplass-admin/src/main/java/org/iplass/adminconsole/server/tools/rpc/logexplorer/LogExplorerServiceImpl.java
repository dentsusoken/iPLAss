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

package org.iplass.adminconsole.server.tools.rpc.logexplorer;

import java.io.File;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.iplass.adminconsole.server.base.rpc.util.AuthUtil;
import org.iplass.adminconsole.server.base.service.AdminConsoleService;
import org.iplass.adminconsole.shared.tools.dto.logexplorer.LogConditionInfo;
import org.iplass.adminconsole.shared.tools.dto.logexplorer.LogFile;
import org.iplass.adminconsole.shared.tools.rpc.logexplorer.LogExplorerService;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.logging.LogCondition;
import org.iplass.mtp.impl.logging.LoggingContext;
import org.iplass.mtp.impl.logging.LoggingService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.DateUtil;
import org.iplass.mtp.web.template.TemplateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.server.rpc.XsrfProtectedServiceServlet;

public class LogExplorerServiceImpl extends XsrfProtectedServiceServlet implements LogExplorerService {

	private static final long serialVersionUID = -846200535030463112L;

	private static final Logger logger = LoggerFactory.getLogger(LogExplorerServiceImpl.class);

	/** web.xmlで指定されたLOG_HOME（service-configで指定されていない場合に利用） */
	private String initLogHome;

	private AdminConsoleService acs = ServiceRegistry.getRegistry().getService(AdminConsoleService.class);
	private LoggingService ls = ServiceRegistry.getRegistry().getService(LoggingService.class);

	@Override
	public void init() throws ServletException {
		super.init();

		//configからLOGHOMEを取得
		ServletConfig servletConfig = getServletConfig();
		String loghome = servletConfig.getInitParameter("loghome");
		if (loghome != null) {
			this.initLogHome = loghome;
		}
	}

	/**
	 * ログファイル名を返します。
	 *
	 * @param tenantId テナントID
	 */
	@Override
	public List<LogFile> getLogfileNames(int tenantId) {

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<List<LogFile>>() {

			@Override
			public List<LogFile> call() {
				return searchLogFile();
			}
		});
	}

	private List<LogFile> searchLogFile() {
		//ログの取得不可チェック
		if (!acs.isLogDownloadEnabled()) {
			logger.debug("LogService is disabled.");
			return Collections.emptyList();
		}

		List<String> logHomes = acs.getTenantLogHomes(initLogHome);
		List<String> filters = acs.getTenantLogFileFilters();

		List<LogFile> logFiles = new ArrayList<>();

		DateFormat dateFormat = DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getOutputDatetimeSecFormat(), true);
		for (String logHome : logHomes) {
			logFiles.addAll(searchLogFile(logHome, filters, dateFormat));
		}

		if (logFiles.isEmpty()) {
			logger.debug("log file is not found. log home=" + ToStringBuilder.reflectionToString(logHomes.toArray(new String[]{}), ToStringStyle.SIMPLE_STYLE));
		} else {
			Collections.sort(logFiles, new Comparator<LogFile>() {
				@Override
				public int compare(LogFile o1, LogFile o2) {
					return o1.getFileName().compareTo(o2.getFileName());
				}
			});
		}

		return logFiles;
	}

	/**
	 * <p>ファイル検索</p>
	 *
	 * @param home RootとなるHOMEパス
	 * @param fileFilters ファイルに対するFilter
	 * @param dateFormat 最終更新日時変換用Format
	 * @return
	 */
	private List<LogFile> searchLogFile(String home, List<String> fileFilters, DateFormat dateFormat) {

		if (home.contains("/*/")) {
			//*を含む場合は、パスを分解。固定部分の抽出
			String homePaths[] = home.split("/");
			String rootPath = "/";
			int index = -1;
			for (String path : homePaths) {
				index++;
				if (path.isEmpty()) {
					continue;
				}
				if (path.equals("*")){
					break;
				}
				rootPath += path + "/";
			}
			return searchDynamicLogFile(rootPath, homePaths, index, rootPath, fileFilters, dateFormat);
		} else {
			//*が含まれない場合は単純に検索(こっちのほうが無駄な処理はない)
			return searchStaticLogFile(home, home, fileFilters, dateFormat);
		}
	}


	/**
	 * <p>固定ルートファイル検索</p>
	 *
	 * @param home RootとなるHOMEパス
	 * @param path 検索対象パス
	 * @param fileFilters ファイル名に対するFilter
	 * @param dateFormat 最終更新日時変換用Format
	 * @return
	 */
	private List<LogFile> searchStaticLogFile(String home, String path, List<String> fileFilters, DateFormat dateFormat) {

		List<LogFile> dirList = new ArrayList<>();
		List<LogFile> fileList = new ArrayList<>();

		File logsDir = new File(path);
		if (logsDir.exists() && logsDir.isDirectory()) {
			File[] logs = logsDir.listFiles();
			for (File f : logs) {
				if (f.isDirectory()) {
					dirList.addAll(searchStaticLogFile(home, f.getPath(), fileFilters, dateFormat));
				} else {
					String checkName = getFileName(home, f);
					if (fileFilters != null) {
						// Filterチェック
						for (String filter : fileFilters) {
							if (checkName.matches(filter)) {
								LogFile info = new LogFile();
								info.setPath(f.getPath());
								info.setFileName(checkName);
								info.setLastModified(dateFormat.format(new Timestamp(f.lastModified())));
								info.setSize(f.length());
								fileList.add(info);
								break;
							}
						}
					} else {
						LogFile info = new LogFile();
						info.setPath(f.getPath());
						info.setFileName(checkName);
						info.setLastModified(dateFormat.format(new Timestamp(f.lastModified())));
						info.setSize(f.length());
						fileList.add(info);
					}
				}
			}
		} else {
			logger.debug("either logsDir doesn't exist or is not a folder. path=" + path);
		}

		// directy->fileの順番で
		List<LogFile> list = new ArrayList<>(dirList.size() + fileList.size());
		list.addAll(dirList);
		list.addAll(fileList);

		return list;
	}

	/**
	 * <p>動的ルートファイル検索</p>
	 *
	 * @param fixedPath 固定ルートパス
	 * @param homePaths HOMEを/で区切ったパス
	 * @param index     現在のindex
	 * @param path      検索対象パス
	 * @param fileFilters ファイル名に対するFilter
	 * @param dateFormat 最終更新日時変換用Format
	 * @return
	 */
	private List<LogFile> searchDynamicLogFile(String fixedPath, String[] homePaths, int index, String path, List<String> fileFilters, DateFormat dateFormat) {

		List<LogFile> dirList = new ArrayList<>();
		List<LogFile> fileList = new ArrayList<>();

		File logsDir = new File(path);
		if (logsDir.exists() && logsDir.isDirectory()) {
			File[] logs = logsDir.listFiles();
			for (File f : logs) {
				if (f.isDirectory()) {
					if (index < homePaths.length) {
						//フォルダ名チェック
						if (!homePaths[index].equals("*")) {
							if (!f.getName().equals(homePaths[index])) {
								//対象外
								continue;
							}
						}
					}
					dirList.addAll(searchDynamicLogFile(fixedPath, homePaths, index+1, f.getPath(), fileFilters, dateFormat));
				} else {
					if (index < homePaths.length) {
						//階層として、対象外
						continue;
					}
					if (fileFilters != null) {
						// Filterチェック
						for (String filter : fileFilters) {
							if (f.getName().matches(filter)) {
								LogFile info = new LogFile();
								info.setPath(f.getPath());
								//info.setFileName(f.getName());
								info.setFileName(getFileName(fixedPath, f));
								info.setLastModified(dateFormat.format(new Timestamp(f.lastModified())));
								info.setSize(f.length());
								fileList.add(info);
								break;
							}
						}
					} else {
						LogFile info = new LogFile();
						info.setPath(f.getPath());
						info.setFileName(getFileName(fixedPath, f));
						info.setLastModified(dateFormat.format(new Timestamp(f.lastModified())));
						info.setSize(f.length());
						fileList.add(info);
					}
				}
			}
		} else {
			logger.debug("either logsDir doesn't exist or is not a folder. path=" + path);
		}

		// directy->fileの順番で
		List<LogFile> list = new ArrayList<>(dirList.size() + fileList.size());
		list.addAll(dirList);
		list.addAll(fileList);

		return list;
	}

	/**
	 * <p>Pathから固定部分を除いたパスを取得</p>
	 *
	 * @param hidePrefixPath 除外するパス
	 * @param file ログファイル
	 * @return
	 */
	private String getFileName(String hidePrefixPath, File file) {
		//Fileのpathからhomeを除去
		String path = file.getPath().replaceAll("\\\\", "/");
		if (path.startsWith(hidePrefixPath)) {
			return path.substring(hidePrefixPath.length());
		}
		return path;
	}

	@Override
	public List<LogConditionInfo> getLogConditions(int tenantId) {

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<List<LogConditionInfo>>() {

			@Override
			public List<LogConditionInfo> call() {
				ExecuteContext ec = ExecuteContext.getCurrentContext();
				LoggingContext lc = ls.getLoggingContext(ec.getTenantContext());
				List<LogCondition> conditions = lc.list();
				return convertFrom(conditions);
			};
		});
	}

	private List<LogConditionInfo> convertFrom(List<LogCondition> conditions) {
		if (conditions == null) {
			return null;
		}
		List<LogConditionInfo> infos = new ArrayList<>();
		for (LogCondition condition : conditions) {
			LogConditionInfo info = new LogConditionInfo();
			info.setLevel(condition.getLevel());
			info.setExpiresAt(condition.getExpiresAt());
			info.setCondition(condition.getCondition());
			info.setLoggerNamePattern(condition.getLoggerNamePattern());
			infos.add(info);
		}
		return infos;
	}

	@Override
	public void applyLogConditions(int tenantId, List<LogConditionInfo> logConditions) {

		AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<Void>() {

			@Override
			public Void call() {
				List<LogCondition> conditions = null;
				if (logConditions != null) {
					conditions = convertTo(logConditions);
				}

				ExecuteContext ec = ExecuteContext.getCurrentContext();
				LoggingContext lc = ls.getLoggingContext(ec.getTenantContext());
				lc.apply(conditions);

				return null;
			}

		});

	}

	private List<LogCondition> convertTo(List<LogConditionInfo> infos) {
		if (infos == null) {
			return null;
		}
		List<LogCondition> conditions = new ArrayList<>();
		for (LogConditionInfo info : infos) {
			LogCondition condition = new LogCondition();
			condition.setLevel(info.getLevel());
			condition.setExpiresAt(info.getExpiresAt());
			condition.setCondition(info.getCondition());
			condition.setLoggerNamePattern(info.getLoggerNamePattern());
			conditions.add(condition);
		}
		return conditions;
	}
}
