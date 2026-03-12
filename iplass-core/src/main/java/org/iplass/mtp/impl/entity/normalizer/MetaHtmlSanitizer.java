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

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import org.iplass.mtp.entity.ValidationContext;
import org.iplass.mtp.entity.definition.NormalizerDefinition;
import org.iplass.mtp.entity.definition.normalizers.HtmlSanitizer;
import org.iplass.mtp.entity.definition.normalizers.SafelistType;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.entity.property.MetaProperty;
import org.iplass.mtp.impl.script.Script;
import org.iplass.mtp.impl.script.ScriptContext;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;

public class MetaHtmlSanitizer extends MetaNormalizer {
	private static final long serialVersionUID = 7103458921654823710L;

	private static final String SAFELIST_BINDING_NAME = "safelist";
	private static final String SCRIPT_PREFIX = "HtmlSanitizer_customizeScript";

	private SafelistType safelistType = SafelistType.BASIC;
	private String customizeScript;

	public SafelistType getSafelistType() {
		return safelistType;
	}

	public void setSafelistType(SafelistType safelistType) {
		this.safelistType = safelistType;
	}

	public String getCustomizeScript() {
		return customizeScript;
	}

	public void setCustomizeScript(String customizeScript) {
		this.customizeScript = customizeScript;
	}

	@Override
	public int hashCode() {
		return Objects.hash(safelistType, customizeScript);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		MetaHtmlSanitizer other = (MetaHtmlSanitizer) obj;
		return safelistType == other.safelistType
				&& Objects.equals(customizeScript, other.customizeScript);
	}

	@Override
	public MetaHtmlSanitizer copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public void applyConfig(NormalizerDefinition definition) {
		HtmlSanitizer d = (HtmlSanitizer) definition;
		this.safelistType = d.getSafelistType();
		this.customizeScript = d.getCustomizeScript();
	}

	@Override
	public HtmlSanitizer currentConfig(EntityContext context) {
		HtmlSanitizer d = new HtmlSanitizer();
		d.setSafelistType(safelistType);
		d.setCustomizeScript(customizeScript);
		return d;
	}

	@Override
	public NormalizerRuntime createRuntime(MetaEntity entity, MetaProperty property) {
		return new HtmlSanitizerRuntime(entity, property);
	}

	private static Safelist toJsoupSafelist(SafelistType type) {
		switch (type) {
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
				return Safelist.basic();
		}
	}

	private Safelist buildSafelist(MetaEntity entity, MetaProperty property) {
		Safelist safelist = toJsoupSafelist(safelistType);

		if (customizeScript == null || customizeScript.isEmpty()) {
			return safelist;
		}

		return applyCustomizeScript(safelist, entity, property);
	}

	private Safelist applyCustomizeScript(Safelist safelist, MetaEntity entity, MetaProperty property) {
		ScriptEngine scriptEngine = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();

		String scriptName = resolveScriptName(entity, property);
		Script compiledScript = scriptEngine.createScript(customizeScript, scriptName);

		ScriptContext scriptContext = scriptEngine.newScriptContext();
		scriptContext.setAttribute(SAFELIST_BINDING_NAME, safelist);
		compiledScript.eval(scriptContext);
		return safelist;
	}

	private String resolveScriptName(MetaEntity entity, MetaProperty property) {
		List<MetaNormalizer> normalizers = property.getNormalizers();
		int index = IntStream.range(0, normalizers.size())
				.filter(i -> normalizers.get(i) == MetaHtmlSanitizer.this)
				.findFirst()
				.orElse(-1);
		return SCRIPT_PREFIX + "_" + entity.getId() + "_" + property.getId() + "_" + index;
	}

	public class HtmlSanitizerRuntime extends NormalizerRuntime {

		private final Safelist runtimeSafelist;
		private final Document.OutputSettings outputSettings;

		HtmlSanitizerRuntime(MetaEntity entity, MetaProperty property) {
			this.runtimeSafelist = buildSafelist(entity, property);
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

			return Jsoup.clean(html, "", runtimeSafelist, outputSettings);
		}
	}
}
