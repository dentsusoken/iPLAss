/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.view.generic.common;

import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.view.generic.EntityViewRuntime;
import org.iplass.mtp.view.generic.common.AutocompletionSetting;
import org.iplass.mtp.view.generic.common.JavascriptAutocompletionSetting;

public class MetaJavascriptAutocompletionSetting extends MetaAutocompletionSetting {

	private static final long serialVersionUID = -8199611279009246728L;

	public static MetaAutocompletionSetting createInstance(AutocompletionSetting setting) {
		return new MetaJavascriptAutocompletionSetting();
	}

	/** Javascript */
	private String javascript;

	/**
	 * @return javascript
	 */
	public String getJavascript() {
		return javascript;
	}

	/**
	 * @param javascript セットする javascript
	 */
	public void setJavascript(String javascript) {
		this.javascript = javascript;
	}

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public void applyConfig(AutocompletionSetting setting, EntityHandler entity, EntityHandler rootEntity) {
		super.fillFrom(setting, entity, rootEntity);

		JavascriptAutocompletionSetting _setting = (JavascriptAutocompletionSetting) setting;
		javascript = _setting.getJavascript();
	}

	@Override
	public AutocompletionSetting currentConfig(EntityHandler entity, EntityHandler rootEntity) {
		JavascriptAutocompletionSetting setting = new JavascriptAutocompletionSetting();

		super.fillTo(setting, entity, rootEntity);

		setting.setJavascript(javascript);
		return setting;
	}

	@Override
	public AutocompletionSettingRuntime createRuntime(EntityViewRuntime entityView) {
		// server側で処理しないのでhandlerは作らない
		return null;
	}

}
