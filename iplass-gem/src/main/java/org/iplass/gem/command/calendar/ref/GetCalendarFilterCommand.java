/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.gem.command.calendar.ref;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.iplass.gem.command.Constants;
import org.iplass.gem.command.generic.search.SearchFormViewData;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.RestJson;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.view.calendar.EntityCalendar;
import org.iplass.mtp.view.calendar.EntityCalendarItem;
import org.iplass.mtp.view.calendar.EntityCalendarManager;
import org.iplass.mtp.view.filter.EntityFilter;
import org.iplass.mtp.view.filter.EntityFilterManager;
import org.iplass.mtp.view.generic.EntityView;
import org.iplass.mtp.view.generic.EntityViewManager;
import org.iplass.mtp.view.generic.FormViewUtil;
import org.iplass.mtp.view.generic.SearchFormView;
import org.iplass.mtp.view.generic.editor.BinaryPropertyEditor;
import org.iplass.mtp.view.generic.editor.NestProperty;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor;
import org.iplass.mtp.view.generic.element.property.PropertyItem;
import org.iplass.mtp.web.template.TemplateUtil;
import org.iplass.mtp.webapi.definition.RequestType;
import org.iplass.mtp.webapi.definition.MethodType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * カレンダーフィルター設定コマンド
 * @author lis7zi
 */
@WebApi(
		name=GetCalendarFilterCommand.WEBAPI_NAME,
		accepts=RequestType.REST_JSON,
		methods=MethodType.POST,
		restJson=@RestJson(parameterName="param"),
		results="calendarFilterData",
		checkXRequestedWithHeader=true
	)
@CommandClass(name="gem/calendar/ref/GetCalendarFilterCommand", displayName="カレンダーフィルター取得")
public final class GetCalendarFilterCommand implements Command {

	public  static final String WEBAPI_NAME = "gem/calendar/ref/getCalendarFilter";

	private static Logger logger = LoggerFactory.getLogger(GetCalendarFilterCommand.class);

	/** EntityCalendarManager */
	private EntityCalendarManager ecm = null;

	/** EntityDefinitionManager */
	private EntityDefinitionManager edm = null;

	/** EntityFilterManager */
	private EntityFilterManager efm = null;

	/** EntityViewManager */
	EntityViewManager evm = null;

	/**
	 * コンストラクタ
	 */
	public GetCalendarFilterCommand() {
		ecm = ManagerLocator.getInstance().getManager(EntityCalendarManager.class);
		edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
		efm = ManagerLocator.getInstance().getManager(EntityFilterManager.class);
		evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
	}

	@Override
	public String execute(RequestContext request) {
		String calendarName = request.getParam(Constants.CALENDAR_NAME);

		EntityCalendar ec = ecm.get(calendarName);
		List<CalendarFilterData> calendarFilterDataList = new ArrayList<>();

		if(ec != null) {
			for (EntityCalendarItem item : ec.getItems()) {
				String defName = item.getDefinitionName();

				CalendarFilterData cfd = new CalendarFilterData();

				EntityDefinition ed = edm.get(defName);
				cfd.setEntityDefinition(ed);

				SearchFormViewData viewData = new SearchFormViewData();
				viewData.setEntityDefinition(ed);

				EntityView entityView = evm.get(defName);

				String viewName = item.getViewName();
				SearchFormView view = FormViewUtil.getSearchFormView(ed, entityView, viewName);

				if (view == null) {
					logger.warn("Entity [" + defName + "] is not defined SearchView [" + viewName + "], skip calendar condition.");
					continue;
				}

				// フィルター項目となるPopertyItemの一覧を作成する
				List<CalendarFilterPropertyItem> piList = new ArrayList<>();
				List<PropertyItem> properties = view.getCondSection().getElements().stream()
						.filter(e -> e instanceof PropertyItem).map(e -> (PropertyItem) e)
						.collect(Collectors.toList());

				for (PropertyItem pi : properties) {
					if (pi.isHideDetail()) {
						continue;
					}

					PropertyEditor pe = pi.getEditor();
					if (pe instanceof ReferencePropertyEditor) {
						ReferencePropertyEditor rpe = (ReferencePropertyEditor) pe;
						if (!rpe.getNestProperties().isEmpty()) {
							getNestPropertyItem(pi, rpe, ed, piList);
						} else {
							PropertyDefinition pd = ed.getProperty(pi.getPropertyName());
							CalendarFilterPropertyItem cpi = new CalendarFilterPropertyItem();
							cpi.setPropertyName(pi.getPropertyName());
							cpi.setDisplayName(TemplateUtil.getMultilingualString(pi.getDisplayLabel(), pi.getLocalizedDisplayLabelList(), pd.getDisplayName(), pd.getLocalizedDisplayNameList()));
							cpi.setPropertyEditor(pi.getEditor(), pd);
							cpi.setPropertyType(pd.getType().toString());
							piList.add(cpi);
						}
					} else if (pe instanceof BinaryPropertyEditor){
						// バイナリーは除外
						continue;
					} else {

						PropertyDefinition pd = ed.getProperty(pi.getPropertyName());
						CalendarFilterPropertyItem cpi = new CalendarFilterPropertyItem();
						cpi.setPropertyName(pi.getPropertyName());
						cpi.setDisplayName(TemplateUtil.getMultilingualString(pi.getDisplayLabel(), pi.getLocalizedDisplayLabelList(), pd.getDisplayName(), pd.getLocalizedDisplayNameList()));
						cpi.setPropertyEditor(pi.getEditor(), pd);
						cpi.setPropertyType(pd.getType().toString());
						piList.add(cpi);
					}
				}

				cfd.setCalendarFilterPropertyItemList(piList);

				EntityFilter ef = efm.get(defName);
				cfd.setEntityFilterIteList(ef != null ? ef.getItems() : null);

				calendarFilterDataList.add(cfd);
			}
		}

		request.setAttribute("calendarFilterData", calendarFilterDataList);
		return Constants.CMD_EXEC_SUCCESS;
	}

	private void getNestPropertyItem(PropertyItem pi, ReferencePropertyEditor rpe, EntityDefinition ed, List<CalendarFilterPropertyItem> piList) {

		// プロパティと一緒にネスト項目を条件設定の場合は自分自身を追加
		if (rpe.isUseNestConditionWithProperty()) {
			CalendarFilterPropertyItem cpi = new CalendarFilterPropertyItem();
			PropertyDefinition pd = ed.getProperty(pi.getPropertyName());
			cpi.setPropertyName(pi.getPropertyName());
			cpi.setDisplayName(TemplateUtil.getMultilingualString(pi.getDisplayLabel(), pi.getLocalizedDisplayLabelList(), pd.getDisplayName(), pd.getLocalizedDisplayNameList()));
			cpi.setPropertyEditor(pi.getEditor(), pd);
			cpi.setPropertyType(pd.getType().toString());
			piList.add(cpi);
		}

		List<NestProperty> npList = rpe.getNestProperties();

		for (NestProperty np : npList) {
			String npName = pi.getPropertyName() + "." + np.getPropertyName();
			PropertyDefinition pd = ed.getProperty(np.getPropertyName());
			if (pd instanceof ReferenceProperty
					&& np.getEditor() instanceof ReferencePropertyEditor
					&& !((ReferencePropertyEditor) np.getEditor()).getNestProperties().isEmpty()) {
				getLastPropertyItem(npName, np, (ReferencePropertyEditor) np.getEditor(), edm.get(((ReferenceProperty) pd).getObjectDefinitionName()), piList);
			} else {
				CalendarFilterPropertyItem cpi = new CalendarFilterPropertyItem();
				cpi.setPropertyName(npName);
				cpi.setDisplayName(TemplateUtil.getMultilingualString(np.getDisplayLabel(), np.getLocalizedDisplayLabelList(), pd.getDisplayName(), pd.getLocalizedDisplayNameList()));
				cpi.setPropertyEditor(np.getEditor(), pd);
				cpi.setPropertyType(pd.getType().toString());
				piList.add(cpi);
			}
		}
	}

	private void getLastPropertyItem(String npName, NestProperty nestProperty, ReferencePropertyEditor rpe, EntityDefinition ed, List<CalendarFilterPropertyItem> piList) {

		// プロパティと一緒にネスト項目を条件設定の場合は自分自身を追加
		if (rpe.isUseNestConditionWithProperty()) {
			CalendarFilterPropertyItem cpi = new CalendarFilterPropertyItem();
			PropertyDefinition pd = ed.getProperty(nestProperty.getPropertyName());
			cpi.setPropertyName(npName);
			cpi.setDisplayName(TemplateUtil.getMultilingualString(nestProperty.getDisplayLabel(), nestProperty.getLocalizedDisplayLabelList(), pd.getDisplayName(), pd.getLocalizedDisplayNameList()));
			cpi.setPropertyEditor(nestProperty.getEditor(), pd);
			cpi.setPropertyType(pd.getType().toString());
			piList.add(cpi);
		}

		List<NestProperty> npList = rpe.getNestProperties();

		for (NestProperty np : npList) {
			npName = npName + "." + np.getPropertyName();
			PropertyDefinition pd = ed.getProperty(np.getPropertyName());
			if (pd instanceof ReferenceProperty
					&& np.getEditor() instanceof ReferencePropertyEditor
					&& !((ReferencePropertyEditor) np.getEditor()).getNestProperties().isEmpty()) {
				getLastPropertyItem(npName, np, (ReferencePropertyEditor) np.getEditor(), edm.get(((ReferenceProperty) pd).getObjectDefinitionName()), piList);
			} else {
				CalendarFilterPropertyItem cpi = new CalendarFilterPropertyItem();
				cpi.setPropertyName(npName);
				cpi.setDisplayName(TemplateUtil.getMultilingualString(np.getDisplayLabel(), np.getLocalizedDisplayLabelList(), pd.getDisplayName(), pd.getLocalizedDisplayNameList()));
				cpi.setPropertyEditor(np.getEditor(), pd);
				cpi.setPropertyType(pd.getType().toString());
				piList.add(cpi);
			}
		}
	}

}
