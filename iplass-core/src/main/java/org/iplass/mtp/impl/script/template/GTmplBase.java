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
package org.iplass.mtp.impl.script.template;

import java.sql.Time;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.regex.Pattern;

import org.codehaus.groovy.runtime.InvokerHelper;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.auth.Permission;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.prefs.MetaPreference.PreferenceRuntime;
import org.iplass.mtp.impl.prefs.MetaPreferenceSet.PreferenceSetRuntime;
import org.iplass.mtp.impl.prefs.PreferenceService;
import org.iplass.mtp.message.MessageItem;
import org.iplass.mtp.message.MessageManager;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.DateUtil;
import org.iplass.mtp.util.ResourceBundleUtil;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import groovy.lang.Closure;
import groovy.lang.MissingPropertyException;

public class GTmplBase {
	private static Logger logger = LoggerFactory.getLogger(GTmplBase.class);
	
	private static Pattern sp = Pattern.compile("[,\\s]+");

	private MessageManager messageManager = ManagerLocator.getInstance().getManager(MessageManager.class);
	private PreferenceService ps = ServiceRegistry.getRegistry().getService(PreferenceService.class);

	protected String msg(String categoryName, String messageId, Object... params) {
		if (messageManager == null) {
			messageManager = ManagerLocator.getInstance().getManager(MessageManager.class);
		}

		MessageItem message = messageManager.getMessageItem(categoryName, messageId);
		if (message == null) {
			return "";
		}
		String str = I18nUtil.stringDef(message.getMessage(), message.getLocalizedMessageList());
		if (params == null || params.length == 0) {
			return str;
		}

		if (params.length == 1 && params[0] instanceof Collection<?>) {
			return MessageFormat.format(str, (Object[]) ((Collection<?>) params[0]).toArray(new Object[((Collection<?>) params[0]).size()]));
		} else {
			return MessageFormat.format(str, params);
		}
	}

	protected String escJs(Object value) {
		if (value instanceof Closure) {
			try {
				value = ((Closure<?>) value).call();
			} catch (NullPointerException | MissingPropertyException e) {
				//ignore NPE/MPE
				if (logger.isDebugEnabled()) {
					logger.debug("igrore NPE/MPE on escJs:" + e);
				}
				return "";
			}
		}
		if (value == null) {
			return "";
		}
		return StringUtil.escapeJavaScript(value.toString());
	}
	
	protected String escHtml(Object value) {
		if (value instanceof Closure) {
			try {
				value = ((Closure<?>) value).call();
			} catch (NullPointerException | MissingPropertyException e) {
				//ignore NPE/MPE
				if (logger.isDebugEnabled()) {
					logger.debug("igrore NPE/MPE on escHtml:" + e);
				}
				return "";
			}
		}
		if (value == null) {
			return "";
		}
		return StringUtil.escapeHtml(value.toString());
	}
	
	protected String escXml(Object value) {
		if (value instanceof Closure) {
			try {
				value = ((Closure<?>) value).call();
			} catch (NullPointerException | MissingPropertyException e) {
				//ignore NPE/MPE
				if (logger.isDebugEnabled()) {
					logger.debug("igrore NPE/MPE on escXml:" + e);
				}
				return "";
			}
		}
		if (value == null) {
			return "";
		}
		return StringUtil.escapeXml10(value.toString());
	}
	
	protected String escEql(Object value) {
		if (value instanceof Closure) {
			try {
				value = ((Closure<?>) value).call();
			} catch (NullPointerException | MissingPropertyException e) {
				//ignore NPE/MPE
				if (logger.isDebugEnabled()) {
					logger.debug("igrore NPE/MPE on escEql:" + e);
				}
				return "";
			}
		}
		if (value == null) {
			return "";
		}
		return StringUtil.escapeEql(value.toString());
	}

	@Deprecated
	protected String escSql(Object value) {
		return escEql(value);
	}

	protected String escEqlLike(Object value, boolean escapeSingleQuote) {
		if (value instanceof Closure) {
			try {
				value = ((Closure<?>) value).call();
			} catch (NullPointerException | MissingPropertyException e) {
				//ignore NPE/MPE
				if (logger.isDebugEnabled()) {
					logger.debug("igrore NPE/MPE on escapeEqlForLike:" + e);
				}
				return "";
			}
		}
		if (value == null) {
			return "";
		}
		if (escapeSingleQuote) {
			return StringUtil.escapeEql(StringUtil.escapeEqlForLike(value.toString()));
		} else {
			return StringUtil.escapeEqlForLike(value.toString());
		}
	}
	
	protected String escEqlLike(Object value) {
		return escEqlLike(value, true);
	}

	@Deprecated
	protected String escSqlLike(Object value) {
		return escEqlLike(value);
	}

	protected Object nte(Object value) {
		if (value instanceof Closure) {
			try {
				value = ((Closure<?>) value).call();
			} catch (NullPointerException | MissingPropertyException e) {
				//ignore NPE/MPE
				if (logger.isDebugEnabled()) {
					logger.debug("igrore NPE/MPE on nte:" + e);
				}
				return "";
			}
		}
		if (value == null) {
			return "";
		}
		return value;
	}

	protected Object prefs(String name) {
		if (ps == null) {
			ps = ServiceRegistry.getRegistry().getService(PreferenceService.class);
		}

		PreferenceRuntime pr = ps.getRuntimeByName(name);
		if (pr == null) {
			return null;
		}
		if (pr.getRuntime() != null) {
			return pr.getRuntime();
		}
		if (pr instanceof PreferenceSetRuntime) {
			return pr.getMap();
		} else {
			return pr.getMetaData().getValue();
		}
	}

	protected String fmt(Object value, String pattern) {
		try {
			if (value instanceof java.sql.Date) {
				SimpleDateFormat fmt = DateUtil.getSimpleDateFormat(pattern, false);
				return fmt.format((java.sql.Date) value);
			}
			if (value instanceof Time) {
				SimpleDateFormat fmt = DateUtil.getSimpleDateFormat(pattern, false);
				return fmt.format((java.sql.Time) value);
			}
			if (value instanceof Date) {
				SimpleDateFormat fmt = DateUtil.getSimpleDateFormat(pattern, true);
				return fmt.format((Date) value);
			}
			if (value instanceof Number) {
				DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance(ExecuteContext.getCurrentContext().getLocale());
				DecimalFormat fmt = new DecimalFormat(pattern, dfs);
				return fmt.format(value);
			}
		} catch (RuntimeException e) {
			logger.debug("cant format:" + value + ",pattern:" + pattern + ", " + e);
		}

		return "";
	}

	public static String rs(String baseName, String key, Object... params) {
		if (params == null || params.length == 0) {
			return ResourceBundleUtil.resourceString(ResourceBundleUtil.getResourceBundle(baseName), key);
		}
		if (params.length == 1 && params[0] instanceof Collection<?>) {
			return ResourceBundleUtil.resourceString(ResourceBundleUtil.getResourceBundle(baseName), key,
					(Object[]) ((Collection<?>) params[0]).toArray(new Object[((Collection<?>) params[0]).size()]));
		}
		return ResourceBundleUtil.resourceString(ResourceBundleUtil.getResourceBundle(baseName), key, params);
	}

	//println to logger
	public void println() {
		GroovyTemplateContext.getContext().loggerPrintWriter().println();
	}

	public void print(Object value) {
		GroovyTemplateContext.getContext().loggerPrintWriter().print(InvokerHelper.toString(value));
	}

	public void println(Object value) {
		GroovyTemplateContext.getContext().loggerPrintWriter().println(InvokerHelper.toString(value));
	}

	public void printf(String format, Object value) {
		GroovyTemplateContext.getContext().loggerPrintWriter().printf(format, value);
	}

	public void printf(String format, Object[] values) {
		GroovyTemplateContext.getContext().loggerPrintWriter().printf(format, values);
	}
	
	public void auth(Map<String, Object> params, Closure<Void> inner) {
		AuthContext auth = AuthContext.getCurrentContext();
		
		String role = (String) params.get("role");
		if (role != null) {
			boolean userInRole = false;
			if (role.indexOf(',') < 0) {
				userInRole = auth.userInRole(role);
			} else {
				String[] roles = sp.split(role);
				for (String r: roles) {
					userInRole |= userInRole | auth.userInRole(r);
					if (userInRole) {
						break;
					}
				}
			}
			if (!userInRole) {
				if (logger.isDebugEnabled()) {
					logger.debug("userInRole(\"" + role + "\") == false, SKIP_BODY");
				}
				return;
			}
		}
		
		Permission permission = (Permission) params.get("permission");
		if (permission != null) {
			if (!auth.checkPermission(permission)) {
				if (logger.isDebugEnabled()) {
					logger.debug("checkPermission(" + permission + ") == false, SKIP_BODY");
				}
				return;
			}
		}
		
		Boolean privileged = (Boolean) params.get("privileged");
		if (privileged != null && privileged.booleanValue()) {
			AuthContext.doPrivileged(() -> {
				inner.call();
			}); 
		} else {
			inner.call();
		}
	}
}
