package com.tw.music.utils.imagecache;

import android.content.Context;
import android.graphics.Bitmap;

public class MemoryAndDiskCache implements ImageCache {

	private MemoryCache mMemoryCache;
 
    public MemoryAndDiskCache(Context context) {
        mMemoryCache = new MemoryCache();
    }
 
    @Override
    public Bitmap getBitmap(String url) {
        return mMemoryCache.getBitmap(url);
    }
 
    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        mMemoryCache.putBitmap(url, bitmap);
    }


}
