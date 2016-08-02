package com.m.h5hall.comm.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

@SuppressWarnings("deprecation")
public class ProgressWebView extends WebView {

	private ProgressBar mInnerProgressBar;
	private ProgressBar mOuterProgressBar;

	public ProgressBar getProgressBar() {
		return mOuterProgressBar != null ? mOuterProgressBar : mInnerProgressBar;
	}

	public void setProgressBar(ProgressBar progressBar) {
		this.mInnerProgressBar.setVisibility(View.GONE);
		this.mOuterProgressBar = progressBar;
	}

	public ProgressWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mInnerProgressBar = new ProgressBar(context, null,
				android.R.attr.progressBarStyleHorizontal);
		final Float density = getResources().getDisplayMetrics().density;
		Float hight = 3 * density;
		mInnerProgressBar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				hight.intValue(), 0, 0));
		addView(mInnerProgressBar);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		LayoutParams lp = (LayoutParams) mInnerProgressBar.getLayoutParams();
		lp.x = l;
		lp.y = t;
		mInnerProgressBar.setLayoutParams(lp);
		super.onScrollChanged(l, t, oldl, oldt);
	}

}
