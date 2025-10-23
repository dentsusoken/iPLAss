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

package org.iplass.mtp.impl.view.top.parts;

import jakarta.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.view.top.TopViewHandler;
import org.iplass.mtp.view.top.parts.ApplicationMaintenanceParts;
import org.iplass.mtp.view.top.parts.CsvDownloadSettingsParts;
import org.iplass.mtp.view.top.parts.FulltextSearchViewParts;
import org.iplass.mtp.view.top.parts.PreviewDateParts;
import org.iplass.mtp.view.top.parts.ScriptParts;
import org.iplass.mtp.view.top.parts.TopViewContentParts;
import org.iplass.mtp.view.top.parts.TopViewParts;
import org.iplass.mtp.view.top.parts.UserMaintenanceParts;

/**
 * TOP画面パーツ
 * @author lis3wg
 */
@XmlSeeAlso({MetaTopViewContentParts.class, MetaScriptParts.class, MetaUserMaintenanceParts.class,
	MetaFulltextSearchViewParts.class, MetaCsvDownloadSettingsParts.class,
	MetaApplicationMaintenanceParts.class, MetaPreviewDateParts.class})
public abstract class MetaTopViewParts implements MetaData {

	/** SerialVersionUID */
	private static final long serialVersionUID = 329805942769537467L;

	/** パーツID */
	private String partsId;

	/**
	 * インスタンスを生成します。
	 * @param parts TOP画面パーツ
	 * @return インスタンス
	 */
	public static MetaTopViewParts createInstance(TopViewParts parts) {
		if (parts == null) return null;
		if (parts instanceof TopViewContentParts) {
			return MetaTopViewContentParts.createInstance(parts);
		} else if (parts instanceof ScriptParts) {
			return MetaScriptParts.createInstance(parts);
		} else if (parts instanceof UserMaintenanceParts) {
			return MetaUserMaintenanceParts.createInstance(parts);
		} else if (parts instanceof FulltextSearchViewParts) {
			return MetaFulltextSearchViewParts.createInstance(parts);
		} else if (parts instanceof CsvDownloadSettingsParts) {
			return MetaCsvDownloadSettingsParts.createInstance(parts);
		} else if (parts instanceof ApplicationMaintenanceParts) {
			return MetaApplicationMaintenanceParts.createInstance(parts);
		} else if (parts instanceof PreviewDateParts) {
			return MetaPreviewDateParts.createInstance(parts);
		}
		return null;
	}

	/**
	 * パーツIDを設定します
	 * @param partsId パーツID
	 */
	public void setPartsId(String partsId) {
		this.partsId = partsId;
	}

	/**
	 * パーツIDを取得します
	 * @return パーツID
	 */
	public String getPartsId() {
		return partsId;
	}

	/**
	 * パーツIDを生成するかどうか
	 * <p>
	 * デフォルトでは {@code false} を返します。サブクラスでパーツIDを自動生成したい場合はこのメソッドをオーバーライドして {@code true} を返してください。
	 * パーツIDを自動生成する必要がない場合は、オーバーライド不要です。
	 * </p>
	 * @return パーツIDを自動生成する場合は {@code true}、それ以外は {@code false}
	 */
	public boolean generatePartsId() {
		return false;
	}

	/**
	 * 定義の内容を自身に設定します。
	 * @param parts 定義
	 */
	protected void fillFrom(TopViewParts parts) {
		partsId = parts.getPartsId();
	}

	/**
	 * 定義の内容を自身に設定します。
	 * @param parts 定義
	 */
	public abstract void applyConfig(TopViewParts parts);

	/**
	 * 自身の内容を定義に設定します。
	 * @param parts 定義
	 */
	protected void fillTo(TopViewParts parts) {
		parts.setPartsId(partsId);
	}

	/**
	 * 自身の内容を定義に設定します。
	 * @return 定義
	 */
	public abstract TopViewParts currentConfig();

	/**
	 * ランタイムを生成します。
	 * @return ランタイム
	 */
	public abstract TopViewPartsHandler createRuntime(TopViewHandler topView);
}
