package com.example.jubair.wear.util;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

import java.util.HashMap;
import java.util.Map;


public class BitmapUtil {

    public static void scaleBitmaps(Map<Integer, Bitmap> bitmapMap, float scale) {
        for (Integer key: bitmapMap.keySet()) {
            Bitmap bitmap = bitmapMap.get(key);
            bitmapMap.put(key, scaleBitmap(bitmap, scale));
        }
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, float scale) {
        int width = (int) ((float) bitmap.getWidth() * scale);
        int height = (int) ((float) bitmap.getHeight() * scale);
        if (bitmap.getWidth() != width
                || bitmap.getHeight() != height) {
            return Bitmap.createScaledBitmap(bitmap,
                    width, height, true /* filter */);
        } else {
            return bitmap;
        }
    }

    public static int[] getIntArray(int resId, Resources mResources) {
        TypedArray array = mResources.obtainTypedArray(resId);
        int[] rc = new int[array.length()];
        TypedValue value = new TypedValue();
        for (int i = 0; i < array.length(); i++) {
            array.getValue(i, value);
            rc[i] = value.resourceId;
        }
        return rc;
    }


    public static Map<Integer, Bitmap> loadBitmaps(int arrayId, Resources resources) {
        Map<Integer, Bitmap> bitmapsMaps = new HashMap<>();
        int[] bitmapIds = getIntArray(arrayId, resources);
        for (int i = 0; i < bitmapIds.length; i++) {
            Drawable backgroundDrawable = resources.getDrawable(bitmapIds[i]);
            Bitmap bitmap = ((BitmapDrawable) backgroundDrawable).getBitmap();
            bitmapsMaps.put(bitmapIds[i], bitmap);
        }
        return bitmapsMaps;
    }

}
