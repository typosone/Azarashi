package jp.typosone.android.azarashi;
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

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * インターネット上の指定されたURLから画像を取得し、Drawableとして返します。
 */
public class DrawableLoader extends AsyncTaskLoader<Bitmap> {
    private String mImageUrl;
    private ImageLoader mImageLoader;

    public DrawableLoader(Context context, String url, ImageLoader loader) {
        super(context);

        mImageUrl = url;
        mImageLoader = loader;
    }

    @Override
    public Bitmap loadInBackground() {
//        mImageLoader.loadImage(mImageUrl);
        return null;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
