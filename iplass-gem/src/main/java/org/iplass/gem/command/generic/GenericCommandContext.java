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

package org.iplass.gem.command.generic;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

import org.iplass.gem.command.Constants;
import org.iplass.gem.command.GemResourceBundleUtil;
import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.entity.ValidateError;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.VersionControlType;
import org.iplass.mtp.view.generic.EntityView;
import org.iplass.mtp.view.generic.FormView;
import org.iplass.mtp.web.template.TemplateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 汎用管理画面用コマンド関連の情報を管理するクラスのスーパークラス
 * @author lis3wg
 */
public abstract class GenericCommandContext {
	private static Logger logger = LoggerFactory.getLogger(GenericCommandContext.class);

	/** リクエスト */
	protected RequestContext request;

	/** Entity定義名 */
	protected String defName;

	/** Entity定義 */
	protected EntityDefinition entityDefinition;

	/** 画面定義 */
	protected EntityView entityView;

	/**
	 * コンストラクタ
	 * @param request リクエスト
	 */
	public GenericCommandContext(RequestContext request) {
		this.request = request;
		this.defName = getParam(Constants.DEF_NAME);
	}

	/**
	 * コンストラクタ
	 * @param request リクエスト
	 * @param defName Entity定義名
	 */
	public GenericCommandContext(RequestContext request, String defName) {
		this.request = request;
		this.defName = defName;
	}

	/**
	 * リクエストを取得します。
	 * @return リクエスト
	 */
	protected RequestContext getRequest() {
		return request;
	}

	/**
	 * Entity定義を取得します。
	 * @return Entity定義
	 */
	public EntityDefinition getEntityDefinition() {
		return entityDefinition;
	}

	/**
	 * Entity定義を設定します。
	 * @param entityDefinition Entity定義
	 */
	public void setEntityDefinition(EntityDefinition entityDefinition) {
		this.entityDefinition = entityDefinition;
	}

	/**
	 * 画面定義を取得します。
	 * @return 画面定義
	 */
	public EntityView getEntityView() {
		return entityView;
	}

	/**
	 * 画面定義を設定します。
	 * @param entityView 画面定義
	 */
	public void setEntityView(EntityView entityView) {
		this.entityView = entityView;
	}

	/**
	 * 変換時に発生したエラー情報を取得します。
	 * @return 変換時に発生したエラー情報
	 */
	public abstract List<ValidateError> getErrors();

	/**
	 * 変換時に発生したエラー情報を設定します。
	 * @param errors 変換時に発生したエラー情報
	 */
	public abstract void addError(ValidateError error);

	/**
	 * 変換時に発生したエラー情報があるかをチェックします。
	 * @return 変換時に発生したエラー情報があるか
	 */
	public abstract boolean hasErrors();

	/**
	 * Entityがバージョン管理されているかをチェックします。
	 * @return Entityがバージョン管理されているか
	 */
	public boolean isVersioned() {
		return entityDefinition.getVersionControlType() != VersionControlType.NONE;
	}

	/**
	 * Entity定義名を取得します。
	 * @return Entity定義名
	 */
	public String getDefinitionName() {
		return defName;
	}

	/**
	 * propNameで指定したプロパティ定義を取得します。
	 * @param propName プロパティ名
	 * @return プロパティ定義
	 */
	public PropertyDefinition getProperty(String propName) {
		return entityDefinition.getProperty(propName);
	}

	/**
	 * プロパティ定義の一覧を取得します。
	 * @return プロパティ定義の一覧
	 */
	public List<PropertyDefinition> getPropertyList() {
		return entityDefinition.getPropertyList();
	}

	/**
	 * リクエストから指定のパラメータを取得します。
	 * @param name パラメータ名
	 * @return パラメータ
	 */
	public String getParam(String name) {
		return request.getParam(name);
	}

	/**
	 * リクエストから指定のパラメータをLong型で取得します。
	 * @param name パラメータ名
	 * @return パラメータ
	 */
	public Long getLongValue(String name) {
		try {
			return request.getParamAsLong(name);
		} catch (ApplicationException e) {
			if (logger.isDebugEnabled()) {
				logger.debug(e.getMessage(), e);
			}

			ValidateError error = new ValidateError();
			error.setPropertyName(name);
			error.addErrorMessage(resourceString("command.generic.GenericCommandContext.incorrect"));
			addError(error);
		}
		return null;
	}

	/**
	 * リクエストから指定のパラメータをDouble型で取得します。
	 * @param name パラメータ名
	 * @return パラメータ
	 */
	public Double getDoubleValue(String name) {
		try {
			return request.getParamAsDouble(name);
		} catch (ApplicationException e) {
			if (logger.isDebugEnabled()) {
				logger.debug(e.getMessage(), e);
			}

			ValidateError error = new ValidateError();
			error.setPropertyName(name);
			error.addErrorMessage(resourceString("command.generic.GenericCommandContext.incorrect"));
			addError(error);
		}
		return null;
	}

	/**
	 * リクエストから指定のパラメータをBigDecimal型で取得します。
	 * @param name パラメータ名
	 * @return パラメータ
	 */
	public BigDecimal getDecimalValue(String name) {
		try {
			return request.getParamAsBigDecimal(name);
		} catch (ApplicationException e) {
			if (logger.isDebugEnabled()) {
				logger.debug(e.getMessage(), e);
			}

			ValidateError error = new ValidateError();
			error.setPropertyName(name);
			error.addErrorMessage(resourceString("command.generic.GenericCommandContext.incorrect"));
			addError(error);
		}
		return null;
	}

	/**
	 * リクエストから指定のパラメータをDate型で取得します。
	 * @param name パラメータ名
	 * @return パラメータ
	 */
	public Date getDateValue(String name) {
		try {
			return request.getParamAsDate(name, TemplateUtil.getLocaleFormat().getServerDateFormat());
		} catch (ApplicationException e) {
			if (logger.isDebugEnabled()) {
				logger.debug(e.getMessage(), e);
			}

			ValidateError error = new ValidateError();
			error.setPropertyName(name);
			error.addErrorMessage(resourceString("command.generic.GenericCommandContext.incorrect"));
			addError(error);
		}
		return null;
	}

	/**
	 * リクエストから指定のパラメータをTime型で取得します。
	 * @param name パラメータ名
	 * @return パラメータ
	 */
	public Time getTimeValue(String name) {
		try {
			return request.getParamAsTime(name, "HHmmssSSS");
		} catch (ApplicationException e) {
			if (logger.isDebugEnabled()) {
				logger.debug(e.getMessage(), e);
			}

			ValidateError error = new ValidateError();
			error.setPropertyName(name);
			error.addErrorMessage(resourceString("command.generic.GenericCommandContext.incorrect"));
			addError(error);
		}
		return null;
	}

	/**
	 * リクエストから指定のパラメータをTimestamp型で取得します。
	 * @param name パラメータ名
	 * @return パラメータ
	 */
	public Timestamp getTimestampValue(String name) {
		try {
			return request.getParamAsTimestamp(name, TemplateUtil.getLocaleFormat().getServerDateTimeFormat());
		} catch (ApplicationException e) {
			if (logger.isDebugEnabled()) {
				logger.debug(e.getMessage(), e);
			}

			ValidateError error = new ValidateError();
			error.setPropertyName(name);
			error.addErrorMessage(resourceString("command.generic.GenericCommandContext.incorrect"));
			addError(error);
		}
		return null;
	}

	/**
	 * リクエストから指定のパラメータの配列を取得します。
	 * @param name パラメータ名
	 * @return パラメータの配列
	 */
	public String[] getParams(String name) {
		try {
			return request.getParams(name);
		} catch (ApplicationException e) {
			if (logger.isDebugEnabled()) {
				logger.debug(e.getMessage(), e);
			}

			ValidateError error = new ValidateError();
			error.setPropertyName(name);
			error.addErrorMessage(resourceString("command.generic.GenericCommandContext.incorrect"));
			addError(error);
		}
		return null;
	}

	/**
	 * リクエストパラメータからLong型の値を配列で取得します。
	 * @param name リクエストパラメータ名
	 * @return Long型の値の配列
	 */
	public Long[] getLongValues(String name) {
		try {
			return request.getParamsAsLong(name);
		} catch (ApplicationException e) {
			if (logger.isDebugEnabled()) {
				logger.debug(e.getMessage(), e);
			}

			ValidateError error = new ValidateError();
			error.setPropertyName(name);
			error.addErrorMessage(resourceString("command.generic.GenericCommandContext.incorrect"));
			addError(error);
		}
		return null;
	}

	/**
	 * リクエストパラメータからDouble型の値を配列で取得します。
	 * @param name リクエストパラメータ名
	 * @return Double型の値の配列
	 */
	public Double[] getDoubleValues(String name) {
		try {
			return request.getParamsAsDouble(name);
		} catch (ApplicationException e) {
			if (logger.isDebugEnabled()) {
				logger.debug(e.getMessage(), e);
			}

			ValidateError error = new ValidateError();
			error.setPropertyName(name);
			error.addErrorMessage(resourceString("command.generic.GenericCommandContext.incorrect"));
			addError(error);
		}
		return null;
	}

	/**
	 * リクエストパラメータからBigDecimal型の値を配列で取得します。
	 * @param name リクエストパラメータ名
	 * @return BigDecimal型の値の配列
	 */
	public BigDecimal[] getDecimalValues(String name) {
		try {
			return request.getParamsAsBigDecimal(name);
		} catch (ApplicationException e) {
			if (logger.isDebugEnabled()) {
				logger.debug(e.getMessage(), e);
			}

			ValidateError error = new ValidateError();
			error.setPropertyName(name);
			error.addErrorMessage(resourceString("command.generic.GenericCommandContext.incorrect"));
			addError(error);
		}
		return null;
	}

	/**
	 * リクエストパラメータからDate型の値を配列で取得します。
	 * @param name リクエストパラメータ名
	 * @return Long型の値の配列
	 */
	public Date[] getDateValues(String name) {
		try {
			return request.getParamsAsDate(name, TemplateUtil.getLocaleFormat().getServerDateFormat());
		} catch (ApplicationException e) {
			if (logger.isDebugEnabled()) {
				logger.debug(e.getMessage(), e);
			}

			ValidateError error = new ValidateError();
			error.setPropertyName(name);
			error.addErrorMessage(resourceString("command.generic.GenericCommandContext.incorrect"));
			addError(error);
		}
		return null;
	}

	/**
	 * リクエストパラメータからTime型の値を配列で取得します。
	 * @param name リクエストパラメータ名
	 * @return Time型の値の配列
	 */
	public Time[] getTimeValues(String name) {
		try {
			return request.getParamsAsTime(name, "HHmmssSSS");
		} catch (ApplicationException e) {
			if (logger.isDebugEnabled()) {
				logger.debug(e.getMessage(), e);
			}

			ValidateError error = new ValidateError();
			error.setPropertyName(name);
			error.addErrorMessage(resourceString("command.generic.GenericCommandContext.incorrect"));
			addError(error);
		}
		return null;
	}

	/**
	 * リクエストパラメータからTimestamp型の値を配列で取得します。
	 * @param name リクエストパラメータ名
	 * @return Timestamp型の値の配列
	 */
	public Timestamp[] getTimestampValues(String name) {
		try {
			return request.getParamsAsTimestamp(name, TemplateUtil.getLocaleFormat().getServerDateTimeFormat());
		} catch (ApplicationException e) {
			if (logger.isDebugEnabled()) {
				logger.debug(e.getMessage(), e);
			}

			ValidateError error = new ValidateError();
			error.setPropertyName(name);
			error.addErrorMessage(resourceString("command.generic.GenericCommandContext.incorrect"));
			addError(error);
		}
		return null;
	}

	/**
	 * リクエストから画面名を取得します。
	 * @return 画面名
	 */
	public String getViewName() {
		return getParam(Constants.VIEW_NAME);
	}

	private static String resourceString(String key, Object... arguments) {
		return GemResourceBundleUtil.resourceString(key, arguments);
	}

	/**
	 * Formレイアウト情報を取得します。
	 * @return Formレイアウト情報を取得します
	 */
	public abstract <T extends FormView> T getView();

	public void setAttribute(String name, Object value) {
		request.setAttribute(name, value);
	}

	public Object getAttribute(String name) {
		return request.getAttribute(name);
	}
}
