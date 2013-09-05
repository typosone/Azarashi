package jp.typosone.android.azarashi;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * タイムライン表示用のFragmentクラス
 */
public class TimeLineFragment extends ListFragment {

    public static TimeLineFragment factory(int layoutId) {
        TimeLineFragment fragment = new TimeLineFragment();

        Bundle args = new Bundle();
        args.putInt("layoutId", layoutId);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity activity = getActivity();
        if (activity != null) {
            ArrayAdapter<View> adapter = new ArrayAdapter<View>(
                    activity, android.R.layout.simple_list_item_1);

            setListAdapter(adapter);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            return inflater.inflate(args.getInt("layoutId"), container, false);
        }
        return null;
    }
}
