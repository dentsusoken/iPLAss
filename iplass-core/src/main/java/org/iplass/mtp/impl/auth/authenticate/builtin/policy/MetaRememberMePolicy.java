/*
 * Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.auth.authenticate.builtin.policy;

import org.iplass.mtp.auth.policy.definition.RememberMePolicyDefinition;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;

public class MetaRememberMePolicy implements MetaData {
	private static final long serialVersionUID = 7382024040191366366L;

	private long lifetimeMinutes;//単位：分。0はRememberMe機能を利用しない。
	private boolean absoluteLifetime;
	
	public boolean isAbsoluteLifetime() {
		return absoluteLifetime;
	}
	public void setAbsoluteLifetime(boolean absoluteLifetime) {
		this.absoluteLifetime = absoluteLifetime;
	}
	public long getLifetimeMinutes() {
		return lifetimeMinutes;
	}
	public void setLifetimeMinutes(long lifetimeMinutes) {
		this.lifetimeMinutes = lifetimeMinutes;
	}
	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}
	public void applyConfig(RememberMePolicyDefinition def) {
		lifetimeMinutes = def.getLifetimeMinutes();
		absoluteLifetime = def.isAbsoluteLifetime();
	}
	public RememberMePolicyDefinition currentConfig() {
		RememberMePolicyDefinition def = new RememberMePolicyDefinition();
		def.setAbsoluteLifetime(absoluteLifetime);
		def.setLifetimeMinutes(lifetimeMinutes);
		return def;
	}
}
