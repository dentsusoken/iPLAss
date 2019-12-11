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
 * <% if (doclang == "ja") {%>
 * EntryPointのBuilderです。
 * <%} else {%>
 * Builder of EntryPoint.
 * <%}%>
 * 
 * @author K.Higuchi
 *
 */
public interface EntryPointBuilder {
	
	/**
	 * <% if (doclang == "ja") {%>
	 * EntryPointを構築します。
	 * すでに初期化済みのEntryPointが存在する場合、
	 * AlreadyInitializedExceptionがスローされます。
	 * <%} else {%>
	 * Build a EntryPoint.
	 * If there is an EntryPoint that has already been initialized,
	 * an AlreadyInitializedException is thrown.
	 * <%}%>
	 * 
	 * @return <%=doclang == 'ja' ? 'EntryPointのインスタンス': 'EntryPoint instance'%>
	 * @throws AlreadyInitializedException <%=doclang == 'ja' ? 'EntryPointが既に初期化済みの場合': 'When EntryPoint has already been initialized'%>
	 */
	public EntryPoint build();
	
	/**
	 * <% if (doclang == "ja") {%>
	 * iPLAssのBootstrapプロパティが指定されているプロパティファイルのパスを指定します。
	 * serverEnvFileが未指定の場合は、システムプロパティをBootstrapプロパティとして利用します。
	 * <%} else {%>
	 * Specify the path of the property file where the iPLAss Bootstrap property is specified.
	 * If serverEnvFile is not specified, system properties are used as Bootstrap properties.
	 * <%}%>
	 * 
	 * @param serverEnvFile
	 * @return
	 */
	public EntryPointBuilder serverEnvFile(String serverEnvFile);
	
	/**
	 * <% if (doclang == "ja") {%>
	 * このiPLAsssのインスタンスのserverIdを指定します。
	 * 未指定の場合は、Host名がserverIdとなります。
	 * <%} else {%>
	 * Specify the serverId of this iPLAsss instance.
	 * If not specified, the Host name will be serverId.
	 * <%}%>
	 * 
	 * @param serverId
	 * @return
	 */
	public EntryPointBuilder serverId(String serverId);
	
	/**
	 * <% if (doclang == "ja") {%>
	 * このiPLAsssのインスタンスのserverRoleを必要に応じて指定します。
	 * <%} else {%>
	 * Specify the serverRole of this iPLAsss instance as needed.
	 * <%}%>
	 * 
	 * @param serverRole
	 * @return
	 */
	public EntryPointBuilder serverRole(String... serverRole);
	
	/**
	 * <% if (doclang == "ja") {%>
	 * 設定ファイル（service-config.xml）のパスを指定します。
	 * 未指定の場合のデフォルト値は、"/mtp-service-config.xml"です。
	 * <%} else {%>
	 * Specify the path of the configuration file (service-config.xml).
	 * If not specified, the default value is "/mtp-service-config.xml".
	 * <%}%>
	 * 
	 * @param configFileName
	 * @return
	 */
	public EntryPointBuilder config(String configFileName);
	
	/**
	 * <% if (doclang == "ja") {%>
	 * service-config.xmlを難読化する場合、難読化設定ファイル（crypt.properties）のパスを指定します。
	 * <%} else {%>
	 * To encrypt service-config.xml,
	 * specify the path of the encryption configuration file (crypt.properties).
	 * <%}%>
	 * 
	 * @param configCryptFileName
	 * @return
	 */
	public EntryPointBuilder crypt(String configCryptFileName);
	
	/**
	 * <% if (doclang == "ja") {%>
	 * 設定ファイルを読み込むConfigLoaderのclass名を指定します。
	 * 未指定の場合はデフォルトのloaderが利用されます。
	 * <%} else {%>
	 * Specify the ConfigLoader class name to load the configuration file.
	 * If not specified, the default loader will be used.
	 * <%}%>
	 * 
	 * @param loaderClassName
	 * @return
	 */
	public EntryPointBuilder loader(String loaderClassName);
	
	/**
	 * <% if (doclang == "ja") {%>
	 * iPLAssのBootstrapプロパティをnameを指定して設定します。
	 * <%} else {%>
	 * Set the iPLAss Bootstrap property.
	 * <%}%>
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public EntryPointBuilder property(String name, String value);
	
}
