/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.io.download.PostDownloadFrame;
import org.iplass.adminconsole.client.base.io.download.XsrfProtectedImageLoader;
import org.iplass.adminconsole.client.base.io.upload.UploadFileItem;
import org.iplass.adminconsole.client.base.io.upload.UploadResultInfo;
import org.iplass.adminconsole.client.base.io.upload.UploadSubmitCompleteHandler;
import org.iplass.adminconsole.client.base.io.upload.XsrfProtectedMultipartForm;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.template.TemplateMultiLanguagePane.LocalizedBinaryDefinitionInfo;
import org.iplass.adminconsole.shared.metadata.dto.AdminDefinitionModifyResult;
import org.iplass.adminconsole.shared.metadata.dto.template.BinaryTemplateDownloadProperty;
import org.iplass.adminconsole.shared.metadata.dto.template.BinaryTemplateUploadProperty;
import org.iplass.mtp.web.actionmapping.definition.result.ContentDispositionType;
import org.iplass.mtp.web.template.definition.BinaryTemplateDefinition;
import org.iplass.mtp.web.template.definition.LocalizedBinaryDefinition;
import org.iplass.mtp.web.template.definition.TemplateDefinition;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class BinaryTemplateEditPane extends TemplateTypeEditPane implements HasEditLocalizedBinaryDefinition {

	/** フォーム */
	private XsrfProtectedMultipartForm form;
	private FlowPanel paramPanel;

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

	private AsyncCallback<AdminDefinitionModifyResult> callback;

	/**
	 * コンストラクタ
	 */
	public BinaryTemplateEditPane() {

		setWidth100();

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

		Label previewComment = new Label(AdminClientMessageUtil.getString("ui_metadata_template_BinaryTemplateEditPane_previewComment"));
		previewComment.setHeight(25);
		previewComment.setWrap(false);
		previewContents.addMember(previewComment);
		previewComposit.addMember(previewContents);

		// 入力部分
		form = new XsrfProtectedMultipartForm();
		form.setVisible(false); // formは非表示でOK
		form.setHeight("5px");
		form.setService(BinaryTemplateUploadProperty.ACTION_URL);
		form.addSubmitCompleteHandler(new BinaryTemplateDefinitionSubmitCompleteHandler());
		paramPanel = new FlowPanel();
		//		form.add(paramPanel);
		form.insertAfter(paramPanel);

		// 配置
		addMember(form);
		addMember(itmFileUpload);
		addMember(downloadForm);
		addMember(previewComposit);
	}

	@Override
	public void setDefinition(TemplateDefinition definition) {

		BinaryTemplateDefinition btd = (BinaryTemplateDefinition) definition;

		// Previewの設定
		setupImagePreview(btd.getName(), btd.getFileName(), btd.getBinary(), null);
	}

	@Override
	public TemplateDefinition getEditDefinition(TemplateDefinition definition) {
		BinaryTemplateDefinition binary = (BinaryTemplateDefinition) definition;

		// byteはFileUpload経由で送るのでセットしない。
		return binary;
	}

	@Override
	public boolean validate() {
		return true;
	}

	@Override
	public boolean isFileUpload() {
		return true;
	}

	public void updateBinaryTemplate(final TemplateDefinition definition, List<LocalizedBinaryDefinitionInfo> localeList,
			int curVersion, boolean checkVersion, AsyncCallback<AdminDefinitionModifyResult> callback) {
		this.callback = callback;

		// 上書き再実行の可能性があるので一度クリア
		paramPanel.clear();
		paramPanel.add(itmFileUpload.getEditFileUpload());

		addUploadParametar(BinaryTemplateUploadProperty.TENANT_ID, Integer.toString(TenantInfoHolder.getId()));

		addUploadParametar(BinaryTemplateUploadProperty.DEF_NAME, definition.getName());
		addUploadParametar(BinaryTemplateUploadProperty.DISPLAY_NAME, definition.getDisplayName());
		addUploadParametar(BinaryTemplateUploadProperty.DESCRIPTION, definition.getDescription());

		addUploadParametar(BinaryTemplateUploadProperty.CONTENT_TYPE, definition.getContentType());
		addUploadParametar(BinaryTemplateUploadProperty.VERSION, Integer.toString(curVersion));
		addUploadParametar(BinaryTemplateUploadProperty.CHECK_VERSION, String.valueOf(checkVersion));

		if (localeList != null && !localeList.isEmpty()) {
			for (LocalizedBinaryDefinitionInfo info : localeList) {
				String locale = info.getDefinition()
						.getLocaleName();

				String prefix = BinaryTemplateUploadProperty.LOCALE_PREFIX + locale + "_";

				if (!SmartGWTUtil.isEmpty(info.getStoredLang())) {
					// 更新の場合は、更新前のLocaleを送ることで削除対象にしない
					addUploadParametar(prefix + BinaryTemplateUploadProperty.LOCALE_BEFORE, info.getStoredLang());
				}
				// ファイルが選択されているもののみ送る
				// (新規でFileが選択されていないものは除外される
				if (info.getFileItem() != null) {
					FileUpload localeFile = info.getFileItem()
							.getEditFileUpload();
					localeFile.setName(prefix + BinaryTemplateUploadProperty.UPLOAD_FILE);
					localeFile.setVisible(false);
					paramPanel.add(localeFile);
				}
			}
		}

		form.submit();
	}

	private void addUploadParametar(String key, String value) {
		paramPanel.add(new Hidden(key, value));
	}

	private void setupImagePreview(String defName, String fileName, byte[] binary, String lang) {
		if (binary != null) {
			if (SmartGWTUtil.isEmpty(fileName)) {
				txtDownloadFileName.setValue("unknown file name.");
			} else {
				txtDownloadFileName.setValue(fileName);
			}
			setTemplateDownloadAction(defName, lang);
			downloadForm.setVisible(true);

			Map<String, String> params = new LinkedHashMap<String, String>();
			params.put(BinaryTemplateDownloadProperty.TENANT_ID, String.valueOf(TenantInfoHolder.getId()));
			params.put(BinaryTemplateDownloadProperty.DEFINITION_NAME, defName);
			params.put(BinaryTemplateDownloadProperty.CONTENT_DISPOSITION_TYPE, ContentDispositionType.INLINE.name());
			if (!SmartGWTUtil.isEmpty(lang)) {
				params.put(BinaryTemplateDownloadProperty.LANG, lang);
			}
			params.put("dummy", String.valueOf(System.currentTimeMillis())); // 画像変換時の再描画のため
			lblRegistedStatus.setContents("Binary data is registered.");
			loadPreviewImage(GWT.getModuleBaseURL() + BinaryTemplateDownloadProperty.ACTION_URL, params);
		} else {
			downloadForm.setVisible(false);
			lblRegistedStatus.setContents("Still no binary data.");
			releasePreviewImage();
			imgRegistedBinary.setContents("");
			imgRegistedBinary.setVisible(false);
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

	private void setTemplateDownloadAction(final String templateName, final String lang) {
		com.smartgwt.client.widgets.form.fields.events.ClickHandler handler = new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				PostDownloadFrame frame = new PostDownloadFrame();
				frame.setAction(GWT.getModuleBaseURL() + BinaryTemplateDownloadProperty.ACTION_URL)
						.addParameter(BinaryTemplateDownloadProperty.TENANT_ID, String.valueOf(TenantInfoHolder.getId()))
						.addParameter(BinaryTemplateDownloadProperty.DEFINITION_NAME, templateName)
						.addParameter(BinaryTemplateDownloadProperty.CONTENT_DISPOSITION_TYPE, ContentDispositionType.ATTACHMENT.name())
						.addParameter("dummy", String.valueOf(System.currentTimeMillis()));
				if (!SmartGWTUtil.isEmpty(lang)) {
					frame.addParameter(BinaryTemplateDownloadProperty.LANG, lang);
				}
				frame.execute();
			}

		};
		downloadFilebtn.addClickHandler(handler);
	}

	@Override
	public void setLocalizedBinaryDefinition(LocalizedBinaryDefinition definition, String templateDefName, UploadFileItem fileItem) {
		if (fileItem != null) {
			itmFileUpload.setFileUpload(fileItem.getEditFileUpload());
		}
		setupImagePreview(templateDefName, definition.getFileName(), definition.getBinaryValue(), definition.getLocaleName());
	}

	@Override
	public LocalizedBinaryDefinition getEditLocalizedBinaryDefinition(LocalizedBinaryDefinition definition) {
		// byteはFileUpload経由で送るのでセットしない。
		return definition;
	}

	@Override
	public UploadFileItem getEditUploadFileItem() {
		return itmFileUpload;
	}

	@Override
	public int getLocaleDialogHeight() {
		return 500;
	}

	private class BinaryTemplateDefinitionSubmitCompleteHandler extends UploadSubmitCompleteHandler {

		public BinaryTemplateDefinitionSubmitCompleteHandler() {
			super(form);
		}

		@Override
		protected void onSuccess(UploadResultInfo result) {

			// FlowPanelに追加しているので戻す
			itmFileUpload.redrawFileUpload();

			releasePreviewImage();
			imgRegistedBinary.setContents("");

			if (callback != null) {
				String message = null;
				if (result.getMessages() != null && result.getMessages()
						.size() > 0) {
					message = result.getMessages()
							.get(0);
				}
				callback.onSuccess(new AdminDefinitionModifyResult(result.isFileUploadStatusSuccess(), message));
			}
			callback = null;
		}

		@Override
		protected void onFailure(String message) {

			// FlowPanelに追加しているので戻す
			itmFileUpload.redrawFileUpload();

			if (callback != null) {
				callback.onFailure(new RuntimeException(message));
			}
			callback = null;
		}

	}

}
