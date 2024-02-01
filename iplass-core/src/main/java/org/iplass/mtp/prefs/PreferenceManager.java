/*
 * Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.prefs;

import java.util.Map;

import org.iplass.mtp.definition.TypedDefinitionManager;

/**
 * PreferenceのManager。
 * Preferenceの取得、保存を行うことが可能。
 *
 *
 * @author K.Higuchi
 *
 */
public interface PreferenceManager extends TypedDefinitionManager<Preference> {

	/**
	 * 指定のnameのPreference（もしくは、PreferenceSet）を取得する。
	 * nullAsDefaultがtrueの場合は、"ブランクのPreference"が返却される（nullではない）。
	 *
	 * @param name
	 * @param nullAsdefault
	 * @return
	 */
	public Preference get(String name, boolean nullAsDefault);

	/**
	 * 指定のnameのPreference（PreferenceSet）をMapとして取得する。
	 * Mapには、keyにname、valueにvalueがセットされて取得される。
	 * PreferenceSetに同一nameのPreferenceエントリが複数あった場合は、単一のkeyに対して、Listがvalueにセットされる。
	 * PreferenceSetのsubSetにネストされたPreferenceSetが設定されている場合、MapにはMapがネストされてセットされる。
	 * もし、nameで指定されるPreferenceが、単一のPreferenceだった場合、
	 * Mapには、key="value"、value="実際の設定値"の形で返却される。
	 * nameで指定された、Preferenceが存在しない場合は、emptyMapが返却される。
	 * また、このメソッドで返却されるMapは変更不可に設定されている。
	 *
	 * @param name 取得するPreferenceのname
	 * @return
	 */
	public Map<String, Object> getAsMap(String name);

	/**
	 * 指定のnameのPreferenceのruntimeClassNameのインスタンスを取得する。
	 * PreferenceにruntimeClassName未指定の場合は、nullが返却される。
	 *
	 * @param name Preferenceのname
	 * @return
	 */
	public Object getRuntime(String name);
}
