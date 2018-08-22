/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.script;

import java.io.PrintWriter;

public interface ScriptEngine {
	
	/**
	 * sharedClass=falseとして、Scriptを生成。
	 * 
	 * @param script
	 * @param name
	 * @return
	 */
	public Script createScript(String script, String name);
	
	/**
	 * Scriptを生成。
	 * 
	 * @param script 生成（コンパイル）されたScript
	 * @param name スクリプト名（英数以外の文字は_へ自動的に置換）
	 * @param sharedClass その他のScriptで共有するScript（class）とする場合true
	 * @return
	 */
	public Script createScript(String script, String name, boolean sharedClass);
	
	/**
	 * Scriptへ変数を受け渡すためのScriptContextを生成。
	 * 
	 * @return
	 */
	public ScriptContext newScriptContext();
	
	/**
	 * Scriptへ変数を受け渡すためのScriptContextを生成。
	 * 
	 * @param out 標準出力の出力先
	 * @return
	 */
	public ScriptContext newScriptContext(PrintWriter out);

	public void invalidate();

}
