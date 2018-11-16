package org.iplass.gem.command.generic.bulk;

import org.iplass.gem.command.generic.detail.RegistrationCommandBase;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.view.generic.element.property.PropertyColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BulkCommandBase extends RegistrationCommandBase<BulkCommandContext, PropertyColumn> {

	private static Logger logger = LoggerFactory.getLogger(BulkCommandBase.class);

	@Override
	protected Logger getLogger() {
		return logger;
	}

	@Override
	protected BulkCommandContext getContext(RequestContext request) {
		BulkCommandContext context = new BulkCommandContext(request, em, edm);
		context.setEntityDefinition(edm.get(context.getDefinitionName()));
		context.setEntityView(evm.get(context.getDefinitionName()));

		return context;
	}

}
