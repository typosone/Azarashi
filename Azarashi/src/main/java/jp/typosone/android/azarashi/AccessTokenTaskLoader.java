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

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.auth.RequestToken;

/**
 * TwitterAPIへアクセスするためのAccessTokenを取得します。
 */
public final class AccessTokenTaskLoader extends AsyncTaskLoader<AccessToken> {
    private Intent mIntent;
    private OAuthAuthorization mOAuth;
    private RequestToken mRequestToken;
    private String mCallbackUrl;

    public AccessTokenTaskLoader(Context context, Intent intent,
                                 OAuthAuthorization oauth, RequestToken token,
                                 String callbackUrl) {
        super(context);
        mIntent = intent;
        mOAuth = oauth;
        mRequestToken = token;
        mCallbackUrl = callbackUrl;
    }

    @Override
    public AccessToken loadInBackground() {

        Uri uri = mIntent.getData();
        AccessToken token = null;
        if (uri != null && uri.toString().startsWith(mCallbackUrl)) {
            String verifier = uri.getQueryParameter("oauth_verifier");
            try {
                token = mOAuth.getOAuthAccessToken(mRequestToken, verifier);
            } catch (TwitterException e) {
                Log.e(this.getClass().getPackage().getName(), e.getMessage(), e);
            }
        }

        return token;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
