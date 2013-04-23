package com.anna.sent.soft.childbirthdate.fragments;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.FrameLayout;

public class CheckableFrameLayout extends FrameLayout implements Checkable {
	private boolean mChecked;

	public CheckableFrameLayout(Context context) {
		super(context);
	}

	public CheckableFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@SuppressWarnings("deprecation")
	public void setChecked(boolean checked) {
		mChecked = checked;
		setBackgroundDrawable(checked ? new ColorDrawable(0xff0000a0/*003ea8f6*/) : null);
	}

	public boolean isChecked() {
		return mChecked;
	}

	public void toggle() {
		setChecked(!mChecked);
	}
}