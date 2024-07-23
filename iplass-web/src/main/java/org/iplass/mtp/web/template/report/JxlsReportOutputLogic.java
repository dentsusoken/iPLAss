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

import java.io.OutputStream;
import java.util.Map;

import org.jxls.builder.JxlsTemplateFillerBuilder;
import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.command.GridCommand;
import org.jxls.common.CellRef;
import org.jxls.transform.poi.PoiTransformer;

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
	 * @param builder <%if (doclang == "ja") {%> Jxls FillerBuilder インスタンス <%} else {%> instance of Jxls FillerBuilder <%}%>
	 * @param reportData <%if (doclang == "ja") {%> 帳票データ <%} else {%> report data <%}%>
	 * @param out <%if (doclang == "ja") {%> 帳票出力先 <%} else {%> report output to <%}%>
	 */
	public void reportWrite(JxlsTemplateFillerBuilder<?> builder, Map<String, Object> reportData, OutputStream out);

	/**
	 * <% if (doclang == "ja") {%>
	 * 指定された FillerBuilder の設定で、テンプレート処理結果をターゲットシートに出力し、結果を OutputStream に書き込む。
	 * <h2>注意</h2>
	 * <p>
	 * Jxls 2.x JxlsHelper#processTemplateAtCell の互換機能として用意していますが、機能が不完全です。
	 * シート名の変更は可能ですが、出力先セル位置の変更はできません。
	 * </p>
	 * <%} else {%>
	 * With the specified FillerBuilder settings, output the template processing results to the target cell and write the results to OutputStream.
	 * <h2>Caution</h2>
	 * <p>
	 * Jxls 2.x JxlsHelper#processTemplateAtCell is provided as a compatible function of JxlsHelper#processTemplateAtCell, but the function is incomplete.
	 * The sheet name can be changed, but the output destination cell position cannot be changed.
	 * </p>
	 * <%}%>
	 *
	 * @deprecated <%if (doclang == "ja") {%> Jxls 3.x JxlsHelper と関連機能が無くなっていたため、次期バージョンで削除する予定です。 <%} else {%> Jxls 3.x JxlsHelper and related functions were missing and will be removed in the next version. <%}%>
	 * @param builder <%if (doclang == "ja") {%> Jxls FillerBuilder インスタンス <%} else {%> instance of Jxls FillerBuilder <%}%>
	 * @param reportData <%if (doclang == "ja") {%> 帳票データ <%} else {%> report data <%}%>
	 * @param out <%if (doclang == "ja") {%> 帳票出力先 <%} else {%> report output to <%}%>
	 * @param targetCell <%if (doclang == "ja") {%> テンプレート出力先セル <%} else {%> template output destination cell <%}%>
	 */
	@Deprecated
	default void processTemplateAtCell(JxlsTemplateFillerBuilder<?> builder, Map<String, Object> reportData, OutputStream out, String targetCell) {
		final var cellRef = new CellRef(targetCell);
		// シート名の変更
		builder
				.withPreWriteAction((transformer, ctx) -> {
					if (transformer instanceof PoiTransformer poiTransformer) {
						var book = poiTransformer.getWorkbook();

						if (cellRef.getSheetName() != null && cellRef.getSheetName().length() > 0) {
							book.setSheetName(0, cellRef.getSheetName());
						}
					}
				})
				.build()
				.fill(reportData, () -> out);
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * 指定された FillerBuilder の設定で、テンプレート処理結果を出力し、結果を OutputStream に書き込む。
	 * テンプレートのエリアの GridCommand の props に入力パラメータ objectProps を設定する。
	 *
	 * <h2>注意</h2>
	 * <p>
	 * Jxls 2.x JxlsHelper#processGridTemplate の互換機能として用意しています。
	 * </p>
	 * <%} else {%>
	 * Outputs the results of template processing with the specified FillerBuilder settings and writes the results to OutputStream.
	 * Set the input parameter objectProps to props in GridCommand in the template area.
	 *
	 * <h2>Caution</h2>
	 * <p>
	 * Jxls 2.x JxlsHelper#processGridTemplate is provided as a compatible function of JxlsHelper#processGridTemplate.
	 * </p>
	 * <%}%>
	 *
	 * @deprecated <%if (doclang == "ja") {%> Jxls 3.x JxlsHelper と関連機能が無くなっていたため、次期バージョンで削除する予定です。 <%} else {%> Jxls 3.x JxlsHelper and related functions were missing and will be removed in the next version. <%}%>
	 * @param builder <%if (doclang == "ja") {%> Jxls FillerBuilder インスタンス <%} else {%> instance of Jxls FillerBuilder <%}%>
	 * @param reportData <%if (doclang == "ja") {%> 帳票データ <%} else {%> report data <%}%>
	 * @param out <%if (doclang == "ja") {%> 帳票出力先 <%} else {%> report output to <%}%>
	 * @param objectProps <%if (doclang == "ja") {%> GridCommand の props の設定値 <%} else {%> Setting value of props of GridCommand <%}%>
	 */
	@Deprecated
	default void processGridTemplate(JxlsTemplateFillerBuilder<?> builder, Map<String, Object> reportData, OutputStream out, String objectProps) {
		builder
				// grid コマンドの props に設定
				.withAreaBuilder((transformer, clearTemplateCells) -> {
					var areaList = new XlsCommentAreaBuilder().build(transformer, clearTemplateCells);
					for (var area : areaList) {
						for (var commandData : area.getCommandDataList()) {
							if (commandData.getCommand() instanceof GridCommand gridCommand) {
								gridCommand.setProps(objectProps);
							}
						}
					}
					return areaList;
				})
				.build()
				.fill(reportData, () -> out);
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * 指定された FillerBuilder の設定で、テンプレート処理結果をターゲットシートに出力し、結果を OutputStream に書き込む。
	 * テンプレートのエリアの GridCommand の props に入力パラメータ objectProps を設定する。
	 * <h2>注意</h2>
	 * <p>
	 * Jxls 2.x JxlsHelper#processGridTemplateAtCell の互換機能として用意していますが、機能が不完全です。
	 * GridCommand の props 設定およびシート名の変更は可能ですが、出力先セル位置の変更はできません。
	 * </p>
	 * <%} else {%>
	 * Output the template processing results to the target sheet with the specified FillerBuilder settings and write the results to OutputStream.
	 * Set the input parameter objectProps to props in GridCommand in the template area.
	 * <h2>Caution</h2>
	 * <p>
	 * Jxls 2.x JxlsHelper#processGridTemplateAtCell is provided as a compatible function of JxlsHelper#processGridTemplateAtCell, but the function is incomplete.
	 * The GridCommand props setting and sheet name can be changed, but the output destination cell position cannot be changed.
	 * </p>
	 * <%}%>
	 *
	 * @deprecated <%if (doclang == "ja") {%> Jxls 3.x JxlsHelper と関連機能が無くなっていたため、次期バージョンで削除する予定です。 <%} else {%> Jxls 3.x JxlsHelper and related functions were missing and will be removed in the next version. <%}%>
	 * @param builder <%if (doclang == "ja") {%> Jxls FillerBuilder インスタンス <%} else {%> instance of Jxls FillerBuilder <%}%>
	 * @param reportData <%if (doclang == "ja") {%> 帳票データ <%} else {%> report data <%}%>
	 * @param out <%if (doclang == "ja") {%> 帳票出力先 <%} else {%> report output to <%}%>
	 * @param objectProps <%if (doclang == "ja") {%> GridCommand の props の設定値 <%} else {%> Setting value of props of GridCommand <%}%>
	 * @param targetCell <%if (doclang == "ja") {%> テンプレート出力先セル <%} else {%> template output destination cell <%}%>
	 */
	@Deprecated
	default void processGridTemplateAtCell(JxlsTemplateFillerBuilder<?> builder, Map<String, Object> reportData, OutputStream out, String objectProps,
			String targetCell) {

		final var cellRef = new CellRef(targetCell);
		builder
				// grid コマンドの props に設定
				.withAreaBuilder((transformer, clearTemplateCells) -> {
					var areaList = new XlsCommentAreaBuilder().build(transformer, clearTemplateCells);

					GridCommand gridCommand = (GridCommand) areaList.get(0).getCommandDataList().get(0).getCommand();
					gridCommand.setProps(objectProps);

					return areaList;
				})
				// シート名の変更
				.withPreWriteAction((transformer, ctx) -> {
					if (transformer instanceof PoiTransformer poiTransformer) {
						var book = poiTransformer.getWorkbook();

						if (cellRef.getSheetName() != null && cellRef.getSheetName().length() > 0) {
							book.setSheetName(0, cellRef.getSheetName());
						}
					}
				})
				.build()
				.fill(reportData, () -> out);
	}
}
