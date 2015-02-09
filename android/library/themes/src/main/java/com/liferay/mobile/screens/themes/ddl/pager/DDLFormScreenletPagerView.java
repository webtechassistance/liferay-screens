/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.mobile.screens.themes.ddl.pager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.liferay.mobile.screens.ddl.model.Field;
import com.liferay.mobile.screens.themes.R;
import com.liferay.mobile.screens.themes.ddl.DDLFormScreenletView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Silvio Santos
 */
public class DDLFormScreenletPagerView extends DDLFormScreenletView {

	public DDLFormScreenletPagerView(Context context) {
		this(context, null);
	}

	public DDLFormScreenletPagerView(Context context, AttributeSet attributes) {
		super(context, attributes);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		_pager = (ViewPager)findViewById(R.id.pager);
	}

	@Override
	public void setFields(List<Field> fields) {
		List<Field.EditorType> editorTypes = Field.EditorType.all();
		Map<Field.EditorType, Integer> layoutIds = new HashMap<>();

		for (Field.EditorType editorType: editorTypes) {
			layoutIds.put(editorType, getFieldLayoutId(editorType));
		}

		DDLFormViewPagerAdapter adapter = new DDLFormViewPagerAdapter(
			fields, layoutIds);

		_pager.setAdapter(adapter);
	}

	private ViewPager _pager;

}