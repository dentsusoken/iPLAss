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

package org.iplass.mtp.impl.entity.auditlog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
	private Map<String, PropertyMaskTarget> maskTargetMap;
	private boolean hasEntityWildcard;

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

		// マスクの設定がある場合、マスク対象を集約する
		if (config.getValues("maskTarget", MaskTarget.class) != null) {
			List<MaskTarget> maskTarget = config.getValues("maskTarget", MaskTarget.class);
			Map<String, List<MaskTarget>> entityGroupingMap = maskTarget.stream()
					.collect(Collectors.groupingBy(MaskTarget::getEntity));

			maskTargetMap = new HashMap<>();
			for (String entityName : entityGroupingMap.keySet()) {
				Map<String, LogMaskHandler> propertyMaskMap = new HashMap<>();
				PropertyMaskTarget propertyMaskTarget = new PropertyMaskTarget();
				List<MaskTarget> targetEntityList = entityGroupingMap.get(entityName);
				targetEntityList.forEach(target -> propertyMaskMap.put(target.getProperty(), target.getMaskHandler()));
				propertyMaskTarget.propertyMap = propertyMaskMap;

				// ワイルドカードのPropertyがあるか
				propertyMaskTarget.hasWildcard = propertyMaskMap.containsKey("*");
				maskTargetMap.put(entityName, propertyMaskTarget);
			}

			if (maskTargetMap.containsKey("*")) {
				hasEntityWildcard = true;

				// Entityのワイルドカードがある場合、各Entityのマスク設定に追加する
				maskTargetMap.forEach((key, value) -> {
					if (!key.equals("*")) {
						PropertyMaskTarget target = maskTargetMap.get("*");
						
						target.propertyMap.forEach((wildcardKey, wildcardValue) -> {
							// 各Entityのproperty設定を上書きしないように追加
							value.propertyMap.putIfAbsent(wildcardKey, wildcardValue);
						});

						if (target.hasWildcard) {
							value.hasWildcard = true;
						}
					}
				});
			}
		}
	}

	private class PropertyMaskTarget {
		boolean hasWildcard;
		Map<String, LogMaskHandler> propertyMap;
	}

	public void log(String action, Object detail) {
		String str = null;
		//FIXME 制御文字を取り除く（どちらかと言うと、Entity側でのチェック制御か？）
		if (detail != null) {
			str = detail.toString().replace("\n", "\\n").replace("\r", "\\r");
		}
		logger.info(action + "," + str);
	}

	private Object maskValue(String propertyName, Object value, PropertyMaskTarget propertyMaskTarget) {
		if (value == null || propertyMaskTarget == null) {
			return value;
		}

		Map<String, LogMaskHandler> propertyMap = propertyMaskTarget.propertyMap;
		LogMaskHandler maskHandler = null;

		maskHandler = propertyMap.get(propertyName);
		if (maskHandler == null && propertyMaskTarget.hasWildcard) {
			maskHandler = propertyMap.get("*");
		}

		if (maskHandler == null) {
			return value;
		}

		return maskHandler.mask(value.toString());
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

			PropertyMaskTarget propertyMaskTarget = null;
			if (maskTargetMap != null) {
				propertyMaskTarget = maskTargetMap.get(entity.getDefinitionName());
				if (propertyMaskTarget == null && hasEntityWildcard) {
					propertyMaskTarget = maskTargetMap.get("*");
				}
			}

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
							cutAppend(sb, maskValue(key.getName(), ((GenericEntity) valArray[i]).getName(), propertyMaskTarget));
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
								cutAppend(sb, maskValue(key.getName(), toLogVal, propertyMaskTarget));
								sb.append("\"");
							} else {
								sb.append(maskValue(key.getName(), toLogVal, propertyMaskTarget));
							}
						}
					}
					sb.append("]");
				} else if (val instanceof GenericEntity) {
					sb.append("{\"oid\":\"").append(((GenericEntity) val).getOid()).append("\",\"name\":\"");
					cutAppend(sb, maskValue(key.getName(), ((GenericEntity) val).getName(), propertyMaskTarget));
					sb.append("\"}");
				} else {
					if (pt != null) {
						val = pt.formatToLog(val);
					}
					if (val instanceof String) {
						sb.append("\"");
						cutAppend(sb, maskValue(key.getName(), val, propertyMaskTarget));
						sb.append("\"");
					} else {
						sb.append(maskValue(key.getName(), val, propertyMaskTarget));
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
