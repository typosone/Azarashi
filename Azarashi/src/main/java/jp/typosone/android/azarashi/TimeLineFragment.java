/*
 * Copyright (C) 2013 Nakasone Tsuyoshi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.typosone.android.azarashi;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import twitter4j.Status;

/**
 * タイムライン表示用のFragmentクラス
 */
public class TimeLineFragment extends ListFragment {

    private ArrayAdapter<View> mAdapter;

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
            mAdapter = new ArrayAdapter<View>(
                    activity, android.R.layout.simple_list_item_1);

            setListAdapter(mAdapter);
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

    public void addStatusView(View view) {
        mAdapter.add(view);
    }

    public static class StatusListAdapter extends ArrayAdapter<Status> {

        public StatusListAdapter(Context context, int resource) {
            super(context, resource);
        }
    }
}
