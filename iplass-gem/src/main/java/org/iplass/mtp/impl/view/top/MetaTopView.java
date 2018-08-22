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

package org.iplass.mtp.impl.view.top;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.impl.definition.DefinableMetaData;
import org.iplass.mtp.impl.metadata.BaseRootMetaData;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.view.top.parts.MetaTopViewParts;
import org.iplass.mtp.view.top.TopViewDefinition;
import org.iplass.mtp.view.top.parts.TopViewParts;

/**
 * TOP画面定義
 * @author lis3wg
 */
public class MetaTopView extends BaseRootMetaData implements DefinableMetaData<TopViewDefinition> {

	/** SerialVersionUID */
	private static final long serialVersionUID = 6339958147628350370L;

	/** 画面パーツ */
	private List<MetaTopViewParts> parts;

	/** ウィジェット */
	private List<MetaTopViewParts> widgets;

	@Override
	public MetaTopView copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public TopViewHandler createRuntime(MetaDataConfig metaDataConfig) {
		return new TopViewHandler(this);
	}

	/**
	 * 画面パーツを取得します。
	 * @return 画面パーツ
	 */
	public List<MetaTopViewParts> getParts() {
		if (parts == null) parts = new ArrayList<MetaTopViewParts>();
	    return parts;
	}

	/**
	 * 画面パーツを設定します。
	 * @param parts 画面パーツ
	 */
	public void setParts(List<MetaTopViewParts> parts) {
	    this.parts = parts;
	}

	/**
	 * 画面パーツを追加します。
	 * @param parts 画面パーツ
	 */
	public void addParts(MetaTopViewParts parts) {
		getParts().add(parts);
	}

	/**
	 * ウィジェットを取得します。
	 * @return ウィジェット
	 */
	public List<MetaTopViewParts> getWidgets() {
		if (widgets == null) widgets = new ArrayList<MetaTopViewParts>();
	    return widgets;
	}

	/**
	 * ウィジェットを設定します。
	 * @param widgets ウィジェット
	 */
	public void setWidgets(List<MetaTopViewParts> widgets) {
	    this.widgets = widgets;
	}

	/**
	 * ウィジェットを追加します。
	 * @param wdget ウィジェット
	 */
	public void addWidget(MetaTopViewParts widget) {
		getWidgets().add(widget);
	}

	/**
	 * TOP画面定義をメタデータに変換します。
	 * @param definition TOP画面定義
	 */
	public void applyConfig(TopViewDefinition definition) {
		this.name = definition.getName();
		this.displayName = definition.getDisplayName();
		this.description = definition.getDescription();
		for (TopViewParts parts : definition.getParts()) {
			MetaTopViewParts meta = MetaTopViewParts.createInstance(parts);
			if (meta != null) {
				meta.applyConfig(parts);
				addParts(meta);
			}
		}
		for (TopViewParts widget : definition.getWidgets()) {
			MetaTopViewParts meta = MetaTopViewParts.createInstance(widget);
			if (meta != null) {
				meta.applyConfig(widget);
				addWidget(meta);
			}
		}
	}

	/**
	 * メタデータをTOP画面定義に変換します。
	 * @return TOP画面定義
	 */
	public TopViewDefinition currentConfig() {
		TopViewDefinition definition = new TopViewDefinition();
		definition.setName(name);
		definition.setDisplayName(displayName);
		definition.setDescription(description);
		for (MetaTopViewParts meta : getParts()) {
			TopViewParts parts = meta.currentConfig();
			if (parts != null) definition.addParts(parts);
		}
		for (MetaTopViewParts meta : getWidgets()) {
			TopViewParts parts = meta.currentConfig();
			if (parts != null) definition.addWidget(parts);
		}
		return definition;
	}
}
