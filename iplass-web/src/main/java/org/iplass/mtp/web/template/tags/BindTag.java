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
package org.iplass.mtp.web.template.tags;

import java.util.ArrayList;

import javax.el.PropertyNotFoundException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;

import org.iplass.mtp.command.beanmapper.BeanParamMapper;
import org.iplass.mtp.command.beanmapper.MappingError;
import org.iplass.mtp.command.beanmapper.MappingException;
import org.iplass.mtp.command.beanmapper.MappingResult;
import org.iplass.mtp.impl.command.beanmapper.el.ELMapper;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.web.WebRequestConstants;
import org.iplass.mtp.web.template.ValueFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Beanに格納されている値、関連するエラーをpageContextにバインドするJSPタグです。
 * </p>
 * 
 * <p>JSPでの利用例を以下に示します。</p>
 * <pre>
 * &lt;%{@literal @}page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%&gt;
 * &lt;%{@literal @}taglib prefix="m" uri="http://iplass.org/tags/mtp"%&gt;
 * 
 * :
 * 
 * &lt;form&gt;
 * &lt;!-- formBeanという名前で参照（RequestやSessionから）されるBeanをバインドします --&gt;
 * &lt;m:bind bean="${formBean}"&gt;
 * 
 *   &lt;!-- formBeanのプロパティuserNameをバインドします。
 *       ${value}、${name}でformに指定すべきvalue, nameが取得できます。
 *       また、&lt;m:errors/&gt;を指定することにより、{@link BeanParamMapper}でのマッピングエラー、バリデーションエラーを表示可能です。 --&gt;
 *   &lt;m:bind prop="userName"&gt;
 *   user name : &lt;input type="text" value="${value}" name="${name}"&gt; &lt;m:errors /&gt;
 *   &lt;/m:bind&gt;
 *   
 *   :
 *   
 *   &lt;m:bind prop="mailAddress"&gt;
 *   mail address : &lt;input type="text" value="${value}" name="${name}"&gt; &lt;m:errors /&gt;
 *   &lt;/m:bind&gt;
 * 
 * :
 * 
 * &lt;/m:bind&gt;
 * &lt;/form&gt;
 * 
 * </pre>
 * 
 * <p>
 * 指定可能な属性の説明
 * <table border="1">
 * <tr>
 * <th>属性名</th><th>Script可</th><th>デフォルト値</th><th>説明</th>
 * </tr>
 * <tr>
 * <td>bean</td><td>○</td><td>&nbsp;</td><td>
 * バインドするBeanのインスタンスを指定します。<br>
 * バインドされたBeanは、beanという変数名でpageContextに公開されます。
 * 公開する際の変数名を変更したい場合は、beanVariableNameにて変数名を変更可能です。
 * </td>
 * </tr>
 * <tr>
 * <td>beanVariableName</td><td>&nbsp;</td><td>bean</td><td>バインドされたBeanをpageContextに公開する際の変数名を指定可能です。</td>
 * </tr>
 * <tr>
 * <td>mappingResult</td><td>○</td><td>&nbsp;</td><td>
 * {@link BeanParamMapper}でのバインド結果である{@link MappingResult}のインスタンスを指定可能です。
 * mappingResultが指定された場合、当該Bean、プロパティに紐付くエラーがバインドされます。<br>
 * 当該属性が未指定の場合、かつautoDetectErrorsがtrueの場合、mappingResultは自動解決されます。<br>
 * バインドされた{@link MappingResult}はmappingResultという変数名でpageContextに公開されます。
 * 公開する際の変数名を変更したい場合は、mappingResultVariableNameにて変数名を変更可能です。
 * </td>
 * </tr>
 * <tr>
 * <td>autoDetectErrors</td><td>&nbsp;</td><td>true</td><td>エラー（mappingResult）を自動解決するか否かを指定可能です。
 * trueが指定された場合、requestから{@link WebRequestConstants#EXCEPTION}をキーに{@link MappingException}を取得し、
 * 存在した場合、その例外から{@link MappingResult}のインスタンスを自動的に解決します。
 * </td>
 * </tr>
 * <tr>
 * <td>mappingResultVariableName</td><td>&nbsp;</td><td>mappingResult</td><td>バインドされた{@link MappingResult}をpageContextに公開する際の変数名を指定可能です。</td>
 * </tr>
 * <tr>
 * <td>prop</td><td>○</td><td>&nbsp;</td><td>バインドされているBeanのプロパティのパスを指定します。
 * EL式の記法によって、ネストされたプロパティを指定可能です。<br>
 * 例）<br>
 * <pre>
 * userName
 * accout.mail
 * details[0].id
 * </pre>
 * 当該パスが指定されたbindタグの内側ではpageContextにプロパティ名、値、当該プロパティに関連するエラーがバインドされます。<br>
 * pageContextに公開される際の変数名は、デフォルトでは以下の名前で公開されます。
 * <ul>
 * <li>name : HTTPのパラメータ名として利用可能な形のプロパティのパス。{@link BeanParamMapper}のパラメータ名と一致</li>
 * <li>value : プロパティの値の文字列表現。文字列表現はhtmlEscape、formatter設定にて制御可能</li>
 * <li>rawValue : 生のプロパティの値</li>
 * <li>errors : 当該プロパティに関するエラーが存在する場合、エラーメッセージのList&lt;String&gt;</li>
 * </ul>
 * 公開する際の変数名を変更したい場合は、propertyNameVariableName、propertyValueVariableName、
 * propertyRawValueVariableName、errorsVariableNameにて変数名を変更可能です。<br>
 * また、propには、scriptでの指定が可能なので、Bean内にネストされたリストをバインドしたい場合、例えば次のような記述が可能です。<br>
 * JSTLのforEachを利用する例
 * <pre>
 * &lt;m:bind bean="${bean}"&gt;
 * 
 * :
 * 
 * &lt;c:forEach var="item" items="${bean.children}" varStatus="stat"&gt;
 *   ${stat.index}.
 *   &lt;m:bind prop="children[${stat.index}].name"&gt;
 *   child name : &lt;input type="text" value="${value}" name="${name}"&gt; &lt;m:errors /&gt;
 *   &lt;/m:bind&gt;
 * &lt;/c:forEach&gt;
 * 
 * &lt;/m:bind&gt;
 * 
 * </pre>
 * prop指定と同時にbeanが指定された場合は、そのbeanのプロパティをバインドします。
 * beanが未指定の場合は、親タグに指定されるbeanのプロパティをバインドします。
 * </td>
 * </tr>
 * <tr>
 * <td>htmlEscape</td><td>&nbsp;</td><td>true</td><td>value（プロパティ値の文字列表現）を出力する際にhtmlエスケープ処理をするか否かを指定可能です。<br>
 * このフラグによってエスケープ処理されるのはvalueのみです。name、errorsの値はエスケープされません。
 * </td>
 * </tr>
 * <tr>
 * <td>formatter</td><td>○</td><td>{@link DefaultValueFormatter}のインスタンス</td><td>
 * value（プロパティ値の文字列表現）を出力する際のフォーマット処理を行う{@link ValueFormatter}のインスタンスを指定します。<br>
 * このformatterをbeanが指定される親のbindタグに指定することにより個別のプロパティがバインドされる値に一律の処理が可能ですが、
 * 次のように個別にrawValueからフォーマットすることも可能です。
 * 以下例では、エラーが発生した場合は、エラー値を出力するようにnvl関数を利用しています。
 * <pre>
 * &lt;m:bind prop="dateProp"&gt;
 * :
 * &lt;input type="text" value="${m:nvl(errorValue, m:fmt(rawValue, 'yyyy/MM/dd'))}" name="${name}"&gt;
 * &lt;/m:bind&gt;
 * </pre>
 * </td>
 * </tr>
 * <tr>
 * <td>propertyNameVariableName</td><td>&nbsp;</td><td>name</td><td>バインドされたプロパティのHTTPパラメータ名をpageContextに公開する際の変数名を指定可能です。</td>
 * </tr>
 * <tr>
 * <td>propertyValueVariableName</td><td>&nbsp;</td><td>value</td><td>バインドされたプロパティの値の文字列表現をpageContextに公開する際の変数名を指定可能です。</td>
 * </tr>
 * <tr>
 * <td>propertyRawValueVariableName</td><td>&nbsp;</td><td>rawValue</td><td>バインドされたプロパティの生の値をpageContextに公開する際の変数名を指定可能です。</td>
 * </tr>
 * <tr>
 * <td>propertyErrorValueVariableName</td><td>&nbsp;</td><td>errorValue</td><td>バインドされたプロパティがエラーの場合、そのエラー値が格納される変数名を指定可能です。</td>
 * </tr>
 * <tr>
 * <td>errorsVariableName</td><td>&nbsp;</td><td>errors</td><td>バインドされたプロパティに関連するエラーをpageContextに公開する際の変数名を指定可能です。</td>
 * </tr>
 * <tr>
 * <td>prefix</td><td>&nbsp;</td><td>&nbsp;</td><td>name（HTTPパラメータ名）を出力する際のprefixを指定します。
 * HTTPリクエストを{@link BeanParamMapper}でマッピングする場合、{@link BeanParamMapper}のparamPrefixの値と一致させる必要があります。</td>
 * </tr>
 * <tr>
 * <td>propertyDelimiter</td><td>&nbsp;</td><td>.</td><td>name（HTTPパラメータ名）を出力する際のネストされたプロパティのデリミタを指定します。
 * HTTPリクエストを{@link BeanParamMapper}でマッピングする場合、{@link BeanParamMapper}のpropertyDelimiterの値と一致させる必要があります。</td>
 * </tr>
 * <tr>
 * <td>indexPrefix</td><td>&nbsp;</td><td>[</td><td>name（HTTPパラメータ名）を出力する際のインデックス指定のプレフィックス文字を指定します。
 * HTTPリクエストを{@link BeanParamMapper}でマッピングする場合、{@link BeanParamMapper}のindexPrefixの値と一致させる必要があります。</td>
 * </tr>
 * <tr>
 * <td>indexPostfix</td><td>&nbsp;</td><td>]</td><td>name（HTTPパラメータ名）を出力する際のインデックス指定のポストフィックス文字を指定します。
 * HTTPリクエストを{@link BeanParamMapper}でマッピングする場合、{@link BeanParamMapper}のindexPostfixの値と一致させる必要があります。</td>
 * </tr>
 * </table>
 * </p>
 * 
 * @author K.Higuchi
 *
 */
public class BindTag extends TagSupport implements TryCatchFinally {
	private static final long serialVersionUID = 1L;

	private static Logger log = LoggerFactory.getLogger(BindTag.class);
	
	public static final String DEFAULT_PROPERTY_DELIMITER = ".";
	public static final String DEFAULT_INDEX_PREFIX = "[";
	public static final String DEFAULT_INDEX_POSTFIX = "]";
	public static final String DEFAULT_BEAN_VARIABLE_NAME = "bean";
	public static final String DEFAULT_MAPPING_RESULT_VARIABLE_NAME = "mappingResult";
	public static final String DEFAULT_PROPERTY_NAME_VARIABLE_NAME = "name";
	public static final String DEFAULT_PROPERTY_VALUE_VARIABLE_NAME = "value";
	public static final String DEFAULT_PROPERTY_RAW_VALUE_VARIABLE_NAME = "rawValue";
	public static final String DEFAULT_PROPERTY_ERROR_VALUE_VARIABLE_NAME = "errorValue";
	public static final String DEFAULT_ERROR_VARIABLE_NAME = "errors";
	
	private String propertyDelimiter;
	private String indexPrefix;
	private String indexPostfix;
	private String prefix;
	
	private String beanVariableName;
	private String mappingResultVariableName;
	private Boolean autoDetectErrors = Boolean.TRUE;
	private Object bean;
	private MappingResult mappingResult;
	
	private String propertyNameVariableName;
	private String propertyValueVariableName;
	private String propertyRawValueVariableName;
	private String propertyErrorValueVariableName;
	private String errorsVariableName;
	private String prop;
	private Boolean htmlEscape;
	private ValueFormatter formatter;
	
	private boolean setBean;
	private boolean setProp;
	private ELMapper elMapper;
	
	public String getPropertyErrorValueVariableName() {
		return propertyErrorValueVariableName;
	}
	public void setPropertyErrorValueVariableName(String propertyErrorValueVariableName) {
		this.propertyErrorValueVariableName = propertyErrorValueVariableName;
	}
	public ValueFormatter getFormatter() {
		return formatter;
	}
	public void setFormatter(ValueFormatter formatter) {
		this.formatter = formatter;
	}
	public boolean isHtmlEscape() {
		if (htmlEscape == null) {
			return false;
		}
		return htmlEscape;
	}
	public void setHtmlEscape(boolean htmlEscape) {
		this.htmlEscape = htmlEscape;
	}
	public String getPropertyDelimiter() {
		return propertyDelimiter;
	}
	public void setPropertyDelimiter(String propertyDelimiter) {
		this.propertyDelimiter = propertyDelimiter;
	}
	public String getIndexPrefix() {
		return indexPrefix;
	}
	public void setIndexPrefix(String indexPrefix) {
		this.indexPrefix = indexPrefix;
	}
	public String getIndexPostfix() {
		return indexPostfix;
	}
	public void setIndexPostfix(String indexPostfix) {
		this.indexPostfix = indexPostfix;
	}
	public String getBeanVariableName() {
		return beanVariableName;
	}
	public void setBeanVariableName(String beanVariableName) {
		this.beanVariableName = beanVariableName;
	}
	public String getMappingResultVariableName() {
		return mappingResultVariableName;
	}
	public void setMappingResultVariableName(String mappingResultVariableName) {
		this.mappingResultVariableName = mappingResultVariableName;
	}
	public String getPropertyNameVariableName() {
		return propertyNameVariableName;
	}
	public void setPropertyNameVariableName(String propertyNameVariableName) {
		this.propertyNameVariableName = propertyNameVariableName;
	}
	public String getPropertyValueVariableName() {
		return propertyValueVariableName;
	}
	public void setPropertyValueVariableName(String propertyValueVariableName) {
		this.propertyValueVariableName = propertyValueVariableName;
	}
	public String getPropertyRawValueVariableName() {
		return propertyRawValueVariableName;
	}
	public void setPropertyRawValueVariableName(String propertyRawValueVariableName) {
		this.propertyRawValueVariableName = propertyRawValueVariableName;
	}
	public String getErrorsVariableName() {
		return errorsVariableName;
	}
	public void setErrorsVariableName(String errorsVariableName) {
		this.errorsVariableName = errorsVariableName;
	}
	public Object getBean() {
		return bean;
	}
	public void setBean(Object bean) {
		this.bean = bean;
		setBean = true;
		elMapper = new ELMapper();
		elMapper.setTargetBean(bean);
	}
	public Boolean getAutoDetectErrors() {
		return autoDetectErrors;
	}
	public void setAutoDetectErrors(Boolean autoDetectErrors) {
		this.autoDetectErrors = autoDetectErrors;
	}
	public MappingResult getMappingResult() {
		return mappingResult;
	}
	public void setMappingResult(MappingResult mappingResult) {
		this.mappingResult = mappingResult;
	}
	public String getProp() {
		return prop;
	}
	public void setProp(String prop) {
		this.prop = prop;
		setProp = true;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	private void exposeBean() {
		pageContext.setAttribute(beanVariableName, bean);
		
		if (autoDetectErrors != null && autoDetectErrors.booleanValue()) {
			if (mappingResult == null) {
				Exception e = (Exception) pageContext.getAttribute(WebRequestConstants.EXCEPTION, PageContext.REQUEST_SCOPE);
				if (e != null && e instanceof MappingException) {
					MappingResult mr = ((MappingException) e).getResult();
					if (bean == mr.getBean()) {
						mappingResult = mr;
					}
				}
			}
			if (mappingResult == null) {
				mappingResult = new MappingResult(bean);
			}
		}
		pageContext.setAttribute(mappingResultVariableName, mappingResult);
	}
	
	private void exposeProp() {
		if (bean != null) {
			
			String name = prop;
			if (propertyDelimiter != null) {
				name = name.replace(DEFAULT_PROPERTY_DELIMITER, propertyDelimiter);
			}
			if (indexPrefix != null) {
				name = name.replace(DEFAULT_INDEX_PREFIX, indexPrefix);
			}
			if (indexPostfix != null) {
				name = name.replace(DEFAULT_INDEX_POSTFIX, indexPostfix);
			}
			if (prefix != null) {
				name = prefix + name;
			}
			
			pageContext.setAttribute(propertyNameVariableName, name);
			
			Object value = null;
			boolean valResolve = false;
			if (mappingResult.hasError()) {
				MappingError e = mappingResult.getError(prop);
				if (e != null) {
					value = e.getErrorValue();
					valResolve = true;
				}
				
				ArrayList<String> errMsgs = new ArrayList<>();
				Object errorValue = null;
				for (MappingError me: mappingResult.getErrors()) {
					if (me.getPropertyPath().startsWith(prop)) {
						if (me.getPropertyPath().length() == prop.length()) {
							errMsgs.addAll(me.getErrorMessages());
							errorValue = me.getErrorValue();
						} else {
							char c = me.getPropertyPath().charAt(prop.length());
							if (c == '.' || c == '[') {
								errMsgs.addAll(me.getErrorMessages());
							}
						}
					}
				}
				if (errMsgs.size() > 0) {
					pageContext.setAttribute(errorsVariableName, errMsgs);
					if (errorValue != null && htmlEscape) {
						pageContext.setAttribute(propertyErrorValueVariableName, StringUtil.escapeHtml(formatter.apply(errorValue)));
					} else {
						pageContext.setAttribute(propertyErrorValueVariableName, formatter.apply(errorValue));
					}
				}
			}
			
			if (!valResolve) {
				try {
					value = elMapper.getValue(prop);
				} catch (PropertyNotFoundException e) {
					if (log.isDebugEnabled()) {
						log.debug(prop + " not reachable in target bean: " + bean + ", cause:" + e.toString());
					}
				}
			}
			
			if (value != null && htmlEscape) {
				pageContext.setAttribute(propertyValueVariableName, StringUtil.escapeHtml(formatter.apply(value)));
			} else {
				pageContext.setAttribute(propertyValueVariableName, formatter.apply(value));
			}
			pageContext.setAttribute(propertyRawValueVariableName, value);
		}
	}
	
	boolean isSetBean() {
		return setBean;
	}
	
	BindTag getBeanBindTag() {
		Tag tag = this;
		for (;;) {
			tag = tag.getParent();
			if (tag == null) {
				return null;
			}
			
			if (tag instanceof BindTag && ((BindTag) tag).setBean) {
				return (BindTag) tag;
			}
		}
	}
	
	private void resolveBean(BindTag beanTag) {
		if (beanTag != null) {
			bean = beanTag.bean;
			elMapper = beanTag.elMapper;
			mappingResult = beanTag.mappingResult;
		}
	}
	
	private void initParam(BindTag beanTag) {
		if (propertyDelimiter == null) {
			if (beanTag != null) {
				propertyDelimiter = beanTag.propertyDelimiter;
			}
		}
		if (indexPrefix == null) {
			if (beanTag != null) {
				indexPrefix = beanTag.indexPrefix;
			}
		}
		if (indexPostfix == null) {
			if (beanTag != null) {
				indexPostfix = beanTag.indexPostfix;
			}
		}
		if (prefix == null) {
			if (beanTag != null) {
				prefix = beanTag.prefix;
			}
		}
		if (htmlEscape == null) {
			if (beanTag != null) {
				htmlEscape = beanTag.htmlEscape;
			}
			if (htmlEscape == null) {
				htmlEscape = Boolean.TRUE;
			}
		}
		if (formatter == null) {
			if (beanTag != null) {
				formatter = beanTag.formatter;
			}
			if (formatter == null) {
				formatter = ValueFormatter.DEFAULT_FORMATTER;
			}
		}
		
		if (beanVariableName == null) {
			if (beanTag != null) {
				beanVariableName = beanTag.beanVariableName;
			}
			if (beanVariableName == null) {
				beanVariableName = DEFAULT_BEAN_VARIABLE_NAME;
			}
		}
		if (mappingResultVariableName == null) {
			if (beanTag != null) {
				mappingResultVariableName = beanTag.mappingResultVariableName;
			}
			if (mappingResultVariableName == null) {
				mappingResultVariableName = DEFAULT_MAPPING_RESULT_VARIABLE_NAME;
			}
		}
		if (propertyNameVariableName == null) {
			if (beanTag != null) {
				propertyNameVariableName = beanTag.propertyNameVariableName;
			}
			if (propertyNameVariableName == null) {
				propertyNameVariableName = DEFAULT_PROPERTY_NAME_VARIABLE_NAME;
			}
		}
		if (propertyValueVariableName == null) {
			if (beanTag != null) {
				propertyValueVariableName = beanTag.propertyValueVariableName;
			}
			if (propertyValueVariableName == null) {
				propertyValueVariableName = DEFAULT_PROPERTY_VALUE_VARIABLE_NAME;
			}
		}
		if (propertyRawValueVariableName == null) {
			if (beanTag != null) {
				propertyRawValueVariableName = beanTag.propertyRawValueVariableName;
			}
			if (propertyRawValueVariableName == null) {
				propertyRawValueVariableName = DEFAULT_PROPERTY_RAW_VALUE_VARIABLE_NAME;
			}
		}
		if (propertyErrorValueVariableName == null) {
			if (beanTag != null) {
				propertyErrorValueVariableName = beanTag.propertyErrorValueVariableName;
			}
			if (propertyErrorValueVariableName == null) {
				propertyErrorValueVariableName = DEFAULT_PROPERTY_ERROR_VALUE_VARIABLE_NAME;
			}
		}
		if (errorsVariableName == null) {
			if (beanTag != null) {
				errorsVariableName = beanTag.errorsVariableName;
			}
			if (errorsVariableName == null) {
				errorsVariableName = DEFAULT_ERROR_VARIABLE_NAME;
			}
		}
	}
	
	@Override
	public int doStartTag() throws JspException {
		if (setBean) {
			initParam(null);
			exposeBean();
		}
		if (setProp) {
			if (!setBean) {
				BindTag beanTag = getBeanBindTag();
				initParam(beanTag);
				resolveBean(beanTag);
			}
			exposeProp();
		}
		
		return EVAL_BODY_INCLUDE;
	}
	@Override
	public int doEndTag() throws JspException {
		if (setProp) {
			pageContext.removeAttribute(propertyNameVariableName, PageContext.PAGE_SCOPE);
			pageContext.removeAttribute(propertyValueVariableName, PageContext.PAGE_SCOPE);
			pageContext.removeAttribute(propertyRawValueVariableName, PageContext.PAGE_SCOPE);
			pageContext.removeAttribute(propertyErrorValueVariableName, PageContext.PAGE_SCOPE);
			pageContext.removeAttribute(errorsVariableName, PageContext.PAGE_SCOPE);
		}
		
		if (setBean) {
			pageContext.removeAttribute(beanVariableName, PageContext.PAGE_SCOPE);
			pageContext.removeAttribute(mappingResultVariableName, PageContext.PAGE_SCOPE);
			
			BindTag parentBeanTag = getBeanBindTag();
			if (parentBeanTag != null) {
				parentBeanTag.exposeBean();
			}
		}
		
		return EVAL_PAGE;
	}
	
	@Override
	public void doCatch(Throwable t) throws Throwable {
		throw t;
	}
	
	@Override
	public void doFinally() {
		propertyDelimiter = null;
		indexPrefix = null;
		indexPostfix = null;
		prefix = null;
		
		beanVariableName = null;
		mappingResultVariableName = null;
		autoDetectErrors = Boolean.TRUE;
		bean = null;
		mappingResult = null;
		
		propertyNameVariableName = null;
		propertyValueVariableName = null;
		propertyRawValueVariableName = null;
		propertyErrorValueVariableName = null;
		errorsVariableName = null;
		prop = null;
		htmlEscape = null;
		formatter = null;
		
		setBean = false;
		setProp = false;
		elMapper = null;
	}

}
