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

import org.apache.commons.text.StringEscapeUtils;
import org.iplass.gem.GemConfigService;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.auth.User;
import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.entity.csv.MultipleFormat;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.entity.definition.VersionControlType;
import org.iplass.mtp.entity.definition.properties.BinaryProperty;
import org.iplass.mtp.entity.definition.properties.ExpressionProperty;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
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
import org.iplass.mtp.view.generic.element.property.PropertyColumn;
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
	private EntityDefinitionManager edm = null;
	private EntityManager em = null;


	public CSVDownloadSearchViewWriter(CsvDownloadSearchContext context) {
		this.context = context;

		gcs = ServiceRegistry.getRegistry().getService(GemConfigService.class);
		edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
		em = ManagerLocator.getInstance().getManager(EntityManager.class);
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

		List<String> columns = context.getColumns();

		if (context.outputSpecifyProperties() && !context.isOutputResult()) {

			for (String propName : columns) {
				PropertyDefinition pd = context.getPropertyDefinition(propName);
				int multi = pd.getMultiplicity();
				if (multi <= 1) {
					// カラム名を保存
					//FIXME ReferencePropertyのパターンがあるか(getColumnsでつぶしている)
					if (pd instanceof ReferenceProperty) {
						writeText(getColumnName(context, propName + "." + Entity.NAME));
					} else {
						writeText(getColumnName(context, propName));
					}
				} else {
					if (context.getMultipleFormat() == MultipleFormat.EACH_COLUMN) {
						for (int i = 0; i < multi; i++) {
							if (i != 0) {
								writeComma();
							}
							writeText(getColumnName(context, propName) + "[" + i + "]");
						}
					} else {
						writeText(getColumnName(context, propName));
					}
				}
				if (columns.indexOf(propName) < columns.size() - 1) {
					writeComma();
				}
			}
		} else {
			SearchConditionSection section = context.getConditionSection();

			if (!section.isNonOutputOid()) {
				writeText(context.getEntityLabel() + "(ID)");
				writeComma();
			}
			for (String propName : columns) {
				PropertyDefinition pd = context.getPropertyDefinition(propName);
				int multi = pd.getMultiplicity();
				if (multi <= 1) {
					// カラム名を保存
					//FIXME ReferencePropertyのパターンがあるか(getColumnsでつぶしている)
					if (pd instanceof ReferenceProperty) {
						if (!section.isNonOutputReference()) {
							if (!section.isNonOutputOid()) {
								writeText(getColumnName(context, propName + "." + Entity.OID));
								writeComma();
							}
							writeText(getColumnName(context, propName + "." + Entity.NAME));
							if (columns.indexOf(propName) < columns.size() - 1) {
								writeComma();
							}
						}
					} else if (pd instanceof BinaryProperty) {
						if (!section.isNonOutputBinaryRef()) {
							writeText(getColumnName(context, propName));
							if (columns.indexOf(propName) < columns.size() - 1) {
								writeComma();
							}
						}
					} else {
						writeText(getColumnName(context, propName));
						if (columns.indexOf(propName) < columns.size() - 1) {
							writeComma();
						}
					}
				} else {
					if (context.getMultipleFormat() == MultipleFormat.EACH_COLUMN) {
						for (int i = 0; i < multi; i++) {
							if (i != 0) {
								writeComma();
							}
							writeText(getColumnName(context, propName) + "[" + i + "]");
						}
					} else {
						writeText(getColumnName(context, propName));
					}
					if (columns.indexOf(propName) < columns.size() - 1) {
						writeComma();
					}
				}
			}
		}
		writeCR();

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

		searchEntity(query, columns);

		writeFooter();

		writer.flush();
	}

	private void searchEntity(Query query, List<String> columns) {

		PropertyColumn createBy = context.getPropertyColumn(Entity.CREATE_BY);
		PropertyColumn updateBy = context.getPropertyColumn(Entity.UPDATE_BY);
		PropertyColumn lockedBy = context.getPropertyColumn(Entity.LOCKED_BY);

		Map<String, Boolean> userMap = new HashMap<>();
		userMap.put(Entity.CREATE_BY, createBy != null && createBy.getEditor() instanceof UserPropertyEditor);
		userMap.put(Entity.UPDATE_BY, updateBy != null && updateBy.getEditor() instanceof UserPropertyEditor);
		userMap.put(Entity.LOCKED_BY, lockedBy != null && lockedBy.getEditor() instanceof UserPropertyEditor);

		int cacheLimit = gcs.getSearchResultCacheLimit();

		SearchQueryInterrupterHandler handler = context.getSearchQueryInterrupterHandler(false);

		new CSVDownloadTransPredicate(handler, userMap, cacheLimit).searchEntity(
				query, new CSVDownloadPredicate(this, columns, context));
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
	 * CSVカラム用の名前を取得
	 * @param context
	 * @param propName
	 * @return
	 */
	private String getColumnName(CsvDownloadSearchContext context, String propName) {
		//表示項目出力⇒表示名を利用
		return context.getDisplayLabel(propName);
	}

	private class CSVDownloadTransPredicate {

		private SearchQueryInterrupterHandler handler;
		private Map<String, Boolean> userMap;
		private int cacheLimit;

		/**
		 * コンストラクタ
		 * @param context	CSV出力情報
		 * @param userMap UserPropertyEditorが設定されているプロパティ情報
		 */
		public CSVDownloadTransPredicate(SearchQueryInterrupterHandler handler, Map<String, Boolean> userMap, int cacheLimit) {
			this.handler = handler;
			this.userMap = userMap;
			this.cacheLimit = cacheLimit;
		}

		/**
		 * @param dataModel エンティティ（１行分）
		 */
		public void searchEntity(Query query, final CSVDownloadPredicate callback) {

			final SearchQueryContext sqc = handler.beforeSearch(query.versioned(true), SearchQueryType.CSV);

			if (sqc.isDoPrivileged()) {
				AuthContext.doPrivileged(() -> searchEntity(sqc, callback));
			} else {
				if (sqc.getWithoutConditionReferenceName() != null) {
					EntityPermission.doQueryAs(sqc.getWithoutConditionReferenceName(), () -> {
						searchEntity(sqc, callback);
						return null;
					});
				} else {
					searchEntity(sqc, callback);
				}
			}
		}

		private void searchEntity(final SearchQueryContext sqc, final CSVDownloadPredicate callback) {

			final List<Entity> tmpResult = new ArrayList<>();
			final Set<String> userOides = new HashSet<>();

			em.searchEntity(sqc.getQuery(), new Predicate<Entity>() {

				@Override
				public boolean test(Entity entity) {
					handler.afterSearch(sqc.getQuery(), entity, SearchQueryType.CSV);

					tmpResult.add(entity);

					if (userMap.get(Entity.CREATE_BY)) {
						userOides.add(entity.getCreateBy());
					}
					if (userMap.get(Entity.UPDATE_BY)) {
						userOides.add(entity.getUpdateBy());
					}
					if (userMap.get(Entity.LOCKED_BY)) {
						userOides.add(entity.getLockedBy());
					}

					if (tmpResult.size() == cacheLimit) {

						//ユーザ情報を反映
						setUserProperty(tmpResult, userMap, userOides);

						//指定されたcallbackの呼び出し
						for (Entity afterModel : tmpResult) {
							if (!callback.test(afterModel)) {
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

			setUserProperty(tmpResult, userMap, userOides);
			for (Entity afterModel : tmpResult) {
				if (!callback.test(afterModel)) {
					return;
				}
			}
		}

		/**
		 * Userプロパティ情報をデータにセットします。
		 *
		 * @param tmpResult
		 * @param userUseMap
		 * @param userOides
		 */
		private void setUserProperty(List<Entity> tmpResult, Map<String, Boolean> userUseMap, Set<String> userOides) {

			// 対象項目、対象データがない場合は終了
			if (userUseMap.isEmpty() || userOides.isEmpty()) {
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

				if (userUseMap.get(Entity.CREATE_BY)) {
					String oid = entity.getCreateBy();
					if (userMap.containsKey(oid)) {
						entity.setCreateBy(userMap.get(oid));
					}
				}
				if (userUseMap.get(Entity.UPDATE_BY)) {
					String oid = entity.getUpdateBy();
					if (userMap.containsKey(oid)) {
						entity.setUpdateBy(userMap.get(oid));
					}
				}
				if (userUseMap.get(Entity.LOCKED_BY)) {
					String oid = entity.getLockedBy();
					if (userMap.containsKey(oid)) {
						entity.setLockedBy(userMap.get(oid));
					}
				}

			}
		}
	}

	/**
	 * 結果を１行ずつ取得し、StringBufferにCSV形式で追加するクラス
	 * @author S.Kimura
	 */
	private class CSVDownloadPredicate implements Predicate<Entity> {

		private CSVDownloadSearchViewWriter writer;

		/** カラム情報 */
		private List<String> columns;
		/** 処理結果数 */
		private CsvDownloadSearchContext context;

		/**
		 * コンストラクタ
		 * @param stringBuffer	結果保存用のStringBuffer
		 * @param columns カラム情報
		 */
		public CSVDownloadPredicate(CSVDownloadSearchViewWriter writer, List<String> columns, CsvDownloadSearchContext context) {
			this.writer = writer;
			this.columns = columns;
			this.context = context;
		}

		/**
		 * @param dataModel エンティティ（１行分）
		 */
		@Override
		public boolean test(Entity entity) {

			if (context.outputSpecifyProperties() && !context.isOutputResult()) {
				// 設定された項目だけを出力

				for (Iterator<String> itc = columns.iterator(); itc.hasNext();) {
					String propName = itc.next();
					PropertyDefinition pd = context.getPropertyDefinition(propName);
					Object value = entity.getValue(propName);

					if (value instanceof Object[]) {
						//多重度複数の場合
						int multi = pd.getMultiplicity();
						Object[] array = (Object[]) value;
						// TODO Tree型は、getMultiplicity()で-1

						outputMultipleValue(pd, propName, multi, array);

					} else if (value != null && value.toString().length() != 0) {
						// referenceの場合の処理を追加
						//FIXME ReferencePropertyのパターンがあるか(getColumnsでつぶしている)
						if (pd instanceof ReferenceProperty) {
							Entity reference = (Entity) value;
							writer.writeText(reference.getName());
						} else if (pd instanceof SelectProperty && context.isOutputCodeValue()) {
							writeValue(value, pd);
						} else if (pd instanceof ExpressionProperty && context.isOutputCodeValue()) {
							ExpressionProperty ep = (ExpressionProperty)pd;
							//ResultTypeSpecがSelectPropertyの場合はコードで出力
							if (ep.getResultTypeSpec() != null && ep.getResultTypeSpec() instanceof SelectProperty) {
								writeValue(value, ep.getResultTypeSpec());
							} else {
								writer.writeText(getToString(value));
							}
						} else {
							writer.writeText(getToString(value));
						}
					} else if (value == null) {
						//多重度複数の場合
						int multi = pd.getMultiplicity();
						if (context.getMultipleFormat() == MultipleFormat.EACH_COLUMN
								&& !(pd instanceof ReferenceProperty) && multi > 1) {
							for (int i = 0; i < multi - 1; i++) {
								writer.writeComma();
							}
						}
					}
					if (itc.hasNext()) {	//	次がある場合、カンマを挿入
						writer.writeComma();
					}
				}

				writer.writeCR();

			} else {

				SearchConditionSection section = context.getConditionSection();

				// OIDは先頭に。
				if (!section.isNonOutputOid()) {
					writer.writeText(entity.getOid());
					writer.writeComma();
				}

				for (Iterator<String> itc = columns.iterator(); itc.hasNext();) {
					String propName = itc.next();
					PropertyDefinition pd = context.getPropertyDefinition(propName);
					Object value = entity.getValue(propName);

					if (value instanceof Object[]) {
						//多重度複数の場合(Reference、Expressionは対象にならない)
						int multi = pd.getMultiplicity();
						Object[] array = (Object[]) value;
						// TODO Tree型は、getMultiplicity()で-1

						outputMultipleValue(pd, propName, multi, array);

						if (itc.hasNext()) {	//	次がある場合、カンマを挿入
							writer.writeComma();
						}

					} else if (value != null && !value.toString().isEmpty()) {

						// referenceの場合の処理を追加
						//FIXME ReferencePropertyのパターンがあるか(getColumnsでつぶしている)
						if (pd instanceof ReferenceProperty) {

							Entity reference = (Entity) value;
							if (!section.isNonOutputReference()) {
								if (!section.isNonOutputOid()) {
									writer.writeText(reference.getOid());
									writer.writeComma();
								}
								writer.writeText(reference.getName());
								if (itc.hasNext()) {	//	次がある場合、カンマを挿入
									writer.writeComma();
								}
							}
						} else if (pd instanceof BinaryProperty) {
							BinaryReference bin = (BinaryReference) value;
							if (!section.isNonOutputBinaryRef()) {
								writer.writeText(bin.getName());
								if (itc.hasNext()) {	//	次がある場合、カンマを挿入
									writer.writeComma();
								}
							}
						} else if (pd instanceof SelectProperty && context.isOutputCodeValue()) {
							writeValue(value, pd);
							if (itc.hasNext()) {	//	次がある場合、カンマを挿入
								writer.writeComma();
							}
						} else if (pd instanceof ExpressionProperty && context.isOutputCodeValue()) {
							ExpressionProperty ep = (ExpressionProperty)pd;
							//ResultTypeSpecがSelectPropertyの場合はコードで出力
							if (ep.getResultTypeSpec() != null && ep.getResultTypeSpec() instanceof SelectProperty) {
								writeValue(value, ep.getResultTypeSpec());
							} else {
								writer.writeText(getToString(value));
							}
							if (itc.hasNext()) {	//	次がある場合、カンマを挿入
								writer.writeComma();
							}
						} else {
							writer.writeText(getToString(value));
							if (itc.hasNext()) {	//	次がある場合、カンマを挿入
								writer.writeComma();
							}
						}

					} else if (value == null) {
						//多重度複数の場合
						int multi = pd.getMultiplicity();
						if (context.getMultipleFormat() == MultipleFormat.EACH_COLUMN
								&& !(pd instanceof ReferenceProperty) && multi > 1) {
							for (int i = 0; i < multi - 1; i++) {
								writer.writeComma();
							}
						} else if (pd instanceof ReferenceProperty) {
							//FIXME ReferencePropertyのパターンがあるか(getColumnsでつぶしている)
							if (!section.isNonOutputReference()) {
								if (!section.isNonOutputOid()) {
									writer.writeComma();
								}
							}
						}
						if (itc.hasNext()) {	//	次がある場合、カンマを挿入
							writer.writeComma();
						}
					} else {
						if (pd instanceof ReferenceProperty) {
							//FIXME ReferencePropertyのパターンがあるか(getColumnsでつぶしている)
							if (!section.isNonOutputReference()) {
								if (!section.isNonOutputOid()) {
									writer.writeComma();
								}
								if (itc.hasNext()) {	//	次がある場合、カンマを挿入
									writer.writeComma();
								}
							}
						} else {
							if (itc.hasNext()) {	//	次がある場合、カンマを挿入
								writer.writeComma();
							}
						}
					}
				}

				writer.writeCR();
			}
			return true;
		}

		private void outputMultipleValue(PropertyDefinition pd, String propName, int multi, Object[] array) {

			if (pd instanceof SelectProperty) {
				outputSelectValue(propName, (SelectProperty) pd, multi, array, context.isOutputCodeValue());
			}else {
				//SelectProperty以外は通常出力
				if (context.getMultipleFormat() == MultipleFormat.EACH_COLUMN) {
					outputMultipleValueSplit(pd, multi, array);
				} else {
					outputMultipleValueUnSplit(pd, multi, array);
				}
			}
		}

		private void outputMultipleValueSplit(PropertyDefinition pd, int multi, Object[] array) {

			//SelectProperty以外は通常出力
			for (int i = 0; i < multi; i++) {

				// 配列の分ループ
				if (array.length - 1 >= i && array[i] != null) {
					writer.writeText(getToString(array[i]));
				}

				if (i < multi - 1) {
					writer.writeComma();
				}
			}
		}

		private void outputMultipleValueUnSplit(PropertyDefinition pd, int multi, Object[] array) {

			int arrayLen = array.length;
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < multi; i++) {
				if (i >= arrayLen && context.getMultipleFormat() == MultipleFormat.ONE_COLUMN) {
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

				String strValue = getToString(array[i]);
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

		private void outputSelectValue(String propName, SelectProperty sp, int multi, Object[] array, boolean isOutputCodeValue) {
			//PropertyColumnのEditorから出力方式を取得
			boolean sortValue = false;
			PropertyColumn pc = context.getPropertyColumn(propName);
			if (pc != null && pc.getEditor() instanceof SelectPropertyEditor) {
				SelectPropertyEditor spe = (SelectPropertyEditor)pc.getEditor();
				if (spe.isSortCsvOutputValue()) {
					sortValue = true;
				}
			}
			if (sortValue) {
				//ソート出力

				List<SelectValue> selectableValues = sp.getSelectValueList();
				List<SelectValue> sortedValues = new ArrayList<>(selectableValues.size());
				for (int i = 0; i < multi; i++) {
					if (selectableValues.size() < i + 1) {
						//選択値より多重度が多い場合(念のためチェック)
						if (i < multi - 1) {
							sortedValues.add(null);
						}
						continue;
					}
					SelectValue targetValue = selectableValues.get(i);
					boolean exist = false;
					for (Object storeValue : array) {
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
				if (context.getMultipleFormat() == MultipleFormat.EACH_COLUMN) {
					outputMultipleValueSplit(sp, multi, sortedValues.toArray());
				} else {
					outputMultipleValueUnSplit(sp, multi, sortedValues.toArray());
				}

			} else {
				//通常出力
				if (context.getMultipleFormat() == MultipleFormat.EACH_COLUMN) {
					outputMultipleValueSplit(sp, multi, array);
				} else {
					outputMultipleValueUnSplit(sp, multi, array);
				}
			}
		}

		/**
		 * Object型のtoString()した値を返しますが、
		 * BigDecimalは、toPlainString()の値を返します
		 * ※JDK5よりtoString()で指数を返すことがあるため
		 * @param obj オブジェクト
		 * @return toString()またはtoPlainString()した値
		 */
		private String getToString(Object obj) {
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

		private void writeValue(Object val, PropertyDefinition pd) {
			if (val == null) {
				return;
			}

			switch (pd.getType()) {
				case EXPRESSION:
					ExpressionProperty ep = (ExpressionProperty)pd;
					if (ep.getResultType() != null) {
						writeValue(val, ep.getResultType());
					} else {
						writeValue(val, ep.getType());
					}
					break;
				case REFERENCE:
					ReferenceProperty rpd = (ReferenceProperty) pd;
					EntityDefinition ed = edm.get(rpd.getObjectDefinitionName());
					if (rpd.getMultiplicity() == 1) {
						Entity entity = (Entity) val;
						if (ed.getVersionControlType().equals(VersionControlType.NONE) && !gcs.isCsvDownloadReferenceVersion()) {
							writer.writeText(entity.getOid());
						} else {
							writer.writeText(entity.getOid() + "." + entity.getVersion());
						}
					} else {
						Entity[] eList = (Entity[]) val;
						StringBuilder sb = new StringBuilder();
						for (int i = 0; i < eList.length; i++) {
							if (i != 0) {
								sb.append(",");
							}
							if (eList[i] != null) {
								if (ed.getVersionControlType().equals(VersionControlType.NONE) && !gcs.isCsvDownloadReferenceVersion()) {
									sb.append(eList[i].getOid());
								} else {
									sb.append(eList[i].getOid() + "." + eList[i].getVersion());
								}
							}
						}
						writer.writeText(sb.toString());
					}
					break;
				default:
					writeValue(val, pd.getType());
			}
		}

		private void writeValue(Object val, PropertyDefinitionType type) {

			switch (type) {
				case BOOLEAN:
					Boolean b = (Boolean) val;
					writer.writeText(b.booleanValue() ? "1" : "0");
					break;
				case DATE:
					writer.writeText(DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getOutputDateFormat(), false).format((java.sql.Date) val));
					break;
				case DATETIME:
					// Timezoneをtrueにするとアップロード時にもTimezoneが適用されるためダウンロード時はfalseにする。
					writer.writeText(DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getOutputDatetimeSecFormat(), false).format((Timestamp) val));
					break;
				case TIME:
					writer.writeText(DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getOutputTimeSecFormat(), false).format((Time) val));
					break;
				case SELECT:
					SelectValue sv = (SelectValue) val;
					if (context.isOutputCodeValue()) {
						writer.writeText(sv.getValue());
					} else {
						writer.writeText(sv.getDisplayName());
					}
					break;
				case DECIMAL:
					writer.writeText(((BigDecimal)val).toPlainString());
					break;
				case FLOAT:
					writer.writeText(BigDecimal.valueOf((Double)val).toPlainString());
					break;
				case INTEGER:
				case LONGTEXT:
				case STRING:
				case AUTONUMBER:
				case EXPRESSION:
					writer.writeText(val.toString());
					break;
				case BINARY:
				case REFERENCE:
					break;
				default:
					throw new EntityRuntimeException("can not convert from " + type + ":" + val);
			}
		}

	}

}
