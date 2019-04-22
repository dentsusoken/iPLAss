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

package org.iplass.adminconsole.client.metadata.ui.action.result;

import java.util.LinkedHashMap;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpSelectItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.web.actionmapping.definition.result.ContentDispositionType;
import org.iplass.mtp.web.actionmapping.definition.result.ResultDefinition;
import org.iplass.mtp.web.actionmapping.definition.result.StreamResultDefinition;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;

public class StreamResultEditPane extends ResultTypeEditPane {

	/** フォーム */
	private DynamicForm form;

	/** ImputStreamが入っているAttribute名 */
	private TextItem streamAttributeNameField;

	/** ContentTypeが入っているAttribute名 */
	private TextItem contentTypeAttributeNameField;

	/** ContentLengthが入っているAttribute名 */
	private TextItem contentLengthAttributeNameField;

	/** rangeヘッダ対応するかのフラグ */
	private CheckboxItem acceptRangesField;

	private DynamicForm contentDispositionForm;

	/** ContentDispotisionを利用するかのフラグ */
	private CheckboxItem useContentDispositionField;

	/** ContentDispotision Type */
	private SelectItem contentDispositionTypeField;

	/** ファイル名が入っているAttribute名 */
	private TextItem fileNameAttributeNameField;

	/**
	 * コンストラクタ
	 */
	public StreamResultEditPane() {

		setWidth100();

		//入力部分
		form = new MtpForm();
		form.setAutoHeight();

		streamAttributeNameField = new MtpTextItem("streamAttributeName", "Stream AttributeName");
		streamAttributeNameField.setRequired(true);
		SmartGWTUtil.setRequired(streamAttributeNameField);

		contentTypeAttributeNameField = new MtpTextItem("contentTypeAttributeName", "contentType AttributeName");

		contentLengthAttributeNameField = new MtpTextItem("contentLengthAttributeName", "contentLength AttributeName");

		acceptRangesField = new CheckboxItem("acceptRanges", "Accept Ranges");
		SmartGWTUtil.addHoverToFormItem(acceptRangesField, AdminClientMessageUtil.getString("ui_metadata_action_StreamResultEditPane_acceptRangesTooltip"));

		form.setItems(streamAttributeNameField, contentTypeAttributeNameField, contentLengthAttributeNameField, acceptRangesField);

		contentDispositionForm = new MtpForm();
		contentDispositionForm.setAutoHeight();
		contentDispositionForm.setIsGroup(true);
		contentDispositionForm.setGroupTitle("Content Disposition Setting");

		useContentDispositionField = new CheckboxItem("useContentDisposition", "Set ContentDisposition");

		contentDispositionTypeField = new MtpSelectItem();
		contentDispositionTypeField.setTitle("Content Disposition Type");

		LinkedHashMap<String, String> contentDispositionTypeMap = new LinkedHashMap<String, String>();
		contentDispositionTypeMap.put("", "Default");
		contentDispositionTypeMap.put(ContentDispositionType.ATTACHMENT.name(), "Attachment");
		contentDispositionTypeMap.put(ContentDispositionType.INLINE.name(), "Inline");
		contentDispositionTypeField.setValueMap(contentDispositionTypeMap);

		fileNameAttributeNameField = new MtpTextItem("fileNameAttributeName", "FileName AttributeName");

		contentDispositionForm.setItems(useContentDispositionField, contentDispositionTypeField, fileNameAttributeNameField);

		//配置
		addMember(form);
		addMember(contentDispositionForm);

	}

	/**
	 * ResultDefinitionを展開します。
	 *
	 * @param definition StreamResultDefinition
	 */
	@Override
	public void setDefinition(ResultDefinition definition) {
		StreamResultDefinition stream = (StreamResultDefinition)definition;
		streamAttributeNameField.setValue(stream.getInputStreamAttributeName());
		contentTypeAttributeNameField.setValue(stream.getContentTypeAttributeName());
		contentLengthAttributeNameField.setValue(stream.getContentLengthAttributeName());
		acceptRangesField.setValue(stream.isAcceptRanges());
		useContentDispositionField.setValue(stream.isUseContentDisposition());
		if (stream.getContentDispositionType() != null) {
			contentDispositionTypeField.setValue(stream.getContentDispositionType().name());
		} else {
			contentDispositionTypeField.setValue("");
		}
		fileNameAttributeNameField.setValue(stream.getFileNameAttributeName());
	}

	/**
	 * 編集されたResultDefinition情報を返します。
	 *
	 * @return 編集ResultDefinition情報
	 */
	@Override
	public ResultDefinition getEditDefinition(ResultDefinition definition) {
		StreamResultDefinition stream = (StreamResultDefinition)definition;
		stream.setInputStreamAttributeName(SmartGWTUtil.getStringValue(streamAttributeNameField));
		stream.setContentTypeAttributeName(SmartGWTUtil.getStringValue(contentTypeAttributeNameField));
		stream.setContentLengthAttributeName(SmartGWTUtil.getStringValue(contentLengthAttributeNameField));
		stream.setAcceptRanges(SmartGWTUtil.getBooleanValue(acceptRangesField));
		stream.setUseContentDisposition(SmartGWTUtil.getBooleanValue(useContentDispositionField));
		String contentDispositionTypeName = SmartGWTUtil.getStringValue(contentDispositionTypeField);
		if (contentDispositionTypeName != null && !contentDispositionTypeName.isEmpty()) {
			stream.setContentDispositionType(ContentDispositionType.valueOf(contentDispositionTypeName));
		} else {
			stream.setContentDispositionType(null);
		}
		stream.setFileNameAttributeName(SmartGWTUtil.getStringValue(fileNameAttributeNameField));
		return stream;
	}

	/**
	 * 入力チェックを実行します。
	 *
	 * @return 入力チェック結果
	 */
	@Override
	public boolean validate() {
		boolean formValidate = form.validate();
		boolean contentDispositionFormValidate = contentDispositionForm.validate();
		return formValidate && contentDispositionFormValidate;
	}

}
