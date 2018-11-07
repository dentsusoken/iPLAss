package org.iplass.gem.command.generic.detail;

import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.element.property.PropertyBase;

public interface RegistrationPropertyBaseHandler<T extends PropertyBase> {

	public boolean isHidden(T property);

	public PropertyEditor getEditor(T property);
}
