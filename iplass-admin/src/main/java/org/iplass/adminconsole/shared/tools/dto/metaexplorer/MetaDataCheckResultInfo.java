/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
 */

package org.iplass.adminconsole.shared.tools.dto.metaexplorer;

import java.io.Serializable;
import java.util.List;

/**
 * メタデータチェック結果
 */
public class MetaDataCheckResultInfo implements Serializable {

	private static final long serialVersionUID = 2689277971210264992L;

	/** エラーかどうか */
	private boolean isError = false;
	/** 警告かどうか */
	private boolean isWarn = false;
	/** チェック結果メッセージ */
	private String message = null;
	/** チェック結果メタデータパス */
	private List<String> metaDataPaths = null;

	public MetaDataCheckResultInfo() {
		// デフォルトコンストラクターがないとAdminConsoleのコンパイルでエラーになるので
		this(false);
	}

	public MetaDataCheckResultInfo(boolean isError) {
		this.isError = isError;
	}

	/**
	 * エラーかどうかを取得します
	 * 
	 * @return エラーかどうか
	 */
	public boolean isError() {
		return isError;
	}

	/**
	 * 警告かどうかを取得します
	 * 
	 * @return 警告かどうか
	 */
	public boolean isWarn() {
		return isWarn;
	}

	/**
	 * 警告かどうかを設定します
	 * 
	 * @param isWarn 警告かどうか
	 */
	public void setWarn(boolean isWarn) {
		this.isWarn = isWarn;
	}

	/**
	 * チェック結果メッセージをを取得します
	 * 
	 * @return チェック結果メッセージ
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * チェック結果メッセージを設定します
	 * 
	 * @param message チェック結果メッセージ
	 */
	public void setMessage(String message) {
		this.message = message;
	}


	/**
	 * チェック結果メタデータパスを取得します
	 * 
	 * @return チェック結果メタデータパス
	 */
	public List<String> getMetaDataPaths() {
		return metaDataPaths;
	}

	/**
	 * チェック結果メタデータパスを設定します
	 * 
	 * @param metaDataPaths チェック結果メタデータパス
	 */
	public void setMetaDataPaths(List<String> metaDataPaths) {
		this.metaDataPaths = metaDataPaths;
	}
}
