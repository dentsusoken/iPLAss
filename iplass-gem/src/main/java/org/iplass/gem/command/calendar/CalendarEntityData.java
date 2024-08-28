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

package org.iplass.gem.command.calendar;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.view.calendar.EntityCalendarItem;

/**
 * カレンダーに表示するEntityのデータ
 *
 * @author lis3wg
 */
public class CalendarEntityData {
	/** Entity定義名 */
	private String defName;

	/** OID */
	private String oid;

	/** バージョン */
	private Long version;

	/** 表示用の名前 */
	private String name;

	/** 時間 */
	private String time;

	/** 時間を表示するか */
	private boolean dispTime;

	/** 表示用のアクション */
	private String viewAction;

	/** レコードの新規登録を許可するか */
	private boolean allowNewRecordRegistration;

	/** ビュー名 */
	private String viewName;

	/**
	 * コンストラクタ
	 *
	 * @param entity Entity
	 * @param time   時間
	 * @param item   カレンダーに表示するEntityの定義
	 */
	public CalendarEntityData(Entity entity, String time, EntityCalendarItem item) {
		this.defName = entity.getDefinitionName();
		this.oid = entity.getOid();
		this.version = entity.getVersion();
		this.name = entity.getName();
		this.time = time;
		this.dispTime = item.getDisplayTime();
		this.viewAction = item.getViewAction();
		this.allowNewRecordRegistration = item.getAllowNewRecordRegistration() != null
				? item.getAllowNewRecordRegistration()
				: true;
		this.viewName = item.getViewName();
	}

	/**
	 * Entity定義名を取得します。
	 *
	 * @return Entity定義名
	 */
	public String getDefName() {
		return defName;
	}

	/**
	 * Entity定義名を設定します。
	 *
	 * @param defName Entity定義名
	 */
	public void setDefName(String defName) {
		this.defName = defName;
	}

	/**
	 * OIDを取得します。
	 *
	 * @return OID
	 */
	public String getOid() {
		return oid;
	}

	/**
	 * OIDを設定します。
	 *
	 * @param oid OID
	 */
	public void setOid(String oid) {
		this.oid = oid;
	}

	/**
	 * バージョンを取得します。
	 *
	 * @return バージョン
	 */
	public Long getVersion() {
		return version;
	}

	/**
	 * バージョンを設定します。
	 *
	 * @param version バージョン
	 */
	public void setVersion(Long version) {
		this.version = version;
	}

	/**
	 * 表示用の名前を取得します。
	 *
	 * @return 表示用の名前
	 */
	public String getName() {
		return name;
	}

	/**
	 * 表示用の名前を設定します。
	 *
	 * @param name 表示用の名前
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 時間を取得します。
	 *
	 * @return 時間
	 */
	public String getTime() {
		return time;
	}

	/**
	 * 時間を設定します。
	 *
	 * @param time 時間
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * 時間を表示するかを取得します。
	 *
	 * @return 時間を表示するか
	 */
	public boolean isDispTime() {
		return dispTime;
	}

	/**
	 * 時間を表示するかを設定します。
	 *
	 * @param dispTime 時間を表示するか
	 */
	public void setDispTime(boolean dispTime) {
		this.dispTime = dispTime;
	}

	/**
	 * 表示用のアクションを取得します。
	 *
	 * @return 表示用のアクション
	 */
	public String getViewAction() {
		return viewAction;
	}

	/**
	 * 表示用のアクションを設定します。
	 *
	 * @param viewAction 表示用のアクション
	 */
	public void setViewAction(String viewAction) {
		this.viewAction = viewAction;
	}

	/**
	 * レコードの新規登録を許可するかを取得します。
	 *
	 * @return レコードの新規登録を許可するか
	 */
	public boolean getAllowNewRecordRegistration() {
		return allowNewRecordRegistration;
	}

	/**
	 * レコードの新規登録を許可するかを設定します。
	 *
	 * @param allowNewRecordRegistration レコードの新規登録を許可するか
	 */
	public void setAllowNewRecordRegistration(boolean allowNewRecordRegistration) {
		this.allowNewRecordRegistration = allowNewRecordRegistration;
	}

	/**
	 * ビュー名を取得します。
	 *
	 * @return ビュー名
	 */
	public String getViewName() {
		return viewName;
	}

	/**
	 * ビュー名を設定します。
	 *
	 * @param viewName ビュー名
	 */
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

}
