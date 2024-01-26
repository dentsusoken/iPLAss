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

package org.iplass.adminconsole.client.base.io.upload;

import org.iplass.adminconsole.client.base.util.SmartGWTUtil;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FileUpload;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;


public class UploadFileItem extends VLayout {

	private Label title;

	private HLayout pnlFileUpload;
	private FileUpload fileUpload;
	private HandlerRegistration fileUploadChangeHandler;

	private HLayout pnlFileClear;

	public UploadFileItem() {

		setMembersMargin(5);
		setWidth100();
		setAutoHeight();

		HLayout pnlFileSelect = new HLayout();
		pnlFileSelect.setHeight(25);
		pnlFileSelect.setWidth100();
		pnlFileSelect.setMembersMargin(5);

		title = new Label("<b>File :</b>");
		title.setStyleName("formTitle");
		title.setWrap(false);
		title.setAlign(Alignment.RIGHT);

		pnlFileUpload = new HLayout();
		pnlFileUpload.setHeight100();
		pnlFileUpload.setAutoWidth();

		fileUpload = new FileUpload();
		fileUpload.setName("fileUpload");

		pnlFileUpload.addMember(fileUpload);

		pnlFileSelect.addMember(title);
		pnlFileSelect.addMember(pnlFileUpload);

		pnlFileClear = new HLayout();
		pnlFileClear.setHeight(25);
		pnlFileClear.setWidth100();
		pnlFileClear.setMembersMargin(5);
		pnlFileClear.setVisible(false);

		Label dummyTitle = new Label("");
		dummyTitle.setStyleName("formTitle");

		Button btnClear = new Button("Clear");
		btnClear.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				clearFile();
			}
		});

		pnlFileClear.addMember(dummyTitle);
		pnlFileClear.addMember(btnClear);

		addMember(pnlFileSelect);
		addMember(pnlFileClear);

		setFileChangeEventHandler();
	}

	/**
	 * <p>
	 * FileUploadを返します。
	 * </p>
	 *
	 * @return FileUpload
	 */
	public FileUpload getEditFileUpload() {

		//念のためEventHandlerを削除
		removeFileChangeEventHandler();

		return fileUpload;
	}

	/**
	 * <p>
	 * 多言語設定などのダイアログ編集で一度閉じたりする場合、 以前に選択状態にあったFileUploadに置き換えます。
	 * </p>
	 *
	 * @param fileUpload
	 *            FileUpload
	 */
	public void setFileUpload(FileUpload fileUpload) {

		this.fileUpload = fileUpload;

		setFileChangeEventHandler();

		redrawFileUpload();
	}

	/**
	 * <p>
	 * サーバにSubmitする際にFlowPanelにaddされた状態になるので、 それを元に戻す際に呼び出します。
	 * </p>
	 */
	public void redrawFileUpload() {

		// fileUploadはAddした際にWidgetCanvasでラップされるのでそのWidgetCanvasを消す
		for (Canvas member : pnlFileUpload.getMembers()) {
			pnlFileUpload.removeMember(member);
		}
		// 送信時にFlowPanelにaddされるので、再度Addし直す
		if (fileUpload.getParent() != null) {
			fileUpload.removeFromParent();
		}
		pnlFileUpload.addMember(fileUpload);

		changeClearButtonVisible();
	}

	public void clearFile() {
		if (!SmartGWTUtil.isEmpty(fileUpload.getFilename())) {
			//FileUploadを再作成
			FileUpload reCreate = new FileUpload();
			reCreate.setName("fileUpload");
			setFileUpload(reCreate);
			pnlFileClear.setVisible(false);
		}
	}

	private void setFileChangeEventHandler() {

		removeFileChangeEventHandler();

		fileUploadChangeHandler = fileUpload.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				changeClearButtonVisible();
			}
		});
	}

	private void removeFileChangeEventHandler() {
		if (fileUploadChangeHandler != null) {
			fileUploadChangeHandler.removeHandler();
			fileUploadChangeHandler = null;
		}
	}

	private void changeClearButtonVisible() {
		if (SmartGWTUtil.isEmpty(fileUpload.getFilename())) {
			pnlFileClear.setVisible(false);
		} else {
			pnlFileClear.setVisible(true);
		}
	}
}
