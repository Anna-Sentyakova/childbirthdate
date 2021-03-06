package com.anna.sent.soft.childbirthdate.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;

abstract class TitlesPagerAdapter extends BaseFragmentPagerAdapter {
    private final String[] mTitles;

    protected TitlesPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mTitles = getTitlesFromContext(context);
    }

    String[] getTitlesFromContext(Context context) {
        return null;
    }

    @Override
    public int getCount() {
        return mTitles != null ? mTitles.length : 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles != null ? mTitles[position] : "";
    }
}
