/*
 * Copyright (C) 2018 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.csv;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

import org.iplass.mtp.SystemException;
import org.iplass.mtp.async.TaskStatus;
import org.iplass.mtp.impl.async.ExceptionHandleable;
import org.iplass.mtp.impl.entity.csv.EntityCsvException;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>非同期CSV Upload用タスク</p>
 *
 */
public class CsvUploadTask implements Callable<CsvUploadStatus>, ExceptionHandleable, Serializable {

	private static final long serialVersionUID = -3723274599525554443L;

	private static Logger logger = LoggerFactory.getLogger(CsvUploadTask.class);

	/** Uploadされたファイルの物理Path */
	private String filePath;
	/** Uploadされたファイル名 */
	private String fileName;

	/** Uploadされた日時 */
	private long uploadDateTime;

	/** Entity定義名 */
	private String defName;

	/** タスクのパラメータ(起動側での判断用) */
	private String parameter;

	/** オプション */
	private CsvUploadOption option;

	/**
	 * コンストラクタ
	 * @param filePath Uploadされたファイルの物理Path
	 * @param fileName Uploadされたファイル名
	 * @param uploadDateTime Uploadされた日時
	 * @param defName Entity定義名
	 * @param parameter タスクパラメータ
	 * @param uniqueKey UniqueKeyプロパティ名
	 * @param isDenyInsert CSVアップロードで登録を許可しない
	 * @param isDenyUpdate CSVアップロードで更新を許可しない
	 * @param isDenyDelete CSVアップロードで削除を許可しない
	 * @param insertProperties CSVアップロード登録項目
	 * @param updateProperties CSVアップロード更新項目
	 * @param transactionType トランザクション方法
	 * @param commitLimit トランザクション分割時のCommit単位
	 * @param withReferenceVersion 参照値にバージョンが含まれているか
	 * @param deleteSpecificVersion 特定バージョンを削除するか
	 * @param updateTargetVersionForNoneVersionedEntity CSVアップロードのバージョン管理Entity以外の場合の更新時のデフォルトTargetVersion
	 * @param interrupterClassName CsvUploadInterrupterクラス名
	 */
	public CsvUploadTask(
			String filePath,
			String fileName,
			long uploadDateTime,
			String defName,
			String parameter,
			CsvUploadOption option) {
		super();

		this.filePath = filePath;
		this.fileName = fileName;
		this.uploadDateTime = uploadDateTime;
		this.defName = defName;
		this.parameter = parameter;
		this.option = option;
	}

	public String getFilePath() {
		return filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public long getUploadDateTime() {
		return uploadDateTime;
	}

	public String getDefName() {
		return defName;
	}

	public String getParameter() {
		return parameter;
	}

	public CsvUploadOption getCsvUploadOption() {
		return option;
	}

	@Override
	public void aborted(Throwable cause) {
	}

	@Override
	public void timeouted() {
	}

	@Override
	public void canceled() {
	}

	@Override
	public CsvUploadStatus call() throws Exception {

		CsvUploadService service = ServiceRegistry.getRegistry().getService(CsvUploadService.class);

		try (InputStream is = new FileInputStream(filePath)) {
			service.validate(is, defName, option.isWithReferenceVersion(), option.getInterrupterClassName());
		} catch (FileNotFoundException e) {
			throw new SystemException(e);
		} catch (EntityCsvException e) {
			try {
				CsvUploadStatus result = new CsvUploadStatus();
				result.setFileName(getFileName());
				result.setUploadDateTime(getUploadDateTime());
				result.setCode(e.getCode());
				result.setMessage(e.getMessage());
				result.setStatus(TaskStatus.ABORTED);
				return result;
			} finally {
				deleteFile();
			}
		}

		try (InputStream is = new FileInputStream(filePath)){
			CsvUploadStatus result = service.upload(is, defName, option);
			return result;
		} catch (FileNotFoundException e) {
			throw new SystemException(e);
		} finally {
			deleteFile();
		}
	}

	private void deleteFile() {
		try {
			Files.deleteIfExists(Paths.get(filePath));
		} catch (IOException e) {
			// 一時ファイルが削除出来なかった場合でもエラーとしない
			logger.warn("Fail to delete a Temporary's File.", e);
		}
	}

}
