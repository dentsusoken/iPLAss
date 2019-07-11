/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.runtime;

/**
 * EntryPointのBuilderです。
 * 
 * @author K.Higuchi
 *
 */
public interface EntryPointBuilder {
	
	/**
	 * EntryPointを構築します。
	 * すでに初期化済みのEntryPointが存在する場合、
	 * SystemExceptionがスローされます。
	 * 
	 * @return
	 */
	public EntryPoint build();
	
	/**
	 * iPLAssのBootstrapプロパティが指定されているプロパティファイルのパスを指定します。
	 * serverEnvFileが未指定の場合は、システムプロパティをBootstrapプロパティとして利用します。
	 * 
	 * @param serverEnvFile
	 * @return
	 */
	public EntryPointBuilder serverEnvFile(String serverEnvFile);
	
	/**
	 * このiPLAsssのインスタンスのserverIdを指定します。
	 * 未指定の場合は、Host名がserverIdとなります。
	 * 
	 * @param serverId
	 * @return
	 */
	public EntryPointBuilder serverId(String serverId);
	
	/**
	 * このiPLAsssのインスタンスのserverRoleを必要に応じて指定します。
	 * 
	 * @param serverRole
	 * @return
	 */
	public EntryPointBuilder serverRole(String... serverRole);
	
	/**
	 * 設定ファイル（service-config.xml）のパスを指定します。
	 * 未指定の場合のデフォルト値は、"/mtp-service-config.xml"です。
	 * 
	 * @param configFileName
	 * @return
	 */
	public EntryPointBuilder config(String configFileName);
	
	/**
	 * service-config.xmlを難読化する場合、難読化設定ファイル（crypt.properties）のパスを指定します。
	 * 
	 * @param configCryptFileName
	 * @return
	 */
	public EntryPointBuilder crypt(String configCryptFileName);
	
	/**
	 * 設定ファイルを読み込むloaderClass名を指定します。
	 * 未指定の場合はデフォルトのloaderが利用されます。
	 * 
	 * @param loaderClassName
	 * @return
	 */
	public EntryPointBuilder loader(String loaderClassName);
	
	/**
	 * iPLAssのBootstrapプロパティをnameを指定して設定します。
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public EntryPointBuilder property(String name, String value);
	
}
