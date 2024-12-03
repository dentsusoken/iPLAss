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

package org.iplass.adminconsole.client.metadata.ui.template;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.io.upload.UploadFileItem;
import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.web.template.definition.LocalizedBinaryDefinition;
import org.iplass.mtp.web.template.report.definition.LocalizedReportDefinition;

import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.layout.VLayout;

public class TemplateMultiLanguageEditDialog extends MtpDialog {

	private static final String TITLE = "Multilingual Template Setting";

	/** 言語選択 */
	protected DynamicForm langSelectForm;
	protected SelectItem langSelectItem;

	/** タイプ別属性 */
	protected VLayout templateTypeMainPane;
	protected TemplateTypeEditPane typeEditPane;

	/** データ変更ハンドラ */
	protected List<DataChangedHandler> handlers = new ArrayList<DataChangedHandler>();

	/** LocalizedDefinitionは共通のクラスがないのでそれぞれで定義 */
	protected TemplateType type;
	protected LocalizedStringDefinition curStringDefinition;
	protected LocalizedBinaryDefinition curBinaryDefinition;
	protected LocalizedReportDefinition curReportDefinition;

	public TemplateMultiLanguageEditDialog(LinkedHashMap<String, String> enableLang) {

		setTitle(TITLE);
		centerInPage();

		langSelectForm = new MtpForm();

		langSelectItem = new SelectItem("language", "Language");
		langSelectItem.setValueMap(enableLang);
		SmartGWTUtil.setRequired(langSelectItem);

		langSelectForm.setItems(langSelectItem);

		//個別は枠のみ追加(setDefinitionで決定)
		templateTypeMainPane = new VLayout();

		container.addMember(langSelectForm);
		container.addMember(templateTypeMainPane);

		IButton save = new IButton("OK");
		save.addClickHandler(createOkClickHandler());

		IButton cancel = new IButton("Cancel");
		cancel.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				destroy();
			}
		});

		footer.setMembers(save, cancel);
	}

	/**
	 * Okボタン用のClickHandlerを生成する。
	 * @return ClickHandler
	 */
	protected ClickHandler createOkClickHandler() {
		return new ClickHandler() {
			public void onClick(ClickEvent event) {

				boolean validate = langSelectForm.validate() & typeEditPane.validate();
				if (!validate) {
					return;
				}

				switch (type) {
				case HTML:
				case JSP:
				case GROOVY:
					LocalizedStringDefinition stringDefinition = curStringDefinition;
					stringDefinition.setLocaleName(langSelectItem.getValueAsString());
					((HasEditLocalizedStringDefinition) typeEditPane).getEditLocalizedStringDefinition(stringDefinition);
					fireDataChanged(stringDefinition);
					break;
				case BINARY:
					LocalizedBinaryDefinition binaryDefinition = curBinaryDefinition;
					binaryDefinition.setLocaleName(langSelectItem.getValueAsString());
					((HasEditLocalizedBinaryDefinition) typeEditPane).getEditLocalizedBinaryDefinition(binaryDefinition);
					UploadFileItem binaryFile = ((HasEditLocalizedBinaryDefinition) typeEditPane).getEditUploadFileItem();
					fireDataChanged(binaryDefinition, binaryFile);
					break;
				case REPORT:
					LocalizedReportDefinition reportDefinition = curReportDefinition;
					reportDefinition.setLocaleName(langSelectItem.getValueAsString());
					((HasEditLocalizedReportDefinition) typeEditPane).getEditLocalizedReportDefinition(reportDefinition);
					UploadFileItem reportFile = ((HasEditLocalizedReportDefinition) typeEditPane).getEditUploadFileItem();
					fireDataChanged(reportDefinition, reportFile);
					break;
				default:
					break;
				}
				destroy();
			}
		};
	}

	public void setDefinition(TemplateType type, LocalizedStringDefinition definition) {
		this.type = type;
		this.curStringDefinition = definition;

		setTitle(TITLE + "(" + type.displayName() + ")");

		typeEditPane = TemplateType.typeOfEditPane(type);

		//サイズ調整
		fitSize();

		templateTypeMainPane.addMember(typeEditPane);
		((HasEditLocalizedStringDefinition) typeEditPane).setLocalizedStringDefinition(definition);

		if (definition != null) {
			langSelectItem.setValue(definition.getLocaleName());
		}
	}

	public void setDefinition(TemplateType type, LocalizedBinaryDefinition definition, String templateDefName, UploadFileItem fileItem) {
		this.type = type;
		this.curBinaryDefinition = definition;

		setTitle(TITLE + "(" + type.displayName() + ")");

		typeEditPane = TemplateType.typeOfEditPane(type);

		//サイズ調整
		fitSize();

		templateTypeMainPane.addMember(typeEditPane);
		((HasEditLocalizedBinaryDefinition) typeEditPane).setLocalizedBinaryDefinition(definition, templateDefName, fileItem);

		if (definition != null) {
			langSelectItem.setValue(definition.getLocaleName());
		}
	}

	public void setDefinition(TemplateType type, LocalizedReportDefinition definition, String templateDefName, UploadFileItem fileItem) {
		this.type = type;
		this.curReportDefinition = definition;

		setTitle(TITLE + "(" + type.displayName() + ")");

		typeEditPane = TemplateType.typeOfEditPane(type);

		//サイズ調整
		fitSize();

		templateTypeMainPane.addMember(typeEditPane);
		((HasEditLocalizedReportDefinition) typeEditPane).setLocalizedReportDefinition(definition, templateDefName, fileItem);

		if (definition != null) {
			langSelectItem.setValue(definition.getLocaleName());
		}
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

	private void fitSize() {

		if (typeEditPane.getLocaleDialogHeight() == 0) {
			setHeight("80%");
		} else {
			setHeight(typeEditPane.getLocaleDialogHeight());
		}

		centerInPage();
	}

	/**
	 * データ変更通知処理
	 */
	protected void fireDataChanged(LocalizedStringDefinition definition) {
		TemplateDataChangedEvent event = new TemplateDataChangedEvent();
		event.setValueObject(definition);
		for (DataChangedHandler handler : handlers) {
			handler.onDataChanged(event);
		}
	}

	/**
	 * データ変更通知処理
	 */
	protected void fireDataChanged(LocalizedBinaryDefinition definition, UploadFileItem fileItem) {
		TemplateDataChangedEvent event = new TemplateDataChangedEvent();
		event.setValueObject(definition);
		event.setUploadFileItem(fileItem);
		for (DataChangedHandler handler : handlers) {
			handler.onDataChanged(event);
		}
	}

	/**
	 * データ変更通知処理
	 */
	protected void fireDataChanged(LocalizedReportDefinition definition, UploadFileItem fileItem) {
		TemplateDataChangedEvent event = new TemplateDataChangedEvent();
		event.setValueObject(definition);
		event.setUploadFileItem(fileItem);
		for (DataChangedHandler handler : handlers) {
			handler.onDataChanged(event);
		}
	}

	public static class TemplateDataChangedEvent extends DataChangedEvent {

		private UploadFileItem fileItem;

		public UploadFileItem getUploadFileItem() {
			return fileItem;
		}

		public void setUploadFileItem(UploadFileItem fileItem) {
			this.fileItem = fileItem;
		}

	}

}
