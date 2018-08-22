/*
 * Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.command.annotation.template;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.iplass.mtp.definition.annotation.LocalizedString;
import org.iplass.mtp.web.template.definition.TemplateDefinition;

/**
 * Templateを定義するアノテーションです。
 *
 * @see TemplateDefinition
 * @author Y.Yasuda
 */
@Repeatable(Templates.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Template {
	String id() default "##default";
	String name();
	String displayName() default "##default";
	LocalizedString[] localizedDisplayName() default {};
	String description() default "##default";

	/**
	 * このTemplateのContent-Typeを設定します。
	 *
	 * @return
	 */
	String contentType() default "";

	/**
	 * JSPファイルのパスを設定します。
	 * JSPファイルが「src/main/resources/META-INF/resources/jsp/web/sample.jsp」の場合、
	 * 「/jsp/web/sample.jsp」と設定してください。
	 *
	 * @return
	 */
	String path();

	/**
	 * LayoutAction名を設定します。
	 */
	String layoutActionName() default "";

	/**
	 * ローカルテナントで上書き可能か否か。
	 * @return
	 */
	boolean overwritable() default true;

}
