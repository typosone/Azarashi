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
import android.util.Log;

import twitter4j.TwitterException;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.auth.RequestToken;

/**
 * Twitter認証を行うためのRequestTokenを取得します。
 */
public final class RequestTokenTaskLoader extends AsyncTaskLoader<RequestToken> {
    private OAuthAuthorization mOAuth;
    private String mCallbackUrl;

    public RequestTokenTaskLoader(Context context, OAuthAuthorization oauth, String callbackUrl) {
        super(context);
        mOAuth = oauth;
        mCallbackUrl = callbackUrl;
    }

    @Override
    public RequestToken loadInBackground() {
        mOAuth.setOAuthConsumer(
                getContext().getString(R.string.consumer_key),
                getContext().getString(R.string.consumer_secret));
        RequestToken requestToken = null;
        try {
            requestToken = mOAuth.getOAuthRequestToken(mCallbackUrl);
        } catch (TwitterException e) {
            Log.e(this.getClass().getPackage().getName(), e.getMessage(), e);
        }

        return requestToken;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}