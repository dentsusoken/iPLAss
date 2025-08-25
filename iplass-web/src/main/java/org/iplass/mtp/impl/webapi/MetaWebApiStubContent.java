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
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.webapi.definition.WebApiStubContent;

/**
 * WebAPI スタブコンテンツ メタデータ
 * <p>
 * WebAPI スタブコンテンツを管理します。
 * WebAPIとしては複数のスタブコンテンツを管理するため、自身の情報をラベリングし管理するためのクラス。
 * </p>
 * @author SEKIGUCHI Naoya
 */
public class MetaWebApiStubContent implements MetaData {
	/** ContentType */
	private String contentType;
	/** ラベル */
	private String label;
	/** コンテンツ */
	private String content;

	/**
	 * ContentType を取得します。
	 * <p>
	 * {@link #getContent()} で取得可能なコンテンツの ContentType です。
	 * </p>
	 * @return contentType ContentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * ContentType を設定します。
	 * <p>
	 * {@link #getContent()} で取得可能なコンテンツの ContentType です。
	 * </p>
	 * @param contentType セットする contentType
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * ラベルを取得します。
	 * <p>
	 * 本スタブコンテンツの識別用です。
	 * </p>
	 * @return label ラベル
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * ラベルを設定します。
	 * <p>
	 * 本スタブコンテンツの識別用です。
	 * </p>
	 * @param label ラベル
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * コンテンツを取得します。
	 * <p>
	 * スタブレスポンスとして返却するコンテンツです。
	 * </p>
	 * @return content コンテンツ
	 */
	public String getContent() {
		return content;
	}

	/**
	 * コンテンツを設定します。
	 * <p>
	 * スタブレスポンスとして返却するコンテンツです。
	 * {@link #getContentType()} で設定した ContentType に応じたコンテンツを設定してください。
	 * </p>
	 * @param content コンテンツ
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * Definition インスタンスの設定を現在の設定に適用します。
	 * <p>
	 * 本メソッドは、Definition から Meta への変換メソッドです。
	 * </p>
	 * @param definition 適用する Definition インスタンス
	 */
	public void applyConfig(WebApiStubContent definition) {
		this.contentType = definition.getContentType();
		this.label = definition.getLabel();
		this.content = definition.getContent();
	}

	/**
	 * 現在の設定を Definition インスタンスに変換します。
	 * <p>
	 * 本メソッドは、Meta から Definition への変換メソッドです。
	 * </p>
	 * @return 現在の設定を反映した Definition インスタンス
	 */
	public WebApiStubContent currentConfig() {
		WebApiStubContent definition = new WebApiStubContent();
		definition.setContentType(contentType);
		definition.setLabel(label);
		definition.setContent(content);
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
	public WebApiStubContentRuntime createRuntime() {
		return new WebApiStubContentRuntime(this);
	}

	/**
	 * WebAPI スタブコンテンツランタイム
	 */
	public static class WebApiStubContentRuntime {
		/** メタデータ */
		private MetaWebApiStubContent meta;

		/**
		 * コンストラクタ
		 * @param meta メタデータ
		 */
		public WebApiStubContentRuntime(MetaWebApiStubContent meta) {
			this.meta = meta;
		}

		/**
		 * メタデータを取得します。
		 * @return メタデータ
		 */
		public MetaWebApiStubContent getMetaData() {
			return meta;
		}
	}
}
