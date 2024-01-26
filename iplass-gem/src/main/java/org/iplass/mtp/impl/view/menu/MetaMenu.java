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

package org.iplass.mtp.impl.view.menu;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.definition.DefinableMetaData;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.metadata.BaseMetaDataRuntime;
import org.iplass.mtp.impl.metadata.BaseRootMetaData;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.metadata.MetaDataRuntime;
import org.iplass.mtp.impl.script.Script;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.view.menu.ActionMenuItem;
import org.iplass.mtp.view.menu.EntityMenuItem;
import org.iplass.mtp.view.menu.MenuItem;
import org.iplass.mtp.view.menu.NodeMenuItem;
import org.iplass.mtp.view.menu.UrlMenuItem;

@XmlRootElement
@XmlSeeAlso({MetaActionMenu.class, MetaEntityMenu.class, MetaNodeMenu.class, MetaUrlMenu.class})
public abstract class MetaMenu extends BaseRootMetaData implements DefinableMetaData<MenuItem> {

	private static final long serialVersionUID = 5233713258294539616L;

	/** メニューアイコン */
	String icon;

	/** アイコンタグ */
	private String iconTag;

	/** イメージカラー */
	private String imageColor;

	/** カスタマイズ用スクリプト */
	private String customizeScript;

	// FIXME その他必要な項目 javaScriptのメソッド（onclickで利用）、target、class名

	@Override
	public MetaMenu copy() {
		return ObjectUtil.deepCopy(this);
	}

	//Definition → Meta インスタンス
	public static MetaMenu createInstance(MenuItem definition) {
		if (definition instanceof ActionMenuItem) {
			return new MetaActionMenu();
		} else if (definition instanceof EntityMenuItem) {
			return new MetaEntityMenu();
		} else if (definition instanceof NodeMenuItem) {
			return new MetaNodeMenu();
		} else if (definition instanceof UrlMenuItem) {
			return new MetaUrlMenu();
		}
		return null;
	}

	//Definition → Meta
	public abstract void applyConfig(MenuItem definition);

	//Definition → Meta共通項目
	protected void fillFrom(MenuItem definition) {
		name = definition.getName();
		displayName = definition.getDisplayName();
		description = definition.getDescription();
		icon = definition.getImageUrl();
		iconTag = definition.getIconTag();
		imageColor = definition.getImageColor();
		customizeScript = definition.getCustomizeScript();
		// 言語毎の文字情報設定
		localizedDisplayNameList = I18nUtil.toMeta(definition.getLocalizedDisplayNameList());
	}

	//Meta → Definition
	public abstract MenuItem currentConfig();

	//Meta共通項目 → Definition
	protected void fillTo(MenuItem definition) {
		definition.setName(name);
		definition.setDisplayName(displayName);
		definition.setDescription(description);
		definition.setImageUrl(icon);
		definition.setIconTag(iconTag);
		definition.setImageColor(imageColor);
		definition.setCustomizeScript(customizeScript);
		definition.setLocalizedDisplayNameList(I18nUtil.toDef(localizedDisplayNameList));
	}

	/**
	 * メニューアイコンを取得します。
	 * @return メニューアイコン
	 */
	public String getIcon() {
	    return icon;
	}

	/**
	 * メニューアイコンを設定します。
	 * @param icon メニューアイコン
	 */
	public void setIcon(String icon) {
	    this.icon = icon;
	}

	/**
	 * イメージカラーを取得します。
	 * @return イメージカラー
	 */
	public String getImageColor() {
	    return imageColor;
	}

	/**
	 * イメージカラーを設定します。
	 * @param imageColor イメージカラー
	 */
	public void setImageColor(String imageColor) {
	    this.imageColor = imageColor;
	}

	/**
	 * アイコンタグを取得します。
	 * @return アイコンタグ
	 */
	public String getIconTag() {
	    return iconTag;
	}

	/**
	 * アイコンタグを設定します。
	 * @param iconTag アイコンタグ
	 */
	public void setIconTag(String iconTag) {
	    this.iconTag = iconTag;
	}

	/**
	 * カスタマイズ用スクリプトを返します。
	 *
	 * @return カスタマイズ用スクリプト
	 */
	public String getCustomizeScript() {
		return customizeScript;
	}

	/**
	 * カスタマイズ用スクリプトを設定します。
	 *
	 * @param customizeScript カスタマイズ用スクリプト
	 */
	public void setCustomizeScript(String customizeScript) {
		this.customizeScript = customizeScript;
	}

	public abstract MetaDataRuntime createRuntime(MetaDataConfig metaDataConfig);

	public abstract class MetaMenuHandler extends BaseMetaDataRuntime {

		private static final String SCRIPT_PREFIX = "MetaMenuHandler_customizeScript";

		public abstract MetaMenu getMetaData();

		private Script compiledCustomizeScript;

		public MetaMenuHandler() {

			ScriptEngine scriptEngine = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();

			if (MetaMenu.this.getCustomizeScript() != null) {
				String scriptName = SCRIPT_PREFIX + "_" + MetaMenu.this.getId();

				compiledCustomizeScript = scriptEngine.createScript(MetaMenu.this.getCustomizeScript(), scriptName);
			}

		}

		public Script getCompiledCustomizeScript() {
			return compiledCustomizeScript;
		}

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((icon == null) ? 0 : icon.hashCode());
		result = prime * result
				+ ((iconTag == null) ? 0 : iconTag.hashCode());
		result = prime * result
				+ ((imageColor == null) ? 0 : imageColor.hashCode());
		result = prime * result + ((customizeScript == null) ? 0 : customizeScript.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		MetaMenu other = (MetaMenu) obj;
		if (icon == null) {
			if (other.icon != null)
				return false;
		} else if (!icon.equals(other.icon))
			return false;
		if (iconTag == null) {
			if (other.iconTag != null)
				return false;
		} else if (!iconTag.equals(other.iconTag))
			return false;
		if (imageColor != other.imageColor)
			return false;
		if (customizeScript == null) {
			if (other.customizeScript != null)
			return false;
		} else if (!customizeScript.equals(other.customizeScript))
			return false;
		return true;
	}

}
