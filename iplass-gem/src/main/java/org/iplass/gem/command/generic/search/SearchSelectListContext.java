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

package org.iplass.gem.command.generic.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.entity.query.From;
import org.iplass.mtp.entity.query.Limit;
import org.iplass.mtp.entity.query.OrderBy;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.Select;
import org.iplass.mtp.entity.query.SortSpec.NullOrderingSpec;
import org.iplass.mtp.entity.query.SortSpec.SortType;
import org.iplass.mtp.entity.query.Where;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.view.generic.EntityView;
import org.iplass.mtp.view.generic.NullOrderType;
import org.iplass.mtp.view.generic.SearchFormView;
import org.iplass.mtp.view.generic.SearchQueryContext;
import org.iplass.mtp.view.generic.SearchQueryInterrupter;
import org.iplass.mtp.view.generic.SearchQueryInterrupter.SearchQueryType;
import org.iplass.mtp.view.generic.editor.NestProperty;
import org.iplass.mtp.view.generic.element.property.PropertyColumn;
import org.iplass.mtp.view.generic.element.property.PropertyItem;
import org.iplass.mtp.view.generic.element.section.SearchConditionSection;
import org.iplass.mtp.view.generic.element.section.SearchResultSection;
import org.iplass.mtp.view.generic.element.section.SortSetting;

public class SearchSelectListContext extends SearchContextBase {

	private SearchContextBase context;

	public SearchSelectListContext(SearchContextBase context) {
		this.context = context;
	}

	@Override
	public RequestContext getRequest() {
		return context.getRequest();
	}

	@Override
	public EntityDefinition getEntityDefinition() {
		return context.getEntityDefinition();
	}

	@Override
	public EntityView getEntityView() {
		return context.getEntityView();
	}

	@Override
	public String getDefName() {
		return context.getDefName();
	}

	@Override
	public Select getSelect() {
		ArrayList<String> select = new ArrayList<String>();
		select.add(Entity.OID);
		select.add(Entity.VERSION);

		Select s = new Select().add(select.toArray());

		boolean distinct = getConditionSection().isDistinct();
		s.setDistinct(distinct);

		return s;
	}

	@Override
	public From getFrom() {
		return context.getFrom();
	}

	@Override
	public Where getWhere() {
		return context.getWhere();
	}

	@Override
	public OrderBy getOrderBy() {
		//order by は不要なのでnullに
		return null;
	}

	@Override
	public Limit getLimit() {
		return null;
	}

	@Override
	public boolean isVersioned() {
		return context.isVersioned();
	}

	@Override
	public boolean isSearch() {
		return context.isSearch();
	}

	@Override
	public boolean isCount() {
		return context.isCount();
	}

	@Override
	public boolean validation() {
		return context.validation();
	}

	@Override
	public boolean checkParameter() {
		return context.checkParameter();
	}

	@Override
	protected SearchFormView getForm() {
		return context.getForm();
	}

	@Override
	protected SearchConditionSection getConditionSection() {
		return context.getConditionSection();
	}

	@Override
	protected SearchResultSection getResultSection() {
		return context.getResultSection();
	}

	@Override
	protected PropertyItem getLayoutProperty(String propName) {
		return context.getLayoutProperty(propName);
	}

	@Override
	protected PropertyColumn getLayoutPropertyColumn(String propName) {
		return context.getLayoutPropertyColumn(propName);
	}

	@Override
	protected PropertyItem getLayoutPropertyForCheck(String propName) {
		return context.getLayoutPropertyForCheck(propName);
	}

	@Override
	protected List<PropertyDefinition> getPropertyList() {
		return context.getPropertyList();
	}

	@Override
	protected Condition getDefaultCondition() {
		return context.getDefaultCondition();
	}

	@Override
	protected String getViewName() {
		return context.getViewName();
	}

	@Override
	protected String getSortKey() {
		return context.getSortKey();
	}

	@Override
	protected SortType getSortType() {
		return context.getSortType();
	}

	@Override
	protected NullOrderingSpec getNullOrderingSpec(NullOrderType type) {
		return context.getNullOrderingSpec(type);
	}

	@Override
	protected boolean hasSortSetting() {
		return context.hasSortSetting();
	}

	@Override
	protected List<SortSetting> getSortSetting() {
		return context.getSortSetting();
	}

	@Override
	protected Integer getSearchLimit() {
		return context.getSearchLimit();
	}

	@Override
	protected Integer getOffset() {
		return context.getOffset();
	}

	@Override
	protected void addSearchProperty(ArrayList<String> select, String propName, NestProperty... nest) {
		context.addSearchProperty(select, propName, nest);
	}

	@Override
	protected EntityDefinition getReferenceEntityDefinition(ReferenceProperty rp) {
		return context.getReferenceEntityDefinition(rp);
	}

	@Override
	protected PropertyDefinition getPropertyDefinition(String propName) {
		return context.getPropertyDefinition(propName);
	}

	@Override
	public boolean isUseUserPropertyEditor() {
		return context.isUseUserPropertyEditor();
	}

	@Override
	public Set<String> getUseUserPropertyEditorPropertyName() {
		return context.getUseUserPropertyEditorPropertyName();
	}

	@Override
	public SearchQueryContext beforeSearch(Query query, SearchQueryType type) {
		return context.beforeSearch(query, type);
	}

	@Override
	public void afterSearch(Query query, Entity entity, SearchQueryType type) {
		context.afterSearch(query, entity, type);
	}

	@Override
	public SearchQueryInterrupterHandler getSearchQueryInterrupterHandler() {
		return context.getSearchQueryInterrupterHandler();
	}

	@Override
	protected SearchQueryInterrupter createInterrupter(String className) {
		return context.createInterrupter(className);
	}

	@Override
	protected SearchQueryInterrupter getDefaultSearchQueryInterrupter() {
		return context.getDefaultSearchQueryInterrupter();
	}
}
