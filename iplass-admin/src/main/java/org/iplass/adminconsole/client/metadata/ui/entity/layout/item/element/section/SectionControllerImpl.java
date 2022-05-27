/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.entity.layout.item.element.section;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.rpc.AdminAsyncCallback;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpIntegerItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.data.entity.layout.ViewType;
import org.iplass.adminconsole.client.metadata.ui.common.EntityPropertySelectItem;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.ItemControl;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.adminconsole.shared.metadata.rpc.refrect.RefrectionServiceFactory;
import org.iplass.adminconsole.view.annotation.Refrectable;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.view.generic.element.section.DefaultSection;
import org.iplass.mtp.view.generic.element.section.MassReferenceSection;
import org.iplass.mtp.view.generic.element.section.ReferenceSection;
import org.iplass.mtp.view.generic.element.section.ScriptingSection;
import org.iplass.mtp.view.generic.element.section.Section;
import org.iplass.mtp.view.generic.element.section.TemplateSection;
import org.iplass.mtp.view.generic.element.section.VersionSection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.IntegerItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.validator.IntegerRangeValidator;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class SectionControllerImpl implements SectionController {

	@Override
	public ItemControl createControl(Section section, String defName, FieldReferenceType triggerType, EntityDefinition ed) {

		ItemControl window = null;
		if (section instanceof DefaultSection) {
			window = new DefaultSectionControl(defName, triggerType, (DefaultSection) section);
		} else if (section instanceof ScriptingSection) {
			window = new ScriptingSectionControl(defName, triggerType, (ScriptingSection) section);
		} else if (section instanceof TemplateSection) {
			window = new TemplateSectionControl(defName, triggerType, (TemplateSection) section);
		} else if (section instanceof VersionSection) {
			window = new VersionSectionControl(defName, triggerType, (VersionSection) section);
		} else if (section instanceof ReferenceSection) {
			window = new ReferenceSectionControl(defName, triggerType, (ReferenceSection) section);
		} else if (section instanceof MassReferenceSection) {
			window = new MassReferenceSectionControl(defName, triggerType, (MassReferenceSection) section, ed);
		}
		return window;
	}

	@Override
	public void createControl(String sectionClassName, String defName, FieldReferenceType triggerType, Callback callback) {

		if (DefaultSection.class.getName().equals(sectionClassName)) {
			DefaultSectionDialog dialog = new DefaultSectionDialog(defName, triggerType, callback);
			dialog.show();
		} else if (ScriptingSection.class.getName().equals(sectionClassName)) {
			//HTMLセクション
			ScriptingSection section = new ScriptingSection();
			section.setDispFlag(true);
			ScriptingSectionControl window = new ScriptingSectionControl(defName, triggerType, section);
			callback.onCreated(window);
		} else if (TemplateSection.class.getName().equals(sectionClassName)) {
			//カスタムセクション
			TemplateSection section = new TemplateSection();
			section.setDispFlag(true);
			TemplateSectionControl window = new TemplateSectionControl(defName, triggerType, section);
			callback.onCreated(window);
		} else if (VersionSection.class.getName().equals(sectionClassName)) {
			VersionSection section = new VersionSection();
			section.setDispFlag(true);
			VersionSectionControl window = new VersionSectionControl(defName, triggerType, section);
			callback.onCreated(window);
		} else if (ReferenceSection.class.getName().equals(sectionClassName)) {
			ReferenceSectionDialog dialog = new ReferenceSectionDialog(defName, triggerType, callback);
			dialog.show();
		} else if (MassReferenceSection.class.getName().equals(sectionClassName)) {
			MassReferenceSectionDialog dialog = new MassReferenceSectionDialog(defName, triggerType, callback);
			dialog.show();
		}
	}

	/**
	 * セクション追加時の入力ダイアログ
	 */
	private class DefaultSectionDialog extends MtpDialog {

		private DefaultSectionDialog(final String defName, final FieldReferenceType triggerType, final Callback callback) {

			setHeight(200);
			setTitle("Section Setting");
			centerInPage();

			final TextItem title = new MtpTextItem();
			title.setTitle(AdminClientMessageUtil.getString("ui_metadata_entity_layout_DetailDropLayout_title"));

			final IntegerItem colNum = new MtpIntegerItem();
			colNum.setTitle(AdminClientMessageUtil.getString("ui_metadata_entity_layout_DetailDropLayout_colNum"));
			colNum.setDefaultValue(1);
			SmartGWTUtil.setRequired(colNum);
			SmartGWTUtil.addHoverToFormItem(colNum, AdminClientMessageUtil.getString("ui_metadata_entity_layout_DetailDropLayout_colNumTooltip"));
			IntegerRangeValidator colNumValidator = new IntegerRangeValidator();
			colNumValidator.setMin(1);
			colNum.setValidators(colNumValidator);

			final CheckboxItem expand = new CheckboxItem();
			expand.setTitle(AdminClientMessageUtil.getString("ui_metadata_entity_layout_DetailDropLayout_expandInitDisp"));
			expand.setValue(true);

			final DynamicForm form = new MtpForm();
			form.setAutoFocus(true);
			form.setFields(title, colNum, expand);

			container.addMember(form);

			IButton ok = new IButton("OK");
			ok.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					if (!form.validate()) {
						return;
					}

					// FIXME newした際にAdminConsole側でSystemPropertyが取得できないためサーバ側でインスタンス生成
					// depricatedのフラグを削除した際は直接newする形に戻す
					RefrectionServiceFactory.get().create(TenantInfoHolder.getId(), DefaultSection.class.getName(), new AdminAsyncCallback<Refrectable>() {
						@Override
						public void onSuccess(Refrectable result) {
							DefaultSection section = (DefaultSection) result;
							section.setDispFlag(true);
							section.setTitle(title.getValue() != null ? title.getValue().toString() : "");
							section.setColNum(SmartGWTUtil.getIntegerValue(colNum));
							section.setExpandable(Boolean.parseBoolean(expand.getValue().toString()));

							DefaultSectionControl window = new DefaultSectionControl(defName, triggerType, section);
							callback.onCreated(window);
							destroy();
						}
					});
				}
			});

			IButton cancel = new IButton("Cancel");
			cancel.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					destroy();
				}
			});

			footer.setMembers(ok, cancel);
		}
	}

	/**
	 * セクション追加時の入力ダイアログ
	 */
	private class ReferenceSectionDialog extends MtpDialog {

		private ReferenceSectionDialog(final String defName, final FieldReferenceType triggerType, final Callback callback) {

			setHeight(170);
			setTitle("Reference Section Setting");
			centerInPage();

			final SelectItem propName = new EntityPropertySelectItem(defName);
			propName.setTitle(AdminClientMessageUtil.getString("ui_metadata_entity_layout_DetailDropLayout_propName"));

			final CheckboxItem expand = new CheckboxItem();
			expand.setTitle(AdminClientMessageUtil.getString("ui_metadata_entity_layout_DetailDropLayout_expandInitDisp"));
			expand.setValue(true);

			DynamicForm form = new MtpForm();
			form.setAutoFocus(true);
			form.setFields(propName, expand);

			container.addMember(form);

			IButton ok = new IButton("OK");
			ok.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					//チェック
					if (propName.getValue() == null) {
						SC.say(AdminClientMessageUtil.getString("ui_metadata_entity_layout_DetailDropLayout_checkPropNameRequirErr"));
						return;
					}
					final String name = propName.getValueAsString();

					MetaDataServiceAsync service = MetaDataServiceFactory.get();
					service.getEntityDefinition(TenantInfoHolder.getId(), defName, new AsyncCallback<EntityDefinition>() {

						@Override
						public void onSuccess(EntityDefinition result) {
							PropertyDefinition pd = result.getProperty(name);
							if (!(pd instanceof ReferenceProperty)) {
								SC.say(AdminClientMessageUtil.getString("ui_metadata_entity_layout_DetailDropLayout_checkPropNotRefErr"));
								return;
							}

							// FIXME newした際にAdminConsole側でSystemPropertyが取得できないためサーバ側でインスタンス生成
							// depricatedのフラグを削除した際は直接newする形に戻す
							RefrectionServiceFactory.get().create(TenantInfoHolder.getId(), ReferenceSection.class.getName(), new AdminAsyncCallback<Refrectable>() {
								@Override
								public void onSuccess(Refrectable result) {
									ReferenceProperty rp = (ReferenceProperty) pd;
									ReferenceSection section = (ReferenceSection) result;
									section.setDefintionName(rp.getObjectDefinitionName());
									section.setPropertyName(name);
									section.setDispFlag(true);
									section.setTitle(rp.getDisplayName());
									section.setExpandable(Boolean.parseBoolean(expand.getValue().toString()));

									ReferenceSectionControl window = new ReferenceSectionControl(defName, triggerType, section);
									callback.onCreated(window);
									destroy();
								}
							});
						}

						@Override
						public void onFailure(Throwable caught) {
							SC.say(AdminClientMessageUtil.getString("ui_metadata_entity_layout_DetailDropLayout_failed"),
									AdminClientMessageUtil.getString("ui_metadata_entity_layout_DetailDropLayout_failedToReadEntityDef"));

							GWT.log(caught.toString(), caught);
						}
					});
				}
			});

			IButton cancel = new IButton("cancel");
			cancel.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					destroy();
				}
			});

			footer.setMembers(ok, cancel);
		}
	}

	/**
	 * セクション追加時の入力ダイアログ
	 */
	private class MassReferenceSectionDialog extends MtpDialog {

		private MassReferenceSectionDialog(final String defName, final FieldReferenceType triggerType, final Callback callback) {

			setHeight(170);
			setTitle("MassReference Section Setting");
			centerInPage();

			final SelectItem propName = new EntityPropertySelectItem(defName);
			propName.setTitle(AdminClientMessageUtil.getString("ui_metadata_entity_layout_DetailDropLayout_propName"));

			final CheckboxItem expand = new CheckboxItem();
			expand.setTitle(AdminClientMessageUtil.getString("ui_metadata_entity_layout_DetailDropLayout_expandInitDisp"));
			expand.setValue(true);

			final DynamicForm form = new MtpForm();
			form.setAutoFocus(true);
			form.setFields(propName, expand);

			container.addMember(form);

			IButton ok = new IButton("OK");
			ok.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					//チェック
					if (propName.getValue() == null) {
						SC.say(AdminClientMessageUtil.getString("ui_metadata_entity_layout_DetailDropLayout_checkPropNameRequirErr"));
						return;
					}

					final String name = propName.getValueAsString();

					MetaDataServiceAsync service = MetaDataServiceFactory.get();
					service.getEntityDefinition(TenantInfoHolder.getId(), defName, new AsyncCallback<EntityDefinition>() {

						@Override
						public void onSuccess(EntityDefinition result) {
							PropertyDefinition pd = result.getProperty(name);
							if (!(pd instanceof ReferenceProperty)) {
								SC.say(AdminClientMessageUtil.getString("ui_metadata_entity_layout_DetailDropLayout_checkPropNotRefErr"));
								return;
							}

							ReferenceProperty rp = (ReferenceProperty) pd;
							if (rp.getMappedBy() == null) {
								SC.say(AdminClientMessageUtil.getString("ui_metadata_entity_layout_DetailDropLayout_checkPropNotInRefErr"));
								return;
							}

							MassReferenceSection section = new MassReferenceSection();
							section.setDefintionName(rp.getObjectDefinitionName());
							section.setPropertyName(name);
							section.setDispFlag(true);
							section.setTitle(rp.getDisplayName());
							section.setExpandable(Boolean.parseBoolean(expand.getValue().toString()));

							MassReferenceSectionControl window = new MassReferenceSectionControl(defName, triggerType, section, result);
							callback.onCreated(window);
							destroy();
						}

						@Override
						public void onFailure(Throwable caught) {
							SC.say(AdminClientMessageUtil.getString("ui_metadata_entity_layout_DetailDropLayout_failed"),
									AdminClientMessageUtil.getString("ui_metadata_entity_layout_DetailDropLayout_failedToReadEntityDef"));

							GWT.log(caught.toString(), caught);
						}
					});
				}
			});

			IButton cancel = new IButton("cancel");
			cancel.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					destroy();
				}
			});

			footer.setMembers(ok, cancel);
		}
	}

	@Override
	public List<ListGridRecord> sectionItemList(ViewType viewType) {

		//TODO セクションの情報を動的に取得できるようにする
		List<ListGridRecord> records = new ArrayList<ListGridRecord>();
		if (viewType == ViewType.DETAIL || viewType == ViewType.BULK) {
			ListGridRecord dflt = new ListGridRecord();
			dflt.setAttribute("name", DefaultSection.class.getName());
			dflt.setAttribute("displayName", AdminClientMessageUtil.getString("datasource_SectionListDS_defaultSection"));
			records.add(dflt);
		}

		ListGridRecord script = new ListGridRecord();
		script.setAttribute("name", ScriptingSection.class.getName());
		script.setAttribute("displayName", AdminClientMessageUtil.getString("datasource_SectionListDS_scriptSection"));
		records.add(script);

		ListGridRecord template = new ListGridRecord();
		template.setAttribute("name", TemplateSection.class.getName());
		template.setAttribute("displayName", AdminClientMessageUtil.getString("datasource_SectionListDS_templateSection"));
		records.add(template);

		if (viewType == ViewType.DETAIL) {
			ListGridRecord version = new ListGridRecord();
			version.setAttribute("name", VersionSection.class.getName());
			version.setAttribute("displayName", AdminClientMessageUtil.getString("datasource_SectionListDS_versionSection"));
			records.add(version);

			ListGridRecord reference = new ListGridRecord();
			reference.setAttribute("name", ReferenceSection.class.getName());
			reference.setAttribute("displayName", AdminClientMessageUtil.getString("datasource_SectionListDS_referenceSection"));
			records.add(reference);

			ListGridRecord massReference = new ListGridRecord();
			massReference.setAttribute("name", MassReferenceSection.class.getName());
			massReference.setAttribute("displayName", AdminClientMessageUtil.getString("datasource_SectionListDS_massReferenceSection"));
			records.add(massReference);
		}

		return records;
	}

}
