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

package org.iplass.mtp.entity.query;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.query.hint.Hint;
import org.iplass.mtp.entity.query.hint.HintComment;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.primary.EntityField;


/**
 * SELECT句を表す。
 * 
 * @author K.Higuchi
 *
 */
public class Select implements ASTNode {
	private static final long serialVersionUID = -5756828427274970672L;

	private HintComment hintComment;
	private boolean isDistinct;
	private List<ValueExpression> selectValues = new ArrayList<ValueExpression>();

	public Select() {
	}

	public Select(boolean isDistinct, List<ValueExpression> selectValues) {
		this.isDistinct = isDistinct;
		this.selectValues = selectValues;
	}

	public Select(boolean isDistinct, ValueExpression[] selectValueArray) {
		this.isDistinct = isDistinct;
		if (selectValues != null) {
			if (selectValues == null) {
				selectValues = new ArrayList<ValueExpression>();
			}
			for (ValueExpression ve: selectValueArray) {
				selectValues.add(ve);
			}
		}
	}

	public Select(HintComment hintComment, boolean isDistinct, List<ValueExpression> selectValues) {
		this.hintComment = hintComment;
		this.isDistinct = isDistinct;
		this.selectValues = selectValues;
	}

	public Select(HintComment hintComment, boolean isDistinct, ValueExpression[] selectValueArray) {
		this.hintComment = hintComment;
		this.isDistinct = isDistinct;
		if (selectValues != null) {
			if (selectValues == null) {
				selectValues = new ArrayList<ValueExpression>();
			}
			for (ValueExpression ve: selectValueArray) {
				selectValues.add(ve);
			}
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((hintComment == null) ? 0 : hintComment.hashCode());
		result = prime * result + (isDistinct ? 1231 : 1237);
		result = prime * result
				+ ((selectValues == null) ? 0 : selectValues.hashCode());
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
		Select other = (Select) obj;
		if (hintComment == null) {
			if (other.hintComment != null)
				return false;
		} else if (!hintComment.equals(other.hintComment))
			return false;
		if (isDistinct != other.isDistinct)
			return false;
		if (selectValues == null) {
			if (other.selectValues != null)
				return false;
		} else if (!selectValues.equals(other.selectValues))
			return false;
		return true;
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append("select ");
		if (hintComment != null) {
			sb.append(hintComment).append(" ");
		}
		if (isDistinct) {
			sb.append("distinct ");
		}
		if (selectValues != null) {
			for (int i = 0; i < selectValues.size(); i++) {
				if (i != 0) {
					sb.append(",");
				}
				sb.append(selectValues.get(i));
			}
		}

		return sb.toString();
	}

	public HintComment getHintComment() {
		return hintComment;
	}

	public void setHintComment(HintComment hintComment) {
		this.hintComment = hintComment;
	}
	
	public boolean isDistinct() {
		return isDistinct;
	}

	public void setDistinct(boolean isDistinct) {
		this.isDistinct = isDistinct;
	}

	public List<ValueExpression> getSelectValues() {
		return selectValues;
	}

	public void setSelectValues(List<ValueExpression> selectValues) {
		this.selectValues = selectValues;
	}

	public void accept(QueryVisitor visitor) {
		if (visitor.visit(this)) {
			if (hintComment != null) {
				hintComment.accept(visitor);
			}
			if (selectValues != null) {
				for (ValueExpression exp: selectValues) {
					exp.accept(visitor);
				}
			}
		}
	}
	
	public Select values(Object... value) {
		if (value != null) {
			ArrayList<ValueExpression> newSelectValues = new ArrayList<ValueExpression>();
			addToList(newSelectValues, value);
			selectValues = newSelectValues;
		} else {
			selectValues = null;
		}
		return this;
	}

	public Select distinct() {
		isDistinct = true;
		return this;
	}
	
	public HintComment hintComment() {
		if (hintComment == null) {
			hintComment = new HintComment();
		}
		return hintComment;
	}

	public Select addHint(Hint hint) {
		if (hintComment == null) {
			hintComment = new HintComment();
		}
		hintComment.add(hint);
		return this;
	}

	public Select addHint(List<Hint> hintList) {
		if (hintComment == null) {
			hintComment = new HintComment();
		}
		hintComment.add(hintList);
		return this;
	}
	
	public Select add(Object... value) {
		if (value != null) {
			ArrayList<ValueExpression> newSelectValues = new ArrayList<ValueExpression>();
			if (selectValues != null) {
				newSelectValues.addAll(selectValues);
			}
			addToList(newSelectValues, value);
			selectValues = newSelectValues;
		}
		return this;
	}

	public Select add(Object value) {
		if (value == null) {
			throw new NullPointerException("value is null");
		}
		if (selectValues == null) {
			selectValues = new ArrayList<ValueExpression>();
		}
		if (value instanceof ValueExpression) {
			selectValues.add((ValueExpression) value);
		} else if (value instanceof String) {
			selectValues.add(new EntityField((String) value));
		} else {
			throw new EntityRuntimeException("ValueExpression or String type required.");
		}

		return this;
	}

	private void addToList(List<ValueExpression> list, Object[] values) {

		for (Object v: values) {
			if (v instanceof ValueExpression) {
				list.add((ValueExpression) v);
			} else if (v instanceof String) {
				list.add(new EntityField((String) v));
			} else {
				throw new EntityRuntimeException("ValueExpression or String type required.");
			}
		}

	}
	public ASTNode accept(ASTTransformer transformer) {
		return transformer.visit(this);
	}


}
