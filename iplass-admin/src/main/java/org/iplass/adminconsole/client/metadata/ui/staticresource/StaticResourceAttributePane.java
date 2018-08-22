/*
 * Copyright (C) 2016 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.util.LinkedHashMap;
import java.util.List;

import org.iplass.adminconsole.client.base.io.upload.UploadFileItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.metadata.dto.staticresource.EntryPathType;
import org.iplass.adminconsole.shared.metadata.dto.staticresource.StaticResourceInfo;
import org.iplass.mtp.web.staticresource.definition.EntryPathTranslatorDefinition;
import org.iplass.mtp.web.staticresource.definition.MimeTypeMappingDefinition;

import com.google.gwt.user.client.ui.FileUpload;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * StaticResource属性編集パネル
 *
 * @author lis9cb
 *
 */
public class StaticResourceAttributePane extends VLayout {

	/** EntryPathの種類選択用Map */
	private static LinkedHashMap<String, String> entryPathTypeMap;
	static {
		entryPathTypeMap = new LinkedHashMap<String, String>();
		for (EntryPathType type : EntryPathType.values()) {
			entryPathTypeMap.put(type.name(), type.displayName());
		}
	}

	private DynamicForm commonForm;

	/** StaticResource Common Field */
	private TextItem contentTypeField;
	private StaticResourceUploadPane uploadPane;

	private DynamicForm entryForm;
	private VLayout entryPane;

	/** Entry Text Charset */
	private TextItem entryTextCharsetField;
	/** Entry Path Type */
	private SelectItem entryPathTypeField;
	private EntryPathTypeEditPane<?> entryPathTypeEditPane;

	/** MIMETypeMappingList属性部分 */
	private MimeTypeMapGridPane mimeTypeMapGridPane;

	/**
	 * コンストラクタ
	 */
	public StaticResourceAttributePane() {

		setAutoHeight();
		setMargin(5);

		// 共通入力部分
		commonForm = new DynamicForm();
		commonForm.setWidth100();
		commonForm.setNumCols(5);	//間延びしないように最後に１つ余分に作成
		commonForm.setColWidths(100, "*", 100, "*", "*");
		commonForm.setMargin(5);

		contentTypeField = new TextItem("contentType", "Content Type");
		contentTypeField.setWidth("100%");

		uploadPane = new StaticResourceUploadPane();

		commonForm.setItems(contentTypeField);

		entryTextCharsetField = new TextItem("entryTextCharset", "Entry Text Charset");
		entryTextCharsetField.setWidth("30%");

		// EntryPathType入力部分
		entryPathTypeField = new SelectItem("entryPathType", "Entry Path Translator");
		entryPathTypeField.setValueMap(entryPathTypeMap);

		entryForm = new DynamicForm();
		entryForm.setWidth100();
		entryForm.setMargin(5);

		entryForm.setItems(entryTextCharsetField, entryPathTypeField);

		entryPane = new VLayout();

		// MIME Type Mapping 入力部分
		mimeTypeMapGridPane = new MimeTypeMapGridPane();

		//配置
		addMember(commonForm);
		addMember(uploadPane);
		addMember(SmartGWTUtil.separator());
		addMember(entryForm);
		addMember(entryPane);
		addMember(SmartGWTUtil.separator());
		addMember(mimeTypeMapGridPane);
	}

	public void setDefinition(StaticResourceInfo definition) {
		this.contentTypeField.setValue(definition.getContentType());

		this.uploadPane.setDefinition(definition);

		this.entryTextCharsetField.setValue(definition.getEntryTextCharset());

		if (this.entryPathTypeEditPane != null) {
			this.entryPane.removeMember(this.entryPathTypeEditPane);
		}

		EntryPathTranslatorDefinition entryPathTranslator = definition.getEntryPathTranslator();
		if (entryPathTranslator != null) {
			EntryPathType entryPathType = EntryPathType.valueOf(entryPathTranslator);
			this.entryPathTypeField.setValue(entryPathType);

			switch (entryPathType) {
			case JAVA:
				this.entryPathTypeEditPane = new JavaClassEntryPathEditPane();
				break;
			case PREFIX:
				this.entryPathTypeEditPane = new PrefixEntryPathEditPane();
				break;
			case SCRIPT:
				this.entryPathTypeEditPane = new ScriptingEntryPathEditPane();
				break;
			}
			this.entryPathTypeEditPane.setDefinition(definition);
			this.entryPane.addMember(this.entryPathTypeEditPane);

			mimeTypeMapGridPane.clearRecord();
			List<MimeTypeMappingDefinition> mimeTypeMappingList = definition.getMimeTypeMapping();
			if (mimeTypeMappingList != null) {
				this.mimeTypeMapGridPane.setMimeTypeMap(mimeTypeMappingList.toArray(new MimeTypeMappingDefinition[]{}));
			}
		} else {
			this.entryPathTypeField.clearValue();
			this.entryPathTypeEditPane = null;
		}
	}

	public StaticResourceInfo getEditDefinition(StaticResourceInfo definition) {
		definition.setContentType(SmartGWTUtil.getStringValue(this.contentTypeField));
		definition.setEntryTextCharset(SmartGWTUtil.getStringValue(this.entryTextCharsetField));

		definition = this.uploadPane.getEditStaticResourceDefinition(definition);
		if (this.entryPathTypeEditPane != null) {
			definition = this.entryPathTypeEditPane.getEditDefinition(definition);
		}
		definition = this.mimeTypeMapGridPane.getEditDefinition(definition);

		return definition;
	}

	public FileUpload getEditFileUpload() {
		return this.uploadPane.getEditFileUpload();
	}

	public UploadFileItem getEditUploadFileItem() {
		return this.uploadPane.getEditUploadFileItem();
	}

	public void resetSrcImg() {
		this.uploadPane.resetSrcImg();
	}

	public boolean validate() {
		return this.commonForm.validate()
				& this.uploadPane.validate()
				& (this.entryPathTypeEditPane == null || this.entryPathTypeEditPane.validate())
				& this.mimeTypeMapGridPane.validate();
	}

	/**
	 * EntryPathType変更イベントを設定します。
	 * @param handler
	 */
	public void setEntryPathTypeChangedHandler(ChangedHandler handler) {
		entryPathTypeField.addChangedHandler(handler);
	}

	/**
	 * 選択されているEntryPathTypeを返します。
	 * @return
	 */
	public EntryPathType selectedEntryPathType() {
		return EntryPathType.valueOf(SmartGWTUtil.getStringValue(entryPathTypeField));
	}

}
