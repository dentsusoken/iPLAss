/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
 */

package org.iplass.mtp.impl.tools.metaport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.util.CollectionUtil;
import org.iplass.mtp.util.StringUtil;

/**
 * MetaData整合性チェック結果
 */
public class MetaDataCheckResult implements Serializable {

	private static final long serialVersionUID = -4000661299012819750L;

	/** チェック結果ステータス */
	private ResultStatus resultStatus = ResultStatus.Success;
	/** チェック結果メッセージ */
	private String message = null;
	/** チェック結果メタデータパス */
	private List<String> metaDataPaths = null;

	/**
	 * チェック結果ステータスを返却します
	 * 
	 * @return チェック結果ステータスを
	 */
	public ResultStatus getResultStatus() {
		return resultStatus;
	}

	/**
	 * チェック結果ステータスを設定します
	 * 
	 * @param resultStatus チェック結果ステータス
	 */
	public void setResultStatus(ResultStatus resultStatus) {
		this.resultStatus = resultStatus;
	}

	/**
	 * エラーかどうかを取得します
	 * 
	 * @return エラーかどうか
	 */
	public boolean isError() {
		return resultStatus == ResultStatus.Error;
	}

	/**
	 * 警告かどうかを取得します
	 * 
	 * @return 警告かどうか
	 */
	public boolean isWarn() {
		return resultStatus == ResultStatus.Warn;
	}

	/**
	 * チェック結果メッセージを取得します
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

	/**
	 * チェック結果メタデータパスを追加します
	 * 
	 * @param metaDataPath チェック結果メタデータパス
	 */
	public void addMetaDataPaths(String metaDataPath) {
		if (metaDataPaths == null) {
			metaDataPaths = new ArrayList<>();
		}

		metaDataPaths.add(metaDataPath);
	}

	/**
	 * チェック結果メタデータパスをクリアします
	 */
	public void clearMetaDataPaths() {
		metaDataPaths = null;
	}

	/**
	 * 出力メッセージを生成
	 * 
	 * @param separator 区切り文字
	 * @return 出力メッセージ
	 */
	public String createMessage(String separator) {
		if (StringUtil.isEmpty(this.message)) {
			return "";
		}

		StringBuilder sb = new StringBuilder(this.message);
		if (CollectionUtil.isEmpty(this.metaDataPaths)) {
			return sb.toString();
		}

		sb.append(separator);
		sb.append(String.join(separator, this.metaDataPaths));

		return sb.toString();
	}
}
