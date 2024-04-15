/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.entity.definition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.entity.definition.properties.AutoNumberProperty;
import org.iplass.mtp.entity.definition.properties.BinaryProperty;
import org.iplass.mtp.entity.definition.properties.BooleanProperty;
import org.iplass.mtp.entity.definition.properties.DateProperty;
import org.iplass.mtp.entity.definition.properties.DateTimeProperty;
import org.iplass.mtp.entity.definition.properties.DecimalProperty;
import org.iplass.mtp.entity.definition.properties.ExpressionProperty;
import org.iplass.mtp.entity.definition.properties.FloatProperty;
import org.iplass.mtp.entity.definition.properties.IntegerProperty;
import org.iplass.mtp.entity.definition.properties.LongTextProperty;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.entity.definition.properties.SelectProperty;
import org.iplass.mtp.entity.definition.properties.StringProperty;
import org.iplass.mtp.entity.definition.properties.TimeProperty;

import com.fasterxml.jackson.annotation.JsonTypeInfo;


/**
 * Entityが保持するプロパティを定義するクラス。
 *
 * @author K.Higuchi
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso(value = { AutoNumberProperty.class, BinaryProperty.class, BooleanProperty.class,
		DateProperty.class, DateTimeProperty.class, DecimalProperty.class, FloatProperty.class,
		IntegerProperty.class, LongTextProperty.class,
		ReferenceProperty.class, StringProperty.class, ExpressionProperty.class, SelectProperty.class, TimeProperty.class })
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS)
public abstract class PropertyDefinition implements Serializable {

	private static final long serialVersionUID = -7811682916880170135L;

	public static final int MULTIPLICITY_INFINITE = -1;

	/** プロパティ名 */
	private String name;

	/** 表示名 */
	@MultiLang(itemNameGetter = "getName", itemKey = "displayName", itemGetter = "getDisplayName", itemSetter = "setDisplayName", multiLangGetter = "getLocalizedDisplayNameList", multiLangSetter = "setLocalizedDisplayNameList")
	private String displayName;

	/** 説明 */
	private String description;

	/** デフォルト値 */
	private String defaultValue;
	//TODO デフォルト値の機能の実装。Scriptか？

	/** 変更可能項目か否か */
	private boolean updatable = true;//TODO oid項目は変更不可項目である必要あり。でないと、更新範囲（参照テーブル、Indexテーブル、、、）が大きい。。。

	/** この項目が読み取り専用かどうか（自動採番ID、更新者、更新日、数式等のシステムによる自動設定項目の意） */
	private boolean readOnly;

	//TODO 多重度が指定されているプロパティは、インデックス不可。
	//TODO プリミティブタイプの配列の場合は必ず指定（最大値。maxは128位か、、別途設定ファイルで指定とする）。ReferencePropertyの場合のみ、*指定（値的には-1）可能。
	/** 多重度 */
	private int multiplicity = 1;

	@MultiLang(itemNameGetter = "getName", isMultiLangValue = false, itemKey = "validation", itemGetter = "getValidations", itemSetter = "setValidations")
	private List<ValidationDefinition> validations;

	private List<NormalizerDefinition> normalizers;

	/** 多言語化用文字情報リスト */
	private List<LocalizedStringDefinition> localizedDisplayNameList;

	//TODO 複合INDEXをネイティブにサポートするかどうか？（Indexテーブルに親子関係を持たせれば、できなくはないが、速い気がしない。）
	//     もしくは、もうちょっと改良して、String型（として扱ってよい項目）が連続する場合は同一Indexテーブルカラムに-区切りなどとして格納。Date型や数値型がきたところで、サブIndexテーブルに格納。（そこまでやって相応の速度をだせるかどうか。。。）
	private IndexType indexType;

	/** このプロパティが継承されたものかどうか */
	private boolean inherited;

	public List<NormalizerDefinition> getNormalizers() {
		return normalizers;
	}

	public void setNormalizers(List<NormalizerDefinition> normalizers) {
		this.normalizers = normalizers;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public boolean isUpdatable() {
		return updatable;
	}

	public void setUpdatable(boolean updatable) {
		this.updatable = updatable;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getMultiplicity() {
		return multiplicity;
	}

	public void setMultiplicity(int multiplicity) {
		this.multiplicity = multiplicity;
	}

	public boolean isInherited() {
		return inherited;
	}

	public void setInherited(boolean inherited) {
		this.inherited = inherited;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public PropertyDefinition() {
	}

	public abstract Class<?> getJavaType();

	/**
	 * enumでPropertyの型を返す。
	 * switch/case文で利用可能なように。
	 *
	 * @return
	 */
	public abstract PropertyDefinitionType getType();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public List<ValidationDefinition> getValidations() {
		return validations;
	}

	public void setValidations(List<ValidationDefinition> validations) {
		this.validations = validations;
	}

	public List<LocalizedStringDefinition> getLocalizedDisplayNameList() {
		return localizedDisplayNameList;
	}

	public void setLocalizedDisplayNameList(List<LocalizedStringDefinition> localizedDisplayNameList) {
		this.localizedDisplayNameList = localizedDisplayNameList;
	}

	public void addLocalizedDisplayName(LocalizedStringDefinition localizedDisplayName) {
		if (localizedDisplayNameList == null) {
			localizedDisplayNameList = new ArrayList<LocalizedStringDefinition>();
		}

		localizedDisplayNameList.add(localizedDisplayName);
	}

//	/**
//	 * 実装クラスで使用することを想定した、{@link Definition#copy()}を実装する際のユーティリティメソッド。
//	 * @param copy コピー対象のインスタンス
//	 */
//	protected void copyTo(PropertyDefinition copy) {
//		copy.displayName = displayName;
//		copy.indexType = indexType;
//		copy.multiple = multiple;
//		copy.name = name;
//		copy.readOnly = readOnly;
//		if (validations != null) {
//			ArrayList<ValidationDefinition> copyVals = new ArrayList<ValidationDefinition>();
//			for (ValidationDefinition valDef: validations) {
//				copyVals.add(valDef.copy());
//			}
//			copy.validations = copyVals;
//		}
//	}

	public void setIndexType(IndexType indexType) {
		this.indexType = indexType;
	}

	public IndexType getIndexType() {
		return indexType;
	}

//	public abstract PropertyDefinition copy();

}
