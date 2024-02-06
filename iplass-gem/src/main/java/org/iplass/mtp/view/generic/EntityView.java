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

package org.iplass.mtp.view.generic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.mtp.definition.Definition;

/**
 * 画面定義
 * @author lis3wg
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class EntityView implements Definition {

	/** シリアルバージョンID */
	private static final long serialVersionUID = -3739791401558711906L;

	/** 定義名(=Entity定義名) */
	private String name;

	/** 表示名 */
	private String displayName;

	/** 説明 */
	private String description;

	/** Entity名 */
	private String definitionName;

	/** FormView */
	@MultiLang(isMultiLangValue = false)
	private List<FormView> views;

	/** Viewの自動生成設定 */
	private List<ViewControlSetting> viewControlSettings;

	/**
	 * デフォルトコンストラクタ
	 */
	public EntityView() {
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}

	@Override
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Entity定義の名前を取得します。
	 * @return Entity定義の名前
	 */
	public String getDefinitionName() {
		return definitionName;
	}

	/**
	 * Entity定義の名前を設定します。
	 * @param definitionId Entity定義の名前
	 */
	public void setDefinitionName(String definitionName) {
		this.definitionName = definitionName;
	}

	/**
	 * FormViewを取得します。
	 * @return FormView
	 */
	public List<FormView> getViews() {
		if (this.views == null) this.views = new ArrayList<>();
		return views;
	}

	/**
	 * FormViewを設定します。
	 * @param views FormView
	 */
	public void setViews(List<FormView> views) {
		this.views = views;
	}

	/**
	 * FormViewを追加します。
	 * @param view FormView
	 */
	public void addView(FormView view) {
		getViews().add(view);
	}

	/**
	 * FormViewを削除します。
	 * @param view FormView
	 */
	public void removeView(FormView view) {
		if (this.views != null) {
			this.views.remove(view);
		}
	}

	public List<ViewControlSetting> getViewControlSettings() {
		if (this.viewControlSettings == null) this.viewControlSettings = new ArrayList<>();
		return viewControlSettings;
	}

	public void setViewControlSettings(List<ViewControlSetting> viewControlSettings) {
		this.viewControlSettings = viewControlSettings;
	}

	public void addViewControlSetting(ViewControlSetting viewControlSetting) {
		getViewControlSettings().add(viewControlSetting);
	}

	/**
	 * 詳細画面のデフォルトViewを取得します。
	 * @return 詳細画面のFormView
	 */
	public DetailFormView getDefaultDetailFormView() {
		return getDetailFormViewByName(null);
	}

	/**
	 * 詳細画面の指定Viewを取得します。
	 * @param name 詳細画面のFormViewの名前
	 * @return 詳細画面のFormView
	 */
	public DetailFormView getDetailFormView(String name) {
//		if (name == null) throw new IllegalArgumentException("name is null.");
		return getDetailFormViewByName(name);
	}

	/**
	 * Viewの名前を元に詳細画面のViewを取得します
	 * @param name 詳細画面のFormViewの名前
	 * @return 詳細画面のFormView
	 */
	private DetailFormView getDetailFormViewByName(String name) {
		DetailFormView view = null;
		if (this.getViews() != null && this.getViews().size() > 0) {
			for (FormView fv : this.getViews()) {
				if (fv instanceof DetailFormView) {
//					if (name == null) {
					if (name == null || name.isEmpty()) {
						if (fv.getName() == null || fv.getName().equals("")) {
							view = (DetailFormView) fv;
						}
					} else {
						if (name.equals(fv.getName())) {
							view = (DetailFormView) fv;
						}
					}
					if (view != null) break;
				}
			}
		}
		return view;
	}

	/**
	 * 詳細画面のView名の一覧を取得します。
	 * @return 詳細画面のView名の一覧
	 */
	public String[] getDetailFormViewNames() {
		List<String> names = new ArrayList<>();
		for (FormView fv : this.getViews()) {
			if (fv instanceof DetailFormView) {
				if (fv.getName() == null) {
					names.add("");
				} else {
					names.add(fv.getName());
				}
			}
		}
		return names.toArray(new String[names.size()]);
	}

	/**
	 * 詳細画面のデフォルトFormViewを設定します。
	 * @param formView 詳細画面のFormView
	 */
	public void setDefaultDetailFormView(DetailFormView formView) {
		DetailFormView fv = getDefaultDetailFormView();
		if (fv != null) {
			//既にあれば上書き
			this.removeView(fv);
		}
		this.addView(formView);
	}

	/**
	 * 詳細画面のFormViewを設定します。
	 * @param formView 詳細画面のFormView
	 */
	public void setDetailFormView(DetailFormView formView) {
		DetailFormView fv = getDetailFormView(formView.getName());
		if (fv != null) {
			//既にあれば上書き
			this.removeView(fv);
		}
		this.addView(formView);
	}

	/**
	 * 詳細画面のFormViewを削除します
	 * @param name 詳細画面のFormViewの名前
	 */
	public void removeDetailForView(String name) {
		DetailFormView form = null;
		if (name == null || name.equals("")) {
			form = getDefaultDetailFormView();
		} else {
			form = getDetailFormView(name);
		}
		if (form != null) removeDetailForView(form);
	}

	/**
	 * 詳細画面のFormViewを削除します
	 * @param formView 詳細画面のFormView
	 */
	public void removeDetailForView(DetailFormView formView) {
		if (formView == null) throw new IllegalArgumentException("FormView is null.");
		removeView(formView);
	}

	/**
	 * 検索画面のデフォルトViewを取得します。
	 * @return 検索画面のFormView
	 */
	public SearchFormView getDefaultSearchFormView() {
		return getSearchFormViewByName(null);
	}

	/**
	 * 検索画面の指定Viewを取得します。
	 * @return 検索画面のFormView
	 */
	public SearchFormView getSearchFormView(String name) {
//		if (name == null) throw new IllegalArgumentException("name is null.");
		return getSearchFormViewByName(name);
	}

	/**
	 * Viewの名前を元に検索画面のViewを取得します。
	 * @param name 検索画面のFormViewの名前
	 * @return 検索画面のFormView
	 */
	private SearchFormView getSearchFormViewByName(String name) {
		SearchFormView view = null;
		if (this.getViews() != null && this.getViews().size() > 0) {
			for (FormView fv : this.getViews()) {
				if (fv instanceof SearchFormView) {
//					if (name == null) {
					if (name == null || name.isEmpty()) {
						if (fv.getName() == null || fv.getName().equals("")) {
							view = (SearchFormView) fv;
						}
					} else {
						if (name.equals(fv.getName())) {
							view = (SearchFormView) fv;
						}
					}
					if (view != null) break;
				}
			}
		}
		return view;
	}

	/**
	 * 検索画面のView名の一覧を取得します。
	 * @return 検索画面のView名の一覧
	 */
	public String[] getSearchFormViewNames() {
		List<String> names = new ArrayList<>();
		for (FormView fv : this.getViews()) {
			if (fv instanceof SearchFormView) {
				if (fv.getName() == null) {
					names.add("");
				} else {
					names.add(fv.getName());
				}
			}
		}
		return names.toArray(new String[names.size()]);
	}

	/**
	 * 検索画面のデフォルトFormViewを設定します。
	 * @param formView 検索画面のFormView
	 */
	public void setDefaultSearchFormView(SearchFormView formView) {
		SearchFormView fv = getDefaultSearchFormView();
		if (fv != null) {
			//既にあれば上書き
			this.removeView(fv);
		}
		this.addView(formView);
	}

	/**
	 * 検索画面のFormViewを設定します。
	 * @param formView 検索画面のFormView
	 */
	public void setSearchFormView(SearchFormView formView) {
		SearchFormView fv = getSearchFormView(formView.getName());
		if (fv != null) {
			//既にあれば上書き
			this.removeView(fv);
		}
		this.addView(formView);
	}

	/**
	 * 検索画面のFormViewを削除します
	 * @param name 検索画面のFormViewの名前
	 */
	public void removeSearchForView(String name) {
		SearchFormView form = null;
		if (name == null || name.equals("")) {
			form = getDefaultSearchFormView();
		} else {
			form = getSearchFormView(name);
		}
		if (form != null) removeSearchForView(form);
	}

	/**
	 * 検索画面のFormViewを削除します
	 * @param formView 検索画面のFormView
	 */
	public void removeSearchForView(SearchFormView formView) {
		if (formView == null) throw new IllegalArgumentException("FormView is null.");
		removeView(formView);
	}

	/**
	 * 一括更新画面のデフォルトViewを取得します。
	 * @return 一括更新画面のFormView
	 */
	public BulkFormView getDefaultBulkFormView() {
		return getBulkFormViewByName(null);
	}

	/**
	 * 一括更新画面の指定Viewを取得します。
	 * @param name 一括更新画面のFormViewの名前
	 * @return 一括更新画面のFormView
	 */
	public BulkFormView getBulkFormView(String name) {
//		if (name == null) throw new IllegalArgumentException("name is null.");
		return getBulkFormViewByName(name);
	}

	/**
	 * Viewの名前を元に一括更新画面のViewを取得します
	 * @param name 一括更新画面のFormViewの名前
	 * @return 一括更新画面のFormView
	 */
	private BulkFormView getBulkFormViewByName(String name) {
		BulkFormView view = null;
		if (this.getViews() != null && this.getViews().size() > 0) {
			for (FormView fv : this.getViews()) {
				if (fv instanceof BulkFormView) {
//					if (name == null) {
					if (name == null || name.isEmpty()) {
						if (fv.getName() == null || fv.getName().equals("")) {
							view = (BulkFormView) fv;
						}
					} else {
						if (name.equals(fv.getName())) {
							view = (BulkFormView) fv;
						}
					}
					if (view != null) break;
				}
			}
		}
		return view;
	}

	/**
	 * 一括更新画面のView名の一覧を取得します。
	 * @return 一括更新画面のView名の一覧
	 */
	public String[] getBulkFormViewNames() {
		List<String> names = new ArrayList<>();
		for (FormView fv : this.getViews()) {
			if (fv instanceof BulkFormView) {
				if (fv.getName() == null) {
					names.add("");
				} else {
					names.add(fv.getName());
				}
			}
		}
		return names.toArray(new String[names.size()]);
	}

	/**
	 * 一括更新画面のデフォルトFormViewを設定します。
	 * @param formView 一括更新画面のFormView
	 */
	public void setDefaultBulkFormView(BulkFormView formView) {
		BulkFormView fv = getDefaultBulkFormView();
		if (fv != null) {
			//既にあれば上書き
			this.removeView(fv);
		}
		this.addView(formView);
	}

	/**
	 * 一括更新画面のFormViewを設定します。
	 * @param formView 一括更新画面のFormView
	 */
	public void setBulkFormView(BulkFormView formView) {
		BulkFormView fv = getBulkFormView(formView.getName());
		if (fv != null) {
			//既にあれば上書き
			this.removeView(fv);
		}
		this.addView(formView);
	}

	/**
	 * 一括更新画面のFormViewを削除します
	 * @param name 一括更新画面のFormViewの名前
	 */
	public void removeBulkForView(String name) {
		BulkFormView form = null;
		if (name == null || name.equals("")) {
			form = getDefaultBulkFormView();
		} else {
			form = getBulkFormView(name);
		}
		if (form != null) removeDetailForView(form);
	}

	/**
	 * 一括更新画面のFormViewを削除します
	 * @param formView 一括更新画面のFormView
	 */
	public void removeDetailForView(BulkFormView formView) {
		if (formView == null) throw new IllegalArgumentException("FormView is null.");
		removeView(formView);
	}

	/**
	 * 詳細画面のFormViewを自動生成するかを返します。
	 *
	 * @param viewName View名
	 * @return true 自動生成
	 */
	public boolean isAutoGenerateDetailView(String viewName) {
		Optional<ViewControlSetting> setting = getViewControlSetting(viewName);
		if (setting.isPresent()) {
			return setting.get().isAutoGenerateDetailView();
		} else {
			return false;
		}
	}

	/**
	 * 検索画面のFormViewを自動生成するかを返します。
	 *
	 * @param viewName View名
	 * @return true 自動生成
	 */
	public boolean isAutoGenerateSearchView(String viewName) {
		Optional<ViewControlSetting> setting = getViewControlSetting(viewName);
		if (setting.isPresent()) {
			return setting.get().isAutoGenerateSearchView();
		} else {
			return false;
		}
	}

	/**
	 * 一括更新画面のFormViewを自動生成するかを返します。
	 *
	 * @param viewName View名
	 * @return true 自動生成
	 */
	public boolean isAutoGenerateBulkView(String viewName) {
		Optional<ViewControlSetting> setting = getViewControlSetting(viewName);
		if (setting.isPresent()) {
			return setting.get().isAutoGenerateBulkView();
		} else {
			return false;
		}
	}

	private Optional<ViewControlSetting> getViewControlSetting(final String viewName) {
		return getViewControlSettings().stream()
					.filter(s -> {
						//don't use StringUtil
						if (viewName == null || viewName.isEmpty()) {
							return s.getName() == null || s.getName().isEmpty();
						} else {
							return viewName.equals(s.getName());
						}
					})
					.findFirst();
	}
}
