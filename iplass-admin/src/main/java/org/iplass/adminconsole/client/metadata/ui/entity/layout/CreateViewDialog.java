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

package org.iplass.adminconsole.client.metadata.ui.entity.layout;

import org.iplass.adminconsole.client.base.event.MTPEvent;
import org.iplass.adminconsole.client.base.event.MTPEventHandler;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.mtp.view.generic.EntityView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * View追加用ダイアログ
 * @author lis3wg
 *
 */
public class CreateViewDialog extends AbstractWindow {
	/** 名前入力用アイテム */
	private TextItem nameItem;
	private DynamicForm form;

	/** OKボタン押下後の処理 */
	private MTPEventHandler okClickHandler;

	/**
	 * コンストラクタ
	 * @param service
	 * @param defName
	 * @param type
	 */
	public CreateViewDialog(final MetaDataServiceAsync service, final String defName) {

		setTitle(AdminClientMessageUtil.getString("ui_metadata_entity_layout_CreateViewDialog_createNewView"));

		setWidth(450);
		setHeight(150);

		setShowMinimizeButton(false);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();

		nameItem = new TextItem("name", "View Name");
		SmartGWTUtil.setRequired(nameItem);

		form = new DynamicForm();
		form.setPadding(5);
		form.setWidth100();
		form.setNumCols(2);
		form.setColWidths(100, "*");
		form.setAutoFocus(true);
		form.setItems(nameItem);

		//Mainコンテンツ
		VLayout contents = new VLayout(5);
		contents.setPadding(10);
		contents.setMembers(form);

		//Footer
		HLayout footer = new HLayout(5);
		footer.setMargin(10);
		footer.setAutoHeight();
		footer.setWidth100();
		footer.setAlign(VerticalAlignment.CENTER);

		IButton ok = new IButton("OK");
		ok.addClickHandler(new OkClickHandler(service, defName));
		IButton cancel = new IButton("Cancel");
		cancel.addClickHandler(new CancelClickHandler());

		footer.setMembers(ok, cancel);

		addItem(contents);
		addItem(SmartGWTUtil.separator());
		addItem(footer);

	}

	/**
	 * OKボタンイベント
	 */
	private final class OkClickHandler implements ClickHandler {
		private final MetaDataServiceAsync service;
		private final String defName;

		private OkClickHandler(MetaDataServiceAsync service, String defName) {
			this.service = service;
			this.defName = defName;
		}

		public void onClick(ClickEvent event) {

			if (!form.validate()) {
				return;
			}

			//View定義を取得
			service.getDefinition(TenantInfoHolder.getId(), EntityView.class.getName(), defName, new AsyncCallback<EntityView>() {

				@Override
				public void onSuccess(EntityView ev) {
					//作成済みのView定義名取得
					String name = nameItem.getValue().toString();

					//プルダウンに追加、処理は画面の方に作成
					if (okClickHandler != null) {
						MTPEvent event = new MTPEvent();
						event.setValue("name", name);
						event.setValue("entityView", ev);
						okClickHandler.execute(event);
					}

					destroy();
				}

				@Override
				public void onFailure(Throwable caught) {
					SC.say("失敗", "画面情報の読込に失敗しました。");

					GWT.log(caught.toString(), caught);
				}
			});
		}
	}

	/**
	 * Cancelボタンイベント
	 */
	private final class CancelClickHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			destroy();
		}
	}

	/**
	 * OKボタン押下後の処理を設定
	 * @param handler
	 */
	public void setOkClickHandler(MTPEventHandler handler) {
		this.okClickHandler = handler;
	}
}

