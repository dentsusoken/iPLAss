/*
 * Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.
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

import jakarta.servlet.http.HttpServletRequest;

import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.view.top.TopViewHandler;
import org.iplass.mtp.view.top.parts.TopViewParts;
import org.iplass.mtp.view.top.parts.TreeViewParts;

/**
 * ツリービューパーツ
 * @author lis3wg
 */
public class MetaTreeViewParts extends MetaTemplateParts {

	/** SerialVersionUID */
	private static final long serialVersionUID = -1412208019553670440L;

	/**
	 * インスタンスを生成します。
	 * @param parts TOP画面パーツ
	 * @return インスタンス
	 */
	public static MetaTreeViewParts createInstance(TopViewParts parts) {
		return new MetaTreeViewParts();
	}

	/** ツリービュー定義名 */
	private String treeViewName;

	/** アイコンタグ */
	private String iconTag;

	/**
	 * ツリービュー定義名を取得します。
	 * @return ツリービュー定義名
	 */
	public String getTreeViewName() {
	    return treeViewName;
	}

	/**
	 * ツリービュー定義名を設定します。
	 * @param treeViewName ツリービュー定義名
	 */
	public void setTreeViewName(String treeViewName) {
	    this.treeViewName = treeViewName;
	}

	/**
	 * アイコンタグを取得します。
	 * @return アイコンタグ
	 */
	public String getIconTag() {
	    return iconTag;
	}

	/**
	 * アイコンタグを設定します。
	 * @param iconTag アイコンタグ
	 */
	public void setIconTag(String iconTag) {
	    this.iconTag = iconTag;
	}

	@Override
	public void applyConfig(TopViewParts parts) {
		TreeViewParts tree = (TreeViewParts) parts;
		fillFrom(tree);

		treeViewName = tree.getTreeViewName();
		iconTag = tree.getIconTag();
	}

	@Override
	public TopViewParts currentConfig() {
		TreeViewParts tree = new TreeViewParts();
		fillTo(tree);

		tree.setTreeViewName(treeViewName);
		tree.setIconTag(iconTag);

		return tree;
	}

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public TemplatePartsHandler createRuntime(TopViewHandler topView) {
		return new TemplatePartsHandler(this) {
			private static final String TEMPLATE_PATH_WIDGET = "gem/treeview/treeViewWidget";
			private static final String TEMPLATE_PATH_PARTS = "gem/treeview/treeViewParts";

			@Override
			public boolean isParts() {
				return true;
			}

			@Override
			public boolean isWidget() {
				return true;
			}

			@Override
			public String getTemplatePathForParts(HttpServletRequest req) {
				return TEMPLATE_PATH_PARTS;
			}

			@Override
			public String getTemplatePathForWidget(HttpServletRequest req) {
				return TEMPLATE_PATH_WIDGET;
			}

			@Override
			public void setAttribute(HttpServletRequest req) {
				req.setAttribute("treeViewName", treeViewName);
				req.setAttribute("treeViewParts", currentConfig());
			}

			@Override
			public void clearAttribute(HttpServletRequest req) {
				req.removeAttribute("treeViewName");
				req.removeAttribute("treeViewParts");
			}
			
			@Override
			protected boolean useWrapper(Integer maxHeight) {
				return false;
			}
		};
	}
}
