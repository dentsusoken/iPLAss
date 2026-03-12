/*
 * Copyright (C) 2026 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.entity.normalizer;

import org.iplass.mtp.entity.ValidationContext;
import org.iplass.mtp.entity.definition.NormalizerDefinition;
import org.iplass.mtp.entity.definition.normalizers.HtmlSanitize;
import org.iplass.mtp.entity.definition.normalizers.HtmlSanitizePolicy;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.entity.property.MetaProperty;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;

public class MetaHtmlSanitize extends MetaNormalizer {
	private static final long serialVersionUID = 7103458921654823710L;

	private HtmlSanitizePolicy policy;

	public HtmlSanitizePolicy getPolicy() {
		return policy;
	}

	public void setPolicy(HtmlSanitizePolicy policy) {
		this.policy = policy;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((policy == null) ? 0 : policy.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MetaHtmlSanitize other = (MetaHtmlSanitize) obj;
		if (policy != other.policy)
			return false;
		return true;
	}

	@Override
	public MetaHtmlSanitize copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public void applyConfig(NormalizerDefinition definition) {
		this.policy = ((HtmlSanitize) definition).getPolicy();
	}

	@Override
	public HtmlSanitize currentConfig(EntityContext context) {
		return new HtmlSanitize(policy);
	}

	@Override
	public NormalizerRuntime createRuntime(MetaEntity entity, MetaProperty property) {
		return new HtmlSanitizeRuntime(property);
	}

	private static Safelist toSafelist(HtmlSanitizePolicy policy) {
		if (policy == null) {
			return Safelist.none();
		}
		switch (policy) {
		case NONE:
			return Safelist.none();
		case SIMPLE_TEXT:
			return Safelist.simpleText();
		case BASIC:
			return Safelist.basic();
		case BASIC_WITH_IMAGES:
			return Safelist.basicWithImages();
		case RELAXED:
			return Safelist.relaxed();
		default:
			return Safelist.none();
		}
	}

	public class HtmlSanitizeRuntime extends NormalizerRuntime {

		private final Safelist safelist;
		private final Document.OutputSettings outputSettings;

		HtmlSanitizeRuntime(MetaProperty property) {
			if (policy == null) {
				throw new NullPointerException(property.getName() + "'s HtmlSanitize policy must be specified");
			}
			this.safelist = toSafelist(policy);
			this.outputSettings = new Document.OutputSettings().prettyPrint(false);
		}

		@Override
		public Object normalize(Object value, ValidationContext context) {
			if (value == null) {
				return null;
			}

			String html = value.toString();
			if (html.isEmpty()) {
				return html;
			}

			return Jsoup.clean(html, "", safelist, outputSettings);
		}
	}
}
