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

package org.iplass.mtp.impl.script;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.definition.AbstractTypedMetaDataService;
import org.iplass.mtp.impl.definition.DefinitionMetaDataTypeMap;
import org.iplass.mtp.impl.definition.DefinitionNameChecker;
import org.iplass.mtp.impl.script.MetaUtilityClass.UtilityClassRuntime;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.transaction.TransactionListener;
import org.iplass.mtp.transaction.TransactionStatus;
import org.iplass.mtp.utilityclass.definition.UtilityClassDefinition;
import org.iplass.mtp.utilityclass.definition.UtilityClassDefinitionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import groovy.lang.ExpandoMetaClass;
import groovy.lang.GroovyClassLoader;


public class GroovyScriptService extends AbstractTypedMetaDataService<MetaUtilityClass, UtilityClassRuntime> implements ScriptService {
	private static Logger logger = LoggerFactory.getLogger(GroovyScriptService.class);

	private static final String ENCODING = "UTF-8";

	public static final String UTILITY_CLASS_META_PATH = "/utilityClass/";

	private static final String TRANSACTION_FLAG_KEY = "mtp.script.invalidateTenantContext";

	public static class TypeMap extends DefinitionMetaDataTypeMap<UtilityClassDefinition, MetaUtilityClass> {
		private static final String NAME_CHECK_PATTERN = "^[0-9a-zA-Z_][0-9a-zA-Z_-]*(\\.[0-9a-zA-Z_-]+)*$";

		private static final String NAME_CHECK_MESSAGE = "impl.definition.DefinitionNameChecker.NamePathPeriod.invalidPattern";

		public TypeMap() {
			super(getFixedPath(), MetaUtilityClass.class, UtilityClassDefinition.class);
		}
		@Override
		public TypedDefinitionManager<UtilityClassDefinition> typedDefinitionManager() {
			return ManagerLocator.getInstance().getManager(UtilityClassDefinitionManager.class);
		}
		@Override
		public String toPath(String defName) {
			return pathPrefix + defName.replace('.', '/');
		}
		@Override
		public String toDefName(String path) {
			return path.substring(pathPrefix.length()).replace("/", ".");
		}

		@Override
		protected DefinitionNameChecker createDefinitionNameChecker() {
			return new DefinitionNameChecker(NAME_CHECK_PATTERN, NAME_CHECK_MESSAGE) {
			};
		}
	}

	//TODO 実行可能なコードの制限（ファイル操作、直接RDB接続などをできないように）。
	//JavaSecurityの適用のため、GroovyScriptEngineを直接使用するしかないか？
	//直接Groovyのエンジンを使ったほうがよい。Scriptのインスタンス（コンパイルされたGroovyClass）のコピーができて、別コンテキストで実行できる。

	//GroovyCodeSource を使って、codebaseを書き換え可能

	private GroovyClassLoader parentClassLoader;
	private List<GroovyScript> expandScriptRefs;
	
	private List<String> initScript;
	private List<String> importList;
	private List<String> staticImportList;

	private boolean debug;

	public GroovyScriptService() {
	}
	
	private String getScriptSrc(String path) {
		InputStream is = GroovyScriptService.class.getResourceAsStream(path);
		byte[] buf = new byte[1024];
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		int count;
		try {
			while ((count = is.read(buf)) != -1) {
				os.write(buf, 0, count);
			}
			os.flush();
			return os.toString(ENCODING);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					logger.warn("can not close resource:" + path + ". may be reak...");
				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					//発生しえない
				}
			}
		}
	}

	public void destroy() {
	}

	public void init(Config config) {
		ExpandoMetaClass.enableGlobally();
		if (config.getValue("debug") != null) {
			debug = Boolean.valueOf(config.getValue("debug"));
		}
		importList = config.getValues("import");
		staticImportList = config.getValues("staticImport");

		//文字コードをUTF-8に固定
		CompilerConfiguration conf = new CompilerConfiguration();
		conf.setSourceEncoding(ENCODING);
		if (debug) {
			conf.setVerbose(true);
			conf.setDebug(true);
		}
		parentClassLoader = new GroovyClassLoader(GroovyScriptService.class.getClassLoader(), conf, false);
		
		initScript = config.getValues("initScript");
		if (initScript != null) {
			expandScriptRefs = new ArrayList<>();
			for (int i = 0; i < initScript.size(); i++) {
				String src = getScriptSrc(initScript.get(i));
				GroovyScript expandScriptRef = new GroovyScript(parentClassLoader, src, "___initScript_" + i + "___");
				expandScriptRef.eval(new GroovyScriptContext());
				expandScriptRefs.add(expandScriptRef);
			}
		}
	}

	public static String getFixedPath() {
		return UTILITY_CLASS_META_PATH;
	}

	public boolean isDebug() {
		return debug;
	}
	
	private CompilerConfiguration newCompilerConfiguration() {
		CompilerConfiguration conf = new CompilerConfiguration();
		conf.setSourceEncoding(ENCODING);
		if (debug) {
			conf.setVerbose(true);
			conf.setDebug(true);
		}
		ImportCustomizer ic = null;
		if (importList != null && importList.size() > 0) {
			ic = new ImportCustomizer();
			for (String i: importList) {
				i = i.trim();
				if (i.endsWith(".*")) {
					ic.addStarImports(i.substring(0, i.length() - 2));
				} else {
					ic.addImports(i);
				}
			}
		}
		if (staticImportList != null && staticImportList.size() > 0) {
			if (ic == null) {
				ic = new ImportCustomizer();
			}
			for (String si: staticImportList) {
				si = si.trim();
				if (si.endsWith(".*")) {
					ic.addStaticStars(si.substring(0, si.length() - 2));
				} else {
					int dot = si.lastIndexOf('.');
					ic.addStaticImport(si.substring(0, dot), si.substring(dot + 1));
				}
			}
		}
		if (ic != null) {
			conf.addCompilationCustomizers(ic);
		}
		return conf;

	}

	public ScriptEngine createScriptEngine() {
		return createScriptEngine(false);
	}

	public ScriptEngine createScriptEngine(boolean vanilla) {
		return new GroovyScriptEngine(new GroovyClassLoader(parentClassLoader, newCompilerConfiguration(), false), this, vanilla);
	}

	@Override
	public void updateMetaData(MetaUtilityClass meta) {
		super.updateMetaData(meta);
		invalidateTenantContext();
	}

	@Override
	public void removeMetaData(String definitionName) {
		super.removeMetaData(definitionName);
		invalidateTenantContext();
	}

	private void invalidateTenantContext() {

		//TenantContextのリロード。このスレッド自身は、旧のTenantContextで実行される
		final TenantContext current = ExecuteContext.getCurrentContext().getTenantContext();
		Transaction t = Transaction.getCurrent();
		if (t.getStatus() == TransactionStatus.ACTIVE) {
			if (t.getAttribute(TRANSACTION_FLAG_KEY) == null) {
				t.addTransactionListener(new TransactionListener() {
					@Override
					public void afterCommit(Transaction t) {
						TenantContextService tcService = ServiceRegistry.getRegistry().getService(TenantContextService.class);
						tcService.reloadTenantContext(current.getTenantId(), false);
					}
				});
			}
		} else {
			TenantContextService tcService = ServiceRegistry.getRegistry().getService(TenantContextService.class);
			tcService.reloadTenantContext(current.getTenantId(), false);
		}

	}

	@Override
	public Class<MetaUtilityClass> getMetaDataType() {
		return MetaUtilityClass.class;
	}

	@Override
	public Class<UtilityClassRuntime> getRuntimeType() {
		return UtilityClassRuntime.class;
	}

}
