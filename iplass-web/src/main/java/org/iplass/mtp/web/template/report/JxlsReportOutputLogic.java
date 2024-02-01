/*
 * Copyright (C) 2020 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.web.template.report;

import java.io.IOException;
import java.util.List;

import org.jxls.area.Area;
import org.jxls.builder.AreaBuilder;
import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.command.GridCommand;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.formula.StandardFormulaProcessor;
import org.jxls.transform.Transformer;
import org.jxls.util.JxlsHelper;

/**
 <% if (doclang == "ja") {%>
 * <p>
 * JXLS専用帳票出力ロジックインターフェース
 * </p>
 * JXLSを用いて帳票出力する際に出力処理を記述してもらう為のインタフェースです。<br>
 * アプリ担当者は、このインタフェースを継承して独自に帳票処理を記載する事が可能。
 * 
 * <%} else {%>
 * <p>
 * The interface for implementing Report Output Logic for JXLS.
 * </p>
 * This interface is used to have the output process described when outputting a report using JXLS.<br>
 * The person in charge of the application can inherit this interface and describe their own report output processing.
 * 
 * <%}%>
 * 
 * @author Y.Ishida
 */
public interface JxlsReportOutputLogic {
	/**
	 * <% if (doclang == "ja") {%>
	 * JXLS帳票出力処理
	 * <%} else {%>
	 * Report Output Logic for JXLS
	 * <%}%>
	 * 
	 * @param transformer
	 * @param context
	 */
	public void reportWrite(Transformer transformer, Context context);

	/**
	 * <% if (doclang == "ja") {%>
	 * {@link JxlsHelper#processTemplateAtCell(InputStream, OutputStream, Context, String)}対応のデフォルトメソッド
	 * <%} else {%>
	 * Default method corresponding to {@link JxlsHelper#processTemplateAtCell(InputStream, OutputStream, Context, String)}
	 * <%}%>
	 * 
	 * @param transformer
	 * @param context
	 * @param targetCell
	 * @return
	 * @throws IOException
	 */
	default void processTemplateAtCell(Transformer transformer, Context context, String targetCell) throws IOException {
		AreaBuilder areaBuilder = new XlsCommentAreaBuilder();
		areaBuilder.setTransformer(transformer);
		List<Area> xlsAreaList = areaBuilder.build();
		if (xlsAreaList.isEmpty()) {
			throw new IllegalStateException("No XlsArea were detected for this processing");
		}
		Area firstArea = xlsAreaList.get(0);
		CellRef targetCellRef = new CellRef(targetCell);
		firstArea.applyAt(targetCellRef, context);
		firstArea.setFormulaProcessor(new StandardFormulaProcessor());
		firstArea.processFormulas();
		String sourceSheetName = firstArea.getStartCellRef().getSheetName();
		if (!sourceSheetName.equalsIgnoreCase(targetCellRef.getSheetName())) {
				transformer.deleteSheet(sourceSheetName);
		}
		transformer.write();
	}
	
    /**
	 * <% if (doclang == "ja") {%>
	 * {@link JxlsHelper#processGridTemplate(InputStream, OutputStream, Context, String)}対応のデフォルトメソッド
	 * <%} else {%>
	 * Default method corresponding to {@link JxlsHelper#processGridTemplate(InputStream, OutputStream, Context, String)}
	 * <%}%>
	 * 
	 * @param transformer
	 * @param context
	 * @param objectProps
	 * @return
	 * @throws IOException
	 */
	default void processGridTemplate(Transformer transformer, Context context, String objectProps) throws IOException {
		AreaBuilder areaBuilder = new XlsCommentAreaBuilder();
		areaBuilder.setTransformer(transformer);
		List<Area> xlsAreaList = areaBuilder.build();
		for (Area xlsArea : xlsAreaList) {
			GridCommand gridCommand = (GridCommand) xlsArea.getCommandDataList().get(0).getCommand();
			gridCommand.setProps(objectProps);
			xlsArea.setFormulaProcessor(new StandardFormulaProcessor());
			xlsArea.applyAt(new CellRef(xlsArea.getStartCellRef().getCellName()), context);
			xlsArea.processFormulas();
		}
		transformer.write();
	}
	
    /**
	 * <% if (doclang == "ja") {%>
	 * {@link JxlsHelper#processGridTemplateAtCell(InputStream, OutputStream, Context, String, String)}対応のデフォルトメソッド
	 * <%} else {%>
	 * Default method corresponding to{@link JxlsHelper#processGridTemplateAtCell(InputStream, OutputStream, Context, String, String)}
	 * <%}%>
     * 
     * @param transformer
     * @param context
     * @param objectProps
     * @param targetCell
     * @throws IOException
     */
	default void processGridTemplateAtCell(Transformer transformer, Context context,
			String objectProps, String targetCell) throws IOException {
		AreaBuilder areaBuilder = new XlsCommentAreaBuilder();
		areaBuilder.setTransformer(transformer);
		List<Area> xlsAreaList = areaBuilder.build();
		Area firstArea = xlsAreaList.get(0);
		CellRef targetCellRef = new CellRef(targetCell);
		GridCommand gridCommand = (GridCommand) firstArea.getCommandDataList().get(0).getCommand();
		gridCommand.setProps(objectProps);
		firstArea.applyAt(targetCellRef, context);
		firstArea.setFormulaProcessor(new StandardFormulaProcessor());
		firstArea.processFormulas();
		String sourceSheetName = firstArea.getStartCellRef().getSheetName();
		if (!sourceSheetName.equalsIgnoreCase(targetCellRef.getSheetName())) {
			transformer.deleteSheet(sourceSheetName);
		}
		transformer.write();
	}
	
}
