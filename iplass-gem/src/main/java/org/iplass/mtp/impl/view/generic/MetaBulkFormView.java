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

package org.iplass.mtp.impl.view.generic;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.i18n.MetaLocalizedString;
import org.iplass.mtp.view.generic.BulkFormView;
import org.iplass.mtp.view.generic.FormView;

@XmlAccessorType(XmlAccessType.FIELD)
public class MetaBulkFormView extends MetaFormView {

	private static final long serialVersionUID = 510164329032096078L;

	public static MetaBulkFormView createInstance(FormView view) {
		return new MetaBulkFormView();
	}

	/** 更新アクション名 */
	private String updateActionName;

	/** 更新ボタン表示ラベル */
	private String updateDisplayLabel;

	/** 多言語設定情報 */
	private List<MetaLocalizedString> localizedUpdateDisplayLabelList = new ArrayList<MetaLocalizedString>();

	/** Javascriptコード有効可否 */
	private Boolean validJavascriptBulkPage;

	/** 更新ボタン非表示 */
	private boolean hideUpdate;

	/** 親子関係の参照を物理削除するか */
	private boolean purgeCompositionedEntity;

	/** 定義されている参照プロパティのみを取得 */
	private boolean loadDefinedReferenceProperty;

	/** 更新時に強制的に更新処理を行う */
	private boolean forceUpadte;

	/** カスタム登録処理クラス名 */
	private String interrupterName;

	/** カスタムロード処理クラス名 */
	private String loadEntityInterrupterName;

	/** JavaScript */
	private String javaScript;

	public String getUpdateActionName() {
		return updateActionName;
	}

	public void setUpdateActionName(String updateActionName) {
		this.updateActionName = updateActionName;
	}

	public String getUpdateDisplayLabel() {
		return updateDisplayLabel;
	}

	public void setUpdateDisplayLabel(String updateDisplayLabel) {
		this.updateDisplayLabel = updateDisplayLabel;
	}

	public List<MetaLocalizedString> getLocalizedUpdateDisplayLabelList() {
		return localizedUpdateDisplayLabelList;
	}

	public void setLocalizedUpdateDisplayLabelList(List<MetaLocalizedString> localizedUpdateDisplayLabelList) {
		this.localizedUpdateDisplayLabelList = localizedUpdateDisplayLabelList;
	}

	public Boolean getValidJavascriptBulkPage() {
		return validJavascriptBulkPage;
	}

	public void setValidJavascriptBulkPage(Boolean validJavascriptBulkPage) {
		this.validJavascriptBulkPage = validJavascriptBulkPage;
	}

	public boolean isHideUpdate() {
		return hideUpdate;
	}

	public void setHideUpdate(boolean hideUpdate) {
		this.hideUpdate = hideUpdate;
	}

	public boolean isPurgeCompositionedEntity() {
		return purgeCompositionedEntity;
	}

	public void setPurgeCompositionedEntity(boolean purgeCompositionedEntity) {
		this.purgeCompositionedEntity = purgeCompositionedEntity;
	}

	public boolean isLoadDefinedReferenceProperty() {
		return loadDefinedReferenceProperty;
	}

	public void setLoadDefinedReferenceProperty(boolean loadDefinedReferenceProperty) {
		this.loadDefinedReferenceProperty = loadDefinedReferenceProperty;
	}

	public boolean isForceUpadte() {
		return forceUpadte;
	}

	public void setForceUpadte(boolean forceUpadte) {
		this.forceUpadte = forceUpadte;
	}

	public String getInterrupterName() {
		return interrupterName;
	}

	public void setInterrupterName(String interrupterName) {
		this.interrupterName = interrupterName;
	}

	public String getLoadEntityInterrupterName() {
		return loadEntityInterrupterName;
	}

	public void setLoadEntityInterrupterName(String loadEntityInterrupterName) {
		this.loadEntityInterrupterName = loadEntityInterrupterName;
	}

	public String getJavaScript() {
		return javaScript;
	}

	public void setJavaScript(String javaScript) {
		this.javaScript = javaScript;
	}

	@Override
	public void applyConfig(FormView form, String definitionId) {
		super.fillFrom(form, definitionId);

		BulkFormView bForm = (BulkFormView) form;
		updateActionName = bForm.getUpdateActionName();
		updateDisplayLabel = bForm.getUpdateDisplayLabel();
		localizedUpdateDisplayLabelList = I18nUtil.toMeta(bForm.getLocalizedUpdateDisplayLabelList());
		purgeCompositionedEntity = bForm.isPurgeCompositionedEntity();
		loadDefinedReferenceProperty = bForm.isLoadDefinedReferenceProperty();
		forceUpadte = bForm.isForceUpadte();
		javaScript = bForm.getJavaScript();
		validJavascriptBulkPage = bForm.isValidJavascriptBulkPage();
		interrupterName = bForm.getInterrupterName();
		loadEntityInterrupterName = bForm.getLoadEntityInterrupterName();

	}

	@Override
	public FormView currentConfig(String definitionId) {
		BulkFormView form = new BulkFormView();
		super.fillTo(form, definitionId);

		form.setUpdateActionName(updateActionName);
		form.setUpdateDisplayLabel(updateDisplayLabel);
		form.setLocalizedUpdateDisplayLabelList(I18nUtil.toDef(localizedUpdateDisplayLabelList));
		form.setPurgeCompositionedEntity(purgeCompositionedEntity);
		form.setLoadDefinedReferenceProperty(loadDefinedReferenceProperty);
		form.setForceUpadte(forceUpadte);
		form.setJavaScript(javaScript);
		form.setInterrupterName(interrupterName);
		form.setLoadEntityInterrupterName(loadEntityInterrupterName);
		if (validJavascriptBulkPage != null) {
			form.setValidJavascriptBulkPage(validJavascriptBulkPage);
		} else {
			// 未設定なら有効扱い
			form.setValidJavascriptBulkPage(true);
		}

		return form;
	}

	@Override
	public FormViewHandler createRuntime(EntityViewHandler entityView) {
		return new BulkFormViewHandler(this, entityView);
	}

}
