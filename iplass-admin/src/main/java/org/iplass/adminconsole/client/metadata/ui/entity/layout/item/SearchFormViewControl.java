/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.entity.layout.item;

import org.iplass.adminconsole.client.base.event.MTPEventHandler;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.element.section.ScriptingSectionControl;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.element.section.SearchConditionSectionControl;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.element.section.SearchResultSectionControl;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.element.section.SectionControl;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.element.section.TemplateSectionControl;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.view.generic.SearchFormView;
import org.iplass.mtp.view.generic.element.section.ScriptingSection;
import org.iplass.mtp.view.generic.element.section.Section;
import org.iplass.mtp.view.generic.element.section.TemplateSection;

import com.smartgwt.client.types.HeaderControls;
import com.smartgwt.client.util.EventHandler;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.DropEvent;
import com.smartgwt.client.widgets.events.DropHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class SearchFormViewControl extends ItemControl {
	/** 検索条件 */
	private SearchConditionSectionControl condition = null;

	/** 検索結果 */
	private SearchResultSectionControl result;

	private DropLayout topDropLayout1;
	private DropLayout topDropLayout2;
	private DropLayout centerDropLayout;
	private DropLayout bottomDropLayout;

	public SearchFormViewControl(String defName) {
		super(defName, FieldReferenceType.SEARCHCONDITION); // カスタムボタンの表示タイプを隠すためにSearchConditionを指定
		setHeaderControls(HeaderControls.HEADER_LABEL, setting);
		setTitle(AdminClientMessageUtil.getString("ui_metadata_entity_layout_item_SearchFormWindow_searchScreen"));
		setHeight100();
		setWidth100();
		setMargin(2);
		setCanDrag(false);
		setCanDragReposition(false);
		setBorder("1px solid navy");

		// 編集用のエリア
		VLayout dropArea = new VLayout();
		dropArea.setMembersMargin(2);
		dropArea.setWidth100();

		topDropLayout1 = new DropLayout(AdminClientMessageUtil.getString("ui_metadata_entity_layout_item_SearchFormWindow_buttonTop"));
		dropArea.addMember(topDropLayout1);

		topDropLayout2 = new DropLayout(AdminClientMessageUtil.getString("ui_metadata_entity_layout_item_SearchFormWindow_buttonBottom"));
		dropArea.addMember(topDropLayout2);

		condition = new SearchConditionSectionControl(defName, FieldReferenceType.SEARCHCONDITION);
		dropArea.addMember(condition);

		centerDropLayout = new DropLayout(AdminClientMessageUtil.getString("ui_metadata_entity_layout_item_SearchFormWindow_searchCenterScreen"));
		dropArea.addMember(centerDropLayout);

		result = new SearchResultSectionControl(defName, FieldReferenceType.SEARCHRESULT);
		dropArea.addMember(result);

		bottomDropLayout = new DropLayout(AdminClientMessageUtil.getString("ui_metadata_entity_layout_item_SearchFormWindow_searchBottomScreen"));
		dropArea.addMember(bottomDropLayout);

		addItem(dropArea);

		SearchFormView fv = new SearchFormView();
		setValueObject(fv);
		setClassName(fv.getClass().getName());
	}

	/**
	 * 編集開始イベント設定。
	 * @param handler
	 */
	public void setEditStartHandler(MTPEventHandler handler) {
		condition.setEditStartHandler(handler);
		result.setEditStartHandler(handler);
	}

	/**
	 * Viewをリセット。
	 */
	public void reset() {
		condition.clear();
		result.clear();
		topDropLayout1.clearSection();
		topDropLayout2.clearSection();
		centerDropLayout.clearSection();
		bottomDropLayout.clearSection();
	}

	public void setEntityDefinition(EntityDefinition ed) {
		condition.setEntityDefinition(ed);
		result.setEntityDefinition(ed);
	}

	/**
	 * Viewの情報を展開。
	 * @param fv
	 */
	public void apply(SearchFormView fv) {
		setValueObject(fv);
		if (fv.getTitle() == null) {
			setTitle(AdminClientMessageUtil.getString("ui_metadata_entity_layout_item_SearchFormWindow_searchScreen"));
		} else {
			setTitle(fv.getTitle());
		}
		if (fv.getCondSection() != null) {
			condition.restore(fv.getCondSection());
		}
		if (fv.getResultSection() != null) {
			result.restore(fv.getResultSection());
		}
		if (fv.getTopSection1() != null) {
			topDropLayout1.setSection(fv.getTopSection1());
		}
		if (fv.getTopSection2() != null) {
			topDropLayout2.setSection(fv.getTopSection2());
		}
		if (fv.getCenterSection() != null) {
			centerDropLayout.setSection(fv.getCenterSection());
		}
		if (fv.getBottomSection() != null) {
			bottomDropLayout.setSection(fv.getBottomSection());
		}
	}

	public SearchFormView getForm() {
		SearchFormView form = (SearchFormView) getValueObject();
		if (form.getSections() != null) form.getSections().clear();
		form.addSection(condition.getSection());
		form.addSection(result.getSection());
		form.setTopSection1(topDropLayout1.getSection());
		form.setTopSection2(topDropLayout2.getSection());
		form.setCenterSection(centerDropLayout.getSection());
		form.setBottomSection(bottomDropLayout.getSection());
		return form;
	}

	/**
	 * 内部レイアウト
	 */
	private class DropLayout extends HLayout {

		private VLayout dropLayout;

		/**
		 * コンストラクタ
		 */
		private DropLayout(String contents) {
			setHeight(42);
			setBackgroundColor("#99DDDD");
			setBorder("1px solid navy");
			setMargin(4);
			setMembersMargin(6);
			setPadding(5);

			Label label = new Label(contents);
			label.setWidth(80);
			addMember(label);

			dropLayout = new VLayout();
			dropLayout.setCanAcceptDrop(true);
			dropLayout.setDropLineThickness(4);

			//セクションのみドロップ可能なエリア
			dropLayout.setDropTypes("section");

			//ドロップ先を表示する設定
			Canvas dropLineProperties = new Canvas();
			dropLineProperties.setBackgroundColor("aqua");
			dropLayout.setDropLineProperties(dropLineProperties);

			//ドラッグ中のコンポーネントを表示する設定
			dropLayout.setShowDragPlaceHolder(true);
			Canvas placeHolderProperties = new Canvas();
			placeHolderProperties.setBorder("2px solid #8289A6");
			dropLayout.setPlaceHolderProperties(placeHolderProperties);

			dropLayout.addDropHandler(new DropHandler() {

				@Override
				public void onDrop(DropEvent event) {

					Canvas dragTarget = EventHandler.getDragTarget();
					if (dragTarget instanceof ListGrid) {

						clearSection();
						ListGridRecord record = ((ListGrid) dragTarget).getSelectedRecord();
						String name = record.getAttribute("name");

						Section section = null;
						if (ScriptingSection.class.getName().equals(name)) {
							//HTMLセクション
							section = new ScriptingSection();
							section.setDispFlag(true);
						} else if (TemplateSection.class.getName().equals(name)) {
							//カスタムセクション
							section = new TemplateSection();
							section.setDispFlag(true);
						}
						if (section != null) {
							setSection(section);
						}

						// cancelしないとdrop元自体が移動してしまう
						event.cancel();
					} else if (dragTarget instanceof SectionControl) {
						clearSection();

						dropLayout.addMember(dragTarget);
					}
				}
			});
			addMember(dropLayout);
		}

		public void clearSection() {
			//ツリーからのdropはWidgetを作成し、古いWidgetは削除
			for (Canvas member : dropLayout.getMembers()) {
				member.destroy();
			}
		}

		public void setSection(Section section) {

			//それぞれトリガは関係ないのでALLを指定
			if (section instanceof ScriptingSection) {
				ScriptingSectionControl newSection = new ScriptingSectionControl(defName, FieldReferenceType.ALL, (ScriptingSection) section);
				dropLayout.addMember(newSection);
			} else if (section instanceof TemplateSection) {
				TemplateSectionControl newSection = new TemplateSectionControl(defName, FieldReferenceType.ALL, (TemplateSection) section);
				dropLayout.addMember(newSection);
			}
		}

		public Section getSection() {
			for (Canvas member : dropLayout.getMembers()) {
				if (member instanceof SectionControl) {
					return ((SectionControl) member).getSection();
				}
			}
			return null;
		}
	}
}
