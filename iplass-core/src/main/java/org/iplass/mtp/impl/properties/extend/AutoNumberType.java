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

package org.iplass.mtp.impl.properties.extend;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import org.codehaus.groovy.runtime.MethodClosure;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.entity.definition.properties.AutoNumberProperty;
import org.iplass.mtp.entity.definition.properties.NumberingType;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.primary.EntityField;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.counter.CounterService;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.entity.property.MetaProperty;
import org.iplass.mtp.impl.entity.property.PrimitivePropertyHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.entity.property.PropertyType;
import org.iplass.mtp.impl.properties.basic.StringType;
import org.iplass.mtp.impl.script.GroovyScriptEngine;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.impl.script.ScriptRuntimeException;
import org.iplass.mtp.impl.script.template.GroovyTemplate;
import org.iplass.mtp.impl.script.template.GroovyTemplateBinding;
import org.iplass.mtp.impl.script.template.GroovyTemplateCompiler;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.DateUtil;
import org.iplass.mtp.util.StringUtil;


//FIXME 普通のWapperTypeが妥当
public class AutoNumberType extends ComplexWrapperType {
	private static final long serialVersionUID = 5091141571328404880L;

	public static final String ACCEPT_SKIP_COUNTER_SERVICE_NAME = "AutoNumberTypeCounterAcceptSkip";
	public static final String NO_SKIP_COUNTER_SERVICE_NAME = "AutoNumberTypeCounterNoSkip";
	
	private static StringType actualType = new StringType();

	private static ComplexWrapperTypeLoadAdapter loadAdaper = new ComplexWrapperTypeLoadAdapter() {
		@Override
		public void setContext(EntityContext context) {
		}
		@Override
		public void nextCalled(List<Object> values) {
		}
		@Override
		public Object toComplexWrapperTypeValue(Object value) {
			return value;
		}
		@Override
		public Object[] newComplexWrapperTypeArray(int size) {
			return new String[size];
		}
		@Override
		public void close() {
		}
	};

	private int fixedNumberOfDigits = -1;
	private String formatScript;
	private long startsWith = 0;
	private NumberingType numberingType = NumberingType.ALLOW_SKIPPING;

	public static String createIncrementUnitKey(String entityDefId, String propDefId) {
		return entityDefId + "." + propDefId;
	}



	@Override
	public boolean isCompatibleTo(PropertyType another) {
		if (another instanceof AutoNumberType) {
			return true;
		} else {
			return super.isCompatibleTo(another);
		}
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + fixedNumberOfDigits;
		result = prime * result
				+ ((formatScript == null) ? 0 : formatScript.hashCode());
		result = prime * result
				+ ((numberingType == null) ? 0 : numberingType.hashCode());
		result = prime * result + (int) (startsWith ^ (startsWith >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AutoNumberType other = (AutoNumberType) obj;
		if (fixedNumberOfDigits != other.fixedNumberOfDigits)
			return false;
		if (formatScript == null) {
			if (other.formatScript != null)
				return false;
		} else if (!formatScript.equals(other.formatScript))
			return false;
		if (numberingType != other.numberingType)
			return false;
		if (startsWith != other.startsWith)
			return false;
		return true;
	}


	public NumberingType getNumberingType() {
		return numberingType;
	}

	public void setNumberingType(NumberingType numberingType) {
		this.numberingType = numberingType;
	}

	public long getStartsWith() {
		return startsWith;
	}

	public void setStartsWith(long startsWith) {
		this.startsWith = startsWith;
	}

	public int getFixedNumberOfDigits() {
		return fixedNumberOfDigits;
	}

	public void setFixedNumberOfDigits(int fixedNumberOfDigits) {
		this.fixedNumberOfDigits = fixedNumberOfDigits;
	}

	public String getFormatScript() {
		return formatScript;
	}

	public void setFormatScript(String formatScript) {
		this.formatScript = formatScript;
	}

	@Override
	public PropertyType actualType() {
		return actualType;
	}

	@Override
	public PropertyDefinitionType getEnumType() {
		return PropertyDefinitionType.AUTONUMBER;
	}
	
	@Override
	public PropertyDefinitionType getDataStoreEnumType() {
		return PropertyDefinitionType.STRING;
	}

	@Override
	public PropertyDefinition createPropertyDefinitionInstance() {
		AutoNumberProperty p = new AutoNumberProperty();
		p.setFixedNumberOfDigits(fixedNumberOfDigits);
		p.setFormatScript(formatScript);
		p.setStartsWith(startsWith);
		p.setNumberingType(numberingType);
		return p;
	}

	@Override
	public void applyDefinition(PropertyDefinition def) {
		super.applyDefinition(def);
		AutoNumberProperty ap = (AutoNumberProperty) def;
		fixedNumberOfDigits = ap.getFixedNumberOfDigits();
		formatScript = ap.getFormatScript();
		startsWith = ap.getStartsWith();
		numberingType = ap.getNumberingType();
	}

	@Override
	public PropertyType copy() {
		AutoNumberType copy = new AutoNumberType();
		copy.fixedNumberOfDigits = fixedNumberOfDigits;
		copy.formatScript = formatScript;
		copy.startsWith = startsWith;
		copy.numberingType = numberingType;
		return copy;
	}

	@Override
	public Object createRuntime(
			MetaProperty metaProperty, MetaEntity metaEntity) {
		return new AutoNumberTypeRuntime(metaEntity.getId(), metaProperty.getId());
	}

	@Override
	public ComplexWrapperTypeLoadAdapter createLoadAdapter() {
		return loadAdaper;
	}

	@Override
	public boolean isNeedPrevStoreTypeValueOnToStoreTypeValue() {
		return false;
	}

	@Override
	public Object toStoreTypeValue(Object extendTypeValue,
			Object prevStoreTypeValue, PropertyHandler ph, EntityHandler eh,
			String oid, Long version, Entity entity) {

//		//一度採番されたら変更不可<-このチェックは、EntityHandler#updateでやってるので、ここではやらない
//		if (prevStoreTypeValue != null) {
//			return prevStoreTypeValue;
//		}

		if (extendTypeValue != null) {
			return extendTypeValue;
		}
		int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
		if (eh.isUseSharedData()) {
			tenantId = ServiceRegistry.getRegistry().getService(TenantContextService.class).getSharedTenantId();
		}
		return ((AutoNumberTypeRuntime) ((PrimitivePropertyHandler) ph).getTypeSpecificRuntime()).newValue(tenantId, entity);
	}

	@Override
	public void notifyAfterDelete(Object storeTypeValue, PropertyHandler ph,
			EntityHandler eh, String oid, Long rbid) {
	}

	@Override
	public void notifyAfterPurge(EntityHandler eh, Long rbid) {
	}

	@Override
	public void notifyAfterRestore(EntityHandler eh, Long rbid) {
	}

	@Override
	public Class<?> extendType() {
		return String.class;
	}

	@Override
	public String toString(Object value) {
		return actualType.toString(value);
	}
	
	@Override
	public Object fromString(String strValue) {
		return actualType.fromString(strValue);
	}

	public class AutoNumberTypeRuntime {
		GroovyTemplate compiledFormatScript;
		CounterService counter;
		private String incrementUnitKey;

		public AutoNumberTypeRuntime(String entityDefId, String propDefId) {
			
			if (formatScript != null) {
				ScriptEngine se = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();
				compiledFormatScript = GroovyTemplateCompiler.compile(
						formatScript, "AutoNumberFormat_" + GroovyTemplateCompiler.randomName(), (GroovyScriptEngine) se);
			}
			if (numberingType == NumberingType.ALLOW_SKIPPING) {
				counter = (CounterService) ServiceRegistry.getRegistry().getService(ACCEPT_SKIP_COUNTER_SERVICE_NAME);
			} else {
				counter = (CounterService) ServiceRegistry.getRegistry().getService(NO_SKIP_COUNTER_SERVICE_NAME);
			}
			incrementUnitKey = createIncrementUnitKey(entityDefId, propDefId);
		}


		String newValue(int tenantId, Entity entity) {
			if (compiledFormatScript == null) {
				return increment(tenantId);
			} else {
				StringWriter sw = new StringWriter();
				try {
					compiledFormatScript.doTemplate(new AutoNumberGroovyTemplateBinding(sw, tenantId, entity));
				} catch (IOException e) {
					throw new ScriptRuntimeException("can not generate auto number:" + e.getMessage(), e);
				}
				return sw.toString();
			}
		}

		public long currentValue() {
			int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
			return counter.current(tenantId, incrementUnitKey);
		}

		public void resetCounter(long startsWith) {
			int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
			counter.resetCounter(tenantId, incrementUnitKey, startsWith - 1);
		}

		String increment(final int tenantId) {
			Long v = counter.increment(tenantId, incrementUnitKey, getStartsWith());
			String vStr = Long.toString(v);
			if (fixedNumberOfDigits >= 0) {
				if (fixedNumberOfDigits >= vStr.length()) {
					vStr = StringUtil.leftPad(vStr, fixedNumberOfDigits, '0');
				}
			}
			return vStr;
		}


		private class AutoNumberGroovyTemplateBinding extends GroovyTemplateBinding {

			private int tenantId;

			AutoNumberGroovyTemplateBinding(StringWriter out, int tenantId, Entity entity) throws IOException {
				super(out);
				this.tenantId = tenantId;
				ExecuteContext ex = ExecuteContext.getCurrentContext();
				Timestamp date = ex.getCurrentTimestamp();//同一トランザクション内の時間を一緒にするため
				setVariable("date", date);
				setVariable("user", AuthContextHolder.getAuthContext().newUserBinding());
				setVariable("entity", entity);
				SimpleDateFormat f = DateUtil.getSimpleDateFormat("yyyy", true);
				setVariable("yyyy", f.format(date));
				f.applyPattern("MM");
				setVariable("MM", f.format(date));
				f.applyPattern("dd");
				setVariable("dd", f.format(date));
				f.applyPattern("HH");
				setVariable("HH", f.format(date));
				f.applyPattern("mm");
				setVariable("mm", f.format(date));
				f.applyPattern("ss");
				setVariable("ss", f.format(date));
				setVariable("nextVal", new MethodClosure((AutoNumberGroovyTemplateBinding) this, "nextVal"));
			}

			@SuppressWarnings("unused")
			public String nextVal() {
				return increment(tenantId);
			}
		}
	}

	@Override
	public ValueExpression translate(EntityField field) {
		return field;
	}

}
