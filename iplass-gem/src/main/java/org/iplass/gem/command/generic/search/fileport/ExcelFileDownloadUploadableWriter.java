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
package org.iplass.gem.command.generic.search.fileport;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.gem.command.generic.search.EntityFileDownloadSearchContext;
import org.iplass.gem.command.generic.search.EntityFileDownloadSearchContext.FileColumn;
import org.iplass.gem.command.generic.search.EntityFileDownloadUploadableWriter;
import org.iplass.gem.command.generic.search.SearchQueryInterrupterHandler;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.properties.SelectProperty;
import org.iplass.mtp.impl.entity.fileport.EntityExcelWriteOption;
import org.iplass.mtp.impl.entity.fileport.EntitySearchExcelWriter;
import org.iplass.mtp.impl.entity.fileport.EntityCsvWriteOption.SearchQueryCsvContext;
import org.iplass.mtp.view.generic.SearchQueryContext;
import org.iplass.mtp.view.generic.SearchQueryInterrupter.SearchQueryType;
import org.iplass.mtp.view.generic.editor.SelectPropertyEditor;
import org.iplass.mtp.view.generic.element.section.SearchConditionSection;

public class ExcelFileDownloadUploadableWriter extends EntityFileDownloadUploadableWriter {

	public ExcelFileDownloadUploadableWriter(EntityFileDownloadSearchContext context) {
		super(context);
	}

	@Override
	public void write(final OutputStream out) throws IOException {

		EntityDefinition ed = context.getEntityDefinition();

		SearchConditionSection section = context.getConditionSection();

		//Limit
		int maxCount = gcs.getCsvDownloadMaxCount();
		if (section.getCsvdownloadMaxCount() != null) {
			maxCount = section.getCsvdownloadMaxCount();
		}

		//多重度複数の参照を含む検索時に一括ロードするか
		boolean loadOnceOfHasMultipleReferenceEntity = section.isUploadableCsvdownloadLoadAtOnce();

		//多重度複数の参照を含む検索時のロード単位
		int loadSizeOfHasMultipleReferenceEntity = gcs.getUploadableCsvDownloadLoadSize();
		if (section.getUploadableCsvdownloadLoadSize() != null) {
			loadSizeOfHasMultipleReferenceEntity = section.getUploadableCsvdownloadLoadSize();
		}

		//直接プロパティ指定
		List<String> directProperties = null;
		if (section.getCsvdownloadUploadableProperties() != null) {
			directProperties = new ArrayList<String>(section.getCsvdownloadUploadablePropertiesSet());
		}

		//Selectプロパティをソート条件保持用
		final Map<SelectProperty, Boolean> sortMap = new HashMap<>();

		//Interrupter
		final SearchQueryInterrupterHandler queryInterrupter = context.getSearchQueryInterrupterHandler();

		//Writer生成
		EntityExcelWriteOption option = new EntityExcelWriteOption()
				.withReferenceVersion(gcs.isCsvDownloadReferenceVersion())
				.withMappedByReference(gcs.isUploadableCsvDownloadWithMappedByReference())
				.properties(directProperties)
				.where(context.getWhere())
				.orderBy(context.getOrderBy())
				.limit(maxCount)
				.loadOnceOfHasMultipleReferenceEntity(loadOnceOfHasMultipleReferenceEntity)
				.loadSizeOfHasMultipleReferenceEntity(loadSizeOfHasMultipleReferenceEntity)
				.versioned(context.isVersioned())
				.mustOrderByWithLimit(cus.isMustOrderByWithLimit())
				.columnName(property -> context.getColumnName(property))
				.multipleColumnName((property, index) -> context.getMultipleColumnName(property, index))
				.sortSelectValue(property -> {
					//Selectプロパティをソートするか
					return sortMap.computeIfAbsent(property, select -> {
						boolean sortValue = false;
						FileColumn column = context.getFileColumn(select.getName());
						if (column != null && column.getEditor() instanceof SelectPropertyEditor) {
							SelectPropertyEditor spe = (SelectPropertyEditor)column.getEditor();
							if (spe.isSortCsvOutputValue()) {
								sortValue = true;
							}
						}
						return sortValue;
					});
				})
				.beforeSearch(query -> {
					SearchQueryContext sqc = queryInterrupter.beforeSearch(query, SearchQueryType.CSV);
					SearchQueryCsvContext sqcc = new SearchQueryCsvContext(sqc.getQuery());
					sqcc.setDoPrivileged(sqc.isDoPrivileged());
					sqcc.setWithoutConditionReferenceName(sqc.getWithoutConditionReferenceName());
					return sqcc;
				})
				.afterSearch((query, entity) -> {
					queryInterrupter.afterSearch(query, entity, SearchQueryType.CSV);
				});

		try (EntitySearchExcelWriter writer = new EntitySearchExcelWriter(out, ed.getName(), option)) {
			writer.write();
		}
	}

}
