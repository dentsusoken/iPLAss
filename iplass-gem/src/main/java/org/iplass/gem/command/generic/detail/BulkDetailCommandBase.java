package org.iplass.gem.command.generic.detail;

import org.iplass.mtp.command.RequestContext;

public abstract class BulkDetailCommandBase extends DetailCommandBase{

	public BulkDetailCommandBase() {
		super();
	}

	@Override
	protected BulkDetailCommandContext getContext(RequestContext request) {
		BulkDetailCommandContext context = new BulkDetailCommandContext(request, em, edm);
		context.setEntityDefinition(edm.get(context.getDefinitionName()));
		context.setEntityView(evm.get(context.getDefinitionName()));
		
		return context;
	}
}
