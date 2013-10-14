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

import android.content.Context;
import android.content.SharedPreferences;

import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Twitter関連の複雑な手順を1メソッドにまとめたクラスです。
 */
public class TwitterUtils {
    public static final String PREFERENCES_NAME = TwitterUtils.class.getPackage().getName();
    public static final String PREF_ACCESS_TOKEN = "acc_ess_Tok_en?";
    public static final String PREF_ACCESS_SECRET = "acc_ess_sec_ret";
    private Context mContext;
    private SharedPreferences mPreferences;
    private AccessToken mAccessToken;
    private TwitterStream mTwitterStream;

    public TwitterUtils(Context context) {
        mContext = context;
        mPreferences = context.getSharedPreferences(
                PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public boolean hasAccessToken() {
        return mPreferences.getString(PREF_ACCESS_TOKEN, null) != null
                && mPreferences.getString(PREF_ACCESS_SECRET, null) != null;
    }

    public void saveAccessToken(AccessToken token) {
        SharedPreferences.Editor editor = mPreferences.edit();

        editor.putString(PREF_ACCESS_TOKEN, token.getToken());
        editor.putString(PREF_ACCESS_SECRET, token.getTokenSecret());

        editor.commit();
    }

    public AccessToken getAccessToken() {
        if (mAccessToken == null && hasAccessToken()) {
            mAccessToken = new AccessToken(
                    mPreferences.getString(PREF_ACCESS_TOKEN, null),
                    mPreferences.getString(PREF_ACCESS_SECRET, null)
            );
        }
        return mAccessToken;
    }

    public TwitterStream getStream() {
        if (mTwitterStream == null && hasAccessToken()) {
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(mContext.getString(R.string.consumer_key));
            builder.setOAuthConsumerSecret(mContext.getString(R.string.consumer_secret));
            mTwitterStream
                    = new TwitterStreamFactory(builder.build()).getInstance(getAccessToken());
        }
        return mTwitterStream;
    }
}
