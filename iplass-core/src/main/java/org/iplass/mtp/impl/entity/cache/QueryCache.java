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
package org.iplass.mtp.impl.entity.cache;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.GenericEntity;
import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.entity.interceptor.InvocationType;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.primary.EntityField;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.util.ObjectUtil;

public class QueryCache implements Serializable {
	private static final long serialVersionUID = 3525984317258640329L;

	private final Integer count;
	private final List<?> list;
	private final InvocationType listType;
	private final long eol;

	public QueryCache(Integer count, List<?> list, InvocationType listType) {
		this.count = count;
		this.list = list;
		this.listType = listType;
		this.eol = -1;
	}
	public QueryCache(Integer count, List<?> list, InvocationType listType, int ttl) {
		this.count = count;
		this.list = list;
		this.listType = listType;
		if (ttl > 0) {
			this.eol = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(ttl);
		} else {
			this.eol = -1;
		}
	}

	public Integer getCount() {
		return count;
	}
	public List<?> getList() {
		return list;
	}
	public InvocationType getListType() {
		return listType;
	}
	public long getEol() {
		return eol;
	}

	public boolean eol() {
		if (eol <= 0) {
			return false;
		} else {
			long now = System.currentTimeMillis();
			return now > eol;
		}
	}

	public boolean canIterate(InvocationType invocationType) {
		if (invocationType == InvocationType.SEARCH_ENTITY) {
			if (list != null) {
				return true;
			}
		} else {
			if (listType == InvocationType.SEARCH) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public <T> void iterate(Predicate<T> callback, Query query, InvocationType invocationType, EntityHandler eh) {
		//Object[] -> Entityは可だが、Entity -> Object[]は不可。
		//Entity化する過程でselect項目欠落している可能性ある
		if (invocationType == InvocationType.SEARCH_ENTITY) {
			if (listType == InvocationType.SEARCH_ENTITY) {
				for (Entity e: (List<Entity>) list) {
					if (e instanceof GenericEntity) {
						e = ((GenericEntity) e).deepCopy();
					} else if (e instanceof Serializable) {
						e = (Entity) ObjectUtil.deepCopy((Serializable) e);
					} else {
						throw new EntityRuntimeException("cant copy from entity query cache:" + e);
					}
					boolean ret = callback.test((T) e);
					if (!ret) {
						break;
					}
				}
			} else if (listType == InvocationType.SEARCH) {
				for (Object[] rawData: (List<Object[]>) list) {
					Entity e = eh.newInstance();
					for (int i = 0; i < query.getSelect().getSelectValues().size(); i++) {
						ValueExpression propName = query.getSelect().getSelectValues().get(i);
						if (propName instanceof EntityField) {
							EntityField ef = (EntityField) propName;
							e.setValue(ef.getPropertyName(), copyVal(rawData[i]));
						}
					}
					boolean ret = callback.test((T) e);
					if (!ret) {
						break;
					}
				}
			}


		} else {
			if (listType == InvocationType.SEARCH) {
				for (Object[] rawData: (List<Object[]>) list) {
					Object[] copy = new Object[rawData.length];
					for (int i = 0; i < rawData.length; i++) {
						copy[i] = copyVal(rawData[i]);
					}
					boolean ret = callback.test((T) copy);
					if (!ret) {
						break;
					}
				}
			}
		}
	}

	private Object copyVal(Object val) {
		if (val instanceof Object[]) {
			Object[] valArray = (Object[]) val;
			Object[] newValArray = (Object[]) Array.newInstance(valArray.getClass().getComponentType(), valArray.length);
			for (int i = 0; i < valArray.length; i++) {
				newValArray[i] = copyVal(valArray[i]);
			}
			return valArray;
		}
		if (val instanceof SelectValue) {
			return ((SelectValue) val).copy();
		}
		if (val instanceof BinaryReference) {
			return ((BinaryReference) val).copy();
		}
		if (val instanceof Date) {
			return ((Date) val).clone();
		}
		return val;
	}

}
