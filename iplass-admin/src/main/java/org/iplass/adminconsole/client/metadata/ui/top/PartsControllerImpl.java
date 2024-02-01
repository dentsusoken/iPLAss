/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.top;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.event.MTPEvent;
import org.iplass.adminconsole.client.metadata.ui.top.item.ApplicationMaintenanceItem;
import org.iplass.adminconsole.client.metadata.ui.top.item.CalendarItem;
import org.iplass.adminconsole.client.metadata.ui.top.item.CsvDownloadSettingsItem;
import org.iplass.adminconsole.client.metadata.ui.top.item.FulltextSearchViewItem;
import org.iplass.adminconsole.client.metadata.ui.top.item.InformationItem;
import org.iplass.adminconsole.client.metadata.ui.top.item.LastLoginItem;
import org.iplass.adminconsole.client.metadata.ui.top.item.PartsItem;
import org.iplass.adminconsole.client.metadata.ui.top.item.PreviewDateItem;
import org.iplass.adminconsole.client.metadata.ui.top.item.ScriptItem;
import org.iplass.adminconsole.client.metadata.ui.top.item.SearchResultListItem;
import org.iplass.adminconsole.client.metadata.ui.top.item.SeparatorItem;
import org.iplass.adminconsole.client.metadata.ui.top.item.TemplateItem;
import org.iplass.adminconsole.client.metadata.ui.top.item.TreeViewItem;
import org.iplass.adminconsole.client.metadata.ui.top.item.UserMaintenanceItem;
import org.iplass.adminconsole.client.metadata.ui.top.node.CalendarNodeManager;
import org.iplass.adminconsole.client.metadata.ui.top.node.CustomNodeManager;
import org.iplass.adminconsole.client.metadata.ui.top.node.EntityNodeManager;
import org.iplass.adminconsole.client.metadata.ui.top.node.ItemNodeManager;
import org.iplass.adminconsole.client.metadata.ui.top.node.SingleNodeManager;
import org.iplass.adminconsole.client.metadata.ui.top.node.ToolbarNodeManager;
import org.iplass.adminconsole.client.metadata.ui.top.node.TopViewNode;
import org.iplass.adminconsole.client.metadata.ui.top.node.TreeViewNodeManager;
import org.iplass.mtp.view.top.parts.ApplicationMaintenanceParts;
import org.iplass.mtp.view.top.parts.CalendarParts;
import org.iplass.mtp.view.top.parts.CsvDownloadSettingsParts;
import org.iplass.mtp.view.top.parts.EntityListParts;
import org.iplass.mtp.view.top.parts.FulltextSearchViewParts;
import org.iplass.mtp.view.top.parts.InformationParts;
import org.iplass.mtp.view.top.parts.LastLoginParts;
import org.iplass.mtp.view.top.parts.PreviewDateParts;
import org.iplass.mtp.view.top.parts.ScriptParts;
import org.iplass.mtp.view.top.parts.SeparatorParts;
import org.iplass.mtp.view.top.parts.TemplateParts;
import org.iplass.mtp.view.top.parts.TopViewParts;
import org.iplass.mtp.view.top.parts.TreeViewParts;
import org.iplass.mtp.view.top.parts.UserMaintenanceParts;

public class PartsControllerImpl implements PartsController {

	@Override
	public PartsItem createWindow(TopViewParts parts, PartsOperationHandler controler, String dropAreaType) {

		PartsItem item = null;
		boolean isUnique = false;
		String defName = "";
		if (parts instanceof InformationParts) {
			item = new InformationItem((InformationParts) parts, controler);
			isUnique = true;
		} else if (parts instanceof CalendarParts) {
			item = new CalendarItem((CalendarParts) parts, controler);
			defName = ((CalendarParts) parts).getCalendarName();
			isUnique = true;
		} else if (parts instanceof TreeViewParts) {
			item = new TreeViewItem((TreeViewParts) parts, controler);
			defName = ((TreeViewParts) parts).getTreeViewName();
			isUnique = true;
		} else if (parts instanceof EntityListParts) {
			item = new SearchResultListItem((EntityListParts) parts);
		} else if (parts instanceof ScriptParts) {
			item = new ScriptItem((ScriptParts) parts);
		} else if (parts instanceof SeparatorParts) {
			item = new SeparatorItem((SeparatorParts) parts, controler);
		} else if (parts instanceof LastLoginParts) {
			item = new LastLoginItem((LastLoginParts) parts, controler);
			isUnique = true;
		} else if (parts instanceof TemplateParts) {
			item = new TemplateItem((TemplateParts) parts);
		} else if (parts instanceof UserMaintenanceParts) {
			item = new UserMaintenanceItem((UserMaintenanceParts) parts, controler);
			isUnique = true;
		} else if (parts instanceof FulltextSearchViewParts) {
			item = new FulltextSearchViewItem((FulltextSearchViewParts) parts, controler);
			isUnique = true;
		} else if (parts instanceof CsvDownloadSettingsParts) {
			item = new CsvDownloadSettingsItem((CsvDownloadSettingsParts) parts, controler);
			isUnique = true;
		} else if (parts instanceof ApplicationMaintenanceParts) {
			item = new ApplicationMaintenanceItem((ApplicationMaintenanceParts)parts, controler);
			isUnique = true;
		} else if (parts instanceof PreviewDateParts) {
			item = new PreviewDateItem((PreviewDateParts)parts, controler);
			isUnique = true;
		}

		if (item != null) item.setDropAreaType(dropAreaType);

		if (isUnique) {
			MTPEvent e = new MTPEvent();
			String key = dropAreaType + "_" + parts.getClass().getName() + "_" + defName;
			e.setValue("key", key);
			controler.add(e);
		}
		return item;
	}

	@Override
	public PartsItem createWindow(TopViewNode node, PartsOperationHandler controler, String dropAreaType) {

		PartsItem item = null;
		if (InformationParts.class.getName().equals(node.getType())) {
			item = new InformationItem(new InformationParts(), controler);
		} else if (CalendarParts.class.getName().equals(node.getType())) {
			CalendarParts parts = new CalendarParts();
			parts.setCalendarName(node.getDefName());
			item = new CalendarItem(parts, controler);
		} else if (TreeViewParts.class.getName().equals(node.getType())) {
			TreeViewParts parts = new TreeViewParts();
			parts.setTreeViewName(node.getDefName());
			item = new TreeViewItem(parts, controler);
		} else if (EntityListParts.class.getName().equals(node.getType())) {
			item = new SearchResultListItem(new EntityListParts());
		} else if (ScriptParts.class.getName().equals(node.getType())) {
			item = new ScriptItem(new ScriptParts());
		} else if (SeparatorParts.class.getName().equals(node.getType())) {
			item = new SeparatorItem(new SeparatorParts(), controler);
		} else if (LastLoginParts.class.getName().equals(node.getType())) {
			LastLoginParts parts = new LastLoginParts();
			item = new LastLoginItem(parts, controler);
		} else if (TemplateParts.class.getName().equals(node.getType())) {
			item = new TemplateItem(new TemplateParts());
		} else if (UserMaintenanceParts.class.getName().equals(node.getType())) {
			item = new UserMaintenanceItem(new UserMaintenanceParts(), controler);
		} else if (FulltextSearchViewParts.class.getName().equals(node.getType())) {
			FulltextSearchViewParts parts = new FulltextSearchViewParts();
			parts.setDispSearchWindow(true);
			item = new FulltextSearchViewItem(parts, controler, true);
		} else if (CsvDownloadSettingsParts.class.getName().equals(node.getType())) {
			item = new CsvDownloadSettingsItem(new CsvDownloadSettingsParts(), controler);
		} else if (ApplicationMaintenanceParts.class.getName().equals(node.getType())) {
			item = new ApplicationMaintenanceItem(new ApplicationMaintenanceParts(), controler);
		} else if (PreviewDateParts.class.getName().equals(node.getType())) {
			PreviewDateParts parts = new PreviewDateParts();
			parts.setUsePreviewDate(true);
			item = new PreviewDateItem(parts, controler);
		}

		if (item != null) item.setDropAreaType(dropAreaType);

		return item;
	}

	@Override
	public List<ItemNodeManager> partsNodeList() {

		List<ItemNodeManager> list = new ArrayList<>();

		list.add(new SingleNodeManager());
		list.add(new CustomNodeManager());//ffffff
		list.add(new ToolbarNodeManager());//000000

		list.add(new EntityNodeManager());//0000ff
		list.add(new CalendarNodeManager());//ff00ff
		list.add(new TreeViewNodeManager());//00ffff

		return list;
	}

}
