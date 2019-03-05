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
package org.iplass.mtp.impl.auth.oauth.subtypes;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.User;
import org.iplass.mtp.auth.oauth.definition.SubjectIdentifierTypeDefinition;
import org.iplass.mtp.auth.oauth.definition.subtypes.PairwiseSubjectIdentifierTypeDefinition;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.LoadOption;
import org.iplass.mtp.entity.UpdateOption;
import org.iplass.mtp.impl.auth.oauth.MetaOAuthClient.OAuthClientRuntime;
import org.iplass.mtp.impl.auth.oauth.MetaSubjectIdentifierType;
import org.iplass.mtp.impl.auth.oauth.OAuthAuthorizationService;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.StringUtil;

/**
 * client毎に異なるsubjectIdが付与されるMetaSubjectIdentifierType。
 * ハッシュベース。
 * 
 * @author K.Higuchi
 *
 */
public class MetaPairwiseSubjectIdentifierType extends MetaSubjectIdentifierType {
	private static final long serialVersionUID = 4993666973627956478L;

	private String subjectIdMappedUserProperty;

	public String getSubjectIdMappedUserProperty() {
		return subjectIdMappedUserProperty;
	}

	public void setSubjectIdMappedUserProperty(String subjectIdMappedUserProperty) {
		this.subjectIdMappedUserProperty = subjectIdMappedUserProperty;
	}
	
	@Override
	public SubjectIdentifierTypeRuntime createRuntime() {
		return new PairwiseSubjectIdentifierTypeRuntime();
	}
	
	public class PairwiseSubjectIdentifierTypeRuntime extends SubjectIdentifierTypeRuntime {
		private OAuthAuthorizationService oauthAuthService = ServiceRegistry.getRegistry().getService(OAuthAuthorizationService.class);
		public PairwiseSubjectIdentifierTypeRuntime() {
			if (oauthAuthService.getSubjectIdHashAlgorithm() == null || oauthAuthService.getSubjectIdHashSalt() == null) {
				throw new IllegalStateException("no hashing configration defined. OAuthAuthorizationService's subjectIdHashAlgorithm and subjectIdHashSalt must specify.");
			}
		}

		@Override
		public String subjectId(User user, OAuthClientRuntime client) {
			
			Object value = user.getValue(subjectIdMappedUserProperty);
			if (value == null) {
				return null;
			}
			String ret = value.toString();
			try {
				MessageDigest md = MessageDigest.getInstance(oauthAuthService.getSubjectIdHashAlgorithm());
				String msg = ret + "-" + client.sectorIdentifier() + "-" + ExecuteContext.getCurrentContext().getClientTenantId() + "-" + oauthAuthService.getSubjectIdHashSalt();
				byte[] bytes = md.digest(msg.getBytes("UTF-8"));
				ret = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
			} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
			return ret;
		}

		@Override
		public User handleOnLoad(User user) {
			Object value = user.getValue(subjectIdMappedUserProperty);
			if (value == null) {
				//maybe another session is update...
				EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);
				User u = (User) em.load(user.getOid(), User.DEFINITION_NAME, new LoadOption(false, false));
				value = u.getValue(subjectIdMappedUserProperty);
				if (value == null) {
					value = "s" + StringUtil.randomToken();//TODO configurable?
					u.setValue(subjectIdMappedUserProperty, value);
					em.update(u, new UpdateOption(true).add(subjectIdMappedUserProperty));
				}
				user.setValue(subjectIdMappedUserProperty, value);
			}
			return user;
		}
		
	}

	@Override
	public void applyConfig(SubjectIdentifierTypeDefinition subjectIdentifierType) {
		subjectIdMappedUserProperty = ((PairwiseSubjectIdentifierTypeDefinition) subjectIdentifierType).getSubjectIdMappedUserProperty();
	}

	@Override
	public SubjectIdentifierTypeDefinition currentConfig() {
		PairwiseSubjectIdentifierTypeDefinition def = new PairwiseSubjectIdentifierTypeDefinition();
		def.setSubjectIdMappedUserProperty(subjectIdMappedUserProperty);
		return def;
	}
}
