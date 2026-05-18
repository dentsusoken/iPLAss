/*
 * Copyright (C) 2026 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.webapi.openapi.schema;

/**
 * クラスのプロパティに関するスキーマ情報を表すクラス。
 * <p>
 * クラスからJSONSchemaを生成する際に、特定のクラス・プロパティの組み合わせまたはプロパティに、<br>
 * スキーマタイプや説明、フォーマットなどの追加情報を提供するために使用されます。<br>
 * 本クラスは、{@link org.iplass.mtp.impl.webapi.openapi.OpenApiService} のプロパティとして Service-Config から設定します。
 * </p>
 *
 * <h3>必須項目</h3>
 * <ul>
 * <li>propertyName: プロパティ名</li>
 * <li>schemaType: スキーマタイプ（STRING, INTEGER, BOOLEAN, DATE, DATETIMEのいずれか）</li>
 * </ul>
 *
 * @author SEKIGUCHI Naoya
 */
public class PropertySchema {
	/**
	 * スキーマタイプ
	 */
	public static enum SchemaType {
		/** 文字列 */
		STRING,
		/** 整数 */
		INTEGER,
		/** 真偽値 */
		BOOLEAN,
		/** 日付 */
		DATE,
		/** 日付時刻 */
		DATETIME;
	}

	/** 完全修飾クラス名 */
	private String className;
	/** プロパティ名 */
	private String propertyName;

	/** スキーマタイプ */
	private SchemaType schemaType;
	/** 説明 */
	private String description;
	/** フォーマット */
	private String format;

	/**
	 * 完全修飾クラス名を取得します。
	 *
	 * @return 完全修飾クラス名
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * 完全修飾クラス名を設定します。
	 *
	 * @param className 完全修飾クラス名
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * プロパティ名を取得します。
	 *
	 * @return プロパティ名
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * プロパティ名を設定します。
	 *
	 * @param propertyName プロパティ名
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * スキーマタイプを取得します。
	 *
	 * @return スキーマタイプ
	 */
	public SchemaType getSchemaType() {
		return schemaType;
	}

	/**
	 * スキーマタイプを設定します。
	 *
	 * @param schemaType スキーマタイプ
	 */
	public void setSchemaType(SchemaType schemaType) {
		this.schemaType = schemaType;
	}

	/**
	 * 説明を取得します。
	 *
	 * @return 説明
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 説明を設定します。
	 *
	 * @param description 説明
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * フォーマットを取得します。
	 *
	 * @return フォーマット
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * フォーマットを設定します。
	 *
	 * @param format フォーマット
	 */
	public void setFormat(String format) {
		this.format = format;
	}
}
