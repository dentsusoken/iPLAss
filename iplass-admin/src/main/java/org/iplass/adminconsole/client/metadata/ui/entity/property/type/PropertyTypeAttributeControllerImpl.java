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

package org.iplass.adminconsole.client.metadata.ui.entity.property.type;

import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyAttribute;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyAttributePane;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyAttribute.DefaultAttribute;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.entity.definition.properties.AutoNumberProperty;
import org.iplass.mtp.entity.definition.properties.BinaryProperty;
import org.iplass.mtp.entity.definition.properties.BooleanProperty;
import org.iplass.mtp.entity.definition.properties.DateProperty;
import org.iplass.mtp.entity.definition.properties.DateTimeProperty;
import org.iplass.mtp.entity.definition.properties.DecimalProperty;
import org.iplass.mtp.entity.definition.properties.ExpressionProperty;
import org.iplass.mtp.entity.definition.properties.FloatProperty;
import org.iplass.mtp.entity.definition.properties.IntegerProperty;
import org.iplass.mtp.entity.definition.properties.LongTextProperty;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.entity.definition.properties.SelectProperty;
import org.iplass.mtp.entity.definition.properties.StringProperty;
import org.iplass.mtp.entity.definition.properties.TimeProperty;

public class PropertyTypeAttributeControllerImpl implements PropertyTypeAttributeController {

	private PropertyDefinitionType[] expressionResultTypes;

	public PropertyTypeAttributeControllerImpl() {

		expressionResultTypes = new PropertyDefinitionType[]{
				PropertyDefinitionType.BOOLEAN,
				PropertyDefinitionType.DATE,
				PropertyDefinitionType.DATETIME,
				PropertyDefinitionType.DECIMAL,
				PropertyDefinitionType.FLOAT,
				PropertyDefinitionType.INTEGER,
				PropertyDefinitionType.SELECT,
				PropertyDefinitionType.STRING,
				PropertyDefinitionType.TIME
		};
	}

	@Override
	public String getTypeDisplayName(PropertyDefinitionType type) {
		return PropertyViewControllType.valueOfDefinitionType(type).displayName();
	}

	@Override
	public PropertyAttribute createTypeAttribute(PropertyDefinitionType type) {
		return PropertyViewControllType.valueOfDefinitionType(type).createAttribute();
	}

	@Override
	public PropertyAttributePane createTypeAttributePane(PropertyDefinitionType type) {
		return PropertyViewControllType.valueOfDefinitionType(type).createAttributePane();
	}

	@Override
	public PropertyDefinition createTypeDefinition(PropertyDefinitionType type) {
		return PropertyViewControllType.valueOfDefinitionType(type).createDefinition();
	}

	@Override
	public PropertyDefinitionType[] getExpressionResultTypes() {
		return expressionResultTypes;
	}

	private enum PropertyViewControllType {

		AUTONUMBER(PropertyDefinitionType.AUTONUMBER, "AutoNumber") {
			@Override
			public PropertyDefinition createDefinition() {
				return new AutoNumberProperty();
			}

			@Override
			public PropertyAttribute createAttribute() {
				return new AutoNumberAttribute();
			}

			@Override
			public PropertyAttributePane createAttributePane() {
				return new AutoNumberAttributePane();
			}
		},
		BINARY(PropertyDefinitionType.BINARY, "Binary") {
			@Override
			public PropertyDefinition createDefinition() {
				return new BinaryProperty();
			}

			@Override
			public PropertyAttribute createAttribute() {
				return new DefaultAttribute();
			}

			@Override
			public PropertyAttributePane createAttributePane() {
				return null;
			}
		},
		BOOLEAN(PropertyDefinitionType.BOOLEAN, "Boolean") {
			@Override
			public PropertyDefinition createDefinition() {
				return new BooleanProperty();
			}

			@Override
			public PropertyAttribute createAttribute() {
				return new DefaultAttribute();
			}

			@Override
			public PropertyAttributePane createAttributePane() {
				return null;
			}
		},
		DATE(PropertyDefinitionType.DATE, "Date") {
			@Override
			public PropertyDefinition createDefinition() {
				return new DateProperty();
			}

			@Override
			public PropertyAttribute createAttribute() {
				return new DefaultAttribute();
			}

			@Override
			public PropertyAttributePane createAttributePane() {
				return null;
			}
		},
		DATETIME(PropertyDefinitionType.DATETIME, "DateTime") {
			@Override
			public PropertyDefinition createDefinition() {
				return new DateTimeProperty();
			}

			@Override
			public PropertyAttribute createAttribute() {
				return new DefaultAttribute();
			}

			@Override
			public PropertyAttributePane createAttributePane() {
				return null;
			}
		},
		DECIMAL(PropertyDefinitionType.DECIMAL, "Decimal") {
			@Override
			public PropertyDefinition createDefinition() {
				return new DecimalProperty();
			}

			@Override
			public PropertyAttribute createAttribute() {
				return new DecimalAttribute();
			}

			@Override
			public PropertyAttributePane createAttributePane() {
				return new DecimalAttributePane();
			}
		},
		EXPRESSION(PropertyDefinitionType.EXPRESSION, "Expression") {
			@Override
			public PropertyDefinition createDefinition() {
				return new ExpressionProperty();
			}

			@Override
			public PropertyAttribute createAttribute() {
				return new ExpressionAttribute();
			}

			@Override
			public PropertyAttributePane createAttributePane() {
				return new ExpressionAttributePane();
			}
		},
		FLOAT(PropertyDefinitionType.FLOAT, "Float") {
			@Override
			public PropertyDefinition createDefinition() {
				return new FloatProperty();
			}

			@Override
			public PropertyAttribute createAttribute() {
				return new DefaultAttribute();
			}

			@Override
			public PropertyAttributePane createAttributePane() {
				return null;
			}
		},
		INTEGER(PropertyDefinitionType.INTEGER, "Integer") {
			@Override
			public PropertyDefinition createDefinition() {
				return new IntegerProperty();
			}

			@Override
			public PropertyAttribute createAttribute() {
				return new DefaultAttribute();
			}

			@Override
			public PropertyAttributePane createAttributePane() {
				return null;
			}
		},
		LONGTEXT(PropertyDefinitionType.LONGTEXT, "LongText") {
			@Override
			public PropertyDefinition createDefinition() {
				return new LongTextProperty();
			}

			@Override
			public PropertyAttribute createAttribute() {
				return new DefaultAttribute();
			}

			@Override
			public PropertyAttributePane createAttributePane() {
				return null;
			}
		},
		REFERENCE(PropertyDefinitionType.REFERENCE, "Reference") {
			@Override
			public PropertyDefinition createDefinition() {
				return new ReferenceProperty();
			}

			@Override
			public PropertyAttribute createAttribute() {
				return new ReferenceAttribute();
			}

			@Override
			public PropertyAttributePane createAttributePane() {
				return new ReferenceAttributePane();
			}
		},
		SELECT(PropertyDefinitionType.SELECT, "Select") {
			@Override
			public PropertyDefinition createDefinition() {
				return new SelectProperty();
			}

			@Override
			public PropertyAttribute createAttribute() {
				return new SelectAttribute();
			}

			@Override
			public PropertyAttributePane createAttributePane() {
				return new SelectAttributePane();
			}
		},
		STRING(PropertyDefinitionType.STRING, "String") {
			@Override
			public PropertyDefinition createDefinition() {
				return new StringProperty();
			}

			@Override
			public PropertyAttribute createAttribute() {
				return new DefaultAttribute();
			}

			@Override
			public PropertyAttributePane createAttributePane() {
				return null;
			}
		},
		TIME(PropertyDefinitionType.TIME, "Time") {
			@Override
			public PropertyDefinition createDefinition() {
				return new TimeProperty();
			}

			@Override
			public PropertyAttribute createAttribute() {
				return new DefaultAttribute();
			}

			@Override
			public PropertyAttributePane createAttributePane() {
				return null;
			}
		};

		private final PropertyDefinitionType definitionType;
		private final String displayName;

		PropertyViewControllType(PropertyDefinitionType definitionType, String displayName) {
			this.definitionType = definitionType;
			this.displayName = displayName;
		}

		public abstract PropertyDefinition createDefinition();
		public abstract PropertyAttribute createAttribute();
		public abstract PropertyAttributePane createAttributePane();

		public PropertyDefinitionType getDefinitionType() {
			return definitionType;
		}

		public String displayName() {
			return displayName;
		}

		public static PropertyViewControllType valueOfDefinitionType(PropertyDefinitionType definitionType) {
			for (PropertyViewControllType type : values()) {
				if (type.getDefinitionType() == definitionType) {
					return type;
				}
			}
			return null;
		}

	}

}
