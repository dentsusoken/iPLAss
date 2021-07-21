/*
 * Copyright (C) 2021 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.entity.property;

import java.util.LinkedHashMap;

import org.iplass.adminconsole.client.metadata.ui.entity.property.normalizer.ICUTransliteratorAttributePane;
import org.iplass.adminconsole.client.metadata.ui.entity.property.normalizer.JavaClassNormalizerAttributePane;
import org.iplass.adminconsole.client.metadata.ui.entity.property.normalizer.NewLineNormalizerAttributePane;
import org.iplass.adminconsole.client.metadata.ui.entity.property.normalizer.NormalizerAttributePane;
import org.iplass.adminconsole.client.metadata.ui.entity.property.normalizer.RegexReplaceAttributePane;
import org.iplass.adminconsole.client.metadata.ui.entity.property.normalizer.ScriptingNormalizerAttributePane;
import org.iplass.adminconsole.client.metadata.ui.entity.property.normalizer.UnicodeNormalizerAttributePane;
import org.iplass.adminconsole.client.metadata.ui.entity.property.normalizer.WhiteSpaceTrimmerAttributePane;
import org.iplass.mtp.entity.definition.NormalizerDefinition;
import org.iplass.mtp.entity.definition.normalizers.ICUTransliterator;
import org.iplass.mtp.entity.definition.normalizers.JavaClassNormalizer;
import org.iplass.mtp.entity.definition.normalizers.NewlineNormalizer;
import org.iplass.mtp.entity.definition.normalizers.RegexReplace;
import org.iplass.mtp.entity.definition.normalizers.ScriptingNormalizer;
import org.iplass.mtp.entity.definition.normalizers.UnicodeNormalizer;
import org.iplass.mtp.entity.definition.normalizers.WhiteSpaceTrimmer;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class NormalizerListGridRecord extends ListGridRecord {

	public enum NormalizerType {

		TRIM_WHITE_SPACE("Trim White Space") {
			@Override
			public NormalizerAttributePane attributePane() {
				return new WhiteSpaceTrimmerAttributePane();
			}
		},
		NORMALIZE_NEW_LINE("New line Normalizer"){
			@Override
			public NormalizerAttributePane attributePane() {
				return new NewLineNormalizerAttributePane();
			}
		},
		ICU_TRANSLITERATE("ICU Transliterator"){
			@Override
			public NormalizerAttributePane attributePane() {
				return new ICUTransliteratorAttributePane();
			}
		},
		REPLACE_REGEX("Regex Replace"){
			@Override
			public NormalizerAttributePane attributePane() {
				return new RegexReplaceAttributePane();
			}
		},
		NORMALIZE_UNICODE("Unicode Normalizer"){
			@Override
			public NormalizerAttributePane attributePane() {
				return new UnicodeNormalizerAttributePane();
			}
		},
		SCRIPT("Scripting"){
			@Override
			public NormalizerAttributePane attributePane() {
				return new ScriptingNormalizerAttributePane();
			}
		},
		JAVA_CLASS("Java Class"){
			@Override
			public NormalizerAttributePane attributePane() {
				return new JavaClassNormalizerAttributePane();
			}
		};

		/** 表示名 */
		private String displayName;

		/**
		 * 個別編集パネルを返します。
		 *
		 * @return 個別編集パネル
		 */
		public abstract NormalizerAttributePane attributePane();

		private NormalizerType(String displayName) {
			this.displayName = displayName;
		}

		/**
		 * 表示名を返します。
		 *
		 * @return 表示名
		 */
		public String displayName() {
			return displayName;
		}

		/**
		 * 全タイプの表示名を返します。
		 *
		 * @return 全タイプの表示名
		 */
		public static LinkedHashMap<String, String> allTypeMap() {
			LinkedHashMap<String, String> typeMap = new LinkedHashMap<>();
			for (NormalizerType type : values()) {
				typeMap.put(type.name(), type.displayName());
			}
			return typeMap;
		}
	}

	public static final String VALUE_OBJECT = "valObject";
	public static final String TYPE = "type";
	public static final String TYPE_NAME = "typeName";
	public static final String GP = "gp";

	public NormalizerListGridRecord(NormalizerDefinition definition) {
		setNormalizerDefinition(definition);
	}

	public NormalizerDefinition getNormalizerDefinition() {
		return (NormalizerDefinition)getAttributeAsObject(VALUE_OBJECT);
	}

	public void setNormalizerDefinition(NormalizerDefinition definition) {
		setAttribute(VALUE_OBJECT, definition);
		init();
	}

	public NormalizerType getType() {
		return (NormalizerType)getAttributeAsObject(TYPE);
	}

	public void setType(NormalizerType type) {
		setAttribute(TYPE, type);
		if (type != null) {
			setTypeName(type.displayName());
		} else {
			setTypeName(null);
		}
	}

	public String getTypeName() {
		return getAttribute(TYPE_NAME);
	}

	public void setTypeName(String typeName) {
		setAttribute(TYPE_NAME, typeName);
	}

	public String getGeneralPurpus() {
		return getAttribute(GP);
	}

	public void setGeneralPurpus(String value) {
		setAttribute(GP, value);
	}

	private void init() {
		NormalizerDefinition definition = getNormalizerDefinition();
		if (definition == null) {
			setType(null);
			setGeneralPurpus(null);
			return;
		}

		if (definition instanceof ICUTransliterator) {
			ICUTransliterator impl = (ICUTransliterator)definition;
			setType(NormalizerType.ICU_TRANSLITERATE);
			setGeneralPurpus(impl.getTransliteratorId());
		} else if (definition instanceof JavaClassNormalizer) {
			JavaClassNormalizer impl = (JavaClassNormalizer)definition;
			setType(NormalizerType.JAVA_CLASS);
			setGeneralPurpus(impl.getClassName());
		} else if (definition instanceof NewlineNormalizer) {
			NewlineNormalizer impl = (NewlineNormalizer)definition;
			setType(NormalizerType.NORMALIZE_NEW_LINE);
			setGeneralPurpus(impl.getType().name());
		} else if (definition instanceof RegexReplace) {
			RegexReplace impl = (RegexReplace)definition;
			setType(NormalizerType.REPLACE_REGEX);
			StringBuilder sb = new StringBuilder();
			sb.append("regex=" + impl.getRegex() + " , replace=" + impl.getReplacement());
			setGeneralPurpus(sb.toString());
		} else if (definition instanceof ScriptingNormalizer) {
			setType(NormalizerType.SCRIPT);
			setGeneralPurpus(null);
		} else if (definition instanceof UnicodeNormalizer) {
			UnicodeNormalizer impl = (UnicodeNormalizer)definition;
			setType(NormalizerType.NORMALIZE_UNICODE);
			setGeneralPurpus(impl.getForm());
		} else if (definition instanceof WhiteSpaceTrimmer) {
			setType(NormalizerType.TRIM_WHITE_SPACE);
			setGeneralPurpus(null);
		}
	}

}
