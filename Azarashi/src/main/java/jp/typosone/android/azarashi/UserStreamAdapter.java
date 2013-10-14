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


import android.os.Handler;
import android.os.Message;

import twitter4j.Status;

/**
 * TwitterStream のイベントを処理するListenerを実装したクラスです。
 *
 * 全てのインタフェースを実装する必要がないので
 * twitter4j.UserStreamAdapterクラスを継承して実装しています。
 *
 * @see twitter4j.UserStreamAdapter
 * @see twitter4j.UserStreamListener
 */
public class UserStreamAdapter extends twitter4j.UserStreamAdapter {
    private Handler mHandler;

    public UserStreamAdapter(Handler handler) {
        mHandler = handler;
    }

    @Override
    public void onStatus(Status status) {
        super.onStatus(status);

        Message msg = new Message();
        msg.what = MainActivity.UiHandler.ADD_STATUS_TO_HOME;
        msg.obj = status;
        mHandler.sendMessage(msg);
    }


}
