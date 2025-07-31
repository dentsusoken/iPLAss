/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.webapi;

import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ClassUtil;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.webapi.definition.WebApiResultAttribute;

/**
 * WebAPI 結果属性メタデータ
 * <p>
 * WebAPI レスポンスに関する属性を設定します。
 * </p>
 * @author SEKIGUCHI Naoya
 */
public class MetaWebApiResultAttribute implements MetaData {
	/** serialVersionUID */
	private static final long serialVersionUID = -3523907144750010550L;

	/** レスポンス属性名 */
	private String name;
	/** レスポンスデータ型 */
	private String dataType;

	/**
	 * レスポンス属性名を取得します。
	 * @return レスポンス属性名
	 */
	public String getName() {
		return name;
	}

	/**
	 * レスポンス属性名を設定します。
	 * @param name レスポンス属性名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * レスポンスデータ型を取得します。
	 * @return レスポンスデータ型
	 */
	public String getDataType() {
		return dataType;
	}

	/**
	 * レスポンスデータ型を設定します。
	 * @param dataType レスポンスデータ型
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	/**
	 * Definition インスタンスの設定を現在の設定に適用します。
	 * <p>
	 * 本メソッドは、Definition から Meta への変換メソッドです。
	 * </p>
	 * @param definition 適用する Definition インスタンス
	 */
	public void applyConfig(WebApiResultAttribute definition) {
		name = definition.getName();
		dataType = definition.getDataType();
	}

	/**
	 * 現在の設定を Definition インスタンスに変換します。
	 * <p>
	 * 本メソッドは、Meta から Definition への変換メソッドです。
	 * </p>
	 * @return 現在の設定を反映した Definition インスタンス
	 */
	public WebApiResultAttribute currentConfig() {
		WebApiResultAttribute definition = new WebApiResultAttribute();
		definition.setName(name);
		definition.setDataType(dataType);
		return definition;
	}

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

	/**
	 * このメタデータのランタイムインスタンスを生成します。
	 * @return このメタデータのランタイムインスタンス
	 */
	public WebApiResultAttributeRuntime createRuntime() {
		return new WebApiResultAttributeRuntime(this);
	}

	/**
	 * WebAPI 結果属性のランタイムインスタンス
	 */
	public static class WebApiResultAttributeRuntime {
		/** メタデータ */
		private MetaWebApiResultAttribute meta;
		/** データタイプクラス */
		private Class<?> dataType;

		/**
		 * コンストラクタ
		 * @param meta メタデータ
		 */
		public WebApiResultAttributeRuntime(MetaWebApiResultAttribute meta) {
			this.meta = meta;
			if (StringUtil.isNotEmpty(meta.dataType)) {
				this.dataType = ClassUtil.forName(meta.dataType);
			}
		}

		/**
		 * レスポンス属性名を取得します。
		 * @return レスポンス属性名
		 */
		public String getName() {
			return meta.getName();
		}

		/**
		 * レスポンスデータ型を取得します。
		 * @return レスポンスデータ型
		 */
		public Class<?> getDataType() {
			return dataType;
		}
	}

}
