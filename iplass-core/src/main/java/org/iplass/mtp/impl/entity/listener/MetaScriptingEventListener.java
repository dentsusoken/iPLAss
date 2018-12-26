/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
import java.util.List;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityEventContext;
import org.iplass.mtp.entity.EntityEventListener;
import org.iplass.mtp.entity.definition.EventListenerDefinition;
import org.iplass.mtp.entity.definition.listeners.EventType;
import org.iplass.mtp.entity.definition.listeners.ScriptingEventListenerDefinition;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.entity.MetaEventListener;
import org.iplass.mtp.impl.script.Script;
import org.iplass.mtp.impl.script.ScriptContext;
import org.iplass.mtp.impl.script.ScriptEngine;


public class MetaScriptingEventListener extends MetaEventListener {
	private static final long serialVersionUID = -5525839977593609749L;

	public static final String ENTITY_BINDING_NAME = "entity";
	public static final String EVENT_TYPE_BINDING_NAME = "event";
	public static final String CONTEXT_BINDING_NAME = "context";
	public static final String USER_BINDING_NAME = "user";
	public static final String DATE_BINGING_NAME = "date";

	private String script;
	private List<EventType> listenEvent;

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public List<EventType> getListenEvent() {
		return listenEvent;
	}

	public void setListenEvent(List<EventType> listenEvent) {
		this.listenEvent = listenEvent;
	}

	@Override
	public MetaScriptingEventListener copy() {
		MetaScriptingEventListener copy = new MetaScriptingEventListener();
		copyTo(copy);
		copy.script = script;
		if (listenEvent != null) {
			copy.listenEvent = new ArrayList<EventType>();
			copy.listenEvent.addAll(listenEvent);
		}
		return copy;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((listenEvent == null) ? 0 : listenEvent.hashCode());
		result = prime * result + ((script == null) ? 0 : script.hashCode());
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
		MetaScriptingEventListener other = (MetaScriptingEventListener) obj;
		if (listenEvent == null) {
			if (other.listenEvent != null)
				return false;
		} else if (!listenEvent.equals(other.listenEvent))
			return false;
		if (script == null) {
			if (other.script != null)
				return false;
		} else if (!script.equals(other.script))
			return false;
		return true;
	}

	@Override
	public void applyConfig(EventListenerDefinition def) {
		fillFrom(def);
		ScriptingEventListenerDefinition d = (ScriptingEventListenerDefinition) def;
		script = d.getScript();
		if (d.getListenEvent() != null) {
			listenEvent = new ArrayList<EventType>();
			listenEvent.addAll(d.getListenEvent());
		} else {
			// listenEventをnullにクリアする
			listenEvent = null;
		}
	}

	@Override
	public EventListenerDefinition currentConfig() {
		ScriptingEventListenerDefinition d = new ScriptingEventListenerDefinition();
		fillTo(d);
		d.setScript(script);
		if (listenEvent != null) {
			List<EventType> es = new ArrayList<EventType>();
			es.addAll(listenEvent);
			d.setListenEvent(es);
		}
		return d;
	}


	@Override
	public ScriptingEventListenerHandler createRuntime(MetaEntity entity) {
		return new ScriptingEventListenerHandler(entity);
	}

	public class ScriptingEventListenerHandler extends EventListenerRuntime {

		private static final String SCRIPT_PREFIX = "ScriptingEventListenerHandler_script";

		private Script compiledScript;
		private ScriptEngine scriptEngine;
		private EntityEventListener listener;

		private boolean isAfterDelete;
		private boolean isAfterInsert;
		private boolean isAfterUpdate;
		private boolean isBeforeDelete;
		private boolean isBeforeInsert;
		private boolean isBeforeUpdate;
		private boolean isBeforeValidate;
		private boolean isOnLoad;
		private boolean isAfterRestore;
		private boolean isAfterPurge;

		public ScriptingEventListenerHandler(MetaEntity entity) {

			//TODO tenantIDの決定は、このメソッドを呼び出した際のスレッドに紐付いているテナントIDとなる。これでセキュリティ的、動作的に大丈夫か？
			TenantContext tc = ExecuteContext.getCurrentContext().getTenantContext();
			scriptEngine = tc.getScriptEngine();

			if (script != null) {
				String scriptWithImport = "import " + EventType.class.getName() + ";\n" + script;
				String scriptName = null;
				for (int i = 0; i < entity.getEventListenerList().size(); i++) {
					if (MetaScriptingEventListener.this == entity.getEventListenerList().get(i)) {
						scriptName = SCRIPT_PREFIX + "_" + entity.getId() + "_" + i;
						break;
					}
				}

				compiledScript = scriptEngine.createScript(scriptWithImport, scriptName);
				if (compiledScript.isInstantiateAs(EntityEventListener.class)) {
					listener = compiledScript.createInstanceAs(EntityEventListener.class, null);
				} else {
					listener = null;
				}

				if (listenEvent != null) {
					for (EventType type: listenEvent) {
						switch (type) {
						case AFTER_DELETE:
							isAfterDelete = true;
							break;
						case AFTER_INSERT:
							isAfterInsert = true;
							break;
						case AFTER_UPDATE:
							isAfterUpdate = true;
							break;
						case BEFORE_DELETE:
							isBeforeDelete = true;
							break;
						case BEFORE_INSERT:
							isBeforeInsert = true;
							break;
						case BEFORE_UPDATE:
							isBeforeUpdate = true;
							break;
						case BEFORE_VALIDATE:
							isBeforeValidate = true;
							break;
						case ON_LOAD:
							isOnLoad = true;
							break;
						case AFTER_RESTORE:
							isAfterRestore = true;
							break;
						case AFTER_PURGE:
							isAfterPurge = true;
						default:
							break;
						}
					}
				}
			}
		}

		private Object callScript(Entity entity, EventType type, EntityEventContext context) {
			ScriptContext sc = scriptEngine.newScriptContext();
			sc.setAttribute(ENTITY_BINDING_NAME, entity);
			sc.setAttribute(EVENT_TYPE_BINDING_NAME, type);
			sc.setAttribute(CONTEXT_BINDING_NAME, context);
			ExecuteContext ex = ExecuteContext.getCurrentContext();
			sc.setAttribute(USER_BINDING_NAME, AuthContextHolder.getAuthContext().newUserBinding());
			sc.setAttribute(DATE_BINGING_NAME, ex.getCurrentTimestamp());
			return compiledScript.eval(sc);
		}

		@Override
		public void handleAfterDelete(Entity entity, EntityEventContext context) {
			if (isAfterDelete) {
				if (listener != null) {
					listener.afterDelete(entity, context);
				} else {
					callScript(entity, EventType.AFTER_DELETE, context);
				}
			}
		}

		@Override
		public void handleAfterInsert(Entity entity, EntityEventContext context) {
			if (isAfterInsert) {
				if (listener != null) {
					listener.afterInsert(entity, context);
				} else {
					callScript(entity, EventType.AFTER_INSERT, context);
				}

			}
		}

		@Override
		public void handleAfterUpdate(Entity entity, EntityEventContext context) {
			if (isAfterUpdate) {
				if (listener != null) {
					listener.afterUpdate(entity, context);
				} else {
					callScript(entity, EventType.AFTER_UPDATE, context);
				}
			}
		}

		@Override
		public boolean handleBeforeDelete(Entity entity, EntityEventContext context) {
			if (isBeforeDelete) {
				if (listener != null) {
					return listener.beforeDelete(entity, context);
				} else {
					Object ret = callScript(entity, EventType.BEFORE_DELETE, context);
					if (ret instanceof Boolean) {
						if (!((Boolean) ret).booleanValue()) {
							return false;
						}
					}
				}
			}
			return true;
		}

		@Override
		public boolean handleBeforeInsert(Entity entity, EntityEventContext context) {
			if (isBeforeInsert) {
				if (listener != null) {
					return listener.beforeInsert(entity, context);
				} else {
					Object ret = callScript(entity, EventType.BEFORE_INSERT, context);
					if (ret instanceof Boolean) {
						if (!((Boolean) ret).booleanValue()) {
							return false;
						}
					}
				}
			}
			return true;
		}

		@Override
		public boolean handleBeforeUpdate(Entity entity, EntityEventContext context) {
			if (isBeforeUpdate) {
				if (listener != null) {
					return listener.beforeUpdate(entity, context);
				} else {
					Object ret = callScript(entity, EventType.BEFORE_UPDATE, context);
					if (ret instanceof Boolean) {
						if (!((Boolean) ret).booleanValue()) {
							return false;
						}
					}
				}
			}
			return true;
		}

		@Override
		public void handleOnLoad(Entity entity) {
			if (isOnLoad) {
				if (listener != null) {
					listener.onLoad(entity);
				} else {
					callScript(entity, EventType.ON_LOAD, null);
				}
			}
		}

		@Override
		public void handleBeforeValidate(Entity entity, EntityEventContext context) {
			if (isBeforeValidate) {
				if (listener != null) {
					listener.beforeValidate(entity, context);
				} else {
					callScript(entity, EventType.BEFORE_VALIDATE, context);
				}
			}
		}

		public MetaScriptingEventListener getMetaData() {
			return MetaScriptingEventListener.this;
		}

		@Override
		public void handleAfterRestore(Entity entity) {
			if (isAfterRestore) {
				if (listener != null) {
					listener.afterRestore(entity);
				} else {
					callScript(entity, EventType.AFTER_RESTORE, null);
				}
			}
		}

		@Override
		public void handleAfterPurge(Entity entity) {
			if (isAfterPurge) {
				if (listener != null) {
					listener.afterPurge(entity);
				} else {
					callScript(entity, EventType.AFTER_PURGE, null);
				}
			}
		}
	}

}
