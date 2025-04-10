/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
 */

package org.iplass.mtp.impl.tools.validator;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.iplass.mtp.impl.view.generic.MetaEntityView;
import org.iplass.mtp.impl.view.generic.MetaFormView;
import org.iplass.mtp.util.StringUtil;

/**
 * EntityViewメタデータ定義名をチェックするValidatorクラス
 * 
 * <p>
 * 下記のようになってるかチェックする
 * </p>
 * <ul>
 * <li>英数、アンダスコア、先頭の数字不可、マイナス不可</li>
 * <li>パスにはピリオドを利用</li>
 * </ul>
 * 
 */
public class EntityViewValidator extends EntityValidator<MetaEntityView> {

	@Override
	public Optional<String> validate(MetaEntityView meta) {
		if (Objects.isNull(meta)) {
			return Optional.empty();
		}

		// まずはEntity定義名のチェック
		Optional<String> optResultEntityName = super.validate(meta);
		if (optResultEntityName.isPresent()) {
			return optResultEntityName;
		}

		// EntityView取得
		List<MetaFormView> views = meta.getViews();
		if (CollectionUtils.isEmpty(views)) {
			return Optional.empty();
		}

		// EntityView名のチェック（1個でも許容されてないView名があったらチェックNG）
		Optional<MetaFormView> optErrorView = views.stream().filter(view -> {
			String viewName = view.getName();
			if (StringUtil.isEmpty(viewName)) {
				return false;
			}

			return !this.check(viewName, DefinitionNameValidatorConstants.NAME_REG_EXP_PATH_PERIOD);
		}).findFirst();

		return optErrorView.isEmpty() ? Optional.empty()
				: Optional.of(this.getMessage("validator.regularExpression.viewName", optErrorView.get().getName()));
	}
}
