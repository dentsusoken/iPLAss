package org.iplass.mtp.impl.view.generic.element.section;

import org.iplass.mtp.view.generic.element.section.AdjustableHeightSection;

import jakarta.xml.bind.annotation.XmlSeeAlso;

@XmlSeeAlso({ MetaTemplateSection.class, MetaDefaultSection.class, MetaScriptingSection.class,
		MetaVersionSection.class, MetaSearchConditionSection.class,
		MetaReferenceSection.class, MetaMassReferenceSection.class })
public abstract class MetaAdjustableHeightSection extends MetaSection {

	
	/** シリアルバージョンUID */
	private static final long serialVersionUID = 6828935675180470369L;

	/** セクション高さ設定（px） */
	private int sectionHeight;

	/**
	 * セクション高さ設定（px）を取得します。
	 * @return セクション高さ設定（px）
	 */
	public int getSectionHeight() {
		return sectionHeight;
	}

	/**
	 * セクション高さ設定（px）を設定します。
	 * @param sectionHeight セクション高さ設定（px）
	 */
	public void setSectionHeight(int sectionHeight) {
		this.sectionHeight = sectionHeight;
	}

	@Override
	protected void fillFrom(org.iplass.mtp.view.generic.element.Element element, String definitionId) {
		super.fillFrom(element, definitionId);
		AdjustableHeightSection section = (AdjustableHeightSection) element;
		this.sectionHeight = section.getSectionHeight();

	}

	@Override
	protected void fillTo(org.iplass.mtp.view.generic.element.Element element, String definitionId) {
		super.fillTo(element, definitionId);
		AdjustableHeightSection section = (AdjustableHeightSection) element;
		section.setSectionHeight(this.sectionHeight);
	}
}
