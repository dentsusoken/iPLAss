/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.view.top.parts;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.ViewConst;
import org.iplass.mtp.view.top.parts.SeparatorParts;
import org.iplass.mtp.view.top.parts.TopViewParts;

/**
 * 2分割パーツ
 * @author lis3wg
 */
public class MetaSeparatorParts extends MetaTopViewContentParts {

	/** SerialVersionUID */
	private static final long serialVersionUID = 7481198924024805153L;

	/**
	 * インスタンスを生成します。
	 * @param parts TOP画面パーツ
	 * @return インスタンス
	 */
	public static MetaSeparatorParts createInstance(TopViewParts parts) {
		return new MetaSeparatorParts();
	}

	/** 左エリアパーツ */
	private MetaTopViewParts leftParts;

	/** 右エリアパーツ */
	private MetaTopViewParts rightParts;

	/**
	 * 左エリアパーツを取得します。
	 * @return 左エリアパーツ
	 */
	public MetaTopViewParts getLeftParts() {
	    return leftParts;
	}

	/**
	 * 左エリアパーツを設定します。
	 * @param leftParts 左エリアパーツ
	 */
	public void setLeftParts(MetaTopViewParts leftParts) {
	    this.leftParts = leftParts;
	}

	/**
	 * 右エリアパーツを取得します。
	 * @return 右エリアパーツ
	 */
	public MetaTopViewParts getRightParts() {
	    return rightParts;
	}

	/**
	 * 右エリアパーツを設定します。
	 * @param rightParts 右エリアパーツ
	 */
	public void setRightParts(MetaTopViewParts rightParts) {
	    this.rightParts = rightParts;
	}

	@Override
	public void applyConfig(TopViewParts parts) {
		SeparatorParts separator = (SeparatorParts) parts;
		fillFrom(separator);
		
		this.leftParts = MetaTopViewParts.createInstance(separator.getLeftParts());
		if (leftParts != null) leftParts.applyConfig(separator.getLeftParts());
		this.rightParts = MetaTopViewParts.createInstance(separator.getRightParts());
		if (rightParts != null) rightParts.applyConfig(separator.getRightParts());
		
	}

	@Override
	public SeparatorParts currentConfig() {
		SeparatorParts parts = new SeparatorParts();
		fillTo(parts);
		
		if (leftParts != null) {
			TopViewParts lp = leftParts.currentConfig();
			if (lp != null) parts.setLeftParts(lp);
		}
		if (rightParts != null) {
			TopViewParts rp = rightParts.currentConfig();
			if (rp != null) parts.setRightParts(rp);
		}
		//
		return parts;
	}

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public TopViewPartsHandler createRuntime() {
		return new SeparatorPartsHandler(this);
	}

	/**
	 * 2分割パーツランタイム
	 * @author lis3wg
	 */
	public class SeparatorPartsHandler extends TopViewPartsHandler {

		/** 左エリアパーツ */
		private TopViewPartsHandler leftParts;

		/** 右エリアパーツ */
		private TopViewPartsHandler rightParts;

		/**
		 * コンストラクタ
		 * @param metadata
		 */
		public SeparatorPartsHandler(MetaSeparatorParts metadata) {
			super(metadata);
			if (metadata.getLeftParts() != null) leftParts = metadata.getLeftParts().createRuntime();
			if (metadata.getRightParts() != null) rightParts = metadata.getRightParts().createRuntime();
		}

		@Override
		public void loadParts(HttpServletRequest req, HttpServletResponse res,
				ServletContext application, PageContext page)
				throws IOException, ServletException {
			String separatorClass = null;
			String separatorLeftClass = null;
			String separatorRightClass = null;

			String designType = (String) req.getAttribute(ViewConst.DESIGN_TYPE);
			if (ViewConst.DESIGN_TYPE_GEM.equals(designType)) {
				separatorClass = "col2-wrap";
				if(StringUtil.isNotBlank(getStyle())) {
					separatorClass = separatorClass + " " + getStyle(); 
				}
				separatorLeftClass = "col-left";
				separatorRightClass = "col-right";
			}


			Writer writer = page.getOut();
			writer.write("<div class=\"" + separatorClass + "\">\n");

			writer.write("<div class=\"" + separatorLeftClass + "\">\n");
			if (leftParts != null) {
				leftParts.setAttribute(req);
				leftParts.loadParts(req, res, application, page);
				leftParts.clearAttribute(req);
			}
			writer.write("</div>\n");

			writer.write("<div class=\"" + separatorRightClass + "\">\n");
			if (rightParts != null) {
				if (req.getAttribute("partsCnt") != null) {
					req.setAttribute("partsCnt", (Integer) req.getAttribute("partsCnt") + 1);
				}
				rightParts.setAttribute(req);
				rightParts.loadParts(req, res, application, page);
				rightParts.clearAttribute(req);
			}
			writer.write("</div>\n");

			writer.write("</div>\n");
		}

		@Override
		public void loadWidgets(HttpServletRequest req,
				HttpServletResponse res, ServletContext application,
				PageContext page) throws IOException, ServletException {
		}

		@Override
		public void setAttribute(HttpServletRequest req) {
		}

		@Override
		public void clearAttribute(HttpServletRequest req) {
		}

		public TopViewPartsHandler leftParts() {
			return leftParts;
		}

		public TopViewPartsHandler rightParts() {
			return rightParts;
		}

	}
}
