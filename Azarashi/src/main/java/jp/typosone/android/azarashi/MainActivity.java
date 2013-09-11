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

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import twitter4j.Status;
import twitter4j.TwitterStream;

public class MainActivity extends Activity {
    public static final int REQUEST_OAUTH = 0x11111111;
    private TwitterUtils mTwitterUtils;
    private Handler mHandler;
    private TimeLineFragment mHomeFragment, mMentionsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTwitterUtils = new TwitterUtils(this);

        if (!mTwitterUtils.hasAccessToken()) {
            Intent intent = new Intent(this, OAuthActivity.class);
            startActivityForResult(intent, REQUEST_OAUTH);
            return;
        }
        mHandler = new UiHandler(this);

        setContentView(R.layout.activity_main);

        ActionBar bar = getActionBar();
        if (bar != null) {
            bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

            mHomeFragment = TimeLineFragment.factory(R.layout.fragment_timeline);
            mMentionsFragment = TimeLineFragment.factory(R.layout.fragment_timeline);

            bar.addTab(bar.newTab().setText(R.string.bar_home).setTabListener(
                    new TimeLineTabListener(mHomeFragment)
            ));
            bar.addTab(bar.newTab().setText(R.string.bar_mentions).setTabListener(
                    new TimeLineTabListener(mMentionsFragment)
            ));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        TwitterStream stream = mTwitterUtils.getStream();
        stream.addListener(new UserStreamAdapter(mHandler));
        stream.user();
    }

    /**
     * <p/>
     * 起動したActivityの結果が正しいか確認します。
     * <p/>
     * 現状では以下のActivityに対して結果を返すよう期待しています。
     * <ul>
     * <li>OAuthActivity</li>
     * </ul>
     *
     * @see android.app.Activity#startActivityForResult(android.content.Intent, int)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_OAUTH:
                if (!mTwitterUtils.hasAccessToken()) {
                    throw new IllegalStateException("アクセストークンがありません");
                }
                //TODO: タイムラインの読み込みを行う
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    /**
     * Homeのタイムラインにステータスを追加します。
     *
     * @param status twitter4j.Status のインスタンス
     * @see twitter4j.Status
     */
    public void addStatus(Status status) {
        View item = View.inflate(this, R.layout.status_item, null);
        ((ImageView) item.findViewById(R.id.user_icon))
                .setImageURI(Uri.parse(status.getUser().getMiniProfileImageURL()));
        ((TextView)item.findViewById(R.id.user_name))
                .setText(status.getUser().getName());
        ((TextView)item.findViewById(R.id.user_screen_name))
                .setText(status.getUser().getScreenName());
        ((TextView) item.findViewById(R.id.status_body))
                .setText(status.getText());
        ((TextView) item.findViewById(R.id.status_client))
                .setText(status.getSource());
        mHomeFragment.addStatusView(item);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public static class UiHandler extends Handler {
        public static final int ADD_STATUS_TO_HOME = 0x11111111;
        public static final int ADD_STATUS_TO_MENTIONS = 0x22222222;
        private WeakReference<MainActivity> mMainActivityReference;

        public UiHandler(MainActivity activity) {
            mMainActivityReference = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = mMainActivityReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case ADD_STATUS_TO_HOME:
                        if (msg.obj instanceof Status) {
                            Status status = (Status) msg.obj;
                            activity.addStatus(status);
                        }
                        break;
                    case ADD_STATUS_TO_MENTIONS:
                        break;
                    default:
                        super.handleMessage(msg);
                        break;
                }
            }
        }
    }
}
