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

import java.io.IOException;
import java.io.StringWriter;
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
import org.iplass.mtp.impl.script.GroovyScriptEngine;
import org.iplass.mtp.impl.script.Script;
import org.iplass.mtp.impl.script.ScriptContext;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.impl.script.template.GroovyTemplate;
import org.iplass.mtp.impl.script.template.GroovyTemplateBinding;
import org.iplass.mtp.impl.script.template.GroovyTemplateCompiler;
import org.iplass.mtp.impl.webhook.DefaultWebhookResponseHandler;
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
import org.iplass.mtp.webhook.Webhook;
import org.iplass.mtp.webhook.WebhookManager;
import org.iplass.mtp.webhook.WebhookResponseHandler;
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
	private boolean sendTogether;
	private List<String> destinationList;


	/**ウェッブフックだけの設定項目*/
	private boolean synchronous;
	private String responseHandler;

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

	public List<String> getDestinationList() {
		return destinationList;
	}

	public void setDestinationList(List<String> destinationList) {
		this.destinationList = destinationList;
	}

	public String getResponseHandler() {
		return responseHandler;
	}

	public void setResponseHandler(String responseHandler) {
		this.responseHandler = responseHandler;
	}

	public boolean isSynchronous() {
		return synchronous;
	}

	public void setSynchronous(boolean isSynchronous) {
		this.synchronous = isSynchronous;
	}

	public boolean isSendTogether() {
		return sendTogether;
	}

	public void setSendTogether(boolean isSendTogether) {
		this.sendTogether = isSendTogether;
	}

	@Override
	public MetaEventListener copy() {
		MetaSendNotificationEventListener copy = new MetaSendNotificationEventListener();
		copyTo(copy);
		copy.notificationType = notificationType;
		copy.tmplDefName = tmplDefName;
		copy.notificationCondScript = notificationCondScript;
		copy.responseHandler = responseHandler;
		copy.setSendTogether(sendTogether);
		if (listenEvent != null) {
			copy.listenEvent = new ArrayList<EventType>();
			copy.listenEvent.addAll(listenEvent);
		}
		copy.setSynchronous(synchronous);
		if (destinationList !=null) {
			copy.destinationList = new ArrayList<>(destinationList );
			copy.destinationList.addAll(destinationList);
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
		result = prime * result + ((sendTogether) ? 1231 : 1237);
		result = prime * result + ((destinationList == null) ? 0 : destinationList.hashCode());
		result = prime * result + ((synchronous) ? 1231 : 1237);
		result = prime * result + ((responseHandler == null) ? 0 : responseHandler.hashCode());
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
			
		} else if (!synchronous==other.synchronous) {
			return false;
		} else if (destinationList==null) {
			if (other.destinationList!=null) {
				return false;
			}
		} else if(!destinationList.equals(other.destinationList)) {
			return false;
		} else if (responseHandler==null) {
			if (other.responseHandler!=null) {
				return false;
			}
		} else if (!responseHandler.equals(other.responseHandler)) {
			return false;
		} else if (!sendTogether==other.sendTogether) {
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
		responseHandler = d.getResponseHandler();
		synchronous = d.isSynchronous();
		sendTogether = d.isSendTogether();
		if (d.getListenEvent() != null) {
			listenEvent = new ArrayList<EventType>();
			listenEvent.addAll(d.getListenEvent());
		} else {
			// listenEventをnullにクリアする
			listenEvent = null;
		}
		if (d.getDestinationList() != null) {
			destinationList = new ArrayList<String>();
			destinationList.addAll(d.getDestinationList());
		} else {
			destinationList = null;
		}

	}

	@Override
	public EventListenerDefinition currentConfig() {
		SendNotificationEventListenerDefinition d = new SendNotificationEventListenerDefinition();
		fillTo(d);
		d.setNotificationType(notificationType);
		d.setTmplDefName(tmplDefName);
		d.setNotificationCondScript(notificationCondScript);
		d.setResponseHandler(responseHandler);
		d.setSendTogether(sendTogether);
		
		if (listenEvent != null) {
			List<EventType> es = new ArrayList<EventType>();
			es.addAll(listenEvent);
			d.setListenEvent(es);
		}
		d.setSynchronous(synchronous);
		if (destinationList != null) {
			List<String> es = new ArrayList<String>();
			es.addAll(destinationList);
			d.setDestinationList(es);
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
			return new SendWebhookNotificationEventListenerHandler(entity);
		}
		return null;
	}

	private abstract class SendNotificationListenerEventHandler extends EventListenerRuntime {

		private static final String SCRIPT_PREFIX = "SendNotificationListenerEventHandler_notificationCondScript";
		private static final String DESTINATION_PREFIX = "SendNotificationListenerEventHandler_destinationList";

		private Script compiledScript;
		private ScriptEngine scriptEngine;
		private List<GroovyTemplate> destinationTemplateList;

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

			destinationTemplateList = createDestinationTemplate(destinationList, entity);
			
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

		protected List<String> processDestinationGroovyTemplate(Map<String, Object> bindings) {
			List<String> processedDestinationList = new ArrayList<String>();
			if (destinationTemplateList == null) {
				return processedDestinationList;
			}
			for (GroovyTemplate destinationTemplate : destinationTemplateList) {
				StringWriter sw = new StringWriter();
				GroovyTemplateBinding gtb = new GroovyTemplateBinding(sw, bindings);
				try {
					destinationTemplate.doTemplate(gtb);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				String temp = sw.toString();
				if (temp != null && !temp.replaceAll("\\s+", "").isEmpty()) {
					processedDestinationList.add(temp);
				}
			}
			return processedDestinationList;
		}

		private List<GroovyTemplate> createDestinationTemplate(List<String> destinationList, MetaEntity entity){
			List<GroovyTemplate> templateList = new ArrayList<GroovyTemplate>();
			if (destinationList == null) {
				return templateList;
			}
			String entityName = new String(entity.getName());
			
			//何番目のeventlisterのかを計算
			int i = 0;
			for (; i < entity.getEventListenerList().size(); i++) {
				if (MetaSendNotificationEventListener.this == entity.getEventListenerList().get(i)) {
					break;
				}
			}
			for (int j = 0; j< destinationList.size(); j++) {
				String script = destinationList.get(j);
				ScriptEngine se = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();
				GroovyTemplate destinationTemplate = GroovyTemplateCompiler.compile(script, DESTINATION_PREFIX + "_Entity_" + entityName + "_listener" + i + "_destination" + j, (GroovyScriptEngine) se);
				templateList.add(destinationTemplate);
			}
			return templateList;
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
			List<String> processedDestinationList = processDestinationGroovyTemplate(bindings);
			List<Mail> mailList= new ArrayList<Mail>();
			if(sendTogether) {
				Mail mail = mm.createMail(tmplDefName, bindings);
				for (String destination : processedDestinationList) {
					mail.addRecipientTo(destination, "");
				}
				mailList.add(mail);
			} else {
				for (String destination : processedDestinationList) {
					Mail mail = mm.createMail(tmplDefName, bindings);
					mail.addRecipientTo(destination, "");
					mailList.add(mail);
				}
			}
			return mailList;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void sendNotification(Object mail) {
			for (Mail m : (List<Mail>) mail) {
				mm.sendMail(m);
			}
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
			List<String> processedDestinationList = processDestinationGroovyTemplate(bindings);
			List<SmsMail> smsMailList = new ArrayList<SmsMail>();
			for (String desination : processedDestinationList) {
				SmsMail smsMail = smm.createMail(tmplDefName, bindings);
				smsMail.setTo(desination);
				smsMailList.add(smsMail);
			}
			return smsMailList;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void sendNotification(Object mail) {
			for (SmsMail m : (List<SmsMail>) mail) {
				smm.sendMail((SmsMail) m);
			}
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
			List<PushNotification> pushNotificationList= new ArrayList<PushNotification>();
			List<String> processedDestinationList = processDestinationGroovyTemplate(bindings);
			if(sendTogether) {
				PushNotification pushNotification = pm.createNotification(tmplDefName, bindings);
				for (String destination : processedDestinationList) {
					pushNotification.addTo(destination);
				}
				pushNotificationList.add(pushNotification);
			} else {
				for (String destination : processedDestinationList) {
					PushNotification pushNotification = pm.createNotification(tmplDefName, bindings);
					pushNotification.addTo(destination);
					pushNotificationList.add(pushNotification);
				}
			}
			return pushNotificationList;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void sendNotification(Object mail) {
			for (PushNotification m : (List<PushNotification>) mail) {
				pm.push(m);
			}
		}
	}

	public class SendWebhookNotificationEventListenerHandler extends SendNotificationListenerEventHandler {

		private WebhookManager wm = ManagerLocator.getInstance().getManager(WebhookManager.class);

		public SendWebhookNotificationEventListenerHandler(MetaEntity entity) {
			super(entity);
		}
		@Override
		protected Object createNotification(Entity entity, EventType type, EntityEventContext context) {
			Map<String, Object> bindings = generateBindings(entity, type, context);
			List<String> processedDestinationList = processDestinationGroovyTemplate(bindings);
			ArrayList<Webhook> webhookList = new ArrayList<Webhook>();
			for (String endpoint : processedDestinationList) {
				Webhook webhook = wm.createWebhook(tmplDefName, bindings, endpoint);
				WebhookResponseHandler whrh;
				if (responseHandler==null||responseHandler.isEmpty()) {
					whrh= new DefaultWebhookResponseHandler();
				} else {
					try {
						whrh= (WebhookResponseHandler) Class.forName(responseHandler).newInstance();
					} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
						whrh= new DefaultWebhookResponseHandler();
					}
				}
				webhook.setResponseHandler(whrh);
				webhookList.add(webhook);
			}
			return webhookList;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void sendNotification(Object webhookList) {
			//call async and sync
			if (synchronous) {
				for (Webhook wh : (List<Webhook>)webhookList) {
					wm.sendWebhookSync(wh);
				}
			} else {
				for (Webhook wh : (List<Webhook>)webhookList) {
					wm.sendWebhookAsync(wh);
				}
			}
		}
	}
}
