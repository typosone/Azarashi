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
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import twitter4j.auth.AccessToken;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationContext;

/**
 * Twitter との連携を開始するためのアクティビティです。
 * <p/>
 * 認証用のURLを取得し、ブラウザでアクセスするようインテントを投げます。<br />
 * 認証後はCallbackURLパラメータを利用し、そのままアプリに戻ってきます。
 */
public class OAuthActivity extends Activity {
    public static final String CALLBACK_URL
            = OAuthActivity.class.getPackage().getName() + "://callback";
    private static final int LID_REQ = 0x70000001;
    private static final int LID_ACC = 0x70000002;
    private OAuthAuthorization mOAuth;
    private RequestToken mRequestToken;
    private RequestTokenLoaderCallbacks mRequestCallbacks;
    private AccessTokenLoaderCallbacks mAccessCallbacks;

    /**
     * Twitter OAuth 認証のためにブラウザを開きます。
     * <p/>
     * OAuth 認証のために RequestToken を取得し
     * その後、AccessToken を取得するための認証URLを組み立てて、暗黙的インテントを発行します。
     *
     * @see Activity#onCreate
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration conf = ConfigurationContext.getInstance();
        mOAuth = new OAuthAuthorization(conf);
        mRequestCallbacks = new RequestTokenLoaderCallbacks();
        mAccessCallbacks = new AccessTokenLoaderCallbacks();

        getLoaderManager().initLoader(LID_REQ, null, mRequestCallbacks);
    }

    /**
     * ブラウザで Twitter との認証後にコールバックを受け取ります。
     * <p/>
     * Twitter 認証 URL に指定された callbackURL へアクセスがあると
     * IntentFilter の構成によりこのアクティビティが起動します。
     * <p/>
     * その後、取得した AccessToken と AccessTokenSecret を
     * SharedPreferences へ保存します。
     * <p/>
     *
     * @see #onResume
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Bundle args = new Bundle();
        args.putParcelable("intent", intent);
        getLoaderManager().restartLoader(LID_ACC, args, mAccessCallbacks);
    }

    private class RequestTokenLoaderCallbacks
            implements LoaderManager.LoaderCallbacks<RequestToken> {

        @Override
        public Loader<RequestToken> onCreateLoader(int id, Bundle args) {
            return new RequestTokenTaskLoader(OAuthActivity.this, mOAuth, CALLBACK_URL);
        }

        @Override
        public void onLoadFinished(Loader<RequestToken> loader, RequestToken data) {
            if (data == null) {
                throw new IllegalStateException("リクエストトークンの取得に失敗しました。");
            }
            getLoaderManager().destroyLoader(loader.getId());

            mRequestToken = data;
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(mRequestToken.getAuthorizationURL())));
        }

        @Override
        public void onLoaderReset(Loader<RequestToken> loader) {

        }
    }

    private class AccessTokenLoaderCallbacks
            implements LoaderManager.LoaderCallbacks<AccessToken> {

        @Override
        public Loader<AccessToken> onCreateLoader(int id, Bundle args) {
            Intent intent = args.getParcelable("intent");
            return new AccessTokenTaskLoader(
                    OAuthActivity.this, intent, mOAuth, mRequestToken, CALLBACK_URL);
        }

        @Override
        public void onLoadFinished(Loader<AccessToken> loader, AccessToken data) {
            if (data != null) {
                Log.d(getPackageName(), "アクセストークン取得完了");
            }
        }

        @Override
        public void onLoaderReset(Loader<AccessToken> loader) {

        }
    }
}
