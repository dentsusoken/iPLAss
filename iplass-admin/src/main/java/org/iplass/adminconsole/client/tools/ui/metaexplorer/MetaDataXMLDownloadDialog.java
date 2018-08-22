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

package org.iplass.adminconsole.client.tools.ui.metaexplorer;

import org.iplass.adminconsole.client.base.io.download.PostDownloadFrame;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.shared.base.dto.io.download.DownloadProperty.ENCODE;
import org.iplass.adminconsole.shared.base.dto.io.download.DownloadProperty.FILETYPE;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.ConfigDownloadProperty;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.TargetMode;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.layout.HLayout;

public class MetaDataXMLDownloadDialog extends AbstractWindow {

	public MetaDataXMLDownloadDialog(final String path, final String repositoryType) {
		this(new String[]{path}, repositoryType);
	}

	public MetaDataXMLDownloadDialog(final String[] paths, final String repositoryType) {

		setWidth(400);
		setHeight(130);
		setTitle("Export MetaData");
		setShowMinimizeButton(false);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();

		final SelectItem fileTypeField = new SelectItem();
		fileTypeField.setTitle("File Type");
		fileTypeField.setValueMap(FILETYPE.XML.name());
		fileTypeField.setValue(FILETYPE.XML.name());
		fileTypeField.setDisabled(true);	//XML固定

		final SelectItem encodeField = new SelectItem();
		encodeField.setTitle("Encode");
//		encodeField.setValueMap(ENCODE.UTF8.name(), ENCODE.MS932.name());
		encodeField.setValueMap(ENCODE.UTF8.name());
		encodeField.setValue(ENCODE.UTF8.name());
		encodeField.setDisabled(true);	//UTF8固定

		final DynamicForm form = new DynamicForm();
		form.setMargin(5);
		form.setHeight100();
		form.setWidth100();
		form.setItems(fileTypeField, encodeField);

		IButton download = new IButton("Export");
		download.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
//				GetRequestBuilder builder = new GetRequestBuilder()
//					.baseUrl(GWT.getModuleBaseURL())
//					.targetUrl("service/metaconfigdownload")
//					.parameterWithValue("tenantId", String.valueOf(TenantInfoHolder.getId()))
//					.parameterWithValue("paths", pathArrayString(paths))
//					.parameterWithValue("fileType", FILETYPE.valueOf(fileTypeField.getValueAsString()).name())
//					.parameterWithValue("encode", ENCODE.valueOf(encodeField.getValueAsString()).getValue())
//					.parameterWithValue("repoKind", repositoryKind);
//				new GetDownloadFrame(builder.toEncodedUrl());
				PostDownloadFrame frame = new PostDownloadFrame();
				frame.setAction(GWT.getModuleBaseURL() + ConfigDownloadProperty.ACTION_URL)
					.addParameter(ConfigDownloadProperty.TENANT_ID, String.valueOf(TenantInfoHolder.getId()))
					.addParameter(ConfigDownloadProperty.TARGET_MODE, TargetMode.LIVE.name())
					.addParameter(ConfigDownloadProperty.TARGET_PATH, pathArrayString(paths))
					.addParameter(ConfigDownloadProperty.FILE_TYPE, FILETYPE.valueOf(fileTypeField.getValueAsString()).name())
					.addParameter(ConfigDownloadProperty.ENCODE, ENCODE.valueOf(encodeField.getValueAsString()).getValue())
					.addParameter(ConfigDownloadProperty.REPOSITORY_TYPE, repositoryType)
					.execute();
				destroy();
			}
		});

		IButton cancel = new IButton("Cancel");
		cancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				destroy();
			}
		});

//		Label comment = new Label("<font color=\"red\">※XML形式の場合、関連する定義を全て出力します。</font>");
//		comment.setMargin(5);
//		comment.setHeight(20);

		HLayout footer = new HLayout(5);
		footer.setMargin(5);
		footer.setHeight(20);
		footer.setWidth100();
		//footer.setAlign(Alignment.LEFT);
		footer.setAlign(VerticalAlignment.CENTER);
		footer.setMembers(download, cancel);

		addItem(form);
//		addItem(comment);
		addItem(footer);
	}

	private String pathArrayString(String[] paths) {
		if (paths == null || paths.length == 0) {
			return "";
		}

		StringBuilder builder = new StringBuilder();
		for (String path : paths) {
			builder.append(path + ",");
		}
		builder.deleteCharAt(builder.length() - 1);
		return builder.toString();
	}
}
