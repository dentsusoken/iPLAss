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

package org.iplass.mtp.impl.view.generic;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.iplass.mtp.impl.definition.DefinableMetaData;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.metadata.BaseRootMetaData;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.view.generic.ViewControlSetting;
import org.iplass.mtp.view.generic.EntityView;
import org.iplass.mtp.view.generic.FormView;

/**
 * 画面定義
 * @author lis3wg
 */
@XmlRootElement
public class MetaEntityView extends BaseRootMetaData implements DefinableMetaData<EntityView> {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -2770147037258806116L;

	/** Entity定義のID */
	private String definitionId;

	/** レイアウト情報 */
	private List<MetaFormView> views;

	private List<MetaViewControlSetting> viewControlSettings;

	/**
	 * Entity定義のIDを取得します。
	 * @return Entity定義のID
	 */
	public String getDefinitionId() {
		return definitionId;
	}

	/**
	 * Entity定義のIDを設定します。
	 * @param definitionId Entity定義のID
	 */
	public void setDefinitionId(String definitionId) {
		this.definitionId = definitionId;
	}

	/**
	 * レイアウト情報を取得します。
	 * @return レイアウト情報
	 */
	public List<MetaFormView> getViews() {
		if (this.views == null) this.views = new ArrayList<MetaFormView>();
		return views;
	}

	/**
	 * レイアウト情報を設定します。
	 * @param views レイアウト情報
	 */
	public void setViews(List<MetaFormView> views) {
		this.views = views;
	}

	/**
	 * レイアウト情報を追加します。
	 * @param view レイアウト情報
	 */
	public void addView(MetaFormView view) {
		getViews().add(view);
	}

	public List<MetaViewControlSetting> getViewControlSettings() {
		if (this.viewControlSettings == null) this.viewControlSettings = new ArrayList<>();
		return viewControlSettings;
	}

	public void setViewControlSettings(List<MetaViewControlSetting> viewControlSettings) {
		this.viewControlSettings = viewControlSettings;
	}

	public void addViewControlSetting(MetaViewControlSetting viewControlSetting) {
		getViewControlSettings().add(viewControlSetting);
	}

	@Override
	public MetaEntityView copy() {
		return ObjectUtil.deepCopy(this);
	}

	/**
	 * ランタイムを生成します。
	 * @return ランタイム
	 */
	public EntityViewRuntime createRuntime(MetaDataConfig metaDataConfig) {
		return new EntityViewRuntime(this);
	}

	/**
	 * 画面定義の内容を自身に適用します。
	 * @param entityView 画面定義
	 */
	public void applyConfig(EntityView entityView) {
		//name -> id
		EntityContext eContext = EntityContext.getCurrentContext();
		MetaEntity metaEntity = eContext.getHandlerByName(entityView.getDefinitionName()).getMetaData();

		this.name = entityView.getName();
		this.displayName = entityView.getDisplayName();
		this.description = entityView.getDescription();

		this.definitionId = metaEntity.getId();

		if (entityView.getViews().size() > 0) {
			for (FormView view : entityView.getViews()) {
				MetaFormView mView = MetaFormView.createInstance(view);
				mView.applyConfig(view, definitionId);
				this.addView(mView);
			}

			//追加/更新の度に順序が変わる、構成管理上履歴が追いにくくなるので型とView名でソートしておく
			this.getViews().sort(Comparator.comparing(MetaFormView::getTypeName)
					.thenComparing(Comparator.comparing(MetaFormView::getName, Comparator.nullsFirst(Comparator.naturalOrder()))));
		}

		if (!entityView.getViewControlSettings().isEmpty()) {
			for (ViewControlSetting viewControlSetting : entityView.getViewControlSettings()) {
				MetaViewControlSetting meta = new MetaViewControlSetting();
				meta.applyConfig(viewControlSetting);
				addViewControlSetting(meta);
			}
		}
	}

	/**
	 * 自身の内容を画面定義に適用します。
	 * @return 画面定義
	 */
	public EntityView currentConfig() {
		EntityView entityView = new EntityView();

		entityView.setName(name);
		entityView.setDisplayName(displayName);
		entityView.setDescription(description);

		//id -> name
		EntityContext eContext = EntityContext.getCurrentContext();
		EntityHandler eh = eContext.getHandlerById(this.definitionId);
		if (eh != null) {
			entityView.setDefinitionName(eh.getMetaData().getName());
		}

		if (this.getViews().size() > 0) {
			for (MetaFormView mv : this.getViews()) {
				FormView formView = mv.currentConfig(definitionId);
				entityView.addView(formView);
			}
		}

		if (!this.getViewControlSettings().isEmpty()) {
			for (MetaViewControlSetting meta : this.getViewControlSettings()) {
				ViewControlSetting viewControlSetting = meta.currentConfig();
				entityView.addViewControlSetting(viewControlSetting);
			}
		}
		return entityView;
	}
}
