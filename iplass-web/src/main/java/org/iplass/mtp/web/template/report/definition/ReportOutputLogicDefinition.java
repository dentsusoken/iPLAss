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

package org.iplass.mtp.web.template.report.definition;

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlSeeAlso;


/**
 * <% if (doclang == "ja") {%>
 * <p>
 * 帳票出力機能でPOIまたはJXLSを利用して出力する際の定義
 * </p>
 * <p>
 * Javaクラス指定、Groovyスクリプト指定の２通りで設定可能。<br>
 * Javaクラス指定：ReportOutputLogicインタフェースを継承したクラスを設定<br>
 * Groovyスクリプト指定：Groovyスクリプトを記述
 * </p>
 * 
 * <%} else {%>
 * <p>
 * The definition of a report output function using POI or JXLS
 * </p>
 * <p>
 * It is possible to set up the report output logic from two options: the Java class or Groovy script.<br>
 * Java class : Set up a class that extends the ReportOutputLogic interface<br>
 * Groovy script : Write a Groovy script
 * </p>
 * <%}%>
 *
 * @author lis71n
 *
 */
@XmlSeeAlso (value = {
		JavaClassReportOutputLogicDefinition.class,
		GroovyReportOutputLogicDefinition.class
})
public abstract class ReportOutputLogicDefinition implements Serializable {
	private static final long serialVersionUID = -3893705237664300024L;
}
