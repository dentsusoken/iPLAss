package org.iplass.mtp.impl.information;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityEventContext;
import org.iplass.mtp.entity.EntityEventListener;
import org.iplass.mtp.impl.core.ExecuteContext;

public class InformationEntityEventListener implements EntityEventListener {

	public InformationEntityEventListener() {
	}

	@Override
	public boolean beforeInsert(Entity entity, EntityEventContext context) {
		if (entity.getEndDate() == null) {
			entity.setEndDate(ExecuteContext.getCurrentContext().getDefaultEndDate());
		}
		return true;
	}
}
