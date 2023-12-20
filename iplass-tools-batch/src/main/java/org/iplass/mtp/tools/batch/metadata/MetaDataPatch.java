/*
 * Copyright 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 */

package org.iplass.mtp.tools.batch.metadata;

import org.apache.commons.lang3.StringUtils;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.tools.metaport.MetaDataPortingService;
import org.iplass.mtp.impl.tools.metaport.PatchEntityDataParameter;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tools.batch.ExecMode;
import org.iplass.mtp.tools.batch.MtpCuiBase;
import org.iplass.mtp.tools.batch.MtpBatchResourceDisposer;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetaDataPatch extends MtpCuiBase {

	private static Logger logger = LoggerFactory.getLogger(MetaDataPatch.class);

	private static final String EMPTY = "_empty_";

	private ExecMode execMode = ExecMode.WIZARD;

	private int tenantId = -1;
	private String oldMetaDataFilePath;
	private String newMetaDataFilePath;
	private String userId;
	private String password;

	private TenantContextService tenantContextService = ServiceRegistry.getRegistry().getService(TenantContextService.class);
	private MetaDataPortingService service = ServiceRegistry.getRegistry().getService(MetaDataPortingService.class);

	/**
	 * コンストラクタ
	 *
	 * args[0]・・・execMode["Wizard" or "Silent"]
	 * args[1]・・・tenantId
	 * args[2]・・・oldMetaDataFilePath
	 * args[3]・・・newMetaDataFilePath
	 * args[4]・・・userId
	 * args[5]・・・password
	 *
	 * @param args
	 */
	public MetaDataPatch(String... args) {
		if (args.length > 0) {
			execMode = ExecMode.valueOf(args[0].toUpperCase());
		}
		if (args.length > 1) {
			if (!isEmpty(args[1])) {
				tenantId = Integer.parseInt(args[1]);
			}
			if (tenantId < 0) {
				execMode = ExecMode.WIZARD;
			}
		}
		if (args.length > 2) {
			oldMetaDataFilePath = args[2];
			if (isEmpty(oldMetaDataFilePath)) {
				execMode = ExecMode.WIZARD;
			}
		}
		if (args.length > 3) {
			newMetaDataFilePath = args[3];
			if (isEmpty(newMetaDataFilePath)) {
				execMode = ExecMode.WIZARD;
			}
		}
		// ユーザIDが未指定の場合は特権モードで実行
		if (args.length > 4 && !isEmpty(args[4])) {
			userId = args[4];
		}
		if (args.length > 5 && !isEmpty(args[5])) {
			password = args[5];
		}
	}

	/**
	 * メイン処理
	 *
	 * args[0]・・・execMode["Wizard" or "Silent"]
	 * args[1]・・・tenantId
	 * args[2]・・・oldMetaDataFilePath
	 * args[3]・・・newMetaDataFilePath
	 * args[4]・・・userId
	 * args[5]・・・password
	 *
	 * @param args 引数
	 */
	public static void main(String... args) {
		try {
			new MetaDataPatch(args).execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// リソース破棄
			MtpBatchResourceDisposer.disposeResource();
		}
	}

	private boolean isEmpty(String str) {
		return StringUtil.isBlank(str) || EMPTY.equals(str.toLowerCase());
	}

	private boolean execute() {
		clearLog();

		//Console出力
		switchLog(true, false);

		// 環境情報出力
		logEnvironment();

		switch (execMode) {
		case WIZARD:
			return proceed(wizard());
		case SILENT:

			//Silentの場合はConsole出力を外す
			switchLog(false, true);

			return proceed(silent());
		default :
			logError("unsupport execute mode : " + execMode);
			return false;
		}
	}

	private PatchEntityDataParameter silent() {
		PatchEntityDataParameter param = new PatchEntityDataParameter();

		// 対象テナントID
		param.setTenantId(tenantId);

		// 旧MetaDataファイルパス
		if (StringUtil.isEmpty(oldMetaDataFilePath)) {
			// 以前のMetaDataファイルパスが指定されていません。
			logWarn(rs("Silent.noOldMetaDataFilePathMsg"));
			return null;
		}
		param.setOldMetaDataFilePath(oldMetaDataFilePath);

		// 新MetaDataファイルパス
		if (StringUtil.isEmpty(newMetaDataFilePath)) {
			// パッチ先のMetaDataファイルパスが指定されていません。
			logWarn(rs("Silent.noNewMetaDataFilePathMsg"));
			return null;
		}
		param.setNewMetaDataFilePath(newMetaDataFilePath);

		return param;
	}

	private PatchEntityDataParameter wizard() {
		PatchEntityDataParameter param = new PatchEntityDataParameter();

		// 対象テナントID
		boolean validTenantId = false;
		do {
			// テナントIDを入力してください。
			String input = readConsole(rs("Wizard.inputTenantIdMsg"));
			if (StringUtil.isEmpty(input)) {
				// テナントIDは必須です。
				logWarn(rs("Wizard.requiredTenantIdMsg"));
				continue;
			}
			try {
				param.setTenantId(Integer.parseInt(input));
				validTenantId = true;
			} catch (NumberFormatException e) {
				// テナントIDは数字のみで入力してください。
				logWarn(rs("Wizard.warnTenantIdMsg"));
			}
		} while (!validTenantId);

		// 旧MetaDataファイルパス
		boolean validOldMetaDataFilePath = false;
		do {
			// 以前のMetaDataファイルパスを入力してください。
			String input = readConsole(rs("Wizard.inputOldMetaDataFilePathMsg"));
			if (StringUtil.isEmpty(input)) {
				// 以前のMetaDataファイルパスは必須です。
				logWarn(rs("Wizard.requiredOldMetaDataFilePathMsg"));
				continue;
			}
			param.setOldMetaDataFilePath(input);
			validOldMetaDataFilePath = true;
		} while (!validOldMetaDataFilePath);

		// 新MetaDataファイルパス
		boolean validNewMetaDataFilePath = false;
		do {
			// 新しいMetaDataファイルパスを入力してください。
			String input = readConsole(rs("Wizard.inputNewMetaDataFilePathMsg"));
			if (StringUtil.isEmpty(input)) {
				// 新しいMetaDataファイルパスは必須です。
				logWarn(rs("Wizard.requiredNewMetaDataFilePathMsg"));
				continue;
			}
			param.setNewMetaDataFilePath(input);
			validNewMetaDataFilePath = true;
		} while (!validNewMetaDataFilePath);

		// ユーザID(任意)
		// ユーザIDを入力してください。
		userId = readConsole(rs("Wizard.inputUserIdMsg"));

		// パスワード(任意)
		// パスワードを入力してください。
		if (StringUtil.isNotBlank(userId)) {
			password = readConsole(rs("Wizard.inputPasswordMsg"));
		}

		return param;
	}

	/**
	 * 実行情報を出力します。
	 */
	private void logArguments(PatchEntityDataParameter param) {
		logInfo("-----------------------------------------------------------");
		logInfo("■Execute Argument");
		logInfo("\t" + rs("Proceed.tenantId") + " :" + param.getTenantId());
		logInfo("\t" + rs("Proceed.oldMetaDataFilePath") + " :" + param.getOldMetaDataFilePath());
		logInfo("\t" + rs("Proceed.newMetaDataFilePath") + " :" + param.getNewMetaDataFilePath());
		if (StringUtil.isNotBlank(userId)) {
			logInfo("\t" + rs("Proceed.userId") + " :" + userId);
		}
		if (StringUtil.isNotBlank(password)) {
			logInfo("\t" + rs("Proceed.password") + " :" + StringUtils.repeat('*', password.length()));
		}
		logInfo("-----------------------------------------------------------");
		logInfo("");
	}

	private boolean proceed(PatchEntityDataParameter param) {
		if (param == null) return false;

		if (ExecMode.WIZARD == execMode) {
			// 実行確認
			boolean validExecute = false;
			do {
				logArguments(param);
				// 上記の内容でパッチ処理を実行してよろしいですか？
				validExecute = readConsoleBoolean(rs("Wizard.confirmExecutePatchMsg"), false);
				if (!validExecute) {
					// もう一度設定し直しますか？
					boolean retry = readConsoleBoolean(rs("Wizard.confirmRetryMsg"), true);
					if (retry) wizard();
				}
			} while (!validExecute);
		}

		//Consoleを削除してLogに切り替え
		switchLog(false, true);

		logArguments(param);

		setSuccess(false);

		// テナント存在チェック
		TenantContext tc = tenantContextService.getTenantContext(param.getTenantId());
		if (tc == null) {
			logError(rs("Proceed.notFoundTenantMsg", param.getTenantId()));
			return isSuccess();
		}

		return ExecuteContext.executeAs(tc, () -> {
			return executeTask(param, (paramA) -> {
				setSuccess(proceedPatch(paramA));
				return isSuccess();
			});
		});
	}

	private boolean proceedPatch(PatchEntityDataParameter param) {

		if (StringUtil.isNotBlank(userId)) {
			// ユーザ認証
			service.patchEntityDataWithUserAuth(param, userId, password);
		} else {
			// 特権モード
			service.patchEntityDataWithPrivilegedAuth(param);
		}
		return true;
	}

	@Override
	protected String rs(String key, Object... args) {
		return super.rs("MetaDataPatch." + key, args);
	}

	@Override
	protected Logger loggingLogger() {
		return logger;
	}
}
