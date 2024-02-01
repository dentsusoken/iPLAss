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

package org.iplass.adminconsole.client.tools.ui.langexplorer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.iplass.adminconsole.client.base.ui.widget.GridActionImgButton;
import org.iplass.adminconsole.client.base.ui.widget.MtpTreeGrid;
import org.iplass.adminconsole.client.base.ui.widget.MtpWidgetConstants;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.tools.data.metaexplorer.MetaDataTreeDS;
import org.iplass.adminconsole.client.tools.data.metaexplorer.MetaDataTreeDS.FIELD_NAME;
import org.iplass.adminconsole.client.tools.ui.metaexplorer.MetaDataTagSelectDialog;
import org.iplass.adminconsole.client.tools.ui.metaexplorer.MetaDataTagSelectDialog.MetaDataTagSelectedHandler;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.RepositoryType;

import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ImageStyle;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.CanvasItem;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.DateTimeItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.TimeItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.DataArrivedEvent;
import com.smartgwt.client.widgets.tree.events.DataArrivedHandler;

public class LangTreeGridPane extends VLayout {

	private AdvancedSearchExecHandler handler;

	private MetaDataAdvancedSearchPane searchPane;
	private LangMetaDataTreeGrid grid;

	public LangTreeGridPane(AdvancedSearchExecHandler handler) {
		this.handler = handler;

		setWidth100();

		searchPane = new MetaDataAdvancedSearchPane();

		SectionStackSection advancedSearchSection = new SectionStackSection();
		advancedSearchSection.setTitle("Advanced Search");
		advancedSearchSection.setItems(searchPane);
		advancedSearchSection.setExpanded(false);
		advancedSearchSection.setResizeable(true);

		grid = new LangMetaDataTreeGrid();
		grid.setCanDragResize(true);

		SectionStackSection metaDataGridSection = new SectionStackSection();
		metaDataGridSection.setShowHeader(false);
		metaDataGridSection.setItems(grid);
		metaDataGridSection.setExpanded(true);
		metaDataGridSection.setResizeable(true);

		SectionStack sectionStack = new SectionStack();
		sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
		sectionStack.setShowShadow(false);
		sectionStack.setSections(advancedSearchSection, metaDataGridSection);

		addMember(sectionStack);
	}

	public void refreshGrid(RepositoryType repositoryType) {
		grid.refreshGrid(repositoryType, searchPane.getUpdateDateFrom(), searchPane.getUpdateDateTo(), searchPane.getTagCreateDateFrom(), searchPane.getTagCreateDateTo());
	}

	public void expandRoot() {
		grid.expandRoot();
	}

	public void expandAll() {
		grid.expandAll();
	}

	public boolean isSelected() {
		return grid.isSelected();
	}

	public boolean isSelectedShared() {
		return grid.isSelectedShared();
	}

	public List<String> getSelectedPathList() {
		return grid.getSelectedPathList();
	}

	public String[] getSelectedPathArray() {
		return grid.getSelectedPathArray();
	}

	public void addRecordDoubleClickHandler(RecordDoubleClickHandler handler) {
		grid.addRecordDoubleClickHandler(handler);
	}

	public Tree getTree() {
		return grid.getTree();
	}

	public ListGridRecord[] getSelectedRecords(boolean excludePartialSelections) {
		return grid.getSelectedRecords(excludePartialSelections);
	}

	public class MetaDataAdvancedSearchPane extends VLayout {

		private CheckboxItem updateDateField;
		private DateItem updateDateFromField;
		private TimeItem updateDateTimeFromField;
		private DateItem updateDateToField;
		private TimeItem updateDateTimeToField;

		private CheckboxItem tagDateField;
		private TextItem tagFromNameField;
		private DateTimeItem tagFromDateField;
		private TextItem tagToNameField;
		private DateTimeItem tagToDateField;

		public MetaDataAdvancedSearchPane() {
			setAutoHeight();
			setMargin(5);

			DynamicForm condForm = new DynamicForm();
			condForm.setWidth100();
			condForm.setAutoHeight();
			condForm.setPadding(5);
			condForm.setNumCols(1);
			condForm.setColWidths("100%");

			//----------------------------
			//Update Date
			//----------------------------
			CanvasItem updateDateFormItems = new CanvasItem();
			updateDateFormItems.setShowTitle(false);
			updateDateFormItems.setStartRow(true);

			DynamicForm updateDateForm = new DynamicForm();
			updateDateForm.setWidth100();
			updateDateForm.setAutoHeight();
			updateDateForm.setNumCols(7);
			updateDateForm.setColWidths(130, 110, 60, 30, 110, 60, "*");

			//RadioGroupだとレイアウトが難しいのでCheckboxを利用
			updateDateField = new CheckboxItem();
			updateDateField.setTitle("By Update Date:");
			updateDateField.setShowTitle(false);
			updateDateField.setValue(true);		//初期選択
			updateDateField.addChangedHandler(new ChangedHandler() {
				@Override
				public void onChanged(ChangedEvent event) {
					//自力でOptionと同じ制御
					setSelectUpdateDate(SmartGWTUtil.getBooleanValue(updateDateField));
				}
			});

			updateDateFromField = SmartGWTUtil.createDateItem();
			updateDateFromField.setShowTitle(false);
			updateDateFromField.addChangedHandler(new ChangedHandler() {
				@Override
				public void onChanged(ChangedEvent event) {
					if (SmartGWTUtil.getStringValue(updateDateFromField) != null && !SmartGWTUtil.getStringValue(updateDateFromField).isEmpty()) {
						setSelectUpdateDate(true);
					}
				}
			});
			updateDateTimeFromField = SmartGWTUtil.createTimeItem(false);
			updateDateTimeFromField.setShowTitle(false);
			updateDateTimeFromField.addChangedHandler(new ChangedHandler() {
				@Override
				public void onChanged(ChangedEvent event) {
					if (SmartGWTUtil.getStringValue(updateDateTimeFromField) != null && !SmartGWTUtil.getStringValue(updateDateTimeFromField).isEmpty()) {
						setSelectUpdateDate(true);
					}
				}
			});

			StaticTextItem updateDateDummy = new StaticTextItem();
			updateDateDummy.setShowTitle(false);
			updateDateDummy.setWidth(20);
			updateDateDummy.setAlign(Alignment.CENTER);
			updateDateDummy.setValue("-");

			updateDateToField = SmartGWTUtil.createDateItem();
			updateDateToField.setShowTitle(false);
			updateDateToField.addChangedHandler(new ChangedHandler() {
				@Override
				public void onChanged(ChangedEvent event) {
					if (SmartGWTUtil.getStringValue(updateDateToField) != null && !SmartGWTUtil.getStringValue(updateDateToField).isEmpty()) {
						setSelectUpdateDate(true);
					}
				}
			});
			updateDateTimeToField = SmartGWTUtil.createTimeItem(false);
			updateDateTimeToField.setShowTitle(false);
			updateDateTimeToField.addChangedHandler(new ChangedHandler() {
				@Override
				public void onChanged(ChangedEvent event) {
					if (SmartGWTUtil.getStringValue(updateDateTimeToField) != null && !SmartGWTUtil.getStringValue(updateDateTimeToField).isEmpty()) {
						setSelectUpdateDate(true);
					}
				}
			});

			updateDateForm.setItems(updateDateField, updateDateFromField, updateDateTimeFromField, updateDateDummy, updateDateToField, updateDateTimeToField);

			HLayout updateDatePane = new HLayout(5);
			updateDatePane.setAutoHeight();
			updateDatePane.addMember(updateDateForm);
			updateDateFormItems.setCanvas(updateDatePane);

			//----------------------------
			//Tag CreateDate
			//----------------------------
			CanvasItem tagDateFormItems = new CanvasItem();
			tagDateFormItems.setShowTitle(false);
			tagDateFormItems.setStartRow(true);

			DynamicForm tagDateForm = new DynamicForm();
			tagDateForm.setWidth100();
			tagDateForm.setAutoHeight();
			tagDateForm.setNumCols(9);
			tagDateForm.setColWidths(130, 30, 200, 100, 30, 30, 200, 100, "*");

			//RadioGroupだとレイアウトが難しいのでCheckboxを利用
			tagDateField = new CheckboxItem();
			tagDateField.setTitle("By Tag Create Date:");
			tagDateField.setShowTitle(false);
			tagDateField.setValue(false);		//初期未選択
			tagDateField.addChangedHandler(new ChangedHandler() {
				@Override
				public void onChanged(ChangedEvent event) {
					//自力でOptionと同じ制御
					setSelectUpdateDate(!SmartGWTUtil.getBooleanValue(tagDateField));
				}
			});

			ButtonItem tagFromSelectFiled = new ButtonItem();
			tagFromSelectFiled.setStartRow(false);
			tagFromSelectFiled.setEndRow(false);
			tagFromSelectFiled.setTitle("");
			tagFromSelectFiled.setIcon(MtpWidgetConstants.ICON_SEARCH);
			tagFromSelectFiled.setTooltip("Select Tag");
			tagFromSelectFiled.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

				@Override
				public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
					MetaDataTagSelectDialog dialog = new MetaDataTagSelectDialog(new MetaDataTagSelectedHandler() {

						@Override
						public void selected(String fileOid, String name, Date createDate) {
							tagFromNameField.setValue(name);
							tagFromDateField.setValue(createDate);

							setSelectUpdateDate(false);
						}
					});
					dialog.show();
				}
			});

			tagFromNameField = new TextItem();
			tagFromNameField.setShowTitle(false);
			tagFromNameField.setDisabled(true);
			tagFromNameField.setWidth(200);

			tagFromDateField = SmartGWTUtil.createDateTimeItem();
			tagFromDateField.setShowTitle(false);
			tagFromDateField.setDisabled(true);
			tagFromDateField.setShowPickerIcon(false);

			StaticTextItem tagDateDummy = new StaticTextItem();
			tagDateDummy.setShowTitle(false);
			tagDateDummy.setWidth(20);
			tagDateDummy.setAlign(Alignment.CENTER);
			tagDateDummy.setValue("-");

			ButtonItem tagToSelectFiled = new ButtonItem();
			tagToSelectFiled.setStartRow(false);
			tagToSelectFiled.setEndRow(false);
			tagToSelectFiled.setTitle("");
			tagToSelectFiled.setIcon(MtpWidgetConstants.ICON_SEARCH);
			tagToSelectFiled.setTooltip("Select Tag");
			tagToSelectFiled.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

				@Override
				public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
					MetaDataTagSelectDialog dialog = new MetaDataTagSelectDialog(new MetaDataTagSelectedHandler() {

						@Override
						public void selected(String fileOid, String name, Date createDate) {
							tagToNameField.setValue(name);
							tagToDateField.setValue(createDate);

							setSelectUpdateDate(false);
						}
					});
					dialog.show();
				}
			});

			tagToNameField = new TextItem();
			tagToNameField.setShowTitle(false);
			tagToNameField.setDisabled(true);
			tagToNameField.setWidth(200);

			tagToDateField = SmartGWTUtil.createDateTimeItem();
			tagToDateField.setShowTitle(false);
			tagToDateField.setDisabled(true);
			tagToDateField.setShowPickerIcon(false);

			tagDateForm.setItems(tagDateField, tagFromSelectFiled, tagFromNameField, tagFromDateField, tagDateDummy, tagToSelectFiled, tagToNameField, tagToDateField);

			HLayout tagDatePane = new HLayout(5);
			tagDatePane.setAutoHeight();
			tagDatePane.addMember(tagDateForm);
			tagDateFormItems.setCanvas(tagDatePane);

			//----------------------------
			//Buttons
			//----------------------------
			CanvasItem buttonItems = new CanvasItem();
			buttonItems.setShowTitle(false);
			buttonItems.setStartRow(true);

			IButton advanceSearchBtn = new IButton();
			advanceSearchBtn.setTitle("Search");
			advanceSearchBtn.setIcon(MtpWidgetConstants.ICON_SEARCH);
			advanceSearchBtn.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					handler.doSearch();
				}
			});
			IButton clearBtn = new IButton();
			clearBtn.setTitle("Clear");
			clearBtn.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					updateDateFromField.clearValue();
					updateDateTimeFromField.clearValue();
					updateDateToField.clearValue();
					updateDateTimeToField.clearValue();

					tagFromNameField.clearValue();
					tagFromDateField.clearValue();
					tagToNameField.clearValue();
					tagToDateField.clearValue();

					setSelectUpdateDate(true);

					handler.doSearch();
				}
			});

			HLayout buttonPane = new HLayout(5);
			buttonPane.setMargin(5);
			buttonPane.setAutoHeight();
			buttonPane.addMember(advanceSearchBtn);
			buttonPane.addMember(clearBtn);
			buttonItems.setCanvas(buttonPane);

			condForm.setItems(updateDateFormItems
					,tagDateFormItems
					,buttonItems);

			addMember(condForm);
		}

		private void setSelectUpdateDate(boolean selected) {
			if (selected) {
				updateDateField.setValue(true);
				tagDateField.setValue(false);
			} else {
				updateDateField.setValue(false);
				tagDateField.setValue(true);
			}
		}

		public boolean isSearchTypeAsUpdateDate() {
			return SmartGWTUtil.getBooleanValue(updateDateField);
		}

		public Date getUpdateDateFrom() {
			if (!isSearchTypeAsUpdateDate()) {
				return null;
			}
			if (!updateDateFromField.validate()) {
				updateDateFromField.clearValue();
				updateDateTimeFromField.clearValue();
			}
			updateDateFromField.validate();	//validate結果をクリアするためvalidateを再度呼び出し

			return SmartGWTUtil.getDateTimeValue(updateDateFromField.getValueAsDate(), (Date)updateDateTimeFromField.getValue(), false, "00:00", "00", "000");
		}

		public Date getUpdateDateTo() {
			if (!isSearchTypeAsUpdateDate()) {
				return null;
			}
			if (!updateDateToField.validate()) {
				updateDateToField.clearValue();
				updateDateTimeToField.clearValue();
			}
			updateDateToField.validate();	//validate結果をクリアするためvalidateを再度呼び出し

			return SmartGWTUtil.getDateTimeValue(updateDateToField.getValueAsDate(), (Date)updateDateTimeToField.getValue(), false, "23:59", "59", "999");
		}

		public Date getTagCreateDateFrom() {
			if (isSearchTypeAsUpdateDate()) {
				return null;
			}
			return tagFromDateField.getValueAsDate();
		}

		public Date getTagCreateDateTo() {
			if (isSearchTypeAsUpdateDate()) {
				return null;
			}
			return tagToDateField.getValueAsDate();
		}

	}

	private class LangMetaDataTreeGrid extends MtpTreeGrid {

		private static final String ERROR_ICON = "[SKINIMG]/actions/exclamation.png";

		public LangMetaDataTreeGrid() {
			super(true);

			setLeaveScrollbarGap(false);
			setCanSort(false);
			setCanFreezeFields(false);
			setSelectionAppearance(SelectionAppearance.CHECKBOX);
			setShowSelectedStyle(false);
			setShowPartialSelection(true);
			setCascadeSelection(true);
			setCanGroupBy(false);
			setCanPickFields(false);

			// この２つを指定することでcreateRecordComponentが有効
			setShowRecordComponents(true);
			setShowRecordComponentsByCell(true);

			//IDなどDragで選択可能にしたいが、Treeの場合ドラッグ時にエラーになる(JavaScriptException)
			//動作上は問題がないので、GWTログのみ出力するように対応して選択可能にした(@see MtpAdmin.java)
			setCanDragSelectText(true);

			addDataArrivedHandler(new DataArrivedHandler() {

				@Override
				public void onDataArrived(DataArrivedEvent event) {
					// Root配下を展開する
					expandRoot();
				}
			});
		}

		@Override
		protected Canvas createRecordComponent(final ListGridRecord record,
				Integer colNum) {
			final String fieldName = this.getFieldName(colNum);
			if ("error".equals(fieldName)) {
				Boolean isError = record
						.getAttributeAsBoolean(FIELD_NAME.IS_ERROR.name());
				if (isError != null && isError) {
					GridActionImgButton recordCanvas = new GridActionImgButton();
					recordCanvas.setActionButtonSrc(ERROR_ICON);
					recordCanvas.setActionButtonPrompt(
							record.getAttributeAsString(FIELD_NAME.ERROR_MESSAGE.name()));

					// ITEMの場合は選択不可
					if (!getTree().isFolder((TreeNode)record)) {
						record.setEnabled(false);
					}

					return recordCanvas;
				}
			} else if ((FIELD_NAME.SHARABLE.name()  + "_disp").equals(fieldName)) {
				Boolean isSharable = record.getAttributeAsBoolean(FIELD_NAME.SHARABLE.name());
				if (isSharable != null && isSharable) {
					Img img = new Img("tick.png", 16, 16);
					img.setImageWidth(16);
					img.setImageHeight(16);
					img.setImageType(ImageStyle.CENTER);
					return img;
				}
			} else if ((FIELD_NAME.OVERWRITABLE.name()  + "_disp").equals(fieldName)) {
				Boolean isOverwritable = record.getAttributeAsBoolean(FIELD_NAME.OVERWRITABLE.name());
				if (isOverwritable != null && isOverwritable) {
					Img img = new Img("tick.png", 16, 16);
					img.setImageWidth(16);
					img.setImageHeight(16);
					img.setImageType(ImageStyle.CENTER);
					return img;
				}
			}
			return null;
		}

		public void refreshGrid(RepositoryType repositoryType, Date updateDateFrom, Date updateDateTo, Date tagCreateDataFrom, Date tagCreateDataTo) {

			setDataSource(MetaDataTreeDS.getInstance());

			TreeGridField errorField = new TreeGridField("error", " ");
			errorField.setWidth(25);
			TreeGridField displayName = new TreeGridField(FIELD_NAME.DISPLAY_NAME.name(), "Path");
			displayName.setWidth(300);
			displayName.setShowHover(true);
			displayName.setHoverCustomizer(new HoverCustomizer() {
				@Override
				public String hoverHTML(Object value, ListGridRecord record,
						int rowNum, int colNum) {
					return SmartGWTUtil.getHoverString(record.getAttribute(FIELD_NAME.DISPLAY_NAME.name()));
				}
			});
			TreeGridField path = new TreeGridField(FIELD_NAME.ITEM_ID.name(), "ID");
			path.setWidth(300);
			path.setShowHover(true);
			path.setHoverCustomizer(new HoverCustomizer() {
				@Override
				public String hoverHTML(Object value, ListGridRecord record,
						int rowNum, int colNum) {
					return SmartGWTUtil.getHoverString(record.getAttribute(FIELD_NAME.ITEM_ID.name()));
				}
			});

			TreeGridField updateDate = new TreeGridField(FIELD_NAME.UPDATE_DATE.name(), "Update Date");
			updateDate.setWidth(130);
			//updateDate.setAlign(Alignment.CENTER);
			//updateDate.setDateFormatter(DateDisplayFormat.TOJAPANSHORTDATETIME);	//Stringに変えて秒まで表示した

			TreeGridField shared = new TreeGridField(FIELD_NAME.SHARED_NAME.name(), "SharedType");
			shared.setWidth(100);
			shared.setAlign(Alignment.CENTER);

			TreeGridField sharable = new TreeGridField(FIELD_NAME.SHARABLE.name() + "_disp", "Sharable");
			sharable.setWidth(70);
			sharable.setAlign(Alignment.CENTER);

			TreeGridField overwritable = new TreeGridField(FIELD_NAME.OVERWRITABLE.name() + "_disp", "Overwritable");
			overwritable.setWidth(70);
			overwritable.setAlign(Alignment.CENTER);

			TreeGridField repository = new TreeGridField(FIELD_NAME.REPOSITORY.name(), "Repository");
			repository.setWidth(70);
			repository.setAlign(Alignment.CENTER);
			repository.setShowHover(true);
			repository.setHoverCustomizer(new HoverCustomizer() {
				@Override
				public String hoverHTML(Object value, ListGridRecord record,
						int rowNum, int colNum) {
					return SmartGWTUtil.getHoverString(record.getAttribute(FIELD_NAME.REPOSITORY.name()));
				}
			});
			setFields(displayName, errorField, path, updateDate, shared, sharable, overwritable, repository);

			Criteria criteria = new Criteria();
			criteria.addCriteria(MetaDataTreeDS.CRITERIA_REPOSITORY_TYPE, repositoryType.typeName());
			criteria.addCriteria(MetaDataTreeDS.CRITERIA_UPDATE_FROM, updateDateFrom);
			criteria.addCriteria(MetaDataTreeDS.CRITERIA_UPDATE_TO, updateDateTo);
			criteria.addCriteria(MetaDataTreeDS.CRITERIA_TAG_FROM, tagCreateDataFrom);
			criteria.addCriteria(MetaDataTreeDS.CRITERIA_TAG_TO, tagCreateDataTo);

			fetchData(criteria);
		}

		public void expandRoot() {
			getTree().closeAll();
			getTree().openFolders(
					getTree().getChildren(getTree().getRoot()));
		}

		public void expandAll() {
			getTree().openAll();
		}

		public boolean isSelected() {
			//trueを指定することでPathは全て選択されていないと含まれない
			ListGridRecord[] records = getSelectedRecords(true);
			if (records == null || records.length == 0) {
				return false;
			}
			return true;
		}

		public boolean isSelectedShared() {
			//trueを指定することでPathは全て選択されていないと含まれない
			ListGridRecord[] records = getSelectedRecords(true);
			for (ListGridRecord record : records) {
				TreeNode node = (TreeNode)record;
				if (node.getAttributeAsBoolean(FIELD_NAME.SHARED.name()))  {
					return true;
				}
			}
			return false;
		}

		public List<String> getSelectedPathList() {
			ListGridRecord[] records = getSelectedRecords(true);
			List<String> selectPaths = new ArrayList<>();
			for (ListGridRecord record : records) {
				String path = record.getAttribute(FIELD_NAME.PATH.name());
				//Rootは除外
				if (path == null || path.isEmpty()) {
					continue;
				}

				TreeNode node = (TreeNode)record;
				if (!getTree().isFolder(node)) {
					selectPaths.add(path);
				}
			}

			return selectPaths;
		}

		public String[] getSelectedPathArray() {
			return getSelectedPathList().toArray(new String[] {});
		}
	}

	public interface AdvancedSearchExecHandler {
		public void doSearch();
	}

}
