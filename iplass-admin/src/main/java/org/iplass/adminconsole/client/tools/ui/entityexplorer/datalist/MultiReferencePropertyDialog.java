/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.tools.ui.entityexplorer.datalist;

import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.tools.data.entityexplorer.MultiReferencePropertyDS;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;

import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.events.DataArrivedEvent;
import com.smartgwt.client.widgets.grid.events.DataArrivedHandler;

public class MultiReferencePropertyDialog extends MtpDialog {

	public MultiReferencePropertyDialog(final EntityDefinition definition, final Entity entity, String propertyName) {

		setTitle(SafeHtmlUtils.htmlEscape(definition.getName() + " - " + entity.getOid() + "(" + entity.getVersion() + ") - " + propertyName));
		setHeight(600);

		final Label oidLabel = new Label();
		oidLabel.setHeight(18);
		oidLabel.setMargin(5);
		oidLabel.setContents(SafeHtmlUtils.htmlEscape("Target Entity：" + entity.getName() + "(oid=" + entity.getOid() + ",ver=" + entity.getVersion() + ")"));

		ReferenceProperty property = (ReferenceProperty)definition.getProperty(propertyName);

		final Label propertyNameLabel = new Label();
		propertyNameLabel.setHeight(18);
		propertyNameLabel.setMargin(5);
		propertyNameLabel.setContents("Property：" + property.getDisplayName() + "(" + property.getName() + ")");

		final Label multiplicityLabel = new Label();
		multiplicityLabel.setHeight(18);
		multiplicityLabel.setMargin(5);
		String multiStr = (property.getMultiplicity() > 0 ? "" + property.getMultiplicity() : "*");
		multiplicityLabel.setContents("Multiple：" + multiStr);

		final Label referenceNameLabel = new Label();
		referenceNameLabel.setHeight(18);
		referenceNameLabel.setMargin(5);
		referenceNameLabel.setContents("Reference Entity：" + property.getObjectDefinitionName()
				+ (property.getMappedBy() != null ? ", Mapped By：" + property.getMappedBy() : "")
				);
		final Label referenceTypeLabel = new Label();
		referenceTypeLabel.setHeight(18);
		referenceTypeLabel.setMargin(5);
		referenceTypeLabel.setContents("Reference Type：" + property.getReferenceType()
				+ ", Version Control Type：" + property.getVersionControlType()
				);


		final Label countLabel = new Label();
		countLabel.setHeight(30);
		countLabel.setMargin(5);
		countLabel.setAlign(Alignment.RIGHT);

		final ListGrid grid = new ListGrid();
		grid.setMargin(5);
		grid.setHeight100();
		grid.setWidth100();
		grid.setShowAllColumns(true);
		grid.setShowAllRecords(true);
		grid.setCanResizeFields(true);
		grid.setLeaveScrollbarGap(false);

		grid.setCanDragSelectText(true);	//セルの値をドラッグで選択可能（コピー用）にする

		grid.addDataArrivedHandler(new DataArrivedHandler() {

			@Override
			public void onDataArrived(DataArrivedEvent event) {
				countLabel.setContents("Total Count：" + grid.getRecords().length);
			}
		});

		grid.setDataSource(MultiReferencePropertyDS.getInstance(definition.getName(), entity.getOid(), entity.getVersion(), propertyName));
		grid.setAutoFetchData(true);

		container.addMember(oidLabel);
		container.addMember(propertyNameLabel);
		container.addMember(multiplicityLabel);
		container.addMember(referenceNameLabel);
		container.addMember(referenceTypeLabel);
		container.addMember(countLabel);
		container.addMember(grid);

		IButton cancelBtn = new IButton();
		cancelBtn.setWidth(100);
		cancelBtn.setTitle("Close");
		cancelBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				destroy();
			}
		});
		footer.setMembers(cancelBtn);

		centerInPage();
	}

}
