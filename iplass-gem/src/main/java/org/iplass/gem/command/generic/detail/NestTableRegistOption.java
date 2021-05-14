package org.iplass.gem.command.generic.detail;

import java.util.ArrayList;
import java.util.List;

/**
 * カスタム登録処理によるNestTableの更新制御オプション
 * @author Y.Ishida
 *
 */
public class NestTableRegistOption {
	/** 更新対象の範囲 */
	private boolean isSpecifyAllProperties;

	/** Reference項目として更新対象に指定されたか */
	private boolean specifiedAsReference;

	/** 更新対象として個別指定のあったネストプロパティ */
	private List<String> specifiedUpdateNestProperties = new ArrayList<String>();

	public boolean isSpecifyAllProperties() {
		return isSpecifyAllProperties;
	}

	public void setSpecifyAllProperties(boolean isSpecifyAllProperties) {
		this.isSpecifyAllProperties = isSpecifyAllProperties;
	}

	public boolean isSpecifiedAsReference() {
		return specifiedAsReference;
	}

	public void setSpecifiedAsReference(boolean specifiedAsReference) {
		this.specifiedAsReference = specifiedAsReference;
	}

	public List<String> getSpecifiedUpdateNestProperties() {
		return specifiedUpdateNestProperties;
	}

	public void setSpecifiedUpdateNestProperties(List<String> specifiedUpdateNestProperties) {
		this.specifiedUpdateNestProperties = specifiedUpdateNestProperties;
	}

}
