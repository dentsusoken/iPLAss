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

package org.iplass.adminconsole.client.metadata.ui.top.item;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.data.MetaDataNameDS;
import org.iplass.mtp.view.top.parts.TemplateParts;
import org.iplass.mtp.web.template.definition.TemplateDefinition;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.HeaderControls;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.HeaderControl;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.layout.HLayout;

/**
 *
 * @author lis3wg
 */
public class TemplateItem extends PartsItem {

	private TemplateParts parts;

	/**
	 * コンストラクタ
	 */
	public TemplateItem(TemplateParts parts) {
		this.parts = parts;
		setTitle("Template(" + parts.getTemplatePath() + ")");
		setBackgroundColor("#F0F0F0");

		setHeaderControls(HeaderControls.HEADER_LABEL, new HeaderControl(HeaderControl.SETTINGS, new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				TemplateItemSettingDialog dialog = new TemplateItemSettingDialog(
						TemplateItem.this.parts.getTemplatePath());
				dialog.addDataChangedHandler(new DataChangedHandler() {

					@Override
					public void onDataChanged(DataChangedEvent event) {
						setTitle("Template(" + parts.getTemplatePath() + ")");
					}
				});
				dialog.show();
			}
		}), HeaderControls.CLOSE_BUTTON);
	}

	@Override
	public TemplateParts getParts() {
		return parts;
	}

	@Override
	public void doDropAction(final DropActionCallback callback) {
		TemplateItemSettingDialog dialog = new TemplateItemSettingDialog(
				TemplateItem.this.parts.getTemplatePath());
		dialog.addDataChangedHandler(new DataChangedHandler() {

			@Override
			public void onDataChanged(DataChangedEvent event) {
				setTitle("Template(" + parts.getTemplatePath() + ")");
				callback.handle();
			}
		});
		dialog.show();
	}

	private class TemplateItemSettingDialog extends AbstractWindow {

		private List<DataChangedHandler> handlers = new ArrayList<DataChangedHandler>();

		/**
		 * コンストラクタ
		 */
		public TemplateItemSettingDialog(String path) {
			setTitle("Template");
			setHeight(130);
			setWidth(430);
			setMargin(10);
			setMembersMargin(10);

			setShowMinimizeButton(false);
			setIsModal(true);
			setShowModalMask(true);
			centerInPage();

			final DynamicForm form = new DynamicForm();
			form.setAlign(Alignment.CENTER);
			form.setAutoFocus(true);

			final SelectItem templateField = new SelectItem("template", "Template");
			templateField.setWidth(250);
			MetaDataNameDS.setDataSource(templateField, TemplateDefinition.class);
			SmartGWTUtil.setRequired(templateField);
			templateField.setValue(path);

			form.setItems(templateField);

			HLayout footer = new HLayout(5);
			footer.setMargin(10);
			footer.setWidth100();
			footer.setAlign(VerticalAlignment.CENTER);

			IButton save = new IButton("OK");
			save.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if (form.validate()){
						parts.setTemplatePath(SmartGWTUtil.getStringValue(templateField));
						fireDataChanged();
						destroy();
					}
				}
			});

			IButton cancel = new IButton("Cancel");
			cancel.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					destroy();
				}
			});

			footer.setMembers(save, cancel);

			addItem(form);
			addItem(footer);
		}

		public void addDataChangedHandler(DataChangedHandler handler) {
			handlers.add(handler);
		}

		private void fireDataChanged() {
			DataChangedEvent event = new DataChangedEvent();
			for (DataChangedHandler handler : handlers) {
				handler.onDataChanged(event);
			}
		}
	}
}
