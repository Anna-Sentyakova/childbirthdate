package com.anna.sent.soft.childbirthdate;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;

import com.anna.sent.soft.childbirthdate.fragments.TabCalculationFragment;
import com.anna.sent.soft.childbirthdate.fragments.TabHelpFragment;
import com.anna.sent.soft.childbirthdate.fragments.TabSettingsFragment;
import com.anna.sent.soft.childbirthdate.gui.TabsAdapter;
import com.anna.sent.soft.childbirthdate.shared.Shared;
import com.anna.sent.soft.childbirthdate.widget.MyPregnancyWidget;
import com.anna.sent.soft.utils.StateSaver;
import com.anna.sent.soft.utils.Utils;

public class MainActivity extends FragmentActivity implements StateSaver {

	private TabHost mTabHost;
	ViewPager mViewPager;
	TabsAdapter mTabsAdapter;

	private static final String EXTRA_GUI_CURRENT_TAB = "com.anna.sent.soft.childbirthdate.currenttab";
	private static final String EXTRA_GUI_THEME_ID = "com.anna.sent.soft.childbirthdate.themeid";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		SharedPreferences settings = Shared.getSettings(this);
		int themeId = settings.getInt(EXTRA_GUI_THEME_ID, Utils.DARK_THEME);
		Utils.onActivityCreateSetTheme(this, themeId);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final String tab_settings = "tab_settings", tab_calculation = "tab_calculation", tab_help = "tab_help";

		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setOffscreenPageLimit(2);

		mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);
		mTabsAdapter.addTab(
				mTabHost.newTabSpec(tab_settings).setIndicator(
						getString(R.string.settings)),
				TabSettingsFragment.class, null);
		mTabsAdapter.addTab(
				mTabHost.newTabSpec(tab_calculation).setIndicator(
						getString(R.string.calculation)),
				TabCalculationFragment.class, null);
		mTabsAdapter.addTab(
				mTabHost.newTabSpec(tab_help).setIndicator(
						getString(R.string.help)), TabHelpFragment.class, null);

		if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
			mTabHost.setCurrentTabByTag(savedInstanceState
					.getString(EXTRA_GUI_CURRENT_TAB));
		} else {
			savedInstanceState = getIntent().getExtras();
			if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
				mTabHost.setCurrentTabByTag(savedInstanceState
						.getString(EXTRA_GUI_CURRENT_TAB));
			} else {
				mTabHost.setCurrentTabByTag(tab_calculation);
			}
		}
	}

	private TabCalculationFragment getTabCalculationFragment() {
		return (TabCalculationFragment) mTabsAdapter.getFragment(1);
	}

	private TabSettingsFragment getTabSettingsFragment() {
		return (TabSettingsFragment) mTabsAdapter.getFragment(0);
	}

	private TabHelpFragment getTabHelpFragment() {
		return (TabHelpFragment) mTabsAdapter.getFragment(2);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		SharedPreferences settings = Shared.getSettings(this);
		Editor editor = settings.edit();
		editor.putInt(EXTRA_GUI_THEME_ID, Utils.getThemeId());
		editor.commit();

		// update widgets
		Intent updateWidget = new Intent(this, MyPregnancyWidget.class);
		updateWidget.setAction(MyPregnancyWidget.UPDATE_ACTION);
		PendingIntent pending = PendingIntent.getBroadcast(this, 0,
				updateWidget, PendingIntent.FLAG_CANCEL_CURRENT);
		try {
			pending.send();
		} catch (CanceledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void calculate(View view) {
		Intent intent = new Intent(this,
				com.anna.sent.soft.childbirthdate.ResultActivity.class);
		TabCalculationFragment tabCalculationFragment = getTabCalculationFragment();
		if (tabCalculationFragment != null) {
			tabCalculationFragment.putExtras(intent);
		}

		TabSettingsFragment tabSettingsFragment = getTabSettingsFragment();
		if (tabSettingsFragment != null) {
			tabSettingsFragment.putExtras(intent);
		}

		int viewId = view.getId();
		if (view.getId() == R.id.buttonCalculateEstimatedChildbirthDate) {
			intent.putExtra(Shared.ResultParam.EXTRA_WHAT_TO_DO,
					Shared.ResultParam.Calculate.ECD);
		} else if (viewId == R.id.buttonCalculateEstimatedGestationalAge) {
			intent.putExtra(Shared.ResultParam.EXTRA_WHAT_TO_DO,
					Shared.ResultParam.Calculate.EGA);
		}

		startActivity(intent);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putString(EXTRA_GUI_CURRENT_TAB, mTabHost.getCurrentTabTag());

		TabCalculationFragment tabCalculationFragment = getTabCalculationFragment();
		if (tabCalculationFragment != null) {
			tabCalculationFragment.onSaveInstanceState(outState);
		}

		TabSettingsFragment tabSettingsFragment = getTabSettingsFragment();
		if (tabSettingsFragment != null) {
			tabSettingsFragment.onSaveInstanceState(outState);
		}

		TabHelpFragment tabHelpFragment = getTabHelpFragment();
		if (tabHelpFragment != null) {
			tabHelpFragment.onSaveInstanceState(outState);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		switch (Utils.getThemeId()) {
		case Utils.LIGHT_THEME:
			menu.findItem(R.id.lighttheme).setChecked(true);
			break;
		case Utils.DARK_THEME:
		default:
			menu.findItem(R.id.darktheme).setChecked(true);
			break;
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.lighttheme:
			Utils.changeToTheme(this, Utils.LIGHT_THEME);
			return true;
		case R.id.darktheme:
			Utils.changeToTheme(this, Utils.DARK_THEME);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
