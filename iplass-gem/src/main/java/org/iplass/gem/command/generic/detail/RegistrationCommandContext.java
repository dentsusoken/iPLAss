package org.iplass.gem.command.generic.detail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.iplass.gem.command.GemResourceBundleUtil;
import org.iplass.gem.command.generic.GenericCommandContext;
import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.GenericEntity;
import org.iplass.mtp.entity.LoadOption;
import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.entity.ValidateError;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
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
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.utilityclass.definition.UtilityClassDefinitionManager;
import org.iplass.mtp.view.generic.FormView;
import org.iplass.mtp.view.generic.LoadEntityContext;
import org.iplass.mtp.view.generic.LoadEntityInterrupter;
import org.iplass.mtp.view.generic.RegistrationInterrupter;
import org.iplass.mtp.view.generic.editor.BooleanPropertyEditor;
import org.iplass.mtp.view.generic.editor.DatePropertyEditor;
import org.iplass.mtp.view.generic.editor.DecimalPropertyEditor;
import org.iplass.mtp.view.generic.editor.FloatPropertyEditor;
import org.iplass.mtp.view.generic.editor.IntegerPropertyEditor;
import org.iplass.mtp.view.generic.editor.SelectPropertyEditor;
import org.iplass.mtp.view.generic.editor.StringPropertyEditor;
import org.iplass.mtp.view.generic.editor.TimePropertyEditor;
import org.iplass.mtp.view.generic.editor.TimestampPropertyEditor;
import org.slf4j.Logger;

public abstract class RegistrationCommandContext extends GenericCommandContext{

	protected EntityManager entityManager;
	protected EntityDefinitionManager definitionManager;
	private UtilityClassDefinitionManager ucdm;

	/** 変換時に発生したエラー情報 */
	private List<ValidateError> errors;

	private RegistrationInterrupterHandler interrupterHandler;

	private LoadEntityInterrupterHandler loadEntityInterrupterHandler;

	private List<ReferenceRegistHandler> referenceRegistHandlers = new ArrayList<>();

	protected abstract Logger getLogger();

	public RegistrationCommandContext(RequestContext request, EntityManager entityLoader, EntityDefinitionManager definitionLoader) {
		super(request);
		this.entityManager = entityLoader;
		this.definitionManager = definitionLoader;

		ucdm = ManagerLocator.getInstance().getManager(UtilityClassDefinitionManager.class);
	}

	protected Entity newEntity() {
		Entity res = null;
		if (entityDefinition.getMapping() != null && entityDefinition.getMapping().getMappingModelClass() != null) {
			try {
				res = (Entity) Class.forName(entityDefinition.getMapping().getMappingModelClass()).newInstance();
			} catch (InstantiationException e) {
				throw new EntityRuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new EntityRuntimeException(e);
			} catch (ClassNotFoundException e) {
				throw new EntityRuntimeException(e);
			}
		} else {
			res = new GenericEntity();
		}
		res.setDefinitionName(entityDefinition.getName());
		return res;
	}

	protected Object getPropValue(PropertyDefinition p, String paramPrefix) {
		Object value = null;
		boolean isMultiple = p.getMultiplicity() != 1;
		String name = paramPrefix +  p.getName();
		if (p instanceof BinaryProperty) {
			value = isMultiple ? getBinaryReferenceValues(name) : getBinaryReferenceValue(name);
		} else if (p instanceof BooleanProperty) {
			value = isMultiple ? getBooleanValues(name, p.getMultiplicity()) : getBooleanValue(name);
		} else if (p instanceof DateProperty) {
			value = isMultiple ? getDateValues(name) : getDateValue(name);
		} else if (p instanceof DateTimeProperty) {
			value = isMultiple ? getTimestampValues(name) : getTimestampValue(name);
		} else if (p instanceof DecimalProperty) {
			value = isMultiple ? getDecimalValues(name) : getDecimalValue(name);
		} else if (p instanceof ExpressionProperty) {
			//数式型は更新プロパティに含めない
			value = getExpressionValue((ExpressionProperty) p, name);
		} else if (p instanceof FloatProperty) {
			value = isMultiple ? getDoubleValues(name) : getDoubleValue(name);
		} else if (p instanceof IntegerProperty) {
			value = isMultiple ? getLongValues(name) : getLongValue(name);
		} else if (p instanceof LongTextProperty) {
			value = isMultiple ? getStringValues(name) : getStringValue(name);
		} else if (p instanceof ReferenceProperty) {
			value = createReference(p, paramPrefix);
		} else if (p instanceof SelectProperty) {
			value = isMultiple ? getSelectValues(name) : getSelectValue(name);
		} else if (p instanceof StringProperty) {
			value = isMultiple ? getStringValues(name) : getStringValue(name);
		} else if (p instanceof TimeProperty) {
			value = isMultiple ? getTimeValues(name) : getTimeValue(name);
		} else if (p instanceof AutoNumberProperty) {
			//AutoNumber型は更新プロパティに含めない
			value = getStringValue(name);
		}
		return value;
	}

	private BinaryReference getBinaryReferenceValue(String name) {
		BinaryReference br = null;
		Long lobId = getLongValue(name);
		if (lobId != null) {
			//バリデーションエラー時に消えないようにデータ読み込み
			br = entityManager.loadBinaryReference(lobId);
		}
		return br;
	}

	private BinaryReference[] getBinaryReferenceValues(String name) {
		Long[] params = getLongValues(name);
		List<BinaryReference> list = new ArrayList<BinaryReference>();
		if (params != null) {
			for (Long lobId : params) {
				if (lobId != null) {
					//バリデーションエラー時に消えないようにデータ読み込み
					BinaryReference br = entityManager.loadBinaryReference(lobId);
					br.setLobId(lobId);
					list.add(br);
				}
			}
		}
		return list.toArray(new BinaryReference[list.size()]);
	}

	private Object getExpressionValue(ExpressionProperty ep, String name) {

		//値の型とEditorの種類で取得する型を変える
		String editorType = getParam(name + "_editorType");
		if (editorType != null) {
			if (ep.getResultType() == PropertyDefinitionType.BOOLEAN
					&& BooleanPropertyEditor.class.getSimpleName().equals(editorType)) {
				return getBooleanValue(name);
			} else if (ep.getResultType() == PropertyDefinitionType.DATE
					&& DatePropertyEditor.class.getSimpleName().equals(editorType)) {
				return getDateValue(name);
			} else if (ep.getResultType() == PropertyDefinitionType.DATETIME
					&& TimestampPropertyEditor.class.getSimpleName().equals(editorType)) {
				return getTimestampValue(name);
			} else if (ep.getResultType() == PropertyDefinitionType.DECIMAL
					&& DecimalPropertyEditor.class.getSimpleName().equals(editorType)) {
				return getDecimalValue(name);
			} else if (ep.getResultType() == PropertyDefinitionType.FLOAT
					&& FloatPropertyEditor.class.getSimpleName().equals(editorType)) {
				return getDoubleValue(name);
			} else if (ep.getResultType() == PropertyDefinitionType.INTEGER
					&& IntegerPropertyEditor.class.getSimpleName().equals(editorType)) {
				return getLongValue(name);
			} else if (ep.getResultType() == PropertyDefinitionType.SELECT
					&& SelectPropertyEditor.class.getSimpleName().equals(editorType)) {
				return getSelectValue(name);
			} else if (ep.getResultType() == PropertyDefinitionType.STRING
					&& StringPropertyEditor.class.getSimpleName().equals(editorType)) {
				return getStringValue(name);
			} else if (ep.getResultType() == PropertyDefinitionType.TIME
					&& TimePropertyEditor.class.getSimpleName().equals(editorType)) {
				return getTimeValue(name);
			}
		}

		//上記にはまらないのは文字列扱い
		return getStringValue(name);
	}

	protected Boolean getBooleanValue(String name) {
		String param = getParam(name);
		String type = getParam(name + "Type");
		Boolean ret = null;
		if ("Select".equals(type) || "Label".equals(type)) {
			//未選択時は空文字→nullで登録
			ret = StringUtil.isNotBlank(param) ? Boolean.parseBoolean(param) : null;
		} else {
			ret = param != null ? Boolean.parseBoolean(param) : null;

			//チェックボックス未チェック時はNullになるがfalseに置き換える
			if (ret == null && "Checkbox".equals(type)) ret = false;
		}
		return ret;
	}
	protected Boolean[] getBooleanValues(String name, int multiplicity) {
		List<Boolean> list = new ArrayList<Boolean>();
		String type = getParam(name + "Type");
		if ("Select".equals(type)) {
			String[] params = getParams(name);
			if (params != null) {
				for (String value : params) {
					list.add(Boolean.parseBoolean(value));
				}
			}
		} else if ("Label".equals(type)){
			String[] params = getParams(name);
			if (params != null) {
				for (String value : params) {
					if (StringUtil.isNotBlank(value)) list.add(Boolean.parseBoolean(value));
				}
			}
		} else {
			//同一名で取得できないので個別に取得
			for (int i = 0; i < multiplicity; i++) {
				String param = getParam(name + i);
				Boolean b = param != null ? Boolean.parseBoolean(param) : null;
				if (b == null && "Checkbox".equals(type)) b = false;
				list.add(b);
			}
		}
		return list.toArray(new Boolean[list.size()]);
	}

	protected SelectValue getSelectValue(String name) {
		String param = getParam(name);
		SelectValue ret = null;
		if (StringUtil.isNotBlank(param)) {
			ret = new SelectValue(param);
		}
		return ret;
	}

	protected SelectValue[] getSelectValues(String name) {
		String[] params = getParams(name);
		List<SelectValue> list = new ArrayList<SelectValue>();
		if (params != null) {
			for (String value : params) {
				list.add(new SelectValue(value));
			}
		}
		return list.toArray(new SelectValue[list.size()]);
	}

	protected String getStringValue(String name) {
		String value = getParam(name);
		if (StringUtil.isBlank(value)) {
			return null;
		} else {
			return value;
		}
	}

	protected String[] getStringValues(String name) {
		String[] params = getParams(name);
		if (params == null || params.length == 0) {
			return new String[0];
		} else {
			return params;
		}
	}

	protected abstract Object createReference(PropertyDefinition p, String paramPrefix);

	/**
	 * カスタム登録処理を取得します。
	 * @return カスタム登録処理
	 */
	public RegistrationInterrupterHandler getRegistrationInterrupterHandler() {
		if (interrupterHandler == null) {
			RegistrationInterrupter interrupter = createInterrupter(getInterrupterName());
			interrupterHandler = new RegistrationInterrupterHandler(request, this, interrupter);
		}
		return interrupterHandler;
	}

	protected abstract String getInterrupterName();

	private RegistrationInterrupter createInterrupter(String className) {
		RegistrationInterrupter interrupter = null;
		if (StringUtil.isNotEmpty(className)) {
			getLogger().debug("set registration interrupter. class=" + className);
			try {
				interrupter = ucdm.createInstanceAs(RegistrationInterrupter.class, className);
			} catch (ClassNotFoundException e) {
				getLogger().error(className + " can not instantiate.", e);
				throw new ApplicationException(resourceString("command.generic.detail.DetailCommandContext.internalErr"));
			}
		}
		if (interrupter == null) {
			//何もしないデフォルトInterrupter生成
			getLogger().debug("set defaul registration interrupter.");
			interrupter = new RegistrationInterrupter() {

				@Override
				public boolean isSpecifyAllProperties() { return false; }

				@Override
				public String[] getAdditionalProperties() { return new String[]{}; }

				@Override
				public void dataMapping(Entity entity, RequestContext request,
						EntityDefinition definition, FormView view) {
				}

				@Override
				public List<ValidateError> beforeRegist(Entity entity,
						RequestContext request, EntityDefinition definition,
						FormView view, RegistrationType registrationType) {
					return Collections.emptyList();
				}

				@Override
				public List<ValidateError> afterRegist(Entity entity,
						RequestContext request, EntityDefinition definition,
						FormView view, RegistrationType registType) {
					return Collections.emptyList();
				}
			};
		}
		return interrupter;
	}

	/**
	 * カスタムロード処理を取得します。
	 * @return カスタムロード処理
	 */
	public LoadEntityInterrupterHandler getLoadEntityInterrupterHandler() {
		if (loadEntityInterrupterHandler == null) {
			LoadEntityInterrupter interrupter = createLoadEntityInterrupter(getLoadEntityInterrupterName());
			loadEntityInterrupterHandler = new LoadEntityInterrupterHandler(request, this, interrupter);
		}
		return loadEntityInterrupterHandler;
	}

	protected abstract String getLoadEntityInterrupterName();

	public List<ReferenceRegistHandler> getReferenceRegistHandlers() {
		return referenceRegistHandlers;
	}

	private LoadEntityInterrupter createLoadEntityInterrupter(String className) {
		LoadEntityInterrupter interrupter = null;
		if (StringUtil.isNotEmpty(className)) {
			getLogger().debug("set load entity interrupter. class=" + className);
			try {
				interrupter = ucdm.createInstanceAs(LoadEntityInterrupter.class, className);
			} catch (ClassNotFoundException e) {
				getLogger().error(className + " can not instantiate.", e);
				throw new ApplicationException(resourceString("command.generic.detail.DetailCommandContext.internalErr"));
			}
		}
		if (interrupter == null) {
			//何もしないデフォルトInterrupter生成
			getLogger().debug("set defaul load entity interrupter.");
			interrupter = new LoadEntityInterrupter() {

				@Override
				public LoadEntityContext beforeLoadEntity(RequestContext request, FormView view, String defName,
						LoadOption loadOption, LoadType type) {
					return new LoadEntityContext(loadOption);
				}

				@Override
				public LoadEntityContext beforeLoadReference(RequestContext request, FormView view, String defName,
						LoadOption loadOption, ReferenceProperty property, LoadType type) {
					return new LoadEntityContext(loadOption);
				}

			};
		}
		return interrupter;
	}

	public void regist(ReferenceRegistHandlerFunction function, Entity inputEntity, Entity loadedEntity) {
		for (ReferenceRegistHandler handler : referenceRegistHandlers) {
			handler.regist(function, inputEntity, loadedEntity);
		}
	}

	public void registMappedby(ReferenceRegistHandlerFunction fucntion, Entity inputEntity, Entity loadedEntity) {
		for (ReferenceRegistHandler handler : referenceRegistHandlers) {
			handler.registMappedby(fucntion, inputEntity, loadedEntity);
		}
	}

	protected static String resourceString(String key, Object... arguments) {
		return GemResourceBundleUtil.resourceString(key, arguments);
	}

	@Override
	public void addError(ValidateError error) {
		for (ValidateError e : getErrors()) {
			//同じプロパティのものがあればメッセージだけ追加
			if (e.getPropertyName().equals(error.getPropertyName())) {
				e.getErrorMessages().addAll(error.getErrorMessages());
				return;
			}
		}

		//同じプロパティがない場合はエラー自体を追加
		getErrors().add(error);
	}

	@Override
	public List<ValidateError> getErrors() {
		if (errors == null) errors = new ArrayList<ValidateError>();
		return errors;
	}

	@Override
	public boolean hasErrors() {
		return errors == null ? false : !errors.isEmpty();
	}

}
