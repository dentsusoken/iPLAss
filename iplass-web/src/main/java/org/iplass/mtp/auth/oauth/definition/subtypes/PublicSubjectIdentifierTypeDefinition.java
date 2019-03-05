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
 * すべてのclientに共通のsubjectIdを返すSubjectIdentifierType定義です。
 * subjectIdとしてUserのプロパティ（oidやaccountIdなど）を利用可能ですが、
 * 外部にその値を公開したくない場合はその値をハッシュするように設定することも可能です。
 * 
 * @author K.Higuchi
 *
 */
public class PublicSubjectIdentifierTypeDefinition extends SubjectIdentifierTypeDefinition {
	private static final long serialVersionUID = 6212120199107521690L;

	private String subjectIdMappedUserProperty;
	private boolean hashing;
	
	public boolean isHashing() {
		return hashing;
	}

	/**
	 * Userエンティティのプロパティ値のハッシュ値を公開する場合、trueを設定します。
	 * 
	 * @param hashing
	 */
	public void setHashing(boolean hashing) {
		this.hashing = hashing;
	}

	public String getSubjectIdMappedUserProperty() {
		return subjectIdMappedUserProperty;
	}

	/**
	 * 公開用のsubjectIdとするUserエンティティのプロパティを指定します。
	 * 当該プロパティの値はユーザ単位にユニークである必要があります。
	 * 
	 * @param subjectIdMappedUserProperty
	 */
	public void setSubjectIdMappedUserProperty(String subjectIdMappedUserProperty) {
		this.subjectIdMappedUserProperty = subjectIdMappedUserProperty;
	}
}
