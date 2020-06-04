/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.SearchResult;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.impl.datastore.strategy.SearchResultIterator;

public class EntityStreamSearchHandler<T> {

	private T currentRow;

	private EntityHandler eh;
	private Class<T> resultType;

	private int totalCount = -1;

	private Query q;
	private SearchResultIterator sri;
	private Predicate<T> callback;

	private StreamSearchResult result;
	private It it;

	public EntityStreamSearchHandler(EntityHandler eh, Class<T> resultType) {
		this.eh = eh;
		this.resultType = resultType;
	}

	public Predicate<T> getDummyCallback() {
		return new DummyCallback();
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public void setStreamSearchResult(Query q, SearchResultIterator sri, Predicate<T> callback) {
		this.sri = sri;
		this.q = q;
		this.callback = callback;
		result = new StreamSearchResult(totalCount);
	}

	public StreamSearchResult getStreamSearchResult() {
		return result;
	}

	class DummyCallback implements Predicate<T> {

		@Override
		public boolean test(T dataModel) {
			currentRow = dataModel;
			return true;
		}

	}

	class StreamSearchResult extends SearchResult<T> {

		public StreamSearchResult(int totalCount) {
			super(totalCount, null);
		}

		@Override
		public List<T> getList() {
			if (it != null) {
				throw new IllegalStateException("SearchResult Iterator already fetched, so can not get as list.");
			}
			List<T> ret = new ArrayList<>();
			try {
				for (T t: this) {
					ret.add(t);
				}
			} finally {
				close();
			}
			return ret;
		}

		@Override
		public T getFirst() {
			if (it != null) {
				throw new IllegalStateException("SearchResult Iterator already fetched, so can not get first data.");
			}

			try {
				Iterator<T> myIt = iterator();
				if (myIt.hasNext()) {
					return myIt.next();
				} else {
					return null;
				}
			} finally {
				close();
			}
		}

		@Override
		public <P> List<P> getValueList(String propertyName) {
			if (it != null) {
				throw new IllegalStateException("SearchResult Iterator already fetched, so can not get as list.");
			}
			if (resultType  != Entity.class) {
				throw new EntityRuntimeException("for use getValueList(propertyName), result type must Entity.");
			}

			List<P> res = new ArrayList<>();
			try {
				for (T t: this) {
					res.add(((Entity) t).<P>getValue(propertyName));
				}
			} finally {
				close();
			}
			return res;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <P> List<P> getValueList(int index) {
			if (it != null) {
				throw new IllegalStateException("SearchResult Iterator already fetched, so can not get as list.");
			}
			if (resultType  != Object[].class) {
				throw new EntityRuntimeException("for use getValueList(index), result type must Object[].");
			}

			List<P> res = new ArrayList<P>();
			try {
				for (T t: this) {
					res.add((P) ((Object[]) t)[index]);
				}
			} finally {
				close();
			}
			return res;
		}

		@Override
		public Iterator<T> iterator() {
			if (it != null) {
				throw new IllegalStateException("SearchResult Iterator already created, so can not create new iterator.");
			}
			it = new It();
			return it;
		}

		@Override
		public ResultMode getResultMode() {
			return ResultMode.STREAM;
		}

		@Override
		public void close() {
			if (sri != null) {
				sri.close();
				sri = null;
			}
		}

		@Override
		protected void finalize() throws Throwable {
			close();
		}

	}

	private class It implements Iterator<T> {

		private boolean nextCalled;
		private boolean hasNext;

		@Override
		public boolean hasNext() {
			if (!nextCalled) {
				hasNext = sri.next();
				nextCalled = true;
			}
			return hasNext;
		}

		@SuppressWarnings("unchecked")
		@Override
		public T next() {
			if (!nextCalled) {
				hasNext();
			}
			if (!hasNext) {
				throw new NoSuchElementException("no such Element");
			}

			T t = null;
			if (resultType == Object[].class) {
				List<ValueExpression> select = q.getSelect().getSelectValues();
				Object[] row = new Object[select.size()];
				for (int i = 0; i < row.length; i++) {
					row[i] = sri.getValue(i);
				}
				t = (T) row;
			} else if (resultType == Entity.class) {
				Entity entity = EntityHandler.getAsEntity(eh, sri, q);
				t = (T) entity;
			} else {
				throw new IllegalArgumentException("resultType:" + resultType + " not supported");
			}

			callback.test(t);

			nextCalled = false;

			return currentRow;
		}
	}

}
