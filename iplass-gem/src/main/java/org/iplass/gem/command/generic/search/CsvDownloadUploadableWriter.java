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
import java.util.HashMap;
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

		//Limit
		int maxCount = gcs.getCsvDownloadMaxCount();
		SearchConditionSection section = context.getConditionSection();
		if (section.getCsvdownloadMaxCount() != null) {
			maxCount = section.getCsvdownloadMaxCount();
		}

		//Selectプロパティをソート条件保持用
		final Map<SelectProperty, Boolean> sortMap = new HashMap<>();

		//Interrupter
		final SearchQueryInterrupterHandler handler = context.getSearchQueryInterrupterHandler();

		//Writer生成
		EntityWriteOption option = new EntityWriteOption()
				.charset(charset)
				.quoteAll(gcs.isCsvDownloadQuoteAll())
				.withReferenceVersion(gcs.isCsvDownloadReferenceVersion())
				.where(context.getWhere())
				.orderBy(context.getOrderBy())
				.limit(maxCount)
				.versioned(context.isVersioned())
				.mustOrderByWithLimit(cus.isMustOrderByWithLimit())
				.columnDisplayName(property -> {
					if (context.isNoDispName()) {
						return "";
					} else {
						return "(" + context.getColumnLabel(property.getName()) + ")";
					}
				})
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
					SearchQueryContext sqc = handler.beforeSearch(query, SearchQueryType.CSV);
					SearchQueryCsvContext sqcc = new SearchQueryCsvContext(sqc.getQuery());
					sqcc.setDoPrivileged(sqc.isDoPrivileged());
					sqcc.setWithoutConditionReferenceName(sqc.getWithoutConditionReferenceName());
					return sqcc;
				})
				.afterSearch((query, entity) -> {
					handler.afterSearch(query, entity, SearchQueryType.CSV);
				});

		try (EntitySearchCsvWriter writer = new EntitySearchCsvWriter(out, ed.getName(), option)) {
			writer.write();
		}
	}

}
