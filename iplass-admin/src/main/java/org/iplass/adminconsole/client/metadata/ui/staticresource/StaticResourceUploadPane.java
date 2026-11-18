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

import java.util.LinkedHashMap;
import java.util.Map;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.io.download.PostDownloadFrame;
import org.iplass.adminconsole.client.base.io.download.XsrfProtectedImageLoader;
import org.iplass.adminconsole.client.base.io.upload.UploadFileItem;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.metadata.dto.staticresource.FileType;
import org.iplass.adminconsole.shared.metadata.dto.staticresource.LocalizedStaticResourceInfo;
import org.iplass.adminconsole.shared.metadata.dto.staticresource.StaticResourceDownloadProperty;
import org.iplass.adminconsole.shared.metadata.dto.staticresource.StaticResourceInfo;
import org.iplass.mtp.web.actionmapping.definition.result.ContentDispositionType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FileUpload;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class StaticResourceUploadPane extends VLayout {

	/** Fileの種類選択用Map */
	private static LinkedHashMap<String, String> fileTypeMap;
	static {
		fileTypeMap = new LinkedHashMap<>();
		for (FileType type : FileType.values()) {
			fileTypeMap.put(type.name(), type.displayName());
		}
	}

	/** ファイルタイプ */
	private DynamicForm fileTypePane;
	private SelectItem fileTypeField;

	/** バイナリファイル */
	private UploadFileItem itmFileUpload;

	/** 保存済みテンプレートファイルダウンロード */
	private DynamicForm downloadForm;
	private ButtonItem downloadFilebtn;
	private StaticTextItem txtDownloadFileName;

	private Label lblRegistedStatus;
	private Canvas imgRegistedBinary;
	/** プレビュー画像のblob URL */
	private String previewObjectUrl;

	public StaticResourceUploadPane() {
		// レイアウト設定
		setHeight(300);
		setWidth100();
		setMargin(5);
		setMembersMargin(5);

		fileTypeField = new SelectItem("fileType", "File Type");
		fileTypeField.setValueMap(fileTypeMap);

		fileTypePane = new DynamicForm();
		fileTypePane.setItems(fileTypeField);
		SmartGWTUtil.setRequired(fileTypeField);

		itmFileUpload = new UploadFileItem();

		downloadForm = new DynamicForm();
		downloadForm.setWidth100();
		downloadForm.setMargin(5);

		txtDownloadFileName = new StaticTextItem();
		txtDownloadFileName.setTitle("Saved File");

		downloadFilebtn = new ButtonItem("Download", "Download");
		downloadFilebtn.setWidth(100);
		downloadFilebtn.setStartRow(false);
		downloadForm.setItems(txtDownloadFileName, new SpacerItem(), downloadFilebtn);

		HLayout previewComposit = new HLayout(5);
		previewComposit.setHeight100();

		Label previewLabel = new Label("Saved File Preview :");
		previewLabel.setStyleName("formTitle");
		previewLabel.setWrap(false);
		previewLabel.setAlign(Alignment.RIGHT);
		previewComposit.addMember(previewLabel);

		VLayout previewContents = new VLayout();
		previewContents.setWidth100();

		lblRegistedStatus = new Label();
		lblRegistedStatus.setHeight(25);
		previewContents.addMember(lblRegistedStatus);

		imgRegistedBinary = new Canvas();
		imgRegistedBinary.setPadding(5);
		imgRegistedBinary.setWidth100();
		imgRegistedBinary.setHeight100();
		imgRegistedBinary.setOverflow(Overflow.AUTO);
		imgRegistedBinary.setBorder("1px solid gray");
		previewContents.addMember(imgRegistedBinary);

		Label previewComment = new Label(AdminClientMessageUtil.getString("ui_metadata_staticresource_StaticResourceUploadPane_previewComment"));
		previewComment.setHeight(25);
		previewComment.setWrap(false);
		previewContents.addMember(previewComment);
		previewComposit.addMember(previewContents);

		// 配置
		addMember(fileTypePane);
		addMember(itmFileUpload);
		addMember(downloadForm);
		addMember(previewComposit);
	}

	public void setDefinition(StaticResourceInfo definition) {

		if (definition.getFileType() != null) {
			this.fileTypeField.setValue(definition.getFileType());
		} else {
			this.fileTypeField.setValue(FileType.SIMPLE);
		}

		//選択ファイルをクリア
		this.itmFileUpload.clearFile();

		// Previewの設定
		setupImagePreview(definition);
	}

	public void setDefinition(LocalizedStaticResourceInfo definition, UploadFileItem fileItem) {
		if (definition.getFileType() != null) {
			this.fileTypeField.setValue(definition.getFileType());
		} else {
			this.fileTypeField.setValue(FileType.SIMPLE);
		}

		if (fileItem != null) {
			this.itmFileUpload.setFileUpload(fileItem.getEditFileUpload());
		}

		// Previewの設定
		setupImagePreview(definition);
	}

	public StaticResourceInfo getEditStaticResourceDefinition(StaticResourceInfo definition) {
		String fileTypeStr = SmartGWTUtil.getStringValue(this.fileTypeField);
		definition.setFileType(fileTypeStr != null && !fileTypeStr.isEmpty() ? FileType.valueOf(fileTypeStr) : null);

		return definition;
	}

	public LocalizedStaticResourceInfo getEditLocalizedStaticResourceDefinition(LocalizedStaticResourceInfo definition) {
		String fileTypeStr = SmartGWTUtil.getStringValue(this.fileTypeField);
		definition.setFileType(fileTypeStr != null && !fileTypeStr.isEmpty() ? FileType.valueOf(fileTypeStr) : null);

		return definition;
	}

	public UploadFileItem getEditUploadFileItem() {
		return this.itmFileUpload;
	}

	public FileUpload getEditFileUpload() {
		return this.itmFileUpload.getEditFileUpload();
	}

	public void resetSrcImg() {
		releasePreviewImage();
		this.imgRegistedBinary.setContents("");
	}

	public boolean validate() {
		return this.fileTypePane.validate();
	}

	private void setupImagePreview(StaticResourceInfo definition) {
		setupImagePreview(definition.getFileType(), definition.getName(), definition.getStoredBinaryName(), null);
	}

	private void setupImagePreview(LocalizedStaticResourceInfo definition) {
		setupImagePreview(definition.getFileType(), definition.getName(), definition.getStoredBinaryName(), definition.getLocaleName());
	}

	private void setupImagePreview(FileType fileType, String defName, String binaryName, String lang) {
		if (!SmartGWTUtil.isEmpty(binaryName)) {
			txtDownloadFileName.setValue(binaryName);
			setTemplateDownloadAction(defName, lang);
			downloadForm.setVisible(true);

			if (fileType == FileType.SIMPLE) {
				Map<String, String> params = new LinkedHashMap<String, String>();
				params.put(StaticResourceDownloadProperty.TENANT_ID, String.valueOf(TenantInfoHolder.getId()));
				params.put(StaticResourceDownloadProperty.DEFINITION_NAME, defName);
				params.put(StaticResourceDownloadProperty.CONTENT_DISPOSITION_TYPE,
						ContentDispositionType.INLINE.name());
				if (!SmartGWTUtil.isEmpty(lang)) {
					params.put(StaticResourceDownloadProperty.LANG, lang);
				}
				params.put("dummy", String.valueOf(System.currentTimeMillis())); // 画像変換時の再描画のため
				loadPreviewImage(GWT.getModuleBaseURL() + StaticResourceDownloadProperty.ACTION_URL, params);
			} else {
				releasePreviewImage();
				imgRegistedBinary.setContents("");
				imgRegistedBinary.setVisible(false);
			}
			lblRegistedStatus.setContents("Resource data is registered.");
		} else {
			downloadForm.setVisible(false);

			imgRegistedBinary.setContents("");
			imgRegistedBinary.setVisible(false);
			lblRegistedStatus.setContents("Still no resource data.");
		}
	}

	/**
	 * XSRFトークン付きPOSTでプレビュー画像を取得する。
	 *
	 * @param url    ダウンロードサーブレットのURL
	 * @param params リクエストパラメータ
	 */
	private void loadPreviewImage(String url, Map<String, String> params) {
		releasePreviewImage();
		imgRegistedBinary.setVisible(false);

		XsrfProtectedImageLoader.load(url, params, new XsrfProtectedImageLoader.Callback() {

			@Override
			public void onLoad(String objectUrl) {
				previewObjectUrl = objectUrl;
				imgRegistedBinary.setContents(previewImageHtml(objectUrl));
				imgRegistedBinary.setVisible(true);
			}

			@Override
			public void onError(int status) {
				previewObjectUrl = null;
				imgRegistedBinary.setContents("");
				imgRegistedBinary.setVisible(false);
			}
		});
	}

	/**
	 * プレビュー画像のblob URLを解放する。
	 */
	private void releasePreviewImage() {
		if (previewObjectUrl != null) {
			XsrfProtectedImageLoader.release(previewObjectUrl);
			previewObjectUrl = null;
		}
	}

	/**
	 * プレビュー画像を Canvas の内容として出力する。
	 *
	 * @param objectUrl {@code URL.createObjectURL} が返した blob URL
	 * @return プレビュー用の img 要素の HTML
	 */
	private static String previewImageHtml(String objectUrl) {
		return "<img src=\"" + objectUrl + "\" style=\"max-width:100%;max-height:100%;\"/>";
	}

	private void setTemplateDownloadAction(final String defName, final String lang) {

		com.smartgwt.client.widgets.form.fields.events.ClickHandler handler = new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				PostDownloadFrame frame = new PostDownloadFrame();
				frame.setAction(GWT.getModuleBaseURL() + StaticResourceDownloadProperty.ACTION_URL)
						.addParameter(StaticResourceDownloadProperty.TENANT_ID, String.valueOf(TenantInfoHolder.getId()))
						.addParameter(StaticResourceDownloadProperty.DEFINITION_NAME, defName)
						.addParameter(StaticResourceDownloadProperty.CONTENT_DISPOSITION_TYPE, ContentDispositionType.ATTACHMENT.name())
						.addParameter("dummy", String.valueOf(System.currentTimeMillis()));
				if (!SmartGWTUtil.isEmpty(lang)) {
					frame.addParameter(StaticResourceDownloadProperty.LANG, lang);
				}
				frame.execute();
			}
		};
		downloadFilebtn.addClickHandler(handler);
	}

}
