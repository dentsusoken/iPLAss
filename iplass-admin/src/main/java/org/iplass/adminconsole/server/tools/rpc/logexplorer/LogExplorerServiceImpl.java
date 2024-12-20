/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.iplass.adminconsole.server.base.rpc.util.AuthUtil;
import org.iplass.adminconsole.server.base.service.AdminConsoleService;
import org.iplass.adminconsole.shared.tools.dto.logexplorer.LogConditionInfo;
import org.iplass.adminconsole.shared.tools.dto.logexplorer.LogFile;
import org.iplass.adminconsole.shared.tools.dto.logexplorer.LogFileCondition;
import org.iplass.adminconsole.shared.tools.rpc.logexplorer.LogExplorerService;
import org.iplass.mtp.MtpException;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.logging.LogCondition;
import org.iplass.mtp.impl.logging.LoggingContext;
import org.iplass.mtp.impl.logging.LoggingService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.DateUtil;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.web.template.TemplateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.server.rpc.jakarta.XsrfProtectedServiceServlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;

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
	 * @param logFileCondition ログファイル検索条件
	 */
	@Override
	public List<LogFile> getLogfileNames(int tenantId, LogFileCondition logFileCondition) {

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId,
				new AuthUtil.Callable<List<LogFile>>() {

					@Override
					public List<LogFile> call() {
						return searchLogFile(logFileCondition);
					}
				});
	}

	private List<LogFile> searchLogFile(final LogFileCondition logFileCondition) {
		//ログの取得不可チェック
		if (!acs.isLogDownloadEnabled()) {
			logger.debug("LogService is disabled.");
			return Collections.emptyList();
		}

		List<String> logHomes = acs.getTenantLogHomes(initLogHome);
		List<String> filters = acs.getTenantLogFileFilters();
		List<Pattern> filterPatterns = filters.stream().map(Pattern::compile).collect(Collectors.toList());

		List<LogFile> logFiles = new ArrayList<>();

		DateFormat dateFormat = DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getOutputDatetimeSecFormat(), true);
		for (String logHome : logHomes) {
			logFiles.addAll(searchLogFile(logHome, filterPatterns, dateFormat));
		}

		if (logFiles.isEmpty()) {
			logger.debug("log file is not found. log home="
					+ ToStringBuilder.reflectionToString(logHomes.toArray(new String[] {}), ToStringStyle.SIMPLE_STYLE));
		} else {
			// 画面上のFilter条件を適用
			try {
				final Pattern fileNamePattern = StringUtil.isNotEmpty(logFileCondition.getFileName())
						? Pattern.compile(logFileCondition.getFileName())
						: null;
				final Pattern lastModifiedPattern = StringUtil.isNotEmpty(logFileCondition.getLastModified())
						? Pattern.compile(logFileCondition.getLastModified())
						: null;

				logFiles = logFiles.stream()
						.filter(logFile -> {
							if (fileNamePattern != null) {
								Matcher matcher = fileNamePattern.matcher(logFile.getFileName());
								return matcher.find();
							}
							return true;
						}).filter(logFile -> {
							if (lastModifiedPattern != null) {
								Matcher matcher = lastModifiedPattern.matcher(logFile.getLastModified());
								return matcher.find();
							}
							return true;
						}).sorted(Comparator.comparing((LogFile logFile) -> logFile.getFileName()))
						.collect(Collectors.toList());
			} catch (PatternSyntaxException e) {
				logger.warn("log file filter pattern is invalid. LogFileCondition=" + logFileCondition.getFileName() + ",LastModifiedCondition="
						+ logFileCondition.getLastModified());
				return Collections.emptyList();
			}
		}
		return logFiles;
	}

	/**
	 * <p>ファイル検索</p>
	 *
	 * @param home RootとなるHOMEパス
	 * @param filterPatterns ファイルに対するFilter
	 * @param dateFormat 最終更新日時変換用Format
	 * @return
	 */
	private List<LogFile> searchLogFile(String home, List<Pattern> filterPatterns, DateFormat dateFormat) {

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
				if (path.equals("*")) {
					break;
				}
				rootPath += path + "/";
			}
			return searchDynamicLogFile(rootPath, homePaths, index, rootPath, filterPatterns, dateFormat);
		} else {
			//*が含まれない場合は単純に検索(こっちのほうが無駄な処理はない)
			return searchStaticLogFile(home, home, filterPatterns, dateFormat);
		}
	}

	/**
	 * <p>固定ルートファイル検索</p>
	 *
	 * @param home RootとなるHOMEパス
	 * @param path 検索対象パス
	 * @param filterPatterns ファイル名に対するFilter
	 * @param dateFormat 最終更新日時変換用Format
	 * @return
	 */
	private List<LogFile> searchStaticLogFile(String home, String path, List<Pattern> filterPatterns, DateFormat dateFormat) {

		List<LogFile> dirList = new ArrayList<>();
		List<LogFile> fileList = new ArrayList<>();

		File logsDir = new File(path);
		if (logsDir.exists() && logsDir.isDirectory()) {
			File[] logs = logsDir.listFiles();
			for (File f : logs) {
				if (f.isDirectory()) {
					dirList.addAll(searchStaticLogFile(home, f.getPath(), filterPatterns, dateFormat));
				} else {
					String checkName = getFileName(home, f);
					if (!filterPatterns.isEmpty()) {
						// Filterチェック
						for (Pattern filter : filterPatterns) {
							if (filter.matcher(checkName).matches()) {
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
	 * @param filterPatterns ファイル名に対するFilter
	 * @param dateFormat 最終更新日時変換用Format
	 * @return
	 */
	private List<LogFile> searchDynamicLogFile(String fixedPath, String[] homePaths, int index, String path, List<Pattern> filterPatterns,
			DateFormat dateFormat) {

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
					dirList.addAll(searchDynamicLogFile(fixedPath, homePaths, index + 1, f.getPath(), filterPatterns, dateFormat));
				} else {
					if (index < homePaths.length) {
						//階層として、対象外
						continue;
					}
					if (!filterPatterns.isEmpty()) {
						// Filterチェック
						for (Pattern filter : filterPatterns) {
							if (filter.matcher(f.getName()).matches()) {
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

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId,
				new AuthUtil.Callable<List<LogConditionInfo>>() {

					@Override
					public List<LogConditionInfo> call() {
						ExecuteContext ec = ExecuteContext.getCurrentContext();
						LoggingContext lc = ls.getLoggingContext(ec.getTenantContext());
						List<LogCondition> conditions = lc.list();
						return convertFrom(conditions);
					}
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
	public String applyLogConditions(int tenantId, List<LogConditionInfo> logConditions) {

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId,
				new AuthUtil.Callable<String>() {

					@Override
					public String call() {
						List<LogCondition> conditions = null;
						if (logConditions != null) {
							conditions = convertTo(logConditions);
						}

						try {
							ExecuteContext ec = ExecuteContext.getCurrentContext();
							LoggingContext lc = ls.getLoggingContext(ec.getTenantContext());
							lc.apply(conditions);
						} catch (MtpException e) {
							return e.getMessage();
						}

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
