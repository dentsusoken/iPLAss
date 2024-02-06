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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 複数のPreferenceを保持するPreference。
 * 複数の設定値をまとめて管理したい場合に利用する。
 *
 * @author K.Higuchi
 *
 */
@XmlRootElement
public class PreferenceSet extends Preference {
	private static final long serialVersionUID = -6340236717961297709L;

	private List<Preference> subSet;

	private transient Map<String, Preference[]> map;


	public PreferenceSet() {
	}

	public PreferenceSet(String name, List<Preference> subSet) {
		super(name, null);
		this.subSet = subSet;
	}

	public PreferenceSet(String name, Preference... prefs) {
		this(name, null, prefs);
	}

	public PreferenceSet(String name, String runtimeClassName, Preference... prefs) {
		super(name, null);
		setRuntimeClassName(runtimeClassName);
		subSet = new ArrayList<>();
		if (prefs != null) {
			for (Preference p: prefs) {
				subSet.add(p);
			}
		}
	}

	public List<Preference> getSubSet() {
		return subSet;
	}

	public void setSubSet(List<Preference> subSet) {
		this.subSet = subSet;
	}

	/**
	 * 設定値を取得する。
	 * もし、設定値が未設定の場合、defaultValueを返却する。
	 * subSetの値を再帰的に取得することが可能。
	 * subPathの区切りとして"/"を利用。
	 * 単一のnameに複数の値が紐づいている場合は、[0]の形で配列index指定可能。
	 * 例："prefA/subX[1]/subSubY"
	 *
	 * @param defaultValue
	 * @return
	 */
	public String getValue(String subPath, String defaultValue) {

		Object p = getPrefCascade(subPath);
		if (p == null) {
			return defaultValue;
		}
		if (p instanceof Preference[]) {
			return ((Preference[]) p)[0].getValue(defaultValue);
		}
		return ((Preference) p).getValue(defaultValue);
	}

	private void initMap() {
		if (map == null) {
			if (subSet == null) {
				map = Collections.emptyMap();
			} else {
				map = new HashMap<>();
				for (Preference p: subSet) {
					Preference[] pre = map.get(p.getName());
					if (pre == null) {
						map.put(p.getName(), new Preference[]{p});
					} else {
						Preference[] newPre = new Preference[pre.length + 1];
						System.arraycopy(pre, 0, newPre, 0, pre.length);
						newPre[pre.length] = p;
						map.put(p.getName(), newPre);
					}
				}
			}
		}
	}

	private Object getPrefCascade(String path) {
		initMap();

		int sl = path.indexOf('/');
		String path1;
		String path2 = null;
		if (sl > 0) {
			path1 = path.substring(0, sl);
			path2 = path.substring(sl + 1);
		} else {
			path1 = path;
		}

		String subName;
		int index = -1;
		int pa1 = path1.indexOf('[');
		int pa2 = path1.indexOf(']');
		if (pa1 > 0) {
			if (pa2 > 0 && pa1 < pa2) {
				index = Integer.parseInt(path1.substring(pa1 + 1, pa2));
			} else {
				throw new ArrayIndexOutOfBoundsException("cant parse array expression:" + path);
			}
			subName = path1.substring(0, pa1);
		} else {
			subName = path1;
		}

		Preference[] subp = map.get(subName);
		if (subp == null) {
			return null;
		}
		if (index == -1) {
			if (path2 == null) {
				return subp;
			} else {
				return ((PreferenceSet) subp[0]).getPrefCascade(path2);
			}
		} else {
			if (path2 == null) {
				return subp[index];
			} else {
				return ((PreferenceSet) subp[index]).getPrefCascade(path2);
			}
		}
	}

	/**
	 * subPathで指定されるsubSetを再帰的に取得し、Preference[]の形で返却する。
	 * subPathの区切りとして"/"を利用。
	 * 単一のnameに複数の値が紐づいている場合は、[0]の形で配列index指定可能。
	 *
	 * @param subPath
	 * @return
	 */
	public Preference[] getSubSet(String subPath) {
		Object p = getPrefCascade(subPath);
		if (p == null) {
			return new Preference[0];
		}
		if (p instanceof Preference) {
			return new Preference[]{(Preference) p};
		}
		return (Preference[]) p;
	}

}
