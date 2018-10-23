/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.gem.command;

import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.iplass.gem.EntityViewHelper;
import org.iplass.gem.GemConfigService;
import org.iplass.gem.Skin;
import org.iplass.gem.Theme;
import org.iplass.gem.command.generic.detail.DetailFormViewData;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.tenant.gem.TenantGemInfo;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.EntityView;
import org.iplass.mtp.view.generic.EntityViewManager;
import org.iplass.mtp.view.generic.FormView;
import org.iplass.mtp.view.generic.common.WebApiAutocompletionSetting;
import org.iplass.mtp.view.generic.editor.AutoNumberPropertyEditor;
import org.iplass.mtp.view.generic.editor.ExpressionPropertyEditor;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.UserPropertyEditor;
import org.iplass.mtp.view.generic.element.Element;
import org.iplass.mtp.view.generic.element.Element.EditDisplayType;
import org.iplass.mtp.view.generic.element.property.PropertyItem;
import org.iplass.mtp.view.generic.element.section.DefaultSection;
import org.iplass.mtp.view.generic.element.section.MassReferenceSection;
import org.iplass.mtp.view.generic.element.section.SearchConditionSection.CsvDownloadSpecifyCharacterCode;
import org.iplass.mtp.view.generic.element.section.Section;
import org.iplass.mtp.view.top.TopViewDefinition;
import org.iplass.mtp.view.top.TopViewDefinitionManager;
import org.iplass.mtp.view.top.parts.CsvDownloadSettingsParts;
import org.iplass.mtp.view.top.parts.TopViewParts;
import org.iplass.mtp.web.template.TemplateUtil;

public class ViewUtil {

	/**
	 * テナントに設定されたスキンを返します。
	 * 未設定の場合、デフォルトを返します。
	 *
	 * @return スキン
	 */
	public static Skin getSkin() {
		Skin skin = getCurrentSkin();
		if (skin == null) {
			List<Skin> skins = getSkinList();
			if (skins != null && !skins.isEmpty()) {
				skin = skins.get(0);
			}
		}
		return skin;
	}

	/**
	 * テナントに設定されたスキンを返します。
	 * 未設定の場合、nullを返します。
	 *
	 * @return スキン
	 */
	private static Skin getCurrentSkin() {
		Tenant tenant = TemplateUtil.getTenant();
		String skinName = getTenantGemInfo(tenant).getSkin();
		if (StringUtil.isNotEmpty(skinName)) {
			//Skinの検索
			for (Skin skin : getSkinList()) {
				if (skinName.equals(skin.getSkinName())) {
					return skin;
				}
			}
		}
		return null;
	}

	private static List<Skin> getSkinList() {
		List<Skin> skins = ServiceRegistry.getRegistry().getService(GemConfigService.class).getSkins();
		if (skins == null) {
			skins = Collections.emptyList();
		}
		return skins;
	}

	/**
	 * テナントに設定されたテーマを返します。
	 * 未設定の場合、デフォルトを返します。
	 *
	 * @return テーマ
	 */
	public static Theme getTheme() {
		Theme theme = getCurrentTheme();
		if (theme == null) {
			List<Theme> themes = getThemeList();
			if (themes != null && !themes.isEmpty()) {
				theme = themes.get(0);
			}
		}
		return theme;
	}

	/**
	 * テナントに設定されたテーマを返します。
	 * 未設定の場合、nullを返します。
	 *
	 * @return テーマ
	 */
	private static Theme getCurrentTheme() {
		Tenant tenant = TemplateUtil.getTenant();
		String themeName = getTenantGemInfo(tenant).getTheme();
		if (StringUtil.isNotEmpty(themeName)) {
			//Themeの検索
			for (Theme theme : getThemeList()) {
				if (themeName.equals(theme.getThemeName())) {
					return theme;
				}
			}
		}
		return null;
	}

	private static List<Theme> getThemeList() {
		List<Theme> themes = ServiceRegistry.getRegistry().getService(GemConfigService.class).getThemes();
		if (themes == null) {
			themes = Collections.emptyList();
		}
		return themes;
	}

	/**
	 * @deprecated {@link #getSkinImagePath()} を利用してください
	 */
	@Deprecated
	public static String getImagePath() {
		return getSkinImagePath();
	}
	public static String getSkinImagePath() {
		String contentPath = TemplateUtil.getStaticContentPath();
		Skin skin = getSkin();

		return contentPath + "/images/gem/skin/" + skin.getPageSkinName();
	}

	public static String getThemeImagePath() {
//		String contentPath = TemplateUtil.getStaticContentPath();
		String skinImagePath = getSkinImagePath();
		Theme theme = getTheme();

//		return contentPath + "/images/gem/theme/" + theme.getThemeName();
		return skinImagePath + "/theme/" + theme.getThemeName();
	}

	/**
	 * FormView取得。
	 * FormView未定義時のデフォルト設定生成はしない。
	 *
	 * @param defName
	 * @param viewName
	 * @param isSearchForm
	 * @return
	 */
	public static FormView getFormView(String defName, String viewName, boolean isSearchForm) {
		EntityViewManager evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
		EntityView ev = evm.get(defName);
		if (ev == null) return null;

		FormView view = null;
		if (isSearchForm) {
			if (viewName == null || viewName.isEmpty()) {
				view = ev.getDefaultSearchFormView();
			} else {
				view = ev.getSearchFormView(viewName);
			}
		} else {
			if (viewName == null || viewName.isEmpty()) {
				view = ev.getDefaultDetailFormView();
			} else {
				view = ev.getDetailFormView(viewName);
			}
		}
		return view;
	}

	public static String getEntityImageColor(String defName, String viewName, boolean isSearchForm) {
		FormView view = getFormView(defName, viewName, isSearchForm);
		return getEntityImageColor(view);
	}

	public static String getEntityImageColor(FormView view) {
		if (view == null || view.getImageColor() == null) return null;
		return view.getImageColor();
	}

	public static String getIconTag(FormView view) {
		if (view == null) return "";// 何も無い場合は空文字でそのまま出力
		return StringUtil.isNotBlank(view.getIconTag()) ? view.getIconTag() : "";
	}

	public static List<PropertyItem> filterPropertyItem(List<Element> elements) {
		return elements.stream().filter(e -> e instanceof PropertyItem).map(e -> (PropertyItem) e).collect(Collectors.toList());
	}

	public static PropertyItem filterPropertyItem(List<Element> elements, String propName) {
		Optional<PropertyItem> property = elements.stream().filter(e -> e instanceof PropertyItem).map(e -> (PropertyItem) e)
				.filter(e -> propName.equals(e.getPropertyName())).findFirst();
		if (property.isPresent()) {
			return property.get();
		}
		return null;
	}

	public static List<DefaultSection> filterDefaultSection(List<Element> elements) {
		return elements.stream().filter(e -> e instanceof DefaultSection).map(e -> (DefaultSection) e).collect(Collectors.toList());
	}

	public static List<PropertyItem> collectNestedPropertyItem(List<? extends Element> elements) {
		List<PropertyItem> properties = new ArrayList<PropertyItem>();
		elements.stream().filter(e -> e instanceof DefaultSection).map(e -> (DefaultSection) e).collect(Collectors.toList()).forEach(d -> {
			properties.addAll(ViewUtil.filterPropertyItem(d.getElements()));
			// 再帰的にネストされたDefaultSectionからPropertyItemを探す
			List<PropertyItem> nestProperties = collectNestedPropertyItem(ViewUtil.filterDefaultSection(d.getElements()));
			properties.addAll(nestProperties);
		});
		return properties;
	}
	
	/**
	 * プロパティが一括更新対象となるか
	 * @return
	 */
	public static boolean isBulkUpdatable(PropertyItem pi) {
		PropertyEditor pe = pi.getEditor();
		if (pe instanceof AutoNumberPropertyEditor || pe instanceof UserPropertyEditor || pe instanceof ExpressionPropertyEditor) {
			return false;
		}
		return true;
	}

	/**
	 * 編集画面でのキャンセル時に確認のメッセージを表示するか
	 * @return
	 */
	public static boolean isConfirmEditCancel() {
		GemConfigService gemConfigService = ServiceRegistry.getRegistry().getService(GemConfigService.class);
		return gemConfigService.isConfirmEditCancel();
	}

	/**
	 * 検索画面でリセットボタンを表示するか
	 * @return
	 */
	public static boolean isShowSeachCondResetButton() {
		GemConfigService gemConfigService = ServiceRegistry.getRegistry().getService(GemConfigService.class);
		return gemConfigService.isShowSeachCondResetButton();
	}

	public static List<String> getCsvDownloadCharacterCode() {
		GemConfigService gemConfigService = ServiceRegistry.getRegistry().getService(GemConfigService.class);
		return gemConfigService.getCsvDownloadCharacterCode();
	}

	public static int getSearchInterval() {
		GemConfigService gemConfigService = ServiceRegistry.getRegistry().getService(GemConfigService.class);
		return gemConfigService.getSearchInterval();
	}

	public static int getCsvDownloadInterval() {
		GemConfigService gemConfigService = ServiceRegistry.getRegistry().getService(GemConfigService.class);
		return gemConfigService.getCsvDownloadInterval();
	}

	public static boolean isEntityViewCsvDownloadSpecifyCharacterCode(CsvDownloadSpecifyCharacterCode csvDownloadSpecifyCharacterCode) {
		boolean entityViewCsvDownloadSpecifyCharacterCode = false;

		switch (csvDownloadSpecifyCharacterCode) {
		case SPECIFY:
			entityViewCsvDownloadSpecifyCharacterCode = true;
			break;

		case NOT_SPECIFY:
			entityViewCsvDownloadSpecifyCharacterCode = false;
			break;

		default:
			CsvDownloadSettingsParts csvDownloadSettingsParts = ViewUtil.getCsvDownloadSettingsParts();
			if (csvDownloadSettingsParts != null) {
				entityViewCsvDownloadSpecifyCharacterCode = csvDownloadSettingsParts.isSpecfyCharacterCodeEntityView();
			}
			break;
		}

		return entityViewCsvDownloadSpecifyCharacterCode;
	}

	private static CsvDownloadSettingsParts getCsvDownloadSettingsParts() {
		String roleName = (String) TemplateUtil.getRequestContext().getSession().getAttribute(Constants.ROLE_NAME);
		if (roleName == null) roleName = "DEFAULT";
		TopViewDefinitionManager manager = ManagerLocator.getInstance().getManager(TopViewDefinitionManager.class);
		TopViewDefinition td = manager.get(roleName);
		CsvDownloadSettingsParts csvDownloadSettingsParts = null;
		if (td != null) {
			for (TopViewParts parts : td.getParts()) {
				if (parts instanceof CsvDownloadSettingsParts) {
					csvDownloadSettingsParts = (CsvDownloadSettingsParts) parts;
					break;
				}
			}
		}
		return csvDownloadSettingsParts;
	}

	public static boolean isShowPulldownPleaseSelectLabel() {
		GemConfigService gemConfigService = ServiceRegistry.getRegistry().getService(GemConfigService.class);
		return gemConfigService.isShowPulldownPleaseSelectLabel();

	}

	public static boolean isCsvUploadAsync() {
		GemConfigService gemConfigService = ServiceRegistry.getRegistry().getService(GemConfigService.class);
		return gemConfigService.isCsvUploadAsync();
	}

	public static int getCsvUploadStatusPollingInterval() {
		GemConfigService gemConfigService = ServiceRegistry.getRegistry().getService(GemConfigService.class);
		return gemConfigService.getCsvUploadStatusPollingInterval();
	}

	public static List<String> getCssPathList(String skinName) {
		GemConfigService gemConfigService = ServiceRegistry.getRegistry().getService(GemConfigService.class);
		return gemConfigService.getCssPathList(skinName);
	}

	public static final EntityViewHelper getEntityViewHelper() {
		GemConfigService gemConfigService = ServiceRegistry.getRegistry().getService(GemConfigService.class);
		return gemConfigService.getEntityViewHelper();
	}

	/**
	 * <p>表示されている画面がダイアログかを返します。</p>
	 * <p>
	 * 標準のダイアログ用Layout定義(dialog.jsp)で設定されたisDialogをもとに判定をします。
	 * </p>
	 *
	 * @param context RequestContext
	 * @return true：ダイアログ
	 */
	public static boolean isDialog(RequestContext context) {
		Boolean isDialog = (Boolean) context.getAttribute(Constants.DIALOG_MODE);
		return isDialog != null && isDialog;
	}

	public static boolean dispElement(Element element) {
		return dispElement(TemplateUtil.getRequestContext(), element);
	}
	public static boolean dispElement(RequestContext context, Element element) {
		DetailFormViewData data = (DetailFormViewData) context.getAttribute(Constants.DATA);
		String execType = data.getExecType();
		return dispElement(execType, element);
	}
	public static boolean dispElement(String execType, Element element) {
		if (execType == null) return true;//画面表示前
		if (element.getEditDisplayType() == EditDisplayType.INSERT) {
			return EditDisplayType.INSERT.name().toLowerCase().equals(execType.toLowerCase());
		} else if (element.getEditDisplayType() == EditDisplayType.UPDATE) {
			return EditDisplayType.UPDATE.name().toLowerCase().equals(execType.toLowerCase());
		} else {
			//未指定orBOTH
			return true;
		}
	}

	public static String getDispTenantNameWithDispChecked() {
		String dispTenantName = getDispTenantName();

		Tenant tenant = TemplateUtil.getTenant();
		boolean isDisp = getTenantGemInfo(tenant).isDispTenantName();
		if (!isDisp) {
			dispTenantName = ServiceRegistry.getRegistry().getService(TenantContextService.class).getDefaultTenantName();
		}

		return dispTenantName;
	}

	public static String getDispTenantName() {
		Tenant tenant = TemplateUtil.getTenant();
		String dispTenantName = tenant.getDisplayName();

		dispTenantName = TemplateUtil.getMultilingualString(dispTenantName, tenant.getLocalizedDisplayNameList());

		if (StringUtil.isEmpty(dispTenantName)){
			dispTenantName = tenant.getName();
		}

		return dispTenantName;
	}

	public static String getTenantImgUrlWithDispChecked() {
		String tenantImgUrl = getTenantImgUrl();

		Tenant tenant = TemplateUtil.getTenant();
		boolean isDisp = getTenantGemInfo(tenant).isDispTenantName();
		if (!isDisp) {
			tenantImgUrl = "";
		}

		return tenantImgUrl;
	}

	public static String getTenantImgUrl() {
		Tenant tenant = TemplateUtil.getTenant();
		String tenantImgUrl = getTenantGemInfo(tenant).getTenantImageUrl();

		if (!StringUtil.isEmpty(tenantImgUrl)) {
			tenantImgUrl = TemplateUtil.getResourceContentPath(tenantImgUrl);
		}
		return tenantImgUrl;
	}

	public static String getTenantMiniImgUrl() {
		Tenant tenant = TemplateUtil.getTenant();
		String tenantMiniImgUrl = getTenantGemInfo(tenant).getTenantMiniImageUrl();

		if (!StringUtil.isEmpty(tenantMiniImgUrl)) {
			tenantMiniImgUrl = TemplateUtil.getResourceContentPath(tenantMiniImgUrl);
		}
		return tenantMiniImgUrl;
	}

	public static String getTenantLargeImgUrl() {
		Tenant tenant = TemplateUtil.getTenant();
		String tenantLargeImgUrl = getTenantGemInfo(tenant).getTenantLargeImageUrl();

		if (!StringUtil.isEmpty(tenantLargeImgUrl)) {
			tenantLargeImgUrl = TemplateUtil.getResourceContentPath(tenantLargeImgUrl);
		}
		return tenantLargeImgUrl;
	}

	public static String getGroupingSeparator() {
		return String.valueOf(DecimalFormatSymbols.getInstance(TemplateUtil.getLocale()).getGroupingSeparator());
	}

	public static boolean isAutocompletionTarget() {
		//自動補完の対象でWebAPI経由かどうか
		Object setting = TemplateUtil.getRequestContext().getAttribute(Constants.AUTOCOMPLETION_SETTING);
		return setting != null && setting instanceof WebApiAutocompletionSetting;
	}

	public static String getParamMappingPath(String defName, String viewName) {
		StringBuilder sb = new StringBuilder();
		if (StringUtil.isNotBlank(viewName)) {
			sb.append("/").append(viewName);
		}
		sb.append("/").append(defName);
		return sb.toString();
	}

	public static TenantGemInfo getTenantGemInfo(Tenant tenant) {
		return (tenant.getTenantConfig(TenantGemInfo.class) != null ?
				tenant.getTenantConfig(TenantGemInfo.class) : new TenantGemInfo());
	}
}
