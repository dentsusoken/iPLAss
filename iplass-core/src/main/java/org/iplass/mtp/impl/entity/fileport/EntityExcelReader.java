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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.format.CellFormat;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.util.ConvertUtil;
import org.iplass.mtp.impl.util.CoreResourceBundleUtil;
import org.iplass.mtp.util.DateUtil;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EntityのExcelファイルのリーダークラスです。
 */
public class EntityExcelReader extends EntityFileReader<EntityExcelReader> {

	private static final Logger logger = LoggerFactory.getLogger(EntityExcelReader.class);

	private InputStream inputStream;

	private Workbook workbook;
	private Sheet sheet;

	private int rowIndex = 1;

	private String dateFormat;
	private String dateTimeFormat;
	private String timeFormat;

	public EntityExcelReader(EntityDefinition definition, InputStream inputStream) {
		super(definition);
		this.inputStream = inputStream;
	}

	@Override
	public void close() throws Exception {
		if (workbook != null) {
			try {
				workbook.close();
			} catch (IOException e) {
				logger.warn("fail to close EntityExcelReader resource. check whether resource is leak or not.", e);
			}
			workbook = null;
		}
	}

	@Override
	protected List<String> readHeader() {

		try {
			workbook = WorkbookFactory.create(inputStream);
		} catch (EncryptedDocumentException | IOException e) {
			throw new EntityCsvException("fail to read EntityExcelReader.", e);
		}
		sheet = workbook.getSheetAt(0);

		int lastRow = sheet.getLastRowNum();
		if (lastRow < 1) {
			throw new EntityCsvException("CE2000", rs("impl.csv.EntityCsvReader.emptyFile"));
		}

		Row headerRow = sheet.getRow(0);
		if (headerRow == null) {
			throw new EntityCsvException("CE2000", rs("impl.csv.EntityCsvReader.emptyFile"));
		}

		int lastCell = headerRow.getLastCellNum();
		if (lastCell < 1) {
			throw new EntityCsvException("CE2000", rs("impl.csv.EntityCsvReader.emptyFile"));
		}

		List<String> headerLine = new ArrayList<String>();
		for (int i = 0; i < lastCell; i++) {
			String cellValue = headerRow.getCell(i).getStringCellValue();
			if (StringUtil.isEmpty(cellValue)) {
				break;
			}
			headerLine.add(cellValue);
		}

		return headerLine;
	}

	@Override
	protected List<String> readData() {
		if (sheet.getLastRowNum() < rowIndex) {
			return null;
		}

		Row currentRow = sheet.getRow(rowIndex);

		// 空行が含まれていた場合は終了
		if (currentRow == null) {
			return null;
		}

		// セル数がヘッダー数より多い場合はヘッダー数まで取得
		int lastCell = currentRow.getLastCellNum();
		int maxCellNum = lastCell > header().size() ? header().size() : lastCell;

		List<String> currentLine = new ArrayList<String>();
		for (int i = 0; i < maxCellNum; i++) {
			PropertyDefinition propertyDefinition = definition.getProperty(properties().get(i));
			String cellValue = getCellStringValue(currentRow.getCell(i), propertyDefinition);
			currentLine.add(cellValue);
		}

		// 空行が含まれていた場合は終了
		if (currentLine.stream().anyMatch(value -> StringUtil.isNotEmpty(value))) {
			rowIndex++;
			return currentLine;
		}

		return null;
	}

	/**
	 * セルの文字列値を返します。
	 *
	 * @param cell               セル
	 * @param propertyDefinition プロパティ定義
	 * @return セルの文字列値
	 */
	private String getCellStringValue(Cell cell, PropertyDefinition propertyDefinition) {
		String value = null;
		CellType cellType = cell.getCellType();
		switch (cellType) {
		case STRING:
			value = cell.getStringCellValue();
			break;
		case NUMERIC:
			value = getNumericStringValue(cell, propertyDefinition);
			break;
		case BOOLEAN:
			value = String.valueOf(cell.getBooleanCellValue());
			break;
//		case FORMULA:
//		case ERROR:
//		case BLANK:
		default:
			break;
		}
		return value;
	}

	/**
	 * セルの数値型の文字列値を返します。
	 *
	 * @param cell               セル
	 * @param propertyDefinition プロパティ定義
	 * @return セルの数値型の文字列値
	 */
	private String getNumericStringValue(Cell cell, PropertyDefinition propertyDefinition) {
		// 標準の日付の場合
		if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
			// 日付系はプロパティ定義でフォーマットを判定

			if (propertyDefinition == null) {
				// 不明なのでnullを返す
				return null;
			}
			switch (propertyDefinition.getType()) {
			case DATE:
				return DateUtil.getSimpleDateFormat(getDateFormat(), false).format(cell.getDateCellValue());
			case DATETIME:
				return DateUtil.getSimpleDateFormat(getDateTimeFormat(), false).format(cell.getDateCellValue());
			case TIME:
				return DateUtil.getSimpleDateFormat(getTimeFormat(), false).format(cell.getDateCellValue());
			default:
				return null;
			}
		}
		// ユーザー定義型で日付を設定している場合
		if (BuiltinFormats.FIRST_USER_DEFINED_FORMAT_INDEX <= cell.getCellStyle().getDataFormat()) {
			CellFormat cellFormat = CellFormat.getInstance(cell.getCellStyle().getDataFormatString());
			return cellFormat.apply(cell).text;
		}
		// 日付じゃない場合は数値を返す。
		return ConvertUtil.convertToString(cell.getNumericCellValue());
	}

	/**
	 * 日付フォーマットを返します。
	 *
	 * @return 日付フォーマット
	 */
	private String getDateFormat() {

		if (dateFormat != null) {
			return dateFormat;
		}
		dateFormat = ExecuteContext.getCurrentContext().getLocaleFormat().getOutputDateFormat();
		return dateFormat;

	}

	/**
	 * 日時フォーマットを返します。
	 *
	 * @return 日時フォーマット
	 */
	private String getDateTimeFormat() {

		if (dateTimeFormat != null) {
			return dateTimeFormat;
		}
		dateTimeFormat = ExecuteContext.getCurrentContext().getLocaleFormat().getOutputDatetimeSecFormat();
		return dateTimeFormat;
	}

	/**
	 * 時刻フォーマットを返します。
	 *
	 * @return 時刻フォーマット
	 */
	private String getTimeFormat() {

		if (timeFormat != null) {
			return timeFormat;
		}
		timeFormat = ExecuteContext.getCurrentContext().getLocaleFormat().getOutputTimeSecFormat();
		return timeFormat;
	}

	private static String rs(String key, Object... arguments) {
		return CoreResourceBundleUtil.resourceString(key, arguments);
	}
}
