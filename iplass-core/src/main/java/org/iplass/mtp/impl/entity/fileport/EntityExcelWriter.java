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

package org.iplass.mtp.impl.entity.fileport;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.entity.definition.VersionControlType;
import org.iplass.mtp.entity.definition.properties.BinaryProperty;
import org.iplass.mtp.entity.definition.properties.ExpressionProperty;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.entity.definition.properties.SelectProperty;
import org.iplass.mtp.entity.permission.EntityPropertyPermission;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.util.CollectionUtil;
import org.iplass.mtp.util.DateUtil;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <p>EntityのExcelファイル出力クラス</p>
 */
public class EntityExcelWriter implements AutoCloseable, Flushable {

	// FIXME: EntityCsvWriterとの共通化の検討

	private static final Logger logger = LoggerFactory.getLogger(EntityExcelWriter.class);

	// Excelの日付出力形式、org.iplass.mtp.impl.i18n.LocaleFormatのoutputDateFormatに合わせる
	// FIXME: Optionでの書式設定の検討
	private static final String DATE_EXCEL_FORMAT = "yyyy/mm/dd";
	private static final String TIME_EXCEL_FORMAT = "hh:mm:ss";

	private EntityDefinition definition;
	private OutputStream out;

	private EntityExcelWriteOption option;
	private ZipOutputStream binaryStore;

	private AuthContext auth;
	private EntityDefinitionManager edm;
	private EntityManager em;
	private boolean isInit;
	private List<PropertyDefinition> properties;

	private String dateFormat;
	private String dateTimeFormat;
	private String timeFormat;
	private ObjectMapper mapper;

	private SXSSFWorkbook workbook;
	private SXSSFSheet sheet;

	private Row headerRow;
	private CellStyle headerCellStyle;

	private int rowIndex = 0;
	private CellStyle dataCellStyle;
	private CellStyle dateTimeCellStyle;
	private CellStyle dateCellStyle;
	private CellStyle timeCellStyle;
	private Row currentRow;

	public EntityExcelWriter(EntityDefinition definition, OutputStream out) throws IOException {
		this(definition, out, new EntityExcelWriteOption());
	}

	public EntityExcelWriter(EntityDefinition definition, OutputStream out, EntityExcelWriteOption option) throws IOException {
		this(definition, out, option, null);
	}

	public EntityExcelWriter(EntityDefinition definition, OutputStream out, EntityExcelWriteOption option, ZipOutputStream binaryStore) throws IOException {
		this.definition = definition;
		this.out = out;
		this.option = option;
		this.binaryStore = binaryStore;

		auth = AuthContext.getCurrentContext();
		edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
		em = ManagerLocator.getInstance().getManager(EntityManager.class);

		String sheetName = I18nUtil.stringDef(definition.getDisplayName(), definition.getLocalizedDisplayNameList());

		this.workbook = new SXSSFWorkbook();
		// this.workbook.setCompressTempFiles(true);

		this.sheet = workbook.createSheet(WorkbookUtil.createSafeSheetName(sheetName));
		// this.sheet.setRandomAccessWindowSize(100);

	}

	@Override
	public void flush() throws IOException {
	}

	@Override
	public void close() {

		if (workbook != null) {

			try {
				workbook.close();
			} catch (IOException e) {
				logger.warn("fail to close EntityExcelWriter resource. check whether resource is leak or not.", e);
			}
			workbook = null;
		}
	}

	public List<PropertyDefinition> getProperties() {
		init();

		return properties;
	}

	public void writeHeader() {
		init();

		headerRow = sheet.createRow(0);

		headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerCellStyle.setBorderLeft(BorderStyle.THIN);
		headerCellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		headerCellStyle.setBorderTop(BorderStyle.THIN);
		headerCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
		headerCellStyle.setBorderRight(BorderStyle.THIN);
		headerCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
		headerCellStyle.setBorderBottom(BorderStyle.THIN);
		headerCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());

		int columnIndex[] = {0};
		IntStream.range(0, properties.size()).forEach(i -> {

			PropertyDefinition property = properties.get(i);
			if (!(property instanceof ReferenceProperty) && property.getMultiplicity() != 1) {
				IntStream.range(0, property.getMultiplicity()).forEach(j -> {
					if (j != 0) {
						columnIndex[0]++;
					}
					writeHeaderColumn(columnIndex[0], option.getMultipleColumnName().apply(property, j));
				});
			} else {
				writeHeaderColumn(columnIndex[0], option.getColumnName().apply(property));
			}
			columnIndex[0]++;
		});
	}

	private void writeHeaderColumn(int columnIndex, String text) {
		Cell cell = headerRow.createCell(columnIndex);
		cell.setCellStyle(headerCellStyle);
		cell.setCellValue(text);
	}

	public void beforeWriteData() {
		dataCellStyle = workbook.createCellStyle();
		applyDataCellStyle(dataCellStyle);

		CreationHelper createHelper = workbook.getCreationHelper();

		dateCellStyle = workbook.createCellStyle();
		applyDataCellStyle(dateCellStyle);
		dateCellStyle.setDataFormat(
				createHelper.createDataFormat().getFormat(DATE_EXCEL_FORMAT));

		dateTimeCellStyle = workbook.createCellStyle();
		applyDataCellStyle(dateTimeCellStyle);
		dateTimeCellStyle.setDataFormat(createHelper.createDataFormat().getFormat(DATE_EXCEL_FORMAT + " " + TIME_EXCEL_FORMAT));

		timeCellStyle = workbook.createCellStyle();
		applyDataCellStyle(timeCellStyle);
		timeCellStyle.setDataFormat(
				createHelper.createDataFormat().getFormat(TIME_EXCEL_FORMAT));
	}

	/**
	 * データセルのスタイルを適用します。
	 *
	 * @param cellStyle セルスタイル
	 */
	private void applyDataCellStyle(CellStyle cellStyle) {
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
	}

	public void writeEntity(final Entity entity) {
		init();

		rowIndex++;
		currentRow = sheet.createRow(rowIndex);

		int columnIndex[] = {0};
		for (Iterator<PropertyDefinition> it = properties.iterator(); it.hasNext();) {
			PropertyDefinition property = it.next();

			if (!(property instanceof ReferenceProperty) && property.getMultiplicity() != 1) {
				Object[] values = (Object[])entity.getValue(property.getName());

				//SelectPropertyの出力方式を取得
				if (property instanceof SelectProperty && option.getSortSelectValue().apply((SelectProperty)property)) {

					//ソート出力
					SelectProperty sp = (SelectProperty)property;
					List<SelectValue> selectableValues = sp.getSelectValueList();

					IntStream.range(0, property.getMultiplicity()).forEach(i -> {
						if (i != 0) {
							columnIndex[0]++;
						}
						if (values == null || i > selectableValues.size() - 1) {
							//登録値がnullか選択可能値より多重度が多い場合
							writeValue(columnIndex[0], null, property, binaryStore);
						} else {
							final SelectValue targetValue = selectableValues.get(i);
							boolean exist = Arrays.stream(values).anyMatch(value ->
								value != null && ((SelectValue)value).getValue().equals(targetValue.getValue())
							);
							if (exist) {
								writeValue(columnIndex[0], targetValue, property, binaryStore);
							} else {
								writeValue(columnIndex[0], null, property, binaryStore);
							}
						}
					});

				}else {
					//通常出力
					IntStream.range(0, property.getMultiplicity()).forEach(i -> {
						if (i != 0) {
							columnIndex[0]++;
						}
						if (values == null || i >= values.length) {
							writeValue(columnIndex[0], null, property, binaryStore);
						} else {
							writeValue(columnIndex[0], values[i], property, binaryStore);
						}
					});
				}
			} else {
				Object val = entity.getValue(property.getName());
				writeValue(columnIndex[0], val, property, binaryStore);
			}

			if (it.hasNext()) {
				columnIndex[0]++;
			}
		}
	}

	public void endData() {
		try {
			workbook.write(out);
		} catch (IOException e) {
			throw new EntityCsvException("fail to write EntityExcelWriter.", e);
		}
	}

	protected void init() {
		if (isInit) {
			return;
		}

		//対象プロパティ取得
		List<PropertyDefinition> outputProperties = getOutputProperties();

		//対象プロパティの検証
		properties = outputProperties.stream()
				.filter(property ->
					// 参照可能権限を保持している場合のみ追加
					auth.checkPermission(
							new EntityPropertyPermission(definition.getName(), property.getName(), EntityPropertyPermission.Action.REFERENCE))
				)
				.filter(property ->
					// バージョン管理している場合のみversionプロパティを追加
					!Entity.VERSION.equals(property.getName()) || definition.getVersionControlType() != VersionControlType.NONE
				)
				.filter(property -> {
					//被参照の除外チェック
					return option.isWithMappedByReference()
							|| CollectionUtil.isNotEmpty(option.getProperties())
							|| (!(property instanceof ReferenceProperty) || ((ReferenceProperty)property).getMappedBy() == null);
				})
				.filter(property -> {
					//BinaryPropertyの除外チェック
					return option.isWithBinary()
							|| CollectionUtil.isNotEmpty(option.getProperties())
							|| !(property instanceof BinaryProperty);
				})
				.collect(Collectors.toList());

		isInit = true;
	}

	/**
	 * 出力対象のプロパティを返します。
	 *
	 * @return 出力対象のプロパティ
	 */
	private List<PropertyDefinition> getOutputProperties() {
		List<PropertyDefinition> outputProperties = null;
		if (CollectionUtil.isNotEmpty(option.getProperties())) {
			// 出力プロパティの直接指定
			outputProperties = new ArrayList<>();

			// バージョンの出力要否は後続の検証によりチェック
			outputProperties.add(definition.getProperty(Entity.OID));
			outputProperties.add(definition.getProperty(Entity.VERSION));

			outputProperties.addAll(option.getProperties().stream()
					.filter(propertyName ->
						!(propertyName.equals(Entity.OID) || propertyName.equals(Entity.VERSION))
					)
					.map(propertyName -> {
						PropertyDefinition property = definition.getProperty(propertyName);
						if (property == null) {
							throw new EntityCsvException(propertyName + " is invalid property in " + definition.getName());
						}
						return property;
					})
					.collect(Collectors.toList()));

		} else {
			outputProperties = definition.getPropertyList();
		}
		return outputProperties;
	}

	private void writeText(Cell cell, String text) {

		if (StringUtil.isEmpty(text)) {
			return;
		}

		cell.setCellValue(text);
	}

	private void writeValue(int columnIndex, final Object val, final PropertyDefinition pd, final ZipOutputStream binaryStore) {

		Cell cell = currentRow.createCell(columnIndex);
		cell.setCellStyle(getCellStyle(pd));

		if (val == null) {
			return;
		}

		switch (pd.getType()) {
			case EXPRESSION:
				ExpressionProperty ep = (ExpressionProperty)pd;
				if (ep.getResultType() != null) {
					writeValue(cell, val, ep.getResultType());
				} else {
					writeValue(cell, val, ep.getType());
				}
				break;
			case REFERENCE:
				ReferenceProperty rpd = (ReferenceProperty) pd;
				EntityDefinition ed = edm.get(rpd.getObjectDefinitionName());
				if (rpd.getMultiplicity() == 1) {
					Entity entity = (Entity) val;
					if (ed.getVersionControlType().equals(VersionControlType.NONE) && !option.isWithReferenceVersion()) {
						writeText(cell, entity.getOid());
					} else {
						writeText(cell, entity.getOid() + "." + entity.getVersion());
					}
				} else {
					Entity[] eList = (Entity[]) val;
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < eList.length; i++) {
						if (i != 0) {
							sb.append(",");
						}
						if (eList[i] != null) {
							if (ed.getVersionControlType().equals(VersionControlType.NONE) && !option.isWithReferenceVersion()) {
								sb.append(eList[i].getOid());
							} else {
								sb.append(eList[i].getOid() + "." + eList[i].getVersion());
							}
						}
					}
					writeText(cell, sb.toString());
				}
				break;
			case BINARY:
				BinaryReference br = (BinaryReference)val;
				Map<String, String> valueMap = new LinkedHashMap<>();
				valueMap.put("lobid", String.valueOf((br.getLobId())));
				valueMap.put("name", br.getName());
				valueMap.put("type", br.getType());
				writeText(cell, toJsonString(valueMap));

				writeBinaryData(br, binaryStore);
				break;
			default:
				writeValue(cell, val, pd.getType());
		}
	}

	private void writeValue(Cell cell, Object val, PropertyDefinitionType type) {

		switch (type) {
			case BOOLEAN:
				Boolean b = (Boolean) val;
//				writeText(cell, b.booleanValue() ? "1" : "0");
				writeText(cell, b.toString());
				break;
			case DATE:
				writeText(cell, DateUtil.getSimpleDateFormat(getDateFormat(), false).format((java.sql.Date) val));
				break;
			case DATETIME:
				// Timezoneをtrueにするとアップロード時にもTimezoneが適用されるためダウンロード時はfalseにする。
				writeText(cell, DateUtil.getSimpleDateFormat(getDateTimeFormat(), false).format((Timestamp) val));
				break;
			case TIME:
				writeText(cell, DateUtil.getSimpleDateFormat(getTimeFormat(), false).format((Time) val));
				break;
			case SELECT:
				SelectValue sv = (SelectValue) val;
				writeText(cell, sv.getValue());
				break;
			case DECIMAL:
				writeText(cell, ((BigDecimal)val).toPlainString());
				break;
			case FLOAT:
				writeText(cell, BigDecimal.valueOf((Double)val).toPlainString());
				break;
			case INTEGER:
			case LONGTEXT:
			case STRING:
			case AUTONUMBER:
			case EXPRESSION:
				writeText(cell, val.toString());
				break;
			case BINARY:
			case REFERENCE:
				break;
			default:
				throw new EntityCsvException("can not convert from " + type + ":" + val);
		}
	}

	/**
	 * セルのスタイルを取得します。
	 *
	 * @param propertyDefinition プロパティ定義
	 * @return セルのスタイル
	 */
	private CellStyle getCellStyle(PropertyDefinition propertyDefinition) {

		PropertyDefinitionType type = propertyDefinition.getType();
		switch (type) {
		case DATE:
			return dateCellStyle;
		case DATETIME:
			return dateTimeCellStyle;
		case TIME:
			return timeCellStyle;
		default:
			return dataCellStyle;
		}

	}

	private void writeBinaryData(final BinaryReference br, final ZipOutputStream binaryStore) {
		try {
			//Zipが渡されている場合、Zipに追加する
			if (binaryStore != null) {
				//ファイル名をEntity定義名.LOBIDに設定
				//(参考)データ自体はLOBIDで一意（OracleはDB単位、MySQLはテナント単位）
				String entryName = "lobs/" + definition.getName() + "." + br.getLobId();

				ZipEntry zentry = new ZipEntry(entryName);
				binaryStore.putNextEntry(zentry);
				if(write(br, binaryStore)) {
					binaryStore.closeEntry();
				}

			} else {
				//出力先ディレクトリが指定されている場合はExportする
				if(StringUtil.isNotBlank(option.getExportBinaryDataDir())) {
					File lobDir = new File(option.getExportBinaryDataDir());
					if (!lobDir.exists()) {
						lobDir.mkdir();
					}
					//ファイル名をEntity定義名.LOBIDに設定
					//(参考)データ自体はLOBIDで一意（OracleはDB単位、MySQLはテナント単位）
					File lobFile = new File(option.getExportBinaryDataDir() , definition.getName() + "." + br.getLobId());
					if (lobFile.exists()) {
						lobFile.delete();
					}
					lobFile.createNewFile();

					try (FileOutputStream fileBinaryStore = new FileOutputStream(lobFile);) {
						write(br, fileBinaryStore);
					} catch (IOException e) {
						throw new EntityCsvException(e);
					}
				}
			}
		} catch (IOException e) {
			throw new EntityCsvException(e);
		}
	}

	private boolean write(final BinaryReference br, final OutputStream outputStream) throws IOException {
		InputStream is = em.getInputStream(br);
		if (is != null) {
			try (InputStream bis = new BufferedInputStream(is)) {
				byte[] buf = new byte[1024];
				int len = 0;
				while ((len = bis.read(buf)) >= 0) {
					outputStream.write(buf, 0, len);
				}
				return true;
			}
		}
		//エラーにしてしまうと作成と出力が止まるので、ログにWaringメッセージ出力
		logger.warn("cannot output binary data. entity = " + br.getDefinitionName() + ", lobid = " + br.getLobId());
		return false;
	}

	private String toJsonString(Object value) {

		if (mapper == null) {
			mapper = new ObjectMapper();
			//for backward compatibility
			mapper.configOverride(java.sql.Date.class).setFormat(JsonFormat.Value.forPattern("yyyy-MM-dd").withTimeZone(TimeZone.getDefault()));
		}
		try (StringWriter writer = new StringWriter()) {
			mapper.writeValue(writer, value);
			return writer.toString();
		} catch (JsonProcessingException e) {
			throw new EntityCsvException(e);
		} catch (IOException e) {
			throw new EntityCsvException(e);
		}
	}

	private String getDateFormat() {

		if (dateFormat != null) {
			return dateFormat;
		}
		if (option.getDateFormat() != null) {
			dateFormat = option.getDateFormat();
		} else {
			dateFormat = ExecuteContext.getCurrentContext().getLocaleFormat().getOutputDateFormat();
		}
		return dateFormat;

	}

	private String getDateTimeFormat() {

		if (dateTimeFormat != null) {
			return dateTimeFormat;
		}
		if (option.getDatetimeSecFormat() != null) {
			dateTimeFormat = option.getDatetimeSecFormat();
		} else {
			dateTimeFormat = ExecuteContext.getCurrentContext().getLocaleFormat().getOutputDatetimeSecFormat();
		}
		return dateTimeFormat;
	}

	private String getTimeFormat() {

		if (timeFormat != null) {
			return timeFormat;
		}
		if (option.getTimeSecFormat() != null) {
			timeFormat = option.getTimeSecFormat();
		} else {
			timeFormat = ExecuteContext.getCurrentContext().getLocaleFormat().getOutputTimeSecFormat();
		}
		return timeFormat;

	}

}
