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
package org.iplass.mtp.auth.oauth.definition;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.auth.oauth.definition.consents.AlwaysConsentTypeDefinition;
import org.iplass.mtp.auth.oauth.definition.consents.OnceConsentTypeDefinition;
import org.iplass.mtp.auth.oauth.definition.consents.ScriptingConsentTypeDefinition;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * スコープ承認画面の表示有無を判断するための定義です。
 * 
 * @author K.Higuchi
 *
 */
@XmlSeeAlso({
	AlwaysConsentTypeDefinition.class,
	OnceConsentTypeDefinition.class,
	ScriptingConsentTypeDefinition.class})
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS)
public abstract class ConsentTypeDefinition implements Serializable {
	private static final long serialVersionUID = 4679081921898475535L;
}
