/*
 * Copyright (C) 2016 CaMnter yuanyu.camnter@gmail.com
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

package com.ianlee.lazy.base.lib.utils.glide;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.util.concurrent.ExecutionException;

import com.ianlee.lazy.base.lib.R;


/**
 * Description：GlideUtils
 * Created by：CaMnter
 * Time：2016-01-04 22:19
 */
public class GlideUtils {


    private static final String TAG = "GlideUtils";

    private static RequestOptions myOptions = new RequestOptions()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .skipMemoryCache(true)
            .error(R.mipmap.bg_error_small);


    private static RequestOptions myOption = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .skipMemoryCache(true)
            .placeholder(R.mipmap.bg_error_small)
            .error(R.mipmap.bg_error_small);

    public static RequestOptions adsOptions = new RequestOptions()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .skipMemoryCache(true)
            .placeholder(R.mipmap.bg_error_small)
            .error(R.mipmap.bg_error_small)
            .diskCacheStrategy(DiskCacheStrategy.ALL);

    public static RequestOptions alsOptions = new RequestOptions()
            .centerCrop()
            .placeholder(R.mipmap.bg_error_small)
            .error(R.mipmap.bg_error_small)
            .diskCacheStrategy(DiskCacheStrategy.ALL);


    public static RequestOptions crsOptions = new RequestOptions()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .skipMemoryCache(true)
            .placeholder(R.mipmap.bg_error_small)
            .error(R.mipmap.bg_error_small)
            .diskCacheStrategy(DiskCacheStrategy.ALL);


    public static RequestOptions picOptions = new RequestOptions()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.ALL);

    public static RequestOptions picOptionsto = new RequestOptions()
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.ALL);


    public static RequestOptions animOptions = new RequestOptions()
            .centerCrop()
            .dontAnimate()
            .diskCacheStrategy(DiskCacheStrategy.ALL);


    public static RequestOptions CirmyOptions = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .override(120, 120)
            .error(R.mipmap.bg_error_small);


    public static RequestOptions getoptionsto(Context context) {
        RequestOptions CirmyOptions = new RequestOptions()
                .centerCrop()
                .circleCropTransform()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(100, 100)
                .error(R.mipmap.ic_default_avatar);
        return CirmyOptions;
    }

    public static RequestOptions myOptionAdapter4 = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .skipMemoryCache(true)
            .priority(Priority.HIGH)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .error(R.mipmap.bg_error_small);


    public static RequestOptions fixedOptions = new RequestOptions()
            .override(120, 120)
            .error(R.mipmap.bg_error_small)
            .diskCacheStrategy(DiskCacheStrategy.ALL);


    public static RequestOptions getoptionstest(int a, int b) {
        RequestOptions testOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .override(a, b)
                .error(R.mipmap.bg_error_small);
        return testOptions;

    }

    private static RequestOptions gifOptionTo = new RequestOptions()
            .centerCrop()
            .skipMemoryCache(true)
            .priority(Priority.HIGH)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .error(R.mipmap.bg_error_small);


    public static RequestOptions getoptionstwo(Context context, Drawable error_drawable, boolean isCircle) {
        RequestOptions myOptions;
        if (null != error_drawable) {
            if (isCircle) {//圆形
                myOptions = new RequestOptions()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .override(100, 100)
                        .circleCropTransform()
                        .error(error_drawable);
            } else {
                myOptions = new RequestOptions()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .override(100, 100)
                        .error(error_drawable);
            }
        } else {
            if (isCircle) {//圆形
                myOptions = new RequestOptions()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.mipmap.ic_default_avatar)
                        .override(100, 100)
                        .circleCropTransform()
                        .error(R.mipmap.ic_default_avatar);
            } else {
                myOptions = new RequestOptions()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .override(100, 100)
                        .circleCropTransform()
                        .error(R.mipmap.ic_default_avatar);

            }

        }
        return myOptions;

    }

    //不是圆，没有限定大小
    public static void lImg(Context context, String url, ImageView imageVie) {
        Glide.with(context)
                .load(url)
                .apply(myOption)
                .into(imageVie);
    }


    //跳过缓存
    public static void lImgSkip(Context context, String url, ImageView imageVie) {
        Glide.with(context)
                .load(url)
                .apply(myOptions)
                .into(imageVie);
    }

    //大小100X100
    public static void lImg(Context context, String url, ImageView imageView, Drawable error_drawable, boolean isCircle) {

        if (!TextUtils.isEmpty(url)) {
            if (context != null) {

                Glide.with(context)
                        .load(url)
                        .apply(getoptionstwo(context, error_drawable, isCircle))
                        .into(imageView);

            }
        }
    }


    //是否是圆,不是圆，大小没有限定
    public static void lImg(Context context, String url, ImageView imageView, boolean isCircle) {

        if (!TextUtils.isEmpty(url)) {
            if (context != null) {
                RequestOptions CirmyOptions;

                if (isCircle) {//圆形图片
                    CirmyOptions = new RequestOptions()
                            .centerCrop()
                            .circleCropTransform()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .error(R.mipmap.ic_default_avatar)
                            .placeholder(R.mipmap.ic_default_avatar)
                    ;
                } else {
                    CirmyOptions = new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.mipmap.bg_error_small)
                            .error(R.mipmap.bg_error_small);

                }

                Glide.with(context)
                        .load(url)
                        .apply(CirmyOptions)
                        .into(imageView);
            }
        }
    }

    //need小测里的图片的设置,图片是全屏 设置480X800,列表是480X200(主要这两种)
    //不是圆
    //2.素材
    public static void testLimg(Context context, String url, ImageView imageView, int a, int b) {

        Glide.with(context)
                .load(url)
                .apply(getoptionstest(a, b))
                .into(imageView);
    }


    //大小100X100，不是圆
    public static void fixedLimgN(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .apply(fixedOptions)
                .into(imageView);
    }


    //可以设置错误的加载图片  不是圆  没有限制大小
    public static void lImgN(Context context, String url, ImageView imageView, Drawable errorDrawable) {

        if (!TextUtils.isEmpty(url)) {
            if (context != null) {
                RequestOptions options;
                if (null != errorDrawable) {

                    options = new RequestOptions()
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .error(errorDrawable);
                } else {

                    options = new RequestOptions()
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .error(R.mipmap.bg_error_small);

                }

                Glide.with(context)
                        .load(url)
                        .apply(options)
                        .into(imageView);

            }
        }
    }

    public static void lImg(Context context, int resourceId, ImageView imageView) {
        if (context != null) {

            Glide.with(context)
                    .load(resourceId)
                    .apply(myOptions)
                    .into(imageView);

        }
    }


    //大小100X100,不是圆
    public static void lImgNoScale(Context context, String url, ImageView imageView) {

        if (!TextUtils.isEmpty(url)) {
            if (context != null) {
                Glide.with(context)
                        .load(url)
                        .apply(myOptions)
                        .into(imageView);


            }
        }
    }

    //大小100X100, 圆
    public static void lImgCircle(Context context, String url, ImageView imageView) {

        if (!TextUtils.isEmpty(url)) {


            if (context != null) {

                Glide.with(context)
                        .load(url)
                        .apply(getoptionsto(context))
                        .into(imageView);
            }
        }
    }

    //大小100X100, 圆
    public static void lImgCircle(Context context, Integer resourceId, ImageView imageView) {

//        if (!TextUtils.isEmpty(url)) {
        if (context != null) {


            Glide.with(context)
                    .load(resourceId)
                    .apply(getoptionsto(context))
                    .into(imageView);

        }
    }


    public static File getGlideCacheFile(Context context, String url) {
        FutureTarget<File> future = Glide.with(context)
                .load(url)
                .downloadOnly(500, 500);

        try {
            return future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static void displayNative(final ImageView view, @DrawableRes int resId) {
        // 不能崩
        if (view == null) {
            Log.e(TAG, "GlideUtils -> display -> imageView is null");
            return;
        }
        Context context = view.getContext();
        // View你还活着吗？
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }
        try {
            Glide.with(context)
                    .load(resId)
                    .apply(myOptions)
                    .into(view)
                    .getSize(new SizeReadyCallback() {
                        @Override
                        public void onSizeReady(int width, int height) {
                            view.setVisibility(View.VISIBLE);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cleanData(Context context) {
        new Thread(() -> Glide.get(context).clearDiskCache()).start();
        Glide.get(context).clearMemory();
    }


    /**
     * @param context
     * @param url
     * @param imageVie 动态加载动态图
     */
    public static void lGifTo(Context context, String url, ImageView imageVie) {
        Glide.with(context)
                .load(url)
                .apply(myOptionAdapter4)
                .into(imageVie);
    }


    public static void lGifto(Context context, String url, ImageView imageVie) {
        Glide.with(context)
                .load(url)
                .apply(gifOptionTo)
                .into(imageVie);
    }


    private static RequestOptions myOptionAdapter_one = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .skipMemoryCache(true);


    private static Drawable zoomDrawable(Drawable drawable, float w, float h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap oldbmp = drawableToBitmap(drawable);
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);

        Log.e("Adakda", "scaleWidth:" + scaleWidth + "--setHeight1:" + scaleHeight);


        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true);
        return new BitmapDrawable(null, newbmp);
    }

    private static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }


}
