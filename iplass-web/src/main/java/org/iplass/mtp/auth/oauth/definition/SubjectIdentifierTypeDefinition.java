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

import jakarta.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.auth.oauth.definition.subtypes.PairwiseSubjectIdentifierTypeDefinition;
import org.iplass.mtp.auth.oauth.definition.subtypes.PublicSubjectIdentifierTypeDefinition;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * subjectIdの方式に関する定義です。
 * 
 * @author K.Higuchi
 *
 */
@XmlSeeAlso({
	PairwiseSubjectIdentifierTypeDefinition.class,
	PublicSubjectIdentifierTypeDefinition.class})
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS)
public class SubjectIdentifierTypeDefinition implements Serializable {
	private static final long serialVersionUID = 6494917476553206278L;
}
