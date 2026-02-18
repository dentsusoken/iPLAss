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

package org.iplass.adminconsole.client.tools.ui.eql;

import java.util.List;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.io.download.PostDownloadFrame;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.EQLExecutePane;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.client.widgets.form.fields.events.FocusEvent;
import com.smartgwt.client.widgets.form.fields.events.FocusHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * EQLWorksheetパネル
 */
public class EqlWorksheetMainPane extends VLayout {

	private static final String EXPORT_ICON = "[SKIN]/actions/download.png";

	private EQLWorksheetPane worksheetPane;
	private EQLExecutePane executePane;

	/**
	 * コンストラクタ
	 *
	 * @param tabNum タブ番号（複数起動対応）
	 */
	public EqlWorksheetMainPane(int tabNum) {

		setWidth100();
		setHeight100();

		worksheetPane = new EQLWorksheetPane();
		worksheetPane.setHeight("30%");
		worksheetPane.setShowResizeBar(true);		//リサイズ可能
		worksheetPane.setResizeBarTarget("next");	//リサイズバーをダブルクリックした際、下を収縮

		executePane = new EQLExecutePane();
		executePane.setHeight("70%");

		addMember(worksheetPane);
		addMember(executePane);
	}

	private class EQLWorksheetPane extends HLayout {

		public EQLWorksheetPane() {

			setWidth100();

			EQLEditPane worksheetPane = new EQLEditPane();
			worksheetPane.setWidth("60%");
			worksheetPane.setShowResizeBar(true);	//リサイズ可能
			worksheetPane.setResizeBarTarget("next");	//リサイズバーをダブルクリックした際、下を収縮

			EQLHintPane hintPane = new EQLHintPane();
			hintPane.setWidth("40%");

			addMember(worksheetPane);
			addMember(hintPane);
		}

	}

	private class EQLEditPane extends VLayout {

		private TextAreaItem eqlField;
		private CheckboxItem searchAllVersion;
		private Label cursorPositionLabel;

		public EQLEditPane() {
			setWidth100();
			setHeight100();

			ToolStrip toolStrip = new ToolStrip();
			toolStrip.setWidth100();
			toolStrip.setMembersMargin(5);

			final ToolStripButton executeButton = new ToolStripButton();
			executeButton.setIcon("[SKIN]/actions/forward.png");
			executeButton.setTitle(AdminClientMessageUtil.getString("ui_tools_eql_EqlWorksheetMainPane_execute"));
			SmartGWTUtil.addHoverToCanvas(executeButton, AdminClientMessageUtil.getString("ui_tools_eql_EqlWorksheetMainPane_runEqlComment"));
			executeButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					execute(getEql(), SmartGWTUtil.getBooleanValue(searchAllVersion));
				}
			});
			toolStrip.addButton(executeButton);

			final ToolStripButton dataExportButton = new ToolStripButton();
			dataExportButton.setIcon(EXPORT_ICON);
			dataExportButton.setTitle(AdminClientMessageUtil.getString("ui_tools_eql_EqlWorksheetMainPane_exportCsv"));
			dataExportButton.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_tools_eql_EqlWorksheetMainPane_exportCsvFileEql")));
			dataExportButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					exportData(getEql(), SmartGWTUtil.getBooleanValue(searchAllVersion));
				}
			});
			toolStrip.addButton(dataExportButton);

			final ToolStripButton entitySelectButton = new ToolStripButton();
			entitySelectButton.setIcon("[SKIN]/actions/configure.png");
			entitySelectButton.setTitle(AdminClientMessageUtil.getString("ui_tools_eql_EqlWorksheetMainPane_selectEntity"));
			SmartGWTUtil.addHoverToCanvas(entitySelectButton, AdminClientMessageUtil.getString("ui_tools_eql_EqlWorksheetMainPane_createEqlFromEntityDef"));
			entitySelectButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					selectEntity();
				}
			});

			toolStrip.addButton(entitySelectButton);

			searchAllVersion = new CheckboxItem();
			searchAllVersion.setTitle(AdminClientMessageUtil.getString("ui_tools_eql_EqlWorksheetMainPane_searchAllVersion"));
			SmartGWTUtil.addHoverToFormItem(searchAllVersion, AdminClientMessageUtil.getString("ui_tools_eql_EqlWorksheetMainPane_allVerSearch"));
			toolStrip.addFormItem(searchAllVersion);


			toolStrip.addFill();

			cursorPositionLabel = new Label();
			cursorPositionLabel.setWrap(false);
			cursorPositionLabel.setAutoWidth();
			toolStrip.addMember(cursorPositionLabel);

			Label dummyLabel = new Label();
			dummyLabel.setWidth(5);
			toolStrip.addMember(dummyLabel);

			DynamicForm form = new DynamicForm();
			form.setWidth100();
			form.setHeight100();
			form.setNumCols(1);	//これを指定することでTextAreaの幅が自動的に決定
			form.setCellPadding(0);
			form.setAutoFocus(true);

			eqlField = new TextAreaItem("eql");
			eqlField.setShowTitle(false);
			eqlField.setWidth("100%");
			eqlField.setHeight("100%");
			eqlField.setSelectOnFocus(true);
			eqlField.setShowHintInField(true);
			eqlField.setBrowserSpellCheck(false);

			eqlField.addKeyPressHandler(new KeyPressHandler() {
				@Override
				public void onKeyPress(KeyPressEvent event) {
					if (event.getKeyName() != null && "F8".equals(event.getKeyName().toUpperCase())) {
						String eql = getEql();
						executeButton.focus();	//連続実行を阻止するためフォーカスを逃がす
						execute(eql, SmartGWTUtil.getBooleanValue(searchAllVersion));
					}
					setCursorPosition();
				}
			});
			eqlField.addFocusHandler(new FocusHandler() {
				@Override
				public void onFocus(FocusEvent event) {
					setCursorPosition();
				}
			});
			eqlField.addBlurHandler(new BlurHandler() {
				@Override
				public void onBlur(BlurEvent event) {
					setCursorPosition();
				}
			});
			eqlField.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
				@Override
				public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
					setCursorPosition();
				}
			});

			form.setFields(eqlField);

			addMember(toolStrip);
			addMember(form);

			setCursorPosition();
		}

		private String getEql() {
			int[] selected = eqlField.getSelectionRange();
			if (selected == null || selected[0] == selected[1]) {
				return eqlField.getValueAsString();
			} else {
				return eqlField.getValueAsString().substring(selected[0], selected[1]);
			}
		}

		private void execute(final String eql, final boolean isSearchAllVersion) {
			if (SmartGWTUtil.isEmpty(eql)) {
				//空なのでNG
				SC.warn(AdminClientMessageUtil.getString("ui_tools_eql_EqlWorksheetMainPane_emptyEqlErr"));
				return;
			}

			executePane.executeEQL(eql, isSearchAllVersion);
		}

		private void exportData(final String eql, final boolean isSearchAllVersion) {
			if (SmartGWTUtil.isEmpty(eql)) {
				//空なのでNG
				SC.warn(AdminClientMessageUtil.getString("ui_tools_eql_EqlWorksheetMainPane_emptyEqlErr"));
				return;
			}

			PostDownloadFrame frame = new PostDownloadFrame();
			frame.setAction(GWT.getModuleBaseURL() + "service/eqldownload")
				.addParameter("tenantId", String.valueOf(TenantInfoHolder.getId()))
				.addParameter("eql", eql)
				.addParameter("isSearchAllVersion", String.valueOf(isSearchAllVersion))
				.execute();

		}

		private void selectEntity() {
			EntitySelectDialog dialog = new EntitySelectDialog();
			dialog.addDataChangedHandler(new DataChangedHandler() {

				@Override
				public void onDataChanged(DataChangedEvent event) {
					final String entityName = event.getValueObject(String.class);
					final Boolean isExcludeRef = event.getValue(Boolean.class, EntitySelectDialog.IS_EXCLUDE_REFERENCE_KEY);
					final Boolean isExcludeInherited = event.getValue(Boolean.class, EntitySelectDialog.IS_EXCLUDE_INHERITED_KEY);
					final Boolean isFormatEql = event.getValue(Boolean.class, EntitySelectDialog.IS_FORMAT_EQL_KEY);

					MetaDataServiceAsync service = MetaDataServiceFactory.get();
					service.getEntityDefinition(TenantInfoHolder.getId(), entityName, new AsyncCallback<EntityDefinition>() {

						@Override
						public void onFailure(Throwable caught) {
							GWT.log(caught.toString(), caught);
							SC.warn(AdminClientMessageUtil.getString("ui_tools_eql_EqlWorksheetMainPane_failedToGetEntityDef") + caught.getMessage());
						}

						@Override
						public void onSuccess(EntityDefinition result) {
							createEntityEql(result, isExcludeRef, isExcludeInherited, isFormatEql);
						}
					});
				}
			});
			dialog.show();
		}

		private void createEntityEql(EntityDefinition def, boolean isExcludeRef, boolean isExcludeInherited, boolean isFormatEql) {

			String formatPrefix = "";
			String formatSuffix = "";
			if (isFormatEql) {
				formatPrefix += "\t";
				formatSuffix += "\n";
			}

			StringBuilder builder = new StringBuilder();
			builder.append("select " + formatSuffix);
			List<PropertyDefinition> properties = def.getPropertyList();
			for (PropertyDefinition property : properties) {
				if (isExcludeInherited) {
					//oid,name,version以外は対象外にする
					if (!Entity.OID.equals(property.getName())
							&& !Entity.NAME.equals(property.getName())
							&& !Entity.VERSION.equals(property.getName())
							&& property.isInherited()) {
						continue;
					}
				}
				if (property instanceof ReferenceProperty) {
					if (!isExcludeRef) {
						ReferenceProperty refp = (ReferenceProperty)property;
						builder.append(formatPrefix + refp.getName() + "." + Entity.OID + ", " + formatSuffix);
						builder.append(formatPrefix + refp.getName() + "." + Entity.NAME + ", " + formatSuffix);
						builder.append(formatPrefix + refp.getName() + "." + Entity.VERSION + ", " + formatSuffix);
					}
				} else {
					builder.append(formatPrefix + property.getName() + ", " + formatSuffix);
				}
			}
			//builder.deleteCharAt(builder.length() - (", ".length() + formatSuffix.length()));
			builder.deleteCharAt(builder.length() - (2 + formatSuffix.length()));
			builder.append(" from ");
			builder.append(def.getName());
			eqlField.setValue(builder.toString());
		}

		private void setCursorPosition() {

			//onKeyPressで文字を追加した場合に、まだカーソルが前の位置のため10ms遅延させる
			new Timer() {
				@Override
				public void run() {
					int position = 0;
					//eqlFieldのlengthチェックをしている理由
					//Windowを最小化した場合などに、「setHint」の文字数分でカーソル位置が取得されてしまうため
					if (eqlField.getValueAsString() != null
							&& !eqlField.getValueAsString().isEmpty()
							&& eqlField.getSelectionRange() != null
							&& eqlField.getSelectionRange().length > 1) {
						position = eqlField.getSelectionRange()[0];
					}
					cursorPositionLabel.setContents("cursor：" + position);
				}
			}.schedule(10);
		}

	}

	private class EQLHintPane extends SectionStack {

		public EQLHintPane() {

			setVisibilityMode(VisibilityMode.MUTEX);

			SectionStackSection hintSection = new SectionStackSection("Hint");
			VLayout hintPane = new VLayout();
			hintPane.setHeight100();
			hintPane.setWidth100();

			Canvas hintContents = new Canvas();
			hintContents.setHeight100();
			hintContents.setWidth100();
			hintContents.setPadding(5);
			hintContents.setOverflow(Overflow.AUTO);
			hintContents.setCanSelectText(true);

			StringBuilder contents = new StringBuilder();
			contents.append("<style type=\"text/css\"><!--");
			contents.append("ul.notes{margin-top:5px;padding-left:15px;list-style-type:disc;}");
			contents.append("ul.notes li{padding:5px 0px;}");
			contents.append("ul.notes li span.strong {text-decoration:underline;color:red}");
			contents.append("ul.subnotes {margin-top:5px;padding-left:10px;list-style-type:circle;}");
			contents.append("--></style>");
			contents.append("<h3>Notes</h3>");
			contents.append("<ul class=\"notes\">");
			contents.append(AdminClientMessageUtil.getString("ui_tools_eql_EqlWorksheetMainPane_contentComment1"));
			contents.append(AdminClientMessageUtil.getString("ui_tools_eql_EqlWorksheetMainPane_contentComment2"));
			contents.append(AdminClientMessageUtil.getString("ui_tools_eql_EqlWorksheetMainPane_contentComment3"));
			contents.append(AdminClientMessageUtil.getString("ui_tools_eql_EqlWorksheetMainPane_contentComment4"));
			contents.append(AdminClientMessageUtil.getString("ui_tools_eql_EqlWorksheetMainPane_contentComment5"));
			contents.append(AdminClientMessageUtil.getString("ui_tools_eql_EqlWorksheetMainPane_contentComment6"));
			contents.append("<ul class=\"subnotes\">");
			contents.append(AdminClientMessageUtil.getString("ui_tools_eql_EqlWorksheetMainPane_contentComment7"));
			contents.append(AdminClientMessageUtil.getString("ui_tools_eql_EqlWorksheetMainPane_contentComment8"));
			contents.append(AdminClientMessageUtil.getString("ui_tools_eql_EqlWorksheetMainPane_contentComment9"));
			contents.append(AdminClientMessageUtil.getString("ui_tools_eql_EqlWorksheetMainPane_contentComment10"));
			contents.append(AdminClientMessageUtil.getString("ui_tools_eql_EqlWorksheetMainPane_contentComment11"));
			contents.append(AdminClientMessageUtil.getString("ui_tools_eql_EqlWorksheetMainPane_contentComment12"));
			contents.append("</ul>");
			contents.append("</ul>");
			hintContents.setContents(contents.toString());

			hintPane.addMember(hintContents);
			hintSection.addItem(hintPane);
			hintSection.setExpanded(true);
			addSection(hintSection);
		}
	}

}
