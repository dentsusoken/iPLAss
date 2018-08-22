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

package org.iplass.mtp.impl.entity.interceptor;

import java.util.function.Predicate;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.SearchOption;
import org.iplass.mtp.entity.interceptor.EntityInterceptor;
import org.iplass.mtp.entity.interceptor.EntityQueryInvocation;
import org.iplass.mtp.entity.interceptor.InvocationType;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.EntityStreamSearchHandler;

public class EntityQueryInvocationImpl extends EntityInvocationImpl<Void> implements EntityQueryInvocation {

	private Query query;
	private Predicate<?> resultCallback;
	private EntityStreamSearchHandler<?> streamSearchHandler;
	private InvocationType type;
	private SearchOption searchOption;

	public EntityQueryInvocationImpl(Query query, SearchOption searchOption, Predicate<?> resultCallback,
			InvocationType type,
			EntityInterceptor[] entityInterceptors,
			EntityHandler entityHandler) {
		super(entityInterceptors, entityHandler);
		this.query = query;
		this.searchOption = searchOption;
		this.resultCallback = resultCallback;
		this.type = type;
	}

	public EntityQueryInvocationImpl(Query query, SearchOption searchOption, EntityStreamSearchHandler<?> streamSearchHandler,
			InvocationType type,
			EntityInterceptor[] entityInterceptors,
			EntityHandler entityHandler) {
		super(entityInterceptors, entityHandler);
		this.query = query;
		this.searchOption = searchOption;
		this.resultCallback = streamSearchHandler.getDummyCallback();
		this.streamSearchHandler = streamSearchHandler;
		this.type = type;
	}

	public Query getQuery() {
		return query;
	}

	public void setQuery(Query query) {
		this.query = query;
	}

	public InvocationType getType() {
		return type;
	}

	public Predicate<?> getPredicate() {
		return resultCallback;
	}

	public void setPredicate(Predicate<?> resultCallback) {
		this.resultCallback = resultCallback;
	}

	public EntityStreamSearchHandler<?> getStreamSearchHandler() {
		return streamSearchHandler;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Void callEntityHandler(EntityHandler eh) {
		switch (type) {
		case SEARCH:
			eh.search(query, (EntityStreamSearchHandler<Object[]>) streamSearchHandler, (Predicate<Object[]>) resultCallback);
			break;
		case SEARCH_ENTITY:
			eh.searchEntity(query, searchOption.isReturnStructuredEntity(), (EntityStreamSearchHandler<Entity>) streamSearchHandler, (Predicate<Entity>) resultCallback);
			break;
		default:
			break;
		}
		return null;
	}

	@Override
	public SearchOption getSearchOption() {
		return searchOption;
	}

}
