/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.base.ui.widget;

import org.iplass.adminconsole.client.base.data.eql.EqlDS;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.base.dto.entity.EqlResultInfo;
import org.iplass.adminconsole.shared.base.rpc.entity.EqlServiceAsync;
import org.iplass.adminconsole.shared.base.rpc.entity.EqlServiceFactory;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.AutoFitWidthApproach;
import com.smartgwt.client.types.FetchMode;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

public class EQLExecutePane extends VLayout {

	private EqlServiceAsync service = EqlServiceFactory.get();

	private EQLResultTabSet resultTabSet;
	private MessageTabSet messageTabSet;

	public EQLExecutePane() {

		resultTabSet = new EQLResultTabSet();
		resultTabSet.setHeight("70%");
		resultTabSet.setShowResizeBar(true);		//リサイズ可能
		resultTabSet.setResizeBarTarget("next");	//リサイズバーをダブルクリックした際、下を収縮

		messageTabSet = new MessageTabSet();
		messageTabSet.setHeight("30%");

		addMember(resultTabSet);
		addMember(messageTabSet);
	}

	public void executeEQL(final String eql, final boolean isSearchAllVersion) {

		if (SmartGWTUtil.isEmpty(eql)) {
			//空なのでNG
			//SC.warn(AdminClientMessageUtil.getString("ui_tools_eql_EqlWorksheetMainPane_emptyEqlErr"));
			return;
		}

		startExecute();

		service.execute(TenantInfoHolder.getId(), eql, isSearchAllVersion, new AsyncCallback<EqlResultInfo>() {

			@Override
			public void onFailure(Throwable caught) {
				//SC.warn("EQLの実行でエラーが発生しました。<br/>" + caught.getMessage());
				EqlResultInfo result = new EqlResultInfo();
				result.setEql(eql);
				result.addLogMessage(AdminClientMessageUtil.getString("ui_tools_eql_EqlWorksheetMainPane_runEql") + eql);
				result.addLogMessage(AdminClientMessageUtil.getString("ui_tools_eql_EqlWorksheetMainPane_runEqlErr"));
				result.addLogMessage(caught.getMessage());
				result.setError(true);
				showResult(result);
			}

			@Override
			public void onSuccess(EqlResultInfo result) {
				showResult(result);
			}

		});

	}

	private void startExecute() {
		messageTabSet.clearMessage();
		resultTabSet.setTabTitleProgress();
		messageTabSet.setTabTitleProgress();
	}

	private void showResult(EqlResultInfo result) {

		resultTabSet.setTabTitleNormal();
		messageTabSet.setTabTitleNormal();

		if (result.isError()) {
			messageTabSet.setErrorMessage(result.getLogMessages());
		} else {
			messageTabSet.setMessage(result.getLogMessages());
		}
		resultTabSet.showResult(result);
	}

	private static class EQLResultTabSet extends TabSet {

		private static final String PROGRESS_ICON = "[SKINIMG]/shared/progressCursorTracker.gif";

		private Tab resultTab;
		private EQLResultPane resultPane;

		public EQLResultTabSet() {

			setTabBarPosition(Side.TOP);
			setWidth100();
			setHeight100();

			resultTab = new Tab();
			//resultTab.setTitle("Result");
			setTabTitleNormal();

			resultPane = new EQLResultPane();
			resultTab.setPane(resultPane);
			addTab(resultTab);
		}

		public void setTabTitleProgress() {
			resultTab.setTitle("<span>" + Canvas.imgHTML(PROGRESS_ICON) + "&nbsp;Execute...</span>");
		}

		public void setTabTitleNormal() {
			resultTab.setTitle("Result");
		}

		public void showResult(EqlResultInfo result) {
			resultPane.showResult(result);
		}

	}

	private static class EQLResultPane extends VLayout {

		private ListGrid grid;
		private EqlDS ds;

		public EQLResultPane() {
		}

		public void showResult(EqlResultInfo result) {

			if (result.isError()) {

				if (grid != null) {
					removeMember(grid);
					grid.destroy();
					grid = null;
					ds.destroy();
					ds = null;
				}
			} else {

				if (grid != null) {
					ds.destroy();
					ds = null;

					//DS再作成
					ds = EqlDS.getInstance(result);

					//行番号列が行数によりつぶれるので再設定
					int rownoSize = String.valueOf(result.getRecords().size()).length() * 11;
					grid.setRowNumberFieldProperties(new ListGridField("autorownumber", rownoSize));

					//グリッド表示
					grid.setDataSource(ds);
					grid.fetchData();

				} else {
					grid = new MtpListGrid();
					grid.setWidth100();
					grid.setHeight100();

					//データ件数が多い場合を考慮し、false
					grid.setShowAllRecords(false);
					//列数が多い場合を考慮し、false
					grid.setShowAllColumns(false);

					//列幅自動調節（タイトルに設定）
					grid.setAutoFitFieldWidths(true);
					grid.setAutoFitWidthApproach(AutoFitWidthApproach.TITLE);
					//幅が足りないときに先頭行を自動的に伸ばさない
					grid.setAutoFitFieldsFillViewport(false);

					//行番号表示
					grid.setShowRowNumbers(true);
					int rownoSize = String.valueOf(result.getRecords().size()).length() * 11;
					grid.setRowNumberFieldProperties(new ListGridField("autorownumber", rownoSize));

					grid.setDataFetchMode(FetchMode.PAGED);
					grid.setDataPageSize(10);

					ds = EqlDS.getInstance(result);

					grid.setDataSource(ds);
					grid.setAutoFetchData(true);

					addMember(grid);
				}
			}
		}

	}

}
