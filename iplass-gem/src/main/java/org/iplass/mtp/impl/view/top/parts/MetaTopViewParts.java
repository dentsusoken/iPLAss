/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import javax.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.view.top.parts.ActionParts;
import org.iplass.mtp.view.top.parts.ApplicationMaintenanceParts;
import org.iplass.mtp.view.top.parts.CsvDownloadSettingsParts;
import org.iplass.mtp.view.top.parts.FulltextSearchViewParts;
import org.iplass.mtp.view.top.parts.ScriptParts;
import org.iplass.mtp.view.top.parts.SeparatorParts;
import org.iplass.mtp.view.top.parts.TemplateParts;
import org.iplass.mtp.view.top.parts.TopViewParts;
import org.iplass.mtp.view.top.parts.UserMaintenanceParts;

/**
 * TOP画面パーツ
 * @author lis3wg
 */
@XmlSeeAlso({MetaActionParts.class, MetaTemplateParts.class, MetaScriptParts.class,
	MetaSeparatorParts.class, MetaUserMaintenanceParts.class, MetaFulltextSearchViewParts.class,
	MetaCsvDownloadSettingsParts.class, MetaApplicationMaintenanceParts.class})
public abstract class MetaTopViewParts implements MetaData {

	/** SerialVersionUID */
	private static final long serialVersionUID = 329805942769537467L;

	/**
	 * インスタンスを生成します。
	 * @param parts TOP画面パーツ
	 * @return インスタンス
	 */
	public static MetaTopViewParts createInstance(TopViewParts parts) {
		if (parts == null) return null;
		if (parts instanceof ActionParts) {
			return MetaActionParts.createInstance(parts);
		} else if (parts instanceof TemplateParts) {
			return MetaTemplateParts.createInstance(parts);
		} else if (parts instanceof ScriptParts) {
			return MetaScriptParts.createInstance(parts);
		} else if (parts instanceof SeparatorParts) {
			return MetaSeparatorParts.createInstance(parts);
		} else if (parts instanceof UserMaintenanceParts) {
			return MetaUserMaintenanceParts.createInstance(parts);
		} else if (parts instanceof FulltextSearchViewParts) {
			return MetaFulltextSearchViewParts.createInstance(parts);
		} else if (parts instanceof CsvDownloadSettingsParts) {
			return MetaCsvDownloadSettingsParts.createInstance(parts);
		} else if (parts instanceof ApplicationMaintenanceParts) {
			return MetaApplicationMaintenanceParts.createInstance(parts);
		}
		return null;
	}

	/**
	 * 定義の内容を自身に設定します。
	 * @param parts 定義
	 */
	protected void fillFrom(TopViewParts parts) {
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
	public abstract TopViewPartsHandler createRuntime();
}
