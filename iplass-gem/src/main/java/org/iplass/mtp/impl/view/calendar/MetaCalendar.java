/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.impl.definition.DefinableMetaData;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.metadata.BaseMetaDataRuntime;
import org.iplass.mtp.impl.metadata.BaseRootMetaData;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.metadata.MetaDataRuntime;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.view.calendar.MetaEntityCalendarItem.EntityCalendarItemRuntime;
import org.iplass.mtp.view.calendar.EntityCalendar;
import org.iplass.mtp.view.calendar.EntityCalendarItem;
import org.iplass.mtp.view.calendar.EntityCalendar.CalendarType;


/**
 * カレンダー定義
 * @author lis3wg
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class MetaCalendar extends BaseRootMetaData implements DefinableMetaData<EntityCalendar> {

	/** SerialVersionUID */
	private static final long serialVersionUID = 7232009428982319568L;

	/** 期間タイプ */
	private CalendarType type;

	/** Entityの設定 */
	private List<MetaEntityCalendarItem> items;

	/**
	 * Entityの設定を取得します。
	 * @return Entityの設定
	 */
	public List<MetaEntityCalendarItem> getItems() {
		if (items == null) items = new ArrayList<MetaEntityCalendarItem>();
		return items;
	}

	/**
	 * Entityの設定を設定します。
	 * @param items Entityの設定
	 */
	public void setItems(List<MetaEntityCalendarItem> items) {
		this.items = items;
	}

	/**
	 * 期間タイプを取得します。
	 * @return 期間タイプ
	 */
	public CalendarType getType() {
		return type;
	}

	/**
	 * 期間タイプを設定します。
	 * @param type 期間タイプ
	 */
	public void setType(CalendarType type) {
		this.type = type;
	}

	/**
	 * Entityの設定を追加します。
	 * @param item Entityの設定
	 */
	public void addItem(MetaEntityCalendarItem item) {
		getItems().add(item);
	}

	/**
	 * カレンダー定義の内容を自身に反映します。
	 * @param calendar カレンダー定義
	 */
	public void applyConfig(EntityCalendar calendar) {
		this.name = calendar.getName();
		this.displayName = calendar.getDisplayName();
		this.description = calendar.getDescription();
		this.type = calendar.getType();
		for (EntityCalendarItem item : calendar.getItems()) {
			MetaEntityCalendarItem mItem = new MetaEntityCalendarItem();
			mItem.applyConfig(item);
			addItem(mItem);
		}

		// 言語毎の文字情報設定
		localizedDisplayNameList = I18nUtil.toMeta(calendar.getLocalizedDisplayNameList());
	}

	/**
	 * 自身の内容をカレンダー定義に反映します。
	 * @return カレンダー定義
	 */
	public EntityCalendar currentConfig() {
		EntityCalendar calendar = new EntityCalendar();
		calendar.setName(name);
		calendar.setDisplayName(displayName);
		calendar.setDescription(description);
		calendar.setType(type);
		for (MetaEntityCalendarItem mItem : getItems()) {
			EntityCalendarItem item = mItem.currentConfig();
			calendar.addItem(item);
		}

		calendar.setLocalizedDisplayNameList(I18nUtil.toDef(localizedDisplayNameList));

		return calendar;
	}

	@Override
	public MetaCalendar copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public MetaDataRuntime createRuntime(MetaDataConfig metaDataConfig) {
		return new CalendarHandler();
	}

	/**
	 * ランタイム
	 * @author lis3wg
	 */
	public class CalendarHandler extends BaseMetaDataRuntime {

		public CalendarHandler() {
			try {
				if (items != null) {
					for (MetaEntityCalendarItem item :items) {
						//ItemのcreateRuntimeを実行して有効性チェック
						item.createRuntime();
					}
				}
			} catch (RuntimeException e) {
				setIllegalStateException(e);
			}
		}

		@Override
		public MetaCalendar getMetaData() {
			return MetaCalendar.this;
		}

		public String getColorConfigResult(Entity entity) {
			String result = null;

			if (items != null) {
				for (MetaEntityCalendarItem item :items) {

					EntityCalendarItemRuntime runtime = item.createRuntime();

					if (entity.getDefinitionName().equals(runtime.getDefinitionName())) {
						result = runtime.getColor(entity);
						break;
					}
				}
			}
			return result;
		}

	}
}
