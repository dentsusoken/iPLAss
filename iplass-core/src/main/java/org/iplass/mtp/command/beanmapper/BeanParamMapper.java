/*
 * Copyright (C) 2016 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.command.beanmapper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.iplass.mtp.SystemException;
import org.iplass.mtp.beanvalidation.BeanValidation;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.command.beanmapper.Mapper;
import org.iplass.mtp.impl.command.beanmapper.el.ELMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * パラメータをBean、Entityにセットするためのユーティリティです。
 * </p>
 * <p>
 * Commandのインスタンスとして当BeanParamMapperのインスタンスを保持し、
 * {@link Command#execute(RequestContext)}にて、{@link #populate(Object, Map)}呼び出すことにより、 パラメータの値を格納可能です。<br>
 * パラメータ名（formのinputタグのnameなど）にて、beaｎのどのプロパティに値をセットするかを指定します。デフォルトの設定では、以下のような命名規則が適用されます。<br>
 * <ul>
 * <li>パラメータ名と名前が一致するプロパティに値をセットします</li>
 * <li>"."により、ネストされたプロパティを表現します。</li>
 * <li>"[x]"(xは数値)により、List/配列のインデックス指定可能です。</li>
 * <li>"['key']"(keyは任意の文字列)により、Mapのキー指定可能です。</li>
 * <li>Mapのキー表現"propA['key']"は、propA.keyと表現も可能です。</li>
 * </ul>
 * パラメータの値は可能な限りbeanの各プロパティの型に自動変換を試みます。
 * 変換出来なかった場合は、最終的にMappingExceptionがスローされます。MappingExceptionには、変換できなかった対象のプロパティと値が保持されます。
 * </p>
 * <p>
 * 以下に、HTML上のFormのname定義と、その際呼び出されるメソッドのイメージの例を示します。
 * </p>
 * 
 * <table>
 * <tr>
 * <th>HTML上のFormでの定義</th><th>呼び出されるメソッドのイメージ</th>
 * </tr>
 * <tr>
 * <td>&lt;input type="text" name="age" value="25"&gt;</td><td>formBean.setAge(25)</td>
 * </tr>
 * <tr>
 * <td>&lt;input type="text" name="accout.mail" value="test@test.dentsusoken.com"&gt;</td><td>formBean.getAccount().setMail("test@test.dentsusoken.com")</td>
 * </tr>
 * <tr>
 * <td>&lt;input type="text" name="details[0].id" value="123"&gt;</td><td>formBean.getDetails()[0].setId("123")</td>
 * </tr>
 * <tr>
 * <td>&lt;input type="text" name="map['key1'].id" value="123"&gt;</td><td>formBean.getMap().get('key1').setId("123")</td>
 * </tr>
 * <tr>
 * <td>&lt;input type="text" name="map.key1.id" value="123"&gt;</td><td>formBean.getMap().get('key1').setId("123")</td>
 * </tr>
 * </table>
 * 
 * <p>
 * また、name表現時のデリミタを{@link #delimiters(char, char, char)}にて設定することが可能です。それぞれのデリミタ文字はそれぞれ異なる必要があります。<br>
 * ある特定のパラメータのみ自動マッピングしたい場合は、paramPrefixを指定可能です。指定した場合、paramPrefixに前方一致するパラメータのみマッピングされます。<br>
 * whitelistPropertyNameRegexを指定することにより、明示的にセット可能なプロパティを正規表現にて制限することが可能です。
 * </p>
 * <h3>注意</h3>
 * <p>
 * 当ユーティリティクラスを利用しパラメータを自動的にBeanに格納する際、パラメータ名、値は改竄の恐れがあることを十分注意してください。
 * 入力値のチェックはもちろんのこと、特にパラメータ名も改竄されうることを認識し、その対策を行ってください。
 * 各Formに対して、それぞれ個別のFormBeanを作成しない場合（Entityなどにダイレクトにマッピングする場合、複数のFormで共通のBeanを利用する場合など）は、
 * 対策としてwhitelistPropertyNameRegexを指定し、設定可能なプロパティの範囲を制限してください。
 * </p>
 * <p>
 * autoGrowをtrueに設定した場合、ネストされたBeanがnullの場合、配列、List、Mapのサイズが足りない、またインスタンスがnullの場合、自動的に拡張させることが可能です。
 * インスタンスはプロパティとして定義される型のインスタンスが生成されます。List、Mapの場合はparameterized typeのインスタンスが生成されます。
 * 配列、List、Mapのサイズの最大サイズは、indexedPropertySizeLimitで定義されます。この値以上のindex値を指定されたとしても自動拡張は行われません。
 * デフォルト値は128です。
 * </p>
 * <p>
 * また、withValidationをtrueに設定すると、マッピング処理の際にBeanValidtionに基づくバリデーションを実施することが可能です。
 * バリデーションにてエラーがある場合、最終的にMappingExceptionがスローされます。MappingExceptionには、バリデーションエラーとなったプロパティと値、エラーメッセージが保持されます。
 * </p>
 * 
 * <p>
 * 以下にCommandでの利用例を示します。
 * <pre>
 *   public class SampleCommand implements Command {
 *     //BeanParamMapperのインスタンスをコンストラクト時に初期化
 *     private BeanParamMapper mapper = new BeanParamMapper().paramPrefix("_").whitelistPropertyNameRegex("^(age|name|details\..*)$");
 * 
 *     {@literal @}Override
 *     public String execute(RequestContext request) {
 *       FormBean bean = new FormBean();
 *       
 *       //beanに値を格納。_age, _name, _details.id などparamPrefix、whitelistPropertyNameRegexにマッチするパラメータがプロパティにセットされる。
 *       mapper.populate(bean, request.getParamMap());
 * 
 *       //業務ロジックなど
 *       :
 *       :
 *       
 *       return "OK";
 *     }
 * }
 * 
 * </pre>
 * 
 * {@link #populate(Object, Map, Class...)}メソッドはスレッドセーフですが、それ以外の {@link #delimiters(char, char, char)}等の設定用メソッドがスレッドセーフではありません。
 * BeanParamMapperの初期化はCommandのコンストラクタ内で行い、executeメソッド内では設定変更は行わないでください。
 * </p>
 * 
 * @author K.Higuchi
 *
 */
public class BeanParamMapper {
	
	/**
	 * デフォルトのpropertyDelimiterです。
	 */
	public static final char DEFAULT_PROPERTY_DELIMITER = '.';
	/**
	 * デフォルトのindexPrefixです。
	 */
	public static final char DEFAULT_INDEX_PREFIX = '[';
	/**
	 * デフォルトのindexPostfixです。
	 */
	public static final char DEFAULT_INDEX_POSTFIX = ']';
	/**
	 * デフォルトのindexedPropertySizeLimitです。
	 */
	public static final int DEFAULT_INDEXED_PROPERTY_SIZE_LIMIT = 128;

	private static Logger log = LoggerFactory.getLogger(BeanParamMapper.class);
	
	private char propertyDelimiter = DEFAULT_PROPERTY_DELIMITER;
	private char indexPrefix = DEFAULT_INDEX_PREFIX;
	private char indexPostfix = DEFAULT_INDEX_POSTFIX;
	
	private int indexedPropertySizeLimit = DEFAULT_INDEXED_PROPERTY_SIZE_LIMIT;
	
	private String paramPrefix;
	private String whitelistPropertyNameRegex;
	private Pattern whitelistPropertyNameRegexPattern;
	
	private boolean trim;
	private boolean emptyToNull;
	
	private boolean withValidation;
	private boolean autoGrow;
	
	private Validator beanValidator;
	private Consumer<MappingError> typeConversionErrorHandler;
	
	private boolean validDelim = true;
	
	public BeanParamMapper() {
		beanValidator = BeanValidation.getValidator();
	}
	
	/**
	 * パラメータをBeanにセットする際に、BeanValidationによるバリデーションを行うように設定します。
	 * 
	 * @return BeanParamMapper自身のインスタンス
	 */
	public BeanParamMapper withValidation() {
		setWithValidation(true);
		return this;
	}
	
	/**
	 * ネストされたBean、List, 配列、Mapにおいて、パラメータ名で指定されるパスに値が存在しない場合、
	 * 自動的に拡張するように設定します。
	 * 
	 * @return BeanParamMapper自身のインスタンス
	 */
	public BeanParamMapper enableAutoGrow() {
		setAutoGrow(true);
		return this;
	}
	
	/**
	 * パラメータの値がStringの場合、trimを行うように設定します。
	 * 
	 * @return BeanParamMapper自身のインスタンス
	 */
	public BeanParamMapper enableTrim() {
		setTrim(true);
		return this;
	}
	
	/**
	 * パラメータの値が空文字の場合、nullに変換するように設定します。
	 * 
	 * @return BeanParamMapper自身のインスタンス
	 */
	public BeanParamMapper enableEmptyToNull() {
		setEmptyToNull(true);
		return this;
	}

	private void checkDelim() {
		if (propertyDelimiter == indexPrefix) {
			validDelim = false;
			return;
		}
		if (indexPrefix == indexPostfix) {
			validDelim = false;
			return;
		}
		if (indexPostfix == propertyDelimiter) {
			validDelim = false;
			return;
		}
		
		validDelim = true;
	}
	
	/**
	 * パラメータ名のデリミタ表現を設定します。
	 * 各デリミタに指定する文字はそれぞれ異なる必要があります。
	 * 
	 * @param propertyDelimiter ネストされたプロパティを指定する際の文字
	 * @param indexPrefix インデックス値を囲む先頭文字
	 * @param indexPostfix インデックス値を囲む接尾文字
	 * @return　BeanParamMapper自身のインスタンス
	 */
	public BeanParamMapper delimiters(char propertyDelimiter, char indexPrefix, char indexPostfix) {
		this.propertyDelimiter = propertyDelimiter;
		this.indexPrefix = indexPrefix;
		this.indexPostfix = indexPostfix;
		checkDelim();
		return this;
	}
	
	/**
	 * BeanMapperでbeanにマップするパラメータを限定するためのPrefixを設定します。
	 * 当該のparamPrefixと前方一致するパラメータのみbeanにマップされます。
	 *
	 * @param paramPrefix
	 * @return　BeanParamMapper自身のインスタンス
	 */
	public BeanParamMapper paramPrefix(String paramPrefix) {
		this.paramPrefix = paramPrefix;
		return this;
	}
	
	/**
	 * 設定可能なプロパティ名表現の正規表現を設定します。
	 * 正規表現によるチェックは、paramPrefixを取り除いた後に実施されるため、
	 * 正規表現には、paramPrefixは省いた形で指定します。
	 * 
	 * @param whitelistPropertyNameRegex
	 * @return　BeanParamMapper自身のインスタンス
	 */
	public BeanParamMapper whitelistPropertyNameRegex(String whitelistPropertyNameRegex) {
		setWhitelistPropertyNameRegex(whitelistPropertyNameRegex);
		return this;
	}
	
	/**
	 * autoGrowが有効化されている場合、List、配列、Mapにおいて自動拡張する最大サイズを設定します。
	 * 
	 * @param indexedPropertySizeLimit
	 * @return　BeanParamMapper自身のインスタンス
	 */
	public BeanParamMapper indexedPropertySizeLimit(int indexedPropertySizeLimit) {
		setIndexedPropertySizeLimit(indexedPropertySizeLimit);
		return this;
	}
	
	/**
	 * 型変換に失敗した場合のエラーメッセージをカスタマイズするなどの処理を行う場合、セットします。
	 * 
	 * 例）
	 * <pre>
	 * BeanParamMapper mapper = new BeanParamMapper().typeConversionError(e -> e.addMessage("データの変換に失敗しました"))
	 * </pre>
	 * 
	 * @param typeConversionErrorHandler 引数のMappingErrorに対して、メッセージ等をセットするロジックを記述する
	 * @return　BeanParamMapper自身のインスタンス
	 */
	public BeanParamMapper typeConversionError(Consumer<MappingError> typeConversionErrorHandler) {
		setTypeConversionErrorHandler(typeConversionErrorHandler);
		return this;
	}
	
	public boolean isTrim() {
		return trim;
	}

	public void setTrim(boolean trim) {
		this.trim = trim;
	}

	public boolean isEmptyToNull() {
		return emptyToNull;
	}

	public void setEmptyToNull(boolean emptyToNull) {
		this.emptyToNull = emptyToNull;
	}

	public boolean isAutoGrow() {
		return autoGrow;
	}

	public void setAutoGrow(boolean autoGrow) {
		this.autoGrow = autoGrow;
	}

	public boolean getWithValidation() {
		return withValidation;
	}

	public void setWithValidation(boolean withValidation) {
		this.withValidation = withValidation;
	}

	public char getPropertyDelimiter() {
		return propertyDelimiter;
	}
	public void setPropertyDelimiter(char propertyDelimiter) {
		this.propertyDelimiter = propertyDelimiter;
		checkDelim();
	}
	public char getIndexPrefix() {
		return indexPrefix;
	}
	public void setIndexPrefix(char indexPrefix) {
		this.indexPrefix = indexPrefix;
		checkDelim();
	}
	public char getIndexPostfix() {
		return indexPostfix;
	}
	public void setIndexPostfix(char indexPostfix) {
		this.indexPostfix = indexPostfix;
		checkDelim();
	}
	public int getIndexedPropertySizeLimit() {
		return indexedPropertySizeLimit;
	}
	public void setIndexedPropertySizeLimit(int indexedPropertySizeLimit) {
		this.indexedPropertySizeLimit = indexedPropertySizeLimit;
	}
	public String getParamPrefix() {
		return paramPrefix;
	}
	public void setParamPrefix(String paramPrefix) {
		this.paramPrefix = paramPrefix;
	}
	public String getWhitelistPropertyNameRegex() {
		return whitelistPropertyNameRegex;
	}
	public void setWhitelistPropertyNameRegex(String whitelistPropertyNameRegex) {
		this.whitelistPropertyNameRegex = whitelistPropertyNameRegex;
		if (whitelistPropertyNameRegex != null) {
			whitelistPropertyNameRegexPattern = Pattern.compile(whitelistPropertyNameRegex);
		} else {
			whitelistPropertyNameRegexPattern = null;
		}
	}
	public Consumer<MappingError> getTypeConversionErrorHandler() {
		return typeConversionErrorHandler;
	}

	public void setTypeConversionErrorHandler(Consumer<MappingError> typeConversionErrorHandler) {
		this.typeConversionErrorHandler = typeConversionErrorHandler;
	}

	
	private String normPath(String path) {
		if (propertyDelimiter != DEFAULT_PROPERTY_DELIMITER) {
			path = path.replace(propertyDelimiter, DEFAULT_PROPERTY_DELIMITER);
		}
		if (indexPrefix != DEFAULT_INDEX_PREFIX) {
			path = path.replace(indexPrefix, DEFAULT_INDEX_PREFIX);
		}
		if (indexPostfix != DEFAULT_INDEX_POSTFIX) {
			path = path.replace(indexPostfix, DEFAULT_INDEX_POSTFIX);
		}
		return path;
	}
	
	/**
	 * 指定のbeanに、paramsで指定されるパラメータをセットします。
	 * 
	 * @param bean
	 * @param params
	 * @param validationGroups validationを行う場合（withValidation=true）、BeanValidaitonのvalidationGroupsを指定可能
	 * @throws MappingException マッピング処理時に型変換エラー、Validationエラーが発生した場合スロー
	 */
	public void populate(Object bean, Map<String, Object> params, Class<?>... validationGroups) throws MappingException {
		if (!validDelim) {
			throw new SystemException("Delimiter setting invalid. Each delimiter must different char.");
		}
		
		if ((bean == null) || (params == null)) {
			return;
		}
		if (log.isDebugEnabled()) {
			log.debug("BeanParamMapper.populate(" + bean + ", " + params + ")");
		}
		
		MappingResult res = new MappingResult(bean);
		Map<String, Object> valueMap = new HashMap<>();
		for (Map.Entry<String, ? extends Object> entry : params.entrySet()) {
			String name = entry.getKey();
			if (name == null) {
				continue;
			}
			if (paramPrefix != null) {
				if (!name.startsWith(paramPrefix)) {
					if (log.isTraceEnabled()) {
						log.trace("no match paramPrefix :" + name);
					}
					continue;
				}
				name = name.substring(paramPrefix.length());
			}
			
			if (whitelistPropertyNameRegexPattern != null) {
				if (!whitelistPropertyNameRegexPattern.matcher(name).matches()) {
					if (log.isTraceEnabled()) {
						log.trace("no match whitelistPropertyNameRegex :" + name);
					}
					continue;
				}
			}
			
			name = normPath(name);
			valueMap.put(name, entry.getValue());
		}
		
		Mapper beanMapper = new ELMapper();
		beanMapper.setTrim(trim);
		beanMapper.setEmptyToNull(emptyToNull);
		beanMapper.setAutoGrow(autoGrow);
		beanMapper.setIndexedPropertySizeLimit(indexedPropertySizeLimit);
		beanMapper.setTypeConversionErrorHandler(typeConversionErrorHandler);
		beanMapper.setTargetBean(bean);
		
		beanMapper.map(valueMap, res);
		
		if (withValidation) {
			Set<String> typeConversionErrorProps = new HashSet<>();
			if (res.hasError()) {
				for (MappingError me: res.getErrors()) {
					typeConversionErrorProps.add(me.getPropertyPath());
				}
			}
			
			Set<ConstraintViolation<Object>> violationSet = beanValidator.validate(bean, validationGroups);
			if (violationSet != null) {
				for (ConstraintViolation<Object> cv: violationSet) {
					String path = cv.getPropertyPath().toString();
					if (!typeConversionErrorProps.contains(path)) {
						MappingError preError = res.getError(path);
						if (preError == null) {
							res.addError(new MappingError(path, cv.getMessage(), cv.getInvalidValue(), cv));
						} else {
							preError.addMessage(cv.getMessage(), cv);
						}
					}
				}
			}
		}
		
		if (res.hasError()) {
			if (log.isDebugEnabled()) {
				for (MappingError err: res.getErrors()) {
					log.debug("mapping fail: property=" + err.getPropertyPath() + ", message=" + err.getErrorMessages() + ", value=" + err.getErrorValue() + ", cause=" + err.getCause(), err.getCause());
				}
			}
			throw new MappingException("mapping failed", res);
		}
	}
	
	/**
	 * 明示的に指定のbeanに対してValidationを実行します。
	 * 
	 * @param bean
	 * @param validationGroups BeanValidaitonのvalidationGroupsを指定可能
	 * @throws MappingException Validationエラーが発生した場合スロー
	 */
	public void validate(Object bean, Class<?>... validationGroups) throws MappingException {
		MappingResult res = new MappingResult(bean);
		Set<ConstraintViolation<Object>> violationSet = beanValidator.validate(bean, validationGroups);
		if (violationSet != null) {
			for (ConstraintViolation<Object> cv: violationSet) {
				String path = cv.getPropertyPath().toString();
				MappingError preError = res.getError(path);
				if (preError == null) {
					res.addError(new MappingError(path, cv.getMessage(), cv.getInvalidValue(), cv));
				} else {
					preError.addMessage(cv.getMessage(), cv);
				}
			}
		}
		
		if (res.hasError()) {
			throw new MappingException("validation failed", res);
		}
	}

}
