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
import org.iplass.gem.command.generic.search.EntityFileDownloadSearchContext;
import org.iplass.gem.command.generic.search.EntityFileDownloadSearchContext.FileColumn;
import org.iplass.gem.command.generic.search.EntityFileDownloadSearchViewWriter;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.web.template.TemplateUtil;

public class ExcelFileDownloadSearchViewWriter extends EntityFileDownloadSearchViewWriter {

	private OutputStream out;

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

	public ExcelFileDownloadSearchViewWriter(EntityFileDownloadSearchContext context) {
		super(context);
	}

	@Override
	protected void initWriter(OutputStream out) throws IOException {
		this.out = out;

		String sheetName = TemplateUtil.getMultilingualString(context.getEntityDefinition().getDisplayName(),
				context.getEntityDefinition().getLocalizedDisplayNameList());

		this.workbook = new SXSSFWorkbook();
		// this.workbook.setCompressTempFiles(true);

		this.sheet = workbook.createSheet(WorkbookUtil.createSafeSheetName(sheetName));
		// this.sheet.setRandomAccessWindowSize(100);
	}

	@Override
	protected void beforeWriteHeader() {
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
	}

	@Override
	protected void writeHeaderColumn(int columnIndex, String text) {
		Cell cell = headerRow.createCell(columnIndex);
		cell.setCellStyle(headerCellStyle);
		cell.setCellValue(text);
	}

	@Override
	protected void beforeWriteData() {
		dataCellStyle = workbook.createCellStyle();
		applyDataCellStyle(dataCellStyle);

		CreationHelper createHelper = workbook.getCreationHelper();

		dateCellStyle = workbook.createCellStyle();
		applyDataCellStyle(dateCellStyle);
		dateCellStyle.setDataFormat(
				createHelper.createDataFormat().getFormat(TemplateUtil.getLocaleFormat().getExcelDateFormat()));

		dateTimeCellStyle = workbook.createCellStyle();
		applyDataCellStyle(dateTimeCellStyle);
		dateTimeCellStyle.setDataFormat(
				createHelper.createDataFormat().getFormat(TemplateUtil.getLocaleFormat().getExcelDateFormat() + " "
						+ TemplateUtil.getLocaleFormat().getExcelTimeFormat()));

		timeCellStyle = workbook.createCellStyle();
		applyDataCellStyle(timeCellStyle);
		timeCellStyle.setDataFormat(
				createHelper.createDataFormat().getFormat(TemplateUtil.getLocaleFormat().getExcelTimeFormat()));
	}

	@Override
	protected void startRow() {
		rowIndex++;
		currentRow = sheet.createRow(rowIndex);
	}

	@Override
	protected void writeValueColumn(int columnIndex, FileColumn fileColumn, Object value) {
//		CellType cellType = getCellType(fileColumn);
//		Cell cell = currentRow.createCell(columnIndex, cellType);
		Cell cell = currentRow.createCell(columnIndex);

		cell.setCellStyle(getCellStyle(fileColumn));

		if (value != null) {
			cell.setCellValue(valueToString(value));
		}
	}

	@Override
	protected void nextColumn() {
	}

	@Override
	protected void endRow() {
	}

	@Override
	protected void endData() throws IOException {

		try {
			workbook.write(out);
		} finally {
			workbook.close();
		}

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

	/**
	 * セルのスタイルを取得します。
	 *
	 * @param fileColumn 列情報
	 * @return セルのスタイル
	 */
	private CellStyle getCellStyle(FileColumn fileColumn) {
		if (fileColumn.getPropertyDefinition() == null) {
			return dataCellStyle;
		}

		PropertyDefinitionType type = fileColumn.getPropertyDefinition().getType();
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

//	private CellType getCellType(FileColumn fileColumn) {
//		if (fileColumn.getPropertyDefinition() == null) {
//			return CellType.STRING;
//		}
//
//		PropertyDefinitionType type = fileColumn.getPropertyDefinition().getType();
//		switch (type) {
//		case BOOLEAN:
//			return CellType.BOOLEAN;
//		case DATE:
//		case DATETIME:
//		case TIME:
//		case INTEGER:
//		case FLOAT:
//		case DECIMAL:
//		    return CellType.NUMERIC;
//		default:
//			return CellType.STRING;
//		}
//	}

}
