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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityEventContext;
import org.iplass.mtp.entity.definition.EventListenerDefinition;
import org.iplass.mtp.entity.definition.listeners.EventType;
import org.iplass.mtp.entity.definition.listeners.SendNotificationEventListenerDefinition;
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
import org.iplass.mtp.webhook.WebHook;
import org.iplass.mtp.webhook.WebHookManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetaSendNotificationEventListener extends MetaEventListener {
	private static final long serialVersionUID = 3089787592456473699L;

	private static final Logger fatalLog = LoggerFactory.getLogger("mtp.fatal.mail");

	public static final String TENANT_BINDING_NAME = "tenant";
	public static final String ENTITY_BINDING_NAME = "entity";
	public static final String EVENT_TYPE_BINDING_NAME = "event";
	public static final String CONTEXT_BINDING_NAME = "context";
	public static final String USER_BINDING_NAME = "user";
	public static final String DATE_BINGING_NAME = "date";

	private SendNotificationType notificationType;
	private String tmplDefName;
	private String notificationCondScript;
	private List<EventType> listenEvent;

	/**ウェッブフックだけの設定項目*/
	private boolean isSynchronous;
	private List<String> endPointDefList;
	private String webHookResultHandlerDef;

	public SendNotificationType getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(SendNotificationType notificationType) {
		this.notificationType = notificationType;
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

	public List<EventType> getListenEvent() {
		return listenEvent;
	}

	public void setListenEvent(List<EventType> listenEvent) {
		this.listenEvent = listenEvent;
	}

	public List<String> getEndPointDefList() {
		return endPointDefList;
	}

	public void setEndPointDefList(List<String> endPointDefList) {
		this.endPointDefList = endPointDefList;
	}
	
	public String getWebHookResultHandlerDef() {
		return webHookResultHandlerDef;
	}

	public void setWebHookResultHandlerDef(String webHookResultHandlerDef) {
		this.webHookResultHandlerDef = webHookResultHandlerDef;
	}

	public boolean getIsSynchronous() {
		return isSynchronous;
	}

	public void setIsSynchronous(boolean isSynchronous) {
		this.isSynchronous = isSynchronous;
	}

	@Override
	public MetaEventListener copy() {
		MetaSendNotificationEventListener copy = new MetaSendNotificationEventListener();
		copyTo(copy);
		copy.notificationType = notificationType;
		copy.tmplDefName = tmplDefName;
		copy.notificationCondScript = notificationCondScript;
		copy.webHookResultHandlerDef = webHookResultHandlerDef;
		if (listenEvent != null) {
			copy.listenEvent = new ArrayList<EventType>();
			copy.listenEvent.addAll(listenEvent);
		}
		copy.setIsSynchronous(isSynchronous);
		if (endPointDefList !=null) {
			copy.endPointDefList = new ArrayList<>(endPointDefList );
			copy.endPointDefList.addAll(endPointDefList);
		}		
		return copy;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((listenEvent == null) ? 0 : listenEvent.hashCode());
		result = prime * result + ((notificationCondScript == null) ? 0 : notificationCondScript.hashCode());
		result = prime * result + ((notificationType == null) ? 0 : notificationType.hashCode());
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
		MetaSendNotificationEventListener other = (MetaSendNotificationEventListener) obj;
		if (listenEvent == null) {
			if (other.listenEvent != null)
				return false;
		} else if (!listenEvent.equals(other.listenEvent))
			return false;
		if (notificationCondScript == null) {
			if (other.notificationCondScript != null)
				return false;
		} else if (!notificationCondScript.equals(other.notificationCondScript))
			return false;
		if (notificationType != other.notificationType)
			return false;
		if (tmplDefName == null) {
			if (other.tmplDefName != null)
				return false;
		} else if (!tmplDefName.equals(other.tmplDefName)) {
			return false;
			
		} else if (!isSynchronous==other.isSynchronous) {
			return false;
		} else if (endPointDefList==null) {
			if (other.endPointDefList!=null) {
				return false;
			}
		} else if(!endPointDefList.equals(other.endPointDefList)) {
			return false;
		} else if (webHookResultHandlerDef==null) {
			if (other.webHookResultHandlerDef!=null) {
				return false;
			}
		} else if (!webHookResultHandlerDef.equals(other.webHookResultHandlerDef)) {
			return false;
		}
		
		return true;
	}

	@Override
	public void applyConfig(EventListenerDefinition def) {
		fillFrom(def);
		SendNotificationEventListenerDefinition d = (SendNotificationEventListenerDefinition) def;
		notificationType = d.getNotificationType();
		tmplDefName = d.getTmplDefName();
		notificationCondScript = d.getNotificationCondScript();
		webHookResultHandlerDef = d.getWebHookResultHandlerDef();
		if (d.getListenEvent() != null) {
			listenEvent = new ArrayList<EventType>();
			listenEvent.addAll(d.getListenEvent());
		} else {
			// listenEventをnullにクリアする
			listenEvent = null;
		}
		if (d.getEndPointDefList() != null) {
			endPointDefList = new ArrayList<String>();
			endPointDefList.addAll(d.getEndPointDefList());
		} else {
			endPointDefList = null;
		}

	}

	@Override
	public EventListenerDefinition currentConfig() {
		SendNotificationEventListenerDefinition d = new SendNotificationEventListenerDefinition();
		fillTo(d);
		d.setNotificationType(notificationType);
		d.setTmplDefName(tmplDefName);
		d.setNotificationCondScript(notificationCondScript);
		d.setWebHookResultHandlerDef(webHookResultHandlerDef);
		
		if (listenEvent != null) {
			List<EventType> es = new ArrayList<EventType>();
			es.addAll(listenEvent);
			d.setListenEvent(es);
		}
		d.setIsSynchronous(isSynchronous);
		if (endPointDefList != null) {
			List<String> es = new ArrayList<String>();
			es.addAll(endPointDefList);
			d.setEndPointDefList(es);
		}
		return d;
	}

	@Override
	public EventListenerRuntime createRuntime(MetaEntity entity) {
		if (notificationType == SendNotificationType.MAIL) {
			return new SendMailNotificationEventListenerHandler(entity);
		} else if (notificationType == SendNotificationType.SMS) {
			return new SendSMSNotificationEventListenerHandler(entity);
		} else if (notificationType == SendNotificationType.PUSH) {
			return new SendPushNotificationEventListenerHandler(entity);
		} else if (notificationType == SendNotificationType.WEBHOOK) {
			return new SendWebHookNotificationEventListenerHandler(entity);
		}
		return null;
	}

	private abstract class SendNotificationListenerEventHandler extends EventListenerRuntime {

		private static final String SCRIPT_PREFIX = "SendNotificationListenerEventHandler_notificationCondScript";

		private Script compiledScript;
		private ScriptEngine scriptEngine;

		private boolean isNotifyAfterDelete;
		private boolean isNotifyAfterInsert;
		private boolean isNotifyAfterUpdate;
		private boolean isNotifyBeforeDelete;
		private boolean isNotifyBeforeInsert;
		private boolean isNotifyBeforeUpdate;
		private boolean isNotifyBeforeValidate;
		private boolean isNotifyOnLoad;
		private boolean isNotifyAfterRestore;
		private boolean isNotifyAfterPurge;

		public SendNotificationListenerEventHandler(MetaEntity entity) {

			// TODO tenantIDの決定は、このメソッドを呼び出した際のスレッドに紐付いているテナントIDとなる。これでセキュリティ的、動作的に大丈夫か？
			TenantContext tc = ExecuteContext.getCurrentContext().getTenantContext();
			scriptEngine = tc.getScriptEngine();

			if (StringUtil.isNotEmpty(notificationCondScript)) {
				String scriptWithImport = "import " + EventType.class.getName() + ";\n" + notificationCondScript;
				String scriptName = null;
				for (int i = 0; i < entity.getEventListenerList().size(); i++) {
					if (MetaSendNotificationEventListener.this == entity.getEventListenerList().get(i)) {
						scriptName = SCRIPT_PREFIX + "_" + entity.getId() + "_" + i;
						break;
					}
				}
				compiledScript = scriptEngine.createScript(scriptWithImport, scriptName);
			}

			if (listenEvent != null) {
				for (EventType eType : listenEvent) {
					switch (eType) {
					case AFTER_DELETE:
						isNotifyAfterDelete = true;
						break;
					case AFTER_INSERT:
						isNotifyAfterInsert = true;
						break;
					case AFTER_UPDATE:
						isNotifyAfterUpdate = true;
						break;
					case BEFORE_DELETE:
						isNotifyBeforeDelete = true;
						break;
					case BEFORE_INSERT:
						isNotifyBeforeInsert = true;
						break;
					case BEFORE_UPDATE:
						isNotifyBeforeUpdate = true;
						break;
					case BEFORE_VALIDATE:
						isNotifyBeforeValidate = true;
						break;
					case ON_LOAD:
						isNotifyOnLoad = true;
						break;
					case AFTER_RESTORE:
						isNotifyAfterRestore = true;
						break;
					case AFTER_PURGE:
						isNotifyAfterPurge = true;
					default:
						break;
					}
				}
			}
		}

		private void setupHandleAfterCommit(Entity entity, EventType type, EntityEventContext context) {
			Transaction t = ManagerLocator.getInstance().getManager(TransactionManager.class).currentTransaction();
			final Object n = createNotification(entity, type, context);
			if (t != null && t.getStatus() == TransactionStatus.ACTIVE) {
				t.afterCommit(() -> {
					try {
						sendNotification(n);
					} catch (RuntimeException e) {
						fatalLog.error("cannot send notification:"
								+ "entity=" + entity
								+ ",event=" + type
								+ ",context=" + context
								+ ",templateName=" + tmplDefName,
								e);
					}
				});
			} else {
				// トランザクションがないので、即時通知する
				sendNotification(n);
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

		protected abstract Object createNotification(Entity entity, EventType type, EntityEventContext context);

		protected abstract void sendNotification(Object mail);

		@Override
		public void handleAfterDelete(Entity entity, EntityEventContext context) {
			if (isNotifyAfterDelete) {
				// 通知する条件が未設定の場合、通知します。
				if (StringUtil.isEmpty(notificationCondScript) || callScript(entity, EventType.AFTER_DELETE, context)) {
					setupHandleAfterCommit(entity, EventType.AFTER_DELETE, context);
				}
			}
		}

		@Override
		public void handleAfterInsert(Entity entity, EntityEventContext context) {
			if (isNotifyAfterInsert) {
				// 通知する条件が未設定の場合、通知します。
				if (StringUtil.isEmpty(notificationCondScript) || callScript(entity, EventType.AFTER_INSERT, context)) {
					setupHandleAfterCommit(entity, EventType.AFTER_INSERT, context);
				}
			}
		}

		@Override
		public void handleAfterUpdate(Entity entity, EntityEventContext context) {
			if (isNotifyAfterUpdate) {
				// 通知する条件が未設定の場合、通知します。
				if (StringUtil.isEmpty(notificationCondScript) || callScript(entity, EventType.AFTER_UPDATE, context)) {
					setupHandleAfterCommit(entity, EventType.AFTER_UPDATE, context);
				}
			}
		}

		@Override
		public boolean handleBeforeDelete(Entity entity, EntityEventContext context) {
			if (isNotifyBeforeDelete) {
				// 通知する条件が未設定の場合、通知します。
				if (StringUtil.isEmpty(notificationCondScript) || callScript(entity, EventType.BEFORE_DELETE, context)) {
					setupHandleAfterCommit(entity, EventType.BEFORE_DELETE, context);
				}
			}
			return true;
		}

		@Override
		public boolean handleBeforeInsert(Entity entity, EntityEventContext context) {
			if (isNotifyBeforeInsert) {
				// 通知する条件が未設定の場合、通知します。
				if (StringUtil.isEmpty(notificationCondScript) || callScript(entity, EventType.BEFORE_INSERT, context)) {
					setupHandleAfterCommit(entity, EventType.BEFORE_INSERT, context);
				}
			}
			return true;
		}

		@Override
		public boolean handleBeforeUpdate(Entity entity, EntityEventContext context) {
			if (isNotifyBeforeUpdate) {
				// 通知する条件が未設定の場合、通知します。
				if (StringUtil.isEmpty(notificationCondScript) || callScript(entity, EventType.BEFORE_UPDATE, context)) {
					setupHandleAfterCommit(entity, EventType.BEFORE_UPDATE, context);
				}
			}
			return true;
		}

		@Override
		public void handleOnLoad(Entity entity) {
			if (isNotifyOnLoad) {
				// 通知する条件が未設定の場合、通知します。
				if (StringUtil.isEmpty(notificationCondScript) || callScript(entity, EventType.ON_LOAD, null)) {
					setupHandleAfterCommit(entity, EventType.ON_LOAD, null);
				}
			}
		}

		@Override
		public void handleBeforeValidate(Entity entity, EntityEventContext context) {
			if (isNotifyBeforeValidate) {
				// 通知する条件が未設定の場合、通知します。
				if (StringUtil.isEmpty(notificationCondScript) || callScript(entity, EventType.BEFORE_VALIDATE, context)) {
					setupHandleAfterCommit(entity, EventType.BEFORE_VALIDATE, context);
				}
			}
		}

		@Override
		public void handleAfterRestore(Entity entity) {
			if (isNotifyAfterRestore) {
				// 通知する条件が未設定の場合、通知します。
				if (StringUtil.isEmpty(notificationCondScript) || callScript(entity, EventType.AFTER_RESTORE, null)) {
					setupHandleAfterCommit(entity, EventType.AFTER_RESTORE, null);
				}
			}
		}

		@Override
		public void handleAfterPurge(Entity entity) {
			if (isNotifyAfterPurge) {
				// 通知する条件が未設定の場合、通知します。
				if (StringUtil.isEmpty(notificationCondScript) || callScript(entity, EventType.AFTER_PURGE, null)) {
					setupHandleAfterCommit(entity, EventType.AFTER_PURGE, null);
				}
			}
		}

		public MetaSendNotificationEventListener getMetaData() {
			return MetaSendNotificationEventListener.this;
		}

		protected Map<String, Object> generateBindings(Entity entity, EventType type, EntityEventContext context) {
			ExecuteContext ex = ExecuteContext.getCurrentContext();

			Map<String, Object> bindings = new HashMap<String, Object>();
			bindings.put(TENANT_BINDING_NAME, ex.getCurrentTenant());
			bindings.put(ENTITY_BINDING_NAME, entity);
			bindings.put(EVENT_TYPE_BINDING_NAME, type);
			bindings.put(CONTEXT_BINDING_NAME, context);
			bindings.put(USER_BINDING_NAME, AuthContextHolder.getAuthContext().newUserBinding());
			bindings.put(DATE_BINGING_NAME, ex.getCurrentTimestamp());
			return bindings;
		}
	}

	public class SendMailNotificationEventListenerHandler extends SendNotificationListenerEventHandler {

		private MailManager mm = ManagerLocator.getInstance().getManager(MailManager.class);

		public SendMailNotificationEventListenerHandler(MetaEntity entity) {
			super(entity);
		}

		@Override
		protected Object createNotification(Entity entity, EventType type, EntityEventContext context) {
			Map<String, Object> bindings = generateBindings(entity, type, context);
			return mm.createMail(tmplDefName, bindings);
		}

		@Override
		protected void sendNotification(Object mail) {
			mm.sendMail((Mail) mail);
		}
	}

	public class SendSMSNotificationEventListenerHandler extends SendNotificationListenerEventHandler {

		private SmsMailManager smm = ManagerLocator.getInstance().getManager(SmsMailManager.class);

		public SendSMSNotificationEventListenerHandler(MetaEntity entity) {
			super(entity);
		}

		@Override
		protected Object createNotification(Entity entity, EventType type, EntityEventContext context) {
			Map<String, Object> bindings = generateBindings(entity, type, context);
			return smm.createMail(tmplDefName, bindings);
		}

		@Override
		protected void sendNotification(Object mail) {
			smm.sendMail((SmsMail) mail);
		}
	}

	public class SendPushNotificationEventListenerHandler extends SendNotificationListenerEventHandler {

		private PushNotificationManager pm = ManagerLocator.getInstance().getManager(PushNotificationManager.class);

		public SendPushNotificationEventListenerHandler(MetaEntity entity) {
			super(entity);
		}

		@Override
		protected Object createNotification(Entity entity, EventType type, EntityEventContext context) {
			Map<String, Object> bindings = generateBindings(entity, type, context);
			return pm.createNotification(tmplDefName, bindings);
		}

		@Override
		protected void sendNotification(Object mail) {
			pm.push((PushNotification) mail);
		}
	}
	
	public class SendWebHookNotificationEventListenerHandler extends SendNotificationListenerEventHandler {

		private WebHookManager wm = ManagerLocator.getInstance().getManager(WebHookManager.class);

		public SendWebHookNotificationEventListenerHandler(MetaEntity entity) {
			super(entity);
		}

		@Override
		protected Object createNotification(Entity entity, EventType type, EntityEventContext context) {
			Map<String, Object> bindings = generateBindings(entity, type, context);
			WebHook wh = wm.createWebHook(tmplDefName, bindings);
			wh.setResultHandler(webHookResultHandlerDef);
			wh.setSynchronous(isSynchronous);
			wh.setEndPoints((ArrayList<String>)endPointDefList);
			
			return wh;
		}

		@Override
		protected void sendNotification(Object webHook) {
			wm.sendWebHook((WebHook)webHook);
		}
	}
}
