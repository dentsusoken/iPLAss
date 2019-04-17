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

package org.iplass.adminconsole.client.metadata.ui.top.item;

import java.util.HashMap;
import java.util.Map;

import org.iplass.adminconsole.client.base.event.MTPEvent;
import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.top.PartsOperationHandler;
import org.iplass.mtp.view.top.parts.FulltextSearchViewParts;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.HeaderControls;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HeaderControl;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.grid.ListGrid;

/**
 *
 * @author lis3wg
 */
public class FulltextSearchViewItem extends PartsItem {

	private PartsOperationHandler controler;
	private FulltextSearchViewParts parts;
	private FulltextSearchTargetEntityPane listPane;
	private DynamicForm form;
	private boolean isInitDrop;

	/**
	 * コンストラクタ
	 */
	public FulltextSearchViewItem(FulltextSearchViewParts parts, PartsOperationHandler controler) {
		this(parts, controler, false);
	}

	public FulltextSearchViewItem(FulltextSearchViewParts parts, PartsOperationHandler controler, boolean isInitDrop) {
		this.parts = parts;
		this.controler = controler;
		this.isInitDrop = isInitDrop;

		setTitle("Fulltext Search");
		setBackgroundColor("#A0A0A0");
		setHeaderControls(HeaderControls.HEADER_LABEL, new HeaderControl(HeaderControl.SETTINGS, new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				FulltextSearchViewItemSettingDialog dialog = new FulltextSearchViewItemSettingDialog();
				dialog.show();
			}
		}), HeaderControls.CLOSE_BUTTON);
	}

	@Override
	public FulltextSearchViewParts getParts() {
		return parts;
	}

	@Override
	protected boolean onPreDestroy() {
		MTPEvent e = new MTPEvent();
		e.setValue("key", dropAreaType + "_" + FulltextSearchViewParts.class.getName() + "_");
		controler.remove(e);
		return true;
	}

	private class FulltextSearchViewItemSettingDialog extends MtpDialog {

		/**
		 * コンストラクタ
		 */
		public FulltextSearchViewItemSettingDialog() {

			setTitle("Fulltext Search");
			setHeight(400);
			centerInPage();

			form = new MtpForm();

			CheckboxItem isDispSearchWindow = new CheckboxItem();
			isDispSearchWindow.setTitle("Display search textbox");
			isDispSearchWindow.setName("dispSearchWindow");
			if (isInitDrop) {
				isDispSearchWindow.setValue(true);
			} else {
				isDispSearchWindow.setValue(parts.isDispSearchWindow());
			}
			form.setItems(isDispSearchWindow);

			container.addMember(form);

			listPane = new FulltextSearchTargetEntityPane(parts, isInitDrop);
			container.addMember(listPane);

			IButton save = new IButton("OK");
			save.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					//入力情報をパーツに
					Map<String, String> viewNamsMap = new HashMap<String, String>();
					Map<String, Boolean> dispEntityMap = new HashMap<String, Boolean>();

					Canvas[] canvas = listPane.getChildren();
					ListGrid grid = (ListGrid) canvas[0];
					Record[] records = grid.getRecords();

					for (Record record : records) {

						String viewName = record.getAttribute("entityView");
						String entityName = record.getAttribute("name");
						boolean isDispEntity = record.getAttributeAsBoolean("isDispEntity");

						if (viewName == null || viewName.equals("(default)")) {
							viewName = "";
						}
						viewNamsMap.put(entityName, viewName);
						dispEntityMap.put(entityName, isDispEntity);
					}
					parts.setViewNames(viewNamsMap);
					parts.setDispEntities(dispEntityMap);

					FormItem dispSearchWindow = form.getField("dispSearchWindow");
					parts.setDispSearchWindow(SmartGWTUtil.getBooleanValue(dispSearchWindow));

					destroy();
				}
			});

			IButton cancel = new IButton("Cancel");
			cancel.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					destroy();
				}
			});

			footer.setMembers(save, cancel);
		}
	}

}
