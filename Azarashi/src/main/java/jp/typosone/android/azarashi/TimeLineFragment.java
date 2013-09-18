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
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import twitter4j.Status;

/**
 * タイムライン表示用のFragmentクラス
 */
public class TimeLineFragment extends ListFragment {

    private StatusListAdapter mAdapter;
    private LinkedList<Status> mStatusList;

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
        mStatusList = new LinkedList<Status>();

        Activity activity = getActivity();
        if (activity != null) {
            mAdapter = new StatusListAdapter(getActivity(), 0, mStatusList);
            setListAdapter(mAdapter);
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            return inflater.inflate(args.getInt("layoutId"), container, false);
        }
        return null;
    }

    public void addStatus(Status status) {
        mAdapter.add(status);
    }

    public class StatusListAdapter extends ArrayAdapter<Status> {

        private LayoutInflater mLayoutInflater;

        public StatusListAdapter(Context context, int textViewResourceId, List<Status> objects) {
            super(context, textViewResourceId, objects);
            Object obj = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (obj instanceof LayoutInflater) {
                mLayoutInflater = (LayoutInflater) obj;
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Status item = getItem(position);

            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.status_item, null);
            }

            if (convertView != null) {
                ((ImageView) convertView.findViewById(R.id.user_icon))
                        .setImageDrawable(downloadImage(item.getUser().getMiniProfileImageURL()));
//                URI(Uri.parse(item.getUser().getMiniProfileImageURL()));
                ((TextView) convertView.findViewById(R.id.user_name))
                        .setText(item.getUser().getName());
                ((TextView) convertView.findViewById(R.id.user_screen_name))
                        .setText(item.getUser().getScreenName());
                ((TextView) convertView.findViewById(R.id.status_body))
                        .setText(item.getText());
                ((TextView) convertView.findViewById(R.id.status_client))
                        .setText(item.getSource());
            }

            return convertView;
        }

        private Drawable downloadImage(String address) {
            Drawable data = null;
            try {
                URL url = new URL(address);
                InputStream in = (InputStream) url.getContent();
                data = Drawable.createFromStream(in,"icon");
            } catch (MalformedURLException e) {
                Log.e(this.getClass().getPackage().getName(), e.getMessage(), e);
            } catch (IOException e) {
                Log.e(this.getClass().getPackage().getName(), e.getMessage(), e);
            }
            return data;
        }
    }

}
