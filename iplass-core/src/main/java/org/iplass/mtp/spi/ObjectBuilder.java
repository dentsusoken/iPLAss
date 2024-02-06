/*
 * Copyright (C) 2019 DENTSU SOKEN INC. All Rights Reserved.
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

import java.util.Map;



/**
 * <% if (doclang == 'ja') {%>
 *  Service定義配下のproperty/beanの値の生成処理をカスタマイズする際に利用可能な
 *  Builderのインタフェースです。
 * <%} else {%>
 *  The Builder interface that can be used to customize the Service definition's property/bean value generation process.
 * <%}%>
 * 
 * @author K.Higuchi
 *
 * @param <T>
 */
public interface ObjectBuilder<T> {
	
	/**
	 * <% if (doclang == 'ja') {%>
	 *  設定ファイルのproperty/bean定義に指定されたname属性がセットされます。
	 * <%} else {%>
	 *  The name attribute specified in the property/bean definition of the configuration file is set.
	 * <%}%>
	 * 
	 * @param name
	 */
	public default void setName(String name) {
	}
	
	/**
	 * <% if (doclang == 'ja') {%>
	 *  設定ファイルのproperty定義にvalue属性（もしくはネストされたvalue要素）が指定されている場合、その値がセットされます。
	 * <%} else {%>
	 *  If value attribute (or nested value element) is specified in property definition of configuration file, that value will be set.
	 * <%}%>
	 * 
	 * @param value
	 */
	public default void setValue(String value) {
	}
	
	/**
	 * <% if (doclang == 'ja') {%>
	 *  設定ファイルのproperty/bean定義にclass属性が指定されている場合、その値がセットされます。
	 * <%} else {%>
	 *  If class attribute is specified in property/bean definition of configuration file, that value will be set.
	 * <%}%>
	 * 
	 * @param className
	 */
	public default void setClassName(String className) {
	}
	
	/**
	 * <% if (doclang == 'ja') {%>
	 *  設定ファイルのproperty/bean定義にネストされたproperty定義が存在する場合、その値がセットされます。
	 *  同一nameのproperty定義が複数存在した場合、propertiesのvalueにはList型で複数のインスタンスが保持されます。
	 * <%} else {%>
	 *  If there is a nested property definition in the property/bean definition of the configuration file, its value is set.
	 *  If there are multiple property definitions with the same name,
	 *  multiple instances are stored in the map value as List type.
	 * <%}%>
	 * 
	 * @param properties
	 */
	public default void setProperties(Map<String, Object> properties) {
	}
	
	/**
	 * <% if (doclang == 'ja') {%>
	 *  設定ファイルのproperty/bean定義にネストされたarg定義が存在する場合、その値がセットされます。
	 *  同一nameのarg定義が複数存在した場合、argsのvalueにはList型で複数のインスタンスが保持されます。
	 * <%} else {%>
	 *  If there is a nested arg definition in the property/bean definition of the configuration file, its value is set.
	 *  If there are multiple arg definitions with the same name,
	 *  multiple instances are stored in the map value as List type.
	 * <%}%>
	 * 
	 * @param args
	 */
	public default void setArgs(Map<String, Object> args) {
	}
	
	/**
	 * <% if (doclang == 'ja') {%>
	 *  インスタンスを生成して返却するように実装します。
	 * <%} else {%>
	 *  Implement to generate and return an instance.
	 * <%}%>
	 * 
	 * @return
	 */
	public T build();
	
}
