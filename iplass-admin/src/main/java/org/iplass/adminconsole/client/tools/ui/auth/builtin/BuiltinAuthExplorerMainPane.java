/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.io.download.PostDownloadFrame;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.MessageTabSet;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataSelectItem;
import org.iplass.adminconsole.client.base.ui.widget.MtpListGrid;
import org.iplass.adminconsole.client.base.ui.widget.MtpWidgetConstants;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.tools.data.auth.builtin.BuiltinUserDS;
import org.iplass.adminconsole.shared.tools.dto.auth.builtin.BuiltinAuthUserDto;
import org.iplass.adminconsole.shared.tools.dto.auth.builtin.BuiltinAuthUserListResultDto;
import org.iplass.adminconsole.shared.tools.dto.auth.builtin.BuiltinAuthUserSearchConditionDto;
import org.iplass.adminconsole.shared.tools.dto.auth.builtin.BuiltinAuthUserSearchOperator;
import org.iplass.adminconsole.shared.tools.dto.auth.builtin.BuiltinAuthUserSearchType;
import org.iplass.adminconsole.shared.tools.dto.auth.builtin.BuiltinAuthUserSpecificType;
import org.iplass.mtp.auth.policy.definition.AuthenticationPolicyDefinition;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.AutoFitWidthApproach;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.SliderItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.DataArrivedEvent;
import com.smartgwt.client.widgets.grid.events.DataArrivedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class BuiltinAuthExplorerMainPane extends VLayout {

	private static final String EXPORT_ICON = "[SKIN]/actions/download.png";
	private static final String RESET_ERROR_COUNT_ICON = "icon_edit.png";

	private ResultListPane resultPane;

	/** message panel */
	private MessageTabSet messageTabSet;

	public BuiltinAuthExplorerMainPane() {
		setWidth100();

		CriteriaTab criteriaTab = new CriteriaTab();
		criteriaTab.setHeight(250);
		criteriaTab.setShowResizeBar(true);		//リサイズ可能

		resultPane = new ResultListPane();
		resultPane.setShowResizeBar(true);		//リサイズ可能
		resultPane.setResizeBarTarget("next");	//リサイズバーをダブルクリックした際、下を収縮

		messageTabSet = new MessageTabSet();
		messageTabSet.setHeight(120);

		addMember(criteriaTab);
		addMember(resultPane);
		addMember(messageTabSet);

		initialize();
	}

	private void initialize() {
	}

	private void startExecute(String message) {
		if (messageTabSet != null) {
			messageTabSet.clearMessage();
			messageTabSet.setTabTitleProgress();
		}
	}

	private void finishExecute() {
		if (messageTabSet != null) {
			messageTabSet.setTabTitleNormal();
		}
	}

	private class CriteriaTab extends TabSet {

		public CriteriaTab() {
			setTabBarPosition(Side.TOP);
			setWidth100();
			setHeight100();

			Tab specificTab = new Tab();
			specificTab.setTitle("Specific Search");

			SpecificCriteriaPane specificPane = new SpecificCriteriaPane();
			specificTab.setPane(specificPane);

			Tab attributeTab = new Tab();
			attributeTab.setTitle("User Attribute Search");

			UserAttributeCriteriaPane attributePane = new UserAttributeCriteriaPane();
			attributeTab.setPane(attributePane);

			addTab(specificTab);
			addTab(attributeTab);
		}
	}

	/**
	 * 特殊検索
	 */
	private class SpecificCriteriaPane extends VLayout {

		private RadioGroupItem specificTypeItem;

		private VLayout condPane;
		private DynamicForm lockedUserForm;
		private DynamicForm remainPWDaysForm;
		private DynamicForm lastLoginForm;

		private SelectItem lockedUserPolicyNameField;

		private SelectItem remainDaysPolicyNameField;
		private SelectItem remainDaysOperatorItem;
		private SliderItem remainDaysValueItem;

		private DateItem lastLoginFromItem;
		private DateItem lastLoginToItem;

		public SpecificCriteriaPane() {
			setWidth100();
			setHeight100();
			setMembersMargin(5);

			condPane = new VLayout();
			condPane.setWidth100();
			condPane.setAutoHeight();

			//------------------------
			//タイプ選択部
			//------------------------
			DynamicForm specificTypeForm = new DynamicForm();
			specificTypeForm.setWidth100();
			specificTypeForm.setHeight100();
			specificTypeForm.setNumCols(3);
			specificTypeForm.setColWidths(100, 200, "*");
			specificTypeForm.setAutoFocus(true);

			LinkedHashMap<String, String> specificTypeValues = new LinkedHashMap<String, String>();
			specificTypeValues.put(BuiltinAuthUserSpecificType.LOCKED.name(), BuiltinAuthUserSpecificType.LOCKED.displayName());
			specificTypeValues.put(BuiltinAuthUserSpecificType.PASSWORDDAYS.name(), BuiltinAuthUserSpecificType.PASSWORDDAYS.displayName());
			specificTypeValues.put(BuiltinAuthUserSpecificType.LASTLOGIN.name(), BuiltinAuthUserSpecificType.LASTLOGIN.displayName());
			specificTypeItem = new RadioGroupItem();
			specificTypeItem.setTitle("Condition");
			specificTypeItem.setValueMap(specificTypeValues);
			specificTypeItem.setValue(BuiltinAuthUserSpecificType.LOCKED.name());
			specificTypeItem.addChangedHandler(new ChangedHandler() {

				@Override
				public void onChanged(ChangedEvent event) {
					typeChanged();
				}
			});
			SmartGWTUtil.addHintToFormItem(specificTypeItem,
					"<br/>"
					+ "<div><b>・" + BuiltinAuthUserSpecificType.LOCKED.displayName() + "</b><br/>"
					+ AdminClientMessageUtil.getString("ui_tools_auth_builtin_BuiltinAuthExplorerMainPane_speItemComment1")
					+ AdminClientMessageUtil.getString("ui_tools_auth_builtin_BuiltinAuthExplorerMainPane_speItemComment2")
					+ "</div>"
					+ "<div><b>・" + BuiltinAuthUserSpecificType.PASSWORDDAYS.displayName() + "</b><br/>"
					+ AdminClientMessageUtil.getString("ui_tools_auth_builtin_BuiltinAuthExplorerMainPane_speItemComment3")
					+ AdminClientMessageUtil.getString("ui_tools_auth_builtin_BuiltinAuthExplorerMainPane_speItemComment4")
					+ "</div>"
					+ "<div><b>・" + BuiltinAuthUserSpecificType.LASTLOGIN.displayName() + "</b><br/>"
					+ AdminClientMessageUtil.getString("ui_tools_auth_builtin_BuiltinAuthExplorerMainPane_speItemComment5")
					+ "</div>"
					);
			specificTypeItem.setIconVAlign(VerticalAlignment.TOP);	//効かない・・

			specificTypeForm.setFields(specificTypeItem);

			//------------------------
			//ロックユーザー
			//------------------------
			lockedUserForm = new DynamicForm();
			lockedUserForm.setWidth100();
			lockedUserForm.setHeight100();
			lockedUserForm.setNumCols(3);
			lockedUserForm.setColWidths(100, 400, "*");

			lockedUserPolicyNameField = new MetaDataSelectItem(AuthenticationPolicyDefinition.class, "Policy Name");
			lockedUserPolicyNameField.setValue("DEFAULT");

			lockedUserForm.setFields(lockedUserPolicyNameField);

			//------------------------
			//パスワード残日数
			//------------------------
			remainPWDaysForm = new DynamicForm();
			remainPWDaysForm.setWidth100();
			remainPWDaysForm.setHeight100();
			remainPWDaysForm.setNumCols(4);
			remainPWDaysForm.setColWidths(100, 70, 400, "*");

			remainDaysPolicyNameField = new MetaDataSelectItem(AuthenticationPolicyDefinition.class, "Policy Name");
			remainDaysPolicyNameField.setColSpan(3);
			remainDaysPolicyNameField.setValue("DEFAULT");

			LinkedHashMap<String, String> remainDaysOparatorValues = new LinkedHashMap<String, String>();
			remainDaysOparatorValues.put(BuiltinAuthUserSearchOperator.LESSTHAN.name(), BuiltinAuthUserSearchOperator.LESSTHAN.displayName());
			remainDaysOparatorValues.put(BuiltinAuthUserSearchOperator.EQUAL.name(), BuiltinAuthUserSearchOperator.EQUAL.displayName());
			remainDaysOperatorItem = new SelectItem();
			remainDaysOperatorItem.setStartRow(true);
			remainDaysOperatorItem.setTitle("Remain days");
			remainDaysOperatorItem.setWidth(50);
			remainDaysOperatorItem.setValueMap(remainDaysOparatorValues);
			remainDaysOperatorItem.setValue(BuiltinAuthUserSearchOperator.LESSTHAN.name());

			remainDaysValueItem = new SliderItem();
			remainDaysValueItem.setShowTitle(false);
			remainDaysValueItem.setWidth(350);
			remainDaysValueItem.setHeight(30);	//これを指定しないと間延び
			remainDaysValueItem.setMinValue(0d);
			remainDaysValueItem.setMaxValue(20d);
			remainDaysValueItem.setValue(5);	//default 5 days

			remainPWDaysForm.setFields(remainDaysPolicyNameField, remainDaysOperatorItem, remainDaysValueItem);

			//------------------------
			//ラストログイン日
			//------------------------
			lastLoginForm = new DynamicForm();
			lastLoginForm.setWidth100();
			lastLoginForm.setHeight100();
			lastLoginForm.setNumCols(5);
			lastLoginForm.setColWidths(100, 120, 30, 220, "*");

//			lastLoginFromItem = new DateItem();
//			lastLoginFromItem.setTitle("Last Login Date");
//			lastLoginFromItem.setWidth(100);
//			lastLoginFromItem.setStartRow(true);
//			lastLoginFromItem.setUseTextField(true);
//			lastLoginFromItem.setDateFormatter(DateDisplayFormat.TOJAPANSHORTDATE);
			lastLoginFromItem = SmartGWTUtil.createDateItem();
			lastLoginFromItem.setTitle("Last Login Date");
			lastLoginFromItem.setStartRow(true);

			StaticTextItem lastLoginDummy = new StaticTextItem();
			lastLoginDummy.setShowTitle(false);
			lastLoginDummy.setWidth(30);
			lastLoginDummy.setAlign(Alignment.CENTER);
			lastLoginDummy.setValue("-");

//			lastLoginToItem = new DateItem();
//			lastLoginToItem.setShowTitle(false);
//			lastLoginToItem.setWidth(100);
//			lastLoginToItem.setUseTextField(true);
//			lastLoginToItem.setDateFormatter(DateDisplayFormat.TOJAPANSHORTDATE);
//			lastLoginToItem.setHint("<nobr>(yyyy/MM/dd)</nobr>");
			lastLoginToItem = SmartGWTUtil.createDateItem();
			lastLoginToItem.setShowTitle(false);

			lastLoginForm.setFields(lastLoginFromItem, lastLoginDummy, lastLoginToItem);

			condPane.addMember(specificTypeForm);	//起動時はタイプのみ


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
			IButton exportCSVButton = new IButton("Export CSV");
			exportCSVButton.setIcon(EXPORT_ICON);
			exportCSVButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					exportCsv();
				}
			});

			LayoutSpacer footerSpace = new LayoutSpacer();
			footerSpace.setWidth(100);
			footer.addMember(footerSpace);
			footer.addMember(searchButton);
			footer.addMember(exportCSVButton);

			//------------------------
			//下余白
			//------------------------
			LayoutSpacer condSpace = new LayoutSpacer();
			condSpace.setWidth100();
			condSpace.setHeight100();

			//------------------------
			//全体
			//------------------------
			addMember(condPane);
			addMember(footer);
			addMember(condSpace);

			initialize();
		}

		private void initialize() {
			typeChanged();
		}

		private void typeChanged() {
			if (condPane.contains(lockedUserForm)) {
				condPane.removeMember(lockedUserForm);
			}
			if (condPane.contains(remainPWDaysForm)) {
				condPane.removeMember(remainPWDaysForm);
			}
			if (condPane.contains(lastLoginForm)) {
				condPane.removeMember(lastLoginForm);
			}

			String typeValue = SmartGWTUtil.getStringValue(specificTypeItem);
			if (BuiltinAuthUserSpecificType.LOCKED.name().equals(typeValue)) {
				condPane.addMember(lockedUserForm);
			} else if (BuiltinAuthUserSpecificType.PASSWORDDAYS.name().equals(typeValue)) {
				condPane.addMember(remainPWDaysForm);
			} else if (BuiltinAuthUserSpecificType.LASTLOGIN.name().equals(typeValue)) {
				condPane.addMember(lastLoginForm);
			}
		}

		private void searchData() {
			resultPane.setPageNum(1);

			BuiltinAuthUserSearchConditionDto condition = createCondition();

			resultPane.doFetch(condition);
		}

		private void exportCsv() {
			PostDownloadFrame frame = new PostDownloadFrame();
			frame.setAction(GWT.getModuleBaseURL() + "service/userdownload")
				.addParameter("tenantId", String.valueOf(TenantInfoHolder.getId()))
				.addParameter("searchType", BuiltinAuthUserSearchType.SPECIFIC.name())
				.addParameter("specificType", BuiltinAuthUserSpecificType.valueOf(SmartGWTUtil.getStringValue(specificTypeItem)).name())
				.addParameter("lockedUserPolicyName", SmartGWTUtil.getStringValue(lockedUserPolicyNameField))
				.addParameter("passwordRemainDaysPolicyName", SmartGWTUtil.getStringValue(remainDaysPolicyNameField))
				.addParameter("passwordRemainDaysOparator", BuiltinAuthUserSearchOperator.valueOf(SmartGWTUtil.getStringValue(remainDaysOperatorItem)).name())
				.addParameter("passwordRemainDays", String.valueOf(remainDaysValueItem.getValueAsFloat().intValue()));
			if (lastLoginFromItem.getValueAsDate() != null) {
				frame.addParameter("lastLoginFrom", String.valueOf(lastLoginFromItem.getValueAsDate().getTime()));
			}
			if (lastLoginToItem.getValueAsDate() != null) {
				frame.addParameter("lastLoginTo", String.valueOf(lastLoginToItem.getValueAsDate().getTime()));
			}
			frame.execute();
		}

		private BuiltinAuthUserSearchConditionDto createCondition() {
			BuiltinAuthUserSearchConditionDto cond = new BuiltinAuthUserSearchConditionDto();
			cond.setSearchType(BuiltinAuthUserSearchType.SPECIFIC);

			cond.setSpecificType(BuiltinAuthUserSpecificType.valueOf(SmartGWTUtil.getStringValue(specificTypeItem)));

			cond.setLockedUserPolicyName(SmartGWTUtil.getStringValue(lockedUserPolicyNameField));

			cond.setPasswordRemainDaysPolicyName(SmartGWTUtil.getStringValue(remainDaysPolicyNameField));
			cond.setPasswordRemainDaysOparator(BuiltinAuthUserSearchOperator.valueOf(SmartGWTUtil.getStringValue(remainDaysOperatorItem)));
			cond.setPasswordRemainDays(remainDaysValueItem.getValueAsFloat().intValue());

			cond.setLastLoginFrom(lastLoginFromItem.getValueAsDate());
			cond.setLastLoginTo(lastLoginToItem.getValueAsDate());

			return cond;
		}
	}

	/**
	 * ユーザー属性での検索
	 */
	private class UserAttributeCriteriaPane extends VLayout {

		private TextItem accountIdItem;
		private TextItem nameItem;
		private TextItem mailItem;

		private SelectItem remainDaysOperatorItem;
		private SliderItem remainDaysValueItem;

		private TextAreaItem whereCondItem;

		public UserAttributeCriteriaPane() {
			setWidth100();
			setHeight100();
			setMembersMargin(5);

			HLayout condPane = new HLayout();
			condPane.setMargin(0);
			condPane.setPadding(0);
			condPane.setWidth100();
			condPane.setHeight100();

			VLayout condLeftPane = new VLayout();
			condLeftPane.setMargin(0);
			condLeftPane.setPadding(0);
//			condLeftPane.setWidth100();
			condLeftPane.setWidth(450);
			condLeftPane.setHeight100();

			VLayout condRightPane = new VLayout();
			condRightPane.setMargin(0);
			condRightPane.setPadding(0);
			condRightPane.setWidth100();
			condRightPane.setHeight100();

			condPane.setMembers(condLeftPane, condRightPane);

			//------------------------
			//標準項目
			//------------------------
			DynamicForm standardForm = new DynamicForm();
			standardForm.setWidth100();
			standardForm.setHeight100();
			standardForm.setNumCols(5);
			standardForm.setColWidths(100, 120, 30, 220, "*");
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
			//有効期間残日数
			//------------------------
			DynamicForm remainDaysForm = new DynamicForm();
			remainDaysForm.setWidth100();
			remainDaysForm.setHeight100();
			remainDaysForm.setNumCols(4);
			remainDaysForm.setColWidths(100, 70, 280, "*");

			LinkedHashMap<String, String> remainDaysOparatorValues = new LinkedHashMap<String, String>();
			remainDaysOparatorValues.put("", "");
			remainDaysOparatorValues.put(BuiltinAuthUserSearchOperator.LESSTHAN.name(), BuiltinAuthUserSearchOperator.LESSTHAN.displayName());
			remainDaysOparatorValues.put(BuiltinAuthUserSearchOperator.EQUAL.name(), BuiltinAuthUserSearchOperator.EQUAL.displayName());
			remainDaysOperatorItem = new SelectItem();
			remainDaysOperatorItem.setStartRow(true);
			remainDaysOperatorItem.setTitle("Remain days to expire user");
			remainDaysOperatorItem.setWidth(50);
			remainDaysOperatorItem.setValueMap(remainDaysOparatorValues);
			remainDaysOperatorItem.setValue("");

			remainDaysValueItem = new SliderItem();
			remainDaysValueItem.setShowTitle(false);
			remainDaysValueItem.setWidth(250);
			remainDaysValueItem.setHeight(30);	//これを指定しないと間延び
			remainDaysValueItem.setMinValue(0d);
			remainDaysValueItem.setMaxValue(30d);
			remainDaysValueItem.setValue(5);	//default 5 days
			SmartGWTUtil.addHoverToFormItem(remainDaysValueItem,
					AdminClientMessageUtil.getString("ui_tools_auth_builtin_BuiltinAuthExplorerMainPane_remainDaysValueItemComment"));

			remainDaysForm.setFields(remainDaysOperatorItem, remainDaysValueItem);

			condLeftPane.setMembers(standardForm, remainDaysForm);

			//------------------------
			//Where項目
			//------------------------
			DynamicForm whereForm = new DynamicForm();
			whereForm.setWidth100();
			whereForm.setHeight100();
			whereForm.setNumCols(3);
			whereForm.setColWidths(100, "*", 10);

			whereCondItem = new TextAreaItem();
			whereCondItem.setTitle("Direct Where EQL");
			whereCondItem.setWidth("100%");
			whereCondItem.setHeight(140);
			SmartGWTUtil.addHoverToFormItem(whereCondItem,
					AdminClientMessageUtil.getString("ui_tools_auth_builtin_BuiltinAuthExplorerMainPane_whereCondItemComment"));

			whereForm.setFields(whereCondItem);

			condRightPane.setMembers(whereForm);

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
			IButton exportCSVButton = new IButton("Export CSV");
			exportCSVButton.setIcon(EXPORT_ICON);
			exportCSVButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					exportCsv();
				}
			});

			LayoutSpacer footerSpace = new LayoutSpacer();
			footerSpace.setWidth(100);
			footer.addMember(footerSpace);
			footer.addMember(searchButton);
			footer.addMember(exportCSVButton);

			//------------------------
			//下余白
			//------------------------
			LayoutSpacer condSpace = new LayoutSpacer();
			condSpace.setWidth100();
			condSpace.setHeight100();

			//------------------------
			//全体
			//------------------------
//			addMember(standardForm);
//			addMember(remainDaysForm);
			addMember(condPane);
			addMember(footer);
			addMember(condSpace);

			initialize();
		}

		private void initialize() {
		}

		private void searchData() {
			resultPane.setPageNum(1);

			BuiltinAuthUserSearchConditionDto condition = createCondition();

			resultPane.doFetch(condition);
		}

		private void exportCsv() {
			PostDownloadFrame frame = new PostDownloadFrame();
			frame.setAction(GWT.getModuleBaseURL() + "service/userdownload")
				.addParameter("tenantId", String.valueOf(TenantInfoHolder.getId()))
				.addParameter("searchType", BuiltinAuthUserSearchType.ATTRIBUTE.name())
				.addParameter("accountId", SmartGWTUtil.getStringValue(accountIdItem))
				.addParameter("name", SmartGWTUtil.getStringValue(nameItem))
				.addParameter("mail", SmartGWTUtil.getStringValue(mailItem))
				.addParameter("directWhere", SmartGWTUtil.getStringValue(whereCondItem));

			if (!SmartGWTUtil.getStringValue(remainDaysOperatorItem).isEmpty()) {
				frame.addParameter("validTermRemainDaysOparator", BuiltinAuthUserSearchOperator.valueOf(SmartGWTUtil.getStringValue(remainDaysOperatorItem)).name())
					.addParameter("validTermRemainDays", String.valueOf(remainDaysValueItem.getValueAsFloat().intValue()));
			}
			frame.execute();
		}

		private BuiltinAuthUserSearchConditionDto createCondition() {
			BuiltinAuthUserSearchConditionDto cond = new BuiltinAuthUserSearchConditionDto();
			cond.setSearchType(BuiltinAuthUserSearchType.ATTRIBUTE);

			cond.setAccountId(SmartGWTUtil.getStringValue(accountIdItem));
			cond.setName(SmartGWTUtil.getStringValue(nameItem));
			cond.setMail(SmartGWTUtil.getStringValue(mailItem));

			if (!SmartGWTUtil.getStringValue(remainDaysOperatorItem).isEmpty()) {
				cond.setValidTermRemainDaysOparator(BuiltinAuthUserSearchOperator.valueOf(SmartGWTUtil.getStringValue(remainDaysOperatorItem)));
				cond.setValidTermRemainDays(remainDaysValueItem.getValueAsFloat().intValue());
			}

			cond.setDirectWhere(SmartGWTUtil.getStringValue(whereCondItem));

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

		private BuiltinUserDS ds;

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

			//------------------------
			//Reset Error Count
			//------------------------
			final ToolStripButton resetCountButton = new ToolStripButton();
			resetCountButton.setIcon(RESET_ERROR_COUNT_ICON);
			resetCountButton.setTitle("Reset Error Count");
			SmartGWTUtil.addHoverToCanvas(resetCountButton, AdminClientMessageUtil.getString("ui_tools_auth_builtin_BuiltinAuthExplorerMainPane_resetErrorNumSelectUser"));
			resetCountButton.setHoverWrap(false);
			resetCountButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					resetErrorCount();
				}
			});
			toolStrip.addButton(resetCountButton);


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

			//CheckBox選択設定
			grid.setSelectionType(SelectionStyle.SIMPLE);
			grid.setSelectionAppearance(SelectionAppearance.CHECKBOX);

			grid.addDataArrivedHandler(new DataArrivedHandler() {

				@Override
				public void onDataArrived(DataArrivedEvent event) {
					showResultInfo(ds.getResult());
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
			ds = BuiltinUserDS.setDataSource(grid);
		}

		public void setPageNum(int page) {
			pageNum.setValue(page + "");
		}

		public void doFetch(BuiltinAuthUserSearchConditionDto condition) {
			startExecute("search");

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

		private void resetErrorCount() {
			//選択チェック
			if (!isSelected()) {
				return;
			}

			//選択ユーザーの取得
			ListGridRecord[] records = grid.getSelectedRecords();
			List<BuiltinAuthUserDto> users = new ArrayList<BuiltinAuthUserDto>(records.length);
			for (ListGridRecord record : records) {
				users.add((BuiltinAuthUserDto)record.getAttributeAsObject(BuiltinUserDS.VALUE_OBJECT));
			}

			ResetErrorCountDialog dialog = new ResetErrorCountDialog(users);
			dialog.show();
		}

		private boolean isSelected() {
			//trueを指定することでPathは全て選択されていないと含まれない
			ListGridRecord[] records = grid.getSelectedRecords(true);
			if (records == null || records.length == 0) {
				SC.say(AdminClientMessageUtil.getString("ui_tools_auth_builtin_BuiltinAuthExplorerMainPane_selectDesUser"));
				return false;
			}
			return true;
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
				messageTabSet.setErrorMessage(result.getLogMessages());
			} else {
				totalCount = result.getTotalCount();
				offset = result.getExecuteOffset();	//実行時に変更される可能性がある
				messageTabSet.setMessage(result.getLogMessages());
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

}
