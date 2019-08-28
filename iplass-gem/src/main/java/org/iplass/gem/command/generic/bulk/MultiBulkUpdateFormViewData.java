/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.gem.command.generic.bulk;

import java.util.HashMap;
import java.util.Map;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.view.generic.BulkFormView;

public class MultiBulkUpdateFormViewData{

	/** Entity定義 */
	private EntityDefinition entityDefinition;

	/** 一括更新画面定義 */
	private BulkFormView view;

	/** 選択された行番号とエンティティ */
	private Map<Integer, Entity> selected;

	public MultiBulkUpdateFormViewData(MultiBulkCommandContext context) {
		this.entityDefinition = context.getEntityDefinition();
		this.view = context.getView();
		this.selected = new HashMap<>();
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
	 * 一括更新画面定義を取得します。
	 * @return 一括更新画面定義
	 */
	public BulkFormView getView() {
		return view;
	}

	/**
	 * 一括更新画面定義を設定します。
	 * @param view 一括更新画面定義
	 */
	public void setView(BulkFormView view) {
		this.view = view;
	}

	public Map<Integer, Entity> getSelected() {
		return selected;
	}

	public void setSelected(Integer row, Entity entity) {
		this.selected.put(row, entity);
	}
}
