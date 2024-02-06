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
package org.iplass.mtp.spi;

import java.util.List;
import java.util.Set;

/**
 * Service初期化時の設定をあらわすインタフェースです。
 * 
 * Note:スレッドセーフではない点注意ください。 {@link Service#init(Config)}内のみで扱う想定のインスタンスです。
 * 
 * @author K.Higuchi
 *
 */
public interface Config {

	/**
	 * 設定ファイルに定義されているServiceのnameを取得します。
	 * 
	 * @return
	 */
	public String getServiceName();

	/**
	 * 設定ファイルに定義されているDependentServiceのnameの一覧を取得します。
	 * 
	 * @return
	 */
	public List<String> getDependentServiceNames();

	/**
	 * 設定ファイルに定義されているDependentServiceのインスタンスを取得します。
	 * 
	 * @param type Serviceのクラス。クラス名をnameとして取得。
	 * @return
	 */
	public <T extends Service> T getDependentService(Class<T> type);

	/**
	 * 設定ファイルに定義されているDependentServiceのインスタンスを取得します。
	 * 
	 * @param serviceName
	 * @return
	 */
	public <T extends Service> T getDependentService(String serviceName);

	/**
	 * 設定ファイルに定義されている設定項目（Service直下のproperty）のname一覧を取得します。
	 * @return
	 */
	public Set<String> getNames();

	/**
	 * 設定ファイルに定義されている設定項目（Service直下のproperty）に定義される値（value）をStringとして取得します。
	 * @param name
	 * @return
	 */
	public String getValue(String name);

	/**
	 * 設定ファイルに定義されている同一のnameの設定項目（Service直下のproperty）に定義される値（value）をList<String>として取得します。
	 * 
	 * @param name
	 * @return
	 */
	public List<String> getValues(String name);
	
	/**
	 * 設定ファイルに定義されている設定項目（Service直下のproperty）に定義される値を指定のtypeとして取得します。
	 * typeは、プリミティブ型、Map、JavaBeans形式のクラスを指定可能です。
	 * 
	 * @param name
	 * @param type
	 * @return
	 */
	public <T> T getValue(String name, Class<T> type);
	/**
	 * 設定ファイルに定義されている設定項目（Service直下のproperty）に定義される値を指定のtypeとして取得します。
	 * typeは、プリミティブ型、Map、JavaBeans形式のクラスを指定可能です。
	 * もし、nameで指定される設定項目が存在しない場合は、defaultValueを返却します。
	 * 
	 * @param name
	 * @param type
	 * @param defaultValue
	 * @return
	 */
	public <T> T getValue(String name, Class<T> type, T defaultValue);
	
	/**
	 * 設定ファイルに定義されている設定項目（Service直下のproperty）に定義される値を指定のtypeのListとして取得します。
	 * typeは、プリミティブ型、Map、JavaBeans形式のクラスを指定可能です。
	 * 
	 * @param name
	 * @param type
	 * @return
	 */
	public <T> List<T> getValues(String name, Class<T> type);
	
	/**
	 * 設定ファイルに定義されている設定項目（Service直下のproperty）に定義される値を指定のtypeのListとして取得します。
	 * typeは、プリミティブ型、Map、JavaBeans形式のクラスを指定可能です。
	 * もし、nameで指定される設定項目が存在しない場合は、defaultValueを返却します。
	 * 
	 * @param name
	 * @param type
	 * @param defaultValues
	 * @return
	 */
	public <T> List<T> getValues(String name, Class<T> type, List<T> defaultValues);

	/**
	 * 設定項目の型が不定の設定項目（Service直下のproperty）を取得します。
	 * 設定ファイル上のpropertyに、class属性が指定されている必要があります。
	 * 
	 * @param name
	 * @return
	 * @deprecated 当該メソッドは将来削除する予定です
	 */
	@Deprecated
	public Object getBean(String name);

	/**
	 * 設定項目の型が不定の設定項目（Service直下のproperty）のListを取得します。
	 * 設定ファイル上のpropertyに、class属性が指定されている必要があります。
	 * 
	 * @param name
	 * @return
	 * @deprecated 当該メソッドは将来削除する予定です
	 */
	@Deprecated
	public List<?> getBeans(String name);
	
}
