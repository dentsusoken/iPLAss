/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.query.prepared;

import java.io.IOException;
import java.io.StringWriter;

import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.query.prepared.PreparedQueryBinding;
import org.iplass.mtp.impl.script.GroovyScriptEngine;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.impl.script.template.GroovyTemplate;
import org.iplass.mtp.impl.script.template.GroovyTemplateCompiler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * PreparedQueryの実装本体。
 * 
 * @author K.Higuchi
 *
 */
public class PreparedQueryTemplate {
	
	private static final Logger logger = LoggerFactory.getLogger(PreparedQueryTemplate.class);
	
	private String queryString;

	private GroovyTemplate compiledQuery;
	
	public PreparedQueryTemplate(String queryString) {
		this.queryString = queryString;
		
		ScriptEngine se = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();
		compiledQuery = GroovyTemplateCompiler.compile(
				queryString, "Query_" + GroovyTemplateCompiler.randomName(), (GroovyScriptEngine) se);
	}
	
	public String getQueryString() {
		return queryString;
	}
	
	public String doBind(PreparedQueryBinding binding) {
		StringWriter w = new StringWriter();
		binding.setVariable("out", w);
		try {
			compiledQuery.doTemplate(binding);
		} catch (IOException e) {
			//発生しえない
			throw new RuntimeException(e);
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("binding to expression: org:\"" + queryString + "\": binded:\"" + w.toString() + "\"");
		}
		
		return w.toString();
	}
	
	/**
	 * queryExpressionをQueryとして取得する。
	 * 
	 * @param binding
	 * @return
	 */
	public Query query(PreparedQueryBinding binding) {
		return Query.newQuery(doBind(binding));
	}
	
	/**
	 * queryExpressionをConditionとして取得する。
	 * 
	 * @param binding
	 * @return
	 */
	public Condition condition(PreparedQueryBinding binding) {
		return Condition.newCondition(doBind(binding));
	}
	
	/**
	 * queryExpressionをValueExpressionとして取得する。
	 * 
	 * @param binding
	 * @return
	 */
	public ValueExpression value(PreparedQueryBinding binding) {
		return ValueExpression.newValue(doBind(binding));
	}

}
