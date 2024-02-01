/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.gem.command.fulltext;

import java.util.List;
import java.util.Map;

/**
 * 全文検索結果を表現するクラス。
 */
public class FullTextSearchResult {

	private String defName;

	private String displayName;

	private String viewAction;

	private String detailAction;

	private boolean showDetailLink;

	private List<ColModel> colModels;

	private String crawlDate;

	private List<Map<String, String>> values;

	public FullTextSearchResult() {
	}

	public FullTextSearchResult(String defName) {
		this.defName = defName;
	}

	public String getDefName() {
		return defName;
	}

	public void setDefName(String defName) {
		this.defName = defName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getViewAction() {
		return viewAction;
	}

	public void setViewAction(String viewAction) {
		this.viewAction = viewAction;
	}

	public String getDetailAction() {
		return detailAction;
	}

	public void setDetailAction(String detailAction) {
		this.detailAction = detailAction;
	}

	public boolean isShowDetailLink() {
		return showDetailLink;
	}

	public void setShowDetailLink(boolean showDetailLink) {
		this.showDetailLink = showDetailLink;
	}

	public List<ColModel> getColModels() {
		return colModels;
	}

	public void setColModels(List<ColModel> colModels) {
		this.colModels = colModels;
	}

	public String getCrawlDate() {
		return crawlDate;
	}

	public void setCrawlDate(String crawlDate) {
		this.crawlDate = crawlDate;
	}

	public List<Map<String, String>> getValues() {
		return values;
	}

	public void setValues(List<Map<String, String>> values) {
		this.values = values;
	}

	public static class ColModel {

		/** jqGrid.colModel.name */
		private String name;	//col + 列番号(同一Propertyを考慮)

		/** jqGrid.colModel.label */
		private String label;	//colNamesが未指定の場合、labelが使用される(colNamesは未指定で利用)

		/** jqGrid.colModel.index */
		private String index;	//nameと同じ

		/** jqGrid.colModel.sortable */
		private boolean sortable = false;

		/** jqGrid.colModel.hidden */
		private boolean hidden = false;

		/** jqGrid.colModel.frozen */
		private boolean frozen = false;

		/** jqGrid.colModel.align */
		private String align = "left";	//left, center, right

		/** jqGrid.colModel.width */
		private Integer width = 150;

		/** jqGrid.colModel.classes */
		private String classes;

		/** cellAttrでのグループ化 */
		private boolean grouping = false;
		
		public ColModel(String name, String label) {
			this.name = name;
			this.index = name;
			this.label = "<p class=\"title\">" + label + "</p>";
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public String getIndex() {
			return index;
		}

		public void setIndex(String index) {
			this.index = index;
		}

		public boolean isSortable() {
			return sortable;
		}

		public void setSortable(boolean sortable) {
			this.sortable = sortable;
		}

		public boolean isHidden() {
			return hidden;
		}

		public void setHidden(boolean hidden) {
			this.hidden = hidden;
		}

		public boolean isFrozen() {
			return frozen;
		}

		public void setFrozen(boolean frozen) {
			this.frozen = frozen;
		}

		public String getAlign() {
			return align;
		}

		public void setAlign(String align) {
			this.align = align;
		}

		public Integer getWidth() {
			return width;
		}

		public void setWidth(Integer width) {
			this.width = width;
		}

		public String getClasses() {
			return classes;
		}

		public void setClasses(String classes) {
			this.classes = classes;
		}

		public boolean isGrouping() {
			return grouping;
		}
		
		public void setGrouping(boolean grouping) {
			this.grouping = grouping;
		}
	}
}
