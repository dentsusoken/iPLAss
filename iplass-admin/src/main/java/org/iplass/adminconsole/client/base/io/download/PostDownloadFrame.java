/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.base.io.download;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.NamedFrame;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;

/**
 * Postメソッドによるレスポンスダウンロード用フレーム
 *
 * @author lis70i
 *
 */
public class PostDownloadFrame extends NamedFrame {

	//TODO removeするタイミングがない
	//     onSubmitCompleteはTypeが「text/html」の場合のみ発生
	//     onLoadイベントでremoveしてしまうとResponseが取得できない

	/** frame名 **/
	private static final String FRAME_NAME = "__mtp_post_dummyFrame";

	/** 送信Form */
	private PostFormPanel form;

	public PostDownloadFrame() {
		//FormPanelのターゲットとなるようにFrameに名前を設定
		super(FRAME_NAME);

		setSize("0px", "0px");
		setVisible(false);
		setUrl("javascript:''");

		//送信用Formの生成
		form = new PostFormPanel(this);
		form.addSubmitHandler(new SubmitHandler() {
			@Override
			public void onSubmit(SubmitEvent event) {
				GWT.log("submit start. url=" + form.getAction());
			}
		});
		//onSubmitCompleteはResponseのtypeがtext/htmlの場合のみ発生
		form.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				GWT.log("submit complete. url=" + form.getAction());
			}
		});

		addLoadHandler(new LoadHandler() {
			@Override
			public void onLoad(LoadEvent event) {
				GWT.log("browser onload. name=" + FRAME_NAME);
				//このタイミングでremoveしてしまうとResponseが取得できない
//				RootPanel.get().remove(HiddenPostFrame.this);
			}
		});

		RootPanel.get().add(this);
	}

	/**
	 * 対象となるAction URL を設定します。
	 *
	 * @param url Action URL
	 * @return
	 */
	public PostDownloadFrame setAction(String url) {
		form.setAction(url);
		return this;
	}

	/**
	 * パラメータを追加します。
	 *
	 * @param name 名前
	 * @param value 値
	 * @return
	 */
	public PostDownloadFrame addParameter(String name, String value) {
		form.addParameter(name, value);
		return this;
	}

	/**
	 * submit処理を実行します。
	 */
	public void execute() {
		form.execute();
	}

	/**
	 * 送信用FormPanel
	 *
	 * @author lis70i
	 */
	private class PostFormPanel extends FormPanel {

		/** パラメータ保持用Panel */
		private FlowPanel paramPanel;

		public PostFormPanel(NamedFrame target) {
			//targetをこのPanelが含まれるFrameと別にすると、
			//FormSubmitComplete Eventが発生しない
			super(target);

			setSize("0px", "0px");
			setVisible(false);

			//hidden条件保持用Panel
			paramPanel = new FlowPanel();
			add(paramPanel);

			//メソッドはPOST
			setMethod(METHOD_POST);

			RootPanel.get().add(this);
		}

		/**
		 * パラメータを追加します。
		 *
		 * @param name 名前
		 * @param value 値
		 * @return
		 */
		public PostFormPanel addParameter(String name, String value) {
			Hidden hidden = new Hidden(name, value);
			paramPanel.add(hidden);
			return this;
		}

		/**
		 * submit処理を実行します。
		 */
		public void execute() {
			submit();
		}

	}

}
