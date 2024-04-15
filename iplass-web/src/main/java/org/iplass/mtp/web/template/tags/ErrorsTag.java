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

import java.io.IOException;
import java.util.List;

import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.PageContext;
import jakarta.servlet.jsp.tagext.BodyTagSupport;
import jakarta.servlet.jsp.tagext.TryCatchFinally;

import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.command.beanmapper.MappingError;
import org.iplass.mtp.command.beanmapper.MappingException;
import org.iplass.mtp.command.beanmapper.MappingResult;
import org.iplass.mtp.impl.web.WebResourceBundleUtil;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.web.WebRequestConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * エラーが存在する場合、エラー内容をフォーマットしてhtml出力するJSPタグです。
 * </p>
 * <p>
 * ErrorsTagが記述される場所、設定される属性値により、出力される内容が異なります。
 *
 * <ul>
 * <li>
 * bindタグ配下、かつprop指定がある場合<br>
 * 当該プロパティに紐付くエラーがある場合、エラーを出力します。<br>
 * 利用例<br>
 * <pre>
 * &lt;%{@literal @}page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%&gt;
 * &lt;%{@literal @}taglib prefix="m" uri="http://iplass.org/tags/mtp"%&gt;
 *
 * :
 *
 * &lt;m:bind bean="${formBean}"&gt;
 *   &lt;m:bind prop="userName"&gt;
 *   user name : &lt;input type="text" value="${value}" name="${name}"&gt; &lt;m:errors /&gt;
 *   &lt;/m:bind&gt;
 * &lt;/m:bind&gt;
 * </pre>
 * </li>
 * <li>
 * bindタグ配下、かつprop指定がない場合<br>
 * 当該Beanに紐付くエラーがある場合、そのすべてのエラーを出力します。<br>
 * 利用例<br>
 * <pre>
 * &lt;%{@literal @}page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%&gt;
 * &lt;%{@literal @}taglib prefix="m" uri="http://iplass.org/tags/mtp"%&gt;
 *
 * :
 *
 * &lt;m:bind bean="${formBean}"&gt;
 *   &lt;m:errors /&gt;
 *
 *   :
 *
 * &lt;/m:bind&gt;
 * </pre>
 * </li>
 * <li>
 * bindタグ配下ではない場合<br>
 * requestから{@link WebRequestConstants#EXCEPTION}をキーに例外を取得しメッセージ出力します。
 * 当該Exceptionが{@link MappingException}の場合、その例外に保持される{@link MappingResult}のメッセージを出力します。<br>
 * 当該Exceptionが{@link ApplicationException}の場合、その例外のメッセージを出力します。<br>
 * 当該Exceptionがそれ以外の場合、固定のシステム例外メッセージを出力します。
 * </li>
 * <li>
 * タグ属性にて明示的にerrorsを指定した場合<br>
 * 指定されたインスタンスにより適切にメッセージ出力します。
 * 出力内容については属性の説明：errorsを参照してください。
 * </li>
 * </ul>
 *
 * </p>
 * <h5>指定可能な属性の説明</h5>
 * <p>
 * <table border="1">
 * <tr>
 * <th>属性名</th><th>Script可</th><th>デフォルト値</th><th>説明</th>
 * </tr>
 * <tr>
 * <td>errors</td><td>○</td><td>&nbsp;</td><td>
 * 出力するエラー対象を指定します。指定されたエラー対象により適切にエラーメッセージ出力します。<br>
 * <ul>
 * <li>
 * Stringの場合<br>
 * 指定されたStringを出力します。
 * </li>
 * <li>
 * {@link MappingError}の場合<br>
 * 指定されたMappingErrorのerrorMessagesを出力します。
 * </li>
 * <li>
 * {@link MappingResult}の場合<br>
 * 指定されたMappingResultが保持するMappingErrorのerrorMessagesを出力します。
 * </li>
 * <li>
 * {@link MappingException}の場合<br>
 * 指定されたMappingExceptionのMappingResultの内容を出力します。
 * </li>
 * <li>
 * {@link ApplicationException}の場合<br>
 * 指定されたApplicationExceptionのmessageを出力します。
 * </li>
 * <li>
 * Throwableの場合<br>
 * 固定のシステム例外メッセージを出力します。
 * </li>
 * <li>
 * List&lt;String&gt;、配列の場合<br>
 * 指定されたリスト、配列の内容を1件ずつ出力します。
 * </li>
 * <li>
 * それ以外の場合<br>
 * 指定されたインスタンスのtoString()を出力します。
 * </li>
 * </ul>
 * errorsが指定されない場合、 errorsTagの記述場所から自動的にerrorsのインスタンスを解決しようとします。
 * プロパティがバインドされている場合、プロパティに紐付くエラーを探します。
 * Beanがバインドされている場合、Beanに紐付くすべてのエラーを探します。
 * Bean、プロパティともバインドされていない場合、requestから{@link WebRequestConstants#EXCEPTION}をキーに例外を探します。
 * </td>
 * </tr>
 * <tr>
 * <td>delimiter</td><td>&nbsp;</td><td>&lt;br&gt;</td><td>エラーメッセージが複数ある場合のデリミタを指定可能です。</td>
 * </tr>
 * <tr>
 * <td>header</td><td>&nbsp;</td><td>&lt;span class=\"error\"&gt;</td><td>エラーメッセージを出力する際、先頭に出力する内容を指定可能です。</td>
 * </tr>
 * <tr>
 * <td>footer</td><td>&nbsp;</td><td>&lt;/span&gt;</td><td>エラーメッセージを出力する際、最後に出力する内容を指定可能です。</td>
 * </tr>
 * <tr>
 * <td>htmlEscape</td><td>&nbsp;</td><td>true</td><td>エラーメッセージを出力する際にhtmlエスケープ処理をするか否かを指定可能です。</td>
 * </tr>
 * <tr>
 * <td>errorsVariableName</td><td>&nbsp;</td><td>errors</td><td>
 * エラーをpageContextに公開する際の変数名を指定可能です。また、この変数名はバインドされているエラーを探す場合にも利用されます。
 * </td>
 * </tr>
 * </table>
 * </p>
 * <h5>メッセージ出力内容のカスタム</h5>
 * <p>
 * タグ内のBODYにJSPコードを記述することにより、エラーメッセージ出力内容をカスタマイズすることが可能です。<br>
 * カスタマイズ例<br>
 * <pre>
 * &lt;%{@literal @}page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%&gt;
 * &lt;%{@literal @}taglib prefix="m" uri="http://iplass.org/tags/mtp"%&gt;
 *
 * :
 *
 * &lt;m:errors&gt;
 * &lt;span&gt;
 * &lt;b&gt;エラーが発生しました&lt;/b&gt;&lt;br&gt;
 * エラー内容：${errors}
 * &lt;/span&gt;
 * &lt;/m:errors&gt;
 *
 * </pre>
 *
 * カスタマイズ出力する場合は、delimiter、header、footer、htmlEscapeの設定は利用されません。
 *
 * </p>
 *
 * @author K.Higuchi
 *
 */
public class ErrorsTag extends BodyTagSupport implements TryCatchFinally {
	private static final long serialVersionUID = 1L;

	public static final String DEFAULT_ERROR_VARIABLE_NAME = "errors";
	public static final String DEFAULT_DELIMITER = "<br>";
	public static final String DEFAULT_HEADER = "<span class=\"error\">";
	public static final String DEFAULT_FOOTER = "</span>";

	private static Logger log = LoggerFactory.getLogger(ErrorsTag.class);

	private String errorsVariableName;
	private String delimiter;
	private String header;
	private String footer;

	private Object errors;
	private Boolean htmlEscape;

	private Object prevErrors;
	private BindTag parentTag;

	private boolean writeFirst;

	public String getErrorsVariableName() {
		return errorsVariableName;
	}

	public void setErrorsVariableName(String errorsVariableName) {
		this.errorsVariableName = errorsVariableName;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getFooter() {
		return footer;
	}

	public void setFooter(String footer) {
		this.footer = footer;
	}

	public Object getErrors() {
		return errors;
	}

	public void setErrors(Object errors) {
		this.errors = errors;
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

	private void initParam() {
		parentTag = (BindTag) findAncestorWithClass(this, BindTag.class);

		if (errorsVariableName == null) {
			if (parentTag != null) {
				errorsVariableName = parentTag.getErrorsVariableName();
			} else {
				errorsVariableName = DEFAULT_ERROR_VARIABLE_NAME;
			}
		}
		if (htmlEscape == null) {
			if (parentTag != null) {
				htmlEscape = parentTag.isHtmlEscape();
			}
			if (htmlEscape == null) {
				htmlEscape = Boolean.TRUE;
			}
		}

		if (delimiter == null) {
			delimiter = DEFAULT_DELIMITER;
		}
		if (header == null) {
			header = DEFAULT_HEADER;
		}
		if (footer == null) {
			footer = DEFAULT_FOOTER;
		}
	}

	@Override
	public int doStartTag() throws JspException {
		initParam();
		if (errors == null) {
			errors = pageContext.getAttribute(errorsVariableName);
			if (errors == null) {
				if (parentTag != null && parentTag.isSetBean()) {
					MappingResult me = parentTag.getMappingResult();
					if (me != null && me.hasError()) {
						errors = me.getErrors();
					}
				}
			}

			if (parentTag == null && errors == null) {
				errors = pageContext.getAttribute(WebRequestConstants.EXCEPTION, PageContext.REQUEST_SCOPE);
			}
		}

		if (errors == null) {
			return SKIP_BODY;
		} else {
			prevErrors = pageContext.getAttribute(errorsVariableName);
			pageContext.setAttribute(errorsVariableName, errors);
			return EVAL_BODY_BUFFERED;
		}

	}

	@Override
	public int doEndTag() throws JspException {
		try {
			if (bodyContent == null) {
				if (errors != null) {
					JspWriter out = pageContext.getOut();
					out.print(header);
					writeError(errors, out);
					out.print(footer);
				}
			} else {
				bodyContent.writeOut(getPreviousOut());
			}

			pageContext.setAttribute(errorsVariableName, prevErrors);

		} catch (IOException e) {
			throw new JspException(e);
		}
		return EVAL_PAGE;
	}

	@SuppressWarnings("rawtypes")
	private void writeError(Object errors, JspWriter out) throws IOException {
		if (errors instanceof String) {
			writeMessage((String) errors, out);
		} else if (errors instanceof List) {
			for (Object e: (List) errors) {
				writeError(e, out);
			}
		} else if (errors instanceof Object[]) {
			Object[] array = (Object[]) errors;
			for (int i = 0; i < array.length; i++) {
				writeError(array[i], out);
			}
		} else if (errors instanceof MappingError) {
			for (String m: ((MappingError) errors).getErrorMessages()) {
				writeError(m, out);
			}
		} else if (errors instanceof MappingResult) {
			for (MappingError me: ((MappingResult) errors).getErrors()) {
				writeError(me, out);
			}
		} else if (errors instanceof MappingException) {
			writeError(((MappingException) errors).getResult(), out);
		} else if (errors instanceof ApplicationException) {
			writeMessage(((ApplicationException) errors).getMessage(), out);
		} else if (errors instanceof Throwable) {
			if (log.isDebugEnabled()) {
				log.debug("System Exception: " + errors + " occoured. Message details are not output.");
			}
			if (writeFirst) {
				out.print(delimiter);
			} else {
				writeFirst = true;
			}
			writeMessage(resourceString("error.Error.retryMsg"), out);
		} else if (errors != null) {
			writeMessage(errors.toString(), out);
		}
	}

	private void writeMessage(String msg, JspWriter out) throws IOException {
		if (writeFirst) {
			out.print(delimiter);
		} else {
			writeFirst = true;
		}
		if (htmlEscape) {
			out.print(StringUtil.escapeHtml(msg));
		} else {
			out.print(msg);
		}
	}

	@Override
	public void doCatch(Throwable t) throws Throwable {
		throw t;
	}

	@Override
	public void doFinally() {
		bodyContent = null;

		errorsVariableName = null;
		delimiter = null;
		header = null;
		footer = null;
		errors = null;
		htmlEscape = null;
		parentTag = null;
		prevErrors = null;
		writeFirst = false;
	}

	private static String resourceString(String key, Object... arguments) {
		return WebResourceBundleUtil.resourceString(key, arguments);
	}
}
