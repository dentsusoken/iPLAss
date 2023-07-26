/*
 * Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.gem.command.generic.search;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.gem.GemConfigService;
import org.iplass.gem.command.generic.search.CsvDownloadSearchContext.CsvColumn;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.properties.SelectProperty;
import org.iplass.mtp.impl.csv.CsvUploadService;
import org.iplass.mtp.impl.entity.csv.EntitySearchCsvWriter;
import org.iplass.mtp.impl.entity.csv.EntityWriteOption;
import org.iplass.mtp.impl.entity.csv.EntityWriteOption.SearchQueryCsvContext;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.view.generic.SearchQueryContext;
import org.iplass.mtp.view.generic.SearchQueryInterrupter.SearchQueryType;
import org.iplass.mtp.view.generic.editor.SelectPropertyEditor;
import org.iplass.mtp.view.generic.element.section.SearchConditionSection;
import org.iplass.mtp.web.ResultStreamWriter;

/**
 * <p>EntityのCsvファイル出力クラス。Upload可能な形式で出力を行います。</p>
 */
public class CsvDownloadUploadableWriter implements ResultStreamWriter {

	private CsvDownloadSearchContext context;

	private GemConfigService gcs = null;
	private CsvUploadService cus = null;

	public CsvDownloadUploadableWriter(final CsvDownloadSearchContext context) {
		this.context = context;

		gcs = ServiceRegistry.getRegistry().getService(GemConfigService.class);
		cus = ServiceRegistry.getRegistry().getService(CsvUploadService.class);
	}

	@Override
	public void write(final OutputStream out) throws IOException {

		EntityDefinition ed = context.getEntityDefinition();
		String charset = context.getCharacterCode();

		SearchConditionSection section = context.getConditionSection();

		//Limit
		int maxCount = gcs.getCsvDownloadMaxCount();
		if (section.getCsvdownloadMaxCount() != null) {
			maxCount = section.getCsvdownloadMaxCount();
		}

		//多重度複数の参照を含む検索時の一括ロード件数
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
		EntityWriteOption option = new EntityWriteOption()
				.charset(charset)
				.quoteAll(gcs.isCsvDownloadQuoteAll())
				.withReferenceVersion(gcs.isCsvDownloadReferenceVersion())
				.properties(directProperties)
				.where(context.getWhere())
				.orderBy(context.getOrderBy())
				.limit(maxCount)
				.loadSizeOfHasMultipleReferenceEntity(loadSizeOfHasMultipleReferenceEntity)
				.versioned(context.isVersioned())
				.mustOrderByWithLimit(cus.isMustOrderByWithLimit())
				.columnName(property -> context.getColumnName(property))
				.multipleColumnName((property, index) -> context.getMultipleColumnName(property, index))
				.sortSelectValue(property -> {
					//Selectプロパティをソートするか
					return sortMap.computeIfAbsent(property, select -> {
						boolean sortValue = false;
						CsvColumn column = context.getCsvColumn(select.getName());
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

		try (EntitySearchCsvWriter writer = new EntitySearchCsvWriter(out, ed.getName(), option)) {
			writer.write();
		}
	}

}
