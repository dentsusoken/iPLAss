/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

import org.apache.poi.ss.usermodel.Workbook;
import org.iplass.mtp.command.RequestContext;

/**
 <% if (doclang == "ja") {%>
 * <p>
 * POI専用帳票出力ロジックインターフェース
 * </p>
 * POIを用いて帳票出力する際に出力処理を記述してもらう為のインタフェースです。<br>
 * アプリ担当者は、このインタフェースを継承して独自に帳票処理を記載する事が可能。
 * 
 * <%} else {%>
 * <p>
 * The interface for implementing Report Output Logic for POI.
 * </p>
 * This interface is used to have the output process described when outputting a report using POI.<br>
 * The person in charge of the application can inherit this interface and describe their own report output processing.
 * 
 * <%}%>
 *  
 * @author lis71n
 *
 */
public interface PoiReportOutputLogic {
	
	/**
	 * <% if (doclang == "ja") {%>
	 * POI帳票出力処理
	 * <%} else {%>
	 * Report Output Logic for POI
	 * <%}%>
	 * 
	 * @param RequestContext context
	 * @param WorkBook book
	 * 
	 */
	public void reportOutput(RequestContext context, Workbook book);

}
