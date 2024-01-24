/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.tools.entityport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.query.OrderBy;
import org.iplass.mtp.entity.query.SortSpec;
import org.iplass.mtp.entity.query.SortSpec.SortType;
import org.iplass.mtp.entity.query.Where;
import org.iplass.mtp.impl.csv.CsvUploadService;
import org.iplass.mtp.impl.csv.EntityCsvImportOption;
import org.iplass.mtp.impl.csv.EntityCsvImportResult;
import org.iplass.mtp.impl.csv.EntityCsvImportService;
import org.iplass.mtp.impl.entity.csv.EntitySearchCsvWriter;
import org.iplass.mtp.impl.entity.csv.EntityWriteOption;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.impl.parser.ParseContext;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.parser.SyntaxContext;
import org.iplass.mtp.impl.parser.SyntaxService;
import org.iplass.mtp.impl.query.OrderBySyntax;
import org.iplass.mtp.impl.query.QuerySyntaxRegister;
import org.iplass.mtp.impl.tools.metaport.MetaDataTagEntity;
import org.iplass.mtp.impl.tools.pack.PackageEntity;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.StringUtil;

/**
 * EntityのExport/Import用Service
 */
public class EntityPortingService implements Service {

	/** LOBデータ格納パス */
	public static final String ENTITY_LOB_DIR = "lobs/";

	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSSXXX";
	private static final String TIME_FORMAT = "HH:mm:ss";

	/** Upload形式のCSVダウンロード時の一括ロード件数 */
	private int uploadableCsvDownloadLoadSize;

	private SyntaxService syntaxService;

	private EntityDefinitionManager edm;

	@Override
	public void init(Config config) {

		uploadableCsvDownloadLoadSize = config.getValue("uploadableCsvDownloadLoadSize", Integer.class, 1);

		syntaxService = config.getDependentService(SyntaxService.class);

		edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
	}

	@Override
	public void destroy() {
	}

	/**
	 * EntityデータをCSV形式でExportします。
	 *
	 * @param os        出力先CSVファイル(Stream)
	 * @param entry     出力対象Entity
	 * @param condition Export条件
	 * @return 出力件数
	 * @throws IOException
	 */
	public int write(final OutputStream os, final MetaDataEntry entry, final EntityDataExportCondition condition) throws IOException {

		return writeWithBinary(os, entry, condition, null);
	}

	/**
	 * EntityデータをCSV形式でExportします。
	 *
	 * @param os        出力先CSVファイル(Stream)
	 * @param entry     出力対象Entity
	 * @param condition Export条件
	 * @param zos    Lobを追加するZipのOutputStream
	 * @param lobPrefixPath LobをZipに追加する際のPrefixPath
	 * @return 出力件数
	 * @throws IOException
	 */
	public int writeWithBinary(final OutputStream os, final MetaDataEntry entry, final EntityDataExportCondition condition, final ZipOutputStream zos) throws IOException {
		return writeWithBinary(os, entry, condition, zos, null);
	}

	/**
	 * EntityデータをCSV形式でExportします。
	 *
	 * @param os        出力先CSVファイル(Stream)
	 * @param entry     出力対象Entity
	 * @param condition Export条件
	 * @param zos    Lobを追加するZipのOutputStream
	 * @param lobPrefixPath LobをZipに追加する際のPrefixPath
	 * @param exportBinaryDataDir Binaryデータの出力先ディレクトリ
	 * @return 出力件数
	 * @throws IOException
	 */
	public int writeWithBinary(final OutputStream os, final MetaDataEntry entry, final EntityDataExportCondition condition, final ZipOutputStream zos, String exportBinaryDataDir) throws IOException {

		EntityDefinition definition = edm.get(entry.getMetaData().getName());

		Where where = null;
		if (StringUtil.isNotEmpty(condition.getWhereClause())) {
			where = Where.newWhere("where " + condition.getWhereClause());
		}
		OrderBy orderBy = null;
		if (StringUtil.isNotEmpty(condition.getOrderByClause())) {
			try {
				SyntaxContext sc = syntaxService.getSyntaxContext(QuerySyntaxRegister.QUERY_CONTEXT);
				orderBy = sc.getSyntax(OrderBySyntax.class).parse(new ParseContext("order by " + condition.getOrderByClause()));
			} catch(ParseException e) {
				throw new EntityDataPortingRuntimeException(e);
			}
		} else {
			orderBy = new OrderBy();
			orderBy.add(new SortSpec(Entity.OID, SortType.ASC));
			orderBy.add(new SortSpec(Entity.VERSION, SortType.ASC));
		}

		CsvUploadService csvUploadService = ServiceRegistry.getRegistry().getService(CsvUploadService.class);

		//Writer生成
		EntityWriteOption option = new EntityWriteOption()
				.withReferenceVersion(true)
				.withBinary(true)
				.exportBinaryDataDir(exportBinaryDataDir)
				.where(where)
				.orderBy(orderBy)
				.dateFormat(DATE_FORMAT)
				.datetimeSecFormat(DATE_TIME_FORMAT)
				.timeSecFormat(TIME_FORMAT)
				.loadSizeOfHasMultipleReferenceEntity(uploadableCsvDownloadLoadSize)
				.mustOrderByWithLimit(csvUploadService.isMustOrderByWithLimit());
		int count = 0;
		try (EntitySearchCsvWriter writer = new EntitySearchCsvWriter(os, definition.getName(), option, zos)) {
			count = writer.write();
		}

		return count;
	}

	/**
	 * EntityデータをImportします。
	 *
	 * @param targetName インポート対象の名前(Package名またはCSVファイル名)
	 * @param is CSVファイル(Stream)
	 * @param entry 対象Entity
	 * @param condition Import条件
	 * @param zipFile LOBファイルが格納されているzipファイル(nullの場合、LOBファイルは取り込みません)
	 * @return Import結果
	 */
	public EntityDataImportResult importEntityData(String targetName, final InputStream is, final MetaDataEntry entry, final EntityDataImportCondition condition, final ZipFile zipFile) {
		return importEntityData(targetName, is, entry, condition, zipFile, null);
	}

	/**
	 * EntityデータをImportします。
	 *
	 * @param targetName インポート対象の名前(Package名またはCSVファイル名)
	 * @param is CSVファイル(Stream)
	 * @param entry 対象Entity
	 * @param condition Import条件
	 * @param zipFile LOBファイルが格納されているzipファイル(nullの場合、LOBファイルは取り込みません)
	 * @param importBinaryDataDir LOBファイルが格納されているディレクトリ(nullの場合、LOBファイルは取り込みません)
	 * @return Import結果
	 */
	public EntityDataImportResult importEntityData(String targetName, final InputStream is, final MetaDataEntry entry, final EntityDataImportCondition condition, final ZipFile zipFile, final String importBinaryDataDir) {
		// tools -> webのクラスへ変換
		EntityCsvImportOption option = new EntityCsvImportOption()
				.truncate(condition.isTruncate())
				.bulkUpdate(condition.isBulkUpdate())
				.errorSkip(condition.isErrorSkip())
				.ignoreNotExistsProperty(condition.isIgnoreNotExistsProperty())
				.notifyListeners(condition.isNotifyListeners())
				.withValidation(condition.isWithValidation())
				.updateDisupdatableProperty(condition.isUpdateDisupdatableProperty())
				.insertEnableAuditPropertySpecification(condition.isInsertEnableAuditPropertySpecification())
				.prefixOid(condition.getPrefixOid())
				.commitLimit(condition.getCommitLimit())
				.fourceUpdate(condition.isFourceUpdate())
				.uniqueKey(condition.getUniqueKey())
				.locale(condition.getLocale())
				.timezone(condition.getTimezone());

		// 取り込み対象外のEntity
		List<String> excludeEntityNames = Arrays.asList(PackageEntity.ENTITY_DEFINITION_NAME, MetaDataTagEntity.ENTITY_DEFINITION_NAME);

		EntityCsvImportService service = ServiceRegistry.getRegistry().getService(EntityCsvImportService.class);
		EntityCsvImportResult ret = service.importEntityData(targetName, is, entry, option, zipFile, importBinaryDataDir, excludeEntityNames);

		// web -> toolsのクラスへ変換
		EntityDataImportResult result = new EntityDataImportResult();
		result.setInsertCount(ret.getInsertCount());
		result.setUpdateCount(ret.getUpdateCount());
		result.setDeleteCount(ret.getDeleteCount());
		result.setMergeCount(ret.getMergeCount());
		result.setErrorCount(ret.getErrorCount());
		result.setError(ret.isError());
		result.setMessages(ret.getMessages());
		return result;
	}
}
