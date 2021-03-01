package com.tw.music.utils.imagecache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class MemoryCache implements ImageCache{
	private LruCache<String, Bitmap> mLruCache;
    private static final int MAX_LRU_CACHE_SIZE = (int) (Runtime.getRuntime().maxMemory() / 8);
 
    public MemoryCache() {
        //初始化LruCache
        initLruCache();
    }
 
    private void initLruCache() {
        mLruCache = new LruCache<String, Bitmap>(MAX_LRU_CACHE_SIZE) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight();
            }
        };
    }
 
    @Override
    public Bitmap getBitmap(String url) {
        return mLruCache.get(url);
    }
 
    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        mLruCache.put(url, bitmap);
    }

    
}
