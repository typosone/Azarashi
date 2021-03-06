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
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity {
    public static final int REQUEST_OAUTH = 0x10000001;
    private TwitterUtils mTwitterUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTwitterUtils = new TwitterUtils(this);

        if (!mTwitterUtils.hasAccessToken()) {
            Intent intent = new Intent(this, OAuthActivity.class);
            startActivityForResult(intent, REQUEST_OAUTH);
        }

        setContentView(R.layout.activity_main);

    }

    /**
     * 起動したActivityの結果が正しいか確認します。
     *
     * 現状では以下のActivityに対して結果を返すよう期待しています。
     * <ul>
     *     <li>OAuthActivity</li>
     * </ul>
     *
     * @see android.app.Activity#startActivityForResult(android.content.Intent, int)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_OAUTH:
                //TODO: アクセストークン取得チェックを行う(SharedPreferencesから)
                //TODO: タイムラインの読み込みを行う
                break;
            default:
                super.onActivityResult(requestCode,resultCode,data);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
