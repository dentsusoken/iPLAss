package org.iplass.gem.command.generic.detail;

import java.util.ArrayList;
import java.util.List;

/**
 * カスタム登録処理におけるNestTableの更新制御オプション
 * @author Y.Ishida
 *
 */
public class NestTableRegistOption {
	/** Reference項目として更新対象に指定されたか */
	private boolean specifiedAsReference;

	/** 更新対象のNestProperty */
	private List<String> updateNestProperty = new ArrayList<String>();

	public boolean isSpecifiedAsReference() {
		return specifiedAsReference;
	}

	public void setSpecifiedAsReference(boolean specifiedAsReference) {
		this.specifiedAsReference = specifiedAsReference;
	}

	public List<String> getUpdateNestProperty() {
		return updateNestProperty;
	}

	public void setUpdateNestProperty(List<String> updateNestProperty) {
		this.updateNestProperty = updateNestProperty;
	}

}
