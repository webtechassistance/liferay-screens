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

package com.liferay.mobile.screens.viewsets.defaultviews.userportrait;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.liferay.mobile.screens.userportrait.view.UserPortraitViewModel;
import com.liferay.mobile.screens.util.LiferayLogger;
import com.liferay.mobile.screens.viewsets.R;
import com.liferay.mobile.screens.viewsets.defaultviews.DefaultTheme;

/**
 * @author Javier Gamarra
 * @author Jose Manuel Navarro
 */
public class UserPortraitView extends FrameLayout implements UserPortraitViewModel {

    public UserPortraitView(Context context) {
        super(context);

		DefaultTheme.initIfThemeNotPresent(context);
    }

    public UserPortraitView(
		Context context, AttributeSet attributes) {
        super(context, attributes);

		DefaultTheme.initIfThemeNotPresent(context);
    }

    public UserPortraitView(Context context, AttributeSet attributes, int defaultStyle) {
        super(context, attributes, defaultStyle);

		DefaultTheme.initIfThemeNotPresent(context);
    }

	@Override
	public void showStartOperation(String actionName) {
		_portraitProgress.setVisibility(VISIBLE);
	}

	@Override
	public void showFinishOperation(String actionName) {
		throw new AssertionError("Use showFinishOperation(bitmap) instead");
	}

	@Override
	public void showFinishOperation(Bitmap bitmap) {
		LiferayLogger.i("portrait loaded");
		_portraitProgress.setVisibility(INVISIBLE);
		_portraitImage.setImageBitmap(transformBitmap(bitmap));
	}

	@Override
	public void showFailedOperation(String actionName, Exception e) {
		LiferayLogger.e("portrait failed to load", e);
		_portraitProgress.setVisibility(INVISIBLE);
		setDefaultImagePlaceholder();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		_portraitImage = (ImageView) findViewById(R.id.portrait_image);
		_portraitProgress = (ProgressBar) findViewById(R.id.portrait_progress);

		setDefaultImagePlaceholder();
	}

	protected float getBorderRadius() {
		return (float) getResources().getInteger(R.integer.userportrait_default_border_radius);
	}

	protected float getBorderWidth() {
		return (float) getResources().getInteger(R.integer.userportrait_default_border_width);
	}

    protected Bitmap transformBitmap(Bitmap bitmap) {
		float borderRadius = getBorderRadius();
		float borderWidth = getBorderWidth();

		RectF rect = getRectF(bitmap, borderWidth);

        Bitmap finalBitmap = Bitmap.createBitmap(
			bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(finalBitmap);

        canvas.drawRoundRect(rect, borderRadius, borderRadius, getPaint(bitmap));
        canvas.drawRoundRect(rect, borderRadius, borderRadius, getBorderPaint(borderWidth, R.color.default_dark_gray));

        return finalBitmap;
    }

    protected RectF getRectF(Bitmap bitmap, float borderWidth) {
        RectF rect = new RectF(0.0f, 0.0f, bitmap.getWidth(), bitmap.getHeight());
        rect.inset(borderWidth, borderWidth);
        return rect;
    }

    protected Paint getBorderPaint(float borderWidth, int color) {
        Paint borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
        borderPaint.setColor(getResources().getColor(color));
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(borderWidth);
        return borderPaint;
    }

    protected Paint getPaint(Bitmap bitmap) {
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);
        return paint;
    }

	private void setDefaultImagePlaceholder() {
		Bitmap defaultBitmap = BitmapFactory.decodeResource(
			getResources(), R.drawable.default_portrait_placeholder);
		_portraitImage.setImageBitmap(transformBitmap(defaultBitmap));
	}

    private ImageView _portraitImage;
    private ProgressBar _portraitProgress;

}