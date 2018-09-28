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

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.StopwordAnalyzerBase;
import org.apache.lucene.analysis.ja.JapaneseAnalyzer;
import org.apache.lucene.analysis.ja.JapaneseTokenizer.Mode;
import org.apache.lucene.analysis.ja.dict.UserDictionary;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnalyzerFactory {

	private static Logger logger = LoggerFactory.getLogger(AnalyzerFactory.class);

	public static Analyzer createAnalyzer(String className, AnalyzerSetting analyzerSetting)
			throws ClassNotFoundException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
		if (analyzerSetting != null && JapaneseAnalyzer.class.isAssignableFrom(Class.forName(className))) {
			// 日本語用アナライザーの初期化設定に変換
			JapaneseAnalyzerSetting japaneseAnalyzerSetting = (JapaneseAnalyzerSetting) analyzerSetting;
			UserDictionary userDict = AnalyzerFactoryJapaneseAnalyzer.createUserDictionary(japaneseAnalyzerSetting.getUserDictionary());
			Mode mode = AnalyzerFactoryJapaneseAnalyzer.createMode(japaneseAnalyzerSetting.getMode());
			CharArraySet stopwords = AnalyzerFactoryJapaneseAnalyzer.createStopwords(japaneseAnalyzerSetting.getStopwords());
			Set<String> stoptags = AnalyzerFactoryJapaneseAnalyzer.createStopTags(japaneseAnalyzerSetting.getStoptags());
			try {
				Constructor<?> c = Class.forName(className).getConstructor(UserDictionary.class, Mode.class, CharArraySet.class, Set.class);
				return (Analyzer) c.newInstance(userDict, mode, stopwords, stoptags);
			} catch (NoSuchMethodException e) {
			}
		}
		return (Analyzer) Class.forName(className).getConstructor().newInstance();
	}

	private static abstract class AnalyzerFactoryJapaneseAnalyzer extends StopwordAnalyzerBase {

		public static UserDictionary createUserDictionary(String userDictionary) {
			if (StringUtil.isEmpty(userDictionary)) {
				return null;
			}

			InputStreamReader reader = null;
			try {
				reader = new InputStreamReader(AnalyzerFactoryJapaneseAnalyzer.class.getResourceAsStream(userDictionary), Charset.forName("utf-8"));
				return UserDictionary.open(reader);
			} catch (IOException e) {
				logger.warn("User Dictionary not found. : " + userDictionary);
			} finally {
				try {
					if (reader != null) {
						reader.close();
					}
				} catch (IOException e) {
					logger.warn("Error occured when close the reader of user dictionary. :" + userDictionary);
				}
			}
			return null;
		}

		public static Mode createMode(String mode) {
			try {
				if (StringUtil.isEmpty(mode)) {
					return Mode.SEARCH;
				}
				return Mode.valueOf(mode);
			} catch (IllegalArgumentException e) {
				return Mode.SEARCH;
			}
		}

		public static CharArraySet createStopwords(String stopwords) {
			if (StringUtil.isEmpty(stopwords)) {
				return JapaneseAnalyzer.getDefaultStopSet();
			}

			try {
				return StopwordAnalyzerBase.loadStopwordSet(true, AnalyzerFactoryJapaneseAnalyzer.class, stopwords, "#");
			} catch (IOException e) {
				logger.warn("Unable to load stopword set : " + stopwords + ",  So use default one.");
			}
			return JapaneseAnalyzer.getDefaultStopSet();
		}

		public static Set<String> createStopTags(String stoptags) {
			if (StringUtil.isEmpty(stoptags)) {
				return JapaneseAnalyzer.getDefaultStopTags();
			}

			try {
				final CharArraySet tagset = StopwordAnalyzerBase.loadStopwordSet(false, AnalyzerFactoryJapaneseAnalyzer.class, stoptags, "#");
				Set<String> tags = new HashSet<>();
				for (Object element : tagset) {
					char chars[] = (char[]) element;
					tags.add(new String(chars));
				}
				return tags;
			} catch (IOException e) {
				logger.warn("Unable to load stoptag set : " + stoptags + ", So use default one.");
			}
			return JapaneseAnalyzer.getDefaultStopTags();
		}
	}
}
