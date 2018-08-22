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

package org.iplass.mtp.impl.tools.entity;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.util.StringUtil;

public class EntityJavaMappingClassWriter implements Closeable {

	private EntityDefinition definition;

	private PrintWriter writer;

	private String directClassName;

	private ClassInfo classInfo;
	private Set<String> imports;
	private List<PropertyInfo> properties;

	public EntityJavaMappingClassWriter(OutputStream out, EntityDefinition definition, String directClassName) throws IOException {
		this.writer = new PrintWriter(new OutputStreamWriter(out, "UTF-8"));
		this.definition = definition;
		this.directClassName = directClassName;
	}

	@Override
	public void close() throws IOException {
		writer.close();
	}

	public void writeJavaClass() throws IOException {
		if (definition == null) {
			return;
		}

		analyze();

		writePackage();
		writeImports();
		writeClassComment();
		writeClassDef();
		writeStaticField();
		writeConstractor();
		writeFieldAccessor();
		writeClassEndTag();

	}

	private void analyze() {
		analyzeClassName();
		analyzeProperty();
	}

	private void analyzeClassName() {
		classInfo = new ClassInfo();

		String classFullName = null;
		if (StringUtil.isNotBlank(directClassName)) {
			//直接Classが指定されている場合はその値
			classFullName = directClassName;
		} else if (definition.getMapping() != null && StringUtil.isNotBlank(definition.getMapping().getMappingModelClass())) {
			//Mappingクラスが指定されている場合はその値
			classFullName = definition.getMapping().getMappingModelClass();
		} else {
			//Mappingクラスが指定されていない場合はname
			classFullName = definition.getName();
		}

		String packageName = null;
		if (classFullName.lastIndexOf(".") >= 0) {
			packageName = classFullName.substring(0, classFullName.lastIndexOf("."));
		} else {
			packageName = "";
		}
		classInfo.packageName = packageName;
		classInfo.className = getSimpleClassName(classFullName);

	}

	private void analyzeProperty() {
		properties = new ArrayList<PropertyInfo>();

		imports = new TreeSet<String>(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});
		imports.add("org.iplass.mtp.entity.GenericEntity");

		for (PropertyDefinition propDef : definition.getPropertyList()) {
			if (propDef.isInherited()) {
				continue;
			}

			PropertyInfo property = new PropertyInfo();
			property.propertyName = propDef.getName();
			property.propertyDisplayName = propDef.getDisplayName();
			property.constName = StringUtil.decamelize(propDef.getName());
			property.methodName = StringUtil.capitalize(StringUtil.camelize(property.constName));

			if (propDef instanceof ReferenceProperty) {
				//ReferenceのMappingクラスチェック
				property.typeFullClassName = getReferenceClassName((ReferenceProperty)propDef);
				property.typeClassName = getSimpleClassName(property.typeFullClassName);
				property.isReference = true;
			} else {
				property.typeFullClassName = propDef.getJavaType().getName();
				property.typeClassName = propDef.getJavaType().getSimpleName();
				property.isReference = false;
			}
			//import定義用
			if (!(property.typeFullClassName.startsWith("java.lang"))) {
				imports.add(property.typeFullClassName);
			}

			property.isArray = (propDef.getMultiplicity() > 1 || propDef.getMultiplicity() == -1);
			property.isBoolean = (propDef.getJavaType() == Boolean.class);
			property.isReadOnly = propDef.isReadOnly();

			properties.add(property);
		}

	}

	private String getSimpleClassName(String classFullName) {
		if (classFullName.lastIndexOf(".") >= 0) {
			return StringUtil.capitalize(classFullName.substring(classFullName.lastIndexOf(".") + 1));
		} else {
			return StringUtil.capitalize(classFullName);
		}
	}

	private String getReferenceClassName(ReferenceProperty property) {
		//参照Entity定義を取得
		EntityDefinitionManager edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
		EntityDefinition definition = edm.get(property.getObjectDefinitionName());
		if (definition != null && definition.getMapping() != null) {
			return definition.getMapping().getMappingModelClass();
		}
		//デフォルト定義を返す
		return property.getJavaType().getName();
	}

	private void writePackage() {

		if (StringUtil.isNotBlank(classInfo.packageName)) {
			writer.println("package " + classInfo.packageName + ";");
			writer.println("");
		}
	}

	private void writeImports() {
		//GenericEntityとReferenceMappingClassをimport
		Iterator<String> ite = imports.iterator();
		while(ite.hasNext()) {
			writer.println("import " + ite.next() + ";");
		}
		writer.println("");
	}

	private void writeClassComment() {

		writer.println("/**");

		String displayName = null;
		if (StringUtil.isBlank(definition.getDisplayName())) {
			displayName = definition.getName();
		} else {
			displayName = definition.getDisplayName();
		}
		writer.println(" * " + displayName + " Entity。");

		if (StringUtil.isNotBlank(definition.getDescription())) {
			String description = definition.getDescription();
			description = "<p>" + description + "</p>";
			description.replaceAll("\r\n", "\n");
			description.replaceAll("\r", "\n");
			String[] descArray = description.split("\n");

			writer.println(" * ");
			for (String desc : descArray) {
				writer.println(" * " + desc);
			}
		}

		writer.println(" */");
	}

	private void writeClassDef() {
		writer.println("public class " + classInfo.className + " extends GenericEntity {");
		writer.println("");
	}

	private void writeStaticField() {

		//serialVersionUID
		writer.println("\tprivate static final long serialVersionUID = 1L;");
		writer.println("");

		//DEFINITION_NAME
		writer.println("\t/** Entity Definition Name */");
		writer.println("\tpublic static final String DEFINITION_NAME = \"" + definition.getName() + "\";");
		writer.println("");

		//PROPERTY_NAME
		for (PropertyInfo property : properties) {
			writer.println("\t/** " + property.propertyDisplayName + " */");
			writer.println("\tpublic static final String " + property.constName + " = \"" + property.propertyName + "\";");
		}
		writer.println("");

	}

	private void writeConstractor() {
		writer.println("\tpublic " + classInfo.className + "() {");
		writer.println("\t\tsetDefinitionName(DEFINITION_NAME);");
		writer.println("\t}");
		writer.println("");
	}

	private void writeFieldAccessor() {
		for (PropertyInfo property : properties) {
			String typeMultiSuffix = "";
			if (property.isArray) {
				typeMultiSuffix = "[]";
			}

			//getter
			writer.println("\t" + "/**");
			writer.println("\t" + " * " + property.propertyDisplayName + "を返します。");
			writer.println("\t" + " * ");
			writer.println("\t" + " * @return " + property.propertyDisplayName);
			writer.println("\t" + " */");

//			String methodPrefix = "get";
//			if (property.isBoolean) {
//				methodPrefix = "is";
//			}
			writer.println("\t" + "public " + property.typeClassName + typeMultiSuffix + " get" + property.methodName + "() {");

			if (property.isReference && property.isArray) {
				//配列判定処理(loadとsearchで違うため)
				writer.println("\t\t" + "Object value = getValue(" + property.constName + ");");
				writer.println("\t\t" + "if (value instanceof " + property.typeClassName + ") {");
				writer.println("\t\t\t" + "return new " + property.typeClassName + "[]{(" + property.typeClassName + ")value};\t//for search");
				writer.println("\t\t" + "} else {");
				writer.println("\t\t\t" + "return (" + property.typeClassName + "[])value;\t//for load");
				writer.println("\t\t" + "}");
			} else {
				writer.println("\t\t" + "return getValue(" + property.constName + ");");
			}
			writer.println("\t" + "}");
			writer.println("");

			//Booleanの場合はisも作成
			if (property.isBoolean && !property.isArray) {
				writer.println("\t" + "/**");
				writer.println("\t" + " * " + property.propertyDisplayName + "を返します。");
				writer.println("\t" + " * 値がnullの場合はfalseを返します。");
				writer.println("\t" + " * ");
				writer.println("\t" + " * @return " + property.propertyDisplayName);
				writer.println("\t" + " */");

				writer.println("\t" + "public boolean is" + property.methodName + "() {");
				writer.println("\t\t" + "Boolean value = getValue(" + property.constName + ");");
				writer.println("\t\t" + "if (value == null) {");
				writer.println("\t\t\t" + "return false;");
				writer.println("\t\t" + "}");
				writer.println("\t\t" + "return value.booleanValue();");
				writer.println("\t" + "}");
				writer.println("");
			}


			//setter
			if (property.isReadOnly) {
				continue;
			}

			writer.println("\t" + "/**");
			writer.println("\t" + " * " + property.propertyDisplayName + "を設定します。");
			writer.println("\t" + " * ");
			writer.println("\t" + " * @param " + property.propertyName + " " + property.propertyDisplayName);
			writer.println("\t" + " */");

			writer.println("\t" + "public void set" + property.methodName + "(" + property.typeClassName + typeMultiSuffix + " " + property.propertyName + ") {");
			writer.println("\t\t" + "setValue(" + property.constName + ", " + property.propertyName + ");");
			writer.println("\t" + "}");
			writer.println("");
		}
	}

	private void writeClassEndTag() {
		writer.println("}");
	}

	private class ClassInfo {
//		public String classFullName;
		public String packageName;
		public String className;
	}

	private class PropertyInfo {
		public String propertyName;
		public String propertyDisplayName;
		public String constName;
		public String methodName;
		public String typeFullClassName;
		public String typeClassName;
		public boolean isArray;
		public boolean isReadOnly;
		public boolean isBoolean;
		public boolean isReference;
	}

}
