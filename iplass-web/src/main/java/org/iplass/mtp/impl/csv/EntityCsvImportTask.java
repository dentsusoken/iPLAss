/*
 * Copyright (C) 2024 DENTSU SOKEN INC. All Rights Reserved.
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
import java.util.List;
import java.util.concurrent.Callable;

import org.iplass.mtp.SystemException;
import org.iplass.mtp.impl.async.ExceptionHandleable;
import org.iplass.mtp.impl.entity.EntityService;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entityの非同期CSVインポート用タスク
 */
public class EntityCsvImportTask implements Callable<EntityCsvImportResult>, ExceptionHandleable, Serializable {

	private static final long serialVersionUID = 8823049367792641192L;

	private static Logger logger = LoggerFactory.getLogger(EntityCsvImportTask.class);

	/** Uploadされたファイルの物理Path */
	private String filePath;

	/** Entity定義名 */
	private String defName;

	/** タスクのオプション */
	private EntityCsvImportOption option;

	/** インポート対象外とするエンティティ名 */
	final List<String> excludeEntityNames;

	/**
	 * コンストラクタ
	 * @param filePath Uploadされたファイルの物理Path
	 * @param uploadDateTime Uploadされた日時
	 * @param defName Entity定義名
	 * @param option タスクのオプション
	 * @param excludeEntityNames インポート対象外とするエンティティ名
	 */
	public EntityCsvImportTask(
			String filePath,
			String defName,
			EntityCsvImportOption option,
			List<String> excludeEntityNames) {
		super();

		this.filePath = filePath;
		this.defName = defName;
		this.option = option;
		this.excludeEntityNames = excludeEntityNames;
	}

	public String getFilePath() {
		return filePath;
	}

	public String getDefName() {
		return defName;
	}

	public EntityCsvImportOption getOption() {
		return option;
	}

	public List<String> getExcludeEntityNames() {
		return excludeEntityNames;
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
	public EntityCsvImportResult call() throws Exception {
		EntityCsvImportService importService = ServiceRegistry.getRegistry().getService(EntityCsvImportService.class);

		//MetaDataEntry
		String entityPath = EntityService.ENTITY_META_PATH + defName.replace(".", "/");
		MetaDataEntry entry = MetaDataContext.getContext().getMetaDataEntry(entityPath);
		try (InputStream is = new FileInputStream(filePath)) {
			EntityCsvImportResult ret = importService.importEntityData(defName, is, entry, option, null, null, excludeEntityNames);

			return ret;
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
