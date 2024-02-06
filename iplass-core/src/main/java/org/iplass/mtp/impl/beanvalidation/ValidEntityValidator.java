/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.beanvalidation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderDefinedContext;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.beanvalidation.constraints.ValidEntity;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.ValidateError;
import org.iplass.mtp.entity.ValidateResult;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.entity.property.ReferencePropertyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidEntityValidator implements ConstraintValidator<ValidEntity, Entity> {
	private static Logger logger = LoggerFactory.getLogger(ValidEntityValidator.class);
	
	private PropTree propTree;
	
	private boolean hasMessage;
	private EntityManager em;
	
	@Override
	public void initialize(ValidEntity constraintAnnotation) {
		String[] props = constraintAnnotation.properties();
		if (props != null && props.length > 0) {
			propTree = new PropTree();
			for (String p: props) {
				propTree.parse(p);
			}
		}
		
		if (constraintAnnotation.message() != null && constraintAnnotation.message().length() > 0) {
			hasMessage = true;
		}
		
		em = ManagerLocator.getInstance().getManager(EntityManager.class);
	}
	
	@Override
	public boolean isValid(Entity value, ConstraintValidatorContext context) {
		if (!hasMessage) {
			context.disableDefaultConstraintViolation();
		}
		
		return isValidCascade(value, new LinkedList<>(), propTree, context);
	}
	
	private Set<String> allRefs(EntityHandler eh, EntityContext ec) {
		
		HashSet<String> refs = new HashSet<>();
		for (ReferencePropertyHandler rh: eh.getReferencePropertyList(false, ec)) {
			refs.add(rh.getName());
		}
		return refs;
	}
	
	private boolean isValidCascade(Entity value, List<Object> path, PropTree target, ConstraintValidatorContext context) {
		
		List<String> props = (target == null || target.isAllPrimitive) ? null: target.reaf();
		
		ValidateResult vr = em.validate(value, props);
		if (vr.hasError()) {
			for (ValidateError ve: vr.getErrors()) {
				for (String msg: ve.getErrorMessages()) {
					NodeBuilderCustomizableContext bbcc = null;
					NodeBuilderDefinedContext nbdc = null;
					Integer index = null;
					for (Object p: path) {
						if (p instanceof String) {
							if (nbdc != null) {
								bbcc = nbdc.addPropertyNode((String) p);
								nbdc = null;
							} else if (bbcc != null) {
								bbcc = bbcc.addPropertyNode((String) p);
							} else {
								bbcc = context.buildConstraintViolationWithTemplate(msg).addPropertyNode((String) p);
							}
							if (index != null) {
								nbdc = bbcc.inIterable().atIndex(index);
								index = null;
							}
						} else {
							index = (Integer) p;
						}
					}
					
					if (nbdc != null) {
						bbcc = nbdc.addPropertyNode(ve.getPropertyName());
					} else if (bbcc != null) {
						bbcc = bbcc.addPropertyNode(ve.getPropertyName());
					} else {
						bbcc = context.buildConstraintViolationWithTemplate(msg)
							.addPropertyNode(ve.getPropertyName());
					}
					if (index != null) {
						bbcc.inIterable().atIndex(index).addConstraintViolation();
					} else {
						bbcc.addConstraintViolation();
					}
				}
			}
		}
		
		boolean isValid = !vr.hasError();
		
		EntityContext ec = EntityContext.getCurrentContext();
		EntityHandler eh = ec.getHandlerByName(value.getDefinitionName());
		
		Set<String> childKeys = (target == null || target.isAllReference) ? allRefs(eh, ec): target.childKeys();
		
		for (String ref: childKeys) {
			PropertyHandler ph = eh.getProperty(ref, ec);
			if (ph == null || !(ph instanceof ReferencePropertyHandler)) {
				logger.warn("ReferenceProperty:" + ref + " is undefined on " + value.getDefinitionName() + ", so can not validate..");
				continue;
			}
			
			PropTree child = (target == null || target.isAllReference) ? null: target.children.get(ref);
			if (ph.getMetaData().getMultiplicity() == 1) {
				Entity refEntity = value.getValue(ref);
				if (refEntity != null) {
					path.add(ref);
					try {
						isValid &= isValidCascade(refEntity, path, child, context);
					} finally {
						path.remove(path.size() - 1);
					}
				}
			} else {
				Entity[] refEntities = value.getValue(ref);
				if (refEntities != null) {
					path.add(ref);
					try {
						for (int i = 0; i < refEntities.length; i++) {
							path.add(i);
							try {
								isValid &= isValidCascade(refEntities[i], path, child, context);
							} finally {
								path.remove(path.size() - 1);
							}
						}
					} finally {
						path.remove(path.size() - 1);
					}
				}
			}
		}
		
		return isValid;
	}
	
	private static class PropTree {
		PropTree parent;
		boolean isAllPrimitive;
		boolean isAllReference;
		List<String> reaf;
		LinkedHashMap<String, PropTree> children;
		
		Set<String> childKeys() {
			if (children == null) {
				return Collections.emptySet();
			}
			return children.keySet();
		}
		
		List<String> reaf() {
			if (reaf == null) {
				return Collections.emptyList();
			}
			return reaf;
		}
		
		void parse(String path) {
			int index = path.indexOf('.');
			if (index > 0) {
				String name = path.substring(0, index);
				String subpath = path.substring(index + 1);
				PropTree child = child(name, parent);
				child.parse(subpath);
			} else {
				if (reaf == null) {
					reaf = new ArrayList<>();
				}
				reaf.add(path);
				if (path.equals("*")) {
					isAllPrimitive = true;
				}
				if (path.equals("**")) {
					isAllPrimitive = true;
					isAllReference = true;
				}
			}
		}
		
		PropTree child(String name, PropTree parent) {
			if (children == null) {
				children = new LinkedHashMap<>();
			}
			PropTree p = children.get(name);
			if (p == null) {
				p = new PropTree();
				p.parent = parent;
				children.put(name, p);
			}
			return p;
		}
	}

}
