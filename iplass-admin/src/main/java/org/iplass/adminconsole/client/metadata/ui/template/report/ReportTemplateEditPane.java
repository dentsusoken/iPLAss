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

package org.iplass.adminconsole.client.metadata.ui.template.report;

import java.util.LinkedHashMap;
import java.util.List;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.io.download.PostDownloadFrame;
import org.iplass.adminconsole.client.base.io.upload.UploadFileItem;
import org.iplass.adminconsole.client.base.io.upload.UploadResultInfo;
import org.iplass.adminconsole.client.base.io.upload.UploadSubmitCompleteHandler;
import org.iplass.adminconsole.client.base.io.upload.XsrfProtectedMultipartForm;
import org.iplass.adminconsole.client.base.rpc.AdminAsyncCallback;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpSelectItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.template.HasEditLocalizedReportDefinition;
import org.iplass.adminconsole.client.metadata.ui.template.TemplateMultiLanguagePane.LocalizedReportDefinitionInfo;
import org.iplass.adminconsole.client.metadata.ui.template.TemplateTypeEditPane;
import org.iplass.adminconsole.shared.metadata.dto.AdminDefinitionModifyResult;
import org.iplass.adminconsole.shared.metadata.dto.Name;
import org.iplass.adminconsole.shared.metadata.dto.template.ReportTemplateDownloadProperty;
import org.iplass.adminconsole.shared.metadata.dto.template.ReportTemplateUploadProperty;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.web.template.definition.TemplateDefinition;
import org.iplass.mtp.web.template.report.definition.GroovyReportOutputLogicDefinition;
import org.iplass.mtp.web.template.report.definition.JasperReportType;
import org.iplass.mtp.web.template.report.definition.JavaClassReportOutputLogicDefinition;
import org.iplass.mtp.web.template.report.definition.JxlsReportType;
import org.iplass.mtp.web.template.report.definition.LocalizedReportDefinition;
import org.iplass.mtp.web.template.report.definition.OutputFileType;
import org.iplass.mtp.web.template.report.definition.PoiReportType;
import org.iplass.mtp.web.template.report.definition.ReportOutputLogicDefinition;
import org.iplass.mtp.web.template.report.definition.ReportParamMapDefinition;
import org.iplass.mtp.web.template.report.definition.ReportTemplateDefinition;
import org.iplass.mtp.web.template.report.definition.ReportType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.Hidden;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;

public class ReportTemplateEditPane extends TemplateTypeEditPane implements HasEditLocalizedReportDefinition {

	private final MetaDataServiceAsync service = MetaDataServiceFactory.get();

	private XsrfProtectedMultipartForm form;
	private FlowPanel paramPanel;

	private DynamicForm reportTypeForm;
	private SelectItem reportTypeField;

	private DynamicForm outputTypeForm;
	private SelectItem outputTypeField;

	/** Jasper ParamMap部分 */
	private JasperReportParamMapGridPane jasperParamMapPane;
	/** Jasper Attribute部分 */
	private DynamicForm jasperAttributeForm;
	/** Jasper DataSourceAttributeName */
	private TextItem jasperDataSourceAttributeNameField;
	/** Jasper PasswordAttributeName */
	private TextItem jasperPasswordAttributeNameField;

	/** Poi PasswordAttributeName */
	private DynamicForm poiPasswordAttributeNameForm;
	private TextItem poiPasswordAttributeNameField;
	/** Poiレポート出力ロジック部分 */
	private PoiReportOutLogicPane reportOutPane;

	/** JXLS PasswordAttributeName */
	private DynamicForm jxlsPasswordAttributeNameForm;
	private TextItem jxlsPasswordAttributeNameField;
	/** JXLS ContextParamMap */
	private JxlsReportParamMapGridPane jxlsContextParamMapPane;
	/** JXLS ReportOutputLogic */
	private JxlsReportOutLogicPane jxlsReportOutputLogicPane;

	/** テンプレートファイル */
	private UploadFileItem itmFileUpload;

	/** 保存済みテンプレートファイルダウンロード */
	private DynamicForm downloadForm;
	private ButtonItem downloadFilebtn;
	private StaticTextItem txtDownloadFileName;

	private AsyncCallback<AdminDefinitionModifyResult> callback;

	/** 変更前のReportTypeの値を保持  */
	private String beforeReportType;

	/**
	 * コンストラクタ
	 */
	public ReportTemplateEditPane() {

		setWidth100();

		reportTypeForm = new MtpForm();

		reportTypeField = new MtpSelectItem("outputType", "Report Type");
		reportTypeField.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				//アップロード可能拡張子変更
				String reportType = SmartGWTUtil.getStringValue(reportTypeField);
				getOutputFileType(reportType);

				//プルダウン、ファイルクリア
				outputTypeField.clearValue();

				if (PoiReportType.class.getName().equals(reportType)) {
					//Poi利用の場合
					jasperParamMapPane.deleteAll();
					jxlsReportOutputLogicPane.deleteAll();
					jxlsContextParamMapPane.deleteAll();
					removeSpecificMember(beforeReportType);
					addMembers(poiPasswordAttributeNameForm, reportOutPane);
					beforeReportType = PoiReportType.class.getName();

				} else if (JasperReportType.class.getName().equals(reportType)) {
					//Jasper利用の場合
					reportOutPane.deleteAll();
					jxlsReportOutputLogicPane.deleteAll();
					jxlsContextParamMapPane.deleteAll();
					removeSpecificMember(beforeReportType);
					addMembers(jasperAttributeForm, jasperParamMapPane);
					beforeReportType = JasperReportType.class.getName();

				} else if (JxlsReportType.class.getName().equals(reportType)) {
					//JXLS利用の場合
					jasperParamMapPane.deleteAll();
					reportOutPane.deleteAll();
					removeSpecificMember(beforeReportType);
					addMembers(jxlsPasswordAttributeNameForm, jxlsContextParamMapPane, jxlsReportOutputLogicPane);
					beforeReportType = JxlsReportType.class.getName();

				}
			}
		});
		SmartGWTUtil.setRequired(reportTypeField);
		reportTypeForm.setItems(reportTypeField);

		outputTypeForm = new MtpForm();

		outputTypeField = new MtpSelectItem("outputType", "Format");
		outputTypeField.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				//アップロード可能拡張子変更
				// TODO 拡張子チェックを追加する
			}
		});
		SmartGWTUtil.setRequired(outputTypeField);
		outputTypeForm.setItems(outputTypeField);

		itmFileUpload = new UploadFileItem();

		downloadForm = new MtpForm();

		txtDownloadFileName = new StaticTextItem();
		txtDownloadFileName.setTitle(AdminClientMessageUtil.getString("ui_metadata_template_report_ReportTemplateEditPane_savedFile"));

		SpacerItem downloadSpace = new SpacerItem();
		downloadSpace.setStartRow(true);

		downloadFilebtn = new ButtonItem("Download", "Download");
		downloadFilebtn.setWidth(100);
		downloadFilebtn.setStartRow(false);
		downloadForm.setItems(txtDownloadFileName, downloadSpace, downloadFilebtn);

		//入力部分
		form = new XsrfProtectedMultipartForm();
		form.setVisible(false); // formは非表示でOK
		form.setHeight("5px");
		form.setService(ReportTemplateUploadProperty.ACTION_URL);

		paramPanel = new FlowPanel();
		form.insertAfter(paramPanel);

		form.addSubmitHandler(new SubmitHandler() {
			@Override
			public void onSubmit(SubmitEvent event) {
				//				GWT.log("submit start. url=" + form.getAction());
				GWT.log("submit start. url=" + form.getService());
			}
		});
		form.addSubmitCompleteHandler(new ReportTemplateDefinitionSubmitCompleteHandler());

		//JasperAttributeForm
		jasperAttributeForm = new MtpForm();

		jasperDataSourceAttributeNameField = new MtpTextItem();
		jasperDataSourceAttributeNameField.setTitle(AdminClientMessageUtil.getString("ui_metadata_template_report_ReportTemplateEditPane_dataSourceAttributeName"));
		jasperPasswordAttributeNameField = new MtpTextItem();
		jasperPasswordAttributeNameField.setTitle(AdminClientMessageUtil.getString("ui_metadata_template_report_ReportTemplateEditPane_passwordAttributeName"));

		jasperAttributeForm.setItems(jasperDataSourceAttributeNameField, jasperPasswordAttributeNameField);

		//Jasper ParamMap部分
		jasperParamMapPane = new JasperReportParamMapGridPane();

		//PoiパスワードAttributeName
		poiPasswordAttributeNameForm = new MtpForm();
		poiPasswordAttributeNameField = new MtpTextItem("poiPasswordAttributeName", "Password AttributeName");
		poiPasswordAttributeNameForm.setItems(poiPasswordAttributeNameField);

		//Poiレポート出力ロジック部分
		reportOutPane = new PoiReportOutLogicPane();

		/** JXLS PasswordAttributeName */
		jxlsPasswordAttributeNameForm = new MtpForm();
		jxlsPasswordAttributeNameField = new MtpTextItem("jxlsPasswordAttributeName", "Password AttributeName");
		jxlsPasswordAttributeNameForm.setItems(jxlsPasswordAttributeNameField);

		/** JXLS ContextParamMap */
		jxlsContextParamMapPane = new JxlsReportParamMapGridPane();

		/** JXLS ReportOutputLogic */
		jxlsReportOutputLogicPane = new JxlsReportOutLogicPane();

		//配置
		addMember(form);
		addMembers(reportTypeForm, outputTypeForm, itmFileUpload, downloadForm);
		addMembers(jasperAttributeForm, jasperParamMapPane);
		addMembers(poiPasswordAttributeNameForm, reportOutPane);
		addMembers(jxlsPasswordAttributeNameForm, jxlsContextParamMapPane, jxlsReportOutputLogicPane);

		//レポートタイプ取得
		getReportType();
	}

	//画面上に表示されているCanvasをremove
	public void removeSpecificMember(String beforeReportType) {
		if (beforeReportType != null) {
			if (beforeReportType.equals(PoiReportType.class.getName())) {
				removeMembers(poiPasswordAttributeNameForm, reportOutPane);
			} else if (beforeReportType.equals(JasperReportType.class.getName())) {
				removeMembers(jasperAttributeForm, jasperParamMapPane);
			} else if (beforeReportType.equals(JxlsReportType.class.getName())) {
				removeMembers(jxlsPasswordAttributeNameForm, jxlsContextParamMapPane, jxlsReportOutputLogicPane);
			}
		}
	}

	@Override
	public void setDefinition(TemplateDefinition definition) {

		ReportTemplateDefinition rtd = (ReportTemplateDefinition)definition;

		if(rtd.getReportType() != null){
			reportTypeField.setValue(rtd.getReportType().getClass().getName());
			String outputFileType = rtd.getReportType().getOutputFileType();
			if(outputFileType != null){
				outputTypeField.setValue(outputFileType);
				// TODO 拡張子チェックを追加する
			}
			//出力ファイル形式
			getOutputFileType(reportTypeField.getValueAsString());
		}

		//ReportTypeごとの設定
		setReportType(rtd.getReportType());

		if (SmartGWTUtil.isEmpty(rtd.getFileName())) {
			downloadForm.setVisible(false);
		} else {
			txtDownloadFileName.setValue(rtd.getFileName());
			setTemplateDownloadAction(rtd.getName(), null);
			downloadForm.setVisible(true);
		}
	}

	@Override
	public TemplateDefinition getEditDefinition(TemplateDefinition definition) {
		ReportTemplateDefinition repTemplate = (ReportTemplateDefinition)definition;

		// byteはFileUpload経由で送るのでセットしない。

		if (PoiReportType.class.getName().equals(reportTypeField.getValueAsString())) {
			//出力形式設定
			PoiReportType poiTemplate = new PoiReportType();
			poiTemplate.setOutputFileType(outputTypeField.getValueAsString());

			//PasswordAttributeName
			poiTemplate.setPasswordAttributeName(SmartGWTUtil.getStringValue(poiPasswordAttributeNameField, true));
			//POI用出力設定
			poiTemplate = reportOutPane.getEditDefinition(poiTemplate);

			repTemplate.setReportType(poiTemplate);
		} else if (JasperReportType.class.getName().equals(reportTypeField.getValueAsString())) {
			JasperReportType jasperTemplate = new JasperReportType();
			jasperTemplate.setOutputFileType(outputTypeField.getValueAsString());

			//DataSourceAttributeName
			jasperTemplate.setDataSourceAttributeName(SmartGWTUtil.getStringValue(jasperDataSourceAttributeNameField, true));

			//PasswordAttributeName
			jasperTemplate.setPasswordAttributeName(SmartGWTUtil.getStringValue(jasperPasswordAttributeNameField, true));
			//パラメータマッピング設定
			jasperTemplate.setParamMap(jasperParamMapPane.getParamMap());

			repTemplate.setReportType(jasperTemplate);
		} else if (JxlsReportType.class.getName().equals(reportTypeField.getValueAsString())) {
			JxlsReportType jxlsTemplate = new JxlsReportType();
			jxlsTemplate.setOutputFileType(outputTypeField.getValueAsString());

			//PasswordAttributeName
			jxlsTemplate.setPasswordAttributeName(SmartGWTUtil.getStringValue(jxlsPasswordAttributeNameField, true));

			//JXLS用出力設定
			jxlsTemplate = jxlsReportOutputLogicPane.getEditDefinition(jxlsTemplate);

			//ContextParamMapping
			jxlsTemplate.setParamMap(jxlsContextParamMapPane.getParamMap());

			repTemplate.setReportType(jxlsTemplate);
		}

		return repTemplate;
	}

	@Override
	public boolean validate() {
		return reportTypeForm.validate() & outputTypeForm.validate();
	}

	@Override
	public boolean isFileUpload() {
		return true;
	}

	public void updateReportTemplate(final TemplateDefinition definition, List<LocalizedReportDefinitionInfo> localeList,
			int curVersion, boolean checkVersion, AsyncCallback<AdminDefinitionModifyResult> callback) {
		this.callback = callback;

		// 上書き再実行の可能性があるので一度クリア
		paramPanel.clear();
		paramPanel.add(itmFileUpload.getEditFileUpload());

		addUploadParameter(ReportTemplateUploadProperty.TENANT_ID, Integer.toString(TenantInfoHolder.getId()));
		addUploadParameter(ReportTemplateUploadProperty.DEF_NAME, definition.getName());
		addUploadParameter(ReportTemplateUploadProperty.DISPLAY_NAME, definition.getDisplayName());
		addUploadParameter(ReportTemplateUploadProperty.DESCRIPTION, definition.getDescription());

		addUploadParameter(ReportTemplateUploadProperty.CONTENT_TYPE, definition.getContentType());

		addUploadParameter(ReportTemplateUploadProperty.VERSION, Integer.toString(curVersion));
		addUploadParameter(ReportTemplateUploadProperty.CHECK_VERSION, String.valueOf(checkVersion));

		ReportTemplateDefinition repTemplate = (ReportTemplateDefinition)definition;

		addReportTypeParameter(repTemplate.getReportType());

		if (localeList != null && !localeList.isEmpty()) {
			for (LocalizedReportDefinitionInfo info : localeList) {
				String locale = info.getDefinition().getLocaleName();

				String prefix = ReportTemplateUploadProperty.LOCALE_PREFIX + locale + "_";

				if (!SmartGWTUtil.isEmpty(info.getStoredLang())) {
					// 更新の場合は、更新前のLocaleを送ることで削除対象にしない
					addUploadParameter(prefix + ReportTemplateUploadProperty.LOCALE_BEFORE, info.getStoredLang());
				}
				// ファイルが選択されているもののみ送る
				// (新規でFileが選択されていないものは除外される
				if (info.getFileItem() != null) {
					FileUpload localeFile = info.getFileItem().getEditFileUpload();
					localeFile.setName(prefix + ReportTemplateUploadProperty.UPLOAD_FILE);
					localeFile.setVisible(false);
					paramPanel.add(localeFile);
				}

				LocalizedReportDefinition def = info.getDefinition();
				ReportType repoType = def.getReportType();
				addReportTypeParameter(repoType, prefix);
			}
		}

		form.submit();
	}

	private void addReportTypeParameter(ReportType type) {
		addReportTypeParameter(type, "");
	}

	private void addReportTypeParameter(ReportType type, String prefix) {

		if (type != null) {

			String outputFileType = type.getOutputFileType();
			addUploadParameter(prefix + ReportTemplateUploadProperty.OUTPUT_FILE_TYPE, outputFileType);

			if (type instanceof JasperReportType) {
				addUploadParameter(prefix + ReportTemplateUploadProperty.REPORT_TYPE, JasperReportType.class.getName());

				JasperReportType jasRepo = (JasperReportType)type;
				ReportParamMapDefinition[] repoPramDef = jasRepo.getParamMap();

				int i = 0;
				if (repoPramDef != null) {

					for (ReportParamMapDefinition rpmd : repoPramDef) {
						addUploadParameter(prefix + ReportTemplateUploadProperty.JASPER_PARAM_MAP_NAME+"_"+i, rpmd.getName());
						addUploadParameter(prefix + ReportTemplateUploadProperty.JASPER_PARAM_MAP_FROM+"_"+i, rpmd.getMapFrom());
						addUploadParameter(prefix + ReportTemplateUploadProperty.JASPER_PARAM_TYPE+"_"+i, rpmd.getParamType());
						i++;
					}
				}
				addUploadParameter(prefix + ReportTemplateUploadProperty.JASPER_PARAM_MAP_CNT, Integer.toString(i));

				if (!SmartGWTUtil.isEmpty(jasRepo.getDataSourceAttributeName())) {
					addUploadParameter(prefix + ReportTemplateUploadProperty.JASPER_DATASOURCE_ATTRIBUTE_NAME, jasRepo.getDataSourceAttributeName());
				}
				if (!SmartGWTUtil.isEmpty(jasRepo.getPasswordAttributeName())) {
					addUploadParameter(prefix + ReportTemplateUploadProperty.JASPER_PASSWORD_ATTRIBUTE_NAME, jasRepo.getPasswordAttributeName());
				}

			} else if (type instanceof PoiReportType) {
				addUploadParameter(prefix + ReportTemplateUploadProperty.REPORT_TYPE, PoiReportType.class.getName());

				PoiReportType poiRepo = (PoiReportType)type;
				ReportOutputLogicDefinition repoOutputLogicDef =  poiRepo.getReportOutputLogicDefinition();

				if (repoOutputLogicDef != null) {
					if (repoOutputLogicDef instanceof JavaClassReportOutputLogicDefinition) {
						addUploadParameter(prefix + ReportTemplateUploadProperty.POI_LOGIC_NAME, ReportTemplateUploadProperty.POI_LOGIC_NAME_JAVA);
						addUploadParameter(prefix + ReportTemplateUploadProperty.POI_LOGIC_VALUE, ((JavaClassReportOutputLogicDefinition) repoOutputLogicDef).getClassName());
					} else if (repoOutputLogicDef instanceof GroovyReportOutputLogicDefinition) {
						addUploadParameter(prefix + ReportTemplateUploadProperty.POI_LOGIC_NAME, ReportTemplateUploadProperty.POI_LOGIC_NAME_GROOVY);
						addUploadParameter(prefix + ReportTemplateUploadProperty.POI_LOGIC_VALUE, ((GroovyReportOutputLogicDefinition) repoOutputLogicDef).getScript());

					}
				}
				if (!SmartGWTUtil.isEmpty(poiRepo.getPasswordAttributeName())) {
					addUploadParameter(prefix + ReportTemplateUploadProperty.POI_PASSWORD_ATTRIBUTE_NAME, poiRepo.getPasswordAttributeName());
				}
			} else if (type instanceof JxlsReportType) {
				addUploadParameter(prefix + ReportTemplateUploadProperty.REPORT_TYPE, JxlsReportType.class.getName());

				JxlsReportType jxlsRepo = (JxlsReportType)type;
				ReportOutputLogicDefinition repoOutputLogicDef =  jxlsRepo.getReportOutputLogicDefinition();

				if (repoOutputLogicDef != null) {
					if (repoOutputLogicDef instanceof JavaClassReportOutputLogicDefinition) {
						addUploadParameter(prefix + ReportTemplateUploadProperty.JXLS_LOGIC_NAME, ReportTemplateUploadProperty.JXLS_LOGIC_NAME_JAVA);
						addUploadParameter(prefix + ReportTemplateUploadProperty.JXLS_LOGIC_VALUE, ((JavaClassReportOutputLogicDefinition) repoOutputLogicDef).getClassName());
					} else if (repoOutputLogicDef instanceof GroovyReportOutputLogicDefinition) {
						addUploadParameter(prefix + ReportTemplateUploadProperty.JXLS_LOGIC_NAME, ReportTemplateUploadProperty.JXLS_LOGIC_NAME_GROOVY);
						addUploadParameter(prefix + ReportTemplateUploadProperty.JXLS_LOGIC_VALUE, ((GroovyReportOutputLogicDefinition) repoOutputLogicDef).getScript());
					}
				}

				ReportParamMapDefinition[] paramMap = jxlsRepo.getParamMap();
				int i =0;
				if (paramMap != null) {
					for(ReportParamMapDefinition rpmd : paramMap) {
						addUploadParameter(prefix + ReportTemplateUploadProperty.JXLS_PARAM_MAP_NAME+"_"+i, rpmd.getName());
						addUploadParameter(prefix + ReportTemplateUploadProperty.JXLS_PARAM_MAP_VALUE+"_"+i, rpmd.getMapFrom());
						addUploadParameter(prefix + ReportTemplateUploadProperty.JXLS_PARAM_MAP_TO_MAP + "_"+i, String.valueOf( rpmd.isConvertEntityToMap()));
						i++;
					}
				}

				addUploadParameter(prefix + ReportTemplateUploadProperty.JXLS_PARAM_MAP_CNT, Integer.toString(i));

				if (!SmartGWTUtil.isEmpty(jxlsRepo.getPasswordAttributeName())) {
					addUploadParameter(prefix + ReportTemplateUploadProperty.JXLS_PASSWORD_ATTRIBUTE_NAME, jxlsRepo.getPasswordAttributeName());
				}
			}
		}
	}

	private void addUploadParameter(String key, String value) {
		paramPanel.add(new Hidden(key, value));
	}

	private void getReportType(){
		service.getReportTypeList(TenantInfoHolder.getId(), new AdminAsyncCallback<List<Name>>() {

			@Override
			public void onSuccess(List<Name> reportTypes) {
				LinkedHashMap<String, String> reportTypeMap = new LinkedHashMap<String, String>();
				for (Name name: reportTypes) {
					reportTypeMap.put(name.getName(), name.getDisplayName());
				}
				reportTypeField.setValueMap(reportTypeMap);
			}
		});
	}

	private void getOutputFileType(String type){

		if(type == null){
			//デフォルトは、JasperReportType
			type = JasperReportType.class.getName();
		}

		service.getOutputFileTypeList(TenantInfoHolder.getId(), type, new AdminAsyncCallback<List<OutputFileType>>() {

			@Override
			public void onSuccess(List<OutputFileType> fileTypes) {
				LinkedHashMap<String, String> outputFileTypeMap = new LinkedHashMap<String, String>();
				for (OutputFileType type : fileTypes) {
					outputFileTypeMap.put(type.name(), type.displayName());
				}
				outputTypeField.setValueMap(outputFileTypeMap);
			}
		});
	}

	private void setReportType(ReportType type) {

		if (type instanceof PoiReportType) {
			//Poi利用の場合
			PoiReportType poiRepo = (PoiReportType)type;
			reportOutPane.setDefinition(poiRepo);
			poiPasswordAttributeNameField.setValue(poiRepo.getPasswordAttributeName());
			removeMembers(jasperAttributeForm, jasperParamMapPane);
			removeMembers(jxlsPasswordAttributeNameForm, jxlsContextParamMapPane, jxlsReportOutputLogicPane);
			beforeReportType = PoiReportType.class.getName();
		} else if (type instanceof JasperReportType) {
			//Jasper利用の場合
			JasperReportType jasRepo = (JasperReportType)type;
			jasperParamMapPane.setParamMap(jasRepo.getParamMap());
			jasperDataSourceAttributeNameField.setValue(jasRepo.getDataSourceAttributeName());
			jasperPasswordAttributeNameField.setValue(jasRepo.getPasswordAttributeName());
			removeMembers(poiPasswordAttributeNameForm, reportOutPane);
			removeMembers(jxlsPasswordAttributeNameForm, jxlsContextParamMapPane, jxlsReportOutputLogicPane);
			beforeReportType = JasperReportType.class.getName();
		} else if (type instanceof JxlsReportType) {
			//Jxls利用の場合
			JxlsReportType jxlsRepo = (JxlsReportType)type;
			jxlsPasswordAttributeNameField.setValue(jxlsRepo.getPasswordAttributeName());
			jxlsReportOutputLogicPane.setDefinition(jxlsRepo);
			jxlsContextParamMapPane.setParamMap(jxlsRepo.getParamMap());
			removeMembers(jasperAttributeForm, jasperParamMapPane);
			removeMembers(poiPasswordAttributeNameForm, reportOutPane);
			beforeReportType = JxlsReportType.class.getName();
		} else {
			//初期の未選択の場合
			removeMembers(jasperAttributeForm, jasperParamMapPane);
			removeMembers(poiPasswordAttributeNameForm, reportOutPane);
			removeMembers(jxlsPasswordAttributeNameForm, jxlsContextParamMapPane, jxlsReportOutputLogicPane);
		}
	}

	private void setTemplateDownloadAction(final String templateName, final String lang) {

		com.smartgwt.client.widgets.form.fields.events.ClickHandler handler = new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				PostDownloadFrame frame = new PostDownloadFrame();
				frame.setAction(GWT.getModuleBaseURL() + ReportTemplateDownloadProperty.ACTION_URL)
						.addParameter(ReportTemplateDownloadProperty.TENANT_ID, String.valueOf(TenantInfoHolder.getId()))
						.addParameter(ReportTemplateDownloadProperty.DEFINITION_NAME, templateName)
						.addParameter("dummy", String.valueOf(System.currentTimeMillis()));
				if (!SmartGWTUtil.isEmpty(lang)) {
					frame.addParameter(ReportTemplateDownloadProperty.LANG, lang);
				}
				frame.execute();
			}
		};
		downloadFilebtn.addClickHandler(handler);
	}


	@Override
	public void setLocalizedReportDefinition(LocalizedReportDefinition definition, String templateDefName, UploadFileItem fileItem) {

		if(definition.getReportType() != null){
			reportTypeField.setValue(definition.getReportType().getClass().getName());
			String outputFileType = definition.getReportType().getOutputFileType();
			if(outputFileType != null){
				outputTypeField.setValue(outputFileType);
				// TODO 拡張子チェックを追加する
			}
			//出力ファイル形式
			getOutputFileType(reportTypeField.getValueAsString());
		}

		//ReportTypeごとの設定
		setReportType(definition.getReportType());

		//File入れ替え
		if (fileItem != null) {
			itmFileUpload.setFileUpload(fileItem.getEditFileUpload());
		}

		if (SmartGWTUtil.isEmpty(definition.getFileName())) {
			downloadForm.setVisible(false);
		} else {
			txtDownloadFileName.setValue(definition.getFileName());
			setTemplateDownloadAction(templateDefName, definition.getLocaleName());
			downloadForm.setVisible(true);
		}
	}

	@Override
	public LocalizedReportDefinition getEditLocalizedReportDefinition(LocalizedReportDefinition definition) {

		if (PoiReportType.class.getName().equals(reportTypeField.getValueAsString())) {
			//出力形式設定
			PoiReportType poiTemplate = new PoiReportType();
			poiTemplate.setOutputFileType(outputTypeField.getValueAsString());

			//POI用出力設定
			poiTemplate = reportOutPane.getEditDefinition(poiTemplate);
			//PasswordAttributeName
			poiTemplate.setPasswordAttributeName(SmartGWTUtil.getStringValue(poiPasswordAttributeNameField, true));

			definition.setReportType(poiTemplate);
		} else if (JasperReportType.class.getName().equals(reportTypeField.getValueAsString())) {

			JasperReportType jasperTemplate = new JasperReportType();
			jasperTemplate.setOutputFileType(outputTypeField.getValueAsString());

			//パラメータマッピング設定
			jasperTemplate.setParamMap(jasperParamMapPane.getParamMap());
			//DataSourceAttributeName
			jasperTemplate.setDataSourceAttributeName(SmartGWTUtil.getStringValue(jasperDataSourceAttributeNameField, true));
			//PasswordAttributeName
			jasperTemplate.setPasswordAttributeName(SmartGWTUtil.getStringValue(jasperPasswordAttributeNameField, true));

			definition.setReportType(jasperTemplate);
		} else if (JxlsReportType.class.getName().equals(reportTypeField.getValueAsString())) {
			JxlsReportType jxlsTemplate = new JxlsReportType();
			jxlsTemplate.setOutputFileType(outputTypeField.getValueAsString());

			//PasswordAttributeName
			jxlsTemplate.setPasswordAttributeName(SmartGWTUtil.getStringValue(jxlsPasswordAttributeNameField, true));
			//JXLS用出力設定
			jxlsTemplate = jxlsReportOutputLogicPane.getEditDefinition(jxlsTemplate);
			//ContextParamMapping
			jxlsTemplate.setParamMap(jxlsContextParamMapPane.getParamMap());

			definition.setReportType(jxlsTemplate);
		}

		return definition;
	}

	@Override
	public UploadFileItem getEditUploadFileItem() {
		return itmFileUpload;
	}

	@Override
	public int getLocaleDialogHeight() {
		return 600;
	}

	private class ReportTemplateDefinitionSubmitCompleteHandler extends UploadSubmitCompleteHandler {

		public ReportTemplateDefinitionSubmitCompleteHandler() {
			super(form);
		}

		@Override
		protected void onSuccess(UploadResultInfo result) {

			// FlowPanelに追加しているので戻す
			itmFileUpload.redrawFileUpload();

			if (callback != null) {
				String message = null;
				if (result.getMessages() != null && result.getMessages().size() > 0) {
					message = result.getMessages().get(0);
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
