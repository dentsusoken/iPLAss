/*
 * Copyright (C) 2016 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.staticresource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.io.upload.UploadFileItem;
import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.metadata.dto.staticresource.LocalizedStaticResourceInfo;

import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.SpacerItem;

public class StaticResourceMultiLanguageEditDialog extends MtpDialog {

	private static final String TITLE = "Multilingual StaticResource Setting";

	/** 言語選択 */
	private DynamicForm langSelectForm;
	private SelectItem langSelectItem;

	private StaticResourceUploadPane uploadPane;

	/** データ変更ハンドラ */
	private List<DataChangedHandler> handlers = new ArrayList<DataChangedHandler>();

	/** LocalizedDefinitionは共通のクラスがないのでそれぞれで定義 */
	private LocalizedStaticResourceInfo curStaticResourceDefinition;

	public StaticResourceMultiLanguageEditDialog(LinkedHashMap<String, String> enableLang) {

		setHeight(500);
		setTitle(TITLE);
		centerInPage();

		langSelectItem = new SelectItem("language", "Language");
		langSelectItem.setValueMap(enableLang);
		SmartGWTUtil.setRequired(langSelectItem);

		langSelectForm = new DynamicForm();
		langSelectForm.setWidth100();
		langSelectForm.setNumCols(3);
		langSelectForm.setColWidths(100, "*", "*");
		langSelectForm.setItems(langSelectItem, new SpacerItem());

		uploadPane = new StaticResourceUploadPane();

		container.addMember(langSelectForm);
		container.addMember(uploadPane);

		IButton save = new IButton("OK");
		save.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				boolean validate = langSelectForm.validate() & uploadPane.validate();
				if (!validate) {
					return;
				}

				LocalizedStaticResourceInfo definition = curStaticResourceDefinition;
				definition.setLocaleName(langSelectItem.getValueAsString());
				definition = uploadPane.getEditLocalizedStaticResourceDefinition(definition);
				UploadFileItem fileItem = uploadPane.getEditUploadFileItem();
				fireDataChanged(definition, fileItem);

				destroy();
			}
		});

		IButton cancel = new IButton("Cancel");
		cancel.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				destroy();
			}
		});
		footer.setMembers(save, cancel);

	}

	public void setDefinition(LocalizedStaticResourceInfo definition, UploadFileItem fileItem) {
		this.curStaticResourceDefinition = definition;

		if (definition != null) {
			langSelectItem.setValue(definition.getLocaleName());
		}

		uploadPane.setDefinition(definition, fileItem);
	}

	/**
	 * {@link DataChangedHandler} を追加します。
	 *
	 * @param handler {@link DataChangedHandler}
	 */
	public void addDataChangeHandler(DataChangedHandler handler) {
		handlers.add(0, handler);
	}

	/**
	 * {@link DataChangedHandler} を追加します。
	 *
	 * @param handler {@link DataChangedHandler}
	 */
	public void clearDataChangeHandlers() {
		handlers.clear();
	}

	public static class StaticResourceDataChangedEvent extends DataChangedEvent {
		private UploadFileItem fileItem;

		public UploadFileItem getUploadFileItem() {
			return fileItem;
		}

		public void setUploadFileItem(UploadFileItem fileItem) {
			this.fileItem = fileItem;
		}
	}

	/**
	 * データ変更通知処理
	 */
	private void fireDataChanged(LocalizedStaticResourceInfo definition, UploadFileItem fileItem) {
		StaticResourceDataChangedEvent event = new StaticResourceDataChangedEvent();
		event.setValueObject(definition);
		event.setUploadFileItem(fileItem);
		for (DataChangedHandler handler : handlers) {
			handler.onDataChanged(event);
		}
	}

}
