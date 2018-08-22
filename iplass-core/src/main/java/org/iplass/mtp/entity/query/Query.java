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
import java.util.Iterator;
import java.util.List;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.hint.Hint;
import org.iplass.mtp.entity.query.hint.HintComment;
import org.iplass.mtp.entity.query.value.primary.EntityField;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.query.HavingSyntax;
import org.iplass.mtp.impl.query.QueryConstants;
import org.iplass.mtp.impl.query.QueryServiceHolder;
import org.iplass.mtp.impl.query.QuerySyntax;
import org.iplass.mtp.impl.query.WhereSyntax;
import org.iplass.mtp.util.StringUtil;


/**
 * <p>
 * Entityの検索をする際利用するクエリーを表現するクラスです。
 * SQLライクな検索条件を指定可能です。
 * </p>
 *
 * <p>
 * Queryをインスタンス化する方法として、2種類あります。
 * <ul>
 * <li>EQL（Entity Query Language、SQLライクな文法）を文字列として指定し、newQuery()、newPreparedQuery()を通してQueryインスタンスを取得する方法</li>
 * <li>コード上から、Queryのインスタンスをnewして条件を設定する方法</li>
 * </ul>
 * Queryのインスタンスを使って、EntityManager経由でEntityを検索します。
 * </p>
 *
 * <h5>EQL(Entity Query Language)からQueryをインスタンス化</h5>
 * <p>
 * コード例：
 * <pre>
 * Query q = Query.newQuery("select oid, name, orderId, totalamount, detail.amount from OrderEntity where orderDate &gt; '2011-10-01' and orderStatus='5'");
 * 
 * or
 * 
 * Query q = new Query("select oid, name, orderId, totalamount, detail.amount from OrderEntity where orderDate &gt; '2011-10-01' and orderStatus='5'");
 * </pre>
 * ※EQLの文法は、下記BNFを参考にしてください。
 * </p>
 *
 * <h5>コードからQueryをインスタンス化</h5>
 * <p>
 * コード例：
 * <pre>
 * Query q = new Query()
 * 	.select("oid", "name", "orderId", "totalamount", "detail.amount")
 * 	.from("OrderEntity")
 * 	.where(new And().gt("orderDate", "2011-10-01").eq("orderStatus", "5"));
 * </pre>
 * </p>
 * <h5>EQLのBNF</h5>
 * <p>
 * ※BNFを拡張した記述式の説明<br>
 * ・[] = オプションの要素<br>
 * ・{} = グループ化された要素<br>
 * ・* = 0回以上の繰り返し<br>
 * ・"" = 明示的な文字列(BNFの式やシンボルではない)を示す<br>
 * </p>
 * <p>
 * &lt;query&gt; ::=  &lt;select clause&gt; &lt;from clause&gt; [&lt;refer clause&gt;] [&lt;where clause&gt;] [&lt;group by clause&gt;] [&lt;having clause&gt;] [&lt;order by clause&gt;] [&lt;limit clause&gt;]<br>
 * <br>
 * &lt;select clause&gt; ::= SELECT [DISTINCT] &lt;value expression&gt; {,&lt;value expression&gt;}*<br>
 * &lt;from clause&gt; ::= FROM &lt;entity name&gt;<br>
 * &lt;refer clause&gt; ::= REFER &lt;reference&gt; [ON &lt;condition&gt;] [AS OF &lt;value expression&gt;] {,REFER &lt;reference&gt; [ON &lt;condition&gt;] [AS OF &lt;value expression&gt;]}*<br>
 * &lt;where clause&gt; ::= WHERE &lt;condition&gt;<br>
 * &lt;group by clause&gt; ::= GROUP BY &lt;value expression&gt; {,&lt;value expression&gt;}* [ROLLUP | CUBE]<br>
 * &lt;having clause&gt; ::= HAVING &lt;condition&gt;<br>
 * &lt;order by clause&gt; ::= ORDER BY &lt;sort spec&gt; {,&lt;sort spec&gt;}*<br>
 * &lt;sort spec&gt; ::= &lt;value expression&gt; [ASC | DESC] [NULLS FIRST | NULLS LAST]<br>
 * &lt;limit clause&gt; ::= LIMIT 件数 [OFFSET 開始行]<br>
 * <br>
 * &lt;condition&gt; ::= &lt;and&gt; | &lt;or&gt; | &lt;not&gt; | &lt;paren&gt;<br>
 * &lt;and&gt; ::= &lt;condition&gt; {AND &lt;condition&gt;}*<br>
 * &lt;or&gt; ::= &lt;condition&gt; {OR &lt;condition&gt;}*<br>
 * &lt;not&gt; ::= NOT &lt;paren&gt;<br>
 * &lt;paren&gt; ::= &lt;predicate&gt; | (&lt;condition&gt;)<br>
 * &lt;predicate&gt; ::= &lt;comparison predicate&gt; | &lt;between&gt; | &lt;contains&gt; | &lt;in&gt; | &lt;is not null&gt; | &lt;is null&gt; | &lt;like&gt;<br>
 * &lt;comparison predicate&gt; ::= &lt;value expression&gt; &lt;comparison operator&gt; &lt;value expression&gt;<br>
 * &lt;comparison operator&gt; ::= = | &gt; | &gt;= | &lt; | &lt;= | !=<br>
 * &lt;between&gt; ::= &lt;value expression&gt; BETWEEN &lt;value expression&gt; AND &lt;value expression&gt;<br>
 * &lt;contains&gt; ::= "CONTAINS('" &lt;full text search expression&gt; "')"<br>
 * &lt;in&gt; ::= &lt;simple in&gt; | &lt;row value list in&gt; | &lt;subquery in&gt;<br>
 * &lt;simple in&gt; ::= &lt;value expression&gt; IN (&lt;value expression&gt; {,&lt;value expression&gt;}*)<br>
 * &lt;row value list in&gt; ::= (&lt;value expression&gt; {,&lt;value expression&gt;}*) IN (&lt;row value list&gt; {,&lt;row value list&gt;}*)<br>
 * &lt;row value list&gt; ::= (&lt;value expression&gt; {,&lt;value expression&gt;}*)<br>
 * &lt;subquery in&gt; ::= {&lt;value expression&gt; IN &lt;subquery&gt;} | {(&lt;value expression&gt; {,&lt;value expression&gt;}*) IN &lt;subquery&gt;}<br>
 * &lt;is not null&gt; ::= &lt;value expression&gt; IS NOT NULL<br>
 * &lt;is null&gt; ::= &lt;value expression&gt; IS NULL<br>
 * &lt;like&gt; ::= &lt;value expression&gt; LIKE "'"&lt;string&gt;"'" [CS | CI]<br>
 * <br>
 * &lt;value expression&gt; ::= &lt;polynomial&gt; | &lt;term&gt; | &lt;minus sign&gt; | &lt;paren value&gt;<br>
 * &lt;polynomial&gt; ::= &lt;value expression&gt; {+ &lt;value expression&gt; | - &lt;value expression&gt;}*<br>
 * &lt;term&gt; ::= &lt;value expression&gt; {"*" &lt;value expression&gt; | / &lt;value expression&gt;}*<br>
 * &lt;minus sign&gt; ::= - &lt;paren value&gt;<br>
 * &lt;paren value&gt; ::= &lt;primary value&gt; | (&lt;value expression&gt;)<br>
 * &lt;primary value&gt; := &lt;aggregate&gt; | &lt;array value&gt; | &lt;case&gt; | &lt;entity field&gt; | &lt;function&gt; | &lt;cast&gt; | &lt;literal&gt; | &lt;scalar subquery&gt; | &lt;window function&gt;<br>
 * &lt;aggregate&gt; ::= {AVG | MAX | MEDIAN | MIN | MODE | SUM | STDDEV_POP | STDDEV_SAMP | VAR_POP | VAR_SAMP}(&lt;value expression&gt;) | COUNT([DISTINCT] [&lt;value expression&gt;])<br>
 * &lt;array value&gt; ::= "ARRAY[" &lt;value expression&gt; {,&lt;value expression&gt;}* "]"<br>
 * &lt;case&gt; ::= CASE WHEN &lt;condition&gt; THEN &lt;value expression&gt; {WHEN &lt;condition&gt; THEN &lt;value expression&gt;}* [ELSE &lt;value expression&gt;] END<br>
 * &lt;entity field&gt; ::= &lt;property name&gt; | &lt;reference&gt;.&lt;property name&gt; | &lt;correlated entity field&gt;<br>
 * &lt;reference&gt; ::= &lt;reference name&gt;{.&lt;reference name&gt;}*<br>
 * &lt;function&gt; ::= &lt;function name&gt;() | &lt;function name&gt;(&lt;value expression&gt;{,&lt;value expression&gt;}*)<br>
 * &lt;function name&gt; ::= REPLACE | UPPER | LOWER | CONCAT | SUBSTR | INSTR | CHAR_LENGTH | MOD | SQRT | POWER | ABS | CEIL | FLOOR | ROUND | TRUNCATE | YEAR | MONTH | DAY | HOUR | MINUTE | SECOND | DATE_ADD | DATE_DIFF | CURRENT_DATE | CURRENT_TIME | CURRENT_DATETIME | LOCALTIME<br>
 * &lt;cast&gt; ::= CAST(&lt;value expression&gt; AS &lt;data type&gt;)<br>
 * &lt;data type&gt; ::= STRING | INTEGER | FLOAT | DECIMAL | BOOLEAN | SELECT | DATE | TIME | DATETIME<br>
 * &lt;scalar subquery&gt; ::= &lt;subquery&gt;<br>
 * &lt;window function&gt; ::= &lt;window function type&gt; OVER([&lt;window partition by clause&gt;] [&lt;window order by clause&gt;])<br>
 * &lt;window function type&gt; ::= {ROW_NUMBER | RANK | DENSE_RANK | PERCENT_RANK | CUME_DIST}() |  &lt;aggregate&gt;<br>
 * &lt;window partition by clause&gt; ::= PARTITION BY &lt;value expression&gt; {,&lt;value expression&gt;}*<br>
 * &lt;window order by clause&gt; ::= ORDER BY &lt;sort spec&gt; {,&lt;sort spec&gt;}*<br>
 * &lt;literal&gt; ::= &lt;boolean literal&gt; | &lt;string literal&gt; | &lt;integer literal&gt; | &lt;floating point literal&gt; | &lt;fixed point literal&gt; | &lt;datetime literal&gt; | &lt;date literal&gt; | &lt;time literal&gt; | &lt;select value literal&gt; | NULL<br>
 * &lt;boolean literal&gt; ::= TRUE | FALSE<br>
 * &lt;string literal&gt; ::= "'" 文字列 "'"<br>
 * &lt;integer literal&gt; ::= [-]数字[数字]*[i|I]<br>
 * &lt;floating point literal&gt; ::= [-]数字[数字]*.数字[数字]*[f|F]<br>
 * &lt;fixed point literal&gt; ::= [-]数字[数字]*.数字[数字]*[g|G]<br>
 * &lt;datetime literal&gt; ::= "'" yyyy-MM-dd HH:mm:ss.SSS "'"{m|M}<br>
 * &lt;date literal&gt; ::= "'" yyyy-MM-dd "'"{d|D}<br>
 * &lt;time literal&gt; ::= "'" HH:mm:ss "'"{t|T}<br>
 * &lt;select value literal&gt; ::= "'" SelectValueのvalue "'"{s|S}<br>
 * &lt;entity name&gt; ::= Entityの定義名<br>
 * &lt;property name&gt; ::= Entity属性の定義名<br>
 * &lt;reference name&gt; ::= Entity属性（参照）の定義名<br>
 * <br>
 * &lt;subquery&gt; ::= (&lt;query&gt; [ON &lt;condition&gt;])<br>
 * &lt;correlated entity field&gt; ::= .[.]*{THIS | &lt;entity field&gt; | &lt;reference&gt;}<br>
 * 
 * </p>
 * <p>
 * ※scalar subqueryは単一値（1行1列）を返すsubquery<br>
 * ※correlated entity fieldはsubqueryのON句でのみ利用可能<br>
 * </p>
 *
 * @author K.Higuchi
 *
 */
public class Query implements ASTNode {
	private static final long serialVersionUID = -1325433515844859791L;

	/**
	 * EQL文字列を指定してQueryインスタンスを生成します。
	 * 
	 * @param query
	 * @return
	 */
	public static Query newQuery(String query) {
		try {
			return QueryServiceHolder.getInstance().getQueryParser().parse(query, QuerySyntax.class);
		} catch (ParseException e) {
			throw new QueryException(e.getMessage(), e);
		}
	}

	/**
	 * queryでEQL文字列を指定してPreparedQueryインスタンスを生成します。
	 * @param query
	 * @return
	 */
	public static PreparedQuery newPreparedQuery(String query) {
		//TODO キャッシュしたら速くなる？
		return new PreparedQuery(query);
	}

	private Select select;
	private From from;
	private List<Refer> refer;
	private Where where;
	private GroupBy groupBy;
	private Having having;
	private OrderBy orderBy;
	private Limit limit;

	private boolean versiond = false;
	private boolean localized = false;

	public Query() {
	}
	public Query(Select select, From from, Where where) {
		this.select = select;
		this.from = from;
		this.where = where;
	}
	
	/**
	 * EQL文字列を指定してQueryインスタンスを生成します。
	 * 
	 * @param query
	 * @throws QueryException
	 */
	public Query(String query) throws QueryException {
		Query q = newQuery(query);
		this.from = q.from;
		this.groupBy = q.groupBy;
		this.having = q.having;
		this.limit = q.limit;
		this.localized = q.localized;
		this.orderBy = q.orderBy;
		this.refer = q.refer;
		this.select = q.select;
		this.versiond = q.versiond;
		this.where = q.where;
	}
	
	@Override
	public Query copy() {
		return (Query) ASTNode.super.copy();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((groupBy == null) ? 0 : groupBy.hashCode());
		result = prime * result + ((having == null) ? 0 : having.hashCode());
		result = prime * result + ((limit == null) ? 0 : limit.hashCode());
		result = prime * result + (localized ? 1231 : 1237);
		result = prime * result + ((orderBy == null) ? 0 : orderBy.hashCode());
		result = prime * result + ((refer == null) ? 0 : refer.hashCode());
		result = prime * result + ((select == null) ? 0 : select.hashCode());
		result = prime * result + (versiond ? 1231 : 1237);
		result = prime * result + ((where == null) ? 0 : where.hashCode());
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
		Query other = (Query) obj;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (groupBy == null) {
			if (other.groupBy != null)
				return false;
		} else if (!groupBy.equals(other.groupBy))
			return false;
		if (having == null) {
			if (other.having != null)
				return false;
		} else if (!having.equals(other.having))
			return false;
		if (limit == null) {
			if (other.limit != null)
				return false;
		} else if (!limit.equals(other.limit))
			return false;
		if (localized != other.localized)
			return false;
		if (orderBy == null) {
			if (other.orderBy != null)
				return false;
		} else if (!orderBy.equals(other.orderBy))
			return false;
		if (refer == null) {
			if (other.refer != null)
				return false;
		} else if (!refer.equals(other.refer))
			return false;
		if (select == null) {
			if (other.select != null)
				return false;
		} else if (!select.equals(other.select))
			return false;
		if (versiond != other.versiond)
			return false;
		if (where == null) {
			if (other.where != null)
				return false;
		} else if (!where.equals(other.where))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (select != null) {
			sb.append(select.toString());
			sb.append(" ");
		}
		if (from != null) {
			sb.append(from.toString());
			sb.append(" ");
		}
		if (refer != null) {
			for (Refer r: refer) {
				sb.append(r.toString());
				sb.append(" ");
			}
		}
		if (where != null) {
			sb.append(where.toString());
		}
		if (groupBy != null) {
			sb.append(" ");
			sb.append(groupBy.toString());
		}
		if (having != null) {
			sb.append(" ");
			sb.append(having.toString());
		}
		if (orderBy != null) {
			sb.append(" ");
			sb.append(orderBy.toString());
		}
		if (limit != null) {
			sb.append(" ");
			sb.append(limit.toString());
		}
		if (versiond) {
			sb.append(" versioned");
		}
		if (localized) {
			sb.append(" localized");
		}

		return sb.toString();
	}

	public Select getSelect() {
		return select;
	}

	public void setSelect(Select select) {
		this.select = select;
	}

	public From getFrom() {
		return from;
	}

	public void setFrom(From from) {
		this.from = from;
	}

	public List<Refer> getRefer() {
		return refer;
	}

	public void setRefer(List<Refer> refer) {
		this.refer = refer;
	}

	public Where getWhere() {
		return where;
	}

	public void setWhere(Where where) {
		this.where = where;
	}

	public GroupBy getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(GroupBy groupBy) {
		this.groupBy = groupBy;
	}

	public Having getHaving() {
		return having;
	}

	public void setHaving(Having having) {
		this.having = having;
	}

	public OrderBy getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(OrderBy orderBy) {
		this.orderBy = orderBy;
	}

	public Limit getLimit() {
		return limit;
	}
	public void setLimit(Limit limit) {
		this.limit = limit;
	}

	public boolean isVersiond() {
		return versiond;
	}

	public void setVersiond(boolean versiond) {
		this.versiond = versiond;
	}

	public boolean isLocalized() {
		return localized;
	}

	public void setLocalized(boolean localized) {
		this.localized = localized;
	}

	public void accept(QueryVisitor visitor) {
		if (visitor.visit(this)) {
			if (select != null) {
				select.accept(visitor);
			}
			if (from != null) {
				from.accept(visitor);
			}
			if (refer != null) {
				for (Refer r: refer) {
					r.accept(visitor);
				}
			}
			if (where != null) {
				where.accept(visitor);
			}
			if (groupBy != null) {
				groupBy.accept(visitor);
			}
			if (having != null) {
				having.accept(visitor);
			}
			if (orderBy != null) {
				orderBy.accept(visitor);
			}
			if (limit != null) {
				limit.accept(visitor);
			}
		}
	}

	public Select select() {
		if (select == null) {
			select = new Select();
		}
		return select;
	}

	/**
	 * selectを構築します。
	 * その際distinctはtrueとします。
	 *
	 * @param value selectする項目(ValueExpression or String)
	 * @return
	 */
	public Query selectDistinct(Object... value) {
		Select clause = select();
		clause.setDistinct(true);
		clause.add(value);
		return this;
	}

	/**
	 * selectを構築します。
	 * その際distinctはtrueとします。
	 *
	 * @param hint ヒントコメント
	 * @param value selectする項目(ValueExpression or String)
	 * @return
	 */
	public Query selectDistinct(HintComment hint, Object... value) {
		Select clause = select();
		clause.setDistinct(true);
		clause.setHintComment(hint);
		clause.add(value);
		return this;
	}
	
	/**
	 * selectを構築します。
	 * その際distinctはfalse（デフォルト値）とします。
	 *
	 * @param value selectする項目（ValueExpression or String）
	 * @return
	 */
	public Query select(Object... value) {
		Select clause = select();
		clause.add(value);
		return this;
	}
	
	/**
	 * ヒント句指定しつつ、selectを構築します。
	 * その際distinctはfalse（デフォルト値）とします。
	 *
	 * @param hint ヒントコメント
	 * @param value selectする項目（ValueExpression or String）
	 * @return
	 */
	public Query select(HintComment hint, Object... value) {
		Select clause = select();
		clause.setHintComment(hint);
		clause.add(value);
		return this;
	}
	

	public Query selectAll(String definitionName, boolean distinct, boolean withReferenceOidAndName) {
		return selectAll(definitionName, distinct, withReferenceOidAndName, false);
	}

	public Query selectAll(String definitionName, boolean distinct, boolean withReferenceOidAndName, boolean withReferenceVersion) {
		return selectAll(definitionName, distinct, withReferenceOidAndName, withReferenceVersion, true);
	}

	public Query selectAll(String definitionName, boolean distinct, boolean withReferenceOidAndName, boolean withReferenceVersion, boolean withMappedByReference) {
		return selectAll(definitionName, null, distinct, withReferenceOidAndName, withReferenceVersion, withMappedByReference);
	}

	public Query selectAll(String definitionName, HintComment hint, boolean distinct, boolean withReferenceOidAndName, boolean withReferenceVersion, boolean withMappedByReference) {
		Select clause = new Select();
		clause.setHintComment(hint);
		clause.setDistinct(distinct);
		EntityDefinition ed = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class).get(definitionName);
		if (ed == null) {
			throw new EntityRuntimeException(definitionName + " not found.");
		}
		for (PropertyDefinition pd: ed.getPropertyList()) {
			if (pd instanceof ReferenceProperty) {
				ReferenceProperty rp = (ReferenceProperty)pd;
				if (StringUtil.isNotEmpty(rp.getMappedBy()) && !withMappedByReference) {
					continue;
				}
				if (withReferenceOidAndName) {
					clause.add(new EntityField(pd.getName() + "." + Entity.OID));
					clause.add(new EntityField(pd.getName() + "." + Entity.NAME));
				}
				if (withReferenceVersion) {
					clause.add(new EntityField(pd.getName() + "." + Entity.VERSION));
				}
			} else {
				clause.add(pd.getName());
			}
		}
		setSelect(clause);
		from(definitionName);
		return this;
	}
	
	/**
	 * ヒントを追加します。
	 * 注意：selectAllを利用した場合、Selectインスタンスが初期化されるので、selectAllの後に呼び出すこと。
	 * 
	 * @param hint
	 * @return
	 */
	public Query hint(Hint hint) {
		select().addHint(hint);
		return this;
	}
	
	public Query hint(List<Hint> hintList) {
		select().addHint(hintList);
		return this;
	}
	
	private From from() {
		if (from == null) {
			from = new From();
		}
		return from;
	}

	public Query from(String entityName) {
		from().setEntityName(entityName);
		return this;
	}
	
	public Query from(String entityName, AsOf asOf) {
		from().setEntityName(entityName);
		from().setAsOf(asOf);
		return this;
	}

	public Query refer(String referenceName, Condition onCondition) {
		return refer(referenceName, null, onCondition);
	}
	
	public Query refer(String referenceName, AsOf asOf) {
		return refer(referenceName, asOf, null);
	}
	
	public Query refer(String referenceName, AsOf asOf, Condition onCondition) {
		if (refer == null) {
			refer = new ArrayList<Refer>();
		}
		if (refer.size() > 0) {
			//check duplicate
			Iterator<Refer> it = refer.iterator();
			while (it.hasNext()) {
				Refer r = it.next();
				if (r.getReferenceName().getPropertyName().equals(referenceName)) {
					it.remove();
					break;
				}
			}
		}
		refer.add(new Refer(new EntityField(referenceName), asOf, onCondition));
		return this;
	}

	public Refer refer(String referenceName) {
		if (refer == null) {
			refer = new ArrayList<Refer>();
		}
		if (refer.size() > 0) {
			for (Refer r: refer) {
				if (r.getReferenceName().getPropertyName().equals(referenceName)) {
					return r;
				}
			}
		}

		Refer newR = new Refer(new EntityField(referenceName), null);
		refer.add(newR);
		return newR;
	}

	public Where where() {
		if (where == null) {
			where = new Where();
		}
		return where;
	}

	public Query where(String whereClause) {
		String whereStr = QueryConstants.WHERE + " " + whereClause;
		try {
			where = QueryServiceHolder.getInstance().getQueryParser().parse(whereStr, WhereSyntax.class);
		} catch (ParseException e) {
			throw new QueryException(e.getMessage(), e);
		}

		return this;
	}

	public Query where(Condition whereCondition) {
		where = new Where(whereCondition);
		return this;
	}

	/**
	 * groupBy項目を指定します。
	 *
	 * @param groupingField groupByする項目（ValueExpression or String）
	 * @return
	 */
	public Query groupBy(Object... groupingField) {
		if (groupBy == null) {
			groupBy = new GroupBy();
		}
		for (Object v: groupingField) {
			groupBy.add(v);
		}
		return this;
	}

	public Query having(Condition havingCondition) {
		having = new Having(havingCondition);
		return this;
	}

	public Query having(String havingClause) {
		String havingStr = QueryConstants.HAVING + " " + havingClause;
		try {
			having = QueryServiceHolder.getInstance().getQueryParser().parse(havingStr, HavingSyntax.class);
		} catch (ParseException e) {
			throw new QueryException(e.getMessage(), e);
		}

		return this;
	}

	public Query order(SortSpec... sortSpec) {
		if (orderBy == null) {
			orderBy = new OrderBy();
		}
		for (SortSpec o: sortSpec) {
			orderBy.add(o);
		}
		return this;
	}

	public Query limit(int limit) {
		this.limit = new Limit(limit);
		return this;
	}

	public Query limit(int limit, int offset) {
		this.limit = new Limit(limit, offset);
		return this;
	}

	public Query versioned(boolean versioned) {
		this.versiond = versioned;
		return this;
	}
	
	/**
	 * versioned=trueに設定します
	 * 
	 * @return
	 */
	public Query versioned() {
		return versioned(true);
	}
	
	public Query localized(boolean localized) {
		this.localized = localized;
		return this;
	}
	
	/**
	 * localized=trueに設定します
	 * 
	 * @return
	 */
	public Query localized() {
		return localized(true);
	}


	public ASTNode accept(ASTTransformer transformer) {
		return transformer.visit(this);
	}

}
