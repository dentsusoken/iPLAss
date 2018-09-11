/*
 * Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.fulltextsearch;

/**
 * 日本語用アナライザーの初期化設定クラス
 */
public class JapaneseAnalyzerSetting extends AbstractAnalyzerSetting {

	private String userDictionary;
	private String stopwords;
	private String stoptags;
	private String mode;

	public String getUserDictionary() {
		return userDictionary;
	}

	public void setUserDictionary(String userDictionary) {
		this.userDictionary = userDictionary;
	}

	public String getStopwords() {
		return stopwords;
	}

	public void setStopwords(String stopwords) {
		this.stopwords = stopwords;
	}

	public String getStoptags() {
		return stoptags;
	}

	public void setStoptags(String stoptags) {
		this.stoptags = stoptags;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
}
