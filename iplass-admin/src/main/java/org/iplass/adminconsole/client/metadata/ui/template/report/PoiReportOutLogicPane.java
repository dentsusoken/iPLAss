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

package org.iplass.adminconsole.client.metadata.ui.template.report;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.web.template.report.definition.PoiReportType;
import org.iplass.mtp.web.template.report.definition.ReportOutputLogicDefinition;

import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * レポート出力ロジック
 *
 * @author lis71n
 *
 */
public class PoiReportOutLogicPane extends VLayout {

	private ReportOutLogicListGrid grid;

	public PoiReportOutLogicPane() {

		setMargin(5);

		HLayout captionComposit = new HLayout(5);
		captionComposit.setHeight(25);

		Label caption = new Label("Output Logic :");
		caption.setHeight(21);
		caption.setWrap(false);
		captionComposit.addMember(caption);

		Label captionHint = new Label();
		SmartGWTUtil.addHintToLabel(captionHint,
				"<style type=\"text/css\"><!--"
				+ "ul.notes{margin-top:5px;padding-left:15px;list-style-type:disc;}"
				+ "ul.notes li{padding:5px 0px;}"
				+ "ul.notes li span.strong {text-decoration:underline;color:red}"
				+ "ul.subnotes {margin-top:5px;padding-left:10px;list-style-type:circle;}"
				+ "--></style>"
				+ "<h3>Notes</h3>"
				+ "<ul class=\"notes\">"
				+ AdminClientMessageUtil.getString("ui_metadata_template_report_ReportOutLogicPane_captionHintComment1")
				+ AdminClientMessageUtil.getString("ui_metadata_template_report_ReportOutLogicPane_captionHintComment2")
				+ AdminClientMessageUtil.getString("ui_metadata_template_report_ReportOutLogicPane_captionHintComment3")
				+ "</ul>");
		captionComposit.addMember(captionHint);

		grid = new ReportOutLogicListGrid();
		grid.setScriptHint("ui_metadata_template_report_ReportOutLogicListGrid_scriptHint");
		grid.setJavaClassNameItemComment("ui_metadata_template_report_ReportOutLogicListGrid_javaClassNameItemComment");

		// 追加ボタン
		IButton addEventListener = new IButton("Add");
		addEventListener.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				grid.startReportOutputLogicEdit(true, new ReportOutLogicListGridRecord());
			}
		});
		// 削除ボタン
		IButton delEventListener = new IButton("Remove");
		delEventListener.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				grid.removeSelectedData();
			}
		});

		// EventListenerボタン用レイアウト
		HLayout elButtonPane = new HLayout(5);
		elButtonPane.setMargin(5);
		elButtonPane.addMember(addEventListener);
		elButtonPane.addMember(delEventListener);

		addMember(captionComposit);
		// PaneにScript用TextAreaとチェックボックス用Layoutをadd
		addMember(grid);
		addMember(elButtonPane);
	}

	public void setDefinition(PoiReportType definition) {
		grid.setDefinition(definition);
	}

	public PoiReportType getEditDefinition(PoiReportType definition) {
		grid.getEditDefinition(definition);
		return definition;
	}

	public ReportOutputLogicDefinition getReportOutputLogicDefinition() {
		return grid.getReportOutputLogicDefinition();
	}

	public void deleteAll() {
		ListGridRecord[] records = grid.getRecords();
		if (!(records == null || records.length == 0)) {
			for(ListGridRecord record : records){
				grid.removeData(record);
			}
		}
	}

	/**
	 * 入力チェックを実行します。
	 *
	 * @return 入力チェック結果
	 */
	public boolean validate() {
		return true;
	}

	/**
	 * エラー表示をクリアします。
	 */
	public void clearErrors() {
	}

}
