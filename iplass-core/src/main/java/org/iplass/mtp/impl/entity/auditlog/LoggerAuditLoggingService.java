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

package org.iplass.mtp.impl.entity.auditlog;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.DeleteCondition;
import org.iplass.mtp.entity.DeleteOption;
import org.iplass.mtp.entity.DeleteTargetVersion;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.GenericEntity;
import org.iplass.mtp.entity.UpdateCondition;
import org.iplass.mtp.entity.UpdateOption;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.EntityService;
import org.iplass.mtp.impl.entity.property.PrimitivePropertyHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.entity.property.PropertyType;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LoggerAuditLoggingService implements AuditLoggingService {

	//TODO カテゴリ名を設定可能に
	private static Logger logger = LoggerFactory.getLogger("mtp.audit");

	private boolean logCompact;
	private int textMaxLength = 256;
	private boolean logQuery;
	private boolean logSelectValueWithLabel;
	private boolean logReferenceWithLabel;
	private LogMaskHandler logMaskHandler;

	public boolean isLogQuery() {
		return logQuery;
	}

	public boolean isLogCompact() {
		return logCompact;
	}

	public boolean isLogSelectValueWithLabel() {
		return logSelectValueWithLabel;
	}

	public boolean isLogReferenceWithLabel() {
		return logReferenceWithLabel;
	}

	public int getTextMaxLength() {
		return textMaxLength;
	}

	@Override
	public void destroy() {
	}

	@Override
	public void init(Config config) {
		if ("TRUE".equalsIgnoreCase(config.getValue("logQuery"))) {
			logQuery = true;
		} else {
			logQuery = false;
		}
		if ("TRUE".equalsIgnoreCase(config.getValue("logCompact"))) {
			logCompact = true;
		} else {
			logCompact = false;
		}
		if ("TRUE".equalsIgnoreCase(config.getValue("logSelectValueWithLabel"))) {
			logSelectValueWithLabel = true;
		} else {
			logSelectValueWithLabel = false;
		}
		if ("TRUE".equalsIgnoreCase(config.getValue("logReferenceWithLabel"))) {
			logReferenceWithLabel = true;
		} else {
			logReferenceWithLabel = false;
		}
		if (config.getValue("cutSize") != null) {
			textMaxLength = Integer.parseInt(config.getValue("textMaxLength"));
		}

		logMaskHandler = (LogMaskHandler) config.getValue("logMaskHandler", LogMaskHandler.class);
	}

	public void log(String action, Object detail) {
		String str = null;
		//FIXME 制御文字を取り除く（どちらかと言うと、Entity側でのチェック制御か？）
		if (detail != null) {
			str = detail.toString().replace("\n", "\\n").replace("\r", "\\r");
		}
		logger.info(action + "," + str);
	}

	private Object maskValue(String definitionName, String keyName,  Object target) {
		if (target == null || logMaskHandler == null) {
			return target;
		}
		return logMaskHandler.maskingProperty(definitionName, keyName, target.toString());
	}

	private void cutAppend(StringBuilder sb, Object target) {
		if (target == null) {
			sb.append(target);
		} else {
			String s = target.toString();
			if (logCompact) {
				if (s.length() > textMaxLength) {
					s = s.substring(0, textMaxLength);
					sb.append(s);
					sb.append("...");
				}
			} else {
				sb.append(s);
			}
		}
	}

	private StringBuilder toLogFormat(Entity entity, List<PropertyHandler> logProps) {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"definitionName\":\"").append(entity.getDefinitionName()).append("\"");
		sb.append(",\"oid\":\"").append(entity.getOid()).append("\"");
		if (logProps != null) {
			for (PropertyHandler key: logProps) {
				sb.append(",");
				sb.append("\"").append(key.getName()).append("\":");
				PropertyType pt = null;
				if (key instanceof PrimitivePropertyHandler) {
					pt = ((PrimitivePropertyHandler) key).getMetaData().getType();
				}

				Object val = entity.getValue(key.getName());
				if (val instanceof Object[]) {
					Object[] valArray = (Object[]) val;
					sb.append("[");
					for (int i = 0; i < valArray.length; i++) {
						if (i != 0) {
							sb.append(",");
						}
						if (valArray[i] instanceof GenericEntity) {
							sb.append("{\"oid\":\"").append(((GenericEntity) valArray[i]).getOid()).append("\",\"name\":\"");
							cutAppend(sb, maskValue(entity.getDefinitionName(), key.getName(), ((GenericEntity) valArray[i]).getName()));
							sb.append("\"}");
						} else {
							Object toLogVal;
							if (pt != null) {
								toLogVal = pt.formatToLog(valArray[i]);
							} else {
								toLogVal = valArray[i];
							}
							if (toLogVal instanceof String) {
								sb.append("\"");
								cutAppend(sb, maskValue(entity.getDefinitionName(), key.getName(),toLogVal));
								sb.append("\"");
							} else {
								sb.append(toLogVal);
							}
						}
					}
					sb.append("]");
				} else if (val instanceof GenericEntity) {
					sb.append("{\"oid\":\"").append(((GenericEntity) val).getOid()).append("\",\"name\":\"");
					cutAppend(sb, maskValue(entity.getDefinitionName(), key.getName(),((GenericEntity) val).getName()));
					sb.append("\"}");
				} else {
					if (pt != null) {
						val = pt.formatToLog(val);
					}
					if (val instanceof String) {
						sb.append("\"");
						cutAppend(sb, maskValue(entity.getDefinitionName(), key.getName(),(String) val));
						sb.append("\"");
					} else {
						sb.append(val);
					}
				}
			}
		}
		sb.append("}");
		return sb;
	}

	@Override
	public void logInsert(Entity entity) {
		EntityHandler eh = ServiceRegistry.getRegistry().getService(EntityService.class).getRuntimeByName(entity.getDefinitionName());
		if (eh != null) {
			log(ACTION_INSERT, toLogFormat(entity, eh.getPropertyList(EntityContext.getCurrentContext())));
		}
	}

	@Override
	public void logDelete(Entity entity, DeleteOption option) {
		String oid = entity.getOid();
		String defName = entity.getDefinitionName();
		StringBuilder sb = new StringBuilder();
		sb.append("{\"definitionName\":\"").append(defName).append("\"");
		sb.append(",\"oid\":\"").append(oid).append("\"");
		if (option.getTargetVersion() == DeleteTargetVersion.SPECIFIC) {
			sb.append(",\"version\":").append(entity.getVersion());
		}
		sb.append("}");
		log(ACTION_DELETE, sb);
	}

	@Override
	public void logDeleteAll(DeleteCondition cond) {
		log(ACTION_DELETE_ALL, cond);
	}

	@Override
	public boolean isLogBeforeEntity(String definitionName) {
		return false;
	}

	@Override
	public void logUpdate(Entity beforeEntity, Entity entity, UpdateOption option) {
		EntityHandler eh = ServiceRegistry.getRegistry().getService(EntityService.class).getRuntimeByName(entity.getDefinitionName());
		if (eh != null) {
			List<PropertyHandler> props = new ArrayList<>();
			if (option.getUpdateProperties() != null) {
				EntityContext ec = EntityContext.getCurrentContext();
				for (String up: option.getUpdateProperties()) {
					//updateByはこの段階で、更新前の値なので、混乱を避けるため出力しない
					if (!Entity.UPDATE_BY.equals(up)) {
						props.add(eh.getProperty(up, ec));
					}
				}
			}
			StringBuilder sb = toLogFormat(entity, props);
			log(ACTION_UPDATE, sb);
		}
	}

	@Override
	public void logUpdateAll(UpdateCondition cond) {
		log(ACTION_UPDATE_ALL, cond);
	}

	@Override
	public void logPurge(Long rbid) {
		log(ACTION_PURGE, "{\"recycleBinId\":" + rbid + "}");
	}

	@Override
	public void logRestore(String oid, String defName, Long rbid) {
		log(ACTION_RESTORE, "{\"definitionName\":\"" + defName + "\",\"oid\":\"" + oid + "\",\"recycleBinId\":" + rbid + "}");
	}

	@Override
	public void logBulkUpdate(String defName) {
		log(ACTION_BULK_UPDATE, defName);
	}

	@Override
	public void logQuery(Query query, boolean isCount) {
		if (logQuery) {
			if (isCount) {
				log(ACTION_COUNT_QUERY, query);
			} else {
				log(ACTION_QUERY, query);
			}
		}
	}

}
