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

package com.liferay.mobile.screens.viewsets.defaultviews.webcontentdisplay;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.liferay.mobile.screens.context.LiferayServerContext;
import com.liferay.mobile.screens.util.LiferayLogger;
import com.liferay.mobile.screens.viewsets.R;
import com.liferay.mobile.screens.viewsets.defaultviews.DefaultCrouton;
import com.liferay.mobile.screens.viewsets.defaultviews.DefaultTheme;
import com.liferay.mobile.screens.webcontentdisplay.view.WebContentDisplayViewModel;

/**
 * @author Silvio Santos
 */
public class WebContentDisplayView extends FrameLayout
	implements WebContentDisplayViewModel {

	public WebContentDisplayView(Context context) {
		super(context);

		DefaultTheme.initIfThemeNotPresent(context);
	}

	public WebContentDisplayView(Context context, AttributeSet attributes) {

		super(context, attributes);

		DefaultTheme.initIfThemeNotPresent(context);
	}

	public WebContentDisplayView(Context context, AttributeSet attributes, int defaultStyle) {
		super(context, attributes, defaultStyle);

		DefaultTheme.initIfThemeNotPresent(context);
	}

	@Override
	public void showStartOperation(String actionName) {
		_progressBar.setVisibility(View.VISIBLE);
		_webView.setVisibility(View.GONE);
	}

	@Override
	public void showFinishOperation(String html) {
		_progressBar.setVisibility(View.GONE);
		_webView.setVisibility(View.VISIBLE);

		LiferayLogger.i("article loaded: " + html);

		String styledHtml = STYLES + "<div class=\"MobileCSS\">" + html + "</div>";

		//TODO check encoding
		_webView.loadDataWithBaseURL(
				LiferayServerContext.getServer(), styledHtml, "text/html", "utf-8",
				null);
	}

	@Override
	public void showFailedOperation(String actionName, Exception e) {
		_progressBar.setVisibility(View.GONE);
		_webView.setVisibility(View.VISIBLE);

		DefaultCrouton.error(getContext(), getContext().getString(R.string.loading_article_error), e);
		LiferayLogger.e(getContext().getString(R.string.loading_article_error), e);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		_webView = (WebView) findViewById(R.id.webview);
		_progressBar = (ProgressBar) findViewById(R.id.webview_progress_bar);
	}

	private static final String STYLES =
		"<style>" +
		".MobileCSS {padding: 4%; width: 92%;} " +
		".MobileCSS, .MobileCSS span, .MobileCSS p, .MobileCSS h1, " +
			".MobileCSS h2, .MobileCSS h3{ " +
		"font-size: 110%; font-weight: 200;" +
			"font-family: \"Helvetica Neue\", Helvetica, Arial, sans-serif;} " +
		".MobileCSS img { width: 100% !important; } " +
		".span2, .span3, .span4, .span6, .span8, .span10 { width: 100%; }" +
		"</style>";

	private WebView _webView;
	private ProgressBar _progressBar;

}