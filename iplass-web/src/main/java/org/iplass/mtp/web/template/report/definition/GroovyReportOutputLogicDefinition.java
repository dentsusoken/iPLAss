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

import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.session.Session;

/**
 * <p>
 * GroovyScriptによるReportOutputの定義。
 * </p>
 *
 * <p>
 * Groovyでの記述方法。
 * </p>
 * <p>
 * ReportTemplateでバインド設定された値がバインドされているので、
 * 使用する場合は、バインド設定しておくこと。
 * </p>
 * <p>
 * デフォルトでバインドされている変数
 * <ul>
 * <li>session : {@link Session}</li>
 * <li>request : {@link RequestContext}</li>
 * </ul>
 * </p>
 * <h5>記述例：</h5>
 * <code><pre>
 * 作成中
 * </pre></code>
 *
 * @author lis71n
 *
 */
public class GroovyReportOutputLogicDefinition extends ReportOutputLogicDefinition {
	
	private static final long serialVersionUID = -6363313428336523812L;
	
	private String script;

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}
}
