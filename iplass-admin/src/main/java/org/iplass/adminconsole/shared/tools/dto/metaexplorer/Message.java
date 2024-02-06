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

package org.iplass.adminconsole.shared.tools.dto.metaexplorer;

import java.io.Serializable;

/**
 * サーバとクライアントで受け渡す汎用的なメッセージ格納クラス
 */
public class Message implements Serializable {

	private static final long serialVersionUID = 5679276394857882465L;

	public enum Level {
		INFO, WARN, ERROR
	}

	private Level level;
	private String message;

	public Message() {
	}

	public Message(Level level, String message) {
		this.level = level;
		this.message = message;
	}

	public boolean isError() {
		return Level.ERROR.equals(level);
	}

	public boolean isWarn() {
		return Level.WARN.equals(level);
	}

	public boolean isInfo() {
		return Level.INFO.equals(level);
	}

	public Level getLevel() {
		return level;
	}
	public void setLevel(Level level) {
		this.level = level;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
