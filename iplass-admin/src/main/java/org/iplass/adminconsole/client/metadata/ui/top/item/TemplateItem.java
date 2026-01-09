/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataSelectItem;
import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.view.top.parts.TemplateParts;
import org.iplass.mtp.web.template.definition.TemplateDefinition;

import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.IntegerItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;

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
	}

	@Override
	public TemplateParts getParts() {
		return parts;
	}

	@Override
	protected void onOpen() {
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

	private class TemplateItemSettingDialog extends MtpDialog {

		private List<DataChangedHandler> handlers = new ArrayList<DataChangedHandler>();

		private IntegerItem maxHeightField;
		/**
		 * コンストラクタ
		 */
		public TemplateItemSettingDialog(String path) {

			setTitle("Template");
			setHeight(180);
			centerInPage();

			final DynamicForm form = new MtpForm();
			form.setAutoFocus(true);

			final SelectItem templateField = new MetaDataSelectItem(TemplateDefinition.class, "Template");
			SmartGWTUtil.setRequired(templateField);
			templateField.setValue(path);

			maxHeightField = new IntegerItem("maxHeight", "Max Height");
			maxHeightField.setWidth("100%");
			if (parts.getMaxHeight() != null && parts.getMaxHeight() > 0) {
				maxHeightField.setValue(parts.getMaxHeight());
			}
			SmartGWTUtil.addHoverToFormItem(maxHeightField,
					AdminClientMessageUtil.getString("ui_metadata_top_item_TopViewContentParts_maxHeightDescriptionKey"));

			form.setItems(templateField, maxHeightField);

			container.addMember(form);

			IButton save = new IButton("OK");
			save.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if (form.validate()){
						parts.setTemplatePath(SmartGWTUtil.getStringValue(templateField));
						parts.setMaxHeight(maxHeightField.getValueAsInteger());
						fireDataChanged();
						destroy();
					}
				}
			});

			IButton cancel = new IButton("Cancel");
			cancel.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					destroy();
				}
			});

			footer.setMembers(save, cancel);
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
