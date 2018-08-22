/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.auth.authenticate;

import java.util.Map;

import org.iplass.mtp.auth.User;
import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.impl.auth.UserContext;

public final class AnonymousUserContext implements UserContext {
	//TODO パッケージ移動->auth直下

	private static final long serialVersionUID = 6503268584220120432L;

	private static final User anonymousUser;
	private static final AccountHandle anonymousAccount = new AccountHandle() {
		private static final long serialVersionUID = 6431803480686097702L;

		@Override
		public boolean isExpired() {
			return false;
		}

		@Override
		public boolean isAccountLocked() {
			return false;
		}

		@Override
		public boolean isInitialLogin() {
			return false;
		}

		@Override
		public Credential getCredential() {
			return new Credential() {
				@Override
				public String getId() {
					return "Anonymous";
				}
				@Override
				public Object getAuthenticationFactor(String name) {
					return null;
				}

				@Override
				public void setAuthenticationFactor(String name, Object value) {
				}
			};
		}

		@Override
		public Map<String, Object> getAttributeMap() {
			return null;
		}

		@Override
		public void setAuthenticationProviderIndex(int authenticationProviderIndex) {
		}

		@Override
		public int getAuthenticationProviderIndex() {
			return 0;
		}

		@Override
		public String getUnmodifiableUniqueKey() {
			return null;
		}

	};

	static {
		anonymousUser = new User("Anonymous", "Anonymous", true);
	}


//	@Override
//	public User getUser() {
//		User user = new User("Anonymous", "Anonymous", true);
//		user.setName("Anonymous");
//		return user;
//	}

	@Override
	public User getUser() {
		return anonymousUser;
	}

	@Override
	public AccountHandle getAccount() {
		return anonymousAccount;
	}

	@Override
	public Object getAttribute(String name) {
		return anonymousUser.getValue(name);
	}

	@Override
	public String[] getGroupCode() {
		return null;
	}

	@Override
	public String getIdForLog() {
		return "Anonymous";
	}

	@Override
	public void resetUserEntity(User userEntity) {
	}

}
