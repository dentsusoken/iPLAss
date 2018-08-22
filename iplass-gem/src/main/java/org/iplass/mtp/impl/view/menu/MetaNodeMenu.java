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

package org.iplass.mtp.impl.view.menu;

import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.metadata.MetaDataRuntime;
import org.iplass.mtp.view.menu.MenuItem;
import org.iplass.mtp.view.menu.NodeMenuItem;

public final class MetaNodeMenu extends MetaMenu {

	private static final long serialVersionUID = -473240701624008090L;

	@Override
	public MetaDataRuntime createRuntime(MetaDataConfig metaDataConfig) {
		return new MetaNodeMenuHandler();
	}

	@Override
	public void applyConfig(MenuItem definition) {
		fillFrom(definition);
	}

	@Override
	public MenuItem currentConfig() {
		NodeMenuItem definition = new NodeMenuItem();
		fillTo(definition);
		return definition;
	}

	public class MetaNodeMenuHandler extends MetaMenuHandler {
		@Override
		public MetaNodeMenu getMetaData() {
			return MetaNodeMenu.this;
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MetaNodeMenu [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", displayName=");
		builder.append(displayName);
		builder.append(", description=");
		builder.append(description);
		builder.append(", icon=");
		builder.append(icon);
		builder.append("]");
		return builder.toString();
	}

}
