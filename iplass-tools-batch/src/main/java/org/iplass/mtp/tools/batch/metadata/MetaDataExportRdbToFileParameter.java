package org.iplass.mtp.tools.batch.metadata;

import java.util.Collections;
import java.util.List;

/**
 * RDB管理のメタデータをローカルファイルへ格納する機能のパラメータ
 *
 * @author SEKIGUCHI Naoya
 */
public class MetaDataExportRdbToFileParameter {
	/** テナントID */
	private int tenantId;
	/** テナント名 */
	private String tenantName;
	/** 初期移行か（初期移行の場合 true） */
	private boolean isInitaialConvert = false;
	/** 全メタデータを対象とするか */
	private boolean isExportAllMetaData = true;
	/** 個別指定のExportメタデータパス（カンマ区切り） */
	private String exportMetaDataPath;
	/** export対象のメタデータパス */
	private List<String> exportMetaDataPathList = Collections.emptyList();

	/**
	 * テナントIDを取得する
	 * @return テナントID
	 */
	public int getTenantId() {
		return tenantId;
	}

	/**
	 * テナントIDを設定する
	 * @param tenantId テナントID
	 */
	public void setTenantId(int tenantId) {
		this.tenantId = tenantId;
	}

	/**
	 * テナント名を取得する
	 * @return テナント名
	 */
	public String getTenantName() {
		return tenantName;
	}

	/**
	 * テナント名を設定する
	 * @param tenantName テナント名
	 */
	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	/**
	 * 初期移行であるか
	 * @return 初期移行であるか（true: 初期移行）
	 */
	public boolean isInitaialConvert() {
		return isInitaialConvert;
	}

	/**
	 * 初期移行であるかを設定する
	 * @param isInitaialConvert 初期移行であるか
	 */
	public void setInitaialConvert(boolean isInitaialConvert) {
		this.isInitaialConvert = isInitaialConvert;
	}

	/**
	 * 全メタデータを対象とするか
	 * @return 全メタデータを対象とするか（true: 全メタデータが対象）
	 */
	public boolean isExportAllMetaData() {
		return isExportAllMetaData;
	}

	/**
	 * 全メタデータを対象とするかを設定する
	 * @param isExportAllMetaData 全メタデータを対象とするか
	 */
	public void setExportAllMetaData(boolean isExportAllMetaData) {
		this.isExportAllMetaData = isExportAllMetaData;
	}

	/**
	 * 対象メタデータパスを取得する
	 * @return 対象メタデータパス
	 */
	public String getExportMetaDataPath() {
		return exportMetaDataPath;
	}

	/**
	 * 対象メタデータパスを設定する
	 *
	 * <ul>
	 * <li>メタデータパスは、カンマ区切りで複数指定可能</li>
	 * <li>末尾にワイルドカードとして "*" を指定可能</li>
	 * </ul>
	 *
	 * @param exportMetaDataPathStr 対象メタデータパス
	 */
	public void setExportMetaDataPath(String exportMetaDataPathStr) {
		this.exportMetaDataPath = exportMetaDataPathStr;
	}

	/**
	 * 対象メタデータパスリストを取得する
	 * @return 対象メタデータパスリスト
	 */
	public List<String> getExportMetaDataPathList() {
		return exportMetaDataPathList;
	}

	/**
	 * 対象メタデータパスリストを設定する
	 * <p>
	 * このメタデータパスリストは、メタデータへの実パスが設定される。
	 * </p>
	 *
	 * @param exportMetaDataPathList 対象メタデータパスリスト
	 */
	public void setExportMetaDataPathList(List<String> exportMetaDataPathList) {
		this.exportMetaDataPathList = exportMetaDataPathList;
	}
}
