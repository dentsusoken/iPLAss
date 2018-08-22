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

package org.iplass.mtp.impl.entity.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.primary.EntityField;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.property.ReferencePropertyHandler;

public class EntityBuilder {

	//TODO 追加検索した参照も取り込めるように

	private EntityHandler eh;

	private boolean isMulti;

	private int oidIndex = -1;
	private int versionIndex = -1;
	private List<Object[]> primitives = new ArrayList<>();//nameとindex
	private List<Object[]> references;//nameとEntityBuilder

	private Map<String, Entity> entities = new LinkedHashMap<>();

	public EntityBuilder(EntityHandler eh, EntityContext context, List<ValueExpression> selectValues) {
		this(eh, context, toProps(selectValues));
	}

	private static String[] toProps(List<ValueExpression> selectValues) {
		String[] properties = new String[selectValues.size()];
		ValueExpression v;
		for (int i = 0; i < selectValues.size(); i++) {
			v = selectValues.get(i);
			if (v instanceof EntityField) {
				properties[i] = ((EntityField) v).getPropertyName();
			}
		}
		return properties;
	}

	public EntityBuilder(EntityHandler eh, EntityContext context, String[] properties) {
		this.eh = eh;

		HashMap<String, String[]> subRefs = null;

		for (int i = 0; i < properties.length; i++) {
			String p = properties[i];
			if (p != null) {
				int index = p.indexOf('.');
				if (index < 0) {
					//primitive
					if (oidIndex == -1
							&& p.equals(Entity.OID)) {
						oidIndex = i;
					}
					if (versionIndex == -1
							&& eh.isVersioned()
							&& p.equals(Entity.VERSION)) {
						versionIndex = i;
					}
					primitives.add(new Object[]{p, Integer.valueOf(i)});
				} else {
					//reference
					if (subRefs == null) {
						subRefs = new HashMap<>();
					}
					String refName = p.substring(0, index);
					String[] subProps = subRefs.get(refName);
					if (subProps == null) {
						subProps = new String[properties.length];
						subRefs.put(refName, subProps);
					}
					subProps[i] = p.substring(index + 1);
				}
			}
		}

		if (subRefs != null) {
			references = new ArrayList<>();
			for (Map.Entry<String, String[]> e: subRefs.entrySet()) {
				ReferencePropertyHandler rp = (ReferencePropertyHandler) eh.getDeclaredProperty(e.getKey());
				EntityHandler subEh = rp.getReferenceEntityHandler(context);
				EntityBuilder eb = new EntityBuilder(subEh, context, e.getValue());
				if (rp.getMetaData().getMultiplicity() == PropertyDefinition.MULTIPLICITY_INFINITE
						|| rp.getMetaData().getMultiplicity() > 1) {
					eb.isMulti = true;
				}
				references.add(new Object[]{e.getKey(), eb});
			}
		}

	}

	public Collection<Entity> getCollection() {
		return entities.values();
	}

	public void finished() {
		if (references != null && references.size() > 0) {
			for (Object[] nameAndBuilder: references) {
				EntityBuilder eb = (EntityBuilder) nameAndBuilder[1];
				if (eb.isMulti) {
					//List -> Arrayに変換
					for (Entity e: entities.values()) {
						List<Entity> el = e.getValue((String) nameAndBuilder[0]);
						if (el != null) {
							e.setValue((String) nameAndBuilder[0], el.toArray(eb.eh.newArrayInstance(el.size())));
						}
					}
				}

				eb.finished();
			}
		}
	}

	public Entity handle(Object[] datas) {
		if (datas[oidIndex] == null) {
			return null;
		}

		String key;
		if (versionIndex == -1) {
			key = (String) datas[oidIndex];
		} else {
			key = datas[oidIndex] + "-" + datas[versionIndex];
		}

		boolean isNew = false;
		Entity e = entities.get(key);
		if (e == null) {
			isNew = true;
			e = eh.newInstance();
			if (primitives != null) {
				for (Object[] nameAndIndex: primitives) {
					e.setValue((String) nameAndIndex[0], datas[((Integer) nameAndIndex[1]).intValue()]);
				}
			}
			entities.put(key, e);
		}
		if (references != null) {
			for (Object[] nameAndBuilder: references) {
				EntityBuilder eb = (EntityBuilder) nameAndBuilder[1];
				Entity ref = eb.handle(datas);
				if (ref != null) {
					if (!eb.isMulti) {
						e.setValue((String) nameAndBuilder[0], ref);
					} else {
						//一旦Listで格納
						List<Entity> el = e.getValue((String) nameAndBuilder[0]);
						if (el == null) {
							el = new LinkedList<>();
							e.setValue((String) nameAndBuilder[0], el);
						}
						el.add(ref);
					}
				}
			}
		}

		if (isNew) {
			return e;
		} else {
			return null;
		}
	}

}
