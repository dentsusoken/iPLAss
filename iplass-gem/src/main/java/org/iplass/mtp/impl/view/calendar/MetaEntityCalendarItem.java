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

package org.iplass.mtp.impl.view.calendar;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.entity.property.MetaProperty;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.script.Script;
import org.iplass.mtp.impl.script.ScriptContext;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.util.DateUtil;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.calendar.EntityCalendarItem;
import org.iplass.mtp.view.calendar.EntityCalendarItem.CalendarSearchType;

/**
 * カレンダーに表示するEntityの設定
 * @author lis3wg
 *
 */
public class MetaEntityCalendarItem implements MetaData {

	/** SerialVersionUID */
	private static final long serialVersionUID = -143884759940160050L;

	/** Entity定義のID */
	private String definitionId;

	/** Entityの表示色 */
	private String entityColor;

	/** カレンダーの検索方法 */
	private CalendarSearchType calendarSearchType;

	/** プロパティのID */
	private String propertyId;

	/** FromのプロパティのID */
	private String fromPropertyId;

	/** ToのプロパティのID */
	private String toPropertyId;

	/** 詳細アクション名 */
	private String viewAction;

	/** 追加アクション名 */
	private String addAction;

	/** レコードの新規登録を拒否するか */
	private Boolean allowNoEntryOfRecords;

	/** ビュー名 */
	private String viewName;

	/** 時間を表示するか */
	private Boolean displayTime;

	/** 検索上限 */
	private Integer limit;

	/** フィルタ条件 */
	private String filterCondition;

	/** Entityの表示色（groovy） */
	private String colorConfig;

	/**
	 * Entity定義のIDを取得します。
	 * @return Entity定義
	 */
	public String getDefinitionId() {
		return definitionId;
	}

	/**
	 * Entity定義のIDを設定します。
	 * @param definitionId Entity定義
	 */
	public void setDefinitionId(String definitionId) {
		this.definitionId = definitionId;
	}

	/**
	 * Entityの表示色を取得します。
	 * @return Entityの表示色
	 */
	public String getEntityColor() {
		return entityColor;
	}

	/**
	 * Entityの表示色を設定します。
	 * @param entityColor Entityの表示色
	 */
	public void setEntityColor(String entityColor) {
		this.entityColor = entityColor;
	}

	/**
	 * カレンダーの検索方法を取得します。
	 * @return カレンダーの検索方法
	 */
	public CalendarSearchType getCalendarSearchType() {
		return calendarSearchType;
	}

	/**
	 * カレンダーの検索方法を設定します。
	 * @param calendarSearchType カレンダーの検索方法
	 */
	public void setCalendarSearchType(CalendarSearchType calendarSearchType) {
		this.calendarSearchType = calendarSearchType;
	}

	/**
	 * プロパティのIDを取得します。
	 * @return プロパティのID
	 */
	public String getPropertyId() {
		return propertyId;
	}

	/**
	 * プロパティのIDを設定します。
	 * @param propertyId プロパティのID
	 */
	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	/**
	 * FromプロパティのIDを取得します。
	 * @return FromプロパティのID
	 */
	public String getFromPropertyId() {
		return fromPropertyId;
	}

	/**
	 * FromプロパティのIDを設定します。
	 * @param fromPropertyId FromプロパティのID
	 */
	public void setFromPropertyId(String fromPropertyId) {
		this.fromPropertyId = fromPropertyId;
	}

	/**
	 * ToプロパティのIDを取得します。
	 * @return ToプロパティのID
	 */
	public String getToPropertyId() {
		return toPropertyId;
	}

	/**
	 * ToプロパティのIDを設定します。
	 * @param toPropertyId ToプロパティのID
	 */
	public void setToPropertyId(String toPropertyId) {
		this.toPropertyId = toPropertyId;
	}

	/**
	 * 詳細アクション名を取得します。
	 * @return 詳細アクション名
	 */
	public String getViewAction() {
		return viewAction;
	}

	/**
	 * 詳細アクション名を設定します。
	 * @param viewAction 詳細アクション名
	 */
	public void setViewAction(String viewAction) {
		this.viewAction = viewAction;
	}

	/**
	 * 追加アクション名を取得します。
	 * @return 追加アクション名
	 */
	public String getAddAction() {
		return addAction;
	}

	/**
	 * 追加アクション名を設定します。
	 * @param addAction 追加アクション名
	 */
	public void setAddAction(String addAction) {
		this.addAction = addAction;
	}

	/**
	 * ビュー名を取得します。
	 * @return ビュー名
	 */
	public String getViewName() {
		return viewName;
	}

	/**
	 * ビュー名を設定します。
	 * @param viewName ビュー名
	 */
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	/**
	 * 時間を表示するかを取得します。
	 * @return 時間を表示するか
	 */
	public Boolean getDisplayTime() {
		return displayTime;
	}

	/**
	 * 時間を表示するかを設定します。
	 * @param displayTime 時間を表示するか
	 */
	public void setDisplayTime(Boolean displayTime) {
		this.displayTime = displayTime;
	}

	/**
	 * レコードの新規登録を拒否するかを取得します。
	 * @return レコードの新規登録を拒否するか
	 */
	public Boolean getAllowNoEntryOfRecords() {
		return allowNoEntryOfRecords;
	}

	/**
	 * レコードの新規登録を拒否するかを設定します。
	 * @param allowNoEntryOfRecords レコードの新規登録を拒否するか
	 */
	public void setAllowNoEntryOfRecords(Boolean allowNoEntryOfRecords) {
		this.allowNoEntryOfRecords = allowNoEntryOfRecords;
	}

	/**
	 * 検索上限を取得します。
	 * @return 検索上限
	 */
	public Integer getLimit() {
	    return limit;
	}

	/**
	 * 検索上限を設定します。
	 * @param limit 検索上限
	 */
	public void setLimit(Integer limit) {
	    this.limit = limit;
	}

	/**
	 * フィルタ条件を取得します。
	 * @return フィルタ条件
	 */
	public String getFilterCondition() {
		return filterCondition;
	}

	/**
	 * フィルタ条件を設定します。
	 * @param filterCondition フィルタ条件
	 */
	public void setFilterCondition(String filterCondition) {
		this.filterCondition = filterCondition;
	}

	/**
	 * Entityの表示色（groovy）を取得します。
	 * @return Entityの表示色（groovy）
	 */
	public String getColorConfig() {
		return colorConfig;
	}

	/**
	 * Entityの表示色（groovy）を設定します。
	 * @param colorConfig Entityの表示色（groovy）
	 */
	public void setColorConfig(String colorConfig) {
		this.colorConfig = colorConfig;
	}


	/**
	 * カレンダーに表示するEntityの設定の内容を自身に反映します。
	 * @param item カレンダーに表示するEntityの設定
	 */
	public void applyConfig(EntityCalendarItem item) {
		EntityContext metaContext = EntityContext.getCurrentContext();
		EntityHandler entity = metaContext.getHandlerByName(item.getDefinitionName());
		this.definitionId = entity.getMetaData().getId();
		this.entityColor = item.getEntityColor();
		if (item.getPropertyName() != null) {
			PropertyHandler property = entity.getProperty(item.getPropertyName(), metaContext);
			if (property != null) this.propertyId = property.getId();
		}
		if (item.getFromPropertyName() != null) {
			PropertyHandler property = entity.getProperty(item.getFromPropertyName(), metaContext);
			if (property != null) this.fromPropertyId = property.getId();
		}
		if (item.getToPropertyName() != null) {
			PropertyHandler property = entity.getProperty(item.getToPropertyName(), metaContext);
			if (property != null) this.toPropertyId = property.getId();
		}

		this.calendarSearchType = item.getCalendarSearchType();
		this.viewAction = item.getViewAction();
		this.addAction = item.getAddAction();
		this.allowNoEntryOfRecords = item.getAllowNoEntryOfRecords();
		this.viewName = item.getViewName();
		this.displayTime = item.getDisplayTime();
		this.limit = item.getLimit();
		this.filterCondition = item.getFilterCondition();
		this.colorConfig = item.getColorConfig();
	}

	/**
	 * 自身の内容をカレンダーに表示するEntityの設定に反映します。
	 * @return カレンダーに表示するEntityの設定
	 */
	public EntityCalendarItem currentConfig() {

		EntityCalendarItem item = new EntityCalendarItem();

		EntityContext metaContext = EntityContext.getCurrentContext();
		EntityHandler eHandler = metaContext.getHandlerById(definitionId);
		if (eHandler != null) {
			MetaEntity entity = eHandler.getMetaData();
			item.setDefinitionName(entity.getName());

			if (propertyId != null) {
				MetaProperty property = eHandler.getPropertyById(propertyId, metaContext).getMetaData();
				if (property != null) {
					item.setPropertyName(property.getName());
				}
			}
			if (fromPropertyId != null) {
				MetaProperty property = eHandler.getPropertyById(fromPropertyId, metaContext).getMetaData();
				if (property != null) {
					item.setFromPropertyName(property.getName());
				}
			}
			if (toPropertyId != null) {
				MetaProperty property = eHandler.getPropertyById(toPropertyId, metaContext).getMetaData();
				if (property != null) {
					item.setToPropertyName(property.getName());
				}
			}
		}

		item.setEntityColor(entityColor);
		item.setCalendarSearchType(calendarSearchType);
		item.setViewAction(viewAction);
		item.setAddAction(addAction);
		item.setAllowNoEntryOfRecords(allowNoEntryOfRecords);
		item.setViewName(viewName);
		item.setDisplayTime(displayTime);
		item.setLimit(limit);
		item.setFilterCondition(filterCondition);
		item.setColorConfig(colorConfig);
		return item;
	}


	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

	public EntityCalendarItemRuntime createRuntime() {
		return new EntityCalendarItemRuntime();
	}

	public class EntityCalendarItemRuntime {

		private static final String SCRIPT_PREFIX = "EntityCalendarItemRuntime_colorConfig";

		private Script colorConfigScript;
		private String definitionName;

		public EntityCalendarItemRuntime() {

			EntityContext metaContext = EntityContext.getCurrentContext();
			EntityHandler eHandler = metaContext.getHandlerById(definitionId);

			if (eHandler == null) {
				throw new NullPointerException("definitionId:" + definitionId + " MetaEntity not found");
			}

			if (StringUtil.isNotEmpty(colorConfig)) {
				ScriptEngine scriptEngine = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();
				colorConfigScript = scriptEngine.createScript(colorConfig, SCRIPT_PREFIX);
			}

			definitionName = eHandler.getMetaData().getName();
		}

		public MetaEntityCalendarItem getMetaData() {
			return MetaEntityCalendarItem.this;
		}

		public String getDefinitionName() {
			return definitionName;
		}

		public String getColor(Entity entity) {
			ScriptContext sc = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine().newScriptContext();

			sc.setAttribute("today", DateUtil.getCurrentTimestamp());
			sc.setAttribute("propertyName", this.getMetaData().currentConfig().getPropertyName());
			sc.setAttribute("fromPropertyName", this.getMetaData().currentConfig().getFromPropertyName());
			sc.setAttribute("toPropertyName", this.getMetaData().currentConfig().getToPropertyName());
			sc.setAttribute("entity", entity);

			Object color = colorConfigScript.eval(sc);

			if (color == null) {
				return null;
			} else {
				return color.toString();
			}
		}
	}
}
