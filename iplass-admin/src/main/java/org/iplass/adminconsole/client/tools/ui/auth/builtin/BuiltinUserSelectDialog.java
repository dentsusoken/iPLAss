/*
 * Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.tools.ui.auth.builtin;

import java.util.LinkedHashMap;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.ui.widget.MtpWidgetConstants;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.tools.data.auth.builtin.SelectBuiltinUserDS;
import org.iplass.adminconsole.shared.tools.dto.auth.builtin.BuiltinAuthUserDto;
import org.iplass.adminconsole.shared.tools.dto.auth.builtin.BuiltinAuthUserListResultDto;
import org.iplass.adminconsole.shared.tools.dto.auth.builtin.BuiltinAuthUserSearchConditionDto;
import org.iplass.adminconsole.shared.tools.dto.auth.builtin.BuiltinAuthUserSearchType;

import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.types.AutoFitWidthApproach;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.DataArrivedEvent;
import com.smartgwt.client.widgets.grid.events.DataArrivedHandler;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class BuiltinUserSelectDialog extends AbstractWindow {

	private static final String PROGRESS_ICON = "[SKINIMG]/shared/progressCursorTracker.gif";

	private BuiltinUserSelectedHandler handler;

	private CriteriaTab criteriaTab;
	private ResultListPane resultPane;

	public BuiltinUserSelectDialog(BuiltinUserSelectedHandler handler) {
		this.handler = handler;

		setWidth(700);
		setMinWidth(500);
		setHeight(600);
		setMinHeight(600);
		setTitle("Select User");
		setCanDragResize(true);
		setShowMinimizeButton(false);
		setShowMaximizeButton(true);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();

		criteriaTab = new CriteriaTab();
		criteriaTab.setHeight(150);

		resultPane = new ResultListPane();

		VLayout mainLayout = new VLayout();
		mainLayout.setWidth100();
		mainLayout.setHeight100();
		mainLayout.setMargin(5);

		mainLayout.addMember(criteriaTab);
		mainLayout.addMember(resultPane);

		addItem(mainLayout);

		initialize();
	}

	private void initialize() {
	}

	private void startExecute() {
		if (criteriaTab != null) {
			criteriaTab.setTabTitleProgress();
		}
	}

	private void finishExecute() {
		if (criteriaTab != null) {
			criteriaTab.setTabTitleNormal();
		}
	}

	private void cancel() {
		destroy();
	}

	private class CriteriaTab extends TabSet {

		private Tab attributeTab;

		public CriteriaTab() {
			setTabBarPosition(Side.TOP);
			setWidth100();
			setHeight100();

			attributeTab = new Tab();
			attributeTab.setTitle("User Search");

			UserAttributeCriteriaPane attributePane = new UserAttributeCriteriaPane();
			attributeTab.setPane(attributePane);

			addTab(attributeTab);
		}

		public void setTabTitleProgress() {
			attributeTab.setTitle("<span>" + Canvas.imgHTML(PROGRESS_ICON) + "&nbsp;Execute...</span>");
		}
		public void setTabTitleNormal() {
			attributeTab.setTitle("User Search");
		}
	}

	/**
	 * ユーザー属性での検索
	 */
	private class UserAttributeCriteriaPane extends VLayout {

		private TextItem accountIdItem;
		private TextItem nameItem;
		private TextItem mailItem;

		public UserAttributeCriteriaPane() {
			setWidth100();
			setHeight100();
			setMembersMargin(5);

			//------------------------
			//標準項目
			//------------------------
			DynamicForm standardForm = new DynamicForm();
			standardForm.setWidth100();
			standardForm.setHeight100();
			standardForm.setNumCols(3);
			standardForm.setColWidths(100, 120, "*");
			standardForm.setAutoFocus(true);

			accountIdItem = new TextItem("accountId", "Account ID");
			accountIdItem.setColSpan(3);
			accountIdItem.setWidth(300);
			nameItem = new TextItem("name", "Name");
			nameItem.setColSpan(3);
			nameItem.setWidth(300);
			SmartGWTUtil.addHoverToFormItem(nameItem,
					AdminClientMessageUtil.getString("ui_tools_auth_builtin_BuiltinAuthExplorerMainPane_nameItemComment"));
			mailItem = new TextItem("mail", "Mail");
			mailItem.setColSpan(3);
			mailItem.setWidth(300);

			standardForm.setFields(accountIdItem, nameItem, mailItem);

			//------------------------
			//フッター
			//------------------------
			HLayout footer = new HLayout();
			footer.setHeight(30);
			footer.setMembersMargin(10);

			IButton searchButton = new IButton("Search");
			searchButton.setIcon(MtpWidgetConstants.ICON_SEARCH);
			searchButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					searchData();
				}
			});

			IButton cancel = new IButton("Cancel");
			cancel.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					cancel();
				}
			});

			LayoutSpacer footerSpace = new LayoutSpacer();
			footerSpace.setWidth(100);
			footer.addMember(footerSpace);
			footer.addMember(searchButton);
			footer.addMember(cancel);

			//------------------------
			//全体
			//------------------------
			addMember(standardForm);
			addMember(footer);

			initialize();
		}

		private void initialize() {
		}

		private void searchData() {
			resultPane.setPageNum(1);

			BuiltinAuthUserSearchConditionDto condition = createCondition();

			resultPane.doFetch(condition);
		}

		private BuiltinAuthUserSearchConditionDto createCondition() {
			BuiltinAuthUserSearchConditionDto cond = new BuiltinAuthUserSearchConditionDto();
			cond.setSearchType(BuiltinAuthUserSearchType.ATTRIBUTE);

			cond.setAccountId(SmartGWTUtil.getStringValue(accountIdItem));
			cond.setName(SmartGWTUtil.getStringValue(nameItem));
			cond.setMail(SmartGWTUtil.getStringValue(mailItem));

			return cond;
		}
	}

	private class ResultListPane extends VLayout {

		private static final int LIMIT = 30;

		private SelectItem pageNum;
		private int maxPageNum;
		private Label countLabel;

		private ListGrid grid;

		private BuiltinAuthUserSearchConditionDto curCondition;

		private SelectBuiltinUserDS ds;

		public ResultListPane() {
			setWidth100();
			setHeight100();

			//========================
			//Toolbar
			//========================
			ToolStrip toolStrip = new ToolStrip();
			toolStrip.setWidth100();
			toolStrip.setMembersMargin(5);
			toolStrip.setAlign(VerticalAlignment.BOTTOM);

			toolStrip.addFill();

			//------------------------
			//Paging
			//------------------------
			final ToolStripButton prevButton = new ToolStripButton();
			prevButton.setIcon("resultset_previous.png");
			prevButton.setTitle("Prev");
			prevButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					doPrev();
				}
			});
			toolStrip.addButton(prevButton);

			final ToolStripButton nextButton = new ToolStripButton();
			nextButton.setIcon("resultset_next.png");
			nextButton.setTitle("Next");
			nextButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					doNext();
				}
			});
			toolStrip.addButton(nextButton);

			toolStrip.addSeparator();

			pageNum = new SelectItem();
			pageNum.setShowTitle(false);
			pageNum.setWidth(80);
			pageNum.setValueMap("1");
			pageNum.setDefaultValue("1");
			pageNum.addChangedHandler(new ChangedHandler() {

				@Override
				public void onChanged(ChangedEvent event) {
					changePage();
				}
			});
			toolStrip.addFormItem(pageNum);

			Label allPageLabel = new Label();
			allPageLabel.setWrap(false);
			allPageLabel.setAutoWidth();
			allPageLabel.setContents("Page");
			toolStrip.addMember(allPageLabel);

			toolStrip.addSeparator();

			countLabel = new Label();
			countLabel.setWrap(false);
			countLabel.setAutoWidth();
			createPagePane(0, 0);
			toolStrip.addMember(countLabel);

			toolStrip.addSpacer(5);

			//========================
			//Grid
			//========================
			grid = new ListGrid();

			grid.setWidth100();
			grid.setHeight100();
			grid.setShowAllRecords(false);		//データ件数が多い場合を考慮し、false
			grid.setLeaveScrollbarGap(false);	//falseで縦スクロールバー領域が自動表示制御される

			grid.setAutoFitFieldWidths(true);							//データにより幅自動調節
			grid.setAutoFitWidthApproach(AutoFitWidthApproach.BOTH);	//幅の調節をタイトルとデータに設定
//			grid.setAutoFitWidthApproach(AutoFitWidthApproach.TITLE);	//幅の調節をタイトルとデータに設定
			grid.setAutoFitFieldsFillViewport(false);					//幅が足りないときに先頭行の自動的に伸ばさない

			grid.setShowRowNumbers(true);		//行番号表示
			grid.setCanDragSelectText(true);	//セルの値をドラッグで選択可能（コピー用）にする

			grid.setCanSort(false);
			grid.setCanGroupBy(false);
			grid.setCanPickFields(false);
			grid.setCanFreezeFields(false);

			grid.setSelectionType(SelectionStyle.SINGLE);	//単一選択

			grid.addDataArrivedHandler(new DataArrivedHandler() {

				@Override
				public void onDataArrived(DataArrivedEvent event) {
					showResultInfo(ds.getResult());
				}
			});

			grid.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {

				@Override
				public void onRecordDoubleClick(RecordDoubleClickEvent event) {
					BuiltinAuthUserDto user = (BuiltinAuthUserDto)((ListGridRecord)event.getRecord()).getAttributeAsObject(SelectBuiltinUserDS.VALUE_OBJECT);
					handler.selected(user);
					cancel();
				}
			});

			//明示的にFetchする
			grid.setAutoFetchData(false);

			//========================
			//ALL
			//========================
			addMember(toolStrip);
			addMember(grid);

			initialize();
		}

		private void initialize() {
			ds = SelectBuiltinUserDS.setDataSource(grid);
		}

		public void setPageNum(int page) {
			pageNum.setValue(page + "");
		}

		public void doFetch(BuiltinAuthUserSearchConditionDto condition) {
			startExecute();

			//条件の作成
			BuiltinAuthUserSearchConditionDto newCondition = null;
			if (condition == null && curCondition != null) {
				newCondition = curCondition;
			} else {
				newCondition = condition;
			}

			//limit情報は最新にする
			setLimit(newCondition);

			//DSに条件をセット
			ds.setCondition(newCondition);

			Criteria criteria = new Criteria();
			criteria.addCriteria("dummy", System.currentTimeMillis() + "");	//同じ条件だとDSに飛ばないので

			//fetch
			grid.fetchData(criteria);

			//条件の保持
			curCondition = newCondition;
		}

		private void doPrev() {
			int page = getPageNum();
			if (page == 1) {
				return;
			}
			setPageNum(page - 1);

			doFetch(null);
		}

		private void doNext() {
			int page = getPageNum();
			int maxPage = getMaxPageNum();

			if (page == maxPage) {
				return;
			}
			setPageNum(page + 1);

			doFetch(null);
		}

		private void changePage() {

			doFetch(null);
		}

		private int getPageNum() {
			return Integer.parseInt(SmartGWTUtil.getStringValue(pageNum));
		}

		private int getMaxPageNum() {
			return maxPageNum;
		}

		private int getOffset() {
			return (getPageNum() - 1) * LIMIT ;
		}

		private void setLimit(BuiltinAuthUserSearchConditionDto condition) {
			if (condition != null) {
				condition.setLimit(LIMIT);
				condition.setOffset(getOffset());
			}
		}

		private void showResultInfo(BuiltinAuthUserListResultDto result) {

			int totalCount = 0;
			int offset = 0;
			if (result == null) {
			} else if (result.isError()) {
				SC.warn(result.getLogMessages().get(0));	//とりあえず先頭を表示
			} else {
				totalCount = result.getTotalCount();
				offset = result.getExecuteOffset();	//実行時に変更される可能性がある
			}

			createPagePane(totalCount, offset);

			finishExecute();
		}

		private void createPagePane(int totalCount, int offset) {

			//Maxページ番号計算
			int maxPage = totalCount / LIMIT;
			if (totalCount % LIMIT > 0) {
				maxPage++;
			}
			if (maxPage == 0) {
				maxPage = 1;
			}
			maxPageNum = maxPage;

			LinkedHashMap<String, String> pageMap = new LinkedHashMap<String, String>();
			for (int i = 1; i <= maxPage; i++) {
				pageMap.put(i + "", i + "");
			}
			pageNum.setValueMap(pageMap);

			//Page番号のセット
			int page = (offset / LIMIT) + 1;
			pageNum.setValue(page);

			//TotalCount更新
			countLabel.setContents("Total Count：" + totalCount);
		}
	}

	public interface BuiltinUserSelectedHandler {
		public void selected(BuiltinAuthUserDto user);
	}
}
