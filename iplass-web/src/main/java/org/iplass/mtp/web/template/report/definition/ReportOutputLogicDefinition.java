/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import javax.xml.bind.annotation.XmlSeeAlso;


/**
 * <p>
 * 帳票出力機能でPOIまたはJXLSを利用して出力する際の定義。
 * </p>
 * <p>
 * Javaクラス指定、Groovyスクリプト指定の２通り設定可能。<br>
 * Javaクラス指定：ReportOutputLogicインタフェースを継承したクラスを設定<br>
 * Groovyスクリプト指定：Groovyスクリプトを記述
 * </p>
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
