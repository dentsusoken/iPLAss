/*
 * Copyright (C) 2020 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import org.jxls.common.Context;
import org.jxls.transform.Transformer;

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
	 * @param  jxlsHelper
	 */
	public void reportWrite(Transformer transformer, Context context, MtpJxlsHelper jxlsHelper);
}
