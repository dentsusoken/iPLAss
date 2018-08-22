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

package org.iplass.gem.command.generic.detail;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.view.generic.DetailFormView;

/**
 * 詳細画面表示用データ
 * @author lis3wg
 */
public class DetailFormViewData {

	/** Entity定義 */
	private EntityDefinition entityDefinition;

	/** Entity */
	private Entity entity;

	/** 詳細画面定義 */
	private DetailFormView view;

	/** 処理タイプ */
	private String execType;

	/**
	 * コンストラクタ
	 */
	public DetailFormViewData() {
	}

	/**
	 * コンストラクタ
	 */
	public DetailFormViewData(DetailCommandContext context) {
		this.entityDefinition = context.getEntityDefinition();
		this.view = context.getView();
	}

	/**
	 * Entity定義を取得します。
	 * @return Entity定義
	 */
	public EntityDefinition getEntityDefinition() {
		return entityDefinition;
	}

	/**
	 * Entity定義を設定します。
	 * @param entityDefinition Entity定義
	 */
	public void setEntityDefinition(EntityDefinition entityDefinition) {
		this.entityDefinition = entityDefinition;
	}

	/**
	 * Entityを取得します。
	 * @return Entity
	 */
	public Entity getEntity() {
		return entity;
	}

	/**
	 * Entityを設定します。
	 * @param entity Entity
	 */
	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	/**
	 * 詳細画面定義を取得します。
	 * @return 詳細画面定義
	 */
	public DetailFormView getView() {
		return view;
	}

	/**
	 * 詳細画面定義を設定します。
	 * @param view 詳細画面定義
	 */
	public void setView(DetailFormView view) {
		this.view = view;
	}

	/**
	 * 処理タイプを取得します。
	 * @return 処理タイプ
	 */
	public String getExecType() {
		return execType;
	}

	/**
	 * 処理タイプを設定します。
	 * @param execType 処理タイプ
	 */
	public void setExecType(String execType) {
		this.execType = execType;
	}


}
