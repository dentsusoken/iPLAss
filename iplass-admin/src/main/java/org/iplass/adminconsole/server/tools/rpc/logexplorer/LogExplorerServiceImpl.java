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

		// 画面上の入力Filter条件を取得
		Pattern fileNamePattern = null;
		Pattern lastModifiedPattern = null;
		try {
			fileNamePattern = StringUtil.isNotEmpty(logFileCondition.getFileName())
					? Pattern.compile(logFileCondition.getFileName())
					: null;
			lastModifiedPattern = StringUtil.isNotEmpty(logFileCondition.getLastModified())
					? Pattern.compile(logFileCondition.getLastModified())
					: null;
		} catch (PatternSyntaxException e) {
			logger.warn("log file filter pattern is invalid. LogFileCondition=" + logFileCondition.getFileName() + ",LastModifiedCondition="
					+ logFileCondition.getLastModified());
			return Collections.emptyList();
		}

		List<LogFile> logFiles = new ArrayList<>();

		DateFormat dateFormat = DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getOutputDatetimeSecFormat(), true);
		for (String logHome : logHomes) {
			logFiles.addAll(searchLogFile(logHome, filterPatterns, fileNamePattern, lastModifiedPattern,
					logFiles.size(), logFileCondition.getLimit(), dateFormat, logHomes.size() > 1));

			// checkLimit
			if (logFileCondition.getLimit() > 0 && logFiles.size() >= logFileCondition.getLimit()) {
				break;
			}
		}

		if (logFiles.isEmpty()) {
			logger.debug("log file is not found. log home="
					+ ToStringBuilder.reflectionToString(logHomes.toArray(new String[] {}), ToStringStyle.SIMPLE_STYLE));
		} else {
			logFiles = logFiles.stream()
					.sorted(Comparator.comparing((LogFile logFile) -> logFile.getFileName()))
					.collect(Collectors.toList());
		}
		return logFiles;
	}

	/**
	 * <p>ファイル検索</p>
	 *
	 * @param home RootとなるHOMEパス
	 * @param filterPatterns ファイル名に対するconfig定義のFilter
	 * @param fileNamePattern ファイル名に対するFilter
	 * @param lastModifiedPattern 最終更新日時に対するFilter
	 * @param count 現在の対象ファイル数
	 * @param limit 取得上限数
	 * @param dateFormat 最終更新日時変換用Format
	 * @param multiHomes Logホームが複数設定されているか
	 * @return ログファイル情報
	 */
	private List<LogFile> searchLogFile(String home, List<Pattern> filterPatterns,
			Pattern fileNamePattern, Pattern lastModifiedPattern, int count, int limit, DateFormat dateFormat, boolean multiHomes) {

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
			return searchDynamicLogFile(rootPath, homePaths, index, rootPath, filterPatterns,
					fileNamePattern, lastModifiedPattern, count, limit, dateFormat, multiHomes);
		} else {
			// *が含まれない場合は単純に検索(こっちのほうが無駄な処理はない)
			return searchStaticLogFile(home, home, filterPatterns, fileNamePattern, lastModifiedPattern, count, limit, dateFormat, multiHomes);
		}
	}

	/**
	 * <p>固定ルートファイル検索</p>
	 *
	 * @param home RootとなるHOMEパス
	 * @param path 検索対象パス
	 * @param filterPatterns ファイル名に対するconfig定義のFilter
	 * @param fileNamePattern ファイル名に対するFilter
	 * @param lastModifiedPattern 最終更新日時に対するFilter
	 * @param count 現在の対象ファイル数
	 * @param limit 取得上限数
	 * @param dateFormat 最終更新日時変換用Format
	 * @param multiHomes Logホームが複数設定されているか
	 * @return ログファイル情報
	 */
	private List<LogFile> searchStaticLogFile(String home, String path, List<Pattern> filterPatterns,
			Pattern fileNamePattern, Pattern lastModifiedPattern, int count, int limit, DateFormat dateFormat, boolean multiHomes) {

		List<LogFile> dirList = new ArrayList<>();
		List<LogFile> fileList = new ArrayList<>();

		File logsDir = new File(path);
		if (logsDir.exists() && logsDir.isDirectory()) {
			File[] logs = logsDir.listFiles();
			for (File file : logs) {
				if (file.isDirectory()) {
					dirList.addAll(searchStaticLogFile(home, file.getPath(), filterPatterns,
							fileNamePattern, lastModifiedPattern, count + dirList.size() + fileList.size(), limit, dateFormat, multiHomes));
				} else {
					String checkName = getFileName(home, file, multiHomes);

					boolean isMatch = isMatchFile(file, checkName, filterPatterns, fileNamePattern, lastModifiedPattern, dateFormat);
					if (isMatch) {
						LogFile info = new LogFile();
						info.setPath(file.getPath());
						info.setFileName(checkName);
						info.setLastModified(dateFormat.format(new Timestamp(file.lastModified())));
						info.setSize(file.length());
						fileList.add(info);
					}
				}

				// checkLimit
				if (limit > 0 && count + dirList.size() + fileList.size() >= limit) {
					break;
				}
			}
		} else {
			logger.debug("either logsDir doesn't exist or is not a folder. path=" + path);
		}

		// サブdirectory配下->fileの順番で
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
	 * @param filterPatterns ファイル名に対するconfig定義のFilter
	 * @param fileNamePattern ファイル名に対するFilter
	 * @param lastModifiedPattern 最終更新日時に対するFilter
	 * @param count 現在の対象ファイル数
	 * @param limit 取得上限数
	 * @param dateFormat 最終更新日時変換用Format
	 * @param multiHomes Logホームが複数設定されているか
	 * @return ログファイル情報
	 */
	private List<LogFile> searchDynamicLogFile(String fixedPath, String[] homePaths, int index, String path, List<Pattern> filterPatterns,
			Pattern fileNamePattern, Pattern lastModifiedPattern, int count, int limit, DateFormat dateFormat, boolean multiHomes) {

		List<LogFile> dirList = new ArrayList<>();
		List<LogFile> fileList = new ArrayList<>();

		File logsDir = new File(path);
		if (logsDir.exists() && logsDir.isDirectory()) {
			File[] logs = logsDir.listFiles();
			for (File file : logs) {
				if (file.isDirectory()) {
					if (index < homePaths.length) {
						//フォルダ名チェック
						if (!homePaths[index].equals("*")) {
							if (!file.getName().equals(homePaths[index])) {
								//対象外
								continue;
							}
						}
					}
					dirList.addAll(searchDynamicLogFile(fixedPath, homePaths, index + 1, file.getPath(), filterPatterns,
							fileNamePattern, lastModifiedPattern, count + dirList.size() + fileList.size(), limit, dateFormat, multiHomes));
				} else {
					if (index < homePaths.length) {
						//階層として、対象外
						continue;
					}

					String checkName = getFileName(fixedPath, file, multiHomes);

					boolean isMatch = isMatchFile(file, checkName, filterPatterns, fileNamePattern, lastModifiedPattern, dateFormat);
					if (isMatch) {
						LogFile info = new LogFile();
						info.setPath(file.getPath());
						info.setFileName(checkName);
						info.setLastModified(dateFormat.format(new Timestamp(file.lastModified())));
						info.setSize(file.length());
						fileList.add(info);
					}
				}

				// checkLimit
				if (limit > 0 && count + dirList.size() + fileList.size() >= limit) {
					break;
				}
			}
		} else {
			logger.debug("either logsDir doesn't exist or is not a folder. path=" + path);
		}

		// サブdirectory配下->fileの順番で
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
	 * @param multiHomes ログホームが複数設定されているか
	 * @return ファイル名
	 */
	private String getFileName(String hidePrefixPath, File file, boolean multiHomes) {

		String path = file.getPath().replaceAll("\\\\", "/");

		// ログホームが複数の場合、パスからホームを除去しない
		if (!multiHomes && path.startsWith(hidePrefixPath)) {
			return path.substring(hidePrefixPath.length());
		}
		return path;
	}

	/**
	 * <p>ファイルのFilterチェック</p>
	 *
	 * @param file ファイル
	 * @param fileName ファイル名(チェック用)
	 * @param filterPatterns ファイル名に対するconfig定義のFilter
	 * @param fileNamePattern ファイル名に対するFilter
	 * @param lastModifiedPattern 最終更新日時に対するFilter
	 * @param dateFormat 最終更新日時変換用Format
	 * @return チェック結果
	 */
	private boolean isMatchFile(File file, String fileName, List<Pattern> filterPatterns,
			Pattern fileNamePattern, Pattern lastModifiedPattern, DateFormat dateFormat) {

		boolean isMatch = false;

		// Filterチェック
		if (!filterPatterns.isEmpty()) {
			for (Pattern filter : filterPatterns) {
				if (filter.matcher(fileName).matches()) {
					isMatch = true;
					break;
				}
			}
		} else {
			isMatch = true;
		}

		// ファイル名チェック
		if (isMatch && fileNamePattern != null) {
			isMatch = fileNamePattern.matcher(fileName).find();
		}

		// 最終更新日時チェック
		if (isMatch && lastModifiedPattern != null) {
			isMatch = lastModifiedPattern.matcher(dateFormat.format(new Timestamp(file.lastModified()))).find();
		}

		return isMatch;
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
