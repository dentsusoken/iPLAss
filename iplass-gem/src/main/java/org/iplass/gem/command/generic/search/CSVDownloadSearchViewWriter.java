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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.text.StringEscapeUtils;
import org.iplass.gem.GemConfigService;
import org.iplass.gem.command.generic.search.CsvDownloadSearchContext.CsvColumn;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.auth.User;
import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.entity.csv.MultipleFormat;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.SelectProperty;
import org.iplass.mtp.entity.permission.EntityPermission;
import org.iplass.mtp.entity.query.Limit;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.condition.predicate.In;
import org.iplass.mtp.impl.entity.csv.EntityCsvException;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.DateUtil;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.SearchQueryContext;
import org.iplass.mtp.view.generic.SearchQueryInterrupter.SearchQueryType;
import org.iplass.mtp.view.generic.editor.SelectPropertyEditor;
import org.iplass.mtp.view.generic.editor.UserPropertyEditor;
import org.iplass.mtp.view.generic.element.CsvItem;
import org.iplass.mtp.view.generic.element.section.SearchConditionSection;
import org.iplass.mtp.web.ResultStreamWriter;
import org.iplass.mtp.web.template.TemplateUtil;

/**
 * <p>EntityのCsvファイル出力クラス。SearchLayout設定に伴なって出力を行います。</p>
 */
public class CSVDownloadSearchViewWriter implements ResultStreamWriter {

	private static final String CR = "\n";	// 改行コード。CSV出力なので、SJIS(MS932)を想定
	private static final String DOUBLE_QUOT = "\"";
	private static final int BOM = '\ufeff';

	private CsvDownloadSearchContext context;

	private Writer writer;

	private GemConfigService gcs = null;

	public CSVDownloadSearchViewWriter(CsvDownloadSearchContext context) {
		this.context = context;

		gcs = ServiceRegistry.getRegistry().getService(GemConfigService.class);
	}

	@Override
	public void write(OutputStream out) throws IOException {

		String charSet = context.getCharacterCode();

		this.writer = new BufferedWriter(new OutputStreamWriter(out, charSet));

		//BOM対応
		if ("UTF-8".equalsIgnoreCase(charSet)) {
			try {
				writer.write(BOM);
			} catch (IOException e) {
				throw new EntityCsvException(e);
			}
		}

		//列情報を取得
		List<CsvColumn> columns = context.getCsvColumns();

		//ヘッダ出力
		writeHeader(columns);

		//データ出力
		writeData(columns);

		//フッタ出力
		writeFooter();

		writer.flush();
	}

	/**
	 * ヘッダを出力します。
	 *
	 * @param columns 列情報
	 */
	private void writeHeader(List<CsvColumn> columns) {

		for (CsvColumn column : columns) {
			int multi = column.getMultiplicity();
			if (multi <= 1) {
				writeText(column.getColumnLabel());
				if (columns.indexOf(column) < columns.size() - 1) {
					writeComma();
				}
			} else {
				if (context.getMultipleFormat() == MultipleFormat.EACH_COLUMN) {
					for (int i = 0; i < multi; i++) {
						if (i != 0) {
							writeComma();
						}
						writeText(column.getColumnLabel() + "[" + i + "]");
					}
				} else {
					writeText(column.getColumnLabel());
				}
				if (columns.indexOf(column) < columns.size() - 1) {
					writeComma();
				}
			}
		}
		writeCR();
	}

	/**
	 * データを出力します。
	 *
	 * @param columns 列情報
	 */
	private void writeData(List<CsvColumn> columns) {

		Query query = new Query();
		query.setSelect(context.getSelect());
		query.from(context.getDefName());
		query.setWhere(context.getWhere());
		query.setVersiond(context.isVersioned());
		query.setOrderBy(context.getOrderBy());

		int maxCount = gcs.getCsvDownloadMaxCount();
		SearchConditionSection section = context.getConditionSection();
		if (section.getCsvdownloadMaxCount() != null) {
			maxCount = section.getCsvdownloadMaxCount();
		}
		query.setLimit(new Limit(maxCount));

		int cacheLimit = gcs.getSearchResultCacheLimit();

		SearchQueryInterrupterHandler handler = context.getSearchQueryInterrupterHandler();
		MultipleFormat multipleFormat = context.getMultipleFormat();
		boolean isOutputCodeValue = context.isOutputCodeValue();

		new CsvDownloadSearchImpl(handler, columns, cacheLimit).execute(
				query, new EntityViewTypeFormatter(this, columns, multipleFormat, isOutputCodeValue));
	}

	private void writeFooter() {
		GemConfigService service = ServiceRegistry.getRegistry().getService(GemConfigService.class);
		if (service.isCsvDownloadWithFooter()) {
			try {
				writer.write(service.getCsvDownloadFooter());
			} catch (IOException e) {
				throw new EntityCsvException(e);
			}

			writeCR();
		}
	}

	private void writeText(String text) {

		try {
			if (StringUtil.isEmpty(text)) {
				return;
			}

			String outText = StringEscapeUtils.escapeCsv(text);
			if (gcs.isCsvDownloadQuoteAll()) {
				if (outText.startsWith("\"") && outText.endsWith("\"")) {
					writer.write(outText);
				} else {
					writer.write(DOUBLE_QUOT + outText + DOUBLE_QUOT);
				}
			} else {
				if (outText.startsWith(" ") || outText.endsWith(" ")) {
					writer.write(DOUBLE_QUOT + outText + DOUBLE_QUOT);
				} else {
					writer.write(outText);
				}
			}
		} catch (IOException e) {
			throw new EntityCsvException(e);
		}

	}

	private void writeComma() {

		try {
			writer.write(",");
		} catch (IOException e) {
			throw new EntityCsvException(e);
		}
	}

	private void writeCR() {

		try {
			writer.write(CR);
		} catch (IOException e) {
			throw new EntityCsvException(e);
		}
	}

	/**
	 * SearchQueryInterrupterHandlerを実行し、Entityを検索する。
	 * UserPropertyEditorの設定されているプロパティに対して名称を取得する。
	 * 検索結果をCSVDownloadPredicateに渡し、CSVを出力する。
	 */
	private static class CsvDownloadSearchImpl {

		private SearchQueryInterrupterHandler handler;
		private List<CsvColumn> columns;
		private int cacheLimit;
		private EntityManager em;

		private Set<String> userProperties;

		/**
		 * コンストラクタ
		 *
		 * @param handler	SearchQueryInterrupterHandler
		 * @param columns 列情報
		 * @param cacheLimit １度の出力件数
		 */
		public CsvDownloadSearchImpl(
				final SearchQueryInterrupterHandler handler,
				final List<CsvColumn> columns,
				final int cacheLimit) {
			this.handler = handler;
			this.columns = columns;
			this.cacheLimit = cacheLimit;
			this.em = ManagerLocator.manager(EntityManager.class);
		}

		/**
		 * Entityの検索を実行します。
		 *
		 * @param query Query
		 * @param formatter CSV Formatter
		 */
		public void execute(Query query, final EntityViewTypeFormatter formatter) {

			//UserPropertyEditorのチェック
			checkUserPropertyEditor();

			final SearchQueryContext sqc = handler.beforeSearch(query.versioned(true), SearchQueryType.CSV);

			if (sqc.isDoPrivileged()) {
				AuthContext.doPrivileged(() -> searchEntity(sqc, formatter));
			} else {
				if (sqc.getWithoutConditionReferenceName() != null) {
					EntityPermission.doQueryAs(sqc.getWithoutConditionReferenceName(), () -> {
						searchEntity(sqc, formatter);
						return null;
					});
				} else {
					searchEntity(sqc, formatter);
				}
			}
		}

		/**
		 * UserPropertyEditorが設定されている列情報を取得します。
		 */
		private void checkUserPropertyEditor() {

			userProperties = columns.stream()
				.filter(column -> column.getEditor() instanceof UserPropertyEditor)
				.map(column -> column.getPropertyName())
				.collect(Collectors.toSet());
		}

		/**
		 * Entityを検索します。
		 *
		 * @param sqc Interrupter用SearchQueryContext
		 * @param formatter CSV Formatter
		 */
		private void searchEntity(final SearchQueryContext sqc, final EntityViewTypeFormatter formatter) {

			final List<Entity> tmpResult = new ArrayList<>();
			final Set<String> userOides = new HashSet<>();

			em.searchEntity(sqc.getQuery(), new Predicate<Entity>() {

				@Override
				public boolean test(Entity entity) {
					handler.afterSearch(sqc.getQuery(), entity, SearchQueryType.CSV);

					tmpResult.add(entity);

					//User名を出力するUserのOIDを取得
					for (String userProp : userProperties) {
						if (StringUtil.isNotEmpty(entity.getValue(userProp))) {
							userOides.add(entity.getValue(userProp));
						}
					}

					if (tmpResult.size() == cacheLimit) {

						//ユーザ情報を反映
						setUserProperty(tmpResult, userOides);

						//指定されたformatterの呼び出し
						for (Entity afterModel : tmpResult) {
							if (!formatter.test(afterModel)) {
								tmpResult.clear();
								userOides.clear();
								return false;
							}
						}

						tmpResult.clear();
						userOides.clear();
					}
					return true;
				}

			});

			//ユーザ情報を反映
			setUserProperty(tmpResult, userOides);

			//指定されたformatterの呼び出し
			for (Entity afterModel : tmpResult) {
				if (!formatter.test(afterModel)) {
					return;
				}
			}
		}

		/**
		 * Userプロパティ情報をEntityにセットします。
		 *
		 * @param tmpResult 検索結果のEntityのリスト
		 * @param userOides ユーザのOID情報
		 */
		private void setUserProperty(List<Entity> tmpResult, Set<String> userOides) {

			// 対象項目、対象データがない場合は終了
			if (userProperties.isEmpty() || userOides.isEmpty()) {
				return;
			}

			// ユーザ情報の検索
			final Map<String, String> userMap = new HashMap<>();

			Query q = new Query().select(Entity.OID, Entity.NAME)
					 .from(User.DEFINITION_NAME)
					 .where(new In(Entity.OID, userOides.toArray()));

			em.searchEntity(q, new Predicate<Entity>() {
				@Override
				public boolean test(Entity entity) {
					userMap.put(entity.getOid(), entity.getName());
					return true;
				}
			});

			for (Entity entity : tmpResult) {

				for (String userProp : userProperties) {
					String userOid = entity.getValue(userProp);
					if (StringUtil.isNotEmpty(userOid)) {
						if (userMap.containsKey(userOid)) {
							entity.setValue(userProp, userMap.get(userOid));
						}
					}
				}

			}
		}
	}

	/**
	 * Entityデータを出力します。
	 */
	private static class EntityViewTypeFormatter implements Predicate<Entity> {

		/** Writer */
		private CSVDownloadSearchViewWriter writer;

		/** カラム情報 */
		private List<CsvColumn> columns;

		/** 多重度複数の出力形式 */
		private MultipleFormat multipleFormat;

		/** SelectValueをコード形式で出力するか */
		private boolean isOutputCodeValue;

		/**
		 * コンストラクタ
		 *
		 * @param writer Writer
		 * @param columns カラム情報
		 * @param multipleFormat 多重度複数の出力形式
		 * @param isOutputCodeValue SelectValueのコード出力
		 */
		public EntityViewTypeFormatter(
				CSVDownloadSearchViewWriter writer, List<CsvColumn> columns,
				MultipleFormat multipleFormat, boolean isOutputCodeValue) {
			this.writer = writer;
			this.columns = columns;
			this.multipleFormat = multipleFormat;
			this.isOutputCodeValue = isOutputCodeValue;
		}

		@Override
		public boolean test(Entity entity) {

			for (Iterator<CsvColumn> itc = columns.iterator(); itc.hasNext();) {
				CsvColumn csvColumn = itc.next();
				String propName = csvColumn.getPropertyName();
				int multiple = csvColumn.getMultiplicity();

				Object value = entity.getValue(propName);

				if (value instanceof Object[]) {
					//多重度複数の場合
					Object[] values = (Object[]) value;

					formatMultipleValue(csvColumn, multiple, values);

				} else if (value != null && value.toString().length() != 0) {

					writer.writeText(valueToString(value));

				} else if (value == null) {
					//多重度複数の場合で各列出力の場合は埋める
					if (multiple > 1 && multipleFormat == MultipleFormat.EACH_COLUMN) {
						for (int i = 0; i < multiple - 1; i++) {
							writer.writeComma();
						}
					}
				}
				if (itc.hasNext()) {
					writer.writeComma();
				}
			}

			writer.writeCR();

			return true;
		}

		/**
		 * 多重度複数のデータを出力します。
		 *
		 * @param csvColumn 列情報
		 * @param multiple 多重度
		 * @param values 値
		 */
		private void formatMultipleValue(CsvColumn csvColumn, int multiple, Object[] values) {

			PropertyDefinition pd = csvColumn.getPropertyDefinition();
			CsvItem csvItem = csvColumn.getCsvItem();
			if (pd != null && pd instanceof SelectProperty
					&& csvItem != null && csvItem.getEditor() instanceof SelectPropertyEditor) {
				//SelectPropertyEditorによるソートの可能性があるため別制御
				formatMultipleSelectValue((SelectProperty)pd, (SelectPropertyEditor)csvItem.getEditor(), multiple, values);
			} else {
				if (multipleFormat == MultipleFormat.EACH_COLUMN) {
					formatSplitMultipleValue(multiple, values);
				} else {
					formatUnSplitMultipleValue(multiple, values);
				}
			}
		}

		/**
		 * 多重度複数のデータを多重度分各列に出力します。
		 *
		 * @param multiple 多重度
		 * @param values 値
		 */
		private void formatSplitMultipleValue(int multiple, Object[] values) {

			for (int i = 0; i < multiple; i++) {

				// 配列の分ループ
				if (values.length - 1 >= i && values[i] != null) {
					writer.writeText(valueToString(values[i]));
				}

				if (i < multiple - 1) {
					writer.writeComma();
				}
			}
		}

		/**
		 * 多重度複数のデータを１列に出力します。
		 *
		 * @param multiple 多重度
		 * @param values 値
		 */
		private void formatUnSplitMultipleValue(int multiple, Object[] values) {

			int arrayLen = values.length;
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < multiple; i++) {
				if (i >= arrayLen && multipleFormat == MultipleFormat.ONE_COLUMN) {
					//多重度分値がないので終了
					break;
				}

				if (i != 0) {
					sb.append(",");
				}

				if (i >= arrayLen) {
					//多重度分値がないので次へ
					continue;
				}

				String strValue = valueToString(values[i]);
				if (StringUtil.isNotEmpty(strValue)) {
					String outText = StringEscapeUtils.escapeCsv(strValue);
					//値にダブルクォーテーションが含まれる場合は、最後に２重にエスケープされるので戻す
					if (strValue.contains("\"")) {
						outText = outText.replaceAll(Pattern.quote("\"\""), "\"");
					}
					sb.append(outText);
				}
			}
			writer.writeText(sb.toString());
		}

		/**
		 * 多重度複数のSelectValueを出力します。
		 *
		 * @param sp SelectProperty
		 * @param spe SelectPropertyEditor
		 * @param multiple 多重度
		 * @param values 値
		 */
		private void formatMultipleSelectValue(SelectProperty sp, SelectPropertyEditor spe, int multiple, Object[] values) {

			//Editor設定から出力方式を取得
			Object[] outputValues = values;
			if (spe.isSortCsvOutputValue()) {
				//ソート出力

				List<SelectValue> selectableValues = sp.getSelectValueList();
				List<SelectValue> sortedValues = new ArrayList<>(selectableValues.size());
				for (int i = 0; i < multiple; i++) {
					if (selectableValues.size() < i + 1) {
						//選択値より多重度が多い場合(念のためチェック)
						if (i < multiple - 1) {
							sortedValues.add(null);
						}
						continue;
					}
					SelectValue targetValue = selectableValues.get(i);
					boolean exist = false;
					for (Object storeValue : values) {
						if (storeValue != null && ((SelectValue)storeValue).getValue().equals(targetValue.getValue())) {
							exist = true;
							break;
						}
					}
					if (exist) {
						sortedValues.add(targetValue);
					} else {
						sortedValues.add(null);
					}
				}
				outputValues = sortedValues.toArray();
			}

			if (multipleFormat == MultipleFormat.EACH_COLUMN) {
				formatSplitMultipleValue(multiple, outputValues);
			} else {
				formatUnSplitMultipleValue(multiple, outputValues);
			}
		}

		/**
		 * Object型のtoString()した値を返します。
		 *
		 * BigDecimalは、toPlainString()の値を返します
		 *
		 * @param obj オブジェクト
		 * @return toString()またはtoPlainString()した値
		 */
		private String valueToString(Object obj) {
			if (obj == null) {
				return "";
			}

			if (obj instanceof Entity) {
				Entity ge = (Entity)obj;
				return ge.getOid();
			}
			if (obj instanceof BigDecimal) {
				return ((BigDecimal)obj).toPlainString();
			}
			if (obj instanceof Float) {
				return BigDecimal.valueOf((Float)obj).toPlainString();
			}
			if (obj instanceof Double) {
				return BigDecimal.valueOf((Double)obj).toPlainString();
			}
			if (obj instanceof SelectValue) {
				if (isOutputCodeValue) {
					return ((SelectValue) obj).getValue();
				} else {
					return ((SelectValue) obj).getDisplayName();
				}
			}
			if (obj instanceof BinaryReference) {
				return ((BinaryReference) obj).getName();
			}
			if (obj instanceof Timestamp) {
				return DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getOutputDatetimeSecFormat(), true).format(obj);
			} else if (obj instanceof Date) {
				return DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getOutputDateFormat(), false).format(obj);
			} else if (obj instanceof Time) {
				return DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getOutputTimeSecFormat(), false).format(obj);
			}

			return obj.toString();
		}
	}

}
