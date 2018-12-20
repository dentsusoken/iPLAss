/*
 * Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.entity.listener;

import java.util.HashMap;
import java.util.Map;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityEventContext;
import org.iplass.mtp.entity.definition.EventListenerDefinition;
import org.iplass.mtp.entity.definition.listeners.EventType;
import org.iplass.mtp.entity.definition.listeners.SendNotificationListenerDefinition;
import org.iplass.mtp.entity.definition.listeners.SendNotificationType;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.entity.MetaEventListener;
import org.iplass.mtp.impl.script.Script;
import org.iplass.mtp.impl.script.ScriptContext;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.mail.Mail;
import org.iplass.mtp.mail.MailManager;
import org.iplass.mtp.pushnotification.PushNotification;
import org.iplass.mtp.pushnotification.PushNotificationManager;
import org.iplass.mtp.sms.SmsMail;
import org.iplass.mtp.sms.SmsMailManager;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.transaction.TransactionManager;
import org.iplass.mtp.transaction.TransactionStatus;
import org.iplass.mtp.util.StringUtil;

public class MetaSendNotificationListener extends MetaEventListener {
	private static final long serialVersionUID = -6140973695520029215L;

	public static final String TENANT_BINDING_NAME = "tenant";
	public static final String ENTITY_BINDING_NAME = "entity";
	public static final String EVENT_TYPE_BINDING_NAME = "event";
	public static final String CONTEXT_BINDING_NAME = "context";
	public static final String USER_BINDING_NAME = "user";
	public static final String DATE_BINGING_NAME = "date";

	private String notificationTmplType;
	private String tmplDefName;
	private String notificationCondScript;

	public String getNotificationTmplType() {
		return notificationTmplType;
	}

	public void setNotificationTmplType(String notificationTmplType) {
		this.notificationTmplType = notificationTmplType;
	}

	public String getTmplDefName() {
		return tmplDefName;
	}

	public void setTmplDefName(String tmplDefName) {
		this.tmplDefName = tmplDefName;
	}

	public String getNotificationCondScript() {
		return notificationCondScript;
	}

	public void setNotificationCondScript(String notificationCondScript) {
		this.notificationCondScript = notificationCondScript;
	}

	@Override
	public MetaEventListener copy() {
		MetaSendNotificationListener copy = new MetaSendNotificationListener();
		copyTo(copy);
		copy.notificationTmplType = notificationTmplType;
		copy.tmplDefName = tmplDefName;
		copy.notificationCondScript = notificationCondScript;

		return copy;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((notificationCondScript == null) ? 0 : notificationCondScript.hashCode());
		result = prime * result + ((notificationTmplType == null) ? 0 : notificationTmplType.hashCode());
		result = prime * result + ((tmplDefName == null) ? 0 : tmplDefName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		MetaSendNotificationListener other = (MetaSendNotificationListener) obj;
		if (notificationCondScript == null) {
			if (other.notificationCondScript != null)
				return false;
		} else if (!notificationCondScript.equals(other.notificationCondScript))
			return false;
		if (notificationTmplType == null) {
			if (other.notificationTmplType != null)
				return false;
		} else if (!notificationTmplType.equals(other.notificationTmplType))
			return false;
		if (tmplDefName == null) {
			if (other.tmplDefName != null)
				return false;
		} else if (!tmplDefName.equals(other.tmplDefName))
			return false;
		return true;
	}

	@Override
	public void applyConfig(EventListenerDefinition def) {
		fillFrom(def);
		SendNotificationListenerDefinition d = (SendNotificationListenerDefinition) def;
		notificationTmplType = d.getNotificationTmplType();
		tmplDefName = d.getTmplDefName();
		notificationCondScript = d.getNotificationCondScript();
	}

	@Override
	public EventListenerDefinition currentConfig() {
		SendNotificationListenerDefinition d = new SendNotificationListenerDefinition();
		fillTo(d);
		d.setNotificationTmplType(notificationTmplType);
		d.setTmplDefName(tmplDefName);
		d.setNotificationCondScript(notificationCondScript);
		return d;
	}

	@Override
	public EventListenerRuntime createRuntime(MetaEntity entity) {
		if (SendNotificationType.MAIL.name().equals(notificationTmplType)) {
			return new SendMailNotificationListenerHandler(entity);
		} else if (SendNotificationType.SMS.name().equals(notificationTmplType)) {
			return new SendSMSNotificationListenerHandler(entity);
		} else if (SendNotificationType.PUSH.name().equals(notificationTmplType)) {
			return new SendPushNotificationListenerHandler(entity);
		}
		return null;
	}

	private abstract class SendNotificationListenerHandler extends EventListenerRuntime {

		private static final String SCRIPT_PREFIX = "SendNotificationListenerHandler_notificationCondScript";

		private Script compiledScript;
		private ScriptEngine scriptEngine;

		public SendNotificationListenerHandler(MetaEntity entity) {

			// TODO tenantIDの決定は、このメソッドを呼び出した際のスレッドに紐付いているテナントIDとなる。これでセキュリティ的、動作的に大丈夫か？
			TenantContext tc = ExecuteContext.getCurrentContext().getTenantContext();
			scriptEngine = tc.getScriptEngine();

			if (StringUtil.isNotEmpty(notificationCondScript)) {
				String scriptWithImport = "import " + EventType.class.getName() + ";\n" + notificationCondScript;
				String scriptName = null;
				for (int i = 0; i < entity.getEventListenerList().size(); i++) {
					if (MetaSendNotificationListener.this == entity.getEventListenerList().get(i)) {
						scriptName = SCRIPT_PREFIX + "_" + entity.getId() + "_" + i;
						break;
					}
				}
				compiledScript = scriptEngine.createScript(scriptWithImport, scriptName);
			}
		}

		private void setupHandleAfterCommit(Entity entity, EventType type, EntityEventContext context) {
			// 通知する条件が未設定の場合、通知します。
			if (StringUtil.isEmpty(notificationCondScript) || callScript(entity, type, context)) {
				Transaction t = ManagerLocator.getInstance().getManager(TransactionManager.class).currentTransaction();
				if (t != null && t.getStatus() == TransactionStatus.ACTIVE) {
					t.afterCommit(() -> {
						sendNotification(entity, type, context);
					});
				}
			}
		}

		private boolean callScript(Entity entity, EventType type, EntityEventContext context) {
			ScriptContext sc = scriptEngine.newScriptContext();
			sc.setAttribute(ENTITY_BINDING_NAME, entity);
			sc.setAttribute(EVENT_TYPE_BINDING_NAME, type);
			sc.setAttribute(CONTEXT_BINDING_NAME, context);
			ExecuteContext ex = ExecuteContext.getCurrentContext();
			sc.setAttribute(USER_BINDING_NAME, AuthContextHolder.getAuthContext().newUserBinding());
			sc.setAttribute(DATE_BINGING_NAME, ex.getCurrentTimestamp());

			Object val = compiledScript.eval(sc);
			if (val != null) {
				if (val instanceof Boolean) {
					return (Boolean) val;
				} else if (val instanceof String) {
					return Boolean.parseBoolean((String) val);
				}
			}
			return false;
		}

		protected abstract void sendNotification(Entity entity, EventType type, EntityEventContext context);

		@Override
		public void handleAfterDelete(Entity entity, EntityEventContext context) {
		}

		@Override
		public void handleAfterInsert(Entity entity, EntityEventContext context) {
		}

		@Override
		public void handleAfterUpdate(Entity entity, EntityEventContext context) {
		}

		@Override
		public boolean handleBeforeDelete(Entity entity, EntityEventContext context) {
			setupHandleAfterCommit(entity, EventType.AFTER_DELETE_COMMIT, context);
			return true;
		}

		@Override
		public boolean handleBeforeInsert(Entity entity, EntityEventContext context) {
			setupHandleAfterCommit(entity, EventType.AFTER_INSERT_COMMIT, context);
			return true;
		}

		@Override
		public boolean handleBeforeUpdate(Entity entity, EntityEventContext context) {
			setupHandleAfterCommit(entity, EventType.AFTER_UPDATE_COMMIT, context);
			return true;
		}

		@Override
		public void handleOnLoad(Entity entity) {
		}

		@Override
		public void handleBeforeValidate(Entity entity, EntityEventContext context) {
		}

		@Override
		public void handleAfterRestore(Entity entity) {
		}

		@Override
		public void handleAfterPurge(Entity entity) {
		}

		public MetaSendNotificationListener getMetaData() {
			return MetaSendNotificationListener.this;
		}
	}

	public class SendMailNotificationListenerHandler extends SendNotificationListenerHandler {

		public SendMailNotificationListenerHandler(MetaEntity entity) {
			super(entity);
		}

		@Override
		protected void sendNotification(Entity entity, EventType type, EntityEventContext context) {
			ExecuteContext ex = ExecuteContext.getCurrentContext();

			MailManager mm = ManagerLocator.getInstance().getManager(MailManager.class);
			Map<String, Object> bindings = new HashMap<String, Object>();
			bindings.put(TENANT_BINDING_NAME, ex.getCurrentTenant());
			bindings.put(ENTITY_BINDING_NAME, entity);
			bindings.put(EVENT_TYPE_BINDING_NAME, type);
			bindings.put(CONTEXT_BINDING_NAME, context);
			bindings.put(USER_BINDING_NAME, AuthContextHolder.getAuthContext().newUserBinding());
			bindings.put(DATE_BINGING_NAME, ex.getCurrentTimestamp());

			Mail mail = mm.createMail(tmplDefName, bindings);
			mm.sendMail(mail);
		}
	}

	public class SendSMSNotificationListenerHandler extends SendNotificationListenerHandler {

		public SendSMSNotificationListenerHandler(MetaEntity entity) {
			super(entity);
		}

		@Override
		protected void sendNotification(Entity entity, EventType type, EntityEventContext context) {
			ExecuteContext ex = ExecuteContext.getCurrentContext();

			SmsMailManager smm = ManagerLocator.getInstance().getManager(SmsMailManager.class);
			Map<String, Object> bindings = new HashMap<String, Object>();
			bindings.put(TENANT_BINDING_NAME, ex.getCurrentTenant());
			bindings.put(ENTITY_BINDING_NAME, entity);
			bindings.put(EVENT_TYPE_BINDING_NAME, type);
			bindings.put(CONTEXT_BINDING_NAME, context);
			bindings.put(USER_BINDING_NAME, AuthContextHolder.getAuthContext().newUserBinding());
			bindings.put(DATE_BINGING_NAME, ex.getCurrentTimestamp());
			SmsMail smsMail = smm.createMail(tmplDefName, bindings);
			smm.sendMail(smsMail);
		}
	}

	public class SendPushNotificationListenerHandler extends SendNotificationListenerHandler {

		public SendPushNotificationListenerHandler(MetaEntity entity) {
			super(entity);
		}

		@Override
		protected void sendNotification(Entity entity, EventType type, EntityEventContext context) {
			ExecuteContext ex = ExecuteContext.getCurrentContext();

			PushNotificationManager pm = ManagerLocator.getInstance().getManager(PushNotificationManager.class);
			Map<String, Object> bindings = new HashMap<String, Object>();
			bindings.put(TENANT_BINDING_NAME, ex.getCurrentTenant());
			bindings.put(ENTITY_BINDING_NAME, entity);
			bindings.put(EVENT_TYPE_BINDING_NAME, type);
			bindings.put(CONTEXT_BINDING_NAME, context);
			bindings.put(USER_BINDING_NAME, AuthContextHolder.getAuthContext().newUserBinding());
			bindings.put(DATE_BINGING_NAME, ex.getCurrentTimestamp());
			PushNotification notification = pm.createNotification(tmplDefName, bindings);
			pm.push(notification);
		}
	}
}
