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

package org.iplass.mtp.tools.batch.entity;

import java.io.OutputStream;
import java.util.Scanner;

import org.apache.commons.io.output.CloseShieldOutputStream;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.tools.entity.EntityToolService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tools.batch.MtpCuiBase;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EQL実行バッチ
 */
public class EQLExecutor extends MtpCuiBase {

	private static Logger logger = LoggerFactory.getLogger(EQLExecutor.class);

	private static final String SET_EQL_EXEC_MODE = "EQL_EXEC_MODE=";

	private static TenantContextService tenantContextService = ServiceRegistry.getRegistry().getService(TenantContextService.class);
	private static EntityToolService entityToolService = ServiceRegistry.getRegistry().getService(EntityToolService.class);

	/** テナントID */
	private int tenantId = -1;

	/** EQL */
	private String eql;

	/** 全バージョン検索 */
	private boolean isSearchAllVersion = false;

	/** 実行モード */
	private ExecMode execMode = ExecMode.BATCH;
	private enum ExecMode {
		BATCH, INTERACT
	}

	/** EQL実行モード */
	private EQLExecMode eqlExecMode = EQLExecMode.ONLY_EXEC;
	private enum EQLExecMode {
		ONLY_EXEC, ONLY_COUNT, SHOW_SEARCH_RESULT
	}

	/** ユーザID */
	private String userId;

	/** パスワード */
	private String password;

	/**
	 * コンストラクタ
	 *
	 * args[0]・・・EQL
	 * args[1]・・・tenantId
	 * args[2]・・・isSearchAllVersion
	 * args[3]・・・execMode["BATCH":バッチモード, "INTERACT":対話モード]
	 * args[4]・・・eqlExecMode["ONLY_EXEC":実行のみ, "ONLY_COUNT":カウントのみ, "SHOW_SEARCH_RESULT":検索結果表示]
	 * args[5]・・・userId
	 * args[6]・・・password
	 **/
	public EQLExecutor(String... args) {
		if (args == null || args.length < 2) {
			return;
		}

		if (args.length > 0) {
			eql = args[0];
		}
		if (args.length > 1) {
			tenantId = Integer.parseInt(args[1]);
		}
		if (args.length > 2) {
			isSearchAllVersion = Boolean.parseBoolean(args[2]);
		}
		if (args.length > 3) {
			execMode = ExecMode.valueOf(args[3]);
		}
		if (args.length > 4) {
			eqlExecMode = EQLExecMode.valueOf(args[4]);
		}
		if (args.length > 5) {
			userId = args[5];
		}
		if (args.length > 6) {
			password = args[6];
		}
	}

	public static void main(String... args) {
		try {
			new EQLExecutor(args).execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean execute() {
		clearLog();

		switchLog(false, true);

		// 環境情報出力
		logEnvironment();

		return executeTask(null, (param) -> {
			return proceed();
		}, false, true);
	}

	private boolean proceed() {
		setSuccess(false);

		if (tenantId < 0) {
			logError(rs("EQLExecutor.invalidTenantId", tenantId));
			return isSuccess();
		}

		// テナント存在チェック
		TenantContext tc = tenantContextService.getTenantContext(tenantId);
		if (tc == null) {
			logError(rs("EQLExecutor.notFoundTenant", tenantId));
			return isSuccess();
		}

		return ExecuteContext.executeAs(tc, () -> {
			switch (execMode) {
			case BATCH:
				if (StringUtil.isBlank(eql)) {
					logError(rs("EQLExecutor.notSpecifiedEQL"));
					return isSuccess();
				}
				proceedBatch(eql);
				break;
			case INTERACT:
				proceedInteract();
				break;
			}

			setSuccess(true);

			return isSuccess();
		});
	}

	private void proceedBatch(String eql) {
		int count = -1;
		switch (eqlExecMode) {
		case ONLY_EXEC:
			if (StringUtil.isBlank(userId)) {
				entityToolService.executeEQLWithAuth(eql, isSearchAllVersion, false);
			} else {
				entityToolService.executeEQLWithAuth(eql, isSearchAllVersion, false, userId, password);
			}
			break;
		case ONLY_COUNT:
			if (StringUtil.isBlank(userId)) {
				count = entityToolService.executeEQLWithAuth(eql, isSearchAllVersion, true);
			} else {
				count = entityToolService.executeEQLWithAuth(eql, isSearchAllVersion, true, userId, password);
			}
			break;
		case SHOW_SEARCH_RESULT:
			OutputStream out = new CloseShieldOutputStream(System.out);
			if (StringUtil.isBlank(userId)) {
				count = entityToolService.executeEQLWithAuth(out, System.getProperty("file.encoding"), eql, isSearchAllVersion);
			} else {
				count = entityToolService.executeEQLWithAuth(out, System.getProperty("file.encoding"), eql, isSearchAllVersion, userId, password);
			}
			break;
		}

		if (count < 0) {
			System.out.println("\nexecuted.");
		} else {
			String format = count > 1 ? "\n%d rows selected." : "\n%d row selected.";
			System.out.println(String.format(format, count));
		}
	}

	private void proceedInteract() {
		try (Scanner sc = new Scanner(System.in)) {
			boolean isMultiLine = false;
			StringBuilder sb = new StringBuilder();
			while (true) {
				System.out.print(isMultiLine ? "  -> " : "EQL> ");

				String cmd = sc.nextLine().trim();
				if (StringUtil.isBlank(cmd)) continue;

				if (!isMultiLine) {
					if ("exit".equals(cmd.toLowerCase()) || "quit".equals(cmd.toLowerCase())) {
						System.out.println("Bye");
						break;
					}
					if (cmd.startsWith(SET_EQL_EXEC_MODE)) {
						if (cmd.length() > SET_EQL_EXEC_MODE.length()) {
							String mode = cmd.substring(SET_EQL_EXEC_MODE.length());
							try {
								eqlExecMode = EQLExecMode.valueOf(mode);
							} catch (Exception e) {
								StringBuilder sbMode = new StringBuilder();
								for (EQLExecMode eqlExecMode : EQLExecMode.values()) {
									if (sbMode.length() > 0) sbMode.append("|");
									sbMode.append(eqlExecMode.toString());
								}
								System.out.println("Usage: " + SET_EQL_EXEC_MODE + "[" + sbMode.toString() + "]");
							}
						} else {
							System.out.println(eqlExecMode.toString());
						}
						continue;
					}
				}

				if (cmd.endsWith(";")) {
					sb.append(cmd.substring(0, cmd.length() - 1));
					if (sb.length() > 0) {
						try {
							proceedBatch(sb.toString());
						} catch (Exception e) {
							System.out.println(e.getMessage());
						}
						sb.setLength(0);
					} else {
						System.out.println("No query specified");
					}
					isMultiLine = false;
				} else {
					sb.append(cmd).append(' ');
					isMultiLine = true;
				}
			}
		}
	}

	@Override
	protected Logger loggingLogger() {
		return logger;
	}
}
