/*
 * Copyright (C) 2020 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.fulltextsearch.lucene;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.StopwordAnalyzerBase;
import org.apache.lucene.analysis.WordlistLoader;
import org.apache.lucene.analysis.ja.JapaneseAnalyzer;
import org.apache.lucene.analysis.ja.JapaneseTokenizer;
import org.apache.lucene.analysis.ja.JapaneseTokenizer.Mode;
import org.apache.lucene.analysis.ja.dict.UserDictionary;
import org.iplass.mtp.entity.fulltextsearch.FulltextSearchRuntimeException;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日本語分析設定
 */
public class JapaneseAnalyzerSetting implements AnalyzerSetting {
	private static Logger logger = LoggerFactory.getLogger(JapaneseAnalyzerSetting.class);

	private String className = JapaneseAnalyzer.class.getName();
	private JapaneseTokenizer.Mode mode = Mode.SEARCH;
	private String userDictionary;
	private String stopwords;
	private String stoptags;

	private Analyzer analyzer;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

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

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	@Override
	public Analyzer getAnalyzer(int tenantId, String entityDefName) {
		return analyzer;
	}

	@Override
	public void inited(LuceneFulltextSearchService service, Config config) {
		if (service.isUseFulltextSearch()) {
			try {
				Class<?> clazz = Class.forName(className);

				if (JapaneseAnalyzer.class.isAssignableFrom(clazz)) {
					UserDictionary userDict = createUserDictionary();
					CharArraySet sw = JapaneseAnalyzerSettingInternalAnalyzer.createStopwords(stopwords);
					Set<String> st = JapaneseAnalyzerSettingInternalAnalyzer.createStopTags(stoptags);
					try {
						Constructor<?> c = clazz.getConstructor(UserDictionary.class, Mode.class, CharArraySet.class, Set.class);
						analyzer = (Analyzer) c.newInstance(userDict, mode, sw, st);
					} catch (NoSuchMethodException e) {
						//use default constructor
						if (logger.isDebugEnabled()) {
							logger.debug(className + " has no constructor of signature (UserDictionary, Mode, CharArraySet, Set), so use default constructor.");
						}
					}
				}

				if (analyzer == null) {
					analyzer = (Analyzer) clazz.getConstructor().newInstance();
				}

			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e1) {
				throw new FulltextSearchRuntimeException("can not instantiate Analyzer class: " + className, e1);
			}
		}
	}

	private UserDictionary createUserDictionary() {
		if (userDictionary == null) {
			return null;
		}

		try (InputStreamReader reader = new InputStreamReader(JapaneseAnalyzerSettingInternalAnalyzer.class.getResourceAsStream(userDictionary), Charset.forName("utf-8"))) {
			return UserDictionary.open(reader);
		} catch (IOException e) {
			throw new FulltextSearchRuntimeException("User Dictionary can not read. : " + userDictionary, e);
		}
	}

	@Override
	public void destroyed() {
		if (analyzer != null) {
			analyzer.close();
		}
	}

	private static abstract class JapaneseAnalyzerSettingInternalAnalyzer extends StopwordAnalyzerBase {
		//このインスタンス自体がnewされることはない。
		//CharArraySetのインスタンスを作るため（StopwordAnalyzerBase.loadStopwordSet()を呼び出す）のみに存在

		/** CharArraySet 初期サイズ */
		private static final int CHAR_ARRAY_SET_INITIAL_CAPACITY = 16;
		/** リソース読み取り時コメント行 */
		private static final String WORDS_COMMENT = "#";

		public static CharArraySet createStopwords(String stopwords) {
			if (StringUtil.isEmpty(stopwords)) {
				return JapaneseAnalyzer.getDefaultStopSet();
			}

			try(InputStream input = JapaneseAnalyzerSettingInternalAnalyzer.class.getResourceAsStream(stopwords);
					Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {
				return WordlistLoader.getWordSet(reader, WORDS_COMMENT, new CharArraySet(CHAR_ARRAY_SET_INITIAL_CAPACITY, true));
			} catch (IOException e) {
				logger.warn("Unable to load stopword set : " + stopwords + ",  So use default one.");
			}
			return JapaneseAnalyzer.getDefaultStopSet();
		}

		public static Set<String> createStopTags(String stoptags) {
			if (StringUtil.isEmpty(stoptags)) {
				return JapaneseAnalyzer.getDefaultStopTags();
			}

			try (InputStream input = JapaneseAnalyzerSettingInternalAnalyzer.class.getResourceAsStream(stoptags);
					Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {
				final CharArraySet tagset = WordlistLoader.getWordSet(reader, WORDS_COMMENT, new CharArraySet(CHAR_ARRAY_SET_INITIAL_CAPACITY, false));
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
