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

package org.iplass.gem.command.generic.search;

import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.query.From;
import org.iplass.mtp.entity.query.Limit;
import org.iplass.mtp.entity.query.OrderBy;
import org.iplass.mtp.entity.query.Select;
import org.iplass.mtp.entity.query.Where;
import org.iplass.mtp.view.generic.EntityView;

public interface SearchContext {

	public RequestContext getRequest();
	public void setRequest(RequestContext request);

	public EntityDefinition getEntityDefinition();
	public void setEntityDefinition(EntityDefinition definition);

	public EntityView getEntityView();
	public void setEntityView(EntityView view);

	public String getDefName();

	public Select getSelect();

	public From getFrom();

	public Where getWhere();

	public OrderBy getOrderBy();

	public Limit getLimit();

	public boolean isVersioned();

	public boolean isSearch();

	public boolean isCount();

	public boolean isDelete();

	public boolean isBulk();

	public boolean checkParameter();

	public boolean validation();

}
