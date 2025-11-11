package org.iplass.mtp.view.generic.element.section;

import org.iplass.adminconsole.view.annotation.FieldOrder;
import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSeeAlso;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({ TemplateSection.class, DefaultSection.class, ScriptingSection.class, VersionSection.class,
		SearchConditionSection.class, ReferenceSection.class, MassReferenceSection.class })
@FieldOrder(manual = true)
public abstract class AdjustableHeightSection extends Section {
	
	/** シリアルバージョンUID */
	private static final long serialVersionUID = -6145648008070643845L;

//	/** セクション高さ調整可否設定 */
//	@MetaFieldInfo(
//			displayName = "セクション高さ調整可否設定",
//			displayNameKey = "generic_element_section_Section_adjustableHeightDisplayNameKey",
//			inputType = InputType.CHECKBOX,
//			displayOrder = 340,
//			description = "セクションの高さをユーザーが調整できるかどうかを設定します。",
//			descriptionKey = "generic_element_section_Section_adjustableHeightDescriptionKey"
//	)
//	private boolean adjustableHeight;

	/** セクション高さ設定（px） */
	@MetaFieldInfo(
			displayName = "セクション高さ設定（px）",
			displayNameKey = "generic_element_section_Section_sectionHeightDisplayNameKey",
			inputType = InputType.NUMBER,
			displayOrder = 350,
			description = "セクションの高さをピクセル単位で指定します。",
			descriptionKey = "generic_element_section_Section_sectionHeightDescriptionKey"
	)
	private int sectionHeight;

//	public boolean isAdjustableHeight() {
//		return adjustableHeight;
//	}
//
//	public void setAdjustableHeight(boolean adjustableHeight) {
//		this.adjustableHeight = adjustableHeight;
//	}
	
	public int getSectionHeight() {
		return sectionHeight;
	}

	public void setSectionHeight(int sectionHeight) {
		this.sectionHeight = sectionHeight;
	}

}
