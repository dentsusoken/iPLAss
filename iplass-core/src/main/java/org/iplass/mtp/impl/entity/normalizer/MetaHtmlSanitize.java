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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.iplass.mtp.entity.ValidationContext;
import org.iplass.mtp.entity.definition.NormalizerDefinition;
import org.iplass.mtp.entity.definition.normalizers.HtmlSanitizer;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.entity.property.MetaProperty;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;

public class MetaHtmlSanitize extends MetaNormalizer {
	private static final long serialVersionUID = 7103458921654823710L;

	private List<String> allowTags = new ArrayList<>();

	public List<String> getAllowTags() {
		return allowTags;
	}

	public void setAllowTags(List<String> allowTags) {
		if (allowTags == null) {
			throw new IllegalArgumentException("allowTags cannot be null");
		}
		this.allowTags = allowTags;
	}

	@Override
	public int hashCode() {
		return Objects.hash(allowTags);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		MetaHtmlSanitize other = (MetaHtmlSanitize) obj;
		return Objects.equals(allowTags, other.allowTags);
	}

	@Override
	public MetaHtmlSanitize copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public void applyConfig(NormalizerDefinition definition) {
		this.allowTags = ((HtmlSanitizer) definition).getAllowTags();
	}

	@Override
	public HtmlSanitizer currentConfig(EntityContext context) {
		return new HtmlSanitizer(allowTags);
	}

	@Override
	public NormalizerRuntime createRuntime(MetaEntity entity, MetaProperty property) {
		return new HtmlSanitizeRuntime();
	}

	public class HtmlSanitizeRuntime extends NormalizerRuntime {

		private final Safelist safelist;
		private final Document.OutputSettings outputSettings;

		HtmlSanitizeRuntime() {
			Safelist sl = Safelist.none();
			sl.addTags(allowTags.toArray(new String[0]));
			this.safelist = sl;
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
