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
package org.iplass.mtp.impl.auth.oauth;

import javax.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.auth.User;
import org.iplass.mtp.auth.oauth.definition.SubjectIdentifierTypeDefinition;
import org.iplass.mtp.auth.oauth.definition.subtypes.PairwiseSubjectIdentifierTypeDefinition;
import org.iplass.mtp.auth.oauth.definition.subtypes.PublicSubjectIdentifierTypeDefinition;
import org.iplass.mtp.impl.auth.oauth.MetaOAuthClient.OAuthClientRuntime;
import org.iplass.mtp.impl.auth.oauth.subtypes.MetaPairwiseSubjectIdentifierType;
import org.iplass.mtp.impl.auth.oauth.subtypes.MetaPublicSubjectIdentifierType;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;

@XmlSeeAlso({
	MetaPairwiseSubjectIdentifierType.class,
	MetaPublicSubjectIdentifierType.class
})
public abstract class MetaSubjectIdentifierType implements MetaData {
	private static final long serialVersionUID = 8046182964089190022L;

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}
	
	public abstract void applyConfig(SubjectIdentifierTypeDefinition subjectIdentifierType);
	public abstract SubjectIdentifierTypeDefinition currentConfig();
	public abstract SubjectIdentifierTypeRuntime createRuntime();
	
	public static abstract class SubjectIdentifierTypeRuntime {
		public abstract String subjectId(User user, OAuthClientRuntime client);
		public abstract User handleOnLoad(User user);

	}

	public static MetaSubjectIdentifierType createInstance(SubjectIdentifierTypeDefinition subjectIdentifierType) {
		if (subjectIdentifierType instanceof PairwiseSubjectIdentifierTypeDefinition) {
			return new MetaPairwiseSubjectIdentifierType();
		}
		if (subjectIdentifierType instanceof PublicSubjectIdentifierTypeDefinition) {
			return new MetaPublicSubjectIdentifierType();
		}
		return null;
	}

}
