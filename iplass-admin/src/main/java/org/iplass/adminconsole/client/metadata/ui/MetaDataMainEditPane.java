/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;

public abstract class MetaDataMainEditPane extends VLayout {

	protected MetaDataItemMenuTreeNode targetNode;
	protected DefaultMetaDataPlugin plugin;
	protected String defName;

	protected SectionStack mainStack;

	public MetaDataMainEditPane() {
		mainStack = createMainStack();
	}

	public MetaDataMainEditPane(MetaDataItemMenuTreeNode targetNode, DefaultMetaDataPlugin plugin) {
		mainStack = createMainStack();

		setTarget(targetNode, plugin);
	}

	public void setTarget(MetaDataItemMenuTreeNode targetNode, DefaultMetaDataPlugin plugin) {
		this.targetNode = targetNode;
		this.plugin = plugin;
		this.defName = targetNode.getDefName();
	}

	private SectionStack createMainStack() {
		SectionStack mainStack = new SectionStack();
		mainStack.setWidth100();
		mainStack.setVisibilityMode(VisibilityMode.MULTIPLE);
		mainStack.setAnimateSections(false);
		return mainStack;
	}

	protected void resetMainStack() {
		if (mainStack != null) {
			mainStack.destroy();
		}
		mainStack = createMainStack();
	}

	protected MetaDataSectionStackSection createSection(String name, Canvas... contents) {
		return createSection(name, true, contents);
	}

	protected MetaDataSectionStackSection createSection(String name, boolean isExpand, Canvas... contents) {
		MetaDataSectionStackSection section = new MetaDataSectionStackSection(name, contents);
		section.setExpanded(isExpand);
		return section;
	}

	protected void setMainSections(SectionStackSection... sections) {
		mainStack.setSections(sections);
	}

	public static class MetaDataSectionStackSection extends SectionStackSection {

		private VLayout layout;

		public MetaDataSectionStackSection(String name, Canvas... contents) {
			super(name);

			layout = new VLayout();
			layout.setOverflow(Overflow.AUTO);
			layout.setMembers(contents);

			addItem(layout);
		}

		public VLayout getLayout() {
			return layout;
		}

	}
}
