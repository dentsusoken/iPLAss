/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.webapi.definition;

import java.io.Serializable;

/**
 * <p>
 * リクエスト時のURLのパスをパラメータとして受けとる際のマッピング定義です。
 * </p>
 * <p>
 * nameに指定したパラメータ名に、mapFromで指定されたパス、もしくは別パラメータをマッピングします。<br>
 * mapFromには、リクエストURLのパスの一部を表す文字列、もしくは別パラメータ名を指定することが可能です。
 * mapFromを${0}とした場合、WebApi名より1階層下層のパスの値がマップされます。
 * ${1}とした場合、WebApi名より2階層下層のパスの値がマップされます。
 * また、mapFromを${paths}とした場合、サブパスすべてマップします。
 * </p>
 * <p>
 * たとえば、WebApi名が"sample/webApi1"の場合において、
 * "sample/webApi1/path1/path2/path3?paramX=fuga"を呼び出した場合、mapFromに設定した値によって、
 * それぞれ次の値がパラメータにマップされます。
 * <ul>
 * <li>${0} -&gt; path1</li>
 * <li>${1} -&gt; path2</li>
 * <li>${paths} -&gt; path1/path2/path3</li>
 * <li>paramX -&gt; fuga</li>
 * </ul>
 * </p>
 * <p>
 * conditionを指定することにより、パラメータをマッピングを実行する条件を指定することが可能です。
 * conditionはgroovyScriptで記述可能で、次の変数がバインドされており判断に利用可能です。
 * <ul>
 * <li>subPath : webApiより下層のサブパスを/で分割したString配列</li>
 * <li>fullPath : webApi含めたフルパスを/で分割したString配列</li>
 * <li>paramMap : リクエストパラメータのMap</li>
 * </ul>
 * </p>
 * <p>
 * たとえば、次のようなParamMap定義がある場合、
 * </p>
 * <table border=1>
 * <tr>
 * <th>name</th><th>mapFrom</th><th>condition</th>
 * </tr>
 * <tr>
 * <td>defName</td><td>${0}</td><td>subPath.length==1</td>
 * </tr>
 * <tr>
 * <td>viewName</td><td>${0}</td><td>subPath.length==2</td>
 * </tr>
 * <tr>
 * <td>defName</td><td>${1}</td><td>subPath.length==2</td>
 * </tr>
 * </table>
 * <p>
 * webApi1に対するリクエストパスが、
 * <ul>
 * <li>webApi1/hogeだった場合、defName=hoge</li>
 * <li>webApi1/hoge/fugaだった場合、viewName=hoge, defName=fuga</li>
 * </ul>
 * となります。
 * </p>
 *
 */
public class WebApiParamMapDefinition implements Serializable {

	private static final long serialVersionUID = -8571119343858716267L;

	/**
	 * WebApiのパスを除いたサブパスの文字列をマップする場合の定数です。
	 */
	public static final String PATHS = "${paths}";

	private String name;
	private String mapFrom;
	
	private String condition;

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            セットする name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return mapFrom
	 */
	public String getMapFrom() {
		return mapFrom;
	}

	/**
	 * @param mapFrom
	 *            セットする mapFrom
	 */
	public void setMapFrom(String mapFrom) {
		this.mapFrom = mapFrom;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

}
