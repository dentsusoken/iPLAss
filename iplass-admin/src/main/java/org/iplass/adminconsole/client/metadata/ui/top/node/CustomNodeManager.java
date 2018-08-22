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

package org.iplass.adminconsole.client.metadata.ui.top.node;

import org.iplass.mtp.view.top.parts.ScriptParts;
import org.iplass.mtp.view.top.parts.SeparatorParts;
import org.iplass.mtp.view.top.parts.TemplateParts;

import com.smartgwt.client.widgets.tree.Tree;

/**
 *
 * @author lis3wg
 */
public class CustomNodeManager extends ItemNodeManager {

	@Override
	public String getName() {
		return "Custom Parts";
	}

	@Override
	public void loadChild(Tree tree, TopViewNode parent) {
		TopViewNode separator = new TopViewNode("Separator", SeparatorParts.class.getName(), "", true, false, false);
		TopViewNode script = new TopViewNode("Script", ScriptParts.class.getName(), "", true, true, false);
		TopViewNode template = new TopViewNode("Template", TemplateParts.class.getName(), "", true, true, false);
		tree.addList(new TopViewNode[]{script, template, separator}, parent);
	}

}
