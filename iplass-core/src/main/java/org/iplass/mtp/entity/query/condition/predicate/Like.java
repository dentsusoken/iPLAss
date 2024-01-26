/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.entity.query.condition.predicate;

import org.iplass.mtp.entity.query.ASTNode;
import org.iplass.mtp.entity.query.ASTTransformer;
import org.iplass.mtp.entity.query.condition.ConditionVisitor;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.ValueExpressionVisitor;
import org.iplass.mtp.entity.query.value.primary.EntityField;
import org.iplass.mtp.entity.query.value.primary.Literal;
import org.iplass.mtp.util.StringUtil;

/**
 * Like条件文を表す。
 * patternは、%、_でワイルドカード指定可能。
 * エスケープ文字は、\（%、_のエスケープは、\%、\_。\のエスケープは\\。）。
 * 
 * @author K.Higuchi
 *
 */
public class Like extends Predicate {
	private static final long serialVersionUID = 5430090669405885205L;

	/**
	 * pattern中、一文字のワイルドカードを示す。_、アンダースコア。
	 */
	public static final String US ="_";
	/**
	 * pattern中、複数文字のワイルドカードを示す。%、パーセントシンボル。
	 */
	public static final String PS = "%";
	
	/**
	 * pattern中、_、%をエスケープする際のエスケープ文字。\（java文字列上は\\）。
	 */
	public static final String ES = "\\";
	
	public enum MatchPattern {
		/** 前方一致 */
		PREFIX,
		/** 後方一致 */
		POSTFIX,
		/** 部分一致 */
		PARTIAL
	}
	
	public enum CaseType {
		/** Case Insensitive。大文字小文字区別しない。デフォルト。 */
		CI,
		/** Case Sensitive。大文字小文字区別する。 */
		CS
	}
	
	private Literal pattern;
	private ValueExpression property;
	private CaseType caseType = CaseType.CI;
	
	public Like() {
	}
	
	/**
	 * <b>このメソッドのpatternは、自動的にエスケープ処理されない点、注意</b>
	 * 
	 * @param propertyName
	 * @param pattern
	 */
	@Deprecated
	public Like(String propertyName, String pattern) {
		this(propertyName, pattern, CaseType.CI);
	}

	/**
	 * <b>このメソッドのpatternは、自動的にエスケープ処理されない点、注意</b>
	 * 
	 * @param propertyName
	 * @param pattern
	 * @param caseType
	 */
	@Deprecated
	public Like(String propertyName, String pattern, CaseType caseType) {
		setPropertyName(propertyName);
		this.pattern = new Literal(pattern);
		this.caseType = caseType;
	}
	
	/**
	 * <b>このメソッドのpatternは、自動的にエスケープ処理されない点、注意</b>
	 * 
	 * @param propertyName
	 * @param pattern
	 * @param caseType
	 */
	@Deprecated
	public Like(String propertyName, Literal pattern, CaseType caseType) {
		setPropertyName(propertyName);
		this.pattern = pattern;
		this.caseType = caseType;
	}
	
	/**
	 * 指定のstrをエスケープしたのち、matchPatternの指定により、
	 * 前後に%を付与する形でpatternを構築。
	 * 
	 * @param property
	 * @param str
	 * @param matchPattern
	 */
	public Like(String property, String str, MatchPattern matchPattern) {
		this(property, str, matchPattern, CaseType.CI);
	}
	
	/**
	 * 指定のstrをエスケープしたのち、matchPatternの指定により、
	 * 前後に%を付与する形でpatternを構築。
	 * 
	 * @param property
	 * @param str
	 * @param matchPattern
	 */
	public Like(ValueExpression property, String str, MatchPattern matchPattern) {
		this(property, str, matchPattern, CaseType.CI);
	}

	/**
	 * 指定のstrをエスケープしたのち、matchPatternの指定により、
	 * 前後に%を付与する形でpatternを構築。
	 * caseTypeで大文字小文字を区別するかどうかを指定可能。
	 * 
	 * @param property
	 * @param str
	 * @param matchPattern
	 * @param caseType
	 */
	public Like(String property, String str, MatchPattern matchPattern, CaseType caseType) {
		this(property, str, matchPattern, caseType, true);
	}
	
	/**
	 * 指定のstrをエスケープしたのち、matchPatternの指定により、
	 * 前後に%を付与する形でpatternを構築。
	 * caseTypeで大文字小文字を区別するかどうかを指定可能。
	 * asBindVariableでpatternをバインド変数として扱うか否かを指定
	 * 
	 * @param property
	 * @param str
	 * @param matchPattern
	 * @param caseType
	 * @param asBindVariable
	 */
	public Like(String property, String str, MatchPattern matchPattern, CaseType caseType, boolean asBindVariable) {
		setPropertyName(property);
		StringBuilder sb= new StringBuilder();
		if (matchPattern == MatchPattern.POSTFIX || matchPattern == MatchPattern.PARTIAL) {
			sb.append(PS);
		}
		sb.append(StringUtil.escapeEqlForLike(str));
		if (matchPattern == MatchPattern.PREFIX || matchPattern == MatchPattern.PARTIAL) {
			sb.append(PS);
		}
		this.pattern = new Literal(sb.toString(), asBindVariable);
		this.caseType = caseType;
	}
	
	/**
	 * <b>このメソッドのpatternは、自動的にエスケープ処理されない点、注意</b>
	 * 
	 * @param property
	 * @param pattern
	 */
	@Deprecated
	public Like(ValueExpression property, String pattern) {
		this(property, pattern, CaseType.CI);
	}

	/**
	 * <b>このメソッドのpatternは、自動的にエスケープ処理されない点、注意</b>
	 * 
	 * @param property
	 * @param pattern
	 * @param caseType
	 */
	@Deprecated
	public Like(ValueExpression property, String pattern, CaseType caseType) {
		setProperty(property);
		this.pattern = new Literal(pattern);
		this.caseType = caseType;
	}
	
	/**
	 * <b>このメソッドのpatternは、自動的にエスケープ処理されない点、注意</b>
	 * 
	 * @param property
	 * @param pattern
	 * @param caseType
	 */
	public Like(ValueExpression property, Literal pattern, CaseType caseType) {
		setProperty(property);
		this.pattern = pattern;
		this.caseType = caseType;
	}
	
	public Like(ValueExpression property, String str, MatchPattern matchPattern, CaseType caseType) {
		this(property, str, matchPattern, caseType, false);
	}
	
	public Like(ValueExpression property, String str, MatchPattern matchPattern, CaseType caseType, boolean asBindVariable) {
		setProperty(property);
		StringBuilder sb= new StringBuilder();
		if (matchPattern == MatchPattern.POSTFIX || matchPattern == MatchPattern.PARTIAL) {
			sb.append(PS);
		}
		sb.append(StringUtil.escapeEqlForLike(str));
		if (matchPattern == MatchPattern.PREFIX || matchPattern == MatchPattern.PARTIAL) {
			sb.append(PS);
		}
		this.pattern = new Literal(sb.toString(), asBindVariable);
		this.caseType = caseType;
	}
	
	public CaseType getCaseType() {
		return caseType;
	}

	public void setCaseType(CaseType caseType) {
		this.caseType = caseType;
	}

	public String getPropertyName() {
		if (property == null) {
			return null;
		}
		return property.toString();
	}
	
	public void setPropertyName(String propertyName) {
		property = new EntityField(propertyName);
	}
	
	public void setProperty(ValueExpression property) {
		this.property = property;
	}
	
	public ValueExpression getProperty() {
		return property;
	}
	
	public String getPattern() {
		if (pattern == null) {
			return null;
		}
		return (String) pattern.getValue();
	}
	
	public void setPattern(String pattern) {
		this.pattern = new Literal(pattern);
	}
	
	public Literal getPatternAsLiteral() {
		return pattern;
	}
	
	public void setPatternAsLiteral(Literal pattern) {
		this.pattern = pattern;
	}
	
	public void accept(ConditionVisitor visitor) {
		if (visitor.visit(this)) {
			if (visitor instanceof ValueExpressionVisitor) {
				if (getProperty() != null) {
					getProperty().accept((ValueExpressionVisitor) visitor);
				}
			}
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getPropertyName()).append(" like ");
		if (pattern == null) {
			sb.append("null");
		} else {
			sb.append(pattern.toString());
		}
		if (caseType != null && caseType == CaseType.CS) {
			sb.append(" cs");
		}
		return sb.toString();
	}
	public ASTNode accept(ASTTransformer transformer) {
		return transformer.visit(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((caseType == null) ? 0 : caseType.hashCode());
		result = prime * result + ((pattern == null) ? 0 : pattern.hashCode());
		result = prime * result
				+ ((property == null) ? 0 : property.hashCode());
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
		Like other = (Like) obj;
		if (caseType != other.caseType)
			return false;
		if (pattern == null) {
			if (other.pattern != null)
				return false;
		} else if (!pattern.equals(other.pattern))
			return false;
		if (property == null) {
			if (other.property != null)
				return false;
		} else if (!property.equals(other.property))
			return false;
		return true;
	}

}
