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

package org.iplass.mtp.web.actionmapping.definition.cache;

import org.iplass.mtp.entity.query.PreparedQuery;

/**
 * <p>
 * キャッシュの一致判定ロジックをスクリプトで記述する。
 * </p>
 * 
 * <p>
 * スクリプト中では以下の変数をバインド済み。
 * <ul>
 * <li>request: RequestContextのインスタンス</li>
 * <li>user : 実行するユーザの情報。※１{@link PreparedQuery}を参照のこと。</li>
 * <li>auth : AuthContextのインスタンス</li>
 * </ul>
 * </p>
 * 
 * @author K.Higuchi
 *
 */
public class ScriptingCacheCriteriaDefinition extends CacheCriteriaDefinition {

	private static final long serialVersionUID = 6378242695686413725L;

	/** キャッシュのキー比較に利用する文字列を返却するよう実装する。 */
	private String script;

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	@Override
	public String summaryInfo() {
		String summary = "";
		if (script != null) {
			if (script.length() > 20) {
				summary = script.substring(0, 20) + "...";
			} else {
				summary = script;
			}
		} else {
			summary = "empty";
		}
		return "Script = " + summary;
	}
}
