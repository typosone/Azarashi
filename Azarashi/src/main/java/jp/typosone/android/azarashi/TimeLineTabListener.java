package jp.typosone.android.azarashi;

import android.app.ActionBar;
import android.app.FragmentTransaction;

/**
 * ActionBarのタブをクリックイベントを処理するクラスです。
 *
 */
public class TimeLineTabListener implements ActionBar.TabListener {

    private TimeLineFragment mFragment;

    public TimeLineTabListener(TimeLineFragment fragment) {
        mFragment = fragment;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        if (!mFragment.isAdded()) {
            ft.add(android.R.id.list, mFragment);
        } else {
            ft.show(mFragment);
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        ft.hide(mFragment);
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }
}
