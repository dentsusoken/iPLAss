/*
 * Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.gem.command.generic.upload;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.iplass.gem.GemConfigService;
import org.iplass.gem.command.Constants;
import org.iplass.gem.command.GemResourceBundleUtil;
import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.action.ActionMapping;
import org.iplass.mtp.command.annotation.action.ParamMapping;
import org.iplass.mtp.command.annotation.action.Result;
import org.iplass.mtp.command.annotation.action.Result.Type;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.GenericEntity;
import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.AutoNumberProperty;
import org.iplass.mtp.entity.definition.properties.BinaryProperty;
import org.iplass.mtp.entity.definition.properties.BooleanProperty;
import org.iplass.mtp.entity.definition.properties.DateProperty;
import org.iplass.mtp.entity.definition.properties.DateTimeProperty;
import org.iplass.mtp.entity.definition.properties.DecimalProperty;
import org.iplass.mtp.entity.definition.properties.ExpressionProperty;
import org.iplass.mtp.entity.definition.properties.FloatProperty;
import org.iplass.mtp.entity.definition.properties.IntegerProperty;
import org.iplass.mtp.entity.definition.properties.LongTextProperty;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.entity.definition.properties.SelectProperty;
import org.iplass.mtp.entity.definition.properties.StringProperty;
import org.iplass.mtp.entity.definition.properties.TimeProperty;
import org.iplass.mtp.impl.entity.fileport.EntityCsvWriteOption;
import org.iplass.mtp.impl.entity.fileport.EntityCsvWriter;
import org.iplass.mtp.impl.entity.fileport.EntityExcelWriteOption;
import org.iplass.mtp.impl.entity.fileport.EntityExcelWriter;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.DateUtil;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.utilityclass.definition.UtilityClassDefinitionManager;
import org.iplass.mtp.view.generic.EntityView;
import org.iplass.mtp.view.generic.EntityViewManager;
import org.iplass.mtp.view.generic.FormViewUtil;
import org.iplass.mtp.view.generic.SearchFormCsvUploadInterrupter;
import org.iplass.mtp.view.generic.SearchFormView;
import org.iplass.mtp.view.generic.element.property.PropertyColumn;
import org.iplass.mtp.view.generic.element.section.SearchConditionSection;
import org.iplass.mtp.view.generic.element.section.SearchConditionSection.FileSupportType;
import org.iplass.mtp.view.generic.element.section.SearchResultSection;
import org.iplass.mtp.web.ResultStreamWriter;
import org.iplass.mtp.web.template.TemplateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ActionMapping(name = EntityFileSampleDownloadCommand.ACTION_NAME, displayName = "サンプルファイルダウンロード", paramMapping = {
		@ParamMapping(name = Constants.DEF_NAME, mapFrom = "${0}", condition = "subPath.length==1"),
		@ParamMapping(name = Constants.VIEW_NAME, mapFrom = "${0}", condition = "subPath.length==2"),
		@ParamMapping(name = Constants.DEF_NAME, mapFrom = "${1}", condition = "subPath.length==2"), }, result = @Result(status = Constants.CMD_EXEC_SUCCESS, type = Type.STREAM, useContentDisposition = true))
@CommandClass(name = "gem/generic/upload/EntityFileSampleDownloadCommand", displayName = "サンプルファイルダウンロード")
public final class EntityFileSampleDownloadCommand implements Command {

	private static Logger logger = LoggerFactory.getLogger(EntityFileSampleDownloadCommand.class);

	public static final String ACTION_NAME = "gem/generic/upload/sample";

	private static final String DEFAULT_CHAR_SET = "UTF-8";

	private static final String ENTITY_NAME_BINDING_NAME = "entityName";
	private static final String ENTITY_DISP_BINDING_NAME = "entityDisplayName";
	private static final String VIEW_NAME_BINDING_NAME = "viewName";

	private EntityDefinitionManager edm = null;
	private EntityViewManager evm = null;
	private GemConfigService gcs = null;

	public EntityFileSampleDownloadCommand() {
		edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
		evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
		gcs = ServiceRegistry.getRegistry().getService(GemConfigService.class);
	}

	@Override
	public String execute(RequestContext request) {

		String defName = request.getParam(Constants.DEF_NAME);
		String viewName = request.getParam(Constants.VIEW_NAME);
		EntityDefinition ed = edm.get(defName);
		EntityView ev = evm.get(defName);

		FileDownloadSampleWriter writer = getWriter(request, ed, ev, viewName);

		// 保存ファイル名
		Map<String, Object> fileNameVariableMap = new HashMap<>();
		fileNameVariableMap.put(ENTITY_NAME_BINDING_NAME, ed.getName());
		fileNameVariableMap.put(ENTITY_DISP_BINDING_NAME,
				TemplateUtil.getMultilingualString(ed.getDisplayName(), ed.getLocalizedDisplayNameList()));
		fileNameVariableMap.put(VIEW_NAME_BINDING_NAME, StringUtil.isEmpty(viewName) ? null : viewName);
		String defaultName = TemplateUtil.getMultilingualString(ed.getDisplayName(), ed.getLocalizedDisplayNameList());
		String filename = evm.getEntityDownloadFileName(defName, viewName, defaultName, fileNameVariableMap) + writer.getFileExtension();
		request.setAttribute(Constants.CMD_RSLT_STREAM_FILENAME, filename);
		request.setAttribute(Constants.CMD_RSLT_STREAM_CONTENT_TYPE, writer.getFileContentType());

		request.setAttribute(Constants.CMD_RSLT_STREAM, writer);

		return Constants.CMD_EXEC_SUCCESS;
	}

	private String getCharacterCode(RequestContext request) {
		if (StringUtil.isEmpty(request.getParam(Constants.CSV_CHARACTER_CODE))) {
			return DEFAULT_CHAR_SET;
		} else {
			return request.getParam(Constants.CSV_CHARACTER_CODE);
		}
	}

	private FileDownloadSampleWriter getWriter(RequestContext request, EntityDefinition ed, EntityView ev, String viewName) {

		SearchFormView form = FormViewUtil.getSearchFormView(ed, ev, viewName);
		SearchConditionSection condition = form != null ? form.getCondSection() : null;
		SearchResultSection result = form != null ? form.getResultSection() : null;

		FileSupportType fileSupportType = null;
		if (condition != null) {
			fileSupportType = condition.getFileSupportType();
		}

		// 未指定の場合は、GemConfigServiceから取得
		if (fileSupportType == null) {
			fileSupportType = gcs.getFileSupportType();
		}

		if (fileSupportType == FileSupportType.SPECIFY) {
			String specifyType = request.getParam(Constants.FILE_SUPPORT_TYPE);
			if (FileSupportType.CSV.name().equals(specifyType)) {
				fileSupportType = FileSupportType.CSV;
			} else if (FileSupportType.EXCEL.name().equals(specifyType)) {
				fileSupportType = FileSupportType.EXCEL;
			} else {
                fileSupportType = FileSupportType.CSV;
            }
		}

		if (fileSupportType == FileSupportType.CSV) {
			CsvDownloadSampleWriter csvDownloadSampleWriter = new CsvDownloadSampleWriter(ed, condition, result);
			csvDownloadSampleWriter.setCharset(getCharacterCode(request));
			return csvDownloadSampleWriter;
		} else if (fileSupportType == FileSupportType.EXCEL) {
			return new ExcelDownloadSampleWriter(ed, condition, result);
		} else {
			logger.error("Unsupported file support type: " + fileSupportType);
			throw new ApplicationException(GemResourceBundleUtil.resourceString("command.generic.search.EntityFileDownloadSearchContext.internalErr"));
        }
	}

	private abstract class FileDownloadSampleWriter implements ResultStreamWriter {

		protected EntityDefinition ed;
		protected SearchConditionSection condition;
		protected SearchResultSection result;

		private SearchFormCsvUploadInterrupter interrupter;
		private Map<String, String> cusomColumnNameMap;

		public FileDownloadSampleWriter(EntityDefinition ed, SearchConditionSection condition, SearchResultSection result) {
			this.ed = ed;
			this.condition = condition;
			this.result = result;
		}

		/**
		 * ファイルのコンテントタイプを返します。
		 *
		 * @return ファイルのコンテントタイプ
		 */
		public abstract String getFileContentType();

		/**
		 * ファイルの拡張子を返します。
		 *
		 * @return ファイルの拡張子
		 */
		public abstract String getFileExtension();

		protected SearchFormCsvUploadInterrupter getSearchFormCsvUploadInterrupter() {
			if (interrupter != null) {
				return interrupter;
			}

			if (condition != null && StringUtil.isNotEmpty(condition.getCsvUploadInterrupterName())) {
				UtilityClassDefinitionManager ucdm = ManagerLocator.getInstance()
						.getManager(UtilityClassDefinitionManager.class);
				try {
					interrupter = ucdm.createInstanceAs(SearchFormCsvUploadInterrupter.class,
							condition.getCsvUploadInterrupterName());
				} catch (ClassNotFoundException e) {
					logger.error(condition.getCsvUploadInterrupterName() + " can not instantiate.", e);
					throw new ApplicationException(GemResourceBundleUtil
							.resourceString("command.generic.upload.CsvSampleDownloadCommand.internalErr"));
				}
			} else {
				interrupter = new SearchFormCsvUploadInterrupter() {
				};
			}
			return interrupter;
		}

		/**
		 * プロパティ名に対する出力CSV列名のマッピング定義を返します。
		 *
		 * @return プロパティ名に対する出力CSV列名のマッピング定義
		 */
		private Map<String, String> getCustomColumnNameMap() {

			if (cusomColumnNameMap != null) {
				return cusomColumnNameMap;
			}

			SearchFormCsvUploadInterrupter csvUploadInterrupter = getSearchFormCsvUploadInterrupter();

			cusomColumnNameMap = csvUploadInterrupter.columnNameMap(ed);

			// 再作成しないように初期化
			if (cusomColumnNameMap == null) {
				cusomColumnNameMap = Collections.emptyMap();
			}
			return cusomColumnNameMap;
		}

		/**
		 * 出力列名を返します。
		 *
		 * @param property プロパティ定義
		 * @return 出力列名
		 */
		protected String getColumnName(PropertyDefinition property) {

			// 出力列名のカスタマイズチェック
			String columnName = getCustomColumnNameMap().get(property.getName());
			if (StringUtil.isNotEmpty(columnName)) {
				return columnName;
			}

			if (condition != null && condition.isNonOutputDisplayName()) {
				return property.getName();
			} else {
				return property.getName() + "(" + getColumnLabel(property) + ")";
			}
		}

		/**
		 * 多重度複数プロパティの出力列名を返します。
		 *
		 * @param property プロパティ定義
		 * @param index    多重度のIndex
		 * @return 出力列名
		 */
		protected String getMultipleColumnName(PropertyDefinition property, int index) {

			// 出力列名のカスタマイズチェック
			String columnName = getCustomColumnNameMap().get(property.getName());
			if (StringUtil.isNotEmpty(columnName)) {
				return columnName + "[" + index + "]";
			}

			if (condition != null && condition.isNonOutputDisplayName()) {
				return property.getName() + "[" + index + "]";
			} else {
				return property.getName() + "[" + index + "]" + "(" + getColumnLabel(property) + ")";
			}

		}

		/**
		 * 出力列名のラベルを返します。
		 *
		 * @param property プロパティ定義
		 * @return 出力列名のラベル
		 */
		private String getColumnLabel(PropertyDefinition property) {
			// 画面定義からカラムの表示ラベル取得
			PropertyColumn column = getPropertyColumn(property);
			if (column != null && StringUtil.isNotEmpty(column.getDisplayLabel())) {
				String displayLabel = TemplateUtil.getMultilingualString(column.getDisplayLabel(),
						column.getLocalizedDisplayLabelList());
				if (displayLabel != null) {
					return displayLabel;
				}
			}

			// 取れない場合はEntity定義から取得
			return TemplateUtil.getMultilingualString(property.getDisplayName(),
					property.getLocalizedDisplayNameList());
		}

		/**
		 * 検索結果設定から対象のプロパティ設定を取得します。
		 *
		 * @param property プロパティ定義
		 * @return 検索結果プロパティ設定
		 */
		private PropertyColumn getPropertyColumn(PropertyDefinition property) {

			if (result == null) {
				return null;
			}

			Optional<PropertyColumn> column = result.getElements().stream().filter(e -> e instanceof PropertyColumn)
					.map(e -> (PropertyColumn) e).filter(e -> property.getName().equals(e.getPropertyName()))
					.findFirst();
			if (column.isPresent()) {
				return column.get();
			}
			return null;
		}

		/**
		 * 出力するEntityデータを生成します。
		 *
		 * @param properties 出力対象プロパティ
		 * @return Entityデータ
		 */
		protected Entity createDummyEntity(List<PropertyDefinition> properties) {

			Entity entity = new GenericEntity();

			for (PropertyDefinition pd : properties) {
				String propName = pd.getName();

				if (propName.equals(Entity.OID)) {
					entity.setOid(RandomStringUtils.random(1, "123456789") + RandomStringUtils.randomNumeric(7));
					continue;
				}

				if (propName.equals(Entity.STATE)) {
					entity.setState(new SelectValue(Entity.STATE_VALID_VALUE));
					continue;
				}

				String createBy = "mtp" + RandomStringUtils.random(1, "123456789") + RandomStringUtils.randomNumeric(3);
				if (propName.equals(Entity.CREATE_BY)) {
					entity.setCreateBy(createBy);
					continue;
				}

				String updateBy = "mtp" + RandomStringUtils.random(1, "123456789") + RandomStringUtils.randomNumeric(3);
				if (propName.equals(Entity.UPDATE_BY)) {
					entity.setUpdateBy(updateBy);
					continue;
				}

				if (pd instanceof BinaryProperty) {
					// Binayは何もしない
					continue;
				} else if (pd instanceof BooleanProperty) {
					if (pd.getMultiplicity() == 1) {
						entity.setValue(propName, true);
					} else {
						entity.setValue(propName, new Boolean[] { true, false });
					}
					continue;
				} else if (pd instanceof DateProperty) {
					if (pd.getMultiplicity() == 1) {
						entity.setValue(propName, DateUtil.getCurrentDate());
					} else {
						entity.setValue(propName, new Date[] { DateUtil.getCurrentDate(), DateUtil.getCurrentDate() });
					}
					continue;
				} else if (pd instanceof DateTimeProperty) {
					if (pd.getMultiplicity() == 1) {
						entity.setValue(propName, DateUtil.getCurrentTimestamp());
					} else {
						entity.setValue(propName,
								new Timestamp[] { DateUtil.getCurrentTimestamp(), DateUtil.getCurrentTimestamp() });
					}
					continue;
				} else if (pd instanceof DecimalProperty) {
					if (pd.getMultiplicity() == 1) {
						entity.setValue(propName, new BigDecimal(
								RandomStringUtils.random(1, "123456789") + RandomStringUtils.randomNumeric(7)));
					} else {
						entity.setValue(propName,
								new BigDecimal[] {
										new BigDecimal(RandomStringUtils.random(1, "123456789")
												+ RandomStringUtils.randomNumeric(7)),
										new BigDecimal(RandomStringUtils.random(1, "123456789")
												+ RandomStringUtils.randomNumeric(7)) });
					}
					continue;
				} else if (pd instanceof ExpressionProperty) {
					// Expressionは何もしない
					continue;
				} else if (pd instanceof FloatProperty) {
					if (pd.getMultiplicity() == 1) {
						entity.setValue(propName, new Double(
								RandomStringUtils.random(1, "123456789") + RandomStringUtils.randomNumeric(7)));
					} else {
						entity.setValue(propName,
								new Double[] {
										new Double(RandomStringUtils.random(1, "123456789")
												+ RandomStringUtils.randomNumeric(7)),
										new Double(RandomStringUtils.random(1, "123456789")
												+ RandomStringUtils.randomNumeric(7)) });
					}
					continue;
				} else if (pd instanceof IntegerProperty) {
					if (pd.getMultiplicity() == 1) {
						entity.setValue(propName, new Long(
								RandomStringUtils.random(1, "123456789") + RandomStringUtils.randomNumeric(7)));
					} else {
						entity.setValue(propName, new Long[] {
								new Long(RandomStringUtils.random(1, "123456789") + RandomStringUtils.randomNumeric(7)),
								new Long(RandomStringUtils.random(1, "123456789")
										+ RandomStringUtils.randomNumeric(7)) });
					}
					continue;
				} else if (pd instanceof LongTextProperty) {
					if (pd.getMultiplicity() == 1) {
						entity.setValue(propName, "ロングテキスト\nサンプル");
					} else {
						entity.setValue(propName, new String[] { "ロングテキスト\nサンプル", "ロングテキスト\nサンプル" });
					}
					continue;
				} else if (pd instanceof ReferenceProperty) {
					if (pd.getMultiplicity() == 1) {
						Entity refEntity = new GenericEntity();
						refEntity.setOid(RandomStringUtils.random(1, "123456789") + RandomStringUtils.randomNumeric(7));
						refEntity.setVersion(new Long(0));
						entity.setValue(propName, refEntity);
					} else {
						Entity ref1Entity = new GenericEntity();
						ref1Entity
								.setOid(RandomStringUtils.random(1, "123456789") + RandomStringUtils.randomNumeric(7));
						ref1Entity.setVersion(new Long(0));
						Entity ref2Entity = new GenericEntity();
						ref2Entity
								.setOid(RandomStringUtils.random(1, "123456789") + RandomStringUtils.randomNumeric(7));
						ref2Entity.setVersion(new Long(0));
						entity.setValue(propName, new Entity[] { ref1Entity, ref2Entity });
					}
					continue;
				} else if (pd instanceof SelectProperty) {
					if (pd.getMultiplicity() == 1) {
						SelectValue selectValue = ((SelectProperty) pd).getSelectValueList().get(0);
						entity.setValue(propName, selectValue);
					} else {
						SelectValue selectValue1 = ((SelectProperty) pd).getSelectValueList().get(0);
						if (((SelectProperty) pd).getSelectValueList().size() > 1) {
							SelectValue selectValue2 = ((SelectProperty) pd).getSelectValueList().get(1);
							entity.setValue(propName, new SelectValue[] { selectValue1, selectValue2 });
						} else {
							entity.setValue(propName, new SelectValue[] { selectValue1 });
						}
					}
					continue;
				} else if (pd instanceof StringProperty) {
					if (pd.getMultiplicity() == 1) {
						entity.setValue(propName, RandomStringUtils.random(10, "abcdefghtjklmnopqrstuvwxyz"));
					} else {
						entity.setValue(propName,
								new String[] { RandomStringUtils.random(10, "abcdefghtjklmnopqrstuvwxyz"),
										RandomStringUtils.random(10, "abcdefghtjklmnopqrstuvwxyz"), });
					}
					continue;
				} else if (pd instanceof TimeProperty) {
					if (pd.getMultiplicity() == 1) {
						entity.setValue(propName, DateUtil.getCurrentTime());
					} else {
						entity.setValue(propName, new Time[] { DateUtil.getCurrentTime(), DateUtil.getCurrentTime() });
					}
					continue;
				} else if (pd instanceof AutoNumberProperty) {
					if (pd.getMultiplicity() == 1) {
						entity.setValue(propName,
								RandomStringUtils.random(1, "123456789") + RandomStringUtils.randomNumeric(7));
					} else {
						entity.setValue(propName, new String[] {
								RandomStringUtils.random(1, "123456789") + RandomStringUtils.randomNumeric(7),
								RandomStringUtils.random(1, "123456789") + RandomStringUtils.randomNumeric(7) });
					}
					continue;
				} else {
					throw new EntityRuntimeException("can not convert property : " + pd.getClass().getName());
				}
			}

			setOidValue(entity);

			return entity;
		}

		private void setOidValue(Entity entity) {

			if (ed.getOidPropertyName() != null) {
				String oidValue = "";
				int cnt = 0;
				for (String propName : ed.getOidPropertyName()) {
					if (cnt == 0) {
						oidValue = entity.getValue(propName);
					} else {
						oidValue = oidValue + "-" + entity.getValue(propName);
					}
					cnt++;
				}
				if (StringUtil.isNotEmpty(oidValue)) {
					entity.setOid(oidValue);
				}
			}
		}

	}

	private class CsvDownloadSampleWriter extends FileDownloadSampleWriter {

		private String charset;

		public CsvDownloadSampleWriter(EntityDefinition ed, SearchConditionSection condition, SearchResultSection result) {
			super(ed, condition, result);
		}

		public void setCharset(String charset) {
			this.charset = charset;
		}

		@Override
		public String getFileContentType() {
			return "text/csv;charset=" + charset;
		}

		@Override
		public String getFileExtension() {
			return ".csv";
		}

		@Override
		public void write(OutputStream out) throws IOException {
			// 直接プロパティ指定
			List<String> directProperties = null;
			if (condition != null && condition.getCsvdownloadUploadableProperties() != null) {
				directProperties = new ArrayList<String>(condition.getCsvdownloadUploadablePropertiesSet());
			}

			// Writer生成
			EntityCsvWriteOption option = new EntityCsvWriteOption().charset(charset)
					.quoteAll(gcs.isCsvDownloadQuoteAll()).withReferenceVersion(gcs.isCsvDownloadReferenceVersion())
					.withMappedByReference(gcs.isUploadableCsvDownloadWithMappedByReference())
					.properties(directProperties).columnName(property -> getColumnName(property))
					.multipleColumnName((property, index) -> getMultipleColumnName(property, index));

			try (EntityCsvWriter writer = new EntityCsvWriter(ed, out, option)) {

				// 出力データの生成
				List<Entity> entities = getSearchFormCsvUploadInterrupter().sampleCsvData(ed, writer.getProperties());
				if (entities == null) {
					// ダミーエンティティの作成
					entities = new ArrayList<>(2);
					for (int i = 0; i < 2; i++) {
						entities.add(createDummyEntity(writer.getProperties()));
					}
				}

				// Header出力
				writer.writeHeader();

				// CSV レコードを出力
				entities.forEach(entity -> {
					writer.writeEntity(entity);
				});
			}
		}

	}

	private class ExcelDownloadSampleWriter extends FileDownloadSampleWriter {

		public ExcelDownloadSampleWriter(EntityDefinition ed, SearchConditionSection condition, SearchResultSection result) {
			super(ed, condition, result);
		}

		@Override
		public String getFileContentType() {
			return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
		}

		@Override
		public String getFileExtension() {
			return ".xlsx";
		}

		@Override
		public void write(OutputStream out) throws IOException {
			// 直接プロパティ指定
			List<String> directProperties = null;
			if (condition != null && condition.getCsvdownloadUploadableProperties() != null) {
				directProperties = new ArrayList<String>(condition.getCsvdownloadUploadablePropertiesSet());
			}

			// Writer生成
			EntityExcelWriteOption option = new EntityExcelWriteOption()
					.withReferenceVersion(gcs.isCsvDownloadReferenceVersion())
					.withMappedByReference(gcs.isUploadableCsvDownloadWithMappedByReference())
					.properties(directProperties).columnName(property -> getColumnName(property))
					.multipleColumnName((property, index) -> getMultipleColumnName(property, index));

			try (EntityExcelWriter writer = new EntityExcelWriter(ed, out, option)) {

				// 出力データの生成
				List<Entity> entities = getSearchFormCsvUploadInterrupter().sampleCsvData(ed, writer.getProperties());
				if (entities == null) {
					// ダミーエンティティの作成
					entities = new ArrayList<>(2);
					for (int i = 0; i < 2; i++) {
						entities.add(createDummyEntity(writer.getProperties()));
					}
				}

				// Header出力
				writer.writeHeader();

				writer.beforeWriteData();

				// CSV レコードを出力
				entities.forEach(entity -> {
					writer.writeEntity(entity);
				});

				writer.endData();
			}
		}
	}

}
