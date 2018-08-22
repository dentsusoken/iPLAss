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

import org.iplass.adminconsole.client.base.event.MTPEvent;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.data.entity.layout.ViewType;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.PropertyOperationHandler;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.ViewEditWindow;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
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
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.IntegerItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.validator.IntegerRangeValidator;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;

public class SectionWindowControllerImpl implements SectionWindowController {

	@Override
	public ViewEditWindow createWindow(Section section, String defName, FieldReferenceType triggerType, EntityDefinition ed) {

		ViewEditWindow window = null;
		if (section instanceof DefaultSection) {
			window = new DefaultSectionWindow(defName, triggerType, (DefaultSection) section);
		} else if (section instanceof ScriptingSection) {
			window = new ScriptingSectionWindow(defName, triggerType, (ScriptingSection) section);
		} else if (section instanceof TemplateSection) {
			window = new TemplateSectionWindow(defName, triggerType, (TemplateSection) section);
		} else if (section instanceof VersionSection) {
			window = new VersionSectionWindow(defName, triggerType, (VersionSection) section);
		} else if (section instanceof ReferenceSection) {
			window = new ReferenceSectionWindow(defName, triggerType, (ReferenceSection) section);
		} else if (section instanceof MassReferenceSection) {
			window = new MassReferenceSectionWindow(defName, triggerType, (MassReferenceSection) section, ed);
		}
		return window;
	}

	@Override
	public void createWindow(String sectionClassName, String defName, FieldReferenceType triggerType, PropertyOperationHandler propertyOperationHandler, Callback callback) {

		if (DefaultSection.class.getName().equals(sectionClassName)) {
			DefaultSectionDialog dialog = new DefaultSectionDialog(defName, triggerType, callback);
			dialog.show();
		} else if (ScriptingSection.class.getName().equals(sectionClassName)) {
			//HTMLセクション
			ScriptingSection section = new ScriptingSection();
			section.setDispFlag(true);
			ScriptingSectionWindow window = new ScriptingSectionWindow(defName, triggerType, section);
			callback.onCreated(window);
		} else if (TemplateSection.class.getName().equals(sectionClassName)) {
			//カスタムセクション
			TemplateSection section = new TemplateSection();
			section.setDispFlag(true);
			TemplateSectionWindow window = new TemplateSectionWindow(defName, triggerType, section);
			callback.onCreated(window);
		} else if (VersionSection.class.getName().equals(sectionClassName)) {
			VersionSection section = new VersionSection();
			section.setDispFlag(true);
			VersionSectionWindow window = new VersionSectionWindow(defName, triggerType, section);
			callback.onCreated(window);
		} else if (ReferenceSection.class.getName().equals(sectionClassName)) {
			ReferenceSectionDialog dialog = new ReferenceSectionDialog(defName, triggerType, propertyOperationHandler, callback);
			dialog.show();
		} else if (MassReferenceSection.class.getName().equals(sectionClassName)) {
			MassReferenceSectionDialog dialog = new MassReferenceSectionDialog(defName, triggerType, propertyOperationHandler, callback);
			dialog.show();
		}
	}

	/**
	 * セクション追加時の入力ダイアログ
	 */
	private class DefaultSectionDialog extends AbstractWindow {

		private DynamicForm form = new DynamicForm();

		private TextItem title = null;
		private IntegerItem colNum = null;
		private CheckboxItem expand = null;
		private IButton ok = null;
		private IButton cancel = null;

		private DefaultSectionDialog(final String defName, final FieldReferenceType triggerType, final Callback callback) {
			setWidth(300);
			setHeight(150);
			setTitle("Section Setting");
			setShowMinimizeButton(false);
			setIsModal(true);
			setShowModalMask(false);
			centerInPage();

			title = new TextItem();
			title.setTitle(AdminClientMessageUtil.getString("ui_metadata_entity_layout_DetailDropLayout_title"));

			colNum = new IntegerItem();
			colNum.setTitle(AdminClientMessageUtil.getString("ui_metadata_entity_layout_DetailDropLayout_colNum"));
			colNum.setDefaultValue(1);
			SmartGWTUtil.setRequired(colNum);
			SmartGWTUtil.addHoverToFormItem(colNum, AdminClientMessageUtil.getString("ui_metadata_entity_layout_DetailDropLayout_colNumTooltip"));
			IntegerRangeValidator colNumValidator = new IntegerRangeValidator();
			colNumValidator.setMin(1);
			colNum.setValidators(colNumValidator);


			expand = new CheckboxItem();
			expand.setTitle(AdminClientMessageUtil.getString("ui_metadata_entity_layout_DetailDropLayout_expandInitDisp"));
			expand.setValue(true);

			ok = new IButton("OK");
			cancel = new IButton("cancel");

			//OK押下時はウィンドウ追加
			ok.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					if (!form.validate()) {
						return;
					}

					DefaultSection section = new DefaultSection();
					section.setDispFlag(true);
					section.setTitle(title.getValue() != null ? title.getValue().toString() : "");
					section.setColNum(SmartGWTUtil.getIntegerValue(colNum));
					section.setExpandable(Boolean.parseBoolean(expand.getValue().toString()));

					DefaultSectionWindow window = new DefaultSectionWindow(defName, triggerType, section);
					callback.onCreated(window);
					destroy();
				}
			});

			//Cancel押下時はダイアログを閉じる
			cancel.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					destroy();
				}
			});

			form.setAutoFocus(true);
			form.setWidth100();
			form.setPadding(5);
			form.setFields(title, colNum, expand);

			HLayout hl = new HLayout();
			hl.setAlign(Alignment.CENTER);
			hl.setAlign(VerticalAlignment.CENTER);
			hl.addMember(ok);
			hl.addMember(cancel);

			addItem(form);
			addItem(hl);
		}
	}

	/**
	 * セクション追加時の入力ダイアログ
	 */
	private class ReferenceSectionDialog extends AbstractWindow {
		private TextItem propName = null;
		private CheckboxItem expand = null;
		private IButton ok = null;
		private IButton cancel = null;

		private ReferenceSectionDialog(final String defName, final FieldReferenceType triggerType, final PropertyOperationHandler propertyOperationHandler, final Callback callback) {
			setWidth(300);
			setHeight(150);
			setTitle("Section Setting");
			setShowMinimizeButton(false);
			setIsModal(true);
			setShowModalMask(false);
			centerInPage();

			propName = new TextItem();
			propName.setTitle(AdminClientMessageUtil.getString("ui_metadata_entity_layout_DetailDropLayout_propName"));

			expand = new CheckboxItem();
			expand.setTitle(AdminClientMessageUtil.getString("ui_metadata_entity_layout_DetailDropLayout_expandInitDisp"));
			expand.setValue(true);

			ok = new IButton("OK");
			cancel = new IButton("cancel");

			//OK押下時はウィンドウ追加
			ok.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					//チェック
					if (propName.getValue() == null) {
						SC.say(AdminClientMessageUtil.getString("ui_metadata_entity_layout_DetailDropLayout_checkPropNameRequirErr"));
						return;
					}
					final String name = propName.getValueAsString();
					final MTPEvent propCheck = new MTPEvent();
					propCheck.setValue("name", name);
					final MTPEvent sectionCheck = new MTPEvent();
					sectionCheck.setValue("name", name + ReferenceSectionWindow.SECTION_SUFFIX);
					boolean chk = false;
					if (propertyOperationHandler.check(propCheck)) {
						if (!propertyOperationHandler.check(sectionCheck)) {
							// プロパティは重複してるが、セクションとして追加されてないのでエラー
							SC.say(AdminClientMessageUtil.getString("ui_metadata_entity_layout_DetailDropLayout_checkPropExistsErr"));
							return;
						}
						chk = true;
					}
					final boolean addedHandler = chk;

					MetaDataServiceAsync service = MetaDataServiceFactory.get();
					service.getEntityDefinition(TenantInfoHolder.getId(), defName, new AsyncCallback<EntityDefinition>() {

						@Override
						public void onSuccess(EntityDefinition result) {
							PropertyDefinition pd = result.getProperty(name);
							if (!(pd instanceof ReferenceProperty)) {
								SC.say(AdminClientMessageUtil.getString("ui_metadata_entity_layout_DetailDropLayout_checkPropNotRefErr"));
								return;
							}

							Integer count = (Integer) propertyOperationHandler.getContext().get(name + ReferenceSectionWindow.SECTION_COUNT_KEY);
							if (count == null) count = 0;
							if (pd.getMultiplicity() != -1 && pd.getMultiplicity() <= count) {
								//セクション追加済みだが多重度より多くなる場合はエラー
								SC.say(AdminClientMessageUtil.getString("ui_metadata_entity_layout_DetailDropLayout_checkPropCountErr"));
								return;
							}

							ReferenceProperty rp = (ReferenceProperty) pd;
							ReferenceSection section = new ReferenceSection();
							section.setDefintionName(rp.getName());
							section.setPropertyName(name);
							section.setDispFlag(true);
							section.setTitle(rp.getDisplayName());
							section.setExpandable(Boolean.parseBoolean(expand.getValue().toString()));

							ReferenceSectionWindow window = new ReferenceSectionWindow(defName, triggerType, section);

							// handlerに未登録の場合だけ追加
							if (!addedHandler) {
								propertyOperationHandler.add(propCheck);
								propertyOperationHandler.add(sectionCheck);
							}
							propertyOperationHandler.getContext().set(name + ReferenceSectionWindow.SECTION_COUNT_KEY, ++count);

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

			//Cancel押下時はダイアログを閉じる
			cancel.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					destroy();
				}
			});

			DynamicForm form = new DynamicForm();
			form.setAutoFocus(true);
			form.setWidth100();
			form.setPadding(5);
			form.setFields(propName, expand);

			HLayout hl = new HLayout();
			hl.setAlign(Alignment.CENTER);
			hl.setAlign(VerticalAlignment.CENTER);
			hl.addMember(ok);
			hl.addMember(cancel);

			addItem(form);
			addItem(hl);
		}
	}

	/**
	 * セクション追加時の入力ダイアログ
	 */
	private class MassReferenceSectionDialog extends AbstractWindow {
		private TextItem propName = null;
		private CheckboxItem expand = null;
		private IButton ok = null;
		private IButton cancel = null;

		private MassReferenceSectionDialog(final String defName, final FieldReferenceType triggerType, final PropertyOperationHandler propertyOperationHandler, final Callback callback) {
			setWidth(300);
			setHeight(150);
			setTitle("Section Setting");
			setShowMinimizeButton(false);
			setIsModal(true);
			setShowModalMask(false);
			centerInPage();

			propName = new TextItem();
			propName.setTitle(AdminClientMessageUtil.getString("ui_metadata_entity_layout_DetailDropLayout_propName"));

			expand = new CheckboxItem();
			expand.setTitle(AdminClientMessageUtil.getString("ui_metadata_entity_layout_DetailDropLayout_expandInitDisp"));
			expand.setValue(true);

			ok = new IButton("OK");
			cancel = new IButton("cancel");

			//OK押下時はウィンドウ追加
			ok.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					//チェック
					if (propName.getValue() == null) {
						SC.say(AdminClientMessageUtil.getString("ui_metadata_entity_layout_DetailDropLayout_checkPropNameRequirErr"));
						return;
					}
					final String name = propName.getValueAsString();
					final MTPEvent mtpEvent = new MTPEvent();
					mtpEvent.setValue("name", name);
					if (propertyOperationHandler.check(mtpEvent)) {
						SC.say(AdminClientMessageUtil.getString("ui_metadata_entity_layout_DetailDropLayout_checkPropExistsErr"));
						return;
					}

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
							section.setPropertyName(name);
							section.setDispFlag(true);
							section.setTitle(rp.getDisplayName());
							section.setExpandable(Boolean.parseBoolean(expand.getValue().toString()));

							MassReferenceSectionWindow window = new MassReferenceSectionWindow(defName, triggerType, section, result);
							propertyOperationHandler.add(mtpEvent);

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

			//Cancel押下時はダイアログを閉じる
			cancel.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					destroy();
				}
			});

			DynamicForm form = new DynamicForm();
			form.setAutoFocus(true);
			form.setWidth100();
			form.setPadding(5);
			form.setFields(propName, expand);

			HLayout hl = new HLayout();
			hl.setAlign(Alignment.CENTER);
			hl.setAlign(VerticalAlignment.CENTER);
			hl.addMember(ok);
			hl.addMember(cancel);

			addItem(form);
			addItem(hl);
		}
	}

	@Override
	public List<ListGridRecord> sectionItemList(ViewType viewType) {

		//TODO セクションの情報を動的に取得できるようにする
		List<ListGridRecord> records = new ArrayList<ListGridRecord>();
		if (viewType == ViewType.DETAIL) {
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
