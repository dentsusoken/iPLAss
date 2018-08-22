/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.tools.ui.pack;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.ui.widget.AnimationFullScreenCallback;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.tools.dto.pack.PackageCreateInfo;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Packageを作成するダイアログ
 */
public class PackageCreateDialog extends AbstractWindow {

	private static final int MIN_WIDTH = 800;
	private static final int MIN_HEIGHT = 600;

	private HTMLFlow description;
	private CardLayout cardLayout;

	private PackageListPane owner;

	//作成情報
	private PackageCreateInfo createInfo = new PackageCreateInfo();

	public static void showFullScreen(final PackageListPane owner) {
		SmartGWTUtil.showAnimationFullScreen(new AnimationFullScreenCallback() {
			@Override
			public void execute(boolean earlyFinish) {
              animateOutline.hide();
              PackageCreateDialog dialog = new PackageCreateDialog(owner, width, height);
              dialog.show();
			}
		});
	}

	/**
	 * コンストラクタ
	 */
	private PackageCreateDialog(PackageListPane owner, int width, int height) {
		this.owner = owner;

		setWidth(width);
		setMinWidth(MIN_WIDTH);
		setHeight(height);
		setMinHeight(MIN_HEIGHT);
		setTitle("Create Package");
		setCanDragResize(true);
		setShowMinimizeButton(false);
		setShowMaximizeButton(true);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();

		//------------------------
		//Header Layout
		//------------------------
		HLayout header = createControllerPane();

		//------------------------
		//Operation Pane
		//------------------------
		description = new HTMLFlow();
		description.setAutoHeight();
		description.setWidth100();
		description.setPadding(5);

		cardLayout = new CardLayout();

		//------------------------
		//Footer Layout
		//------------------------
		HLayout footer = createControllerPane();

		//------------------------
		//Main Layout
		//------------------------
		VLayout mainLayout = new VLayout();
		mainLayout.setWidth100();
		mainLayout.setHeight100();
		mainLayout.setMargin(10);

		mainLayout.addMember(header);
		mainLayout.addMember(SmartGWTUtil.separator());

		mainLayout.addMember(description);
		mainLayout.addMember(cardLayout);

		mainLayout.addMember(SmartGWTUtil.separator());
		mainLayout.addMember(footer);

		addItem(mainLayout);

		initializeData();
	}

	public void refreshGrid() {
		owner.refresh();
	}

	private HLayout createControllerPane() {

		List<Canvas> members = new ArrayList<Canvas>();

		CreateOperation[] operations = CreateOperation.values();
		for (int i = 0; i < operations.length; i++) {
			final CreateOperation operation = operations[i];
			IButton button = new IButton(operation.displayName());
			button.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					showOperationPane(operation);
				}
			});
			members.add(button);

			if (i < operations.length - 1) {
				Label lblDummy = new Label("＞");
				lblDummy.setWidth(20);
				lblDummy.setHeight(20);
				lblDummy.setAlign(Alignment.CENTER);
				lblDummy.setValign(VerticalAlignment.BOTTOM);
				members.add(lblDummy);
			}
		}

		members.add(new LayoutSpacer());

		IButton cancelButton = new IButton("Cancel");
		cancelButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				destroy();
			}
		});
		members.add(cancelButton);

		HLayout controller = new HLayout(5);
		controller.setMargin(5);
		controller.setHeight(20);
		controller.setWidth100();
		controller.setAlign(VerticalAlignment.CENTER);
		controller.setMembers(members.toArray(new Canvas[]{}));

		return controller;
	}

	private void initializeData() {

		for (CreateOperation operation : CreateOperation.values()) {
			if (cardLayout.getCard(operation.name()) == null) {
				cardLayout.addCard(operation.name(), operation.getOperationPane(this));
			}
		}

		showOperationPane(CreateOperation.valueOf((String)cardLayout.getFirstCardKey()));
	}

	private void showOperationPane(CreateOperation operation) {
		//現在のCardの情報を反映
		((CreateOperationPane)cardLayout.getCurrentCard()).applyTo(createInfo);

		//切り替えるCardに情報を渡す
		((CreateOperationPane)cardLayout.getCard(operation.name())).setCurrentCreateInfo(createInfo);

		description.setContents("<div>" + operation.displayName() + "</div>" + operation.description());
		cardLayout.showCard(operation.name());
	}

}
