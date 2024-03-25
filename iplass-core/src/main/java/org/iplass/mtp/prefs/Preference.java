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

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.definition.Definition;

/**
 * テナント単位に定義可能な汎用の設定を表す。
 * name、valueのペアを持つ。
 * また、この設定を元に実行時に初期化、インスタンス化されるruntimeClassを定義することが可能。
 * runtimeClassは、この設定の読み込み時に1度だけ初期化され、テナントが有効の間、同一のインスタンスが保持される。
 * ただし、テナント起動中に、Preferenceの変更・再読み込みが発生した場合は、以前のruntimeClassのインスタンスが破棄（GC対象に）され、
 * 新たなインスタンスが生成され初期化される。<br>
 * 
 * runtimeClassはデフォルトコンストラクタを持つ、POJOもしくはPreferenceAwareを実装したクラス。<br>
 * POJOの場合は、次の形でPreferenceの値が設定される。
 * <ul>
 * <li>Preference:POJOのname,valueプロパティに値をセット。その名前のプロパティがない場合は、セットしない。</li>
 * <li>PreferenceSet:上記に加えて、subSetで指定されている各Preferenceのnameをプロパティ名としてvalueを値としてセット。</li>
 * </ul>
 * PreferenceAware実装クラスの場合は、初期化時にinitializeメソッドが呼び出される。
 * 
 * runtimeClassにはutilityClassとして作成されたGroovyクラスの指定も可能。
 * utilityClassの場合は、そのクラスはGroovyコード内では可視であるが、javaで実装されたクラスからはGroovyObjectとしてのみ参照可能。
 * 
 * @author K.Higuchi
 * @see PreferenceAware
 *
 */
@XmlSeeAlso(PreferenceSet.class)
@XmlRootElement
public class Preference implements Definition {
	private static final long serialVersionUID = -2478688296064953043L;
	
	private String name;
	private String displayName;
	private String description;
	private String value;
	
	private String runtimeClassName;
	
	/**
	 * コンストラクタ。
	 */
	public Preference() {
	}

	/**
	 * コンストラクタ。
	 * 
	 * @param name 設定名
	 * @param value 設定値
	 */
	public Preference(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	/**
	 * コンストラクタ。
	 * 
	 * @param name 設定名
	 * @param value 設定値
	 * @param runtimeClassName runtimeClass名
	 */
	public Preference(String name, String value, String runtimeClassName) {
		this.name = name;
		this.value = value;
		this.runtimeClassName = runtimeClassName;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getRuntimeClassName() {
		return runtimeClassName;
	}

	public void setRuntimeClassName(String runtimeClassName) {
		this.runtimeClassName = runtimeClassName;
	}
	
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 設定値を取得する。
	 * もし、設定値が未設定の場合、defaultValueを返却する。
	 * 
	 * @param defaultValue
	 * @return
	 */
	public String getValue(String defaultValue) {
		if (value == null) {
			return defaultValue;
		}
		return value;
	}
	
}
