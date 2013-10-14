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
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import twitter4j.Status;

/**
 * タイムライン表示用のFragmentクラス
 */
public class TimeLineFragment extends ListFragment {

    private StatusListAdapter mAdapter;
    private LinkedList<Status> mStatusList;
    private ImageLoader mImageLoader;

    public static TimeLineFragment factory(int layoutId) {
        TimeLineFragment fragment = new TimeLineFragment();

        Bundle args = new Bundle();
        args.putInt("layoutId", layoutId);

        fragment.setArguments(args);
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            Serializable serializable = savedInstanceState.getSerializable("time_line");

            if (serializable != null && serializable instanceof List) {
                @SuppressWarnings("unchecked")
                List<Status> list = (List<Status>) savedInstanceState.getSerializable("time_line");
                mStatusList = new LinkedList<Status>(list);
            }
        }
        if (mStatusList == null) {
            mStatusList = new LinkedList<Status>();
        }

        Activity activity = getActivity();
        if (activity != null) {
            mAdapter = new StatusListAdapter(activity, 0, mStatusList);
            setListAdapter(mAdapter);
        }

        mImageLoader = ImageLoader.getInstance();


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
        if (mAdapter.getCount() == 0) {
            mAdapter.add(status);
        } else {
            mAdapter.insert(status, 0);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("time_line", mStatusList);
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
                Log.d(this.getClass().getPackage().getName(), "convertView is null");
            }

            if (convertView != null) {
                ImageView icon = (ImageView) convertView.findViewById(R.id.user_icon);
                setStatusUserIcon(icon, item);
                ((TextView) convertView.findViewById(R.id.user_name))
                        .setText(item.getUser().getName());
                ((TextView) convertView.findViewById(R.id.user_screen_name))
                        .setText(item.getUser().getScreenName());
                ((TextView) convertView.findViewById(R.id.status_body))
                        .setText(item.getText());

                TextView via = ((TextView) convertView.findViewById(R.id.status_client));
                setStatusVia(via, item);
            }

            return convertView;
        }

        private void setStatusVia(TextView view, Status status) {
            String source = status.getSource();
            int start = source.indexOf(">") + 1;
            int end = source.indexOf("<", start);
            if (start > 0 && end > start) {
                source = source.substring(start, end);
            }
            view.setText(source);
        }

        private void setStatusUserIcon(ImageView view, Status status) {
            String url = status.getUser().getMiniProfileImageURL();
            mImageLoader.displayImage(url, view);
            mImageLoader.loadImage(url, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {
                    Log.i(getClass().getPackage().getName(), "start: " + s);
                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    ((ImageView)view).setImageBitmap(bitmap);
                    Log.i(getClass().getPackage().getName(), "finished: \n" + s + "\n" + bitmap.hashCode());
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });
        }

    }
}
