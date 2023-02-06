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
package org.iplass.mtp.auth.oauth.definition.subtypes;

import org.iplass.mtp.auth.oauth.definition.SubjectIdentifierTypeDefinition;

/**
 * client毎に異なるsubjectIdが付与されるSubjectIdentifierType定義です。
 * 特定のユーザー単位のIDをベースにclient毎にそのハッシュ値を返却します。
 * 
 * @author K.Higuchi
 *
 */
public class PairwiseSubjectIdentifierTypeDefinition extends SubjectIdentifierTypeDefinition {
	private static final long serialVersionUID = -8199680802708360459L;

	private String subjectIdMappedUserProperty;

	public String getSubjectIdMappedUserProperty() {
		return subjectIdMappedUserProperty;
	}

	/**
	 * ハッシュ値の元となるUserエンティティのプロパティを指定します。
	 * 当該プロパティの値はユーザー単位にユニークである必要があります。
	 * 
	 * @param subjectIdMappedUserProperty
	 */
	public void setSubjectIdMappedUserProperty(String subjectIdMappedUserProperty) {
		this.subjectIdMappedUserProperty = subjectIdMappedUserProperty;
	}
}
