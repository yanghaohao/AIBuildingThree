package com.zgw.company.base.core.config;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;

public class BlurTransformation extends BitmapTransformation {

    private static final int VERSION = 1;
    private static final String ID = "jp.wasabeef.glide.transformations.BlurTransformation." + VERSION;

    private static int MAX_RADIUS = 25;
    private static int DEFAULT_DOWN_SAMPLING = 1;

    private int radius;
    private int sampling;

    public BlurTransformation() {
        this(MAX_RADIUS,DEFAULT_DOWN_SAMPLING);
    }

    public BlurTransformation(int radius) {
        this(radius,DEFAULT_DOWN_SAMPLING);
    }

    public BlurTransformation(int radius, int sampling) {
        this.radius = radius;
        this.sampling = sampling;
    }

    @NotNull
    @Override
    public Resource<Bitmap> transform(@NotNull Context context, @NotNull Resource<Bitmap> resource, int outWidth, int outHeight) {
        return super.transform(context, resource, outWidth, outHeight);
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update((ID + radius + sampling).getBytes(CHARSET));
    }

    @NotNull
    @Override
    public String key() {
        return "BlurTransformation(radius=" + radius + ", margin=" + ")";
    }

    @NotNull
    @Override
    public Bitmap transform(@NotNull Context context, @NotNull BitmapPool pool, @NotNull Bitmap toTransform, int outWidth, int outHeight) {
        int width = toTransform.getWidth();
        int height = toTransform.getHeight();
        int scaleWidth = width/sampling;
        int scaleHeight = height/sampling;

        Bitmap bitmap = pool.get(scaleWidth, scaleHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        canvas.scale(1/(float)sampling,1/(float)sampling);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(toTransform, 0, 0,paint);

        bitmap = FastBlur.blur(bitmap, radius, true);

        return bitmap;
    }

    @Override
    public String toString() {
        return "BlurTransformation{" +
                "radius=" + radius +
                ", sampling=" + sampling +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof BlurTransformation && radius == ((BlurTransformation)o).radius &&
                sampling == ((BlurTransformation)o).sampling;
    }

    @Override
    public int hashCode() {
        return ID.hashCode() + radius * 1000 + sampling * 10;
    }


}
