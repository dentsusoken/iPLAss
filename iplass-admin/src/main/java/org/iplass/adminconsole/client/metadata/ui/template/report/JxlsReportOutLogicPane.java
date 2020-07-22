package org.iplass.adminconsole.client.metadata.ui.template.report;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.web.template.report.definition.JxlsReportType;

import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class JxlsReportOutLogicPane  extends VLayout {

	private ReportOutLogicListGrid grid;
	
	public JxlsReportOutLogicPane() {

		setMargin(5);

		HLayout captionComposit = new HLayout(5);
		captionComposit.setHeight(25);

		Label caption = new Label("Custom Output Logic :");
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
				+ AdminClientMessageUtil.getString("ui_metadata_template_report_JxlsReportOutLogicPane_captionHintComment1")
				+ AdminClientMessageUtil.getString("ui_metadata_template_report_JxlsReportOutLogicPane_captionHintComment2")
				+ AdminClientMessageUtil.getString("ui_metadata_template_report_JxlsReportOutLogicPane_captionHintComment3")
				+ "</ul>");
		captionComposit.addMember(captionHint);

		grid = new ReportOutLogicListGrid();
		grid.setScriptHint("ui_metadata_template_report_JxlsReportOutLogicListGrid_scriptHint");
		grid.setJavaClassNameItemComment("ui_metadata_template_report_JxlsReportOutLogicListGrid_javaClassNameItemComment");

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

	public JxlsReportType getEditDefinition(JxlsReportType definition) {
		grid.getEditDefinition(definition);
		return definition;
	}

	public void setDefinition(JxlsReportType definition) {
		grid.setDefinition(definition);
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
