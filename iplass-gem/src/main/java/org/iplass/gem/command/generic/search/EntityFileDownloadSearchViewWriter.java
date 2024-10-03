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

package org.iplass.gem.command.generic.search;

import java.io.IOException;
import java.io.OutputStream;
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
import java.util.stream.Collectors;

import org.iplass.gem.GemConfigService;
import org.iplass.gem.command.generic.search.EntityFileDownloadSearchContext.FileColumn;
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
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.DateUtil;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.SearchQueryContext;
import org.iplass.mtp.view.generic.SearchQueryInterrupter.SearchQueryType;
import org.iplass.mtp.view.generic.editor.SelectPropertyEditor;
import org.iplass.mtp.view.generic.editor.UserPropertyEditor;
import org.iplass.mtp.view.generic.element.FileItem;
import org.iplass.mtp.view.generic.element.section.SearchConditionSection;
import org.iplass.mtp.web.ResultStreamWriter;
import org.iplass.mtp.web.template.TemplateUtil;

/**
 * <p>Entityのファイル出力クラス。SearchLayout設定に伴なって出力を行います。</p>
 */
public abstract class EntityFileDownloadSearchViewWriter implements ResultStreamWriter {

	protected final EntityFileDownloadSearchContext context;

	protected final GemConfigService gcs;

	public EntityFileDownloadSearchViewWriter(EntityFileDownloadSearchContext context) {
		this.context = context;

		gcs = ServiceRegistry.getRegistry().getService(GemConfigService.class);
	}

	@Override
	public void write(OutputStream out) throws IOException {

		// 初期化
		initWriter(out);

		// 列情報を取得
		List<FileColumn> columns = context.getFileColumns();

		// ヘッダ出力
		writeHeader(columns);

		// データ出力
		writeData(columns);

		// データ出力終了
		endData();
	}

	/**
	 * Writerの初期化を行います。
	 *
	 * @param out 出力ストリーム
	 */
	protected abstract void initWriter(OutputStream out) throws IOException;

	/**
	 * ヘッダーの出力を開始します。
	 *
	 */
	protected abstract void beforeWriteHeader();

	/**
	 * ヘッダーの列を出力します。
	 *
	 * @param columnIndex 列番号
	 * @param text テキスト
	 */
	protected abstract void writeHeaderColumn(int columnIndex, String text);

	/**
	 * データの出力を開始します。
	 *
	 */
	protected abstract void beforeWriteData();

	/**
     * データ行の出力を開始します。
     */
	protected abstract void startRow();

	/**
     * 値の列を出力します。
     *
     * @param columnIndex 列番号
     * @param fileColumn 列情報
     * @param value 値
     */
	protected abstract void writeValueColumn(int columnIndex, FileColumn fileColumn, Object value);

	/**
     * 多重度複数値を纏めて出力する際の個々の文字列値を返します。
     * 各値はカンマで連結します。
     *
     * @param value 値
     */
	protected abstract String valueToUnSplitMultipleValueString(Object value);

	/**
	 * 次の列へ移動します。
	 */
	protected abstract void nextColumn();

	/**
     * 行の終了を出力します。
     */
	protected abstract void endRow();

	/**
     * データ出力を終了します。
     */
	protected abstract void endData() throws IOException;

	/**
	 * ヘッダを出力します。
	 *
	 * @param columns 列情報
	 */
	private void writeHeader(List<FileColumn> columns) {

		beforeWriteHeader();

		int columnIndex = 0;
		for (FileColumn column : columns) {
			int multi = column.getMultiplicity();
			if (multi <= 1) {
				writeHeaderColumn(columnIndex, column.getColumnLabel());
				if (columns.indexOf(column) < columns.size() - 1) {
					nextColumn();
				}
			} else {
				if (context.getMultipleFormat() == MultipleFormat.EACH_COLUMN) {
					for (int i = 0; i < multi; i++) {
						if (i != 0) {
							nextColumn();
							columnIndex++;
						}
						writeHeaderColumn(columnIndex, column.getColumnLabel() + "[" + i + "]");
					}
				} else {
					writeHeaderColumn(columnIndex, column.getColumnLabel());
				}
				if (columns.indexOf(column) < columns.size() - 1) {
					nextColumn();
				}
			}
			columnIndex++;
		}
		endRow();
	}

	/**
	 * データを出力します。
	 *
	 * @param columns 列情報
	 */
	private void writeData(List<FileColumn> columns) {

		beforeWriteData();

		Query query = new Query();
		query.setSelect(context.getSelect());
		query.from(context.getDefName());
		query.setWhere(context.getWhere());
		query.setVersioned(context.isVersioned());
		query.setOrderBy(context.getOrderBy());

		int maxCount = gcs.getCsvDownloadMaxCount();
		SearchConditionSection section = context.getConditionSection();
		if (section.getCsvdownloadMaxCount() != null) {
			maxCount = section.getCsvdownloadMaxCount();
		}
		if (maxCount > 0) {
			query.setLimit(new Limit(maxCount));
		}

		int cacheLimit = gcs.getSearchResultCacheLimit();

		SearchQueryInterrupterHandler handler = context.getSearchQueryInterrupterHandler();
		MultipleFormat multipleFormat = context.getMultipleFormat();
		boolean isShowUserNameWithPrivilegedValue = ((SearchContextBase) context).getForm().isShowUserNameWithPrivilegedValue();

		new CsvDownloadSearchImpl(handler, columns, cacheLimit, isShowUserNameWithPrivilegedValue).execute(
				query, new EntityViewTypeFormatter(this, columns, multipleFormat));
	}

	/**
	 * Object型のtoString()した値を返します。
	 *
	 * BigDecimalは、toPlainString()の値を返します
	 *
	 * @param obj オブジェクト
	 * @return toString()またはtoPlainString()した値
	 */
	protected String valueToString(Object obj) {
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
			if (context.isOutputCodeValue()) {
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

	/**
	 * SearchQueryInterrupterHandlerを実行し、Entityを検索する。
	 * UserPropertyEditorの設定されているプロパティに対して名称を取得する。
	 * 検索結果をCSVDownloadPredicateに渡し、CSVを出力する。
	 */
	private static class CsvDownloadSearchImpl {

		private SearchQueryInterrupterHandler handler;
		private List<FileColumn> columns;
		private int cacheLimit;
		private EntityManager em;
		private boolean isShowUserNameWithPrivilegedValue;

		private Set<String> userProperties;

		/**
		 * コンストラクタ
		 *
		 * @param handler	SearchQueryInterrupterHandler
		 * @param columns 列情報
		 * @param cacheLimit １度の出力件数
		 * @param isShowUserNameWithPrivilegedValue 特権実行でユーザー名を取得
		 */
		public CsvDownloadSearchImpl(
				final SearchQueryInterrupterHandler handler,
				final List<FileColumn> columns,
				final int cacheLimit,
				final boolean isShowUserNameWithPrivilegedValue) {
			this.handler = handler;
			this.columns = columns;
			this.cacheLimit = cacheLimit;
			this.em = ManagerLocator.manager(EntityManager.class);
			this.isShowUserNameWithPrivilegedValue = isShowUserNameWithPrivilegedValue;
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

			final SearchQueryContext sqc = handler.beforeSearch(query, SearchQueryType.CSV);

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

						//ユーザー情報を反映
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

			//ユーザー情報を反映
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
		 * @param userOides ユーザーのOID情報
		 */
		private void setUserProperty(List<Entity> tmpResult, Set<String> userOides) {

			// 対象項目、対象データがない場合は終了
			if (userProperties.isEmpty() || userOides.isEmpty()) {
				return;
			}

			// ユーザー情報の検索
			final Map<String, String> userMap = new HashMap<>();

			Query q = new Query().select(Entity.OID, Entity.NAME)
					 .from(User.DEFINITION_NAME)
					 .where(new In(Entity.OID, userOides.toArray()));

			if (isShowUserNameWithPrivilegedValue) {
				AuthContext.doPrivileged(() -> {
					em.searchEntity(q, new Predicate<Entity>() {
						@Override
						public boolean test(Entity entity) {
							userMap.put(entity.getOid(), entity.getName());
							return true;
						}
					});
				});
			} else {
				em.searchEntity(q, new Predicate<Entity>() {
					@Override
					public boolean test(Entity entity) {
						userMap.put(entity.getOid(), entity.getName());
						return true;
					}
				});
			}

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
		private EntityFileDownloadSearchViewWriter writer;

		/** カラム情報 */
		private List<FileColumn> columns;

		/** 多重度複数の出力形式 */
		private MultipleFormat multipleFormat;

		/**
		 * コンストラクタ
		 *
		 * @param writer Writer
		 * @param columns カラム情報
		 * @param multipleFormat 多重度複数の出力形式
		 */
		public EntityViewTypeFormatter(
				EntityFileDownloadSearchViewWriter writer, List<FileColumn> columns,
				MultipleFormat multipleFormat) {
			this.writer = writer;
			this.columns = columns;
			this.multipleFormat = multipleFormat;
		}

		@Override
		public boolean test(Entity entity) {

			writer.startRow();

			int columnIndex = 0;
			for (Iterator<FileColumn> itc = columns.iterator(); itc.hasNext();) {
				FileColumn fileColumn = itc.next();
				String propName = fileColumn.getPropertyName();
				int multiple = fileColumn.getMultiplicity();

				Object value = entity.getValue(propName);

				if (value instanceof Object[]) {
					//多重度複数の場合
					Object[] values = (Object[]) value;

					columnIndex = formatMultipleValue(columnIndex, fileColumn, multiple, values);

				} else if (value != null && value.toString().length() != 0) {

					writer.writeValueColumn(columnIndex, fileColumn, value);

				} else if (value == null) {
					//多重度複数の場合で各列出力の場合は埋める
					if (multiple > 1 && multipleFormat == MultipleFormat.EACH_COLUMN) {
						for (int i = 0; i < multiple - 1; i++) {
							writer.writeValueColumn(columnIndex, fileColumn, null);
							writer.nextColumn();
							columnIndex++;
						}
					}
					writer.writeValueColumn(columnIndex, fileColumn, null);
				}
				if (itc.hasNext()) {
					writer.nextColumn();
				}
				columnIndex++;
			}

			writer.endRow();

			return true;
		}

		/**
		 * 多重度複数のデータを出力します。
		 *
		 * @param columnIndex 列番号
		 * @param fileColumn 列情報
		 * @param multiple 多重度
		 * @param values 値
		 * @return 列番号
		 */
		private int formatMultipleValue(int columnIndex, FileColumn fileColumn, int multiple, Object[] values) {

			PropertyDefinition pd = fileColumn.getPropertyDefinition();
			FileItem fileItem = fileColumn.getFileItem();
			if (pd != null && pd instanceof SelectProperty
					&& fileItem != null && fileItem.getEditor() instanceof SelectPropertyEditor) {
				//SelectPropertyEditorによるソートの可能性があるため別制御
				return formatMultipleSelectValue(columnIndex, fileColumn, (SelectProperty)pd, (SelectPropertyEditor)fileItem.getEditor(), multiple, values);
			} else {
				if (multipleFormat == MultipleFormat.EACH_COLUMN) {
					return formatSplitMultipleValue(columnIndex, fileColumn, multiple, values);
				} else {
					return formatUnSplitMultipleValue(columnIndex, fileColumn, multiple, values);
				}
			}
		}

		/**
		 * 多重度複数のデータを多重度分各列に出力します。
		 *
		 * @param columnIndex 列番号
		 * @param fileColumn 列情報
		 * @param multiple 多重度
		 * @param values 値
		 * @return 列番号
		 */
		private int formatSplitMultipleValue(int columnIndex, FileColumn fileColumn, int multiple, Object[] values) {

			for (int i = 0; i < multiple; i++) {

				// 配列の分ループ
				if (values.length - 1 >= i && values[i] != null) {
					writer.writeValueColumn(columnIndex, fileColumn, values[i]);
				} else {
					writer.writeValueColumn(columnIndex, fileColumn, null);
				}

				if (i < multiple - 1) {
					writer.nextColumn();
					columnIndex++;
				}
			}
			return columnIndex;
		}

		/**
		 * 多重度複数のデータを１列に出力します。
		 *
		 * @param columnIndex 列番号
		 * @param fileColumn 列情報
		 * @param multiple 多重度
		 * @param values 値
		 * @return 列番号
		 */
		private int formatUnSplitMultipleValue(int columnIndex, FileColumn fileColumn, int multiple, Object[] values) {

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

				String strValue = writer.valueToUnSplitMultipleValueString(values[i]);
				if (StringUtil.isNotEmpty(strValue)) {
					sb.append(strValue);
				}
			}
			writer.writeValueColumn(columnIndex, fileColumn, sb.toString());

			return columnIndex;
		}

		/**
		 * 多重度複数のSelectValueを出力します。
		 *
		 * @param columnIndex 列番号
		 * @param fileColumn 列情報
		 * @param sp SelectProperty
		 * @param spe SelectPropertyEditor
		 * @param multiple 多重度
		 * @param values 値
		 * @return 列番号
		 */
		private int formatMultipleSelectValue(int columnIndex, FileColumn fileColumn, SelectProperty sp, SelectPropertyEditor spe, int multiple, Object[] values) {

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
				return formatSplitMultipleValue(columnIndex, fileColumn, multiple, outputValues);
			} else {
				return formatUnSplitMultipleValue(columnIndex, fileColumn, multiple, outputValues);
			}
		}

	}
}
