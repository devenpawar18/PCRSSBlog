package com.pcrssblog.util.cache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * LRUCache to store Bitmaps for easy access
 */
public class ImageCache extends LruCache<String, Bitmap> {
    public ImageCache(int pMaxSize) {
        super(pMaxSize);
    }

    @Override
    protected int sizeOf(final String pKey, final Bitmap pValue) {
        return pValue.getByteCount();
    }
}